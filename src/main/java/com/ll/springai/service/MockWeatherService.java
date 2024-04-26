/*
 * Copyright 2023 - 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ll.springai.service;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

import java.util.function.Function;

/**
 * Mock 3rd party weather service.
 *
 * @author Christian Tzolov
 */
public class MockWeatherService implements Function<MockWeatherService.Request, MockWeatherService.Response> {

	/**
	 * Weather Function request.
	 */
	@JsonInclude(Include.NON_NULL)
	@JsonClassDescription("城市天气查询")
	public record Request(@JsonProperty(required = true,
			value = "location") @JsonPropertyDescription("城市名，如:北京") String location,
			@JsonProperty(required = true, value = "lat") @JsonPropertyDescription("城市经度") double lat,
			@JsonProperty(required = true, value = "lon") @JsonPropertyDescription("城市纬度") double lon,
			@JsonProperty(required = true, value = "unit") @JsonPropertyDescription("温度单位") Unit unit) {
	}

	/**
	 * Temperature units.
	 */
	public enum Unit {

		/**
		 * Celsius.
		 */
		C("metric"),
		/**
		 * Fahrenheit.
		 */
		F("imperial");

		/**
		 * Human readable unit name.
		 */
		public final String unitName;

		private Unit(String text) {
			this.unitName = text;
		}

	}

	/**
	 * Weather Function response.
	 */
	public record Response(double temp, double feels_like, double temp_min, double temp_max, int pressure, int humidity,
			Unit unit) {
	}

	@Override
	public Response apply(Request request) {
		double temperature = 0;
		if (request.location().contains("太原")) {
			temperature = 29;
		} else if (request.location().contains("北京")) {
			temperature = 28;
		} else if (request.location().contains("西安")) {
			temperature = 32;
		}

		return new Response(temperature, 15, 20, 2, 53, 45, Unit.C);
	}

}