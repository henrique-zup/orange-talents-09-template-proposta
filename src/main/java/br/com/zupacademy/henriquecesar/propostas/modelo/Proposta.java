package br.com.zupacademy.henriquecesar.propostas.modelo;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import br.com.zupacademy.henriquecesar.propostas.common.annotation.CpfOrCnpj;
import br.com.zupacademy.henriquecesar.propostas.dto.request.NovaPropostaRequest;

@Entity
public class Proposta {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@CpfOrCnpj
	private String documento;

	@Email
	@NotBlank
	private String email;

	@NotBlank
	private String endereco;

	@NotNull
	private BigDecimal salario;

	@Deprecated
	public Proposta() {
	}

	public Proposta(String documento, @Email @NotBlank String email, @NotBlank String endereco,
	        @NotNull BigDecimal salario) {
		this.documento = documento;
		this.email = email;
		this.endereco = endereco;
		this.salario = salario;
	}

	public static Proposta buildProposta(@Valid NovaPropostaRequest request) {
		return new Proposta(
			request.getDocumento(),
			request.getEmail(),
			request.getEndereco(),
			request.getSalario()
		);
	}

	public Long getId() {
		return id;
	}

}
