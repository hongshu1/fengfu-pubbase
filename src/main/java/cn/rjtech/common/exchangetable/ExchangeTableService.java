package cn.rjtech.common.exchangetable;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.OrgAccessKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.common.model.ExchangeTable;
import cn.rjtech.constants.ErrorMsg;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.kit.Ret;

import java.util.Date;
import java.util.List;

/**
 * @Author: lidehui
 * @Date: 2023/1/30 13:22
 * @Version: 1.0
 * @Desc:
 */
public class ExchangeTableService extends BaseService<ExchangeTable> {
    
    private final ExchangeTable dao = new ExchangeTable().dao();

    @Override
    public String dataSourceConfigName() {
        return OrgAccessKit.getU8DbAlias();
    }

    @Override
    protected String database() {
        return OrgAccessKit.getU8DbName();
    }

    @Override
    protected ExchangeTable dao() {
        return dao;
    }

    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

    public Ret save(List<ExchangeTable> exchangeTables){
        if (exchangeTables == null){
            return fail(JBoltMsg.PARAM_ERROR);
        }
        for(ExchangeTable exchangeTable : exchangeTables){
            exchangeTable.setCreateDate(new Date());
            ValidationUtils.isTrue(exchangeTable.save(), ErrorMsg.SAVE_FAILED);
        }
        /*tx(() -> {
            return true;
        });*/
        return SUCCESS;
    }

}
