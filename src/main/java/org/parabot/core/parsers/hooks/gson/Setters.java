
package org.parabot.core.parsers.hooks.gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Setters {

    @SerializedName("accessor")
    @Expose
    private String accessor;
    @SerializedName("into")
    @Expose
    private String into;
    @SerializedName("field")
    @Expose
    private String field;
    @SerializedName("methodname")
    @Expose
    private String methodname;
    @SerializedName("descfield")
    @Expose
    private String descfield;

    public String getAccessor() {
        return accessor;
    }

    public void setAccessor(String accessor) {
        this.accessor = accessor;
    }

    public String getInto() {
        return into;
    }

    public void setInto(String into) {
        this.into = into;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getMethodname() {
        return methodname;
    }

    public void setMethodname(String methodname) {
        this.methodname = methodname;
    }

    public String getDescfield() {
        return descfield;
    }

    public void setDescfield(String descfield) {
        this.descfield = descfield;
    }

}
