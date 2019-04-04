package org.isel.jingle.dto;

public class SearchDto {
<<<<<<< HEAD

    private final ArtistMatchesDto artistMatchesDto;

    public ArtistMatchesDto getArtistMatchesDto() {
        return artistMatchesDto;
    }

    public SearchDto(ArtistMatchesDto artistMatchesDto) {
        this.artistMatchesDto = artistMatchesDto;
=======
    private ResultArtistDto results;
    private ResultAlbumsDto topalbums;

    public SearchDto(ResultArtistDto results) {
        this.results = results;
    }

    public SearchDto(ResultAlbumsDto topalbuns) {
        this.topalbums = topalbuns;
    }

    public ResultArtistDto getResults() {
        return results;
    }

    public ResultAlbumsDto getTopalbums() {
        return topalbums;
>>>>>>> master
    }
}
