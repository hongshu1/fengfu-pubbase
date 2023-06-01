package cn.rjtech.config;

/**
 * MES系统配置KEY
 *
 * @author Kephon
 */
public class MesConfigKey {

    public static final MesConfigKey ME = new MesConfigKey();

    public static final String APP_NAME = "APP_NAME";

    /**
     * 默认税率
     */
    public static final String TAX_RATE = "tax_rate";
    /**
     * 部门层级
     */
    public static final String DEP_GRADE = "dep_grade";

    private MesConfigKey() {
        // ignored
    }

}
