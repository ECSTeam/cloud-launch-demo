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
package com.ecsteam.cloudlaunch.services.jenkins.model;

/**
 * @author Josh Ghiloni
 *
 */
public class QueuedBuildResponse {
	private String monitorUri;

	private long wait = 0L;

	public String getMonitorUri() {
		return monitorUri;
	}

	public void setMonitorUri(String monitorUri) {
		this.monitorUri = monitorUri;
	}

	public long getWait() {
		return wait;
	}

	public void setWait(long wait) {
		this.wait = wait;
	}
}
