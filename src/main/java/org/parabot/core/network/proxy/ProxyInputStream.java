package org.parabot.core.network.proxy;

import java.io.*;
import org.parabot.core.GameSync;

final class ProxyInputStream extends FilterInputStream{
    
    public ProxyInputStream(InputStream in){
        super(in);
    }
    
    @Override
    public int available() throws IOException{
        GameSync.unlock(); // give other stuff a chance to run
        GameSync.lock(); // now only the game can currently run
        return super.available();
    }
}