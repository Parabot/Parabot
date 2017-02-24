package org.parabot.core.ui.newui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import org.parabot.core.ui.newui.custom.ServerItem;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Fryslan, JKetelaar
 */
public class ServerSelectorController implements Initializable {

    @FXML
    private VBox server_items_box;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //// TODO: 23-2-2017 add all server items to the list.
        ServerItem test = new ServerItem();
        test.setName("Dreamscape");
        test.setVersion(1.0);
        test.setDescription("Dreamscape is a server with custom items and a stable player base of 100+. They have many new features added all the time.");
        server_items_box.getChildren().add(test.getServerSelectorItemPane());

        ServerItem test1 = new ServerItem();
        test1.setName("Dreamscape1");
        test1.setVersion(1.1);
        test1.setDescription("Dreamscape is a server with custom items and a stable player base of 100+. They have many new features added all the time.");
        server_items_box.getChildren().add(test1.getServerSelectorItemPane());

        ServerItem test2 = new ServerItem();
        test2.setName("Dreamscape2");
        test2.setVersion(1.2);
        test2.setDescription("Dreamscape is a server with custom items and a stable player base of 100+. They have many new features added all the time.");
        server_items_box.getChildren().add(test2.getServerSelectorItemPane());

    }

}
