package org.isel.jingle.View;

import htmlflow.StaticHtml;
import io.vertx.core.http.HttpServerResponse;
import org.xmlet.htmlapifaster.EnumTypeInputType;
import org.xmlet.htmlapifaster.EnumTypeSimpleContentType;

public class IndexView implements View{
    @Override
    public void write(HttpServerResponse resp, Object model) {
        throw new UnsupportedOperationException("This view does not require a Model. You should invoke write(resp) instead!");
    }

    @Override
    public void write(HttpServerResponse resp) {
        String html = StaticHtml
                .view()
                .html()
                .head()
                .title().text("HtmlFlow").__()
                .__() //head
                .body()
                .a()
                    .attrHref("/artists")
                    .text("artist")
                .__()
                .div().attrClass("container")
                .h1().text("MPD Index").__()
                .img().attrSrc("https://avatars0.githubusercontent.com/u/1398561?s=200&v=4").__()
                .__()
                .__() //body
                .__() //html
                .render();

        resp.putHeader("content-type", "text/html");
        // write to the response and end it
        resp.end(html);

    }
}
