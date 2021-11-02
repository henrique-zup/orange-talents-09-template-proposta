package br.com.zupacademy.henriquecesar.propostas.client.sistema_cartoes.dto;

import org.springframework.beans.factory.annotation.Value;

public class NovoBloqueioCartaoRequest {

	@Value("{app.name}")
	private String sistemaResponsavel;

	public String getSistemaResponsavel() {
		return sistemaResponsavel;
	}
}
