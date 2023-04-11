package cn.rjtech.admin.manualorderm;

import com.jfinal.plugin.activerecord.Page;
import java.util.Date;
import cn.jbolt.core.bean.JBoltDateRange;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.rjtech.model.momdata.ManualOrderM;
import com.jfinal.plugin.activerecord.Record;

/**
 * 客户订单-手配订单主表
 * @ClassName: ManualOrderMService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-10 15:18
 */
public class ManualOrderMService extends BaseService<ManualOrderM> {
	private final ManualOrderM dao=new ManualOrderM().dao();
	@Override
	protected ManualOrderM dao() {
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
		return dbTemplate("manualorderm.list",kv).paginate(pageNumber,pageSize);
	}

	/**
	 * 保存
	 * @param manualOrderM
	 * @return
	 */
	public Ret save(ManualOrderM manualOrderM) {
		if(manualOrderM==null || isOk(manualOrderM.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(manualOrderM.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=manualOrderM.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(manualOrderM.getIAutoId(), JBoltUserKit.getUserId(), manualOrderM.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param manualOrderM
	 * @return
	 */
	public Ret update(ManualOrderM manualOrderM) {
		if(manualOrderM==null || notOk(manualOrderM.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		ManualOrderM dbManualOrderM=findById(manualOrderM.getIAutoId());
		if(dbManualOrderM==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(manualOrderM.getName(), manualOrderM.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=manualOrderM.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(manualOrderM.getIAutoId(), JBoltUserKit.getUserId(), manualOrderM.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param manualOrderM 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(ManualOrderM manualOrderM, Kv kv) {
		//addDeleteSystemLog(manualOrderM.getIAutoId(), JBoltUserKit.getUserId(),manualOrderM.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param manualOrderM model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(ManualOrderM manualOrderM, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

}