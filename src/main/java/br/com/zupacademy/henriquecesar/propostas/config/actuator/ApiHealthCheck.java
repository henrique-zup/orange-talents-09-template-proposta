package br.com.zupacademy.henriquecesar.propostas.config.actuator;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;

@Component
public class ApiHealthCheck implements HealthIndicator {

	@Override
	public Health health() {
		Map<String, Object> details = new HashMap<String, Object>();
		details.put("versao", "1.0.0");
		details.put("descricao", "Serviço de propostas");
		details.put("endereco", "http://localhost:8080");
		return Health.status(Status.UP).withDetails(details).build();
	}

}
