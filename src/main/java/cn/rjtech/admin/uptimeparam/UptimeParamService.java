package cn.rjtech.admin.uptimeparam;

import cn.hutool.core.util.StrUtil;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.rjtech.admin.cusfieldsmappingd.CusFieldsMappingDService;
import cn.rjtech.admin.uptimecategory.UptimeCategoryService;
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
import cn.rjtech.model.momdata.UptimeParam;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;
import com.jfinal.weixin.sdk.utils.RetryUtils;
import org.apache.poi.hwpf.dev.RecordUtil;

import java.io.File;
import java.util.*;

/**
 * 稼动时间建模-稼动时间参数
 * @ClassName: UptimeParamService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-06-26 15:14
 */
public class UptimeParamService extends BaseService<UptimeParam> {
	private final UptimeParam dao=new UptimeParam().dao();
	@Override
	protected UptimeParam dao() {
		return dao;
	}

	@Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }
	@Inject
	private CusFieldsMappingDService cusFieldsMappingdService;
	@Inject
	private UptimeCategoryService uptimeCategoryService;


	/**
	 * 后台管理数据查询
	 * @param pageNumber 第几页
	 * @param pageSize   每页几条数据
	 * @param kv   查询条件
	 * @return
	 */
	public Page<Record> getAdminDatas(int pageNumber, int pageSize, Kv kv) {
		return dbTemplate("uptimeparam.getAdminDatas", kv).paginate(pageNumber, pageSize);
	}

	/**
	 * 保存
	 * @param uptimeParam
	 * @return
	 */
	public Ret save(UptimeParam uptimeParam) {
		if (uptimeParam == null || isOk(uptimeParam.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		// 设置其他信息
		uptimeParam.setIOrgId(getOrgId());
		uptimeParam.setCOrgCode(getOrgCode());
		uptimeParam.setCOrgName(getOrgName());
		uptimeParam.setICreateBy(JBoltUserKit.getUserId());
		uptimeParam.setCCreateName(JBoltUserKit.getUserName());
		uptimeParam.setDCreateTime(new Date());
		uptimeParam.setIUpdateBy(JBoltUserKit.getUserId());
		uptimeParam.setCUpdateName(JBoltUserKit.getUserName());
		uptimeParam.setDUpdateTime(new Date());
		uptimeParam.setIsDeleted(false);
		boolean success = uptimeParam.save();
		if (success) {
			//添加日志
			//addSaveSystemLog(uptimeParam.getIAutoId(), JBoltUserKit.getUserId(), uptimeParam.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param uptimeParam
	 * @return
	 */
	public Ret update(UptimeParam uptimeParam) {
		if(uptimeParam==null || notOk(uptimeParam.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		UptimeParam dbUptimeParam=findById(uptimeParam.getIAutoId());
		if(dbUptimeParam==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		boolean success=uptimeParam.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(uptimeParam.getIAutoId(), JBoltUserKit.getUserId(), uptimeParam.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param uptimeParam 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(UptimeParam uptimeParam, Kv kv) {
		//addDeleteSystemLog(uptimeParam.getIAutoId(), JBoltUserKit.getUserId(),uptimeParam.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param uptimeParam model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(UptimeParam uptimeParam, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(UptimeParam uptimeParam, String column, Kv kv) {
		//addUpdateSystemLog(uptimeParam.getIAutoId(), JBoltUserKit.getUserId(), uptimeParam.getName(),"的字段["+column+"]值:"+uptimeParam.get(column));
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

	/**
	 * 数据导入
	 * @param file
	 * @return
	 */
	public Ret importExcelData(File file) {
		List<Record> records = cusFieldsMappingdService.getImportRecordsByTableName(file, table());
		if (notOk(records)) {
			return fail(JBoltMsg.DATA_IMPORT_FAIL_EMPTY);
		}


		for (Record record : records) {

			if (StrUtil.isBlank(record.getStr("cuptimeparamname"))) {
				return fail("参数名称不能为空");
			}
			if (StrUtil.isBlank(record.getStr("iUptimeCategoryId"))) {
				return fail("分类名称不能为空");
			}

			// 分类名称不存在就新增
			Long iUptimeCategoryId = uptimeCategoryService.getOrAddUptimeCategoryByName(record.get("iUptimeCategoryId"));
			record.set("IUptimeCategoryId",iUptimeCategoryId);
			Date now=new Date();
			record.set("iAutoId", JBoltSnowflakeKit.me.nextId());
			record.set("iCreateBy", JBoltUserKit.getUserId());
			record.set("cOrgCode",getOrgCode());
			record.set("cOrgName",getOrgName());
			record.set("iOrgId",getOrgId());
			record.set("dCreateTime", now);
			record.set("isEnabled",1);
			record.set("cCreateName", JBoltUserKit.getUserName());
			record.set("isDeleted",0);
			record.set("iUpdateBy", JBoltUserKit.getUserId());
			record.set("dUpdateTime", now);
			record.set("cUpdateName", JBoltUserKit.getUserName());
		}

		// 执行批量操作
		tx(() -> {
			batchSaveRecords(records);
			return true;
		});
		return SUCCESS;
	}


	public Long getOrAddUptimeParamByName(Long iUptimeCategoryId, String cuptimeparamname) {
		UptimeParam uptimeParam = findFirst(selectSql().eq("isDeleted", "0").eq("cUptimeParamName", cuptimeparamname));
		if (isOk(uptimeParam)) {
			return uptimeParam.getIAutoId();
		}

		UptimeParam saveUptimeParam = new UptimeParam();
		saveUptimeParam.setCUptimeParamName(cuptimeparamname);
		saveUptimeParam.setIUptimeCategoryId(iUptimeCategoryId);
		saveUptimeParam.setIsEnabled(true);
		// 保存数据
		save(saveUptimeParam);
		return saveUptimeParam.getIAutoId();
	}
}