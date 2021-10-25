package br.com.zupacademy.henriquecesar.propostas.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import br.com.zupacademy.henriquecesar.propostas.common.annotation.validator.CpfOrCnpjValidator;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CpfOrCnpjValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
public @interface CpfOrCnpj {

	String message() default "CPF ou CNPJ inválido.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
