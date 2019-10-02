/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */

package com.example.demo;

import com.azure.data.cosmos.CosmosClient;
import com.azure.data.cosmos.CosmosContainer;
import com.azure.data.cosmos.FeedOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@RestController
public class CityController {

    @Value("${azure.cosmosdb.uri}")
    private String cosmosDbUrl;

    @Value("${azure.cosmosdb.key}")
    private String cosmosDbKey;

    @Value("${azure.cosmosdb.database}")
    private String cosmosDbDatabase;

    private CosmosContainer container;

    @PostConstruct
    public void init() {
        container = CosmosClient.builder()
                .endpoint(cosmosDbUrl)
                .key(cosmosDbKey)
                .build()
                .getDatabase(cosmosDbDatabase)
                .getContainer("City");
    }

    @GetMapping("/cities")
    public Flux<List<City>> getCities() {
        FeedOptions options = new FeedOptions();
        options.enableCrossPartitionQuery(true);
        return container.queryItems("SELECT TOP 20 * FROM City c", options)
                .map(i -> {
                    List<City> results = new ArrayList<>();
                    i.results().forEach(props -> {
                        City city = new City();
                        city.setName(props.getString("name"));
                        results.add(city);
                    });
                    return results;
                });
    }
}


