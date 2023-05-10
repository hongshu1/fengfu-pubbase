package cn.rjtech.admin.syspureceive;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.User;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.rjtech.constants.ErrorMsg;
import cn.rjtech.model.momdata.SysPuinstore;
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
import cn.rjtech.model.momdata.SysPureceive;
import com.jfinal.plugin.activerecord.Record;

import java.util.Date;
import java.util.List;

/**
 * 采购收料单
 * @ClassName: SysPureceiveService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-10 10:01
 */
public class SysPureceiveService extends BaseService<SysPureceive> {
	private final SysPureceive dao=new SysPureceive().dao();
	@Override
	protected SysPureceive dao() {
		return dao;
	}

	@Inject
	private SysPureceivedetailService syspureceivedetailservice;

	@Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

	/**
	 * 后台管理数据查询
	 * @param pageNumber 第几页
	 * @param pageSize   每页几条数据
	 * @param keywords   关键词
     * @param BillType 到货单类型;采购PO  委外OM
     * @param IsDeleted 删除状态：0. 未删除 1. 已删除
	 * @return
	 */
	public Page<SysPureceive> getAdminDatas(int pageNumber, int pageSize, String keywords, String BillType, Boolean IsDeleted) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
	    //sql条件处理
        sql.eq("BillType",BillType);
        sql.eqBooleanToChar("IsDeleted",IsDeleted);
        //关键词模糊查询
        sql.like("repositoryName",keywords);
        //排序
        sql.desc("AutoID");
		return paginate(sql);
	}

	/**
	 * 保存
	 * @param sysPureceive
	 * @return
	 */
	public Ret save(SysPureceive sysPureceive) {
		if(sysPureceive==null || isOk(sysPureceive.getAutoID())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(sysPureceive.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=sysPureceive.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(sysPureceive.getAutoID(), JBoltUserKit.getUserId(), sysPureceive.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param sysPureceive
	 * @return
	 */
	public Ret update(SysPureceive sysPureceive) {
		if(sysPureceive==null || notOk(sysPureceive.getAutoID())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		SysPureceive dbSysPureceive=findById(sysPureceive.getAutoID());
		if(dbSysPureceive==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(sysPureceive.getName(), sysPureceive.getAutoID())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=sysPureceive.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(sysPureceive.getAutoID(), JBoltUserKit.getUserId(), sysPureceive.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param sysPureceive 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(SysPureceive sysPureceive, Kv kv) {
		//addDeleteSystemLog(sysPureceive.getAutoID(), JBoltUserKit.getUserId(),sysPureceive.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param sysPureceive model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(SysPureceive sysPureceive, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(SysPureceive sysPureceive, String column, Kv kv) {
		//addUpdateSystemLog(sysPureceive.getAutoID(), JBoltUserKit.getUserId(), sysPureceive.getName(),"的字段["+column+"]值:"+sysPureceive.get(column));
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
		Page<Record> paginate = dbTemplate("syspureceive.recpor", kv).paginate(pageNumber, pageSize);
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
				update("update T_Sys_PUReceiveDetail  set  IsDeleted = 1 where  MasID = ?",s);
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
			update("update T_Sys_PUReceiveDetail  set  IsDeleted = 1 where  MasID = ?",id);
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
		SysPureceive sysotherin = jBoltTable.getFormModel(SysPureceive.class,"sysPureceive");
		//获取当前用户信息？
		User user = JBoltUserKit.getUser();
		Date now = new Date();
		tx(()->{
			//通过 id 判断是新增还是修改
			if(sysotherin.getAutoID() == null){
				sysotherin.setOrganizeCode(getOrgCode());
				sysotherin.setCreatePerson(user.getUsername());
				sysotherin.setCreateDate(now);
				sysotherin.setModifyPerson(user.getUsername());
				sysotherin.setState("1");
				sysotherin.setModifyDate(now);
				//主表新增
				ValidationUtils.isTrue(sysotherin.save(), ErrorMsg.SAVE_FAILED);
			}else{
				sysotherin.setModifyPerson(user.getUsername());
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
	private void saveTableSubmitDatas(JBoltTable jBoltTable,SysPureceive sysotherin){
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
//			row.set("inventorycode",row.get("cinvcode"));
//			row.set("inventorycodeh",row.get("cinvcodeh"));
			row.remove("crcvdate");
			row.remove("crcvtime");
			row.remove("cbarcode");
			row.remove("cversion");
			row.remove("caddress");
			row.remove("cinvcode");
		}
		syspureceivedetailservice.batchSaveRecords(list);
	}
	//可编辑表格提交-修改数据
	private void updateTableSubmitDatas(JBoltTable jBoltTable,SysPureceive sysotherin){
		List<Record> list = jBoltTable.getUpdateRecordList();
		if(CollUtil.isEmpty(list)) return;
		Date now = new Date();
		for(int i = 0;i < list.size(); i++){
			Record row = list.get(i);
			row.set("ModifyDate", now);

			row.remove("crcvdate");
			row.remove("crcvtime");
			row.remove("cbarcode");
			row.remove("cversion");
			row.remove("caddress");
			row.remove("cinvcode");

		}
		syspureceivedetailservice.batchUpdateRecords(list);
	}
	//可编辑表格提交-删除数据
	private void deleteTableSubmitDatas(JBoltTable jBoltTable){
		Object[] ids = jBoltTable.getDelete();
		if(ArrayUtil.isEmpty(ids)) return;
		for (Object id : ids) {
			update("update T_Sys_PUInStoreDetail  set  IsDeleted = 1 where  AutoID = ?",id);
		}
	}
}