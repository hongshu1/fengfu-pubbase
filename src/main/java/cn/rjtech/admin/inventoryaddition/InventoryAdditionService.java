package cn.rjtech.admin.inventoryaddition;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.InventoryAddition;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
/**
 * 存货档案-附加
 * @ClassName: InventoryAdditionService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-24 13:59
 */
public class InventoryAdditionService extends BaseService<InventoryAddition> {
	private final InventoryAddition dao=new InventoryAddition().dao();
	@Override
	protected InventoryAddition dao() {
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
	 * @param keywords   关键词
     * @param privatedescseg9 结算模式
	 * @return
	 */
	public Page<InventoryAddition> getAdminDatas(int pageNumber, int pageSize, String keywords, Boolean privatedescseg9) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
	    //sql条件处理
        sql.eqBooleanToChar("privatedescseg9",privatedescseg9);
        //关键词模糊查询
        sql.like("cDescription",keywords);
        //排序
        sql.desc("iAutoId");
		return paginate(sql);
	}

	/**
	 * 保存
	 * @param inventoryAddition
	 * @return
	 */
	public Ret save(InventoryAddition inventoryAddition) {
		if(inventoryAddition==null || isOk(inventoryAddition.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(inventoryAddition.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=inventoryAddition.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(inventoryAddition.getIAutoId(), JBoltUserKit.getUserId(), inventoryAddition.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param inventoryAddition
	 * @return
	 */
	public Ret update(InventoryAddition inventoryAddition) {
		if(inventoryAddition==null || notOk(inventoryAddition.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		InventoryAddition dbInventoryAddition=findById(inventoryAddition.getIAutoId());
		if(dbInventoryAddition==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(inventoryAddition.getName(), inventoryAddition.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=inventoryAddition.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(inventoryAddition.getIAutoId(), JBoltUserKit.getUserId(), inventoryAddition.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param inventoryAddition 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(InventoryAddition inventoryAddition, Kv kv) {
		//addDeleteSystemLog(inventoryAddition.getIAutoId(), JBoltUserKit.getUserId(),inventoryAddition.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param inventoryAddition model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(InventoryAddition inventoryAddition, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(InventoryAddition inventoryAddition, String column, Kv kv) {
		//addUpdateSystemLog(inventoryAddition.getIAutoId(), JBoltUserKit.getUserId(), inventoryAddition.getName(),"的字段["+column+"]值:"+inventoryAddition.get(column));
		/**
		switch(column){
		    case "privatedescseg9":
		        break;
		}
		*/
		return null;
	}

}