package com.example.demo;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AllCitiesWeatherController.class)
public class AllCitiesWeatherControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private CityServiceClient cityServiceClient;
    @MockBean
    private WeatherServiceClient weatherServiceClient;

    @Test
    public void test_weather() throws Exception {
        Mockito.when(weatherServiceClient.getWeatherForCity(Mockito.any())).thenReturn(new Weather("Pune", "City in india", ""));
        Mockito.when(cityServiceClient.getAllCities()).thenReturn(List.of(List.of(new City("Pune"))));
        mvc.perform(MockMvcRequestBuilders
                        .get("/")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].city").value("Pune"));
    }
}
