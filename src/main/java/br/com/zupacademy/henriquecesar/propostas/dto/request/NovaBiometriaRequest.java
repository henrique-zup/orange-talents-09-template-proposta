package br.com.zupacademy.henriquecesar.propostas.dto.request;

import javax.validation.constraints.NotBlank;

import br.com.zupacademy.henriquecesar.propostas.common.annotation.Base64;

public class NovaBiometriaRequest {
	
	@NotBlank
	@Base64
	private String fingerPrint;

	public String getFingerPrint() {
		return fingerPrint;
	}
}
