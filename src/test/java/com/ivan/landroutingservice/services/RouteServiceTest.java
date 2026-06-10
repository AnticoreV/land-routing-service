package com.ivan.landroutingservice.services;

import com.ivan.landroutingservice.exceptions.RouteNotFoundException;
import com.ivan.landroutingservice.exceptions.UnknownCountryException;
import com.ivan.landroutingservice.providers.CountryGraphProvider;
import com.ivan.landroutingservice.services.strategy.BfsRouteSearchStrategy;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RouteServiceTest {

    private final RouteService routeService = new RouteService(testGraphProvider(), new BfsRouteSearchStrategy());

    @Test
    void returnsShortestRoute() {
        assertThat(routeService.findRoute("CZE", "ITA"))
                .containsExactly("CZE", "AUT", "ITA");
    }

    @Test
    void returnsSingleCountryWhenOriginEqualsDestination() {
        assertThat(routeService.findRoute("cze", "CZE"))
                .containsExactly("CZE");
    }

    @Test
    void throwsBadRequestWhenRouteDoesNotExist() {
        assertThatThrownBy(() -> routeService.findRoute("PRT", "ITA"))
                .isInstanceOf(RouteNotFoundException.class)
                .hasMessage("No land route found");
    }

    @Test
    void throwsBadRequestWhenCountryCodeIsUnknown() {
        assertThatThrownBy(() -> routeService.findRoute("XXX", "ITA"))
                .isInstanceOf(UnknownCountryException.class)
                .hasMessage("Unknown country code");
    }

    private CountryGraphProvider testGraphProvider() {
        return () -> Map.of(
                "CZE", Set.of("AUT", "DEU", "POL"),
                "AUT", Set.of("CZE", "ITA"),
                "DEU", Set.of("CZE", "POL"),
                "POL", Set.of("CZE", "DEU"),
                "ITA", Set.of("AUT"),
                "PRT", Set.of("ESP"),
                "ESP", Set.of("PRT")
        );
    }
}
