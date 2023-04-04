package cn.rjtech.admin.container;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.poi.excel.JBoltExcel;
import cn.jbolt.core.poi.excel.JBoltExcelHeader;
import cn.jbolt.core.poi.excel.JBoltExcelSheet;
import cn.jbolt.core.poi.excel.JBoltExcelUtil;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.rjtech.admin.ContainerStockInD.ContainerStockInDService;
import cn.rjtech.admin.ContainerStockInD.ContainerStockInMService;
import cn.rjtech.admin.containerclass.ContainerClassService;
import cn.rjtech.admin.warehouse.WarehouseService;
import cn.rjtech.model.momdata.*;
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
import com.jfinal.plugin.activerecord.Record;
import org.springframework.beans.BeanUtils;

import java.io.File;
import java.lang.reflect.Array;
import java.util.Date;
import java.util.List;

import static cn.hutool.core.text.StrPool.COMMA;

/**
 * 仓库建模-容器档案
 * @ClassName: ContainerService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-22 14:48
 */
public class ContainerService extends BaseService<Container> {
	private final Container dao=new Container().dao();
	@Override
	protected Container dao() {
		return dao;
	}

	@Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

    @Inject
    private WarehouseService warehouseService;

    @Inject
    private ContainerClassService containerClassService;

    //明细入库明细service
	@Inject
	private ContainerStockInDService containerStockInDService;

	//明细入库主表service
	@Inject
	private ContainerStockInMService containerStockInMService;

