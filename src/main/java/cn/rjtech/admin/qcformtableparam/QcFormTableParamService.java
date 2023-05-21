package cn.rjtech.admin.qcformtableparam;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.qcformtableitem.QcFormTableItemService;
import cn.rjtech.model.momdata.QcFormTableItem;
import cn.rjtech.model.momdata.QcFormTableParam;
import cn.rjtech.util.ValidationUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 质量建模-检验表格参数录入配置
 * @ClassName: QcFormTableParamService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-04 16:12
 */
public class QcFormTableParamService extends BaseService<QcFormTableParam> {
	private final QcFormTableParam dao=new QcFormTableParam().dao();
	
	@Inject
	private QcFormTableItemService qcFormTableItemService;
	
	@Override
	protected QcFormTableParam dao() {
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
     * @param iType 参数录入方式：1. CPK数值 2. 文本框 3. 选择（√，/，×，△，◎） 4. 单选 5. 复选 6. 下拉列表 7. 日期 8. 时间
     * @param isDeleted 删除状态：0. 未删除 1. 已删除
	 * @return
	 */
	public Page<QcFormTableParam> getAdminDatas(int pageNumber, int pageSize, Integer iType, Boolean isDeleted) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
	    //sql条件处理
        sql.eq("iType",iType);
        sql.eqBooleanToChar("isDeleted",isDeleted);
        //排序
        sql.desc("iAutoId");
		return paginate(sql);
	}

	/**
	 * 保存
	 * @param qcFormTableParam
	 * @return
	 */
	public Ret save(QcFormTableParam qcFormTableParam) {
		if(qcFormTableParam==null || isOk(qcFormTableParam.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(qcFormTableParam.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=qcFormTableParam.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(qcFormTableParam.getIAutoId(), JBoltUserKit.getUserId(), qcFormTableParam.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param qcFormTableParam
	 * @return
	 */
	public Ret update(QcFormTableParam qcFormTableParam) {
		if(qcFormTableParam==null || notOk(qcFormTableParam.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		QcFormTableParam dbQcFormTableParam=findById(qcFormTableParam.getIAutoId());
		if(dbQcFormTableParam==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(qcFormTableParam.getName(), qcFormTableParam.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=qcFormTableParam.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(qcFormTableParam.getIAutoId(), JBoltUserKit.getUserId(), qcFormTableParam.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param qcFormTableParam 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(QcFormTableParam qcFormTableParam, Kv kv) {
		//addDeleteSystemLog(qcFormTableParam.getIAutoId(), JBoltUserKit.getUserId(),qcFormTableParam.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param qcFormTableParam model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(QcFormTableParam qcFormTableParam, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(QcFormTableParam qcFormTableParam, String column, Kv kv) {
		//addUpdateSystemLog(qcFormTableParam.getIAutoId(), JBoltUserKit.getUserId(), qcFormTableParam.getName(),"的字段["+column+"]值:"+qcFormTableParam.get(column));
		/**
		switch(column){
		    case "isDeleted":
		        break;
		}
		*/
		return null;
	}
	
	public QcFormTableParam createQcFormTableParam(Long id, Long qcFormId, Integer seq, Integer type, BigDecimal stdVal, BigDecimal maxVal, BigDecimal minVal, String options, Boolean isDeleted){
		QcFormTableParam qcFormTableParam = new QcFormTableParam();
		if (ObjectUtil.isNull(id)){
			id = JBoltSnowflakeKit.me.nextId();
		}
		qcFormTableParam.setIAutoId(id);
		qcFormTableParam.setIQcFormId(qcFormId);
		qcFormTableParam.setISeq(seq);
		qcFormTableParam.setIType(type);
		qcFormTableParam.setIStdVal(stdVal);
		qcFormTableParam.setIMaxVal(maxVal);
		qcFormTableParam.setIMinVal(minVal);
		qcFormTableParam.setCOptions(options);
		qcFormTableParam.setIsDeleted(isDeleted);
		return qcFormTableParam;
	}
	
	public List<QcFormTableParam> createQcFormTableParamList(Long qcFormId, JSONArray jsonArray){
		if (CollectionUtil.isEmpty(jsonArray)){
			return null;
		}
		List<QcFormTableParam> qcFormTableParamList = new ArrayList<>();
		for (Object obj : jsonArray){
			JSONObject jsonObject = (JSONObject)obj;
			String type = jsonObject.getString(QcFormTableParam.ITYPE.toLowerCase());
			ValidationUtils.notBlank(type, "参数录入方式，不能为空");
			QcFormTableParam qcFormTableParam = null;
			if ("1".equals(type)){
				qcFormTableParam = createQcFormTableParam(
						null,
						qcFormId,
						jsonObject.getInteger(QcFormTableParam.ISEQ.toLowerCase()),
						jsonObject.getInteger(QcFormTableParam.ITYPE.toLowerCase()),
						jsonObject.getBigDecimal(QcFormTableParam.ISTDVAL.toLowerCase()),
						jsonObject.getBigDecimal(QcFormTableParam.IMAXVAL.toLowerCase()),
						jsonObject.getBigDecimal(QcFormTableParam.IMINVAL.toLowerCase()),
						null,
						false
				);
			} else if ("2".equals(type) || "3".equals(type) || "7".equals(type) || "8".equals(type)){
				// 不需要数组及分割
				qcFormTableParam = createQcFormTableParam(
						jsonObject.getLong(QcFormTableParam.IAUTOID.toLowerCase()),
						qcFormId,
						jsonObject.getInteger(QcFormTableParam.ISEQ.toLowerCase()),
						jsonObject.getInteger(QcFormTableParam.ITYPE.toLowerCase()),
						null,
						null,
						null,
						null,
						false
				);
			}else{
				qcFormTableParam = createQcFormTableParam(
						jsonObject.getLong(QcFormTableParam.IAUTOID.toLowerCase()),
						qcFormId,
						jsonObject.getInteger(QcFormTableParam.ISEQ.toLowerCase()),
						jsonObject.getInteger(QcFormTableParam.ITYPE.toLowerCase()),
						null,
						null,
						null,
						jsonObject.getString(QcFormTableParam.COPTIONS.toLowerCase()),
						false
				);
			}
			qcFormTableParamList.add(qcFormTableParam);
			// 设置当前行id
			jsonObject.put(QcFormTableItem.IQCFORMTABLEPARAMID.toLowerCase(), qcFormTableParam.getIAutoId());
		}
		return qcFormTableParamList;
	}
	
	public void removeByQcFormId(Long formId){
		delete("DELETE Bd_QcFormTableParam WHERE iQCFormId = ?", formId);
	}
	
	public List<Map<String, Object>> findByFormId(Long formId){
		List<Record> records = findRecords("SELECT * FROM Bd_QcFormTableParam WHERE iQcFormId = ?  ORDER BY iSeq ASC", formId);
		List<QcFormTableItem> qcFormTableItemList = qcFormTableItemService.findByFormId(formId);
		
		List<Map<String, Object>> mapList = new ArrayList<>();
		
		for (Record record : records){
			Long id = record.getLong(QcFormTableParam.IAUTOID);
			for (QcFormTableItem qcFormTableItem : qcFormTableItemList){
				// 校验当前id是否存在
				if (id.equals(qcFormTableItem.getIQcFormTableParamId())){
					record.set(String.valueOf(qcFormTableItem.getIQcFormItemId()), qcFormTableItem.getIQcFormParamId());
				}
			}
			mapList.add(record.getColumns());
		}
		return mapList;
	}
}
