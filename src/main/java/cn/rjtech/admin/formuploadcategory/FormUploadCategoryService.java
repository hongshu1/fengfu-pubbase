package cn.rjtech.admin.formuploadcategory;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.User;
import cn.jbolt.core.poi.excel.JBoltExcel;
import cn.jbolt.core.poi.excel.JBoltExcelHeader;
import cn.jbolt.core.poi.excel.JBoltExcelSheet;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.workregionm.WorkregionmService;
import cn.rjtech.cache.CusFieldsMappingdCache;
import cn.rjtech.model.momdata.FormUploadCategory;
import cn.rjtech.model.momdata.Workregionm;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 分类管理
 * @ClassName: FormUploadCategoryService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-29 15:16
 */
public class FormUploadCategoryService extends BaseService<FormUploadCategory> {
    
	private final FormUploadCategory dao=new FormUploadCategory().dao();

    @Inject
    private WorkregionmService workregionmService;

	@Override
	protected FormUploadCategory dao() {
		return dao;
	}

	@Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

    /**
     * 后台管理数据查询
     *
     * @param pageNumber 第几页
     * @param pageSize   每页几条数据
     */
	public Page<Record> getAdminDatas(int pageNumber, int pageSize,Kv para) {
		Page<Record> paginate = dbTemplate("formuploadcategory.list",para).paginate(pageNumber, pageSize);
		for (Record record : paginate.getList()) {
			if (ObjUtil.isNotNull(workregionmService.findById(record.getStr("iworkregionmid")))){
				record.set("iworkregionmid",workregionmService.findById(record.getStr("iworkregionmid")).getCWorkName());
			}
		}
		return  paginate;
	}

