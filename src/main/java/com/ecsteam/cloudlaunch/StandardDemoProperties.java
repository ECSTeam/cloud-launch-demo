package com.ecsteam.cloudlaunch;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties("cloudlaunch.standardDemo")
public class StandardDemoProperties {
	private boolean enabled = false;
}
