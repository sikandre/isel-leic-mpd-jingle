package org.isel.jingle.View;

import htmlflow.HtmlView;
import htmlflow.StaticHtml;
import io.vertx.core.http.HttpServerResponse;
import org.isel.jingle.model.Artist;
import org.xmlet.htmlapifaster.Body;
import org.xmlet.htmlapifaster.EnumTypeInputType;
import org.xmlet.htmlapifaster.Html;
import org.xmlet.htmlapifaster.Div;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class ArtistView implements View<Artist> {
    @Override
    public void write(HttpServerResponse resp, Artist model) {
        resp.setChunked(true);
        resp.putHeader("Content-Type","text/html");
        //Tbody<Table<Body<Html<HtmlView>>>> tbody;
        Div<Body<Html<HtmlView>>> tbody;
        tbody = writeHeader(resp, model);
        tbody
            .__()//tbody
            .__()//table
            .__()//body
            .__();//html
        resp.end();
    }

    @Override
    public void write(HttpServerResponse resp) {
        throw new UnsupportedOperationException("This view requires a Model. You should invoke write(resp, model) instead!");
    }
    
    private static Div<Body<Html<HtmlView>>> writeHeader(HttpServerResponse resp, Artist artist) {
        return StaticHtml.view(new ResponsePrintStream(resp))
                .html()
                    .head()
                        .title()
                            .text(artist.getName())
                        .__()//title
                    .__()//head
                    .body()
                        .h1()
                            .text("")
                        .__()//h1
                        .div()
                            .p().text("Name: " + artist.getName()).__()
                            .p().text("Listeners: " + artist.getListeners()).__()
                            .p().text("Mbid: " + artist.getMbid()).__()
                            .p().text("URL: " + artist.getUrl()).__()
                            .p().text("Image: " + artist.getImage()).__()
                            .p().text("To check Albuns: /artists/" + artist.getMbid() + "/albuns" ).__()
                            .p().text("To check Tracks: /artists/" + artist.getMbid() + "/tracks").__()
                        .__()
                        .div();
    }
    
    private static class ResponsePrintStream extends PrintStream {
        /**
         * We may improve this with a Buffer.
         * For now we just want to see the effect of sending
         * char by char to the browser !!!
         */
        public ResponsePrintStream(HttpServerResponse resp) {
            super(new OutputStream() {
                @Override
                public void write(int b) throws IOException {
                    char c = (char) b;
                    resp.write(String.valueOf(c));
                }
            });
        }
    }
}
