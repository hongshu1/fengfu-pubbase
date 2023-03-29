package cn.rjtech.admin.annualorderdamount;

import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.AnnualorderdAmount;
/**
 * 年度计划订单年月金额 Service
 * @ClassName: AnnualorderdAmountService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-29 14:22
 */
public class AnnualorderdAmountService extends BaseService<AnnualorderdAmount> {

	private final AnnualorderdAmount dao = new AnnualorderdAmount().dao();

	@Override
	protected AnnualorderdAmount dao() {
		return dao;
	}

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @return
	 */
	public Page<AnnualorderdAmount> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
		return paginateByKeywords("iAutoId","DESC", pageNumber, pageSize, keywords, "iAutoId");
	}

	/**
	 * 保存
	 * @param annualorderdAmount
	 * @return
	 */
	public Ret save(AnnualorderdAmount annualorderdAmount) {
		if(annualorderdAmount==null || isOk(annualorderdAmount.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(annualorderdAmount.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=annualorderdAmount.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(annualorderdAmount.getIautoid(), JBoltUserKit.getUserId(), annualorderdAmount.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param annualorderdAmount
	 * @return
	 */
	public Ret update(AnnualorderdAmount annualorderdAmount) {
		if(annualorderdAmount==null || notOk(annualorderdAmount.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		AnnualorderdAmount dbAnnualorderdAmount=findById(annualorderdAmount.getIAutoId());
		if(dbAnnualorderdAmount==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(annualorderdAmount.getName(), annualorderdAmount.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=annualorderdAmount.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(annualorderdAmount.getIautoid(), JBoltUserKit.getUserId(), annualorderdAmount.getName());
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
	 * @param annualorderdAmount 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(AnnualorderdAmount annualorderdAmount, Kv kv) {
		//addDeleteSystemLog(annualorderdAmount.getIautoid(), JBoltUserKit.getUserId(),annualorderdAmount.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param annualorderdAmount 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(AnnualorderdAmount annualorderdAmount, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(annualorderdAmount, kv);
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
		return toggleBoolean(id, "isDeleted");
	}

	/**
	 * 检测是否可以toggle操作指定列
	 * @param annualorderdAmount 要toggle的model
	 * @param column 操作的哪一列
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanToggle(AnnualorderdAmount annualorderdAmount,String column, Kv kv) {
		//没有问题就返回null  有问题就返回提示string 字符串
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(AnnualorderdAmount annualorderdAmount, String column, Kv kv) {
		//addUpdateSystemLog(annualorderdAmount.getIautoid(), JBoltUserKit.getUserId(), annualorderdAmount.getName(),"的字段["+column+"]值:"+annualorderdAmount.get(column));
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param annualorderdAmount model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(AnnualorderdAmount annualorderdAmount, Kv kv) {
		//这里用来覆盖 检测AnnualorderdAmount是否被其它表引用
		return null;
	}

}