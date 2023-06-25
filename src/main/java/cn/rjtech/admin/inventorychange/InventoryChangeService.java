package cn.rjtech.admin.inventorychange;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.StrSplitter;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.Dictionary;
import cn.jbolt.core.poi.excel.JBoltExcel;
import cn.jbolt.core.poi.excel.JBoltExcelHeader;
import cn.jbolt.core.poi.excel.JBoltExcelSheet;
import cn.jbolt.core.poi.excel.JBoltExcelUtil;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.cusfieldsmappingd.CusFieldsMappingDService;
import cn.rjtech.model.momdata.InventoryChange;
import cn.rjtech.model.momdata.QcForm;
import cn.rjtech.util.Util;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.io.File;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * 物料建模-物料形态对照表
 * @ClassName: InventoryChangeService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-23 15:45
 */
public class InventoryChangeService extends BaseService<InventoryChange> {
	private final InventoryChange dao=new InventoryChange().dao();
	@Override
	protected InventoryChange dao() {
		return dao;
	}

	@Inject
	private CusFieldsMappingDService cusFieldsMappingDService;

	@Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

	/**
	 * 后台管理数据查询
	 * @param pageNumber 第几页
	 * @param pageSize   每页几条数据
	 * @param sortColumn  排序列名
	 * @param sortType  排序方式 asc desc
	 * @return
	 */
	public Page<Record> getAdminDatas(int pageNumber, int pageSize, String sortColumn, String sortType, Kv kv) {
		kv.set("orgId", getOrgId());
		return dbTemplate("inventorychange.list", kv.set("sortColumn", sortColumn).set("sortType", sortType)).paginate(pageNumber, pageSize);
	}

	public List<Record> findAll(String sortColumn, String sortType, Kv kv){
		kv.set("orgId", getOrgId());
		return dbTemplate("inventorychange.list", kv.set("sortColumn", sortColumn).set("sortType", sortType)).find();
	}

