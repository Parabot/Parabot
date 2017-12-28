package org.parabot.core.user;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.concurrent.Worker;
import javafx.scene.web.WebEngine;
import org.parabot.core.bdn.api.APIConfiguration;
import org.parabot.core.ui.BrowserUI;
import org.parabot.core.ui.components.DialogHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.util.concurrent.Callable;

/**
 * @author JKetelaar
 */
public class BrowserUserAuthenticator implements Callable<String> {

    private WebEngine engine;
    private Boolean   result;
    private String    resultValue;

    public BrowserUserAuthenticator(WebEngine engine) {
        this.engine = engine;

        // Resetting values
        this.result = null;
        this.resultValue = null;
    }

    @Override
    public String call() throws Exception {
        Platform.runLater(() -> BrowserUI.getBrowser().getStage().setOnCloseRequest(event -> {
            result = false;
        }));

        ChangeListener listener = (ChangeListener<Worker.State>) (observable, oldValue, newValue) -> {
            if (engine.getLocation().startsWith(APIConfiguration.COPY_LOGIN)) {
                Document doc = engine.getDocument();

                Element      value = doc.getElementById("oauth-keys");
                NamedNodeMap map   = value.getAttributes();

                for (int i = 0; i < map.getLength(); i++) {
                    Node node = map.item(i);
                    if (node.getNodeName().equalsIgnoreCase("value")) {
                        resultValue = node.getNodeValue();
                        break; // Speed up the value
                    }
                }

                if (resultValue != null) {
                    result = true;
                } else {
                    String failedMessage = "Incorrect key.\nPlease try again.";
                    DialogHelper.showError("Login", "Incorrect key", failedMessage);
                    result = false;
                }
            }
        };

        Platform.runLater(() -> engine.getLoadWorker().stateProperty().addListener(listener));

        while (result == null) {
            Thread.sleep(1);
        }

        Platform.runLater(() -> engine.getLoadWorker().stateProperty().removeListener(listener));

        return resultValue;
    }
}
