package org.parabot.core.asm.hooks;

import org.parabot.api.io.WebUtil;
import org.parabot.core.parsers.hooks.HookParser;
import org.parabot.core.parsers.hooks.JSONHookParser;
import org.parabot.core.parsers.hooks.XMLHookParser;

import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class HookFile {
    public static final int TYPE_XML  = 0;
    public static final int TYPE_JSON = 1;

    private URL url;
    private int type;

    public HookFile(File file, int type) throws MalformedURLException {
        this(file.toURI().toURL(), type);
    }

    public HookFile(URL url, int type) {
        setType(type);
        this.url = url;
    }

    private void setType(int type) {
        if (type < 0 || type > 1) {
            throw new IllegalArgumentException("This type does not exist");
        }
        this.type = type;
    }

    public InputStream getInputStream() {
        return WebUtil.getInputStream(url);
    }

    public HookParser getParser() {
        switch (type) {
            case TYPE_XML:
                return new XMLHookParser(this);
            case TYPE_JSON:
                return new JSONHookParser(this);
        }
        return null;
    }

}
