package cn.rjtech.admin.processdefect;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.ProcessDefect;
import cn.rjtech.util.BillNoUtils;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.Date;
import java.util.List;

/**
 * 制程异常品记录 Service
 * @ClassName: ProcessDefectService
 * @author: RJ
 * @date: 2023-04-21 15:49
 */
public class ProcessdefectService extends BaseService<ProcessDefect> {

	private final ProcessDefect dao = new ProcessDefect().dao();

	@Override
	protected ProcessDefect dao() {
		return dao;
	}

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	public Page<Record> paginateAdminDatas(int pageSize, int pageNumber, Okv kv) {
		return dbTemplate(dao()._getDataSourceConfigName(), "ProcessDefect.paginateAdminDatas", kv).paginate(pageNumber, pageSize);
	}

	/**
	 * 保存
	 * @param processDefect
	 * @return
	 */
	public Ret save(ProcessDefect processDefect) {
		if(processDefect==null || isOk(processDefect.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(processDefect.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=processDefect.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(processDefect.getIautoid(), JBoltUserKit.getUserId(), processDefect.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param processDefect
	 * @return
	 */
	public Ret update(ProcessDefect processDefect) {
		if(processDefect==null || notOk(processDefect.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		ProcessDefect dbProcessDefect=findById(processDefect.getIAutoId());
		if(dbProcessDefect==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(processDefect.getName(), processDefect.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=processDefect.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(processDefect.getIautoid(), JBoltUserKit.getUserId(), processDefect.getName());
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
	 * @param processDefect 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(ProcessDefect processDefect, Kv kv) {
		//addDeleteSystemLog(processDefect.getIautoid(), JBoltUserKit.getUserId(),processDefect.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param processDefect 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(ProcessDefect processDefect, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(processDefect, kv);
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
	 * @param processDefect 要toggle的model
	 * @param column 操作的哪一列
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanToggle(ProcessDefect processDefect,String column, Kv kv) {
		//没有问题就返回null  有问题就返回提示string 字符串
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(ProcessDefect processDefect, String column, Kv kv) {
		//addUpdateSystemLog(processDefect.getIautoid(), JBoltUserKit.getUserId(), processDefect.getName(),"的字段["+column+"]值:"+processDefect.get(column));
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param processDefect model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(ProcessDefect processDefect, Kv kv) {
		//这里用来覆盖 检测ProcessDefect是否被其它表引用
		return null;
	}

	//更新状态并保存数据方法
	public Ret updateEditTable(Kv formRecord) {
		Date now = new Date();

		tx(() -> {
			//判断是否有主键id
			if(isOk(formRecord.getStr("iautoid"))){
				ProcessDefect processDefect = findById(formRecord.getLong("iautoid"));
				if (processDefect.getIStatus() == 1){
					//录入数据
					processDefect.setCApproach(formRecord.getStr("capproach"));
					processDefect.setIStatus(2);


					//更新人和时间
					processDefect.setIUpdateBy(JBoltUserKit.getUserId());
					processDefect.setCUpdateName(JBoltUserKit.getUserName());
					processDefect.setDUpdateTime(now);
				}
				processDefect.update();
			}else{
				//保存未有主键的数据
				processDefectLinesave(formRecord, now);
			}
			return true;
		});


		return SUCCESS;
	}



	//未有主键的保存方法
	public void processDefectLinesave(Kv formRecord, Date now){
		System.out.println("formRecord==="+formRecord);
		ProcessDefect processDefect = new ProcessDefect();
		processDefect.setIAutoId(formRecord.getLong("iautoid"));

		//制造工单-特殊领料单主表明细
		processDefect.setIIssueId(formRecord.getLong("iissueid"));
		processDefect.setIMoDocId(formRecord.getLong("imodocid"));
		processDefect.setIDepartmentId(formRecord.getLong("idepartmentid"));
		processDefect.setDDemandDate(formRecord.getDate("ddemanddate"));

		//录入填写的数据
		processDefect.setIStatus(1);
		processDefect.setProcessName(formRecord.getStr("processname"));
		processDefect.setIDqQty(formRecord.getBigDecimal("idqqty"));
		processDefect.setIRespType(formRecord.getInt("iresptype"));
		processDefect.setIsFirstTime(formRecord.getBoolean("isfirsttime"));
		processDefect.setCBadnessSns(formRecord.getStr("cbadnesssns"));
		processDefect.setCDesc(formRecord.getStr("cdesc"));


		//必录入基本数据
		processDefect.setIAutoId(JBoltSnowflakeKit.me.nextId());
		String billNo = BillNoUtils.getcDocNo(getOrgId(), "YCP", 5);
		processDefect.setCDocNo(billNo);
		processDefect.setIOrgId(getOrgId());
		processDefect.setCOrgCode(getOrgCode());
		processDefect.setCOrgName(getOrgName());
		processDefect.setICreateBy(JBoltUserKit.getUserId());
		processDefect.setCCreateName(JBoltUserKit.getUserName());
		processDefect.setDCreateTime(now);
		processDefect.setIUpdateBy(JBoltUserKit.getUserId());
		processDefect.setCUpdateName(JBoltUserKit.getUserName());
		processDefect.setDUpdateTime(now);
		processDefect.save();
	}




	/**
	 * 获取制造工单信息
	 */
	public List<Record> OneMaterialTitle(String iissueid){
		return dbTemplate("ProcessDefect.OneMaterialTitle", Kv.by("iissueid", iissueid)).find();
	}



}