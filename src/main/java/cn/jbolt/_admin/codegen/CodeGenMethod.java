package cn.jbolt._admin.codegen;

import cn.jbolt.core.util.JBoltArrayUtil;
import com.jfinal.kit.StrKit;

public class CodeGenMethod {
    private String name;
    private String value;
    private boolean isToggle;
    private String toggleColumnName;//多个是逗号
    public String[] getToggleColumns(){
        if(isToggle && StrKit.notBlank(toggleColumnName)){
            return JBoltArrayUtil.from(toggleColumnName);
        }
        return null;
    }
    public CodeGenMethod(String name){
        this.name = name;
    }
    public CodeGenMethod(String name,String value){
        this.name = name;
        this.value = value;
    }
    public CodeGenMethod(String name,boolean isToggle,String toggleColumnName){
        this.name = name;
        if(!isToggle && StrKit.isBlank(toggleColumnName)){
            throw new RuntimeException("isToggle为true时 toggleColumnName必须指定");
        }
        this.isToggle = isToggle;
        this.toggleColumnName = toggleColumnName;
    }

    public CodeGenMethod(String name,boolean isToggle,String[] toggleColumnNames){
        this(name,isToggle,JBoltArrayUtil.join(toggleColumnNames,","));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isToggle() {
        return isToggle;
    }

    public void setToggle(boolean toggle) {
        isToggle = toggle;
    }

    public String getToggleColumnName() {
        return toggleColumnName;
    }

    public void setToggleColumnName(String toggleColumnName) {
        this.toggleColumnName = toggleColumnName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
