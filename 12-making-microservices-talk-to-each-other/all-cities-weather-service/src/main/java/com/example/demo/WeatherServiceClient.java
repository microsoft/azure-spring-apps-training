/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */

package com.example.demo;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient("weather-service")
public interface WeatherServiceClient {

    @GetMapping("/weather/city")
    Weather getWeatherForCity(@RequestParam("name") String cityName);
}
