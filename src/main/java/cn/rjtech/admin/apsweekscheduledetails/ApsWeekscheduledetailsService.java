package cn.rjtech.admin.apsweekscheduledetails;

import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Db;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.ApsWeekscheduledetails;
/**
 * 月周生产计划排产明细 Service
 * @ClassName: ApsWeekscheduledetailsService
 * @author: chentao
 * @date: 2023-04-21 10:51
 */
public class ApsWeekscheduledetailsService extends BaseService<ApsWeekscheduledetails> {

	private final ApsWeekscheduledetails dao = new ApsWeekscheduledetails().dao();

	@Override
	protected ApsWeekscheduledetails dao() {
		return dao;
	}

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @return
	 */
	public Page<ApsWeekscheduledetails> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
		return paginateByKeywords("iAutoId","DESC", pageNumber, pageSize, keywords, "iAutoId");
	}

	/**
	 * 保存
	 * @param apsWeekscheduledetails
	 * @return
	 */
	public Ret save(ApsWeekscheduledetails apsWeekscheduledetails) {
		if(apsWeekscheduledetails==null || isOk(apsWeekscheduledetails.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(apsWeekscheduledetails.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=apsWeekscheduledetails.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(apsWeekscheduledetails.getIautoid(), JBoltUserKit.getUserId(), apsWeekscheduledetails.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param apsWeekscheduledetails
	 * @return
	 */
	public Ret update(ApsWeekscheduledetails apsWeekscheduledetails) {
		if(apsWeekscheduledetails==null || notOk(apsWeekscheduledetails.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		ApsWeekscheduledetails dbApsWeekscheduledetails=findById(apsWeekscheduledetails.getIAutoId());
		if(dbApsWeekscheduledetails==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(apsWeekscheduledetails.getName(), apsWeekscheduledetails.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=apsWeekscheduledetails.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(apsWeekscheduledetails.getIautoid(), JBoltUserKit.getUserId(), apsWeekscheduledetails.getName());
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
	 * @param apsWeekscheduledetails 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(ApsWeekscheduledetails apsWeekscheduledetails, Kv kv) {
		//addDeleteSystemLog(apsWeekscheduledetails.getIautoid(), JBoltUserKit.getUserId(),apsWeekscheduledetails.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param apsWeekscheduledetails 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(ApsWeekscheduledetails apsWeekscheduledetails, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(apsWeekscheduledetails, kv);
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
	 * @param apsWeekscheduledetails 要toggle的model
	 * @param column 操作的哪一列
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanToggle(ApsWeekscheduledetails apsWeekscheduledetails,String column, Kv kv) {
		//没有问题就返回null  有问题就返回提示string 字符串
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(ApsWeekscheduledetails apsWeekscheduledetails, String column, Kv kv) {
		//addUpdateSystemLog(apsWeekscheduledetails.getIautoid(), JBoltUserKit.getUserId(), apsWeekscheduledetails.getName(),"的字段["+column+"]值:"+apsWeekscheduledetails.get(column));
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param apsWeekscheduledetails model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(ApsWeekscheduledetails apsWeekscheduledetails, Kv kv) {
		//这里用来覆盖 检测ApsWeekscheduledetails是否被其它表引用
		return null;
	}

}