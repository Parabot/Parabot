package org.parabot.core.asm.hooks;

import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.parabot.core.parsers.HookParser;
import org.parabot.core.parsers.JSONHookParser;
import org.parabot.core.parsers.XMLHookParser;
import org.parabot.environment.api.utils.WebUtil;

public class HookFile {
	public static final int TYPE_XML = 0;
	public static final int TYPE_JSON = 1;
	
	private URL url;
	private int type;
	
	public HookFile(File file, int type) throws MalformedURLException {
		this(file.toURI().toURL(), type);
	}
	
	public HookFile(URL url, int type) {
		setType(type);
		this.url = url;
		this.type = type;
	}
	
	private void setType(int type) {
		if(type < 0 || type > 1) {
			throw new IllegalArgumentException("This type does not exist");
		}
	}
	
	public InputStream getInputStream() {
		return WebUtil.getInputStream(url);
	}
	
	public HookParser getParser() {
		switch(type) {
		case TYPE_XML:
			return new XMLHookParser(this);
		case TYPE_JSON:
			return new JSONHookParser(this);
		}
		return null;
	}

}
