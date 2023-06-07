package cn.rjtech.admin.formuploadcategory;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.StrSplitter;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.User;
import cn.rjtech.admin.workregionm.WorkregionmService;
import cn.rjtech.util.BillNoUtils;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.plugin.activerecord.Page;

import java.util.Date;
import java.util.List;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import cn.jbolt.core.base.JBoltMsg;
import java.io.File;
import com.jfinal.plugin.activerecord.IAtom;
import java.sql.SQLException;
import cn.jbolt.core.poi.excel.*;
import cn.jbolt.core.db.sql.Sql;
import cn.rjtech.model.momdata.FormUploadCategory;
import com.jfinal.plugin.activerecord.Record;
//import jdk.nashorn.internal.ir.annotations.Ignore;

import static cn.hutool.core.text.StrPool.COMMA;

/**
 * 分类管理
 * @ClassName: FormUploadCategoryService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-29 15:16
 */
public class FormUploadCategoryService extends BaseService<FormUploadCategory> {
	private final FormUploadCategory dao=new FormUploadCategory().dao();
	@Override
	protected FormUploadCategory dao() {
		return dao;
	}

	@Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

	@Inject
	private WorkregionmService workregionmService;

	/**
	 * 后台管理数据查询
	 * @param pageNumber 第几页
	 * @param pageSize   每页几条数据
	 * @return
	 */
	public Page<Record> getAdminDatas(int pageNumber, int pageSize) {
		Page<Record> paginate = dbTemplate("formuploadcategory.list").paginate(pageNumber, pageSize);
		for (Record record : paginate.getList()) {
			if (ObjectUtil.isNotNull(workregionmService.findById(record.getStr("iworkregionmid")))){
				record.set("iworkregionmid",workregionmService.findById(record.getStr("iworkregionmid")).getCWorkName());
			}
		}
		return  paginate;
	}

	/**
	 * 保存
	 * @param formUploadCategory
	 * @return
	 */
	public Ret save(FormUploadCategory formUploadCategory) {
		if(formUploadCategory==null || isOk(formUploadCategory.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		User user = JBoltUserKit.getUser();
		//基础数据
		formUploadCategory.setCCategoryCode(BillNoUtils.genCurrentNo());
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
	 * @param formUploadCategory
	 * @return
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
	 * @return
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
	 * @return
	 */
	@Override
	public String checkInUse(FormUploadCategory formUploadCategory, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

	/**
     * 生成excel导入使用的模板
     * @return
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
	 * 读取excel文件
	 * @param file
	 * @return
	 */
	public Ret importExcel(File file) {
		StringBuilder errorMsg=new StringBuilder();
		JBoltExcel jBoltExcel=JBoltExcel
		//从excel文件创建JBoltExcel实例
		.from(file)
		//设置工作表信息
		.setSheets(
				JBoltExcelSheet.create()
				//设置列映射 顺序 标题名称
                .setHeaders(1,
                        JBoltExcelHeader.create("ccategorycode","分类编码"),
                        JBoltExcelHeader.create("ccategoryname","分类名称")
                        )
				//从第三行开始读取
				.setDataStartRow(2)
				);
		//从指定的sheet工作表里读取数据
		List<FormUploadCategory> formUploadCategorys=JBoltExcelUtil.readModels(jBoltExcel,1, FormUploadCategory.class,errorMsg);
		if(notOk(formUploadCategorys)) {
			if(errorMsg.length()>0) {
				return fail(errorMsg.toString());
			}else {
				return fail(JBoltMsg.DATA_IMPORT_FAIL_EMPTY);
			}
		}
		for (FormUploadCategory formUploadCategory : formUploadCategorys) {
			if (CollUtil.isNotEmpty(list(formUploadCategory.getCCategoryCode()))) {
				return fail("列表中已存在该分类！");
			}
			if (CollUtil.isNotEmpty(list(formUploadCategory.getCCategoryName()))) {
				return fail("列表中已存在该分类！");
			}
			User user = JBoltUserKit.getUser();
			//基础数据
			formUploadCategory.setCOrgCode(getOrgCode());
			formUploadCategory.setCOrgName(getOrgName());
			formUploadCategory.setIOrgId(getOrgId());
			formUploadCategory.setICreateBy(user.getId());
			formUploadCategory.setCCreateName(user.getName());
			formUploadCategory.setDCreateTime(new Date());
			formUploadCategory.setCUpdateName(user.getName());
			formUploadCategory.setDUpdateTime(new Date());
			formUploadCategory.setIUpdateBy(user.getId());
		}
		//执行批量操作
		boolean success=tx(new IAtom() {
			@Override
			public boolean run() throws SQLException {
				batchSave(formUploadCategorys);
				return true;
			}
		});

		if(!success) {
			return fail(JBoltMsg.DATA_IMPORT_FAIL);
		}
		return SUCCESS;
	}

    /**
	 * 生成要导出的Excel
	 * @return
	 */
	public JBoltExcel exportExcel(List<FormUploadCategory> datas) {
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
                            JBoltExcelHeader.create("ccategorycode","分类编码",15),
                            JBoltExcelHeader.create("ccategoryname","分类名称",15)
                            )
    		    	//设置导出的数据源 来自于数据库查询出来的Model List
    		    	.setModelDatas(2,datas)
    		    );
	}

	/**
	 *获取类别档案
	 */
	public List<Record> list(String q) {
		return dbTemplate("formuploadcategory.list", Kv.by("q",q)).find();
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
}