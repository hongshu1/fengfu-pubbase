package cn.rjtech.admin.qcformitem;

import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.rjtech.model.momdata.QcFormItem;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;

/**
 * 质量建模-检验表格项目
 * @ClassName: QcFormItemService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-27 17:09
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
     * @param isDeleted 删除状态：0. 未删除 1. 已删除
	 * @return
	 */
	public Page<QcFormItem> getAdminDatas(int pageNumber, int pageSize, Boolean isDeleted) {
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

	/**
	 * 行数据
	 */
	public List<Record> formItemList(Kv kv) {
		return dbTemplate("qcformitem.formItemList", kv).find();
	}

}