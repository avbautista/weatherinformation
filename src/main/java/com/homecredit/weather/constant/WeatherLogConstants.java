/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.homecredit.weather.constant;

/**
 * @author allenbautista
 *
 */
public final class WeatherLogConstants {
	
	//Params
	public static final String locationParam = "q";
	public static final String apiKeyParam = "appid";
	public static final String modeParam = "mode";
	public static final String unitsParam = "units";
	public static final String langParam = "lang";
	
	//Response Messages
	public static final String retrieveSuccessMessage = "Successfully retrieved weather data.";
	public static final String parsingErrorMessage = "Encountered Errors with Parsing response.";
	public static final String unexpectedErrorMessage = "Unexpected Exception Occured. Please contact administrator.";

}
