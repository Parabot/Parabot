
package org.parabot.core.parsers.hooks.gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Callback {

    @SerializedName("accessor")
    @Expose
    private String accessor;
    @SerializedName("methodname")
    @Expose
    private String methodname;
    @SerializedName("desc")
    @Expose
    private String desc;
    @SerializedName("callclass")
    @Expose
    private String callclass;
    @SerializedName("callmethod")
    @Expose
    private String callmethod;
    @SerializedName("calldesc")
    @Expose
    private String calldesc;
    @SerializedName("callargs")
    @Expose
    private String callargs;

    public String getAccessor() {
        return accessor;
    }

    public void setAccessor(String accessor) {
        this.accessor = accessor;
    }

    public String getMethodname() {
        return methodname;
    }

    public void setMethodname(String methodname) {
        this.methodname = methodname;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCallclass() {
        return callclass;
    }

    public void setCallclass(String callclass) {
        this.callclass = callclass;
    }

    public String getCallmethod() {
        return callmethod;
    }

    public void setCallmethod(String callmethod) {
        this.callmethod = callmethod;
    }

    public String getCalldesc() {
        return calldesc;
    }

    public void setCalldesc(String calldesc) {
        this.calldesc = calldesc;
    }

    public String getCallargs() {
        return callargs;
    }

    public void setCallargs(String callargs) {
        this.callargs = callargs;
    }

}
