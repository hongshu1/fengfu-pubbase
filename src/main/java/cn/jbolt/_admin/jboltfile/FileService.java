package cn.jbolt._admin.jboltfile;

import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.jbolt.common.config.JBoltUploadFolder;
import cn.jbolt.core.common.enums.JBoltSystemLogTargetType;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.JboltFile;
import cn.jbolt.core.service.base.JBoltBaseService;
import cn.jbolt.core.util.JBoltRealUrlUtil;
import com.jfinal.upload.UploadFile;

import java.io.File;
import java.util.Date;

/**
 * 文件上传 二开
 *
 * @author Kephon
 */
public class FileService extends JBoltBaseService<JboltFile> {

    private final JboltFile dao = new JboltFile().dao();

    /**
     * 保存图片
     */
    public String saveImageFile(UploadFile uploadFile, String folder) {
        return saveFile(uploadFile, folder, JboltFile.FILE_TYPE_IMAGE);
    }

    /**
     * 保存Excel
     */
    public String saveExcelFile(UploadFile uploadFile, String folder) {
        return saveFile(uploadFile, folder, JboltFile.FILE_TYPE_OFFICE);
    }

    /**
     * 保存音频
     */
    public String saveAudioFile(UploadFile uploadFile, String folder) {
        return saveFile(uploadFile, folder, JboltFile.FILE_TYPE_AUDIO);
    }

    /**
     * 保存视频
     */
    public String saveVideoFile(UploadFile uploadFile, String folder) {
        return saveFile(uploadFile, folder, JboltFile.FILE_TYPE_VEDIO);
    }

    /**
     * 保存其它附件
     */
    public String saveAttachmentFile(UploadFile uploadFile, String folder) {
        return saveFile(uploadFile, folder, JboltFile.FILE_TYPE_ATTACHMENT);
    }

    /**
     * 保存文件底层方法
     */
    public String saveFile(UploadFile uploadFile, String folder, int fileType) {
        JboltFile jboltFile = saveJBoltFile(uploadFile, folder, fileType);
        return null == jboltFile ? null : jboltFile.getLocalUrl();
    }

    /**
     * 保存文件底层方法
     */
    public JboltFile saveJBoltFile(UploadFile uploadFile, String folder, int fileType) {
        String uploadPath = JBoltUploadFolder.todayFolder(folder);

        // 目标文件路径
        String targetFilePath = JBoltUploadFolder.getUploadPath(uploadPath) + StrUtil.SLASH + uploadFile.getFileName();
        // 目标文件路径
        File targetFile = new File(targetFilePath);

        // 移动文件
        FileUtil.move(uploadFile.getFile(), targetFile, false);

        JboltFile jboltFile = new JboltFile();

        jboltFile.setObjectUserId(JBoltUserKit.getUserId());
        jboltFile.setCreateTime(new Date());
        jboltFile.setFileName(uploadFile.getOriginalFileName());
        jboltFile.setFileType(fileType);
        jboltFile.setLocalPath(FileUtil.normalize(targetFilePath));
        jboltFile.setLocalUrl(FileUtil.normalize(JBoltRealUrlUtil.get(targetFilePath)));

        // 文件大小
        jboltFile.setFileSize((int) FileUtil.size(targetFile));
        // 文件扩展名
        jboltFile.setFileSuffix(FileTypeUtil.getType(targetFile));

        return jboltFile.save() ? jboltFile : null;
    }

    @Override
    protected JboltFile dao() {
        return dao;
    }

    @Override
    protected int systemLogTargetType() {
        return JBoltSystemLogTargetType.JBOLT_FILE.getValue();
    }

}
