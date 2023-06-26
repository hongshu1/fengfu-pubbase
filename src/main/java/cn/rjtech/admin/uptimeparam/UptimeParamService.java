package cn.rjtech.admin.uptimeparam;

import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.rjtech.model.momdata.UptimeParam;
/**
 * 稼动时间建模-稼动时间参数
 * @ClassName: UptimeParamService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-06-26 15:14
 */
public class UptimeParamService extends BaseService<UptimeParam> {
	private final UptimeParam dao=new UptimeParam().dao();
	@Override
	protected UptimeParam dao() {
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
     * @param isEnabled 是否启用;0. 否 1. 是
	 * @return
	 */
	public Page<UptimeParam> getAdminDatas(int pageNumber, int pageSize, String keywords, Boolean isEnabled) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
	    //sql条件处理
        sql.eqBooleanToChar("isEnabled",isEnabled);
        //关键词模糊查询
        sql.like("cUptimeParamName",keywords);
        //排序
        sql.desc("iAutoId");
		return paginate(sql);
	}

	/**
	 * 保存
	 * @param uptimeParam
	 * @return
	 */
	public Ret save(UptimeParam uptimeParam) {
		if(uptimeParam==null || isOk(uptimeParam.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		boolean success=uptimeParam.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(uptimeParam.getIAutoId(), JBoltUserKit.getUserId(), uptimeParam.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param uptimeParam
	 * @return
	 */
	public Ret update(UptimeParam uptimeParam) {
		if(uptimeParam==null || notOk(uptimeParam.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		UptimeParam dbUptimeParam=findById(uptimeParam.getIAutoId());
		if(dbUptimeParam==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		boolean success=uptimeParam.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(uptimeParam.getIAutoId(), JBoltUserKit.getUserId(), uptimeParam.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param uptimeParam 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(UptimeParam uptimeParam, Kv kv) {
		//addDeleteSystemLog(uptimeParam.getIAutoId(), JBoltUserKit.getUserId(),uptimeParam.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param uptimeParam model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(UptimeParam uptimeParam, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(UptimeParam uptimeParam, String column, Kv kv) {
		//addUpdateSystemLog(uptimeParam.getIAutoId(), JBoltUserKit.getUserId(), uptimeParam.getName(),"的字段["+column+"]值:"+uptimeParam.get(column));
		/**
		switch(column){
		    case "isEnabled":
		        break;
		    case "isDeleted":
		        break;
		}
		*/
		return null;
	}

}