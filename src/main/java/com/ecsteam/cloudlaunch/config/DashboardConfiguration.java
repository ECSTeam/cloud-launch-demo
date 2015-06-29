package com.ecsteam.cloudlaunch.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ui.freemarker.SpringTemplateLoader;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.ecsteam.cloudlaunch.ApplicationStatisticsProperties;
import com.ecsteam.cloudlaunch.DashboardProperties;
import com.ecsteam.cloudlaunch.SelfUpdateProperties;
import com.ecsteam.cloudlaunch.StandardDemoProperties;
import com.ecsteam.cloudlaunch.controller.CloudLaunchDemoDashboard;

@Configuration
@EnableConfigurationProperties({ ApplicationStatisticsProperties.class, SelfUpdateProperties.class,
		StandardDemoProperties.class, DashboardProperties.class })
@ConditionalOnProperty(prefix = "cloudlaunch.dashboard", name = "enabled")
public class DashboardConfiguration {

	private static final String DEFAULT_TEMPLATE_LOADER_PATH = "classpath:/templates/";
	private static final String DEFAULT_CHARSET = "UTF-8";

	@Bean
	public CloudLaunchDemoDashboard dashboard() {
		return new CloudLaunchDemoDashboard();
	}

	/**
	* Overrides Spring Boot's {@link org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration} 
	* to prefer using a {@link SpringTemplateLoader} instead of the file system. This corrects an issue
	* where Spring Boot may use an empty 'templates' file resource to resolve templates instead of the 
	* packaged classpath templates. Taken from  
	* 
	* https://github.com/spring-cloud/spring-cloud-netflix/spring-cloud-netflix-hystrix-dashboard/src/main/java/org/springframework/cloud/netflix/hystrix/dashboard/HystrixDashboardConfiguration.java
	*  
	* @return FreeMarker configuration
	*/
	@Bean
	public FreeMarkerConfigurer freeMarkerConfigurer() {
		FreeMarkerConfigurer configurer = new FreeMarkerConfigurer();
		configurer.setTemplateLoaderPaths(DEFAULT_TEMPLATE_LOADER_PATH);
		configurer.setDefaultEncoding(DEFAULT_CHARSET);
		configurer.setPreferFileSystemAccess(false);
		return configurer;
	}
}
