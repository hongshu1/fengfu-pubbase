package cn.rjtech.admin.syspuinstore;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.User;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.rjtech.constants.ErrorMsg;
import cn.rjtech.model.momdata.SysAssem;
import cn.rjtech.util.ValidationUtils;

import com.jfinal.aop.Inject;
import com.jfinal.plugin.activerecord.Page;

import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;

import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.rjtech.model.momdata.SysPuinstore;

import com.jfinal.plugin.activerecord.Record;

import java.util.Date;
import java.util.List;

/**
 * 采购入库单
 *
 * @ClassName: SysPuinstoreService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-09 15:38
 */
public class SysPuinstoreService extends BaseService<SysPuinstore> {

    private final SysPuinstore dao = new SysPuinstore().dao();

    @Override
    protected SysPuinstore dao() {
        return dao;
    }

    @Inject
    private SysPuinstoredetailService syspuinstoredetailservice;

    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

    /**
     * 后台管理数据查询
     *
     * @param pageNumber      第几页
     * @param pageSize        每页几条数据
     * @param keywords        关键词
     * @param BillType        到货单类型;采购PO  委外OM
     * @param procureType     采购类型
     * @param warehousingType 入库类别
     */
    public Page<SysPuinstore> getAdminDatas(int pageNumber, int pageSize, String keywords, String BillType, String procureType,
                                            String warehousingType) {
        //创建sql对象
        Sql sql = selectSql().page(pageNumber, pageSize);
        //sql条件处理
        sql.eq("BillType", BillType);
        sql.eq("procureType", procureType);
        sql.eq("warehousingType", warehousingType);
        //关键词模糊查询
        sql.likeMulti(keywords, "repositoryName", "deptName", "remark");
        //排序
        sql.desc("AutoID");
        return paginate(sql);
    }

