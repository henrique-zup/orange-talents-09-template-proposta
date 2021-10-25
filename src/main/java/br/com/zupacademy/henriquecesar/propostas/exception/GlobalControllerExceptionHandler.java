package br.com.zupacademy.henriquecesar.propostas.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.zupacademy.henriquecesar.propostas.exception.business.BusinessException;
import br.com.zupacademy.henriquecesar.propostas.exception.business.PropostaJaExisteException;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {
    
    @ExceptionHandler(value = { MethodArgumentNotValidException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse argumentoInvalidoException(MethodArgumentNotValidException ex) {
        return ApiErrorResponse.buildFromMethodArgumentNotValidException(ex);
    }
    
    @ExceptionHandler(value = { PropostaJaExisteException.class })
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ApiErrorResponse requestUnprocessableException(BusinessException ex) {
        return ApiErrorResponse.buildFromBusinessException(ex);
    }

}
