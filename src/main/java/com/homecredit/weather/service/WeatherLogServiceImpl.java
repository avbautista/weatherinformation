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
package com.homecredit.weather.service;

import java.net.URI;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.homecredit.weather.constant.WeatherLogConstants;
import com.homecredit.weather.dto.GetLatestWeatherLogResponseDto;
import com.homecredit.weather.dto.GetWeatherResponseDto;
import com.homecredit.weather.dto.WeatherLogDto;
import com.homecredit.weather.dto.thirdparty.OWMGenericReponseDto;
import com.homecredit.weather.dto.thirdparty.OWMWeatherDto;
import com.homecredit.weather.dto.thirdparty.OWMWeatherInformationDto;
import com.homecredit.weather.model.WeatherLog;
import com.homecredit.weather.repository.WeatherLogRepository;

/**
 * @author allenbautista
 *
 */

@Service
public class WeatherLogServiceImpl implements WeatherLogService {
	
	private WeatherLogRepository weatherLogRepository;
	
	/* Values from application properties. This is because API Keys
	 * should not be hardcoded in code, and should be stored in a 
	 * configuration file that is secure. These variables could further
	 * be stored in an Application property object when they become
	 * too many */
	
	@Value("${openweathermap.url}")
	private String openWeatherMapUrl;
	
	@Value("${openweathermap.apikey}")
	private String openWeatherMapAPIKey;
	
	@Autowired
	public WeatherLogServiceImpl(WeatherLogRepository weatherLogRepository) {
		this.weatherLogRepository = weatherLogRepository;
	}
	
	/* RestTemplate could further be generalized if it is needed to be reused,
	 * but for this homework, since it is only used once, I kept it as is */
	
	@Override
	public GetWeatherResponseDto getAndSaveWeather(String q, String units, String lang) throws JsonMappingException, JsonProcessingException {
		
		RestTemplate restTemplate = new RestTemplate();
	    Integer statusCode = HttpStatus.OK.value();
	    String message = WeatherLogConstants.retrieveSuccessMessage;
	    GetWeatherResponseDto response = null;
	    
		final String baseUrl = openWeatherMapUrl;
		
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
	    params.add(WeatherLogConstants.locationParam, q);
	    params.add(WeatherLogConstants.apiKeyParam, openWeatherMapAPIKey);
	    params.add(WeatherLogConstants.unitsParam, units);
	    params.add(WeatherLogConstants.langParam, lang);
	    
	    URI targetUrl= UriComponentsBuilder.fromUriString(baseUrl)
				.queryParams(params)
				.build()
				.encode()
				.toUri();

	    try {
	    	OWMWeatherInformationDto result = restTemplate.getForObject(targetUrl, 
		    		OWMWeatherInformationDto.class);
	    	
	    	String weather = parseAllWeatherDescription(result.getWeather());
	    	
	    	response = new GetWeatherResponseDto(statusCode, message, 
		    		result.getName(),
		    		weather,
		    		result.getMain().getTemp());
	    	
	    	/* Saved as UTC, no need to display so no need for timezones for now */
	    	Timestamp stamp = new Timestamp(System.currentTimeMillis());
	    	Date date = new Date(stamp.getTime());
	    	
	    	WeatherLog log = WeatherLog.builder()
	    		.responseId(UUID.randomUUID().toString())
	    		.location(result.getName())
	    		.actualWeather(weather)
	    		.temperature(result.getMain().getTemp())
	    		.dtimeInserted(date)
	    		.build();
	    	weatherLogRepository.save(log);
	    	
	    } catch (HttpStatusCodeException e) { //Get custom error message
	    	ObjectMapper om = new ObjectMapper();
	    	String errorResponse = e.getResponseBodyAsString();
	    	statusCode = e.getRawStatusCode();
	    	message = e.getMessage();
	    	
	    	OWMGenericReponseDto genericResponse = om.readValue(errorResponse, OWMGenericReponseDto.class);
	    	response = new GetWeatherResponseDto(statusCode, genericResponse.getMessage());
	    } catch (Exception e) {
	    	statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
	    	message = WeatherLogConstants.unexpectedErrorMessage;
	    	
	    	response = new GetWeatherResponseDto(statusCode, message);
	    }
	    
	    return response;
	}
	
	private String parseAllWeatherDescription(List<OWMWeatherDto> weatherList) {
		StringBuilder sb = new StringBuilder();
		for (OWMWeatherDto weather : weatherList) {
			sb.append(weather.getDescription());
			sb.append(" ");
		}
		return sb.toString();
	}

	@Override
	public GetLatestWeatherLogResponseDto getLatestWeatherLogs() {
		Integer statusCode = HttpStatus.OK.value();
		String message = WeatherLogConstants.retrieveSuccessMessage;
		List<WeatherLog> weatherLogList = weatherLogRepository.findTop5ByOrderByDtimeInsertedDesc();
		List<WeatherLogDto> weatherLogDtoList = new ArrayList<WeatherLogDto>();
		
		for (WeatherLog weatherLog : weatherLogList) {
			WeatherLogDto weatherLogDto = WeatherLogDto.builder()
					.responseId(weatherLog.getResponseId())
					.location(weatherLog.getLocation())
					.actualWeather(weatherLog.getActualWeather())
					.temperature(weatherLog.getTemperature())
					.build();
			weatherLogDtoList.add(weatherLogDto);
		}
		
		return new GetLatestWeatherLogResponseDto(statusCode, message, weatherLogDtoList);
	}

}
