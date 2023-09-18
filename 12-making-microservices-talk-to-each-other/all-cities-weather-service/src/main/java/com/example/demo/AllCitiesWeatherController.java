/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */

package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@AllArgsConstructor
@Slf4j
public class AllCitiesWeatherController {

    private final CityServiceClient cityServiceClient;
    private final WeatherServiceClient weatherServiceClient;

    @GetMapping("/")
    public List<Weather> getAllCitiesWeather() {
        Stream<City> allCities = cityServiceClient.getAllCities().stream().flatMap(Collection::stream);

        //Obtain weather for all cities in parallel
       return allCities.parallel()
                .peek(city -> log.info("City: >>" + city.getName() + "<<"))
                .map(city -> weatherServiceClient.getWeatherForCity(city.getName()))
                .collect(Collectors.toList());
    }
}
