package com.ecsteam.cloudlaunch.config;

import java.net.URL;

import org.cloudfoundry.client.lib.CloudCredentials;
import org.cloudfoundry.client.lib.CloudFoundryClient;
import org.cloudfoundry.client.lib.CloudFoundryOperations;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.StringUtils;

import com.ecsteam.cloudlaunch.ApplicationStatisticsProperties;
import com.ecsteam.cloudlaunch.controller.ApplicationStatisticsRestController;
import com.ecsteam.cloudlaunch.services.statistics.ApplicationStatisticsProvider;
import com.ecsteam.cloudlaunch.services.statistics.CloudFoundryStatisticsProvider;
import com.ecsteam.cloudlaunch.services.statistics.JvmStatisticsProvider;

@Configuration
@EnableConfigurationProperties(ApplicationStatisticsProperties.class)
@ConditionalOnProperty(prefix = "cloudfoundry.dashboard", name = "enabled")
public class ApplicationStatisticsConfiguration {
	@Bean
	public ApplicationStatisticsRestController restController() {
		return new ApplicationStatisticsRestController();
	}

	@Bean
	public CloudFoundryOperations cloudFoundryClient() throws Exception {
		ApplicationStatisticsProperties properties = new ApplicationStatisticsProperties();

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
	@Conditional(InCloudFoundry.class)
	@Bean
	public ApplicationStatisticsProvider cloudFoundryStatisticsProvider() {
		return new CloudFoundryStatisticsProvider();
	}

	/**
	 * Create an instance of the provider that only uses info available from the JVM
	 * 
	 * @return
	 */
	@Conditional(NotInCloudFoundry.class)
	@Bean
	public ApplicationStatisticsProvider jvmStatisticsProvider() {
		return new JvmStatisticsProvider();
	}

	/**
	 * Matches if the VCAP_APPLICATION environment variable is present, letting us know that we are in -- or are trying
	 * to pretend we are in -- a Cloud Foundry environment
	 * 
	 * @author Josh Ghiloni
	 *
	 */
	private static class InCloudFoundry extends SpringBootCondition {
		@Override
		public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
			String vcapApp = System.getenv("VCAP_APPLICATION");

			if (StringUtils.hasText(vcapApp)) {
				return ConditionOutcome.match("VCAP_APPLICATION env variable is present");
			} else {
				return ConditionOutcome.noMatch("VCAP_APPLICATION env variable is not present");
			}
		}
	}

	/**
	 * Matches if {@link InCloudFoundry} does NOT match
	 * 
	 * @author Josh Ghiloni
	 *
	 */
	private static class NotInCloudFoundry extends SpringBootCondition {
		private InCloudFoundry opposite = new InCloudFoundry();

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
