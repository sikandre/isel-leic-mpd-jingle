package org.isel.jingle.Controller;

import io.vertx.ext.web.Router;

public class AlbumTracksListController {
    public AlbumTracksListController(Router router) {
        router.route("/albums/:id/tracks");
    }
}
