package org.isel.jingle.Controller;

import io.reactivex.Observable;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.isel.jingle.JingleService;
import org.isel.jingle.View.ArtistView;
import org.isel.jingle.model.Artist;

public class ArtistListController {
    final ArtistView view = new ArtistView();
    private Router router;

    public ArtistListController(Router router) {
        this.router=router;
        router.route("/artists").handler(this::artistHandler);
    }

    private void artistHandler(RoutingContext ctx) {
        JingleService jingleService = new JingleService();
        String name = ctx.request().getParam("name");
        int page = Integer.parseInt(ctx.request().getParam("page"));
        int pageSize = 25;
        Observable<Artist> artists = jingleService.searchArtist(name)
                .skip(pageSize*(page-1))
                .take(pageSize);
        view.write(ctx.response(),artists,name, page);
        ctx.response().end();
    }
}
