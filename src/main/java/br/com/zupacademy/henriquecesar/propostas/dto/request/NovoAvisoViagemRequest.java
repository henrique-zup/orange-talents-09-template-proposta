package br.com.zupacademy.henriquecesar.propostas.dto.request;

import java.time.LocalDateTime;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import br.com.zupacademy.henriquecesar.propostas.modelo.AvisoViagem;
import br.com.zupacademy.henriquecesar.propostas.modelo.Cartao;

public class NovoAvisoViagemRequest {

	@NotBlank
	private String destino;
	
	@NotNull
	@Future
	private LocalDateTime dataTerminoViagem;
	
	@Deprecated
	public NovoAvisoViagemRequest() {
	}

	public NovoAvisoViagemRequest(@NotBlank String destino, @Future LocalDateTime dataTerminoViagem) {
		this.destino = destino;
		this.dataTerminoViagem = dataTerminoViagem;
	}

	public AvisoViagem toModel(@NotNull Cartao cartao, @NotBlank String userAgent, @NotBlank String enderecoIp) {
		return new AvisoViagem(destino, dataTerminoViagem, cartao, userAgent, enderecoIp);
	}

	public String getDestino() {
		return destino;
	}

	public LocalDateTime getDataTerminoViagem() {
		return dataTerminoViagem;
	}
}
