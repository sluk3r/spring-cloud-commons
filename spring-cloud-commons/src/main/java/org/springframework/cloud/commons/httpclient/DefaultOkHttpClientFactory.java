package org.springframework.cloud.commons.httpclient;

import okhttp3.OkHttpClient;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Default implementation of {@link OkHttpClientFactory}.
 * @author Ryan Baxter
 */
public class DefaultOkHttpClientFactory implements OkHttpClientFactory {

	private static final Log LOG = LogFactory.getLog(DefaultOkHttpClientFactory.class);

	private OkHttpClient.Builder builder;

	public DefaultOkHttpClientFactory(OkHttpClient.Builder builder) {
		this.builder = builder;
	}

	@Override
	public OkHttpClient.Builder createBuilder(boolean disableSslValidation) { //wxc 2018-3-17:12:36:04 Factory里创建时， 又返回了一个Builder， 感觉是不是有些啰嗦了。
		if (disableSslValidation) {
			try {
				X509TrustManager disabledTrustManager = new DisableValidationTrustManager();
				TrustManager[] trustManagers = new TrustManager[1];
				trustManagers[0] = disabledTrustManager;
				SSLContext sslContext = SSLContext.getInstance("SSL");
				sslContext.init(null, trustManagers, new java.security.SecureRandom());
				SSLSocketFactory disabledSSLSocketFactory = sslContext.getSocketFactory();
				builder.sslSocketFactory(disabledSSLSocketFactory, disabledTrustManager);//wxc 2018-3-17:12:37:22 这里边， 封装了一个SocketFactory
				builder.hostnameVerifier(new TrustAllHostnames());
			}
			catch (NoSuchAlgorithmException e) {
				LOG.warn("Error setting SSLSocketFactory in OKHttpClient", e);
			}
			catch (KeyManagementException e) {
				LOG.warn("Error setting SSLSocketFactory in OKHttpClient", e);
			}
		}
		return builder;
	}
}
