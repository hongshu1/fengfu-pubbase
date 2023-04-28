package cn.rjtech.admin.person;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.jbolt._admin.dept.DeptService;
import cn.jbolt._admin.dictionary.DictionaryService;
import cn.jbolt._admin.user.UserService;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.cache.JBoltDictionaryCache;
import cn.jbolt.core.cache.JBoltUserCache;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.Dictionary;
import cn.jbolt.core.model.User;
import cn.jbolt.core.poi.excel.JBoltExcel;
import cn.jbolt.core.poi.excel.JBoltExcelHeader;
import cn.jbolt.core.poi.excel.JBoltExcelSheet;
import cn.jbolt.core.poi.excel.JBoltExcelUtil;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.core.util.JBoltDateUtil;
import cn.jbolt.core.util.JBoltStringUtil;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.equipment.EquipmentService;
import cn.rjtech.admin.personequipment.PersonEquipmentService;
import cn.rjtech.admin.workclass.WorkClassService;
import cn.rjtech.config.DictionaryTypeKey;
import cn.rjtech.enums.SourceEnum;
import cn.rjtech.model.momdata.Equipment;
import cn.rjtech.model.momdata.Person;
import cn.rjtech.model.momdata.PersonEquipment;
import cn.rjtech.model.momdata.Workclass;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 人员档案 Service
 *
 * @ClassName: PersonService
 * @author: WYX
 * @date: 2023-03-21 15:11
 */
public class PersonService extends BaseService<Person> {

    @Inject
    private UserService userService;
    @Inject
    private DeptService deptService;
    @Inject
    private EquipmentService equipmentService;
    @Inject
    private WorkClassService workClassService;
    @Inject
    private DictionaryService dictionaryService;
    @Inject
    private PersonEquipmentService personEquipmentService;


    private final Person dao = new Person().dao();

    @Override
    protected Person dao() {
        return dao;
    }

    /**
     * 后台管理分页查询
     */
    public Page<Record> paginateAdminDatas(int pageNumber, int pageSize, Kv para) {
        para.set("iorgid", getOrgId());
        Boolean isenabled = para.getBoolean("isenabled");
        //是否启用boolean转char
        String isenabledStr = isenabled == null ? null : (isenabled ? "1" : "0");
        para.set("isenabled", isenabledStr);
        Page<Record> pageList = dbTemplate("person.paginateAdminDatas", para).paginate(pageNumber, pageSize);
        for (Record row : pageList.getList()) {
            row.set("cdepname", deptService.findNameBySn(row.getStr("cdept_num")));
            row.set("cusername", JBoltUserCache.me.getUserName(row.getLong("iuserid")));
            row.set("sysworkage", calcSysworkage(row.getDate("dhiredate")));
        }
        return pageList;
    }

