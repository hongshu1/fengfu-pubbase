package cn.rjtech.admin.QcInspection;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.QcInspection;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.Date;
import java.util.List;

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
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @return
	 */
	public Page<QcInspection> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
		return paginateByKeywords("iAutoId","DESC", pageNumber, pageSize, keywords, "iAutoId");
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
		return dbTemplate("QcInspection.DepartmentList", kv).find();
	}


	//更新状态并保存数据方法
	public Ret updateEditTable(JBoltTable jBoltTable, Kv formRecord) {
		Date now = new Date();

		tx(() -> {
			//判断是否有主键id
			if(isOk(formRecord.getStr("qcInspection.iautoid"))){
				QcInspection qcInspection = findById(formRecord.getLong("qcInspection.iautoid"));

					//录入数据
					qcInspection.setCChainNo(formRecord.getStr("qcInspection.cchainno"));
					qcInspection.setCChainName(formRecord.getStr("qcInspection.cchainname"));
					qcInspection.setIsFirstCase(formRecord.getBoolean("qcInspection.isfirstcase"));
					qcInspection.setDRecordDate(formRecord.getDate("qcInspection.drecorddate"));
					qcInspection.setIQcDutyPersonId(formRecord.getLong("psnname"));
					qcInspection.setIQcDutyDepartmentId(formRecord.getLong("depname"));
					qcInspection.setCPlace(formRecord.getStr("qcInspection.cplace"));
					qcInspection.setCProblem(formRecord.getStr("qcInspection.cproblem"));
					qcInspection.setCAnalysis(formRecord.getStr("qcInspection.canalysis"));
					qcInspection.setCMeasure(formRecord.getStr("qcInspection.cmeasure"));
					qcInspection.setCMeasure(formRecord.getStr("qcInspection.cmeasure"));
					qcInspection.setDDate(formRecord.getDate("qcInspection.ddate"));
					qcInspection.setIDutyPersonId(formRecord.getLong("qcInspection.idutypersonid"));
					qcInspection.setIEstimate(formRecord.getInt("qcInspection.iestimate"));
					qcInspection.setCMeasureAttachments(formRecord.getStr("fileData"));

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
		qcInspection.setIAutoId(formRecord.getLong("qcInspection.iautoid"));


		//录入填写的数据
		qcInspection.setCChainNo(formRecord.getStr("qcInspection.cchainno"));
		qcInspection.setCChainName(formRecord.getStr("qcInspection.cchainname"));
		qcInspection.setIsFirstCase(formRecord.getBoolean("qcInspection.isfirstcase"));
		qcInspection.setDRecordDate(formRecord.getDate("qcInspection.drecorddate"));
//		qcInspection.setIQcDutyPersonId(formRecord.getLong("psnname"));    //人员
//		qcInspection.setIQcDutyDepartmentId(formRecord.getLong("depname"));    //部门
		qcInspection.setCPlace(formRecord.getStr("qcInspection.cplace"));
		qcInspection.setCProblem(formRecord.getStr("qcInspection.cproblem"));
		qcInspection.setCAnalysis(formRecord.getStr("qcInspection.canalysis"));
		qcInspection.setCMeasure(formRecord.getStr("qcInspection.cmeasure"));
		qcInspection.setCMeasure(formRecord.getStr("qcInspection.cmeasure"));
		qcInspection.setDDate(formRecord.getDate("qcInspection.ddate"));
		qcInspection.setIDutyPersonId(formRecord.getLong("qcInspection.idutypersonid"));
		qcInspection.setIEstimate(formRecord.getInt("qcInspection.iestimate"));
		if (notNull(formRecord.getStr("fileData"))){
			qcInspection.setCMeasureAttachments(formRecord.getStr("fileData"));
		}

		//必录入基本数据
		qcInspection.setIAutoId(JBoltSnowflakeKit.me.nextId());

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
	 * 获取附件信息
	 * @return
	 */
	public QcInspection getFilesById(String supplierInfoId){
		return findFirst("SELECT file_name as fileName,id local_url FROM UGCFF_MOM_System.dbo.jb_jbolt_file  WHERE id = '"+supplierInfoId+"'");
	}


}