package cn.rjtech.admin.uom;

import cn.hutool.core.text.StrSplitter;
import cn.hutool.core.util.StrUtil;
import cn.jbolt.common.util.CACHE;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.service.base.JBoltBaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.cusfieldsmappingd.CusFieldsMappingDService;
import cn.rjtech.admin.person.PersonService;
import cn.rjtech.model.momdata.Uom;
import cn.rjtech.util.ValidationUtils;
import com.alibaba.fastjson.JSON;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.io.File;
import java.util.Date;
import java.util.List;

import static cn.hutool.core.text.StrPool.COMMA;

/**
 * 计量单位档案 Service
 *
 * @ClassName: UomService
 * @author: chentao
 * @date: 2022-11-02 17:29
 */
public class UomService extends JBoltBaseService<Uom> {
    private final Uom dao = new Uom().dao();

    @Override
    protected Uom dao() {
        return dao;
    }

    @Inject
    private PersonService personService;

    @Inject
    private CusFieldsMappingDService cusFieldsMappingdService;
    /**
     * 后台管理分页查询
     */
    public Page<Uom> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
        return paginateByKeywords("iAutoId", "DESC", pageNumber, pageSize, keywords, "cUomName");
    }

    /**
     * 保存
     */
    public Ret save(Uom uom) {
        if (uom == null || isOk(uom.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //if(existsName(uom.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        saveUomHandle(uom, JBoltUserKit.getUserId(), new Date(), JBoltUserKit.getUserName());
        if (uom.getIsBase()) {
            setUnDefault(null, uom.getIUomClassId());
        }
        setUomclass(uom);
        boolean success = uom.save();
        if (success) {
            //添加日志
            //addSaveSystemLog(uom.getIautoid(), JBoltUserKit.getUserId(), uom.getName());
        }
        return ret(success);
    }

    /**
     * 设置参数
     * @param uom
     * @return
     */
    private Uom setUomclass(Uom uom){
        uom.setIsDeleted(false);
        Long userId = JBoltUserKit.getUserId();
        uom.setICreateBy(userId);
        uom.setIUpdateBy(userId);
        String userName = JBoltUserKit.getUserName();
        uom.setCCreateName(userName);
        uom.setCUpdateName(userName);
        Date date = new Date();
        uom.setDCreateTime(date);
        uom.setDUpdateTime(date);
        return uom;
    }

    /**
     * 更新
     */
    public Ret update(Uom uom) {
        if (uom == null || notOk(uom.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //更新时需要判断数据存在
        Uom dbUom = findById(uom.getIAutoId());
        if (dbUom == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        //if(existsName(uom.getName(), uom.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        if (uom.getIsBase()) {
            setUnDefault(uom.getIAutoId(), uom.getIUomClassId());
        }
        boolean success = uom.update();
        if (success) {
            //添加日志
            //addUpdateSystemLog(uom.getIautoid(), JBoltUserKit.getUserId(), uom.getName());
        }
        return ret(success);
    }

    /**
     * 删除 指定多个ID
     */
    public Ret deleteByBatchIds(String ids) {
        boolean result = tx(() -> {
            for (String id : StrSplitter.split(ids, COMMA, true, true)) {
                Uom uom = findById(id);
                ValidationUtils.notNull(uom, JBoltMsg.DATA_NOT_EXIST);
                uom.setIsDeleted(true);
                uom.update();
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
     */
    public Ret delete(Long id) {
        return deleteById(id, true);
    }

    /**
     * 删除数据后执行的回调
     *
     * @param uom 要删除的model
     * @param kv  携带额外参数一般用不上
     */
    @Override
    protected String afterDelete(Uom uom, Kv kv) {
        //addDeleteSystemLog(uom.getIautoid(), JBoltUserKit.getUserId(),uom.getName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param uom 要删除的model
     * @param kv  携带额外参数一般用不上
     */
    @Override
    public String checkCanDelete(Uom uom, Kv kv) {
        //如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
        return checkInUse(uom, kv);
    }

    /**
     * 设置返回二开业务所属的关键systemLog的targetType
     */
    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

    /**
     * 切换isbase属性
     */
    public Ret toggleIsbase(Long id) {
        return toggleBoolean(id, "isBase");
    }

    /**
     * 检测是否可以toggle操作指定列
     *
     * @param uom    要toggle的model
     * @param column 操作的哪一列
     * @param kv     携带额外参数一般用不上
     */
    @Override
    public String checkCanToggle(Uom uom, String column, Kv kv) {
        //没有问题就返回null  有问题就返回提示string 字符串
        return null;
    }

    /**
     * toggle操作执行后的回调处理
     */
    @Override
    protected String afterToggleBoolean(Uom uom, String column, Kv kv) {
        //addUpdateSystemLog(uom.getIautoid(), JBoltUserKit.getUserId(), uom.getName(),"的字段["+column+"]值:"+uom.get(column));
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param uom model
     * @param kv  携带额外参数一般用不上
     */
    @Override
    public String checkInUse(Uom uom, Kv kv) {
        //这里用来覆盖 检测Uom是否被其它表引用
        return null;
    }

    public Page<Uom> paginateAdminDatas(int pageNumber, int pageSize, String keywords, Long iuomclassid) {
        Page<Uom> page = paginateByKeywords("iAutoId", "DESC", pageNumber, pageSize, keywords, "cUomName", Okv.by("iUomClassId", iuomclassid));
        List<Uom> list = page.getList();
        page.setList(list);
        return page;
    }

    public Page<Record> pageList(Kv kv) {
        Page<Record> recordPage = dbTemplate("uom.list", kv).paginate(kv.getInt("page"), kv.getInt("pageSize"));
        if (isOk(recordPage.getList())) {
            for (Record r : recordPage.getList()) {
                r.put("cuomclasssn", personService.formatPattenSn(r.getStr("cuomclasssn"), "uom_type"));
            }
        }
        return recordPage;
    }

    public void saveUomHandle(Uom uom, Long userId, Date date, String username) {
        uom.setICreateBy(userId);
        uom.setDCreateTime(date);
        uom.setCCreateName(username);
    }

    public void setUnDefault(Long id, Long pid) {
        if (notOk(id)) {
            update("update Bd_Uom set isBase ='0' where iUomClassId = ?", pid);
        } else {
            update("update Bd_Uom set isBase ='0' where iUomClassId = ? and iautoid != ?", pid, id);
        }
    }

    public List<Record> list(Kv kv) {
        return dbTemplate("uom.list", kv).find();
    }

    public Ret importExcelData(File file) {
        StringBuilder errorMsg = new StringBuilder();
        //使用字段配置维护
        Object importData =  cusFieldsMappingdService.getImportDatas(file, "计量单位").get("data");
        String docInfoRelaStrings= JSON.toJSONString(importData);
        //从指定的sheet工作表里读取数据
        List<Uom> models = JSON.parseArray(docInfoRelaStrings, Uom.class);
        if (notOk(models)) {
            if (errorMsg.length() > 0) {
                return fail(errorMsg.toString());
            } else {
                return fail(JBoltMsg.DATA_IMPORT_FAIL_EMPTY);
            }
        }
        for (Uom model : models) {
            Long pid = CACHE.me.getUomClassIdByCode(model.getCUpdateName());
            model.setIUomClassId(pid);
            setUomclass(model);
        }
        if (errorMsg.length() > 0) {
            return fail(errorMsg.toString());
        }
        //读取数据没有问题后判断必填字段
        if (models.size() > 0) {
            for (Uom u : models) {
                if (notOk(u.getCUomCode())) {
                    return fail("计量单位编码不能为空");
                }
                if (notOk(u.getCUomName())) {
                    return fail("计量单位名称不能为空");
                }
                if (notOk(u.getIRatioToBase())) {
                    return fail("换算率不能为空");
                }
                //Long pid = CACHE.me.getUomClassIdByCode(u.getCUpdateName());
                //if (notOk(pid)) {
                //    //传入编码不存在时不进行数据转换，让程序报错
                //  return   fail("导入计量单位组编码"+u.getCUpdateName()+"不存在，导入失败");
                //
                //}
            }
        }
        savaModelHandle(models);
        //执行批量操作
        boolean success = tx(() -> {
            batchSave(models);
            return true;
        });
        if (!success) {
            return fail(JBoltMsg.DATA_IMPORT_FAIL);
        }
        return SUCCESS;
    }

    private void savaModelHandle(List<Uom> models) {
        Long userId = JBoltUserKit.getUserId();
        String userName = JBoltUserKit.getUserName();
        String finalDefaultCode = null;
        for (Uom u : models) {
            if (u.getIsBase()) {
                finalDefaultCode = u.getCUomCode();
            }
            saveUomHandle(u, userId, new Date(), userName);
        }
        if (StrUtil.isNotBlank(finalDefaultCode)) {
            for (Uom u : models) {
                if (StrUtil.equals(u.getCUomCode(), finalDefaultCode)) {
                    u.setIsBase(true);
                } else {
                    u.setIsBase(false);
                }
            }
            //将数据库原先设置默认的清理掉
            setUnDefault(null, models.get(0).getIUomClassId());
        }
    }


    public void deleteByClassId(Long id) {
        delete("delete from Bd_Uom where iUomClassId = ?", id);
    }

    public List<Record> getOptions(Kv kv) {
        Sql sql = selectSql().eq("isdeleted", false).orderBy(true, "dCreateTime");
        if (isOk(kv.getStr("iUomClassId"))) {
            sql.eq("iUomClassId", kv.getStr("iUomClassId"));
        }
        return findRecord(sql);
    }

    public List<Uom> getIdAndNameList() {
        return find("SELECT iAutoId,cUomName FROM Bd_Uom WHERE isDeleted = '0' ");
    }


}