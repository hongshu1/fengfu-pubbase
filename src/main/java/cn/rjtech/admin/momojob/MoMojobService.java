package cn.rjtech.admin.momojob;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.MoMojob;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
/**
 * 制造工单-生产任务
 * @ClassName: MoMojobService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-06-27 15:24
 */
public class MoMojobService extends BaseService<MoMojob> {
	private final MoMojob dao=new MoMojob().dao();
	@Override
	protected MoMojob dao() {
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
	public Page<MoMojob> getAdminDatas(int pageNumber, int pageSize) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
        //排序
        sql.desc("iAutoId");
		return paginate(sql);
	}

	/**
	 * 保存
	 * @param moMojob
	 * @return
	 */
	public Ret save(MoMojob moMojob) {
		if(moMojob==null || isOk(moMojob.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		boolean success=moMojob.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(moMojob.getIAutoId(), JBoltUserKit.getUserId(), moMojob.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param moMojob
	 * @return
	 */
	public Ret update(MoMojob moMojob) {
		if(moMojob==null || notOk(moMojob.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		MoMojob dbMoMojob=findById(moMojob.getIAutoId());
		if(dbMoMojob==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		boolean success=moMojob.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(moMojob.getIAutoId(), JBoltUserKit.getUserId(), moMojob.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param moMojob 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(MoMojob moMojob, Kv kv) {
		//addDeleteSystemLog(moMojob.getIAutoId(), JBoltUserKit.getUserId(),moMojob.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param moMojob model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(MoMojob moMojob, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

}