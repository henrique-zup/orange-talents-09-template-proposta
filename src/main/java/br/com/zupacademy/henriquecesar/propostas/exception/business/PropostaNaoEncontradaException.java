package br.com.zupacademy.henriquecesar.propostas.exception.business;

public class PropostaNaoEncontradaException extends RuntimeException implements BusinessException {

	private static final long serialVersionUID = 1L;

	private String erro = "Proposta não encontrada.";
	private String codigoInterno = "003";

	@Override
	public String getErro() {
		return erro;
	}

	@Override
	public String getCodigoInterno() {
		return codigoInterno;
	}

}
