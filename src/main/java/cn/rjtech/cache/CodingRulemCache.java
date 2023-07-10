package cn.rjtech.cache;

import cn.jbolt.core.cache.JBoltCache;
import cn.rjtech.admin.codingrulem.CodingRuleMService;
import cn.rjtech.enums.CodingTypeEnum;
import cn.rjtech.model.momdata.CodingRuleM;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Aop;

/**
 * 编码规则
 *
 * @author Kephon
 */
public class CodingRulemCache extends JBoltCache {

    public static final CodingRulemCache ME = new CodingRulemCache();

    private final CodingRuleMService service = Aop.get(CodingRuleMService.class);

    @Override
    public String getCacheTypeName() {
        return "coding_rulem_cache";
    }

    /**
     * 是否自动生成编码
     * 
     * @param cformcode 表单编码
     */
    public boolean isAutoGenned(String cformcode) {
        CodingRuleM codingRuleM = service.findByTable(cformcode);
        ValidationUtils.notNull(codingRuleM, "当前单据未定义编码规则");

        return codingRuleM.getICodingType().equals(CodingTypeEnum.autoCodeType.getValue()); 
    }

}
