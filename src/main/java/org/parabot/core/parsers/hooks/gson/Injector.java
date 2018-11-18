
package org.parabot.core.parsers.hooks.gson;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Injector {

    @SerializedName("interfaces")
    @Expose
    private List<Interface> interfaces = null;
    @SerializedName("getters")
    @Expose
    private List<Getter> getters = null;
    @SerializedName("setters")
    @Expose
    private Setters setters;
    @SerializedName("callbacks")
    @Expose
    private List<Callback> callbacks = null;
    @SerializedName("invokers")
    @Expose
    private List<Invoker> invokers = null;

    public List<Interface> getInterfaces() {
        return interfaces;
    }

    public void setInterfaces(List<Interface> interfaces) {
        this.interfaces = interfaces;
    }

    public List<Getter> getGetters() {
        return getters;
    }

    public void setGetters(List<Getter> getters) {
        this.getters = getters;
    }

    public Setters getSetters() {
        return setters;
    }

    public void setSetters(Setters setters) {
        this.setters = setters;
    }

    public List<Callback> getCallbacks() {
        return callbacks;
    }

    public void setCallbacks(List<Callback> callbacks) {
        this.callbacks = callbacks;
    }

    public List<Invoker> getInvokers() {
        return invokers;
    }

    public void setInvokers(List<Invoker> invokers) {
        this.invokers = invokers;
    }

}
