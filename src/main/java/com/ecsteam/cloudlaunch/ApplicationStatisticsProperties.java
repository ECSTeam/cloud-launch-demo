package com.ecsteam.cloudlaunch;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties("statistics.dashboard")
public class ApplicationStatisticsProperties {
	
	private boolean enabled = false;
	
	private boolean uienabled = false;
	
	private String url = null;
	
	private String user = null;
	
	private String password = null;
	
	private String monitoredService = null;
	
	private boolean trustSelfSignedCerts = false;
}
