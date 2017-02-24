package org.parabot.core.ui.newui.custom;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.parabot.core.ui.newui.BotUI;

import java.io.IOException;

/**
 * @author Fryslan, JKetelaar
 */
public class ServerItem extends AnchorPane {

    @FXML
    private AnchorPane server_selector_item_pane;

    @FXML
    private Label version_label;

    @FXML
    private Label description_label;

    @FXML
    private Label authors_label;

    @FXML
    private Label name_label;

    public ServerItem() {
        FXMLLoader fxmlLoader = new FXMLLoader(ServerItem.class.getResource("/storage/ui/server/item_control.fxml"));
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public double getVersion() {
        return Double.valueOf(version_label.getText());
    }

    public void setVersion(double version) {
        this.version_label.setText(String.valueOf(version));
    }

    public String getDescription() {
        return description_label.getText();
    }

    public void setDescription(String desc) {
        this.description_label.setText(desc);
    }

    public String getAuthors() {
        return authors_label.getText();
    }

    public void setAuthors(String[] authors) {
        //// TODO: 23-2-2017 remove comment tags when StringUtils class is implemented.
        //this.authors_label.setText(StringUtils.implode(", ", authors);
        this.authors_label.setText("JKetelaar");
    }

    public String getName() {
        return name_label.getText();
    }

    public void setName(String name) {
        this.name_label.setText(name);
    }

    public AnchorPane getServerSelectorItemPane() {
        return server_selector_item_pane;
    }

    public void setServerSelectorItemPane(AnchorPane server_selector_item_pane) {
        this.server_selector_item_pane = server_selector_item_pane;
    }

    @FXML
    void select_server(MouseEvent event) {
        //// TODO: 23-2-2017 handle server selecting in.

        Stage stage = (Stage) server_selector_item_pane.getScene().getWindow();
        new BotUI().setGameInterface(stage);
    }
}
