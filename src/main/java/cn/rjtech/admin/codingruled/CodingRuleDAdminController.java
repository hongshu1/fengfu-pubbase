package cn.rjtech.admin.codingruled;

import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import cn.jbolt._admin.interceptor.JBoltAdminAuthInterceptor;
import com.jfinal.plugin.activerecord.tx.Tx;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.CodingRuleD;
/**
 * 系统设置-编码规则明细
 * @ClassName: CodingRuleDAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-04 13:56
 */
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/codingruled", viewPath = "/_view/admin/codingruled")
public class CodingRuleDAdminController extends BaseAdminController {

	@Inject
	private CodingRuleDService service;

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
		renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), get("cCodingType")));
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
	public void save() {
		renderJson(service.save(getModel(CodingRuleD.class, "codingRuleD")));
	}

   /**
	* 编辑
	*/
	public void edit() {
		CodingRuleD codingRuleD=service.findById(getLong(0));
		if(codingRuleD == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("codingRuleD",codingRuleD);
		render("edit.html");
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(CodingRuleD.class, "codingRuleD")));
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
