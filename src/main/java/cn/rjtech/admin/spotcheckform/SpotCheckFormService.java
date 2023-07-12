package cn.rjtech.admin.spotcheckform;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.spotcheckformitem.SpotCheckFormItemService;
import cn.rjtech.admin.spotcheckformparam.SpotCheckFormParamService;
import cn.rjtech.admin.spotcheckformtableitem.SpotCheckFormTableItemService;
import cn.rjtech.admin.spotcheckformtableparam.SpotCheckFormTableParamService;
import cn.rjtech.model.momdata.*;
import cn.rjtech.util.ValidationUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static cn.hutool.core.text.StrPool.COMMA;

/**
 * 点检表格 Service
 * @ClassName: SpotCheckFormService
 * @author: RJ
 * @date: 2023-04-03 10:42
 */
public class SpotCheckFormService extends BaseService<SpotCheckForm> {

	private final SpotCheckForm dao = new SpotCheckForm().dao();

	@Override
	protected SpotCheckForm dao() {
		return dao;
	}

	@Inject
	private SpotCheckFormItemService spotCheckFormItemService;

	@Inject
	private SpotCheckFormParamService spotCheckFormParamService;

	@Inject
	private SpotCheckFormTableParamService spotCheckFormTableParamService;
	@Inject
	private SpotCheckFormTableItemService spotCheckFormTableItemService;
	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @return
	 */
	public Page<SpotCheckForm> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
		return paginateByKeywords("dcreatetime","desc", pageNumber, pageSize, keywords, "iAutoId",Okv.by("isDeleted",0));
	}

	/**
	 * 保存
	 * @param spotCheckForm
	 * @return
	 */
	public Ret save(SpotCheckForm spotCheckForm) {
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
	public Ret update(SpotCheckForm spotCheckForm, Long userId, String userName, Date date) {
		if (spotCheckForm == null || notOk(spotCheckForm.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		SpotCheckForm dbQcForm = findById(spotCheckForm.getIAutoId());
		if (dbQcForm == null) {
			return fail(JBoltMsg.DATA_NOT_EXIST);
		}
		dbQcForm.setCMemo(spotCheckForm.getCMemo());
		dbQcForm.setCSpotCheckFormName(spotCheckForm.getCSpotCheckFormName());
		dbQcForm.setIsEnabled(spotCheckForm.getIsEnabled());
		dbQcForm.setIUpdateBy(userId);
		dbQcForm.setCUpdateName(userName);
		dbQcForm.setDUpdateTime(date);
		SpotCheckForm obj = queryByQcName(dbQcForm.getCSpotCheckFormName(), dbQcForm.getIAutoId());
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
		String[] idarry = ids.split(",");
		int num = update("UPDATE Bd_SpotCheckForm SET isDeleted = 1 WHERE iAutoId IN (" + ArrayUtil.join(idarry, COMMA) + ") ");
		if (num > 0){
			return SUCCESS;
		}
		return FAIL;
	}

	/**
	 * 删除
	 * @param id
	 * @return
	 */
	public Ret delete(Long id) {
		int num = update("UPDATE Bd_SpotCheckForm SET isDeleted = 1 WHERE iAutoId = ? ",id);
		if (num > 0){
			return SUCCESS;
		}
		return FAIL;
	}

	/**
	 * 删除数据后执行的回调
	 * @param spotCheckForm 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(SpotCheckForm spotCheckForm, Kv kv) {
		//addDeleteSystemLog(spotCheckForm.getIautoid(), JBoltUserKit.getUserId(),spotCheckForm.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param spotCheckForm 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(SpotCheckForm spotCheckForm, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(spotCheckForm, kv);
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
		return toggleBoolean(id, "isDeleted");
	}

	/**
	 * 切换isenabled属性
	 */
	public Ret toggleIsEnabled(Long id) {
		return toggleBoolean(id, "isEnabled");
	}

	/**
	 * 检测是否可以toggle操作指定列
	 * @param spotCheckForm 要toggle的model
	 * @param column 操作的哪一列
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanToggle(SpotCheckForm spotCheckForm,String column, Kv kv) {
		//没有问题就返回null  有问题就返回提示string 字符串
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(SpotCheckForm spotCheckForm, String column, Kv kv) {
		//addUpdateSystemLog(spotCheckForm.getIautoid(), JBoltUserKit.getUserId(), spotCheckForm.getName(),"的字段["+column+"]值:"+spotCheckForm.get(column));
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param spotCheckForm model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(SpotCheckForm spotCheckForm, Kv kv) {
		//这里用来覆盖 检测SpotCheckForm是否被其它表引用
		return null;
	}

	public List<Record> options() {
		return dbTemplate("spotcheckform.list", Kv.of("isenabled", "true")).find();
	}
	/**
	 * 保存
	 */
	public Ret save(SpotCheckForm qcForm, Long orgId, Long userId, String orgCode, String orgName, String userName, Date date) {
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

		SpotCheckForm obj = queryByQcName(qcForm.getCSpotCheckFormName(), null);
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

	public SpotCheckForm queryByQcName(String qcFormName, Long id){
		ValidationUtils.notBlank(qcFormName, "表格名称不能为空");
		String sqlStr = "SELECT * FROM Bd_SpotCheckForm WHERE CSpotCheckFormName = ?";
		if (ObjUtil.isNotNull(id)){
			sqlStr = sqlStr+" AND iAutoId <> '"+id+"'";
		}
		return findFirst(sqlStr, qcFormName);
	}


	/**
	 * 按主表qcformitem查询列表qcform
	 */
	public List<Record> getItemCombinedListByPId(Kv kv) {
		return dbTemplate("spotcheckformitem.formItemLists", kv).find();
	}

	/**
	 * 按主表qcformparam查询列表
	 */
	public Page<Record> getQcFormParamListByPId(int pageNumber, int pageSize, Kv para) {
		return dbTemplate("spotcheckformparam.formParamList", para).paginate(pageNumber, pageSize);
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
		SpotCheckForm qcForm = jBoltTable.getFormModel(SpotCheckForm.class, "qcForm");
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
				List<SpotCheckFormItem> saveQcFormItem = jBoltTable.getSaveModelList(SpotCheckFormItem.class);

				int k = 0;
				for (SpotCheckFormItem saveQcFormItemLine : saveQcFormItem) {
					k++;
					saveQcFormItemLine.setISpotCheckFormId(qcForm.getIAutoId());
					Record BdQcFormItem = findFirstRecord(("SELECT max(iSeq) as iseq FROM Bd_SpotCheckFormItem WHERE iQcFormId = '" + qcForm.getIAutoId() + "' AND isDeleted = 0"));
					Integer iseq = BdQcFormItem.getInt("iseq");
					if (notNull(iseq)) {
						saveQcFormItemLine.setISeq(iseq + k);
					} else {
						saveQcFormItemLine.setISeq(k);
					}
					saveQcFormItemLine.setIsDeleted(false);
				}
				spotCheckFormItemService.batchSave(saveQcFormItem);
			}

			// 更新
			if (jBoltTable.updateIsNotBlank()) {
				List<SpotCheckFormItem> updateQcFormItem = jBoltTable.getUpdateModelList(SpotCheckFormItem.class);
				updateQcFormItem.forEach(updateQcFormItemLine -> {
				});
				spotCheckFormItemService.batchUpdate(updateQcFormItem);
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
		SpotCheckForm qcForm = jBoltTable.getFormModel(SpotCheckForm.class, "qcForm");
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

			List<SpotCheckFormParam> saveQcFormParam = jBoltTable.getSaveModelList(SpotCheckFormParam.class);
			int k = 0;
			for (SpotCheckFormParam saveQcFormParamLine : saveQcFormParam) {
				Long iqcformitemid = saveQcFormParamLine.get("iqcformitemid");
				k++;
				Record BdQcFormParam = findFirstRecord(("SELECT iItemParamSeq FROM Bd_SpotCheckFormParam WHERE iQcFormItemId = '" + iqcformitemid + "' AND isDeleted = 0"));
				if (notNull(BdQcFormParam)) {
					int isep = BdQcFormParam.size();
					saveQcFormParamLine.setIItemParamSeq(isep + k);
				} else {
					saveQcFormParamLine.setIItemParamSeq(k);
				}
				saveQcFormParamLine.setIsDeleted(false);
			}
			spotCheckFormParamService.batchSave(saveQcFormParam);
			//更新
			if (jBoltTable.updateIsNotBlank()) {
				List<SpotCheckFormParam> updateQcFormParam = jBoltTable.getUpdateModelList(SpotCheckFormParam.class);
				updateQcFormParam.forEach(updateQcFormItemLine -> {

				});
				spotCheckFormParamService.batchUpdate(updateQcFormParam);
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
		return dbTemplate("qcform.qcformtableparamOneTitle", Kv.by("iQcFormId", iAutoId)).find();
	}

	/**
	 * 检验报告记录详情表头二
	 */
	public List<Record> qcformtableparamTwoTitle(Long iAutoId) {
		return dbTemplate("qcform.qcformtableparamTwoTitle", Kv.by("iQcFormId", iAutoId)).find();
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
		SpotCheckForm qcFom = createQcFom(formJsonData.getLong(QcForm.IAUTOID), formJsonData.getString(QcForm.CQCFORMNAME), formJsonData.getString(QcForm.CMEMO), Boolean.valueOf(formJsonData.getString(QcForm.ISENABLED)));
		// 用于记录新增是的主键id
		SpotCheckForm qcFormNew = new SpotCheckForm();
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

		List<SpotCheckFormItem> qcFormItemList = spotCheckFormItemService.createQcFormItemList(formId, false, JSONObject.parseArray(qcItemTableJsonDataStr));
		Map<Long, SpotCheckFormItem> qcFormItemMap = qcFormItemList.stream().collect(Collectors.toMap(SpotCheckFormItem::getIQcItemId, Function.identity(), (key1, key2) -> key2));
		for (int i=0; i<qcParamJsonData.size(); i++){
			JSONObject jsonObject = qcParamJsonData.getJSONObject(i);
			Long qcItemId = jsonObject.getLong(SpotCheckFormParam.IQCITEMID.toLowerCase());
			if (qcFormItemMap.containsKey(qcItemId)){
				SpotCheckFormItem qcFormItem = qcFormItemMap.get(qcItemId);
				jsonObject.put(SpotCheckFormParam.ISPOTCHECKFORMITEMID.toLowerCase(), qcFormItem.getIAutoId());
			}
		}

		List<SpotCheckFormParam> qcFormParamList = spotCheckFormParamService.createQcFormParamList(formId, qcParamJsonData);
		JSONArray tableJsonData = JSONObject.parseArray(tableJsonDataStr);
		List<SpotCheckFormTableParam> qcFormTableParamList = spotCheckFormTableParamService.createQcFormTableParamList(formId, tableJsonData);
		List<SpotCheckFormTableItem> qcFormTableItemList = spotCheckFormTableItemService.createQcFormTableItemList(formId, qcFormItemList, tableJsonData);


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
			spotCheckFormItemService.batchSave(qcFormItemList);
			// 保存参数
			spotCheckFormParamService.batchSave(qcFormParamList);
			// 保存记录行数据
			spotCheckFormTableParamService.batchSave(qcFormTableParamList);
			// 保存记录行数据中间表
			spotCheckFormTableItemService.batchSave(qcFormTableItemList);
			return true;
		});

		// 修改 removeById

//
		return SUCCESS;
	}


	public SpotCheckForm createQcFom(Long id, String qcFormName, String memo, Boolean isEnabled){
		SpotCheckForm qcForm = new SpotCheckForm();
		qcForm.setIAutoId(id);
		qcForm.setCSpotCheckFormName(qcFormName);
		qcForm.setCMemo(memo);
		qcForm.setIsEnabled(isEnabled);
		return qcForm;
	}

	/**
	 * 删除关联表数据，重新添加
	 * @param id
	 */
	public void removeById(Long id){
		spotCheckFormItemService.removeByQcFormId(id);
		spotCheckFormParamService.removeByQcFormId(id);
		spotCheckFormTableParamService.removeByQcFormId(id);
		spotCheckFormTableItemService.removeByQcFormId(id);
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
			List<Record> qcFormParamList = spotCheckFormParamService.getQcFormParamListByPId(formId);
			Map<Long, List<Record>> itemParamByItemMap = qcFormParamList.stream().collect(Collectors.groupingBy(obj -> obj.getLong("iqcitemid")));
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

		if (CollUtil.isNotEmpty(mapList)){

			mapList.sort((o1, o2) -> {
                Integer map1 = Integer.valueOf(o1.get("iseq").toString());
                Integer map2 = Integer.valueOf(o2.get("iseq").toString());
                return map1.compareTo(map2);
            });
			return mapList;
		}
		return null;
	}
	/**
	 * 根据表格id获取明细数据
	 */
	public List<Record> findByIdGetDetail(String iprodformid){
		return  dbTemplate("spotcheckform.findByIdGetDetail",Kv.by("iprodformid",iprodformid)).find();
	}

	/**
	 * 根据检验表格名称获取点检表格数据
	 * @param name
	 * @return
	 */
	public SpotCheckForm getSpotCheckFormByname(String name){
		return findFirst("SELECT * FROM Bd_SpotCheckForm WHERE isDeleted = '0' AND cSpotCheckFormName=?",name);
	}
}