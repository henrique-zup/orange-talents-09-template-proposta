package br.com.zupacademy.henriquecesar.propostas.client.sistema_cartoes.dto;

import org.hibernate.validator.constraints.Range;

public class NovoCartaoResponseDiaVencimento {

	@Range(min = 1, max = 31)
	private Integer dia;

	public Integer getDia() {
		return dia;
	}

}
