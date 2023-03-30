package cn.rjtech.admin.warehouse;

import cn.hutool.core.date.DateUtil;
import cn.jbolt._admin.dept.DeptService;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.Dept;
import cn.jbolt.core.poi.excel.JBoltExcel;
import cn.jbolt.core.poi.excel.JBoltExcelHeader;
import cn.jbolt.core.poi.excel.JBoltExcelSheet;
import cn.jbolt.core.poi.excel.JBoltExcelUtil;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.rjtech.model.momdata.Warehouse;
import com.jfinal.plugin.activerecord.Record;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * 仓库建模-仓库档案
 * @ClassName: WarehouseService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-20 16:47
 */
public class WarehouseService extends BaseService<Warehouse> {
	private final Warehouse dao=new Warehouse().dao();
	@Override
	protected Warehouse dao() {
		return dao;
	}

	/**
	 * 设置返回二开业务所属的关键systemLog的targetType
	 */
	@Override
	protected int systemLogTargetType() {
		return ProjectSystemLogTargetType.NONE.getValue();
	}


	@Inject
	private DeptService deptService;
	/**
	 * 后台管理数据查询
	 * @param pageNumber 第几页
	 * @param pageSize   每页几条数据
	 * @param keywords   关键词
     * @param bFreeze 是否冻结
     * @param bMRP 是否参与MRP运算
     * @param bShop 是否门店
     * @param bInCost 记入成本
     * @param bInAvailCalcu 纳入可用量计算
     * @param bProxyWh 代管仓
     * @param bBondedWh 是否保税仓
     * @param bWhAsset 资产仓
     * @param bCheckSubitemCost 是否核算分项成本
     * @param bEB 电商仓
     * @param isDeleted 删除状态: 0. 未删除 1. 已删除
	 * @return
	 */
	public Page<Warehouse> getAdminDatas(int pageNumber, int pageSize, String keywords, Boolean bFreeze, Boolean bMRP, Boolean bShop , Boolean bInCost, Boolean bInAvailCalcu, Boolean bProxyWh, Boolean bBondedWh, Boolean bWhAsset, Boolean bCheckSubitemCost, Boolean bEB, Boolean isDeleted) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
	    //sql条件处理
        sql.eqBooleanToChar("bFreeze",bFreeze);
        sql.eqBooleanToChar("bMRP",bMRP);
        sql.eqBooleanToChar("bShop",bShop);
        sql.eqBooleanToChar("bInCost",bInCost);
        sql.eqBooleanToChar("bInAvailCalcu",bInAvailCalcu);
        sql.eqBooleanToChar("bProxyWh",bProxyWh);
        sql.eqBooleanToChar("bBondedWh",bBondedWh);
        sql.eqBooleanToChar("bWhAsset",bWhAsset);
        sql.eqBooleanToChar("bCheckSubitemCost",bCheckSubitemCost);
        sql.eqBooleanToChar("bEB",bEB);
        sql.eqBooleanToChar("isDeleted",isDeleted);
        //关键词模糊查询
        sql.likeMulti(keywords,"cOrgName", "cWhName");
        //排序
        sql.desc("iAutoId");
		return paginate(sql);
	}

	/**
	 * 保存
	 * @param warehouse
	 * @return
	 */
	public Ret save(Warehouse warehouse) {
		if(warehouse==null || isOk(warehouse.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
//		if(existsName(warehouse.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}

        //查重
        ValidationUtils.assertNull(findByWhCode(warehouse.getCWhCode()), "仓库编码重复！");

		//创建信息
		warehouse.setIcreateby(JBoltUserKit.getUserId());
		warehouse.setCcreatename(JBoltUserKit.getUserName());
		warehouse.setDCreateTime(new Date());

		//更新信息
		warehouse.setIupdateby(JBoltUserKit.getUserId());
		warehouse.setCupdatename(JBoltUserKit.getUserName());
		warehouse.setDupdatetime(new Date());

		warehouse.setCOrgCode(getOrgCode());
		warehouse.setCOrgName(getOrgName());
		warehouse.setIOrgId(getOrgId());
		//TODO 来源1.MES 2.U8  现默认U8
		warehouse.setISource(2);
		//默认未删除
		warehouse.setIsDeleted(false);
		boolean success=warehouse.save();

		if(success) {
			//添加日志
			//addSaveSystemLog(warehouse.getIAutoId(), JBoltUserKit.getUserId(), warehouse.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param warehouse
	 * @return
	 */
	public Ret update(Warehouse warehouse) {
		if(warehouse==null || notOk(warehouse.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		Warehouse dbWarehouse=findById(warehouse.getIAutoId());
		if(dbWarehouse==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}

        //查询编码是否存在
        Warehouse ware = findByWhCode(warehouse.getCWhCode());

        //编码重复判断
        if (ware != null && !warehouse.getIAutoId().equals(ware.getIAutoId())) {
            ValidationUtils.assertNull(ware.getCWhCode(), "仓库编码重复！");
        }

		//if(existsName(warehouse.getName(), warehouse.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=warehouse.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(warehouse.getIAutoId(), JBoltUserKit.getUserId(), warehouse.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param warehouse 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(Warehouse warehouse, Kv kv) {
		//addDeleteSystemLog(warehouse.getIAutoId(), JBoltUserKit.getUserId(),warehouse.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param warehouse model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(Warehouse warehouse, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(Warehouse warehouse, String column, Kv kv) {
		//addUpdateSystemLog(warehouse.getIAutoId(), JBoltUserKit.getUserId(), warehouse.getName(),"的字段["+column+"]值:"+warehouse.get(column));
		/**
		switch(column){
		    case "bFreeze":
		        break;
		    case "bMRP":
		        break;
		    case "bShop":
		        break;
		    case "bInCost":
		        break;
		    case "bInAvailCalcu":
		        break;
		    case "bProxyWh":
		        break;
		    case "bBondedWh":
		        break;
		    case "bWhAsset":
		        break;
		    case "bCheckSubitemCost":
		        break;
		    case "bEB":
		        break;
		    case "isDeleted":
		        break;
		}
		*/
		return null;
	}

	//查询有没有这个仓库编码
	public Warehouse findByWhCode(String whcode){
		return findFirst(Okv.by("cWhCode", whcode).set("isDeleted", false), "iautoid", "asc");
	}

	/**
	 * 后台管理分页查询
	 *
	 * @param pageNumber
	 * @param pageSize
	 * @param kv
	 * @return
	 */
	public Page<Record> paginateAdminDatas(int pageNumber, int pageSize, Kv kv) {
		return dbTemplate("warehouse.paginateAdminDatas", kv).paginate(pageNumber, pageSize);
	}

    public Object dataList() {
        Okv kv=Okv.create();
        kv.set("isDeleted",false);
        return getCommonList(kv,"iAutoId","asc");
    }

    public List<Record> list(Kv kv) {
        return dbTemplate("warehouse.paginateAdminDatas", kv).find();
    }


	/**
	 * 仓库档案导入
	 * @param file
	 * @return
	 */
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
										JBoltExcelHeader.create("cwhcode","仓库编码"),
										JBoltExcelHeader.create("cwhname","仓库名称"),
										JBoltExcelHeader.create("cDepCode","所属部门"),
										JBoltExcelHeader.create("cWhAddress","地址"),
										JBoltExcelHeader.create("cWhPerson","负责人"),
										JBoltExcelHeader.create("iMaxStock","最大存储数量"),
										JBoltExcelHeader.create("iMaxSpace","最大存储空间"),
										JBoltExcelHeader.create("isSpaceControlEnabled","是否启用空间管控"),
										JBoltExcelHeader.create("isStockWarnEnabled","启用库存预警"),
										JBoltExcelHeader.create("cWhMemo","备注")
								)
								//特殊数据转换器
								.setDataChangeHandler((data,index) ->{
									ValidationUtils.notNull(data.get("cwhcode"), "仓库编码为空！");
									ValidationUtils.notNull(data.get("isSpaceControlEnabled"), "启用库存预警为空！");
									data.changeStrToBoolean("isSpaceControlEnabled","是");
									ValidationUtils.notNull(data.get("isStockWarnEnabled"), "启用空间掌控为空！");
									data.changeStrToBoolean("isStockWarnEnabled","是");

                                    //查重
                                    ValidationUtils.assertNull(findByWhCode(data.getStr("cwhcode")), "仓库编码重复！");

									Dept dept = deptService.findFirst(Okv.by("sn", data.get("cDepCode")));
									ValidationUtils.notNull(dept, data.get("cDepCode")+"部门错误！");
									//创建相关信息
									data.change("iCreateBy", JBoltUserKit.getUserId());
									data.change("cCreateName", JBoltUserKit.getUserName());
									data.change("dCreateTime", DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss"));
									//更新相关信息
									data.change("iUpdateBy", JBoltUserKit.getUserId());
									data.change("cUpdateName", JBoltUserKit.getUserName());
									data.change("dUpdateTime", DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss"));
									//组织相关信息
									data.change("cOrgCode", "1");
									data.change("cOrgName", "1");
									data.change("iOrgId", "1");
                                    //TODO 来源1.MES 2.U8  现默认U8
                                    data.change("iSource", "2");


								})
								//从第三行开始读取
								.setDataStartRow(3)
				);
		//从指定的sheet工作表里读取数据
		List<Warehouse> models = JBoltExcelUtil.readModels(jBoltExcel, "sheet1", Warehouse.class, errorMsg);
		if(notOk(models)) {
			if(errorMsg.length()>0) {
				return fail(errorMsg.toString());
			}else {
				return fail(JBoltMsg.DATA_IMPORT_FAIL_EMPTY);
			}
		}

		//读取数据没有问题后判断必填字段
		if(models.size()>0){
			tx(() -> {
				batchSave(models);
				return true;
			});

		}

		return SUCCESS;
	}

	public List<Record> findByWarehouse(){
		return dbTemplate("warehouse.findByWarehouse", Kv.by("orgId", getOrgId())).find();
	}

	public List<Record> options() {
		return dbTemplate("warehouse.options", Kv.of("isenabled", "true")).find();
	}

    /**
     * 批量删除（逻辑）
     * @param ids
     * @return
     */
	public Ret deleteByBatchIds(String ids) {
		update("UPDATE Bd_Warehouse SET isDeleted = 1 WHERE iAutoId IN (" +ids + ") ");
		return SUCCESS;
	}
}
