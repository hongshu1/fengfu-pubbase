package cn.rjtech.admin.mrpdemandcomputed;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.MrpDemandcomputed;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
/**
 * 物料需求计划 Service
 * @ClassName: MrpDemandcomputedService
 * @author: chentao
 * @date: 2023-05-04 15:01
 */
public class MrpDemandcomputedService extends BaseService<MrpDemandcomputed> {

	private final MrpDemandcomputed dao = new MrpDemandcomputed().dao();

	@Override
	protected MrpDemandcomputed dao() {
		return dao;
	}

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @return
	 */
	public Page<MrpDemandcomputed> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
		return paginateByKeywords("iAutoId","DESC", pageNumber, pageSize, keywords, "iAutoId");
	}

	/**
	 * 保存
	 * @param mrpDemandcomputed
	 * @return
	 */
	public Ret save(MrpDemandcomputed mrpDemandcomputed) {
		if(mrpDemandcomputed==null || isOk(mrpDemandcomputed.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(mrpDemandcomputed.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=mrpDemandcomputed.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(mrpDemandcomputed.getIautoid(), JBoltUserKit.getUserId(), mrpDemandcomputed.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param mrpDemandcomputed
	 * @return
	 */
	public Ret update(MrpDemandcomputed mrpDemandcomputed) {
		if(mrpDemandcomputed==null || notOk(mrpDemandcomputed.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		MrpDemandcomputed dbMrpDemandcomputed=findById(mrpDemandcomputed.getIAutoId());
		if(dbMrpDemandcomputed==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(mrpDemandcomputed.getName(), mrpDemandcomputed.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=mrpDemandcomputed.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(mrpDemandcomputed.getIautoid(), JBoltUserKit.getUserId(), mrpDemandcomputed.getName());
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
	 * @param mrpDemandcomputed 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(MrpDemandcomputed mrpDemandcomputed, Kv kv) {
		//addDeleteSystemLog(mrpDemandcomputed.getIautoid(), JBoltUserKit.getUserId(),mrpDemandcomputed.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param mrpDemandcomputed 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(MrpDemandcomputed mrpDemandcomputed, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(mrpDemandcomputed, kv);
	}

	/**
	 * 设置返回二开业务所属的关键systemLog的targetType 
	 * @return
	 */
	@Override
	protected int systemLogTargetType() {
		return ProjectSystemLogTargetType.NONE.getValue();
	}

	/**
	 * 切换islocked1属性
	 */
	public Ret toggleIsLocked1(Long id) {
		return toggleBoolean(id, "isLocked1");
	}

	/**
	 * 切换islocked3属性
	 */
	public Ret toggleIsLocked3(Long id) {
		return toggleBoolean(id, "isLocked3");
	}

	/**
	 * 切换islocked2属性
	 */
	public Ret toggleIsLocked2(Long id) {
		return toggleBoolean(id, "isLocked2");
	}

	/**
	 * 检测是否可以toggle操作指定列
	 * @param mrpDemandcomputed 要toggle的model
	 * @param column 操作的哪一列
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanToggle(MrpDemandcomputed mrpDemandcomputed,String column, Kv kv) {
		//没有问题就返回null  有问题就返回提示string 字符串
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(MrpDemandcomputed mrpDemandcomputed, String column, Kv kv) {
		//addUpdateSystemLog(mrpDemandcomputed.getIautoid(), JBoltUserKit.getUserId(), mrpDemandcomputed.getName(),"的字段["+column+"]值:"+mrpDemandcomputed.get(column));
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param mrpDemandcomputed model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(MrpDemandcomputed mrpDemandcomputed, Kv kv) {
		//这里用来覆盖 检测MrpDemandcomputed是否被其它表引用
		return null;
	}

}