package com.ivan.landroutingservice.controllers;

import com.ivan.landroutingservice.dto.RouteResponse;
import com.ivan.landroutingservice.services.RouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RoutingController {

    private final RouteService routeService;

    @GetMapping("/routing/{origin}/{destination}")
    public RouteResponse findRoute(@PathVariable String origin, @PathVariable String destination) {
        return new RouteResponse(routeService.findRoute(origin, destination));
    }
}
