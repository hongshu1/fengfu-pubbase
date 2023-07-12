package cn.rjtech.admin.equipmentmodel;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.poi.excel.JBoltExcel;
import cn.jbolt.core.poi.excel.JBoltExcelHeader;
import cn.jbolt.core.poi.excel.JBoltExcelSheet;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.cache.CusFieldsMappingdCache;
import cn.rjtech.enums.IsOkEnum;
import cn.rjtech.model.momdata.EquipmentModel;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.io.File;
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

    /**
     * 后台管理数据查询
     *
     * @param pageNumber 第几页
     * @param pageSize   每页几条数据
     */
	public Page<Record> getAdminDatas(int pageNumber, int pageSize, Kv kv) {
	    return dbTemplate("equipment_model.selectEquipmentModels",kv).paginate(pageNumber,pageSize);
	}

	/**
	 * 保存
	 */
	public Ret save(EquipmentModel equipmentModel) {
		if(equipmentModel==null || isOk(equipmentModel.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}

        long userId = JBoltUserKit.getUserId();
        String userName = JBoltUserKit.getUserName();
        Date now = new Date();
		
		setEquipmentModel(equipmentModel, userId, userName, now);
		// 校验机型编码唯一
		if(exists(createSql(equipmentModel))) {
			return fail(JBoltMsg.DATA_SAME_SN_EXIST);
		}
		boolean success=equipmentModel.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(equipmentModel.getIAutoId(), JBoltUserKit.getUserId(), equipmentModel.getName());
		}
		return ret(success);
	}

	private Sql createSql(EquipmentModel equipmentModel){
		Sql sql = selectSql().eq(EquipmentModel.ISDELETED, IsOkEnum.NO.getValue())
				.eq(EquipmentModel.IORGID, equipmentModel.getIOrgId())
				.eq(EquipmentModel.CEQUIPMENTMODELCODE, equipmentModel.getCEquipmentModelCode());
		if (ObjUtil.isNotNull(equipmentModel.getIAutoId())){
			sql.notEq(EquipmentModel.IAUTOID, equipmentModel.getIAutoId());
		}
		return sql;
	}
	
	/**
	 * 设置参数
	 */
    private void setEquipmentModel(EquipmentModel equipmentModel, long userId, String userName, Date now) {
		ValidationUtils.notBlank(equipmentModel.getCEquipmentModelCode(), "机型编码不能为空");
        
		equipmentModel.setIsDeleted(false);
		equipmentModel.setIOrgId(getOrgId());
		equipmentModel.setCOrgCode(getOrgCode());
		equipmentModel.setCOrgName(getOrgName());
		equipmentModel.setICreateBy(userId);
		equipmentModel.setIUpdateBy(userId);
		equipmentModel.setCCreateName(userName);
		equipmentModel.setCUpdateName(userName);
		equipmentModel.setDCreateTime(now);
		equipmentModel.setDUpdateTime(now);
	}
    
//	/**
//	 * 设置参数
//	 */
//    private void setEquipmentModel(Record equipmentModel, long userId, String userName, Date now) {
//		ValidationUtils.notBlank(equipmentModel.getStr("CEquipmentModelCode"), "机型编码不能为空");
//        
//		equipmentModel.set("IsDeleted", ZERO_STR);
//		equipmentModel.set("IOrgId", getOrgId());
//		equipmentModel.set("COrgCode", getOrgCode());
//		equipmentModel.set("COrgName", getOrgName());
//		equipmentModel.set("ICreateBy", userId);
//		equipmentModel.set("IUpdateBy", userId);
//		equipmentModel.set("CCreateName", userName);
//		equipmentModel.set("CUpdateName", userName);
//		equipmentModel.set("DCreateTime", now);
//		equipmentModel.set("DUpdateTime", now);
//	}

	/**
	 * 更新
	 */
	public Ret update(EquipmentModel equipmentModel) {
		if(equipmentModel==null || notOk(equipmentModel.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		EquipmentModel dbEquipmentModel=findById(equipmentModel.getIAutoId());
		if(dbEquipmentModel==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		// 校验机型编码唯一
		if(exists(createSql(equipmentModel))) {
			return fail(JBoltMsg.DATA_SAME_SN_EXIST);
		}
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
	 */
	@Override
	public String checkInUse(EquipmentModel equipmentModel, Kv kv) {
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
                                JBoltExcelHeader.create("机型编码",15),
                                JBoltExcelHeader.create("机型名称",15),
                                JBoltExcelHeader.create("备注",15)
                                )
                    );
    }


    /**
	 * 读取excel文件
	 */
	public Ret importExcel(File file) {
		// 使用字段配置维护
		List<Record> importData =  CusFieldsMappingdCache.ME.getImportRecordsByTableName(file, table());
        ValidationUtils.notEmpty(importData, "导入数据不能为空");

        long userId = JBoltUserKit.getUserId();
        String userName = JBoltUserKit.getUserName();
        Date now = new Date();
        
		for (Record row : importData) {

            EquipmentModel equipmentModel = new EquipmentModel();
            
			setEquipmentModel(equipmentModel, userId, userName, now);
            
			// 校验机型编码唯一
			if(exists(createSql(equipmentModel))) {
				String cEquipmentModelCode = equipmentModel.getCEquipmentModelCode();
				String format = String.format("机型编码【%s】%s", cEquipmentModelCode, JBoltMsg.DATA_SAME_SN_EXIST);
				return fail(format);
			}
		}
        
		//执行批量操作
		boolean success=tx(() -> {
            batchSaveRecords(importData);
            return true;
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

			if (StrUtil.isBlank(record.getStr("cEquipmentModelCode"))) {
				return fail("机型编码不能为空");
			}
			if (StrUtil.isBlank(record.getStr("cEquipmentModelName"))) {
				return fail("机型名称不能为空");
			}

			record.set("iAutoId", JBoltSnowflakeKit.me.nextId());
			record.set("iCreateBy", JBoltUserKit.getUserId());
			record.set("cOrgCode",getOrgCode());
			record.set("cOrgName",getOrgName());
			record.set("iOrgId",getOrgId());
			record.set("dCreateTime", now);
			record.set("cCreateName", JBoltUserKit.getUserName());
			record.set("isDeleted", ZERO_STR);
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

}
