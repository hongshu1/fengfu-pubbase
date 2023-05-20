package cn.rjtech.admin.spotcheckparam;

import cn.hutool.core.date.DateUtil;
import cn.jbolt.common.util.CACHE;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.poi.excel.*;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.qcitem.QcItemService;
import cn.rjtech.model.momdata.QcItem;
import cn.rjtech.model.momdata.SpotCheckParam;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.io.File;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
/**
 * 点检建模-点检参数
 * @ClassName: SpotCheckParamService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-29 09:51
 */
public class SpotCheckParamService extends BaseService<SpotCheckParam> {
	private final SpotCheckParam dao=new SpotCheckParam().dao();

	@Inject
	private QcItemService qcItemService;
	@Override
	protected SpotCheckParam dao() {
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
     * @param isEnabled 是否启用：0. 否 1. 是
     * @param IsDeleted 删除状态：0. 未删除 1. 已删除 
	 * @return
	 */
	public Page<SpotCheckParam> getAdminDatas(int pageNumber, int pageSize, String keywords, Boolean isEnabled, Boolean IsDeleted) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
	    //sql条件处理
        sql.eqBooleanToChar("isEnabled",isEnabled);
        sql.eqBooleanToChar("IsDeleted",IsDeleted);
        //关键词模糊查询
        sql.likeMulti(keywords,"cOrgName", "cQcParamName", "cCreateName", "cUpdateName");
        //排序
        sql.desc("iAutoId");
		return paginate(sql);
	}

	public Page<Record> pageList(Kv kv) {
		Page<Record> recordPage = dbTemplate("spotcheckparam.list", kv).paginate(kv.getInt("page"), kv.getInt("pageSize"));
		return recordPage;
	}

	public List<Record> list(Kv kv) {
		return dbTemplate("spotcheckparam.list", kv).find();
	}

