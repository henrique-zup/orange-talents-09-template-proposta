package br.com.zupacademy.henriquecesar.propostas.client.analise_financeira.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import br.com.zupacademy.henriquecesar.propostas.modelo.Proposta;

public class NovaAnaliseFinanceiraRequest {
	
	@NotBlank
	private String documento;
	
	@NotBlank
	private String nome;
	
	@NotBlank
	private String idProposta;
	
	public NovaAnaliseFinanceiraRequest(@Valid Proposta proposta) {
		this.documento = proposta.getDocumento();
		this.nome = proposta.getNome();
		this.idProposta = proposta.getId().toString();
	}

	public String getDocumento() {
		return documento;
	}

	public String getNome() {
		return nome;
	}

	public String getIdProposta() {
		return idProposta;
	}
}
