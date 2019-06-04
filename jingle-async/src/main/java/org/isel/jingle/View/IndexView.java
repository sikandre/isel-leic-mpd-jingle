package org.isel.jingle.View;

import htmlflow.StaticHtml;
import io.vertx.core.http.HttpServerResponse;

public class IndexView implements View{
    @Override
    public void write(HttpServerResponse resp, Object model) {
        throw new UnsupportedOperationException("This view does not require a Model. You should invoke write(resp) instead!");
    }

    @Override
    public void write(HttpServerResponse resp) {
        /*String html = StaticHtml.view()
                .html()
                    .body()
                        .h1()
                            .input()
                        .__() // h1
                    .__() // body
                .__() // html
                .render();

        resp.putHeader("content-type", "text/html");
        // write to the response and end it
        resp.end(html);*/

    }
}
