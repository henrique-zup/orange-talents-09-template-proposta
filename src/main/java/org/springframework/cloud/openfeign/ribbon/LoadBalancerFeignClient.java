package org.springframework.cloud.openfeign.ribbon;

import org.springframework.cloud.netflix.ribbon.SpringClientFactory;

import feign.Client;

public class LoadBalancerFeignClient {
	// WORKAROUND (resolve problema na inicialização depois de adicionar o Jaeger)
	
	public LoadBalancerFeignClient(Client delegate, CachingSpringLoadBalancerFactory lbClientFactory,
			SpringClientFactory clientFactory) {
		throw new UnsupportedOperationException();
	}

	public Client getDelegate() {
		throw new UnsupportedOperationException();
	}
}