    /**
     * 保存
     */
    public Ret save(Person person) {
        if (person == null || isOk(person.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        if (exists(Person.IUSERID, person.getIUserId())) {
            return fail(JBoltMsg.DATA_SAME_NAME_EXIST);
        }
        boolean success = person.save();
        if (success) {
            //添加日志
            //addSaveSystemLog(person.getIautoid(), JBoltUserKit.getUserId(), person.getName());
        }
        return ret(success);
    }

    /**
     * 更新
     */
    public Ret update(Person person) {
        if (person == null || notOk(person.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //更新时需要判断数据存在
        Person dbPerson = findById(person.getIAutoId());
        if (dbPerson == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        if (ObjUtil.isNull(dbPerson.getIUserId())) {
            if (exists(Person.IUSERID, person.getIUserId())) {
                return fail(JBoltMsg.DATA_SAME_NAME_EXIST);
            }
        } else {
            if (ObjUtil.notEqual(person.getIUserId(), dbPerson.getIUserId())) {
                if (exists(Person.IUSERID, person.getIUserId(), dbPerson.getIUserId())) {
                    return fail(JBoltMsg.DATA_SAME_NAME_EXIST);
                }
            }
        }
        boolean success = person.update();
        if (success) {
            //添加日志
            //addUpdateSystemLog(person.getIautoid(), JBoltUserKit.getUserId(), person.getName());
        }
        return ret(success);
    }

    /**
     * 删除 指定多个ID
     */
    public Ret deleteByBatchIds(String ids) {
        return deleteByIds(ids, true);
    }

    /**
     * 删除
     */
    public Ret delete(Long id) {
        return updateColumn(id, "isdeleted", true);
    }

    /**
     * 删除
     */
    public Ret deleteByAjax() {
        return SUCCESS;
    }

    /**
     * 删除数据后执行的回调
     *
     * @param person 要删除的model
     * @param kv     携带额外参数一般用不上
     */
    @Override
    protected String afterDelete(Person person, Kv kv) {
        //addDeleteSystemLog(person.getIautoid(), JBoltUserKit.getUserId(),person.getName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param person 要删除的model
     * @param kv     携带额外参数一般用不上
     */
    @Override
    public String checkCanDelete(Person person, Kv kv) {
        //如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
        return checkInUse(person, kv);
    }

    /**
     * 设置返回二开业务所属的关键systemLog的targetType
     */
    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

    /**
     * 切换bpsnperson属性
     */
    public Ret toggleBPsnPerson(Long id) {
        return toggleBoolean(id, "bPsnPerson");
    }

    /**
     * 切换bprobation属性
     */
    public Ret toggleBProbation(Long id) {
        return toggleBoolean(id, "bProbation");
    }

    /**
     * 切换bdutylock属性
     */
    public Ret toggleBDutyLock(Long id) {
        return toggleBoolean(id, "bDutyLock");
    }

    /**
     * 切换bpsnshop属性
     */
    public Ret toggleBpsnshop(Long id) {
        return toggleBoolean(id, "bpsnshop");
    }

    /**
     * 切换isenabled属性
     */
    public Ret toggleIsEnabled(Long id) {
        return toggleBoolean(id, "isEnabled");
    }

    /**
     * 切换isdeleted属性
     */
    public Ret toggleIsDeleted(Long id) {
        return toggleBoolean(id, "isDeleted");
    }

    /**
     * 检测是否可以toggle操作指定列
     *
     * @param person 要toggle的model
     * @param column 操作的哪一列
     * @param kv     携带额外参数一般用不上
     */
    @Override
    public String checkCanToggle(Person person, String column, Kv kv) {
        //没有问题就返回null  有问题就返回提示string 字符串
        return null;
    }

    /**
     * toggle操作执行后的回调处理
     */
    @Override
    protected String afterToggleBoolean(Person person, String column, Kv kv) {
        //addUpdateSystemLog(person.getIautoid(), JBoltUserKit.getUserId(), person.getName(),"的字段["+column+"]值:"+person.get(column));
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param person model
     * @param kv     携带额外参数一般用不上
     */
    @Override
    public String checkInUse(Person person, Kv kv) {
        //这里用来覆盖 检测Person是否被其它表引用
        return null;
    }

    /**
     * 表格提交
     */
    public Ret submitTable(JBoltTable jBoltTable) {
        Person person = jBoltTable.getFormModel(Person.class, "person");
        ValidationUtils.notNull(person, JBoltMsg.PARAM_ERROR);

        Long userid = JBoltUserKit.getUserId();
        String username = JBoltUserKit.getUserName();
        Date now = new Date();

        tx(() -> {
            if (person.getIAutoId() == null) {
                if (ObjUtil.isNotNull(person.getIUserId())) {
                    ValidationUtils.isTrue(!exists(Person.IUSERID, person.getIUserId()), "用户名已被占用");
                }

                person.setISource(SourceEnum.MES.getValue())
                        .setICreateBy(userid)
                        .setCCreateName(username)
                        .setDCreateTime(now)
                        .setIUpdateBy(userid)
                        .setCUpdateName(username)
                        .setDUpdateTime(now)
                        .setIsDeleted(false);
                ValidationUtils.isTrue(person.save(), JBoltMsg.FAIL);
            } else {
                Person dbPerson = findById(person.getIAutoId());
                ValidationUtils.notNull(dbPerson, "人员记录不存在");
                ValidationUtils.isTrue(!dbPerson.getIsDeleted(), "人员记录不存在");

                if (ObjUtil.isNull(dbPerson.getIUserId())) {
                    ValidationUtils.isTrue(!exists(Person.IUSERID, person.getIUserId()), "用户名已被占用");
                } else {
                    if (ObjUtil.notEqual(person.getIUserId(), dbPerson.getIUserId())) {
                        ValidationUtils.isTrue(!exists(Person.IUSERID, person.getIUserId(), dbPerson.getIUserId()), "用户名已被占用");
                    }
                }

                person.setIUpdateBy(userid)
                        .setCUpdateName(username)
                        .setDUpdateTime(now);
                ValidationUtils.isTrue(person.update(), JBoltMsg.FAIL);
            }

            // 新增人员设备档案
            personEquipmentService.addSubmitTableDatas(jBoltTable, person.getIAutoId());
            // 修改人员设备档案
            personEquipmentService.updateSubmitTableDatas(jBoltTable);
            // 删除人员设备档案
            personEquipmentService.deleteSubmitTableDatas(jBoltTable);

            return true;
        });

        return SUCCESS;
    }

    /**
     * 数据导入
     */
    public Ret importExcelDatas(List<UploadFile> fileList, Kv para) {
        String iscover = JBoltStringUtil.isBlank(para.getStr("iscover")) ? "1" : "0";
        int startRow = 2;
        for (UploadFile uploadFile : fileList) {
            StringBuilder errorMsg = new StringBuilder();
            JBoltExcel excel = JBoltExcel
                    // 从excel文件创建JBoltExcel实例
                    .from(uploadFile.getFile())
                    // 设置工作表信息
                    .setSheets(
                            JBoltExcelSheet.create("Sheet1")
                                    // 设置列映射 顺序 标题名称
                                    .setHeaders(1,
                                            JBoltExcelHeader.create("indexnum", "序号"),
                                            JBoltExcelHeader.create("iuserid", "所属用户名"),
                                            JBoltExcelHeader.create("cpsnnum", "人员编码"),
                                            JBoltExcelHeader.create("cpsnname", "姓名"),
                                            JBoltExcelHeader.create("vidno", "证件号码"),
                                            JBoltExcelHeader.create("isex", "性别"),
                                            JBoltExcelHeader.create("rpersontype", "人员类别"),
                                            JBoltExcelHeader.create("cpsnmobilephone", "手机号"),
                                            JBoltExcelHeader.create("jobnumber", "工号"),
                                            JBoltExcelHeader.create("cecardno", "电子卡号"),
                                            JBoltExcelHeader.create("cdeptnum", "所属部门编码"),
                                            JBoltExcelHeader.create("remploystate", "在职状态"),
                                            JBoltExcelHeader.create("dhiredate", "入职日期"),
                                            JBoltExcelHeader.create("iworkclassid", "所属工种"),
                                            JBoltExcelHeader.create("dbirthdate", "出生日期"),
                                            JBoltExcelHeader.create("cpsnemail", "邮箱"),
                                            JBoltExcelHeader.create("isenabled", "是否启用"),
                                            JBoltExcelHeader.create("cmemo", "备注"),
                                            JBoltExcelHeader.create("cequipmentcode", "人员设备")
                                    )
                                    // 从第三行开始读取
                                    .setDataStartRow(startRow)
                    );

            // 从指定的sheet工作表里读取数据
            List<Record> rows = JBoltExcelUtil.readRecords(excel, 0, true, errorMsg);
            if (notOk(rows)) {
                if (errorMsg.length() > 0) {
                    return fail(errorMsg.toString());
                } else {
                    return fail(JBoltMsg.DATA_IMPORT_FAIL_EMPTY);
                }
            }
            //校验数据的有效性
            constructPersonModelCheckDatasEffectiveColumnByExcelDatas(rows, errorMsg, startRow);
            ValidationUtils.isTrue(errorMsg.length() == 0, errorMsg.toString());
            //构造model
            List<Person> personList = new ArrayList<Person>();
            List<PersonEquipment> PersonEquipmentList = new ArrayList<PersonEquipment>();
            constructPersonModelByExcelDatas(rows, personList, PersonEquipmentList);
            tx(() -> {
                batchSave(personList);
                personEquipmentService.batchSave(PersonEquipmentList);
                return true;
            });
            return SUCCESS;
        }
        return null;
    }

    /**
     * 导入功能-校验每行数据的有效性：非空 ，数字等
     */
    public void constructPersonModelCheckDatasEffectiveColumnByExcelDatas(List<Record> excelRecordList, StringBuilder errorMsg, int startRow) {
        if (CollUtil.isEmpty(excelRecordList)) return;
        for (Record excelRecord : excelRecordList) {
            String cpsnNum = excelRecord.getStr("cpsnnum");
            String cpsnName = excelRecord.getStr("cpsnname");
            if (JBoltStringUtil.isBlank(cpsnNum)) errorMsg.append("第").append(startRow).append("行,[人员编码]为空,请检查!<br/>");
            if (JBoltStringUtil.isBlank(cpsnName)) errorMsg.append("第").append(startRow).append("行,[姓名]为空,请检查!<br/>");
            try {
                excelRecord.getDate("dhiredate");
            } catch (Exception e) {
                errorMsg.append("第").append(startRow).append("行,[入职日期]格式不正确,请检查!<br/>");
            }
            try {
                excelRecord.getDate("dbirthdate");
            } catch (Exception e) {
                errorMsg.append("第").append(startRow).append("行,[出生日期]格式不正确,请检查!<br/>");
            }

        }
    }

    /**
     * 导入功能-构造model
     */
    public void constructPersonModelByExcelDatas(List<Record> excelRecordList, List<Person> personList, List<PersonEquipment> personEquipmentList) {
        if (CollUtil.isEmpty(excelRecordList)) return;
        for (Record excelRecord : excelRecordList) {
            Person person = new Person();
            Long personId = JBoltSnowflakeKit.me.nextId();
            person.setIAutoId(personId);
            String username = excelRecord.getStr("iuserid");
            User user = userService.getUserByUserName(username);
            if (user != null) person.setIUserId(user.getId());
            person.setCpsnNum(excelRecord.getStr("cpsnnum"));
            person.setCpsnName(excelRecord.getStr("cpsnname"));
            person.setVIDNo(excelRecord.getStr("vidno"));
            String isexStr = excelRecord.getStr("isex");
            Integer isex = JBoltStringUtil.isBlank(isexStr) ? null : ("男".equals(isexStr) ? 1 : 2);
            person.setISex(isex);
            //获取封装的事业类型的字典
            Record rpersontypeDictionaryRecord = dictionaryService.convertEnumByTypeKey(DictionaryTypeKey.RPERSONTYPE);
            String rpersontype = excelRecord.getStr("rpersontype");
            rpersontype = JBoltStringUtil.isNotBlank(rpersontype) ? rpersontypeDictionaryRecord.getStr(rpersontype) : rpersontype;
            person.setRPersonType(rpersontype);
            person.setCPsnMobilePhone(excelRecord.getStr("cpsnmobilephone"));
            person.setJobNumber(excelRecord.getStr("jobnumber"));
            person.setCEcardNo(excelRecord.getStr("cecardno"));
            person.setCdeptNum(deptService.findNameBySn(excelRecord.getStr("cdeptnum")));
            Record remploystateDictionaryRecord = dictionaryService.convertEnumByTypeKey(DictionaryTypeKey.JOB_TYPE);
            String remploystate = excelRecord.getStr("remploystate");
            remploystate = JBoltStringUtil.isNotBlank(remploystate) ? remploystateDictionaryRecord.getStr(remploystate) : remploystate;
            person.setVIDNo(remploystate);
            person.setDHireDate(excelRecord.getDate("dhiredate"));
            String cWorkClassCode = excelRecord.getStr("iworkclassid");
            Workclass workClass = workClassService.findModelByCode(cWorkClassCode);
            if (workClass != null) person.setIWorkClassId(workClass.getIautoid());
            person.setDBirthDate(excelRecord.getStr("dbirthdate"));
            person.setCPsnEmail(excelRecord.getStr("cpsnemail"));
            String isenabled = excelRecord.getStr("isenabled");
            person.setIsEnabled(Objects.equals(isenabled, "是"));
            person.setCMemo(excelRecord.getStr("cmemo"));
            person.setIOrgId(getOrgId());
            person.setCOrgCode(getOrgCode());
            person.setCOrgName(getOrgName());
            User loginUser = JBoltUserKit.getUser();
            Date now = new Date();
            person.setICreateBy(loginUser.getId());
            person.setCCreateName(loginUser.getName());
            person.setDCreateTime(now);
            person.setIUpdateBy(loginUser.getId());
            person.setDUpdateTime(now);
            person.setCUpdateName(loginUser.getName());
            person.setISource(SourceEnum.MES.getValue());
            personList.add(person);
            String cequipmentcodes = excelRecord.getStr("cequipmentcode");
            if (JBoltStringUtil.isBlank(cequipmentcodes)) return;
            for (String cequipmentcode : cequipmentcodes.split(",")) {
                Equipment equipment = equipmentService.findModelByCode(cequipmentcode);
                if (equipment == null) continue;
                PersonEquipment personEquipment = new PersonEquipment();
                personEquipment.setIAutoId(JBoltSnowflakeKit.me.nextId());
                personEquipment.setIPersonId(personId);
                personEquipment.setIEquipmentId(equipment.getIAutoId());
                personEquipment.setIsDeleted(false);
                personEquipmentList.add(personEquipment);
            }
        }
    }

    public Object formatPattenSn(String value, String key) {
        if (notNull(value)) {
            if (notNull(key)) {
                List<Dictionary> list = JBoltDictionaryCache.me.getListByTypeKey(key, true);
                Dictionary find = list.stream().filter(dictionary -> StringUtils.equalsIgnoreCase(dictionary.getSn(), String.valueOf(value))).findFirst().orElse(null);
                return find == null ? null : find.getName();
            }
        }
        return null;
    }

    public Page<Person> paginateDatas(int pageNumber, int pageSize, Kv kv) {
        return daoTemplate("person.findAll", kv).paginate(pageNumber, pageSize);
    }

    public List<Person> findAll(Kv kv) {
        return daoTemplate("person.findAll", kv).find();
    }

    /**
     * 计算工龄
     */
    public BigDecimal calcSysworkage(Date date) {
        if (date == null) return null;
        return BigDecimal.valueOf(JBoltDateUtil.daysBetween(date, new Date()) / 365d).setScale(2, RoundingMode.HALF_UP);
    }

    public List<Person> list(Kv kv) {
        return find("SELECT * FROM Bd_Person WHERE iUserId = ?", kv.get("iuserid"));
    }

}
