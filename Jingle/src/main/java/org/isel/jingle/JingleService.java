/*
 * GNU General Public License v3.0
 *
 * Copyright (c) 2019, Miguel Gamboa (gamboa.pt)
 *
 *   All rights granted under this License are granted for the term of
 * copyright on the Program, and are irrevocable provided the stated
 * conditions are met.  This License explicitly affirms your unlimited
 * permission to run the unmodified Program.  The output from running a
 * covered work is covered by this License only if the output, given its
 * content, constitutes a covered work.  This License acknowledges your
 * rights of fair use or other equivalent, as provided by copyright law.
 *
 *   You may make, run and propagate covered works that you do not
 * convey, without conditions so long as your license otherwise remains
 * in force.  You may convey covered works to others for the sole purpose
 * of having them make modifications exclusively for you, or provide you
 * with facilities for running those works, provided that you comply with
 * the terms of this License in conveying all material for which you do
 * not control copyright.  Those thus making or running the covered works
 * for you must do so exclusively on your behalf, under your direction
 * and control, on terms that prohibit them from making any copies of
 * your copyrighted material outside their relationship with you.
 *
 *   Conveying under any other circumstances is permitted solely under
 * the conditions stated below.  Sublicensing is not allowed; section 10
 * makes it unnecessary.
 *
 */

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
                .filter(Objects::isNull);
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
