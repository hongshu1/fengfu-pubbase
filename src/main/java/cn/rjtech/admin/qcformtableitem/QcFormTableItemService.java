package cn.rjtech.admin.qcformtableitem;

import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.rjtech.model.momdata.QcFormTableItem;
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

}