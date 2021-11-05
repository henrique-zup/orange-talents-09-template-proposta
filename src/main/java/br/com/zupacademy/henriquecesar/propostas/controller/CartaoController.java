package br.com.zupacademy.henriquecesar.propostas.controller;

import java.net.URI;
import java.util.Map;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.zupacademy.henriquecesar.propostas.client.sistema_cartoes.SistemaCartoesClient;
import br.com.zupacademy.henriquecesar.propostas.client.sistema_cartoes.dto.NovoBloqueioCartaoRequest;
import br.com.zupacademy.henriquecesar.propostas.client.sistema_cartoes.dto.NovoBloqueioCartaoResponse;
import br.com.zupacademy.henriquecesar.propostas.client.sistema_cartoes.dto.NovoSistemaAvisoViagemRequest;
import br.com.zupacademy.henriquecesar.propostas.client.sistema_cartoes.dto.NovoSistemaAvisoViagemResponse;
import br.com.zupacademy.henriquecesar.propostas.dto.request.NovaBiometriaRequest;
import br.com.zupacademy.henriquecesar.propostas.dto.request.NovoAvisoViagemRequest;
import br.com.zupacademy.henriquecesar.propostas.exception.business.CartaoBloqueadoException;
import br.com.zupacademy.henriquecesar.propostas.exception.business.CartaoNaoEncontradoException;
import br.com.zupacademy.henriquecesar.propostas.modelo.AvisoViagem;
import br.com.zupacademy.henriquecesar.propostas.modelo.Biometria;
import br.com.zupacademy.henriquecesar.propostas.modelo.BloqueioCartao;
import br.com.zupacademy.henriquecesar.propostas.modelo.Cartao;
import br.com.zupacademy.henriquecesar.propostas.modelo.Exibicao;
import br.com.zupacademy.henriquecesar.propostas.repository.CartaoRepository;
import feign.FeignException;


@RestController
@RequestMapping("/cartoes")
public class CartaoController {

	private CartaoRepository cartaoRepository;
	private EntityManager entityManager;
	private SistemaCartoesClient cartoesClient;
	
	private final Logger logger = LoggerFactory.getLogger(CartaoController.class);

	public CartaoController(CartaoRepository cartaoRepository, EntityManager entityManager, SistemaCartoesClient cartoesClient) {
		this.cartaoRepository = cartaoRepository;
		this.entityManager = entityManager;
		this.cartoesClient = cartoesClient;
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
	
	@Transactional
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
		
		BloqueioCartao bloqueio = cartao.bloquear(novoBloqueio, cartaoRepository);
		
		try {
			NovoBloqueioCartaoResponse response = cartoesClient
				.notificarBloqueio(cartao.getNumeroCartao(Exibicao.NAO_OFUSCADO), new NovoBloqueioCartaoRequest());
			
			if (response.isBloqueado()) {
				bloqueio.setSincronizado(true);
				entityManager.merge(bloqueio);
				return;
			}
			
			logger.error("Falha ao bloquear cartão {} no sistema de cartões.", cartao.getNumeroCartao(Exibicao.OFUSCADO));
			
		} catch (FeignException ex) {
			logger.error("Falha ao sincronizar bloqueio do cartão {} com o sistema de cartões.", cartao.getNumeroCartao(Exibicao.OFUSCADO));
		
		}
		
	}
	
	@Transactional
	@PostMapping("/{idCartao}/avisoViagem")
	public void avisarViagem(@PathVariable UUID idCartao, @Valid @RequestBody NovoAvisoViagemRequest request, HttpServletRequest httpRequest) {
		Cartao cartao = cartaoRepository.findById(idCartao)
				.orElseThrow(CartaoNaoEncontradoException::new);
		
		if (cartao.isBloqueado(entityManager)) {
			throw new CartaoBloqueadoException();
		}
		
		String userAgent = httpRequest.getHeader("User-Agent");
		String enderecoIp = httpRequest.getRemoteAddr();
		
		AvisoViagem avisoViagem = request.toModel(cartao, userAgent, enderecoIp);
		
		avisoViagem = cartao.adicionarAvisoViagem(avisoViagem, cartaoRepository);
		
		try {
			NovoSistemaAvisoViagemResponse response = cartoesClient
				.notificarAvisoViagem(cartao.getNumeroCartao(Exibicao.NAO_OFUSCADO), new NovoSistemaAvisoViagemRequest(avisoViagem));
			
			if (response.isNotificado()) {
				avisoViagem.setSincronizado(true);
				entityManager.merge(avisoViagem);
				return;
			}
			
			logger.error("Falha ao notificar aviso de viagem do cartão {} sistema de cartões.", cartao.getNumeroCartao(Exibicao.OFUSCADO));
			
		} catch (FeignException ex) {
			logger.error("Falha ao notificar aviso de viagem do cartão {} sistema de cartões.", cartao.getNumeroCartao(Exibicao.OFUSCADO));
		
		}
	}

}
