package cn.rjtech.admin.qcformparam;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.rjtech.model.momdata.QcFormItem;
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
import cn.rjtech.model.momdata.QcFormParam;
import com.jfinal.plugin.activerecord.Record;

import java.util.*;

/**
 * 质量建模-检验表格参数
 * @ClassName: QcFormParamService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-04 16:09
 */
public class QcFormParamService extends BaseService<QcFormParam> {
	private final QcFormParam dao=new QcFormParam().dao();
	@Override
	protected QcFormParam dao() {
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
	public Page<QcFormParam> getAdminDatas(int pageNumber, int pageSize, Boolean isDeleted) {
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
	 * @param qcFormParam
	 * @return
	 */
	public Ret save(QcFormParam qcFormParam) {
		if(qcFormParam==null || isOk(qcFormParam.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(qcFormParam.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=qcFormParam.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(qcFormParam.getIAutoId(), JBoltUserKit.getUserId(), qcFormParam.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param qcFormParam
	 * @return
	 */
	public Ret update(QcFormParam qcFormParam) {
		if(qcFormParam==null || notOk(qcFormParam.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		QcFormParam dbQcFormParam=findById(qcFormParam.getIAutoId());
		if(dbQcFormParam==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(qcFormParam.getName(), qcFormParam.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=qcFormParam.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(qcFormParam.getIAutoId(), JBoltUserKit.getUserId(), qcFormParam.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param qcFormParam 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(QcFormParam qcFormParam, Kv kv) {
		//addDeleteSystemLog(qcFormParam.getIAutoId(), JBoltUserKit.getUserId(),qcFormParam.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param qcFormParam model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(QcFormParam qcFormParam, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(QcFormParam qcFormParam, String column, Kv kv) {
		//addUpdateSystemLog(qcFormParam.getIAutoId(), JBoltUserKit.getUserId(), qcFormParam.getName(),"的字段["+column+"]值:"+qcFormParam.get(column));
		/**
		switch(column){
		    case "isDeleted":
		        break;
		}
		*/
		return null;
	}

	public Page<Record> qcformparamlist(int pageNumber, int pageSize, Okv kv) {
		return dbTemplate("qcformparam.qcformparamlist", kv).paginate(pageNumber, pageSize);
	}



	/**
	 * 上移
	 * @param
	 * @param iautoid
	 * @return
	 */
	public Ret up(Long iautoid) {
		QcFormParam qcFormParam=findById(iautoid);
		if(qcFormParam==null){
			return fail("数据不存在或已被删除");
		}
		Integer rank=qcFormParam.getIItemParamSeq();
		if(rank==null||rank<=0){
			return fail("顺序需要初始化");
		}
		if(rank==1){
			return fail("已经是第一个");
		}
		int isep = rank-1;
		QcFormParam upQcFormParam = findFirst("SELECT top 1 * FROM Bd_QcFormParam WHERE 1 = 1 and iItemParamSeq = '"+isep+"' AND isDeleted = 0  ORDER BY iAutoId ASC");

//		QcFormItem upQcFormItem=findFirst(Okv.by("iseq", rank-1));
		if(upQcFormParam==null){
			return fail("顺序需要初始化");
		}
		upQcFormParam.setIItemParamSeq(rank);
		qcFormParam.setIItemParamSeq(rank-1);

		upQcFormParam.update();
		qcFormParam.update();
		return SUCCESS;
	}



	/**
	 * 下移
	 * @param iautoid
	 * @return
	 */
	public Ret down(Long iautoid) {
		QcFormParam qcFormParam=findById(iautoid);
		if(qcFormParam==null){
			return fail("数据不存在或已被删除");
		}
		Integer rank=qcFormParam.getIItemParamSeq();
		Long iQcFormItemId=qcFormParam.getIQcFormItemId();
		if(rank==null||rank<=0){
			return fail("顺序需要初始化");
		}

		QcFormParam max = findFirst("SELECT max(iItemParamSeq) as iseq FROM Bd_QcFormParam WHERE iQcFormItemId = '"+iQcFormItemId+"' AND isDeleted = 0");
		Integer maxiSeq = max.getInt("iseq");
		if(rank==maxiSeq){
			return fail("已经是最后一个");
		}
		int isep = rank+1;
		QcFormParam upQcFormParam = findFirst("SELECT top 1 * FROM Bd_QcFormParam WHERE 1 = 1 and iItemParamSeq = '"+isep+"' AND isDeleted = 0  ORDER BY iAutoId ASC");

		if(upQcFormParam==null){
			return fail("顺序需要初始化");
		}
		upQcFormParam.setIItemParamSeq(rank);
		qcFormParam.setIItemParamSeq(rank+1);

		upQcFormParam.update();
		qcFormParam.update();
		return SUCCESS;
	}



	/**
	 * 删除
	 * @param iautoid
	 * @return
	 */
	public Ret delete(Long iautoid) {
		QcFormParam qcFormParam=findById(iautoid);
		//重新设置其他剩余顺序
		Long iqcformid = qcFormParam.getIQcFormItemId();
		int iSeq = qcFormParam.getIItemParamSeq();
		//软删除isDeleted
		deleteByTopnavId(iautoid);

		update("update Bd_QcFormParam set iItemParamSeq = iItemParamSeq - 1 WHERE iQcFormItemId ='"+iqcformid+"' AND iItemParamSeq >= 0 AND iItemParamSeq >= '"+iSeq+"' AND isDeleted = 0 ");

		return SUCCESS;
	}


	/**
	 * 删除一个顶部导航下的菜单配置
	 * @param iAutoId
	 */
	public void deleteByTopnavId(Long iAutoId) {
		if(isOk(iAutoId)) {
//			deleteBy(Okv.by("iautoid", iAutoId));
			update("update Bd_QcFormParam set isDeleted =  1 WHERE iAutoId ='"+iAutoId+"'");
		}
	}
	
	
	public List<Record> getQcFormParamListByPId(Long qcFormId) {
		if (ObjectUtil.isNull(qcFormId)){
			return null;
		}
		return dbTemplate("qcformparam.findByFormId", Okv.by("qcFormId", qcFormId)).find();
	}
	
	public QcFormParam createQcFormParam(Long id, Long qcFormId, Long qcFormItemId, Long qcItemId, Long qcParamId, Integer itemSeq, Integer itemParamSeq, Boolean isDeleted){
		QcFormParam qcFormParam = new QcFormParam();
		if (ObjectUtil.isNull(id)){
			id = JBoltSnowflakeKit.me.nextId();
		}
		qcFormParam.setIAutoId(id);
		qcFormParam.setIQcFormId(qcFormId);
		qcFormParam.setIQcFormItemId(qcFormItemId);
		qcFormParam.setIQcItemId(qcItemId);
		qcFormParam.setIQcParamId(qcParamId);
		qcFormParam.setIItemSeq(itemSeq);
		qcFormParam.setIItemParamSeq(itemParamSeq);
		qcFormParam.setIsDeleted(isDeleted);
		return qcFormParam;
	}
	
	public List<QcFormParam> createQcFormParamList(Long qcFormId, JSONArray formParamArray){
		if (CollectionUtil.isEmpty(formParamArray)){
			return null;
		}
		List<QcFormParam> qcFormParamList = new ArrayList<>();
		for (Object obj : formParamArray){
			JSONObject jsonObject=  (JSONObject)obj;
			QcFormParam qcFormParam = createQcFormParam(
					null,
					qcFormId,
					jsonObject.getLong(QcFormParam.IQCFORMITEMID.toLowerCase()),
					jsonObject.getLong(QcFormParam.IQCITEMID.toLowerCase()),
					jsonObject.getLong(QcFormParam.IQCPARAMID.toLowerCase()),
					jsonObject.getInteger(QcFormParam.IITEMSEQ.toLowerCase()),
					jsonObject.getInteger(QcFormParam.IITEMPARAMSEQ.toLowerCase()),
					false
			);
			qcFormParamList.add(qcFormParam);
		}
		return qcFormParamList;
	}
	
	public void removeByQcFormId(Long formId){
		delete("DELETE Bd_QcFormParam WHERE iQCFormId = ?", formId);
	}
}
