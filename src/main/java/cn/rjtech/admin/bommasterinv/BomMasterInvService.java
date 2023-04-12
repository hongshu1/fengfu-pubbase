package cn.rjtech.admin.bommasterinv;

import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.rjtech.model.momdata.BomMasterInv;
/**
 * 基础档案-母件物料存货集（新增/修改版本变更时处理，定时处理更新）
 * @ClassName: BomMasterInvService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-12 10:19
 */
public class BomMasterInvService extends BaseService<BomMasterInv> {
	private final BomMasterInv dao=new BomMasterInv().dao();
	@Override
	protected BomMasterInv dao() {
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
	public Page<BomMasterInv> getAdminDatas(int pageNumber, int pageSize) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
        //排序
        sql.desc("iAutoId");
		return paginate(sql);
	}

	/**
	 * 保存
	 * @param bomMasterInv
	 * @return
	 */
	public Ret save(BomMasterInv bomMasterInv) {
		if(bomMasterInv==null || isOk(bomMasterInv.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(bomMasterInv.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=bomMasterInv.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(bomMasterInv.getIAutoId(), JBoltUserKit.getUserId(), bomMasterInv.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param bomMasterInv
	 * @return
	 */
	public Ret update(BomMasterInv bomMasterInv) {
		if(bomMasterInv==null || notOk(bomMasterInv.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		BomMasterInv dbBomMasterInv=findById(bomMasterInv.getIAutoId());
		if(dbBomMasterInv==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(bomMasterInv.getName(), bomMasterInv.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=bomMasterInv.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(bomMasterInv.getIAutoId(), JBoltUserKit.getUserId(), bomMasterInv.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param bomMasterInv 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(BomMasterInv bomMasterInv, Kv kv) {
		//addDeleteSystemLog(bomMasterInv.getIAutoId(), JBoltUserKit.getUserId(),bomMasterInv.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param bomMasterInv model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(BomMasterInv bomMasterInv, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

}