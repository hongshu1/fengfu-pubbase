package cn.rjtech.admin.inventoryroutingsop;

import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.rjtech.model.momdata.InventoryRoutingSop;
/**
 * 物料建模-存货工序作业指导书
 * @ClassName: InventoryRoutingSopService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-29 10:06
 */
public class InventoryRoutingSopService extends BaseService<InventoryRoutingSop> {
	private final InventoryRoutingSop dao=new InventoryRoutingSop().dao();
	@Override
	protected InventoryRoutingSop dao() {
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
     * @param isEnabled 是否启用
	 * @return
	 */
	public Page<InventoryRoutingSop> getAdminDatas(int pageNumber, int pageSize, String keywords, Boolean isEnabled) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
	    //sql条件处理
        sql.eqBooleanToChar("isEnabled",isEnabled);
        //关键词模糊查询
        sql.likeMulti(keywords,"cName", "cCreateName");
        //排序
        sql.desc("iAutoId");
		return paginate(sql);
	}

	/**
	 * 保存
	 * @param inventoryRoutingSop
	 * @return
	 */
	public Ret save(InventoryRoutingSop inventoryRoutingSop) {
		if(inventoryRoutingSop==null || isOk(inventoryRoutingSop.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(inventoryRoutingSop.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=inventoryRoutingSop.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(inventoryRoutingSop.getIAutoId(), JBoltUserKit.getUserId(), inventoryRoutingSop.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param inventoryRoutingSop
	 * @return
	 */
	public Ret update(InventoryRoutingSop inventoryRoutingSop) {
		if(inventoryRoutingSop==null || notOk(inventoryRoutingSop.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		InventoryRoutingSop dbInventoryRoutingSop=findById(inventoryRoutingSop.getIAutoId());
		if(dbInventoryRoutingSop==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(inventoryRoutingSop.getName(), inventoryRoutingSop.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=inventoryRoutingSop.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(inventoryRoutingSop.getIAutoId(), JBoltUserKit.getUserId(), inventoryRoutingSop.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param inventoryRoutingSop 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(InventoryRoutingSop inventoryRoutingSop, Kv kv) {
		//addDeleteSystemLog(inventoryRoutingSop.getIAutoId(), JBoltUserKit.getUserId(),inventoryRoutingSop.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param inventoryRoutingSop model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(InventoryRoutingSop inventoryRoutingSop, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(InventoryRoutingSop inventoryRoutingSop, String column, Kv kv) {
		//addUpdateSystemLog(inventoryRoutingSop.getIAutoId(), JBoltUserKit.getUserId(), inventoryRoutingSop.getName(),"的字段["+column+"]值:"+inventoryRoutingSop.get(column));
		/**
		switch(column){
		    case "isEnabled":
		        break;
		}
		*/
		return null;
	}

}