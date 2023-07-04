package cn.rjtech.admin.prodparam;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.jbolt.core.cache.JBoltUserCache;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.poi.excel.JBoltExcel;
import cn.jbolt.core.poi.excel.JBoltExcelHeader;
import cn.jbolt.core.poi.excel.JBoltExcelSheet;
import cn.rjtech.admin.cusfieldsmappingd.CusFieldsMappingDService;
import cn.rjtech.admin.proditem.ProdItemService;
import cn.rjtech.model.momdata.QcItem;
import cn.rjtech.model.momdata.QcParam;
import cn.rjtech.model.momdata.UptimeParam;
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
import cn.rjtech.model.momdata.ProdParam;
import com.jfinal.plugin.activerecord.Record;

import java.io.File;
import java.util.*;

/**
 * 生产表单建模-生产表单参数
 * @ClassName: ProdParamService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-06-26 13:29
 */
public class ProdParamService extends BaseService<ProdParam> {
	private final ProdParam dao=new ProdParam().dao();
	@Override
	protected ProdParam dao() {

		return dao;
	}
	@Inject
	private CusFieldsMappingDService cusFieldsMappingDService;
	@Inject
	private ProdItemService prodItemService;
	@Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

	/**
	 * 后台管理数据查询
	 * @param pageNumber 第几页
	 * @param pageSize   每页几条数据
	 * @param keywords   关键词
     * @param isEnabled 是否启用;0. 否 1. 是
     * @param isDeleted 删除状态;0. 未删除 1. 已删除
	 * @return
	 */
	public Page<ProdParam> getAdminDatas(int pageNumber, int pageSize, String keywords, Boolean isEnabled, Boolean isDeleted) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
	    //sql条件处理
        sql.eqBooleanToChar("isEnabled",isEnabled);
        sql.eqBooleanToChar("isDeleted",isDeleted);
        //关键词模糊查询
        sql.likeMulti(keywords,"cOrgName", "cProdParamName", "cCreateName", "cUpdateName");
        //排序
        sql.desc("iAutoId");
		return paginate(sql);
	}


	public Page<Record> pageList(Kv kv) {
		return dbTemplate("prodparam.list", kv).paginate(kv.getInt("page"), kv.getInt("pageSize"));
	}

	/**
	 * 保存
	 */
	public Ret save(ProdParam qcParam) {
		if (qcParam == null || isOk(qcParam.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		qcParam.setIAutoId(JBoltSnowflakeKit.me.nextId());
		saveQcParam(qcParam);
		boolean success = qcParam.save();
		if (!success){
			return fail(JBoltMsg.FAIL);
		}
		return ret(success);
	}

	/**
	 * 更新
	 */
	public Ret update(ProdParam qcParam) {
		if (qcParam == null || notOk(qcParam.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		ProdParam dbQcParam = findById(qcParam.getIAutoId());
		if (null == dbQcParam) {
			return fail(JBoltMsg.DATA_NOT_EXIST);
		}
		boolean success = qcParam.update();
		if (!success){
			return fail(JBoltMsg.FAIL);
		}
		return ret(success);
	}




	public List<Record> list(Kv kv) {
		return dbTemplate("prodparam.list", kv).find();
	}

	/**
	 * 切换isenabled属性
	 */
	public Ret toggleIsenabled(Long id) {
		return toggleBoolean(id, "isEnabled");
	}

	/**
	 * 导出excel文件
	 */
	public JBoltExcel exportExcelTpl(List<QcParam> datas) {
		//2、创建JBoltExcel
		//3、返回生成的excel文件
		return JBoltExcel
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
									data.changeWithKey("user_id", "user_username", JBoltUserCache.me.getUserName(data.getLong("user_id")));
									data.changeBooleanToStr("is_enabled", "是", "否");
								})
								.setModelDatas(2, datas)//设置数据
				)
				.setFileName("检验参数" + "_" + DateUtil.today());
	}


	public void saveQcParam(ProdParam param) {
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

	public List<ProdParam> findByIqcItemId(Long iProdItemId){
		return find("select * from Bd_prodParam where iProdItemId=?",iProdItemId);
	}

	/**
	 * 从系统导入字段配置，获得导入的数据
	 */
	public Ret importExcelClass(File file,String cformatName) {
		Ret records = cusFieldsMappingDService.getImportDatas(file, cformatName);
		if (notOk(records)) {
			return fail(JBoltMsg.DATA_IMPORT_FAIL_EMPTY);
		}

			ArrayList<Map> datas = (ArrayList<Map>) records.get("data");
			// 封装数据
			for (Map<String, String> map : datas) {
				// 分类名称不存在就新增
				Long iUptimeCategoryId = prodItemService.getOrAddUptimeCategoryByName(map.get("cproditemname"));

				ProdParam prodParam = new ProdParam();
				prodParam.setCProdParamName(map.get("cprodparamname"));
				prodParam.setIProdItemId(iUptimeCategoryId);
				prodParam.setIsEnabled(true);
				// 保存数据
				save(prodParam);
			}

			return SUCCESS;
		}
}