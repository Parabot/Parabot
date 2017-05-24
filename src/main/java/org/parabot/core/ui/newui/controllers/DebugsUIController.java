package org.parabot.core.ui.newui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.parabot.core.ui.newui.BotUI;

/**
 * @author Fryslan, JKetelaar
 */

public class DebugsUIController {

    @FXML
    private HBox       minimizeBox;
    @FXML
    private Pane       returnPanel;
    @FXML
    private AnchorPane npcsPanel, objectsPanel, groundItemsPanel, bankItemsPanel;
    @FXML
    private GridPane    inventoryGridPanel;
    @FXML
    private AnchorPane  playersPanel;
    private final Pane[] debugPanes = { npcsPanel, objectsPanel, groundItemsPanel, bankItemsPanel, inventoryGridPanel, playersPanel };
    @FXML
    private ListView<?> objectsListView, groundItemsListView, npcsListView, bankItemsListView, playersListView;
    @FXML
    private ImageView interfacesIcon, mouseIcon, animationsIcon, actionsIcon, collisionFlagIcon, mapIcon;

    @FXML
    private void openBankItemsPanel(MouseEvent event) {
        hideAllPanes();
        bankItemsPanel.setVisible(true);
        //// TODO: 23-2-2017 load bankItems to listView
    }

    @FXML
    private void openGroundItemsPanel(MouseEvent event) {
        hideAllPanes();
        groundItemsPanel.setVisible(true);
        //// TODO: 23-2-2017 load groundItems to listView
    }

    @FXML
    private void openInventoryItemsPanel(MouseEvent event) {
        hideAllPanes();
        inventoryGridPanel.setVisible(true);
        //// TODO: 23-2-2017 load inventoryItems to gridPanel
    }

    @FXML
    private void openNpcsPanel(MouseEvent event) {
        hideAllPanes();
        npcsPanel.setVisible(true);
        //// TODO: 23-2-2017 load npcs to listView
    }

    @FXML
    private void openObjectsPanel(MouseEvent event) {
        hideAllPanes();
        objectsPanel.setVisible(true);
        //// TODO: 23-2-2017 load objects to listView
    }

    @FXML
    private void openPlayersPanel(MouseEvent event) {
        hideAllPanes();
        bankItemsPanel.setVisible(true);
        //// TODO: 23-2-2017 load players to listView
    }

    @FXML
    private void tickActions(MouseEvent event) {
        //// TODO: 23-2-2017  enable/disable action debugging + change icon

    }

    @FXML
    private void tickAnimations(MouseEvent event) {
        //// TODO: 23-2-2017  enable/disable animation debugging + change icon
    }

    @FXML
    private void tickCollisionFlags(MouseEvent event) {
        //// TODO: 23-2-2017  enable/disable collision flags debugging + change icon
    }

    @FXML
    private void tickInterfaces(MouseEvent event) {
        //// TODO: 23-2-2017  enable/disable interfaces debugging + change icon
    }

    @FXML
    private void tickMap(MouseEvent event) {
        //// TODO: 23-2-2017  enable/disable map debugging + change icon
    }

    @FXML
    private void tickMouse(MouseEvent event) {
        //// TODO: 23-2-2017  enable/disable mouse debugging + change icon
    }

    @FXML
    private void minimize(MouseEvent event) {
        Stage stage = (Stage) minimizeBox.getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    private void returnToGame(MouseEvent event) {
        Stage stage = (Stage) returnPanel.getScene().getWindow();
        new BotUI().switchState(BotUI.ViewState.GAME, stage);
    }

    private void hideAllPanes() {
        for (Pane pane : debugPanes) {
            if (pane.isVisible()) {
                return;
            }
            pane.setVisible(false);
        }
    }

}

