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
public class MonitorResponse {
	private boolean stillBuilding;
	
	private String result;
	
	private long duration;
	
	private String monitorUri;

	public boolean isStillBuilding() {
		return stillBuilding;
	}

	public void setStillBuilding(boolean stillBuilding) {
		this.stillBuilding = stillBuilding;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public String getMonitorUri() {
		return monitorUri;
	}

	public void setMonitorUri(String monitorUri) {
		this.monitorUri = monitorUri;
	}
}
