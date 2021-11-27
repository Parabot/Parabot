package org.parabot.core.parsers.hooks;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.parabot.core.asm.adapters.AddInterfaceAdapter;
import org.parabot.core.asm.hooks.HookFile;
import org.parabot.core.asm.wrappers.Callback;
import org.parabot.core.asm.wrappers.Getter;
import org.parabot.core.asm.wrappers.Interface;
import org.parabot.core.asm.wrappers.Invoker;
import org.parabot.core.asm.wrappers.Setter;
import org.parabot.core.asm.wrappers.Super;

import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Parses a JSON file which injects the hooks and other bytecode manipulation
 * methods
 *
 * @author Dane, JKetelaar
 */
public class JSONHookParser extends HookParser {
    private JSONObject root;
    private Map<String, String> interfaces;
    private HashMap<String, String> constants;

    public JSONHookParser(HookFile file) {
        super(file);

        JSONParser parser = new JSONParser();

        try {
            parser.parse(new InputStreamReader(file.getInputStream()));
        } catch (Throwable t) {
            throw new RuntimeException("Unable to parse hooks: " + t);
        }
    }

    public String get(JSONObject o, String s) {
        return this.get(o, s);
    }

    public String formatDescription(String s) {
        StringBuilder b = new StringBuilder();

        if (s.charAt(0) == '[') {
            for (int j = 0; j < s.length(); j++) {
                if (s.charAt(j) == '[') {
                    b.append('[');
                }
            }
            s = s.replaceAll("\\[", "");
        }

        return b.append('L').append(String.format(s, AddInterfaceAdapter.getAccessorPackage())).append(';').toString();
    }

    @Override
    public Interface[] getInterfaces() {
        JSONArray a = (JSONArray) root.get("interfaces");
        interfaces = new HashMap<>();

        if (a != null && a.size() > 0) {
            Interface[] i = new Interface[a.size()];
            for (int j = 0; j < a.size(); j++) {
                JSONObject o = (JSONObject) a.get(j);

                String clazz = this.get(o, "class");
                String interfaze = this.get(o, "interface");

                interfaces.put(clazz, interfaze);
                i[j] = new Interface(clazz, interfaze);
            }
            return i;
        }
        return null;
    }

    @Override
    public Map<String, String> getInterfaceMap() {
        return this.interfaces;
    }

    @Override
    public Super[] getSupers() {
        JSONArray a = (JSONArray) root.get("supers");

        if (a != null && a.size() > 0) {
            Super[] s = new Super[a.size()];
            for (int i = 0; i < a.size(); i++) {
                JSONObject o = (JSONObject) a.get(i);
                s[i] = new Super(this.get(o, "class"), this.get(o, "super"));
            }
            return s;
        }
        return null;
    }

    @Override
    public Getter[] getGetters() {
        JSONArray a = (JSONArray) root.get("getters");

        if (a != null && a.size() > 0) {
            Getter[] g = new Getter[a.size()];
            for (int i = 0; i < a.size(); i++) {
                JSONObject o = (JSONObject) a.get(i);

                if (o.containsKey("class") && o.containsKey("accessor")) {
                    throw new RuntimeException("Cannot have class AND accessor tags together!");
                }

                if (o.containsKey("accessor") && this.interfaces == null) {
                    throw new RuntimeException("Cannot use accessor tag before parsing interfaces!");
                }

                String desc = this.get(o, "desc");

                if (desc != null && desc.contains("%s")) {
                    desc = formatDescription(desc);
                }

                String clazz = o.containsKey("class") ? this.get(o, "class") : interfaces.get(this.get(o, "accessor"));
                String into = o.containsKey("into") ? this.get(o, "into") : clazz;

                g[i] = new Getter(into, clazz, this.get(o, "field"), this.get(o, "method"), desc, o.containsKey("static") && (boolean) o.get("static"), 0, null);
            }
            return g;
        }
        return null;
    }

