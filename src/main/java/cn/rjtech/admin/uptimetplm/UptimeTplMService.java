package cn.rjtech.admin.uptimetplm;

import cn.hutool.core.util.NumberUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.uptimecategory.UptimeCategoryService;
import cn.rjtech.admin.uptimeparam.UptimeParamService;
import cn.rjtech.admin.uptimetplcategory.UptimeTplCategoryService;
import cn.rjtech.admin.uptimetpltable.UptimeTplTableService;
import cn.rjtech.admin.workregionm.WorkregionmService;
import cn.rjtech.admin.workshiftm.WorkshiftmService;
import cn.rjtech.cache.CusFieldsMappingdCache;
import cn.rjtech.model.momdata.*;
import cn.rjtech.util.ValidationUtils;
import com.alibaba.fastjson.JSONArray;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * 稼动时间建模-稼动时间模板主表
 *
 * @ClassName: UptimeTplMService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-06-27 19:20
 */
public class UptimeTplMService extends BaseService<UptimeTplM> {
    private final UptimeTplM dao = new UptimeTplM().dao();

    @Override
    protected UptimeTplM dao() {
        return dao;
    }

    @Inject
    private UptimeTplCategoryService uptimeTplCategoryService;
    @Inject
    private UptimeTplTableService uptimeTplTableService;
    @Inject
    private UptimeCategoryService uptimeCategoryService;
    @Inject
    private UptimeParamService uptimeParamService;
    @Inject
    private WorkregionmService workregionmService;
    @Inject
    private WorkshiftmService workshiftmService;

    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

    /**
     * 后台管理数据查询
     *
     * @param pageNumber 第几页
     * @param pageSize   每页几条数据
     * @param kv         查询参数
     */
    public Page<Record> getAdminDatas(int pageNumber, int pageSize, Kv kv) {
        return dbTemplate("uptimetplm.getAdminDatas", kv).paginate(pageNumber, pageSize);
    }

