package br.com.zupacademy.henriquecesar.propostas.client.analise_financeira.dto;

import br.com.zupacademy.henriquecesar.propostas.modelo.PropostaStatus;

public enum NovaAnaliseFinanceiraResponseStatus {
	
	COM_RESTRICAO, SEM_RESTRICAO;
	
	public PropostaStatus toPropostaStatus() {
		if (this.equals(COM_RESTRICAO)) {
			return PropostaStatus.NAO_ELEGIVEL;
		}
		return PropostaStatus.ELEGIVEL;
	}

}
