/*
 * Copyright 2015 ECS Team, Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package com.ecsteam.cloudlaunch.services.statistics;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.cloudfoundry.client.lib.CloudFoundryOperations;
import org.cloudfoundry.client.lib.domain.ApplicationStats;
import org.cloudfoundry.client.lib.domain.CloudApplication;
import org.cloudfoundry.client.lib.domain.CloudEntity.Meta;
import org.cloudfoundry.client.lib.domain.InstanceStats;
import org.cloudfoundry.client.lib.domain.InstanceStats.Usage;
import org.cloudfoundry.client.lib.org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.ecsteam.cloudlaunch.services.statistics.model.ApplicationInstance;
import com.ecsteam.cloudlaunch.services.statistics.model.ApplicationStatistics;
import com.ecsteam.cloudlaunch.services.statistics.model.VcapApplication;

/**
 * Use the Cloud Controller API to determine statistics for the current application
 * 
 * @author Josh Ghiloni
 *
 */
public class CloudFoundryStatisticsProvider implements ApplicationStatisticsProvider {
	@Autowired
	private CloudFoundryOperations client;

	private static long MEGS_TO_BYTES = 1048576L;

	@Override
	public ApplicationStatistics getCurrentStatistics(String applicationName) throws IOException {
		CloudApplication appInfo = client.getApplication(applicationName);
		ApplicationStats cfAppStats = client.getApplicationStats(applicationName);

		ApplicationStatistics stats = new ApplicationStatistics();
		stats.setName(applicationName);
		stats.setDiskLimit(appInfo.getDiskQuota() * MEGS_TO_BYTES);
		stats.setRamLimit(appInfo.getMemory() * MEGS_TO_BYTES);
		addInstanceInfo(stats, cfAppStats);

		Meta meta = appInfo.getMeta();
		if (meta != null) {
			UUID appGuid = meta.getGuid();
			if (appGuid != null) {
				stats.setId(appGuid.toString());
			}
		}

		VcapApplication application = null;
		Map<String, String> appEnv = appInfo.getEnvAsMap();
		String vcapString = appEnv.get("VCAP_APPLICATION");
		if (vcapString != null) {
			application = (new ObjectMapper()).readValue(vcapString, VcapApplication.class);
			stats.setHost(application.getHost());
			stats.setPort(application.getPort());
			stats.setActiveInstance(application.getActiveInstanceIndex());
		}

		return stats;
	}

	private void addInstanceInfo(ApplicationStatistics statistics, ApplicationStats cfAppStats) {
		List<InstanceStats> infos = cfAppStats.getRecords();
		List<ApplicationInstance> appInstances = statistics.getInstances();

		if (infos != null) {
			statistics.setInstanceCount(infos.size());
		}

		for (int index = 0; index < infos.size(); ++index) {
			InstanceStats instance = infos.get(index);

			ApplicationInstance appInstance = new ApplicationInstance();
			appInstance.setState(instance.getState());

			Usage usage = instance.getUsage();

			if (usage != null) {
				appInstance.setCpu(usage.getCpu());
				appInstance.setDisk(usage.getDisk());
				appInstance.setMemory(usage.getMem());
			}

			appInstances.add(appInstance);
		}
	}
}
