package cn.rjtech.admin.moroutingcinve;

import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.MoMoroutinginvc;
/**
 * 制造工单-生产工艺路线物料 Service
 * @ClassName: MoMoroutinginvcService
 * @author: RJ
 * @date: 2023-05-09 16:55
 */
public class MoMoroutinginvcService extends BaseService<MoMoroutinginvc> {

	private final MoMoroutinginvc dao = new MoMoroutinginvc().dao();

	@Override
	protected MoMoroutinginvc dao() {
		return dao;
	}

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @return
	 */
	public Page<MoMoroutinginvc> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
		return paginateByKeywords("iAutoId","DESC", pageNumber, pageSize, keywords, "iAutoId");
	}

	/**
	 * 保存
	 * @param moMoroutinginvc
	 * @return
	 */
	public Ret save(MoMoroutinginvc moMoroutinginvc) {
		if(moMoroutinginvc==null || isOk(moMoroutinginvc.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(moMoroutinginvc.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=moMoroutinginvc.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(moMoroutinginvc.getIAutoId(), JBoltUserKit.getUserId(), moMoroutinginvc.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param moMoroutinginvc
	 * @return
	 */
	public Ret update(MoMoroutinginvc moMoroutinginvc) {
		if(moMoroutinginvc==null || notOk(moMoroutinginvc.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		MoMoroutinginvc dbMoMoroutinginvc=findById(moMoroutinginvc.getIAutoId());
		if(dbMoMoroutinginvc==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(moMoroutinginvc.getName(), moMoroutinginvc.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=moMoroutinginvc.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(moMoroutinginvc.getIAutoId(), JBoltUserKit.getUserId(), moMoroutinginvc.getName());
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
	 * @param moMoroutinginvc 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(MoMoroutinginvc moMoroutinginvc, Kv kv) {
		//addDeleteSystemLog(moMoroutinginvc.getIAutoId(), JBoltUserKit.getUserId(),moMoroutinginvc.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param moMoroutinginvc 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(MoMoroutinginvc moMoroutinginvc, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(moMoroutinginvc, kv);
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
