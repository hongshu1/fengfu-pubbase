package cn.rjtech.admin.processdefect;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.department.DepartmentService;
import cn.rjtech.admin.modoc.MoDocService;
import cn.rjtech.admin.operation.OperationService;
import cn.rjtech.admin.specmaterialsrcvm.SpecMaterialsRcvMService;
import cn.rjtech.model.momdata.MoDoc;
import cn.rjtech.model.momdata.Operation;
import cn.rjtech.model.momdata.ProcessDefect;
import cn.rjtech.model.momdata.SpecMaterialsRcvM;
import cn.rjtech.util.BillNoUtils;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 制程异常品记录 Service
 * @ClassName: ProcessDefectService
 * @author: RJ
 * @date: 2023-04-21 15:49
 */
public class ProcessdefectService extends BaseService<ProcessDefect> {

	private final ProcessDefect dao = new ProcessDefect().dao();

	@Inject
	private SpecMaterialsRcvMService specMaterialsRcvMService;      ////制造工单-来料表

	@Inject
	private OperationService operationService;//工序

	@Inject
	private MoDocService moDocService;
	@Inject
	private DepartmentService departmentService;

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
	public Page<Record> paginateAdminDatas(int pageSize, int pageNumber, Kv kv) {
		return dbTemplate("processdefect.paginateAdminDatas", kv).paginate(pageNumber, pageSize);
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
	 * @param iautoid
	 * @return
	 */
	public Ret delete(Long iautoid) {
		if(notOk(iautoid)){
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		ProcessDefect processDefect=findById(iautoid);
		if(processDefect==null) {
			return fail(JBoltMsg.DATA_NOT_EXIST);
		}
		//已审批
		if(processDefect.getIStatus()!=null&&processDefect.getIStatus().equals(3)) {
			return fail("单据已审批,不允许删除");
		}
		//更新删除状态
		processDefect.setIsDeleted(true);
		processDefect.update();
		return  SUCCESS;
		//return deleteById(id,true);
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
	public void editProcessDefect(Kv formRecord){
		Date now=new Date();
		MoDoc moDoc=moDocService.findById(formRecord.getLong("imodocid"));
		ValidationUtils.notNull(moDoc,"工单信息不存在");
		ProcessDefect processDefect=new ProcessDefect();
		processDefect.setIMoDocId(moDoc.getIAutoId());
		processDefect.setIInventoryId(moDoc.getIInventoryId());
		//部门
		processDefect.setIDepartmentId(moDoc.getIDepartmentId());
		//处置区分
		processDefect.setCApproach(formRecord.getStr("capproach"));
		//不良数量
		processDefect.setIDqQty(formRecord.getBigDecimal("idqqty"));
		//责任区分
		processDefect.setIRespType(formRecord.getInt("iresptype"));
        //不良项目
		processDefect.setCBadnessSns(formRecord.getStr("cbadnesssns"));
        //首发
		processDefect.setIsFirstTime(formRecord.getBoolean("isfirsttime"));
       //不良内容描述
		processDefect.setCDesc(formRecord.getStr("cdesc"));

		processDefect.setCoperationname(formRecord.getStr("processname"));

		processDefect.setIOperationId(formRecord.getLong("ioperationid"));
		tx(() -> {
			if (notOk(formRecord.getLong("iautoid"))) {
				String billNo = BillNoUtils.getcDocNo(getOrgId(), "YCP", 5);
				processDefect.setIStatus(1);
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
				processDefect.setDDemandDate(now);//需求日期
				processDefect.save();
			} else {
				ProcessDefect record = findById(formRecord.getLong("iautoid"));
				ValidationUtils.notNull(record, JBoltMsg.DATA_NOT_EXIST);

				//已审批
				if (record.getIStatus() != null && record.getIStatus().equals(3)) {
					ValidationUtils.error("单据已审批,不允许操作");
				}
				processDefect.setIAutoId(formRecord.getLong("iautoid"));
				processDefect.setIUpdateBy(JBoltUserKit.getUserId());
				processDefect.setCUpdateName(JBoltUserKit.getUserName());
				processDefect.setDUpdateTime(now);
				processDefect.update();

			}
			return  true;
		});



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
					processDefect.setIOperationId(formRecord.getLong("ioperationid"));
					processDefect.setCoperationname(formRecord.getStr("coperationname"));
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
		ProcessDefect processDefect = new ProcessDefect();
		processDefect.setIAutoId(formRecord.getLong("iautoid"));

		//制造工单-特殊领料单主表明细
		SpecMaterialsRcvM specMaterialsRcvM = specMaterialsRcvMService.findById(formRecord.getLong("iissueid"));
		processDefect.setIIssueId(specMaterialsRcvM.getIAutoId());
		processDefect.setIMoDocId(specMaterialsRcvM.getIMoDocId());
		processDefect.setIDepartmentId(specMaterialsRcvM.getIDepartmentId());
		processDefect.setDDemandDate(specMaterialsRcvM.getDDemandDate());

		//录入填写的数据
		processDefect.setIStatus(1);
		//processDefect.setProcessName(formRecord.getStr("processname"));
		processDefect.setIDqQty(formRecord.getBigDecimal("idqqty"));
		processDefect.setIRespType(formRecord.getInt("iresptype"));
		processDefect.setIsFirstTime(formRecord.getBoolean("isfirsttime"));
		processDefect.setCBadnessSns(formRecord.getStr("cbadnesssns"));
		processDefect.setCDesc(formRecord.getStr("cdesc"));
		processDefect.setIOperationId(formRecord.getLong("ioperationid"));
		processDefect.setCoperationname(formRecord.getStr("coperationname"));


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

   public  Ret saveprocessDefect(ProcessDefect processDefect){
	   if(processDefect==null) {
		   return fail(JBoltMsg.PARAM_ERROR);
	   }

		//工序
	  /*if(!isOk(processDefect.getIOperationId())){
		  return fail("指定工序信息");
	   }*/
	   if(isOk(processDefect.getIOperationId())) {
		   Operation operation = operationService.findById(processDefect.getIOperationId());
		   if (operation != null) {
			   processDefect.setCoperationname(operation.getCoperationname());//工序名称
		   }
	   }
       Date now=new Date();
	   String billNo = BillNoUtils.getcDocNo(getOrgId(), "YCP", 5);
	   processDefect.setIStatus(1);
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
	   processDefect.setDDemandDate(now);//需求日期
	   processDefect.save();



      return  SUCCESS;
   }
	public  Ret updateprocessDefect(ProcessDefect processDefect){
		if(processDefect==null || notOk(processDefect.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		ProcessDefect record=findById(processDefect.getIAutoId());
		if(record==null) {
			return fail(JBoltMsg.DATA_NOT_EXIST);
		}
		//已审批
		if(record.getIStatus()!=null&&record.getIStatus().equals(3)) {
			return fail("单据已审批,不允许操作");
		}
		//工序
	  /*if(!isOk(processDefect.getIOperationId())){
		  return fail("指定工序信息");
	   }*/
		if(isOk(processDefect.getIOperationId())) {
			Operation operation = operationService.findById(processDefect.getIOperationId());
			if (operation != null) {
				processDefect.setCoperationname(operation.getCoperationname());//工序名称
			}
		}




		processDefect.setIUpdateBy(JBoltUserKit.getUserId());
		processDefect.setCUpdateName(JBoltUserKit.getUserName());
		processDefect.setDUpdateTime(new Date());

		processDefect.update();
		return  SUCCESS;



	}

	public Ret subprocessDefect(ProcessDefect processDefect){
		if(processDefect==null) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		if(notOk(processDefect.getIAutoId())){
			Date now = new Date();
			String billNo = BillNoUtils.getcDocNo(getOrgId(), "YCP", 5);
			processDefect.setIStatus(2);
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
			processDefect.setDDemandDate(now);//需求日期
			processDefect.save();
		}else{
			//更新时需要判断数据存在
			ProcessDefect record=findById(processDefect.getIAutoId());
			if(record==null) {
				return fail(JBoltMsg.DATA_NOT_EXIST);
			}
			//已审批
			if(record.getIStatus()!=null&&record.getIStatus().equals(3)) {
				return fail("单据已审批,不允许操作");
			}
			processDefect.setIStatus(2);
			processDefect.setIUpdateBy(JBoltUserKit.getUserId());
			processDefect.setCUpdateName(JBoltUserKit.getUserName());
			processDefect.setDUpdateTime(new Date());

			processDefect.update();
		}
		return  SUCCESS;
	}


	/**
	 * 获取制造工单信息
	 */
	public List<Record> OneMaterialTitle(String iissueid){
		return dbTemplate("processdefect.OneMaterialTitle", Kv.by("iissueid", iissueid)).find();
	}

	//API制程异常品主页查询
	public Page<Record> getPageListApi(Integer pageNumber, Integer pageSize, Kv kv) {
		return dbTemplate("processdefect.paginateAdminDatasapi",kv).paginate(pageNumber,pageSize);
	}

	/**
	 * 制程异常品查看明细api
	 */
	public Map<String, Object> getProcessDefectApi(Long iautoid, Long iissueid,String type){

		Map<String, Object> map = new HashMap<>();

		ProcessDefect processDefect=findById(iautoid);
		SpecMaterialsRcvM specMaterialsRcvM = specMaterialsRcvMService.findById(iissueid);
		map.put("type",type );
		map.put("processDefect", processDefect);
		map.put("specMaterialsRcvM", specMaterialsRcvM);
		if (processDefect == null){
			return map;
		}
		if (processDefect.getIStatus() == 1) {
			map.put("isfirsttime", (processDefect.getIsFirstTime() == true) ? "首发" : "再发");
			map.put("iresptype", (processDefect.getIRespType() == 1) ? "本工序" : "其他");
		} else if (processDefect.getIStatus() == 2) {
			int getCApproach = Integer.parseInt(processDefect.getCApproach());
			map.put("capproach", (getCApproach == 1) ? "返修" : "报废");
			map.put("isfirsttime", (processDefect.getIsFirstTime() == true) ? "首发" : "再发");
			map.put("iresptype", (processDefect.getIRespType() == 1) ? "本工序" : "其他");
		}
		return map;
	}

	/**
	 * 打印数据
	 * @param kv 参数
	 * @return
	 */
	public Object getQRCodeCheck(Kv kv) {
		return dbTemplate("processdefect.containerPrintData",kv).find();
	}

	/**
	 *  工序数据源
	 */
	public List<Record> OperationDatas(Kv kv) {
		return dbTemplate("processdefect.OperationDatas", kv).find();
	}


}