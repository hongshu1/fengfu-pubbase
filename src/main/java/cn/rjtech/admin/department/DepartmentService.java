package cn.rjtech.admin.department;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.User;
import cn.jbolt.core.poi.excel.JBoltExcel;
import cn.jbolt.core.poi.excel.JBoltExcelHeader;
import cn.jbolt.core.poi.excel.JBoltExcelSheet;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.enums.IsEnableEnum;
import cn.rjtech.model.momdata.Department;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;

/**
 * 组织建模-部门档案
 *
 * @ClassName: DepartmentService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-22 11:55
 */
public class DepartmentService extends BaseService<Department> {
    private final Department dao = new Department().dao();
    // 证正校验规则
    private final String postCodeRegex = "";
    private final String emailRegex = "^(\\w+([-.][A-Za-z0-9]+)*){3,18}@\\w+([-.][A-Za-z0-9]+)*\\.\\w+([-.][A-Za-z0-9]+)*$";
    private final String phoneRegex = "0\\d{2,3}[-]?\\d{7,8}|0\\d{2,3}\\s?\\d{7,8}|13[0-9]\\d{8}|15[1089]\\d{8}";

    @Override
    protected Department dao() {
        return dao;
    }

    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

    /**
     * 后台管理数据查询
     *
     * @param pageNumber 第几页
     * @param pageSize   每页几条数据
     * @param sortColumn 排序列名
     * @param sortType   排序方式 asc desc
     */
    public Page<Record> getAdminDatas(int pageNumber, int pageSize, Kv kv, String sortColumn, String sortType) {
        return dbTemplate("department.list", kv.set("sortColumn", sortColumn).set("sortType", sortType).set("orgId", getOrgId())).paginate(pageNumber, pageSize);
    }

    public List<Record> findAll(Kv kv) {

        return dbTemplate("department.list", kv.set("orgId", getOrgId())).find();
    }

    //根据部门名称查询部门编码
    public String findCodeByDepName(String cDepName){
        return queryColumn("select cDepCode from Bd_Department where cDepName=?",cDepName);
    }

    //根据部门名称查询部门ID
    public Long findIdByDepName(String cDepName){
        return queryColumn("select iAutoId from Bd_Department where cDepName=?",cDepName);
    }



    /**
     * 保存
     */
    public Ret save(Department department) {
        verifyData(department);
        User user = JBoltUserKit.getUser();
        DateTime date = DateUtil.date();
        department.setICreateBy(user.getId());
        department.setDCreateTime(date);
        department.setCCreateName(user.getUsername());
        department.setIOrgId(getOrgId());
        department.setCOrgCode(getOrgCode());
        department.setIUpdateBy(user.getId());
        department.setDUpdateTime(date);
        department.setCUpdateName(user.getUsername());
        department.setIsDeleted(false);
        department.setIsDeleted(true);
        //if(existsName(department.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = department.save();
        if (success) {
            //添加日志
            //addSaveSystemLog(department.getIAutoId(), JBoltUserKit.getUserId(), department.getName());
        }
        return ret(success);
    }

