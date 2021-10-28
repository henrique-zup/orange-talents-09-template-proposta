package br.com.zupacademy.henriquecesar.propostas.common.annotation.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import br.com.zupacademy.henriquecesar.propostas.common.annotation.Base64;

public class Base64Validator implements ConstraintValidator<Base64, String> {

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		try {
			java.util.Base64.Decoder decoder = java.util.Base64.getDecoder();
			decoder.decode(value);
			return true;
			
		} catch (Exception ex) {
			return false;
			
		}
	}

}
