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
package com.homecredit.weather.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author allenbautista
 *
 */

/* MYSQL DB autoupdate, if not we need to create a flyway for this */
@Entity
@Table(name = "m_weather_log")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeatherLog {
	
	/* Adding id here for now, but we can create an abstract class that
	 * can be extended by models so it is sure to contain id, overridden equals
	 * and hashcode */
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "response_id")
	private String responseId;
	
	@Column(name = "location")
	private String location;
	
	@Column(name = "actual_weather")
	private String actualWeather;
	
	@Column(name = "temperature")
	private String temperature;
	
	@Column(name = "dtime_inserted")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtimeInserted;

}
