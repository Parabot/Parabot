package org.parabot.core.ui.newui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.VBox;
import org.parabot.core.Core;
import org.parabot.core.desc.ServerDescription;
import org.parabot.core.parsers.servers.ServerParser;
import org.parabot.core.settings.Configuration;
import org.parabot.core.ui.newui.models.ServerItem;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Fryslan, JKetelaar
 */
public class ServerSelectorController implements Initializable {

    @FXML
    private Label      title;
    @FXML
    private VBox       serverItemBox;
    @FXML
    private ScrollPane scrollPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        for (ServerDescription serverDescription : ServerParser.getDescriptions()) {
            ServerItem serverItem = Core.getInjector().getInstance(ServerItem.class);
            serverItem.setServerDescription(serverDescription);
            serverItemBox.getChildren().add(serverItem.getServerSelectorItemPane());
        }

        title.setText(Configuration.BOT_TITLE);

        /* Only allow vertical scrolling, disable horizontal scrolling */
        scrollPane.addEventFilter(ScrollEvent.SCROLL, event -> {
            if (event.getDeltaX() != 0) {
                event.consume();
            }
        });
    }
}
