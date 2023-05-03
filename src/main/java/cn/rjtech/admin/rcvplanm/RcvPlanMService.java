package cn.rjtech.admin.rcvplanm;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.User;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.rjtech.constants.ErrorMsg;
import cn.rjtech.model.momdata.Monthorderd;
import cn.rjtech.model.momdata.RcvPlanD;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.plugin.activerecord.DbTemplate;
import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.rjtech.model.momdata.RcvPlanM;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.TableMapping;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 发货管理-取货计划主表
 * @ClassName: RcvPlanMService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-27 14:09
 */
public class RcvPlanMService extends BaseService<RcvPlanM> {
	private final RcvPlanM dao=new RcvPlanM().dao();
	private final RcvPlanD planDdao=new RcvPlanD().dao();
	@Override
	protected RcvPlanM dao() {
		return dao;
	}

	@Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }


	@Inject
	private RcvPlanMService service;

	@Inject
	private RcvPlanDService planDService;


	/**
	 * 后台管理数据查询
	 * @param pageNumber 第几页
	 * @param pageSize   每页几条数据

	 * @return
	 */
	public Page<Record> getAdminDatas(int pageNumber, int pageSize, Kv kv) {
		Page<Record> paginate = dbTemplate("rcvplanm.recpor", kv).paginate(pageNumber, pageSize);

		return paginate;
	}

	/**
	 * 保存
	 * @param rcvPlanM
	 * @return
	 */
	public Ret save(RcvPlanM rcvPlanM, List<RcvPlanD> rcvPlanD) {
		if(rcvPlanM==null || isOk(rcvPlanM.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		tx(() -> {
			boolean success=rcvPlanM.save();
			if(rcvPlanD !=null ) {
				for (int i = 0; i < rcvPlanD.size(); i++) {
					RcvPlanD rcvPlanD1 = rcvPlanD.get(i);
					if( !isOk(rcvPlanD1.getIAutoId())){
						// 添加从表
//						rcvPlanD.save(rcvPlanD1);
					}
				}
			}
			return success;
		});
		
		return ret(true);
	}

	/**
	 * 更新
	 * @param rcvPlanM
	 * @return
	 */
	public Ret update(RcvPlanM rcvPlanM) {
		if(rcvPlanM==null || notOk(rcvPlanM.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		RcvPlanM dbRcvPlanM=findById(rcvPlanM.getIAutoId());
		if(dbRcvPlanM==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(rcvPlanM.getName(), rcvPlanM.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=rcvPlanM.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(rcvPlanM.getIAutoId(), JBoltUserKit.getUserId(), rcvPlanM.getName());
		}
		return ret(success);
	}

	/**
	 * 批量删除主从表
	 */
	public Ret deleteRmRdByIds(String ids) {
		tx(() -> {
			String[] split = ids.split(",");
			for(String s : split){
				updateColumn(s, "isdeleted", true);
				update("update SM_RcvPlanD  set  IsDeleted = 1 where  iRcvPlanMid = ?",s);
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
			update("update SM_RcvPlanD  set  IsDeleted = 1 where  iRcvPlanMid = ?",id);
			return true;
		});
		return ret(true);
	}
	/**
	 * 删除数据后执行的回调
	 * @param rcvPlanM 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(RcvPlanM rcvPlanM, Kv kv) {
		//addDeleteSystemLog(rcvPlanM.getIAutoId(), JBoltUserKit.getUserId(),rcvPlanM.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param rcvPlanM model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(RcvPlanM rcvPlanM, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(RcvPlanM rcvPlanM, String column, Kv kv) {
		//addUpdateSystemLog(rcvPlanM.getIAutoId(), JBoltUserKit.getUserId(), rcvPlanM.getName(),"的字段["+column+"]值:"+rcvPlanM.get(column));
		/**
		switch(column){
		    case "IsDeleted":
		        break;
		}
		*/
		return null;
	}

	/**
	 * 执行JBoltTable表格整体提交
	 *
	 * @param jBoltTable
	 * @return
	 */
	public Ret submitByJBoltTable(JBoltTable jBoltTable) {
		RcvPlanM rcvplanm = jBoltTable.getFormModel(RcvPlanM.class,"rcvplanm");
		//获取当前用户信息？
		User user = JBoltUserKit.getUser();
		Date now = new Date();
		tx(()->{
			//通过 id 判断是新增还是修改
			if(rcvplanm.getIAutoId() == null){
				rcvplanm.setIOrgId(getOrgId());
				rcvplanm.setCOrgCode(getOrgCode());
				rcvplanm.setCOrgName(getOrgName());
				rcvplanm.setIStatus(1);
				rcvplanm.setIAuditWay(1);
				rcvplanm.setIAuditStatus(0);
				rcvplanm.setICreateBy(user.getId());
				rcvplanm.setCCreateName(user.getName());
				rcvplanm.setDCreateTime(now);
				rcvplanm.setIUpdateBy(user.getId());
				rcvplanm.setCUpdateName(user.getName());
				rcvplanm.setDUpdateTime(now);
				//主表新增
				ValidationUtils.isTrue(rcvplanm.save(), ErrorMsg.SAVE_FAILED);
			}else{
				rcvplanm.setIUpdateBy(user.getId());
				rcvplanm.setCUpdateName(user.getName());
				rcvplanm.setDUpdateTime(now);
				//主表修改
				ValidationUtils.isTrue(rcvplanm.update(), ErrorMsg.UPDATE_FAILED);
			}
			//从表的操作
			// 获取保存数据（执行保存，通过 getSaveRecordList）
			saveTableSubmitDatas(jBoltTable,rcvplanm);
			//获取修改数据（执行修改，通过 getUpdateRecordList）
			updateTableSubmitDatas(jBoltTable,rcvplanm);
			//获取删除数据（执行删除，通过 getDelete）
			deleteTableSubmitDatas(jBoltTable);
			return true;
		});
		return SUCCESS;
	}

	//可编辑表格提交-新增数据
	private void saveTableSubmitDatas(JBoltTable jBoltTable,RcvPlanM rcvplanm){
		List<Record> list = jBoltTable.getSaveRecordList();
		if(CollUtil.isEmpty(list)) return;
		for (int i=0;i<list.size();i++) {
			Record row = list.get(i);
			row.set("isdeleted", "0");
			row.set("ircvplanmid", rcvplanm.getIAutoId());
			row.set("iautoid", JBoltSnowflakeKit.me.nextId());
			row.remove("cinvcode1");
			row.remove("cinvname1");
		}
		planDService.batchSaveRecords(list);
	}
	//可编辑表格提交-修改数据
	private void updateTableSubmitDatas(JBoltTable jBoltTable,RcvPlanM rcvplanm){
		List<Record> list = jBoltTable.getUpdateRecordList();
		if(CollUtil.isEmpty(list)) return;
		for(int i = 0;i < list.size(); i++){
			Record row = list.get(i);
			row.remove("cinvcode1");
			row.remove("cinvname1");
		}
		planDService.batchUpdateRecords(list);
	}
	//可编辑表格提交-删除数据
	private void deleteTableSubmitDatas(JBoltTable jBoltTable){
		Object[] ids = jBoltTable.getDelete();
		if(ArrayUtil.isEmpty(ids)) return;
		for (Object id : ids) {
			planDService.deleteById(id);
		}
	}
}