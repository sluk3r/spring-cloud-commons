/*
 *
 *  * Copyright 2013-2016 the original author or authors.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package org.springframework.cloud.commons.httpclient;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 * Factory for creating a new {@link CloseableHttpClient}.
 * @author Ryan Baxter
 */
public interface ApacheHttpClientFactory {

	/**
	 * Creates an {@link HttpClientBuilder} that can be used to create a new {@link CloseableHttpClient}.
	 * @return A {@link HttpClientBuilder}
	 */
	public HttpClientBuilder createBuilder(); //wxc pro 2018-3-17:11:43:00 这个生成的结果跟Docs里提到了CloseableHttpClient有什么关系？是子接口（或子类）？HttpClientBuilder是Apache的实现
}
