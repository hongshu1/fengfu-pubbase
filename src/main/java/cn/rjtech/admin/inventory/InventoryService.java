package cn.rjtech.admin.inventory;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.text.StrSplitter;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.jbolt._admin.dictionary.DictionaryService;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.Dictionary;
import cn.jbolt.core.poi.excel.JBoltExcel;
import cn.jbolt.core.poi.excel.JBoltExcelHeader;
import cn.jbolt.core.poi.excel.JBoltExcelSheet;
import cn.jbolt.core.poi.excel.JBoltExcelUtil;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.core.ui.jbolttable.JBoltTableMulti;
import cn.jbolt.core.util.JBoltRealUrlUtil;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.bomm.BomMService;
import cn.rjtech.admin.inventoryaddition.InventoryAdditionService;
import cn.rjtech.admin.inventorycapacity.InventoryCapacityService;
import cn.rjtech.admin.inventorymfginfo.InventoryMfgInfoService;
import cn.rjtech.admin.inventoryplan.InventoryPlanService;
import cn.rjtech.admin.inventoryroutingconfig.InventoryRoutingConfigService;
import cn.rjtech.admin.inventoryroutingconfigoperation.InventoryroutingconfigOperationService;
import cn.rjtech.admin.inventoryroutingequipment.InventoryRoutingEquipmentService;
import cn.rjtech.admin.inventoryroutinginvc.InventoryRoutingInvcService;
import cn.rjtech.admin.inventorystockconfig.InventoryStockConfigService;
import cn.rjtech.admin.inventoryworkregion.InventoryWorkRegionService;
import cn.rjtech.admin.invpart.InvPartService;
import cn.rjtech.admin.uom.UomService;
import cn.rjtech.cache.CusFieldsMappingdCache;
import cn.rjtech.enums.*;
import cn.rjtech.model.momdata.*;
import cn.rjtech.util.ValidationUtils;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.core.JFinal;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;

import java.io.File;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

import static cn.hutool.core.text.StrPool.COMMA;

/**
 * 物料建模-存货档案
 *
 * @ClassName: InventoryService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-23 09:35
 */
public class InventoryService extends BaseService<Inventory> {

    private final Inventory dao = new Inventory().dao();

    @Inject
    private UomService uomService;
    @Inject
    private BomMService bomMService;
    @Inject
    private InvPartService invPartService;
    @Inject
    private DictionaryService dictionaryService;
    @Inject
    private InventoryPlanService inventoryPlanService;
    @Inject
    private InventoryMfgInfoService inventoryMfgInfoService;
    @Inject
    private InventoryAdditionService inventoryAdditionService;
    @Inject
    private InventoryCapacityService inventoryCapacityService;
    @Inject
    private InventoryWorkRegionService inventoryWorkRegionService;
    @Inject
    private InventoryStockConfigService inventoryStockConfigService;
    @Inject
    private InventoryRoutingInvcService inventoryRoutingInvcService;
    @Inject
    private InventoryRoutingConfigService inventoryRoutingConfigService;
    @Inject
    private InventoryRoutingEquipmentService inventoryRoutingEquipmentService;
    @Inject
    private InventoryroutingconfigOperationService inventoryroutingconfigOperationService;

