package org.parabot.core.ui.newui.controllers;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.VBox;
import org.parabot.core.settings.Configuration;
import org.parabot.core.ui.newui.models.ServerItem;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Fryslan, JKetelaar
 */
public class ServerSelectorController implements Initializable {

    @FXML
    private Label title;
    @FXML
    private VBox  serverItemBox;
    @FXML
    private ScrollPane scrollPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //// TODO: 23-2-2017 add all models items to the list.
        ServerItem test = new ServerItem();
        test.setName("Dreamscape");
        test.setVersion(1.0);
        test.setDescription("Dreamscape is a models with models items and a stable player base of 100+. They have many new features added all the time.");
        test.setAuthors(new String[]{"JKetelaar", "Fryslan"});
        serverItemBox.getChildren().add(test.getServerSelectorItemPane());

        ServerItem test1 = new ServerItem();
        test1.setName("Dreamscape1");
        test1.setVersion(1.1);
        test1.setDescription("Dreamscape is a models with models items and a stable player base of 100+. They have many new features added all the time.");
        test1.setAuthors(new String[]{"JKetelaar", "Fryslan"});
        serverItemBox.getChildren().add(test1.getServerSelectorItemPane());

        ServerItem test2 = new ServerItem();
        test2.setName("Dreamscape2");
        test2.setVersion(1.2);
        test2.setDescription("Dreamscape is a models with models items and a stable player base of 100+. They have many new features added all the time.");
        test2.setAuthors(new String[]{"JKetelaar", "Fryslan"});
        serverItemBox.getChildren().add(test2.getServerSelectorItemPane());

        title.setText(Configuration.BOT_TITLE);

        /* Only allow vertical scrolling, disable horizontal scrolling */
        scrollPane.addEventFilter(ScrollEvent.SCROLL,new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                if (event.getDeltaX() != 0) {
                    event.consume();
                }
            }
        });
    }

}
