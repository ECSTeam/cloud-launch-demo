/*
 * Copyright 2015 ECS Team, Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ecsteam.cloudlaunch.services.github;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.github.api.GitHub;
import org.springframework.social.github.api.GitHubCommit;
import org.springframework.social.github.api.GitHubUser;
import org.springframework.social.github.api.RepoOperations;
import org.springframework.social.github.connect.GitHubServiceProvider;

import com.ecsteam.cloudlaunch.services.github.model.GithubCommit;

/**
 * @author Josh Ghiloni
 *
 */
public class GithubService {
	@Value("${ecs.github.clientId:}")
	private String clientId;
	
	@Value("${ecs.github.clientSecret:}")
	private String clientSecret;
	
	@Value("${ecs.github.repoName:}")
	private String repoName;
	
	@Value("${ecs.github.repoOwner:}")
	private String repoOwner;
	
	@Value("${ecs.github.accessToken:}")
	private String accessToken;

	public GithubCommit getLatestCommit() {
		GitHub client = getClient();
		RepoOperations repoClient = client.repoOperations();
		List<GitHubCommit> commits = repoClient.getCommits(repoOwner, repoName);
		
		if (commits != null && commits.size() > 0) {
			GitHubCommit commit = commits.get(0);
			
			GithubCommit response = new GithubCommit();
			response.setSha(commit.getSha());
			response.setMessage(commit.getMessage());
			
			long time = 0;
			GitHubUser user = commit.getCommitter();
			if (user != null) {
				Date date = user.getDate();
				if (date != null) {
					time = date.getTime();					
				}
			}
			
			response.setTimestamp(time);
			
			return response;
		}
		
		return null;
	}
	
	private GitHub getClient() {
		GitHubServiceProvider factory = new GitHubServiceProvider(clientId, clientSecret);
		return factory.getApi(accessToken);
	}
}
