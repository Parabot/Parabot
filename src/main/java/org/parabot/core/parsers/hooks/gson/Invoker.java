
package org.parabot.core.parsers.hooks.gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Invoker {

    @SerializedName("accessor")
    @Expose
    private String accessor;
    @SerializedName("methodname")
    @Expose
    private String methodname;
    @SerializedName("invokemethod")
    @Expose
    private String invokemethod;
    @SerializedName("desc")
    @Expose
    private String desc;
    @SerializedName("argsdesc")
    @Expose
    private String argsdesc;

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

    public String getInvokemethod() {
        return invokemethod;
    }

    public void setInvokemethod(String invokemethod) {
        this.invokemethod = invokemethod;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getArgsdesc() {
        return argsdesc;
    }

    public void setArgsdesc(String argsdesc) {
        this.argsdesc = argsdesc;
    }

}
