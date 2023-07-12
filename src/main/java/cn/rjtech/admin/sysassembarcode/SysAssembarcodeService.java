package cn.rjtech.admin.sysassembarcode;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.SysAssembarcode;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
/**
 * 形态转换单条码
 * @ClassName: SysAssembarcodeService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-07-08 09:26
 */
public class SysAssembarcodeService extends BaseService<SysAssembarcode> {
	private final SysAssembarcode dao=new SysAssembarcode().dao();
	@Override
	protected SysAssembarcode dao() {
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
     * @param isDeleted 是否删除：0. 否 1. 是
	 * @return
	 */
	public Page<SysAssembarcode> getAdminDatas(int pageNumber, int pageSize, String keywords, Boolean isDeleted) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
	    //sql条件处理
        sql.eqBooleanToChar("isDeleted",isDeleted);
        //关键词模糊查询
        sql.likeMulti(keywords,"Ccreatename", "Cupdatename");
        //排序
        sql.desc("AutoID");
		return paginate(sql);
	}

	/**
	 * 保存
	 * @param sysAssembarcode
	 * @return
	 */
	public Ret save(SysAssembarcode sysAssembarcode) {
		if(sysAssembarcode==null || isOk(sysAssembarcode.getAutoID())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		boolean success=sysAssembarcode.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(sysAssembarcode.getAutoID(), JBoltUserKit.getUserId(), sysAssembarcode.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param sysAssembarcode
	 * @return
	 */
	public Ret update(SysAssembarcode sysAssembarcode) {
		if(sysAssembarcode==null || notOk(sysAssembarcode.getAutoID())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		SysAssembarcode dbSysAssembarcode=findById(sysAssembarcode.getAutoID());
		if(dbSysAssembarcode==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		boolean success=sysAssembarcode.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(sysAssembarcode.getAutoID(), JBoltUserKit.getUserId(), sysAssembarcode.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param sysAssembarcode 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(SysAssembarcode sysAssembarcode, Kv kv) {
		//addDeleteSystemLog(sysAssembarcode.getAutoID(), JBoltUserKit.getUserId(),sysAssembarcode.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param sysAssembarcode model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(SysAssembarcode sysAssembarcode, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(SysAssembarcode sysAssembarcode, String column, Kv kv) {
		//addUpdateSystemLog(sysAssembarcode.getAutoID(), JBoltUserKit.getUserId(), sysAssembarcode.getName(),"的字段["+column+"]值:"+sysAssembarcode.get(column));
		/**
		switch(column){
		    case "isDeleted":
		        break;
		}
		*/
		return null;
	}

}