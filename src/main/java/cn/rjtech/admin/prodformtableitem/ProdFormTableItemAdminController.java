package cn.rjtech.admin.prodformtableitem;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.ProdFormTableItem;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;
/**
 * 生产表单设置-生产表单表格行记录
 * @ClassName: ProdFormTableItemAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-06-27 10:13
 */
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/prodFormTableItem", viewPath = "/_view/admin/prodformtableitem")
public class ProdFormTableItemAdminController extends BaseAdminController {

	@Inject
	private ProdFormTableItemService service;
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
		renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize()));
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
	public void save(@Para("prodFormTableItem") ProdFormTableItem prodFormTableItem) {
		renderJson(service.save(prodFormTableItem));
	}

	/**
	 * 编辑
	 */
	public void edit() {
		ProdFormTableItem prodFormTableItem=service.findById(getLong(0));
		if(prodFormTableItem == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("prodFormTableItem",prodFormTableItem);
		render("edit.html");
	}

	/**
	 * 更新
	 */
	public void update(@Para("prodFormTableItem")ProdFormTableItem prodFormTableItem) {
		renderJson(service.update(prodFormTableItem));
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


}
