package br.com.zupacademy.henriquecesar.propostas.client.sistema_cartoes.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import br.com.zupacademy.henriquecesar.propostas.modelo.CarteiraDigitalServico;

public class NovaInclusaoCarteiraRequest {

	@NotBlank
	private String email;
	
	@NotNull
	private CarteiraDigitalServico carteira;
	
	@Deprecated
	public NovaInclusaoCarteiraRequest() {
	}

	public NovaInclusaoCarteiraRequest(@NotBlank String email, @NotNull CarteiraDigitalServico carteira) {
		this.email = email;
		this.carteira = carteira;
	}

	public String getEmail() {
		return email;
	}

	public CarteiraDigitalServico getCarteira() {
		return carteira;
	}

}
