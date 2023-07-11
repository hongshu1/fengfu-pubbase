package cn.rjtech.cache;

import cn.jbolt.core.cache.JBoltCache;
import cn.rjtech.admin.formapproval.FormApprovalService;
import com.jfinal.aop.Aop;

import java.util.List;

/**
 * @author Kephon
 */
public class FormApprovalCache extends JBoltCache {
    
    public static final FormApprovalCache ME = new FormApprovalCache();

    private final FormApprovalService service = Aop.get(FormApprovalService.class);
    
    @Override
    public String getCacheTypeName() {
        return "form_approval_cache";
    }

    /**
     * 获取审批用户名
     */
    public List<String> getNextApprovalUserNames(long formApprovalId, int size) {
        return service.getNextApprovalUserNames(formApprovalId, size);
    }
    
}
