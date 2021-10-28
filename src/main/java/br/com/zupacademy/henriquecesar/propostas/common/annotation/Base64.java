package br.com.zupacademy.henriquecesar.propostas.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import br.com.zupacademy.henriquecesar.propostas.common.annotation.validator.Base64Validator;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = Base64Validator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
public @interface Base64 {
	
	String message() default "Precisa ser uma string Base64 válida.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
