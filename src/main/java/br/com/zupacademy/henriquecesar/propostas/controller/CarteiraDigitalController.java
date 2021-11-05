package br.com.zupacademy.henriquecesar.propostas.controller;

import java.net.URI;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.zupacademy.henriquecesar.propostas.client.sistema_cartoes.SistemaCartoesClient;
import br.com.zupacademy.henriquecesar.propostas.client.sistema_cartoes.dto.NovaInclusaoCarteiraRequest;
import br.com.zupacademy.henriquecesar.propostas.client.sistema_cartoes.dto.NovaInclusaoCarteiraResponse;
import br.com.zupacademy.henriquecesar.propostas.exception.business.CartaoBloqueadoException;
import br.com.zupacademy.henriquecesar.propostas.exception.business.CartaoNaoEncontradoException;
import br.com.zupacademy.henriquecesar.propostas.exception.business.CarteiraNaoAssociadaException;
import br.com.zupacademy.henriquecesar.propostas.modelo.Cartao;
import br.com.zupacademy.henriquecesar.propostas.modelo.CarteiraDigital;
import br.com.zupacademy.henriquecesar.propostas.modelo.CarteiraDigitalServico;
import br.com.zupacademy.henriquecesar.propostas.modelo.Exibicao;
import br.com.zupacademy.henriquecesar.propostas.repository.CartaoRepository;
import br.com.zupacademy.henriquecesar.propostas.repository.PropostaRepository;
import feign.FeignException;

@RestController
@RequestMapping("/carteiras")
public class CarteiraDigitalController {
	
	private CartaoRepository cartaoRepository;
	private PropostaRepository propostaRepository;
	private EntityManager entityManager;
	private SistemaCartoesClient client;
	
	private final Logger logger = LoggerFactory.getLogger(CarteiraDigitalController.class);
	
	public CarteiraDigitalController(CartaoRepository cartaoRepository, EntityManager entityManager, SistemaCartoesClient client, PropostaRepository propostaRepository) {
		this.cartaoRepository = cartaoRepository;
		this.propostaRepository = propostaRepository;
		this.entityManager = entityManager;
		this.client = client;
	}

	@Transactional
	@PostMapping("/{idCartao}/paypal")
	public ResponseEntity<?> associarPaypal(@PathVariable UUID idCartao, UriComponentsBuilder uriBuilder) {
		Cartao cartao = cartaoRepository.findById(idCartao)
				.orElseThrow(CartaoNaoEncontradoException::new);
		
		if (cartao.isBloqueado(entityManager)) {
			throw new CartaoBloqueadoException();
		}
		
		String email = propostaRepository.findByCartao(cartao).get().getEmail();
		String numeroCartao = cartao.getNumeroCartao(Exibicao.NAO_OFUSCADO);
		
		try {
			NovaInclusaoCarteiraRequest clientRequest = new NovaInclusaoCarteiraRequest(
					email, CarteiraDigitalServico.PAYPAL);
			
			NovaInclusaoCarteiraResponse response = client
				.associarCarteira(numeroCartao, clientRequest);
			
			if (response.isAssociada()) {
				CarteiraDigital carteira = cartao.associarCarteira(clientRequest.getCarteira(), cartaoRepository);
				
				URI location = uriBuilder.replacePath("/propostas/{id}")
						.buildAndExpand(carteira.getId()).toUri();
				
				return ResponseEntity.created(location).build();
				
			} else {
				logger.error("Falha ao sincronizar carteira digital com o sistema legado.");
				throw new CarteiraNaoAssociadaException();
			}
			
		} catch (FeignException e) {
			logger.error("Falha ao sincronizar carteira digital com o sistema legado.");
			throw new CarteiraNaoAssociadaException();
		}

	}

}
