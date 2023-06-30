package cn.rjtech.admin.prodformtableparam;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.prodformtableitem.ProdFormTableItemService;
import cn.rjtech.model.momdata.ProdFormTableItem;
import cn.rjtech.model.momdata.ProdFormTableParam;
import cn.rjtech.model.momdata.SpotCheckFormTableParam;
import cn.rjtech.util.ValidationUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 生产表单设置-生产表格参数录入配置
 * @ClassName: ProdFormTableParamService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-06-27 10:14
 */
public class ProdFormTableParamService extends BaseService<ProdFormTableParam> {
	private final ProdFormTableParam dao=new ProdFormTableParam().dao();
	@Override
	protected ProdFormTableParam dao() {
		return dao;
	}

	@Inject
	private ProdFormTableItemService prodFormTableItemService;
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
	public Page<ProdFormTableParam> getAdminDatas(int pageNumber, int pageSize, Integer iType, Boolean isDeleted) {
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
	 * @param prodFormTableParam
	 * @return
	 */
	public Ret save(ProdFormTableParam prodFormTableParam) {
		if(prodFormTableParam==null || isOk(prodFormTableParam.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		boolean success=prodFormTableParam.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(spotCheckFormTableParam.getIAutoId(), JBoltUserKit.getUserId(), spotCheckFormTableParam.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param prodFormTableParam
	 * @return
	 */
	public Ret update(ProdFormTableParam prodFormTableParam) {
		if(prodFormTableParam==null || notOk(prodFormTableParam.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		ProdFormTableParam dbSpotCheckFormTableParam=findById(prodFormTableParam.getIAutoId());
		if(dbSpotCheckFormTableParam==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		boolean success=prodFormTableParam.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(spotCheckFormTableParam.getIAutoId(), JBoltUserKit.getUserId(), spotCheckFormTableParam.getName());
		}
		return ret(success);
	}


	public List<Map<String, Object>> findByFormId(Long formId){
		List<Record> records = findRecords("SELECT * FROM Bd_ProdFormTableParam WHERE iQcFormId = ?  ORDER BY iSeq ASC", formId);
		List<ProdFormTableItem> qcFormTableItemList = prodFormTableItemService.findByFormId(formId);

		List<Map<String, Object>> mapList = new ArrayList<>();

		for (Record record : records){
			Long id = record.getLong(SpotCheckFormTableParam.IAUTOID);
			for (ProdFormTableItem qcFormTableItem : qcFormTableItemList){
				// 校验当前id是否存在
				if (id.equals(qcFormTableItem.getIProdFormTableParamId())){
					record.set(String.valueOf(qcFormTableItem.getIProdFormItemId()), qcFormTableItem.getIProdFormParamId());
				}
			}
			mapList.add(record.getColumns());
		}
		return mapList;
	}
	public ProdFormTableParam createQcFormTableParam(Long id, Long qcFormId, Integer seq, Integer type, BigDecimal stdVal, BigDecimal maxVal, BigDecimal minVal, String options, Boolean isDeleted){
		ProdFormTableParam qcFormTableParam = new ProdFormTableParam();
		if (ObjUtil.isNull(id)){
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

	public List<ProdFormTableParam> createQcFormTableParamList(Long qcFormId, JSONArray jsonArray){
		if (CollUtil.isEmpty(jsonArray)){
			return null;
		}
		List<ProdFormTableParam> qcFormTableParamList = new ArrayList<>();
		for (Object obj : jsonArray){
			JSONObject jsonObject = (JSONObject)obj;
			String type = jsonObject.getString(SpotCheckFormTableParam.ITYPE.toLowerCase());
			ValidationUtils.notBlank(type, "参数录入方式，不能为空");
			ProdFormTableParam qcFormTableParam = null;
			if ("1".equals(type)){
				qcFormTableParam = createQcFormTableParam(
						null,
						qcFormId,
						jsonObject.getInteger(ProdFormTableParam.ISEQ.toLowerCase()),
						jsonObject.getInteger(ProdFormTableParam.ITYPE.toLowerCase()),
						jsonObject.getBigDecimal(ProdFormTableParam.ISTDVAL.toLowerCase()),
						jsonObject.getBigDecimal(ProdFormTableParam.IMAXVAL.toLowerCase()),
						jsonObject.getBigDecimal(ProdFormTableParam.IMINVAL.toLowerCase()),
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
			jsonObject.put(ProdFormTableItem.IPRODFORMTABLEPARAMID.toLowerCase(), qcFormTableParam.getIAutoId());
		}
		return qcFormTableParamList;
	}
	public void removeByQcFormId(Long formId){
		delete("DELETE Bd_ProdFormTableParam WHERE iQcFormId = ?", formId);
	}

}