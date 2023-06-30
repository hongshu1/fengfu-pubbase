package cn.rjtech.admin.prodformparam;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjUtil;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.rjtech.model.momdata.QcFormParam;
import cn.rjtech.model.momdata.QcParam;
import cn.rjtech.model.momdata.SpotCheckFormParam;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.rjtech.model.momdata.ProdFormParam;
import com.jfinal.plugin.activerecord.Record;

import java.util.ArrayList;
import java.util.List;

/**
 * 生产表单设置-生产表单参数
 * @ClassName: ProdFormParamService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-06-27 10:13
 */
public class ProdFormParamService extends BaseService<ProdFormParam> {
	private final ProdFormParam dao=new ProdFormParam().dao();
	@Override
	protected ProdFormParam dao() {
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
	 * @param isDeleted 删除状态: 0. 未删除 1. 已删除
	 * @return
	 */
	public Page<ProdFormParam> getAdminDatas(int pageNumber, int pageSize, Boolean isDeleted) {
		//创建sql对象
		Sql sql = selectSql().page(pageNumber,pageSize);
		//sql条件处理
		sql.eqBooleanToChar("isDeleted",isDeleted);
		//排序
		sql.desc("iAutoId");
		return paginate(sql);
	}

	/**
	 * 保存
	 * @param prodFormParam
	 * @return
	 */
	public Ret save(ProdFormParam prodFormParam) {
		if(prodFormParam==null || isOk(prodFormParam.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		boolean success=prodFormParam.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(spotCheckFormParam.getIAutoId(), JBoltUserKit.getUserId(), spotCheckFormParam.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param prodFormParam
	 * @return
	 */
	public Ret update(ProdFormParam prodFormParam) {
		if(prodFormParam==null || notOk(prodFormParam.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		ProdFormParam dbSpotCheckFormParam=findById(prodFormParam.getIAutoId());
		if(dbSpotCheckFormParam==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		boolean success=prodFormParam.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(spotCheckFormParam.getIAutoId(), JBoltUserKit.getUserId(), spotCheckFormParam.getName());
		}
		return ret(success);
	}


	public ProdFormParam createQcFormParam(Long id, Long qcFormId, Long qcFormItemId, Long qcItemId, Long qcParamId, Integer itemSeq, Integer itemParamSeq, Boolean isDeleted){
		ProdFormParam qcFormParam = new ProdFormParam();
		if (ObjUtil.isNull(id)){
			id = JBoltSnowflakeKit.me.nextId();
		}
		qcFormParam.setIProdParamId(qcParamId);
		qcFormParam.setIAutoId(id);
		qcFormParam.setIProdFormId(qcFormId);
		qcFormParam.setIProdFormItemId(qcFormItemId);
		qcFormParam.setIProdItemId(qcItemId);
		qcFormParam.setIItemSeq(itemSeq);
		qcFormParam.setIItemParamSeq(itemParamSeq);
		qcFormParam.setIsDeleted(isDeleted);
		return qcFormParam;
	}

	public List<ProdFormParam> createQcFormParamList(Long qcFormId, JSONArray formParamArray){
		if (CollectionUtil.isEmpty(formParamArray)){
			return null;
		}
		List<ProdFormParam> qcFormParamList = new ArrayList<>();
		for (Object obj : formParamArray){
			JSONObject jsonObject=  (JSONObject)obj;
			ProdFormParam qcFormParam = createQcFormParam(
					null,
					qcFormId,
					jsonObject.getLong(ProdFormParam.IPRODFORMITEMID.toLowerCase()),
					jsonObject.getLong(QcFormParam.IQCITEMID.toLowerCase()),
					jsonObject.getLong(QcFormParam.IQCPARAMID.toLowerCase()),
					jsonObject.getInteger(ProdFormParam.IITEMSEQ.toLowerCase()),
					jsonObject.getInteger(ProdFormParam.IITEMPARAMSEQ.toLowerCase()),
					false
			);
			qcFormParamList.add(qcFormParam);
		}
		return qcFormParamList;
	}

	public void removeByQcFormId(Long formId){
		delete("DELETE Bd_ProdFormParam WHERE iProdFormId = ?", formId);
	}

	public List<Record> getQcFormParamListByPId(Long qcFormId) {
		if (ObjUtil.isNull(qcFormId)){
			return null;
		}
		return dbTemplate("prodformparam.findByFormId", Okv.by("qcFormId", qcFormId)).find();
	}


	public Page<Record> qcformparamlist(int pageNumber, int pageSize, Okv kv) {
		return dbTemplate("prodformparam.qcformparamlist", kv).paginate(pageNumber, pageSize);
	}
}