package com.ecsteam.cloudlaunch.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.social.github.api.GitHub;
import org.springframework.social.github.connect.GitHubServiceProvider;

import com.ecsteam.cloudlaunch.SelfUpdateProperties;
import com.ecsteam.cloudlaunch.controller.SelfUpdateRestController;
import com.ecsteam.cloudlaunch.services.github.GithubService;
import com.ecsteam.cloudlaunch.services.jenkins.JenkinsService;

@Configuration
@ConditionalOnProperty(prefix = "cloudlaunch.selfUpdate", name = "enabled")
@ConditionalOnBean(ApplicationStatisticsConfiguration.SelfMonitoringMarker.class)
public class SelfUpdateConfiguration {
	@Autowired
	private SelfUpdateProperties properties;
	
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
	
	@Bean
	@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "session")
	public GitHub getClient() {
		GitHubServiceProvider factory = new GitHubServiceProvider(properties.getGitClientId(), properties.getGitClientSecret());
		return factory.getApi(properties.getGitAccessToken());
	}
}