    /**
     * 保存
     */
    public Ret save(SysPuinstore sysPuinstore) {
        if (sysPuinstore == null || isOk(sysPuinstore.getAutoID())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //if(existsName(sysPuinstore.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = sysPuinstore.save();
        if (success) {
            //添加日志
            //addSaveSystemLog(sysPuinstore.getAutoID(), JBoltUserKit.getUserId(), sysPuinstore.getName());
        }
        return ret(success);
    }

    /**
     * 更新
     */
    public Ret update(SysPuinstore sysPuinstore) {
        if (sysPuinstore == null || notOk(sysPuinstore.getAutoID())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //更新时需要判断数据存在
        SysPuinstore dbSysPuinstore = findById(sysPuinstore.getAutoID());
        if (dbSysPuinstore == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        //if(existsName(sysPuinstore.getName(), sysPuinstore.getAutoID())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = sysPuinstore.update();
        if (success) {
            //添加日志
            //addUpdateSystemLog(sysPuinstore.getAutoID(), JBoltUserKit.getUserId(), sysPuinstore.getName());
        }
        return ret(success);
    }

    /**
     * 删除数据后执行的回调
     *
     * @param sysPuinstore 要删除的model
     * @param kv           携带额外参数一般用不上
     */
    @Override
    protected String afterDelete(SysPuinstore sysPuinstore, Kv kv) {
        //addDeleteSystemLog(sysPuinstore.getAutoID(), JBoltUserKit.getUserId(),sysPuinstore.getName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param sysPuinstore model
     * @param kv           携带额外参数一般用不上
     */
    @Override
    public String checkInUse(SysPuinstore sysPuinstore, Kv kv) {
        //这里用来覆盖 检测是否被其它表引用
        return null;
    }

    /**
     * toggle操作执行后的回调处理
     */
    @Override
    protected String afterToggleBoolean(SysPuinstore sysPuinstore, String column, Kv kv) {
        //addUpdateSystemLog(sysPuinstore.getAutoID(), JBoltUserKit.getUserId(), sysPuinstore.getName(),"的字段["+column+"]值:"+sysPuinstore.get(column));
        /**
         switch(column){
         case "IsDeleted":
         break;
         }
         */
        return null;
    }

    /**
     * 后台管理数据查询
     *
     * @param pageNumber 第几页
     * @param pageSize   每页几条数据
     */
    public Page<Record> getAdminDatas(int pageNumber, int pageSize, Kv kv) {
        return dbTemplate("syspuinstore.recpor", kv).paginate(pageNumber, pageSize);
    }

    /**
     * 删除
     */
    public Ret delete(Long id) {
        tx(() -> {
            //todo 删除主表和字表的数据
            return true;
        });
        return ret(true);
    }

    /**
     * 批量删除主从表
     */
    public Ret deleteRmRdByIds(String ids) {
        tx(() -> {
            String[] split = ids.split(",");
            for (String id : split) {
                //todo 先删除自表，再删除主表
            }
            return true;
        });
        return ret(true);
    }

    /**
     * 执行JBoltTable表格整体提交
     */
    public Ret submitByJBoltTable(JBoltTable jBoltTable) {
        SysPuinstore sysotherin = jBoltTable.getFormModel(SysPuinstore.class, "sysPuinstore");
        //获取当前用户信息？
        User user = JBoltUserKit.getUser();
        Date now = new Date();
        /*tx(() -> {
            //通过 id 判断是新增还是修改
            if (sysotherin.getAutoID() == null) {
                sysotherin.setOrganizeCode(getOrgCode());
                sysotherin.setCreatePerson(user.getUsername());
                sysotherin.setCreateDate(now);
                sysotherin.setModifyPerson(user.getUsername());
                sysotherin.setState("1");
                sysotherin.setModifyDate(now);
                //主表新增
                ValidationUtils.isTrue(sysotherin.save(), ErrorMsg.SAVE_FAILED);
            } else {
                sysotherin.setModifyPerson(user.getUsername());
                sysotherin.setModifyDate(now);
                //主表修改
                ValidationUtils.isTrue(sysotherin.update(), ErrorMsg.UPDATE_FAILED);
            }
            //从表的操作
            // 获取保存数据（执行保存，通过 getSaveRecordList）
            saveTableSubmitDatas(jBoltTable, sysotherin);
            //获取修改数据（执行修改，通过 getUpdateRecordList）
            updateTableSubmitDatas(jBoltTable, sysotherin);
            //获取删除数据（执行删除，通过 getDelete）
            deleteTableSubmitDatas(jBoltTable);
            return true;
        });*/
        return SUCCESS;
    }

    //可编辑表格提交-新增数据
    private void saveTableSubmitDatas(JBoltTable jBoltTable, SysPuinstore sysotherin) {
        List<Record> list = jBoltTable.getSaveRecordList();
        if (CollUtil.isEmpty(list)) {
            return;
        }
        Date now = new Date();
        for (int i = 0; i < list.size(); i++) {
            Record row = list.get(i);
            row.set("MasID", sysotherin.getAutoID());
            row.set("AutoID", JBoltSnowflakeKit.me.nextId());
            row.set("CreateDate", now);
            row.set("ModifyDate", now);
//			row.set("inventorycode",row.get("cinvcode"));
//			row.set("inventorycodeh",row.get("cinvcodeh"));
            row.remove("crcvdate");
            row.remove("crcvtime");
            row.remove("cbarcode");
            row.remove("cversion");
            row.remove("caddress");
            row.remove("cinvcode");
        }
        syspuinstoredetailservice.batchSaveRecords(list);
    }

    //可编辑表格提交-修改数据
    private void updateTableSubmitDatas(JBoltTable jBoltTable, SysPuinstore sysotherin) {
        List<Record> list = jBoltTable.getUpdateRecordList();
        if (CollUtil.isEmpty(list)) {
            return;
        }
        Date now = new Date();
        for (int i = 0; i < list.size(); i++) {
            Record row = list.get(i);
            row.set("ModifyDate", now);

            row.remove("crcvdate");
            row.remove("crcvtime");
            row.remove("cbarcode");
            row.remove("cversion");
            row.remove("caddress");
            row.remove("cinvcode");

        }
        syspuinstoredetailservice.batchUpdateRecords(list);
    }

    //可编辑表格提交-删除数据
    private void deleteTableSubmitDatas(JBoltTable jBoltTable) {
        Object[] ids = jBoltTable.getDelete();
        if (ArrayUtil.isEmpty(ids)) {
            return;
        }
        for (Object id : ids) {

        }
    }

    /*
     * 获取仓库名
     * */
    public List<Record> getWareHouseName(Kv kv) {
        return dbTemplate(u8SourceConfigName(), "syspuinstore.getWareHouseName", kv).find();
    }
}