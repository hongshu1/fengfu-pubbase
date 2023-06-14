package cn.rjtech.admin.inventoryspotcheckform;

import cn.hutool.core.date.DateUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.cache.JBoltUserCache;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.poi.excel.*;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.equipmentmodel.EquipmentModelService;
import cn.rjtech.admin.inventory.InventoryService;
import cn.rjtech.admin.inventoryspotcheckformOperation.InventoryspotcheckformOperationService;
import cn.rjtech.admin.operation.OperationService;
import cn.rjtech.model.momdata.Inventory;
import cn.rjtech.model.momdata.InventorySpotCheckForm;
import cn.rjtech.model.momdata.InventoryspotcheckformOperation;
import cn.rjtech.model.momdata.Operation;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 质量建模-点检适用标准
 *
 * @ClassName: InventorySpotCheckFormService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-29 17:27
 */
public class InventorySpotCheckFormService extends BaseService<InventorySpotCheckForm> {

    private final InventorySpotCheckForm dao = new InventorySpotCheckForm().dao();

    @Inject
    private InventoryService                       inventoryService;        //存货档案
    @Inject
    private OperationService                       operationService;        //工序
    @Inject
    private EquipmentModelService                  equipmentModelService; //机型档案
    @Inject
    private InventoryspotcheckformOperationService checkformOperationService; //质量建模-存货点检工序


