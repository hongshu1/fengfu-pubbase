package cn.rjtech.admin.purchaseattachment;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileTypeUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.service.JBoltFileService;
import cn.jbolt.extend.config.ExtendUploadFolder;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.constants.Constants;
import cn.rjtech.model.momdata.PurchaseAttachment;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;
import com.jfinal.upload.UploadFile;

import java.util.ArrayList;
/**
 * 申购单附件 Controller
 * @ClassName: PurchaseAttachmentAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-06-30 15:15
 */
@UnCheck
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/purchaseattachment", viewPath = "/_view/admin/purchaseattachment")
public class PurchaseAttachmentAdminController extends BaseAdminController {

	@Inject
	private PurchaseAttachmentService service;
	@Inject
    private JBoltFileService jBoltFileService;
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
		renderJsonData(service.paginateAdminDatas(getPageNumber(),getPageSize(),getKeywords()));
	}

   /**
	* 新增
	*/
	public void add() {
		render("add.html");
	}

   /**
	* 编辑
	*/
	public void edit() {
		PurchaseAttachment purchaseAttachment=service.findById(getLong(0)); 
		if(purchaseAttachment == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		set("purchaseAttachment",purchaseAttachment);
		render("edit.html");
	}

  /**
	* 保存
	*/
	public void save() {
		renderJson(service.save(getModel(PurchaseAttachment.class, "purchaseAttachment")));
	}

   /**
	* 更新
	*/
	public void update() {
		renderJson(service.update(getModel(PurchaseAttachment.class, "purchaseAttachment")));
	}

   /**
	* 批量删除
	*/
	public void deleteByIds() {
		renderJson(service.deleteByBatchIds(get("ids")));
	}

   /**
	* 删除
	*/
	public void delete() {
		renderJson(service.delete(getLong(0)));
	}

    @UnCheck
    public void list(@Para(value = "ipurchasemid") Long ipurchasemid) {
        if (null == ipurchasemid) {
            renderJsonData(CollUtil.empty(ArrayList.class));
            return;
        }
        renderJsonData(service.getList(ipurchasemid));
    }

    /**
     * 上传文件
     */
    public void uploadFile() {
        String uploadPath = ExtendUploadFolder.todayFolder(ExtendUploadFolder.PURCHASEM_FILES);
        UploadFile file = getFile("file", uploadPath);
        if (notOk(file)) {
            renderJsonFail("请上传文件");
            return;
        }
        renderJson(jBoltFileService.saveFile(file, uploadPath, Constants.getFileType(FileTypeUtil.getType(file.getFile()))).set("name", file.getOriginalFileName()));
    }
}
