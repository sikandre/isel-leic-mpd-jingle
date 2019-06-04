package org.isel.jingle.Controller;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.isel.jingle.JingleService;
import org.isel.jingle.LastfmWebApi;
import org.isel.jingle.View.ArtistView;
import org.isel.jingle.model.Artist;
import org.isel.jingle.util.BaseRequestAsync;

public class ArtistListController implements AutoCloseable{
    final ArtistView view = new ArtistView();
    private Router router;

    public ArtistListController(Router router) {
        this.router=router;
        router.route("/artists").handler(this::artistHandler);
    }

    private void artistHandler(RoutingContext ctx) {
        JingleService jingleService = new JingleService();
        String name = ctx.request().getParam("name");
        Observable<Artist> artists = jingleService.searchArtist(name);
        Artist artist = artists.firstElement().blockingGet();
        view.write(ctx.response(),artist);
        new AlbumListController(router,artist.getMbid());
        ctx.response().end();
        /*
        HttpServerResponse response = ctx.response();
        response.putHeader("content-type", "text/html");
        response.end("artist");
        */
    }
    
    @Override
    public void close() throws Exception {
    
    }
}
