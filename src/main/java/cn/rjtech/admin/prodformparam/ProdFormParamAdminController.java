package cn.rjtech.admin.prodformparam;

import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.ProdFormParam;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.Okv;
/**
 * 生产表单设置-生产表单参数
 * @ClassName: ProdFormParamAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-06-27 10:13
 */
@Path(value = "/admin/prodForm/qcformparam", viewPath = "/_view/admin/prodForm/qcformparam")
public class ProdFormParamAdminController extends BaseAdminController {

	@Inject
	private ProdFormParamService service;
	/**
	 * 首页
	 */
	public void index() {
		render("index.html");
	}
	/**
	 * 数据源
	 */
	public void datas() {
		renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getBoolean("isDeleted")));
	}

	/**
	 * 新增
	 */
	public void add() {
		render("add.html");
	}

	/**
	 * 保存
	 */
	public void save(@Para("prodFormParam") ProdFormParam prodFormParam) {
		renderJson(service.save(prodFormParam));
	}

	/**
	 * 编辑
	 */
	public void edit() {
		ProdFormParam prodFormParam=service.findById(getLong(0));
		if(prodFormParam == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("prodFormParam",prodFormParam);
		render("edit.html");
	}

	public void getQcFormParamListByPId(@Para(value = "iqcformid") Long qcFormId) {
		renderJsonData(service.getQcFormParamListByPId(qcFormId));
	}
	/**
	 * 查询表格项目
	 */
	public void qcformparamDatas() {
		set("type", get("type"));
		set("FormItemCodes", get("FormItemCodes"));
		set("iqcformitemid", get("typeId"));
		set("iQcItemIds", get("iQcItemIds"));
		keepPara();
		render("qcformparam.html");
	}
	/**
	 * 表格内容数据源
	 */
	public void qcformparamlist() {
		renderJsonData(service.qcformparamlist(getPageNumber(), getPageSize(), Okv.create().set(getKv())));

	}
	/**
	 * 更新
	 */
	public void update(@Para("prodFormParam")ProdFormParam prodFormParam) {
		renderJson(service.update(prodFormParam));
	}

	/**
	 * 批量删除
	 */
	public void deleteByIds() {
		renderJson(service.deleteByIds(get("ids")));
	}

	/**
	 * 删除
	 */
	public void delete() {
		renderJson(service.deleteById(getLong(0)));
	}

	/**
	 * 切换isDeleted
	 */
	public void toggleIsDeleted() {
		renderJson(service.toggleBoolean(getLong(0),"isDeleted"));
	}

	/**
	 * 切换toggleIsEnabled
	 */
	public void toggleIsEnabled() {
		renderJson(service.toggleIsEnabled(getLong(0)));
	}
}
