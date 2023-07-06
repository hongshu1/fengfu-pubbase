package cn.rjtech.cache;

import cn.jbolt.core.cache.JBoltCache;
import cn.rjtech.admin.form.FormService;
import cn.rjtech.model.momdata.Form;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Aop;

/**
 * 表单
 *
 * @author Kephon
 */
public class FormCache extends JBoltCache {

    public static final FormCache ME = new FormCache();

    private final FormService service = Aop.get(FormService.class);

    @Override
    public String getCacheTypeName() {
        return "form";
    }

    public Form get(long iautoid) {
        return service.findById(iautoid);
    }

    /**
     * 获取审批方式
     */
    public long getFormId(String cformsn) {
        Long formId = service.getFormIdByCformSn(cformsn);
        ValidationUtils.notNull(formId, String.format("表单编码“%s”不存在", cformsn));
        return formId;
    }

}
