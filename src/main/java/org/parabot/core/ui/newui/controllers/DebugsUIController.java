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
    private HBox minimize_box;

    @FXML
    private Pane       return_panel;
    @FXML
    private AnchorPane npcs_panel;
    @FXML
    private AnchorPane objects_panel;
    @FXML
    private AnchorPane ground_items_panel;
    @FXML
    private AnchorPane bank_items_panel;
    @FXML
    private GridPane   inventory_grid_panel;
    @FXML
    private AnchorPane players_panel;
    private final Pane[] debugPanes = { npcs_panel, objects_panel, ground_items_panel, bank_items_panel, inventory_grid_panel, players_panel };
    @FXML
    private ListView<?> objects_list_view;
    @FXML
    private ListView<?> ground_items_list_view;
    @FXML
    private ListView<?> npcs_list_view;
    @FXML
    private ListView<?> bank_items_list_view;
    @FXML
    private ListView<?> players_list_view;
    @FXML
    private ImageView interfaces_icon;
    @FXML
    private ImageView animations_icon;
    @FXML
    private ImageView actions_icon;
    @FXML
    private ImageView collision_flag_icon;
    @FXML
    private ImageView map_icon;
    @FXML
    private ImageView mouse_icon;

    @FXML
    private void openBankItemsPanel(MouseEvent event) {
        hideAllPanes();
        bank_items_panel.setVisible(true);
        //// TODO: 23-2-2017 load bankItems to listView
    }

    @FXML
    private void openGroundItemsPanel(MouseEvent event) {
        hideAllPanes();
        ground_items_panel.setVisible(true);
        //// TODO: 23-2-2017 load groundItems to listView
    }

    @FXML
    private void openInventoryItemsPanel(MouseEvent event) {
        hideAllPanes();
        inventory_grid_panel.setVisible(true);
        //// TODO: 23-2-2017 load inventoryItems to gridPanel
    }

    @FXML
    private void openNpcsPanel(MouseEvent event) {
        hideAllPanes();
        npcs_panel.setVisible(true);
        //// TODO: 23-2-2017 load npcs to listView
    }

    @FXML
    private void openObjectsPanel(MouseEvent event) {
        hideAllPanes();
        objects_panel.setVisible(true);
        //// TODO: 23-2-2017 load objects to listView
    }

    @FXML
    private void openPlayersPanel(MouseEvent event) {
        hideAllPanes();
        bank_items_panel.setVisible(true);
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
        Stage stage = (Stage) minimize_box.getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    private void returnToGame(MouseEvent event) {
        Stage stage = (Stage) return_panel.getScene().getWindow();
        new BotUI().setGameInterface(stage);
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

