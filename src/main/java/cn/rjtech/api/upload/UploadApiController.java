package cn.rjtech.api.upload;

import cn.hutool.core.util.StrUtil;
import cn.jbolt._admin.jboltfile.FileService;
import cn.jbolt.core.permission.UnCheck;
import cn.rjtech.base.controller.BaseApiController;
import cn.rjtech.entity.vo.upload.UploadVo;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.Okv;
import com.jfinal.upload.UploadFile;
import io.github.yedaxia.apidocs.ApiDoc;

/**
 * 文件上传
 *
 * @author Kephon
 */
@ApiDoc
@UnCheck
public class UploadApiController extends BaseApiController {

    @Inject
    private FileService fileService;

    /**
     * 图片上传
     *
     * @param folder 文件夹，格式: ${basePath}/image, 按照API路由对应的命名basePath
     * @param file   文件
     */
    @ApiDoc(result = UploadVo.class)
    public void uploadImage(@Para(value = "file") UploadFile file,
                            @Para(value = "folder") String folder) {
        ValidationUtils.notNull(file, "上传文件不能为空");
        ValidationUtils.notBlank(folder, "文件夹不能为空");

        if (notImage(file) && file.getFile().delete()) {
            renderJsonFail("请上传图片类型文件");
            return;
        }

        String uri = fileService.saveImageFile(file, folder);
        if (StrUtil.isBlank(uri)) {
            renderJBoltApiFail("上传失败");
            return;
        }

        renderJBoltApiSuccessWithData(Okv.by("uri", uri));
    }

    /**
     * Office类型文件上传，只支持excel
     *
     * @param folder 文件夹，格式: ${basePath}/${office}, 按照API路由对应的命名basePath, office根据实际类型定义，如word/excel/ppt, 暂时只支持excel格式
     * @param file   文件
     */
    @ApiDoc(result = UploadVo.class)
    public void uploadOffice(@Para(value = "file") UploadFile file,
                             @Para(value = "folder") String folder) {
        ValidationUtils.notNull(file, "上传文件不能为空");
        ValidationUtils.notBlank(folder, "文件夹不能为空");

        if (notExcel(file) && file.getFile().delete()) {
            renderJsonFail("请上传Excel类型文件");
            return;
        }

        String uri = fileService.saveExcelFile(file, folder);
        if (StrUtil.isBlank(uri)) {
            renderJBoltApiFail("上传失败");
            return;
        }

        renderJBoltApiSuccessWithData(Okv.by("uri", uri));
    }

    /**
     * 音频上传
     *
     * @param folder 文件夹，格式: ${basePath}/audio, 按照API路由对应的命名basePath
     * @param file   文件
     */
    @ApiDoc(result = UploadVo.class)
    public void uploadAudio(@Para(value = "file") UploadFile file,
                            @Para(value = "folder") String folder) {
        ValidationUtils.notNull(file, "上传文件不能为空");
        ValidationUtils.notBlank(folder, "文件夹不能为空");

        if (notOk(file)) {
            renderJsonFail("请上传音频类型文件");
            return;
        }

        String uri = fileService.saveAudioFile(file, folder);
        if (StrUtil.isBlank(uri)) {
            renderJBoltApiFail("上传失败");
            return;
        }

        renderJBoltApiSuccessWithData(Okv.by("uri", uri));
    }

    /**
     * 视频上传
     *
     * @param folder 文件夹，格式: ${basePath}/video, 按照API路由对应的命名basePath
     * @param file   文件
     */
    @ApiDoc(result = UploadVo.class)
    public void uploadVideo(@Para(value = "file") UploadFile file,
                            @Para(value = "folder") String folder) {
        ValidationUtils.notNull(file, "上传文件不能为空");
        ValidationUtils.notBlank(folder, "文件夹不能为空");

        if (notOk(file)) {
            renderJsonFail("请上传视频类型文件");
            return;
        }

        String uri = fileService.saveAudioFile(file, folder);
        if (StrUtil.isBlank(uri)) {
            renderJBoltApiFail("上传失败");
            return;
        }

        renderJBoltApiSuccessWithData(Okv.by("uri", uri));
    }

    /**
     * 附件上传
     *
     * @param folder 文件夹，格式: ${basePath}/attachment, 按照API路由对应的命名basePath
     * @param file   文件
     */
    @ApiDoc(result = UploadVo.class)
    public void saveAttachmentFile(@Para(value = "file") UploadFile file,
                                   @Para(value = "folder") String folder) {
        ValidationUtils.notNull(file, "上传文件不能为空");
        ValidationUtils.notBlank(folder, "文件夹不能为空");

        if (notOk(file)) {
            renderJsonFail("请上传文件");
            return;
        }

        String uri = fileService.saveAttachmentFile(file, folder);
        if (StrUtil.isBlank(uri)) {
            renderJBoltApiFail("上传失败");
            return;
        }

        renderJBoltApiSuccessWithData(Okv.by("uri", uri));
    }

}
