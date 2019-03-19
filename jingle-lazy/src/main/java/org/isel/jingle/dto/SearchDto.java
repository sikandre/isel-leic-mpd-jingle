package org.isel.jingle.dto;

public class SearchDto {

    private final ArtistMatchesDto artistMatchesDto;

    public ArtistMatchesDto getArtistMatchesDto() {
        return artistMatchesDto;
    }

    public SearchDto(ArtistMatchesDto artistMatchesDto) {
        this.artistMatchesDto = artistMatchesDto;
    }
}
