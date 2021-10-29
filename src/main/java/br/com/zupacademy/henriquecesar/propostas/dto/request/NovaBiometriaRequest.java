package br.com.zupacademy.henriquecesar.propostas.dto.request;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonCreator;

import br.com.zupacademy.henriquecesar.propostas.common.annotation.Base64;

public class NovaBiometriaRequest {

	@NotBlank
	@Base64
	private String fingerPrint;

	@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
	public NovaBiometriaRequest(@NotBlank @Base64 String fingerPrint) {
		this.fingerPrint = fingerPrint;
	}

	public String getFingerPrint() {
		return fingerPrint;
	}

}
