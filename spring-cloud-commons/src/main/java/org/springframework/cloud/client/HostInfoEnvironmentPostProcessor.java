package org.springframework.cloud.client;

import java.util.LinkedHashMap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.config.ConfigFileApplicationListener;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertySources;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.cloud.commons.util.InetUtils.HostInfo;
import org.springframework.cloud.commons.util.InetUtilsProperties;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

/**
 * @author Spencer Gibb
 */
public class HostInfoEnvironmentPostProcessor
		implements EnvironmentPostProcessor, Ordered {  //wxc pro 2018-3-16:8:50:29 第一次看到Ordered接口， 后续顺着这个接口的使用场景，把这个条逻辑线打通。看了SpringDocs里关于Ordered的描述， 发现配套的有Comparator， 这样也想起来了自己实现Comparator时的一个问题：不方便记住返回正表示升序，还是负表示升序。

	// Before ConfigFileApplicationListener
	private int order = ConfigFileApplicationListener.DEFAULT_ORDER - 1;

	@Override
	public int getOrder() {
		return this.order;
	}

	@Override
	public void postProcessEnvironment(ConfigurableEnvironment environment,
			SpringApplication application) {
		InetUtils.HostInfo hostInfo = getFirstNonLoopbackHostInfo(environment);
		LinkedHashMap<String, Object> map = new LinkedHashMap<>();
		map.put("spring.cloud.client.hostname", hostInfo.getHostname());
		map.put("spring.cloud.client.ip-address", hostInfo.getIpAddress());
		MapPropertySource propertySource = new MapPropertySource(
				"springCloudClientHostInfo", map);
		environment.getPropertySources().addLast(propertySource);
	}

	private HostInfo getFirstNonLoopbackHostInfo(ConfigurableEnvironment environment) {
		InetUtilsProperties target = new InetUtilsProperties();
		ConfigurationPropertySources.attach(environment);
		Binder.get(environment).bind(InetUtilsProperties.PREFIX,
				Bindable.ofInstance(target));
		try (InetUtils utils = new InetUtils(target)) {
			return utils.findFirstNonLoopbackHostInfo();
		}
	}
}
