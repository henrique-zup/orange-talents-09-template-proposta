package br.com.zupacademy.henriquecesar.propostas.client.sistema_cartoes.dto;

public class NovoSistemaAvisoViagemResponse {
	
	private NovoSistemaAvisoViagemRequestStatus resultado;
	
	public NovoSistemaAvisoViagemRequestStatus getResultado() {
		return resultado;
	}

	public boolean isNotificado() {
		return resultado.equals(NovoSistemaAvisoViagemRequestStatus.CRIADO);
	}

}
