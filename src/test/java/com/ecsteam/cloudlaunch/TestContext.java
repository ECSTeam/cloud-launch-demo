package com.ecsteam.cloudlaunch;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.ecsteam.cloudlaunch.services.statistics.ApplicationStatisticsProvider;
import com.ecsteam.cloudlaunch.services.statistics.JvmStatisticsProvider;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages =
{ "com.ecsteam.cloudlaunch" })
public class TestContext 
{
	@Bean
	ApplicationStatisticsProvider jvmStatisticsProvider() {
		return Mockito.mock(JvmStatisticsProvider.class);
	}
}
