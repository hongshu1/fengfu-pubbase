package cn.rjtech.admin.subcontractsaleorderm;

import cn.rjtech.admin.cusordersum.CusOrderSumService;
import cn.rjtech.model.momdata.Subcontractsaleorderd;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.TableMapping;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import java.util.Date;
import java.util.List;
import java.util.Set;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.jbolt._admin.dept.DeptService;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.cache.JBoltDictionaryCache;
import cn.jbolt.core.cache.JBoltUserCache;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.Dept;
import cn.jbolt.core.model.User;
import cn.rjtech.admin.subcontractsaleorderd.SubcontractsaleorderdService;
import cn.rjtech.config.DictionaryTypeKey;
import cn.rjtech.constants.ErrorMsg;
import cn.rjtech.model.momdata.Subcontractsaleorderm;
import cn.rjtech.util.ValidationUtils;
/**
 * 委外销售订单主表 Service
 * @ClassName: SubcontractsaleordermService
 * @author: RJ
 * @date: 2023-04-12 18:57
 */
public class SubcontractsaleordermService extends BaseService<Subcontractsaleorderm> {

	private final Subcontractsaleorderm dao = new Subcontractsaleorderm().dao();
	@Inject
	private SubcontractsaleorderdService subcontractsaleorderdService;
	@Inject
	private DeptService deptService;
	@Inject
	private CusOrderSumService cusOrderSumService;
	@Override
	protected Subcontractsaleorderm dao() {
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
		Page<Record> pageList = dbTemplate("subcontractsaleorderm.paginateAdminDatas",para).paginate(pageNumber, pageSize);
		for (Record row : pageList.getList()) {
			Dept dept = deptService.findById(row.getLong("idepartmentid"));
			row.set("cdepname", dept == null? null : dept.getName());
			row.set("cbususername", JBoltUserCache.me.getUserName(row.getLong("iBusUserId")));
			row.set("ccurrencyname", JBoltDictionaryCache.me.getNameBySn(DictionaryTypeKey.CCURRENCY, row.getStr("cCurrency")));
		}
		return pageList;
	}

