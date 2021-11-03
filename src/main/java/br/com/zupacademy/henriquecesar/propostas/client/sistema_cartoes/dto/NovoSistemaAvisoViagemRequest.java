package br.com.zupacademy.henriquecesar.propostas.client.sistema_cartoes.dto;

import java.time.LocalDateTime;

import br.com.zupacademy.henriquecesar.propostas.modelo.AvisoViagem;

public class NovoSistemaAvisoViagemRequest {

	private String destino;
	
	private LocalDateTime validoAte;
	
	@Deprecated
	public NovoSistemaAvisoViagemRequest() {
	}

	public NovoSistemaAvisoViagemRequest(AvisoViagem avisoViagem) {
		this.destino = avisoViagem.getDestino();
		this.validoAte = avisoViagem.getDataTerminoViagem();
	}

	public String getDestino() {
		return destino;
	}

	public LocalDateTime getValidoAte() {
		return validoAte;
	}
}
