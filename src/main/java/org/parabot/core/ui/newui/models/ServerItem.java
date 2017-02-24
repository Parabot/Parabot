package org.parabot.core.ui.newui.models;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.parabot.core.ui.newui.BotUI;
import org.parabot.environment.api.utils.StringUtils;

import java.io.IOException;

/**
 * @author Fryslan, JKetelaar
 */
public class ServerItem extends AnchorPane {

    @FXML
    private AnchorPane serverSelectorItemPane;
    @FXML
    private Label      versionLabel;
    @FXML
    private Label      descriptionLabel;
    @FXML
    private Label      authorsLabel;
    @FXML
    private Label      nameLabel;

    private double version;

    public ServerItem() {
        FXMLLoader fxmlLoader = new FXMLLoader(ServerItem.class.getResource("/storage/ui/server/item_control.fxml"));
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public double getVersion() {
        return version;
    }

    public void setVersion(double version) {
        this.version = version;
        this.versionLabel.setText(String.format("V: %.2f", version));
    }

    public String getDescription() {
        return descriptionLabel.getText();
    }

    public void setDescription(String desc) {
        this.descriptionLabel.setText(desc);
    }

    public String getAuthors() {
        return authorsLabel.getText();
    }

    public void setAuthors(String[] authors) {
        this.authorsLabel.setText(StringUtils.implode(", ", authors));
    }

    public String getName() {
        return nameLabel.getText();
    }

    public void setName(String name) {
        this.nameLabel.setText(name);
    }

    public AnchorPane getServerSelectorItemPane() {
        return serverSelectorItemPane;
    }

    public void setServerSelectorItemPane(AnchorPane server_selector_item_pane) {
        this.serverSelectorItemPane = server_selector_item_pane;
    }

    @FXML
    private void selectServer(MouseEvent event) {
        //// TODO: 23-2-2017 handle models selecting in.

        Stage stage = (Stage) serverSelectorItemPane.getScene().getWindow();
        new BotUI().setGameInterface(stage);
    }
}
