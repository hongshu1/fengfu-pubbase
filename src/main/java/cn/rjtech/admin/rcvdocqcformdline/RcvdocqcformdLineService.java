package cn.rjtech.admin.rcvdocqcformdline;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.RcvdocqcformdLine;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;

/**
 * 质量管理-来料检明细列值表 Service
 * @ClassName: RcvdocqcformdLineService
 * @author: RJ
 * @date: 2023-04-13 16:53
 */
public class RcvdocqcformdLineService extends BaseService<RcvdocqcformdLine> {

	private final RcvdocqcformdLine dao = new RcvdocqcformdLine().dao();

	@Override
	protected RcvdocqcformdLine dao() {
		return dao;
	}

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @return
	 */
	public Page<RcvdocqcformdLine> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
		return paginateByKeywords("iAutoId","DESC", pageNumber, pageSize, keywords, "iAutoId");
	}

	/**
	 * 保存
	 * @param rcvdocqcformdLine
	 * @return
	 */
	public Ret save(RcvdocqcformdLine rcvdocqcformdLine) {
		if(rcvdocqcformdLine==null || isOk(rcvdocqcformdLine.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(rcvdocqcformdLine.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=rcvdocqcformdLine.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(rcvdocqcformdLine.getIAutoId(), JBoltUserKit.getUserId(), rcvdocqcformdLine.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param rcvdocqcformdLine
	 * @return
	 */
	public Ret update(RcvdocqcformdLine rcvdocqcformdLine) {
		if(rcvdocqcformdLine==null || notOk(rcvdocqcformdLine.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		RcvdocqcformdLine dbRcvdocqcformdLine=findById(rcvdocqcformdLine.getIAutoId());
		if(dbRcvdocqcformdLine==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(rcvdocqcformdLine.getName(), rcvdocqcformdLine.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=rcvdocqcformdLine.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(rcvdocqcformdLine.getIAutoId(), JBoltUserKit.getUserId(), rcvdocqcformdLine.getName());
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
	 * @param rcvdocqcformdLine 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(RcvdocqcformdLine rcvdocqcformdLine, Kv kv) {
		//addDeleteSystemLog(rcvdocqcformdLine.getIAutoId(), JBoltUserKit.getUserId(),rcvdocqcformdLine.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param rcvdocqcformdLine 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(RcvdocqcformdLine rcvdocqcformdLine, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(rcvdocqcformdLine, kv);
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