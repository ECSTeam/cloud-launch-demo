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
package com.ecsteam.cloudlaunch.statistics;

import java.io.IOException;

import com.ecsteam.cloudlaunch.statistics.model.ApplicationStatistics;

/**
 * A platform-agnostic way to provide statistics about an application
 * 
 * @author Josh Ghiloni
 *
 */
public interface ApplicationStatisticsProvider {
	public ApplicationStatistics getCurrentStatistics() throws IOException;
}
