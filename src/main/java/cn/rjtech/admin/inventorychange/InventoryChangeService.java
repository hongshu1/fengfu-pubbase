package cn.rjtech.admin.inventorychange;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.jbolt.common.util.CACHE;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.User;
import cn.rjtech.model.momdata.Department;
import cn.rjtech.util.Util;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.plugin.activerecord.Page;

import java.util.Date;
import java.util.List;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import cn.jbolt.core.base.JBoltMsg;
import java.io.File;
import com.jfinal.plugin.activerecord.IAtom;
import java.sql.SQLException;
import cn.jbolt.core.poi.excel.*;
import cn.jbolt.core.db.sql.Sql;
import cn.rjtech.model.momdata.InventoryChange;
import com.jfinal.plugin.activerecord.Record;

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
						JBoltExcelHeader.create("iAfterInventoryId", "转换后存货编码"),
						JBoltExcelHeader.create("iChangeRate", "转换率")
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
							JBoltExcelHeader.create("ichangerate","转换率", 20),
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
	
	public Record findByIdRecord(String id){
		ValidationUtils.notBlank(id, JBoltMsg.PARAM_ERROR);
		Kv kv = Kv.by("ids", Util.getInSqlByIds(id));
		return dbTemplate("inventorychange.list", kv).findFirst();
	}
	
	public void verifyData(InventoryChange inventoryChange){
		ValidationUtils.notNull(inventoryChange, JBoltMsg.DATA_NOT_EXIST);
		ValidationUtils.notNull(inventoryChange.getIBeforeInventoryId(), "转换前存货编码不能为空");
		ValidationUtils.notNull(inventoryChange.getIAfterInventoryId(), "转换后存货编码不能为空");
		ValidationUtils.notNull(inventoryChange.getIChangeRate(), "转换率不能为空且只能为正数或0");
		String beforeInv = String.valueOf(inventoryChange.getIBeforeInventoryId());
		String afterInv = String.valueOf(inventoryChange.getIAfterInventoryId());
		ValidationUtils.isTrue(!beforeInv.equals(afterInv), JBoltMsg.DATA_SAME_SN_EXIST);
		ValidationUtils.isTrue(ZERO_BIGDECIMAL.compareTo(inventoryChange.getIChangeRate()) <=0,"转换率不能为空且只能为正数或0");
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
}
