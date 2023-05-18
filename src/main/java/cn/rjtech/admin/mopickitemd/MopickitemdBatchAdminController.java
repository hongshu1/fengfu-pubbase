package cn.rjtech.admin.mopickitemd;

import com.jfinal.aop.Inject;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.MopickitemdBatch;
/**
 * 生产订单-备料单存货现品票明细
 * @ClassName: MopickitemdBatchAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-09 15:29
 */
@CheckPermission(PermissionKey.ADMIN_MOPICKITEMD)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/mopickitemdbatch", viewPath = "/_view/admin/mopickitemdbatch")
public class MopickitemdBatchAdminController extends BaseAdminController {

	@Inject
	private MopickitemdBatchService service;
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
		renderJson(service.save(getModel(MopickitemdBatch.class, "mopickitemdBatch")));
	}

   /**
	* 编辑
	*/
	public void edit() {
		MopickitemdBatch mopickitemdBatch=service.findById(getLong(0));
		if(mopickitemdBatch == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("mopickitemdBatch",mopickitemdBatch);
		render("edit().html");
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(MopickitemdBatch.class, "mopickitemdBatch")));
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
