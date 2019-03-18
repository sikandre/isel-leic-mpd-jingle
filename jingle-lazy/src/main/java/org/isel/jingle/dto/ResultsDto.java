package org.isel.jingle.dto;

public class ResultsDto {
    private final ArtistMatchDto artistmatches;

    public ResultsDto(ArtistMatchDto artistmatches) {
        this.artistmatches = artistmatches;
    }

    public ArtistMatchDto getArtistMatchDto() {
        return artistmatches;
    }
}
