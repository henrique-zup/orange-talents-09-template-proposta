package br.com.zupacademy.henriquecesar.propostas.scheduler;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import br.com.zupacademy.henriquecesar.propostas.client.sistema_cartoes.SistemaCartoesClient;
import br.com.zupacademy.henriquecesar.propostas.client.sistema_cartoes.dto.NovoCartaoRequest;
import br.com.zupacademy.henriquecesar.propostas.client.sistema_cartoes.dto.NovoCartaoResponse;
import br.com.zupacademy.henriquecesar.propostas.modelo.Cartao;
import br.com.zupacademy.henriquecesar.propostas.modelo.Exibicao;
import br.com.zupacademy.henriquecesar.propostas.modelo.Proposta;
import br.com.zupacademy.henriquecesar.propostas.modelo.PropostaStatus;
import br.com.zupacademy.henriquecesar.propostas.repository.PropostaRepository;
import feign.FeignException;
import feign.FeignException.FeignClientException;

@Component
public class AssociarCartaoScheduler {

	private PropostaRepository propostaRepository;
	private SistemaCartoesClient sistemaCartoesClient;

	public AssociarCartaoScheduler(PropostaRepository propostaRepository, SistemaCartoesClient sistemaCartoesClient) {
		this.propostaRepository = propostaRepository;
		this.sistemaCartoesClient = sistemaCartoesClient;
	}

	@Value("${associar-cartao.proposta.limite.registros}")
	private Integer LIMITE_REGISTROS;
	private final Logger logger = LoggerFactory.getLogger(AssociarCartaoScheduler.class);

	private List<Proposta> recuperarNovasPropostasAprovadas() {
		return propostaRepository.findByCartaoIsNullAndStatusEquals(PropostaStatus.ELEGIVEL, 
				PageRequest.of(0, LIMITE_REGISTROS));
	}

	@Scheduled(fixedDelayString = "${associar-cartao.frequencia.ms.execucao}")
	private void associarCartoes() {
		List<Proposta> novasPropostasAprovadas = recuperarNovasPropostasAprovadas();

		for (Proposta proposta : novasPropostasAprovadas) {
			try {
				NovoCartaoResponse response = sistemaCartoesClient.gerarCartao(new NovoCartaoRequest(proposta));
				Cartao novoCartao = Cartao.buildCartao(response);
				proposta.adicionaCartao(novoCartao, propostaRepository);
				logger.info("A proposta {} foi associada ao cartão {} com sucesso.", proposta.getId(),
						novoCartao.getNumeroCartao(Exibicao.OFUSCADO));
				
			} catch (FeignClientException ex) {
				logger.warn("Solicitação para a proposta {} ainda não foi processada.", proposta.getId());
				
			} catch (FeignException ex) {
				logger.error("Não foi possível se conectar com o sistema de cartões.");
				
			}
		}
	}

}
