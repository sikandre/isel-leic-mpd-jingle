package org.isel.jingle.dto;

public class ArtistMatchesDto {
    private final ArtistDto artistDto;

    public ArtistDto getArtistDto() {
        return artistDto;
    }

    public ArtistMatchesDto(ArtistDto artistDto) {
        this.artistDto = artistDto;
    }
}
