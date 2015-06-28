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

import java.util.ArrayList;
import java.util.List;

/**
 * Represents interesting information about an application and its instances
 * 
 * @author Josh Ghiloni
 *
 */
public class ApplicationStatistics {
	private String id;

	private String name;

	private String host;

	private int port = -1;

	private int activeInstance = -1;

	private int instanceCount = 0;

	private long ramLimit = -1;

	private long diskLimit = -1;

	private List<ApplicationInstance> instances;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getInstanceCount() {
		return instanceCount;
	}

	public void setInstanceCount(int instanceCount) {
		this.instanceCount = instanceCount;
	}

	public long getRamLimit() {
		return ramLimit;
	}

	public void setRamLimit(long ramLimit) {
		this.ramLimit = ramLimit;
	}

	public long getDiskLimit() {
		return diskLimit;
	}

	public void setDiskLimit(long diskLimit) {
		this.diskLimit = diskLimit;
	}

	public List<ApplicationInstance> getInstances() {
		if (instances == null) {
			instances = new ArrayList<ApplicationInstance>();
		}

		return instances;
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

	public int getActiveInstance() {
		return activeInstance;
	}

	public void setActiveInstance(int activeInstance) {
		this.activeInstance = activeInstance;
	}
}
