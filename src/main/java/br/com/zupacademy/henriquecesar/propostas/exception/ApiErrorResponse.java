package br.com.zupacademy.henriquecesar.propostas.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.MethodArgumentNotValidException;

import br.com.zupacademy.henriquecesar.propostas.exception.business.BusinessException;

public class ApiErrorResponse {
    
    private String erro;
    private String codigoInterno;
    private Map<String, String> campos = new HashMap<String, String>();
    
    private final static String ARGUMENT_NOT_VALID_MSG_ERROR = "Argumento(s) inválido(s).";
    
    private ApiErrorResponse(String erro, String codigoInterno) {
        this.erro = erro;
        this.codigoInterno = codigoInterno;
    }
    
    private void adicionaCampo(String campo, String erro) {
        this.campos.put(campo, erro);
    }

    public String getErro() {
        return erro;
    }

    public String getCodigoInterno() {
        return codigoInterno;
    }

    public Map<String, String> getCampos() {
        return campos;
    }
    
    public static ApiErrorResponse buildFromMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        ApiErrorResponse response = new ApiErrorResponse(ARGUMENT_NOT_VALID_MSG_ERROR, "001");
        ex.getBindingResult().getFieldErrors().forEach(fe -> {
            response.adicionaCampo(fe.getField(), fe.getDefaultMessage());
        });
        return response;
    }

    public static ApiErrorResponse buildFromBusinessException(BusinessException ex) {
        return new ApiErrorResponse(ex.getErro(), ex.getCodigoInterno());
    }
}
