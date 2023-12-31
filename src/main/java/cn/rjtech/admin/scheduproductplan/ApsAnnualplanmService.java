package cn.rjtech.admin.scheduproductplan;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.ApsAnnualplanm;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
/**
 * 年度生产计划 Service
 * @ClassName: ApsAnnualplanmService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-31 13:20
 */
public class ApsAnnualplanmService extends BaseService<ApsAnnualplanm> {

	private final ApsAnnualplanm dao = new ApsAnnualplanm().dao();

	@Override
	protected ApsAnnualplanm dao() {
		return dao;
	}

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @return
	 */
	public Page<ApsAnnualplanm> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
		return paginateByKeywords("iAutoId","DESC", pageNumber, pageSize, keywords, "iAutoId");
	}

	/**
	 * 保存
	 * @param apsAnnualplanm
	 * @return
	 */
	public Ret save(ApsAnnualplanm apsAnnualplanm) {
		if(apsAnnualplanm==null || isOk(apsAnnualplanm.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(apsAnnualplanm.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=apsAnnualplanm.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(apsAnnualplanm.getIautoid(), JBoltUserKit.getUserId(), apsAnnualplanm.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param apsAnnualplanm
	 * @return
	 */
	public Ret update(ApsAnnualplanm apsAnnualplanm) {
		if(apsAnnualplanm==null || notOk(apsAnnualplanm.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		ApsAnnualplanm dbApsAnnualplanm=findById(apsAnnualplanm.getIAutoId());
		if(dbApsAnnualplanm==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(apsAnnualplanm.getName(), apsAnnualplanm.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=apsAnnualplanm.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(apsAnnualplanm.getIautoid(), JBoltUserKit.getUserId(), apsAnnualplanm.getName());
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
	 * @param apsAnnualplanm 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(ApsAnnualplanm apsAnnualplanm, Kv kv) {
		//addDeleteSystemLog(apsAnnualplanm.getIautoid(), JBoltUserKit.getUserId(),apsAnnualplanm.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param apsAnnualplanm 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(ApsAnnualplanm apsAnnualplanm, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(apsAnnualplanm, kv);
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
	 * @param apsAnnualplanm 要toggle的model
	 * @param column 操作的哪一列
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanToggle(ApsAnnualplanm apsAnnualplanm,String column, Kv kv) {
		//没有问题就返回null  有问题就返回提示string 字符串
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(ApsAnnualplanm apsAnnualplanm, String column, Kv kv) {
		//addUpdateSystemLog(apsAnnualplanm.getIautoid(), JBoltUserKit.getUserId(), apsAnnualplanm.getName(),"的字段["+column+"]值:"+apsAnnualplanm.get(column));
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param apsAnnualplanm model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(ApsAnnualplanm apsAnnualplanm, Kv kv) {
		//这里用来覆盖 检测ApsAnnualplanm是否被其它表引用
		return null;
	}

}