    /**
     * 保存
     */
    public Ret save(UptimeTplM uptimeTplM) {
        if (uptimeTplM == null || isOk(uptimeTplM.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        boolean success = uptimeTplM.save();
        if (success) {
            //添加日志
            //addSaveSystemLog(uptimeTplM.getIAutoId(), JBoltUserKit.getUserId(), uptimeTplM.getName());
        }
        return ret(success);
    }

    /**
     * 更新
     */
    public Ret update(UptimeTplM uptimeTplM) {
        if (uptimeTplM == null || notOk(uptimeTplM.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //更新时需要判断数据存在
        UptimeTplM dbUptimeTplM = findById(uptimeTplM.getIAutoId());
        if (dbUptimeTplM == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        boolean success = uptimeTplM.update();
        if (success) {
            //添加日志
            //addUpdateSystemLog(uptimeTplM.getIAutoId(), JBoltUserKit.getUserId(), uptimeTplM.getName());
        }
        return ret(success);
    }

    /**
     * 删除数据后执行的回调
     *
     * @param uptimeTplM 要删除的model
     * @param kv         携带额外参数一般用不上
     */
    @Override
    protected String afterDelete(UptimeTplM uptimeTplM, Kv kv) {
        //addDeleteSystemLog(uptimeTplM.getIAutoId(), JBoltUserKit.getUserId(),uptimeTplM.getName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param uptimeTplM model
     * @param kv         携带额外参数一般用不上
     */
    @Override
    public String checkInUse(UptimeTplM uptimeTplM, Kv kv) {
        //这里用来覆盖 检测是否被其它表引用
        return null;
    }

    /**
     * 保存
     */
    public Ret submitAll(Kv kv) {
        tx(() -> {
            UptimeTplM uptimeTplM = new UptimeTplM();
            uptimeTplM.setIsEnabled(kv.getBoolean("uptimeTplM.isEnabled"));
            uptimeTplM.setIUpdateBy(JBoltUserKit.getUserId());
            uptimeTplM.setCUpdateName(JBoltUserKit.getUserName());
            uptimeTplM.setDUpdateTime(new Date());
            uptimeTplM.setIWorkRegionMid(kv.getLong("uptimeTplM.iWorkRegionMid"));
            uptimeTplM.setIWorkShiftMid(kv.getLong("uptimeTplM.iWorkShiftMid"));
            uptimeTplM.setIBaseMins(kv.getInt("uptimeTplM.iBaseMins"));
            uptimeTplM.setIStopMins(kv.getInt("uptimeTplM.iStopMins"));
            uptimeTplM.setISwitchMins(kv.getInt("uptimeTplM.iSwitchMins"));
            uptimeTplM.setIOtMins(kv.getInt("uptimeTplM.iOtMins"));
            uptimeTplM.setIWorkMins(kv.getInt("uptimeTplM.iWorkMins"));
            if (isNull(kv.getLong("uptimeTplM.iAutoId"))) {
                // 校验产线及班次信息
                UptimeTplM up = findByWorkRegionMidAndWorkShiftMid(kv.getLong("uptimeTplM.iWorkRegionMid"), kv.getLong("uptimeTplM.iWorkShiftMid"));
                ValidationUtils.notNull(up, "已存在相同产线、班次模板数据");

                uptimeTplM.setIOrgId(getOrgId());
                uptimeTplM.setCOrgCode(getOrgCode());
                uptimeTplM.setCOrgName(getOrgName());
                uptimeTplM.setICreateBy(JBoltUserKit.getUserId());
                uptimeTplM.setCCreateName(JBoltUserKit.getUserName());
                uptimeTplM.setDCreateTime(new Date());
                uptimeTplM.setIsDeleted(false);
                uptimeTplM.save();
            } else {
                uptimeTplM.setIAutoId(kv.getLong("uptimeTplM.iAutoId"));
            }
            uptimeTplM.update();

            // 解析表格分类
            uptimeTplCategoryService.delectByUptimeTplMid(uptimeTplM.getIAutoId());
            List<UptimeTplCategory> uptimeTplCategoryList = JSONArray.parseArray(kv.getStr("uptimetplcategorydatas"), UptimeTplCategory.class);
            if (!uptimeTplCategoryList.isEmpty()) {
                Integer iSeq = 1;
                for (UptimeTplCategory uptimeTplCategory : uptimeTplCategoryList) {
                    if (isOk(uptimeTplCategory.getIAutoId()) && isNull(uptimeTplCategory.getIUptimeCategoryId())) {
                        uptimeTplCategory.setIUptimeCategoryId(uptimeTplCategory.getIAutoId());
                    }
                    uptimeTplCategory.setIUptimeTplMid(uptimeTplM.getIAutoId());
                    uptimeTplCategory.setIAutoId(null);
                    uptimeTplCategory.setISeq(iSeq);
                    iSeq++;
                }
                uptimeTplCategoryService.batchSave(uptimeTplCategoryList);
            }

            // 解析表格内容
            uptimeTplTableService.deleteByUptimeTplMid(uptimeTplM.getIAutoId());
            List<UptimeTplTable> uptimeTplTables = JSONArray.parseArray(kv.getStr("uptimetpltabledatas"), UptimeTplTable.class);
            if (!uptimeTplTables.isEmpty()) {
                Integer iSeq = 1;
                for (UptimeTplTable uptimeTplTable : uptimeTplTables) {
                    uptimeTplTable.setISeq(iSeq);
                    uptimeTplTable.setIUptimeTplMid(uptimeTplM.getIAutoId());
                    iSeq++;
                }
                uptimeTplTableService.batchSave(uptimeTplTables);
            }
            return true;
        });

        return SUCCESS;
    }

    /**
     * 删除
     *
     * @param iuptimecategoryid 稼动时间模板主键
     */
    public Ret delete(Long iuptimecategoryid) {
        tx(() -> {
            updateColumn(iuptimecategoryid, "isDeleted", TRUE);
            return true;
        });
        return SUCCESS;
    }

    /**
     * 数据导入
     */
    public Ret importExcelData(File file) {
        List<Record> datas = CusFieldsMappingdCache.ME.getImportRecordsByTableName(file, table());
        ValidationUtils.notEmpty(datas, "导入数据不能为空");

        StringBuilder msg = new StringBuilder();
        
        Date now = new Date();
        
        tx(() -> {
            
            int iseq = 1;
            // 封装数据
            for (Record row : datas) {
                // 基本信息校验
                ValidationUtils.notNull(row.get("cworkname"), "产线名称不允许为空");
                ValidationUtils.notNull(row.get("cworkshiftname"), "班次名称不允许为空");
                ValidationUtils.notNull(row.get("ibasemins"), "基本稼动时间不允许为空");
                ValidationUtils.isTrue(NumberUtil.isNumber(row.getStr("ibasemins")), "基本稼动时间非数字");
                ValidationUtils.notNull(row.get("istopmins"), "不稼动时间不允许为空");
                ValidationUtils.isTrue(NumberUtil.isNumber(row.getStr("istopmins")), "不稼动时间非数字");
                ValidationUtils.notNull(row.get("iswitchmins"), "机种切换时间不允许为空");
                ValidationUtils.isTrue(NumberUtil.isNumber(row.getStr("iswitchmins")), "机种切换时间非数字");
                if (notNull(row.get("iotmins"))) {
                    ValidationUtils.isTrue(NumberUtil.isNumber(row.getStr("iotmins")), "加班时间非数字");
                }
                if (notNull(row.get("iworkmins"))) {
                    ValidationUtils.isTrue(NumberUtil.isNumber(row.getStr("iworkmins")), "工作时间非数字");
                }
                ValidationUtils.notBlank(row.getStr("cuptimecategoryname"), "分类名称不允许为空");
                ValidationUtils.notBlank(row.getStr("cuptimeparamname"), "参数名称不允许为空");
                ValidationUtils.notNull(row.getStr("imins"), "设定值不允许为空");
                ValidationUtils.notNull(NumberUtil.isNumber(row.getStr("imins")), "设定值非数字");

                // 获取产线信息
                String cworkname = row.getStr("cworkname");
                Workregionm workregionm = workregionmService.findFirstByWorkName(cworkname);
                if (notOk(workregionm)) {
                    msg.append(cworkname).append("产线名称不存在,");
                    continue;
                }

                // 获取班次信息
                String cworkshiftname = row.get("cworkshiftname").toString();
                Workshiftm workshiftm = workshiftmService.findByName(cworkshiftname);
                if (notOk(workshiftm)) {
                    msg.append(cworkshiftname).append("班次名称不存在,");
                    continue;
                }

                // 同一个产线班次只允许保存一次
                UptimeTplM up = findByWorkRegionMidAndWorkShiftMid(workregionm.getIAutoId(), workshiftm.getIautoid());
                if (isOk(up)) {
                    msg.append("产线名称:").append(cworkname).append("班次名称:").append(cworkshiftname).append("已存在模板");
                    continue;
                }

                Long iUptimeCategoryId = uptimeCategoryService.getOrAddUptimeCategoryByName(row.getStr("cuptimecategoryname"));
                Long iUptimeParamId = uptimeParamService.getOrAddUptimeParamByName(iUptimeCategoryId, row.getStr("cuptimeparamname"));

                // 保存稼动时间模板数据
                UptimeTplM uptimeTplM = new UptimeTplM();
                uptimeTplM.setIOrgId(getOrgId());
                uptimeTplM.setCOrgCode(getOrgCode());
                uptimeTplM.setCOrgName(getOrgName());
                uptimeTplM.setIWorkRegionMid(workregionm.getIAutoId());
                uptimeTplM.setIWorkShiftMid(workshiftm.getIautoid());
                uptimeTplM.setIBaseMins(row.getInt("ibasemins"));
                uptimeTplM.setIStopMins(row.getInt("istopmins"));
                uptimeTplM.setISwitchMins(row.getInt("iswitchmins"));
                uptimeTplM.setIOtMins(row.getInt("iotmins"));
                uptimeTplM.setIWorkMins(row.getInt("iworkmins"));
                uptimeTplM.setCCreateName(JBoltUserKit.getUserName());
                uptimeTplM.setDCreateTime(now);
                uptimeTplM.setICreateBy(JBoltUserKit.getUserId());
                uptimeTplM.setCUpdateName(JBoltUserKit.getUserName());
                uptimeTplM.setDUpdateTime(now);
                uptimeTplM.setIUpdateBy(JBoltUserKit.getUserId());
                uptimeTplM.setIsDeleted(false);
                uptimeTplM.setIsEnabled(true);
                ValidationUtils.isTrue(uptimeTplM.save(), "保存失败");

                // 保存稼动时间模板表格类别数据
                UptimeTplCategory uptimeTplCategory = new UptimeTplCategory();
                uptimeTplCategory.setIUptimeTplMid(uptimeTplM.getIAutoId());
                uptimeTplCategory.setIUptimeCategoryId(iUptimeCategoryId);
                uptimeTplCategory.setISeq(iseq);
                ValidationUtils.isTrue(uptimeTplCategory.save(), "保存失败");

                // 保存稼动时间模板表格内容数据
                UptimeTplTable uptimeTplTable = new UptimeTplTable();
                uptimeTplTable.setIUptimeTplMid(uptimeTplM.getIAutoId());
                uptimeTplTable.setIUptimeCategoryId(iUptimeCategoryId);
                uptimeTplTable.setIUptimeParamId(iUptimeParamId);
                uptimeTplTable.setISeq(iseq);
                uptimeTplTable.setIMins(Integer.valueOf(row.get("imins").toString()));
                ValidationUtils.isTrue(uptimeTplTable.save(), "保存失败");

                iseq++;
            }

            return true;
        });

        ValidationUtils.assertBlank(msg.toString(), msg + ",其他数据已处理");
        return SUCCESS;
    }

    /**
     * 根据产线主键和班次主键查询数据
     */
    public UptimeTplM findByWorkRegionMidAndWorkShiftMid(Long iWorkRegionMid, Long iWorkShiftMid) {
        return findFirst(selectSql().eq("iWorkRegionMid", iWorkRegionMid).eq("iWorkShiftMid", iWorkShiftMid));
    }
    
}