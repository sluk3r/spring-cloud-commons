package org.springframework.cloud.commons.httpclient;

import org.apache.http.impl.client.HttpClientBuilder;

/**
 * Default implementation of {@link ApacheHttpClientFactory}.
 * @author Ryan Baxter
 */
public class DefaultApacheHttpClientFactory implements ApacheHttpClientFactory {

	private HttpClientBuilder builder;

	public DefaultApacheHttpClientFactory(HttpClientBuilder builder) {
		this.builder = builder;
	}

	/**
	 * A default {@link HttpClientBuilder}. The {@link HttpClientBuilder} returned will
	 * have content compression disabled, cookie management disabled, and use system properties.
	 */
	@Override
	public HttpClientBuilder createBuilder() {
		return this.builder.disableContentCompression() //wxc 2018-3-17:11:46:13 这种方式不错， 充分体现了Builder的意图。
				.disableCookieManagement().useSystemProperties(); //wxc pro 2018-3-17:11:47:08 这个具体在哪配置？
	}
}
