package br.com.zupacademy.henriquecesar.propostas.exception.business;

public class PropostaJaExisteException extends RuntimeException implements BusinessException  {
    
    private static final long serialVersionUID = 1L;
    
    private String erro = "Já existe uma proposta para o documento informado.";
    private String codigoInterno = "002";

    @Override
    public String getErro() {
        return erro;
    }

    @Override
    public String getCodigoInterno() {
        return codigoInterno;
    }

}
