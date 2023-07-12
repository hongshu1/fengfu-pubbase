package cn.rjtech.admin.sysmaterialspreparescan;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.SysMaterialspreparescan;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
/**
 * null管理
 * @ClassName: SysMaterialspreparescanService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-06-17 17:36
 */
public class SysMaterialspreparescanService extends BaseService<SysMaterialspreparescan> {
	private final SysMaterialspreparescan dao=new SysMaterialspreparescan().dao();
	@Override
	protected SysMaterialspreparescan dao() {
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
     * @param State 状态：1=已扫描，0=未扫描
	 * @return
	 */
	public Page<SysMaterialspreparescan> getAdminDatas(int pageNumber, int pageSize, String State) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
	    //sql条件处理
        sql.eq("State",State);
        //排序
        sql.desc("iAutoId");
		return paginate(sql);
	}

	/**
	 * 保存
	 * @param sysMaterialspreparescan
	 * @return
	 */
	public Ret save(SysMaterialspreparescan sysMaterialspreparescan) {
		if(sysMaterialspreparescan==null || isOk(sysMaterialspreparescan.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		boolean success=sysMaterialspreparescan.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(sysMaterialspreparescan.getIAutoId(), JBoltUserKit.getUserId(), sysMaterialspreparescan.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param sysMaterialspreparescan
	 * @return
	 */
	public Ret update(SysMaterialspreparescan sysMaterialspreparescan) {
		if(sysMaterialspreparescan==null || notOk(sysMaterialspreparescan.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		SysMaterialspreparescan dbSysMaterialspreparescan=findById(sysMaterialspreparescan.getIAutoId());
		if(dbSysMaterialspreparescan==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		boolean success=sysMaterialspreparescan.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(sysMaterialspreparescan.getIAutoId(), JBoltUserKit.getUserId(), sysMaterialspreparescan.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param sysMaterialspreparescan 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(SysMaterialspreparescan sysMaterialspreparescan, Kv kv) {
		//addDeleteSystemLog(sysMaterialspreparescan.getIAutoId(), JBoltUserKit.getUserId(),sysMaterialspreparescan.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param sysMaterialspreparescan model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(SysMaterialspreparescan sysMaterialspreparescan, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

}