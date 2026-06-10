package com.ivan.landroutingservice.services.strategy;

import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class DfsRouteSearchStrategyTest {

    private final DfsRouteSearchStrategy routeSearchStrategy = new DfsRouteSearchStrategy();

    @Test
    void returnsAnyReachableRoute() {
        assertThat(routeSearchStrategy.findRoute(testGraph(), "CZE", "ITA"))
                .isPresent()
                .hasValueSatisfying(route -> assertThat(route)
                        .startsWith("CZE")
                        .endsWith("ITA"));
    }

    @Test
    void returnsEmptyWhenDestinationIsUnreachable() {
        assertThat(routeSearchStrategy.findRoute(testGraph(), "PRT", "ITA"))
                .isEmpty();
    }

    private Map<String, Set<String>> testGraph() {
        return Map.of(
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
