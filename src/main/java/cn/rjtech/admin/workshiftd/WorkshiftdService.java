package cn.rjtech.admin.workshiftd;

import cn.hutool.core.util.ArrayUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.service.base.JBoltBaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.Workshiftd;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;

import static cn.hutool.core.text.StrPool.COMMA;

/**
 * 生产班次明细 Service
 * @ClassName: WorkshiftdService   
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-10-27 09:25  
 */
public class WorkshiftdService extends JBoltBaseService<Workshiftd> {
	private final Workshiftd dao=new Workshiftd().dao();
	@Override
	protected Workshiftd dao() {
		return dao;
	}
		
	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param iworkshiftmid
	 * @return
	 */
	public Page<Workshiftd> paginateAdminDatas(int pageNumber, int pageSize, String iworkshiftmid) {
		return paginateByKeywords("iautoid","DESC", pageNumber, pageSize, (isOk(iworkshiftmid)?iworkshiftmid:"I"), "iworkshiftmid");
	}

	public List<Record> list(String iworkshiftmid){
		return dbTemplate("workshiftd.getList", Kv.by("iworkshiftmid", iworkshiftmid)).find();
		//return Db.find("SELECT a.*,d.name FROM Bd_WorkShiftD a LEFT JOIN RJ_MOM_System.dbo.jb_dictionary d ON a.iType = d.sn AND d.type_key = 'worktype' WHERE a.iWorkShiftMId = ?", iworkshiftmid);
	}
	/**
	 * 保存
	 * @param workshiftd
	 * @return
	 */
	public Ret save(Workshiftd workshiftd) {
		if(workshiftd==null || isOk(workshiftd.getIautoid())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(workshiftd.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=workshiftd.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(workshiftd.getIautoid(), JBoltUserKit.getUserId(), workshiftd.getName());
		}
		return ret(success);
	}
	
	/**
	 * 更新
	 * @param workshiftd
	 * @return
	 */
	public Ret update(Workshiftd workshiftd) {
		if(workshiftd==null || notOk(workshiftd.getIautoid())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		Workshiftd dbWorkshiftd=findById(workshiftd.getIautoid());
		if(dbWorkshiftd==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(workshiftd.getName(), workshiftd.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=workshiftd.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(workshiftd.getIautoid(), JBoltUserKit.getUserId(), workshiftd.getName());
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
	 * @param workshiftd 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(Workshiftd workshiftd, Kv kv) {
		//addDeleteSystemLog(workshiftd.getIautoid(), JBoltUserKit.getUserId(),workshiftd.getName());
		return null;
	}
	
	/**
	 * 检测是否可以删除
	 * @param workshiftd 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(Workshiftd workshiftd, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(workshiftd, kv);
	}
	
	/**
	 * 设置返回二开业务所属的关键systemLog的targetType 
	 * @return
	 */
	@Override
	protected int systemLogTargetType() {
		return ProjectSystemLogTargetType.NONE.getValue();
	}

	public void deleteMultiByIds(Object[] deletes) {
		delete("DELETE FROM Bd_WorkShiftD WHERE iautoid IN (" + ArrayUtil.join(deletes, COMMA) + ") ");
	}
	
}