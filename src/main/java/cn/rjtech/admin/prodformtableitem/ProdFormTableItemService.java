package cn.rjtech.admin.prodformtableitem;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.ProdFormItem;
import cn.rjtech.model.momdata.ProdFormTableItem;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 生产表单设置-生产表单表格行记录
 * @ClassName: ProdFormTableItemService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-06-27 10:13
 */
public class ProdFormTableItemService extends BaseService<ProdFormTableItem> {
	private final ProdFormTableItem dao=new ProdFormTableItem().dao();
	@Override
	protected ProdFormTableItem dao() {
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
	 * @return
	 */
	public Page<ProdFormTableItem> getAdminDatas(int pageNumber, int pageSize) {
		//创建sql对象
		Sql sql = selectSql().page(pageNumber,pageSize);
		//排序
		sql.desc("iAutoId");
		return paginate(sql);
	}

	/**
	 * 保存
	 * @param prodFormTableItem
	 * @return
	 */
	public Ret save(ProdFormTableItem prodFormTableItem) {
		if(prodFormTableItem==null || isOk(prodFormTableItem.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		boolean success=prodFormTableItem.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(spotCheckFormTableItem.getIAutoId(), JBoltUserKit.getUserId(), spotCheckFormTableItem.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param spotCheckFormTableItem
	 * @return
	 */
	public Ret update(ProdFormTableItem spotCheckFormTableItem) {
		if(spotCheckFormTableItem==null || notOk(spotCheckFormTableItem.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		ProdFormTableItem dbSpotCheckFormTableItem=findById(spotCheckFormTableItem.getIAutoId());
		if(dbSpotCheckFormTableItem==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		boolean success=spotCheckFormTableItem.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(spotCheckFormTableItem.getIAutoId(), JBoltUserKit.getUserId(), spotCheckFormTableItem.getName());
		}
		return ret(success);
	}



	public List<ProdFormTableItem> findByFormId(Long formId){
		return find("SELECT * FROM Bd_ProdFormTableItem WHERE iProdFormId = ?", formId);
	}
	public ProdFormTableItem createQcFormTableItem(Long id, Long qcFormId, Long qcFormItemId, Long qcFormParamId, Long qcFormTableParamId){
		ProdFormTableItem qcFormTableItem = new ProdFormTableItem();
		if (ObjUtil.isNull(id)){
			id = JBoltSnowflakeKit.me.nextId();
		}
		qcFormTableItem.setIAutoId(id);
		qcFormTableItem.setIProdFormId(qcFormId);
		qcFormTableItem.setIProdFormItemId(qcFormItemId);
		qcFormTableItem.setIProdFormParamId(qcFormParamId);
		qcFormTableItem.setIProdFormTableParamId(qcFormTableParamId);
		return qcFormTableItem;
	}

	public List<ProdFormTableItem> createQcFormTableItemList(Long qcFormId, List<ProdFormItem> qcFormItemList, JSONArray jsonArray){
		if (CollUtil.isEmpty(jsonArray)){
			return null;
		}
		Map<Long, ProdFormItem> qcItemMap = qcFormItemList.stream().collect(Collectors.toMap(ProdFormItem::getIProdItemId, Function.identity(), (key1, key2) -> key2));
		List<ProdFormTableItem> qcFormTableItemList = new ArrayList<>();
		//
		for (Object obj : jsonArray){
			JSONObject jsonObject = (JSONObject)obj;

			for (Long itemId : qcItemMap.keySet()){
				if (jsonObject.containsKey(itemId)){
					ProdFormTableItem qcFormTableItem = createQcFormTableItem(
							null,
							qcFormId,
							itemId,
							jsonObject.getLong(String.valueOf(itemId)),
							jsonObject.getLong(ProdFormTableItem.IPRODFORMTABLEPARAMID.toLowerCase()));
					qcFormTableItemList.add(qcFormTableItem);
					continue;
				}

				ProdFormTableItem qcFormTableItem = createQcFormTableItem(
						null,
						qcFormId,
						itemId,
						null,
						jsonObject.getLong(ProdFormTableItem.IPRODFORMTABLEPARAMID.toLowerCase()));
				qcFormTableItemList.add(qcFormTableItem);
			}
//			for (String key :jsonObject.keySet()){
//				// 判断key是否为id
//				if (NumberUtil.isNumber(key)){
//
//				}
//			}
		}
		return qcFormTableItemList;
	}
	public void removeByQcFormId(Long formId){
		delete("DELETE Bd_ProdFormTableItem WHERE iProdFormId = ?", formId);
	}

}