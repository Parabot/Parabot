package org.parabot.core.ui.components.web;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.util.concurrent.CountDownLatch;

public class MiniBrowser extends Application {

    public static final CountDownLatch latch = new CountDownLatch(1);

    private static MiniBrowser context;
    private Stage stage;
    private WebView browser;

    public MiniBrowser() {
        this.browser = new WebView();
        setMiniBrowser(this);
    }

    @Override
    public void start(Stage stageParam) {
        this.stage = stageParam;

        stage.setTitle("Parabot Browser");
        stage.setWidth(500);
        stage.setHeight(500);

        Scene scene = new Scene(new Group());
        VBox root = new VBox();

        root.getChildren().add(browser);
        scene.setRoot(root);

        stage.setScene(scene);
        stage.show();
    }

    public static void setMiniBrowser(MiniBrowser miniBrowser) {
        context = miniBrowser;
        latch.countDown();
    }

    public static MiniBrowser waitForMiniBrowser() {
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return context;
    }

    public void load(String url) {
        this.browser.getEngine().load(url);
    }

    public static MiniBrowser getContext() {
        return context;
    }

    public static void openWebsite(final String url) {
        new Thread() {
            @Override
            public void run() {
                javafx.application.Application.launch(MiniBrowser.class);
            }
        }.start();

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                MiniBrowser startUpTest = MiniBrowser.waitForMiniBrowser();
                startUpTest.load(url);
            }
        });
    }
}