	/**
	 * 保存
	 * @param spotCheckParam
	 * @return
	 */
	public Ret save(SpotCheckParam spotCheckParam) {
		if(spotCheckParam==null || isOk(spotCheckParam.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		saveCheckParam(spotCheckParam);
		boolean success=spotCheckParam.save();
		return ret(success);
	}

	/**
	 * 更新
	 * @param spotCheckParam
	 * @return
	 */
	public Ret update(SpotCheckParam spotCheckParam) {
		if(spotCheckParam==null || notOk(spotCheckParam.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		SpotCheckParam dbSpotCheckParam=findById(spotCheckParam.getIAutoId());
		if(dbSpotCheckParam==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		boolean success=spotCheckParam.update();
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param spotCheckParam 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(SpotCheckParam spotCheckParam, Kv kv) {
		//addDeleteSystemLog(spotCheckParam.getIAutoId(), JBoltUserKit.getUserId(),spotCheckParam.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param spotCheckParam model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(SpotCheckParam spotCheckParam, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(SpotCheckParam spotCheckParam, String column, Kv kv) {
		return null;
	}

	/*
	 * 导出excel文件
	 */
	public JBoltExcel exportExcelTpl(List<SpotCheckParam> datas) {
		//2、创建JBoltExcel
		JBoltExcel jBoltExcel = JBoltExcel
			.create()
			.addSheet(//设置sheet
				JBoltExcelSheet.create("检验参数")//创建sheet name保持与模板中的sheet一致
					.setHeaders(//sheet里添加表头
						JBoltExcelHeader.create("cqcitemname", "参数项目名称", 20),
						JBoltExcelHeader.create("cqcparamname", "参数名称", 20),
//                        JBoltExcelHeader.create("isenabled", "是否启用", 20),
						JBoltExcelHeader.create("ccreatename", "创建人", 20),
						JBoltExcelHeader.create("dcreatetime", "创建时间", 20)
					)
					.setDataChangeHandler((data, index) -> {//设置数据转换处理器
						//将user_id转为user_name
						data.changeWithKey("user_id", "user_username", CACHE.me.getUserUsername(data.get("user_id")));
						data.changeBooleanToStr("is_enabled", "是", "否");
					})
					.setModelDatas(2, datas)//设置数据
			)
			.setFileName("检验参数" + "_" + DateUtil.today());
		//3、返回生成的excel文件
		return jBoltExcel;
	}

	/*
	 * 上传excel文件
	 */
	public Ret importExcelData(File file) {
		StringBuilder errorMsg = new StringBuilder();
		JBoltExcel jBoltExcel = JBoltExcel
			//从excel文件创建JBoltExcel实例
			.from(file)
			//设置工作表信息
			.setSheets(
				JBoltExcelSheet.create("sheet1")
					//设置列映射 顺序 标题名称
					.setHeaders(
						JBoltExcelHeader.create("iqcitemid", "参数项目名称"),
						JBoltExcelHeader.create("cqcparamname", "参数名称")
					)
					//从第几行开始读取
					.setDataStartRow(2)
			);
		//从指定的sheet工作表里读取数据
		List<SpotCheckParam> models = JBoltExcelUtil.readModels(jBoltExcel, "sheet1", SpotCheckParam.class, errorMsg);
		if (notOk(models)) {
			if (errorMsg.length() > 0) {
				return fail(errorMsg.toString());
			} else {
				return fail(JBoltMsg.DATA_IMPORT_FAIL_EMPTY);
			}
		}
		if (errorMsg.length() > 0) {
			return fail(errorMsg.toString());
		}
		//读取数据没有问题后判断必填字段
		if (models.size() > 0) {
			for (SpotCheckParam param : models) {
				if (notOk(param.getIQcItemId())) {
					return fail("参数项目名称不能为空");
				}
				if (notOk(param.getCQcParamName())) {
					return fail("参数名称不能为空");
				}
			}
		}
		//判断是否存在这个参数项目
		for (SpotCheckParam each : models) {
			List<QcItem> qcItemList = qcItemService.findQcItemName(each.getIQcItemId().toString());
			if (notOk(qcItemList)) {
				return fail(each.getIQcItemId() + "参数项目不存在，请去【检验项目】页面新增！");
			}
		}

		savaModelHandle(models);
		//执行批量操作
		boolean success = tx(new IAtom() {
			@Override
			public boolean run() throws SQLException {
				batchSave(models);
				return true;
			}
		});
		if (!success) {
			return fail(JBoltMsg.DATA_IMPORT_FAIL);
		}
		return SUCCESS;
	}


	public void savaModelHandle(List<SpotCheckParam> models) {
		for (SpotCheckParam param : models) {
			saveCheckParam(param);
			List<QcItem> qcItemList = qcItemService.findQcItemName(param.getIQcItemId().toString());
			param.setIQcItemId(qcItemList.get(0).getIAutoId());
		}
	}

	public void saveCheckParam(SpotCheckParam param) {
		Long userId = JBoltUserKit.getUserId();
		String userName = JBoltUserKit.getUserName();
		Date date = new Date();
		param.setCOrgCode(getOrgCode());
		param.setCOrgName(getOrgName());
		param.setICreateBy(userId);
		param.setCCreateName(userName);
		param.setDCreateTime(date);
		param.setDUpdateTime(date);
		param.setCUpdateName(userName);
		param.setIUpdateBy(userId);
		param.setIsDeleted(false);
		param.setIsEnabled(true);
	}

	/**
	 * 生成Excel 导入模板的数据 byte[]
	 */
	public JBoltExcel getExcelImportTpl() {
		return JBoltExcel
			//创建
			.create()
			.setSheets(
				JBoltExcelSheet.create("sheet1")
					//设置列映射 顺序 标题名称 不处理别名
					.setHeaders(2, false,
						JBoltExcelHeader.create("参数项目名称", 20),
						JBoltExcelHeader.create("参数名称", 20)
					)
					.setMerges(JBoltExcelMerge.create("A", "B", 1, 1, "点检参数"))
			);
	}

}