package cn.rjtech.admin.prodformitem;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.rjtech.model.momdata.QcFormItem;
import cn.rjtech.model.momdata.SpotCheckFormItem;
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
import cn.rjtech.model.momdata.ProdFormItem;
import com.jfinal.plugin.activerecord.Record;

import java.util.ArrayList;
import java.util.List;

/**
 * 生产表单设置-生产表单项目
 * @ClassName: ProdFormItemService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-06-27 10:11
 */
public class ProdFormItemService extends BaseService<ProdFormItem> {
	private final ProdFormItem dao=new ProdFormItem().dao();
	@Override
	protected ProdFormItem dao() {
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
	public Page<ProdFormItem> getAdminDatas(int pageNumber, int pageSize, Boolean isDeleted) {
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
	public Ret save(ProdFormItem spotCheckFormItem) {
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
	 * @param prodFormItem
	 * @return
	 */
	public Ret update(ProdFormItem prodFormItem) {
		if(prodFormItem==null || notOk(prodFormItem.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		ProdFormItem dbSpotCheckFormItem=findById(prodFormItem.getIAutoId());
		if(dbSpotCheckFormItem==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		boolean success=prodFormItem.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(spotCheckFormItem.getIAutoId(), JBoltUserKit.getUserId(), spotCheckFormItem.getName());
		}
		return ret(success);
	}


	public Page<Record> qcitemlist(int pageNumber, int pageSize, Okv kv) {
		return dbTemplate("prodformitem.qcitemlist", kv).paginate(pageNumber, pageSize);
	}
	public ProdFormItem createQcFormItem(Long qcFormItemId, Long qcFormId, Long qcItemId, Integer seq, Boolean isDeleted){
		ProdFormItem qcFormItem = new ProdFormItem();
		if (ObjectUtil.isNull(qcFormItemId)){
			qcFormItemId = JBoltSnowflakeKit.me.nextId();
		}
		qcFormItem.setIAutoId(qcFormItemId);
		qcFormItem.setIProdFormId(qcFormId);
		qcFormItem.setIProdItemId(qcItemId);
		qcFormItem.setISeq(seq);
		qcFormItem.setIsDeleted(isDeleted);
		return qcFormItem;
	}

	public List<ProdFormItem> createQcFormItemList(Long qcFormId, boolean isDelete , JSONArray formItemArray){
		if (CollectionUtil.isEmpty(formItemArray)){
			return null;
		}
		List<ProdFormItem> list = new ArrayList<>();
		for (Object obj : formItemArray){
			JSONObject jsonObject = (JSONObject)obj;
			ProdFormItem qcFormItem = createQcFormItem(
					null,
					qcFormId,
					jsonObject.getLong(QcFormItem.IQCITEMID.toLowerCase()),
					jsonObject.getInteger(ProdFormItem.ISEQ.toLowerCase()),
					isDelete);
			list.add(qcFormItem);
		}
		return list;
	}
	public void removeByQcFormId(Long formId){
		delete("DELETE Bd_ProdFormItem WHERE iProdFormId = ?", formId);
	}
	public List<ProdFormItem> findByFormId(Long formId){
		return find("SELECT * FROM Bd_ProdFormItem WHERE iProdFormId = ?", formId);
	}
}