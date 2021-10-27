package br.com.zupacademy.henriquecesar.propostas.client.analise_financeira;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import br.com.zupacademy.henriquecesar.propostas.client.analise_financeira.dto.NovaAnaliseFinanceiraRequest;
import br.com.zupacademy.henriquecesar.propostas.client.analise_financeira.dto.NovaAnaliseFinanceiraResponse;

@FeignClient(name="analise-financeira", url="${url.service.analise-financeira}")
public interface AnaliseFinanceiraClient {
	
	@PostMapping("/solicitacao")
	NovaAnaliseFinanceiraResponse realizarAnaliseFinanceira(@RequestBody NovaAnaliseFinanceiraRequest request);

}
