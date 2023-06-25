package cn.rjtech.admin.transvouchdetail;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.TransVouchDetail;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
/**
 * 出库管理-调拨单列表 Service
 * @ClassName: TransVouchDetailService
 * @author: RJ
 * @date: 2023-05-11 14:55
 */
public class TransVouchDetailService extends BaseService<TransVouchDetail> {

	private final TransVouchDetail dao = new TransVouchDetail().dao();

	@Override
	protected TransVouchDetail dao() {
		return dao;
	}

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @return
	 */
	public Page<TransVouchDetail> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
		return paginateByKeywords("iAutoId","DESC", pageNumber, pageSize, keywords, "iAutoId");
	}

	/**
	 * 保存
	 * @param transVouchDetail
	 * @return
	 */
	public Ret save(TransVouchDetail transVouchDetail) {
		if(transVouchDetail==null || isOk(transVouchDetail.getAutoID())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(transVouchDetail.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=transVouchDetail.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(transVouchDetail.getAutoid(), JBoltUserKit.getUserId(), transVouchDetail.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param transVouchDetail
	 * @return
	 */
	public Ret update(TransVouchDetail transVouchDetail) {
		if(transVouchDetail==null || notOk(transVouchDetail.getAutoID())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		TransVouchDetail dbTransVouchDetail=findById(transVouchDetail.getAutoID());
		if(dbTransVouchDetail==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(transVouchDetail.getName(), transVouchDetail.getAutoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=transVouchDetail.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(transVouchDetail.getAutoid(), JBoltUserKit.getUserId(), transVouchDetail.getName());
		}
		return ret(success);
	}

	/**
	 * 删除 指定多个ID
	 * @param ids
	 * @return
	 */
	public Ret deleteByBatchIds(String ids) {
		tx(() -> {
			ValidationUtils.notNull(ids, JBoltMsg.DATA_NOT_EXIST);
			delete("delete from T_Sys_TransVouchDetail where MasID = '" + ids + "'");
			return true;
		});
		return SUCCESS;
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
	 * @param transVouchDetail 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(TransVouchDetail transVouchDetail, Kv kv) {
		//addDeleteSystemLog(transVouchDetail.getAutoid(), JBoltUserKit.getUserId(),transVouchDetail.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param transVouchDetail 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(TransVouchDetail transVouchDetail, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(transVouchDetail, kv);
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