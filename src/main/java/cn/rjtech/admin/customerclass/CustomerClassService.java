package cn.rjtech.admin.customerclass;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.bean.JsTreeBean;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.poi.excel.JBoltExcel;
import cn.jbolt.core.poi.excel.JBoltExcelHeader;
import cn.jbolt.core.poi.excel.JBoltExcelSheet;
import cn.jbolt.core.poi.excel.JBoltExcelUtil;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.cache.CusFieldsMappingdCache;
import cn.rjtech.model.momdata.CustomerClass;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 往来单位-客户分类
 *
 * @ClassName: CustomerClassService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-21 11:01
 */
public class CustomerClassService extends BaseService<CustomerClass> {
    private final CustomerClass dao = new CustomerClass().dao();

    @Override
    protected CustomerClass dao() {
        return dao;
    }

    /**
     * 后台管理分页查询
     */
    public Page<CustomerClass> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
        return paginateByKeywords("iAutoId", "DESC", pageNumber, pageSize, keywords, "iAutoId");
    }

    /**
     * 保存
     */
    public Ret save(CustomerClass customerclass) {
        if (customerclass == null || isOk(customerclass.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //查询编码是否存在
        ValidationUtils.isTrue(findFirst(Okv.by("cCCCode", customerclass.getCCCCode()), "iautoid", "asc") == null, "编码重复");
        //if(existsName(customerclass.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        customerclass.setICreateBy(JBoltUserKit.getUserId());
        customerclass.setCCreateName(JBoltUserKit.getUserName());
        customerclass.setDCreateTime(new Date());
        customerclass.setCOrgCode(getOrgCode());
        customerclass.setCOrgName(getOrgName());
        customerclass.setIOrgId(getOrgId());
        customerclass.setIUpdateBy(JBoltUserKit.getUserId());
        customerclass.setCUpdateName(JBoltUserKit.getUserName());
        customerclass.setDUpdateTime(new Date());

        boolean success = customerclass.save();
        if (success) {
            //添加日志
            //addSaveSystemLog(customerclass.getIautoid(), JBoltUserKit.getUserId(), customerclass.getName());
        }
        return ret(success);
    }

    /**
     * 更新
     */
    public Ret update(CustomerClass customerclass) {
        if (customerclass == null || notOk(customerclass.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //更新时需要判断数据存在
        CustomerClass dbCustomerclass = findById(customerclass.getIAutoId());
        if (dbCustomerclass == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        ValidationUtils.isTrue(findFirst(Okv.by("cCCCode = ", customerclass.getCCCCode()).set("iAutoId != ",
                customerclass.getIAutoId()).set("isDeleted = ", "1"), "iautoid", "asc", true) == null, "编码重复");

        customerclass.setIUpdateBy(JBoltUserKit.getUserId());
        customerclass.setCUpdateName(JBoltUserKit.getUserName());
        customerclass.setDUpdateTime(new Date());
        //if(existsName(customerclass.getName(), customerclass.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = customerclass.update();
        if (success) {
            //添加日志
            //addUpdateSystemLog(customerclass.getIautoid(), JBoltUserKit.getUserId(), customerclass.getName());
        }
        return ret(success);
    }

    /**
     * 删除 指定多个ID
     */
    public Ret deleteByBatchIds(String ids) {
        tx(() -> {
            CustomerClass customerclass = findById(ids);
            ValidationUtils.notNull(customerclass, JBoltMsg.DATA_NOT_EXIST);
            customerclass.setIsDeleted(true);
            customerclass.update();
            return true;
        });
        return SUCCESS;
        //return deleteByIds(ids,true);
    }

    /**
     * 删除数据后执行的回调
     *
     * @param customerclass 要删除的model
     * @param kv            携带额外参数一般用不上
     */
    @Override
    protected String afterDelete(CustomerClass customerclass, Kv kv) {
        //addDeleteSystemLog(customerclass.getIautoid(), JBoltUserKit.getUserId(),customerclass.getName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param customerclass 要删除的model
     * @param kv            携带额外参数一般用不上
     */
    @Override
    public String checkCanDelete(CustomerClass customerclass, Kv kv) {
        //如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
        return checkInUse(customerclass, kv);
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
     * @param customerclass 要toggle的model
     * @param column        操作的哪一列
     * @param kv            携带额外参数一般用不上
     */
    @Override
    public String checkCanToggle(CustomerClass customerclass, String column, Kv kv) {
        //没有问题就返回null  有问题就返回提示string 字符串
        return null;
    }

    /**
     * toggle操作执行后的回调处理
     */
    @Override
    protected String afterToggleBoolean(CustomerClass customerclass, String column, Kv kv) {
        //addUpdateSystemLog(customerclass.getIautoid(), JBoltUserKit.getUserId(), customerclass.getName(),"的字段["+column+"]值:"+customerclass.get(column));
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param customerclass model
     * @param kv            携带额外参数一般用不上
     */
    @Override
    public String checkInUse(CustomerClass customerclass, Kv kv) {
        //这里用来覆盖 检测Customerclass是否被其它表引用
        return null;
    }

    public List<Record> getDataExport(Kv kv) {
        return dbTemplate("customerclass.getDataExport", kv).find();
    }

    /**
     * 获取分类数据
     *
     * @param isEnabled 启用禁用状态
     */
    public List<CustomerClass> getCustomerclass(Boolean isEnabled, Boolean isDeleteD) {
        Okv kv = Okv.create();
        if (isDeleteD != null) {
            kv.set("isDeleted", false);
        }
        return getCommonList(kv, "iAutoId", "asc");
    }

    /**
     * 获取分类数据中的所有后端分类数据
     */
    public List<CustomerClass> getMgrList() {
        return getCustomerclass(true, true);
    }

    public List<JsTreeBean> getMgrTree(Long checkedId, int openLevel) {
        List<CustomerClass> customerclassList = getMgrList();
        List<JsTreeBean> jsTreeBeanList = new ArrayList<>();
        for (CustomerClass customerclass : customerclassList) {
            Long id = customerclass.getIAutoId();
            Object pid = customerclass.getIPid();
            if (pid == null) {
                pid = "#";
            }
            String text = "[" + customerclass.getCCCCode() + "]" + customerclass.getCCCName();
            String type = customerclass.getCCCCode();
            JsTreeBean jsTreeBean = new JsTreeBean(id, pid, text, type, "", false);
            jsTreeBeanList.add(jsTreeBean);
        }
        return jsTreeBeanList;
    }

    public List<CustomerClass> getTreeDatas(boolean onlyEnable, boolean asOptions) {
        List<CustomerClass> customerclassList = getMgrList();
        return this.convertToModelTree(customerclassList, "iAutoId", "iPid", (p) -> this.notOk(p.getIPid()));
    }

    public Ret importExcelData(File file) {
        StringBuilder errorMsg=new StringBuilder();
        JBoltExcel jBoltExcel=JBoltExcel
                //从excel文件创建JBoltExcel实例
                .from(file)
                //设置工作表信息
                .setSheets(
                        JBoltExcelSheet.create("sheet1")
                                //设置列映射 顺序 标题名称
                                .setHeaders(
                                        JBoltExcelHeader.create("ccccode","编码"),
                                        JBoltExcelHeader.create("cccname","名称"),
                                        JBoltExcelHeader.create("ipid","上级分类")
                                )
                                //特殊数据转换器
                                .setDataChangeHandler((data,index) ->{
                                    CustomerClass customerclass = findFirst(Okv.by("cCCCode", data.get("ipid")),
                                            "iautoid",
                                            "desc");
                                    if(customerclass!=null){
                                        data.change("ipid", customerclass.getIAutoId());
                                    }else{
                                        data.change("ipid", 0);
                                    }

                                    data.change("icreateby", JBoltUserKit.getUserId());
                                    data.change("ccreatename",JBoltUserKit.getUserName());

                                    data.change("iorgid",1);
                                    data.change("corgcode",1);
                                    data.change("corgname", 1L);
                                })
                                //从第三行开始读取
                                .setDataStartRow(3)
                );
        //从指定的sheet工作表里读取数据
        List<CustomerClass> models = JBoltExcelUtil.readModels(jBoltExcel, "sheet1", CustomerClass.class, errorMsg);
        if(notOk(models)) {
            if(errorMsg.length()>0) {
                return fail(errorMsg.toString());
            }else {
                return fail(JBoltMsg.DATA_IMPORT_FAIL_EMPTY);
            }
        }
        //读取数据没有问题后判断必填字段
        if(models.size()>0){
            Date now = new Date();
            for(CustomerClass p:models){
                p.setDCreateTime(now);
                if(notOk(p.getCCCCode())){
                    return fail("编码不能为空");
                }
                if(notOk(p.getCCCName())){
                    return fail("名称不能为空");
                }
                ValidationUtils.isTrue(findFirst(Okv.by("cCCCode", p.getCCCCode()), "iautoid", "asc")==null,
                        p.getCCCCode()+
                        "编码重复");

                p.setIUpdateBy(JBoltUserKit.getUserId());
                p.setCUpdateName(JBoltUserKit.getUserName());
                p.setDUpdateTime(new Date());

            }
        }

        //执行批量操作
        boolean success=tx(() -> {
            batchSave(models);
            return true;
        });

        if(!success) {
            return fail(JBoltMsg.DATA_IMPORT_FAIL);
        }
        return SUCCESS;
    }

    public Ret importExcelData_bak(File file) {
        // 使用字段配置维护
        List<Record> datas = CusFieldsMappingdCache.ME.getImportRecordsByTableName(file, table());
        ValidationUtils.notEmpty(datas, "导入数据局不能为空");

        // 读取数据没有问题后判断必填字段
        Date now = new Date();

        for (Record p : datas) {

            ValidationUtils.notBlank(p.getStr("CCCCode"), "编码不能为空");
            ValidationUtils.notBlank(p.getStr("CCCName"), "名称不能为空");

            ValidationUtils.assertNull(findFirst(Okv.by("cCCCode", p.getStr("CCCCode")), "iautoid", "asc"), p.getStr("CCCCode") + "编码重复");

            CustomerClass customerclass = findFirst(Okv.by("cCCCode", p.getStr("cupdatename")), "iautoid", "desc");
            if (customerclass != null) {
                p.set("ipid", customerclass.getIAutoId());
            } else {
                p.set("ipid", 0L);
            }

            p.set("ICreateBy", JBoltUserKit.getUserId());
            p.set("CCreateName", JBoltUserKit.getUserName());
            p.set("iorgid", getOrgId());
            p.set("COrgCode", getOrgCode());
            p.set("COrgName", getOrgName());
            p.set("iupdateby", JBoltUserKit.getUserId());
            p.set("CUpdateName", JBoltUserKit.getUserName());
            p.set("DUpdateTime", now);
            p.set("DCreateTime", now);
        }

        // 执行批量操作
        tx(() -> {
            batchSaveRecords(datas);
            return true;
        });
        return SUCCESS;
    }

}
