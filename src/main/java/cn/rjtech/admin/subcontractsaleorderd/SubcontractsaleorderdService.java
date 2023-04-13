package cn.rjtech.admin.subcontractsaleorderd;

import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.Subcontractsaleorderd;
/**
 * 委外销售订单明细 Service
 * @ClassName: SubcontractsaleorderdService
 * @author: RJ
 * @date: 2023-04-12 19:08
 */
public class SubcontractsaleorderdService extends BaseService<Subcontractsaleorderd> {

	private final Subcontractsaleorderd dao = new Subcontractsaleorderd().dao();

	@Override
	protected Subcontractsaleorderd dao() {
		return dao;
	}

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @return
	 */
	public Page<Subcontractsaleorderd> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
		return paginateByKeywords("iAutoId","DESC", pageNumber, pageSize, keywords, "iAutoId");
	}

	/**
	 * 保存
	 * @param subcontractsaleorderd
	 * @return
	 */
	public Ret save(Subcontractsaleorderd subcontractsaleorderd) {
		if(subcontractsaleorderd==null || isOk(subcontractsaleorderd.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(subcontractsaleorderd.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=subcontractsaleorderd.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(subcontractsaleorderd.getIautoid(), JBoltUserKit.getUserId(), subcontractsaleorderd.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param subcontractsaleorderd
	 * @return
	 */
	public Ret update(Subcontractsaleorderd subcontractsaleorderd) {
		if(subcontractsaleorderd==null || notOk(subcontractsaleorderd.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		Subcontractsaleorderd dbSubcontractsaleorderd=findById(subcontractsaleorderd.getIAutoId());
		if(dbSubcontractsaleorderd==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(subcontractsaleorderd.getName(), subcontractsaleorderd.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=subcontractsaleorderd.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(subcontractsaleorderd.getIautoid(), JBoltUserKit.getUserId(), subcontractsaleorderd.getName());
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
	 * @param subcontractsaleorderd 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(Subcontractsaleorderd subcontractsaleorderd, Kv kv) {
		//addDeleteSystemLog(subcontractsaleorderd.getIautoid(), JBoltUserKit.getUserId(),subcontractsaleorderd.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param subcontractsaleorderd 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(Subcontractsaleorderd subcontractsaleorderd, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(subcontractsaleorderd, kv);
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
	 * @param subcontractsaleorderd 要toggle的model
	 * @param column 操作的哪一列
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanToggle(Subcontractsaleorderd subcontractsaleorderd,String column, Kv kv) {
		//没有问题就返回null  有问题就返回提示string 字符串
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(Subcontractsaleorderd subcontractsaleorderd, String column, Kv kv) {
		//addUpdateSystemLog(subcontractsaleorderd.getIautoid(), JBoltUserKit.getUserId(), subcontractsaleorderd.getName(),"的字段["+column+"]值:"+subcontractsaleorderd.get(column));
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param subcontractsaleorderd model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(Subcontractsaleorderd subcontractsaleorderd, Kv kv) {
		//这里用来覆盖 检测Subcontractsaleorderd是否被其它表引用
		return null;
	}

}