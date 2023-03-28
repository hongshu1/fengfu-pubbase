package cn.rjtech.admin.bommaster;

import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.rjtech.model.momdata.BomMaster;
/**
 * 物料建模-Bom母项
 * @ClassName: BomMasterService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-28 16:39
 */
public class BomMasterService extends BaseService<BomMaster> {
	private final BomMaster dao=new BomMaster().dao();
	@Override
	protected BomMaster dao() {
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
     * @param isEnabled 是否启用
     * @param isDeleted 是否删除 1已删除
	 * @return
	 */
	public Page<BomMaster> getAdminDatas(int pageNumber, int pageSize, String keywords, Boolean isEnabled, Boolean isDeleted) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
	    //sql条件处理
        sql.eqBooleanToChar("isEnabled",isEnabled);
        sql.eqBooleanToChar("isDeleted",isDeleted);
        //关键词模糊查询
        sql.likeMulti(keywords,"cOrgName", "cDocName", "cCreateName", "cUpdateName");
        //排序
        sql.desc("iAutoId");
		return paginate(sql);
	}

	/**
	 * 保存
	 * @param bomMaster
	 * @return
	 */
	public Ret save(BomMaster bomMaster) {
		if(bomMaster==null || isOk(bomMaster.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(bomMaster.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=bomMaster.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(bomMaster.getIAutoId(), JBoltUserKit.getUserId(), bomMaster.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param bomMaster
	 * @return
	 */
	public Ret update(BomMaster bomMaster) {
		if(bomMaster==null || notOk(bomMaster.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		BomMaster dbBomMaster=findById(bomMaster.getIAutoId());
		if(dbBomMaster==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(bomMaster.getName(), bomMaster.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=bomMaster.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(bomMaster.getIAutoId(), JBoltUserKit.getUserId(), bomMaster.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param bomMaster 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(BomMaster bomMaster, Kv kv) {
		//addDeleteSystemLog(bomMaster.getIAutoId(), JBoltUserKit.getUserId(),bomMaster.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param bomMaster model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(BomMaster bomMaster, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(BomMaster bomMaster, String column, Kv kv) {
		//addUpdateSystemLog(bomMaster.getIAutoId(), JBoltUserKit.getUserId(), bomMaster.getName(),"的字段["+column+"]值:"+bomMaster.get(column));
		/**
		switch(column){
		    case "isEnabled":
		        break;
		    case "isDeleted":
		        break;
		}
		*/
		return null;
	}

}