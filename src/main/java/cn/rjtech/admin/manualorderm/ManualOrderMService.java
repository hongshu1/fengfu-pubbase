package cn.rjtech.admin.manualorderm;

import cn.hutool.core.util.ArrayUtil;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.rjtech.admin.manualorderd.ManualOrderDService;
import cn.rjtech.model.momdata.ManualOrderD;
import com.jfinal.aop.Inject;
import com.jfinal.plugin.activerecord.Page;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.ManualOrderM;
import com.jfinal.plugin.activerecord.Record;

import static cn.jbolt.core.util.JBoltArrayUtil.COMMA;

/**
 * 客户订单-手配订单主表
 * @ClassName: ManualOrderMService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-10 15:18
 */
public class ManualOrderMService extends BaseService<ManualOrderM> {
	private final ManualOrderM dao=new ManualOrderM().dao();

	@Inject
	private ManualOrderDService manualOrderDService;

	@Override
	protected ManualOrderM dao() {
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
     * @param kv
	 * @return
	 */
	public Page<Record> getAdminDatas(int pageNumber, int pageSize, Kv kv) {
		return dbTemplate("manualorderm.list",kv).paginate(pageNumber,pageSize);
	}

	/**
	 * 保存
	 * @param manualOrderM
	 * @return
	 */
	public Ret save(ManualOrderM manualOrderM) {
		if(manualOrderM==null || isOk(manualOrderM.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		if(exists("cOrderNo",manualOrderM.getCOrderNo())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		setManualOrderM(manualOrderM);
		manualOrderM.setIAuditStatus(0);
		boolean success=manualOrderM.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(manualOrderM.getIAutoId(), JBoltUserKit.getUserId(), manualOrderM.getName());
		}
		return ret(success);
	}

	/**
	 * 设置参数
	 * @param manualOrderM
	 * @return
	 */
	private ManualOrderM setManualOrderM(ManualOrderM manualOrderM){
		manualOrderM.setCOrgCode(getOrgCode());
		manualOrderM.setCOrgName(getOrgName());
		manualOrderM.setIOrgId(getOrgId());
		manualOrderM.setIsDeleted(false);
		Long userId = JBoltUserKit.getUserId();
		manualOrderM.setICreateBy(userId);
		manualOrderM.setIUpdateBy(userId);
		String userName = JBoltUserKit.getUserName();
		manualOrderM.setCCreateName(userName);
		manualOrderM.setCUpdateName(userName);
		Date date = new Date();
		manualOrderM.setDCreateTime(date);
		manualOrderM.setDUpdateTime(date);
		return manualOrderM;
	}
	
	/**
	 * 更新
	 * @param manualOrderM
	 * @return
	 */
	public Ret update(ManualOrderM manualOrderM) {
		if(manualOrderM==null || notOk(manualOrderM.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		ManualOrderM dbManualOrderM=findById(manualOrderM.getIAutoId());
		if(dbManualOrderM==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(manualOrderM.getName(), manualOrderM.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=manualOrderM.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(manualOrderM.getIAutoId(), JBoltUserKit.getUserId(), manualOrderM.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param manualOrderM 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(ManualOrderM manualOrderM, Kv kv) {
		//addDeleteSystemLog(manualOrderM.getIAutoId(), JBoltUserKit.getUserId(),manualOrderM.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param manualOrderM model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(ManualOrderM manualOrderM, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

	public Ret saveForm(ManualOrderM manualOrderM, JBoltTable jBoltTable) {
		AtomicReference<Ret> res = new AtomicReference<>();
		res.set(SUCCESS);
		tx(() -> {
			if(manualOrderM.getIAutoId() == null) {
				Ret save = save(manualOrderM);
				if(!save.isOk()){
					res.set(save);
					return false;
				}
			} else
				update(manualOrderM);
			List<ManualOrderD> saveBeanList = jBoltTable.getSaveBeanList(ManualOrderD.class);
			if(saveBeanList != null && saveBeanList.size() > 0){
				for (ManualOrderD manualOrderD : saveBeanList) {
					manualOrderD.setIManualOrderMid(manualOrderM.getIAutoId());
					manualOrderD.setIsDeleted(false);
				}
				manualOrderDService.batchSave(saveBeanList);
			}
			List<ManualOrderD> updateBeanList = jBoltTable.getUpdateBeanList(ManualOrderD.class);
			if(updateBeanList != null && updateBeanList.size() > 0){
				for (ManualOrderD manualOrderD : updateBeanList) {
					manualOrderD.setIManualOrderMid(manualOrderM.getIAutoId());
				}
				manualOrderDService.batchUpdate(saveBeanList);
			}
			Object[] deletes = jBoltTable.getDelete();
			if (ArrayUtil.isNotEmpty(deletes)) {
				deleteMultiByIds(deletes);
			}
			return true;
		});
		return res.get();
	}

	public void deleteMultiByIds(Object[] deletes) {
		delete("DELETE FROM Co_ManualOrderD WHERE iAutoId IN (" + ArrayUtil.join(deletes, COMMA) + ") ");
	}

	public Ret batchHandle(Kv kv,int status) {
		List<Record> records = dbTemplate("manualorderm.list", kv).find();
		if(records != null && records.size() > 0){
			for (Record record : records) {
				record.put("iorderstatus",status);
			}
			batchUpdateRecords(records);
		}
		return SUCCESS;
	}

	public Ret batchDetect(Kv kv) {
		List<Record> records = dbTemplate("manualorderm.list", kv).find();
		if(records != null && records.size() > 0){
			for (Record record : records) {
				record.put("isdeleted",1);
			}
			batchUpdateRecords(records);
		}
		return SUCCESS;
	}
}