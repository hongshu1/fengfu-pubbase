package cn.rjtech.admin.morouting;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.MoMorouting;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
/**
 * 制造工单-生产工艺路线 Service
 * @ClassName: MoMoroutingService
 * @author: RJ
 * @date: 2023-05-09 16:38
 */
public class MoMoroutingService extends BaseService<MoMorouting> {

	private final MoMorouting dao = new MoMorouting().dao();

	@Override
	protected MoMorouting dao() {
		return dao;
	}

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @return
	 */
	public Page<MoMorouting> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
		return paginateByKeywords("iAutoId","DESC", pageNumber, pageSize, keywords, "iAutoId");
	}
	/**
	 * 保存
	 * @param moMorouting
	 * @return
	 */
	public Ret save(MoMorouting moMorouting) {
		if(moMorouting==null || isOk(moMorouting.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(moMorouting.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=moMorouting.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(moMorouting.getIAutoId(), JBoltUserKit.getUserId(), moMorouting.getName());
		}
		return ret(success);
	}

	/**
	 * 检测是否可以删除
	 * @param moMorouting 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(MoMorouting moMorouting, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(moMorouting, kv);
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
