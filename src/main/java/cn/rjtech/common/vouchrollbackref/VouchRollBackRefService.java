package cn.rjtech.common.vouchrollbackref;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.common.model.VouchRollBackRef;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.sql.Connection;
import java.util.List;

/**
 * 回滚参照信息 Service
 * @ClassName: VouchRollBackRefService
 * @author: lidehui
 * @date: 2023-01-31 16:36
 */
public class VouchRollBackRefService extends BaseService<VouchRollBackRef> {

	private final VouchRollBackRef dao = new VouchRollBackRef().dao();

	@Override
	protected VouchRollBackRef dao() {
		return dao;
	}

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @return
	 */
	public Page<VouchRollBackRef> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
		return paginateByKeywords("AutoId","DESC", pageNumber, pageSize, keywords, "AutoId");
	}

	/**
	 * 保存
	 * @param vouchRollBackRef
	 * @return
	 */
	public Ret save(VouchRollBackRef vouchRollBackRef) {
		if(vouchRollBackRef==null || isOk(vouchRollBackRef.getAutoid())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(vouchRollBackRef.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=vouchRollBackRef.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(vouchRollBackRef.getAutoid(), JBoltUserKit.getUserId(), vouchRollBackRef.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param vouchRollBackRef
	 * @return
	 */
	public Ret update(VouchRollBackRef vouchRollBackRef) {
		if(vouchRollBackRef==null || notOk(vouchRollBackRef.getAutoid())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		VouchRollBackRef dbVouchRollBackRef = findById(vouchRollBackRef.getAutoid());
		if(dbVouchRollBackRef==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(vouchRollBackRef.getName(), vouchRollBackRef.getAutoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=vouchRollBackRef.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(vouchRollBackRef.getAutoid(), JBoltUserKit.getUserId(), vouchRollBackRef.getName());
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
	 * @param vouchRollBackRef 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(VouchRollBackRef vouchRollBackRef, Kv kv) {
		//addDeleteSystemLog(vouchRollBackRef.getAutoid(), JBoltUserKit.getUserId(),vouchRollBackRef.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param vouchRollBackRef 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(VouchRollBackRef vouchRollBackRef, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(vouchRollBackRef, kv);
	}

	/**
	 * 设置返回二开业务所属的关键systemLog的targetType 
	 * @return
	 */
	@Override
	protected int systemLogTargetType() {
		return ProjectSystemLogTargetType.NONE.getValue();
	}

	public void updateRe(VouchRollBackRef tsysVouchrollbackref) {
		update("UPDATE T_Sys_VouchRollBackRef SET ProcessValue =? WHERE VouchBusinessID=?",tsysVouchrollbackref.getProcessvalue(),tsysVouchrollbackref.getVouchbusinessid());
	}

	public Record rollback(String processId) {
		String sql = "select r.*,b.ProcessName,b.ProcessFlag as BProcessFlag,b.MapID as InitializeMapID,api.Url,api.Tag,api.Code,api.ResultType\n" +
				"\t\tfrom T_Sys_VouchRollback r\n" +
				"\t\tleft join T_Sys_VouchBusiness b on b.AutoID=r.ProcessID\n" +
				"\t\tleft join T_Sys_OpenAPI api on api.AutoID=b.APIID\n" +
				"\t\twhere r.MasID='" + processId + "'";
		//Record firstRecord = findFirstRecord(sql);
		return findFirstRecord(sql);
	}

	public List<Record> rollbackref(String seqBusinessID) {
		String sql = "select * from T_Sys_VouchRollBackRef where VouchBusinessID=?";
		return findRecord(sql,true, seqBusinessID);
	}

	public List<Record> rollbackref(String seqBusinessID, String processId) {
		String sql = "select * from T_Sys_VouchRollBackRef where VouchBusinessID=? and ProcessID=?";
		return findRecord(sql,true, seqBusinessID, processId);
	}

    public void updateRefInTx(VouchRollBackRef tsysVouchrollbackref) {
		tx(Connection.TRANSACTION_READ_UNCOMMITTED, () -> {
			update("UPDATE T_Sys_VouchRollBackRef SET ProcessValue =? WHERE VouchBusinessID=? and ProcessID=?",
					tsysVouchrollbackref.getProcessvalue(),tsysVouchrollbackref.getVouchbusinessid(), tsysVouchrollbackref.getProcessid());
			return true;
		});
    }

	public void saveVouchrollbackref(VouchRollBackRef vouchrollbackref) {
		tx(() -> {
			vouchrollbackref.save();
			return true;
		});
	}
}