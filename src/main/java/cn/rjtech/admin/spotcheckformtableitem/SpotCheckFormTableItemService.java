package cn.rjtech.admin.spotcheckformtableitem;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.SpotCheckFormItem;
import cn.rjtech.model.momdata.SpotCheckFormTableItem;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 质量建模-点检表格行记录
 * @ClassName: SpotCheckFormTableItemService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-31 11:08
 */
public class SpotCheckFormTableItemService extends BaseService<SpotCheckFormTableItem> {
	private final SpotCheckFormTableItem dao=new SpotCheckFormTableItem().dao();
	@Override
	protected SpotCheckFormTableItem dao() {
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
	public Page<SpotCheckFormTableItem> getAdminDatas(int pageNumber, int pageSize) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
        //排序
        sql.desc("iAutoId");
		return paginate(sql);
	}

	/**
	 * 保存
	 * @param spotCheckFormTableItem
	 * @return
	 */
	public Ret save(SpotCheckFormTableItem spotCheckFormTableItem) {
		if(spotCheckFormTableItem==null || isOk(spotCheckFormTableItem.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		boolean success=spotCheckFormTableItem.save();
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
	public Ret update(SpotCheckFormTableItem spotCheckFormTableItem) {
		if(spotCheckFormTableItem==null || notOk(spotCheckFormTableItem.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		SpotCheckFormTableItem dbSpotCheckFormTableItem=findById(spotCheckFormTableItem.getIAutoId());
		if(dbSpotCheckFormTableItem==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		boolean success=spotCheckFormTableItem.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(spotCheckFormTableItem.getIAutoId(), JBoltUserKit.getUserId(), spotCheckFormTableItem.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param spotCheckFormTableItem 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(SpotCheckFormTableItem spotCheckFormTableItem, Kv kv) {
		//addDeleteSystemLog(spotCheckFormTableItem.getIAutoId(), JBoltUserKit.getUserId(),spotCheckFormTableItem.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param spotCheckFormTableItem model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(SpotCheckFormTableItem spotCheckFormTableItem, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}


	public List<SpotCheckFormTableItem> findByFormId(Long formId){
		return find("SELECT * FROM Bd_SpotCheckFormTableItem WHERE iSpotCheckFormId = ?", formId);
	}
	public SpotCheckFormTableItem createQcFormTableItem(Long id, Long qcFormId, Long qcFormItemId, Long qcFormParamId, Long qcFormTableParamId){
		SpotCheckFormTableItem qcFormTableItem = new SpotCheckFormTableItem();
		if (ObjUtil.isNull(id)){
			id = JBoltSnowflakeKit.me.nextId();
		}
		qcFormTableItem.setIAutoId(id);
		qcFormTableItem.setISpotCheckFormId(qcFormId);
		qcFormTableItem.setISpotCheckFormItemId(qcFormItemId);
		qcFormTableItem.setISpotCheckFormParamId(qcFormParamId);
		qcFormTableItem.setISpotCheckFormTableParamId(qcFormTableParamId);
		return qcFormTableItem;
	}

	public List<SpotCheckFormTableItem> createQcFormTableItemList(Long qcFormId, List<SpotCheckFormItem> qcFormItemList, JSONArray jsonArray){
		if (CollUtil.isEmpty(jsonArray)){
			return null;
		}
		Map<Long, SpotCheckFormItem> qcItemMap = qcFormItemList.stream().collect(Collectors.toMap(SpotCheckFormItem::getIQcItemId, Function.identity(), (key1, key2) -> key2));
		List<SpotCheckFormTableItem> qcFormTableItemList = new ArrayList<>();
		//
		for (Object obj : jsonArray){
			JSONObject jsonObject = (JSONObject)obj;

			for (Long itemId : qcItemMap.keySet()){
				if (jsonObject.containsKey(itemId)){
					SpotCheckFormTableItem qcFormTableItem = createQcFormTableItem(
							null,
							qcFormId,
							itemId,
							jsonObject.getLong(String.valueOf(itemId)),
							jsonObject.getLong(SpotCheckFormTableItem.ISPOTCHECKFORMTABLEPARAMID.toLowerCase()));
					qcFormTableItemList.add(qcFormTableItem);
					continue;
				}

				SpotCheckFormTableItem qcFormTableItem = createQcFormTableItem(
						null,
						qcFormId,
						itemId,
						null,
						jsonObject.getLong(SpotCheckFormTableItem.ISPOTCHECKFORMTABLEPARAMID.toLowerCase()));
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
		delete("DELETE Bd_SpotCheckFormTableItem WHERE iSpotCheckFormId = ?", formId);
	}
}