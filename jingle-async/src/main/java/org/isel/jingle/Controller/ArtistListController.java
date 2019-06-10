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
        Observable<Artist> artists = jingleService.searchArtist(name).take(100);
        view.write(ctx.response(),artists);
        ctx.response().end();
    }
    
    @Override
    public void close() throws Exception {
    
    }
}
