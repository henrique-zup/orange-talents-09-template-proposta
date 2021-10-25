package br.com.zupacademy.henriquecesar.propostas.client.analise_financeira.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import br.com.zupacademy.henriquecesar.propostas.modelo.PropostaStatus;

public class NovaAnaliseFinanceiraResponse {

	@NotBlank
	private String documento;

	@NotBlank
	private String nome;

	@NotBlank
	private String idProposta;

	@NotNull
	private NovaAnaliseFinanceiraResponseStatus resultadoSolicitacao;

	public String getDocumento() {
		return documento;
	}

	public String getNome() {
		return nome;
	}

	public String getIdProposta() {
		return idProposta;
	}

	public NovaAnaliseFinanceiraResponseStatus getResultadoSolicitacao() {
		return resultadoSolicitacao;
	}

	public PropostaStatus getPropostaStatus() {
		return resultadoSolicitacao.toPropostaStatus();
	}

}
