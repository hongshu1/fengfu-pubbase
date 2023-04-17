package cn.rjtech.admin.qcformtableparam;

import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.rjtech.model.momdata.QcFormTableParam;
/**
 * 质量建模-检验表格参数录入配置
 * @ClassName: QcFormTableParamService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-04 16:12
 */
public class QcFormTableParamService extends BaseService<QcFormTableParam> {
	private final QcFormTableParam dao=new QcFormTableParam().dao();
	@Override
	protected QcFormTableParam dao() {
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
     * @param iType 参数录入方式：1. CPK数值 2. 文本框 3. 选择（√，/，×，△，◎） 4. 单选 5. 复选 6. 下拉列表 7. 日期 8. 时间
     * @param isDeleted 删除状态：0. 未删除 1. 已删除
	 * @return
	 */
	public Page<QcFormTableParam> getAdminDatas(int pageNumber, int pageSize, Integer iType, Boolean isDeleted) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
	    //sql条件处理
        sql.eq("iType",iType);
        sql.eqBooleanToChar("isDeleted",isDeleted);
        //排序
        sql.desc("iAutoId");
		return paginate(sql);
	}

	/**
	 * 保存
	 * @param qcFormTableParam
	 * @return
	 */
	public Ret save(QcFormTableParam qcFormTableParam) {
		if(qcFormTableParam==null || isOk(qcFormTableParam.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(qcFormTableParam.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=qcFormTableParam.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(qcFormTableParam.getIAutoId(), JBoltUserKit.getUserId(), qcFormTableParam.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param qcFormTableParam
	 * @return
	 */
	public Ret update(QcFormTableParam qcFormTableParam) {
		if(qcFormTableParam==null || notOk(qcFormTableParam.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		QcFormTableParam dbQcFormTableParam=findById(qcFormTableParam.getIAutoId());
		if(dbQcFormTableParam==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(qcFormTableParam.getName(), qcFormTableParam.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=qcFormTableParam.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(qcFormTableParam.getIAutoId(), JBoltUserKit.getUserId(), qcFormTableParam.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param qcFormTableParam 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(QcFormTableParam qcFormTableParam, Kv kv) {
		//addDeleteSystemLog(qcFormTableParam.getIAutoId(), JBoltUserKit.getUserId(),qcFormTableParam.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param qcFormTableParam model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(QcFormTableParam qcFormTableParam, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(QcFormTableParam qcFormTableParam, String column, Kv kv) {
		//addUpdateSystemLog(qcFormTableParam.getIAutoId(), JBoltUserKit.getUserId(), qcFormTableParam.getName(),"的字段["+column+"]值:"+qcFormTableParam.get(column));
		/**
		switch(column){
		    case "isDeleted":
		        break;
		}
		*/
		return null;
	}

}