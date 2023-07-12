package cn.rjtech.admin.proditem;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.cache.JBoltUserCache;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.poi.excel.JBoltExcel;
import cn.jbolt.core.poi.excel.JBoltExcelHeader;
import cn.jbolt.core.poi.excel.JBoltExcelSheet;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.util.JBoltRandomUtil;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.cache.CusFieldsMappingdCache;
import cn.rjtech.model.momdata.ProdItem;
import cn.rjtech.model.momdata.QcItem;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * 生产表单建模-生产项目
 * @ClassName: ProdItemService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-06-26 15:25
 */
public class ProdItemService extends BaseService<ProdItem> {
	private final ProdItem dao=new ProdItem().dao();
	@Override
	protected ProdItem dao() {
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
     * @param keywords   关键词
     * @param isDeleted  删除状态;0. 未删除 1. 已删除
     */
	public Page<ProdItem> getAdminDatas(int pageNumber, int pageSize, String keywords, Boolean isDeleted) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
	    //sql条件处理
        sql.eqBooleanToChar("isDeleted",isDeleted);
        //关键词模糊查询
        sql.likeMulti(keywords,"cOrgName", "cProdItemName", "cCreateName", "cUpdatName");
        //排序
        sql.desc("iAutoId");
		return paginate(sql);
	}

	public Page<Record> pageList(Kv kv) {
		return dbTemplate("proditem.list", kv).paginate(kv.getInt("page"), kv.getInt("pageSize"));
	}

