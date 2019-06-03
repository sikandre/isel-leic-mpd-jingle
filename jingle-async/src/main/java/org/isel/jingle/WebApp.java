package org.isel.jingle;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import org.isel.jingle.Controller.AlbumListController;
import org.isel.jingle.Controller.AlbumTracksListController;
import org.isel.jingle.Controller.ArtistListController;
import org.isel.jingle.Controller.ArtistTracksListController;
import org.isel.jingle.model.Artist;

public class WebApp {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();

        HttpServer server = vertx.createHttpServer();
        Router router = Router.router(vertx);
        new ArtistListController(router);
        new AlbumListController(router);
        new ArtistTracksListController(router);
        new AlbumTracksListController(router);

        server.requestHandler(router).listen(3000);
    }
}
