package cn.rjtech.admin.workregionm;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.StrSplitter;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.poi.excel.*;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.cusfieldsmappingd.CusFieldsMappingDService;
import cn.rjtech.admin.department.DepartmentService;
import cn.rjtech.admin.person.PersonService;
import cn.rjtech.admin.warehouse.WarehouseService;
import cn.rjtech.enums.SourceEnum;
import cn.rjtech.model.momdata.Department;
import cn.rjtech.model.momdata.Person;
import cn.rjtech.model.momdata.Warehouse;
import cn.rjtech.model.momdata.Workregionm;
import cn.rjtech.util.ValidationUtils;
import cn.rjtech.wms.utils.StringUtils;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.io.File;
import java.util.*;

import static cn.hutool.core.text.StrPool.COMMA;

/**
 * 工段档案 Service
 * @ClassName: WorkregionmService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-11-01 20:42
 */
public class WorkregionmService extends BaseService<Workregionm> {

    private final Workregionm dao = new Workregionm().dao();

    @Inject
    private PersonService personService;
    @Inject
    private DepartmentService departmentService;
    @Inject
    private WarehouseService warehouseService;
    @Inject
    private CusFieldsMappingDService cusFieldsMappingDService;

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
        Long iDepId = workregionm.getIDepId();
        Department department = departmentService.findByid(iDepId);
        workregionm.setCDepCode(department.getCDepCode());
        workregionm.setCDepName(department.getCDepName());

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
        if (ObjectUtil.isNotNull(workregionm.getIDepId())){
            Department department = departmentService.findById(workregionm.getIDepId());
            workregionm.setCDepCode(department.getCDepCode());
            workregionm.setCDepName(department.getCDepName());
        }
        if (ObjectUtil.isNotNull(workregionm.getIPersonId())){
            Person person = personService.findById(workregionm.getIPersonId());
            workregionm.setCPersonCode(person.getCpsnNum());
            workregionm.setCPersonName(person.getCpsnName());
        }
        //更新时需要判断数据存在
        Workregionm dbWorkregionm = findById(workregionm.getIAutoId());
        if (dbWorkregionm == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        if (!workregionm.getCWorkCode().equals(dbWorkregionm.getCWorkCode())) {
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
        StringBuilder errorMsg = new StringBuilder();

        JBoltExcel excel = JBoltExcel
                // 从excel文件创建JBoltExcel实例
                .from(file)
                // 设置工作表信息
                .setSheets(
                        JBoltExcelSheet.create("workregionm")
                                // 设置列映射 顺序 标题名称
                                .setHeaders(2,
                                        JBoltExcelHeader.create("cworkcode", "产线编码"),
                                        JBoltExcelHeader.create("cworkname", "产线名称"),
                                        JBoltExcelHeader.create("cdepname", "所属部门"),
                                        JBoltExcelHeader.create("cpersonname", "产线长"),
                                        JBoltExcelHeader.create("ipslevel", "排产层级"),
                                        JBoltExcelHeader.create("cwarehousename", "关联仓库名称不能为空"),
                                        JBoltExcelHeader.create("cmemo", "备注")
                                )
                                // 从第三行开始读取
                                .setDataStartRow(3)
                );

        // 从指定的sheet工作表里读取数据
        List<Record> records = JBoltExcelUtil.readRecords(excel, 0, true, errorMsg);
        if (notOk(records)) {
            if (errorMsg.length() > 0) {
                return fail(errorMsg.toString());
            } else {
                return fail("数据为空!");
            }
        }
        List<String> errList = new ArrayList<>();


        //List<Record> records = cusFieldsMappingDService.getImportRecordsByTableName(file, table());
        if (notOk(records)) {
            return fail(JBoltMsg.DATA_IMPORT_FAIL_EMPTY);
        }

        Date now = new Date();
        
        // 部门
        Map<String, Department> departmentMap = new HashMap<>();
        // 仓库
        Map<String, Warehouse> warehouseMap = new HashMap<>();
        // 人员
        Map<String, Person> personMap = new HashMap<>();
        
        for (Record record : records) {

            if (StrUtil.isBlank(record.getStr("cworkcode"))) {
                return fail("产线编码不能为空");
            } else {
                String cWorkCode = record.getStr("cworkcode");
                Workregionm dbModel = findByCworkcode(getOrgId(), cWorkCode);
                ValidationUtils.isTrue(dbModel == null, cWorkCode + ",已存在该条码!");
            }

            if (StrUtil.isBlank(record.getStr("cworkname"))) {
                return fail("产线名称不能为空");
            }
            if (StrUtil.isBlank(record.getStr("cdepname"))) {
                return fail("所属部门不能为空");
            }
//            if (StrUtil.isBlank(record.getStr("cPersonName"))) {
//                return fail("产线长不能为空");
//            }
            if (StrUtil.isBlank(record.getStr("ipslevel"))) {
                return fail("排产层级不能为空");
            }
            
            int ipslevel = record.getInt("ipslevel");
            ValidationUtils.isTrue(ipslevel >= 1 && ipslevel <= 7, "排产层级只能输入1至7");

            if (StrUtil.isBlank(record.getStr("cwarehousename"))) {
                return fail("关联仓库名称不能为空");
            }

            // ---------------------------------------
            // 部门
            // ---------------------------------------
            String ccdepname = record.getStr("cdepname");

            Department department = departmentMap.get(ccdepname);
            
            if (ObjUtil.isNull(department)) {
                department = departmentService.findByCdepName(ccdepname);
                ValidationUtils.notNull(department, String.format("部门“%s”不存在", ccdepname));

                departmentMap.put(ccdepname, department);
            }

            // ---------------------------------------
            // 仓库
            // ---------------------------------------
            
            String cWarehouseName = record.getStr("cwarehousename");

            Warehouse warehouse = warehouseMap.get(cWarehouseName);
            
            if (ObjUtil.isNull(warehouse)) {
                warehouse = warehouseService.findByWhName(cWarehouseName);
                ValidationUtils.notNull(warehouse, "仓库不存在");
                warehouseMap.put(cWarehouseName, warehouse);
            }

            // ---------------------------------------
            // 人员
            // ---------------------------------------

            String cpersonname = record.getStr("cpersonname");
            if(StringUtils.isNotBlank(cpersonname)){
                Person person = personMap.get(cpersonname);
                if (ObjUtil.isNull(person)) {
                    person = personService.findFirstByCpersonName(cpersonname);
                    ValidationUtils.notNull(person, String.format("人员“%s”不存在", cpersonname));

                    personMap.put(cpersonname, person);

                    record.set("iPersonId", person.getIAutoId());
                    record.set("cPersonCode", person.getCpsnNum());
                }


            }




            record.set("cDepCode", department.getCDepCode());
            record.set("iDepId", department.getIAutoId());
            record.set("iWarehouseId", warehouse.getIAutoId());
            record.set("cWarehouseCode", warehouse.getCWhCode());

            record.set("iAutoId", JBoltSnowflakeKit.me.nextId());
            record.set("iCreateBy", JBoltUserKit.getUserId());
            record.set("dCreateTime", now);
            record.set("cCreateName", JBoltUserKit.getUserName());
            record.set("iOrgId", getOrgId());
            record.set("cOrgCode", getOrgCode());
            record.set("cOrgName", getOrgName());
            record.set("iUpdateBy", JBoltUserKit.getUserId());
            record.set("dUpdateTime", now);
            record.set("cUpdateName", JBoltUserKit.getUserName());
            record.set("iSource", SourceEnum.MES.getValue());
        }
        
        // 执行批量操作
        tx(() -> {
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
                                    data.change("idepid", departmentService.getIdepIdByCode(data.getStr("idepid")));
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
                if (notOk(w.getIPsLevel())) {
                    return fail("排产层级不能为空");
                }
                if (notOk(w.getIWarehouseId())) {
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
        if (ObjUtil.isEmpty(workRegionMIdList)) {
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
                .setIfNotNull("iorgid", orgId)
                .setIfNotNull("idepid", idepid);

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


    public Workregionm findByCworkcode(Long orgId, String cworkcode) {
        return findFirst(selectSql().eq(Workregionm.IORGID, orgId).eq(Workregionm.CWORKCODE, cworkcode).eq(Workregionm.ISDELETED, ZERO_STR).first());
    }


    public void deleteCworkCode(String cworkcode) {
        delete("DELETE FROM Bd_WorkRegionM WHERE cWorkCode = ?", cworkcode);
    }

    /**
     * 获取用户产线信息
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
        if (ObjUtil.isNotEmpty(teamList)) {
            workRegionMIdList.addAll(teamList.stream().map(r -> r.getLong(Team.IWORKREGIONMID)).collect(Collectors.toList()));
        }
        List<Workregionm> workRegionMList = find(selectSql()
                .eq(Workregionm.IPERSONID, person.getIautoid())
                .eq(Workregionm.IORGID, getOrgId()));
        if (ObjUtil.isNotEmpty(workRegionMList)) {
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
        //3、返回生成的excel文件
        return JBoltExcel
                .create()//创建JBoltExcel 从模板加载创建
                .addSheet(createJboltExcelSheetTpl().setDataChangeHandler((data, index) -> {//设置数据转换处理器
//                                    //将user_id转为user_name
//                                    data.changeWithKey("user_id", "user_username", CACHE.me.getUserUsername(data.get("user_id")));
//                                    data.changeBooleanToStr("is_deleted", "是", "否");
                                })
                                .setRecordDatas(3, datas)//设置数据
                )
                .setXlsx(true)
                .setFileName("产线档案" + "_" + DateUtil.today());
    }


    /**
     * 统一导入导出模板
     */
    private JBoltExcelSheet createJboltExcelSheetTpl() {
        JBoltExcelSheet jBoltExcelSheet = JBoltExcelSheet.create("sheet");
        
        jBoltExcelSheet.setHeaders(1,
                JBoltExcelHeader.create("cworkcode", "产线编码", 20),
                JBoltExcelHeader.create("cworkname", "产线名称", 20),
                JBoltExcelHeader.create("cdepname", "所属部门", 20),
                JBoltExcelHeader.create("cpersonname", "产线长", 20),
                JBoltExcelHeader.create("ipslevel", "排产层级", 15),
                JBoltExcelHeader.create("cwhname", "关联仓库名称", 20),
                JBoltExcelHeader.create("cmemo", "备注", 20)
        ).setMerges(JBoltExcelMerge.create(0, 0, 6, "产线档案"));
        return jBoltExcelSheet;

    }

    /**
     *根据产线名称获取数据
     */
    public Workregionm findFirstByWorkName(String cWorkName) {
        return  findFirst(selectSql().select().eq(Workregionm.CWORKNAME, cWorkName));
    }



}
