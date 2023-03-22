package cn.rjtech.base.service;

import cn.jbolt.core.kit.OrgAccessKit;
import cn.jbolt.core.model.base.JBoltBaseModel;
import cn.jbolt.core.service.base.JBoltBaseService;
import cn.rjtech.admin.descflexfielddef.DescflexfielddefService;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Record;

/**
 * 基础服务
 *
 * @author Kephon
 */
public abstract class BaseService<M extends JBoltBaseModel<M>> extends JBoltBaseService<M> {

    @Inject
    protected DescflexfielddefService descflexfielddefService;

    protected void tableDescflexfielddefDataSave(Long iautoid, Record record) {
        descflexfielddefService.tableDataSave(iautoid, record);
    }

    protected void tableDescflexfielddefDataSave(Long iautoid, Kv kv) {
        descflexfielddefService.tableDataSave(iautoid, kv);
    }

    /**
     * 获取登录的组织ID
     *
     * @return 可能为NULL，即非组织方式登录
     */
    protected Long getOrgId() {
        return OrgAccessKit.getOrgId();
    }

    /**
     * 获取登录的组织编码
     *
     * @return 可能为NULL，即非组织方式登录
     */
    protected String getOrgCode() {
        return OrgAccessKit.getOrgCode();
    }

    /**
     * 获取登录的组织编码
     *
     * @return 可能为NULL，即非组织方式登录
     */
    public String getOrgName() {
        return OrgAccessKit.getOrgName();
    }

}
