/*
 * Copyright 2013-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.cloud.client;

import java.net.URI;
import java.util.Map;

/**
 * Represents an instance of a Service in a Discovery System
 * @author Spencer Gibb
 */
public interface ServiceInstance {  //wxc pro 2018-3-16:8:44:15 貌似只是Service元数据层面的描述， 没有看到期望中的Start和stop这样跟Service紧密相关的接口方法。

	/**
	 * @return the service id as registered.
	 */
	String getServiceId();

	/**
	 * @return the hostname of the registered ServiceInstance
	 */
	String getHost();

	/**
	 * @return the port of the registered ServiceInstance
	 */
	int getPort();

	/**
	 * @return if the port of the registered ServiceInstance is https or not
	 */
	boolean isSecure();

	/**
	 * @return the service uri address
	 */
	URI getUri();

	/**
	 * @return the key value pair metadata associated with the service instance
	 */
	Map<String, String> getMetadata();

	/**
	 * @return the scheme of the instance
	 */
	default String getScheme() {
		return null;
	}  //wxc pro 2018-3-16:8:43:24 Service的Scheme怎么理解？
}
