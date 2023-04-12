package cn.rjtech.admin.equipment;

import cn.jbolt.core.kit.JBoltUserKit;
import com.jfinal.plugin.activerecord.Page;

import java.util.Date;
import java.util.List;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import cn.jbolt.core.base.JBoltMsg;
import java.io.File;
import com.jfinal.plugin.activerecord.IAtom;
import java.sql.SQLException;
import cn.jbolt.core.poi.excel.*;
import cn.rjtech.model.momdata.Equipment;
import com.jfinal.plugin.activerecord.Record;

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

	@Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

	/**
	 * 后台管理数据查询
	 * @param pageNumber 第几页
	 * @param pageSize   每页几条数据
	 * @return
	 */
	public Page<Record> getAdminDatas(int pageNumber, int pageSize, Kv kv) {

		return dbTemplate("equipment.selectEquipments",kv).paginate(pageNumber,pageSize);

	}

	/**
	 * 后台管理数据查询
	 * @return
	 */
	public List<Record> getAdminDataNoPage( Kv kv) {

		return dbTemplate("equipment.selectEquipments",kv).find();

	}

	/**
	 * 后台管理数据查询
	 * @param pageNumber 第几页
	 * @param pageSize   每页几条数据
	 * @return
	 */
	public Page<Record> getAdminDataResModels(int pageNumber, int pageSize, Kv kv) {

		return dbTemplate("equipment.selectEquipments",kv).paginate(pageNumber,pageSize);

	}

	/**
	 * 保存
	 * @param equipment
	 * @return
	 */
	public Ret save(Equipment equipment) {
		if(equipment==null || isOk(equipment.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
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
	 * @param equipment
	 * @return
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
	 * @param equipment
	 * @return
	 */
	public Ret update(Equipment equipment) {
		if(equipment==null || notOk(equipment.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		Equipment dbEquipment=findById(equipment.getIAutoId());
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
	 * @return
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
	 * @return
	 */
	@Override
	public String checkInUse(Equipment equipment, Kv kv) {
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
		boolean success=tx(new IAtom() {
			@Override
			public boolean run() throws SQLException {
				batchSave(equipments);
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
                            JBoltExcelHeader.create("cequipmentcode","设备编码",15),
                            JBoltExcelHeader.create("cequipmentname","设备名称",15),
                            JBoltExcelHeader.create("workname","所属产线",15),
                            JBoltExcelHeader.create("isnozzleswitchenabled","是否导电咀更换",15),
                            JBoltExcelHeader.create("statename","状态",15),
                            JBoltExcelHeader.create("ccreatename","创建人名称",15),
                            JBoltExcelHeader.create("dcreatetime","创建时间",15)
                            )
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
		/**
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
	 * */
	public Equipment findModelByCode(String cequipmentcode) {
		return findFirst(selectSql().eq("cequipmentcode", cequipmentcode));
	}

	public List<Record> getAutocompleteDatas(String q, Integer limit) {
		return dbTemplate("equipment.getAutocompleteDatas",Kv.by("q", q).set("limit",limit)).find();
	}

	public List<Record> dataList() {
		return dbTemplate("equipment.selectEquipments").find();
	}
    
}