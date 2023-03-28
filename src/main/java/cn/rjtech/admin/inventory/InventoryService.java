package cn.rjtech.admin.inventory;

import cn.jbolt.core.kit.JBoltUserKit;
import cn.rjtech.model.momdata.InventoryClass;
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
import cn.jbolt.core.db.sql.Sql;
import cn.rjtech.model.momdata.Inventory;
import com.jfinal.plugin.activerecord.Record;

/**
 * 物料建模-存货档案
 * @ClassName: InventoryService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-23 09:35
 */
public class InventoryService extends BaseService<Inventory> {
	private final Inventory dao=new Inventory().dao();
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
		return dbTemplate("inventoryclass.inventoryList",kv).paginate(pageNumber,pageSize);
	}

	/**
	 * 保存
	 * @param inventory
	 * @return
	 */
	public Ret save(Inventory inventory) {
		if(inventory==null || isOk(inventory.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		setInventory(inventory);
		//if(existsName(inventory.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=inventory.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(inventory.getIAutoId(), JBoltUserKit.getUserId(), inventory.getName());
		}
		return ret(success);
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
}