package com.ivan.landroutingservice.config;

import com.ivan.landroutingservice.services.strategy.RouteSearchAlgorithm;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "routing")
public record RoutingProperties(String countriesUrl, RouteSearchAlgorithm algorithm) {

    public RoutingProperties {
        if (countriesUrl == null || countriesUrl.isBlank()) {
            countriesUrl = "https://raw.githubusercontent.com/mledoze/countries/master/countries.json";
        }
        if (algorithm == null) {
            algorithm = RouteSearchAlgorithm.BFS;
        }
    }
}
