package cn.rjtech.admin.spotcheckformparam;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.QcFormParam;
import cn.rjtech.model.momdata.SpotCheckFormParam;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.ArrayList;
import java.util.List;

/**
 * 质量建模-点检表格参数
 * @ClassName: SpotCheckFormParamService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-31 11:34
 */
public class SpotCheckFormParamService extends BaseService<SpotCheckFormParam> {
	private final SpotCheckFormParam dao=new SpotCheckFormParam().dao();
	@Override
	protected SpotCheckFormParam dao() {
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
	public Page<SpotCheckFormParam> getAdminDatas(int pageNumber, int pageSize, Boolean isDeleted) {
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
	 * @param spotCheckFormParam
	 * @return
	 */
	public Ret save(SpotCheckFormParam spotCheckFormParam) {
		if(spotCheckFormParam==null || isOk(spotCheckFormParam.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		boolean success=spotCheckFormParam.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(spotCheckFormParam.getIAutoId(), JBoltUserKit.getUserId(), spotCheckFormParam.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param spotCheckFormParam
	 * @return
	 */
	public Ret update(SpotCheckFormParam spotCheckFormParam) {
		if(spotCheckFormParam==null || notOk(spotCheckFormParam.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		SpotCheckFormParam dbSpotCheckFormParam=findById(spotCheckFormParam.getIAutoId());
		if(dbSpotCheckFormParam==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		boolean success=spotCheckFormParam.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(spotCheckFormParam.getIAutoId(), JBoltUserKit.getUserId(), spotCheckFormParam.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param spotCheckFormParam 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(SpotCheckFormParam spotCheckFormParam, Kv kv) {
		//addDeleteSystemLog(spotCheckFormParam.getIAutoId(), JBoltUserKit.getUserId(),spotCheckFormParam.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param spotCheckFormParam model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(SpotCheckFormParam spotCheckFormParam, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(SpotCheckFormParam spotCheckFormParam, String column, Kv kv) {
		//addUpdateSystemLog(spotCheckFormParam.getIAutoId(), JBoltUserKit.getUserId(), spotCheckFormParam.getName(),"的字段["+column+"]值:"+spotCheckFormParam.get(column));
		/**
		switch(column){
		    case "isDeleted":
		        break;
		}
		*/
		return null;
	}
	public SpotCheckFormParam createQcFormParam(Long id, Long qcFormId, Long qcFormItemId, Long qcItemId, Long qcParamId, Integer itemSeq, Integer itemParamSeq, Boolean isDeleted){
		SpotCheckFormParam qcFormParam = new SpotCheckFormParam();
		if (ObjUtil.isNull(id)){
			id = JBoltSnowflakeKit.me.nextId();
		}
		qcFormParam.setIAutoId(id);
		qcFormParam.setISpotCheckFormId(qcFormId);
		qcFormParam.setISpotCheckFormItemId(qcFormItemId);
		qcFormParam.setIQcItemId(qcItemId);
		qcFormParam.setISpotCheckParamId(qcParamId);
		qcFormParam.setIItemSeq(itemSeq);
		qcFormParam.setIItemParamSeq(itemParamSeq);
		qcFormParam.setIsDeleted(isDeleted);
		return qcFormParam;
	}

	public List<SpotCheckFormParam> createQcFormParamList(Long qcFormId, JSONArray formParamArray){
		if (CollUtil.isEmpty(formParamArray)){
			return null;
		}
		List<SpotCheckFormParam> qcFormParamList = new ArrayList<>();
		for (Object obj : formParamArray){
			JSONObject jsonObject=  (JSONObject)obj;
			SpotCheckFormParam qcFormParam = createQcFormParam(
					null,
					qcFormId,
					jsonObject.getLong(SpotCheckFormParam.ISPOTCHECKFORMITEMID.toLowerCase()),
					jsonObject.getLong(QcFormParam.IQCITEMID.toLowerCase()),
					jsonObject.getLong(QcFormParam.IQCPARAMID.toLowerCase()),
					jsonObject.getInteger(SpotCheckFormParam.IITEMSEQ.toLowerCase()),
					jsonObject.getInteger(SpotCheckFormParam.IITEMPARAMSEQ.toLowerCase()),
					false
			);
			qcFormParamList.add(qcFormParam);
		}
		return qcFormParamList;
	}

	public void removeByQcFormId(Long formId){
		delete("DELETE Bd_SpotCheckFormParam WHERE iSpotCheckFormId = ?", formId);
	}

	public List<Record> getQcFormParamListByPId(Long qcFormId) {
		if (ObjUtil.isNull(qcFormId)){
			return null;
		}
		return dbTemplate("spotcheckformparam.findByFormId", Okv.by("qcFormId", qcFormId)).find();
	}


	public Page<Record> qcformparamlist(int pageNumber, int pageSize, Okv kv) {
		return dbTemplate("spotcheckformparam.qcformparamlist", kv).paginate(pageNumber, pageSize);
	}
}