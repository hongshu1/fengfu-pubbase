package cn.rjtech.admin.proposalattachment;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileTypeUtil;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.service.JBoltFileService;
import cn.jbolt.extend.config.ExtendUploadFolder;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.constants.Constants;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;
import com.jfinal.upload.UploadFile;

import java.util.ArrayList;

/**
 * 禀议书附件 Controller
 *
 * @ClassName: ProposalAttachmentAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-10-17 17:14
 */
@UnCheck
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/proposalattachment", viewPath = "/_view/admin/proposalattachment")
public class ProposalAttachmentAdminController extends BaseAdminController {

    @Inject
    private ProposalAttachmentService service;
    @Inject
    private JBoltFileService jBoltFileService;

    /**
     * 首页
     */
    public void index() {
        render("index.html");
    }

    public void list(@Para(value = "iproposalmid") Long iproposalmid) {
        if (null == iproposalmid) {
            renderJsonData(CollUtil.empty(ArrayList.class));
            return;
        }
        renderJsonData(service.getList(iproposalmid));
    }

    /**
     * 新增
     */
    public void add() {
        render("add.html");
    }

    /**
     * 上传文件
     */
    public void uploadFile() {
        String uploadPath = ExtendUploadFolder.todayFolder(ExtendUploadFolder.PROPOSAL);
        UploadFile file = getFile("file", uploadPath);
        if (notOk(file)) {
            renderJsonFail("请上传文件");
            return;
        }
        renderJson(jBoltFileService.saveFile(file, uploadPath, Constants.getFileType(FileTypeUtil.getType(file.getFile()))).set("name", file.getOriginalFileName()));
    }

}
