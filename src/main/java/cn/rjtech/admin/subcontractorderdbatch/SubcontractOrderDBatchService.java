package cn.rjtech.admin.subcontractorderdbatch;

import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.rjtech.model.momdata.SubcontractOrderDBatch;
/**
 * 采购/委外管理-采购现品票
 * @ClassName: SubcontractOrderDBatchService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-25 21:32
 */
public class SubcontractOrderDBatchService extends BaseService<SubcontractOrderDBatch> {
	private final SubcontractOrderDBatch dao=new SubcontractOrderDBatch().dao();
	@Override
	protected SubcontractOrderDBatch dao() {
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
     * @param isEffective 是否生效：0. 否  1. 是
	 * @return
	 */
	public Page<SubcontractOrderDBatch> getAdminDatas(int pageNumber, int pageSize, Boolean isEffective) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
	    //sql条件处理
        sql.eqBooleanToChar("isEffective",isEffective);
        //排序
        sql.desc("iAutoId");
		return paginate(sql);
	}

	/**
	 * 保存
	 * @param subcontractOrderDBatch
	 * @return
	 */
	public Ret save(SubcontractOrderDBatch subcontractOrderDBatch) {
		if(subcontractOrderDBatch==null || isOk(subcontractOrderDBatch.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(subcontractOrderDBatch.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=subcontractOrderDBatch.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(subcontractOrderDBatch.getIAutoId(), JBoltUserKit.getUserId(), subcontractOrderDBatch.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param subcontractOrderDBatch
	 * @return
	 */
	public Ret update(SubcontractOrderDBatch subcontractOrderDBatch) {
		if(subcontractOrderDBatch==null || notOk(subcontractOrderDBatch.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		SubcontractOrderDBatch dbSubcontractOrderDBatch=findById(subcontractOrderDBatch.getIAutoId());
		if(dbSubcontractOrderDBatch==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(subcontractOrderDBatch.getName(), subcontractOrderDBatch.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=subcontractOrderDBatch.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(subcontractOrderDBatch.getIAutoId(), JBoltUserKit.getUserId(), subcontractOrderDBatch.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param subcontractOrderDBatch 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(SubcontractOrderDBatch subcontractOrderDBatch, Kv kv) {
		//addDeleteSystemLog(subcontractOrderDBatch.getIAutoId(), JBoltUserKit.getUserId(),subcontractOrderDBatch.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param subcontractOrderDBatch model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(SubcontractOrderDBatch subcontractOrderDBatch, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(SubcontractOrderDBatch subcontractOrderDBatch, String column, Kv kv) {
		//addUpdateSystemLog(subcontractOrderDBatch.getIAutoId(), JBoltUserKit.getUserId(), subcontractOrderDBatch.getName(),"的字段["+column+"]值:"+subcontractOrderDBatch.get(column));
		/**
		switch(column){
		    case "isEffective":
		        break;
		}
		*/
		return null;
	}

}