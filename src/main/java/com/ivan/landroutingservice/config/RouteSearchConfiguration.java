package com.ivan.landroutingservice.config;

import com.ivan.landroutingservice.services.strategy.BfsRouteSearchStrategy;
import com.ivan.landroutingservice.services.strategy.DfsRouteSearchStrategy;
import com.ivan.landroutingservice.services.strategy.RouteSearchAlgorithm;
import com.ivan.landroutingservice.services.strategy.RouteSearchStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Configuration
public class RouteSearchConfiguration {

    @Bean
    BfsRouteSearchStrategy bfsRouteSearchStrategy() {
        return new BfsRouteSearchStrategy();
    }

    @Bean
    DfsRouteSearchStrategy dfsRouteSearchStrategy() {
        return new DfsRouteSearchStrategy();
    }

    @Bean
    @Primary
    RouteSearchStrategy routeSearchStrategy(
            BfsRouteSearchStrategy bfsRouteSearchStrategy,
            DfsRouteSearchStrategy dfsRouteSearchStrategy,
            RoutingProperties routingProperties
    ) {
        Map<RouteSearchAlgorithm, RouteSearchStrategy> strategiesByAlgorithm = new EnumMap<>(RouteSearchAlgorithm.class);
        List.of(bfsRouteSearchStrategy, dfsRouteSearchStrategy)
                .forEach(strategy -> strategiesByAlgorithm.put(strategy.algorithm(), strategy));

        RouteSearchStrategy strategy = strategiesByAlgorithm.get(routingProperties.algorithm());
        if (strategy == null) {
            throw new IllegalStateException("No route search strategy configured for " + routingProperties.algorithm());
        }

        return strategy;
    }
}
