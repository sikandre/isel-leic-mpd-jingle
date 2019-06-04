package org.isel.jingle.Controller;

import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.isel.jingle.View.ArtistView;

public class ArtistListController {
    final ArtistView view = new ArtistView();

    public ArtistListController(Router router) {
        router.route("/artists").handler(this::artistHandler);
    }

    private void artistHandler(RoutingContext ctx) {
        HttpServerResponse response = ctx.response();
        response.putHeader("content-type", "text/html");
        response.end("artist");
    }
}
