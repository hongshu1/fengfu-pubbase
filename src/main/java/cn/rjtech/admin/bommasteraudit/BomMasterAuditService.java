package cn.rjtech.admin.bommasteraudit;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.BomMasterAudit;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
/**
 * 物料建模-BOM版本审核记录
 * @ClassName: BomMasterAuditService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-28 15:28
 */
public class BomMasterAuditService extends BaseService<BomMasterAudit> {
	private final BomMasterAudit dao=new BomMasterAudit().dao();
	@Override
	protected BomMasterAudit dao() {
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
     * @param IsDeleted 删除状态：0. 未删除 1. 已删除
	 * @return
	 */
	public Page<BomMasterAudit> getAdminDatas(int pageNumber, int pageSize, String keywords, Boolean IsDeleted) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
	    //sql条件处理
        sql.eqBooleanToChar("IsDeleted",IsDeleted);
        //关键词模糊查询
        sql.likeMulti(keywords,"cOrgName", "cCreateName", "cUpdateName");
        //排序
        sql.desc("iAutoId");
		return paginate(sql);
	}

	/**
	 * 保存
	 * @param bomMasterAudit
	 * @return
	 */
	public Ret save(BomMasterAudit bomMasterAudit) {
		if(bomMasterAudit==null || isOk(bomMasterAudit.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(bomMasterAudit.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=bomMasterAudit.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(bomMasterAudit.getIAutoId(), JBoltUserKit.getUserId(), bomMasterAudit.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param bomMasterAudit
	 * @return
	 */
	public Ret update(BomMasterAudit bomMasterAudit) {
		if(bomMasterAudit==null || notOk(bomMasterAudit.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		BomMasterAudit dbBomMasterAudit=findById(bomMasterAudit.getIAutoId());
		if(dbBomMasterAudit==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(bomMasterAudit.getName(), bomMasterAudit.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=bomMasterAudit.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(bomMasterAudit.getIAutoId(), JBoltUserKit.getUserId(), bomMasterAudit.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param bomMasterAudit 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(BomMasterAudit bomMasterAudit, Kv kv) {
		//addDeleteSystemLog(bomMasterAudit.getIAutoId(), JBoltUserKit.getUserId(),bomMasterAudit.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param bomMasterAudit model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(BomMasterAudit bomMasterAudit, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(BomMasterAudit bomMasterAudit, String column, Kv kv) {
		//addUpdateSystemLog(bomMasterAudit.getIAutoId(), JBoltUserKit.getUserId(), bomMasterAudit.getName(),"的字段["+column+"]值:"+bomMasterAudit.get(column));
		/**
		switch(column){
		    case "IsDeleted":
		        break;
		}
		*/
		return null;
	}

}