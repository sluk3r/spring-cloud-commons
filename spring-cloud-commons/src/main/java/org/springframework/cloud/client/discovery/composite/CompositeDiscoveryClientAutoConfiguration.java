package org.springframework.cloud.client.discovery.composite;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.simple.SimpleDiscoveryClientAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.List;

/**
 * Auto-configuration for Composite Discovery Client.
 * 
 * @author Biju Kunjummen
 */

@Configuration
@AutoConfigureBefore(SimpleDiscoveryClientAutoConfiguration.class)
//wxc 2018-3-17:10:55:23 使用这种方式不错， 不再使用XML地达到Factory的作用。
public class CompositeDiscoveryClientAutoConfiguration {
	
	@Bean
	@Primary
	public CompositeDiscoveryClient compositeDiscoveryClient(List<DiscoveryClient> discoveryClients) {
		return new CompositeDiscoveryClient(discoveryClients);
	}
	
}
