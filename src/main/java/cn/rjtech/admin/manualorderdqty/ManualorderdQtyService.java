package cn.rjtech.admin.manualorderdqty;

import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.rjtech.model.momdata.ManualorderdQty;
/**
 * 客户订单-手配订单数量记录
 * @ClassName: ManualorderdQtyService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-07-12 11:06
 */
public class ManualorderdQtyService extends BaseService<ManualorderdQty> {
	private final ManualorderdQty dao=new ManualorderdQty().dao();
	@Override
	protected ManualorderdQty dao() {
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
	 * @return
	 */
	public Page<ManualorderdQty> getAdminDatas(int pageNumber, int pageSize) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
        //排序
        sql.desc("iAutoId");
		return paginate(sql);
	}

	/**
	 * 保存
	 * @param manualorderdQty
	 * @return
	 */
	public Ret save(ManualorderdQty manualorderdQty) {
		if(manualorderdQty==null || isOk(manualorderdQty.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		boolean success=manualorderdQty.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(manualorderdQty.getIAutoId(), JBoltUserKit.getUserId(), manualorderdQty.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param manualorderdQty
	 * @return
	 */
	public Ret update(ManualorderdQty manualorderdQty) {
		if(manualorderdQty==null || notOk(manualorderdQty.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		ManualorderdQty dbManualorderdQty=findById(manualorderdQty.getIAutoId());
		if(dbManualorderdQty==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		boolean success=manualorderdQty.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(manualorderdQty.getIAutoId(), JBoltUserKit.getUserId(), manualorderdQty.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param manualorderdQty 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(ManualorderdQty manualorderdQty, Kv kv) {
		//addDeleteSystemLog(manualorderdQty.getIAutoId(), JBoltUserKit.getUserId(),manualorderdQty.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param manualorderdQty model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(ManualorderdQty manualorderdQty, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

}