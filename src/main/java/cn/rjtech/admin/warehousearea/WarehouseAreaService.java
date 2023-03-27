package cn.rjtech.admin.warehousearea;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.poi.excel.JBoltExcel;
import cn.jbolt.core.poi.excel.JBoltExcelHeader;
import cn.jbolt.core.poi.excel.JBoltExcelSheet;
import cn.jbolt.core.poi.excel.JBoltExcelUtil;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.warehouse.WarehouseService;
import cn.rjtech.base.service.BaseService;
import cn.rjtech.model.momdata.Warehouse;
import cn.rjtech.model.momdata.WarehouseArea;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.io.File;
import java.util.Date;
import java.util.List;

import static cn.hutool.core.text.StrPool.COMMA;

/**
 * 库区档案 Service
 * @ClassName: WarehouseAreaService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-11-02 20:43
 */
public class WarehouseAreaService extends BaseService<WarehouseArea> {

	private final WarehouseArea dao=new WarehouseArea().dao();

	@Override
	protected WarehouseArea dao() {
		return dao;
	}

	@Inject
	private WarehouseService warehouseService;

	/**
	 * 后台管理分页查询
	 */
	public Page<Record> paginateAdminDatas(int pageNumber, int pageSize, Kv kv) {
		return dbTemplate("warehousearea.paginateAdminDatas", kv).paginate(pageNumber, pageSize);
		//return paginateByKeywords("iAutoId","DESC", pageNumber, pageSize, keywords, "iAutoId");
	}

	public List<Record> list(Kv kv){
		return dbTemplate("warehousearea.paginateAdminDatas", kv).find();
	}

	public WarehouseArea findByWhAreaCode(String careacode){
		return findFirst(Okv.by("cAreaCode", careacode).set("isDeleted", false), "iautoid", "asc");
	}