    @Override
    public Setter[] getSetters() {
        JSONArray a = (JSONArray) root.get("setters");

        if (a != null && a.size() > 0) {
            Setter[] s = new Setter[a.size()];
            for (int i = 0; i < a.size(); i++) {
                JSONObject o = (JSONObject) a.get(i);

                if (o.containsKey("class") && o.containsKey("accessor")) {
                    throw new RuntimeException("Cannot have class AND accessor tags together!");
                }

                if (o.containsKey("accessor") && this.interfaces == null) {
                    throw new RuntimeException("Cannot use accessor tag before parsing interfaces!");
                }

                String desc = this.get(o, "desc");

                if (desc != null && desc.contains("%s")) {
                    desc = formatDescription(desc);
                }

                String clazz = o.containsKey("class") ? this.get(o, "class") : interfaces.get(this.get(o, "accessor"));
                String into = o.containsKey("into") ? this.get(o, "into") : clazz;

                s[i] = new Setter(into, clazz, this.get(o, "field"), this.get(o, "method"), desc, o.containsKey("static") && (boolean) o.get("static"), null);
            }
            return s;
        }
        return null;
    }

    @Override
    public Invoker[] getInvokers() {
        JSONArray a = (JSONArray) root.get("invokers");

        if (a != null && a.size() > 0) {
            Invoker[] i = new Invoker[a.size()];
            for (int j = 0; j < a.size(); j++) {
                JSONObject o = (JSONObject) a.get(j);

                if (o.containsKey("class") && o.containsKey("accessor")) {
                    throw new RuntimeException("Cannot have class AND accessor tags together!");
                }

                if (o.containsKey("accessor") && this.interfaces == null) {
                    throw new RuntimeException("Cannot use accessor tag before parsing interfaces!");
                }

                String desc = this.get(o, "desc");

                if (desc != null && desc.contains("%s")) {
                    desc = formatDescription(desc);
                }

                String clazz = o.containsKey("class") ? this.get(o, "class") : interfaces.get(this.get(o, "accessor"));
                String into = o.containsKey("into") ? this.get(o, "into") : clazz;

                i[j] = new Invoker(into, clazz, this.get(o, "invokemethod"), this.get(o, "argdesc"), this.get(o, "desc"), this.get(o, "method"), false, null, null);
            }
            return i;
        }
        return null;
    }

    @Override
    public Callback[] getCallbacks() {
        JSONArray a = (JSONArray) root.get("callbacks");

        if (a != null && a.size() > 0) {
            Callback[] c = new Callback[a.size()];
            for (int j = 0; j < a.size(); j++) {
                JSONObject o = (JSONObject) a.get(j);

                if (o.containsKey("class") && o.containsKey("accessor")) {
                    throw new RuntimeException("Cannot have class AND accessor tags together!");
                }

                if (o.containsKey("accessor") && this.interfaces == null) {
                    throw new RuntimeException("Cannot use accessor tag before parsing interfaces!");
                }

                String desc = this.get(o, "desc");

                if (desc != null && desc.contains("%s")) {
                    desc = formatDescription(desc);
                }

                String clazz = o.containsKey("class") ? this.get(o, "class") : interfaces.get(this.get(o, "accessor"));

                c[j] = new Callback(clazz, this.get(o, "method"), this.get(o, "callclass"), this.get(o, "callmethod"), this.get(o, "calldesc"), this.get(o, "callargs"), this.get(o, "desc"), false);
            }
            return c;
        }
        return null;
    }

    @Override
    public HashMap<String, String> getConstants() {
        if (this.constants == null) {
            this.constants = new HashMap<>();
        }

        if (!this.constants.isEmpty()) {
            return this.constants;
        }

        JSONArray a = (JSONArray) root.get("constants");

        if (a != null && a.size() > 0) {
            for (int j = 0; j < a.size(); j++) {
                JSONObject o = (JSONObject) a.get(j);
                this.constants.put(this.get(o, "name"), (String) o.get("value"));
            }
        }

        return this.constants;
    }
}
