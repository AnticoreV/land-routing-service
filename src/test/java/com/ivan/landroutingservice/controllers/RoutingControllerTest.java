package com.ivan.landroutingservice.controllers;

import com.ivan.landroutingservice.exceptions.GlobalExceptionHandler;
import com.ivan.landroutingservice.providers.CountryGraphProvider;
import com.ivan.landroutingservice.services.RouteService;
import com.ivan.landroutingservice.services.strategy.BfsRouteSearchStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Map;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RoutingControllerTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        CountryGraphProvider countryGraphProvider = () -> Map.of(
                "CZE", Set.of("AUT"),
                "AUT", Set.of("CZE", "ITA"),
                "ITA", Set.of("AUT"),
                "PRT", Set.of("ESP"),
                "ESP", Set.of("PRT")
        );

        RouteService routeService = new RouteService(countryGraphProvider, new BfsRouteSearchStrategy());
        mockMvc = MockMvcBuilders.standaloneSetup(new RoutingController(routeService))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void returnsRouteForReachableCountries() throws Exception {
        mockMvc.perform(get("/routing/CZE/ITA"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                          "route": ["CZE", "AUT", "ITA"]
                        }
                        """));
    }

    @Test
    void returnsBadRequestWhenCountriesAreNotConnected() throws Exception {
        mockMvc.perform(get("/routing/PRT/ITA"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("""
                        {
                          "status": 400,
                          "error": "Bad Request",
                          "message": "No land route found",
                          "path": "/routing/PRT/ITA"
                        }
                        """));
    }
}
