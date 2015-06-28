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
package com.ecsteam.cloudlaunch.services.statistics;

import org.cloudfoundry.client.lib.domain.InstanceState;

import com.ecsteam.cloudlaunch.services.statistics.model.ApplicationInstance;
import com.ecsteam.cloudlaunch.services.statistics.model.ApplicationStatistics;

/**
 * Provides statistics available from the current JVM when not in cloudfoundry
 * 
 * @author Josh Ghiloni
 *
 */
public class JvmStatisticsProvider implements ApplicationStatisticsProvider {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ecsteam.cloudlaunch.statistics.StatisticsProvider#getCurrentStatistics()
	 */
	@Override
	public ApplicationStatistics getCurrentStatistics(String applicationName) {
		ApplicationStatistics stats = new ApplicationStatistics();
		stats.setInstanceCount(1);
		stats.setActiveInstance(0);
		stats.setId(null);
		stats.setName(applicationName);
		stats.setDiskLimit(1);
		stats.setHost(null);
		stats.setPort(0);

		Runtime runtime = Runtime.getRuntime();

		long ramLimit = runtime.maxMemory();
		if (ramLimit == Long.MAX_VALUE) {
			ramLimit = -1;
		}

		stats.setRamLimit(ramLimit);

		ApplicationInstance instance = new ApplicationInstance();

		instance.setMemory(runtime.totalMemory());
		instance.setCpu(0);
		instance.setDisk(0);
		instance.setState(InstanceState.RUNNING);

		stats.getInstances().add(instance);

		return stats;
	}
}
