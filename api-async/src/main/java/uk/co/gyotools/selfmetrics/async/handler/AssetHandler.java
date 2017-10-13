package uk.co.gyotools.selfmetrics.async.handler;

import ratpack.handling.Context;
import ratpack.handling.Handler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class AssetHandler implements Handler {
    public AssetHandler() {
    }

    public void handle(Context ctx) throws Exception {
        String path = ctx.getRequest().getPath();
        if(path.isEmpty() || path.endsWith("/")) {
            path = path + "index.html";
        }

        InputStream i = AssetHandler.class.getResourceAsStream("/" + path);
        if(i == null) {
            ctx.clientError(404);
        } else {
            try {
                ctx.getResponse().contentType(guessContentType(path));
                ctx.getResponse().send(toByteArray(i));
            } finally {
                i.close();
            }

        }
    }

    private static byte[] toByteArray(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];

        int read;
        while((read = inputStream.read(buf)) != -1) {
            outputStream.write(buf, 0, read);
        }

        return outputStream.toByteArray();
    }

    private static String guessContentType(String filename) {
        return filename.endsWith("html")?"text/html":(filename.endsWith("js")?"application/javascript":(filename.endsWith("png")?"image/png":(filename.endsWith("gif")?"image/gif":(filename.endsWith("ico")?"image/x-icon":(filename.endsWith("css")?"text/css":(filename.endsWith("map")?"application/json":(filename.endsWith("eot")?"application/vnd.ms-fontobject":(filename.endsWith("svg")?"image/svg+xml":(filename.endsWith("ttf")?"application/font-sfnt":(filename.endsWith("woff")?"application/font-woff":(filename.endsWith("woff2")?"font/woff2":"application/octet-stream")))))))))));
    }
}
