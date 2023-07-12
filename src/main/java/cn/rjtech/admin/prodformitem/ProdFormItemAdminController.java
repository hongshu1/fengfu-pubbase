package cn.rjtech.admin.prodformitem;

import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.ProdFormItem;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.Okv;
/**
 * 生产表单设置-生产表单项目
 * @ClassName: ProdFormItemAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-06-27 10:11
 */
@Path(value = "/admin/prodForm/qcformitem", viewPath = "/_view/admin/prodForm/qcformitem")
public class ProdFormItemAdminController extends BaseAdminController {

	@Inject
	private ProdFormItemService service;
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
	public void save(@Para("prodFormItem") ProdFormItem prodFormItem) {
		renderJson(service.save(prodFormItem));
	}

	/**
	 * 编辑
	 */
	public void edit() {
		ProdFormItem prodFormItem=service.findById(getLong(0));
		if(prodFormItem == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("prodFormItem",prodFormItem);
		render("edit.html");
	}
	/**
	 * 查询表格项目
	 */
	public void qcFormItemDatas() {
		set("type", get("type"));
		set("FormItemCodes", get("FormItemCodes"));
		System.out.println("===" + get("FormItemCodes"));
		render("qcitem.html");
	}
	/**
	 * 表格项目数据源
	 */
	public void qcitemlist() {
		Okv kv = new Okv();
		kv.setIfNotNull("iQcItemId", get("FormItemCodes"));
		renderJsonData(service.qcitemlist(getPageNumber(), getPageSize(), kv));

	}
	/**
	 * 更新
	 */
	public void update(@Para("prodFormItem")ProdFormItem prodFormItem) {
		renderJson(service.update(prodFormItem));
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
