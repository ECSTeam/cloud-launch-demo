package com.ecsteam.cloudlaunch;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties("cloudlaunch.statistics")
public class ApplicationStatisticsProperties {
	
	private boolean enabled = false;
	
	private String url = null;
	
	private String user = null;
	
	private String password = null;
	
	private MonitoredService monitoredService = null;
	
	private boolean trustSelfSignedCerts = false;
}
