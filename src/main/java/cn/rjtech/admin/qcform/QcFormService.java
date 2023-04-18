package cn.rjtech.admin.qcform;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.Dictionary;
import cn.jbolt.core.model.User;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.core.ui.jbolttable.JBoltTableMulti;
import cn.rjtech.admin.qcformitem.QcFormItemService;
import cn.rjtech.admin.qcformparam.QcFormParamService;
import cn.rjtech.base.exception.ParameterException;
import cn.rjtech.model.momdata.QcFormItem;
import cn.rjtech.model.momdata.QcFormParam;
import cn.rjtech.util.StreamUtils;
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
import cn.rjtech.model.momdata.QcForm;
import com.jfinal.plugin.activerecord.Record;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static cn.hutool.core.text.StrPool.COMMA;

/**
 * 质量建模-检验表格
 * @ClassName: QcFormService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-27 17:53
 */
public class QcFormService extends BaseService<QcForm> {
	private final QcForm dao=new QcForm().dao();

	@Override
	protected QcForm dao() {
		return dao;
	}

	@Override
	protected int systemLogTargetType() {
		return ProjectSystemLogTargetType.NONE.getValue();
	}

	@Inject
	QcFormItemService qcFormItemService;

	@Inject
	QcFormParamService qcFormParamService;


	/**
	 * 后台管理数据查询
	 * @param pageNumber 第几页
	 * @param pageSize   每页几条数据
	 * @param keywords   关键词
	 * @param isDeleted 删除状态：0. 未删除 1. 已删除
	 * @param isEnabled 是否启用：0. 否 1. 是
	 * @return
	 */
	public Page<QcForm> getAdminDatas(int pageNumber, int pageSize, String keywords, Boolean isDeleted, Boolean isEnabled) {
		//创建sql对象
		Sql sql = selectSql().page(pageNumber,pageSize);
		//sql条件处理
		sql.eqBooleanToChar("isDeleted",isDeleted);
		sql.eqBooleanToChar("isEnabled",isEnabled);
		//关键词模糊查询
		sql.likeMulti(keywords,"cOrgName", "cQcFormName", "cCreateName", "cUpdateName");
		//排序
		sql.desc("iAutoId");
		return paginate(sql);
	}



	/**
	 * 后台管理数据查询
	 * @return
	 */
	public Page<Record> getAdminDatas(int pageSize, int pageNumber, Okv kv) {
		return dbTemplate(dao()._getDataSourceConfigName(), "qcform.AdminDatas", kv).paginate(pageNumber, pageSize);
	}

