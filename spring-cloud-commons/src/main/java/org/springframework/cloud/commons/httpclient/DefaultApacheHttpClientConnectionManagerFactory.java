package org.springframework.cloud.commons.httpclient;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.commons.logging.LogFactory;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.commons.logging.Log;

/**
 * Default implementation of {@link ApacheHttpClientConnectionManagerFactory}.
 * @author Ryan Baxter
 * @author Michael Wirth
 */
public class DefaultApacheHttpClientConnectionManagerFactory
		implements ApacheHttpClientConnectionManagerFactory {

	private static final Log LOG = LogFactory
			.getLog(DefaultApacheHttpClientConnectionManagerFactory.class);

	public HttpClientConnectionManager newConnectionManager(boolean disableSslValidation,
			int maxTotalConnections, int maxConnectionsPerRoute) {
		return newConnectionManager(disableSslValidation, maxTotalConnections,
				maxConnectionsPerRoute, -1, TimeUnit.MILLISECONDS, null);
	}

	@Override
	//wxc pro 2018-3-17:11:48:56 Client和Connectio的关系?Client是配置信息的封装是一个静态概念, 而connection是动态概念. 是client创建出来的(也就是说Client是connection的Factory).这里Manager具体Manage了什么?业务概念接口梳理后,先在调用机制上使用接口方式编程, 后续具体使用什么样的接口实现, 通过Factory来解决. 这样,设计模式没有多少高大尚的。另一方面由于大师基于理念把设计问题及对应的方案归纳时， 已经是嚼碎的内容， 使用者如果不整体把握和理解的话，从已嚼碎的内容里， 反向还原出设计理念、问题和设计过程。这个过程，不单单是设计模式， 其它的框架性设计也是类似的问题与思路。
	public HttpClientConnectionManager newConnectionManager(boolean disableSslValidation,
			int maxTotalConnections, int maxConnectionsPerRoute, long timeToLive,
			TimeUnit timeUnit, RegistryBuilder registryBuilder) { //wxc 2018-3-17:12:01:17 RegistryBuilder是Apache自己的一个实现类。
		if (registryBuilder == null) {
			registryBuilder = RegistryBuilder.<ConnectionSocketFactory> create()
					.register(HTTP_SCHEME, PlainConnectionSocketFactory.INSTANCE);
		}
		if (disableSslValidation) {
			try {
				final SSLContext sslContext = SSLContext.getInstance("SSL");
				sslContext.init(null,
					new TrustManager[] { new DisabledValidationTrustManager()},
					new SecureRandom());
				registryBuilder.register(HTTPS_SCHEME, new SSLConnectionSocketFactory(
					sslContext, NoopHostnameVerifier.INSTANCE));
			}
			catch (NoSuchAlgorithmException e) {
				LOG.warn("Error creating SSLContext", e);
			}
			catch (KeyManagementException e) {
				LOG.warn("Error creating SSLContext", e);
			}
		} else {
			registryBuilder.register("https", SSLConnectionSocketFactory.getSocketFactory());
		}
		final Registry<ConnectionSocketFactory> registry = registryBuilder.build();

		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(
				registry, null, null, null, timeToLive, timeUnit);
		connectionManager.setMaxTotal(maxTotalConnections);
		connectionManager.setDefaultMaxPerRoute(maxConnectionsPerRoute);

		return connectionManager;
	}

	class DisabledValidationTrustManager implements X509TrustManager {
		@Override
		public void checkClientTrusted(X509Certificate[] x509Certificates,
			String s) throws CertificateException {
		}

		@Override
		public void checkServerTrusted(X509Certificate[] x509Certificates,
			String s) throws CertificateException {
		}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}
	}
}
