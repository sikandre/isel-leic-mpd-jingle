

package org.isel.jingle;

import org.isel.jingle.dto.*;
import org.isel.jingle.util.BaseRequest;
import org.isel.jingle.util.HttpRequest;
import org.isel.jingle.util.Request;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;


public class LastfmWebApiTest {
    @Test
    public void searchForArtistsNamedDavid(){
        Request req = new BaseRequest(HttpRequest::openStream);
        LastfmWebApi api = new LastfmWebApi(req);
        ArtistDto[] artists = api.searchArtist("david", 1);
        String name = artists[0].getName();
        assertEquals("David Bowie", name);
    }

    @Test
    public void getTopAlbumsFromMuse(){
        Request req = new BaseRequest(HttpRequest::openStream);
        LastfmWebApi api = new LastfmWebApi(req);
        ArtistDto[] artists = api.searchArtist("muse", 1);
        String mbid = artists[0].getMbid();
        AlbumDto[] albums = api.getAlbums(mbid, 1);
        assertEquals("Black Holes and Revelations", albums[0].getName());
    }

    @Test
    public void getStarlightFromBlackHolesAlbumOfMuse(){
        Request req = new BaseRequest(HttpRequest::openStream);
        LastfmWebApi api = new LastfmWebApi(req);
        ArtistDto[] artists = api.searchArtist("muse", 1);
        String mbid = artists[0].getMbid();
        AlbumDto album = api.getAlbums(mbid, 1)[0];
        TrackDto track = api.getAlbumInfo(album.getMbid())[1];
        assertEquals("Starlight", track.getName());
    }

    @Test
    public void getTopTracksFromPortugal(){
        Request req = new BaseRequest(HttpRequest::openStream);
        LastfmWebApi api = new LastfmWebApi(req);
        TrackRankDto[] topTracks = api.getTopTracks("Portugal", 1);
        assertEquals(50, topTracks.length);
    }
}
