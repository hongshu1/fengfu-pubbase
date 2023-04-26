package cn.rjtech.admin.InStockQcFormM;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.InStockQcFormM;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
/**
 * 在库检 Service
 * @ClassName: InStockQcFormMService
 * @author: RJ
 * @date: 2023-04-25 15:00
 */
public class InStockQcFormMService extends BaseService<InStockQcFormM> {

	private final InStockQcFormM dao = new InStockQcFormM().dao();

	@Override
	protected InStockQcFormM dao() {
		return dao;
	}

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @return
	 */
	public Page<InStockQcFormM> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
		return paginateByKeywords("iAutoId","DESC", pageNumber, pageSize, keywords, "iAutoId");
	}

	/**
	 * 保存
	 * @param inStockQcFormM
	 * @return
	 */
	public Ret save(InStockQcFormM inStockQcFormM) {
		if(inStockQcFormM==null || isOk(inStockQcFormM.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(inStockQcFormM.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=inStockQcFormM.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(inStockQcFormM.getIautoid(), JBoltUserKit.getUserId(), inStockQcFormM.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param inStockQcFormM
	 * @return
	 */
	public Ret update(InStockQcFormM inStockQcFormM) {
		if(inStockQcFormM==null || notOk(inStockQcFormM.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		InStockQcFormM dbInStockQcFormM=findById(inStockQcFormM.getIAutoId());
		if(dbInStockQcFormM==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(inStockQcFormM.getName(), inStockQcFormM.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=inStockQcFormM.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(inStockQcFormM.getIautoid(), JBoltUserKit.getUserId(), inStockQcFormM.getName());
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
	 * @param inStockQcFormM 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(InStockQcFormM inStockQcFormM, Kv kv) {
		//addDeleteSystemLog(inStockQcFormM.getIautoid(), JBoltUserKit.getUserId(),inStockQcFormM.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param inStockQcFormM 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(InStockQcFormM inStockQcFormM, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(inStockQcFormM, kv);
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
	 * 切换iscompleted属性
	 */
	public Ret toggleIsCompleted(Long id) {
		return toggleBoolean(id, "isCompleted");
	}

	/**
	 * 切换isok属性
	 */
	public Ret toggleIsOk(Long id) {
		return toggleBoolean(id, "isOk");
	}

	/**
	 * 切换iscpksigned属性
	 */
	public Ret toggleIsCpkSigned(Long id) {
		return toggleBoolean(id, "isCpkSigned");
	}

	/**
	 * 切换isdeleted属性
	 */
	public Ret toggleIsDeleted(Long id) {
		return toggleBoolean(id, "IsDeleted");
	}

	/**
	 * 检测是否可以toggle操作指定列
	 * @param inStockQcFormM 要toggle的model
	 * @param column 操作的哪一列
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanToggle(InStockQcFormM inStockQcFormM,String column, Kv kv) {
		//没有问题就返回null  有问题就返回提示string 字符串
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(InStockQcFormM inStockQcFormM, String column, Kv kv) {
		//addUpdateSystemLog(inStockQcFormM.getIautoid(), JBoltUserKit.getUserId(), inStockQcFormM.getName(),"的字段["+column+"]值:"+inStockQcFormM.get(column));
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param inStockQcFormM model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(InStockQcFormM inStockQcFormM, Kv kv) {
		//这里用来覆盖 检测InStockQcFormM是否被其它表引用
		return null;
	}

}