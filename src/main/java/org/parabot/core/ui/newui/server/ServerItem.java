package org.parabot.core.ui.newui.server;

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
    private AnchorPane serverSelectorItemPane;
    @FXML
    private Label versionLabel;
    @FXML
    private Label descriptionLabel;
    @FXML
    private Label authorsLabel;
    @FXML
    private Label nameLabel;

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
        return Double.valueOf(versionLabel.getText());
    }

    public void setVersion(double version) {
        this.versionLabel.setText(String.valueOf(version));
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
        //// TODO: 23-2-2017 remove comment tags when StringUtils class is implemented.
        //this.authorsLabel.setText(StringUtils.implode(", ", authors);
        this.authorsLabel.setText("JKetelaar");
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
        //// TODO: 23-2-2017 handle server selecting in.

        Stage stage = (Stage) serverSelectorItemPane.getScene().getWindow();
        new BotUI().setGameInterface(stage);
    }
}
