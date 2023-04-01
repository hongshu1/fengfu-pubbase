package cn.rjtech.admin.customerworkdays;

import cn.hutool.core.util.ArrayUtil;
import cn.rjtech.model.momdata.CustomerAddr;
import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.rjtech.model.momdata.CustomerWorkDays;

import java.util.List;

import static cn.hutool.core.text.StrPool.COMMA;

/**
 * 往来单位-客户行事日历
 * @ClassName: CustomerWorkDaysService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-31 15:16
 */
public class CustomerWorkDaysService extends BaseService<CustomerWorkDays> {
	private final CustomerWorkDays dao=new CustomerWorkDays().dao();
	@Override
	protected CustomerWorkDays dao() {
		return dao;
	}

	@Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

	/**
	 * 后台管理数据查询
	 * @param pageNumber 第几页
	 * @param pageSize   每页几条数据
	 * @return
	 */
	public Page<CustomerWorkDays> getAdminDatas(int pageNumber, int pageSize) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
        //排序
        sql.desc("iAutoId");
		return paginate(sql);
	}

	/**
	 * 保存
	 * @param customerWorkDays
	 * @return
	 */
	public Ret save(CustomerWorkDays customerWorkDays) {
		if(customerWorkDays==null || isOk(customerWorkDays.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(customerWorkDays.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=customerWorkDays.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(customerWorkDays.getIAutoId(), JBoltUserKit.getUserId(), customerWorkDays.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param customerWorkDays
	 * @return
	 */
	public Ret update(CustomerWorkDays customerWorkDays) {
		if(customerWorkDays==null || notOk(customerWorkDays.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		CustomerWorkDays dbCustomerWorkDays=findById(customerWorkDays.getIAutoId());
		if(dbCustomerWorkDays==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(customerWorkDays.getName(), customerWorkDays.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=customerWorkDays.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(customerWorkDays.getIAutoId(), JBoltUserKit.getUserId(), customerWorkDays.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param customerWorkDays 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(CustomerWorkDays customerWorkDays, Kv kv) {
		//addDeleteSystemLog(customerWorkDays.getIAutoId(), JBoltUserKit.getUserId(),customerWorkDays.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param customerWorkDays model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(CustomerWorkDays customerWorkDays, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

	/**
	 * 根据主键删除
	 * @param deletes
	 */
	public void deleteMultiByIds(Object[] deletes) {
		delete("DELETE FROM Bd_CustomerWorkDays WHERE iAutoId IN (" + ArrayUtil.join(deletes, COMMA) + ") ");
	}

	public List<CustomerWorkDays> getList(Kv kv){
		return find("SELECT * FROM Bd_CustomerWorkDays WHERE iCustomerId = ?", kv.get("icustomermid"));
	}
}
