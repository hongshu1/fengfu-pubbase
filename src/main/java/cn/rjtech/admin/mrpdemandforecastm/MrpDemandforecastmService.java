package cn.rjtech.admin.mrpdemandforecastm;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.MrpDemandforecastm;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
/**
 * 物料需求预示主表 Service
 * @ClassName: MrpDemandforecastmService
 * @author: chentao
 * @date: 2023-05-06 15:15
 */
public class MrpDemandforecastmService extends BaseService<MrpDemandforecastm> {

	private final MrpDemandforecastm dao = new MrpDemandforecastm().dao();

	@Override
	protected MrpDemandforecastm dao() {
		return dao;
	}

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @return
	 */
	public Page<MrpDemandforecastm> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
		return paginateByKeywords("iAutoId","DESC", pageNumber, pageSize, keywords, "iAutoId");
	}

	/**
	 * 保存
	 * @param mrpDemandforecastm
	 * @return
	 */
	public Ret save(MrpDemandforecastm mrpDemandforecastm) {
		if(mrpDemandforecastm==null || isOk(mrpDemandforecastm.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(mrpDemandforecastm.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=mrpDemandforecastm.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(mrpDemandforecastm.getIautoid(), JBoltUserKit.getUserId(), mrpDemandforecastm.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param mrpDemandforecastm
	 * @return
	 */
	public Ret update(MrpDemandforecastm mrpDemandforecastm) {
		if(mrpDemandforecastm==null || notOk(mrpDemandforecastm.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		MrpDemandforecastm dbMrpDemandforecastm=findById(mrpDemandforecastm.getIAutoId());
		if(dbMrpDemandforecastm==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(mrpDemandforecastm.getName(), mrpDemandforecastm.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=mrpDemandforecastm.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(mrpDemandforecastm.getIautoid(), JBoltUserKit.getUserId(), mrpDemandforecastm.getName());
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
	 * @param mrpDemandforecastm 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(MrpDemandforecastm mrpDemandforecastm, Kv kv) {
		//addDeleteSystemLog(mrpDemandforecastm.getIautoid(), JBoltUserKit.getUserId(),mrpDemandforecastm.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param mrpDemandforecastm 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(MrpDemandforecastm mrpDemandforecastm, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(mrpDemandforecastm, kv);
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
	 * 切换isdeleted属性
	 */
	public Ret toggleIsDeleted(Long id) {
		return toggleBoolean(id, "IsDeleted");
	}

	/**
	 * 检测是否可以toggle操作指定列
	 * @param mrpDemandforecastm 要toggle的model
	 * @param column 操作的哪一列
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanToggle(MrpDemandforecastm mrpDemandforecastm,String column, Kv kv) {
		//没有问题就返回null  有问题就返回提示string 字符串
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(MrpDemandforecastm mrpDemandforecastm, String column, Kv kv) {
		//addUpdateSystemLog(mrpDemandforecastm.getIautoid(), JBoltUserKit.getUserId(), mrpDemandforecastm.getName(),"的字段["+column+"]值:"+mrpDemandforecastm.get(column));
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param mrpDemandforecastm model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(MrpDemandforecastm mrpDemandforecastm, Kv kv) {
		//这里用来覆盖 检测MrpDemandforecastm是否被其它表引用
		return null;
	}

}