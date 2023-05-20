package cn.rjtech.admin.padworkregion;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.PadWorkRegion;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;

/**
 * 系统管理-平板关联产线
 * @ClassName: PadWorkRegionService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-03 16:31
 */
public class PadWorkRegionService extends BaseService<PadWorkRegion> {
	private final PadWorkRegion dao=new PadWorkRegion().dao();
	@Override
	protected PadWorkRegion dao() {
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
     * @param isDefault 是否默认；0. 否 1. 是
     * @param isDeleted 删除状态：0. 未删除 1. 已删除
	 * @return
	 */
	public Page<PadWorkRegion> getAdminDatas(int pageNumber, int pageSize, Boolean isDefault, Boolean isDeleted) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
	    //sql条件处理
        sql.eqBooleanToChar("isDefault",isDefault);
        sql.eqBooleanToChar("isDeleted",isDeleted);
        //排序
        sql.desc("iAutoId");
		return paginate(sql);
	}

	/**
	 * 保存
	 * @param padWorkRegion
	 * @return
	 */
	public Ret save(PadWorkRegion padWorkRegion) {
		if(padWorkRegion==null || isOk(padWorkRegion.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(padWorkRegion.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=padWorkRegion.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(padWorkRegion.getIAutoId(), JBoltUserKit.getUserId(), padWorkRegion.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param padWorkRegion
	 * @return
	 */
	public Ret update(PadWorkRegion padWorkRegion) {
		if(padWorkRegion==null || notOk(padWorkRegion.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		PadWorkRegion dbPadWorkRegion=findById(padWorkRegion.getIAutoId());
		if(dbPadWorkRegion==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(padWorkRegion.getName(), padWorkRegion.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=padWorkRegion.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(padWorkRegion.getIAutoId(), JBoltUserKit.getUserId(), padWorkRegion.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param padWorkRegion 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(PadWorkRegion padWorkRegion, Kv kv) {
		//addDeleteSystemLog(padWorkRegion.getIAutoId(), JBoltUserKit.getUserId(),padWorkRegion.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param padWorkRegion model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(PadWorkRegion padWorkRegion, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(PadWorkRegion padWorkRegion, String column, Kv kv) {
		//addUpdateSystemLog(padWorkRegion.getIAutoId(), JBoltUserKit.getUserId(), padWorkRegion.getName(),"的字段["+column+"]值:"+padWorkRegion.get(column));
		/**
		switch(column){
		    case "isDefault":
		        break;
		    case "isDeleted":
		        break;
		}
		*/
		return null;
	}

	public List<Record> getDatasOfSql(Kv kv) {
		return dbTemplate("pad.workRegions",kv).find();
	}
}