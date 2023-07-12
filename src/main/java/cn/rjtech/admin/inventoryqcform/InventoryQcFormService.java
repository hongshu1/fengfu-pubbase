package cn.rjtech.admin.inventoryqcform;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.StrSplitter;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.jbolt._admin.dictionary.DictionaryService;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.Dictionary;
import cn.jbolt.core.model.JboltFile;
import cn.jbolt.core.model.User;
import cn.jbolt.core.poi.excel.JBoltExcel;
import cn.jbolt.core.poi.excel.JBoltExcelHeader;
import cn.jbolt.core.poi.excel.JBoltExcelSheet;
import cn.jbolt.core.service.JBoltFileService;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.core.util.JBoltArrayUtil;
import cn.jbolt.core.util.JBoltCamelCaseUtil;
import cn.jbolt.core.util.JBoltUploadFileUtil;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.inventory.InventoryService;
import cn.rjtech.admin.inventoryqcformtype.InventoryQcFormTypeService;
import cn.rjtech.admin.qcform.QcFormService;
import cn.rjtech.cache.CusFieldsMappingdCache;
import cn.rjtech.model.momdata.Inventory;
import cn.rjtech.model.momdata.InventoryQcForm;
import cn.rjtech.model.momdata.InventoryQcFormType;
import cn.rjtech.model.momdata.QcForm;
import cn.rjtech.model.momdata.base.BaseInventoryQcForm;
import cn.rjtech.util.ValidationUtils;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 质量建模-检验适用标准
 *
 * @ClassName: InventoryQcFormService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-03 14:20
 */
public class InventoryQcFormService extends BaseService<InventoryQcForm> {
    
    private final InventoryQcForm dao = new InventoryQcForm().dao();

    @Override
    protected InventoryQcForm dao() {
        return dao;
    }

    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

    @Inject
    private QcFormService qcFormService;
    @Inject
    private InventoryService inventoryService;
    @Inject
    private JBoltFileService jboltFileService;
    @Inject
    private DictionaryService dictionaryService;
    @Inject
    private InventoryQcFormTypeService inventoryQcFormTypeService;

    /**
     * 后台管理数据查询
     *
     * @param pageNumber     第几页
     * @param pageSize       每页几条数据
     * @param keywords       关键词
     * @param sortColumn     排序列名
     * @param sortType       排序方式 asc desc
     * @param IsDeleted      删除状态：0. 未删除 1. 已删除
     * @param machineType    机型
     * @param inspectionType 检验类型
     */
    public Page<InventoryQcForm> getAdminDatas(int pageNumber, int pageSize, String keywords, String sortColumn, String sortType, Boolean IsDeleted, String machineType, String inspectionType) {
        //创建sql对象
        Sql sql = selectSql().page(pageNumber, pageSize);
        //sql条件处理
        sql.eqBooleanToChar("IsDeleted", IsDeleted);
        sql.eq("machineType", machineType);
        sql.eq("inspectionType", inspectionType);
        //关键词模糊查询
        sql.likeMulti(keywords, "cOrgName", "cCreateName", "cUpdateName", "iQcFormName", "iInventoryName", "componentName");
        //排序
        sql.orderBy(sortColumn, sortType);
        return paginate(sql);
    }

    /**
     * 列表数据源
     */
    public Page<Record> getAdminDatas(int pageNumber, int pageSize, Kv kv) {
        return dbTemplate("inventoryqcform.pageList", kv).paginate(pageNumber, pageSize);
    }

