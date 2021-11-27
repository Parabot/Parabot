package org.parabot.core.parsers.hooks;

import org.parabot.core.Core;
import org.parabot.core.asm.adapters.AddInterfaceAdapter;
import org.parabot.core.asm.hooks.HookFile;
import org.parabot.core.asm.wrappers.Callback;
import org.parabot.core.asm.wrappers.Getter;
import org.parabot.core.asm.wrappers.Interface;
import org.parabot.core.asm.wrappers.Invoker;
import org.parabot.core.asm.wrappers.Setter;
import org.parabot.core.asm.wrappers.Super;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Parses an XML file which injects the hooks and other bytecode manipulation
 * methods
 *
 * @author JKetelaar
 */
public class XMLHookParser extends HookParser {
    private final Document doc;
    private final HashMap<String, String> interfaceMap;
    private final HashMap<String, String> constants;
    private boolean parsedInterfaces;

    public XMLHookParser(HookFile hookFile) {
        super(hookFile);
        interfaceMap = new HashMap<>();
        constants = new HashMap<>();
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(hookFile.getInputStream(manager));
            doc.getDocumentElement().normalize();
            if (!doc.getDocumentElement().getNodeName().equals("injector")) {
                throw new RuntimeException("Incorrect hook file.");
            }
        } catch (Throwable t) {
            throw new RuntimeException("Unable to parse hooks " + t);
        }
    }

    @Override
    public Interface[] getInterfaces() {
        parsedInterfaces = true;
        final NodeList interfaceRootList = doc
                .getElementsByTagName("interfaces");
        switch (interfaceRootList.getLength()) {
            case 0:
                return null;
            case 1:
                break;
            default:
                throw new RuntimeException(
                        "Hook file may not contains multiple <interfaces> tags ");
        }
        final Node node = interfaceRootList.item(0);
        if (node.getNodeType() != Node.ELEMENT_NODE) {
            return null;
        }
        final Element interfaceRoot = (Element) node;
        final NodeList interfaces = interfaceRoot.getElementsByTagName("add");
        if (interfaces.getLength() == 0) {
            return null;
        }
        final ArrayList<Interface> interfaceList = new ArrayList<Interface>();
        for (int x = 0; x < interfaces.getLength(); x++) {
            final Node n = interfaces.item(x);
            if (n.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            final Element addInterface = (Element) n;
            final String className = getValue("classname", addInterface);
            final String interfaceClass = getValue("interface", addInterface);
            interfaceMap.put(interfaceClass, className);
            final Interface inf = new Interface(className, interfaceClass);
            interfaceList.add(inf);
        }
        return interfaceList.toArray(new Interface[interfaceList.size()]);
    }

    @Override
    public Map<String, String> getInterfaceMap() {
        return this.interfaceMap;
    }

    @Override
    public Super[] getSupers() {
        final NodeList interfaceRootList = doc.getElementsByTagName("supers");
        switch (interfaceRootList.getLength()) {
            case 0:
                return null;
            case 1:
                break;
            default:
                throw new RuntimeException(
                        "Hook file may not contains multiple <supers> tags ");
        }
        final Node node = interfaceRootList.item(0);
        if (node.getNodeType() != Node.ELEMENT_NODE) {
            return null;
        }
        final Element superRoot = (Element) node;
        final NodeList supers = superRoot.getElementsByTagName("add");
        if (supers.getLength() == 0) {
            return null;
        }
        final ArrayList<Super> superList = new ArrayList<Super>();
        for (int x = 0; x < supers.getLength(); x++) {
            final Node n = supers.item(x);
            if (n.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            final Element addSuper = (Element) n;
            final String className = getValue("classname", addSuper);
            final String superClass = getValue("super", addSuper);
            final Super sup = new Super(className, superClass);
            superList.add(sup);
        }
        return superList.toArray(new Super[superList.size()]);
    }

    @Override
    public Getter[] getGetters() {
        final NodeList getterRootList = doc.getElementsByTagName("getters");
        switch (getterRootList.getLength()) {
            case 0:
                return null;
            case 1:
                break;
            default:
                throw new RuntimeException(
                        "Hook file may not contains multiple <getters> tags ");
        }
        final Node node = getterRootList.item(0);
        if (node.getNodeType() != Node.ELEMENT_NODE) {
            return null;
        }
        final Element getterRoot = (Element) node;
        final NodeList getters = getterRoot.getElementsByTagName("add");
        if (getters.getLength() == 0) {
            return null;
        }
        final ArrayList<Getter> getterList = new ArrayList<Getter>();
        for (int x = 0; x < getters.getLength(); x++) {
            final Node n = getters.item(x);
            if (n.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            final Element addGetter = (Element) n;
            if (isSet("classname", addGetter) && isSet("accessor", addGetter)) {
                throw new RuntimeException(
                        "Can't set classname and accessor tag together.");
            }
            if (isSet("accessor", addGetter) && !parsedInterfaces) {
                throw new RuntimeException(
                        "You'll need to parse interfaces first.");
            }
            final String className = isSet("classname", addGetter) ? getValue(
                    "classname", addGetter) : interfaceMap.get(getValue(
                    "accessor", addGetter));
            final String into = isSet("into", addGetter) ? getValue("into",
                    addGetter) : className;
            final long multiplier = isSet("multiplier", addGetter) ? Long.parseLong(getValue("multiplier", addGetter)) : 0L;
            final String fieldName = getValue("field", addGetter);
            final String fieldDesc = isSet("descfield", addGetter) ? getValue("descfield", addGetter) : null;
            final String methodName = getValue("methodname", addGetter);
            boolean staticMethod = isSet("methstatic", addGetter) && (getValue(
                    "methstatic", addGetter).equals("true"));
            String returnDesc = isSet("desc", addGetter) ? getValue("desc",
                    addGetter) : null;
            String array = "";
            if (returnDesc != null && returnDesc.contains("%s")) {
                StringBuilder str = new StringBuilder();
                if (returnDesc.startsWith("[")) {
                    for (int i = 0; i < returnDesc.length(); i++) {
                        if (returnDesc.charAt(i) == '[') {
                            array += '[';
                        }
                    }
                    returnDesc = returnDesc.replaceAll("\\[", "");
                }
                str.append(array)
                        .append('L')
                        .append(String.format(returnDesc,
                                AddInterfaceAdapter.getAccessorPackage()))
                        .append(";");
                returnDesc = str.toString();
            }
            final Getter get = new Getter(into, className, fieldName,
                    methodName, returnDesc, staticMethod, multiplier, fieldDesc);
            getterList.add(get);
        }
        Core.verbose("Fields hooked: " + getterList.size());
        return getterList.toArray(new Getter[getterList.size()]);
    }

    @Override
    public Setter[] getSetters() {
        final NodeList setterRootList = doc.getElementsByTagName("setters");
        switch (setterRootList.getLength()) {
            case 0:
                return null;
            case 1:
                break;
            default:
                throw new RuntimeException(
                        "Hook file may not contains multiple <setters> tags ");
        }
        final Node node = setterRootList.item(0);
        if (node.getNodeType() != Node.ELEMENT_NODE) {
            return null;
        }
        final Element setterRoot = (Element) node;
        final NodeList setters = setterRoot.getElementsByTagName("add");
        if (setters.getLength() == 0) {
            return null;
        }
        final ArrayList<Setter> setterList = new ArrayList<Setter>();
        for (int x = 0; x < setters.getLength(); x++) {
            final Node n = setters.item(x);
            if (n.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            final Element addSetter = (Element) n;
            if (isSet("classname", addSetter) && isSet("accessor", addSetter)) {
                throw new RuntimeException(
                        "Can't set classname and accessor tag together.");
            }
            if (isSet("accessor", addSetter) && !parsedInterfaces) {
                throw new RuntimeException(
                        "You'll need to parse interfaces first.");
            }
            final String className = isSet("classname", addSetter) ? getValue(
                    "classname", addSetter) : interfaceMap.get(getValue(
                    "accessor", addSetter));
            final String into = isSet("into", addSetter) ? getValue("into",
                    addSetter) : className;
            final String fieldName = getValue("field", addSetter);
            final String fieldDesc = isSet("descfield", addSetter) ? getValue("descfield", addSetter) : null;
            final String methodName = getValue("methodname", addSetter);
            boolean staticMethod = isSet("methstatic", addSetter) && (getValue(
                    "methstatic", addSetter).equals("true"));
            String returnDesc = isSet("desc", addSetter) ? getValue("desc",
                    addSetter) : null;
            String array = "";
            if (returnDesc != null && returnDesc.contains("%s")) {
                StringBuilder str = new StringBuilder();
                if (returnDesc.startsWith("[")) {
                    for (int i = 0; i < returnDesc.length(); i++) {
                        if (returnDesc.charAt(i) == '[') {
                            array += '[';
                        }
                    }
                    returnDesc = returnDesc.replaceAll("\\[", "");
                }
                str.append(array)
                        .append('L')
                        .append(String.format(returnDesc,
                                AddInterfaceAdapter.getAccessorPackage()))
                        .append(";");
                returnDesc = str.toString();
            }
            final Setter get = new Setter(className, into, fieldName,
                    methodName, returnDesc, staticMethod, fieldDesc);
            setterList.add(get);
        }
        return setterList.toArray(new Setter[setterList.size()]);
    }

    @Override
    public Invoker[] getInvokers() {
        final NodeList invokerRootList = doc.getElementsByTagName("invokers");
        switch (invokerRootList.getLength()) {
            case 0:
                return null;
            case 1:
                break;
            default:
                throw new RuntimeException(
                        "Hook file may not contains multiple <invokers> tags ");
        }
        final Node node = invokerRootList.item(0);
        if (node.getNodeType() != Node.ELEMENT_NODE) {
            return null;
        }
        final Element invokerRoot = (Element) node;
        final NodeList invokers = invokerRoot.getElementsByTagName("add");
        if (invokers.getLength() == 0) {
            return null;
        }
        final ArrayList<Invoker> invokerList = new ArrayList<Invoker>();
        for (int x = 0; x < invokers.getLength(); x++) {
            final Node n = invokers.item(x);
            if (n.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            final Element addInvoker = (Element) n;
            if (isSet("classname", addInvoker) && isSet("accessor", addInvoker)) {
                throw new RuntimeException(
                        "Can't set classname and accessor tag together.");
            }
            if (isSet("accessor", addInvoker) && !parsedInterfaces) {
                throw new RuntimeException(
                        "You'll need to parse interfaces first.");
            }
            final String className = isSet("classname", addInvoker) ? getValue(
                    "classname", addInvoker) : interfaceMap.get(getValue(
                    "accessor", addInvoker));
            final String into = isSet("into", addInvoker) ? getValue("into",
                    addInvoker) : className;
            final String methodName = getValue("methodname", addInvoker);
            final String invMethodName = getValue("invokemethod", addInvoker);
            final String argsDesc = getValue("argsdesc", addInvoker);
            String returnDesc = isSet("desc", addInvoker) ? resolveDesc(getValue(
                    "desc", addInvoker)) : null;

            final boolean isInterface = isSet("interface", addInvoker) && Boolean.parseBoolean(getValue("interface", addInvoker));
            final String instanceCast = isSet("instancecast", addInvoker) ? getValue("instancecast", addInvoker) : null;
            final String checkCastArgsDesc = isSet("castargs", addInvoker) ? getValue("castargs", addInvoker) : null;

            final Invoker invoker = new Invoker(into, className, invMethodName,
                    argsDesc, returnDesc, methodName, isInterface, instanceCast, checkCastArgsDesc);
            invokerList.add(invoker);
        }
        return invokerList.toArray(new Invoker[invokerList.size()]);
    }

    @Override
    public HashMap<String, String> getConstants() {
        if (!constants.isEmpty()) {
            return constants;
        }
        final NodeList constantsRootList = doc
                .getElementsByTagName("constants");
        switch (constantsRootList.getLength()) {
            case 0:
                return null;
            case 1:
                break;
            default:
                throw new RuntimeException(
                        "Hook file may not contains multiple <constants> tags ");
        }
        final Node node = constantsRootList.item(0);
        if (node.getNodeType() != Node.ELEMENT_NODE) {
            return null;
        }
        final Element constantRoot = (Element) node;
        final NodeList constantsList = constantRoot.getElementsByTagName("add");
        if (constantsList.getLength() == 0) {
            // return empty hashmap
            return constants;
        }
        for (int x = 0; x < constantsList.getLength(); x++) {
            final Node n = constantsList.item(x);
            if (n.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            final Element addConstant = (Element) n;
            final String key = getValue("key", addConstant);
            final String value = getValue("value", addConstant);
            constants.put(key, value);
        }
        return constants;
    }

    @Override
    public Callback[] getCallbacks() {
        final NodeList callbackRootList = doc.getElementsByTagName("callbacks");
        switch (callbackRootList.getLength()) {
            case 0:
                return null;
            case 1:
                break;
            default:
                throw new RuntimeException(
                        "Hook file may not contains multiple <callbacks> tags ");
        }
        final Node node = callbackRootList.item(0);
        if (node.getNodeType() != Node.ELEMENT_NODE) {
            return null;
        }
        final Element callbackRoot = (Element) node;
        final NodeList callbacks = callbackRoot.getElementsByTagName("add");
        if (callbacks.getLength() == 0) {
            return null;
        }
        final ArrayList<Callback> callbackList = new ArrayList<Callback>();
        for (int x = 0; x < callbacks.getLength(); x++) {
            final Node n = callbacks.item(x);
            if (n.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            final Element addCallback = (Element) n;
            if (isSet("classname", addCallback)
                    && isSet("accessor", addCallback)) {
                throw new RuntimeException(
                        "Can't set classname and accessor tag together.");
            }
            if (isSet("accessor", addCallback) && !parsedInterfaces) {
                throw new RuntimeException(
                        "You'll need to parse interfaces first.");
            }
            final String className = isSet("classname", addCallback) ? getValue(
                    "classname", addCallback) : interfaceMap.get(getValue(
                    "accessor", addCallback));

            final String methodName = getValue("methodname", addCallback);
            final String callClass = getValue("callclass", addCallback);
            final String callMethod = getValue("callmethod", addCallback);
            final String callDesc = getValue("calldesc", addCallback);
            final String callArgs = getValue("callargs", addCallback);
            final String desc = getValue("desc", addCallback);
            final boolean conditional = isSet("conditional", addCallback);

            final Callback callback = new Callback(className, methodName, desc,
                    callClass, callMethod, callDesc, callArgs, conditional);
            callbackList.add(callback);
        }
        return callbackList.toArray(new Callback[callbackList.size()]);
    }

    private static String resolveDesc(String returnDesc) {
        String array = "";
        if (returnDesc != null && returnDesc.contains("%s")) {
            StringBuilder str = new StringBuilder();
            if (returnDesc.startsWith("[")) {
                for (int i = 0; i < returnDesc.length(); i++) {
                    if (returnDesc.charAt(i) == '[') {
                        array += '[';
                    }
                }
                returnDesc = returnDesc.replaceAll("\\[", "");
            }
            str.append(array)
                    .append('L')
                    .append(String.format(returnDesc,
                            AddInterfaceAdapter.getAccessorPackage()))
                    .append(";");
            returnDesc = str.toString();
        }
        return returnDesc;
    }

    private static final boolean isSet(String tag, Element element) {
        return element.getElementsByTagName(tag).getLength() > 0;
    }

    private static final String getValue(String tag, Element element) {
        if (element.getElementsByTagName(tag).item(0) == null) {
            throw new NullPointerException("MISSING HOOK TAG: The '" + tag + "' xml tag is missing from one of the hooks of type: " + element.getParentNode().getNodeName());
        }
        NodeList nodes = element.getElementsByTagName(tag).item(0)
                .getChildNodes();
        if (nodes.getLength() == 0 || nodes.item(0) == null) {
            if (Core.inVerboseMode()) {
                System.err.println("WARNING: Invalid Hook " + tag + " subnode. Tag is missing or empty?");
            }
            return "";
        }
        Node node = nodes.item(0);
        return node.getNodeValue();
    }

}
