package cn.rjtech.admin.subcontractorderd;

import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.rjtech.model.momdata.SubcontractOrderD;
/**
 * 采购/委外订单-采购订单明细
 * @ClassName: SubcontractOrderDService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-25 21:33
 */
public class SubcontractOrderDService extends BaseService<SubcontractOrderD> {
	private final SubcontractOrderD dao=new SubcontractOrderD().dao();
	@Override
	protected SubcontractOrderD dao() {
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
     * @param isPresent 是否赠品： 0. 否 1. 是
     * @param isDeleted 删除状态：0. 未删除 1. 已删除
	 * @return
	 */
	public Page<SubcontractOrderD> getAdminDatas(int pageNumber, int pageSize, Boolean isPresent, Boolean isDeleted) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
	    //sql条件处理
        sql.eqBooleanToChar("isPresent",isPresent);
        sql.eqBooleanToChar("isDeleted",isDeleted);
        //排序
        sql.desc("iAutoId");
		return paginate(sql);
	}

	/**
	 * 保存
	 * @param subcontractOrderD
	 * @return
	 */
	public Ret save(SubcontractOrderD subcontractOrderD) {
		if(subcontractOrderD==null || isOk(subcontractOrderD.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(subcontractOrderD.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=subcontractOrderD.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(subcontractOrderD.getIAutoId(), JBoltUserKit.getUserId(), subcontractOrderD.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param subcontractOrderD
	 * @return
	 */
	public Ret update(SubcontractOrderD subcontractOrderD) {
		if(subcontractOrderD==null || notOk(subcontractOrderD.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		SubcontractOrderD dbSubcontractOrderD=findById(subcontractOrderD.getIAutoId());
		if(dbSubcontractOrderD==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(subcontractOrderD.getName(), subcontractOrderD.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=subcontractOrderD.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(subcontractOrderD.getIAutoId(), JBoltUserKit.getUserId(), subcontractOrderD.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param subcontractOrderD 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(SubcontractOrderD subcontractOrderD, Kv kv) {
		//addDeleteSystemLog(subcontractOrderD.getIAutoId(), JBoltUserKit.getUserId(),subcontractOrderD.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param subcontractOrderD model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(SubcontractOrderD subcontractOrderD, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(SubcontractOrderD subcontractOrderD, String column, Kv kv) {
		//addUpdateSystemLog(subcontractOrderD.getIAutoId(), JBoltUserKit.getUserId(), subcontractOrderD.getName(),"的字段["+column+"]值:"+subcontractOrderD.get(column));
		/**
		switch(column){
		    case "isPresent":
		        break;
		    case "isDeleted":
		        break;
		}
		*/
		return null;
	}

}