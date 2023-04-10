package cn.rjtech.admin.backupconfig;

import cn.jbolt.core.kit.JBoltUserKit;
import cn.rjtech.admin.backuplog.BackupLogService;
import cn.rjtech.model.momdata.BackupLog;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.rjtech.model.momdata.BackupConfig;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.Date;
import java.util.List;

/**
 * 开发管理-备份设置
 * @ClassName: BackupConfigService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-03 15:15
 */
public class BackupConfigService extends BaseService<BackupConfig> {
	private final BackupConfig dao=new BackupConfig().dao();
	@Override
	protected BackupConfig dao() {
		return dao;
	}

	@Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

	@Inject
	private BackupLogService backupLogService;
	/**
	 * 后台管理数据查询
	 * @param pageNumber 第几页
	 * @param pageSize   每页几条数据
	 * @param keywords   关键词
     * @param isAutoBackupEnabled 是否启用自动备份：0. 否 1. 是
	 * @return
	 */
	public Page<BackupConfig> getAdminDatas(int pageNumber, int pageSize, String keywords, Boolean isAutoBackupEnabled) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
	    //sql条件处理
        sql.eqBooleanToChar("isAutoBackupEnabled",isAutoBackupEnabled);
        //关键词模糊查询
        sql.like("iUpdateName",keywords);
        //排序
        sql.desc("iAutoId");
		return paginate(sql);
	}

	/**
	 * 保存
	 * @param backupConfig
	 * @return
	 */
	public Ret save(BackupConfig backupConfig) {
		if(backupConfig==null || isOk(backupConfig.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//是否自动备份
		backupConfig.setIsAutoBackupEnabled(true);
		//更新信息
		backupConfig.setIUpdateBy(JBoltUserKit.getUserId());
		backupConfig.setIUpdateName(JBoltUserKit.getUserName());
		backupConfig.setDUpdateTime(new Date());

		//if(existsName(backupConfig.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=backupConfig.save();
		if(success) {
			//添加日志BackupConfig
			//addSaveSystemLog(backupConfig.getIAutoId(), JBoltUserKit.getUserId(), backupConfig.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param backupConfig
	 * @return
	 */
	public Ret update(BackupConfig backupConfig) {
		String cPath = backupConfig.getCPath();
		if (null != cPath) {
			File file = new File(cPath);
			 ValidationUtils.isTrue(file.isAbsolute(),"请输入绝对路径！");
		}
		//数据库没数据  初始化  新增操作
		List<BackupConfig> configs = findAll();
		if (configs.size() <= 0) {
			return save(backupConfig);
		}
		if(backupConfig==null || notOk(backupConfig.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		BackupConfig dbBackupConfig=findById(backupConfig.getIAutoId());
		//更新信息
		backupConfig.setIUpdateBy(JBoltUserKit.getUserId());
		backupConfig.setIUpdateName(JBoltUserKit.getUserName());
		backupConfig.setDUpdateTime(new Date());


		if(dbBackupConfig==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(backupConfig.getName(), backupConfig.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=backupConfig.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(backupConfig.getIAutoId(), JBoltUserKit.getUserId(), backupConfig.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param backupConfig 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(BackupConfig backupConfig, Kv kv) {
		//addDeleteSystemLog(backupConfig.getIAutoId(), JBoltUserKit.getUserId(),backupConfig.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param backupConfig model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(BackupConfig backupConfig, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(BackupConfig backupConfig, String column, Kv kv) {
		//addUpdateSystemLog(backupConfig.getIAutoId(), JBoltUserKit.getUserId(), backupConfig.getName(),"的字段["+column+"]值:"+backupConfig.get(column));
		/**
		switch(column){
		    case "isAutoBackupEnabled":
		        break;
		}
		*/
		return null;
	}

	//时间倒序查询备份设置
	public BackupConfig findFirstConfig() {
		return findFirst(selectSql().orderBy("dUpdateTime", "desc"));
	}

	public void copyFileTask() {
		try {
			copyFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void copyFile() throws IOException {
		//通过备份设置拿到备份文件保存路径
		BackupConfig firstConfig = findFirstConfig();
		InputStream fileInputStream = null;
		OutputStream os = null;
		String strFilePath =  firstConfig.getCPath() + File.separator ;


		File dest = new File(firstConfig.getCPath());//目的地
		//判断目的地目录是否存在，不存在就创建目录
		if(!dest.exists()) {
			dest.mkdirs();
		}
		List<BackupLog> backupLogs = backupLogService.findAll();
		for (BackupLog backupLog : backupLogs) {
			if (backupLog.getIType() == 2) {
				//源文件是否存在
				String sourceFilePath = backupLog.getCPath()+ "/" + backupLog.getCName();
				File file = new File(sourceFilePath);
				if (!file.exists()) {
					throw new NullPointerException();
				}
				//获取要复制的文件
				fileInputStream = new FileInputStream(file);




				show(dest,backupLog.getCName());
				if (dest.exists()) {
					strFilePath += "(1)" + backupLog.getCName();
				}
				os = new FileOutputStream(strFilePath);
				byte[] data = new byte[1024];//缓存容器
				int len = -1;//接收长度
				while((len=fileInputStream.read(data))!=-1) {
					os.write(data, 0, len);
				}
				System.out.println("备份成功！");
			}
		}
		System.out.println("自动备份任务执行完毕");
		if (null != os && null != fileInputStream) {
			os.close();
			fileInputStream.close();
		}
	}

	private void show(File dest,String name) {

		for (File file : dest.listFiles()) {
			if (file.isFile()) {
				if (file.getName().contains(name)) {

				}
			}

		}
	}
}