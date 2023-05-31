package cn.rjtech.admin.qcinspection;

import cn.hutool.core.text.StrSplitter;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.QcInspection;
import cn.rjtech.util.BillNoUtils;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.*;

import static cn.hutool.core.text.StrPool.COMMA;


/**
 * 工程内品质巡查 Service
 * @ClassName: QcInspectionService
 * @author: RJ
 * @date: 2023-04-26 13:49
 */
public class QcInspectionService extends BaseService<QcInspection> {

	private final QcInspection dao = new QcInspection().dao();

	@Override
	protected QcInspection dao() {
		return dao;
	}

	/**
	 * 工程内品质巡查主页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param kv
	 * @return
	 */
	public Page<Record> paginateAdminDatas(int pageSize, int pageNumber, Kv kv) {
		return dbTemplate("qcinspection.paginateAdminDatas", kv).paginate(pageNumber, pageSize);
	}

	/**
	 * 保存
	 * @param qcInspection
	 * @return
	 */
	public Ret save(QcInspection qcInspection) {
		if(qcInspection==null || isOk(qcInspection.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(qcInspection.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=qcInspection.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(qcInspection.getIautoid(), JBoltUserKit.getUserId(), qcInspection.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param qcInspection
	 * @return
	 */
	public Ret update(QcInspection qcInspection) {
		if(qcInspection==null || notOk(qcInspection.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		QcInspection dbQcInspection=findById(qcInspection.getIAutoId());
		if(dbQcInspection==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(qcInspection.getName(), qcInspection.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=qcInspection.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(qcInspection.getIautoid(), JBoltUserKit.getUserId(), qcInspection.getName());
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
	 * @param qcInspection 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(QcInspection qcInspection, Kv kv) {
		//addDeleteSystemLog(qcInspection.getIautoid(), JBoltUserKit.getUserId(),qcInspection.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param qcInspection 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(QcInspection qcInspection, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(qcInspection, kv);
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
	 * 切换isfirstcase属性
	 */
	public Ret toggleIsFirstCase(Long id) {
		return toggleBoolean(id, "isFirstCase");
	}

	/**
	 * 检测是否可以toggle操作指定列
	 * @param qcInspection 要toggle的model
	 * @param column 操作的哪一列
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanToggle(QcInspection qcInspection,String column, Kv kv) {
		//没有问题就返回null  有问题就返回提示string 字符串
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(QcInspection qcInspection, String column, Kv kv) {
		//addUpdateSystemLog(qcInspection.getIautoid(), JBoltUserKit.getUserId(), qcInspection.getName(),"的字段["+column+"]值:"+qcInspection.get(column));
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param qcInspection model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(QcInspection qcInspection, Kv kv) {
		//这里用来覆盖 检测QcInspection是否被其它表引用
		return null;
	}

	/**
	 * 拉取部门和人员资源
	 */
	public List<Record> DepartmentList(Kv kv) {
		kv.set("orgCode", getOrgCode());
		return dbTemplate("qcinspection.DepartmentList", kv).find();
	}


	//更新状态并保存数据方法
	public Ret updateEditTable(Kv formRecord) {
		Date now = new Date();

		tx(() -> {
			//判断是否有主键id
			if(isOk(formRecord.getStr("iautoid"))){
				QcInspection qcInspection = findById(formRecord.getLong("iautoid"));
					//录入数据
					qcInspection.setCChainNo(formRecord.getStr("cchainno"));
					qcInspection.setCChainName(formRecord.getStr("cchainname"));
					qcInspection.setIsFirstCase(formRecord.getBoolean("isfirstcase"));
					qcInspection.setDRecordDate(formRecord.getDate("drecorddate"));
					qcInspection.setIQcDutyPersonId(formRecord.getLong("depnameid"));    //人员
					qcInspection.setIQcDutyDepartmentId(formRecord.getLong("psnnameid"));    //部门
					qcInspection.setCPlace(formRecord.getStr("cplace"));
					qcInspection.setCProblem(formRecord.getStr("cproblem"));
					qcInspection.setCAnalysis(formRecord.getStr("canalysis"));
					qcInspection.setCMeasure(formRecord.getStr("cmeasure"));
					qcInspection.setDDate(formRecord.getDate("ddate"));
					qcInspection.setIDutyPersonId(formRecord.getLong("idutypersonid"));
					qcInspection.setIEstimate(formRecord.getInt("iestimate"));
				if (notNull(formRecord.getStr("fileData"))) {
					qcInspection.setCMeasureAttachments(formRecord.getStr("fileData"));
				}
					//更新人和时间
					qcInspection.setIUpdateBy(JBoltUserKit.getUserId());
					qcInspection.setCUpdateName(JBoltUserKit.getUserName());
					qcInspection.setDUpdateTime(now);


				qcInspection.update();
			}else{
				//保存未有主键的数据
				RcvDocfectLinesave(formRecord, now);
			}
			return true;
		});
		return SUCCESS;
	}


	//未有主键的保存方法
	public void RcvDocfectLinesave(Kv formRecord, Date now){
		System.out.println("formRecord==="+formRecord);
		System.out.println("now==="+now);
		QcInspection qcInspection = new QcInspection();
		qcInspection.setIAutoId(formRecord.getLong("iautoid"));


		//录入填写的数据
		qcInspection.setCChainNo(formRecord.getStr("cchainno"));
		qcInspection.setCChainName(formRecord.getStr("cchainname"));
		qcInspection.setIsFirstCase(formRecord.getBoolean("isfirstcase"));
		qcInspection.setDRecordDate(formRecord.getDate("drecorddate"));
		qcInspection.setIQcDutyPersonId(formRecord.getLong("depnameid"));    //人员
		qcInspection.setIQcDutyDepartmentId(formRecord.getLong("psnnameid"));    //部门
		qcInspection.setCPlace(formRecord.getStr("cplace"));
		qcInspection.setCProblem(formRecord.getStr("cproblem"));
		qcInspection.setCAnalysis(formRecord.getStr("canalysis"));
		qcInspection.setCMeasure(formRecord.getStr("cmeasure"));
		qcInspection.setDDate(formRecord.getDate("ddate"));
		qcInspection.setIDutyPersonId(formRecord.getLong("idutypersonid"));
		qcInspection.setIEstimate(formRecord.getInt("iestimate"));
		if (notNull(formRecord.getStr("fileData"))){
			qcInspection.setCMeasureAttachments(formRecord.getStr("fileData"));
		}

		//必录入基本数据
		qcInspection.setIAutoId(JBoltSnowflakeKit.me.nextId());

		String billNo = BillNoUtils.getcDocNo(getOrgId(), "QCI", 5);
		qcInspection.setCInspectionNo(billNo);
		qcInspection.setIOrgId(getOrgId());
		qcInspection.setCOrgCode(getOrgCode());
		qcInspection.setCOrgName(getOrgName());
		qcInspection.setICreateBy(JBoltUserKit.getUserId());
		qcInspection.setCCreateName(JBoltUserKit.getUserName());
		qcInspection.setDCreateTime(now);
		qcInspection.setIUpdateBy(JBoltUserKit.getUserId());
		qcInspection.setCUpdateName(JBoltUserKit.getUserName());
		qcInspection.setDUpdateTime(now);
		qcInspection.save();
	}

	/**
	 * 获取工程内品质巡查明细
	 * @return
	 */
	public Record getQcInspectionList(Long iautoid){
		return dbTemplate("qcinspection.getQcInspectionList", Kv.by("iautoid",iautoid)).findFirst();
	}



	/**
	 * 制程异常品查看明细api
	 */
	public Map<String, Object> getqcinspectionApi(Long iautoid){

		Map<String, Object> map = new HashMap<>();
		Record qcInspection =this.getQcInspectionList(iautoid);
		map.put("qcInspection", qcInspection);
		String supplierInfoId = qcInspection.get("cmeasureattachments");
		List<String> url = new ArrayList<>();
		for (String idStr : StrSplitter.split(supplierInfoId, COMMA, true, true)) {
			url.add("'"+idStr+"'");
		}
		String urls = String.join(",",url);
		System.out.println("===="+urls);

		if (qcInspection.get("cmeasureattachments") != null) {
			List<Record> files = this.getFilesById(urls);
			map.put("files", files);
		}
		return map;
	}


	/**
	 * 获取附件信息
	 * @return
	 */
	public List<Record> getFilesById(String urls){
		return dbTemplate("qcinspection.getFilesById", Kv.by("urls", urls)).find();
	}
}