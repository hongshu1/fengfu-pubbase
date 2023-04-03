package cn.rjtech.admin.backuplog;

import cn.jbolt.core.kit.JBoltUserKit;
import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.rjtech.model.momdata.BackupLog;

import java.util.Date;

/**
 * 开发管理-备份记录
 * @ClassName: BackupLogService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-31 17:06
 */
public class BackupLogService extends BaseService<BackupLog> {
	private final BackupLog dao=new BackupLog().dao();
	@Override
	protected BackupLog dao() {
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
	 * @return
	 */
	public Page<BackupLog> getAdminDatas(int pageNumber, int pageSize, String keywords) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
        //关键词模糊查询
        sql.likeMulti(keywords,"cName", "cUpdateName");
        //排序
        sql.desc("iAutoId");
		return paginate(sql);
	}

	/**
	 * 保存
	 * @param backupLog
	 * @return
	 */
	public Ret save(BackupLog backupLog) {
		if(backupLog==null || isOk(backupLog.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//创建信息
		backupLog.setICreateBy(JBoltUserKit.getUserId());
		backupLog.setCCreateName(JBoltUserKit.getUserName());
		backupLog.setDCreateTime(new Date());
		//更新信息
		backupLog.setIUpdateBy(JBoltUserKit.getUserId());
		backupLog.setCUpdateName(JBoltUserKit.getUserName());
		backupLog.setDUpdateTime(new Date());

		//if(existsName(backupLog.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=backupLog.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(backupLog.getIAutoId(), JBoltUserKit.getUserId(), backupLog.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param backupLog
	 * @return
	 */
	public Ret update(BackupLog backupLog) {
		if(backupLog==null || notOk(backupLog.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		BackupLog dbBackupLog=findById(backupLog.getIAutoId());
		if(dbBackupLog==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(backupLog.getName(), backupLog.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=backupLog.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(backupLog.getIAutoId(), JBoltUserKit.getUserId(), backupLog.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param backupLog 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(BackupLog backupLog, Kv kv) {
		//addDeleteSystemLog(backupLog.getIAutoId(), JBoltUserKit.getUserId(),backupLog.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param backupLog model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(BackupLog backupLog, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

}