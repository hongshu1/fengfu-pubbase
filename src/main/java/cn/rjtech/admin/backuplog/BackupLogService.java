package cn.rjtech.admin.backuplog;

import cn.jbolt.core.kit.JBoltUserKit;
import cn.rjtech.base.exception.ParameterException;
import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.rjtech.model.momdata.BackupLog;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
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
	 * @param   cName 查询参数
	 * @return
	 */
	public Page<BackupLog> getAdminDatas(int pageNumber, int pageSize, String cName) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
        //关键词模糊查询
        sql.likeMulti(cName,"cName");
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

    public Ret copyFile(BackupLog backupLog) throws IOException {
		String cPath = backupLog.getCPath();

		if (StringUtils.isBlank(cPath)) {
				return fail("请输入文件路径");
			}
		File dest = new File("F:\\demo\\fengfu");//目的地
		//判断是否为目录，不存在则不作操作
		//判断目的地目录是否存在，不存在就创建目录
		if(!dest.exists()) {
			boolean mkdir = dest.mkdirs();
		}

		String printFilePath = "F:\\demo\\fengfu-pubbase\\src\\main\\resources"+cPath+ "/" + backupLog.getCName();

		File file = new File(printFilePath);
		if (!file.exists()) {
			return fail("服务器上文件不存在!");
		}
		//获取要复制的文件
		FileInputStream fileInputStream = new FileInputStream(file);

		/*//要生成的新文件（指定路径如果没有则创建）
		File newfile=new File(cPath);
		//获取父目录
		File fileParent = newfile.getParentFile();
		//判断是否存在
		if (!fileParent.exists()) {
			// 创建父目录文件夹
			fileParent.mkdirs();
		}*/

		OutputStream os = new FileOutputStream("F:\\demo\\fengfu"+File.separator + backupLog.getCName());
		byte[] data = new byte[1024];//缓存容器
		int len = -1;//接收长度
		while((len=fileInputStream.read(data))!=-1) {
			os.write(data, 0, len);
		}

		os.close();
		fileInputStream.close();
		/*	// 设置响应头，控制浏览器下载该文件
			response.reset();
			response.setHeader("content-disposition", "attachment;filename=" + new String((fileName + ".grf").getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1));
			response.setContentType("multipart/form-data");

			IoUtil.copy(IoUtil.toStream(file), response.getOutputStream());

			LOG.info("成功...");*/
			return SUCCESS;
	}
}