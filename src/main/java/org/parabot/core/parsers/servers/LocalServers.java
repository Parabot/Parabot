package org.parabot.core.parsers.servers;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.parabot.core.Configuration;
import org.parabot.core.Core;
import org.parabot.core.Directories;
import org.parabot.core.classpath.ClassPath;
import org.parabot.core.desc.ServerDescription;
import org.parabot.core.desc.ServerProviderInfo;
import org.parabot.environment.api.utils.WebUtil;
import org.parabot.environment.servers.ServerManifest;
import org.parabot.environment.servers.executers.LocalPublicServerExecuter;
import org.parabot.environment.servers.executers.LocalServerExecuter;
import org.parabot.environment.servers.loader.ServerLoader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;

/**
 * Parses local server providers located in the servers directory
 *
 * @author Everel, JKetelaar
 */
public class LocalServers extends ServerParser {

    @Override
    public void execute() {
        // parse classes in server directories
        final ClassPath basePath = new ClassPath();
        basePath.parseJarFiles(false);
        basePath.addClasses(Directories.getServerPath());

        final ArrayList<ClassPath> classPaths = new ArrayList<>();
        classPaths.add(basePath);
        for (final ClassPath classPath : basePath.getJarFiles()) {
            classPaths.add(classPath);
        }

        for (final ClassPath path : classPaths) {
            // init the server loader
            final ServerLoader loader = new ServerLoader(path);

            // loop through all classes which extends the 'ServerProvider' class
            for (final String className : loader.getServerClassNames()) {
                try {
                    // get class
                    final Class<?> serverProviderClass = loader
                            .loadClass(className);
                    // get annotation
                    final Object annotation = serverProviderClass
                            .getAnnotation(ServerManifest.class);
                    if (annotation == null) {
                        throw new RuntimeException("Missing manifest at "
                                + className);
                    }
                    // cast object annotation to server manifest annotation
                    final ServerManifest manifest = (ServerManifest) annotation;
                    // get constructor
                    final Constructor<?> con = serverProviderClass
                            .getConstructor();

                    SERVER_CACHE.put(
                            new ServerDescription(manifest.name(), manifest
                                    .author(), manifest.version()),
                            new LocalServerExecuter(con, path,
                                    manifest.name()));
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        }

        for (File file : Directories.listJSONFiles(Directories.getServerPath())) {
            Core.verbose("[Local server in]: " + file.getName());
            try {
                JSONObject object = (JSONObject) WebUtil.getJsonParser().parse(new FileReader(file));
                String name = (String) object.get("name");
                String author = (String) object.get("author");
                double version = (Double) object.get("version");
                String clientClass = (String) object.get("client-class");
                Object bank;
                int bankTabs = 0;
                if ((bank = object.get("bank")) != null) {
                    bankTabs = (int) bank;
                }
                String uuidStr = (String) object.get("uuid"); // optional

                JSONObject locations = (JSONObject) object.get("locations");
                String server = (String) locations.get("server");
                String provider = (String) locations.get("provider");
                String hooks = (String) locations.get("hooks");
                String randoms = (String) locations.get("randoms");

                if (randoms == null) {
                    randoms = Configuration.GET_RANDOMS + (Configuration.BOT_VERSION.isNightly() ? Configuration.NIGHTLY_APPEND : "");
                }

                Core.verbose("[LocalServers]: Parsed server: " + name);

                ServerProviderInfo serverProviderInfo = new ServerProviderInfo(server, hooks, name, clientClass, bankTabs, randoms);

                ServerDescription desc = new ServerDescription(name, author, version);
                if (uuidStr != null && uuidStr.length() > 0) {
                    desc.uuid = Integer.parseInt(uuidStr);
                }

                SERVER_CACHE.put(desc, new LocalPublicServerExecuter(name, serverProviderInfo, server, provider));
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        }
    }

}
