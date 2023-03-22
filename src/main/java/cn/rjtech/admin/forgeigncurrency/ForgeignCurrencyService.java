package cn.rjtech.admin.forgeigncurrency;

import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;

import java.util.Date;

import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.rjtech.enums.SourceEnum;
import cn.rjtech.model.momdata.ForgeignCurrency;
/**
 * 币种档案 Service
 * @ClassName: ForgeignCurrencyService
 * @author: WYX
 * @date: 2023-03-20 21:09
 */
public class ForgeignCurrencyService extends BaseService<ForgeignCurrency> {

	private final ForgeignCurrency dao = new ForgeignCurrency().dao();

	@Override
	protected ForgeignCurrency dao() {
		return dao;
	}

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @return
	 */
	public Page<Record> paginateAdminDatas(int pageNumber, int pageSize, Kv para) {
		return dbTemplate("forgeigncurrency.paginateAdminDatas",para).paginate(pageNumber, pageSize);
	}

	/**
	 * 保存
	 * @param forgeignCurrency
	 * @return
	 */
	public Ret save(ForgeignCurrency forgeignCurrency) {
		if(forgeignCurrency==null || isOk(forgeignCurrency.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		Date now = new Date();
		Long userid = JBoltUserKit.getUserId();
		String loginUserName = JBoltUserKit.getUserName();
		forgeignCurrency.setIOrgId(getOrgId());
		forgeignCurrency.setCOrgName(getOrgName());
		forgeignCurrency.setCOrgCode(getOrgCode());
		forgeignCurrency.setICreateBy(userid);
		forgeignCurrency.setDCreateTime(now);
		forgeignCurrency.setCCreateName(loginUserName);
		forgeignCurrency.setIUpdateBy(userid);
		forgeignCurrency.setDUpdateTime(now);
		forgeignCurrency.setCUpdateName(loginUserName);
		forgeignCurrency.setIsDeleted(false);
		forgeignCurrency.setISource(SourceEnum.MES.getValue());
		boolean success=forgeignCurrency.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(forgeignCurrency.getIautoid(), JBoltUserKit.getUserId(), forgeignCurrency.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param forgeignCurrency
	 * @return
	 */
	public Ret update(ForgeignCurrency forgeignCurrency) {
		if(forgeignCurrency==null || notOk(forgeignCurrency.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		ForgeignCurrency dbForgeignCurrency=findById(forgeignCurrency.getIAutoId());
		if(dbForgeignCurrency==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(forgeignCurrency.getName(), forgeignCurrency.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=forgeignCurrency.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(forgeignCurrency.getIautoid(), JBoltUserKit.getUserId(), forgeignCurrency.getName());
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
	 * 逻辑删除
	 * @param id
	 * @return
	 */
	public Ret delete(Long id) {
		return updateColumn(id, "isdeleted", true);
	}

	/**
	 * 删除数据后执行的回调
	 * @param forgeignCurrency 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(ForgeignCurrency forgeignCurrency, Kv kv) {
		//addDeleteSystemLog(forgeignCurrency.getIautoid(), JBoltUserKit.getUserId(),forgeignCurrency.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param forgeignCurrency 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(ForgeignCurrency forgeignCurrency, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(forgeignCurrency, kv);
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
	 * 切换bcal属性
	 */
	public Ret toggleBcal(Long id) {
		return toggleBoolean(id, "bcal");
	}
	/**
	 * 切换iotherused属性
	 */	
	public Ret toggleIotherused(Long id) {
		return toggleBoolean(id, "iotherused");
	}
	/**
	 * 切换isdeleted属性
	 */
	public Ret toggleIsDeleted(Long id) {
		return toggleBoolean(id, "isDeleted");
	}

	/**
	 * 检测是否可以toggle操作指定列
	 * @param forgeignCurrency 要toggle的model
	 * @param column 操作的哪一列
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanToggle(ForgeignCurrency forgeignCurrency,String column, Kv kv) {
		//没有问题就返回null  有问题就返回提示string 字符串
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(ForgeignCurrency forgeignCurrency, String column, Kv kv) {
		//addUpdateSystemLog(forgeignCurrency.getIautoid(), JBoltUserKit.getUserId(), forgeignCurrency.getName(),"的字段["+column+"]值:"+forgeignCurrency.get(column));
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param forgeignCurrency model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(ForgeignCurrency forgeignCurrency, Kv kv) {
		//这里用来覆盖 检测ForgeignCurrency是否被其它表引用
		return null;
	}


}