package br.com.zupacademy.henriquecesar.propostas.exception.business;

public class CartaoNaoEncontradoException extends RuntimeException implements BusinessException {

	private static final long serialVersionUID = 1L;

	private String erro = "Cartão não encontrado.";
	private String codigoInterno = "004";

	@Override
	public String getErro() {
		return erro;
	}

	@Override
	public String getCodigoInterno() {
		return codigoInterno;
	}

}
