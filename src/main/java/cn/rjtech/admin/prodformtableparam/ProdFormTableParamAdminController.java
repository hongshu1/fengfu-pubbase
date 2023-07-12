package cn.rjtech.admin.prodformtableparam;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.ProdFormTableParam;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;
/**
 * 生产表单设置-生产表格参数录入配置
 * @ClassName: ProdFormTableParamAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-06-27 10:14
 */
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/prodFormTableParam", viewPath = "/_view/admin/prodformtableparam")
public class ProdFormTableParamAdminController extends BaseAdminController {

	@Inject
	private ProdFormTableParamService service;
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
		renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getInt("iType"), getBoolean("isDeleted")));
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
	public void save(@Para("prodFormTableParam") ProdFormTableParam prodFormTableParam) {
		renderJson(service.save(prodFormTableParam));
	}

	/**
	 * 编辑
	 */
	public void edit() {
		ProdFormTableParam prodFormTableParam=service.findById(getLong(0));
		if(prodFormTableParam == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("prodFormTableParam",prodFormTableParam);
		render("edit.html");
	}

	/**
	 * 更新
	 */
	public void update(@Para("prodFormTableParam")ProdFormTableParam prodFormTableParam) {
		renderJson(service.update(prodFormTableParam));
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


}