	/**
	 * 保存
	 */
	public Ret save(ProdItem qcItem) {
		if (qcItem == null || isOk(qcItem.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//项目编码不能重复
		ValidationUtils.isTrue(findQcItemCode(qcItem.getCProdItemCode()) == null, qcItem.getCProdItemCode() + "：项目编码重复");
		//项目名不能重复
		ValidationUtils.isTrue(findQcItemName(qcItem.getCProdItemName()).isEmpty(), qcItem.getCProdItemName() + "：项目名重复");
		//1、赋值
		String userName = JBoltUserKit.getUserName();
		Long userId = JBoltUserKit.getUserId();
		Date date = new Date();
		qcItem.setIOrgId(getOrgId());
		qcItem.setCOrgCode(getOrgCode());
		qcItem.setCOrgName(getOrgName());
		qcItem.setIsDeleted(false);
		qcItem.setICreateBy(userId);
		qcItem.setCCreateName(userName);
		qcItem.setDCreateTime(date);
		qcItem.setIUpdateBy(userId);
		qcItem.setCUpdatName(userName);
		qcItem.setDUpdateTime(date);
		//2、保存
		boolean result = qcItem.save();
		if (!result){
			return fail(JBoltMsg.FAIL);
		}
		//3、返回保存结果
		return ret(result);
	}

	/**
	 * 项目编码是否重复
	 */
	public Long findQcItemCode(String cqcItemCode) {
		return queryColumn(selectSql().select(ProdItem.CPRODITEMCODE).eq(ProdItem.CPRODITEMCODE, cqcItemCode));
	}

	/**
	 * 检验项目名称
	 */
	public List<QcItem> findQcItemName(String cqcItemName) {
		return query(selectSql().select(ProdItem.CPRODITEMNAME).eq(ProdItem.CPRODITEMNAME, cqcItemName).eq(QcItem.ISDELETED, "0"));
	}

	/**
	 * 更新
	 */
	public Ret update(ProdItem qcItem) {
		if (qcItem == null || notOk(qcItem.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		ProdItem dbQcItem = findById(qcItem.getIAutoId());
		if (dbQcItem == null) {
			return fail(JBoltMsg.DATA_NOT_EXIST);
		}
		//项目编码不能重复
		ValidationUtils.isTrue(findQcItemCode(qcItem.getCProdItemCode()) == null, qcItem.getCProdItemCode() + "：项目编码重复");
		//项目名不能重复
		ValidationUtils.isTrue(findQcItemName(qcItem.getCProdItemName()).isEmpty(), qcItem.getCProdItemName() + "：项目名重复");
		boolean result = qcItem.update();
		if (!result){
			return fail(JBoltMsg.FAIL);
		}
		return SUCCESS;
	}

	public List<Record> list(Kv kv) {
		return dbTemplate("proditem.list", kv).find();
	}

	public List<Record> options() {
		return dbTemplate("proditem.list", Kv.of("isenabled", "true")).find();
	}

	/**
	 * 删除
	 */
	public Ret delete(Long id) {
		return deleteById(id, true);
	}

	/**
	 * 导出excel文件
	 */
	public JBoltExcel exportExcelTpl(List<ProdItem> datas) {
		//2、创建JBoltExcel
		//3、返回生成的excel文件
		return JBoltExcel
				.create()
				.addSheet(//设置sheet
						JBoltExcelSheet.create("检验项目(分类)")//创建sheet name保持与模板中的sheet一致
								.setHeaders(//sheet里添加表头
										JBoltExcelHeader.create("corgcode", "组织编码", 20),
										JBoltExcelHeader.create("corgname", "组织名称", 20),
										JBoltExcelHeader.create("cqcitemcode", "检验项目编码", 20),
										JBoltExcelHeader.create("cqcitemname", "检验项目名称", 20),
										JBoltExcelHeader.create("ccreatename", "创建人", 20),
										JBoltExcelHeader.create("dcreatetime", "创建时间", 20)
								)
								.setDataChangeHandler((data, index) -> {//设置数据转换处理器
									//将user_id转为user_name
									data.changeWithKey("user_id", "user_username", JBoltUserCache.me.getUserName(data.getLong("user_id")));
									data.changeBooleanToStr("is_deleted", "是", "否");
								})
								.setModelDatas(2, datas)//设置数据
				)
				.setFileName("检验项目(分类)" + "_" + DateUtil.today());
	}

	/**
	 * 从系统导入字段配置，获得导入的数据
	 */
	public Ret importExcel(File file) {
		List<Record> records = CusFieldsMappingdCache.ME.getImportRecordsByTableName(file, table());
		if (notOk(records)) {
			return fail(JBoltMsg.DATA_IMPORT_FAIL_EMPTY);
		}

		Date now = new Date();

		for (Record record : records) {

			if (StrUtil.isBlank(record.getStr("cQcItemCode"))) {
				return fail("检验项目编码不能为空");
			}
			if (StrUtil.isBlank(record.getStr("cQcItemName"))) {
				return fail("检验项目名称不能为空");
			}
            
			record.set("iAutoId", JBoltSnowflakeKit.me.nextId());
			record.set("cQcItemCode", record.getStr("cQcItemCode"));
			record.set("cQcItemName", record.getStr("cQcItemName"));
			record.set("iCreateBy", JBoltUserKit.getUserId());
			record.set("dCreateTime", now);
			record.set("cCreateName", JBoltUserKit.getUserName());
			record.set("iOrgId", getOrgId());
			record.set("cOrgCode", getOrgCode());
			record.set("cOrgName", getOrgName());
			record.set("iUpdateBy", JBoltUserKit.getUserId());
			record.set("dUpdateTime", now);
			record.set("cUpdateName", JBoltUserKit.getUserName());
			record.set("isDeleted", ZERO_STR);
		}

		// 执行批量操作
		tx(() -> {
			batchSaveRecords(records);
			return true;
		});
		return SUCCESS;
	}

	public ProdItem findBycQcItemName(String cqcitemname) {
		return findFirst(selectSql().eq(ProdItem.CPRODITEMNAME, cqcitemname).eq(ProdItem.IORGID, getOrgId()).eq(ProdItem.ISDELETED, ZERO_STR).first());
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

		for (Record record : records) {

			if (StrUtil.isBlank(record.getStr("cQcItemCode"))) {
				return fail("检验项目编码不能为空");
			}
			if (StrUtil.isBlank(record.getStr("cQcItemName"))) {
				return fail("检验项目名称不能为空");
			}

			record.set("iAutoId", JBoltSnowflakeKit.me.nextId());
			record.set("iOrgId", getOrgId());
			record.set("cOrgCode", getOrgCode());
			record.set("cOrgName", getOrgName());
			record.set("iCreateBy", JBoltUserKit.getUserId());
			record.set("dCreateTime", now);
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

	public Long getOrAddUptimeCategoryByName(String cprodparamname) {
		ProdItem prodItem = findFirst(selectSql().eq("cProdItemName", cprodparamname));
		if (notNull(prodItem)) {
			return prodItem.getIAutoId();
		}

		ProdItem newProdItem = new ProdItem();
		newProdItem.setCProdItemCode(JBoltRandomUtil.randomNumber(6));//待优化
		newProdItem.setCProdItemName(cprodparamname);
		save(newProdItem);
		return newProdItem.getIAutoId();

	}
}