	/**
	 * 保存
	 */
	public Ret save(FormUploadCategory formUploadCategory) {
		if(formUploadCategory==null || isOk(formUploadCategory.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		User user = JBoltUserKit.getUser();
		//基础数据
		formUploadCategory.setCCategoryCode("FL" +DateUtil.format(new Date(), "yyyyMMdd") + RandomUtil.randomNumbers(6));
		formUploadCategory.setCOrgCode(getOrgCode());
		formUploadCategory.setCOrgName(getOrgName());
		formUploadCategory.setIOrgId(getOrgId());
		formUploadCategory.setICreateBy(user.getId());
		formUploadCategory.setCCreateName(user.getName());
		formUploadCategory.setDCreateTime(new Date());
		formUploadCategory.setCUpdateName(user.getName());
		formUploadCategory.setDUpdateTime(new Date());
		formUploadCategory.setIUpdateBy(user.getId());
		//if(existsName(formUploadCategory.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=formUploadCategory.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(formUploadCategory.getIAutoId(), JBoltUserKit.getUserId(), formUploadCategory.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 */
	public Ret update(FormUploadCategory formUploadCategory) {
		if(formUploadCategory==null || notOk(formUploadCategory.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		FormUploadCategory dbFormUploadCategory=findById(formUploadCategory.getIAutoId());
		if(dbFormUploadCategory==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(formUploadCategory.getName(), formUploadCategory.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=formUploadCategory.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(formUploadCategory.getIAutoId(), JBoltUserKit.getUserId(), formUploadCategory.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param formUploadCategory 要删除的model
	 * @param kv 携带额外参数一般用不上
	 */
	@Override
	protected String afterDelete(FormUploadCategory formUploadCategory, Kv kv) {
		//addDeleteSystemLog(formUploadCategory.getIAutoId(), JBoltUserKit.getUserId(),formUploadCategory.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param formUploadCategory model
	 * @param kv 携带额外参数一般用不上
	 */
	@Override
	public String checkInUse(FormUploadCategory formUploadCategory, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

	/**
     * 生成excel导入使用的模板
     */
    public JBoltExcel getImportExcelTpl() {
        return JBoltExcel
                //创建
                .create()
                .setSheets(
                        JBoltExcelSheet.create()
                        //设置列映射 顺序 标题名称 不处理别名
                        .setHeaders(1,false,
                                JBoltExcelHeader.create("分类编码",15),
                                JBoltExcelHeader.create("分类名称",15)
                                )
                    );
    }

	/**
	 * 从系统导入字段配置，获得导入的数据
	 */
	public Ret importExcelClass(File file) {
		List<Record> records = CusFieldsMappingdCache.ME.getImportRecordsByTableName(file, table());
		if (notOk(records)) {
			return fail(JBoltMsg.DATA_IMPORT_FAIL_EMPTY);
		}
        
        Date now = new Date();
        
		// 产线
		Map<String, Workregionm> workregionmMap = new HashMap<>(16);
		for (Record record : records) {

			if (StrUtil.isBlank(record.getStr("iWorkRegionmId"))) {
				return fail("产线名称不能为空");
			}
			if (StrUtil.isBlank(record.getStr("cCategoryName"))) {
				return fail("目录名称不能为空");
			}
            
			String iWorkRegionmId = record.getStr("iWorkRegionmId");
			Workregionm workregionm = workregionmMap.get(iWorkRegionmId);
			if (ObjUtil.isNull(workregionm)) {
				workregionm = workregionmService.findFirstByWorkName(iWorkRegionmId);
				ValidationUtils.notNull(workregionm, String.format("产线“%s”不存在", workregionmMap));
				record.set("iWorkRegionmId",workregionm.getIAutoId());
				workregionmMap.put(iWorkRegionmId, workregionm);
			}

			record.set("iAutoId", JBoltSnowflakeKit.me.nextId());
			record.set("iOrgId", getOrgId());
			record.set("cOrgCode", getOrgCode());
			record.set("cOrgName", getOrgName());
			record.set("isEnabled",1);
			record.set("iCreateBy", JBoltUserKit.getUserId());
			record.set("dCreateTime", now);
			record.set("cCreateName", JBoltUserKit.getUserName());
			record.set("isDeleted",0);
			record.set("iUpdateBy", JBoltUserKit.getUserId());
			record.set("dUpdateTime", now);
			record.set("cUpdateName", JBoltUserKit.getUserName());
			record.set("cCategoryCode", "FL" +DateUtil.format(new Date(), "yyyyMMdd") + RandomUtil.randomNumbers(6));
		}

		// 执行批量操作
		tx(() -> {
			batchSaveRecords(records);
			return true;
		});
		return SUCCESS;
	}

    /**
	 * 生成要导出的Excel
	 */
	public JBoltExcel exportExcel(List<Record> datas) {
		for (Record record : datas) {
			if (ObjUtil.isNotNull(workregionmService.findById(record.getStr("iworkregionmid")))){
				record.set("iworkregionmid",workregionmService.findById(record.getStr("iworkregionmid")).getCWorkName());
			}
		}
	    return JBoltExcel
			    //创建
			    .create()
    		    //设置工作表
    		    .setSheets(
    				//设置工作表 列映射 顺序 标题名称
    				JBoltExcelSheet
    				.create()
    				//表头映射关系
                    .setHeaders(1,
                            JBoltExcelHeader.create("iworkregionmid","产线名称",40),
                            JBoltExcelHeader.create("ccategoryname","目录名称",20),
                            JBoltExcelHeader.create("cmemo","备注",20)
                            )
    		    	//设置导出的数据源 来自于数据库查询出来的Model List
    		    	.setRecordDatas(2,datas)
    		    );
	}

    /**
     * 获取类别档案
     */
    public List<Record> options(Kv kv) {
        return dbTemplate("formuploadcategory.options", kv.set("isEnabled", false)).find();
    }

	/**
	 *删除
	 */
	public Ret deleteByIds(Long id) {
		tx(() -> {
				FormUploadCategory formuploadcategory = findById(id);
				ValidationUtils.notNull(formuploadcategory, JBoltMsg.DATA_NOT_EXIST);

				// TODO 可能需要补充校验组织账套权限
				// TODO 存在关联使用时，校验是否仍在使用
				formuploadcategory.setIsDeleted(true);
				ValidationUtils.isTrue(formuploadcategory.update(), JBoltMsg.FAIL);

			return true;
		});
		return SUCCESS;
	}
    
	/**
	 * 切换isenabled属性
	 */
	public Ret toggleIsenabled(Long id) {
		return toggleBoolean(id, "isEnabled");
	}

	/**
	 * 获取全部数据
	 */
	public List<Record> list(Kv kv){
		return dbTemplate("formuploadcategory.list", kv).find();
	}

	public List<Record> workregionmOptions(Kv kv) {
		return dbTemplate("formuploadcategory.workregionmOptions",kv).find();
	}
    
}