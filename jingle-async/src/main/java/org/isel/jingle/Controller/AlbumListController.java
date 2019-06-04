package org.isel.jingle.Controller;

import io.reactivex.Observable;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.isel.jingle.View.AlbumView;
import org.isel.jingle.model.Album;
import org.isel.jingle.model.Artist;

public class AlbumListController {
    
    final AlbumView view = new AlbumView();
    private Artist artist;
    public AlbumListController(Router router, String pid) {
        router.route("artists/" + pid + "/albuns ").handler(this::albumListHandler);
    }
    
    public void albumListHandler(RoutingContext ctx) {
        Observable<Album> albuns = artist.getAlbums();
        view.write(ctx.response(),albuns);
        
        
    }
}
