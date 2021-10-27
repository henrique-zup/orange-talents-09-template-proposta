package br.com.zupacademy.henriquecesar.propostas.dto.response;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import br.com.zupacademy.henriquecesar.propostas.modelo.Proposta;
import br.com.zupacademy.henriquecesar.propostas.modelo.PropostaStatus;

public class PropostaStatusResponse {
	
	@Enumerated(EnumType.STRING)
	private PropostaStatus status;

	public PropostaStatus getStatus() {
		return status;
	}
	
	public PropostaStatusResponse(Proposta proposta) {
		status = proposta.getStatus();
	}

	@Deprecated
	public PropostaStatusResponse() {
	}
}
