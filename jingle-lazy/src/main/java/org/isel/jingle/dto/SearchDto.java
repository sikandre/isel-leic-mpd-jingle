package org.isel.jingle.dto;

public class SearchDto {
    private final ResultsDto results;

    public SearchDto(ResultsDto results) {
        this.results = results;
    }

    public ResultsDto getResults() {
        return results;
    }
}
