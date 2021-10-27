package br.com.zupacademy.henriquecesar.propostas.client.sistema_cartoes.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;

public class NovoCartaoResponse {

	@NotBlank
	private String id;
	
	@NotNull
	@PastOrPresent
	private LocalDateTime emitidoEm;
	
	@NotBlank
	private String titular;
	
	@Valid
	private NovoCartaoResponseDiaVencimento vencimento;
	
	@Positive
	private BigDecimal limite;
	
	@NotBlank
	private String idProposta;

	public String getId() {
		return id;
	}

	public LocalDateTime getEmitidoEm() {
		return emitidoEm;
	}

	public String getTitular() {
		return titular;
	}

	public NovoCartaoResponseDiaVencimento getVencimento() {
		return vencimento;
	}

	public BigDecimal getLimite() {
		return limite;
	}

	public String getIdProposta() {
		return idProposta;
	}

}
