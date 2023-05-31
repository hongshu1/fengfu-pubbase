package cn.rjtech.admin.workregionm;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.text.StrSplitter;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.jbolt.common.util.CACHE;
import cn.jbolt.core.base.JBoltIDGenMode;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.poi.excel.*;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.cusfieldsmappingd.CusFieldsMappingDService;
import cn.rjtech.admin.datapermission.DataPermissionService;
import cn.rjtech.admin.department.DepartmentService;
import cn.rjtech.admin.person.PersonService;
import cn.rjtech.admin.warehouse.WarehouseService;
import cn.rjtech.model.momdata.CusFieldsMappingD;
import cn.rjtech.model.momdata.Workregionm;
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
 * 工段档案 Service
 *
 * @ClassName: WorkregionmService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-11-01 20:42
 */
public class WorkregionmService extends BaseService<Workregionm> {

    private final Workregionm dao = new Workregionm().dao();
    
    @Inject
    private DataPermissionService dataPermissionService;
    @Inject
    private CusFieldsMappingDService cusFieldsMappingDService;

    @Inject
    private DepartmentService  departmentService;

    @Inject
    private WarehouseService warehouseService;


    @Inject
    private PersonService personService;

    @Override
    protected Workregionm dao() {
        return dao;
    }

    /**
     * 后台管理分页查询
     */
    public Page<Workregionm> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
        return paginateByKeywords("iAutoId", "DESC", pageNumber, pageSize, keywords, "iAutoId");
    }

    /**
     * 保存
     */
    public Ret save(Workregionm workregionm) {
        if (workregionm == null || isOk(workregionm.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        ValidationUtils.isTrue(getCworkcode(workregionm.getCWorkCode()) == null, "编码重复！");
        //if(existsName(workregionm.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        saveWorkRegionMHandle(workregionm, JBoltUserKit.getUserId(), new Date(), JBoltUserKit.getUserName(), getOrgId(), getOrgCode(), getOrgName());
        boolean success = workregionm.save();
        if (success) {
            //添加日志
            //addSaveSystemLog(workregionm.getIautoid(), JBoltUserKit.getUserId(), workregionm.getName());
        }
        return ret(success);
    }

    /**
     * 更新
     */
    public Ret update(Workregionm workregionm) {
        if (workregionm == null || notOk(workregionm.getIAutoId()) || StrUtil.isBlank(workregionm.getCWorkCode())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //更新时需要判断数据存在
        Workregionm dbWorkregionm = findById(workregionm.getIAutoId());
        if (dbWorkregionm == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        if (!workregionm.getCWorkCode().equals(dbWorkregionm.getCWorkCode())){
            ValidationUtils.isTrue(getCworkcode(workregionm.getCWorkCode()) == null, "产线编码重复！");
        }
        //if(existsName(workregionm.getName(), workregionm.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = workregionm.update();
        if (success) {
            //添加日志
            //addUpdateSystemLog(workregionm.getIautoid(), JBoltUserKit.getUserId(), workregionm.getName());
        }
        return ret(success);
    }

    /**
     * 删除 指定多个ID
     */
    public Ret deleteByBatchIds(String ids) {
        boolean result = tx(() -> {
            for (String id : StrSplitter.split(ids, COMMA, true, true)) {
                Workregionm workregionm = findById(id);
                ValidationUtils.notNull(workregionm, JBoltMsg.DATA_NOT_EXIST);
                workregionm.setIsDeleted(true);
                workregionm.update();
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
     * @param workregionm 要删除的model
     * @param kv          携带额外参数一般用不上
     */
    @Override
    protected String afterDelete(Workregionm workregionm, Kv kv) {
        //addDeleteSystemLog(workregionm.getIautoid(), JBoltUserKit.getUserId(),workregionm.getName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param workregionm 要删除的model
     * @param kv          携带额外参数一般用不上
     */
    @Override
    public String checkCanDelete(Workregionm workregionm, Kv kv) {
        //如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
        return checkInUse(workregionm, kv);
    }

    /**
     * 设置返回二开业务所属的关键systemLog的targetType
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
     * @param workregionm 要toggle的model
     * @param column      操作的哪一列
     * @param kv          携带额外参数一般用不上
     */
    @Override
    public String checkCanToggle(Workregionm workregionm, String column, Kv kv) {
        //没有问题就返回null  有问题就返回提示string 字符串
        return null;
    }

    /**
     * toggle操作执行后的回调处理
     */
    @Override
    protected String afterToggleBoolean(Workregionm workregionm, String column, Kv kv) {
        //addUpdateSystemLog(workregionm.getIautoid(), JBoltUserKit.getUserId(), workregionm.getName(),"的字段["+column+"]值:"+workregionm.get(column));
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param workregionm model
     * @param kv          携带额外参数一般用不上
     */
    @Override
    public String checkInUse(Workregionm workregionm, Kv kv) {
        //这里用来覆盖 检测Workregionm是否被其它表引用
        return null;
    }

    /**
     * 创建工段档案信息预处理
     */
    public void saveWorkRegionMHandle(Workregionm workregionm, Long userId, Date date, String username, Long orgId, String orgCode, String orgName) {
        workregionm.setICreateBy(userId);
        workregionm.setDCreateTime(date);
        workregionm.setCCreateName(username);
        workregionm.setIOrgId(orgId);
        workregionm.setCOrgCode(orgCode);
        workregionm.setCOrgName(orgName);
        workregionm.setIUpdateBy(userId);
        workregionm.setDUpdateTime(date);
        workregionm.setCUpdateName(username);
    }

    public Page<Record> pageList(Integer pageNumber, Integer pageSize, Kv kv) {
        return dbTemplate("workregionm.list", kv).paginate(pageNumber, pageSize);
    }

    public List<Record> list(Kv kv) {
        return dbTemplate("workregionm.list", kv).find();
    }

    /**
     * 从系统导入字段配置，获得导入的数据
     */
    public Ret importExcel(File file) {
        List<Record> records = cusFieldsMappingDService.getImportRecordsByTableName(file, "Bd_WorkRegionM");
        if (notOk(records)) {
                return fail(JBoltMsg.DATA_IMPORT_FAIL_EMPTY);
            }

       // 读取数据没有问题后判断必填字段
        if (records.size() > 0) {
            for (Record record : records) {

                if (ObjectUtil.isNull(record.getStr("cWorkCode"))) {
                    return fail("产线编码不能为空");
                }
                if (ObjectUtil.isNull(record.getStr("cWorkName"))) {
                    return fail("产线名称不能为空");
                }
                if (ObjectUtil.isNull(record.getStr("cDepName"))) {
                    return fail("所属部门不能为空");
                }
                if (ObjectUtil.isNull(record.getStr("cPersonName"))) {
                    return fail("产线长不能为空");
                }
                if (ObjectUtil.isNull(record.getStr("iPsLevel"))){
                    return fail("排产层级不能为空");
                }

                String ipslevel="1,2,3,4,5,6,7";
                if(ipslevel.indexOf(record.getStr("iPsLevel"))==-1){
                    return fail("排产层级只能输入1至7");
                }
                if (ObjectUtil.isNull(record.getStr("cWarehouseName"))){
                    return fail("关联仓库名称不能为空");
                }
                if( ObjectUtil.isNull(departmentService.findCodeByDepName( record.getStr("cDepName")))){
                    return fail("未找到部门相对应部门编码请维护部门档案");
                }
                if( ObjectUtil.isNull(departmentService.findIdByDepName( record.getStr("cDepName")))){
                    return fail("未找到部门相对应部门ID请维护部门档案");
                }
                if(ObjectUtil.isNull(warehouseService.findIdByWhName(record.getStr("cWarehouseName")))){
                    return fail("未找到仓库名称相对应仓库ID请维护仓库档案");
                }
                if(ObjectUtil.isNull(warehouseService.findCodeByWhName(record.getStr("cWarehouseName")))){
                    return fail("未找到仓库名称相对应仓库编码请维护仓库档案");
                }
                if(ObjectUtil.isNull(personService.findIdByName(record.getStr("cPersonName")))){
                    return fail("未找到产线长相对应ID请维护人员档案");
                }
                if(ObjectUtil.isNull(personService.findCodeByName(record.getStr("cPersonName")))){
                    return fail("未找到产线长相对应人员编码请维护人员档案");
                }
            }
        }
        /**
         * 设置信息
         */
        for (Record record: records) {
            record.set("cDepCode",departmentService.findCodeByDepName( record.getStr("cDepName")));
            record.set("iDepId",departmentService.findIdByDepName( record.getStr("cDepName")));
            record.set("iWarehouseId",warehouseService.findIdByWhName(record.getStr("cWarehouseName")));
            record.set("cWarehouseCode",warehouseService.findCodeByWhName(record.getStr("cWarehouseName")));
            record.set("iPersonId",personService.findIdByName(record.getStr("cPersonName")));
            record.set("cPersonCode",personService.findCodeByName(record.getStr("cPersonName")));
            record.set("iAutoId", JBoltSnowflakeKit.me.nextId());
            record.set("iCreateBy",JBoltUserKit.getUserId());
            record.set("dCreateTime",new Date());
            record.set("cCreateName",JBoltUserKit.getUserName());
            record.set("iOrgId",getOrgId());
            record.set("cOrgCode",getOrgCode());
            record.set("cOrgName",getOrgName());
            record.set("iUpdateBy",JBoltUserKit.getUserId());
            record.set("dUpdateTime",new Date());
            record.set("cUpdateName",JBoltUserKit.getUserName());
            record.set("iSource",1);
        }
        // 执行批量操作
        boolean success = tx(() -> {
            batchSaveRecords(records);
            return true;
        });
        return SUCCESS;
    }
    
    public Ret importExcelData(File file) {
        StringBuilder errorMsg = new StringBuilder();
        JBoltExcel jBoltExcel = JBoltExcel
                //从excel文件创建JBoltExcel实例
                .from(file)
                //设置工作表信息
                .setSheets(
                        createJboltExcelSheetTpl()
                                //特殊数据转换器
                                .setDataChangeHandler((data, index) -> {
//                                    data.change("ipersonid", CACHE.me.getPersonIdByCode(data.getStr("ipersonid")));
                                    data.change("idepid", CACHE.me.getDeptIdByCode(data.getStr("idepid")));
                                })
                                //从第三行开始读取
                                .setDataStartRow(2)
                );

        // 从指定的sheet工作表里读取数据
        List<Workregionm> models = JBoltExcelUtil.readModels(jBoltExcel, "sheet1", Workregionm.class, errorMsg);

        if (notOk(models)) {
            if (errorMsg.length() > 0) {
                return fail(errorMsg.toString());
            } else {
                return fail(JBoltMsg.DATA_IMPORT_FAIL_EMPTY);
            }
        }

        // 读取数据没有问题后判断必填字段
        if (models.size() > 0) {
            for (Workregionm w : models) {
                if (notOk(w.getCWorkCode())) {
                    return fail("工段编码不能为空");
                }
                if (notOk(w.getCWorkCode())) {
                    return fail("工段名称不能为空");
                }
                if (notOk(w.getIDepId())) {
                    return fail("部门编码不能为空");
                }
                if (isOk(findWorkregionmCodeInfo(w.getCWorkCode()))) {
                    deleteCworkCode(w.getCWorkCode());
                }
                if (notOk(w.getIPsLevel())){
                    return fail("排产层级不能为空");
                }
                if (notOk(w.getIWarehouseId())){
                    return fail("关联仓库不能为空");
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

    private void savaModelHandle(List<Workregionm> models) {
        Long userId = JBoltUserKit.getUserId();
        String userName = JBoltUserKit.getUserName();
        for (Workregionm w : models) {
            saveWorkRegionMHandle(w, userId, new Date(), userName, getOrgId(), getOrgCode(), getOrgName());
        }
    }

    public List<Workregionm> dataList() {
        return getCommonList(Okv.by("isDeleted", false).set("isEnabled", true), "iAutoId", "ASC");
    }

    /**
     * 查询产线列表
     */
    public List<Workregionm> selectWorkRegionMList() {
        List<Long> workRegionMIdList = getWorkRegionMIdList();
        if (JBoltUserKit.isSystemAdmin()) {
            return find(selectSql()
                    .eq(Workregionm.ISENABLED, 1)
                    .eq(Workregionm.ISDELETED, 0)
                    .eq(Workregionm.IORGID, getOrgId()));
        }
        if (ObjectUtil.isEmpty(workRegionMIdList)) {
            return new ArrayList<>();
        }
        return find(selectSql()
                .in(Workregionm.IAUTOID, workRegionMIdList)
                .eq(Workregionm.ISENABLED, 1)
                .eq(Workregionm.ISDELETED, 0)
                .eq(Workregionm.IORGID, getOrgId()));
    }

    public List<Workregionm> getWorkregionmList(Long orgId, Long idepid) {
        Okv para = Okv.by("isDeleted", false)
                .set("isEnabled", true)
                .set("iorgid", orgId)
                .set("idepid", idepid);

        return getCommonList("iautoid,cworkname", para, "iautoid", "ASC");
    }

    public List<Workregionm> getIdAndNameList() {
        return find("SELECT iAutoId,cWorkName FROM Bd_WorkRegionM WHERE isDeleted = '0' ");
    }

    public List<Workregionm> findWorkRegionMByLikeId(String ids) {
        return daoTemplate("workregionm.findWorkRegionMByLikeId", Kv.by("ids", ids)).find();
    }

    public Long getCworkcode(String cworkcode) {
        return queryColumn(selectSql().select(Workregionm.IAUTOID).eq(Workregionm.CWORKCODE, cworkcode));
    }

    public void deleteCworkCode(String cworkcode) {
        delete("DELETE FROM Bd_WorkRegionM WHERE cWorkCode = ?", cworkcode);
    }


    /**
     * 获取用户产线信息
     *
     * @return
     */
    public List<Long> getWorkRegionMIdList() {
        /*Long userId = JBoltUserKit.getUserId();
        Person person = personService.findFirst(personService.selectSql()
                .eq(Person.IUSERID, userId));
        if (person == null) {
            return null;
        }
        List<Long> workRegionMIdList = new ArrayList<>();
        List<Team> teamList = teamService.find(teamService.selectSql()
                .eq(Team.IPERSONID, person.getIautoid())
                .eq(Team.ISENABLED, 1)
                .eq(Team.ISDELETED, 0)
                .eq(Team.IORGID, getOrgId()));
        if (ObjectUtil.isNotEmpty(teamList)) {
            workRegionMIdList.addAll(teamList.stream().map(r -> r.getLong(Team.IWORKREGIONMID)).collect(Collectors.toList()));
        }
        List<Workregionm> workRegionMList = find(selectSql()
                .eq(Workregionm.IPERSONID, person.getIautoid())
                .eq(Workregionm.IORGID, getOrgId()));
        if (ObjectUtil.isNotEmpty(workRegionMList)) {
            workRegionMIdList.addAll(workRegionMList.stream().map(BaseWorkregionm::getIautoid).collect(Collectors.toList()));
        }
        return workRegionMIdList;*/
        return null;
    }

    public String findWorkregionmCodeInfo(String cCode) {
        return queryColumn(selectSql().select(Workregionm.CWORKCODE).eq(Workregionm.CWORKCODE, cCode));
    }
    
    public JBoltExcel exportExcelTpl(List<Record> datas) {
        //2、创建JBoltExcel
        JBoltExcel jBoltExcel = JBoltExcel
                .create()//创建JBoltExcel 从模板加载创建
                .addSheet(createJboltExcelSheetTpl().setDataChangeHandler((data, index) -> {//设置数据转换处理器
//                                    //将user_id转为user_name
//                                    data.changeWithKey("user_id", "user_username", CACHE.me.getUserUsername(data.get("user_id")));
//                                    data.changeBooleanToStr("is_deleted", "是", "否");
                                })
                                .setRecordDatas(3,datas)//设置数据
                )
                .setXlsx(true)
                .setFileName("产线档案"+ "_"+ DateUtil.today());
        //3、返回生成的excel文件
        return jBoltExcel;
    }




    /**
     * 统一导入导出模板
     * @return
     */
    private JBoltExcelSheet createJboltExcelSheetTpl(){
        JBoltExcelSheet jBoltExcelSheet = JBoltExcelSheet.create("sheet");
        jBoltExcelSheet.setHeaders(1,
                JBoltExcelHeader.create("cworkcode", "产线编码", 20),
                JBoltExcelHeader.create("cworkname", "产线名称", 20),
                JBoltExcelHeader.create("cdepname", "所属部门", 20),
                JBoltExcelHeader.create("cpersonname", "产线长", 20),
                JBoltExcelHeader.create("ipslevel", "排产层级", 15),
                JBoltExcelHeader.create("cwhname", "关联仓库名称", 20),
                JBoltExcelHeader.create("cmemo", "备注",20)
        ).setMerges(JBoltExcelMerge.create(0,0,6,"产线档案"))
        ;
        return jBoltExcelSheet;
    }



    
}
