package cn.rjtech.admin.inventory;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.text.StrSplitter;
import cn.hutool.core.util.ArrayUtil;
import cn.jbolt._admin.dictionary.DictionaryService;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.Dictionary;
import cn.jbolt.core.poi.excel.JBoltExcel;
import cn.jbolt.core.poi.excel.JBoltExcelHeader;
import cn.jbolt.core.poi.excel.JBoltExcelSheet;
import cn.jbolt.core.poi.excel.JBoltExcelUtil;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.util.JBoltRealUrlUtil;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.inventoryaddition.InventoryAdditionService;
import cn.rjtech.admin.inventorymfginfo.InventoryMfgInfoService;
import cn.rjtech.admin.inventoryplan.InventoryPlanService;
import cn.rjtech.admin.inventorystockconfig.InventoryStockConfigService;
import cn.rjtech.admin.inventoryworkregion.InventoryWorkRegionService;
import cn.rjtech.admin.uom.UomService;
import cn.rjtech.model.momdata.*;
import com.jfinal.aop.Inject;
import com.jfinal.core.JFinal;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static cn.hutool.core.text.StrPool.COMMA;

/**
 * 物料建模-存货档案
 * @ClassName: InventoryService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-23 09:35
 */
public class InventoryService extends BaseService<Inventory> {
    
	private final Inventory dao=new Inventory().dao();

    @Inject
    private UomService uomService;
    @Inject
	private InventoryStockConfigService inventoryStockConfigService;
    @Inject
	private InventoryMfgInfoService inventoryMfgInfoService;
    @Inject
	private InventoryPlanService inventoryPlanService;
    @Inject
	private InventoryAdditionService inventoryAdditionService;
    @Inject
	private InventoryWorkRegionService inventoryWorkRegionService;
    @Inject
	private DictionaryService dictionaryService;

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
	 * @param pageNumber 第几页
	 * @param pageSize   每页几条数据
	 * @param kv 参数
	 * @return
	 */
	public Page<Record> getAdminDatas(int pageNumber, int pageSize, Kv kv) {
		Boolean isEnabled = kv.getBoolean("isEnabled");
		if(isEnabled != null)
			kv.set("isEnabled",isEnabled?"1":"0");
		Long id = kv.getLong("iInventoryClassId");
		if(id != null && id == 1L){
			kv.remove("iInventoryClassId");
		}
		String iInventoryClassCode = kv.getStr("iInventoryClassCode");
		if(StringUtils.isNotBlank(iInventoryClassCode)){
			if(!iInventoryClassCode.contains("["))
				kv.remove("iInventoryClassCode");
			else
				kv.set("iInventoryClassCode",iInventoryClassCode.substring(1,iInventoryClassCode.indexOf("]")));
		}
		return dbTemplate("inventoryclass.inventoryList",kv).paginate(pageNumber,pageSize);
	}

