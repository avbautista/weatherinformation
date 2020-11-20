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
package com.homecredit.weather.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.homecredit.weather.constant.WeatherLogConstants;
import com.homecredit.weather.dto.GetLatestWeatherLogResponseDto;
import com.homecredit.weather.dto.GetWeatherResponseDto;
import com.homecredit.weather.service.WeatherLogService;

/**
 * @author allenbautista
 *
 */

/* This is the controller component of the application. This could
 * further implement a controller that have a generic error handling
 * for http status codes but for purposes of this homework, exception 
 * handling is also handled here */

@RestController
public class WeatherLogController {
	
	private WeatherLogService weatherLogService;
	
	@Autowired
	public WeatherLogController(WeatherLogService weatherLogService) {
		this.weatherLogService = weatherLogService;
	}
	
	@GetMapping("/weather")
	public ResponseEntity<GetWeatherResponseDto> getWeather(
			@RequestParam(required = true) String q,
			@RequestParam(required = false) String units,
			@RequestParam(required = false) String lang) {
		
		GetWeatherResponseDto response = null;
		
		try {
			response = weatherLogService.getAndSaveWeather(q, units, lang);
		} catch (JsonProcessingException e) {
			response = new GetWeatherResponseDto(HttpStatus.UNPROCESSABLE_ENTITY.value(),
					WeatherLogConstants.parsingErrorMessage);
		}
		
		return new ResponseEntity<GetWeatherResponseDto>(response, 
				HttpStatus.valueOf(response.getCode()));
	}
	
	/* No saved weather logs will return empty list, so there should be no errors */
	@GetMapping("/weather/getlatest")
	public ResponseEntity<GetLatestWeatherLogResponseDto> getWeather() {
		
		GetLatestWeatherLogResponseDto response = weatherLogService.getLatestWeatherLogs();
		
		return new ResponseEntity<GetLatestWeatherLogResponseDto>(response, HttpStatus.valueOf(response.getCode()));
	}

}
