package br.com.zupacademy.henriquecesar.propostas.exception.business;

public class CarteiraNaoAssociadaException extends RuntimeException implements BusinessException {

	private static final long serialVersionUID = 1L;

	private String erro = "A carteira não pôde ser associada.";
	private String codigoInterno = "007";

	@Override
	public String getErro() {
		return erro;
	}

	@Override
	public String getCodigoInterno() {
		return codigoInterno;
	}
}
