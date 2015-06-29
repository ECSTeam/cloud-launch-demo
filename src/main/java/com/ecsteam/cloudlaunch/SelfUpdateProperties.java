package com.ecsteam.cloudlaunch;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties("cloudlaunch.selfUpdate")
public class SelfUpdateProperties {
	private boolean enabled = false;
}
