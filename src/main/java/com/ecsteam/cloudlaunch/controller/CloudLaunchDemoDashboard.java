package com.ecsteam.cloudlaunch.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ecsteam.cloudlaunch.ApplicationStatisticsProperties;
import com.ecsteam.cloudlaunch.SelfUpdateProperties;
import com.ecsteam.cloudlaunch.StandardDemoProperties;

@Controller
public class CloudLaunchDemoDashboard {
	@Autowired
	private ApplicationStatisticsProperties statisticsProperties;
	
	@Autowired
	private StandardDemoProperties demoProperties;
	
	@Autowired
	private SelfUpdateProperties selfUpdateProperties;

	@RequestMapping("/")
	public String getDashboard(Model model) {
		model.addAttribute("statisticsEnabled", statisticsProperties.isEnabled());
		model.addAttribute("standardDemo", demoProperties.isEnabled());
		model.addAttribute("selfUpdateEnabled", selfUpdateProperties.isEnabled());
		return "dashboard";
	}
}
