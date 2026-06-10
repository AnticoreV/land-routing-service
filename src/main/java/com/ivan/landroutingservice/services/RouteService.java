package com.ivan.landroutingservice.services;

import com.ivan.landroutingservice.exceptions.RouteNotFoundException;
import com.ivan.landroutingservice.exceptions.UnknownCountryException;
import com.ivan.landroutingservice.providers.CountryGraphProvider;
import com.ivan.landroutingservice.services.strategy.RouteSearchStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RouteService {

    private final CountryGraphProvider countryGraphProvider;
    private final RouteSearchStrategy routeSearchStrategy;

    public List<String> findRoute(String origin, String destination) {
        Map<String, Set<String>> graph = countryGraphProvider.getGraph();
        String normalizedOrigin = normalize(origin);
        String normalizedDestination = normalize(destination);

        if (!graph.containsKey(normalizedOrigin) || !graph.containsKey(normalizedDestination)) {
            throw new UnknownCountryException("Unknown country code");
        }

        if (normalizedOrigin.equals(normalizedDestination)) {
            return List.of(normalizedOrigin);
        }

        return routeSearchStrategy.findRoute(graph, normalizedOrigin, normalizedDestination)
                .orElseThrow(() -> new RouteNotFoundException("No land route found"));
    }

    private String normalize(String countryCode) {
        return countryCode.trim().toUpperCase(Locale.ROOT);
    }
}