	/**
	 * 保存
	 * @param inventory
	 * @return
	 */
	public Ret save(Inventory inventory) {
		if(inventory==null) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		Inventory first = findFirst(selectSql().eq("cInvCode", inventory.getCInvCode()));
		if(first != null)
			return fail(JBoltMsg.DATA_SAME_SN_EXIST);
		setInventory(inventory);
		setIItemAttribute(inventory);
		//if(existsName(inventory.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=inventory.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(inventory.getIAutoId(), JBoltUserKit.getUserId(), inventory.getName());
		}
		return ret(success);
	}

	public Inventory setIItemAttributes(Inventory inventory) {
		List<Dictionary> dictionaries = dictionaryService.getOptionListByTypeKey("iItem_attribute_column");
		StringBuilder itemAttributes = new StringBuilder();
		if(dictionaries != null && dictionaries.size() > 0){
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
                            continue;
                        }
                    }
                }
			}
		}
		inventory.setItemAttributes(itemAttributes.length()>1?itemAttributes.substring(0,itemAttributes.length()-1):"");
		return inventory;
	}

	public Inventory setIItemAttribute(Inventory inventory){
		String itemAttributes = inventory.getItemAttributes();
		String[] itemAttribute = null;
		if(StringUtils.isNotBlank(itemAttributes)){
			itemAttribute = itemAttributes.split(",");
		}
		List<Dictionary> dictionaries = dictionaryService.getOptionListByTypeKey("iItem_attribute_column");
		if(dictionaries != null && dictionaries.size() > 0){
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
                                            continue;
                                        }
                                    }
                                }
                            }
                        } catch (Exception e) {
                            continue;
                        }
                    }
                }

			}
		}
		return inventory;
	}

	/**
	 * 设置参数
	 * @param inventory
	 * @return
	 */
	private Inventory setInventoryClass(Inventory inventory){
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
	 * @param inventory
	 * @return
	 */
	public Ret update(Inventory inventory) {
		if(inventory==null || notOk(inventory.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		Inventory dbInventory=findById(inventory.getIAutoId());
		if(dbInventory==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(inventory.getName(), inventory.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		setIItemAttribute(inventory);
		boolean success=inventory.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(inventory.getIAutoId(), JBoltUserKit.getUserId(), inventory.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param inventory 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(Inventory inventory, Kv kv) {
		//addDeleteSystemLog(inventory.getIAutoId(), JBoltUserKit.getUserId(),inventory.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param inventory model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(Inventory inventory, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

	/**
	 * 生成excel导入使用的模板
	 * @return
	 */
	public JBoltExcel getImportExcelTpl() {
		return JBoltExcel
			//创建
			.create()
			.setSheets(
				JBoltExcelSheet.create()
					//设置列映射 顺序 标题名称 不处理别名
					.setHeaders(1,false,
						JBoltExcelHeader.create("料品编码",15),
						JBoltExcelHeader.create("料品名称",15),
						JBoltExcelHeader.create("客户部番",15),
						JBoltExcelHeader.create("UG部番",15),
						JBoltExcelHeader.create("部品名称",15),
						JBoltExcelHeader.create("UG部品名称",15)
					)
			);
	}

	/**
	 * 读取excel文件
	 * @param file
	 * @return
	 */
	public Ret importExcel(File file) {
		StringBuilder errorMsg=new StringBuilder();
		JBoltExcel jBoltExcel=JBoltExcel
			//从excel文件创建JBoltExcel实例
			.from(file)
			//设置工作表信息
			.setSheets(
				JBoltExcelSheet.create()
					//设置列映射 顺序 标题名称
					.setHeaders(1,
						JBoltExcelHeader.create("cInvCode","料品编码"),
						JBoltExcelHeader.create("cInvName","料品名称"),
						JBoltExcelHeader.create("cInvCode1","客户部番"),
						JBoltExcelHeader.create("cInvAddCode1","UG部番"),
						JBoltExcelHeader.create("cInvName1","部品名称"),
						JBoltExcelHeader.create("cInvName2","UG部品名称")
					)
					//从第三行开始读取
					.setDataStartRow(2)
			);
		//从指定的sheet工作表里读取数据
		List<Inventory> inventorys=JBoltExcelUtil.readModels(jBoltExcel,1, Inventory.class,errorMsg);
		if(notOk(inventorys)) {
			if(errorMsg.length()>0) {
				return fail(errorMsg.toString());
			}else {
				return fail(JBoltMsg.DATA_IMPORT_FAIL_EMPTY);
			}
		}
		//执行批量操作
		boolean success=tx(new IAtom() {
			@Override
			public boolean run() throws SQLException {
				batchSave(inventorys);
				return true;
			}
		});

		if(!success) {
			return fail(JBoltMsg.DATA_IMPORT_FAIL);
		}
		return SUCCESS;
	}

	/**
	 * 生成要导出的Excel
	 * @return
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
						JBoltExcelHeader.create("cInvCode","料品编码",15),
						JBoltExcelHeader.create("cInvName","料品名称",15),
						JBoltExcelHeader.create("cInvCode1","客户部番",15),
						JBoltExcelHeader.create("cInvAddCode1","UG部番",15),
						JBoltExcelHeader.create("cInvName1","部品名称",15),
						JBoltExcelHeader.create("cInvName2","UG部品名称",15)
					)
					//设置导出的数据源 来自于数据库查询出来的Model List
					.setRecordDatas(2,datas)
			);
	}

	/**
	 * 复制
	 * @param inventory
	 * @return
	 */
	public Ret copy(Inventory inventory) {
		inventory.setIPid(inventory.getIAutoId());
		inventory.setIAutoId(null);
		setInventory(inventory);
		boolean success = inventory.save();
		if(!success) {
			return fail(JBoltMsg.FAIL);
		}
		return SUCCESS;
	}

	/**
	 * 设置参数
	 * @param inventory
	 * @return
	 */
	private Inventory setInventory(Inventory inventory){
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
		return dbTemplate("inventoryclass.inventoryList",kv).find();
	}

	public List<Record> getAutocompleteData(String q, Integer limit) {
		return dbTemplate("inventory.getAutocompleteData",Kv.by("q", q).set("limit",limit)).find();
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
		return  itempicture ;
	}

	public Ret updateForm(Inventory inventory, InventoryAddition inventoryAddition, InventoryPlan inventoryPlan, InventoryMfgInfo inventoryMfgInfo, InventoryStockConfig inventorystockconfig, List<InventoryWorkRegion> inventoryWorkRegions, List<InventoryWorkRegion> newInventoryWorkRegions, Object[] delete) {
		AtomicReference<Ret> res = new AtomicReference<>();
		res.set(SUCCESS);
		tx(() -> {
			Ret inventoryRet = update(inventory);
			inventoryAddition.setIInventoryId(inventory.getIAutoId());
			Ret additionRet = inventoryAdditionService.update(inventoryAddition);
			inventoryPlan.setIInventoryId(inventory.getIAutoId());
			Ret planRet = inventoryPlanService.update(inventoryPlan);
			inventoryMfgInfo.setIInventoryId(inventory.getIAutoId());
			Ret mfgInfoRet = inventoryMfgInfoService.update(inventoryMfgInfo);
			inventorystockconfig.setIInventoryId(inventory.getIAutoId());
			Ret stockRet = inventoryStockConfigService.update(inventorystockconfig);
			if(inventoryWorkRegions != null && inventoryWorkRegions.size() > 0){
				for (InventoryWorkRegion workRegion : inventoryWorkRegions) {
					workRegion.setIInventoryId(inventory.getIAutoId());
				}
				int[] ints = inventoryWorkRegionService.batchUpdate(inventoryWorkRegions);
			}
			if(newInventoryWorkRegions != null && newInventoryWorkRegions.size() >0){
				for (InventoryWorkRegion workRegion : newInventoryWorkRegions) {
					workRegion.setIInventoryId(inventory.getIAutoId());
					workRegion.setIsDeleted(false);
				}
				inventoryWorkRegionService.batchSave(newInventoryWorkRegions);
			}
			// 删除
			if (ArrayUtil.isNotEmpty(delete)) {
				deleteMultiByIds(delete);
			}
			return true;
		});

		return res.get();
	}

	public void deleteMultiByIds(Object[] deletes) {
		delete("DELETE FROM Bd_InventoryWorkRegion WHERE iAutoId IN (" + ArrayUtil.join(deletes, ",") + ") ");
	}

	public Ret saveForm(Inventory inventory, InventoryAddition inventoryAddition, InventoryPlan inventoryPlan, InventoryMfgInfo inventoryMfgInfo, InventoryStockConfig inventorystockconfig, List<InventoryWorkRegion> inventoryWorkRegions) {
		AtomicReference<Ret> res = new AtomicReference<>();
		res.set(SUCCESS);
		tx(() -> {
			Ret inventoryRet = save(inventory);
			if(inventoryRet.isFail()){
				res.set(inventoryRet);
				return false;
			}
			inventoryAddition.setIInventoryId(inventory.getIAutoId());
			Ret additionRet = inventoryAdditionService.save(inventoryAddition);
			if(additionRet.isFail()){
				res.set(additionRet);
				return false;
			}
			inventoryPlan.setIInventoryId(inventory.getIAutoId());
			Ret planRet = inventoryPlanService.save(inventoryPlan);
			if(planRet.isFail()){
				res.set(planRet);
				return false;
			}
			inventoryMfgInfo.setIInventoryId(inventory.getIAutoId());
			Ret mfgInfoRet = inventoryMfgInfoService.save(inventoryMfgInfo);
			if(mfgInfoRet.isFail()){
				res.set(mfgInfoRet);
				return false;
			}
			inventorystockconfig.setIInventoryId(inventory.getIAutoId());
			Ret stockRet = inventoryStockConfigService.save(inventorystockconfig);
			if(stockRet.isFail()){
				res.set(stockRet);
				return false;
			}
			if(inventoryWorkRegions != null && inventoryWorkRegions.size() > 0){
				for (InventoryWorkRegion workRegion : inventoryWorkRegions) {
					workRegion.setIInventoryId(inventory.getIAutoId());
					workRegion.setIsDeleted(false);
				}
				int[] ints = inventoryWorkRegionService.batchSave(inventoryWorkRegions);
				if(ints.length != inventoryWorkRegions.size())
				{
					res.set(Ret.fail("产线信息异常!"));
					return false;
				}
			}
			return true;
		});

		return res.get();
	}

	public List<Record> options(Kv kv) {
		List<Record> inventories = dbTemplate("inventory.options").find();
		if(inventories != null ){
			Long inventoryId = kv.getLong("inventoryid");
			if(inventoryId == null)
				return inventories;
			if(inventories.size() > 0){
				for (Record i : inventories) {
					if(i.getLong("iautoid").longValue() == inventoryId.longValue()){
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
			if(StringUtils.isBlank(cInvName1))
				return inventories;
			inventory.put("iautoid",inventoryId);
			inventory.put("cinvname1",cInvName1);
			inventory.put("cinvcode",cInvCode);
			inventory.put("cinvcode1",cInvCode1);
			inventory.put("cinvstd",cInvStd);
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
	 * @param kv
	 * @return
	 */
	public List<Record> inventorySpotCheckList(Kv kv) {
        return dbTemplate("inventoryclass.inventorySpotCheckList", kv).find();
	}
	
	/**
	 * 根据客户部番或UG部番 查找存货
	 * @param cInvAddCode
	 */
	public Inventory findBycInvAddCode(String cInvAddCode){
		return findFirst("SELECT * FROM Bd_Inventory WHERE cInvCode1 = ? OR cInvAddCode1 = ?", cInvAddCode, cInvAddCode);
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
    
}

