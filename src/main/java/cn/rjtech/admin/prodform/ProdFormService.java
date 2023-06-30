package cn.rjtech.admin.prodform;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.rjtech.admin.prodformitem.ProdFormItemService;
import cn.rjtech.admin.prodformparam.ProdFormParamService;
import cn.rjtech.admin.prodformtableitem.ProdFormTableItemService;
import cn.rjtech.admin.prodformtableparam.ProdFormTableParamService;
import cn.rjtech.model.momdata.*;
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
import com.jfinal.plugin.activerecord.Record;
//import jdk.nashorn.internal.ir.annotations.Ignore;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 生产表单设置-生产表单
 * @ClassName: ProdFormService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-06-27 09:55
 */
public class ProdFormService extends BaseService<ProdForm> {
	private final ProdForm dao=new ProdForm().dao();
	@Override
	protected ProdForm dao() {
		return dao;
	}

	@Inject
	private ProdFormItemService prodFormItemService;
	@Inject
	private ProdFormParamService prodFormParamService;
	@Inject
	private ProdFormTableParamService prodFormTableParamService;
	@Inject
	private ProdFormTableItemService prodFormTableItemService;

	@Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @return
	 */
	public Page<ProdForm> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
		return paginateByKeywords("dcreatetime","desc", pageNumber, pageSize, keywords, "iAutoId");
	}
	/**
	 * 保存
	 * @param spotCheckForm
	 * @return
	 */
	public Ret save(ProdForm spotCheckForm) {
		if(spotCheckForm==null || isOk(spotCheckForm.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(spotCheckForm.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=spotCheckForm.save();
		if(!success) {
			return fail(JBoltMsg.FAIL);
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param spotCheckForm
	 * @return
	 */
	public Ret update(ProdForm spotCheckForm, Long userId, String userName, Date date) {
		if (spotCheckForm == null || notOk(spotCheckForm.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		ProdForm dbQcForm = findById(spotCheckForm.getIAutoId());
		if (dbQcForm == null) {
			return fail(JBoltMsg.DATA_NOT_EXIST);
		}
		dbQcForm.setCMemo(spotCheckForm.getCMemo());
		dbQcForm.setCProdFormName(spotCheckForm.getCProdFormName());
		dbQcForm.setIsEnabled(spotCheckForm.getIsEnabled());
		dbQcForm.setIUpdateBy(userId);
		dbQcForm.setCUpdateName(userName);
		dbQcForm.setDUpdateTime(date);
		ProdForm obj = queryByQcName(dbQcForm.getCProdFormName(), dbQcForm.getIAutoId());
		ValidationUtils.isTrue(ObjUtil.isEmpty(obj), "表格名称不能重复");
		//if(existsName(qcForm.getName(), qcForm.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST)}
		boolean success = dbQcForm.update();
		if (success) {
			//添加日志
			//addUpdateSystemLog(qcForm.getIAutoId(), JBoltUserKit.getUserId(), qcForm.getName())
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
	 * 切换isdeleted属性
	 */
	public Ret toggleIsDeleted(Long id) {
		return toggleBoolean(id, "isDeleted");
	}

	/**
	 * 切换isenabled属性
	 */
	public Ret toggleIsEnabled(Long id) {
		return toggleBoolean(id, "isEnabled");
	}


	public List<Record> options() {
		return dbTemplate("prodform.list", Kv.of("isenabled", "true")).find();
	}
	/**
	 * 保存
	 */
	public Ret save(ProdForm qcForm, Long orgId, Long userId, String orgCode, String orgName, String userName, Date date) {
		if (qcForm == null) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		qcForm.setIOrgId(orgId);
		qcForm.setCOrgCode(orgCode);
		qcForm.setCOrgName(orgName);

		qcForm.setICreateBy(userId);
		qcForm.setCCreateName(userName);
		qcForm.setDCreateTime(date);

		qcForm.setIUpdateBy(userId);
		qcForm.setCUpdateName(userName);
		qcForm.setDUpdateTime(date);
		qcForm.setIsDeleted(false);

		ProdForm obj = queryByQcName(qcForm.getCProdFormName(), null);
		ValidationUtils.isTrue(ObjUtil.isEmpty(obj), "表格名称不能重复");
		ValidationUtils.notNull(qcForm.getIAutoId(), "未获取到主键id");
		//if(existsName(qcForm.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success = qcForm.save();
		if (success) {
			//添加日志
			//addSaveSystemLog(qcForm.getIAutoId(), JBoltUserKit.getUserId(), qcForm.getName())
		}
		return ret(success);
	}

	public ProdForm queryByQcName(String qcFormName, Long id){
		ValidationUtils.notBlank(qcFormName, "表格名称不能为空");
		String sqlStr = "SELECT * FROM Bd_ProdForm WHERE cprodformname = ?";
		if (ObjUtil.isNotNull(id)){
			sqlStr = sqlStr+" AND iAutoId <> '"+id+"'";
		}
		return findFirst(sqlStr, qcFormName);
	}


	/**
	 * 按主表qcformitem查询列表qcform
	 */
	public List<Record> getItemCombinedListByPId(Kv kv) {
		return dbTemplate("prodformitem.formItemLists", kv).find();
	}

	/**
	 * 按主表qcformparam查询列表
	 */
	public Page<Record> getQcFormParamListByPId(int pageNumber, int pageSize, Kv para) {
		return dbTemplate("prodform.formParamList", para).paginate(pageNumber, pageSize);
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
	 */
	public Ret submitByJBoltTable(JBoltTable jBoltTable) {
		ProdForm qcForm = jBoltTable.getFormModel(ProdForm.class, "prodForm");
		ValidationUtils.notNull(qcForm, JBoltMsg.PARAM_ERROR);

		Long userId = JBoltUserKit.getUserId();
		String name = JBoltUserKit.getUserName();
		Date nowDate = new Date();

		tx(() -> {
			// 修改
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
				qcForm.setIsDeleted(false);
				ValidationUtils.isTrue(qcForm.save(), JBoltMsg.FAIL);
			}

			// 判断table是否为空
			if (jBoltTable.saveIsNotBlank()) {
				List<ProdFormItem> saveQcFormItem = jBoltTable.getSaveModelList(ProdFormItem.class);

				int k = 0;
				for (ProdFormItem saveQcFormItemLine : saveQcFormItem) {
					k++;
					saveQcFormItemLine.setIProdFormId(qcForm.getIAutoId());
					Record BdQcFormItem = findFirstRecord(("SELECT max(iSeq) as iseq FROM Bd_ProdFormItem WHERE iProdFormId = '" + qcForm.getIAutoId() + "' AND isDeleted = 0"));
					Integer iseq = BdQcFormItem.getInt("iseq");
					if (notNull(iseq)) {
						saveQcFormItemLine.setISeq(iseq + k);
					} else {
						saveQcFormItemLine.setISeq(k);
					}
					saveQcFormItemLine.setIsDeleted(false);
				}
				prodFormItemService.batchSave(saveQcFormItem);
			}

			// 更新
			if (jBoltTable.updateIsNotBlank()) {
				List<ProdFormItem> updateQcFormItem = jBoltTable.getUpdateModelList(ProdFormItem.class);
				updateQcFormItem.forEach(updateQcFormItemLine -> {
				});
				prodFormItemService.batchUpdate(updateQcFormItem);
			}

//			// 获取待删除数据 执行删除
//			if (jBoltTable.deleteIsNotBlank()) {
//				qcFormItemService.deleteByIds(jBoltTable.getDelete())
//			}
			return true;
		});
		return successWithData(qcForm.keep("iautoid"));
	}

	/**
	 * qcformparam可编辑表格提交
	 *
	 * @param jBoltTable 编辑表格提交内容
	 */
	public Ret QcFormParamJBoltTable(JBoltTable jBoltTable) {
		ProdForm qcForm = jBoltTable.getFormModel(ProdForm.class, "prodForm");
		ValidationUtils.notNull(qcForm, JBoltMsg.PARAM_ERROR);

		Long userId = JBoltUserKit.getUserId();
		String name = JBoltUserKit.getUserName();
		Date nowDate = new Date();

		tx(() -> {
			// 修改
			if (isOk(qcForm.getIAutoId())) {
				qcForm.setCUpdateName(name);
				qcForm.setIUpdateBy(userId);
				qcForm.setDUpdateTime(nowDate);
				ValidationUtils.isTrue(qcForm.update(), JBoltMsg.FAIL);
			} else {
				// 新增
				qcForm.setCCreateName(name);
				qcForm.setICreateBy(userId);
				qcForm.setDCreateTime(nowDate);
				qcForm.setCUpdateName(name);
				qcForm.setIUpdateBy(userId);
				qcForm.setDUpdateTime(nowDate);
				qcForm.setIsDeleted(false);
				ValidationUtils.isTrue(qcForm.save(), JBoltMsg.FAIL);
			}

			List<ProdFormParam> saveQcFormParam = jBoltTable.getSaveModelList(ProdFormParam.class);
			int k = 0;
			for (ProdFormParam saveQcFormParamLine : saveQcFormParam) {
				Long iqcformitemid = saveQcFormParamLine.get("iqcformitemid");
				k++;
				Record BdQcFormParam = findFirstRecord(("SELECT iItemParamSeq FROM Bd_ProdFormParam WHERE iProdFormItemId = '" + iqcformitemid + "' AND isDeleted = 0"));
				if (notNull(BdQcFormParam)) {
					int isep = BdQcFormParam.size();
					saveQcFormParamLine.setIItemParamSeq(isep + k);
				} else {
					saveQcFormParamLine.setIItemParamSeq(k);
				}
				saveQcFormParamLine.setIsDeleted(false);
			}
			prodFormParamService.batchSave(saveQcFormParam);
			//更新
			if (jBoltTable.updateIsNotBlank()) {
				List<ProdFormParam> updateQcFormParam = jBoltTable.getUpdateModelList(ProdFormParam.class);
				updateQcFormParam.forEach(updateQcFormItemLine -> {

				});
				prodFormParamService.batchUpdate(updateQcFormParam);
			}
			// 获取待删除数据 执行删除
//			if (jBoltTable.deleteIsNotBlank()) {
//				qcFormParamService.deleteByIds(jBoltTable.getDelete())
//			}
			return true;
		});
		return SUCCESS;
	}

	/**
	 * 检验报告记录详情表头一
	 */
	public List<Record> qcformtableparamOneTitle(Long iAutoId) {
		return dbTemplate("prodform.qcformtableparamOneTitle", Kv.by("iQcFormId", iAutoId)).find();
	}

	/**
	 * 检验报告记录详情表头二
	 */
	public List<Record> qcformtableparamTwoTitle(Long iAutoId) {
		return dbTemplate("prodform.qcformtableparamTwoTitle", Kv.by("iQcFormId", iAutoId)).find();
	}

	/**
	 * 拉取客户资源
	 */
	public List<Record> customerList(Kv kv) {
		kv.set("orgCode", getOrgCode());
		return dbTemplate("qcformtableparam.customerList", kv).find();
	}



	public Ret submitForm(String formJsonDataStr, String qcItemTableJsonDataStr, String qcParamTableJsonDataStr, String tableJsonDataStr){
		ValidationUtils.notBlank(formJsonDataStr, JBoltMsg.JBOLTTABLE_IS_BLANK);
		ValidationUtils.notBlank(qcItemTableJsonDataStr, JBoltMsg.JBOLTTABLE_IS_BLANK);
		ValidationUtils.notBlank(qcParamTableJsonDataStr, JBoltMsg.JBOLTTABLE_IS_BLANK);
		ValidationUtils.notBlank(tableJsonDataStr, JBoltMsg.JBOLTTABLE_IS_BLANK);

		JSONArray qcParamJsonData = JSONObject.parseArray(qcParamTableJsonDataStr);
		ValidationUtils.notEmpty(qcParamJsonData, JBoltMsg.JBOLTTABLE_IS_BLANK);

		JSONObject formJsonData = JSONObject.parseObject(formJsonDataStr);
		ProdForm qcFom = createQcFom(formJsonData.getLong(ProdForm.IAUTOID), formJsonData.getString(ProdForm.CPRODFORMNAME), formJsonData.getString(ProdForm.CMEMO), Boolean.valueOf(formJsonData.getString(ProdForm.ISENABLED)));
		// 用于记录新增是的主键id
		ProdForm qcFormNew = new ProdForm();
		Long orgId = getOrgId();
		String orgCode = getOrgCode();
		String orgName = getOrgName();

		Long userId = JBoltUserKit.getUserId();
		String userName = JBoltUserKit.getUserName();
		DateTime date = DateUtil.date();
		Long formId = qcFom.getIAutoId();
		// 判断是否新增

		if (ObjUtil.isNull(formId)){
			formId = JBoltSnowflakeKit.me.nextId();
			qcFormNew.setIAutoId(formId);
		}

		List<ProdFormItem> qcFormItemList = prodFormItemService.createQcFormItemList(formId, false, JSONObject.parseArray(qcItemTableJsonDataStr));
		Map<Long, ProdFormItem> qcFormItemMap = qcFormItemList.stream().collect(Collectors.toMap(ProdFormItem::getIProdItemId, Function.identity(), (key1, key2) -> key2));
		for (int i=0; i<qcParamJsonData.size(); i++){
			JSONObject jsonObject = qcParamJsonData.getJSONObject(i);
			Long qcItemId = null;
			//新增是QcFormParam.IQCITEMID，编辑是ProdFormParam.IPRODITEMID
			if (notNull(jsonObject.get(ProdFormParam.IAUTOID.toLowerCase()))){
				 qcItemId = jsonObject.getLong(ProdFormParam.IPRODITEMID.toLowerCase());
			}else {
				 qcItemId = jsonObject.getLong(QcFormParam.IQCITEMID.toLowerCase());
			}

			if (qcFormItemMap.containsKey(qcItemId)){
				ProdFormItem qcFormItem = qcFormItemMap.get(qcItemId);
				jsonObject.put(ProdFormParam.IPRODFORMITEMID.toLowerCase(), qcFormItem.getIAutoId());
				//QcFormParam.IQCFORMITEMID
			}
		}

		List<ProdFormParam> qcFormParamList = prodFormParamService.createQcFormParamList(formId, qcParamJsonData);
		JSONArray tableJsonData = JSONObject.parseArray(tableJsonDataStr);
		List<ProdFormTableParam> qcFormTableParamList = prodFormTableParamService.createQcFormTableParamList(formId, tableJsonData);
		List<ProdFormTableItem> qcFormTableItemList = prodFormTableItemService.createQcFormTableItemList(formId, qcFormItemList, tableJsonData);


		tx(() -> {
			// 新增
			if (ObjUtil.isNull(qcFom.getIAutoId())){
				qcFom.setIAutoId(qcFormNew.getIAutoId());
				save(qcFom, orgId, userId, orgCode, orgName, userName, date);
			}else {
				// 先删除后添加
				removeById(qcFom.getIAutoId());

				update(qcFom, userId, userName, date);
			}

			// 保存项目
			prodFormItemService.batchSave(qcFormItemList);
			// 保存参数
			prodFormParamService.batchSave(qcFormParamList);
			// 保存记录行数据
			prodFormTableParamService.batchSave(qcFormTableParamList);
			// 保存记录行数据中间表
			prodFormTableItemService.batchSave(qcFormTableItemList);
			return true;
		});

		// 修改 removeById

//
		return SUCCESS;
	}


	public ProdForm createQcFom(Long id, String qcFormName, String memo, Boolean isEnabled){
		ProdForm qcForm = new ProdForm();
		qcForm.setIAutoId(id);
		qcForm.setCProdFormName(qcFormName);
		qcForm.setCMemo(memo);
		qcForm.setIsEnabled(isEnabled);
		return qcForm;
	}

	/**
	 * 删除关联表数据，重新添加
	 * @param id
	 */
	public void removeById(Long id){
		prodFormItemService.removeByQcFormId(id);
		prodFormParamService.removeByQcFormId(id);
		prodFormTableParamService.removeByQcFormId(id);
		prodFormTableItemService.removeByQcFormId(id);
	}

	public List<Map<String, Object>> getTableHeadData(Long formId, String itemJsonStr, String itemParamJsonStr) {
		List<Map<String, Object>> mapList = new ArrayList<>();
		if (StrUtil.isNotBlank(itemJsonStr)){
			JSONArray jsonArray = JSONObject.parseArray(itemJsonStr);
			// 标题选择值
			if (StrUtil.isNotBlank(itemParamJsonStr)){
				JSONArray itemParamJsonArray = JSONObject.parseArray(itemParamJsonStr);

//                Map<String, List<JSONObject>> itemParamMap = itemParamJsonArray.stream().filter(item -> StrUtil.isNotBlank(((JSONObject)item).getString("iqcitemid"))).collect(Collectors.groupingBy(obj -> ((JSONObject) obj).getString("iqcitemid")));
				for (int i=0; i<jsonArray.size(); i++){
					JSONObject item = jsonArray.getJSONObject(i);
					String qcItemId = item.getString("iqcitemid");

					boolean flag = false;
					List<Map<String, Object>> itemParamList = new ArrayList<>();
					for (Object object :itemParamJsonArray){
						JSONObject itemParamJson = (JSONObject)object;
						if (qcItemId.equals(itemParamJson.getString("iqcitemid"))){
							flag = true;
							itemParamList.add(itemParamJson.getInnerMap());
						}
					}
//                    if (itemParamMap.containsKey(qcItemId)){
//                        List<Map<String, Object>> objects =(List<Map<String, Object>>) itemParamMap.get(qcItemId);
//                        item.put("compares", objects);
//                    }
					if (flag){
						item.put("compares", itemParamList);
					}
					mapList.add(item.getInnerMap());
				}
			}
//            jsonArray.sort(Comparator.comparing(obj -> ((JSONObject)obj).getInteger("iseq")));
		}else if (ObjUtil.isNotNull(formId)){
			List<Record> qcFormItemList = getItemCombinedListByPId(Kv.by("iqcformid", formId));
			List<Record> qcFormParamList = prodFormParamService.getQcFormParamListByPId(formId);
			Map<Long, List<Record>> itemParamByItemMap = qcFormParamList.stream().collect(Collectors.groupingBy(obj -> obj.getLong("iproditemid")));//iproditemid 生产ID？？
			for (Record qcFormItemRecord : qcFormItemList){
				Long qcItemId = qcFormItemRecord.getLong("iqcitemid");
				if (itemParamByItemMap.containsKey(qcItemId)){
					List<Record> list = itemParamByItemMap.get(qcItemId);
					List<Map<String, Object>> maps = new ArrayList<>();

					for (Record itemRecord : list){
						maps.add(itemRecord.getColumns());
					}
					qcFormItemRecord.set("compares", maps);
				}
				mapList.add(qcFormItemRecord.getColumns());
			}
		}

		if (CollectionUtil.isNotEmpty(mapList)){

			Collections.sort(mapList, new Comparator<Map<String, Object>>() {
				@Override
				public int compare(Map<String, Object> o1, Map<String, Object> o2) {
					Integer map1 = Integer.valueOf(o1.get("iseq").toString());
					Integer map2 = Integer.valueOf(o2.get("iseq").toString());
					return map1.compareTo(map2);
				}
			});
			return mapList;
		}
		return null;
	}
	/**
	 * 根据表格id获取明细数据
	 */
	public List<Record> findByIdGetDetail(String iprodformid){
		return  dbTemplate("prodform.findByIdGetDetail",Kv.by("iprodformid",iprodformid)).find();
	}
}