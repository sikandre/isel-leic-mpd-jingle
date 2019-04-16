
package org.isel.jingle;

import com.google.gson.Gson;
import org.isel.jingle.dto.*;
import org.isel.jingle.util.Request;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LastfmWebApi {
    private static final String LASTFM_API_KEY = "55394a24c02f82f0b62712b219374964";
    private static final String LASTFM_HOST = "http://ws.audioscrobbler.com/2.0/";
    private static final String LASTFM_SEARCH = LASTFM_HOST
                                                    + "?method=artist.search&format=json&artist=%s&page=%d&api_key="
                                                    + LASTFM_API_KEY;

    private static final String LASTFM_GET_ALBUMS = LASTFM_HOST
                                                    + "?method=artist.gettopalbums&format=json&mbid=%s&page=%d&api_key="
                                                    + LASTFM_API_KEY;

    private static final String LASTFM_GET_ALBUM_INFO = LASTFM_HOST
                                                    + "?method=album.getinfo&format=json&mbid=%s&api_key="
                                                    + LASTFM_API_KEY;
    private final Request request;
    protected final Gson gson;

    public LastfmWebApi(Request request) {
        this(request, new Gson());
    }

    public LastfmWebApi(Request request, Gson gson) {
        this.request = request;
        this.gson = gson;
    }

    public ArtistDto[] searchArtist(String name, int page) {
        String body = getBody(LASTFM_SEARCH, name, page);
        SearchDto dto = gson.fromJson(body, SearchDto.class);
        ArtistDto[] artistDtos = dto.getResults().getArtistMatchDto().getArtist();
        return artistDtos;
    }

    public AlbumDto[] getAlbums(String artistMbid, int page) {
        String body = getBody(LASTFM_GET_ALBUMS, artistMbid, page);
        SearchDto dto = gson.fromJson(body, SearchDto.class);
        AlbumDto[] albums = dto.getTopalbums().getAlbum();
        return albums;
    }

    public TrackDto[] getAlbumInfo(String albumMbid){
        String path = String.format(LASTFM_GET_ALBUM_INFO, albumMbid);
        Stream<String> src = request.getLines(path);
        String body = src.map(Objects::toString).collect(Collectors.joining(""));
        ResultAlbumDto dto = gson.fromJson(body, ResultAlbumDto.class);
        if(dto.getAlbum()==null){
            return new TrackDto[0];
        }
        return dto.getAlbum().getTracks().getTrack();
    }

    private String getBody(String host, String name, int page) {
        String path = String.format(host, name, page);
        Stream<String> src = request.getLines(path);
        return src.map(Objects::toString).collect(Collectors.joining(""));
    }
}
