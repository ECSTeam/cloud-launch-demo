package com.ecsteam.cloudlaunch.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ecsteam.cloudlaunch.controller.SelfUpdateRestController;
import com.ecsteam.cloudlaunch.services.github.GithubService;
import com.ecsteam.cloudlaunch.services.jenkins.JenkinsService;

@Configuration
@ConditionalOnProperty(prefix = "cloudlaunch.selfUpdate", name = "enabled")
public class SelfUpdateConfiguration {
	
	@Bean
	public JenkinsService jenkinsService() {
		return new JenkinsService();
	}
	
	@Bean
	public GithubService githubService() {
		return new GithubService();
	}
	
	@Bean
	public SelfUpdateRestController controller() {
		return new SelfUpdateRestController();
	}
}
