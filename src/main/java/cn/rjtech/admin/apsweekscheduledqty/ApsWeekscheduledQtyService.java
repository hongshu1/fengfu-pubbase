package cn.rjtech.admin.apsweekscheduledqty;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.ApsWeekscheduledQty;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
/**
 * 月周生产计划排产明细数量 Service
 * @ClassName: ApsWeekscheduledQtyService
 * @author: chentao
 * @date: 2023-04-21 10:52
 */
public class ApsWeekscheduledQtyService extends BaseService<ApsWeekscheduledQty> {

	private final ApsWeekscheduledQty dao = new ApsWeekscheduledQty().dao();

	@Override
	protected ApsWeekscheduledQty dao() {
		return dao;
	}

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @return
	 */
	public Page<ApsWeekscheduledQty> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
		return paginateByKeywords("iAutoId","DESC", pageNumber, pageSize, keywords, "iAutoId");
	}

	/**
	 * 保存
	 * @param apsWeekscheduledQty
	 * @return
	 */
	public Ret save(ApsWeekscheduledQty apsWeekscheduledQty) {
		if(apsWeekscheduledQty==null || isOk(apsWeekscheduledQty.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(apsWeekscheduledQty.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=apsWeekscheduledQty.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(apsWeekscheduledQty.getIautoid(), JBoltUserKit.getUserId(), apsWeekscheduledQty.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param apsWeekscheduledQty
	 * @return
	 */
	public Ret update(ApsWeekscheduledQty apsWeekscheduledQty) {
		if(apsWeekscheduledQty==null || notOk(apsWeekscheduledQty.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		ApsWeekscheduledQty dbApsWeekscheduledQty=findById(apsWeekscheduledQty.getIAutoId());
		if(dbApsWeekscheduledQty==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(apsWeekscheduledQty.getName(), apsWeekscheduledQty.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=apsWeekscheduledQty.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(apsWeekscheduledQty.getIautoid(), JBoltUserKit.getUserId(), apsWeekscheduledQty.getName());
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
	 * @param apsWeekscheduledQty 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(ApsWeekscheduledQty apsWeekscheduledQty, Kv kv) {
		//addDeleteSystemLog(apsWeekscheduledQty.getIautoid(), JBoltUserKit.getUserId(),apsWeekscheduledQty.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param apsWeekscheduledQty 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(ApsWeekscheduledQty apsWeekscheduledQty, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(apsWeekscheduledQty, kv);
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
	 * 切换isweekend属性
	 */
	public Ret toggleIsWeekend(Long id) {
		return toggleBoolean(id, "isWeekend");
	}

	/**
	 * 切换ismodified属性
	 */
	public Ret toggleIsModified(Long id) {
		return toggleBoolean(id, "isModified");
	}

	/**
	 * 切换islocked属性
	 */
	public Ret toggleIsLocked(Long id) {
		return toggleBoolean(id, "isLocked");
	}

	/**
	 * 检测是否可以toggle操作指定列
	 * @param apsWeekscheduledQty 要toggle的model
	 * @param column 操作的哪一列
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanToggle(ApsWeekscheduledQty apsWeekscheduledQty,String column, Kv kv) {
		//没有问题就返回null  有问题就返回提示string 字符串
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(ApsWeekscheduledQty apsWeekscheduledQty, String column, Kv kv) {
		//addUpdateSystemLog(apsWeekscheduledQty.getIautoid(), JBoltUserKit.getUserId(), apsWeekscheduledQty.getName(),"的字段["+column+"]值:"+apsWeekscheduledQty.get(column));
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param apsWeekscheduledQty model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(ApsWeekscheduledQty apsWeekscheduledQty, Kv kv) {
		//这里用来覆盖 检测ApsWeekscheduledQty是否被其它表引用
		return null;
	}

}