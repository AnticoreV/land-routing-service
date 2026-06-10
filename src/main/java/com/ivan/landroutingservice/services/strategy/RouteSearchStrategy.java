package com.ivan.landroutingservice.services.strategy;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface RouteSearchStrategy {

    RouteSearchAlgorithm algorithm();

    Optional<List<String>> findRoute(Map<String, Set<String>> graph, String origin, String destination);
}
