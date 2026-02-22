package com.skyscanner;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;
import java.util.stream.Collectors;

@Path("/search")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SearchResource {
    private final List<SearchResult> searchResults;

    public SearchResource(List<SearchResult> searchResults) {
        this.searchResults = searchResults;
    }

    @POST
    public List<SearchResult> search(final Search search) {
        if (search == null || search.getCity() == null) {
            return List.of();
        }

        final String city = search.getCity().trim();
        if (city.isEmpty()) {
            return List.of();
        }

        return searchResults.stream()
                .filter(result -> result.getCity() != null && result.getCity().equalsIgnoreCase(city))
                .collect(Collectors.toList());
    }
}
