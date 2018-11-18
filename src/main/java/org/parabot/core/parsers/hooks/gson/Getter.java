
package org.parabot.core.parsers.hooks.gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Getter {

    @SerializedName("accessor")
    @Expose
    private String accessor;
    @SerializedName("field")
    @Expose
    private String field;
    @SerializedName("methodname")
    @Expose
    private String methodname;
    @SerializedName("desc")
    @Expose
    private String desc;
    @SerializedName("descfield")
    @Expose
    private String descfield;
    @SerializedName("classname")
    @Expose
    private String classname;
    @SerializedName("into")
    @Expose
    private String into;

    public String getAccessor() {
        return accessor;
    }

    public void setAccessor(String accessor) {
        this.accessor = accessor;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDescfield() {
        return descfield;
    }

    public void setDescfield(String descfield) {
        this.descfield = descfield;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getInto() {
        return into;
    }

    public void setInto(String into) {
        this.into = into;
    }

}
