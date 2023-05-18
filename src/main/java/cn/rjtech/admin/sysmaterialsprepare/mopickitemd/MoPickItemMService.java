package cn.rjtech.admin.sysmaterialsprepare.mopickitemd;

import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.rjtech.model.momdata.MoPickItemM;
import com.jfinal.plugin.activerecord.Record;

/**
 * 生产订单-备料单主表
 * @ClassName: MoPickItemMService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-09 15:30
 */
public class MoPickItemMService extends BaseService<MoPickItemM> {
	private final MoPickItemM dao=new MoPickItemM().dao();
	@Override
	protected MoPickItemM dao() {
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
	 * @param
	 * @param  ;1. 生产备料
	 * @return
	 */
//	public Page<MoPickItemM> getAdminDatas(int pageNumber, int pageSize, String keywords, Integer iType) {
//		//创建sql对象
//		Sql sql = selectSql().page(pageNumber,pageSize);
//		//sql条件处理
//		sql.eq("iType",iType);
//		//关键词模糊查询
//		sql.likeMulti(keywords,"cOrgName", "cCreateName", "cUpdateName");
//		//排序
//		sql.desc("iAutoId");
//		return paginate(sql);
//	}
	public Page<Record> getAdminDatas(int pageNumber, int pageSize, Kv kv) {
		Page<Record> paginate = dbTemplate("mopickitem.recpor", kv).paginate(pageNumber, pageSize);
		return paginate;
	}

	/**
	 * 保存
	 * @param moPickItemM
	 * @return
	 */
	public Ret save(MoPickItemM moPickItemM) {
		if(moPickItemM==null || isOk(moPickItemM.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(moPickItemM.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=moPickItemM.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(moPickItemM.getIAutoId(), JBoltUserKit.getUserId(), moPickItemM.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param moPickItemM
	 * @return
	 */
	public Ret update(MoPickItemM moPickItemM) {
		if(moPickItemM==null || notOk(moPickItemM.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		MoPickItemM dbMoPickItemM=findById(moPickItemM.getIAutoId());
		if(dbMoPickItemM==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(moPickItemM.getName(), moPickItemM.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=moPickItemM.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(moPickItemM.getIAutoId(), JBoltUserKit.getUserId(), moPickItemM.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param moPickItemM 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(MoPickItemM moPickItemM, Kv kv) {
		//addDeleteSystemLog(moPickItemM.getIAutoId(), JBoltUserKit.getUserId(),moPickItemM.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param moPickItemM model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(MoPickItemM moPickItemM, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

}