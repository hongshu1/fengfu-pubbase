package cn.rjtech.admin.moroutingconfigequipment;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.MoMoroutingequipment;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
/**
 * 制造工单-生产工艺路线设备 Service
 * @ClassName: MoMoroutingequipmentService
 * @author: RJ
 * @date: 2023-05-09 16:52
 */
public class MoMoroutingequipmentService extends BaseService<MoMoroutingequipment> {

	private final MoMoroutingequipment dao = new MoMoroutingequipment().dao();

	@Override
	protected MoMoroutingequipment dao() {
		return dao;
	}

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @return
	 */
	public Page<MoMoroutingequipment> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
		return paginateByKeywords("iAutoId","DESC", pageNumber, pageSize, keywords, "iAutoId");
	}

	/**
	 * 保存
	 * @param moMoroutingequipment
	 * @return
	 */
	public Ret save(MoMoroutingequipment moMoroutingequipment) {
		if(moMoroutingequipment==null || isOk(moMoroutingequipment.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(moMoroutingequipment.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=moMoroutingequipment.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(moMoroutingequipment.getIAutoId(), JBoltUserKit.getUserId(), moMoroutingequipment.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param moMoroutingequipment
	 * @return
	 */
	public Ret update(MoMoroutingequipment moMoroutingequipment) {
		if(moMoroutingequipment==null || notOk(moMoroutingequipment.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		MoMoroutingequipment dbMoMoroutingequipment=findById(moMoroutingequipment.getIAutoId());
		if(dbMoMoroutingequipment==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(moMoroutingequipment.getName(), moMoroutingequipment.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=moMoroutingequipment.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(moMoroutingequipment.getIAutoId(), JBoltUserKit.getUserId(), moMoroutingequipment.getName());
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
	 * @param moMoroutingequipment 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(MoMoroutingequipment moMoroutingequipment, Kv kv) {
		//addDeleteSystemLog(moMoroutingequipment.getIAutoId(), JBoltUserKit.getUserId(),moMoroutingequipment.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param moMoroutingequipment 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(MoMoroutingequipment moMoroutingequipment, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(moMoroutingequipment, kv);
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
