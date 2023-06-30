package cn.rjtech.admin.qcformitem;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.QcFormItem;
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
 * 质量建模-检验表格项目
 * @ClassName: QcFormItemService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-04 16:10
 */
public class QcFormItemService extends BaseService<QcFormItem> {
	private final QcFormItem dao=new QcFormItem().dao();
	@Override
	protected QcFormItem dao() {
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
	public Page<Record> getAdminDatas(int pageSize, int pageNumber, Kv para) {
		System.out.println("====="+para);
		return dbTemplate(dao()._getDataSourceConfigName(), "qcformitem.formItemList", para).paginate(pageNumber, pageSize);

	}

	/**
	 * 保存
	 * @param qcFormItem
	 * @return
	 */
	public Ret save(QcFormItem qcFormItem) {
		if(qcFormItem==null || isOk(qcFormItem.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(qcFormItem.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=qcFormItem.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(qcFormItem.getIAutoId(), JBoltUserKit.getUserId(), qcFormItem.getName());
		}
		return ret(success);
	}

	public int ISeq(QcFormItem qcFormItem) {
		int success=1;
		qcFormItem.setISeq(getNextSortRank());
		return success;
	}

	/**
	 * 更新
	 * @param qcFormItem
	 * @return
	 */
	public Ret update(QcFormItem qcFormItem) {
		if(qcFormItem==null || notOk(qcFormItem.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		QcFormItem dbQcFormItem=findById(qcFormItem.getIAutoId());
		if(dbQcFormItem==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(qcFormItem.getName(), qcFormItem.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=qcFormItem.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(qcFormItem.getIAutoId(), JBoltUserKit.getUserId(), qcFormItem.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param qcFormItem 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(QcFormItem qcFormItem, Kv kv) {
		//addDeleteSystemLog(qcFormItem.getIAutoId(), JBoltUserKit.getUserId(),qcFormItem.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param qcFormItem model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(QcFormItem qcFormItem, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(QcFormItem qcFormItem, String column, Kv kv) {
		//addUpdateSystemLog(qcFormItem.getIAutoId(), JBoltUserKit.getUserId(), qcFormItem.getName(),"的字段["+column+"]值:"+qcFormItem.get(column));
		/**
		switch(column){
		    case "isDeleted":
		        break;
		}
		*/
		return null;
	}

	public Page<Record> qcitemlist(int pageNumber, int pageSize, Okv kv) {
		return dbTemplate("qcformitem.qcitemlist", kv).paginate(pageNumber, pageSize);
	}


	/**
	 * 上移
	 * @param
	 * @param iautoid
	 * @return
	 */
	public Ret up(Long iautoid) {
		QcFormItem qcFormItem=findById(iautoid);
		if(qcFormItem==null){
			return fail("数据不存在或已被删除");
		}
		Integer rank=qcFormItem.getISeq();
		if(rank==null||rank<=0){
			return fail("顺序需要初始化");
		}
		if(rank==1){
			return fail("已经是第一个");
		}
		int isep = rank-1;
		QcFormItem upQcFormItem = findFirst("SELECT top 1 * FROM Bd_QcFormItem WHERE 1 = 1 and iseq = '"+isep+"' AND isDeleted = 0  ORDER BY iAutoId ASC");

//		QcFormItem upQcFormItem=findFirst(Okv.by("iseq", rank-1));
		if(upQcFormItem==null){
			return fail("顺序需要初始化");
		}
		upQcFormItem.setISeq(rank);
		qcFormItem.setISeq(rank-1);

		upQcFormItem.update();
		qcFormItem.update();
		return SUCCESS;
	}



	/**
	 * 下移
	 * @param iautoid
	 * @return
	 */
	public Ret down(Long iautoid) {
		QcFormItem qcFormItem=findById(iautoid);
		if(qcFormItem==null){
			return fail("数据不存在或已被删除");
		}
		Integer rank=qcFormItem.getISeq();
		Long iQcFormId =qcFormItem.getIQcFormId();
		if(rank==null||rank<=0){
			return fail("顺序需要初始化");
		}
		QcFormItem max = findFirst("SELECT max(iSeq) as iseq FROM Bd_QcFormItem WHERE iQcFormId = '"+iQcFormId+"' AND isDeleted = 0");
		Integer maxiItemParamSeq = max.getInt("iseq");
		if(rank==maxiItemParamSeq){
			return fail("已经是最后一个");
		}
		int isep = rank+1;
		QcFormItem upQcFormItem = findFirst("SELECT top 1 * FROM Bd_QcFormItem WHERE 1 = 1 and iseq = '"+isep+"' AND isDeleted = 0  ORDER BY iAutoId ASC");

		if(upQcFormItem==null){
			return fail("顺序需要初始化");
		}
		upQcFormItem.setISeq(rank);
		qcFormItem.setISeq(rank+1);

		upQcFormItem.update();
		qcFormItem.update();
		return SUCCESS;
	}



	/**
	 * 删除
	 * @param iautoid
	 * @return
	 */
	public Ret delete(Long iautoid) {
		QcFormItem qcFormItem=findById(iautoid);
			//重新设置其他剩余顺序
			Long iqcformid = qcFormItem.getIQcFormId();
			int iSeq = qcFormItem.getISeq();
			//软删除isDeleted
			deleteByTopnavId(iautoid);

			update("update Bd_QcFormItem set ISeq = ISeq - 1 WHERE iQcFormId ='"+iqcformid+"' AND iSeq >= 0 AND iSeq >= '"+iSeq+"' AND isDeleted = 0 ");

		return SUCCESS;
	}


	/**
	 * 删除一个顶部导航下的菜单配置
	 * @param iAutoId
	 */
	public void deleteByTopnavId(Long iAutoId) {
		if(isOk(iAutoId)) {
//			deleteBy(Okv.by("iautoid", iAutoId));
			update("update Bd_QcFormItem set isDeleted =  1 WHERE iAutoId ='"+iAutoId+"'");
		}
	}
	
	public QcFormItem createQcFormItem(Long qcFormItemId, Long qcFormId, Long qcItemId, Integer seq, Boolean isDeleted){
		QcFormItem qcFormItem = new QcFormItem();
		if (ObjUtil.isNull(qcFormItemId)){
			qcFormItemId = JBoltSnowflakeKit.me.nextId();
		}
		qcFormItem.setIAutoId(qcFormItemId);
		qcFormItem.setIQcFormId(qcFormId);
		qcFormItem.setIQcItemId(qcItemId);
		qcFormItem.setISeq(seq);
		qcFormItem.setIsDeleted(isDeleted);
		return qcFormItem;
	}

	public List<QcFormItem> createQcFormItemList(Long qcFormId, boolean isDelete , JSONArray formItemArray){
		if (CollUtil.isEmpty(formItemArray)){
			return null;
		}
		List<QcFormItem> list = new ArrayList<>();
		for (Object obj : formItemArray){
			JSONObject jsonObject = (JSONObject)obj;
			QcFormItem qcFormItem = createQcFormItem(
					null,
					qcFormId,
					jsonObject.getLong(QcFormItem.IQCITEMID.toLowerCase()),
					jsonObject.getInteger(QcFormItem.ISEQ.toLowerCase()),
					isDelete);
			list.add(qcFormItem);
		}
		return list;
	}
	
	public void removeByQcFormId(Long formId){
		delete("DELETE Bd_QcFormItem WHERE iQCFormId = ?", formId);
	}
	
	public List<QcFormItem> findByFormId(Long formId){
		return find("SELECT * FROM Bd_QcFormItem WHERE iQcFormId = ?", formId);
	}
}
