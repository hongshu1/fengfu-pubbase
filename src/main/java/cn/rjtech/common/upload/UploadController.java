package cn.rjtech.common.upload;

import cn.hutool.core.util.StrUtil;
import cn.jbolt._admin.jboltfile.FileService;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.Ret;
import com.jfinal.upload.UploadFile;

/**
 * 资源文件上传
 *
 * @author Kephon
 */
@UnCheck
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/upload")
public class UploadController extends BaseAdminController {

    @Inject
    private FileService fileService;

    /**
     * 上传文件
     *
     * @param file   文件
     * @param folder 文件夹
     * @param type   1. 图片 2. 视频 3. 音频 4. office文档 5. 其它附件
     */
    public void uploadFile(@Para(value = "file") UploadFile file,
                           @Para(value = "folder") String folder,
                           @Para(value = "type") Integer type) {
        ValidationUtils.notNull(file, "上传文件不能为空");
        ValidationUtils.notBlank(folder, "文件夹不能为空");
        ValidationUtils.notNull(type, "文件类型不能为空");

        String uri = fileService.saveFile(file, folder, type);
        if (StrUtil.isBlank(uri)) {
            renderJsonFail("上传失败");
            return;
        }

        renderJson(Ret.ok().set("data", uri));
    }

    /**
     * 多图片上传
     */
    public void uploadFiles() {

    }

}