	/**
	 * 保存
	 * @param qcForm
	 * @return
	 */
	public Ret save(QcForm qcForm) {
		if(qcForm==null || isOk(qcForm.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(qcForm.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=qcForm.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(qcForm.getIAutoId(), JBoltUserKit.getUserId(), qcForm.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param qcForm
	 * @return
	 */
	public Ret update(QcForm qcForm) {
		if(qcForm==null || notOk(qcForm.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		QcForm dbQcForm=findById(qcForm.getIAutoId());
		if(dbQcForm==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(qcForm.getName(), qcForm.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=qcForm.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(qcForm.getIAutoId(), JBoltUserKit.getUserId(), qcForm.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param qcForm 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(QcForm qcForm, Kv kv) {
		//addDeleteSystemLog(qcForm.getIAutoId(), JBoltUserKit.getUserId(),qcForm.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param qcForm model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(QcForm qcForm, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(QcForm qcForm, String column, Kv kv) {
		//addUpdateSystemLog(qcForm.getIAutoId(), JBoltUserKit.getUserId(), qcForm.getName(),"的字段["+column+"]值:"+qcForm.get(column));
		/**
		 switch(column){
		 case "isDeleted":
		 break;
		 case "isEnabled":
		 break;
		 }
		 */
		return null;
	}

	/**
	 * 按主表qcformitem查询列表qcform
	 */
	public List<Record> getItemCombinedListByPId(Kv kv) {
		return dbTemplate("qcformitem.formItemLists", kv).find();
	}

	/**
	 * 按主表qcformparam查询列表
	 */
	public Page<Record> getQcFormParamListByPId(int pageNumber, int pageSize, Okv kv) {
		return dbTemplate("qcformparam.formParamList", kv).paginate(pageNumber, pageSize);
	}



	/**
	 * 按主表qcformtableparam查询列表
	 */
	public Page<Record> getQcFormTableParamListByPId(int pageNumber, int pageSize, Okv kv) {
		return dbTemplate("qcformtableparam.formTableParamList", kv).paginate(pageNumber, pageSize);
	}


	/**
	 * qcformitem可编辑表格提交
	 *
	 * @param jBoltTable 编辑表格提交内容
	 * @return
	 */
	public Ret submitByJBoltTable(JBoltTable jBoltTable) {
		QcForm qcForm = jBoltTable.getFormModel(QcForm.class, "qcForm");
		ValidationUtils.notNull(qcForm, JBoltMsg.PARAM_ERROR);


		Long userId = JBoltUserKit.getUserId();
		String name =JBoltUserKit.getUserName();
		Date nowDate = new Date();


		tx(()->{
			//修改
			Long headerId = null;
			if (isOk(qcForm.getIAutoId())) {
				qcForm.setCUpdateName(name);
				qcForm.setIUpdateBy(userId);
				qcForm.setDUpdateTime(nowDate);
				ValidationUtils.isTrue(qcForm.update(), JBoltMsg.FAIL);
				headerId = qcForm.getIAutoId();
			} else {
				//新增
				qcForm.setCCreateName(name);
				qcForm.setICreateBy(userId);
				qcForm.setDCreateTime(nowDate);
				qcForm.setCUpdateName(name);
				qcForm.setIUpdateBy(userId);
				qcForm.setDUpdateTime(nowDate);
				qcForm.setIsDeleted(0);
				ValidationUtils.isTrue(qcForm.save(), JBoltMsg.FAIL);
			}

			//判断table是否为空
			if (jBoltTable.saveIsNotBlank()) {
				List<QcFormItem> saveQcFormItem = jBoltTable.getSaveModelList(QcFormItem.class);
				int k = 0;
				for (QcFormItem saveQcFormItemLine :saveQcFormItem){
					k++;
					saveQcFormItemLine.setIQcFormId(qcForm.getIAutoId());
					Record BdQcFormItem = findFirstRecord(("SELECT max(iSeq) as iseq FROM Bd_QcFormItem WHERE iQcFormId = '"+qcForm.getIAutoId()+"' AND isDeleted = 0"));
					Integer iseq = BdQcFormItem.getInt("iseq");
					if (notNull(iseq)){
						int s= iseq + k ;
						System.out.println(s);
						saveQcFormItemLine.setISeq(iseq+k);
					}else {
						saveQcFormItemLine.setISeq(k);
					}
					saveQcFormItemLine.setIsDeleted(0);
				}
				qcFormItemService.batchSave(saveQcFormItem);

			}
			//更新
			if (jBoltTable.updateIsNotBlank()) {
				List<QcFormItem> updateQcFormItem = jBoltTable.getUpdateModelList(QcFormItem.class);
				updateQcFormItem.forEach(updateQcFormItemLine -> {
				});
				qcFormItemService.batchUpdate(updateQcFormItem);
			}



//			// 获取待删除数据 执行删除
//			if (jBoltTable.deleteIsNotBlank()) {
//				qcFormItemService.deleteByIds(jBoltTable.getDelete());
//			}

			return true;
		});

		return SUCCESS;
	}


	/**
	 * qcformparam可编辑表格提交
	 *
	 * @param jBoltTable 编辑表格提交内容
	 * @return
	 */
	public Ret QcFormParamJBoltTable(JBoltTable jBoltTable){
		QcForm qcForm = jBoltTable.getFormModel(QcForm.class, "qcForm");
		ValidationUtils.notNull(qcForm, JBoltMsg.PARAM_ERROR);


		Long userId = JBoltUserKit.getUserId();
		String name =JBoltUserKit.getUserName();
		Date nowDate = new Date();


		tx(()->{
			//修改
			if (isOk(qcForm.getIAutoId())) {
				qcForm.setCUpdateName(name);
				qcForm.setIUpdateBy(userId);
				qcForm.setDUpdateTime(nowDate);
				ValidationUtils.isTrue(qcForm.update(), JBoltMsg.FAIL);
			} else {
				//新增
				qcForm.setCCreateName(name);
				qcForm.setICreateBy(userId);
				qcForm.setDCreateTime(nowDate);
				qcForm.setCUpdateName(name);
				qcForm.setIUpdateBy(userId);
				qcForm.setDUpdateTime(nowDate);
				qcForm.setIsDeleted(0);
				ValidationUtils.isTrue(qcForm.save(), JBoltMsg.FAIL);
			}

			List<QcFormParam> saveQcFormParam = jBoltTable.getSaveModelList(QcFormParam.class);
			int k = 0;
			for (QcFormParam saveQcFormParamLine :saveQcFormParam){
				Long iqcformitemid = saveQcFormParamLine.get("iqcformitemid");
				k++;
				Record BdQcFormParam = findFirstRecord(("SELECT iItemParamSeq FROM Bd_QcFormParam WHERE iQcFormItemId = '"+iqcformitemid+"' AND isDeleted = 0"));
				if (notNull(BdQcFormParam)){
					int  isep =  BdQcFormParam.size();
					saveQcFormParamLine.setIItemParamSeq(isep+k);
				}else {
					saveQcFormParamLine.setIItemParamSeq(k);
				}
				saveQcFormParamLine.setIsDeleted(0);
			}
			qcFormParamService.batchSave(saveQcFormParam);
			//更新
			if (jBoltTable.updateIsNotBlank()) {
				List<QcFormParam> updateQcFormParam = jBoltTable.getUpdateModelList(QcFormParam.class);
				updateQcFormParam.forEach(updateQcFormItemLine -> {

				});
				qcFormParamService.batchUpdate(updateQcFormParam);
			}
			// 获取待删除数据 执行删除
//			if (jBoltTable.deleteIsNotBlank()) {
//				qcFormParamService.deleteByIds(jBoltTable.getDelete());
//			}
			return true;
		});
		return SUCCESS;
	}



	/**
	 * 检验报告记录详情表头一
	 */
	public List<Record> qcformtableparamOneTitle(String iAutoId){
		return dbTemplate("qcform.qcformtableparamOneTitle", Kv.by("iQcFormId", iAutoId)).find();
	}

	/**
	 * 检验报告记录详情表头二
	 */
	public List<Record> qcformtableparamTwoTitle(String iAutoId){
		return dbTemplate("qcform.qcformtableparamTwoTitle", Kv.by("iQcFormId", iAutoId)).find();
	}

	/**
	 * 拉取客户资源
	 */
	public List<Record> customerList(Kv kv) {
		kv.set("orgCode", getOrgCode());
		return dbTemplate("qcformtableparam.customerList", kv).find();
	}

	public List<Record> options() {
		return dbTemplate("qcform.AdminDatas", Kv.of("isenabled", "true")).find();
	}

}