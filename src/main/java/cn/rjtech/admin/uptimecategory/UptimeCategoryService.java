package cn.rjtech.admin.uptimecategory;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.UptimeCategory;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
/**
 * 稼动时间建模-稼动时间参数类别
 * @ClassName: UptimeCategoryService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-06-26 14:25
 */
public class UptimeCategoryService extends BaseService<UptimeCategory> {
	private final UptimeCategory dao=new UptimeCategory().dao();
	@Override
	protected UptimeCategory dao() {
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
     * @param cUptimeCategoryName 稼动时间参数类别名称
     * @param isDeleted 删除状态;0. 未删除 1. 已删除
	 * @return
	 */
	public Page<UptimeCategory> getAdminDatas(int pageNumber, int pageSize, String keywords, String cUptimeCategoryName, Boolean isDeleted) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
	    //sql条件处理
        sql.eq("cUptimeCategoryName",cUptimeCategoryName);
        sql.eqBooleanToChar("isDeleted",isDeleted);
        //关键词模糊查询
        sql.like("cUptimeCategoryName",keywords);
        //排序
        sql.desc("iAutoId");
		return paginate(sql);
	}

	/**
	 * 保存
	 * @param uptimeCategory
	 * @return
	 */
	public Ret save(UptimeCategory uptimeCategory) {
		if(uptimeCategory==null || isOk(uptimeCategory.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		boolean success=uptimeCategory.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(uptimeCategory.getIAutoId(), JBoltUserKit.getUserId(), uptimeCategory.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param uptimeCategory
	 * @return
	 */
	public Ret update(UptimeCategory uptimeCategory) {
		if(uptimeCategory==null || notOk(uptimeCategory.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		UptimeCategory dbUptimeCategory=findById(uptimeCategory.getIAutoId());
		if(dbUptimeCategory==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		boolean success=uptimeCategory.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(uptimeCategory.getIAutoId(), JBoltUserKit.getUserId(), uptimeCategory.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param uptimeCategory 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(UptimeCategory uptimeCategory, Kv kv) {
		//addDeleteSystemLog(uptimeCategory.getIAutoId(), JBoltUserKit.getUserId(),uptimeCategory.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param uptimeCategory model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(UptimeCategory uptimeCategory, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(UptimeCategory uptimeCategory, String column, Kv kv) {
		//addUpdateSystemLog(uptimeCategory.getIAutoId(), JBoltUserKit.getUserId(), uptimeCategory.getName(),"的字段["+column+"]值:"+uptimeCategory.get(column));
		/**
		switch(column){
		    case "isDeleted":
		        break;
		}
		*/
		return null;
	}

}