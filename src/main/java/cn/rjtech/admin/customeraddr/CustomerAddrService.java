package cn.rjtech.admin.customeraddr;

import cn.hutool.core.util.ArrayUtil;
import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.CustomerAddr;

import java.util.List;

import static cn.hutool.core.text.StrPool.COMMA;

/**
 * 客户档案-联系地址
 * @ClassName: CustomerAddrService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-22 13:16
 */
public class CustomerAddrService extends BaseService<CustomerAddr> {
	private final CustomerAddr dao=new CustomerAddr().dao();
	@Override
	protected CustomerAddr dao() {
		return dao;
	}

	@Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @return
	 */
	public Page<CustomerAddr> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
		return paginateByKeywords("iAutoId","DESC", pageNumber, pageSize, keywords, "iAutoId");
	}

	public List<CustomerAddr> list(Long icustomermid){
		return find("SELECT * FROM Bd_CustomerAddr WHERE iCustomerId = ?", icustomermid);
	}

	/**
	 * 保存
	 * @param customerd
	 * @return
	 */
	public Ret save(CustomerAddr customerd) {
		if(customerd==null || isOk(customerd.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(customerd.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=customerd.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(customerd.getIautoid(), JBoltUserKit.getUserId(), customerd.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param customerd
	 * @return
	 */
	public Ret update(CustomerAddr customerd) {
		if(customerd==null || notOk(customerd.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		CustomerAddr dbCustomerd=findById(customerd.getIAutoId());
		if(dbCustomerd==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(customerd.getName(), customerd.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=customerd.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(customerd.getIautoid(), JBoltUserKit.getUserId(), customerd.getName());
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
	 * 删除数据后执行的回调
	 * @param customerd 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(CustomerAddr customerd, Kv kv) {
		//addDeleteSystemLog(customerd.getIautoid(), JBoltUserKit.getUserId(),customerd.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param customerd 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(CustomerAddr customerd, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(customerd, kv);
	}

	/**
	 * 切换isenabled属性
	 */
	public Ret toggleIsenabled(Long id) {
		return toggleBoolean(id, "isEnabled");
	}

	/**
	 * 检测是否可以toggle操作指定列
	 * @param customerd 要toggle的model
	 * @param column 操作的哪一列
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanToggle(CustomerAddr customerd,String column, Kv kv) {
		//没有问题就返回null  有问题就返回提示string 字符串
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(CustomerAddr customerd, String column, Kv kv) {
		//addUpdateSystemLog(customerd.getIautoid(), JBoltUserKit.getUserId(), customerd.getName(),"的字段["+column+"]值:"+customerd.get(column));
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param customerd model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(CustomerAddr customerd, Kv kv) {
		//这里用来覆盖 检测Customerd是否被其它表引用
		return null;
	}

	/**
	 * 根据主键删除
	 * @param deletes
	 */
	public void deleteMultiByIds(Object[] deletes) {
		delete("DELETE FROM Bd_CustomerAddr WHERE iAutoId IN (" + ArrayUtil.join(deletes, COMMA) + ") ");
	}


}
