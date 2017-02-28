package org.parabot.core.ui.newui.controllers;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.WindowEvent;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.parabot.api.io.Directories;
import org.parabot.api.io.WebUtil;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.*;
import java.nio.file.Files;
import java.util.ResourceBundle;

/**
 * @author Fryslan
 */
public class BrowserUIController implements Initializable {

    @FXML
    private WebView    webView;
    @FXML
    private AnchorPane pane;
    @FXML
    private ImageView  refreshIcon;

    private static final File cookiesFile = new File(Directories.getSettingsPath(), "cookies.json");
    private CookieManager manager;

    @FXML
    private void refresh(MouseEvent event) {
        WebEngine engine = webView.getEngine();
        engine.load(engine.getLocation());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        manager = new CookieManager();
        manager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(manager);

        loadCookies();
    }

    public CookieManager getManager() {
        return manager;
    }

    public void loadPage(String url) {
        WebEngine engine = getWebView().getEngine();
        engine.load(url);

        addOnExit();
    }

    public WebView getWebView() {
        return webView;
    }

    private void saveCookies() {
        JSONArray array = new JSONArray();
        for (HttpCookie c : manager.getCookieStore().getCookies()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", c.getName());
            jsonObject.put("value", c.getValue());
            jsonObject.put("domain", c.getDomain());
            jsonObject.put("path", c.getPath());
            jsonObject.put("max_age", c.getMaxAge());
            jsonObject.put("secure", c.getSecure());
            jsonObject.put("http_only", c.isHttpOnly());

            array.add(jsonObject);
        }

        try {
            if (!cookiesFile.exists()) {
                cookiesFile.createNewFile();
            }

            FileWriter fileWriter = new FileWriter(cookiesFile);
            fileWriter.write(array.toJSONString());
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadCookies() {
        if (cookiesFile.exists()) {
            try {
                JSONArray    array = (JSONArray) WebUtil.getJsonParser().parse(new FileReader(cookiesFile));
                for (Object object : array) {
                    JSONObject jsonObject = (JSONObject) object;

                    String  name     = (String) jsonObject.get("name");
                    String  value    = (String) jsonObject.get("value");
                    String  domain   = (String) jsonObject.get("domain");
                    String  path     = (String) jsonObject.get("path");
                    long    maxAge   = (Long) jsonObject.get("max_age");
                    boolean secure   = (Boolean) jsonObject.get("secure");
                    boolean httpOnly = (Boolean) jsonObject.get("http_only");

                    final HttpCookie c = new HttpCookie(name, value);
                    c.setDomain(domain);
                    c.setHttpOnly(httpOnly);
                    c.setPath(path);
                    c.setMaxAge(maxAge);
                    c.setSecure(secure);

                    manager.getCookieStore().add(new URI(c.getDomain()), c);
                }
            } catch (URISyntaxException | IOException | ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private void addOnExit() {
        pane.getScene().getWindow().setOnCloseRequest(we -> saveCookies());
        pane.getScene().getWindow().setOnHiding(event -> saveCookies());
    }
}