	/**
	 * 保存
	 * @param inventoryChange
	 * @return
	 */
	public Ret save(InventoryChange inventoryChange) {
		if(inventoryChange==null || isOk(inventoryChange.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		verifyData(inventoryChange);
		Date now = DateUtil.date();
		Long userId = JBoltUserKit.getUserId();
		String userName = JBoltUserKit.getUserName();
		inventoryChange.setICreateBy(userId);
		inventoryChange.setCCreateName(userName);
		inventoryChange.setDCreateTime(now);
		inventoryChange.setIOrgId(getOrgId());
		inventoryChange.setCOrgCode(getOrgCode());
		inventoryChange.setCOrgName(getOrgName());
		inventoryChange.setIUpdateBy(userId);
		inventoryChange.setCUpdateName(userName);
		inventoryChange.setDUpdateTime(now);
		inventoryChange.setIsDeleted(false);

		boolean success=inventoryChange.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(inventoryChange.getIAutoId(), JBoltUserKit.getUserId(), inventoryChange.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param inventoryChange
	 * @return
	 */
	public Ret update(InventoryChange inventoryChange) {
		if(inventoryChange==null || notOk(inventoryChange.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		verifyData(inventoryChange);
		//更新时需要判断数据存在
		InventoryChange dbInventoryChange=findById(inventoryChange.getIAutoId());
		if(dbInventoryChange==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		inventoryChange.setCUpdateName(JBoltUserKit.getUserName());
		inventoryChange.setDUpdateTime(DateUtil.date());
		inventoryChange.setIUpdateBy(JBoltUserKit.getUserId());
		//if(existsName(inventoryChange.getName(), inventoryChange.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=inventoryChange.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(inventoryChange.getIAutoId(), JBoltUserKit.getUserId(), inventoryChange.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param inventoryChange 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(InventoryChange inventoryChange, Kv kv) {
		//addDeleteSystemLog(inventoryChange.getIAutoId(), JBoltUserKit.getUserId(),inventoryChange.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param inventoryChange model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(InventoryChange inventoryChange, Kv kv) {
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
                                JBoltExcelHeader.create("转换前存货编码",15),
                                JBoltExcelHeader.create("转换后存货编码",15),
                                JBoltExcelHeader.create("转换率",15)
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
						JBoltExcelHeader.create("iBeforeInventoryId", "转换前存货编码"),
						JBoltExcelHeader.create("iAfterInventoryId", "转换后存货编码")
                        )//特殊数据转换器
						.setDataChangeHandler((data,index) ->{
							String beforeInventoryCode = data.getStr("iBeforeInventoryId");
							String afterInventoryCode = data.getStr("iAfterInventoryId");
							if(StrUtil.isNotBlank(beforeInventoryCode)){
								data.change("iBeforeInventoryId", getItemId(beforeInventoryCode));
							}
							if (StrUtil.isNotBlank(afterInventoryCode)){
								data.change("iAfterInventoryId", getItemId(afterInventoryCode));
							}
						})
				//从第三行开始读取
				.setDataStartRow(2)
				);
		//从指定的sheet工作表里读取数据
		List<InventoryChange> inventoryChanges=JBoltExcelUtil.readModels(jBoltExcel,1, InventoryChange.class,errorMsg);
		if(notOk(inventoryChanges)) {
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
				for (InventoryChange inventoryChange : inventoryChanges){
					verifyData(inventoryChange);
					save(inventoryChange);
				}
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
							JBoltExcelHeader.create("beforeinventorycode","转换前存货编码", 20),
							JBoltExcelHeader.create("beforeinvcode1","转换前客户部番", 20),
							JBoltExcelHeader.create("beforeinvname1","转换前部品名称", 20),
							JBoltExcelHeader.create("beforuomname","主计量单位", 20),
							JBoltExcelHeader.create("afterinventorycode","转换后存货编码", 20),
							JBoltExcelHeader.create("afterinvcode1","转换后客户部番", 20),
							JBoltExcelHeader.create("afterinvname1","转换后部品名称", 20),
							JBoltExcelHeader.create("afteruomname","主计量单位", 20),
							JBoltExcelHeader.create("ccreatename","创建人名称", 20),
							JBoltExcelHeader.create("dcreatetime","创建时间", 20)
                            )
    		    	//设置导出的数据源 来自于数据库查询出来的Model List
    		    	.setRecordDatas(2,datas)
    		    );
	}

	public Page<Record> inventoryAutocomplete(int pageNumber, int pageSize, Kv kv) {
		return dbTemplate("inventorychange.inventoryAutocomplete", kv).paginate(pageNumber, pageSize);
	}
	public Page<Record> inventoryAutocompleteNew(int pageNumber, int pageSize, Kv kv) {
		kv.set("date",new Date());
		return dbTemplate("inventorychange.inventoryAutocompleteNew", kv).paginate(pageNumber, pageSize);
	}

	public Record findByIdRecord(String id){
		ValidationUtils.notBlank(id, JBoltMsg.PARAM_ERROR);
		Kv kv = Kv.by("ids", Util.getInSqlByIds(id));
		return dbTemplate("inventorychange.list", kv).findFirst();
	}

	public void verifyData(InventoryChange inventoryChange){
		ValidationUtils.notNull(inventoryChange, JBoltMsg.DATA_NOT_EXIST);
		ValidationUtils.notNull(inventoryChange.getIBeforeInventoryId(), "转换前存货编码不能为空");
		ValidationUtils.notNull(inventoryChange.getIAfterInventoryId(), "转换后存货编码不能为空");

		String beforeInv = String.valueOf(inventoryChange.getIBeforeInventoryId());
		String afterInv = String.valueOf(inventoryChange.getIAfterInventoryId());
		ValidationUtils.isTrue(!beforeInv.equals(afterInv), "转换前跟转换后的编码一致，请跟换");
		InventoryChange change = findByBeforeInventoryId(inventoryChange.getIBeforeInventoryId(), inventoryChange.getIAutoId());
		ValidationUtils.isTrue(change == null, JBoltMsg.DATA_SAME_SN_EXIST);
	}

	public Long getItemId(String itemCode){
		return queryLong("SELECT IAUTOID FROM Bd_Inventory WHERE cInvCode=?", itemCode);
	}

	public Ret removeByIds(String ids){
		DateTime date = DateUtil.date();
		Long userId = JBoltUserKit.getUserId();
		String userName = JBoltUserKit.getUserName();
		tx(() -> {
			for (String id : ids.split(",")){
				InventoryChange inventoryChange = findById(id);
				inventoryChange.setIsDeleted(true);
				inventoryChange.setIUpdateBy(userId);
				inventoryChange.setDUpdateTime(date);
				inventoryChange.setCUpdateName(userName);
				inventoryChange.update();
			}
			return true;
		});
		return SUCCESS;
	}

	public List<Record> findByOrgList(Long orgId){
		return dbTemplate("inventorychange.findList", Okv.by("orgId", orgId)).find();
	}

	public InventoryChange findByBeforeInventoryId(Long beforeInventoryId, Long id){
		String sql = "select * from Bd_InventoryChange where IsDeleted = 0 and iBeforeInventoryId = ?";
		// 为空校验全部，不为空排除校验自己
		if (ObjectUtil.isNotNull(id)){
			sql = sql.concat(" and iAutoId <> "+id);
		}
		return findFirst(sql, beforeInventoryId);
	}

	public InventoryChange findByBeforeInventoryId(Long beforeInventoryId){
		return findByBeforeInventoryId(beforeInventoryId, null);
	}

	public List<Record> findInventoryChangeByInventoryId(Long iiventoryid){
		return dbTemplate("inventorychange.findInventoryChangeByInventoryId",Kv.by("iiventoryid",iiventoryid)).find();
	}

	/**
	 * 从系统导入字段配置，获得导入的数据
	 */
	public Ret importExcelClass(File file) {
		List<Record> records = cusFieldsMappingDService.getImportRecordsByTableName(file, table());
		if (notOk(records)) {
			return fail(JBoltMsg.DATA_IMPORT_FAIL_EMPTY);
		}


		for (Record record : records) {


			Date now=new Date();
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
		}

		// 执行批量操作
		tx(() -> {
			batchSaveRecords(records);
			return true;
		});
		return SUCCESS;
	}
}
