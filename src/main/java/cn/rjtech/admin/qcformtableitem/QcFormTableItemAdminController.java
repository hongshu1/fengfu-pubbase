package cn.rjtech.admin.qcformtableitem;

import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.QcFormTableItem;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
/**
 * 质量建模-检验表格行记录
 * @ClassName: QcFormTableItemAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-08 16:57
 */
@Path(value = "/admin/qcFormTableItem", viewPath = "/_view/admin/qcFormTableItem")
public class QcFormTableItemAdminController extends BaseAdminController {

	@Inject
	private QcFormTableItemService service;
   /**
	* 首页
	*/
	public void index() {
		render("index().html");
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
		render("add().html");
	}

   /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(QcFormTableItem.class, "qcFormTableItem")));
	}

   /**
	* 编辑
	*/
	public void edit() {
		QcFormTableItem qcFormTableItem=service.findById(getLong(0));
		if(qcFormTableItem == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("qcFormTableItem",qcFormTableItem);
		render("edit().html");
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(QcFormTableItem.class, "qcFormTableItem")));
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
