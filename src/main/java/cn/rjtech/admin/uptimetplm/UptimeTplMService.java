package cn.rjtech.admin.uptimetplm;

import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.rjtech.model.momdata.UptimeTplM;
/**
 * 稼动时间建模-稼动时间模板主表
 * @ClassName: UptimeTplMService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-06-27 19:20
 */
public class UptimeTplMService extends BaseService<UptimeTplM> {
	private final UptimeTplM dao=new UptimeTplM().dao();
	@Override
	protected UptimeTplM dao() {
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
     * @param isDeleted 删除状态;0. 未删除 1. 已删除
	 * @return
	 */
	public Page<UptimeTplM> getAdminDatas(int pageNumber, int pageSize, String keywords, Boolean isDeleted) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
	    //sql条件处理
        sql.eqBooleanToChar("isDeleted",isDeleted);
        //关键词模糊查询
        sql.like("cCreateName",keywords);
        //排序
        sql.desc("iAutoId");
		return paginate(sql);
	}

	/**
	 * 保存
	 * @param uptimeTplM
	 * @return
	 */
	public Ret save(UptimeTplM uptimeTplM) {
		if(uptimeTplM==null || isOk(uptimeTplM.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		boolean success=uptimeTplM.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(uptimeTplM.getIAutoId(), JBoltUserKit.getUserId(), uptimeTplM.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param uptimeTplM
	 * @return
	 */
	public Ret update(UptimeTplM uptimeTplM) {
		if(uptimeTplM==null || notOk(uptimeTplM.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		UptimeTplM dbUptimeTplM=findById(uptimeTplM.getIAutoId());
		if(dbUptimeTplM==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		boolean success=uptimeTplM.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(uptimeTplM.getIAutoId(), JBoltUserKit.getUserId(), uptimeTplM.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param uptimeTplM 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(UptimeTplM uptimeTplM, Kv kv) {
		//addDeleteSystemLog(uptimeTplM.getIAutoId(), JBoltUserKit.getUserId(),uptimeTplM.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param uptimeTplM model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(UptimeTplM uptimeTplM, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

}