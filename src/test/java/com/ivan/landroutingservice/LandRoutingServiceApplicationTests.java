package com.ivan.landroutingservice;

import com.ivan.landroutingservice.providers.CountryGraphProvider;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.util.Map;
import java.util.Set;

@SpringBootTest
class LandRoutingServiceApplicationTests {

    @Test
    void contextLoads() {
    }

    @TestConfiguration
    static class TestConfig {

        @Bean
        @Primary
        CountryGraphProvider countryGraphProvider() {
            return () -> Map.of(
                    "CZE", Set.of("AUT"),
                    "AUT", Set.of("CZE", "ITA"),
                    "ITA", Set.of("AUT")
            );
        }
    }
}
