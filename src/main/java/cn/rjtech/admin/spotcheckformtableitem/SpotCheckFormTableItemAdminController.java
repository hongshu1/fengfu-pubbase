package cn.rjtech.admin.spotcheckformtableitem;

import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.SpotCheckFormTableItem;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;
/**
 * 质量建模-点检表格行记录
 * @ClassName: SpotCheckFormTableItemAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-31 11:08
 */
@Path(value = "/admin/spotCheckFormTableItem", viewPath = "/_view/admin/spotcheckformtableitem")
public class SpotCheckFormTableItemAdminController extends BaseAdminController {

	@Inject
	private SpotCheckFormTableItemService service;
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
	public void save(@Para("spotCheckFormTableItem")SpotCheckFormTableItem spotCheckFormTableItem) {
		renderJson(service.save(spotCheckFormTableItem));
	}

   /**
	* 编辑
	*/
	public void edit() {
		SpotCheckFormTableItem spotCheckFormTableItem=service.findById(getLong(0));
		if(spotCheckFormTableItem == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("spotCheckFormTableItem",spotCheckFormTableItem);
		render("edit.html");
	}

   /**
	* 更新
	*/
	public void update(@Para("spotCheckFormTableItem")SpotCheckFormTableItem spotCheckFormTableItem) {
		renderJson(service.update(spotCheckFormTableItem));
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
