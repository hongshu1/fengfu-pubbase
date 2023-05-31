package cn.rjtech.admin.otheroutdetail;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.OtherOutDetail;
import cn.rjtech.model.momdata.SysSaledeliverdetail;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;

import java.util.List;

/**
 * 出库管理-特殊领料单列表 Service
 * @ClassName: OtherOutDetailService
 * @author: RJ
 * @date: 2023-05-06 15:06
 */
public class OtherOutDetailService extends BaseService<OtherOutDetail> {

	private final OtherOutDetail dao = new OtherOutDetail().dao();

	@Override
	protected OtherOutDetail dao() {
		return dao;
	}

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @return
	 */
	public Page<OtherOutDetail> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
		return paginateByKeywords("iAutoId","DESC", pageNumber, pageSize, keywords, "iAutoId");
	}

	/**
	 * 保存
	 * @param otherOutDetail
	 * @return
	 */
	public Ret save(OtherOutDetail otherOutDetail) {
		if(otherOutDetail==null || isOk(otherOutDetail.getAutoID())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(otherOutDetail.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=otherOutDetail.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(otherOutDetail.getAutoid(), JBoltUserKit.getUserId(), otherOutDetail.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param otherOutDetail
	 * @return
	 */
	public Ret update(OtherOutDetail otherOutDetail) {
		if(otherOutDetail==null || notOk(otherOutDetail.getAutoID())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		OtherOutDetail dbOtherOutDetail=findById(otherOutDetail.getAutoID());
		if(dbOtherOutDetail==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(otherOutDetail.getName(), otherOutDetail.getAutoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=otherOutDetail.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(otherOutDetail.getAutoid(), JBoltUserKit.getUserId(), otherOutDetail.getName());
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
			delete("delete from T_Sys_OtherOutDetail where MasID = '"+ids+"'");
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
	 * @param otherOutDetail 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(OtherOutDetail otherOutDetail, Kv kv) {
		//addDeleteSystemLog(otherOutDetail.getAutoid(), JBoltUserKit.getUserId(),otherOutDetail.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param otherOutDetail 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(OtherOutDetail otherOutDetail, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(otherOutDetail, kv);
	}

	/**
	 * 设置返回二开业务所属的关键systemLog的targetType 
	 * @return
	 */
	@Override
	protected int systemLogTargetType() {
		return ProjectSystemLogTargetType.NONE.getValue();
	}

	public List<OtherOutDetail> getpushudetail(Kv kv) {
		List<OtherOutDetail> datas = daoTemplate("otheroutreturnlist.pushudetail",kv).find();
		return datas;
	}

}