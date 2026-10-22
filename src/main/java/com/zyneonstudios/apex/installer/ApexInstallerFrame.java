package com.zyneonstudios.apex.installer;

import io.avaje.webview.Webview;

public class ApexInstallerFrame {

    Webview webview;

    public ApexInstallerFrame() {

    }

    public void launchWindow() {
        try {
            Thread.ofPlatform().start(() -> {
                int port = UiServer.start();

                try {
                    this.webview = Webview.builder()
                            .title("NEX App")
                            .width(400)
                            .height(400)
                            .resizable(false)
                            .enableDeveloperTools(true)
                            .navigate("http://localhost:" + port)
                            .transparent(true)
                            .build();

                    this.webview.run();
                    System.exit(0);
                } catch (Throwable e) {
                    throw new RuntimeException("Failed to launch native WebView", e);
                }
            });
        } catch (Exception e) {
            throw new RuntimeException("Failed to launch native WebView", e);
        }
    }
}