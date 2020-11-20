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
package com.homecredit.weather;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.homecredit.weather.dto.GetLatestWeatherLogResponseDto;
import com.homecredit.weather.dto.GetWeatherResponseDto;

/**
 * @author allenbautista
 *
 */

public class WeatherApplicationTest extends AbstractTest {
	
	@Override
	@Before
	public void setUp() {
	      super.setUp();
	}

	@Test
	public void getWeatherAndSaveSuccessTest() throws Exception {
		   String uri = "/weather";
		   MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
			  .param("q", "Quezon")
		      .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
		   
		   int status = mvcResult.getResponse().getStatus();
		   assertEquals(200, status);
		   String content = mvcResult.getResponse().getContentAsString();
		   GetWeatherResponseDto response = super.mapFromJson(content, GetWeatherResponseDto.class);
		   assertTrue(response.getLocation().equals("Quezon"));
	}
	
	@Test
	public void getWeatherAndSaveNoCityFoundTest() throws Exception {
		   String uri = "/weather";
		   MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
			  .param("q", "Qwertyasdf")
		      .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
		   
		   int status = mvcResult.getResponse().getStatus();
		   assertEquals(404, status);
	}
	
	@Test
	public void getWeatherListTest() throws Exception {
		   String uri = "/weather/getlatest";
		   MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
		      .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
		   
		   int status = mvcResult.getResponse().getStatus();
		   assertEquals(200, status);
		   String content = mvcResult.getResponse().getContentAsString();
		   GetLatestWeatherLogResponseDto response = super.mapFromJson(content, GetLatestWeatherLogResponseDto.class);
		   assertTrue(response.getWeatherList().size() <= 5);
	}
}