	/**
	 * 保存
	 * @param subcontractsaleorderm
	 * @return
	 */
	public Ret save(Subcontractsaleorderm subcontractsaleorderm) {
		if(subcontractsaleorderm==null || isOk(subcontractsaleorderm.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(subcontractsaleorderm.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=subcontractsaleorderm.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(subcontractsaleorderm.getIautoid(), JBoltUserKit.getUserId(), subcontractsaleorderm.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param subcontractsaleorderm
	 * @return
	 */
	public Ret update(Subcontractsaleorderm subcontractsaleorderm) {
		if(subcontractsaleorderm==null || notOk(subcontractsaleorderm.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		Subcontractsaleorderm dbSubcontractsaleorderm=findById(subcontractsaleorderm.getIAutoId());
		if(dbSubcontractsaleorderm==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(subcontractsaleorderm.getName(), subcontractsaleorderm.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=subcontractsaleorderm.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(subcontractsaleorderm.getIautoid(), JBoltUserKit.getUserId(), subcontractsaleorderm.getName());
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
	 * @param subcontractsaleorderm 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(Subcontractsaleorderm subcontractsaleorderm, Kv kv) {
		//addDeleteSystemLog(subcontractsaleorderm.getIautoid(), JBoltUserKit.getUserId(),subcontractsaleorderm.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param subcontractsaleorderm 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(Subcontractsaleorderm subcontractsaleorderm, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(subcontractsaleorderm, kv);
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
	 * @param subcontractsaleorderm 要toggle的model
	 * @param column 操作的哪一列
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanToggle(Subcontractsaleorderm subcontractsaleorderm,String column, Kv kv) {
		//没有问题就返回null  有问题就返回提示string 字符串
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(Subcontractsaleorderm subcontractsaleorderm, String column, Kv kv) {
		//addUpdateSystemLog(subcontractsaleorderm.getIautoid(), JBoltUserKit.getUserId(), subcontractsaleorderm.getName(),"的字段["+column+"]值:"+subcontractsaleorderm.get(column));
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param subcontractsaleorderm model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(Subcontractsaleorderm subcontractsaleorderm, Kv kv) {
		//这里用来覆盖 检测Subcontractsaleorderm是否被其它表引用
		return null;
	}
    /**
     * 执行JBoltTable表格整体提交
     *
     * @param jBoltTable
     * @return
     */
    public Ret submitByJBoltTable(JBoltTable jBoltTable) {
    	Subcontractsaleorderm subcontractsaleorderm = jBoltTable.getFormModel(Subcontractsaleorderm.class,"subcontractsaleorderm");
    	User user = JBoltUserKit.getUser();
    	Date now = new Date();
    	tx(()->{
	    	if(subcontractsaleorderm.getIAutoId() == null){
		    	subcontractsaleorderm.setIOrgId(getOrgId());
		    	subcontractsaleorderm.setCOrgCode(getOrgCode());
		    	subcontractsaleorderm.setCOrgName(getOrgName());
		    	subcontractsaleorderm.setICreateBy(user.getId());
		    	subcontractsaleorderm.setCCreateName(user.getName());
		    	subcontractsaleorderm.setDCreateTime(now);
		    	subcontractsaleorderm.setIUpdateBy(user.getId());
		    	subcontractsaleorderm.setCUpdateName(user.getName());
		    	subcontractsaleorderm.setDUpdateTime(now);
		    	ValidationUtils.isTrue(subcontractsaleorderm.save(), ErrorMsg.SAVE_FAILED);
	    	}else{
	    		subcontractsaleorderm.setIUpdateBy(user.getId());
		    	subcontractsaleorderm.setCUpdateName(user.getName());
		    	subcontractsaleorderm.setDUpdateTime(now);
		    	ValidationUtils.isTrue(subcontractsaleorderm.update(), ErrorMsg.UPDATE_FAILED);
	    	}
	    	saveTableSubmitDatas(jBoltTable,subcontractsaleorderm);
	    	updateTableSubmitDatas(jBoltTable,subcontractsaleorderm);
	    	deleteTableSubmitDatas(jBoltTable);
	    	return true;
    	});
        return SUCCESS;
    }
    
    //可编辑表格提交-新增数据
    private void saveTableSubmitDatas(JBoltTable jBoltTable,Subcontractsaleorderm subcontractsaleorderm){
    	List<Record> list = jBoltTable.getSaveRecordList();
    	if(CollUtil.isEmpty(list)) return;
    	for (int i=0;i<list.size();i++) {
    		Record row = list.get(i);
    		if (i == 0) {
    		    // 避免保存不到所有字段的问题
    		    Set<String> columnNames = TableMapping.me().getTable(Subcontractsaleorderm.class).getColumnNameSet();
    		    for (String columnName : columnNames) {
    		        if (null == row.get(columnName)) {
    		            row.set(columnName, null);
    		        }
    		    }
    		}
    		row.keep("iautoid","iinventoryid","iqty1","iqty2","iqty3","iqty4","iqty5","iqty6","iqty7","iqty8","iqty9","iqty10","iqty11","iqty12",
    				"iqty13","iqty14","iqty15","iqty16","iqty17","iqty18","iqty19","iqty20","iqty21","iqty22","iqty23","iqty24","iqty25",
    				"iqty26","iqty27","iqty28","iqty29","iqty30","iqty31","isum");
    		row.set("isdeleted", "0");
    		row.set("isubcontractsaleordermid", subcontractsaleorderm.getIAutoId());
    		row.set("iautoid", JBoltSnowflakeKit.me.nextId());
		}
    	subcontractsaleorderdService.batchSaveRecords(list);
    }
    //可编辑表格提交-修改数据
    private void updateTableSubmitDatas(JBoltTable jBoltTable,Subcontractsaleorderm subcontractsaleorderm){
    	List<Record> list = jBoltTable.getUpdateRecordList();
    	if(CollUtil.isEmpty(list)) return;
    	for(int i = 0;i < list.size(); i++){
    		Record row = list.get(i);
    		row.keep("iautoid","iinventoryid","iqty1","iqty2","iqty3","iqty4","iqty5","iqty6","iqty7","iqty8","iqty9","iqty10","iqty11","iqty12",
    				"iqty13","iqty14","iqty15","iqty16","iqty17","iqty18","iqty19","iqty20","iqty21","iqty22","iqty23","iqty24","iqty25",
    				"iqty26","iqty27","iqty28","iqty29","iqty30","iqty31","isum");
    	}
    	subcontractsaleorderdService.batchUpdateRecords(list);
    }
    //可编辑表格提交-删除数据
    private void deleteTableSubmitDatas(JBoltTable jBoltTable){
    	Object[] ids = jBoltTable.getDelete();
    	if(ArrayUtil.isEmpty(ids)) return;
    	for (Object id : ids) {
    		subcontractsaleorderdService.deleteById(id);
		}
    }

	public void handleCusOrderBySubcontract(Subcontractsaleorderm subcontractsaleorderm) {
		List<Subcontractsaleorderd> subcontractsaleorderds = subcontractsaleorderdService.findByMid(subcontractsaleorderm);
		if (subcontractsaleorderds.size() > 0) {
			cusOrderSumService.handelSubcontractsaleorderd(subcontractsaleorderm, subcontractsaleorderds);
		}
	}
}