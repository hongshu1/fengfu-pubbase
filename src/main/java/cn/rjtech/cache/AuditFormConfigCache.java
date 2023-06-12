package cn.rjtech.cache;

import cn.jbolt.core.cache.JBoltCache;
import cn.rjtech.admin.auditformconfig.AuditFormConfigService;
import cn.rjtech.model.momdata.AuditFormConfig;
import com.jfinal.aop.Aop;

/**
 * 审批表单配置
 *
 * @author Kephon
 */
public class AuditFormConfigCache extends JBoltCache {

    public static final AuditFormConfigCache ME = new AuditFormConfigCache();

    private final AuditFormConfigService service = Aop.get(AuditFormConfigService.class);

    @Override
    public String getCacheTypeName() {
        return "audit_form_config";
    }

    public AuditFormConfig get(long iautoid) {
        return service.findById(iautoid);
    }

    /**
     * 获取审批方式
     */
    public Integer getAuditWay(String cformsn) {
        return service.getAuditWayByCformSn(cformsn);
    }

}
