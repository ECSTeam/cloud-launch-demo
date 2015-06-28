package com.ecsteam.cloudlaunch.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecsteam.cloudlaunch.ApplicationStatisticsProperties;
import com.ecsteam.cloudlaunch.services.statistics.ApplicationStatisticsProvider;
import com.ecsteam.cloudlaunch.services.statistics.model.ApplicationStatistics;
import com.ecsteam.cloudlaunch.services.statistics.model.VcapApplication;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class ApplicationStatisticsRestController {

	@Autowired
	private ApplicationStatisticsProvider statisticsProvider;

	@Autowired
	private Environment env;

	@Autowired
	private ApplicationStatisticsProperties properties;

	@RequestMapping("/statistics")
	public ApplicationStatistics statistics(HttpServletRequest request) throws Exception {
		ApplicationStatistics statistics = statisticsProvider.getCurrentStatistics(getApplicationName());

		statistics.setHost(request.getLocalAddr());
		statistics.setPort(request.getLocalPort());

		return statistics;
	}

	private String getApplicationName() throws Exception {
		String monitoredService = properties.getMonitoredService();

		if (monitoredService == null) {
			String vcapApplicationString = env.getProperty("vcap.application");
			if (StringUtils.hasText(vcapApplicationString)) {
				VcapApplication application = (new ObjectMapper()).readValue(vcapApplicationString,
						VcapApplication.class);
				monitoredService = application.getApplicationName();
			}
		}
		
		return monitoredService;
	}
}