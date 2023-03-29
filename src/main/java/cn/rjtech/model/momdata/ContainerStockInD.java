package cn.rjtech.model.momdata;

import cn.rjtech.model.momdata.base.BaseContainerStockInD;
import cn.jbolt.core.annotation.TableBind;
import cn.jbolt.core.base.JBoltIDGenMode;

/**
 * 容器档案-容器入库明细
 * Generated by JBolt.
 */
@SuppressWarnings("serial")
@TableBind(dataSource = "momdata" , table = "Bd_ContainerStockInD" , primaryKey = "iAutoId" , idGenMode = JBoltIDGenMode.SNOWFLAKE)
public class ContainerStockInD extends BaseContainerStockInD<ContainerStockInD> {
}

