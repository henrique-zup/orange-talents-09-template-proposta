package br.com.zupacademy.henriquecesar.propostas.dto.request;

import java.math.BigDecimal;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import br.com.zupacademy.henriquecesar.propostas.common.annotation.CpfOrCnpj;

public class NovaPropostaRequest {

	@CpfOrCnpj
	private String documento;

	@Email
	@NotBlank
	private String email;

	@NotBlank
	private String endereco;

	@NotNull
	@PositiveOrZero
	private BigDecimal salario;

	public String getDocumento() {
		return documento;
	}

	public String getEmail() {
		return email;
	}

	public String getEndereco() {
		return endereco;
	}

	public BigDecimal getSalario() {
		return salario;
	}

	public NovaPropostaRequest(String documento, @Email @NotBlank String email, @NotBlank String endereco,
	        @NotNull BigDecimal salario) {
		this.documento = documento;
		this.email = email;
		this.endereco = endereco;
		this.salario = salario;
	}
	

}
