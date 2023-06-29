package cn.rjtech.admin.sysassem;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.SysAssemdetail;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
/**
 * 组装拆卸及形态转换单明细
 * @ClassName: SysAssemdetailAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-09 09:47
 */

@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/sysAssemdetail", viewPath = "/_view/admin/sysAssemdetail")
public class SysAssemdetailAdminController extends BaseAdminController {

	@Inject
	private SysAssemdetailService service;
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
		renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getKeywords(), get("SourceType"), get("AssemType"), get("TrackType"), getBoolean("IsDeleted")));
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
		renderJson(service.save(getModel(SysAssemdetail.class, "sysAssemdetail")));
	}

   /**
	* 编辑
	*/
	public void edit() {
		SysAssemdetail sysAssemdetail=service.findById(getLong(0));
		if(sysAssemdetail == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("sysAssemdetail",sysAssemdetail);
		render("edit.html");
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(SysAssemdetail.class, "sysAssemdetail")));
	}

   /**
	* 批量删除
	*/
	public void deleteByIds() {
		renderJson(service.deleteRmRdByIds(get("ids")));
	}

	/**
	 * 删除
	 */
	public void delete() {
		renderJson(service.delete(getLong(0)));
	}

   /**
	* 切换IsDeleted
	*/
	public void toggleIsDeleted() {
	    renderJson(service.toggleBoolean(getLong(0),"IsDeleted"));
	}

    @UnCheck
	public void findEditTableDatas(){
		renderJsonData(service.findEditTableDatas(getKv()));
	}
    
}
