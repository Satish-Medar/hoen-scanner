package com.skyscanner;

import com.fasterxml.jackson.core.type.TypeReference;
import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class HoenScannerApplication extends Application<HoenScannerConfiguration> {

    public static void main(final String[] args) throws Exception {
        new HoenScannerApplication().run(args);
    }

    @Override
    public String getName() {
        return "hoen-scanner";
    }

    @Override
    public void initialize(final Bootstrap<HoenScannerConfiguration> bootstrap) {

    }

    @Override
    public void run(final HoenScannerConfiguration configuration, final Environment environment) {
        try {
            TypeReference<List<SearchResult>> listType = new TypeReference<>() {};

            InputStream hotelsStream = getClass().getClassLoader().getResourceAsStream("hotels.json");
            InputStream rentalCarsStream = getClass().getClassLoader().getResourceAsStream("rental_cars.json");

            if (hotelsStream == null || rentalCarsStream == null) {
                throw new IllegalStateException("Could not load search result resources.");
            }

            List<SearchResult> hotels = environment.getObjectMapper().readValue(hotelsStream, listType);
            hotels.forEach(result -> result.setKind("hotel"));

            List<SearchResult> rentalCars = environment.getObjectMapper().readValue(rentalCarsStream, listType);
            rentalCars.forEach(result -> result.setKind("rental_car"));

            List<SearchResult> searchResults = new ArrayList<>();
            searchResults.addAll(hotels);
            searchResults.addAll(rentalCars);

            environment.jersey().register(new SearchResource(searchResults));
        } catch (Exception exception) {
            throw new RuntimeException("Failed to initialize search results.", exception);
        }
    }

}
