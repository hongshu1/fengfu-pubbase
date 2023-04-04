package cn.rjtech.admin.qcform;

import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.rjtech.model.momdata.QcForm;
import com.jfinal.plugin.activerecord.Record;

/**
 * 质量建模-检验表格
 * @ClassName: QcFormService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-27 17:53
 */
public class QcFormService extends BaseService<QcForm> {
	private final QcForm dao=new QcForm().dao();
	@Override
	protected QcForm dao() {
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
	 * @param keywords   关键词
     * @param isDeleted 删除状态：0. 未删除 1. 已删除
     * @param isEnabled 是否启用：0. 否 1. 是
	 * @return
	 */
	public Page<QcForm> getAdminDatas(int pageNumber, int pageSize, String keywords, Boolean isDeleted, Boolean isEnabled) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
	    //sql条件处理
        sql.eqBooleanToChar("isDeleted",isDeleted);
        sql.eqBooleanToChar("isEnabled",isEnabled);
        //关键词模糊查询
        sql.likeMulti(keywords,"cOrgName", "cQcFormName", "cCreateName", "cUpdateName");
        //排序
        sql.desc("iAutoId");
		return paginate(sql);
	}



	/**
	 * 后台管理数据查询
	 * @return
	 */
	public Page<Record> getAdminDatas(int pageSize, int pageNumber, Kv para) {
		return dbTemplate(dao()._getDataSourceConfigName(), "qcform.AdminDatas", para).paginate(pageNumber, pageSize);
	}

	/**
	 * 保存
	 * @param qcForm
	 * @return
	 */
	public Ret save(QcForm qcForm) {
		if(qcForm==null || isOk(qcForm.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(qcForm.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=qcForm.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(qcForm.getIAutoId(), JBoltUserKit.getUserId(), qcForm.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param qcForm
	 * @return
	 */
	public Ret update(QcForm qcForm) {
		if(qcForm==null || notOk(qcForm.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		QcForm dbQcForm=findById(qcForm.getIAutoId());
		if(dbQcForm==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(qcForm.getName(), qcForm.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=qcForm.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(qcForm.getIAutoId(), JBoltUserKit.getUserId(), qcForm.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param qcForm 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(QcForm qcForm, Kv kv) {
		//addDeleteSystemLog(qcForm.getIAutoId(), JBoltUserKit.getUserId(),qcForm.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param qcForm model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(QcForm qcForm, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(QcForm qcForm, String column, Kv kv) {
		//addUpdateSystemLog(qcForm.getIAutoId(), JBoltUserKit.getUserId(), qcForm.getName(),"的字段["+column+"]值:"+qcForm.get(column));
		/**
		switch(column){
		    case "isDeleted":
		        break;
		    case "isEnabled":
		        break;
		}
		*/
		return null;
	}

}