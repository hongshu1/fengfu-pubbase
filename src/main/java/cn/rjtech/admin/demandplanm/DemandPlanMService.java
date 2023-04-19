package cn.rjtech.admin.demandplanm;

import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.rjtech.model.momdata.DemandPlanM;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;

/**
 * 需求计划管理-到货计划主表
 * @ClassName: DemandPlanMService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-19 16:15
 */
public class DemandPlanMService extends BaseService<DemandPlanM> {
	private final DemandPlanM dao=new DemandPlanM().dao();
	@Override
	protected DemandPlanM dao() {
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
     * @param IsDeleted 删除状态：0. 未删除 1. 已删除
	 * @return
	 */
	public Page<DemandPlanM> getAdminDatas(int pageNumber, int pageSize, String keywords, Boolean IsDeleted) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
	    //sql条件处理
        sql.eqBooleanToChar("IsDeleted",IsDeleted);
        //关键词模糊查询
        sql.likeMulti(keywords,"cOrgName", "cCreateName", "cUpdateName");
        //排序
        sql.desc("iAutoId");
		return paginate(sql);
	}

	/**
	 * 保存
	 * @param demandPlanM
	 * @return
	 */
	public Ret save(DemandPlanM demandPlanM) {
		if(demandPlanM==null || isOk(demandPlanM.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(demandPlanM.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=demandPlanM.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(demandPlanM.getIAutoId(), JBoltUserKit.getUserId(), demandPlanM.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param demandPlanM
	 * @return
	 */
	public Ret update(DemandPlanM demandPlanM) {
		if(demandPlanM==null || notOk(demandPlanM.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		DemandPlanM dbDemandPlanM=findById(demandPlanM.getIAutoId());
		if(dbDemandPlanM==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(demandPlanM.getName(), demandPlanM.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=demandPlanM.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(demandPlanM.getIAutoId(), JBoltUserKit.getUserId(), demandPlanM.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param demandPlanM 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(DemandPlanM demandPlanM, Kv kv) {
		//addDeleteSystemLog(demandPlanM.getIAutoId(), JBoltUserKit.getUserId(),demandPlanM.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param demandPlanM model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(DemandPlanM demandPlanM, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(DemandPlanM demandPlanM, String column, Kv kv) {
		//addUpdateSystemLog(demandPlanM.getIAutoId(), JBoltUserKit.getUserId(), demandPlanM.getName(),"的字段["+column+"]值:"+demandPlanM.get(column));
		/**
		switch(column){
		    case "IsDeleted":
		        break;
		}
		*/
		return null;
	}
	
	public List<Record> findByVendorDate(Kv kv){
		kv.set("orgId", getOrgId());
		return dbTemplate("demandplanm.findByVendorDate", kv).find();
	}

}
