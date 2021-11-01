package br.com.zupacademy.henriquecesar.propostas.modelo;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;

@Entity
public class BloqueioCartao {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank
	private String userAgent;
	
	@NotBlank
	private String enderecoIp;
	
	@NotNull
	@ManyToOne(optional = false)
	private Cartao cartao;
	
	@CreationTimestamp
	private LocalDateTime dataCadastro;
	
	@NotNull
	private boolean ativo;
	
	@NotNull
	private boolean sincronizado;

	@Deprecated
	public BloqueioCartao() {
	}

	public BloqueioCartao(@NotBlank String userAgent, @NotBlank String enderecoIp, @NotNull Cartao cartao) {
		this.userAgent = userAgent;
		this.enderecoIp = enderecoIp;
		this.cartao = cartao;
		this.ativo = true;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public Cartao getCartao() {
		return cartao;
	}

	public boolean isSincronizado() {
		return sincronizado;
	}

	public void setSincronizado(boolean sincronizado) {
		this.sincronizado = sincronizado;
	}
}

