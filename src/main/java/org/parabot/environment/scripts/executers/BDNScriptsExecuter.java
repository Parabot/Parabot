package org.parabot.environment.scripts.executers;

import org.parabot.core.Configuration;
import org.parabot.core.classpath.ClassPath;
import org.parabot.core.forum.AccountManager;
import org.parabot.core.forum.AccountManagerAccess;
import org.parabot.core.ui.utils.UILog;
import org.parabot.environment.api.utils.WebUtil;
import org.parabot.environment.scripts.Script;
import org.parabot.environment.scripts.loader.JavaScriptLoader;

import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLConnection;

import javax.swing.JOptionPane;

/**
 * Loads a script from the BDN
 *
 * @author Everel
 */
public class BDNScriptsExecuter extends ScriptExecuter {

    private static AccountManager manager;

    public static final AccountManagerAccess MANAGER_FETCHER = new AccountManagerAccess() {

        @Override
        public final void setManager(AccountManager manager) {
            BDNScriptsExecuter.manager = manager;
        }

    };

    private int id = -1;

    public BDNScriptsExecuter(final int id) {
        this.id = id;
    }

    @Override
    public void run(ThreadGroup tg) {
        try {
            final URLConnection urlConnection = WebUtil.getConnection(new URL(
                    Configuration.GET_SCRIPT + this.id), manager.getAccount().getURLUsername(), manager.getAccount().getURLPassword());

            final String contentType = urlConnection.getHeaderField("Content-type");
            switch (contentType) {
                case "text/html":
                    // failed to fetch script
                    UILog.log("Error", "Failed to load BDN script, error: [Page returned: " + WebUtil.getContents(urlConnection) + "]", JOptionPane.ERROR_MESSAGE);
                    break;
                case "application/jar":
                    //// JAR LOADING PART ////////
                    // succesfull request, jar returned
                    final ClassPath classPath = new ClassPath();
                    classPath.addJar(urlConnection);

                    final JavaScriptLoader loader = new JavaScriptLoader(classPath);
                    final String[] scriptClasses = loader.getScriptClassNames();
                    if (scriptClasses == null || scriptClasses.length == 0) {
                        UILog.log("Error", "Failed to load BDN script, error: [No script found in jar file.]", JOptionPane.ERROR_MESSAGE);
                        return;
                    } else if (scriptClasses.length > 1) {
                        UILog.log("Error", "Failed to load BDN script, error: [Multiple scripts found in jar file.]");
                        return;
                    }

                    final String className = scriptClasses[0];
                    try {
                        final Class<?> scriptClass = loader.loadClass(className);
                        final Constructor<?> con = scriptClass.getConstructor();
                        final Script script = (Script) con.newInstance();
                        script.setScriptID(this.id);
                        super.finalize(tg, script);

                    } catch (NoClassDefFoundError | ClassNotFoundException ignored) {
                        UILog.log("Error", "Failed to load BDN script, error: [This server provider does not support this script]", JOptionPane.ERROR_MESSAGE);
                    } catch (Throwable t) {
                        t.printStackTrace();
                        UILog.log("Error", "Failed to load BDN script, post the stacktrace/error on the parabot forums.", JOptionPane.ERROR_MESSAGE);
                    }
                    //// END JAR LOADING ////
                    break;
                default:
                    UILog.log("Error", "Failed to load BDN script, error: [Unknown content type: " + contentType + "]", JOptionPane.ERROR_MESSAGE);
                    break;
            }
        } catch (Throwable t) {
            t.printStackTrace();
            UILog.log("Error", "Failed to load BDN script, post the stacktrace/error on the parabot forums.", JOptionPane.ERROR_MESSAGE);
        }
    }
}
