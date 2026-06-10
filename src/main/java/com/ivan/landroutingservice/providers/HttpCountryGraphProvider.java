package com.ivan.landroutingservice.providers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivan.landroutingservice.config.RoutingProperties;
import com.ivan.landroutingservice.models.CountryData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class HttpCountryGraphProvider implements CountryGraphProvider {

    private static final TypeReference<List<CountryData>> COUNTRY_LIST_TYPE = new TypeReference<>() {
    };

    private final RestClient restClient = RestClient.builder().build();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RoutingProperties routingProperties;

    private volatile Map<String, Set<String>> cachedGraph;

    @Override
    public Map<String, Set<String>> getGraph() {
        Map<String, Set<String>> graph = cachedGraph;
        if (graph != null) {
            return graph;
        }

        synchronized (this) {
            if (cachedGraph == null) {
                cachedGraph = loadGraph();
            }
            return cachedGraph;
        }
    }

    private Map<String, Set<String>> loadGraph() {
        try {
            String responseBody = restClient.get()
                    .uri(routingProperties.countriesUrl())
                    .retrieve()
                    .body(String.class);

            if (responseBody == null || responseBody.isBlank()) {
                throw new IllegalStateException("Country dataset response is empty");
            }

            List<CountryData> countries = objectMapper.readValue(responseBody, COUNTRY_LIST_TYPE);
            return countries.stream()
                    .filter(country -> country.cca3() != null && !country.cca3().isBlank())
                    .collect(Collectors.toUnmodifiableMap(
                            country -> normalize(country.cca3()),
                            country -> normalizeBorders(country.borders())
                    ));
        } catch (Exception exception) {
            throw new IllegalStateException("Failed to load country data from %s".formatted(routingProperties.countriesUrl()), exception);
        }
    }

    private Set<String> normalizeBorders(List<String> borders) {
        if (borders == null || borders.isEmpty()) {
            return Set.of();
        }

        return borders.stream()
                .filter(border -> border != null && !border.isBlank())
                .map(this::normalize)
                .collect(Collectors.toUnmodifiableSet());
    }

    private String normalize(String cca3) {
        return cca3.trim().toUpperCase(Locale.ROOT);
    }
}