    @Override
    protected InventorySpotCheckForm dao() {
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
     * @param keywords   关键词
     * @param iType      点检业务： 1. 始业点检表 2. 首末点检表 3. 重点工序点检表 4. 初物巡检表
     * @param IsDeleted  删除状态：0. 未删除 1. 已删除
     */
    public Page<InventorySpotCheckForm> getAdminDatas(int pageNumber, int pageSize, String keywords, Integer iType,
                                                      Boolean IsDeleted) {
        //创建sql对象
        Sql sql = selectSql().page(pageNumber, pageSize);
        //sql条件处理
        sql.eq("iType", iType);
        sql.eqBooleanToChar("IsDeleted", IsDeleted);
        //关键词模糊查询
        sql.likeMulti(keywords, "cOrgName", "cCreateName", "cUpdateName");
        //排序
        sql.desc("iAutoId");
        return paginate(sql);
    }

    public Page<Record> pageList(Kv kv) {
        Page<Record> recordPage = dbTemplate("inventoryspotcheckform.list", kv)
            .paginate(kv.getInt("page"), kv.getInt("pageSize"));
        for (Record record : recordPage.getList()) {
            List<InventoryspotcheckformOperation> spotCheckFormOperationList = checkformOperationService
                .listByIinventorySpotCheckFormId(record.get("iautoid"));
            String coperationname = "";
            for (InventoryspotcheckformOperation checkformOperation : spotCheckFormOperationList) {
                Operation operation = operationService.findById(checkformOperation.getIOperationId());
                coperationname += operation.getCoperationname() + ",";
            }
            //去除最后一个逗号
            if (coperationname.endsWith(",")) {
                coperationname = coperationname.substring(0, coperationname.length() - 1);
            }
            List<String> collect = spotCheckFormOperationList
                .stream()
                .map(e -> String.valueOf(e.getIOperationId()))
                .collect(Collectors.toList());
            record.set("ioperationid", String.join(",", collect));
            record.set("iinventoryspotcheckformid", spotCheckFormOperationList.get(0).getIInventorySpotCheckFormId());
            record.set("coperationname", coperationname);
        }
        return recordPage;
    }

    /**
     * 保存
     */
    public Ret save(InventorySpotCheckForm inventorySpotCheckForm) {
        if (inventorySpotCheckForm == null || isOk(inventorySpotCheckForm.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        boolean success = inventorySpotCheckForm.save();
        return ret(success);
    }

    /**
     * 更新
     */
    public Ret update(InventorySpotCheckForm inventorySpotCheckForm) {
        if (inventorySpotCheckForm == null || notOk(inventorySpotCheckForm.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //更新时需要判断数据存在
        InventorySpotCheckForm dbInventorySpotCheckForm = findById(inventorySpotCheckForm.getIAutoId());
        if (dbInventorySpotCheckForm == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        boolean success = inventorySpotCheckForm.update();
        return ret(success);
    }

    /**
     * 删除数据后执行的回调
     *
     * @param inventorySpotCheckForm 要删除的model
     * @param kv                     携带额外参数一般用不上
     */
    @Override
    protected String afterDelete(InventorySpotCheckForm inventorySpotCheckForm, Kv kv) {
        //addDeleteSystemLog(inventorySpotCheckForm.getIAutoId(), JBoltUserKit.getUserId(),inventorySpotCheckForm.getName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param inventorySpotCheckForm model
     * @param kv                     携带额外参数一般用不上
     */
    @Override
    public String checkInUse(InventorySpotCheckForm inventorySpotCheckForm, Kv kv) {
        //这里用来覆盖 检测是否被其它表引用
        return null;
    }

    /**
     * toggle操作执行后的回调处理
     */
    @Override
    protected String afterToggleBoolean(InventorySpotCheckForm inventorySpotCheckForm, String column, Kv kv) {
        //addUpdateSystemLog(inventorySpotCheckForm.getIAutoId(), JBoltUserKit.getUserId(), inventorySpotCheckForm.getName(),"的字段["+column+"]值:"+inventorySpotCheckForm.get(column));
        /**
         switch(column){
         case "IsDeleted":
         break;
         }
         */
        return null;
    }

    public Ret updateTable(JBoltTable jBoltTable) {
        if (jBoltTable == null || jBoltTable.isBlank()) {
            return fail(JBoltMsg.JBOLTTABLE_IS_BLANK);
        }
        Date now = new Date();
        InventorySpotCheckForm spotCheckForm = jBoltTable.getFormModel(InventorySpotCheckForm.class);
        tx(() -> {
            //1、保存
            ArrayList<InventorySpotCheckForm> checkFormList = new ArrayList<>();//点检适用标准表
            ArrayList<InventoryspotcheckformOperation> operationList = new ArrayList<>();
            if (jBoltTable.saveIsNotBlank()) {
                List<Record> saveRecordList = jBoltTable.getSaveRecordList();
                for (Record saveRecord : saveRecordList) {
                    InventorySpotCheckForm checkForm = new InventorySpotCheckForm();
                    saveInventorySpotCheckForm(checkForm, saveRecord.getLong("iautoid"), now, spotCheckForm);
                    checkFormList.add(checkForm);

                    String[] split = saveRecord.getStr("ioperationid").split(",");
                    for (String ioperationid : split) {
                        InventoryspotcheckformOperation operation = new InventoryspotcheckformOperation();
                        saveSpotCheckFormOperationModel(operation, checkForm.getIAutoId(), Long.valueOf(ioperationid));
                        operationList.add(operation);
                    }
                }
                //保存在【Bd_InventorySpotCheckForm】表
                batchSave(checkFormList);
                //保存在【Bd_InventorySpotCheckForm_Operation】表
                checkformOperationService.batchSave(operationList);
            }
            //2、更新
            if (jBoltTable.updateIsNotBlank()) {
                List<Record> updateRecordList = jBoltTable.getUpdateRecordList();
                for (Record updateRecord : updateRecordList) {
                    InventorySpotCheckForm checkForm = findById(updateRecord.getStr("iautoid"));
                    saveModel(checkForm, now, JBoltUserKit.getUserId(), JBoltUserKit.getUserName(), checkForm.getIType(),
                        checkForm.getISpotCheckFormId());
                    update(checkForm);

                    //变更工序
                    List<InventoryspotcheckformOperation> operations = checkformOperationService
                        .listByIinventorySpotCheckFormId(checkForm.getIAutoId());
                    List<String> collect = operations.stream().map(e -> String.valueOf(e.getIAutoId()))
                        .collect(Collectors.toList());
                    checkformOperationService.deleteByIds(String.join(",", collect));
                    String[] split = splitIOperationId(updateRecord.getStr("ioperationid"));
                    ArrayList<InventoryspotcheckformOperation> list = new ArrayList<>();
                    for (String ioperationid : split) {
                        InventoryspotcheckformOperation operation = new InventoryspotcheckformOperation();
                        saveSpotCheckFormOperationModel(operation, checkForm.getIAutoId(), Long.valueOf(ioperationid));
                        list.add(operation);
                    }
                    checkformOperationService.batchSave(list);
                }
            }
            return true;
        });
        return SUCCESS;
    }

    public String[] splitIOperationId(String ioperationid) {
        return ioperationid.split(",");
    }

    public void saveSpotCheckFormOperationModel(InventoryspotcheckformOperation operation, Long iautoid, Long ioperationid) {
        operation.setIAutoId(JBoltSnowflakeKit.me.nextId());
        operation.setIInventorySpotCheckFormId(iautoid);
        operation.setIOperationId(Long.valueOf(ioperationid));
    }

    public Ret updateEditTable(JBoltTable jBoltTable, Long userId, Date now, Kv kv) {
        if (jBoltTable == null || jBoltTable.isBlank()) {
            return fail(JBoltMsg.JBOLTTABLE_IS_BLANK);
        }
        //1、保存
        if (jBoltTable.saveIsNotBlank()) {
            //头表：bd_inventorySpotCheckForm
            InventorySpotCheckForm spotCheckForm = jBoltTable.getFormModel(InventorySpotCheckForm.class);
            //表单：从bd_inventory表获取数据
            List<Inventory> saveModelList = jBoltTable.getSaveModelList(Inventory.class);
            String userName = JBoltUserKit.getUserName();
            List<InventoryspotcheckformOperation> operationList = getcheckformOperation(jBoltTable);//存货点检工序表
            ArrayList<InventorySpotCheckForm> checkFormList = new ArrayList<>();//点检适用标准表
            ArrayList<InventoryspotcheckformOperation> formOperationList = new ArrayList<>();
            for (Inventory inventory : saveModelList) {
                InventorySpotCheckForm checkForm = new InventorySpotCheckForm();
//                saveInventorySpotCheckForm(checkForm, inventory.getIAutoId(), userId, now, userName, spotCheckForm.getIType(),
//                    spotCheckForm.getISpotCheckFormId());
                checkFormList.add(checkForm);
                //添加到【质量建模-存货点检工序】表
                InventoryspotcheckformOperation operation = operationList.stream()
                    .filter(e -> e.getIAutoId().equals(inventory.getIAutoId())).findFirst()
                    .orElse(new InventoryspotcheckformOperation());
                InventoryspotcheckformOperation formOperation = new InventoryspotcheckformOperation();
                formOperation.setIAutoId(JBoltSnowflakeKit.me.nextId());
                formOperation.setIInventorySpotCheckFormId(checkForm.getIAutoId());
                formOperation.setIOperationId(operation.getIOperationId());
                formOperationList.add(formOperation);
            }
            //保存在【Bd_InventorySpotCheckForm】表
            // batchSave(checkFormList);
            //保存在【Bd_InventorySpotCheckForm_Operation】表
            //checkformOperationService.batchSave(formOperationList);
        }
        //2、更新
        if (jBoltTable.updateIsNotBlank()) {
            //获取表头
            InventorySpotCheckForm spotCheckForm = jBoltTable.getFormBean(InventorySpotCheckForm.class);
            List<InventorySpotCheckForm> updateModelList = jBoltTable.getUpdateModelList(InventorySpotCheckForm.class);
            List<InventoryspotcheckformOperation> operationList = getcheckformOperation(jBoltTable);//存货点检工序表
            List<InventoryspotcheckformOperation> formOperationList = new ArrayList<>();
            for (InventorySpotCheckForm checkForm : updateModelList) {
                checkForm.setIType(spotCheckForm.getIType());
                checkForm.setISpotCheckFormId(spotCheckForm.getISpotCheckFormId());
                checkForm.setDUpdateTime(now);
                checkForm.setIUpdateBy(userId);
                checkForm.setCUpdateName(JBoltUserKit.getUserName());
                //查询[质量建模-存货点检工序]
                List<InventoryspotcheckformOperation> operations = checkformOperationService
                    .listByIinventorySpotCheckFormId(checkForm.getIAutoId());
                InventoryspotcheckformOperation operation = operationList.stream()
                    .filter(e -> e.getIInventorySpotCheckFormId().equals(checkForm.getIAutoId())).findFirst()
                    .orElse(new InventoryspotcheckformOperation());
                for (InventoryspotcheckformOperation op : operations) {
                    op.setIOperationId(operation.getIOperationId());//更新工序主键id
                }
                formOperationList.addAll(operations);
            }
            //更新【Bd_InventorySpotCheckForm】表
            //batchUpdate(updateModelList);
            //更新【Bd_InventorySpotCheckForm_Operation】表
            //checkformOperationService.batchUpdate(formOperationList);
        }
        return SUCCESS;
    }

    /**
     * 获取存货点检工序表
     */
    public List<InventoryspotcheckformOperation> getcheckformOperation(JBoltTable jBoltTable) {
        if (jBoltTable.saveIsNotBlank()) {
            return jBoltTable.getSaveModelList(InventoryspotcheckformOperation.class);
        }
        return jBoltTable.getUpdateModelList(InventoryspotcheckformOperation.class);
    }

    public void saveInventorySpotCheckForm(InventorySpotCheckForm checkForm, Long inventoryiautoid,
                                           Date now, InventorySpotCheckForm spotCheckForm) {
        Long userId = JBoltUserKit.getUserId();
        String userName = JBoltUserKit.getUserName();
        checkForm.setIAutoId(JBoltSnowflakeKit.me.nextId());
        checkForm.setICreateBy(userId);
        checkForm.setDCreateTime(now);
        checkForm.setCCreateName(userName);
        checkForm.setIOrgId(getOrgId());
        checkForm.setCOrgCode(getOrgCode());
        checkForm.setCOrgName(getOrgName());
        checkForm.setIsDeleted(false);
        checkForm.setIInventoryId(inventoryiautoid);//iInventoryId 存货ID
        //
        saveModel(checkForm, now, userId, userName, spotCheckForm.getIType(), spotCheckForm.getISpotCheckFormId());
    }

    public void saveModel(InventorySpotCheckForm checkForm, Date now, Long userId,
                          String userName, int iType, Long iSpotCheckFormId) {
        checkForm.setDUpdateTime(now);
        checkForm.setIUpdateBy(userId);
        checkForm.setCUpdateName(userName);
        checkForm.setIType(iType);//点检业务
        checkForm.setISpotCheckFormId(iSpotCheckFormId);//iSpotCheckFormId 点检表格ID
    }

    public List<Record> list(Kv kv) {
        return dbTemplate("inventoryspotcheckform.exportList", kv).find();
    }

    /**
     * 导出excel文件
     */
    public JBoltExcel exportExcelTpl(List<InventorySpotCheckForm> datas) {
        //2、创建JBoltExcel
        //3、返回生成的excel文件
        return JBoltExcel
            .create()
            .addSheet(//设置sheet
                JBoltExcelSheet.create("供应商分类")//创建sheet name保持与模板中的sheet一致
                    .setHeaders(//sheet里添加表头
                        JBoltExcelHeader.create("fathercvccode", "父级供应商分类编码", 25),
                        JBoltExcelHeader.create("fathercvcname", "父级供应商分类名称", 25),
                        JBoltExcelHeader.create("childcvccoded", "供应商分类编码", 25),
                        JBoltExcelHeader.create("childcvcnamed", "供应商分类名称", 25)
                    )
                    .setDataChangeHandler((data, index) -> {//设置数据转换处理器
                        //将user_id转为user_name
                        data.changeWithKey("user_id", "user_username", JBoltUserCache.me.getUserName(data.getLong("user_id")));
                        data.changeBooleanToStr("is_enabled", "是", "否");
                    })
                    .setModelDatas(2, datas)//设置数据
            )
            .setFileName("供应商分类" + "_" + DateUtil.today());
    }

    /**
     * 生成Excel 导入模板的数据 byte[]
     */
    public JBoltExcel getExcelImportTpl() {
        return JBoltExcel
            //创建
            .create()
            .setSheets(
                JBoltExcelSheet.create("供应商分类")
                    //设置列映射 顺序 标题名称 不处理别名
                    .setHeaders(2, false,
                        JBoltExcelHeader.create("父级供应商分类编码", 25),
//                        JBoltExcelHeader.create("父级供应商分类名称", 25),
                        JBoltExcelHeader.create("供应商分类编码", 25),
                        JBoltExcelHeader.create("供应商分类名称", 25)
                    )
                    .setMerges(JBoltExcelMerge.create("A", "C", 1, 1, "供应商分类"))
            );
    }

    /**
     * 供应商分类excel导入数据库
     */
    public Ret importExcelData(File file) {
        StringBuilder errorMsg = new StringBuilder();
        JBoltExcel jBoltExcel = JBoltExcel
            //从excel文件创建JBoltExcel实例
            .from(file)
            //设置工作表信息
            .setSheets(
                JBoltExcelSheet.create("供应商分类")
                    //设置列映射 顺序 标题名称
                    .setHeaders(
                        JBoltExcelHeader.create("ipid", "父级供应商分类编码"),
                        JBoltExcelHeader.create("cvccode", "供应商分类编码"),
                        JBoltExcelHeader.create("cvcname", "供应商分类名称")
                    )
                    //特殊数据转换器
                    .setDataChangeHandler((data, index) -> {
                        //非空判断
                        ValidationUtils.notNull(data.get("ipid"), "父级供应商分类编码为空！");
                        ValidationUtils.notNull(data.get("cvccode"), "供应商分类编码为空！");
                        ValidationUtils.notNull(data.get("cvcname"), "供应商分类名称为空！");
                        //子级供应商编码不能重复
                       /* ValidationUtils.isTrue(StringUtils.isBlank(findCVCCodeInfo(data.getStr("cvccode"))),
                            data.getStr("cvccode")+" 供应商分类编码重复");*/
                        /*if (!data.getStr("ipid").equals("父级供应商分类编码")){
                            VendorClass vendorClass = findFirst(Okv.by("cVCCode", data.getStr("ipid")), "iautoid", "desc");
                            ValidationUtils.isTrue(null != vendorClass, data.getStr("ipid")+" 父级供应商分类不存在，请手动添加！");
                        }
                        VendorClass vendorClass = findFirst(Okv.by("cVCCode", data.getStr("ipid")), "iautoid", "desc");
                        if (null != vendorClass){
                            data.change("ipid",vendorClass.getIAutoId());
                        }
                        data.change("icreateby", JBoltUserKit.getUserId());
                        data.change("ccreatename", JBoltUserKit.getUserName());
                        data.change("cupdatename",JBoltUserKit.getUserName());
                        data.change("iupdateby",JBoltUserKit.getUserId());*/
                    })
                    //从第2行开始读取
                    .setDataStartRow(2)
            );
        //从指定的sheet工作表里读取数据
        List<InventorySpotCheckForm> models = JBoltExcelUtil.readModels(jBoltExcel, "点检适用标准", InventorySpotCheckForm.class,
            errorMsg);
        if (notOk(models)) {
            if (errorMsg.length() > 0) {
                return fail(errorMsg.toString());
            } else {
                return fail(JBoltMsg.DATA_IMPORT_FAIL_EMPTY);
            }
        }
        //读取数据没有问题后判断必填字段
        if (models.size() > 0) {
            Date now = new Date();
            /*for (VendorClass vendorClass : models) {
                vendorClass.setIAutoId(JBoltSnowflakeKit.me.nextId());
                vendorClass.setDCreateTime(now);
                vendorClass.setDUpdateTime(date);
                vendorClass.setIOrgId(getOrgId());
                vendorClass.setCOrgCode(getOrgCode());
                vendorClass.setCOrgName(getOrgName());
                vendorClass.setIsDeleted(false);
                vendorClass.setISource(SourceEnum.MES.getValue());
            }*/
        }

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


}