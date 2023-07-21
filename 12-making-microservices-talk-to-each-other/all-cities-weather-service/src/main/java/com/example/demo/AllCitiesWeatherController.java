/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */

package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
public class AllCitiesWeatherController {

    private CityServiceClient cityServiceClient;
    private WeatherServiceClient weatherServiceClient;

    public AllCitiesWeatherController(CityServiceClient cityServiceClient, WeatherServiceClient weatherServiceClient) {
        this.cityServiceClient = cityServiceClient;
        this.weatherServiceClient = weatherServiceClient;
    }

    @GetMapping("/")
    public List<Weather> getAllCitiesWeather() {
        Stream<City> allCities = cityServiceClient.getAllCities().stream().flatMap(list -> list.stream());

        //Obtain weather for all cities in parallel
        List<Weather> allCitiesWeather = allCities.parallel()
                .peek(city -> System.out.println("City: >>" + city.getName() + "<<"))
                .map(city -> weatherServiceClient.getWeatherForCity(city.getName()))
                .collect(Collectors.toList());

        return allCitiesWeather;
    }
}
