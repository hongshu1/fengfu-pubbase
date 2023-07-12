package cn.rjtech.admin.momopatchweldd;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.MoMopatchweldd;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
/**
 * 补焊纪录明细 Service
 * @ClassName: MoMopatchwelddService
 * @author: chentao
 * @date: 2023-06-27 10:18
 */
public class MoMopatchwelddService extends BaseService<MoMopatchweldd> {

	private final MoMopatchweldd dao = new MoMopatchweldd().dao();

	@Override
	protected MoMopatchweldd dao() {
		return dao;
	}

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @return
	 */
	public Page<MoMopatchweldd> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
		return paginateByKeywords("iAutoId","DESC", pageNumber, pageSize, keywords, "iAutoId");
	}

	/**
	 * 保存
	 * @param moMopatchweldd
	 * @return
	 */
	public Ret save(MoMopatchweldd moMopatchweldd) {
		if(moMopatchweldd==null || isOk(moMopatchweldd.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(moMopatchweldd.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=moMopatchweldd.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(moMopatchweldd.getIautoid(), JBoltUserKit.getUserId(), moMopatchweldd.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param moMopatchweldd
	 * @return
	 */
	public Ret update(MoMopatchweldd moMopatchweldd) {
		if(moMopatchweldd==null || notOk(moMopatchweldd.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		MoMopatchweldd dbMoMopatchweldd=findById(moMopatchweldd.getIAutoId());
		if(dbMoMopatchweldd==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(moMopatchweldd.getName(), moMopatchweldd.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=moMopatchweldd.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(moMopatchweldd.getIautoid(), JBoltUserKit.getUserId(), moMopatchweldd.getName());
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
	 * @param moMopatchweldd 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(MoMopatchweldd moMopatchweldd, Kv kv) {
		//addDeleteSystemLog(moMopatchweldd.getIautoid(), JBoltUserKit.getUserId(),moMopatchweldd.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param moMopatchweldd 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(MoMopatchweldd moMopatchweldd, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(moMopatchweldd, kv);
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