package com.ecsteam.cloudlaunch.controller;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ecsteam.cloudlaunch.SelfUpdateProperties;
import com.ecsteam.cloudlaunch.services.github.GithubService;
import com.ecsteam.cloudlaunch.services.github.model.GithubCommit;
import com.ecsteam.cloudlaunch.services.jenkins.JenkinsService;
import com.ecsteam.cloudlaunch.services.jenkins.model.MonitorResponse;
import com.ecsteam.cloudlaunch.services.jenkins.model.QueuedBuildResponse;

@RestController
@RequestMapping("/services")
public class SelfUpdateRestController {
	@Autowired
	private JenkinsService jenkinsService;

	@Autowired
	private GithubService githubService;
	
	@Autowired
	private SelfUpdateProperties selfUpdateProperties;

	@RequestMapping(value = "/builds/trigger", method = RequestMethod.POST)
	public ResponseEntity<QueuedBuildResponse> triggerBuild() {
		QueuedBuildResponse responseBody = null;
		ResponseEntity<QueuedBuildResponse> response = null;

		responseBody = jenkinsService.triggerBuild();
		if (responseBody != null) {
			response = new ResponseEntity<QueuedBuildResponse>(responseBody, HttpStatus.OK);
		}
		else {
			response = new ResponseEntity<QueuedBuildResponse>(HttpStatus.BAD_REQUEST);
		}

		return response;
	}

	@RequestMapping(value = "/builds/queue/{queueId}", method = RequestMethod.GET)
	public ResponseEntity<QueuedBuildResponse> getJobNumberFromQueue(@PathVariable("queueId") String queueId) {
		QueuedBuildResponse responseBody = null;
		ResponseEntity<QueuedBuildResponse> response = null;

		responseBody = jenkinsService.getJobNumberFromQueue(queueId);
		if (responseBody != null) {
			// add URI if it's not there
			if (responseBody.getMonitorUri() == null) {
				responseBody.setMonitorUri(String.format("/service/builds/queue/%s", queueId));
			}
			
			response = new ResponseEntity<QueuedBuildResponse>(responseBody, HttpStatus.OK);
		}
		else {
			response = new ResponseEntity<QueuedBuildResponse>(HttpStatus.BAD_REQUEST);
		}

		return response;
	}

	@RequestMapping(value = "/builds/job/{jobNumber}", method = RequestMethod.GET)
	public ResponseEntity<MonitorResponse> monitorBuild(@PathVariable("jobNumber") String jobNumber) {
		MonitorResponse responseBody = null;
		ResponseEntity<MonitorResponse> response = null;

		responseBody = jenkinsService.monitorJob(jobNumber);
		if (responseBody != null) {
			response = new ResponseEntity<MonitorResponse>(responseBody, HttpStatus.OK);
		}
		else {
			response = new ResponseEntity<MonitorResponse>(HttpStatus.BAD_REQUEST);
		}

		return response;
	}

	@RequestMapping(value = "/buildInfo/source", method = RequestMethod.GET)
	public ResponseEntity<GithubCommit> getLatestCommitInfo() {
		GithubCommit responseBody = githubService.getLatestCommit();

		if (responseBody != null) {
			return new ResponseEntity<GithubCommit>(responseBody, HttpStatus.OK);
		}
		else {
			return new ResponseEntity<GithubCommit>(HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/buildInfo/deployed", method = RequestMethod.GET)
	public Map<String, String> getDeployedSha() {
		// show null if empty string
		String sha = null;
		String latestSha = selfUpdateProperties.getLatestGitCommit();
		if (StringUtils.hasText(latestSha)) {
			sha = latestSha;
		}
		
		return Collections.singletonMap("deployedSha", sha);
	}

}
