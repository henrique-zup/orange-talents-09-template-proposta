package br.com.zupacademy.henriquecesar.propostas.exception.business;

public class CarteiraJaAssociadaException extends RuntimeException implements BusinessException {

	private static final long serialVersionUID = 1L;

	private String erro = "Existe uma carteira deste serviço já associada.";
	private String codigoInterno = "006";

	@Override
	public String getErro() {
		return erro;
	}

	@Override
	public String getCodigoInterno() {
		return codigoInterno;
	}

}
