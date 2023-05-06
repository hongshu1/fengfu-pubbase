package cn.rjtech.admin.otherout;

import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Db;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.OtherOut;
/**
 * 出库管理-特殊领料单列表 Service
 * @ClassName: OtherOutService
 * @author: RJ
 * @date: 2023-05-06 15:05
 */
public class OtherOutService extends BaseService<OtherOut> {

	private final OtherOut dao = new OtherOut().dao();

	@Override
	protected OtherOut dao() {
		return dao;
	}

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @return
	 */
	public Page<OtherOut> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
		return paginateByKeywords("iAutoId","DESC", pageNumber, pageSize, keywords, "iAutoId");
	}

	/**
	 * 保存
	 * @param otherOut
	 * @return
	 */
	public Ret save(OtherOut otherOut) {
		if(otherOut==null || isOk(otherOut.getAutoID())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(otherOut.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=otherOut.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(otherOut.getAutoid(), JBoltUserKit.getUserId(), otherOut.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param otherOut
	 * @return
	 */
	public Ret update(OtherOut otherOut) {
		if(otherOut==null || notOk(otherOut.getAutoID())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		OtherOut dbOtherOut=findById(otherOut.getAutoID());
		if(dbOtherOut==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(otherOut.getName(), otherOut.getAutoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=otherOut.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(otherOut.getAutoid(), JBoltUserKit.getUserId(), otherOut.getName());
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
	 * @param otherOut 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(OtherOut otherOut, Kv kv) {
		//addDeleteSystemLog(otherOut.getAutoid(), JBoltUserKit.getUserId(),otherOut.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param otherOut 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(OtherOut otherOut, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(otherOut, kv);
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