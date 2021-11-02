package br.com.zupacademy.henriquecesar.propostas.client.sistema_cartoes.dto;

public class NovoBloqueioCartaoResponse {
	
	private BloqueioCartaoResponseStatus resultado;

	public BloqueioCartaoResponseStatus getResultado() {
		return resultado;
	}

	public boolean isBloqueado() {
		return resultado.equals(BloqueioCartaoResponseStatus.BLOQUEADO);
	}

}