    @Override
    protected Inventory dao() {
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
     * @param kv         参数
     */
    public Page<Record> getAdminDatas(int pageNumber, int pageSize, Kv kv) {
        Boolean isEnabled = kv.getBoolean("isEnabled");
        if (isEnabled != null)
            kv.set("isEnabled", isEnabled ? "1" : "0");
        Long id = kv.getLong("iInventoryClassId");
        if (id != null && id == 1L) {
            kv.remove("iInventoryClassId");
        }
        String iInventoryClassCode = kv.getStr("iInventoryClassCode");
        if (StrUtil.isNotBlank(iInventoryClassCode)) {
            if (!iInventoryClassCode.contains("[")) {
                kv.remove("iInventoryClassCode");
            } else {
                kv.set("iInventoryClassCode", iInventoryClassCode.substring(1, iInventoryClassCode.indexOf("]")));
            }
        }
        return dbTemplate("inventoryclass.inventoryList", kv).paginate(pageNumber, pageSize);
    }

    /**
     * 保存
     */
    public Ret save(Inventory inventory) {
        if (inventory == null) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        Inventory first = findFirst(selectSql().eq("cInvCode", inventory.getCInvCode()));
        if (first != null) {
            return fail(JBoltMsg.DATA_SAME_SN_EXIST);
        }
        setInventory(inventory);
        setIItemAttribute(inventory);
        //if(existsName(inventory.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = inventory.save();
        if (success) {
            //添加日志
            //addSaveSystemLog(inventory.getIAutoId(), JBoltUserKit.getUserId(), inventory.getName());
        }
        return ret(success);
    }

    public Inventory setIItemAttributes(Inventory inventory) {
        List<Dictionary> dictionaries = dictionaryService.getOptionListByTypeKey("iItem_attribute_column");
        StringBuilder itemAttributes = new StringBuilder();
        if (dictionaries != null && dictionaries.size() > 0) {
            for (Dictionary dictionary : dictionaries) {
                Method[] m = inventory.getClass().getMethods();
                for (Method method : m) {
                    if (("get" + dictionary.getName()).equalsIgnoreCase(method.getName())) {
                        try {
                            Boolean invoke = (Boolean) method.invoke(inventory);
                            if (invoke) {
                                itemAttributes.append(dictionary.getSn()).append(",");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            continue;
                        }
                    }
                }
            }
        }
        inventory.setItemAttributes(itemAttributes.length() > 1 ? itemAttributes.substring(0, itemAttributes.length() - 1) : "");
        return inventory;
    }

    public Inventory setIItemAttribute(Inventory inventory) {
        String itemAttributes = inventory.getItemAttributes();
        String[] itemAttribute = null;
        if (StrUtil.isNotBlank(itemAttributes)) {
            itemAttribute = itemAttributes.split(",");
        }
        List<Dictionary> dictionaries = dictionaryService.getOptionListByTypeKey("iItem_attribute_column");
        if (dictionaries != null && dictionaries.size() > 0) {
            for (Dictionary dictionary : dictionaries) {
                Method[] m = inventory.getClass().getMethods();
                for (Method method : m) {
                    if (("set" + dictionary.getName()).equalsIgnoreCase(method.getName())) {
                        try {
                            method.invoke(inventory, false);
                            if (itemAttribute != null) {
                                for (String s : itemAttribute) {
                                    if (s.equals(dictionary.getSn())) {
                                        try {
                                            method.invoke(inventory, true);
                                        } catch (IllegalAccessException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        }
        return inventory;
    }

    /**
     * 设置参数
     */
    private Inventory setInventoryClass(Inventory inventory) {
        inventory.setCOrgCode(getOrgCode());
        inventory.setCOrgName(getOrgName());
        inventory.setIOrgId(getOrgId());
        inventory.setIsDeleted(false);
        Long userId = JBoltUserKit.getUserId();
        inventory.setICreateBy(userId);
        inventory.setIUpdateBy(userId);
        String userName = JBoltUserKit.getUserName();
        inventory.setCCreateName(userName);
        inventory.setCUpdateName(userName);
        Date date = new Date();
        inventory.setDCreateTime(date);
        inventory.setDUpdateTime(date);
        return inventory;
    }

    /**
     * 更新
     */
    public Ret update(Inventory inventory) {
        if (inventory == null || notOk(inventory.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //更新时需要判断数据存在
        Inventory dbInventory = findById(inventory.getIAutoId());
        if (dbInventory == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        //if(existsName(inventory.getName(), inventory.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        setIItemAttribute(inventory);
        boolean success = inventory.update();
        if (success) {
            //添加日志
            //addUpdateSystemLog(inventory.getIAutoId(), JBoltUserKit.getUserId(), inventory.getName());
        }
        return ret(success);
    }

    /**
     * 删除数据后执行的回调
     *
     * @param inventory 要删除的model
     * @param kv        携带额外参数一般用不上
     */
    @Override
    protected String afterDelete(Inventory inventory, Kv kv) {
        //addDeleteSystemLog(inventory.getIAutoId(), JBoltUserKit.getUserId(),inventory.getName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param inventory model
     * @param kv        携带额外参数一般用不上
     */
    @Override
    public String checkInUse(Inventory inventory, Kv kv) {
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
                                        JBoltExcelHeader.create("料品编码", 15),
                                        JBoltExcelHeader.create("料品名称", 15),
                                        JBoltExcelHeader.create("客户部番", 15),
                                        JBoltExcelHeader.create("UG部番", 15),
                                        JBoltExcelHeader.create("部品名称", 15),
                                        JBoltExcelHeader.create("UG部品名称", 15)
                                )
                );
    }

    /**
     * 读取excel文件
     */
    public Ret importExcel(File file) {
        StringBuilder errorMsg = new StringBuilder();
        JBoltExcel jBoltExcel = JBoltExcel
                //从excel文件创建JBoltExcel实例
                .from(file)
                //设置工作表信息
                .setSheets(
                        JBoltExcelSheet.create()
                                //设置列映射 顺序 标题名称
                                .setHeaders(1,
                                        JBoltExcelHeader.create("cInvCode", "料品编码"),
                                        JBoltExcelHeader.create("cInvName", "料品名称"),
                                        JBoltExcelHeader.create("cInvCode1", "客户部番"),
                                        JBoltExcelHeader.create("cInvAddCode1", "UG部番"),
                                        JBoltExcelHeader.create("cInvName1", "部品名称"),
                                        JBoltExcelHeader.create("cInvName2", "UG部品名称")
                                )
                                //从第三行开始读取
                                .setDataStartRow(2)
                );
        //从指定的sheet工作表里读取数据
        List<Inventory> inventorys = JBoltExcelUtil.readModels(jBoltExcel, 1, Inventory.class, errorMsg);
        if (notOk(inventorys)) {
            if (errorMsg.length() > 0) {
                return fail(errorMsg.toString());
            } else {
                return fail(JBoltMsg.DATA_IMPORT_FAIL_EMPTY);
            }
        }

        //执行批量操作
        boolean success = tx(() -> {
            batchSave(inventorys);
            return true;
        });

        if (!success) {
            return fail(JBoltMsg.DATA_IMPORT_FAIL);
        }
        return SUCCESS;
    }

    /**
     * 生成要导出的Excel
     */
    public JBoltExcel exportExcel(List<Record> datas) {
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
                                        JBoltExcelHeader.create("cInvCode", "料品编码", 15),
                                        JBoltExcelHeader.create("cInvName", "料品名称", 15),
                                        JBoltExcelHeader.create("cInvCode1", "客户部番", 15),
                                        JBoltExcelHeader.create("cInvAddCode1", "UG部番", 15),
                                        JBoltExcelHeader.create("cInvName1", "部品名称", 15),
                                        JBoltExcelHeader.create("cInvName2", "UG部品名称", 15)
                                )
                                //设置导出的数据源 来自于数据库查询出来的Model List
                                .setRecordDatas(2, datas)
                );
    }

    /**
     * 复制
     */
    public Ret copy(Inventory inventory) {
        inventory.setIPid(inventory.getIAutoId());
        inventory.setIAutoId(null);
        setInventory(inventory);
        boolean success = inventory.save();
        if (!success) {
            return fail(JBoltMsg.FAIL);
        }
        return SUCCESS;
    }

    /**
     * 设置参数
     */
    private Inventory setInventory(Inventory inventory) {
        inventory.setCOrgCode(getOrgCode());
        inventory.setCOrgName(getOrgName());
        inventory.setIOrgId(getOrgId());
        inventory.setIsDeleted(false);
        Long userId = JBoltUserKit.getUserId();
        inventory.setICreateBy(userId);
        inventory.setIUpdateBy(userId);
        String userName = JBoltUserKit.getUserName();
        inventory.setCCreateName(userName);
        inventory.setCUpdateName(userName);
        Date date = new Date();
        inventory.setDCreateTime(date);
        inventory.setDUpdateTime(date);
        return inventory;
    }

    public List<Record> getAdminDatasNoPage(Kv kv) {
        return dbTemplate("inventoryclass.inventoryList", kv).find();
    }

    public List<Record> getAutocompleteData(String q, Integer limit) {
        return dbTemplate("inventory.getAutocompleteData", Kv.by("q", q).set("limit", limit)).find();
    }

    public List<Record> getAutocompleteData2(String q, Integer limit) {
        return dbTemplate("inventory.getAutocompleteData2", Kv.by("q", q).set("limit", limit)).find();
    }

    public Inventory saveJBoltFile(UploadFile file, String uploadPath, int fileType) {
        String localPath = file.getUploadPath() + File.separator + file.getFileName();
        String localUrl = FileUtil.normalize(JBoltRealUrlUtil.get(JFinal.me().getConstants().getBaseUploadPath() + '/' + uploadPath + '/' + file.getFileName()));
        localPath = FileUtil.normalize(localPath);
        Inventory itempicture = new Inventory();
        itempicture.setCPics(localUrl);
        String originalFileName = file.getOriginalFileName();
        File realFile = file.getFile();
        String fileSuffix = FileTypeUtil.getType(realFile);
        Long fileSize = FileUtil.size(realFile);
        return itempicture;
    }

    public Ret updateForm(Inventory inventory, InventoryAddition inventoryAddition, InventoryPlan inventoryPlan, InventoryMfgInfo inventoryMfgInfo, InventoryStockConfig inventorystockconfig, List<InventoryWorkRegion> inventoryWorkRegions, List<InventoryWorkRegion> newInventoryWorkRegions, Object[] delete, JBoltTable inventoryRoutingJboltTable, JBoltTable inventoryCapacityJboltTable) {
        tx(() -> {
            if (inventory.getIUomType() == null) {
                inventory.setIUomType(null);
            }

            Ret inventoryRet = update(inventory);

            // 存货档案-附加
            inventoryAddition.setIInventoryId(inventory.getIAutoId());

            if (ObjUtil.isNull(inventoryAddition.getIAutoId())) {
                inventoryAdditionService.save(inventoryAddition);
            } else if (ObjUtil.isNotNull(inventoryAddition.getIAutoId())) {
                inventoryAdditionService.update(inventoryAddition);
            }

            // 料品计划档案
            inventoryPlan.setIInventoryId(inventory.getIAutoId());
            if (ObjUtil.isNull(inventoryPlan.getIAutoId())) {
                inventoryPlanService.save(inventoryPlan);
            } else if (ObjUtil.isNotNull(inventoryPlan.getIAutoId())) {
                inventoryPlanService.update(inventoryPlan);
            }

            // 料品生产档案
            inventoryMfgInfo.setIInventoryId(inventory.getIAutoId());
            if (ObjUtil.isNull(inventoryMfgInfo.getIAutoId())) {
                inventoryMfgInfoService.save(inventoryMfgInfo);
            } else if (ObjUtil.isNotNull(inventoryMfgInfo.getIAutoId())) {
                inventoryMfgInfoService.update(inventoryMfgInfo);
            }

            // 料品库存档案
            inventorystockconfig.setIInventoryId(inventory.getIAutoId());
            if (ObjUtil.isNull(inventorystockconfig.getIAutoId())) {
                inventoryStockConfigService.save(inventorystockconfig);
            } else if (ObjUtil.isNotNull(inventorystockconfig.getIAutoId())) {
                inventoryStockConfigService.update(inventorystockconfig);
            }

            // 删除 料品生产--所属生产线
            if (ArrayUtil.isNotEmpty(delete)) {
                deleteMultiByIds(delete);
            }

            // 料品生产--所属生产线修改
            if (inventoryWorkRegions != null && inventoryWorkRegions.size() > 0) {
                for (InventoryWorkRegion workRegion : inventoryWorkRegions) {
                    workRegion.setIInventoryId(inventory.getIAutoId());
                }
                int[] ints = inventoryWorkRegionService.batchUpdate(inventoryWorkRegions);
            }

            // 所属产线新增
            if (newInventoryWorkRegions != null && newInventoryWorkRegions.size() > 0) {
                for (InventoryWorkRegion workRegion : newInventoryWorkRegions) {
                    workRegion.setIInventoryId(inventory.getIAutoId());
                    workRegion.setIsDeleted(false);
                }
                inventoryWorkRegionService.batchSave(newInventoryWorkRegions);
            }

            inventoryWorkRegionService.checkOk(inventory.getIAutoId());

            // 工艺路线操作
            inventoryRoutingOperation(inventory, inventoryRoutingJboltTable);
            // 班次
            operationCapacity(inventory.getIAutoId(), inventoryCapacityJboltTable);
            return true;
        });

        return SUCCESS;
    }

    public void operationCapacity(Long invId, JBoltTable inventoryCapacityJboltTable) {
        if (ObjUtil.isNull(inventoryCapacityJboltTable)) {
            return;
        }
        List<InventoryCapacity> updateInventoryCapacityList = inventoryCapacityJboltTable.getUpdateBeanList(InventoryCapacity.class);
        List<InventoryCapacity> saveInventoryCapacityList = inventoryCapacityJboltTable.getSaveBeanList(InventoryCapacity.class);
        Object[] delCapacityIds = inventoryCapacityJboltTable.getDelete();

        if (CollUtil.isNotEmpty(saveInventoryCapacityList)) {
            setInventoryId(invId, saveInventoryCapacityList);
            inventoryCapacityService.batchSave(saveInventoryCapacityList);
        }

        if (CollUtil.isNotEmpty(updateInventoryCapacityList)) {
            setInventoryId(invId, updateInventoryCapacityList);
            inventoryCapacityService.batchUpdate(updateInventoryCapacityList);
        }

        if (ArrayUtil.isNotEmpty(delCapacityIds)) {
            inventoryCapacityService.deleteByIds(delCapacityIds);
        }
    }

    private void setInventoryId(Long invId, List<InventoryCapacity> capacityList) {
        for (InventoryCapacity capacity : capacityList) {
            capacity.setIInventoryId(invId);
        }
    }

    /**
     * @param inventory                  存货
     * @param inventoryRoutingJboltTable
     */
    public void inventoryRoutingOperation(Inventory inventory, JBoltTable inventoryRoutingJboltTable) {
        if (ObjUtil.isNull(inventoryRoutingJboltTable)) {
            return;
        }

        List<Record> recordList = new ArrayList<>();
        ValidationUtils.notNull(inventory, "未获取到存货档案数据");
        ValidationUtils.notNull(inventory.getIAutoId(), "未获取到存货档案id数据");

        Long masterInvId = inventory.getIAutoId();

        // 获取新增数据
        List<Record> saveRecordList = inventoryRoutingJboltTable.getSaveRecordList();
        // 获取修改数据
        List<Record> updateModelList = inventoryRoutingJboltTable.getUpdateRecordList();

        if (CollUtil.isNotEmpty(saveRecordList)) {
            recordList.addAll(saveRecordList);
        }

        if (CollUtil.isNotEmpty(updateModelList)) {
            recordList.addAll(updateModelList);
        }

        // 获取删除数据
        Object[] deleteRoutingConfig = inventoryRoutingJboltTable.getDelete();

        // 先删除
        if (ArrayUtil.isNotEmpty(deleteRoutingConfig)) {
            inventoryRoutingConfigService.deleteByIds(deleteRoutingConfig);
            // 先删除改工艺路线下所有物料数据
            invPartService.deleteByRoutingConfigIds(deleteRoutingConfig);
            // 关联工序表删除
            inventoryroutingconfigOperationService.deleteByRoutingConfigIds(deleteRoutingConfig);
            // 物料集
            inventoryRoutingInvcService.deleteByRoutingConfigIds(deleteRoutingConfig);
            // 设备集
            inventoryRoutingEquipmentService.deleteByRoutingConfigIds(deleteRoutingConfig);
            // 作业指导书
        }

        // 为空无需操作
        if (CollUtil.isEmpty(recordList)) {
            return;
        }
        // 判断当前工序是否与1， 大于1则按seq排序
        if (CollUtil.isNotEmpty(recordList) && recordList.size() > 1) {
            CollUtil.sort(recordList, Comparator.comparingInt(u -> u.getInt(InventoryRoutingConfig.ISEQ)));
        }

        Long userId = JBoltUserKit.getUserId();
        String userName = JBoltUserKit.getUserName();
        DateTime date = DateUtil.date();
        Long orgId = getOrgId();

        // 添加物料中间表
        List<InvPart> invPartList = operation(masterInvId, recordList);
        // 保存
        invPartService.batchSave(invPartList);

        // 保存工序表及工序关联表
        inventoryRoutingConfigService.saveRoutingConfigAndOperation(orgId, userId, userName, date, recordList);
        // 物料集
        inventoryRoutingInvcService.saveRoutingInv(userId, userName, date, recordList);
        // 保存设备集
        inventoryRoutingEquipmentService.saveRoutingEquipment(userId, userName, date, recordList);
        // 保存作业指导书
    }

    public List<InvPart> operation(Long masterInvId, List<Record> recordList) {
		/*// 第一道工序次序
		Integer minSeq = getMinSeq(recordList);
		// 最后一道工序次序
		Integer maxSeq = getMaxSeq(recordList);*/
		
		/*// 校验是否新增，全是新增的话，无需查数据；
		boolean flag = recordList.stream().allMatch(record -> ObjUtil.isNull(record.getLong(InventoryRoutingConfig.IAUTOID)));
		if (!flag){
			inventoryRoutingConfigService
		}*/

        Long orgId = getOrgId();
        // 获取第一个第一个物料集
        Map<Integer, Record> seqMap = recordList.stream().collect(Collectors.toMap(record -> record.getInt(InventoryRoutingConfig.ISEQ.toLowerCase()), record -> record));
        // 记录中间表数据（存货物料表）
        List<InvPart> invPartList = new ArrayList<>();
        // 从后往前执行
        for (int i = recordList.size() - 1; i >= 0; i--) {
            Record record = recordList.get(i);

            Long routingId = record.getLong(InventoryRoutingConfig.IINVENTORYROUTINGID);

            ValidationUtils.notNull(routingId, "缺少工艺路线id");
            // 标识是否为新增
            boolean isAdd = false;
            Long routingConfigId = record.getLong(InventoryRoutingConfig.IAUTOID);
            if (ObjUtil.isNull(routingConfigId)) {
                routingConfigId = JBoltSnowflakeKit.me.nextId();
                isAdd = true;
            }
            record.set(InventoryRoutingConfig.ISADD, isAdd);
            record.set(InventoryRoutingConfig.IAUTOID, routingConfigId);
            // 半成品
//            String iRsInventoryIdStr = record.getStr(InventoryRoutingConfig.IRSINVENTORYID);
            String iRsInventoryIdStr = null;
            // 工序名称
            String cOperationName = getCOperationName(record.getStr(InventoryRoutingConfig.COPERATIONNAME));

            // 物料类型
            int invPartType = InvPartTypeEnum.inventory.getValue();
            Long iRsInventoryId = StrUtil.isBlank(iRsInventoryIdStr) ? null : Long.valueOf(iRsInventoryIdStr);
            // 半成品为空说明是虚拟件
            if (StrUtil.isBlank(iRsInventoryIdStr)) {
                invPartType = InvPartTypeEnum.virtual.getValue();
            }

            // 工序次序
            Integer seq = record.getInt(InventoryRoutingConfig.ISEQ);
            // 生产工艺
            String cProductTechSn = record.getStr(InventoryRoutingConfig.CPRODUCTTECHSN);
            // 生产方式
            String cProductSn = record.getStr(InventoryRoutingConfig.CPRODUCTSN);
            // 工序类型: 1. 串序 2. 并序
            Integer type = record.getInt(InventoryRoutingConfig.ITYPE);
            Integer perSeq = seq + 10;
            // (没有上一级key，说明是最后一到工序)
            Integer nextSeq = seq - 10;

            Object itemJson = record.get(InventoryRoutingConfig.ITEMJSONSTR);
            List<JSONObject> itemList = null;
            if (itemJson instanceof List) {
                itemList = (List<JSONObject>) itemJson;
            }

            String prefixErrorMsg = "当前工序【" + seq + "】";
            // 校验
            int count = 0;
            // 最后一到工序
            if (i == recordList.size() - 1) {
                /*
                 * 最后一到工序校验逻辑：
                 * 	1.必须为串序
                 * 	2.半成品名称必须为产品名称
                 * 	3.当前物料集必须包含上一级半成品
                 */
                ValidationUtils.notNull(type, "工序类型不能为空");
                ValidationUtils.isTrue(OperationTypeEnum.bunchSequence.getValue() == type, "最后一道工序的工序类型，必须为串序");
                ValidationUtils.isTrue(masterInvId.equals(iRsInventoryId), "最后一道工序的半成品名称，必须为当前产品存货名称");
            }
            // 校验
            checkRoutingInv(prefixErrorMsg, count, type, nextSeq, masterInvId, iRsInventoryId, itemList, seqMap);

            Long pid = null;

            // 只有最后一道工序才会创建)
            if (i == recordList.size() - 1) {
                Inventory inventory = findById(iRsInventoryId);
                ValidationUtils.notNull(inventory, "未匹配到半成品存货");
                String partCode = inventory.getCInvCode();
                String partName = inventory.getCInvName();
                InvPart invPart = invPartService.createInvPart(routingId, orgId, iRsInventoryId, masterInvId, null, routingConfigId,
                        invPartType, null, partCode, partName);
                invPartList.add(invPart);
                pid = invPart.getIAutoId();
            }
            // 获取上一级id
            if (ObjUtil.isNull(pid)) {
                // 默认为虚拟件
                String partName = "【虚拟件-" + cOperationName + "】";
                if (ObjUtil.isNotNull(iRsInventoryId)) {
                    Inventory inventory = findById(iRsInventoryId);
                    ValidationUtils.notNull(inventory, "未匹配到半成品存货");
                    partName = inventory.getCInvName();
                }
                pid = getInvPartPId(type, seq + 10, partName, invPartList, seqMap);
            }
            // 添加半成品下的物料记录
            List<InvPart> childInvPartList = createInvPartList(routingId, orgId, masterInvId, pid, routingConfigId, invPartType, seq, itemList, seqMap);
            if (CollUtil.isNotEmpty(childInvPartList)) {
                invPartList.addAll(childInvPartList);
            }

        }
        return invPartList;
    }

    /**
     * 查找上一级父id
     */
    public Long getInvPartPId(int type, Integer nextSeq, String partName, List<InvPart> invPartList, Map<Integer, Record> seqMap) {
        // 并序
        int parallelSequenceValue = OperationTypeEnum.parallelSequence.getValue();
        int bunchSequenceValue = OperationTypeEnum.bunchSequence.getValue();
        // 串序：直接默认取上一级
        if (type == bunchSequenceValue) {
            // 通过次序分组
            return getPId(partName, nextSeq, invPartList);
        }
        // 判断上一级工序是否并行，并行就继续往上找
        if (seqMap.containsKey(nextSeq)) {
            Record nextRecord = seqMap.get(nextSeq);
            Integer nextType = nextRecord.getInt(InventoryRoutingConfig.ITYPE);
            if (nextType == bunchSequenceValue) {
                return getPId(partName, nextSeq, invPartList);
            }
            return getInvPartPId(nextType, nextSeq + 10, partName, invPartList, seqMap);
        }
        return null;
    }

    private Long getPId(String partName, Integer nextSeq, List<InvPart> invPartList) {
        List<InvPart> invParts = invPartList.stream().filter(invPart -> nextSeq.equals(invPart.getISeq())).collect(Collectors.toList());
        for (InvPart invPart : invParts) {
            String cPartName = invPart.getCPartName();
            if (partName.equals(cPartName)) {
                return invPart.getIAutoId();
            }
        }
        return null;
    }

    public List<InvPart> createInvPartList(Long routingId, Long orgId, Long parentInvId, Long pid, Long routingConfigId, int invPartType, int seq, List<JSONObject> itemList, Map<Integer, Record> seqMap) {
        if (CollUtil.isEmpty(itemList)) {
            return null;
        }

        List<InvPart> invPartList = new ArrayList<>();
        for (JSONObject jsonObject : itemList) {
            Long iInventoryId = jsonObject.getLong(InventoryRoutingInvc.IINVENTORYID.toLowerCase());
            String partCode = jsonObject.getString(Inventory.CINVCODE.toLowerCase());
            String partName = jsonObject.getString(Inventory.CINVNAME.toLowerCase());
            InvPart invPart = invPartService.createInvPart(routingId, orgId, iInventoryId, parentInvId, pid, routingConfigId, invPartType, seq, partCode, partName);
            invPartList.add(invPart);
        }
        // 查找虚拟件 setInvPartList
        // 下一道为串序，判断下一道 半成品是否为空
        createVirtualInvPartList(0, seq, seq - 10, seqMap, invPartList);

        invPartService.setInvPartList(routingId, orgId, parentInvId, pid, routingConfigId, seq, invPartList);
        return invPartList;
    }

    public void createVirtualInvPartList(int count, int thisSeq, int perSeq, Map<Integer, Record> seqMap, List<InvPart> invPartList) {
        // 串序：找上一级所有并序（不包含串序）是否存在虚拟件
        // 并序：只需要找上一级是否为串序（不是串序不用管，是串序就判断是不是虚拟件）
        if (!seqMap.containsKey(perSeq)) {
            return;
        }
        Record record = seqMap.get(thisSeq);
        // 当前类型
        Integer thisType = record.getInt(InventoryRoutingConfig.ITYPE);

        Record perConfig = seqMap.get(perSeq);
//        String iRsInventoryId = perConfig.getStr(InventoryRoutingConfig.IRSINVENTORYID);
        String iRsInventoryId = null;
        String cOperationName = getCOperationName(perConfig.getStr(InventoryRoutingConfig.COPERATIONNAME));

        String partName = "【虚拟件-" + cOperationName + "】";
        Integer type = perConfig.getInt(InventoryRoutingConfig.ITYPE);

        // 并序
        int parallelSequenceValue = OperationTypeEnum.parallelSequence.getValue();
        int bunchSequenceValue = OperationTypeEnum.bunchSequence.getValue();

        // 当前是并序的，下一道也是并序的
        if (thisType == parallelSequenceValue && parallelSequenceValue == type) {
            return;
        } else if (thisType == bunchSequenceValue && parallelSequenceValue == type) {
            createVirtualInvPartList(count, thisSeq, perSeq - 10, seqMap, invPartList);
        }

        if (count > 0 && bunchSequenceValue == type) {
            return;
        }

        if (StrUtil.isBlank(iRsInventoryId)) {
            InvPart invPart = invPartService.createInvPart(InvPartTypeEnum.virtual.getValue(), partName);
            invPartList.add(invPart);
        }
    }

    /**
     * @param perSeq 前工序次序
     */
    public void checkRoutingInv(String prefixErrorMsg, int count, int thisType, Integer perSeq, Long masterInvId, Long rsInventoryId, List<JSONObject> itemList, Map<Integer, Record> seqMap) {
        ValidationUtils.notEmpty(itemList, "未设置物料集！");
        if (!seqMap.containsKey(perSeq)) {
            // 不包含上一级次序，说明是第一到工序
            checkRouting(masterInvId, rsInventoryId, null, prefixErrorMsg, itemList);
            return;
        }

        Record routingConfigRecord = seqMap.get(perSeq);

        Integer type = routingConfigRecord.getInt(InventoryRoutingConfig.ITYPE);

        OperationTypeEnum operationTypeEnum = OperationTypeEnum.toEnum(type);
        ValidationUtils.notNull(operationTypeEnum, prefixErrorMsg + "未知工序类型！！！");

        /*
         * 当前为并序
         * 		1.找上一级是否为并序。为并序无需校验
         * 		2.上一级为串序，才校验物料集是否有包含
         */

        // 并序
        int parallelSequenceValue = OperationTypeEnum.parallelSequence.getValue();
        int bunchSequenceValue = OperationTypeEnum.bunchSequence.getValue();
        // 当前工序为并序 上一道也为并行，无需校验
        if (thisType == parallelSequenceValue && parallelSequenceValue == operationTypeEnum.getValue()) {
            return;
        } else if (thisType == bunchSequenceValue && parallelSequenceValue == operationTypeEnum.getValue()) {
            count += 1;
            checkRoutingInv(prefixErrorMsg, count, thisType, perSeq - 10, masterInvId, rsInventoryId, itemList, seqMap);
        }
//		else if (thisType == parallelSequenceValue || thisType == bunchSequenceValue && bunchSequenceValue == operationTypeEnum.getValue()){
//
//		}

        // 判断当前是第几层进入并行
        if (count > 0 && bunchSequenceValue == operationTypeEnum.getValue()) {
            return;
        }
//        String iRsInventoryIdStr = routingConfigRecord.getStr(InventoryRoutingConfig.IRSINVENTORYID);
//        if (StrUtil.isBlank(iRsInventoryIdStr)) {
//            return;
//        }
//        // 半成品，虚拟件跳过
//        Long iRsInventoryId = routingConfigRecord.getLong(InventoryRoutingConfig.IRSINVENTORYID);
        checkRouting(masterInvId, rsInventoryId, null, prefixErrorMsg, itemList);
    }

    private void checkRouting(Long masterInvId, Long rsInventoryId, Long iRsInventoryId, String prefixErrorMsg, List<JSONObject> itemList) {
        boolean flag = itemList.stream()
                .map(obj -> obj.getLong(InventoryRoutingInvc.IINVENTORYID.toLowerCase()))
                .distinct()
                .count() == itemList.size();

        ValidationUtils.isTrue(flag, prefixErrorMsg + "物料集中存在相同的存货");
        Map<Long, JSONObject> recordMap = itemList.stream().collect(Collectors.toMap(record -> record.getLong(InventoryRoutingInvc.IINVENTORYID.toLowerCase()), record -> record));
        ValidationUtils.isTrue(!recordMap.containsKey(masterInvId), prefixErrorMsg + "物料集不能存在成品存货");

        if (ObjUtil.isNotNull(rsInventoryId)) {
            ValidationUtils.isTrue(!recordMap.containsKey(rsInventoryId), prefixErrorMsg + "物料集不能当前半成品存货");
        }
        if (ObjUtil.isNotNull(iRsInventoryId)) {
            ValidationUtils.isTrue(recordMap.containsKey(iRsInventoryId), prefixErrorMsg + "物料集没有包含上一级工序半成品");
        }
    }

    public Integer getMinSeq(List<Record> recordList) {
        if (CollUtil.isEmpty(recordList)) {
            return 0;
        }
        if (recordList.size() == 1) {
            return recordList.get(0).getInt(InventoryRoutingConfig.ISEQ);
        }
        Record minRecord = Collections.min(recordList, Comparator.comparingInt(u -> u.getInt(InventoryRoutingConfig.ISEQ)));
        return minRecord.getInt(InventoryRoutingConfig.ISEQ);
    }

    public Integer getMaxSeq(List<Record> recordList) {
        if (CollUtil.isEmpty(recordList)) {
            return 0;
        }
        if (recordList.size() == 1) {
            return recordList.get(0).getInt(InventoryRoutingConfig.ISEQ);
        }
        Record manRecord = Collections.max(recordList, Comparator.comparingInt(u -> u.getInt(InventoryRoutingConfig.ISEQ)));
        return manRecord.getInt(InventoryRoutingConfig.ISEQ);
    }

    /**
     * @param masterInvId 存货id
     * @param partInvIds  上一级子件半成品
     * @param invList     当前物料集数据
     */
    public boolean checkInvPartList(Long masterInvId, List<Long> partInvIds, List<Record> invList) {
        return false;
    }

    public void deleteMultiByIds(Object[] deletes) {
        delete("update Bd_InventoryWorkRegion set isDeleted = 1 WHERE iAutoId IN (" + ArrayUtil.join(deletes, ",") + ") ");
    }

    public Ret saveForm(Inventory inventory, InventoryAddition inventoryAddition, InventoryPlan inventoryPlan, InventoryMfgInfo inventoryMfgInfo,
                        InventoryStockConfig inventorystockconfig, List<InventoryWorkRegion> inventoryWorkRegions,
                        JBoltTable inventoryRoutingJboltTable, JBoltTable inventoryCapacityJboltTable) {
        tx(() -> {
            Ret inventoryRet = save(inventory);
            if (inventoryRet.isFail()) {
                return false;
            }
            
            // 存货档案-附加
            inventoryAddition.setIInventoryId(inventory.getIAutoId());
            Ret additionRet = inventoryAdditionService.save(inventoryAddition);
            if (additionRet.isFail()) {
                return false;
            }

            // 料品计划档案
            inventoryPlan.setIInventoryId(inventory.getIAutoId());
            Ret planRet = inventoryPlanService.save(inventoryPlan);
            if (planRet.isFail()) {
                return false;
            }

            // 料品生产档案
            inventoryMfgInfo.setIInventoryId(inventory.getIAutoId());
            Ret mfgInfoRet = inventoryMfgInfoService.save(inventoryMfgInfo);
            if (mfgInfoRet.isFail()) {
                return false;
            }

            // 料品库存档案
            inventorystockconfig.setIInventoryId(inventory.getIAutoId());
            Ret stockRet = inventoryStockConfigService.save(inventorystockconfig);
            if (stockRet.isFail()) {
                return false;
            }

            // 所属产线新增
            if (inventoryWorkRegions != null && inventoryWorkRegions.size() > 0) {
                for (InventoryWorkRegion workRegion : inventoryWorkRegions) {
                    workRegion.setIInventoryId(inventory.getIAutoId());
                    workRegion.setIsDeleted(false);
                }
                int[] ints = inventoryWorkRegionService.batchSave(inventoryWorkRegions);
                ValidationUtils.isTrue(ints.length == inventoryWorkRegions.size(), "产线信息异常!");
            }
            
            // 工艺路线操作
            inventoryRoutingOperation(inventory, inventoryRoutingJboltTable);
            // 班次
            operationCapacity(inventory.getIAutoId(), inventoryCapacityJboltTable);
            
            return true;
        });

        return SUCCESS;
    }

    public List<Record> options(Kv kv) {
        List<Record> inventories = dbTemplate("inventory.options").find();
        if (inventories != null) {
            Long inventoryId = kv.getLong("inventoryid");
            if (inventoryId == null) {
                return inventories;
            }
            if (inventories.size() > 0) {
                for (Record i : inventories) {
                    if (i.getLong("iautoid").longValue() == inventoryId.longValue()) {
                        return inventories;
                    }
                }
            }
            Record inventory = new Record();
            Long iinventoryuomid1 = kv.getLong("iinventoryuomid1");
            String cInvCode = kv.getStr("cinvcode");
            String cInvCode1 = kv.getStr("cinvcode1");
            String cInvName1 = kv.getStr("cinvname1");
            String cInvStd = kv.getStr("cinvstd");
            if (StrUtil.isBlank(cInvName1)) {
                return inventories;
            }
            inventory.put("iautoid", inventoryId);
            inventory.put("cinvname1", cInvName1);
            inventory.put("cinvcode", cInvCode);
            inventory.put("cinvcode1", cInvCode1);
            inventory.put("cinvstd", cInvStd);
            //inventory.put("iautoid",iinventoryuomid1);
            inventories.add(inventory);
        }
        return inventories;
    }

    public List<Record> dataBomList() {
        return dbTemplate("inventory.getInventoryDataList").find();
    }

    /**
     * 查所有
     */
    public Page<Record> inventorySpotCheckList(Kv kv) {
        return dbTemplate("inventoryclass.inventorySpotCheckList", kv).paginate(kv.getInt("page"), kv.getInt(
                "pageSize"));
    }

    /**
     * 根据客户部番或UG部番 查找存货
     */
    public Inventory findBycInvAddCode(String cInvAddCode) {
        return findFirst("SELECT * FROM Bd_Inventory WHERE cInvCode1 = ? OR cInvAddCode1 = ?", cInvAddCode, cInvAddCode);
    }

    public Inventory findBycInvCode(String cInvCode) {
        return findFirst("SELECT top 1 * FROM Bd_Inventory WHERE cInvCode = ? and cOrgCode = ?", cInvCode, getOrgCode());

    }

    public Ret batchFetch(String column, String values) {
        List<Record> inventories = findByColumnsAndValues(column, values);
        return successWithData(inventories);
    }

    public List<Record> findByColumnsAndValues(String column, String values) {
        List<String> codes = StrSplitter.split(values, COMMA, true, true);

        Sql sql = selectSql()
                .select("i.*, u.cuomname")
                .from(table(), "i")
                .leftJoin(uomService.table(), "u", "i.iInventoryUomId1 = u.iautoid")
                .in(column, codes);

        return findRecord(sql);
    }

    public int getCinvcode1Count(String cinvcode1) {
        return queryInt("SELECT COUNT(*) FROM Bd_Inventory WHERE iorgid = ? AND cinvcode1 = ? AND isDeleted = ? ", getOrgId(), cinvcode1, ZERO_STR);
    }

    public Record findFirstByCinvcode1WithUom(String cinvcode1) {
        Sql sql = selectSql()
                .select("i.*, u.cuomname")
                .from(table(), "i")
                .leftJoin(uomService.table(), "u", "i.iInventoryUomId1 = u.iautoid")
                .eq(Inventory.CINVCODE1, cinvcode1)
                .first();

        return findFirstRecord(sql);
    }

    public Ret fetchByCinvcode1s(String cinvcode1) {
        List<String> codes = StrSplitter.split(cinvcode1, COMMA, true, true);

        // 去重后的客户部番
        codes = CollUtil.distinct(codes);

        List<Record> list = new ArrayList<>();

        for (String code : codes) {
            int cnt = getCinvcode1Count(code);
            if (cnt == 0 || cnt > 1) {
                list.add(new Record().set("cinvcode1", code).set("cnt", cnt));
            } else {
                list.add(findFirstByCinvcode1WithUom(code).set("cnt", 1));
            }
        }

        return successWithData(list);
    }

    /**
     * 多个可编辑表格同时提交
     */
    public Ret submitMulti(JBoltTableMulti jboltTableMulti) {
        if (jboltTableMulti == null || jboltTableMulti.isEmpty()) {
            return fail(JBoltMsg.JBOLTTABLE_IS_BLANK);
        }
        //这里可以循环遍历 保存处理每个表格 也可以 按照name自己get出来单独处理
        // 工艺路线
        JBoltTable inventoryRoutingJboltTable = getJboltTable(jboltTableMulti, InventoryTableTypeEnum.INVENTORROUTING.getText());
        // 所属生产线
        JBoltTable inventoryWorkRegionJboltTable = getJboltTable(jboltTableMulti, InventoryTableTypeEnum.INVENTORYWORKREGION.getText());
        // 班组产能
        JBoltTable inventoryCapacity = getJboltTable(jboltTableMulti, InventoryTableTypeEnum.INVENTORYCAPACITY.getText());

        ValidationUtils.notNull(inventoryWorkRegionJboltTable, "未获取到产线table");
        ValidationUtils.notNull(inventoryRoutingJboltTable, "未获取到工序table");

        Inventory inventory = inventoryWorkRegionJboltTable.getFormBean(Inventory.class, "inventory");

        InventoryStockConfig inventorystockconfig = inventoryWorkRegionJboltTable.getFormBean(InventoryStockConfig.class, "inventorystockconfig");
        InventoryPlan inventoryPlan = inventoryWorkRegionJboltTable.getFormBean(InventoryPlan.class, "inventoryplan");
        InventoryAddition inventoryAddition = inventoryWorkRegionJboltTable.getFormBean(InventoryAddition.class, "inventoryaddition");
        InventoryMfgInfo inventoryMfgInfo = inventoryWorkRegionJboltTable.getFormModel(InventoryMfgInfo.class, "inventorymfginfo");
        ValidationUtils.notNull(inventoryMfgInfo, "质检开关");
        // 判断当前存货是否存在
        Inventory byId = findById(inventory.getIAutoId());
        // 新增
        if (byId != null) {
            List<InventoryWorkRegion> inventoryWorkRegions = inventoryWorkRegionJboltTable.getUpdateBeanList(InventoryWorkRegion.class);
            List<InventoryWorkRegion> saveBeanList = inventoryWorkRegionJboltTable.getSaveBeanList(InventoryWorkRegion.class);
            Object[] delete = inventoryWorkRegionJboltTable.getDelete();
            return updateForm(inventory, inventoryAddition, inventoryPlan, inventoryMfgInfo, inventorystockconfig, inventoryWorkRegions, saveBeanList, delete, inventoryRoutingJboltTable, inventoryCapacity);
        }
        List<InventoryWorkRegion> inventoryWorkRegions = inventoryWorkRegionJboltTable.getSaveBeanList(InventoryWorkRegion.class);
        return saveForm(inventory, inventoryAddition, inventoryPlan, inventoryMfgInfo, inventorystockconfig, inventoryWorkRegions, inventoryRoutingJboltTable, inventoryCapacity);
    }

    public JBoltTable getJboltTable(JBoltTableMulti jboltTableMulti, String type) {

        if (CollUtil.isNotEmpty(jboltTableMulti)) {
            for (String tableKey : jboltTableMulti.keySet()) {
                if (type.equals(tableKey)) {
					/*//获取额外传递参数 比如订单ID等信息 在下面数据里可能用到
						if(jBoltTable.paramsIsNotBlank()) {
							System.out.println(jBoltTable.getParams().toJSONString());
						}
						//获取Form对应的数据
						if(jBoltTable.formIsNotBlank()) {
			
						}
						//获取待保存数据 执行保存
						if(jBoltTable.saveIsNotBlank()) {
			//				batchSave(jBoltTable.getSaveModelList(Demotable.class));
						}
						//获取待更新数据 执行更新
						if(jBoltTable.updateIsNotBlank()) {
			//				batchUpdate(jBoltTable.getUpdateModelList(Demotable.class));
						}
						//获取待删除数据 执行删除
						if(jBoltTable.deleteIsNotBlank()) {
							deleteByIds(jBoltTable.getDelete());
						}*/

                    return jboltTableMulti.getJBoltTable(type);
                }
            }
        }

        return null;
    }

    public String getCOperationName(String cOperationName) {
        String[] splitCOperationName = inventoryRoutingConfigService.getSplitCOperationName(cOperationName);
        if (ArrayUtil.isEmpty(splitCOperationName)) {
            return null;
        }
        return splitCOperationName[0];
    }

    public Record findByCinvcode(String cinvcode) {
        return findFirstRecord(selectSql().eq("cinvcode", cinvcode).eq("isdeleted", IsEnableEnum.NO.getValue()).eq("isenabled", IsEnableEnum.NO.getValue()));
    }

    /**
     * 获取存货信息Autocomplete
     */
    public List<Record> getInventoryAutocomplete(Kv kv) {
        Page<Record> page = dbTemplate("inventory.getInventoryList", kv).paginate(1, 100);
        ValidationUtils.notNull(page, JBoltMsg.DATA_NOT_EXIST);
        return page.getList();
    }

    /**
     * 获取存货信息分页
     */
    public Page<Record> getInventoryPaginate(Integer pageNumber, Integer pageSize, Kv para) {
        para.set("iorgid", getOrgId());
        Page<Record> page = dbTemplate("inventory.getInventoryList", para).paginate(pageNumber, pageSize);
        ValidationUtils.notNull(page, JBoltMsg.DATA_NOT_EXIST);
        return page;
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

            if (StrUtil.isBlank(record.getStr("iInventoryClassId"))) {
                return fail("所属分类不能为空");
            }
            if (StrUtil.isBlank(record.getStr("cInvCode"))) {
                return fail("存货编码不能为空");
            }
            if (StrUtil.isBlank(record.getStr("cInvName"))) {
                return fail("存货名称不能为空");
            }
            if (StrUtil.isBlank(record.getStr("isGavePresent"))) {
                return fail("支给件是否赠品不能为空");
            }

            record.set("iAutoId", JBoltSnowflakeKit.me.nextId());
            record.set("iOrgId", getOrgId());
            record.set("cOrgCode", getOrgCode());
            record.set("cOrgName", getOrgName());
            record.set("iSource", SourceEnum.MES.getValue());
            record.set("isEnabled", 1);
            record.set("iCreateBy", JBoltUserKit.getUserId());
            record.set("dCreateTime", now);
            record.set("cCreateName", JBoltUserKit.getUserName());
            record.set("isDeleted", 0);
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

    /**
     * 获取存货信息
     */
    public Inventory findByiInventoryCode(String cinvcode) {
        return findFirst(selectSql().eq(Inventory.CINVCODE, cinvcode).eq(Inventory.IORGID, getOrgId()).eq(Inventory.ISDELETED, ZERO_STR).first());
    }

    public Page<Record> resourceList(Integer pageNumber, Integer pageSize, Kv kv) {
        Page<Record> paginate = dbTemplate("inventory.resourceList", kv).paginate(pageNumber, pageSize);
        if (CollUtil.isNotEmpty(paginate.getList())) {
            DateTime now = DateUtil.date();

            Map<Long, Record> versionMap = bomMService.findByVersionMap(getOrgId());


            for (Record record : paginate.getList()) {
                Long invId = record.getLong(BomD.IINVENTORYID);
                if (versionMap.containsKey(invId)) {
                    Record versionRecord = versionMap.get(invId);
                    record.set(BomD.IINVPARTBOMMID, versionRecord.getLong(BomD.IAUTOID));
                    record.set(BomD.CVERSION, versionRecord.getStr(BomD.CVERSION));
                }

                // 材料类别
                Integer iPartType = record.getInt(Inventory.IPARTTYPE);
                if (ObjUtil.isNotNull(iPartType)) {
                    PartTypeEnum partTypeEnum = PartTypeEnum.toEnum(iPartType);
                    ValidationUtils.notNull(partTypeEnum, "未知材料类别");
                    record.set(Inventory.PARTTYPENAME, partTypeEnum.getText());
                }
                // 是否虚拟件
                Integer isVirtual = record.getInt(Inventory.ISVIRTUAL);
                IsEnableEnum isEnableEnum = IsEnableEnum.toEnum(isVirtual);
                record.set(Inventory.ISVIRTALNAME, isEnableEnum.getText());
            }
        }
        return paginate;
    }
    
}

