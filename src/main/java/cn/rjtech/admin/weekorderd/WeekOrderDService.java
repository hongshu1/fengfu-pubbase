package cn.rjtech.admin.weekorderd;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.WeekOrderD;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;

/**
 * 客户订单-周间客户订单明细
 * @ClassName: WeekOrderDService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-10 16:23
 */
public class WeekOrderDService extends BaseService<WeekOrderD> {
	private final WeekOrderD dao=new WeekOrderD().dao();
	@Override
	protected WeekOrderD dao() {
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
	public Page<WeekOrderD> getAdminDatas(int pageNumber, int pageSize) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
        //排序
        sql.desc("iAutoId");
		return paginate(sql);
	}

	/**
	 * 保存
	 * @param weekOrderD
	 * @return
	 */
	public Ret save(WeekOrderD weekOrderD) {
		if(weekOrderD==null || isOk(weekOrderD.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(weekOrderD.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=weekOrderD.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(weekOrderD.getIAutoId(), JBoltUserKit.getUserId(), weekOrderD.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param weekOrderD
	 * @return
	 */
	public Ret update(WeekOrderD weekOrderD) {
		if(weekOrderD==null || notOk(weekOrderD.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		WeekOrderD dbWeekOrderD=findById(weekOrderD.getIAutoId());
		if(dbWeekOrderD==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(weekOrderD.getName(), weekOrderD.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=weekOrderD.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(weekOrderD.getIAutoId(), JBoltUserKit.getUserId(), weekOrderD.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param weekOrderD 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(WeekOrderD weekOrderD, Kv kv) {
		//addDeleteSystemLog(weekOrderD.getIAutoId(), JBoltUserKit.getUserId(),weekOrderD.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param weekOrderD model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(WeekOrderD weekOrderD, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

    public List<Record> findData(Kv kv) {
		ValidationUtils.notNull(kv.getLong("iWeekOrderMid"), JBoltMsg.PARAM_ERROR);
		return dbTemplate("weekorderd.weekOrderDData",kv).find();
	}

    public Ret delete(Long id) {
		return toggleBoolean(id, "isDeleted");
    }

    public List<WeekOrderD> findByMId(Long iAutoId) {
        return find(selectSql().eq("iWeekOrderMid", iAutoId).eq("isDeleted", 0));
    }

	/**
	 * 保存调整计划时间数据
	 * @param jBoltTable
	 * @return
	 */
	public Ret saveUpdateCplanTime(JBoltTable jBoltTable) {
		return SUCCESS;
    }
}