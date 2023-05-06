package cn.rjtech.admin.schedudemandplan;

import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Db;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.MrpDemandcomputem;
/**
 * 物料需求计划 Service
 * @ClassName: MrpDemandcomputemService
 * @author: chentao
 * @date: 2023-05-02 10:00
 */
public class MrpDemandcomputemService extends BaseService<MrpDemandcomputem> {

	private final MrpDemandcomputem dao = new MrpDemandcomputem().dao();

	@Override
	protected MrpDemandcomputem dao() {
		return dao;
	}

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @return
	 */
	public Page<MrpDemandcomputem> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
		return paginateByKeywords("iAutoId","DESC", pageNumber, pageSize, keywords, "iAutoId");
	}

	/**
	 * 保存
	 * @param mrpDemandcomputem
	 * @return
	 */
	public Ret save(MrpDemandcomputem mrpDemandcomputem) {
		if(mrpDemandcomputem==null || isOk(mrpDemandcomputem.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(mrpDemandcomputem.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=mrpDemandcomputem.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(mrpDemandcomputem.getIautoid(), JBoltUserKit.getUserId(), mrpDemandcomputem.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param mrpDemandcomputem
	 * @return
	 */
	public Ret update(MrpDemandcomputem mrpDemandcomputem) {
		if(mrpDemandcomputem==null || notOk(mrpDemandcomputem.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		MrpDemandcomputem dbMrpDemandcomputem=findById(mrpDemandcomputem.getIAutoId());
		if(dbMrpDemandcomputem==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(mrpDemandcomputem.getName(), mrpDemandcomputem.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=mrpDemandcomputem.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(mrpDemandcomputem.getIautoid(), JBoltUserKit.getUserId(), mrpDemandcomputem.getName());
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
	 * @param mrpDemandcomputem 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(MrpDemandcomputem mrpDemandcomputem, Kv kv) {
		//addDeleteSystemLog(mrpDemandcomputem.getIautoid(), JBoltUserKit.getUserId(),mrpDemandcomputem.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param mrpDemandcomputem 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(MrpDemandcomputem mrpDemandcomputem, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(mrpDemandcomputem, kv);
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
	 * @param mrpDemandcomputem 要toggle的model
	 * @param column 操作的哪一列
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanToggle(MrpDemandcomputem mrpDemandcomputem,String column, Kv kv) {
		//没有问题就返回null  有问题就返回提示string 字符串
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(MrpDemandcomputem mrpDemandcomputem, String column, Kv kv) {
		//addUpdateSystemLog(mrpDemandcomputem.getIautoid(), JBoltUserKit.getUserId(), mrpDemandcomputem.getName(),"的字段["+column+"]值:"+mrpDemandcomputem.get(column));
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param mrpDemandcomputem model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(MrpDemandcomputem mrpDemandcomputem, Kv kv) {
		//这里用来覆盖 检测MrpDemandcomputem是否被其它表引用
		return null;
	}

}