	/**
	 * 后台管理数据查询
	 * @param pageNumber 第几页
	 * @param pageSize   每页几条数据
	 * @param keywords   关键词
     * @param isInner 社内/社外：0. 社内 1. 社外
	 * @return
	 */
	public Page<Container> getAdminDatas(int pageNumber, int pageSize, String keywords, Boolean isInner) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
	    //sql条件处理
        sql.eqBooleanToChar("isInner",isInner);
        //关键词模糊查询
        sql.likeMulti(keywords,"cContainerCode", "cContainerName", "iContainerClassId");
        //排序
        sql.desc("iAutoId");
		return paginate(sql);
	}

	/**
	 * 保存
	 * @param container
	 * @return
	 */
	public Ret save(Container container) {
		if(container==null || isOk(container.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		ValidationUtils.assertNull(findByContainerCode(container.getCContainerCode()), "容器编码重复");

		//创建信息
		container.setICreateBy(JBoltUserKit.getUserId());
		container.setCCreateName(JBoltUserKit.getUserName());
		container.setDCreateTime(new Date());

		//更新信息
		container.setIUpdateBy(JBoltUserKit.getUserId());
		container.setCUpdateName(JBoltUserKit.getUserName());
		container.setDUpdateTime(new Date());

		//组织信息
		container.setCOrgCode(getOrgCode());
		container.setCOrgName(getOrgName());
		container.setIOrgId(getOrgId());
		//if(existsName(container.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=container.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(container.getIAutoId(), JBoltUserKit.getUserId(), container.getName());
		}
		return ret(success);
	}

	/**
	 * 查找容器编码
	 * @param cContainerCode
	 * @return
	 */
	public Container findByContainerCode(String cContainerCode) {
		return findFirst(Okv.by("cContainerCode", cContainerCode).set("isDeleted", false), "iautoid", "asc");
	}


	/**
	 * 更新
	 * @param container
	 * @return
	 */
	public Ret update(Container container) {
		if(container==null || notOk(container.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		Container dbContainer=findById(container.getIAutoId());
		if(dbContainer==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}

		//查询编码是否存在
		Container con = findByContainerCode(container.getCContainerCode());

		//编码重复判断
		if (con != null && !container.getIAutoId().equals(con.getIAutoId())) {
			ValidationUtils.assertNull(con.getCContainerCode(), "容器编码重复！");
		}

		container.setIUpdateBy(JBoltUserKit.getUserId());
		container.setCUpdateName(JBoltUserKit.getUserName());
		container.setDUpdateTime(new Date());

		//if(existsName(container.getName(), container.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=container.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(container.getIAutoId(), JBoltUserKit.getUserId(), container.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param container 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(Container container, Kv kv) {
		//addDeleteSystemLog(container.getIAutoId(), JBoltUserKit.getUserId(),container.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param container model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(Container container, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(Container container, String column, Kv kv) {
		//addUpdateSystemLog(container.getIAutoId(), JBoltUserKit.getUserId(), container.getName(),"的字段["+column+"]值:"+container.get(column));
		/**
		switch(column){
		    case "isInner":
		        break;
		    case "isEnabled":
		        break;
		}
		*/
		return null;
	}

	/**
	 * 分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param kv
	 * @return
	 */
	public Object paginateAdminDatas(Integer pageNumber, Integer pageSize, Kv kv) {
		return dbTemplate("container.paginateAdminDatas", kv).paginate(pageNumber, pageSize);
	}

	public Ret deleted(Long id) {
		return toggleBoolean(id, "isDeleted");
	}

	//删除（逻辑）
	public Ret deleteByBatchIds(String ids) {
		update("UPDATE Bd_Container SET isDeleted = 1 WHERE iAutoId IN (" +ids + ") ");
		return SUCCESS;
	}


	/**
	 * 容器档案导入实现
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
										JBoltExcelHeader.create("cContainerCode","容器编码"),
										JBoltExcelHeader.create("cContainerName","容器名称"),
										JBoltExcelHeader.create("cContainerClassCode","容器类型"),
										JBoltExcelHeader.create("iDepId","部门"),
										JBoltExcelHeader.create("cwhcode","所属仓库"),
										JBoltExcelHeader.create("isInner", "存放地点"),
										JBoltExcelHeader.create("iLength", "长(mm)"),
										JBoltExcelHeader.create("iWidth", "宽(mm)"),
										JBoltExcelHeader.create("iHeight", "高(mm)"),
										JBoltExcelHeader.create("iSupplierId", "供应商"),
										JBoltExcelHeader.create("isEnabled", "是否启用")
								)
								//特殊数据转换器
								.setDataChangeHandler((data,index) ->{
                                    ValidationUtils.notNull(data.get("cContainerCode"),"容器编码为空！");
                                    ValidationUtils.notNull(data.get("cContainerName"),"容器名称为空！");
                                    ValidationUtils.notNull(data.get("cContainerClassCode"),"容器类型为空！");
                                    ValidationUtils.notNull(data.get("iDepId"),"部门为空！");
                                    ValidationUtils.notNull(data.get("cwhcode"),"所属仓库为空！");
                                    ValidationUtils.notNull(data.get("isInner"), "存放地点为空！");
                                    data.changeStrToBoolean("isInner", "社外");
                                    ValidationUtils.notNull(data.get("iLength"), "长(mm)为空！");
                                    ValidationUtils.notNull(data.get("iWidth"), "宽(mm)为空！");
                                    ValidationUtils.notNull(data.get("iHeight"), "高(mm)为空！");
                                    ValidationUtils.notNull(data.get("iSupplierId"), "供应商为空！");
                                    ValidationUtils.notNull(data.get("isEnabled"), "是否启用为空！");
                                    data.changeStrToBoolean("isEnabled", "是");


									ValidationUtils.isTrue(findByContainerCode(data.getStr("cContainerCode"))==null, data.getStr("cContainerCode")+"编码重复");

									Warehouse warehouse = warehouseService.findByWhCode(data.getStr("cwhcode"));
									ValidationUtils.notNull(warehouse, data.getStr("cwhcode")+JBoltMsg.DATA_NOT_EXIST);

									if(isOk(data.getStr("cContainerClassCode"))){
										ContainerClass containerClass = containerClassService.findByConClassCode(data.getStr("cContainerClassCode"));
										ValidationUtils.notNull(warehouse, data.getStr("cContainerClassCode")+JBoltMsg.DATA_NOT_EXIST);
										//类别
										data.change("iContainerClassId", containerClass.getIAutoId());
									}

									//仓库
									data.change("iWarehouseId", warehouse.getIAutoId());

									data.remove("cwhcode");
									data.remove("cContainerClassCode");

									//创建信息
									data.change("iCreateBy", JBoltUserKit.getUserId());
									data.change("cCreateName", JBoltUserKit.getUserName());
									data.change("dCreateTime", DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss"));

									//更新信息
									data.change("iUpdateBy", JBoltUserKit.getUserId());
									data.change("cUpdateName", JBoltUserKit.getUserName());
									data.change("dUpdateTime", DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss"));

									//组织信息
									data.change("iOrgId", getOrgCode());
									data.change("cOrgCode", getOrgCode());
									data.change("cOrgName", getOrgName());
									//删除默认 0：未删除
									data.change("IsDeleted", 0);

								})
								//从第三行开始读取
								.setDataStartRow(3)
				);
		//从指定的sheet工作表里读取数据
		List<Container> models = JBoltExcelUtil.readModels(jBoltExcel, "sheet1", Container.class, errorMsg);
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


	public List<Record> list(Kv kv) {
		return dbTemplate("container.paginateAdminDatas", kv).find();
	}

	public Ret handleData(JBoltTable jBoltTable,String mark) {
		if (jBoltTable == null || jBoltTable.isBlank()) {
			return fail(JBoltMsg.JBOLTTABLE_IS_BLANK);
		}
//		List<Container> containers = jBoltTable.getSaveModelList(Container.class);
		//入库 2
		Kv kv = Kv.create();
		if (mark.equals("3")) {
			//入库主表处理
			ContainerStockInM stockInM = new ContainerStockInM();
			stockInM.setCMemo("");
			String o = kv.isNull("cmemo") ? TRUE : kv.getStr("cmemo");
			containerStockInMService.save(stockInM);
			//入库明细表处理
			containerStockInDService.save(new ContainerStockInD());
		}
		return null;
	}
}
