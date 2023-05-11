package cn.rjtech.admin.transvouch;

import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Db;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.TransVouch;
/**
 * 出库管理-调拨单列表 Service
 * @ClassName: TransVouchService
 * @author: RJ
 * @date: 2023-05-11 14:54
 */
public class TransVouchService extends BaseService<TransVouch> {

	private final TransVouch dao = new TransVouch().dao();

	@Override
	protected TransVouch dao() {
		return dao;
	}

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @return
	 */
	public Page<TransVouch> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
		return paginateByKeywords("iAutoId","DESC", pageNumber, pageSize, keywords, "iAutoId");
	}

	/**
	 * 保存
	 * @param transVouch
	 * @return
	 */
	public Ret save(TransVouch transVouch) {
		if(transVouch==null || isOk(transVouch.getAutoID())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(transVouch.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=transVouch.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(transVouch.getAutoid(), JBoltUserKit.getUserId(), transVouch.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param transVouch
	 * @return
	 */
	public Ret update(TransVouch transVouch) {
		if(transVouch==null || notOk(transVouch.getAutoID())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		TransVouch dbTransVouch=findById(transVouch.getAutoID());
		if(dbTransVouch==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(transVouch.getName(), transVouch.getAutoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=transVouch.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(transVouch.getAutoid(), JBoltUserKit.getUserId(), transVouch.getName());
		}
		return ret(success);
	}

	/**
	 * 删除 指定多个ID
	 * @param ids
	 * @return
	 */
	public Ret deleteByBatchIds(String ids) {
		return deleteByIds(ids,true);
	}

	/**
	 * 删除
	 * @param id
	 * @return
	 */
	public Ret delete(Long id) {
		return deleteById(id,true);
	}

	/**
	 * 删除数据后执行的回调
	 * @param transVouch 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(TransVouch transVouch, Kv kv) {
		//addDeleteSystemLog(transVouch.getAutoid(), JBoltUserKit.getUserId(),transVouch.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param transVouch 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(TransVouch transVouch, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(transVouch, kv);
	}

	/**
	 * 设置返回二开业务所属的关键systemLog的targetType 
	 * @return
	 */
	@Override
	protected int systemLogTargetType() {
		return ProjectSystemLogTargetType.NONE.getValue();
	}

}