package br.com.zupacademy.henriquecesar.propostas.controller;

import java.net.URI;
import java.util.Map;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.zupacademy.henriquecesar.propostas.dto.request.NovaBiometriaRequest;
import br.com.zupacademy.henriquecesar.propostas.exception.business.CartaoBloqueadoException;
import br.com.zupacademy.henriquecesar.propostas.exception.business.CartaoNaoEncontradoException;
import br.com.zupacademy.henriquecesar.propostas.modelo.Biometria;
import br.com.zupacademy.henriquecesar.propostas.modelo.BloqueioCartao;
import br.com.zupacademy.henriquecesar.propostas.modelo.Cartao;
import br.com.zupacademy.henriquecesar.propostas.repository.CartaoRepository;


@RestController
@RequestMapping("/cartoes")
public class CartaoController {

	private CartaoRepository cartaoRepository;
	private EntityManager entityManager;

	public CartaoController(CartaoRepository cartaoRepository, EntityManager entityManager) {
		this.cartaoRepository = cartaoRepository;
		this.entityManager = entityManager;
	}

	@PostMapping("/{idCartao}/adicionarBiometria")
	public ResponseEntity<?> adicionarBiometria(@PathVariable UUID idCartao, @RequestBody @Valid NovaBiometriaRequest request,
			UriComponentsBuilder uriBuilder) {
		Cartao cartao = cartaoRepository.findById(idCartao)
			.orElseThrow(CartaoNaoEncontradoException::new);
		
		Biometria biometria = cartao
				.adicionarBiometria(Biometria.buildBiometria(cartao, request), cartaoRepository);
		
		URI location = uriBuilder.replacePath("/cartoes/{idCartao}/{idBiometria}")
			.buildAndExpand(
				Map.of("idCartao", cartao.getId(),
						"idBiometria", biometria.getId()))
			.toUri();
		
		return ResponseEntity.created(location).build();
	}
	
	@PostMapping("/{idCartao}/bloquear")
	public void bloquearCartao(@PathVariable UUID idCartao, HttpServletRequest request) {
		Cartao cartao = cartaoRepository.findById(idCartao)
				.orElseThrow(CartaoNaoEncontradoException::new);
		
		if (cartao.isBloqueado(entityManager)) {
			throw new CartaoBloqueadoException();
		}
		
		String userAgent = request.getHeader("User-Agent");
		String enderecoIp = request.getRemoteAddr();
		
		BloqueioCartao novoBloqueio = new BloqueioCartao(userAgent, enderecoIp, cartao);
		
		cartao.bloquear(novoBloqueio, cartaoRepository);
	}

}
