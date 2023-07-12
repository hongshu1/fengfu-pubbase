package cn.rjtech.admin.uomclass;

import cn.hutool.core.text.StrSplitter;
import cn.hutool.core.util.StrUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.bean.JsTreeBean;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.uom.UomService;
import cn.rjtech.cache.CusFieldsMappingdCache;
import cn.rjtech.enums.SourceEnum;
import cn.rjtech.model.momdata.Uomclass;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static cn.hutool.core.text.StrPool.COMMA;

/**
 * 计量单位组档案 Service
 *
 * @ClassName: UomclassService
 * @author: chentao
 * @date: 2022-11-02 17:26
 */
public class UomclassService extends BaseService<Uomclass> {
    
    private final Uomclass dao = new Uomclass().dao();

    @Override
    protected Uomclass dao() {
        return dao;
    }

    @Inject
    private UomService uomService;

    /**
     * 后台管理分页查询
     */
    public Page<Uomclass> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
        return paginateByKeywords("iAutoId", "DESC", pageNumber, pageSize, keywords, "cUomClassName");
    }

    /**
     * 保存
     */
    public Ret save(Uomclass uomclass) {
        if (uomclass == null || isOk(uomclass.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }

        Date now = new Date();
        String username = JBoltUserKit.getUserName();
        
        uomclass.setICreateBy(JBoltUserKit.getUserId());
        uomclass.setDCreateTime(now);
        uomclass.setCCreateName(username);
        uomclass.setIOrgId(getOrgId());
        uomclass.setCOrgCode(getOrgCode());
        uomclass.setCOrgName(getOrgName());
        
        if (uomclass.getIsDefault()) {
            //如果设置默认计量组，除了当前计量组以外的计量组全部设为不是默认
            setUnDefault(uomclass.getIAutoId());
        }
        setUomclass(uomclass);
        //if(existsName(uomclass.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = uomclass.save();
        if (success) {
            //添加日志
            //addSaveSystemLog(uomclass.getIautoid(), JBoltUserKit.getUserId(), uomclass.getName());
        }
        return ret(success);
    }

    /**
     * 设置参数
     */
    private Uomclass setUomclass(Uomclass uomclass) {
        uomclass.setIsDeleted(false);
        uomclass.setIOrgId(getOrgId());
        uomclass.setCOrgCode(getOrgCode());
        uomclass.setCOrgName(getOrgName());
        Long userId = JBoltUserKit.getUserId();
        uomclass.setICreateBy(userId);
        uomclass.setIUpdateBy(userId);
        String userName = JBoltUserKit.getUserName();
        uomclass.setCCreateName(userName);
        uomclass.setCUpdateName(userName);
        Date date = new Date();
        uomclass.setDCreateTime(date);
        uomclass.setDUpdateTime(date);
        return uomclass;
    }

    /**
     * 更新
     *
     * @param uomclass
     * @return
     */
    public Ret update(Uomclass uomclass) {
        if (uomclass == null || notOk(uomclass.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //更新时需要判断数据存在
        Uomclass dbUomclass = findById(uomclass.getIAutoId());
        if (dbUomclass == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        uomclass.setIUpdateBy(JBoltUserKit.getUserId());
        uomclass.setCUpdateName(JBoltUserKit.getUserName());
        uomclass.setDUpdateTime(new Date());
        if (uomclass.getIsDefault()) {
            setUnDefault(uomclass.getIAutoId());
        }
        //if(existsName(uomclass.getName(), uomclass.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = uomclass.update();
        if (success) {
            //添加日志
            //addUpdateSystemLog(uomclass.getIautoid(), JBoltUserKit.getUserId(), uomclass.getName());
        }
        return ret(success);
    }

    /**
     * 删除 指定多个ID
     *
     * @param ids
     * @return
     */
    public Ret deleteByBatchIds(String ids) {
        boolean result = tx(() -> {
            for (String id : StrSplitter.split(ids, COMMA, true, true)) {
                Uomclass uomclass = findById(id);
                ValidationUtils.notNull(uomclass, JBoltMsg.DATA_NOT_EXIST);
                uomclass.setIsDeleted(true);
                uomclass.update();
                uomService.deleteByClassId(uomclass.getIAutoId());
            }
            return true;
        });
        if (notOk(result)) {
            return FAIL;
        }
        return SUCCESS;
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    public Ret delete(Long id) {
        return deleteById(id, true);
    }

    /**
     * 删除数据后执行的回调
     *
     * @param uomclass 要删除的model
     * @param kv       携带额外参数一般用不上
     * @return
     */
    @Override
    protected String afterDelete(Uomclass uomclass, Kv kv) {
        //addDeleteSystemLog(uomclass.getIautoid(), JBoltUserKit.getUserId(),uomclass.getName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param uomclass 要删除的model
     * @param kv       携带额外参数一般用不上
     * @return
     */
    @Override
    public String checkCanDelete(Uomclass uomclass, Kv kv) {
        //如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
        return checkInUse(uomclass, kv);
    }

    /**
     * 设置返回二开业务所属的关键systemLog的targetType
     *
     * @return
     */
    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

    /**
     * 切换isenabled属性
     */
    public Ret toggleIsenabled(Long id) {
        return toggleBoolean(id, "isEnabled");
    }

    /**
     * 切换isdeleted属性
     */
    public Ret toggleIsdeleted(Long id) {
        return toggleBoolean(id, "isDeleted");
    }

    /**
     * 检测是否可以toggle操作指定列
     *
     * @param uomclass 要toggle的model
     * @param column   操作的哪一列
     * @param kv       携带额外参数一般用不上
     */
    @Override
    public String checkCanToggle(Uomclass uomclass, String column, Kv kv) {
        //没有问题就返回null  有问题就返回提示string 字符串
        return null;
    }

    /**
     * toggle操作执行后的回调处理
     */
    @Override
    protected String afterToggleBoolean(Uomclass uomclass, String column, Kv kv) {
        //addUpdateSystemLog(uomclass.getIautoid(), JBoltUserKit.getUserId(), uomclass.getName(),"的字段["+column+"]值:"+uomclass.get(column));
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param uomclass model
     * @param kv       携带额外参数一般用不上
     */
    @Override
    public String checkInUse(Uomclass uomclass, Kv kv) {
        //这里用来覆盖 检测Uomclass是否被其它表引用
        return null;
    }

    public List<Uomclass> dataList(Boolean isEnabled) {
        Okv kv = Okv.create();
        kv.set("isDeleted", false);
        if (isEnabled != null) {
            kv.set("isEnabled", isEnabled);
        }
        return getCommonList(kv, "iAutoId", "asc");
    }

    public List<JsTreeBean> getMgrTree(Long selectId, Integer openLevel) {
        List<Uomclass> uomclassList = find("select * from Bd_UomClass where isdeleted='0'");
        List<JsTreeBean> jsTreeBeanList = new ArrayList<>();
        JsTreeBean parent = new JsTreeBean("1", "#", "计量单位组", null, "", false);
        jsTreeBeanList.add(parent);
        for (Uomclass uomclass : uomclassList) {
            Long id = uomclass.getIAutoId();
            String pid = "1";
            String text = "[" + uomclass.getCUomClassCode() + "]" + uomclass.getCUomClassName();
            if (uomclass.getIsDefault()) {
                text += "[默认]";
            }
            String type = uomclass.getCUomClassSn();
            JsTreeBean jsTreeBean = new JsTreeBean(id, pid, text, type, "", false);
            jsTreeBeanList.add(jsTreeBean);
        }
        return jsTreeBeanList;
    }

    public void setUnDefault(Long id) {
        update("update Bd_UomClass set isDefault ='0' where iautoid != ?", id);
    }

    public List<Record> findUomClassRecord() {
        Sql sql = selectSql().eq("isdeleted", false);
        return findRecord(sql);
    }

    public Ret importExcelData(File file) {
        // 使用字段配置维护
        List<Record> datas = CusFieldsMappingdCache.ME.getImportRecordsByTableName(file, table());
        ValidationUtils.notEmpty(datas, "导入数据不能为空");

        Long userId = JBoltUserKit.getUserId();
        String userName = JBoltUserKit.getUserName();
        String finalDefaultCode = null;
        Date now = new Date();

        // 读取数据没有问题后判断必填字段
        for (Record row : datas) {
            
            ValidationUtils.notBlank(row.getStr("cuomclasscode"), "计量单位组编码不能为空");
            ValidationUtils.notBlank(row.get("CUomClassName"), "计量单位组名称不能为空");
            ValidationUtils.notBlank(row.getStr("CUomClassSn"), "计量单位组类型不能为空");

            int bool = row.getBigDecimal("isdefault") != null ? row.getBigDecimal("isdefault").intValue() : 0;
            if (bool == 1) {
                finalDefaultCode = row.getStr("cuomclasscode");
            }

            row.set("isource", SourceEnum.MES.getValue());
            row.set("icreateby", userId);
            row.set("dcreatetime", now);
            row.set("ccreatename", userName);
            row.set("iorgid", getOrgId());
            row.set("corgcode", getOrgCode());
            row.set("COrgName", getOrgName());
            row.set("iupdateby", userId);
            row.set("dupdatetime", now);
            row.set("cupdatename", userName);
            row.set("iautoid", JBoltSnowflakeKit.me.nextId());
        }

        if (StrUtil.isNotBlank(finalDefaultCode)) {
            for (Record u : datas) {
                u.set("isdefault", StrUtil.equals(u.getStr("cuomclasscode"), finalDefaultCode));
            }
            
            //将数据库原先设置默认的清理掉
            setUnDefault(JBoltSnowflakeKit.me.nextId());
        }

        //执行批量操作
        tx(() -> {
            batchSaveRecords(datas);
            return true;
        });
        
        return SUCCESS;
    }

    public List<Record> getOptions() {
        return findUomClassRecord();
    }
}