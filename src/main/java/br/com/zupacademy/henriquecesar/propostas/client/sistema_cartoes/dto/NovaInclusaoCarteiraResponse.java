package br.com.zupacademy.henriquecesar.propostas.client.sistema_cartoes.dto;

public class NovaInclusaoCarteiraResponse {
	
	private NovaInclusaoCarteiraResponseStatus resultado;
	
	public NovaInclusaoCarteiraResponseStatus getResultado() {
		return resultado;
	}
	
	public boolean isAssociada() {
		return resultado.equals(NovaInclusaoCarteiraResponseStatus.ASSOCIADA);
	}

	@Deprecated
	public NovaInclusaoCarteiraResponse() {
	}

	public NovaInclusaoCarteiraResponse(NovaInclusaoCarteiraResponseStatus resultado) {
		this.resultado = resultado;
	}

}
