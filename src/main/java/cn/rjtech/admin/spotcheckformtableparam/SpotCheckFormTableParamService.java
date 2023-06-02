package cn.rjtech.admin.spotcheckformtableparam;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.rjtech.admin.qcformtableitem.QcFormTableItemService;
import cn.rjtech.admin.spotcheckformtableitem.SpotCheckFormTableItemService;
import cn.rjtech.model.momdata.QcFormTableItem;
import cn.rjtech.model.momdata.QcFormTableParam;
import cn.rjtech.model.momdata.SpotCheckFormTableItem;
import cn.rjtech.util.ValidationUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.rjtech.model.momdata.SpotCheckFormTableParam;
import com.jfinal.plugin.activerecord.Record;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 质量建模-点检表格参数录入配置
 * @ClassName: SpotCheckFormTableParamService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-31 10:59
 */
public class SpotCheckFormTableParamService extends BaseService<SpotCheckFormTableParam> {
	private final SpotCheckFormTableParam dao=new SpotCheckFormTableParam().dao();
	@Override
	protected SpotCheckFormTableParam dao() {
		return dao;
	}

	@Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }
	@Inject
	private SpotCheckFormTableItemService SpotCheckFormTableItemService;
	/**
	 * 后台管理数据查询
	 * @param pageNumber 第几页
	 * @param pageSize   每页几条数据
     * @param iType 参数录入方式：1. CPK数值 2. 文本框 3. 选择（√，/，×，△，◎） 4. 单选 5. 复选 6. 下拉列表 7. 日期 8. 时间
     * @param isDeleted 删除状态：0. 未删除 1. 已删除
	 * @return
	 */
	public Page<SpotCheckFormTableParam> getAdminDatas(int pageNumber, int pageSize, Integer iType, Boolean isDeleted) {
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
	 * @param spotCheckFormTableParam
	 * @return
	 */
	public Ret save(SpotCheckFormTableParam spotCheckFormTableParam) {
		if(spotCheckFormTableParam==null || isOk(spotCheckFormTableParam.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		boolean success=spotCheckFormTableParam.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(spotCheckFormTableParam.getIAutoId(), JBoltUserKit.getUserId(), spotCheckFormTableParam.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param spotCheckFormTableParam
	 * @return
	 */
	public Ret update(SpotCheckFormTableParam spotCheckFormTableParam) {
		if(spotCheckFormTableParam==null || notOk(spotCheckFormTableParam.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		SpotCheckFormTableParam dbSpotCheckFormTableParam=findById(spotCheckFormTableParam.getIAutoId());
		if(dbSpotCheckFormTableParam==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		boolean success=spotCheckFormTableParam.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(spotCheckFormTableParam.getIAutoId(), JBoltUserKit.getUserId(), spotCheckFormTableParam.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param spotCheckFormTableParam 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(SpotCheckFormTableParam spotCheckFormTableParam, Kv kv) {
		//addDeleteSystemLog(spotCheckFormTableParam.getIAutoId(), JBoltUserKit.getUserId(),spotCheckFormTableParam.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param spotCheckFormTableParam model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(SpotCheckFormTableParam spotCheckFormTableParam, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(SpotCheckFormTableParam spotCheckFormTableParam, String column, Kv kv) {
		//addUpdateSystemLog(spotCheckFormTableParam.getIAutoId(), JBoltUserKit.getUserId(), spotCheckFormTableParam.getName(),"的字段["+column+"]值:"+spotCheckFormTableParam.get(column));
		/**
		switch(column){
		    case "isDeleted":
		        break;
		}
		*/
		return null;
	}
	public List<Map<String, Object>> findByFormId(Long formId){
		List<Record> records = findRecords("SELECT * FROM Bd_SpotCheckFormTableParam WHERE iSpotCheckFormId = ?  ORDER BY iSeq ASC", formId);
		List<SpotCheckFormTableItem> qcFormTableItemList = SpotCheckFormTableItemService.findByFormId(formId);

		List<Map<String, Object>> mapList = new ArrayList<>();

		for (Record record : records){
			Long id = record.getLong(SpotCheckFormTableParam.IAUTOID);
			for (SpotCheckFormTableItem qcFormTableItem : qcFormTableItemList){
				// 校验当前id是否存在
				if (id.equals(qcFormTableItem.getISpotCheckFormTableParamId())){
					record.set(String.valueOf(qcFormTableItem.getISpotCheckFormItemId()), qcFormTableItem.getISpotCheckFormParamId());
				}
			}
			mapList.add(record.getColumns());
		}
		return mapList;
	}
	public SpotCheckFormTableParam createQcFormTableParam(Long id, Long qcFormId, Integer seq, Integer type, BigDecimal stdVal, BigDecimal maxVal, BigDecimal minVal, String options, Boolean isDeleted){
		SpotCheckFormTableParam qcFormTableParam = new SpotCheckFormTableParam();
		if (ObjectUtil.isNull(id)){
			id = JBoltSnowflakeKit.me.nextId();
		}
		qcFormTableParam.setIAutoId(id);
		qcFormTableParam.setISpotCheckFormId(qcFormId);
		qcFormTableParam.setISeq(seq);
		qcFormTableParam.setIType(type);
		qcFormTableParam.setIStdVal(stdVal);
		qcFormTableParam.setIMaxVal(maxVal);
		qcFormTableParam.setIMinVal(minVal);
		qcFormTableParam.setCOptions(options);
		qcFormTableParam.setIsDeleted(isDeleted);
		return qcFormTableParam;
	}

	public List<SpotCheckFormTableParam> createQcFormTableParamList(Long qcFormId, JSONArray jsonArray){
		if (CollectionUtil.isEmpty(jsonArray)){
			return null;
		}
		List<SpotCheckFormTableParam> qcFormTableParamList = new ArrayList<>();
		for (Object obj : jsonArray){
			JSONObject jsonObject = (JSONObject)obj;
			String type = jsonObject.getString(SpotCheckFormTableParam.ITYPE.toLowerCase());
			ValidationUtils.notBlank(type, "参数录入方式，不能为空");
			SpotCheckFormTableParam qcFormTableParam = null;
			if ("1".equals(type)){
				qcFormTableParam = createQcFormTableParam(
						null,
						qcFormId,
						jsonObject.getInteger(SpotCheckFormTableParam.ISEQ.toLowerCase()),
						jsonObject.getInteger(SpotCheckFormTableParam.ITYPE.toLowerCase()),
						jsonObject.getBigDecimal(SpotCheckFormTableParam.ISTDVAL.toLowerCase()),
						jsonObject.getBigDecimal(SpotCheckFormTableParam.IMAXVAL.toLowerCase()),
						jsonObject.getBigDecimal(SpotCheckFormTableParam.IMINVAL.toLowerCase()),
						null,
						false
				);
			} else if ("2".equals(type) || "3".equals(type) || "7".equals(type) || "8".equals(type)){
				// 不需要数组及分割
				qcFormTableParam = createQcFormTableParam(
						jsonObject.getLong(SpotCheckFormTableParam.IAUTOID.toLowerCase()),
						qcFormId,
						jsonObject.getInteger(SpotCheckFormTableParam.ISEQ.toLowerCase()),
						jsonObject.getInteger(SpotCheckFormTableParam.ITYPE.toLowerCase()),
						null,
						null,
						null,
						null,
						false
				);
			}else{
				qcFormTableParam = createQcFormTableParam(
						jsonObject.getLong(SpotCheckFormTableParam.IAUTOID.toLowerCase()),
						qcFormId,
						jsonObject.getInteger(SpotCheckFormTableParam.ISEQ.toLowerCase()),
						jsonObject.getInteger(SpotCheckFormTableParam.ITYPE.toLowerCase()),
						null,
						null,
						null,
						jsonObject.getString(SpotCheckFormTableParam.COPTIONS.toLowerCase()),
						false
				);
			}
			qcFormTableParamList.add(qcFormTableParam);
			// 设置当前行id
			jsonObject.put(SpotCheckFormTableItem.ISPOTCHECKFORMTABLEPARAMID.toLowerCase(), qcFormTableParam.getIAutoId());
		}
		return qcFormTableParamList;
	}
	public void removeByQcFormId(Long formId){
		delete("DELETE Bd_SpotCheckFormTableParam WHERE iSpotCheckFormId = ?", formId);
	}
}