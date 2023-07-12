package cn.rjtech.admin.workclass;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.StrSplitter;
import cn.hutool.core.util.StrUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.cache.JBoltUserCache;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.poi.excel.*;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.cache.CusFieldsMappingdCache;
import cn.rjtech.enums.SourceEnum;
import cn.rjtech.model.momdata.Workclass;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.io.File;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static cn.hutool.core.text.StrPool.COMMA;

/**
 * 工种档案 Service
 *
 * @ClassName: WorkclassService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-11-09 16:21
 */
public class WorkClassService extends BaseService<Workclass> {

    private final Workclass dao = new Workclass().dao();

    @Override
    protected Workclass dao() {
        return dao;
    }

    /**
     * 后台管理分页查询
     */
    public Page<Workclass> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
        return paginateByKeywords("iAutoId", "DESC", pageNumber, pageSize, keywords, "iAutoId");
    }

    /**
     * 保存
     */
    public Ret save(Workclass workclass) {
        if (workclass == null || isOk(workclass.getIautoid())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        ValidationUtils.assertNull(findWorkClassCodeInfo(workclass.getCworkclasscode()), "编码重复！");
        saveWorkClassHandle(workclass, JBoltUserKit.getUserId(), new Date(), JBoltUserKit.getUserName(), getOrgId(), getOrgCode(), getOrgName());
        boolean success = workclass.save();
        return ret(success);
    }

    /**
     * 更新
     */
    public Ret update(Workclass workclass) {
        if (workclass == null || notOk(workclass.getIautoid())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        // 更新时需要判断数据存在
        Workclass dbWorkclass = findById(workclass.getIautoid());
        if (dbWorkclass == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        if (!workclass.getCworkclasscode().equals(dbWorkclass.getCworkclasscode())){
            ValidationUtils.isTrue(findWorkClassCodeInfo(workclass.getCworkclasscode()) == null,
                workclass.getCworkclasscode()+" 编码重复！");
        }
        if(exists("cWorkClassCode",workclass.getCworkclasscode(), workclass.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}

        workclass.setIupdateby(JBoltUserKit.getUserId());
        workclass.setCupdatename(JBoltUserKit.getUserName());
        workclass.setDupdatetime(new Date());
        boolean success = workclass.update();
        return ret(success);
    }

    /**
     * 删除 指定多个ID
     */
    public Ret deleteByBatchIds(String ids) {
        boolean result = tx(() -> {
            for (String id : StrSplitter.split(ids, COMMA, true, true)) {
                Workclass workclass = findById(id);
                ValidationUtils.notNull(workclass, JBoltMsg.DATA_NOT_EXIST);
                workclass.setIsdeleted(true);
                workclass.update();
            }
            return true;
        });
        if (notOk(result)) {
            return FAIL;
        }
        return SUCCESS;
    }

    /**
     * 删除数据后执行的回调
     *
     * @param workclass 要删除的model
     * @param kv        携带额外参数一般用不上
     */
    @Override
    protected String afterDelete(Workclass workclass, Kv kv) {
        // addDeleteSystemLog(workclass.getIautoid(), JBoltUserKit.getUserId(),workclass.getName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param workclass 要删除的model
     * @param kv        携带额外参数一般用不上
     */
    @Override
    public String checkCanDelete(Workclass workclass, Kv kv) {
        // 如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
        return checkInUse(workclass, kv);
    }

    /**
     * 设置返回二开业务所属的关键systemLog的targetType
     */
    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

    /**
     * 切换isdeleted属性
     */
    public Ret toggleIsdeleted(Long id) {
        return toggleBoolean(id, "isDeleted");
    }

    /**
     * 切换isenabled属性
     */
    public Ret toggleIsenabled(Long id) {
        return toggleBoolean(id, "isEnabled");
    }

    /**
     * 检测是否可以toggle操作指定列
     *
     * @param workclass 要toggle的model
     * @param column    操作的哪一列
     * @param kv        携带额外参数一般用不上
     */
    @Override
    public String checkCanToggle(Workclass workclass, String column, Kv kv) {
        // 没有问题就返回null  有问题就返回提示string 字符串
        return null;
    }

    /**
     * toggle操作执行后的回调处理
     */
    @Override
    protected String afterToggleBoolean(Workclass workclass, String column, Kv kv) {
        // addUpdateSystemLog(workclass.getIautoid(), JBoltUserKit.getUserId(), workclass.getName(),"的字段["+column+"]值:"+workclass.get(column));
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param workclass model
     * @param kv        携带额外参数一般用不上
     */
    @Override
    public String checkInUse(Workclass workclass, Kv kv) {
        // 这里用来覆盖 检测Workclass是否被其它表引用
        return null;
    }

    public void saveWorkClassHandle(Workclass workclass, Long userId, Date date, String username, Long orgId, String orgCode, String orgName) {
        workclass.setIcreateby(userId);
        workclass.setDcreatetime(date);
        workclass.setCcreatename(username);
        workclass.setIorgid(orgId);
        workclass.setCorgcode(orgCode);
        workclass.setCorgname(orgName);
        workclass.setIupdateby(userId);
        workclass.setCupdatename(username);
        workclass.setDupdatetime(date);
    }

    public Page<Record> pageList(Kv kv) {
        return dbTemplate("workclass.list", kv).paginate(kv.getInt("page"), kv.getInt("pageSize"));
    }

    public List<Record> list(Kv kv) {
        return dbTemplate("workclass.list", kv).find();
    }

    /**
     * 上传excel文件
     */
    public Ret importExcelData(File file) {
        StringBuilder errorMsg = new StringBuilder();
        JBoltExcel jBoltExcel = JBoltExcel
                // 从excel文件创建JBoltExcel实例
                .from(file)
                // 设置工作表信息
                .setSheets(
                        JBoltExcelSheet.create("sheet1")
                                // 设置列映射 顺序 标题名称
                                .setHeaders(
                                        JBoltExcelHeader.create("cworkclasscode", "工种编码"),
                                        JBoltExcelHeader.create("cworkclassname", "工种名称"),
                                        JBoltExcelHeader.create("ilevel", "工种等级"),
                                        JBoltExcelHeader.create("isalary", "工种薪酬"),
                                        JBoltExcelHeader.create("cmemo", "备注")
                                )
                                // 特殊数据转换器
                                .setDataChangeHandler((data, index) -> {
                                    try {
                                        String isalary = data.getStr("isalary");
                                        data.change("isalary", new BigDecimal(isalary));
                                    } catch (Exception e) {
                                        errorMsg.append(data.getStr("isalary") + "薪酬填写有误");
                                    }
                                })
                                // 从第三行开始读取
                                .setDataStartRow(2)
                );
        // 从指定的sheet工作表里读取数据
        List<Workclass> models = JBoltExcelUtil.readModels(jBoltExcel, "sheet1", Workclass.class, errorMsg);
        if (notOk(models)) {
            if (errorMsg.length() > 0) {
                return fail(errorMsg.toString());
            } else {
                return fail(JBoltMsg.DATA_IMPORT_FAIL_EMPTY);
            }
        }
        if (errorMsg.length() > 0) {
            return fail(errorMsg.toString());
        }
        // 读取数据没有问题后判断必填字段
        if (models.size() > 0) {
            for (Workclass w : models) {
                if (notOk(w.getCworkclasscode())) {
                    return fail("工种编码不能为空");
                }
                if (notOk(w.getCworkclassname())) {
                    return fail("工种名称不能为空");
                }

                if (notOk(w.getIlevel())) {
                    return fail("工种名称不能为空");
                }
            }
        }
        savaModelHandle(models);
        // 执行批量操作
        boolean success = tx(() -> {
            batchSave(models);
            return true;
        });
        if (!success) {
            return fail(JBoltMsg.DATA_IMPORT_FAIL);
        }
        return SUCCESS;
    }

    private void savaModelHandle(List<Workclass> models) {
        Long userId = JBoltUserKit.getUserId();
        String userName = JBoltUserKit.getUserName();
        for (Workclass w : models) {
            saveWorkClassHandle(w, userId, new Date(), userName, getOrgId(), getOrgCode(), getOrgName());
        }
    }

    public List<Record> options() {
        return dbTemplate("workclass.list", Kv.of("isenabled", "true")).find();
    }

    public Long findWorkClassCodeInfo(String cworkclasscode) {
        return queryColumn(selectSql().select(Workclass.IAUTOID).eq(Workclass.CWORKCLASSCODE, cworkclasscode));
    }

    public List<Workclass> findAllIsDeletedFalse(){
        return find("select * from Bd_WorkClass where isDeleted = '0'");
    }

    /**
     * 生成Excel 导入模板的数据 byte[]
     */
    public JBoltExcel getExcelImportTpl() {
        return JBoltExcel
                // 创建
                .create()
                .setSheets(
                        JBoltExcelSheet.create("工种档案导入模板")
                                // 设置列映射 顺序 标题名称 不处理别名
                                .setHeaders(2, false,
                                        JBoltExcelHeader.create("工种编码", 20),
                                        JBoltExcelHeader.create("工种名称", 20),
                                        JBoltExcelHeader.create("工种等级", 20),
                                        JBoltExcelHeader.create("工种薪酬", 20),
                                        JBoltExcelHeader.create("备注", 20)
                                )
                                .setMerges(JBoltExcelMerge.create("A", "E", 1, 1, "工种档案"))
                );
    }

    /**
     * 导出excel文件
     */
    public JBoltExcel exportExcelTpl(List<Workclass> datas) {
        // 2、创建JBoltExcel
        // 3、返回生成的excel文件
        return JBoltExcel
                .createByTpl("工种档案导出模板.xls")// 创建JBoltExcel 从模板加载创建
                .addSheet(// 设置sheet
                        JBoltExcelSheet.create("工种档案")// 创建sheet name保持与模板中的sheet一致
                                .setHeaders(// sheet里添加表头
                                        JBoltExcelHeader.create("cworkclasscode", "工种编码", 20),
                                        JBoltExcelHeader.create("cworkclassname", "工种名称", 20),
                                        JBoltExcelHeader.create("ilevel", "工种等级", 20),
                                        JBoltExcelHeader.create("isalary", "工种薪酬", 20),
                                        JBoltExcelHeader.create("cmemo", "备注", 20),
                                        JBoltExcelHeader.create("ccreatename", "创建人", 20),
                                        JBoltExcelHeader.create("dcreatetime", "创建时间", 20)
                                )
                                .setDataChangeHandler((data, index) -> {// 设置数据转换处理器
                                    // 将user_id转为user_name
                                    data.changeWithKey("user_id", "user_username", JBoltUserCache.me.getUserName(data.getLong("user_id")));
                                    data.changeBooleanToStr("is_deleted", "是", "否");
                                })
                                .setModelDatas(3, datas)// 设置数据
                )
                .setFileName("工种档案" + "_" + DateUtil.today());
    }

	public Workclass findModelByCode(String cWorkClassCode) {
		return findFirst(selectSql().eq("cworkclasscode", cWorkClassCode).eq("cOrgCode",getOrgCode()));
	}
    
    public String getIdByCode(String code) {
        Sql sql = selectSql().select(Workclass.IAUTOID)
                .eq(Workclass.IORGID, getOrgId())
                .eq(Workclass.CWORKCLASSCODE, code)
                .eq(Workclass.ISDELETED, ZERO_STR)
                .first(); 
        
        return queryColumn(sql);
    }
    /**
     * 从系统导入字段配置，获得导入的数据
     */
    public Ret importExcelClass(File file) {
        List<Record> records = CusFieldsMappingdCache.ME.getImportRecordsByTableName(file, table());
        if (notOk(records)) {
            return fail(JBoltMsg.DATA_IMPORT_FAIL_EMPTY);
        }

        Date now = new Date();

        for (Record record : records) {

            if (StrUtil.isBlank(record.getStr("cWorkClassCode"))) {
                return fail("编码不能为空");
            }
            if (StrUtil.isBlank(record.getStr("cWorkClassName"))) {
                return fail("名称不能为空");
            }

            record.set("iAutoId", JBoltSnowflakeKit.me.nextId());
            record.set("iOrgId", getOrgId());
            record.set("cOrgCode", getOrgCode());
            record.set("cOrgName", getOrgName());
            record.set("iSource", SourceEnum.MES.getValue());
            record.set("iCreateBy", JBoltUserKit.getUserId());
            record.set("dCreateTime", now);
            record.set("cCreateName", JBoltUserKit.getUserName());
            record.set("isEnabled",1);
            record.set("isDeleted",0);
            record.set("iUpdateBy", JBoltUserKit.getUserId());
            record.set("dUpdateTime", now);
            record.set("cUpdateName", JBoltUserKit.getUserName());
        }

        // 执行批量操作
        tx(() -> {
            batchSaveRecords(records);
            return true;
        });
        return SUCCESS;
    }
    
}
