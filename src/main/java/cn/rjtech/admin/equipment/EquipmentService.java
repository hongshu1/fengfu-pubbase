package cn.rjtech.admin.equipment;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.poi.excel.JBoltExcel;
import cn.jbolt.core.poi.excel.JBoltExcelHeader;
import cn.jbolt.core.poi.excel.JBoltExcelSheet;
import cn.jbolt.core.poi.excel.JBoltExcelUtil;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.workregionm.WorkregionmService;
import cn.rjtech.model.momdata.Equipment;
import cn.rjtech.model.momdata.Workregionm;
import cn.rjtech.util.BillNoUtils;
import cn.rjtech.util.ValidationUtils;
import cn.rjtech.wms.utils.StringUtils;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.io.File;
import java.util.*;

/**
 * 设备管理-设备档案
 * @ClassName: EquipmentService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-21 11:32
 */
public class EquipmentService extends BaseService<Equipment> {
	private final Equipment dao=new Equipment().dao();
	@Override
	protected Equipment dao() {
		return dao;
	}

	@Inject
	private WorkregionmService workregionmService;
	@Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

    /**
     * 后台管理数据查询
     *
     * @param pageNumber 第几页
     * @param pageSize   每页几条数据
     */
	public Page<Record> getAdminDatas(int pageNumber, int pageSize, Kv kv) {
		return dbTemplate("equipment.selectEquipments",kv).paginate(pageNumber,pageSize);
	}

	/**
	 * 后台管理数据查询
	 */
	public List<Record> getAdminDataNoPage( Kv kv) {
		return dbTemplate("equipment.selectEquipments",kv).find();
	}

	/**
	 * 后台管理数据查询
	 * @param pageNumber 第几页
	 * @param pageSize   每页几条数据
	 */
	public Page<Record> getAdminDataResModels(int pageNumber, int pageSize, Kv kv) {
		return dbTemplate("equipment.selectEquipments",kv).paginate(pageNumber,pageSize);
	}

