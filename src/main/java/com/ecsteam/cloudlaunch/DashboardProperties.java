package com.ecsteam.cloudlaunch;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties("cloudlaunch.dashboard")
public class DashboardProperties {
	private boolean enabled = false;
}
