package cn.rjtech.admin.instockqcformdline;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.InstockqcformdLine;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
/**
 * 质量管理-在库检单行配置 Service
 * @ClassName: InstockqcformdLineService
 * @author: RJ
 * @date: 2023-05-04 14:26
 */
public class InstockqcformdLineService extends BaseService<InstockqcformdLine> {

	private final InstockqcformdLine dao = new InstockqcformdLine().dao();

	@Override
	protected InstockqcformdLine dao() {
		return dao;
	}

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @return
	 */
	public Page<InstockqcformdLine> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
		return paginateByKeywords("iAutoId","DESC", pageNumber, pageSize, keywords, "iAutoId");
	}

	/**
	 * 保存
	 * @param instockqcformdLine
	 * @return
	 */
	public Ret save(InstockqcformdLine instockqcformdLine) {
		if(instockqcformdLine==null || isOk(instockqcformdLine.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(instockqcformdLine.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=instockqcformdLine.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(instockqcformdLine.getIAutoId(), JBoltUserKit.getUserId(), instockqcformdLine.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param instockqcformdLine
	 * @return
	 */
	public Ret update(InstockqcformdLine instockqcformdLine) {
		if(instockqcformdLine==null || notOk(instockqcformdLine.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		InstockqcformdLine dbInstockqcformdLine=findById(instockqcformdLine.getIAutoId());
		if(dbInstockqcformdLine==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(instockqcformdLine.getName(), instockqcformdLine.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=instockqcformdLine.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(instockqcformdLine.getIAutoId(), JBoltUserKit.getUserId(), instockqcformdLine.getName());
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
	 * @param instockqcformdLine 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(InstockqcformdLine instockqcformdLine, Kv kv) {
		//addDeleteSystemLog(instockqcformdLine.getIAutoId(), JBoltUserKit.getUserId(),instockqcformdLine.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param instockqcformdLine 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(InstockqcformdLine instockqcformdLine, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(instockqcformdLine, kv);
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