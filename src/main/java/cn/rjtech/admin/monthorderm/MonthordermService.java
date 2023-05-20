package cn.rjtech.admin.monthorderm;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.User;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.cusordersum.CusOrderSumService;
import cn.rjtech.admin.monthorderd.MonthorderdService;
import cn.rjtech.constants.ErrorMsg;
import cn.rjtech.model.momdata.Monthorderd;
import cn.rjtech.model.momdata.Monthorderm;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.TableMapping;

import java.util.Date;
import java.util.List;
import java.util.Set;
/**
 * 月度计划订单 Service
 * @ClassName: MonthordermService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-10 18:20
 */
public class MonthordermService extends BaseService<Monthorderm> {

	private final Monthorderm dao = new Monthorderm().dao();
	@Inject
	private MonthorderdService monthorderdService;
	@Inject
	private CusOrderSumService cusOrderSumService;
	@Override
	protected Monthorderm dao() {
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
		para.set("iorgid",getOrgId());
		return dbTemplate("monthorderm.paginateAdminDatas",para).paginate(pageNumber, pageSize);
	}

	/**
	 * 保存
	 * @param monthorderm
	 * @return
	 */
	public Ret save(Monthorderm monthorderm) {
		if(monthorderm==null || isOk(monthorderm.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(monthorderm.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=monthorderm.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(monthorderm.getIautoid(), JBoltUserKit.getUserId(), monthorderm.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param monthorderm
	 * @return
	 */
	public Ret update(Monthorderm monthorderm) {
		if(monthorderm==null || notOk(monthorderm.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		Monthorderm dbMonthorderm=findById(monthorderm.getIAutoId());
		if(dbMonthorderm==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(monthorderm.getName(), monthorderm.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=monthorderm.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(monthorderm.getIautoid(), JBoltUserKit.getUserId(), monthorderm.getName());
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
		return updateColumn(id, "isdeleted", true);
	}

	/**
	 * 删除数据后执行的回调
	 * @param monthorderm 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(Monthorderm monthorderm, Kv kv) {
		//addDeleteSystemLog(monthorderm.getIautoid(), JBoltUserKit.getUserId(),monthorderm.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param monthorderm 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(Monthorderm monthorderm, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(monthorderm, kv);
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
	 * @param monthorderm 要toggle的model
	 * @param column 操作的哪一列
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanToggle(Monthorderm monthorderm,String column, Kv kv) {
		//没有问题就返回null  有问题就返回提示string 字符串
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(Monthorderm monthorderm, String column, Kv kv) {
		//addUpdateSystemLog(monthorderm.getIautoid(), JBoltUserKit.getUserId(), monthorderm.getName(),"的字段["+column+"]值:"+monthorderm.get(column));
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param monthorderm model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(Monthorderm monthorderm, Kv kv) {
		//这里用来覆盖 检测Monthorderm是否被其它表引用
		return null;
	}
    /**
     * 执行JBoltTable表格整体提交
     *
     * @param jBoltTable
     * @return
     */
    public Ret submitByJBoltTable(JBoltTable jBoltTable) {
    	Monthorderm monthorderm = jBoltTable.getFormModel(Monthorderm.class,"monthorderm");
    	User user = JBoltUserKit.getUser();
    	Date now = new Date();
    	tx(()->{
	    	if(monthorderm.getIAutoId() == null){
		    	monthorderm.setIOrgId(getOrgId());
		    	monthorderm.setCOrgCode(getOrgCode());
		    	monthorderm.setCOrgName(getOrgName());
		    	monthorderm.setICreateBy(user.getId());
		    	monthorderm.setCCreateName(user.getName());
		    	monthorderm.setDCreateTime(now);
		    	monthorderm.setIUpdateBy(user.getId());
		    	monthorderm.setCUpdateName(user.getName());
		    	monthorderm.setDUpdateTime(now);
		    	ValidationUtils.isTrue(monthorderm.save(), ErrorMsg.SAVE_FAILED);
	    	}else{
	    		monthorderm.setIUpdateBy(user.getId());
		    	monthorderm.setCUpdateName(user.getName());
		    	monthorderm.setDUpdateTime(now);
		    	ValidationUtils.isTrue(monthorderm.update(), ErrorMsg.UPDATE_FAILED);
	    	}
	    	saveTableSubmitDatas(jBoltTable,monthorderm);
	    	updateTableSubmitDatas(jBoltTable);
	    	deleteTableSubmitDatas(jBoltTable);
	    	return true;
    	});
        return SUCCESS;
    }

    //可编辑表格提交-新增数据
    private void saveTableSubmitDatas(JBoltTable jBoltTable,Monthorderm monthorderm){
    	List<Record> list = jBoltTable.getSaveRecordList();
    	if(CollUtil.isEmpty(list)) return;
    	for (int i=0;i<list.size();i++) {
    		Record row = list.get(i);
    		if (i == 0) {
    		    // 避免保存不到所有字段的问题
    		    Set<String> columnNames = TableMapping.me().getTable(Monthorderd.class).getColumnNameSet();
    		    for (String columnName : columnNames) {
    		        if (null == row.get(columnName)) {
    		            row.set(columnName, null);
    		        }
    		    }
    		}
    		row.keep("iinventoryid","iqty1","iqty2","iqty3","iqty4","iqty5","iqty6","iqty7","iqty8","iqty9","iqty10","iqty11","iqty12",
    				"iqty13","iqty14","iqty15","iqty16","iqty17","iqty18","iqty19","iqty20","iqty21","iqty22","iqty23","iqty24","iqty25",
    				"iqty26","iqty27","iqty28","iqty29","iqty30","iqty31","isum", "cinvcode", "cinvname", "cinvcode1", "cinvname1", "cinvstd", "imonth1qty", "imonth2qty");
    		row.set("isdeleted", "0");
    		row.set("imonthordermid", monthorderm.getIAutoId());
    		row.set("iautoid", JBoltSnowflakeKit.me.nextId());
		}
    	monthorderdService.batchSaveRecords(list);
    }
    
    //可编辑表格提交-修改数据
    private void updateTableSubmitDatas(JBoltTable jBoltTable){
    	List<Record> list = jBoltTable.getUpdateRecordList();
    	if(CollUtil.isEmpty(list)) {
            return;
        }
        
        for (Record row : list) {
            row.keep("iautoid", "iinventoryid", "iqty1", "iqty2", "iqty3", "iqty4", "iqty5", "iqty6", "iqty7", "iqty8", "iqty9", "iqty10", "iqty11", "iqty12",
                    "iqty13", "iqty14", "iqty15", "iqty16", "iqty17", "iqty18", "iqty19", "iqty20", "iqty21", "iqty22", "iqty23", "iqty24", "iqty25",
                    "iqty26", "iqty27", "iqty28", "iqty29", "iqty30", "iqty31", "isum", "cinvcode", "cinvname", "cinvcode1", "cinvname1", "cinvstd", "imonth1qty", "imonth2qty");
        }
        
    	monthorderdService.batchUpdateRecords(list);
    }
    
    //可编辑表格提交-删除数据
    private void deleteTableSubmitDatas(JBoltTable jBoltTable){
    	Object[] ids = jBoltTable.getDelete();
    	if(ArrayUtil.isEmpty(ids)) return;
    	for (Object id : ids) {
    		monthorderdService.deleteById(id);
		}
    }

	/**
	 * 审批
	 * @param id
	 * @return
	 */
	public Ret approve(Long id) {
		Monthorderm monthorderm = findById(id);

		//2. 审核通过
		monthorderm.setIAuditStatus(2);
		// 3. 已审批
		monthorderm.setIOrderStatus(3);
		monthorderm.setIUpdateBy(JBoltUserKit.getUserId());
		monthorderm.setCUpdateName(JBoltUserKit.getUserName());
		monthorderm.setDUpdateTime(new Date());
		monthorderm.update();

		//审批通过生成客户计划汇总
		return cusOrderSumService.approveByMonth(monthorderm);

    }
}