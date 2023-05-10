package cn.rjtech.admin.sysproductin;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.User;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.rjtech.constants.ErrorMsg;
import cn.rjtech.model.momdata.SysOtherin;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.rjtech.model.momdata.SysProductin;
import com.jfinal.plugin.activerecord.Record;

import java.util.Date;
import java.util.List;

/**
 * 产成品入库单
 * @ClassName: SysProductinService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-08 09:56
 */
public class SysProductinService extends BaseService<SysProductin> {
	private final SysProductin dao=new SysProductin().dao();
	@Override
	protected SysProductin dao() {
		return dao;
	}

	@Inject
	private SysProductindetailService sysproductindetailservice;

	@Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

	/**
	 * 后台管理数据查询
	 * @param pageNumber 第几页
	 * @param pageSize   每页几条数据
	 * @param keywords   关键词
     * @param SourceBillType 来源类型;MO生产工单
     * @param BillType 业务类型
     * @param state 状态 1已保存 2待审批 3已审批 4审批不通过 
     * @param IsDeleted 删除状态：0. 未删除 1. 已删除
     * @param warehousingType 入库类别
	 * @return
	 */
	public Page<SysProductin> getAdminDatas(int pageNumber, int pageSize, String keywords, String SourceBillType, String BillType, String state, Boolean IsDeleted, String warehousingType) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
	    //sql条件处理
        sql.eq("SourceBillType",SourceBillType);
        sql.eq("BillType",BillType);
        sql.eq("state",state);
        sql.eqBooleanToChar("IsDeleted",IsDeleted);
        sql.eq("warehousingType",warehousingType);
        //关键词模糊查询
        sql.likeMulti(keywords,"deptName", "remark", "repositoryName");
        //排序
        sql.desc("AutoID");
		return paginate(sql);
	}

	/**
	 * 保存
	 * @param sysProductin
	 * @return
	 */
	public Ret save(SysProductin sysProductin) {
		if(sysProductin==null || isOk(sysProductin.getAutoID())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(sysProductin.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=sysProductin.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(sysProductin.getAutoID(), JBoltUserKit.getUserId(), sysProductin.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param sysProductin
	 * @return
	 */
	public Ret update(SysProductin sysProductin) {
		if(sysProductin==null || notOk(sysProductin.getAutoID())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		SysProductin dbSysProductin=findById(sysProductin.getAutoID());
		if(dbSysProductin==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(sysProductin.getName(), sysProductin.getAutoID())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=sysProductin.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(sysProductin.getAutoID(), JBoltUserKit.getUserId(), sysProductin.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param sysProductin 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(SysProductin sysProductin, Kv kv) {
		//addDeleteSystemLog(sysProductin.getAutoID(), JBoltUserKit.getUserId(),sysProductin.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param sysProductin model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(SysProductin sysProductin, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(SysProductin sysProductin, String column, Kv kv) {
		//addUpdateSystemLog(sysProductin.getAutoID(), JBoltUserKit.getUserId(), sysProductin.getName(),"的字段["+column+"]值:"+sysProductin.get(column));
		/**
		switch(column){
		    case "IsDeleted":
		        break;
		}
		*/
		return null;
	}
	/**
	 * 后台管理数据查询
	 * @param pageNumber 第几页
	 * @param pageSize   每页几条数据
	 * @return
	 */
	public Page<Record> getAdminDatas(int pageNumber, int pageSize, Kv kv) {
		Page<Record> paginate = dbTemplate("sysproductin.recpor", kv).paginate(pageNumber, pageSize);
		return paginate;
	}

	/**
	 * 批量删除主从表
	 */
	public Ret deleteRmRdByIds(String ids) {
		tx(() -> {
			String[] split = ids.split(",");
			for(String s : split){
				updateColumn(s, "isdeleted", true);
				update("update T_Sys_ProductInDetail  set  IsDeleted = 1 where  MasID = ?",s);
			}
			return true;
		});
		return ret(true);
	}

	/**
	 * 删除
	 * @param id
	 * @return
	 */
	public Ret delete(Long id) {
		tx(() -> {
			updateColumn(id, "isdeleted", true);
			update("update T_Sys_ProductInDetail  set  IsDeleted = 1 where  MasID = ?",id);
			return true;
		});
		return ret(true);
	}

	/**
	 * 执行JBoltTable表格整体提交
	 *
	 * @param jBoltTable
	 * @return
	 */
	public Ret submitByJBoltTable(JBoltTable jBoltTable) {
		SysProductin sysotherin = jBoltTable.getFormModel(SysProductin.class,"sysProductin");
		//获取当前用户信息？
		User user = JBoltUserKit.getUser();
		Date now = new Date();
		tx(()->{
			//通过 id 判断是新增还是修改
			if(sysotherin.getAutoID() == null){
				sysotherin.setOrganizeCode(getOrgCode());
				sysotherin.setCreatePerson(user.getId().toString());
				sysotherin.setCreateDate(now);
				sysotherin.setModifyPerson(user.getId().toString());
				sysotherin.setState("1");
				sysotherin.setModifyDate(now);
				//主表新增
				ValidationUtils.isTrue(sysotherin.save(), ErrorMsg.SAVE_FAILED);
			}else{
				sysotherin.setModifyPerson(user.getId().toString());
				sysotherin.setModifyDate(now);
				//主表修改
				ValidationUtils.isTrue(sysotherin.update(), ErrorMsg.UPDATE_FAILED);
			}
			//从表的操作
			// 获取保存数据（执行保存，通过 getSaveRecordList）
			saveTableSubmitDatas(jBoltTable,sysotherin);
			//获取修改数据（执行修改，通过 getUpdateRecordList）
			updateTableSubmitDatas(jBoltTable,sysotherin);
			//获取删除数据（执行删除，通过 getDelete）
			deleteTableSubmitDatas(jBoltTable);
			return true;
		});
		return SUCCESS;
	}

	//可编辑表格提交-新增数据
	private void saveTableSubmitDatas(JBoltTable jBoltTable,SysProductin sysotherin){
		List<Record> list = jBoltTable.getSaveRecordList();
		if(CollUtil.isEmpty(list)) return;
		Date now = new Date();
		for (int i=0;i<list.size();i++) {
			Record row = list.get(i);
			row.set("IsDeleted", "0");
			row.set("MasID", sysotherin.getAutoID());
			row.set("AutoID", JBoltSnowflakeKit.me.nextId());
			row.set("CreateDate", now);
			row.set("ModifyDate", now);


		}
		sysproductindetailservice.batchSaveRecords(list);
	}
	//可编辑表格提交-修改数据
	private void updateTableSubmitDatas(JBoltTable jBoltTable,SysProductin sysotherin){
		List<Record> list = jBoltTable.getUpdateRecordList();
		if(CollUtil.isEmpty(list)) return;
		Date now = new Date();
		for(int i = 0;i < list.size(); i++){
			Record row = list.get(i);
			row.set("ModifyDate", now);

		}
		sysproductindetailservice.batchUpdateRecords(list);
	}
	//可编辑表格提交-删除数据
	private void deleteTableSubmitDatas(JBoltTable jBoltTable){
		Object[] ids = jBoltTable.getDelete();
		if(ArrayUtil.isEmpty(ids)) return;
		for (Object id : ids) {
			update("update T_Sys_ProductInDetail  set  IsDeleted = 1 where  AutoID = ?",id);
		}
	}
}