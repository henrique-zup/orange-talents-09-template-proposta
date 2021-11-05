package br.com.zupacademy.henriquecesar.propostas.modelo;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
public class CarteiraDigital {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	private CarteiraDigitalServico servico;
	
	@ManyToOne
	private Cartao cartao;

	private boolean sincronizada;

	@Deprecated
	public CarteiraDigital() {
	}

	public CarteiraDigital(@NotNull CarteiraDigitalServico servico, Cartao cartao) {
		this.servico = servico;
		this.cartao = cartao;
	}

	public Long getId() {
		return id;
	}

	public boolean isSincronizada() {
		return sincronizada;
	}

	public void setSincronizada(boolean sincronizada) {
		this.sincronizada = sincronizada;
	}

	public boolean isServicoEquals(CarteiraDigitalServico servico) {
		return this.servico.equals(servico);
	}

	public CarteiraDigitalServico getServico() {
		return servico;
	}
	
}
