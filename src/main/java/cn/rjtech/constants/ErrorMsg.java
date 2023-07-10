package cn.rjtech.constants;

/**
 * 错误信息常量
 *
 * @author Kephon
 */
public class ErrorMsg {

    private ErrorMsg() {
        // ignored
    }

    public static final String PERMISSION_DENIED = "您没有操作权限";
    public static final String SAVE_FAILED = "保存失败";
    public static final String UPDATE_FAILED = "更新失败";
    public static final String DELETE_FAILED = "删除失败";
    public static final String ORG_ACCESS_DENIED = "缺少组织权限";
    public static final String ILLEGAL_ARGUMENT = "非法参数";
    public static final String BUDGET_IMPORT_CDEPCODE_NOT_EQUIL = "导入excel中的部门与当前界面选中的部门不一致!";

}