    /**
     * 保存
     */
    public Ret save(InventoryQcForm inventoryQcForm) {
        if (inventoryQcForm == null || isOk(inventoryQcForm.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //if(existsName(inventoryQcForm.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = inventoryQcForm.save();
        if (success) {
            //添加日志
            //addSaveSystemLog(inventoryQcForm.getIAutoId(), JBoltUserKit.getUserId(), inventoryQcForm.getName());
        }
        return ret(success);
    }

    /**
     * 更新
     */
    public Ret update(InventoryQcForm inventoryQcForm) {
        if (inventoryQcForm == null || notOk(inventoryQcForm.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //更新时需要判断数据存在
        InventoryQcForm dbInventoryQcForm = findById(inventoryQcForm.getIAutoId());
        if (dbInventoryQcForm == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        //if(existsName(inventoryQcForm.getName(), inventoryQcForm.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = inventoryQcForm.update();
        if (success) {
            //添加日志
            //addUpdateSystemLog(inventoryQcForm.getIAutoId(), JBoltUserKit.getUserId(), inventoryQcForm.getName());
        }
        return ret(success);
    }

    /**
     * 删除数据后执行的回调
     *
     * @param inventoryQcForm 要删除的model
     * @param kv              携带额外参数一般用不上
     */
    @Override
    protected String afterDelete(InventoryQcForm inventoryQcForm, Kv kv) {
        //addDeleteSystemLog(inventoryQcForm.getIAutoId(), JBoltUserKit.getUserId(),inventoryQcForm.getName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param inventoryQcForm model
     * @param kv              携带额外参数一般用不上
     */
    @Override
    public String checkInUse(InventoryQcForm inventoryQcForm, Kv kv) {
        //这里用来覆盖 检测是否被其它表引用
        return null;
    }

    /**
     * 生成excel导入使用的模板
     */
    public JBoltExcel getImportExcelTpl() {
        return JBoltExcel
                //创建
                .create()
                .setSheets(
                        JBoltExcelSheet.create()
                                //设置列映射 顺序 标题名称 不处理别名
                                .setHeaders(1, false,
                                        JBoltExcelHeader.create("创建人名称", 15),
                                        JBoltExcelHeader.create("更新人名称", 15),
                                        JBoltExcelHeader.create("检验表格名称", 15),
                                        JBoltExcelHeader.create("机型", 15),
                                        JBoltExcelHeader.create("存货编码", 15),
                                        JBoltExcelHeader.create("存货名称", 15),
                                        JBoltExcelHeader.create("客户部番", 15),
                                        JBoltExcelHeader.create("部品名称", 15),
                                        JBoltExcelHeader.create("规格", 15),
                                        JBoltExcelHeader.create("主计量单位", 15),
                                        JBoltExcelHeader.create("检验类型", 15)
                                )
                );
    }


    /**
     * 从系统导入字段配置，获得导入的数据
     */
    public Ret importExcel(File file) {
        List<Record> records = CusFieldsMappingdCache.ME.getImportRecordsByTableName(file, table());
        if (notOk(records)) {
            return fail(JBoltMsg.DATA_IMPORT_FAIL_EMPTY);
        }

        Date now = new Date();
        
        //存货ID
        Map<String, Inventory> inventoryMap = new HashMap<>();
        //检验表格
        Map<String, QcForm> qcFormMap = new HashMap<>();
        
        for (Record record : records) {
            //存货编码
            String cinvcode = record.getStr("iInventoryId");
            //表格名称
            String iqcformname = record.getStr("iQcFormId");
            //检验类型
            String cTypeNames = record.getStr("cTypeNames");

            Inventory inventory = inventoryMap.get(cinvcode);
            QcForm qcform = qcFormMap.get(iqcformname);
            if (ObjUtil.isNull(inventory)) {
                String[] cinvcodeList = cinvcode.split(",");
                for (String s : cinvcodeList) {
                    inventory = inventoryService.findByiInventoryCode(s);
                    ValidationUtils.notNull(inventory, String.format("存货编码“%s”不存在", inventory));
                    inventoryMap.put(s, inventory);
                }
            }
            if (ObjUtil.isNull(qcform)) {
                qcform = qcFormService.findByiInventoryCode(iqcformname);
                ValidationUtils.notNull(qcform, String.format("检验表格名称“%s”不存在", iqcformname));
                qcFormMap.put(iqcformname, qcform);
            }
            if (StrUtil.isBlank(record.getStr("cTypeNames"))) {
                return fail("检验类型不能为空");
            }

            // 检验类型
            List<Dictionary> inspectionList = dictionaryService.getListByTypeKey("inspection_type");
            ValidationUtils.notEmpty(inspectionList, "检验类型【inspection_type】字典未配置");
            String[] typeNames = cTypeNames.split(",");
            // 记录名称类型
            StringBuilder cTypeSN = new StringBuilder();
            Map<String, String> inspectionMap = new HashMap<>();
            for (Dictionary dictionary : inspectionList){
                String name = dictionary.getName();
                for (String typeName :typeNames){
                    if (typeName.equals(name)){
                        cTypeSN.append(dictionary.getSn()).append(",");
                        inspectionMap.put(name, dictionary.getSn());
                    }
                }
            }
            if (StrUtil.isNotBlank(cTypeSN.toString())){
                cTypeSN = new StringBuilder(cTypeSN.substring(0, cTypeSN.length() - 1));
            }
            String newTypeSn = cTypeSN.toString();

            record.set("iAutoId", JBoltSnowflakeKit.me.nextId());
            record.set("iInventoryId", inventory.getIAutoId());
            record.set("iQcFormId", qcform.getIAutoId());
            record.set("cTypeIds",newTypeSn);
            record.set("cTypeNames", cTypeNames);
            record.set("iCreateBy", JBoltUserKit.getUserId());
            record.set("dCreateTime", now);
            record.set("cCreateName", JBoltUserKit.getUserName());
            record.set("iOrgId", getOrgId());
            record.set("cOrgCode", getOrgCode());
            record.set("cOrgName", getOrgName());
            record.set("iUpdateBy", JBoltUserKit.getUserId());
            record.set("dUpdateTime", now);
            record.set("cUpdateName", JBoltUserKit.getUserName());
            record.set("isDeleted",0);
        }

        // 执行批量操作
        tx(() -> {
            batchSaveRecords(records);
            return true;
        });
        return SUCCESS;
    }

    /**
     * 生成要导出的Excel
     */
    public JBoltExcel exportExcel(List<InventoryQcForm> datas) {
        return JBoltExcel
                //创建
                .create()
                //设置工作表
                .setSheets(
                        //设置工作表 列映射 顺序 标题名称
                        JBoltExcelSheet
                                .create()
                                //表头映射关系
                                .setHeaders(1,
                                        JBoltExcelHeader.create("cDcCode", "设变号", 15),
                                        JBoltExcelHeader.create("cMeasure", "测定理由", 15),
                                        JBoltExcelHeader.create("cCreateName", "创建人名称", 15),
                                        JBoltExcelHeader.create("cUpdateName", "更新人名称", 15)
                                )
                                //设置导出的数据源 来自于数据库查询出来的Model List
                                .setModelDatas(2, datas)
                );
    }

    /**
     * toggle操作执行后的回调处理
     */
    @Override
    protected String afterToggleBoolean(InventoryQcForm inventoryQcForm, String column, Kv kv) {
        //addUpdateSystemLog(inventoryQcForm.getIAutoId(), JBoltUserKit.getUserId(), inventoryQcForm.getName(),"的字段["+column+"]值:"+inventoryQcForm.get(column));
		/*
		switch(column){
		    case "IsDeleted":
		        break;
		    case "iQcFormName":
		        break;
		    case "machineType":
		        break;
		    case "iInventoryCode":
		        break;
		    case "iInventoryName":
		        break;
		    case "CustomerManager":
		        break;
		    case "componentName":
		        break;
		    case "specs":
		        break;
		    case "unit":
		        break;
		    case "inspectionType":
		        break;
		}
		*/
        return null;
    }

    /**
     * 获取存货数据
     */
    public Page<Record> resourceList(Kv kv, int pageNum, int pageSize) {
        Page<Record> recordPage = dbTemplate("inventoryqcform.resourceList", kv).paginate(pageNum, pageSize);
        JBoltCamelCaseUtil.keyToCamelCase(recordPage);
        return recordPage;
    }

    /**
     * 可编辑表格提交
     *
     * @param jBoltTable 编辑表格提交内容
     */
    public Ret submitByJBoltTable(JBoltTable jBoltTable) {
        //当前操作人员  当前时间
        User user = JBoltUserKit.getUser();
        Date nowDate = new Date();
        Long orgId = getOrgId();
        String orgCode = getOrgCode();
        String orgName = getOrgName();

        tx(() -> {
            JSONObject formBean = jBoltTable.getForm();
            String iQcFormId = formBean.getString("inventoryQcForm.iQcFormId");
            String iQcFormName = formBean.getString("inventoryQcForm.iQcFormName");
            // Id值
            String cTypeSN = formBean.getString("cTypeNames");
            ValidationUtils.notBlank(cTypeSN, "检验类型不能为空");
            // 检验类型
            List<Dictionary> inspectionList = dictionaryService.getListByTypeKey("inspection_type");
            ValidationUtils.notEmpty(inspectionList, "检验类型【inspection_type】字典未配置");
            String[] typeNames = cTypeSN.split(",");
            // 记录名称类型
            StringBuilder typeNameStr = new StringBuilder();
            Map<String, String> inspectionMap = new HashMap<>();
            for (Dictionary dictionary : inspectionList){
                String sn = dictionary.getSn();
                for (String typeName :typeNames){
                    if (typeName.equals(sn)){
                        typeNameStr.append(dictionary.getName()).append(",");
                        inspectionMap.put(sn, dictionary.getName());
                    }
                }
            }
            if (StrUtil.isNotBlank(typeNameStr.toString())){
                typeNameStr = new StringBuilder(typeNameStr.substring(0, typeNameStr.length() - 1));
            }
            String newTypeNameStr = typeNameStr.toString();
            if (jBoltTable.saveIsNotBlank()) {
                List<InventoryQcForm> saveModelList = jBoltTable.getSaveModelList(InventoryQcForm.class);
                List<InventoryQcForm> saveList = new ArrayList<>();
                saveModelList.forEach(inventoryQcForm -> {
                    inventoryQcForm.setIQcFormId(Long.parseLong(iQcFormId));
                    inventoryQcForm.setICreateBy(user.getId());
                    inventoryQcForm.setCCreateName(user.getName());
                    inventoryQcForm.setDCreateTime(nowDate);
                    inventoryQcForm.setIUpdateBy(user.getId());
                    inventoryQcForm.setCUpdateName(user.getName());
                    inventoryQcForm.setDUpdateTime(nowDate);
                    inventoryQcForm.setIOrgId(orgId);
                    inventoryQcForm.setCOrgCode(orgCode);
                    inventoryQcForm.setCOrgName(orgName);
                    inventoryQcForm.setCTypeIds(cTypeSN);
                    inventoryQcForm.setCTypeNames(newTypeNameStr);
                    saveList.add(inventoryQcForm);
                });
                batchSave(saveList);
                List<InventoryQcFormType> qcFormTypeList = inventoryQcFormTypeService.getQcFormTypeList(saveList, inspectionMap);
                inventoryQcFormTypeService.batchSave(qcFormTypeList);
            }

            if (jBoltTable.updateIsNotBlank()) {
                List<InventoryQcForm> updateModelList = jBoltTable.getUpdateModelList(InventoryQcForm.class);
                List<InventoryQcForm> updateList = new ArrayList<>();
                updateModelList.forEach(inventoryQcForm -> {
                    inventoryQcForm.setIQcFormId(Long.parseLong(iQcFormId));
                    inventoryQcForm.setIUpdateBy(user.getId());
                    inventoryQcForm.setCUpdateName(user.getName());
                    inventoryQcForm.setDUpdateTime(nowDate);
                    inventoryQcForm.setCTypeIds(cTypeSN);
                    inventoryQcForm.setCTypeNames(newTypeNameStr);
                    updateList.add(inventoryQcForm);
                });
                batchUpdate(updateList);
                List<Long> ids = updateList.stream().map(BaseInventoryQcForm::getIAutoId).collect(Collectors.toList());
                // 先删除后添加
                inventoryQcFormTypeService.removeByInventoryQcFormId(ids);
                List<InventoryQcFormType> qcFormTypeList = inventoryQcFormTypeService.getQcFormTypeList(updateList, inspectionMap);
                inventoryQcFormTypeService.batchSave(qcFormTypeList);
            }

            if (jBoltTable.deleteIsNotBlank()) {
                deleteByIds(jBoltTable.getDelete());
                inventoryQcFormTypeService.removeByInventoryQcFormId(CollUtil.toList(jBoltTable.getDelete()));
            }
            return true;
        });
        return SUCCESS;
    }

    /**
     * 检验表格数据源
     */
    public List<Record> getFormList(Kv kv) {
        return dbTemplate("inventoryqcform.getFormList", kv).find();
    }

    /**
     * 行数据源
     */
    public List<Record> listData(Kv kv) {
        List<Record> records = dbTemplate("inventoryqcform.listData", kv).find();
        records.forEach(record -> {
            String inspectiontype = record.getStr("inspectiontype");
            if (StrKit.notBlank(inspectiontype)) {
                List<Record> dic = new ArrayList<>();
                JBoltArrayUtil.listFrom(inspectiontype, ",").forEach(sn -> {
                    Record dicBySn = findDicBySn(sn);
                    dic.add(dicBySn);
                });
                String inspectionName = dic.stream().map(m -> m.getStr("name")).collect(Collectors.joining(","));
                record.set("inspectionname", inspectionName);
            }
        });
        return records;
    }

    public Record findDicBySn(Object sn) {
        return dbTemplate("inventoryqcform.findDicBySn", Kv.by("sn", sn)).findFirst();
    }

    /**
     * 保存文件 并 保存到行数据里
     */
    public Ret saveFileAndUpdateLine(List<UploadFile> files, String uploadPath) {
        Ret ret = new Ret();
        tx(() -> {
            List<JboltFile> retFiles = new ArrayList<>();
            
            JboltFile jboltFile;
            StringBuilder errorMsg = new StringBuilder();
            for (UploadFile uploadFile : files) {
                jboltFile = jboltFileService.saveJBoltFile(uploadFile, uploadPath, JboltFile.FILE_TYPE_ATTACHMENT);
                if (jboltFile != null) {
                    retFiles.add(jboltFile);
                } else {
                    errorMsg.append(uploadFile.getOriginalFileName()).append("上传失败;");
                }
            }
            if (retFiles.size() == 0) {
                ret.setFail().set("msg", errorMsg.toString());
                return false;
            }
//            if (ObjUtil.isNotNull(lineId)){
//                // 保存文件 获取fileId
//                Long fileId = jboltFile.getId();
//                InventoryQcForm inventoryQcForm = findById(lineId);
//                String cPics = inventoryQcForm.getCPics();
//                if (cPics != null) {
//                    cPics = cPics.concat(",").concat(Long.toString(fileId));
//                } else {
//                    cPics = Long.toString(fileId);
//                }
//                inventoryQcForm.setCPics(cPics);
//                inventoryQcForm.update();
//            }
            ret.setOk().set("data", retFiles);
            return true;
        });

        return ret;
    }

    /**
     * 删除文件信息
     */
    public Ret deleteFile(Kv kv) {
        boolean result = tx(() -> {
            Long lineId = kv.getLong("lineId");
            Long fileId = kv.getLong("fileId");
            JboltFile jboltFile = jboltFileService.findById(fileId);
            if (notOk(jboltFile)) {
                return false;
            }
            Ret ret = jboltFileService.deleteById(fileId);
            boolean delete = JBoltUploadFileUtil.delete(jboltFile.getLocalPath());
            if (ret.isOk() && delete) {
                InventoryQcForm inventoryQcForm = findById(lineId);
                String cPics = inventoryQcForm.getCPics();
                StringBuilder warranty = new StringBuilder();
                if (cPics != null) {
                    String[] split = cPics.split(",");
                    for (String s : split) {
                        if (!s.equals(fileId.toString())) {
                            if (warranty.length() > 0) {
                                warranty.append(",").append(s);
                            } else {
                                warranty.append(s);
                            }
                        }
                    }
                    inventoryQcForm.setCPics(warranty.length() > 0 ? warranty.toString() : null);
                    return inventoryQcForm.update();
                }
            }
            return true;
        });
        if (result) {
            return SUCCESS;
        }
        return FAIL;
    }

    public Record findByQcFormId(Long id){
        return dbTemplate("inventoryqcform.pageList", Okv.by("iQcFormId", id)).findFirst();
    }

    public InventoryQcForm findByIInventoryId(Long iinventoryId){
        return findFirst("SELECT * FROM Bd_InventoryQcForm where iInventoryId = ? order by dUpdateTime desc",iinventoryId);
    }

    /**
     * 从系统导入字段配置，获得导入的数据
     */
    public Ret importExcelClass(File file) {
        List<Record> records = CusFieldsMappingdCache.ME.getImportRecordsByTableName(file, table());
        if (notOk(records)) {
            return fail(JBoltMsg.DATA_IMPORT_FAIL_EMPTY);
        }

        Date now=new Date();
        
        for (Record record : records) {

            if (StrUtil.isBlank(record.getStr("iQcFormId"))) {
                return fail("检验表格名称不能为空");
            }
            if (StrUtil.isBlank(record.getStr("cTypeNames"))) {
                return fail("检验类型不能为空");
            }
            if (StrUtil.isBlank(record.getStr("iInventoryId"))) {
                return fail("存货编码不能为空");
            }

            String cTypeNames = record.getStr("cTypeNames");
            String iQcFormId = record.getStr("iQcFormId");
            QcForm qcForm = qcFormService.getBycQcFormName(iQcFormId);
            if (ObjUtil.isNull(qcForm)){
                ValidationUtils.error("检验表格名称:"+iQcFormId+",不存在");
            }
            record.set("iQcFormId",qcForm.getIAutoId());

            String iInventoryId = record.getStr("iInventoryId");
            Inventory inventory = inventoryService.findBycInvCode(iInventoryId);
            if (ObjUtil.isNull(inventory)){
                ValidationUtils.error("存货编码名称:"+iInventoryId+",不存在");
            }

            record.set("iInventoryId",inventory.getIAutoId());

            List<Dictionary> dictionaryList = dictionaryService.getOptionListByTypeKey("inspection_type", true);
            StringBuilder Strsn = new StringBuilder();
            for (Dictionary dictionary : dictionaryList) {
                for (String cname : StrSplitter.split(cTypeNames, ",", true, true)) {
                    String name = dictionary.getName();
                    if (name.equals(cname)) {
                        Strsn.append(dictionary.getSn()).append(",");
                    }

                }
            }
            if (Strsn.length() > 0) {
                Strsn.deleteCharAt(Strsn.lastIndexOf(","));
            }

            record.set("iAutoId", JBoltSnowflakeKit.me.nextId());
            record.set("iOrgId", getOrgId());
            record.set("cOrgCode", getOrgCode());
            record.set("cOrgName", getOrgName());
            record.set("iCreateBy", JBoltUserKit.getUserId());
            record.set("dCreateTime", now);
            record.set("cCreateName", JBoltUserKit.getUserName());
            record.set("isDeleted",0);
            record.set("iUpdateBy", JBoltUserKit.getUserId());
            record.set("dUpdateTime", now);
            record.set("cUpdateName", JBoltUserKit.getUserName());
            record.set("cTypeIds",Strsn.toString());
        }

        // 执行批量操作
        tx(() -> {
            batchSaveRecords(records);
            return true;
        });
        return SUCCESS;
    }

}
