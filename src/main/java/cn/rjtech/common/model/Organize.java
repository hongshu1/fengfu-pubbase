package cn.rjtech.common.model;

import cn.jbolt.core.annotation.TableBind;
import cn.jbolt.core.base.JBoltIDGenMode;
import cn.rjtech.common.model.base.BaseOrganize;

/**
 * 组织表
 * Generated by JBolt.
 */
@TableBind(dataSource = "momdata" , table = "T_Sys_Organize" , primaryKey = "id" , idGenMode = JBoltIDGenMode.SNOWFLAKE)
public class Organize extends BaseOrganize<Organize> {
}

