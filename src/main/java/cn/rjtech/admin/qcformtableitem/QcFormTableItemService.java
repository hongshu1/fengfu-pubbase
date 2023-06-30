package cn.rjtech.admin.qcformtableitem;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.QcFormItem;
import cn.rjtech.model.momdata.QcFormTableItem;
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
 * 质量建模-检验表格行记录
 * @ClassName: QcFormTableItemService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-08 16:57
 */
public class QcFormTableItemService extends BaseService<QcFormTableItem> {
	private final QcFormTableItem dao=new QcFormTableItem().dao();
	@Override
	protected QcFormTableItem dao() {
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
	public Page<QcFormTableItem> getAdminDatas(int pageNumber, int pageSize) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
        //排序
        sql.desc("iAutoId");
		return paginate(sql);
	}

	/**
	 * 保存
	 * @param qcFormTableItem
	 * @return
	 */
	public Ret save(QcFormTableItem qcFormTableItem) {
		if(qcFormTableItem==null || isOk(qcFormTableItem.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(qcFormTableItem.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=qcFormTableItem.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(qcFormTableItem.getIAutoId(), JBoltUserKit.getUserId(), qcFormTableItem.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param qcFormTableItem
	 * @return
	 */
	public Ret update(QcFormTableItem qcFormTableItem) {
		if(qcFormTableItem==null || notOk(qcFormTableItem.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		QcFormTableItem dbQcFormTableItem=findById(qcFormTableItem.getIAutoId());
		if(dbQcFormTableItem==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(qcFormTableItem.getName(), qcFormTableItem.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=qcFormTableItem.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(qcFormTableItem.getIAutoId(), JBoltUserKit.getUserId(), qcFormTableItem.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param qcFormTableItem 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(QcFormTableItem qcFormTableItem, Kv kv) {
		//addDeleteSystemLog(qcFormTableItem.getIAutoId(), JBoltUserKit.getUserId(),qcFormTableItem.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param qcFormTableItem model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(QcFormTableItem qcFormTableItem, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

	public QcFormTableItem createQcFormTableItem(Long id, Long qcFormId, Long qcFormItemId, Long qcFormParamId, Long qcFormTableParamId){
		QcFormTableItem qcFormTableItem = new QcFormTableItem();
		if (ObjUtil.isNull(id)){
			id = JBoltSnowflakeKit.me.nextId();
		}
		qcFormTableItem.setIAutoId(id);
		qcFormTableItem.setIQCFormId(qcFormId);
		qcFormTableItem.setIQcFormItemId(qcFormItemId);
		qcFormTableItem.setIQcFormParamId(qcFormParamId);
		qcFormTableItem.setIQcFormTableParamId(qcFormTableParamId);
		return qcFormTableItem;
	}

	public List<QcFormTableItem> createQcFormTableItemList(Long qcFormId, List<QcFormItem> qcFormItemList, JSONArray jsonArray){
		if (CollUtil.isEmpty(jsonArray)){
			return null;
		}
		Map<Long, QcFormItem> qcItemMap = qcFormItemList.stream().collect(Collectors.toMap(QcFormItem::getIQcItemId, Function.identity(), (key1, key2) -> key2));
		List<QcFormTableItem> qcFormTableItemList = new ArrayList<>();
		//
		for (Object obj : jsonArray){
			JSONObject jsonObject = (JSONObject)obj;

			for (Long itemId : qcItemMap.keySet()){
				if (jsonObject.containsKey(itemId)){
					QcFormTableItem qcFormTableItem = createQcFormTableItem(
							null,
							qcFormId,
							itemId,
							jsonObject.getLong(String.valueOf(itemId)),
							jsonObject.getLong(QcFormTableItem.IQCFORMTABLEPARAMID.toLowerCase()));
					qcFormTableItemList.add(qcFormTableItem);
					continue;
				}

				QcFormTableItem qcFormTableItem = createQcFormTableItem(
						null,
						qcFormId,
						itemId,
						null,
						jsonObject.getLong(QcFormTableItem.IQCFORMTABLEPARAMID.toLowerCase()));
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
		delete("DELETE Bd_QcFormTableItem WHERE iQCFormId = ?", formId);
	}

	public List<QcFormTableItem> findByFormId(Long formId){
		return find("SELECT * FROM Bd_QcFormTableItem WHERE iQcFormId = ?", formId);
	}

}
