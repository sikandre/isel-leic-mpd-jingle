package org.isel.jingle.View;

import htmlflow.HtmlView;
import htmlflow.StaticHtml;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.vertx.core.http.HttpServerResponse;
import org.apache.commons.lang3.StringUtils;
import org.isel.jingle.model.Artist;
import org.xmlet.htmlapifaster.*;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class ArtistView implements View<Observable<Artist>> {
    private static final int PAGESIZE = 25;
    private static final int[] count = {0};

    public void write(HttpServerResponse resp, Observable<Artist> model, String name, int page) {
        count[0] =0;
        resp.setChunked(true);
        resp.putHeader("Content-Type", "text/html");
        model.subscribeWith(new Observer<Artist>() {
            Tbody<Table<Body<Html<HtmlView>>>> tbody;
            public void onSubscribe(Disposable d) {
                tbody = writeHeader(resp, name, page);
            }

            public void onNext(Artist artist) {
                count[0]++;
                writeTableRow(tbody, artist);
            }

            public void onError(Throwable e) {
                //log error
                System.out.println(e.getMessage());
            }


            public void onComplete() {
                tbody
                        .__() // tbody
                        .__() // table

                        /*.div().style()
                        .text("a {\n" +
                        "  text-decoration: none;\n" +
                        "  display: inline-block;\n" +
                        "  padding: 8px 16px;\n" +
                        "}\n" +
                        "\n" +
                        "a:hover {\n" +
                        "  background-color: #9EA9DB;\n" +
                        "  color: black;\n" +
                        "}\n" +
                        "\n" +
                        ".previous {\n" +
                        "  background-color: #B8A99C;\n" +
                        "  color: black;\n" +
                        "}\n" +
                        ".isDisabled {\n" +
                        "pointer-events: none;"+
                        "  color: currentColor;\n" +
                        "  cursor: not-allowed;\n" +
                        "  opacity: 0.5;\n" +
                        "  text-decoration: none;\n" +
                        "}\n"+
                        "\n" +
                        ".next {\n" +
                        "  background-color: #4A578F;\n" +
                        "  color: white;\n" +
                        "}").__()*/
                        .a()
                        .attrHref("/artists?name="+name+"&page="+(page-1)).attrClass(page>1 ?"previous": "isDisabled")
                        .text("&laquo; Previous").__()
                        .a()
                        .attrHref("/artists?name="+name+"&page="+(page+1)).attrClass(count[0]==PAGESIZE ? "next" : "isDisabled")
                        .text("Next &raquo;").__()
                        .__()

                        .__() // body
                        .__();// html
                resp.end();
            }
        });
    }


    @Override
    public void write(HttpServerResponse resp, Observable<Artist> model) {
        throw new UnsupportedOperationException("This view requires a Model. You should invoke write(resp, model) instead!");
    }

    @Override
    public void write(HttpServerResponse resp) {
        throw new UnsupportedOperationException("This view requires a Model. You should invoke write(resp, model) instead!");
    }

    private static Tbody<Table<Body<Html<HtmlView>>>> writeHeader(HttpServerResponse resp, String name, int page) {
        return StaticHtml.view(new ResponsePrintStream(resp))
                .html()
                .head()
                .title()
                .text("Artist")
                .__()
                .__()
                .body()
                    .attrStyle("background-color: #DBCC9E")
                    .style()
                .text("a {\n" +
                        "  text-decoration: none;\n" +
                        "  display: inline-block;\n" +
                        "  padding: 8px 16px;\n" +
                        "}\n" +
                        "\n" +
                        "a:hover {\n" +
                        "  background-color: #9EA9DB;\n" +
                        "  color: black;\n" +
                        "}\n" +
                        "\n" +
                        ".previous {\n" +
                        "  background-color: #B8A99C;\n" +
                        "  color: black;\n" +
                        "}\n" +
                        ".isDisabled {\n" +
                        "pointer-events: none;"+
                        "  color: currentColor;\n" +
                        "  cursor: not-allowed;\n" +
                        "  opacity: 0.5;\n" +
                        "  text-decoration: none;\n" +
                        "}\n"+
                        "\n" +
                        ".next {\n" +
                        "  background-color: #4A578F;\n" +
                        "  color: white;\n" +
                        "}")
                    .text("table {\n" +
                        "border-collapse: collapse;\n" +
                        "width: 100%;\n" +
                        "text-align: center;"+
                        "}\n" +
                        "tr:nth-child(even) {background-color: #C2B180;}" +
                        "tr:hover {background-color:#B8E0D6;" +
                        "}")
                        .__()
                    .h1()
                        .attrStyle("text-align: center;")
                        .text("Results for\""+name+"\"")
                    .__()
                    .table()
                        .addAttr("align", "center" +
                                "width:90%;")
                        .thead()
                        .tr()
                            .th().text("Name").__()
                            .th().text("MBid").__()
                            .th().text("Picture").__()
                            .th().text("URL").__()
                            .th().text("Abums").__()
                            .th().text("Tracks").__()
                        .__()
                    .__()
                .tbody()
                ;
    }

    private static void writeTableRow(Tbody<Table<Body<Html<HtmlView>>>> tbody, Artist artist) {
        tbody
                .tr()
                .td()
                    .attrStyle("width:40%")
                    //.style().text("tr:nth-child(even) {background-color: #C2B180;}").__()
                    .text(artist.getName())
                .__()
                .td().attrStyle("width:30%")
                    .text(artist.getMbid())
                .__()
                .td()
                    .img().attrSrc(artist.getImage()).__()
                .__()
                .td().attrStyle("width:30%")
                    .a()
                        .attrHref(artist.getUrl())
                        .text("Visit Artist")
                .td()
                    .a()
                        .attrHref("/artists/"+artist.getMbid()+"/albums").attrClass(StringUtils.isEmpty(artist.getMbid()) ? "isDisabled": "")
                        .text("Albums")
                .td()
                    .a()
                        .attrHref("/artists/"+artist.getMbid()+"/tracks")
                        .text(!StringUtils.isNotEmpty(artist.getMbid()) ? "" : "Tracks")
                .__()
        ;
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
