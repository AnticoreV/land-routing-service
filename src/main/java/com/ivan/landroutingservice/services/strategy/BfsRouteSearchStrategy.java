package com.ivan.landroutingservice.services.strategy;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class BfsRouteSearchStrategy implements RouteSearchStrategy {

    @Override
    public RouteSearchAlgorithm algorithm() {
        return RouteSearchAlgorithm.BFS;
    }

    @Override
    public Optional<List<String>> findRoute(Map<String, Set<String>> graph, String origin, String destination) {
        Deque<String> queue = new ArrayDeque<>();
        Set<String> visited = new HashSet<>();
        Map<String, String> previousCountry = new HashMap<>();

        queue.add(origin);
        visited.add(origin);

        while (!queue.isEmpty()) {
            String current = queue.remove();

            for (String border : graph.getOrDefault(current, Set.of())) {
                if (!visited.add(border)) {
                    continue;
                }

                previousCountry.put(border, current);
                if (border.equals(destination)) {
                    return Optional.of(RoutePathSupport.buildPath(previousCountry, origin, destination));
                }
                queue.add(border);
            }
        }

        return Optional.empty();
    }
}
