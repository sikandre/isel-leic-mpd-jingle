package org.isel.jingle.View;

import htmlflow.StaticHtml;
import io.vertx.core.http.HttpServerResponse;
import org.xmlet.htmlapifaster.EnumMethodType;
import org.xmlet.htmlapifaster.EnumTypeContentType;
import org.xmlet.htmlapifaster.EnumTypeInputType;

public class IndexView implements View{
    @Override
    public void write(HttpServerResponse resp, Object model) {
        throw new UnsupportedOperationException("This view does not require a Model. You should invoke write(resp) instead!");
    }

    @Override
    public void write(HttpServerResponse resp, Object model, String name, int page) {

    }

    @Override
    public void write(HttpServerResponse resp) {
        String html = StaticHtml
                .view()
                .html()
                .head().style().text("body {\n" +
                        "  text-align: center;\n" +
                        "background-image: url(\"https://wallpapercave.com/wp/qpx5SVU.jpg\");" +
                        "}").__()
                .title().text("E para o 20 caso passe o exame").__()
                .__() //head
                .body().style().text("h1 {\n" +
                        "  text-align: right;\n" +
                        "}" +
                        "form {\n" +
                        "        text-align: right;\n" +
                        "    }").__()
                .div()
                .attrClass("container")
                .h1().text("Jingle Jingle Balls").__()
                //.img().attrSrc("https://avatars0.githubusercontent.com/u/1398561?s=200&v=4").__()
                    .form()
                    .attrAction("/artists")
                    .attrMethod(EnumMethodType.GET)
                        .input()
                            .attrType(EnumTypeInputType.TEXT)
                            .attrName("name")
                            .attrPlaceholder("Artist Name")
                            .attrRequired(Boolean.TRUE)
                        .__()
                        .input()
                            .attrType(EnumTypeInputType.HIDDEN)
                            .attrName("page")
                            .attrValue("1")
                        .__()
                    .input().attrType(EnumTypeInputType.SUBMIT).attrValue("Lets Go").__()
                    .__()
                .__()
                .__() //body
                .__() //html
                .render();

        resp.putHeader("content-type", "text/html");
        // write to the response and end it
        resp.end(html);

    }
}
