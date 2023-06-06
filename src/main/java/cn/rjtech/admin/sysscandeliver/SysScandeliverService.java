package cn.rjtech.admin.sysscandeliver;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.User;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.rjtech.constants.ErrorMsg;
import cn.rjtech.model.momdata.SysSaledeliverplan;
import cn.rjtech.model.momdata.SysSaledeliverplandetail;
import cn.rjtech.model.momdata.SysScandeliverdetail;
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
import cn.rjtech.model.momdata.SysScandeliver;
import com.jfinal.plugin.activerecord.Record;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * (双)扫码发货
 * @ClassName: SysScandeliverService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-22 09:47
 */
public class SysScandeliverService extends BaseService<SysScandeliver> {
	private final SysScandeliver dao=new SysScandeliver().dao();
	@Override
	protected SysScandeliver dao() {
		return dao;
	}

	@Inject
	private SysScandeliverdetailService sysscandeliverdetailservice;

	@Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

	/**
	 * 后台管理数据查询
	 * @param pageNumber 第几页
	 * @param pageSize   每页几条数据
     * @param SourceBillType 来源类型;DP 销售计划
     * @param isDeleted 删除状态默认0，1已删除
     * @param state 状态0保存，1已提交
	 * @return
	 */
	public Page<SysScandeliver> getAdminDatas(int pageNumber, int pageSize, String SourceBillType, String billno, Boolean isDeleted, Boolean state) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
	    //sql条件处理
        sql.eq("SourceBillType",SourceBillType);
        sql.eq("BillNo",billno);
        sql.eqBooleanToChar("isDeleted",isDeleted);
        sql.eqBooleanToChar("state",state);
        //排序
        sql.desc("AutoID");
		return paginate(sql);
	}

	/**
	 * 批量删除主从表
	 */
	public Ret deleteRmRdByIds(String ids) {
		tx(() -> {
			deleteByIds(ids);
			String[] split = ids.split(",");
			for(String s : split){
				delete("DELETE T_Sys_ScanDeliverDetail   where  MasID = ?",s);
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
			deleteById(id);
			delete("DELETE T_Sys_ScanDeliverDetail   where  MasID = ?",id);
			return true;
		});
		return ret(true);
	}

	/**
	 * 保存
	 * @param sysScandeliver
	 * @return
	 */
	public Ret save(SysScandeliver sysScandeliver) {
		if(sysScandeliver==null || isOk(sysScandeliver.getAutoID())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(sysScandeliver.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=sysScandeliver.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(sysScandeliver.getAutoID(), JBoltUserKit.getUserId(), sysScandeliver.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param sysScandeliver
	 * @return
	 */
	public Ret update(SysScandeliver sysScandeliver) {
		if(sysScandeliver==null || notOk(sysScandeliver.getAutoID())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		SysScandeliver dbSysScandeliver=findById(sysScandeliver.getAutoID());
		if(dbSysScandeliver==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(sysScandeliver.getName(), sysScandeliver.getAutoID())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=sysScandeliver.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(sysScandeliver.getAutoID(), JBoltUserKit.getUserId(), sysScandeliver.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param sysScandeliver 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(SysScandeliver sysScandeliver, Kv kv) {
		//addDeleteSystemLog(sysScandeliver.getAutoID(), JBoltUserKit.getUserId(),sysScandeliver.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param sysScandeliver model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(SysScandeliver sysScandeliver, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(SysScandeliver sysScandeliver, String column, Kv kv) {
		//addUpdateSystemLog(sysScandeliver.getAutoID(), JBoltUserKit.getUserId(), sysScandeliver.getName(),"的字段["+column+"]值:"+sysScandeliver.get(column));
		/**
		switch(column){
		    case "isDeleted":
		        break;
		    case "state":
		        break;
		}
		*/
		return null;
	}

	public List<Record> rcvpland(Kv kv) {
		return dbTemplate("sysscandeliver.rcvpland", kv).find();
	}

	public List<Record> customeraddr(Kv kv) {
		return dbTemplate("sysscandeliver.customeraddr", kv).find();
	}


	/**
	 * 执行JBoltTable表格整体提交
	 *
	 * @param jBoltTable
	 * @return
	 */
	public Ret submitByJBoltTable(JBoltTable jBoltTable) {
		if(jBoltTable.getSaveRecordList()==null && jBoltTable.getDelete() == null && jBoltTable.getUpdateRecordList()==null){
			return fail("行数据不能为空");
		}
		SysScandeliver sysotherin = jBoltTable.getFormModel(SysScandeliver.class,"sysscandeliver");
		//获取当前用户信息？
		User user = JBoltUserKit.getUser();
		Date now = new Date();
		tx(()->{
			//通过 id 判断是新增还是修改
			if(sysotherin.getAutoID() == null){
				sysotherin.setOrganizeCode(getOrgCode());
				sysotherin.setCreatePerson(user.getName());
				sysotherin.setCreateDate(now);
				sysotherin.setModifyPerson(user.getName());
				sysotherin.setAuditPerson(user.getName());
				sysotherin.setModifyDate(now);
				sysotherin.setModifyDate(now);
				//主表新增
				ValidationUtils.isTrue(sysotherin.save(), ErrorMsg.SAVE_FAILED);
			}else{
				sysotherin.setModifyPerson(user.getName());
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
	private void saveTableSubmitDatas(JBoltTable jBoltTable,SysScandeliver sysotherin){
		List<Record> list = jBoltTable.getSaveRecordList();
		if(CollUtil.isEmpty(list)) return;
		ArrayList<SysScandeliverdetail> sysproductindetail = new ArrayList<>();
		Date now = new Date();
		for (int i=0;i<list.size();i++) {
			Record row = list.get(i);
			SysScandeliverdetail sysdetail = new SysScandeliverdetail();
			sysdetail.setAutoID(JBoltSnowflakeKit.me.nextIdStr());
//            row.set("iautoid", JBoltSnowflakeKit.me.nextId());
			sysdetail.setBarcode(row.get("barcode"));
			sysdetail.setPackBarcode(row.get("ccontainercode"));
			sysdetail.setInvCode(row.get("cinvcode"));
			sysdetail.setWhCode(row.get("whcode"));
			sysdetail.setQty(new BigDecimal(row.get("qty").toString()));
			sysdetail.setMasID(sysotherin.getAutoID());
			sysdetail.setSourceBillType(row.getStr("sourcebilltype"));
			sysdetail.setSourceBillNo(row.getStr("sourcebillno"));
			sysdetail.setSourceBillDid(row.getStr("sourcebilldid"));
			sysdetail.setSourceBillID(row.getStr("sourcebilldid"));
			sysdetail.setCreateDate(now);
			sysdetail.setModifyDate(now);
			sysproductindetail.add(sysdetail);
		}
		sysscandeliverdetailservice.batchSave(sysproductindetail);
	}
	//可编辑表格提交-修改数据
	private void updateTableSubmitDatas(JBoltTable jBoltTable,SysScandeliver sysotherin){
		List<Record> list = jBoltTable.getUpdateRecordList();
		if(CollUtil.isEmpty(list)) return;
		ArrayList<SysScandeliverdetail> sysproductindetail = new ArrayList<>();
		Date now = new Date();
		for(int i = 0;i < list.size(); i++){
			Record row = list.get(i);
			SysScandeliverdetail sysdetail = new SysScandeliverdetail();
			sysdetail.setAutoID(row.get("autoid").toString());
			sysdetail.setBarcode(row.get("barcode"));
			sysdetail.setInvCode(row.get("cinvcode"));
			System.out.println(row.get("whcode").toString());
			sysdetail.setWhCode(row.get("whcode").toString());
			sysdetail.setQty(new BigDecimal(row.get("qty").toString()));
			sysdetail.setMasID(sysotherin.getAutoID());
			sysdetail.setSourceBillType(row.getStr("sourcebilltype"));
			sysdetail.setSourceBillNo(row.getStr("sourcebillno"));
			sysdetail.setSourceBillDid(row.getStr("sourcebilldid"));
			sysdetail.setSourceBillID(row.getStr("sourcebilldid"));
			sysdetail.setCreateDate(now);
			sysdetail.setModifyDate(now);
			sysproductindetail.add(sysdetail);

		}
		sysscandeliverdetailservice.batchUpdate(sysproductindetail);
	}
	//可编辑表格提交-删除数据
	private void deleteTableSubmitDatas(JBoltTable jBoltTable){
		Object[] ids = jBoltTable.getDelete();
		if(ArrayUtil.isEmpty(ids)) return;
		sysscandeliverdetailservice.deleteByIds(ids);
	}

}