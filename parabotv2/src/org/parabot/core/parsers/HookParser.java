package org.parabot.core.parsers;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.parabot.core.asm.adapters.AddInterfaceAdapter;
import org.parabot.core.asm.interfaces.Injectable;
import org.parabot.core.asm.wrappers.Getter;
import org.parabot.core.asm.wrappers.Interface;
import org.parabot.core.asm.wrappers.Super;
import org.parabot.environment.api.utils.WebUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 
 * Parses an XML files which injects the hooks and other bytecode manipulation methods
 * 
 * @author Clisprail
 *
 */
public class HookParser {
	private Document doc = null;
	private boolean parsedInterfaces = false;
	private HashMap<String, String> interfaceMap = new HashMap<String, String>();
	
	public HookParser(URL url) {
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(WebUtil.getInputStream(url));
			doc.getDocumentElement().normalize();
			if(!doc.getDocumentElement().getNodeName().equals("injector")) {
				throw new RuntimeException("Incorrect hook file.");
			}
		} catch (Throwable t) {
			throw new RuntimeException("Unable to parse hooks " + t);
		}
	}
	
	public final Interface[] getInterfaces() {
		parsedInterfaces = true;
		final NodeList interfaceRootList = doc.getElementsByTagName("interfaces");
		switch(interfaceRootList.getLength()) {
		case 0: 
			return null;
		case 1:
			break;
		default:
			throw new RuntimeException("Hook file may not contains multiple <interfaces> tags ");
		}
		final Node node = interfaceRootList.item(0);
		if(node.getNodeType() != Node.ELEMENT_NODE) {
			return null;
		}
		final Element interfaceRoot = (Element) node;
		final NodeList interfaces = interfaceRoot.getElementsByTagName("add");
		if(interfaces.getLength() == 0) {
			return null;
		}
		final ArrayList<Interface> interfaceList = new ArrayList<Interface>();
		for(int x = 0; x < interfaces.getLength(); x++) {
			final Node n = interfaces.item(x);
			if(n.getNodeType() != Node.ELEMENT_NODE) {
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
	
	public final Super[] getSupers() {
		final NodeList interfaceRootList = doc.getElementsByTagName("supers");
		switch(interfaceRootList.getLength()) {
		case 0: 
			return null;
		case 1:
			break;
		default:
			throw new RuntimeException("Hook file may not contains multiple <supers> tags ");
		}
		final Node node = interfaceRootList.item(0);
		if(node.getNodeType() != Node.ELEMENT_NODE) {
			return null;
		}
		final Element superRoot = (Element) node;
		final NodeList supers = superRoot.getElementsByTagName("add");
		if(supers.getLength() == 0) {
			return null;
		}
		final ArrayList<Super> superList = new ArrayList<Super>();
		for(int x = 0; x < supers.getLength(); x++) {
			final Node n = supers.item(x);
			if(n.getNodeType() != Node.ELEMENT_NODE) {
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
	
	public final Getter[] getGetters() {
		final NodeList getterRootList = doc.getElementsByTagName("getters");
		switch(getterRootList.getLength()) {
		case 0: 
			return null;
		case 1:
			break;
		default:
			throw new RuntimeException("Hook file may not contains multiple <getters> tags ");
		}
		final Node node = getterRootList.item(0);
		if(node.getNodeType() != Node.ELEMENT_NODE) {
			return null;
		}
		final Element getterRoot = (Element) node;
		final NodeList getters = getterRoot.getElementsByTagName("add");
		if(getters.getLength() == 0) {
			return null;
		}
		final ArrayList<Getter> getterList = new ArrayList<Getter>();
		for(int x = 0; x < getters.getLength(); x++) {
			final Node n = getters.item(x);
			if(n.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			final Element addGetter = (Element) n;
			if(isSet("classname", addGetter) && isSet("accessor", addGetter)) {
				throw new RuntimeException("Can't set classname and accessor tag together.");
			}
			if(isSet("accessor", addGetter) && !parsedInterfaces) {
				throw new RuntimeException("You'll need to parse interfaces first.");
			}
			final String className = isSet("classname", addGetter) ? getValue("classname", addGetter) : interfaceMap.get(getValue("accessor", addGetter));
			final String into = isSet("into", addGetter) ? getValue("into", addGetter) : className;
			final String fieldName = getValue("field", addGetter);
			final String methodName = getValue("methodname", addGetter);
			boolean staticMethod = isSet("methstatic", addGetter) ? (getValue("methstatic", addGetter).equals("true")) : false;
			String returnDesc = isSet("desc", addGetter) ? getValue("desc", addGetter) : null;
			String array = "";
			if(returnDesc != null && returnDesc.contains("%s")) {
				StringBuilder str = new StringBuilder();
				if(returnDesc.startsWith("[")) {
					for( int i=0; i<returnDesc.length(); i++ ) {
					    if( returnDesc.charAt(i) == '[' ) {
					        array += '[';
					    } 
					}
					returnDesc = returnDesc.replaceAll("\\[", "");
				}
				str.append(array).append('L').append(String.format(returnDesc, AddInterfaceAdapter.getAccessorPackage())).append(";");
				returnDesc = str.toString();
			}
			final Getter get = new Getter(into, className, fieldName, methodName, returnDesc, staticMethod);
			getterList.add(get);
		}
		return getterList.toArray(new Getter[getterList.size()]);
	}
	
	public Injectable[] getInjectables() {
		ArrayList<Injectable> injectables = new ArrayList<Injectable>();
		Interface[] interfaces = getInterfaces();
		if(interfaces != null) {
			for(Interface inf : interfaces) {
				injectables.add(inf);
			}
		}
		Getter[] getters = getGetters();
		if(getters != null) {
			for(Getter get : getters) {
				injectables.add(get);
			}
		}
		Super[] supers = getSupers();
		if(supers != null) {
			for(Super sup : supers) {
				injectables.add(sup);
			}
		}
		return injectables.toArray(new Injectable[injectables.size()]);
	}
	
	private static final boolean isSet(String tag, Element element) {
		return element.getElementsByTagName(tag).getLength() > 0;
	}
	
	private static final String getValue(String tag, Element element) {
		NodeList nodes = element.getElementsByTagName(tag).item(0).getChildNodes();
		Node node = (Node) nodes.item(0);
		return node.getNodeValue();
	}
	
	

}
