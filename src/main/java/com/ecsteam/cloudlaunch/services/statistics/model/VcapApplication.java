/*
 * Copyright 2015 ECS Team, Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ecsteam.cloudlaunch.services.statistics.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * An (incomplete) object representation of the VCAP_APPLICATION environment variable presented by CloudFoundry
 * 
 * @author Josh Ghiloni
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class VcapApplication {
	@JsonProperty("limits")
	private Limits limits;

	@JsonProperty("application_version")
	private String applicationVersion;

	@JsonProperty("application_name")
	private String applicationName;

	@JsonProperty("application_uris")
	private String[] uris;

	@JsonProperty("application_id")
	private String applicationId;

	@JsonProperty("instance_id")
	private String activeInstanceId;

	@JsonProperty("instance_index")
	private int activeInstanceIndex;

	@JsonProperty("host")
	private String host;

	@JsonProperty("port")
	private int port;

	public Limits getLimits() {
		return limits;
	}

	public void setLimits(Limits limits) {
		this.limits = limits;
	}

	public String getApplicationVersion() {
		return applicationVersion;
	}

	public void setApplicationVersion(String applicationVersion) {
		this.applicationVersion = applicationVersion;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public String[] getUris() {
		return uris;
	}

	public void setUris(String[] uris) {
		this.uris = uris;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public String getActiveInstanceId() {
		return activeInstanceId;
	}

	public void setActiveInstanceId(String activeInstanceId) {
		this.activeInstanceId = activeInstanceId;
	}

	public int getActiveInstanceIndex() {
		return activeInstanceIndex;
	}

	public void setActiveInstanceIndex(int activeInstanceIndex) {
		this.activeInstanceIndex = activeInstanceIndex;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public static class Limits {
		private int mem;

		private int disk;

		private int fds;

		public int getMem() {
			return mem;
		}

		public void setMem(int mem) {
			this.mem = mem;
		}

		public int getDisk() {
			return disk;
		}

		public void setDisk(int disk) {
			this.disk = disk;
		}

		public int getFds() {
			return fds;
		}

		public void setFds(int fds) {
			this.fds = fds;
		}
	}
}
