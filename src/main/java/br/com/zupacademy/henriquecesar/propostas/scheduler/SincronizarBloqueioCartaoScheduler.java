package br.com.zupacademy.henriquecesar.propostas.scheduler;

import java.util.List;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import br.com.zupacademy.henriquecesar.propostas.client.sistema_cartoes.SistemaCartoesClient;
import br.com.zupacademy.henriquecesar.propostas.client.sistema_cartoes.dto.NovoBloqueioCartaoRequest;
import br.com.zupacademy.henriquecesar.propostas.client.sistema_cartoes.dto.NovoBloqueioCartaoResponse;
import br.com.zupacademy.henriquecesar.propostas.modelo.BloqueioCartao;
import br.com.zupacademy.henriquecesar.propostas.modelo.Cartao;
import feign.FeignException;

@Component
public class SincronizarBloqueioCartaoScheduler {
	
	private EntityManager entityManager;
	private SistemaCartoesClient sistemaCartoesClient;
	private final boolean NAO_OFUSCADO = false;
	private final Logger logger = LoggerFactory.getLogger(SincronizarBloqueioCartaoScheduler.class);

	public SincronizarBloqueioCartaoScheduler(EntityManager entityManager, SistemaCartoesClient sistemaCartoesClient) {
		this.entityManager = entityManager;
		this.sistemaCartoesClient = sistemaCartoesClient;
	}
	
	public List<BloqueioCartao> getCartoesBloqueadosNaoAtualizados() {
		return entityManager
			.createQuery("SELECT b FROM BloqueioCartao b WHERE b.sincronizado = false AND ativo = true", BloqueioCartao.class)
			.getResultList();
	}
	
	@Scheduled(fixedDelayString = "${sincronizar-bloqueio-cartao.frequencia.ms.execucao}")
	public void bloquearCartoes() {
		List<BloqueioCartao> cartoesDesatualizados = getCartoesBloqueadosNaoAtualizados();
		
		for (BloqueioCartao bloqueio : cartoesDesatualizados) {
			
			Cartao cartao = bloqueio.getCartao();
			
			try {
				NovoBloqueioCartaoResponse response = sistemaCartoesClient
					.notificarBloqueio(cartao.getNumeroCartao(NAO_OFUSCADO), new NovoBloqueioCartaoRequest());
				
				if (response.isBloqueado()) {
					bloqueio.setSincronizado(true);
					entityManager.merge(bloqueio);
				} else {
					logger.error("Falha ao bloquear cartão {} no sistema de cartões.", cartao.getNumeroCartao(true));
				}

			} catch (FeignException e) {
				logger.error("Falha ao sincronizar bloqueio do cartão {} com o sistema de cartões.", cartao.getNumeroCartao(true));
			
			}
		}
	}

}