    /**
     * 更新
     */
    public Ret update(Department department) {
        if (department == null || notOk(department.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        verifyData(department);
        department.setIUpdateBy(JBoltUserKit.getUserId());
        department.setCUpdateName(JBoltUserKit.getUserName());
        department.setDUpdateTime(DateUtil.date());
        //if(existsName(department.getName(), department.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = department.update();
        if (success) {
            //添加日志
            //addUpdateSystemLog(department.getIAutoId(), JBoltUserKit.getUserId(), department.getName());
        }
        return ret(success);
    }

    /**
     * 删除数据后执行的回调
     *
     * @param department 要删除的model
     * @param kv         携带额外参数一般用不上
     */
    @Override
    protected String afterDelete(Department department, Kv kv) {
        //addDeleteSystemLog(department.getIAutoId(), JBoltUserKit.getUserId(),department.getName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param department model
     * @param kv         携带额外参数一般用不上
     */
    @Override
    public String checkInUse(Department department, Kv kv) {
        //这里用来覆盖 检测是否被其它表引用
        return null;
    }

    /**
     * toggle操作执行后的回调处理
     */
    @Override
    protected String afterToggleBoolean(Department department, String column, Kv kv) {
        //addUpdateSystemLog(department.getIAutoId(), JBoltUserKit.getUserId(), department.getName(),"的字段["+column+"]值:"+department.get(column));
        /*
         switch(column){
         case "bDepEnd":
         break;
         case "isEnabled":
         break;
         case "isApsInvoled":
         break;
         case "isDeleted":
         break;
         }
         */
        return null;
    }

    public void verifyData(Department department) {
        ValidationUtils.notNull(department, JBoltMsg.DATA_NOT_EXIST);
        ValidationUtils.notBlank(department.getCDepCode(), "部门编码不能为空");
        ValidationUtils.notBlank(department.getCDepName(), "部门名称不能为空");
        ValidationUtils.notBlank(department.getCType(), "组织类型不能为空");
        boolean depNameFlag = true;
        boolean flag = true;
        if (department.getIAutoId() != null) {
            //更新时需要判断数据存在
            Department dbDepartment = findById(department.getIAutoId());
            ValidationUtils.notNull(dbDepartment, JBoltMsg.DATA_NOT_EXIST);
            String cDepName = dbDepartment.getCDepName();
            String cDepCode = dbDepartment.getCDepCode();
            if (department.getCDepCode().equals(cDepCode)) {
                flag = false;
            }
            if (department.getCDepName().equals(cDepName)) {
                depNameFlag = false;
            }
        }

        checkRepetitionData(depNameFlag, Department.CDEPNAME, department.getCDepName(), "部门名称重复");
        checkRepetitionData(flag, Department.CDEPCODE, department.getCDepCode(), "部门编码重复");
		
		/*String cDepPhone = department.getCDepPhone();
		if (StrUtil.isNotBlank(cDepPhone)){
			ValidationUtils.isTrue(cDepPhone.matches(phoneRegex), "手机号格式不正确");
		}
		
		String cDepEmail = department.getCDepEmail();
		if (StrUtil.isNotBlank(cDepEmail)){
			ValidationUtils.isTrue(cDepEmail.matches(emailRegex), "Email格式不正确");
		}
		
		String cDepPostCode = department.getCDepPostCode();
		if (StrUtil.isNotBlank(cDepPostCode)){
			ValidationUtils.isTrue(cDepPostCode.matches(postCodeRegex), "必须为6位邮政编码 例：6473098");
		}*/
    }

    public void checkRepetitionData(boolean isCheckFlag, String keyField, String value, String errMsg) {
        if (!isCheckFlag) {
            return;
        }
        Okv okv = Okv.by("keyField", keyField).set("value", value);
        Integer integer = dbTemplate("department.checkRepetitionData", okv).queryInt();
        boolean flag = integer == null || (integer == 0);
        ValidationUtils.isTrue(flag, errMsg);
    }

    public List<Record> findParentData(Long excludeId) {
        return dbTemplate("department.list", Kv.by("excludeId", excludeId)).find();
    }

    public Ret delByIds(String ids) {
        tx(() -> {
            List<Department> departments = getListByIds(ids);
            for (Department department : departments) {
                department.setIsDeleted(true);
                update(department);
            }
            return true;
        });
        return SUCCESS;
    }

    /**
     * 统一导入导出模板
     */
    private JBoltExcelSheet createJboltExcelSheetTpl() {
        JBoltExcelSheet jBoltExcelSheet = JBoltExcelSheet.create("sheet");
        jBoltExcelSheet.setHeaders(JBoltExcelHeader.create("cdepcode", "部门编码", 20), JBoltExcelHeader.create("cdepname", "部门名称", 20), JBoltExcelHeader.create("cdeptype", "部门类型", 20), JBoltExcelHeader.create("cpersonname", "负责人", 20), JBoltExcelHeader.create("isapsinvoled", "是否参与排产", 15), JBoltExcelHeader.create("dcreatetime", "创建时间", 20), JBoltExcelHeader.create("cdepmemo", "备注", 20));
        return jBoltExcelSheet;
    }

    public JBoltExcel exportExcelTpl(List<Record> datas) {
        //2、创建JBoltExcel
        //3、返回生成的excel文件
        return JBoltExcel.create()//创建JBoltExcel 从模板加载创建
                .addSheet(createJboltExcelSheetTpl().setDataChangeHandler((data, index) -> {
                    // 设置数据转换处理器
                    data.changeBooleanToStr("isapsinvoled", "是", "否");
                }).setRecordDatas(2, datas)).setFileName("部门档案" + "_" + DateUtil.today());
    }

    public Department findByCdepcode(Long orgId, String cdepcode) {
        return findFirst(selectSql().eq(Department.IORGID, orgId).eq(Department.CDEPCODE, cdepcode).eq(Department.ISDELETED, ZERO_STR).first());
    }

    /**
     * 通过部门编码查询部门
     *
     * @param cdepcode 部门编码
     * @return 部门档案记录
     */
    public Department findByCdepcode(String cdepcode) {
        return findByCdepcode(getOrgId(), cdepcode);
    }

    public List<Department> getTreeTableDatas(Kv kv) {
        List<Department> datas = daoTemplate("department.list",kv).find();
        return convertToModelTree(datas, "iautoid", "ipid", (p) -> notOk(p.getIPid()));
    }

    /**
     * 根据部门编码查询部门名称 
     */
	public String getCdepName(String cdepcode) {
		Department department = findFirst(selectSql().eq("cdepcode", cdepcode).eq("isdeleted", IsEnableEnum.NO.getValue()).eq("iorgid", getOrgId()));
		if(department != null) return department.getCDepName();
		return null;
	}

	public String getCdepCodeByName(String cdepname) {
		Department department = findFirst(selectSql().eq("cdepname", cdepname).eq("isdeleted", IsEnableEnum.NO.getValue()).eq("iorgid", getOrgId()));
		if(department != null) return department.getCDepCode();
		return null;
	}
    
}
