package com.ecsteam.cloudlaunch;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties("cloudlaunch.selfUpdate")
public class SelfUpdateProperties {
	private boolean enabled = false;
	
	private String latestGitCommit;
	
	private String jenkinsUrl;
	
	private String jenkinsJob = "";
	
	private String jenkinsUser;
	
	private String jenkinsPassword;
	
	private String gitClientId;
	
	private String gitClientSecret;
	
	private String gitRepo;
	
	private String gitRepoOwner;
	
	private String gitAccessToken;
	
	// Currently this has no effect, as spring-social-github only works with github.com
	private String gitUrl = "https://github.com";
}
