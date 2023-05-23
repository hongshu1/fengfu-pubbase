package cn.jbolt.extend.config;

import cn.jbolt.common.config.JBoltUploadFolder;

/**
 * 扩展二开使用上传控制 定义的目录
 *
 * @ClassName: UploadFolder
 * @author: JFinal学院-小木 QQ：909854136
 * @date: 2019年3月24日 上午12:00:48
 */
public class ExtendUploadFolder extends JBoltUploadFolder {
    //下方举例说明
    /**
     * 举例说明这个是哪个地方使用的目录
     */
    public static final String EXTEND_DEMO_EDITOR_IMAGE = "extend" + SEPARATOR + "demo" + SEPARATOR + "editor";
    /**
     * 料品图片上传目录
     */
    public static final String EXTEND_ITEMMASTER_EDITOR_IMAGE = "extend" + SEPARATOR + "itemmaster" + SEPARATOR + "editor";
    /**
     * 工艺文件上传目录
     */
    public static final String EXTEND_ITEMROUTINGDRAWING_EDITOR_FILE = "extend" + SEPARATOR + "itemroutingdrawing" + SEPARATOR + "editor";
    /**
     * 模具图片上传目录
     */
    public static final String EXTEND_MOULDS_EDITOR_FILE = "extend" + SEPARATOR + "moulds" + SEPARATOR + "editor";
    /**
     * 设备文件上传目录
     */
    public static final String EXTEND_EQUIPMENT_EDITOR_FILE = "extend" + SEPARATOR + "equipment" + SEPARATOR + "editor";
    /**
     * 检具图片上传目录
     */
    public static final String EXTEND_QC_TOOLS_EDITOR_FILE = "extend" + SEPARATOR + "qctools" + SEPARATOR + "editor";
    /**
     * SRM卷料采购送货单质保书文件上传目录
     */
    public static final String SRM_JL_PO_DELIVERY_FILE = "extend" + SEPARATOR + "srmjlpodelivery" + SEPARATOR + "editor";
    /**
     * 首末检验记录文件上传目录
     */
    public static final String QC_FIRST_LAST_RECORD = "extend" + SEPARATOR + "firstlastrecord" + SEPARATOR + "editor";
    /**
     * 设备保养图片上传目录
     */
    public static final String KEEPINGRECORD_DRAWING_IMAGE = "extend" + SEPARATOR + "keepingrecord" + SEPARATOR + "editor";

    public static final String PROPOSAL = "proposal" +  SEPARATOR + "file";

	public static final String PURCHASEM_FILES = "purchasem" + SEPARATOR + "files";
}
