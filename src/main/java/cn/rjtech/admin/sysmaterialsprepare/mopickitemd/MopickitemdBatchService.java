package cn.rjtech.admin.sysmaterialsprepare.mopickitemd;

import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.rjtech.model.momdata.MopickitemdBatch;
/**
 * 生产订单-备料单存货现品票明细
 * @ClassName: MopickitemdBatchService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-09 15:29
 */
public class MopickitemdBatchService extends BaseService<MopickitemdBatch> {
	private final MopickitemdBatch dao=new MopickitemdBatch().dao();
	@Override
	protected MopickitemdBatch dao() {
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
	public Page<MopickitemdBatch> getAdminDatas(int pageNumber, int pageSize) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
        //排序
        sql.desc("iAutoId");
		return paginate(sql);
	}

	/**
	 * 保存
	 * @param mopickitemdBatch
	 * @return
	 */
	public Ret save(MopickitemdBatch mopickitemdBatch) {
		if(mopickitemdBatch==null || isOk(mopickitemdBatch.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(mopickitemdBatch.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=mopickitemdBatch.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(mopickitemdBatch.getIAutoId(), JBoltUserKit.getUserId(), mopickitemdBatch.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param mopickitemdBatch
	 * @return
	 */
	public Ret update(MopickitemdBatch mopickitemdBatch) {
		if(mopickitemdBatch==null || notOk(mopickitemdBatch.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		MopickitemdBatch dbMopickitemdBatch=findById(mopickitemdBatch.getIAutoId());
		if(dbMopickitemdBatch==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(mopickitemdBatch.getName(), mopickitemdBatch.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=mopickitemdBatch.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(mopickitemdBatch.getIAutoId(), JBoltUserKit.getUserId(), mopickitemdBatch.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param mopickitemdBatch 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(MopickitemdBatch mopickitemdBatch, Kv kv) {
		//addDeleteSystemLog(mopickitemdBatch.getIAutoId(), JBoltUserKit.getUserId(),mopickitemdBatch.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param mopickitemdBatch model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(MopickitemdBatch mopickitemdBatch, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

}