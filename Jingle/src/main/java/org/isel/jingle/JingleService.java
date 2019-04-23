
package org.isel.jingle;

import org.isel.jingle.dto.AlbumDto;
import org.isel.jingle.dto.ArtistDto;
import org.isel.jingle.dto.TrackDto;
import org.isel.jingle.dto.TrackRankDto;
import org.isel.jingle.model.Album;
import org.isel.jingle.model.Artist;
import org.isel.jingle.model.Track;
import org.isel.jingle.model.TrackRank;
import org.isel.jingle.util.BaseRequest;
import org.isel.jingle.util.HttpRequest;
import org.isel.jingle.util.StreamUtils;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
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
        return id.map(this::getAlbumTracks).flatMap(Function.identity());
    }

    public Stream<TrackRank> getTopTracks(String country){
        Stream<Integer> pageNr = Stream.iterate(1, n -> n+1);
        Stream<TrackRankDto[]> map = pageNr.map(n -> api.getTopTracks(country, n))
                .takeWhile(arr -> arr.length!=0);
        Stream<TrackRankDto> dto = map.flatMap(Stream::of);
        AtomicInteger rankNumber = new AtomicInteger(1);
        return dto.map(e -> createTrackRank(e, rankNumber));
    }

    public Stream<TrackRank> getTracksRank(String artistMbId, String country){
        Stream<Track> artistTracks = getTracks(artistMbId);
        Stream<TrackRank> countryTracks = getTopTracks(country).limit(100);
        return StreamUtils.merge(
                artistTracks,
                countryTracks,
                (a,c) -> a.getName().equals(c.getName()),
                (a,c) -> new TrackRank(a.getName(),a.getUrl(),a.getDuration(),c.getRank()),
                new TrackRank("","",0,0)).get();
    }

    private TrackRank createTrackRank(TrackRankDto dto, AtomicInteger nr) {
        return new TrackRank(
                dto.getName(),
                dto.getUrl(),
                dto.getDuration(),
                nr.getAndIncrement()
        );
    }

    private Artist createArtist(ArtistDto dto) {
        Function<String, Stream<TrackRank>> tr = c -> getTracksRank(dto.getMbid(), c);
        return new Artist(
                dto.getName(),
                dto.getListeners(),
                dto.getMbid(),
                dto.getUrl(),
                dto.getImage()[0].getText(),
                () -> getAlbums(dto.getMbid()),
                () -> getTracks(dto.getMbid()),
                tr
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
