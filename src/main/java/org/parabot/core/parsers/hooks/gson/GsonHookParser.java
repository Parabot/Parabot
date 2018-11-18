
package org.parabot.core.parsers.hooks.gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GsonHookParser {

    @SerializedName("injector")
    @Expose
    private Injector injector;

    public Injector getInjector() {
        return injector;
    }

    public void setInjector(Injector injector) {
        this.injector = injector;
    }

}
