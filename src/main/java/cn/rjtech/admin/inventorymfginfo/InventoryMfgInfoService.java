package cn.rjtech.admin.inventorymfginfo;

import cn.hutool.core.util.ObjUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.InventoryMfgInfo;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;

import java.util.Optional;

/**
 * 料品生产档案
 *
 * @ClassName: InventoryMfgInfoService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-24 13:56
 */
public class InventoryMfgInfoService extends BaseService<InventoryMfgInfo> {
    private final InventoryMfgInfo dao = new InventoryMfgInfo().dao();

    @Override
    protected InventoryMfgInfo dao() {
        return dao;
    }

    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

    /**
     * 后台管理数据查询
     *
     * @param pageNumber            第几页
     * @param pageSize              每页几条数据
     * @param cProdCalendarTypeSn   生产日历
     * @param cSupplyCalendarTypeSn 供应日历
     * @param isIQC1                是否来料质检 1开启
     * @param isIQC2                是否出货检验 1开启
     * @return
     */
    public Page<InventoryMfgInfo> getAdminDatas(int pageNumber, int pageSize, String cProdCalendarTypeSn, String cSupplyCalendarTypeSn, Boolean isIQC1, Boolean isIQC2) {
        //创建sql对象
        Sql sql = selectSql().page(pageNumber, pageSize);
        //sql条件处理
        sql.eq("cProdCalendarTypeSn", cProdCalendarTypeSn);
        sql.eq("cSupplyCalendarTypeSn", cSupplyCalendarTypeSn);
        sql.eqBooleanToChar("isIQC1", isIQC1);
        sql.eqBooleanToChar("isIQC2", isIQC2);
        //排序
        sql.desc("iAutoId");
        return paginate(sql);
    }

    /**
     * 保存
     *
     * @param inventoryMfgInfo
     * @return
     */
    public Ret save(InventoryMfgInfo inventoryMfgInfo) {
        if ((inventoryMfgInfo.getIPurchasePeriod() == null)) {
            inventoryMfgInfo.setIPurchasePeriod(0);
        }
        if (inventoryMfgInfo == null || isOk(inventoryMfgInfo.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //if(existsName(inventoryMfgInfo.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = inventoryMfgInfo.save();
        if (success) {
            //添加日志
            //addSaveSystemLog(inventoryMfgInfo.getIAutoId(), JBoltUserKit.getUserId(), inventoryMfgInfo.getName());
        }
        return ret(success);
    }

    /**
     * 更新
     *
     * @param inventoryMfgInfo
     * @return
     */
    public Ret update(InventoryMfgInfo inventoryMfgInfo) {
        if (inventoryMfgInfo == null || notOk(inventoryMfgInfo.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //更新时需要判断数据存在
        InventoryMfgInfo dbInventoryMfgInfo = findById(inventoryMfgInfo.getIAutoId());
        if (dbInventoryMfgInfo == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        //if(existsName(inventoryMfgInfo.getName(), inventoryMfgInfo.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = inventoryMfgInfo.update();
        if (success) {
            //添加日志
            //addUpdateSystemLog(inventoryMfgInfo.getIAutoId(), JBoltUserKit.getUserId(), inventoryMfgInfo.getName());
        }
        return ret(success);
    }

    /**
     * 删除数据后执行的回调
     *
     * @param inventoryMfgInfo 要删除的model
     * @param kv               携带额外参数一般用不上
     * @return
     */
    @Override
    protected String afterDelete(InventoryMfgInfo inventoryMfgInfo, Kv kv) {
        //addDeleteSystemLog(inventoryMfgInfo.getIAutoId(), JBoltUserKit.getUserId(),inventoryMfgInfo.getName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param inventoryMfgInfo model
     * @param kv               携带额外参数一般用不上
     * @return
     */
    @Override
    public String checkInUse(InventoryMfgInfo inventoryMfgInfo, Kv kv) {
        //这里用来覆盖 检测是否被其它表引用
        return null;
    }

    /**
     * toggle操作执行后的回调处理
     */
    @Override
    protected String afterToggleBoolean(InventoryMfgInfo inventoryMfgInfo, String column, Kv kv) {
        //addUpdateSystemLog(inventoryMfgInfo.getIAutoId(), JBoltUserKit.getUserId(), inventoryMfgInfo.getName(),"的字段["+column+"]值:"+inventoryMfgInfo.get(column));
        /**
         switch(column){
         case "isIQC1":
         break;
         case "isIQC2":
         break;
         }
         */
        return null;
    }

    /**
     * 查询初物按钮是否打开，来料质检开关
     */
    public boolean getIsIqc1(String cinvcode) {
        String bool = dbTemplate("inventorymfginfo.inventoryMfgInfo", Okv.by("cinvcode", cinvcode).set("iorgid", getOrgId())).queryStr();
        ValidationUtils.notNull(bool, String.format("存货“%s”未定义来料质检开关", cinvcode));
        return ObjUtil.notEqual(bool, ZERO_STR);
    }

    /**
     * 查询初物按钮是否打开，出货质检开关
     */
    public Boolean getIsIqc2(Long iInventoryId) {
        InventoryMfgInfo inventoryMfgInfo = findFirst(selectSql().eq("iInventoryId", iInventoryId));
        return Optional.ofNullable(inventoryMfgInfo).map(InventoryMfgInfo::getIsIQC2).orElse(false);
    }
}