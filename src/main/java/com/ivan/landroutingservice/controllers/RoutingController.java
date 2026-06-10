package com.ivan.landroutingservice.controllers;

import com.ivan.landroutingservice.api.RoutingApi;
import com.ivan.landroutingservice.api.model.RouteResponse;
import com.ivan.landroutingservice.services.RouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RoutingController implements RoutingApi {

    private final RouteService routeService;

    @Override
    public ResponseEntity<RouteResponse> findRoute(String origin, String destination) {
        return ResponseEntity.ok(new RouteResponse().route(routeService.findRoute(origin, destination)));
    }
}
