
package org.isel.jingle;

import org.isel.jingle.dto.AlbumDto;
import org.isel.jingle.dto.ArtistDto;
import org.isel.jingle.dto.TrackDto;
import org.isel.jingle.model.Album;
import org.isel.jingle.model.Artist;
import org.isel.jingle.model.Track;
import org.isel.jingle.util.BaseRequest;
import org.isel.jingle.util.HttpRequest;

import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

public class JingleService {

    final LastfmWebApi api;

    public JingleService(LastfmWebApi api) {
        this.api = api;
    }

    public JingleService() {
        this(new LastfmWebApi(new BaseRequest(HttpRequest::openStream)));
    }

    public Stream<Artist> searchArtist(String name) {
        Stream<Integer> pageNr = Stream.iterate(1, n -> n + 1);
        Stream<ArtistDto[]> map = pageNr.map(n -> api.searchArtist(name, n));
        map = map.takeWhile(arr -> arr.length != 0);
        Stream<ArtistDto> dto = map.flatMap(Stream::of);
        return dto.map(this::createArtist);
    }

    public Stream<Album> getAlbums(String artistMbid) {
        Stream<Integer> pageNr = Stream.iterate(1, n -> n + 1);
        Stream<AlbumDto[]> map = pageNr.map(n -> api.getAlbums(artistMbid, n));
        map = map.takeWhile(arr -> arr.length != 0);
        Stream<AlbumDto> dto = map.flatMap(Stream::of);
        return dto.map(this::createAlbums);
    }

    private Stream<Track> getAlbumTracks(String albumMbid) {
        Stream<TrackDto> dto = Stream.of(api.getAlbumInfo(albumMbid));
        return dto.map(this::createTrack);
    }

    private Stream<Track> getTracks(String artistMbid) {//suplier
        Stream<String> id = getAlbums(artistMbid).map(Album::getMbid)
                .filter(Objects::nonNull);
        Stream<Stream<Track>> st = id.map(this::getAlbumTracks);
        Stream<Track> s = st.flatMap(Function.identity());  //s -> s
        return s;
    }

    private Artist createArtist(ArtistDto dto) {
        return new Artist(
                dto.getName(),
                dto.getListeners(),
                dto.getMbid(),
                dto.getUrl(),
                dto.getImage()[0].getText(),
                () -> getAlbums(dto.getMbid()),
                () -> getTracks(dto.getMbid())
        );
    }

    private Album createAlbums(AlbumDto dto) {
        return new Album(
                dto.getName(),
                dto.getPlaycount(),
                dto.getMbid(),
                dto.getUrl(),
                dto.getImage()[0].getText(),
                () -> getAlbumTracks(dto.getMbid())
        );
    }

    private Track createTrack(TrackDto dto) {
        return new Track(
                dto.getName(),
                dto.getUrl(),
                dto.getDuration());
    }
}
