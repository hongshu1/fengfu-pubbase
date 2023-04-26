package cn.rjtech.admin.subcontractorderm;

import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.rjtech.model.momdata.SubcontractOrderM;
/**
 * 采购/委外订单-采购订单主表
 * @ClassName: SubcontractOrderMService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-25 21:33
 */
public class SubcontractOrderMService extends BaseService<SubcontractOrderM> {
	private final SubcontractOrderM dao=new SubcontractOrderM().dao();
	@Override
	protected SubcontractOrderM dao() {
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
     * @param iBusType 业务类型： 1. 普通采购
     * @param iPurchaseTypeId 采购类型ID，取采购类型档案
     * @param iPayableType 应付类型：1. 材料费
     * @param IsDeleted 删除状态：0. 未删除 1. 已删除
     * @param hideInvalid 已失效隐藏：0. 否 1. 是
	 * @return
	 */
	public Page<SubcontractOrderM> getAdminDatas(int pageNumber, int pageSize, String keywords, Integer iBusType, Long iPurchaseTypeId, Integer iPayableType, Boolean IsDeleted, Boolean hideInvalid) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
	    //sql条件处理
        sql.eq("iBusType",iBusType);
        sql.eq("iPurchaseTypeId",iPurchaseTypeId);
        sql.eq("iPayableType",iPayableType);
        sql.eqBooleanToChar("IsDeleted",IsDeleted);
        sql.eqBooleanToChar("hideInvalid",hideInvalid);
        //关键词模糊查询
        sql.likeMulti(keywords,"cOrgName", "cCreateName", "cUpdateName");
        //排序
        sql.desc("iAutoId");
		return paginate(sql);
	}

	/**
	 * 保存
	 * @param subcontractOrderM
	 * @return
	 */
	public Ret save(SubcontractOrderM subcontractOrderM) {
		if(subcontractOrderM==null || isOk(subcontractOrderM.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(subcontractOrderM.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=subcontractOrderM.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(subcontractOrderM.getIAutoId(), JBoltUserKit.getUserId(), subcontractOrderM.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param subcontractOrderM
	 * @return
	 */
	public Ret update(SubcontractOrderM subcontractOrderM) {
		if(subcontractOrderM==null || notOk(subcontractOrderM.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		SubcontractOrderM dbSubcontractOrderM=findById(subcontractOrderM.getIAutoId());
		if(dbSubcontractOrderM==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(subcontractOrderM.getName(), subcontractOrderM.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=subcontractOrderM.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(subcontractOrderM.getIAutoId(), JBoltUserKit.getUserId(), subcontractOrderM.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param subcontractOrderM 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(SubcontractOrderM subcontractOrderM, Kv kv) {
		//addDeleteSystemLog(subcontractOrderM.getIAutoId(), JBoltUserKit.getUserId(),subcontractOrderM.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param subcontractOrderM model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(SubcontractOrderM subcontractOrderM, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(SubcontractOrderM subcontractOrderM, String column, Kv kv) {
		//addUpdateSystemLog(subcontractOrderM.getIAutoId(), JBoltUserKit.getUserId(), subcontractOrderM.getName(),"的字段["+column+"]值:"+subcontractOrderM.get(column));
		/**
		switch(column){
		    case "IsDeleted":
		        break;
		    case "hideInvalid":
		        break;
		}
		*/
		return null;
	}

}