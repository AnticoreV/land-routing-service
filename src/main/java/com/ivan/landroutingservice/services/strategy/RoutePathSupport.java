package com.ivan.landroutingservice.services.strategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

final class RoutePathSupport {

    private RoutePathSupport() {
    }

    static List<String> buildPath(Map<String, String> previousCountry, String origin, String destination) {
        List<String> route = new ArrayList<>();
        String current = destination;
        route.add(current);

        while (!current.equals(origin)) {
            current = previousCountry.get(current);
            route.add(current);
        }

        Collections.reverse(route);
        return List.copyOf(route);
    }
}
