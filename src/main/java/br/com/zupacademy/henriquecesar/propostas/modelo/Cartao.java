package br.com.zupacademy.henriquecesar.propostas.modelo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import br.com.zupacademy.henriquecesar.propostas.client.sistema_cartoes.dto.NovoCartaoResponse;

@Entity
public class Cartao {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(length = 16)
	private UUID id;
	
	@NotBlank
	private String titular;
	
	@NotBlank
	private String numeroCartao;
	
	@NotNull
	private LocalDateTime dataEmissao;
	
	@NotNull
	private BigDecimal limite;
	
	@NotNull 
	@Range(min = 1, max=31)
	private Integer diaVencimento;

	@Deprecated
	public Cartao() {
	}

	private Cartao(@NotBlank String titular, @NotBlank String numeroCartao, @NotNull LocalDateTime dataEmissao,
			@NotNull BigDecimal limite, @NotNull @Range(min = 1, max = 31) Integer diaVencimento) {
		this.titular = titular;
		this.numeroCartao = numeroCartao;
		this.dataEmissao = dataEmissao;
		this.limite = limite;
		this.diaVencimento = diaVencimento;
	}

	public static Cartao buildCartao(NovoCartaoResponse response) {
		return new Cartao(response.getTitular(),
				response.getId(), 
				response.getEmitidoEm(), 
				response.getLimite(),
				response.getVencimento().getDia());
	}
	
	public String getNumeroCartao(boolean ofuscado) {
		if (ofuscado) {
			return String.format("%1$sXX-XXXX-XXXX-%2$s", 
					numeroCartao.substring(0, 2), numeroCartao.substring(15));
		}
		return numeroCartao;
	}
}