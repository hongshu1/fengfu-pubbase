package cn.rjtech.admin.demandpland;

import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.rjtech.model.momdata.DemandPlanD;
/**
 * 需求计划管理-到货计划细表
 * @ClassName: DemandPlanDService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-19 16:15
 */
public class DemandPlanDService extends BaseService<DemandPlanD> {
	private final DemandPlanD dao=new DemandPlanD().dao();
	@Override
	protected DemandPlanD dao() {
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
	public Page<DemandPlanD> getAdminDatas(int pageNumber, int pageSize) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
        //排序
        sql.desc("iAutoId");
		return paginate(sql);
	}

	/**
	 * 保存
	 * @param demandPlanD
	 * @return
	 */
	public Ret save(DemandPlanD demandPlanD) {
		if(demandPlanD==null || isOk(demandPlanD.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(demandPlanD.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=demandPlanD.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(demandPlanD.getIAutoId(), JBoltUserKit.getUserId(), demandPlanD.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param demandPlanD
	 * @return
	 */
	public Ret update(DemandPlanD demandPlanD) {
		if(demandPlanD==null || notOk(demandPlanD.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		DemandPlanD dbDemandPlanD=findById(demandPlanD.getIAutoId());
		if(dbDemandPlanD==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(demandPlanD.getName(), demandPlanD.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=demandPlanD.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(demandPlanD.getIAutoId(), JBoltUserKit.getUserId(), demandPlanD.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param demandPlanD 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(DemandPlanD demandPlanD, Kv kv) {
		//addDeleteSystemLog(demandPlanD.getIAutoId(), JBoltUserKit.getUserId(),demandPlanD.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param demandPlanD model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(DemandPlanD demandPlanD, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

}