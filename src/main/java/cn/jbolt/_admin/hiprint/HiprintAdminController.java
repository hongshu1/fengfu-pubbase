package cn.jbolt._admin.hiprint;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.core.ActionKey;
import com.jfinal.core.Path;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.common.model.HiprintTpl;
import cn.jbolt.core.controller.base.JBoltBaseController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
/**
 * jbolt hiprint 封装
 * @ClassName:  JBoltHiprintAdminController   
 * @author: JFinal学院-小木 QQ：909854136 
 * @date:   2021年8月28日   
 *    
 */
@Path(value = "/admin/hiprint",viewPath = "/hiprint/")
@CheckPermission(PermissionKey.HIPRINT_DESIGN)
@UnCheckIfSystemAdmin
public class HiprintAdminController extends JBoltBaseController {
	@Inject
	private HiprintTplService tplService;
	/**
	 * 设计器
	 */
	public void index() {
		render("index.html");
	}
	/**
	 * 设计器
	 */
	@ActionKey("/admin/hiprint/tpl/designer")
	public void designer() {
		Long id = getLong(0);
		if(notOk(id)) {
			renderPageFail("请指定模板ID");
			return;
		}
		HiprintTpl tpl = tplService.findById(id);
		if(tpl==null) {
			renderPageFail("指定ID模板不存在");
			return;
		}
		set("tpl", tpl);
		render("index.html");
	}
	
	/**
	 * 仓库管理
	 */
	public void tpl() {
		keepPara("selectedId");
		render("tpl/index.html");
	}
	/**
	 * 模板获取
	 */
	@ActionKey("/admin/hiprint/tpl/content")
	public void tplContent() {
		HiprintTpl tpl = tplService.getCacheByKey(get("sn"));
		if(tpl == null) {
			renderJsonFail("hiprint打印模板加载失败");
		}else {
			renderJsonData(tpl.getContent());
		}
	}
	/**
	 * 模板库数据
	 */
	@ActionKey("/admin/hiprint/tpl/datas")
	public void tplDatas() {
		renderJsonData(tplService.paginateAdminDatas(getPageNumber(),getPageSize(),getKeywords(),getEnable(),"content"));
	}
	/**
	 * 模板库数据
	 */
	@ActionKey("/admin/hiprint/tpl/jsonDataEditor")
	public void tplJsonDataEditor() {
		render("tpl/jsondataeditor.html");
	}
	/**
	 * 模板库数据加载一个
	 */
	@ActionKey("/admin/hiprint/tpl/load")
	public void tplLoad() {
		renderJsonData(tplService.findByIdLoadColumns(getLong(0), "id","name","content"));
	}
	/**
	 * 模板库数据加载一个
	 */
	@ActionKey("/admin/hiprint/tpl/testJsonData")
	public void tplTestJsonData() {
		JSONObject json = new JSONObject();
		json.put("title", "JBolt极速开发平台");
		json.put("qrcode", "http://jbolt.cn/jbolt.html");
		json.put("barcode", "123456789");
		json.put("avatar", "assets/img/avatar.png");
		json.put("imgurl", "assets/img/jfinalxylogo.png");
		renderJsonData(json);
	}
	
	/**
	 * 新增模板
	 */
	@ActionKey("/admin/hiprint/tpl/add")
	public void tplAdd() {
		render("tpl/add.html");
	}
	/**
	 * 编辑模板
	 */
	@ActionKey("/admin/hiprint/tpl/edit")
	public void tplEdit() {
		Long id = getLong(0);
		if(notOk(id)) {
			renderPageFail("请指定模板ID");
			return;
		}
		HiprintTpl tpl = tplService.findById(id);
		if(tpl==null) {
			renderPageFail("指定ID模板不存在");
			return;
		}
		set("tpl", tpl);
		render("tpl/edit.html");
	}
	/**
	 * 提交模板
	 */
	@ActionKey("/admin/hiprint/tpl/submit")
	public void submitTpl() {
		renderJson(tplService.submitTpl(getModel(HiprintTpl.class,"tpl")));
	}
	/**
	 * 删除模板
	 */
	@ActionKey("/admin/hiprint/tpl/delete")
	public void tplDelete() {
		renderJson(tplService.deleteById(getLong(0)));
	}
}