	/**
	 * 保存
	 */
	public Ret save(WarehouseArea warehouseArea) {
		if(warehouseArea==null || isOk(warehouseArea.getIautoid())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		ValidationUtils.isTrue(findByWhAreaCode(warehouseArea.getCareacode())==null, "编码重复");

		//if(existsName(warehouseArea.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		warehouseArea.setIcreateby(JBoltUserKit.getUserId());
		warehouseArea.setCcreatename(JBoltUserKit.getUserName());
		warehouseArea.setDcreatetime(new Date());

		warehouseArea.setCorgcode(getOrgCode());
		warehouseArea.setCorgname(getOrgName());
		warehouseArea.setIorgid(getOrgId());

		//TODO 容器id  暂时没有写这个业务模块 先给一个默认值
		warehouseArea.setIcontainerId(Long.parseLong("111"));
		boolean success=warehouseArea.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(warehouseArea.getIautoid(), JBoltUserKit.getUserId(), warehouseArea.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 */
	public Ret update(WarehouseArea warehouseArea) {
		if(warehouseArea==null || notOk(warehouseArea.getIautoid())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		WarehouseArea dbWarehouseArea=findById(warehouseArea.getIautoid());
		if(dbWarehouseArea==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		warehouseArea.setIupdateby(JBoltUserKit.getUserId());
		warehouseArea.setCupdatename(JBoltUserKit.getUserName());
		warehouseArea.setDupdatetime(new Date());
		//if(existsName(warehouseArea.getName(), warehouseArea.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=warehouseArea.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(warehouseArea.getIautoid(), JBoltUserKit.getUserId(), warehouseArea.getName());
		}
		return ret(success);
	}

	/**
	 * 删除 指定多个ID
	 */
	public Ret deleteByBatchIds(String ids) {
		tx(() -> {
			String[] idarry = ids.split(",");


			//料品档案主表
			//deleteByIds(ids,true);
			update("UPDATE Bd_Warehouse_Area SET isDeleted = 1 WHERE iAutoId IN (" + ArrayUtil.join(idarry, COMMA) + ") ");
			return true;
		});
		return SUCCESS;
//    return deleteByIds(ids,true);
	}

	/**
	 * 删除数据后执行的回调
	 * @param warehouseArea 要删除的model
	 * @param kv 携带额外参数一般用不上
	 */
	@Override
	protected String afterDelete(WarehouseArea warehouseArea, Kv kv) {
		//addDeleteSystemLog(warehouseArea.getIautoid(), JBoltUserKit.getUserId(),warehouseArea.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param warehouseArea 要删除的model
	 * @param kv 携带额外参数一般用不上
	 */
	@Override
	public String checkCanDelete(WarehouseArea warehouseArea, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(warehouseArea, kv);
	}

	/**
	 * 设置返回二开业务所属的关键systemLog的targetType
	 */
	@Override
	protected int systemLogTargetType() {
		return ProjectSystemLogTargetType.NONE.getValue();
	}

	/**
	 * 切换isdeleted属性
	 */
	public Ret toggleIsdeleted(Long id) {
		return toggleBoolean(id, "isDeleted");
	}

	/**
	 * 切换isenabled属性
	 */
	public Ret toggleIsenabled(Long id) {
		return toggleBoolean(id, "isEnabled");
	}

	/**
	 * 检测是否可以toggle操作指定列
	 * @param warehouseArea 要toggle的model
	 * @param column 操作的哪一列
	 * @param kv 携带额外参数一般用不上
	 */
	@Override
	public String checkCanToggle(WarehouseArea warehouseArea,String column, Kv kv) {
		//没有问题就返回null  有问题就返回提示string 字符串
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(WarehouseArea warehouseArea, String column, Kv kv) {
		//addUpdateSystemLog(warehouseArea.getIautoid(), JBoltUserKit.getUserId(), warehouseArea.getName(),"的字段["+column+"]值:"+warehouseArea.get(column));
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param warehouseArea model
	 * @param kv 携带额外参数一般用不上
	 */
	@Override
	public String checkInUse(WarehouseArea warehouseArea, Kv kv) {
		//这里用来覆盖 检测WarehouseArea是否被其它表引用
		return null;
	}

	public WarehouseArea findByCode(String careacode){
		return findFirst(Okv.by("cAreaCode", careacode).set("isDeleted", false), "iautoid", "asc");
	}

	public Ret importExcelData(File file) {
		StringBuilder errorMsg=new StringBuilder();
		JBoltExcel jBoltExcel= JBoltExcel
				//从excel文件创建JBoltExcel实例
				.from(file)
				//设置工作表信息
				.setSheets(
						JBoltExcelSheet.create("sheet1")
								//设置列映射 顺序 标题名称
								.setHeaders(
										JBoltExcelHeader.create("careacode","库区编码"),
										JBoltExcelHeader.create("careaname","库区名称"),
										JBoltExcelHeader.create("cwhcode","所属仓库编码"),
										JBoltExcelHeader.create("cmemo","备注")
								)
								//特殊数据转换器
								.setDataChangeHandler((data,index) ->{
									ValidationUtils.notNull(data.get("careacode"), "库区编码为空！");
									ValidationUtils.notNull(data.get("careaname"), "库区名称为空！");
									ValidationUtils.notNull(data.get("cwhcode"), "仓库编码为空！");

									ValidationUtils.isTrue(findByCode(data.getStr("careacode"))==null, data.getStr("careacode")+"编码重复");


									Warehouse warehouse = warehouseService.findByWhCode(data.getStr("cwhcode"));
									ValidationUtils.notNull(warehouse, data.getStr("cwhcode")+ JBoltMsg.DATA_NOT_EXIST);

									data.change("iwarehouseid", warehouse.getIAutoId());
                                    data.remove("cwhcode");

									//TODO 容器id  暂时没有写这个业务模块 先给一个默认值
									data.change("iContainerId", Long.parseLong("111"));
									data.change("icreateby", JBoltUserKit.getUserId());
									data.change("ccreatename", JBoltUserKit.getUserName());
									data.change("dcreatetime", DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss"));
									data.change("corgcode", "1");
									data.change("corgname", "1");
									data.change("iorgid", "1");

								})
								//从第三行开始读取
								.setDataStartRow(3)
				);
		//从指定的sheet工作表里读取数据
		List<WarehouseArea> models = JBoltExcelUtil.readModels(jBoltExcel, "sheet1", WarehouseArea.class, errorMsg);
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
	
	public List<Record> findByWareHouseId(long wareHouseId){
		return dbTemplate("warehousearea.findByWareHouseId", Okv.by("wareHouseId", wareHouseId)).find();
	}

}