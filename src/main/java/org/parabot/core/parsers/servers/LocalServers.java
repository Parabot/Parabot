package org.parabot.core.parsers.servers;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.parabot.api.io.Directories;
import org.parabot.api.io.WebUtil;
import org.parabot.core.Core;
import org.parabot.core.classpath.ClassPath;
import org.parabot.core.desc.ServerDescription;
import org.parabot.environment.servers.executers.LocalServerExecutor;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * @author JKetelaar
 */
public class LocalServers extends ServerParser {

    @Override
    public void execute() {
        final ClassPath basePath = new ClassPath();

        basePath.parseJarFiles(false);
        basePath.addClasses(Directories.getServerPath());

        for (File file : Directories.listJSONFiles(Directories.getServerPath())) {
            Core.verbose("[Local server in]: " + file.getName());
            try {
                JSONObject object  = (JSONObject) WebUtil.getJsonParser().parse(new FileReader(file));
                // Make sure it's the latest format
                if (object.get("id") != null) {
                    long   id      = (long) object.get("id");
                    String name    = (String) object.get("name");
                    String author  = (String) object.get("author");
                    double version = (Double) object.get("version");

                    JSONObject locations   = (JSONObject) object.get("details");
                    String     server      = (String) locations.get("server");
                    String     provider    = (String) locations.get("provider");
                    String     hooks       = (String) locations.get("hooks");
                    String     clientClass = (String) object.get("client-class");

                    Core.verbose("[Local server]: " + name);

                    HashMap<String, String> details = new HashMap<>();
                    details.put("provider", provider);
                    details.put("server", server);
                    details.put("hooks", hooks);
                    details.put("client-class", clientClass);

                    ServerDescription desc = new ServerDescription((int) id, name, author, version, details);
                    SERVER_CACHE.put(desc, Core.getInjector().getInstance(LocalServerExecutor.class).setServerDescription(desc));
                }
            } catch (IOException | ParseException | NullPointerException e) {
                e.printStackTrace();
            }
        }
    }
}
