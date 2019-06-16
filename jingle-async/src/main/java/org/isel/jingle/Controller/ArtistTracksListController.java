package org.isel.jingle.Controller;

import io.vertx.ext.web.Router;

public class ArtistTracksListController {

    public ArtistTracksListController(Router router) {
        router.route("/artists/:id/tracks");
    }
}
