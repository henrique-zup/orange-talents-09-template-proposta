package br.com.zupacademy.henriquecesar.propostas.exception.business;

public class CartaoBloqueadoException extends RuntimeException implements BusinessException  {
    
    private static final long serialVersionUID = 1L;
    
    private String erro = "O cartão está bloqueado.";
    private String codigoInterno = "005";

    @Override
    public String getErro() {
        return erro;
    }

    @Override
    public String getCodigoInterno() {
        return codigoInterno;
    }

}
