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
	private LocalDateTime dataViagem;
	
	@Deprecated
	public NovoAvisoViagemRequest() {
	}

	public NovoAvisoViagemRequest(@NotBlank String destino, @Future LocalDateTime dataViagem) {
		this.destino = destino;
		this.dataViagem = dataViagem;
	}

	public AvisoViagem toModel(@NotNull Cartao cartao, @NotBlank String userAgent, @NotBlank String enderecoIp) {
		return new AvisoViagem(destino, dataViagem, cartao, userAgent, enderecoIp);
	}

	public String getDestino() {
		return destino;
	}

	public LocalDateTime getDataViagem() {
		return dataViagem;
	}
}
