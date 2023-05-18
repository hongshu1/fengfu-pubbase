package cn.rjtech.admin.sysmaterialsprepare.mopickitemd;

import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.rjtech.model.momdata.MoPickItemD;
/**
 * 生产订单-备料单明细
 * @ClassName: MoPickItemDService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-09 15:28
 */
public class MoPickItemDService extends BaseService<MoPickItemD> {
	private final MoPickItemD dao=new MoPickItemD().dao();
	@Override
	protected MoPickItemD dao() {
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
     * @param isScanned 齐料扫码检查;0. 未检查 1. 已检查
	 * @return
	 */
	public Page<MoPickItemD> getAdminDatas(int pageNumber, int pageSize, String keywords, Boolean isScanned) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
	    //sql条件处理
        sql.eqBooleanToChar("isScanned",isScanned);
        //关键词模糊查询
        sql.like("cOperationName",keywords);
        //排序
        sql.desc("iAutoId");
		return paginate(sql);
	}

	/**
	 * 保存
	 * @param moPickItemD
	 * @return
	 */
	public Ret save(MoPickItemD moPickItemD) {
		if(moPickItemD==null || isOk(moPickItemD.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(moPickItemD.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=moPickItemD.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(moPickItemD.getIAutoId(), JBoltUserKit.getUserId(), moPickItemD.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param moPickItemD
	 * @return
	 */
	public Ret update(MoPickItemD moPickItemD) {
		if(moPickItemD==null || notOk(moPickItemD.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		MoPickItemD dbMoPickItemD=findById(moPickItemD.getIAutoId());
		if(dbMoPickItemD==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(moPickItemD.getName(), moPickItemD.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=moPickItemD.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(moPickItemD.getIAutoId(), JBoltUserKit.getUserId(), moPickItemD.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param moPickItemD 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(MoPickItemD moPickItemD, Kv kv) {
		//addDeleteSystemLog(moPickItemD.getIAutoId(), JBoltUserKit.getUserId(),moPickItemD.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param moPickItemD model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(MoPickItemD moPickItemD, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(MoPickItemD moPickItemD, String column, Kv kv) {
		//addUpdateSystemLog(moPickItemD.getIAutoId(), JBoltUserKit.getUserId(), moPickItemD.getName(),"的字段["+column+"]值:"+moPickItemD.get(column));
		/**
		switch(column){
		    case "isScanned":
		        break;
		}
		*/
		return null;
	}

}