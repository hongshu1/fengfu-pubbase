package cn.rjtech.admin.equipmentmodel;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.poi.excel.JBoltExcel;
import cn.jbolt.core.poi.excel.JBoltExcelHeader;
import cn.jbolt.core.poi.excel.JBoltExcelSheet;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.cusfieldsmappingd.CusFieldsMappingDService;
import cn.rjtech.model.momdata.EquipmentModel;
import com.alibaba.fastjson.JSON;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.io.File;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * 物料建模-机型档案
 * @ClassName: EquipmentModelService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-22 11:17
 */
public class EquipmentModelService extends BaseService<EquipmentModel> {
	private final EquipmentModel dao=new EquipmentModel().dao();
	@Override
	protected EquipmentModel dao() {
		return dao;
	}

	@Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }
	@Inject
	private CusFieldsMappingDService cusFieldsMappingdService;
	/**
	 * 后台管理数据查询
	 * @param pageNumber 第几页
	 * @param pageSize   每页几条数据
	 * @return
	 */
	public Page<Record> getAdminDatas(int pageNumber, int pageSize, Kv kv) {
	    return dbTemplate("equipment_model.selectEquipmentModels",kv).paginate(pageNumber,pageSize);
	}

	/**
	 * 保存
	 * @param equipmentModel
	 * @return
	 */
	public Ret save(EquipmentModel equipmentModel) {
		if(equipmentModel==null || isOk(equipmentModel.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		setEquipmentModel(equipmentModel);
		//if(existsName(equipmentModel.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=equipmentModel.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(equipmentModel.getIAutoId(), JBoltUserKit.getUserId(), equipmentModel.getName());
		}
		return ret(success);
	}

	/**
	 * 设置参数
	 * @param equipmentModel
	 * @return
	 */
	private EquipmentModel setEquipmentModel(EquipmentModel equipmentModel){
		equipmentModel.setIsDeleted(false);
		equipmentModel.setIOrgId(getOrgId());
		equipmentModel.setCOrgCode(getOrgCode());
		equipmentModel.setCOrgName(getOrgName());
		Long userId = JBoltUserKit.getUserId();
		equipmentModel.setICreateBy(userId);
		equipmentModel.setIUpdateBy(userId);
		String userName = JBoltUserKit.getUserName();
		equipmentModel.setCCreateName(userName);
		equipmentModel.setCUpdateName(userName);
		Date date = new Date();
		equipmentModel.setDCreateTime(date);
		equipmentModel.setDUpdateTime(date);
		return equipmentModel;
	}

	/**
	 * 更新
	 * @param equipmentModel
	 * @return
	 */
	public Ret update(EquipmentModel equipmentModel) {
		if(equipmentModel==null || notOk(equipmentModel.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		EquipmentModel dbEquipmentModel=findById(equipmentModel.getIAutoId());
		if(dbEquipmentModel==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(equipmentModel.getName(), equipmentModel.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=equipmentModel.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(equipmentModel.getIAutoId(), JBoltUserKit.getUserId(), equipmentModel.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param equipmentModel 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(EquipmentModel equipmentModel, Kv kv) {
		//addDeleteSystemLog(equipmentModel.getIAutoId(), JBoltUserKit.getUserId(),equipmentModel.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param equipmentModel model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(EquipmentModel equipmentModel, Kv kv) {
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
                                JBoltExcelHeader.create("机型编码",15),
                                JBoltExcelHeader.create("机型名称",15),
                                JBoltExcelHeader.create("备注",15)
                                )
                    );
    }


    /**
	 * 读取excel文件
	 * @param file
	 * @return
	 */
	public Ret importExcel(File file, String cformatName) {
		StringBuilder errorMsg=new StringBuilder();
		//使用字段配置维护
		Object importData =  cusFieldsMappingdService.getImportDatas(file, cformatName).get("data");
//		String docInfoRelaStrings= JSON.toJSONStringWithDateFormat(importData,"HH:mm");
		String docInfoRelaStrings= JSON.toJSONString(importData);
		List<EquipmentModel> equipmentModels = JSON.parseArray(docInfoRelaStrings, EquipmentModel.class);
		if(notOk(equipmentModels)) {
			if(errorMsg.length()>0) {
				return fail(errorMsg.toString());
			}else {
				return fail(JBoltMsg.DATA_IMPORT_FAIL_EMPTY);
			}
		}
		for (EquipmentModel equipmentModel : equipmentModels) {
			setEquipmentModel(equipmentModel);
		}
		//执行批量操作
		boolean success=tx(new IAtom() {
			@Override
			public boolean run() throws SQLException {
				batchSave(equipmentModels);
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
                            JBoltExcelHeader.create("cequipmentmodelcode","机型编码",15),
                            JBoltExcelHeader.create("cequipmentmodelname","机型名称",15),
                            JBoltExcelHeader.create("cmemo","备注",15),
                            JBoltExcelHeader.create("ccreatename","创建人名称",15),
                            JBoltExcelHeader.create("dcreatetime","创建时间",15)
                            )
    		    	//设置导出的数据源 来自于数据库查询出来的Model List
    		    	.setRecordDatas(2,datas)
    		    );
	}

	public List<Record> getAdminDataNoPage(Kv kv) {
		return dbTemplate("equipment_model.selectEquipmentModels",kv).find() ;
	}

	public List<Record> options() {
		return dbTemplate("equipment_model.selectEquipmentModels").find();
	}
	
	public EquipmentModel findByName(String equipmentModelName){
		return findFirst("select * from Bd_EquipmentModel where isDeleted = 0 and cequipmentmodelname = ?", equipmentModelName);
	}
}
