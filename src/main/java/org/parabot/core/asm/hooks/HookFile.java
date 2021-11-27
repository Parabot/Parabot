package org.parabot.core.asm.hooks;

import org.parabot.core.forum.AccountManager;
import org.parabot.core.parsers.hooks.HookParser;
import org.parabot.core.parsers.hooks.JSONHookParser;
import org.parabot.core.parsers.hooks.XMLHookParser;
import org.parabot.environment.api.utils.WebUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class HookFile {
    public static final int TYPE_XML = 0;
    public static final int TYPE_JSON = 1;

    private final URL url;
    private int type;

    private boolean isLocal;

    public HookFile(File file, int type) throws MalformedURLException {
        this(file.toURI().toURL(), type);
        this.isLocal = true;
    }

    public HookFile(URL url, int type) {
        setType(type);
        this.url = url;
    }

    public InputStream getInputStream() {
        return WebUtil.getInputStream(url);
    }

    public InputStream getInputStream(AccountManager manager) {
        if (isLocal) {
            return this.getInputStream();
        } else {
            try {
                return WebUtil.getConnection(url, "apikey=" + manager.getAccount().getApi()).getInputStream();
            } catch (IOException e) {
                return null;
            }
        }
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

    private void setType(int type) {
        if (type < 0 || type > 1) {
            throw new IllegalArgumentException("This type does not exist");
        }
        this.type = type;
    }

}