	/**
	 * 保存
	 */
	public Ret save(Equipment equipment) {
		if(equipment==null || isOk(equipment.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		if (StrUtil.isBlank(equipment.getCEquipmentCode())) {
			equipment.setCEquipmentCode(BillNoUtils.genCode(getOrgCode(), table()));
		}
		setEquipment(equipment);
		//if(existsName(equipment.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=equipment.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(equipment.getIAutoId(), JBoltUserKit.getUserId(), equipment.getName());
		}
		return ret(success);
	}

	/**
	 * 设置参数
	 */
	private Equipment setEquipment(Equipment equipment){
		equipment.setIsDeleted(false);
		equipment.setIOrgId(getOrgId());
		equipment.setCOrgCode(getOrgCode());
		equipment.setCOrgName(getOrgName());
		Long userId = JBoltUserKit.getUserId();
		equipment.setICreateBy(userId);
		equipment.setIUpdateBy(userId);
		String userName = JBoltUserKit.getUserName();
		equipment.setCCreateName(userName);
		equipment.setCUpdateName(userName);
		Date date = new Date();
		equipment.setDCreateTime(date);
		equipment.setDUpdateTime(date);
		return equipment;
	}

	/**
	 * 更新
	 */
	public Ret update(Equipment equipment) {
		if(equipment==null || notOk(equipment.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		Equipment dbEquipment=findById(equipment.getIAutoId());
		if (StrUtil.isBlank(equipment.getCEquipmentCode())) {
			equipment.setCEquipmentCode(BillNoUtils.genCode(getOrgCode(), table()));
		}
		if(dbEquipment==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(equipment.getName(), equipment.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=equipment.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(equipment.getIAutoId(), JBoltUserKit.getUserId(), equipment.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param equipment 要删除的model
	 * @param kv 携带额外参数一般用不上
	 */
	@Override
	protected String afterDelete(Equipment equipment, Kv kv) {
		//addDeleteSystemLog(equipment.getIAutoId(), JBoltUserKit.getUserId(),equipment.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param equipment model
	 * @param kv 携带额外参数一般用不上
	 */
	@Override
	public String checkInUse(Equipment equipment, Kv kv) {
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
                        .setHeaders(1,false,
                                JBoltExcelHeader.create("设备编码",15),
                                JBoltExcelHeader.create("设备名称",15),
                                JBoltExcelHeader.create("产线ID",15),
                                JBoltExcelHeader.create("是否导电咀更换",15),
                                JBoltExcelHeader.create("状态",15),
                                JBoltExcelHeader.create("备注",20)
                                )
                    );
    }

    /**
	 * 读取excel文件
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
                        JBoltExcelHeader.create("cEquipmentCode","设备编码"),
                        JBoltExcelHeader.create("cEquipmentName","设备名称"),
                        JBoltExcelHeader.create("iWorkRegionmId","产线ID"),
                        JBoltExcelHeader.create("isNozzleSwitchEnabled","是否导电咀更换"),
                        JBoltExcelHeader.create("iStatus","状态"),
                        JBoltExcelHeader.create("cMemo","备注")
                        )
				//从第三行开始读取
				.setDataStartRow(2)
				);
		//从指定的sheet工作表里读取数据
		List<Equipment> equipments=JBoltExcelUtil.readModels(jBoltExcel,1, Equipment.class,errorMsg);
		if(notOk(equipments)) {
			if(errorMsg.length()>0) {
				return fail(errorMsg.toString());
			}else {
				return fail(JBoltMsg.DATA_IMPORT_FAIL_EMPTY);
			}
		}
		for (Equipment equipment : equipments) {
			setEquipment(equipment);
			equipment.setIsEnabled(true);
		}
		//执行批量操作
		boolean success=tx(() -> {
            batchSave(equipments);
            return true;
        });

		if(!success) {
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
                            JBoltExcelHeader.create("cequipmentcode","设备编码",15),
                            JBoltExcelHeader.create("cequipmentname","设备名称",15),
                            JBoltExcelHeader.create("workname","所属产线",15),
                            JBoltExcelHeader.create("isnozzleswitchenabled","是否导电咀更换",15),
                            JBoltExcelHeader.create("statename","状态",15),
                            JBoltExcelHeader.create("ccreatename","创建人名称",15),
                            JBoltExcelHeader.create("dcreatetime","创建时间",20)
                            ).setDataChangeHandler((data, index) -> {
								//设置数据转换处理器
                             data.changeBooleanToStr("isnozzleswitchenabled", "是", "否");
							})
    		    	//设置导出的数据源 来自于数据库查询出来的Model List
    		    	.setRecordDatas(2,datas)
    		    );
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(Equipment equipment, String column, Kv kv) {
		//addUpdateSystemLog(equipment.getIAutoId(), JBoltUserKit.getUserId(), equipment.getName(),"的字段["+column+"]值:"+equipment.get(column));
		/*
		switch(column){
		    case "isNozzleSwitchEnabled":
		        break;
		    case "isEnabled":
		        break;
		}
		*/
		return null;
	}

	public List<Record> selectWorkRegs() {
		return dbTemplate("equipment.selectWorkRegs").find();
	}

    /**
     * 根据设备编码查询
     */
	public Equipment findModelByCode(String cequipmentcode) {
		return findFirst(selectSql().eq("cequipmentcode", cequipmentcode));
	}

	public List<Record> getAutocompleteDatas(String q, Integer limit) {
		return dbTemplate("equipment.getAutocompleteDatas",Kv.by("q", q).set("limit",limit)).find();
	}

	public List<Record> dataList() {
		return dbTemplate("equipment.selectEquipments").find();
	}

	/**
	 * 从系统导入字段配置，获得导入的数据
	 */
	public Ret importExcelClass(File file) {
		StringBuilder errorMsg = new StringBuilder();

		JBoltExcel excel = JBoltExcel
				// 从excel文件创建JBoltExcel实例
				.from(file)
				// 设置工作表信息
				.setSheets(
						JBoltExcelSheet.create("equipment")
								// 设置列映射 顺序 标题名称
								.setHeaders(2,
										JBoltExcelHeader.create("cequipmentcode", "设备档案"),
										JBoltExcelHeader.create("cequipmentname", "设备名称"),
										JBoltExcelHeader.create("iworkregionm_name", "产线名称"),
										JBoltExcelHeader.create("isnozzleswitchenabled", "是否导电咀更换"),
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

		List<Equipment> addList=new ArrayList<>();

		//List<Record> records = cusFieldsMappingDService.getImportRecordsByTableName(file, table());
		if (notOk(records)) {
			return fail(JBoltMsg.DATA_IMPORT_FAIL_EMPTY);
		}

        Date now = new Date();
        
		// 产线
		Map<String, Workregionm> workregionmMap = new HashMap<>();
		for (Record record : records) {

			if (StrUtil.isBlank(record.getStr("cequipmentcode"))) {
				record.set("cequipmentcode",BillNoUtils.genCode(getOrgCode(), table()));
			}
			if (StrUtil.isBlank(record.getStr("cequipmentname"))) {
				return fail("设备名称不能为空");
			}
			if (StrUtil.isBlank(record.getStr("iworkregionm_name"))) {
				return fail("产线名称不能为空");
			}
			if (StrUtil.isBlank(record.getStr("isnozzleswitchenabled"))) {
				return fail("是否导电咀更换不能为空");
			}

			String cmemo = record.getStr("cmemo");
			if(StringUtils.isNotBlank(cmemo)){
				record.set("cmemo", cmemo);
			}

			String iworkregionm_name = record.getStr("iworkregionm_name");
			Workregionm workregionm = workregionmService.findFirstByWorkName(iworkregionm_name);
			ValidationUtils.notNull(workregionm, String.format("产线“%s”不存在", iworkregionm_name));
			record.set("iWorkRegionmId",workregionm.getIAutoId());

			record.set("iAutoId", JBoltSnowflakeKit.me.nextId());
			record.set("iOrgId", getOrgId());
			record.set("cOrgCode", getOrgCode());
			record.set("cOrgName", getOrgName());
			record.set("iStatus",1);
			record.set("isEnabled",1);
			record.set("iCreateBy", JBoltUserKit.getUserId());
			record.set("dCreateTime", now);
			record.set("cCreateName", JBoltUserKit.getUserName());
			record.set("isDeleted",0);
			record.set("iUpdateBy", JBoltUserKit.getUserId());
			record.set("dUpdateTime", now);
			record.set("cUpdateName", JBoltUserKit.getUserName());
			record.set("isNozzleSwitchEnabled", "是".equals(record.getStr("isNozzleSwitchEnabled"))? 1 : 0);

			Equipment equipment=new Equipment();
			equipment.put(record);
			addList.add(equipment);
		}

		// 执行批量操作
		tx(() -> {
			for (Equipment equipment : addList) {
				equipment.save();
			}
			return true;
		});
		return SUCCESS;
	}
    
}