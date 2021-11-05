package br.com.zupacademy.henriquecesar.propostas.scheduler;

import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import br.com.zupacademy.henriquecesar.propostas.client.sistema_cartoes.SistemaCartoesClient;
import br.com.zupacademy.henriquecesar.propostas.client.sistema_cartoes.dto.NovoSistemaAvisoViagemRequest;
import br.com.zupacademy.henriquecesar.propostas.client.sistema_cartoes.dto.NovoSistemaAvisoViagemResponse;
import br.com.zupacademy.henriquecesar.propostas.modelo.AvisoViagem;
import br.com.zupacademy.henriquecesar.propostas.modelo.Cartao;
import br.com.zupacademy.henriquecesar.propostas.modelo.Exibicao;
import feign.FeignException;

@Component
public class SincronizarAvisoViagemScheduler {
	
	private EntityManager entityManager;
	private SistemaCartoesClient sistemaCartoesClient;
	private final Logger logger = LoggerFactory.getLogger(SincronizarAvisoViagemScheduler.class);

	public SincronizarAvisoViagemScheduler(EntityManager entityManager, SistemaCartoesClient sistemaCartoesClient) {
		this.entityManager = entityManager;
		this.sistemaCartoesClient = sistemaCartoesClient;
	}
	
	public List<AvisoViagem> getAvisosNaoSincronizados() {
		return entityManager
			.createQuery("SELECT a FROM AvisoViagem a WHERE a.sincronizado = false AND a.dataTerminoViagem >= CURRENT_DATE()", AvisoViagem.class)
			.getResultList();
	}
	
	@Transactional
	@Scheduled(fixedDelayString = "${sincronizar-aviso-viagem.frequencia.ms.execucao}")
	protected void notificarAvisoViagem() {
		List<AvisoViagem> avisosNaoSincronizados = getAvisosNaoSincronizados();
		
		for (AvisoViagem aviso : avisosNaoSincronizados) {
			
			Cartao cartao = aviso.getCartao();
			
			try {
				NovoSistemaAvisoViagemResponse response = sistemaCartoesClient
					.notificarAvisoViagem(cartao.getNumeroCartao(Exibicao.NAO_OFUSCADO), new NovoSistemaAvisoViagemRequest(aviso));
				
				if (response.isNotificado()) {
					aviso.setSincronizado(true);
					entityManager.merge(aviso);
				} else {
					logger.error("Falha ao notificar aviso de viagem do cartão {} sistema de cartões.", cartao.getNumeroCartao(Exibicao.OFUSCADO));
				}

			} catch (FeignException e) {
				logger.error("Falha ao notificar aviso de viagem do cartão {} sistema de cartões.", cartao.getNumeroCartao(Exibicao.OFUSCADO));
			
			}
		}
	}

}
