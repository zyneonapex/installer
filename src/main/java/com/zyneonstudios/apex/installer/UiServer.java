package com.zyneonstudios.apex.installer;

import com.sun.net.httpserver.HttpServer;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class UiServer {

    public static int start() {
        try {
            // "0" sucht automatisch nach einem freien Port auf dem System
            HttpServer server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
            
            server.createContext("/", exchange -> {
                String path = exchange.getRequestURI().getPath();
                if (path.equals("/")) {
                    path = "/index.html";
                }

                // Datei aus den eingebetteten GraalVM-Ressourcen laden
                try (InputStream is = UiServer.class.getResourceAsStream("/ui" + path)) {
                    if (is == null) {
                        exchange.sendResponseHeaders(404, -1);
                        exchange.close();
                        return;
                    }

                    // Wichtig für WebView: Mime-Types setzen, sonst verweigert Vite die JS-Module
                    if (path.endsWith(".html")) exchange.getResponseHeaders().set("Content-Type", "text/html");
                    else if (path.endsWith(".js")) exchange.getResponseHeaders().set("Content-Type", "application/javascript");
                    else if (path.endsWith(".css")) exchange.getResponseHeaders().set("Content-Type", "text/css");
                    else if (path.endsWith(".svg")) exchange.getResponseHeaders().set("Content-Type", "image/svg+xml");

                    byte[] bytes = is.readAllBytes();
                    exchange.sendResponseHeaders(200, bytes.length);
                    
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(bytes);
                    }
                } catch (Exception e) {
                    exchange.sendResponseHeaders(500, -1);
                } finally {
                    exchange.close();
                }
            });
            
            // Server in einem Hintergrund-Thread starten
            server.setExecutor(null);
            server.start();
            
            // Den zugewiesenen Port zurückgeben, damit die WebView weiß, wohin sie navigieren muss
            return server.getAddress().getPort();
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to start internal UI server", e);
        }
    }
}