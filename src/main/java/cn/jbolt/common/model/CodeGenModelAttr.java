package cn.jbolt.common.model;

import cn.jbolt.common.model.base.BaseCodeGenModelAttr;
import cn.jbolt.common.util.CACHE;
import cn.jbolt.core.annotation.TableBind;
import cn.jbolt.core.annotation.UnProcessBoolean;
import cn.jbolt.core.base.JBoltIDGenMode;
import cn.jbolt.core.bean.JBoltDateRange;
import cn.jbolt.core.cache.JBoltDictionaryCache;
import cn.jbolt.core.cache.JBoltDictionaryTypeCache;
import cn.jbolt.core.consts.JBoltConst;
import com.jfinal.kit.StrKit;

import java.util.Locale;

/**
 * CodeGen模型详细设计
 * Generated by JBolt.
 */
@UnProcessBoolean("form_default_value")
@SuppressWarnings("serial")
@TableBind(dataSource = "main" , table = "jb_code_gen_model_attr" , primaryKey = "id" , idGenMode = JBoltIDGenMode.SNOWFLAKE)
public class CodeGenModelAttr extends BaseCodeGenModelAttr<CodeGenModelAttr> {
    public String getJavaTypeName() {
        return JBoltConst.JAVA_TYPE_MAP.get(getJavaType());
    }
    public String getJavaTypeNameForGetPara() {
        String name = getJavaTypeName();
        if("Integer".equals(name)){
            return "Int";
        }
        if("String".equals(name)){
            return "";
        }
        return name;
    }
    public String getFormUITypeName() {
        return JBoltDictionaryCache.me.getNameBySn("code_gen_form_ui_type", getFormUiType());
    }
    public String getSearchUITypeName() {
        return JBoltDictionaryCache.me.getNameBySn("code_gen_condition_ui_type", getSearchUiType());
    }
    public String getTableUITypeName() {
        return JBoltDictionaryCache.me.getNameBySn("code_gen_table_ui_type", getTableUiType());
    }
    public String getDataRuleName() {
        return JBoltDictionaryCache.me.getNameBySn("code_gen_form_data_rule", getDataRule());
    }
    public String getDataRuleForSearchName() {
        return JBoltDictionaryCache.me.getNameBySn("code_gen_form_data_rule", getDataRuleForSearch());
    }
    public String getFormDataTypeName() {
        return JBoltDictionaryCache.me.getNameBySn("code_gen_data_type", getFormDataType());
    }
    public String getColFormatName() {
        return JBoltDictionaryCache.me.getNameBySn("code_gen_table_col_format", getColFormat());
    }
    public String getFormDataValueName() {
        String dataType = getFormDataType();
        if("sys_dictionary".equals(dataType)) {
            return JBoltDictionaryTypeCache.me.getNameByKey(getFormDataValue());
        }
        return getFormDataValue();
    }

    public String getFormDefaultValueName() {
        String dataType = getFormDataType();
        String defaultValue = getFormDefaultValue();
        if("sys_dictionary".equals(dataType) && StrKit.notBlank(defaultValue)) {
            String typeKey = getFormDataValue();
            if(StrKit.isBlank(typeKey)){
                return defaultValue;
            }
            return JBoltDictionaryCache.me.getNameBySn(typeKey, defaultValue);
        }
        return defaultValue;
    }


    public String getSearchDataTypeName() {
        return JBoltDictionaryCache.me.getNameBySn("code_gen_data_type", getSearchDataType());
    }
    public String getSearchDataValueName() {
        String dataType = getSearchDataType();
        if("sys_dictionary".equals(dataType)) {
            return JBoltDictionaryTypeCache.me.getNameByKey(getSearchDataValue());
        }
        return getSearchDataValue();
    }

    public String getSearchDefaultValueName() {
        String dataType = getSearchDataType();
        String defaultValue = getSearchDefaultValue();
        if("sys_dictionary".equals(dataType) && StrKit.notBlank(defaultValue)) {
            return JBoltDictionaryCache.me.getNameBySn(getSearchDataValue(), defaultValue);
        }
        return defaultValue;
    }

    public String getSearchUIDateRangeType(){
        String type =  getSearchUiType();
        if(StrKit.notBlank(type)){
            return "JBoltDateRange.TYPE_"+type.substring(type.lastIndexOf("_")+1).toUpperCase();
        }
        return "JBoltDateRange.TYPE_DATE";
    }

}

