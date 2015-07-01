package com.ecsteam.cloudlaunch.config;

import java.net.URL;

import org.cloudfoundry.client.lib.CloudCredentials;
import org.cloudfoundry.client.lib.CloudFoundryClient;
import org.cloudfoundry.client.lib.CloudFoundryOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.StringUtils;

import com.ecsteam.cloudlaunch.ApplicationStatisticsProperties;
import com.ecsteam.cloudlaunch.controller.ApplicationStatisticsRestController;
import com.ecsteam.cloudlaunch.services.statistics.ApplicationStatisticsProvider;
import com.ecsteam.cloudlaunch.services.statistics.CloudFoundryStatisticsProvider;
import com.ecsteam.cloudlaunch.services.statistics.JvmStatisticsProvider;

@Configuration
@ConditionalOnProperty(prefix = "cloudlaunch.statistics", name = "enabled")
public class ApplicationStatisticsConfiguration {
	@Autowired
	private ApplicationStatisticsProperties properties;

	@Bean
	public ApplicationStatisticsRestController restController() {
		return new ApplicationStatisticsRestController();
	}

	@Bean
	@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "session")
	public CloudFoundryOperations cloudFoundryClient() throws Exception {
		URL cfUrl = new URL(properties.getUrl());
		CloudCredentials creds = new CloudCredentials(properties.getUser(), properties.getPassword());
		CloudFoundryOperations client = new CloudFoundryClient(creds, cfUrl, properties.isTrustSelfSignedCerts());

		return client;
	}

	/**
	 * Create an instance of the provider that can call the Cloud Foundry CloudController APIs to get statistic info
	 * 
	 * @return
	 */
	@Conditional(CanMonitorCloudFoundryService.class)
	@Bean
	public ApplicationStatisticsProvider cloudFoundryStatisticsProvider() {
		return new CloudFoundryStatisticsProvider();
	}

	/**
	 * Create an instance of the provider that only uses info available from the JVM
	 * 
	 * @return
	 */
	@Conditional(CannotMonitorCloudFoundryService.class)
	@Bean
	public ApplicationStatisticsProvider jvmStatisticsProvider() {
		return new JvmStatisticsProvider();
	}

	@Bean
	public SelfMonitoringMarker markerBean() {
		String vcapApp = System.getenv("VCAP_APPLICATION");
		String monitoredService = properties.getMonitoredService();
		
		if (StringUtils.hasText(vcapApp) && StringUtils.isEmpty(monitoredService)) {
			return SelfMonitoringMarker.INSTANCE;
		}
		
		return null;
	}

	/**
	 * A marker bean class to indicate that the service is monitoring itself
	 */
	public static class SelfMonitoringMarker {
		private SelfMonitoringMarker() {
		}

		public static final SelfMonitoringMarker INSTANCE = new SelfMonitoringMarker();
	}

	/**
	 * Matches if the VCAP_APPLICATION environment variable is present, letting us know that we are in -- or are trying
	 * to pretend we are in -- a Cloud Foundry environment
	 * 
	 * @author Josh Ghiloni
	 *
	 */
	private static class CanMonitorCloudFoundryService extends SpringBootCondition {
		@Override
		public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
			String monitoredService = context.getEnvironment().getProperty("cloudlaunch.statistics.monitoredService");
			String vcapApp = System.getenv("VCAP_APPLICATION");

			if (StringUtils.hasText(monitoredService)) {
				return ConditionOutcome.match("Monitoring remote service");
			}

			if (StringUtils.hasText(vcapApp)) {
				return ConditionOutcome.match("Monitoring local service and VCAP_APPLICATION env variable is present");
			} else {
				return ConditionOutcome
						.noMatch("Monitoring local service and VCAP_APPLICATION env variable is not present");
			}
		}
	}

	/**
	 * Matches if {@link CanMonitorCloudFoundryService} does NOT match
	 * 
	 * @author Josh Ghiloni
	 *
	 */
	private static class CannotMonitorCloudFoundryService extends SpringBootCondition {
		private CanMonitorCloudFoundryService opposite = new CanMonitorCloudFoundryService();

		@Override
		public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
			ConditionOutcome negativeOutcome = opposite.getMatchOutcome(context, metadata);

			if (negativeOutcome.isMatch()) {
				return ConditionOutcome.noMatch(negativeOutcome.getMessage());
			} else {
				return ConditionOutcome.match(negativeOutcome.getMessage());
			}
		}
	}
}
