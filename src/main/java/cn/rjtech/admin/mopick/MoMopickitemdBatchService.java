package cn.rjtech.admin.mopick;

import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import cn.rjtech.model.mopick.MoMopickitemdBatch;
/**
 * 材料耗用 Service
 * @ClassName: MoMopickitemdBatchService
 * @author: RJ
 * @date: 2023-05-09 15:36
 */
public class MoMopickitemdBatchService extends BaseService<MoMopickitemdBatch> {

	private final MoMopickitemdBatch dao = new MoMopickitemdBatch().dao();

	@Override
	protected MoMopickitemdBatch dao() {
		return dao;
	}

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @return
	 */
	public Page<MoMopickitemdBatch> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
		return paginateByKeywords("iAutoId","DESC", pageNumber, pageSize, keywords, "iAutoId");
	}

	/**
	 * 检测是否可以删除
	 * @param moMopickitemdBatch 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(MoMopickitemdBatch moMopickitemdBatch, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(moMopickitemdBatch, kv);
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
