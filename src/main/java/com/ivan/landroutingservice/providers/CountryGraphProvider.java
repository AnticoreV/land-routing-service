package com.ivan.landroutingservice.providers;

import java.util.Map;
import java.util.Set;

public interface CountryGraphProvider {

    Map<String, Set<String>> getGraph();
}
