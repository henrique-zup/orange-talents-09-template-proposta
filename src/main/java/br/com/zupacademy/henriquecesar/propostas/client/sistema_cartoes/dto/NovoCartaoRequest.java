package br.com.zupacademy.henriquecesar.propostas.client.sistema_cartoes.dto;

import javax.validation.Valid;

import br.com.zupacademy.henriquecesar.propostas.modelo.Proposta;

public class NovoCartaoRequest {
	
	private String documento;
	private String nome;
	private String idProposta;

	public NovoCartaoRequest(@Valid Proposta proposta) {
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
