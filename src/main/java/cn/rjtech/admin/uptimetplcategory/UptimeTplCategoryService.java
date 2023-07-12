package cn.rjtech.admin.uptimetplcategory;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.UptimeTplCategory;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

/**
 * 稼动时间建模-稼动时间模板类别
 * @ClassName: UptimeTplCategoryService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-06-27 19:17
 */
public class UptimeTplCategoryService extends BaseService<UptimeTplCategory> {
	private final UptimeTplCategory dao=new UptimeTplCategory().dao();
	@Override
	protected UptimeTplCategory dao() {
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
	 * @param kv
	 * @return
	 */
	public Page<Record> getAdminDatas(int pageNumber, int pageSize, Kv kv) {
		if (isNull(kv.get("iuptimetplmid"))) {
			return null;
		}
		return dbTemplate("uptimetplcategory.getAdminDatas", kv).paginate(pageNumber, pageSize);
	}

	/**
	 * 保存
	 * @param uptimeTplCategory
	 * @return
	 */
	public Ret save(UptimeTplCategory uptimeTplCategory) {
		if(uptimeTplCategory==null || isOk(uptimeTplCategory.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		boolean success=uptimeTplCategory.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(uptimeTplCategory.getIAutoId(), JBoltUserKit.getUserId(), uptimeTplCategory.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param uptimeTplCategory
	 * @return
	 */
	public Ret update(UptimeTplCategory uptimeTplCategory) {
		if(uptimeTplCategory==null || notOk(uptimeTplCategory.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		UptimeTplCategory dbUptimeTplCategory=findById(uptimeTplCategory.getIAutoId());
		if(dbUptimeTplCategory==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		boolean success=uptimeTplCategory.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(uptimeTplCategory.getIAutoId(), JBoltUserKit.getUserId(), uptimeTplCategory.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param uptimeTplCategory 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(UptimeTplCategory uptimeTplCategory, Kv kv) {
		//addDeleteSystemLog(uptimeTplCategory.getIAutoId(), JBoltUserKit.getUserId(),uptimeTplCategory.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param uptimeTplCategory model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(UptimeTplCategory uptimeTplCategory, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

	public Boolean delectByUptimeTplMid(Long iuptimecategoryid) {
		delete(deleteSql().eq("iUptimeTplMid", iuptimecategoryid));
		return true;
	}
}