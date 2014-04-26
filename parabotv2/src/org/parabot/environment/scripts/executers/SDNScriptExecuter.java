package org.parabot.environment.scripts.executers;

import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLConnection;

import javax.swing.JOptionPane;

import org.parabot.core.Configuration;
import org.parabot.core.classpath.ClassPath;
import org.parabot.core.forum.AccountManager;
import org.parabot.core.forum.AccountManagerAccess;
import org.parabot.core.ui.utils.UILog;
import org.parabot.environment.api.utils.WebUtil;
import org.parabot.environment.scripts.Script;
import org.parabot.environment.scripts.loader.JavaScriptLoader;

/**
 * 
 * Loads a script from the SDN
 * 
 * @author Everel
 *
 */
public class SDNScriptExecuter extends ScriptExecuter {
	
	private static AccountManager manager;

	public static final AccountManagerAccess MANAGER_FETCHER = new AccountManagerAccess() {

		@Override
		public final void setManager(AccountManager manager) {
			SDNScriptExecuter.manager = manager;
		}

	};
	
	private int id = -1;
	
	public SDNScriptExecuter(final int id) {
		this.id = id;
	}

	@Override
	public void run(ThreadGroup tg) {
		try {
			final URLConnection urlConnection = WebUtil.getConnection(new URL(String.format(Configuration.GET_SDN_SCRIPT, manager.getAccount().getUsername(), manager.getAccount().getPassword(), this.id)));
			final String contentType = urlConnection.getHeaderField("Content-type");
			if(contentType.equals("text/html")) {
				// failed to fetch script
				UILog.log("Error", new StringBuilder("Failed to load SDN script, error: [Page returned: ").append(WebUtil.getContents(urlConnection)).append("]").toString(), JOptionPane.ERROR_MESSAGE);
			} else if(contentType.equals("application/jar")) {
				
				//// JAR LOADING PART ////////
				
				
				// succesfull request, jar returned
				final ClassPath classPath = new ClassPath();
				classPath.addJar(urlConnection);
				
				final JavaScriptLoader loader = new JavaScriptLoader(classPath);
				final String[] scriptClasses = loader.getScriptClassNames();
				if(scriptClasses == null || scriptClasses.length == 0) {
					UILog.log("Error", "Failed to load SDN script, error: [No script found in jar file.]", JOptionPane.ERROR_MESSAGE);
					return;
				} else if(scriptClasses.length > 1) {
					UILog.log("Error", "Failed to load SDN script, error: [Multiple scripts found in jar file.]");
					return;
				}
				
				final String className = scriptClasses[0];
				try {
					final Class<?> scriptClass = loader.loadClass(className);
					final Constructor<?> con = scriptClass.getConstructor();
					final Script script = (Script) con.newInstance();
					super.finalize(tg, script);
				} catch (NoClassDefFoundError ignored) {
					UILog.log("Error", "Failed to load SDN script, error: [This server provider does not support this script]", JOptionPane.ERROR_MESSAGE);
				} catch(ClassNotFoundException ignored) {
					UILog.log("Error", "Failed to load SDN script, error: [This server provider does not support this script]", JOptionPane.ERROR_MESSAGE);
				} catch (Throwable t) {
					t.printStackTrace();
					UILog.log("Error", "Failed to load SDN script, post the stacktrace/error on the parabot forums.", JOptionPane.ERROR_MESSAGE);
				}
				
				
				//// END JAR LOADING ////
				
			} else {
				UILog.log("Error", new StringBuilder("Failed to load SDN script, error: [Unknown content type: ").append(contentType).append("]").toString(), JOptionPane.ERROR_MESSAGE);
			}
		} catch (Throwable t) {
			t.printStackTrace();
			UILog.log("Error", "Failed to load SDN script, post the stacktrace/error on the parabot forums.", JOptionPane.ERROR_MESSAGE);
		}
	}

}
