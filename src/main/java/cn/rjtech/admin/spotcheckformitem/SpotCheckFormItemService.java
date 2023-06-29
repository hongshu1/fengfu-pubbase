package cn.rjtech.admin.spotcheckformitem;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.SpotCheckFormItem;
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
 * 质量建模-点检表格项目
 * @ClassName: SpotCheckFormItemService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-31 10:57
 */
public class SpotCheckFormItemService extends BaseService<SpotCheckFormItem> {
	private final SpotCheckFormItem dao=new SpotCheckFormItem().dao();
	@Override
	protected SpotCheckFormItem dao() {
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
     * @param isDeleted 删除状态：0. 未删除 1. 已删除
	 * @return
	 */
	public Page<SpotCheckFormItem> getAdminDatas(int pageNumber, int pageSize, Boolean isDeleted) {
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
	 * @param spotCheckFormItem
	 * @return
	 */
	public Ret save(SpotCheckFormItem spotCheckFormItem) {
		if(spotCheckFormItem==null || isOk(spotCheckFormItem.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		boolean success=spotCheckFormItem.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(spotCheckFormItem.getIAutoId(), JBoltUserKit.getUserId(), spotCheckFormItem.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param spotCheckFormItem
	 * @return
	 */
	public Ret update(SpotCheckFormItem spotCheckFormItem) {
		if(spotCheckFormItem==null || notOk(spotCheckFormItem.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		SpotCheckFormItem dbSpotCheckFormItem=findById(spotCheckFormItem.getIAutoId());
		if(dbSpotCheckFormItem==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		boolean success=spotCheckFormItem.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(spotCheckFormItem.getIAutoId(), JBoltUserKit.getUserId(), spotCheckFormItem.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param spotCheckFormItem 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(SpotCheckFormItem spotCheckFormItem, Kv kv) {
		//addDeleteSystemLog(spotCheckFormItem.getIAutoId(), JBoltUserKit.getUserId(),spotCheckFormItem.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param spotCheckFormItem model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(SpotCheckFormItem spotCheckFormItem, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(SpotCheckFormItem spotCheckFormItem, String column, Kv kv) {
		//addUpdateSystemLog(spotCheckFormItem.getIAutoId(), JBoltUserKit.getUserId(), spotCheckFormItem.getName(),"的字段["+column+"]值:"+spotCheckFormItem.get(column));
		/**
		switch(column){
		    case "isDeleted":
		        break;
		}
		*/
		return null;
	}

	public Page<Record> qcitemlist(int pageNumber, int pageSize, Okv kv) {
		return dbTemplate("spotcheckformitem.qcitemlist", kv).paginate(pageNumber, pageSize);
	}
	public SpotCheckFormItem createQcFormItem(Long qcFormItemId, Long qcFormId, Long qcItemId, Integer seq, Boolean isDeleted){
		SpotCheckFormItem qcFormItem = new SpotCheckFormItem();
		if (ObjectUtil.isNull(qcFormItemId)){
			qcFormItemId = JBoltSnowflakeKit.me.nextId();
		}
		qcFormItem.setIAutoId(qcFormItemId);
		qcFormItem.setISpotCheckFormId(qcFormId);
		qcFormItem.setIQcItemId(qcItemId);
		qcFormItem.setISeq(seq);
		qcFormItem.setIsDeleted(isDeleted);
		return qcFormItem;
	}
	public List<SpotCheckFormItem> createQcFormItemList(Long qcFormId, boolean isDelete , JSONArray formItemArray){
		if (CollectionUtil.isEmpty(formItemArray)){
			return null;
		}
		List<SpotCheckFormItem> list = new ArrayList<>();
		for (Object obj : formItemArray){
			JSONObject jsonObject = (JSONObject)obj;
			SpotCheckFormItem qcFormItem = createQcFormItem(
					null,
					qcFormId,
					jsonObject.getLong(SpotCheckFormItem.IQCITEMID.toLowerCase()),
					jsonObject.getInteger(SpotCheckFormItem.ISEQ.toLowerCase()),
					isDelete);
			list.add(qcFormItem);
		}
		return list;
	}
	public void removeByQcFormId(Long formId){
		delete("DELETE Bd_SpotCheckFormItem WHERE iSpotCheckFormId = ?", formId);
	}

	public List<Record> formItemLists(Kv para){
		return dbTemplate("spotcheckformitem.formItemLists",para).find();
	}
}