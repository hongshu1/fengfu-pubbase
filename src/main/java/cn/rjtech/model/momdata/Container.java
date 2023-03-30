package cn.rjtech.model.momdata;

import cn.jbolt.core.gen.JBoltField;
import cn.rjtech.model.momdata.base.BaseContainer;
import cn.jbolt.core.annotation.TableBind;
import cn.jbolt.core.base.JBoltIDGenMode;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 仓库建模-容器档案
 * Generated by JBolt.
 */
@SuppressWarnings("serial")
@TableBind(dataSource = "momdata" , table = "Bd_Container" , primaryKey = "iAutoId" , idGenMode = JBoltIDGenMode.SNOWFLAKE)
public class Container extends BaseContainer<Container> {
    /**备注*/
    private String cMemo;

    /**
     * 备注
     */
    public void setCMemo(java.lang.String cMemo) {
        this.cMemo = cMemo;
    }

    /**
     * 备注
     */
    public java.lang.String getCMemo() {
        return cMemo;
    }
}

