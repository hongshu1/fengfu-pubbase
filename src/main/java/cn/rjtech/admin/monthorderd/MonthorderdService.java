package cn.rjtech.admin.monthorderd;

import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.Monthorderd;
/**
 * 月度计划订单明细 Service
 * @ClassName: MonthorderdService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-10 18:20
 */
public class MonthorderdService extends BaseService<Monthorderd> {

	private final Monthorderd dao = new Monthorderd().dao();

	@Override
	protected Monthorderd dao() {
		return dao;
	}

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @return
	 */
	public Page<Monthorderd> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
		return paginateByKeywords("iAutoId","DESC", pageNumber, pageSize, keywords, "iAutoId");
	}

	/**
	 * 保存
	 * @param monthorderd
	 * @return
	 */
	public Ret save(Monthorderd monthorderd) {
		if(monthorderd==null || isOk(monthorderd.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(monthorderd.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=monthorderd.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(monthorderd.getIautoid(), JBoltUserKit.getUserId(), monthorderd.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param monthorderd
	 * @return
	 */
	public Ret update(Monthorderd monthorderd) {
		if(monthorderd==null || notOk(monthorderd.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		Monthorderd dbMonthorderd=findById(monthorderd.getIAutoId());
		if(dbMonthorderd==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(monthorderd.getName(), monthorderd.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=monthorderd.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(monthorderd.getIautoid(), JBoltUserKit.getUserId(), monthorderd.getName());
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
	 * @param monthorderd 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(Monthorderd monthorderd, Kv kv) {
		//addDeleteSystemLog(monthorderd.getIautoid(), JBoltUserKit.getUserId(),monthorderd.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param monthorderd 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(Monthorderd monthorderd, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(monthorderd, kv);
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
	 * @param monthorderd 要toggle的model
	 * @param column 操作的哪一列
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanToggle(Monthorderd monthorderd,String column, Kv kv) {
		//没有问题就返回null  有问题就返回提示string 字符串
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(Monthorderd monthorderd, String column, Kv kv) {
		//addUpdateSystemLog(monthorderd.getIautoid(), JBoltUserKit.getUserId(), monthorderd.getName(),"的字段["+column+"]值:"+monthorderd.get(column));
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param monthorderd model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(Monthorderd monthorderd, Kv kv) {
		//这里用来覆盖 检测Monthorderd是否被其它表引用
		return null;
	}

}