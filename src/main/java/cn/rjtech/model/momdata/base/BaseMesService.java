package cn.rjtech.model.momdata.base;

import cn.jbolt.core.cache.JBoltOrgCache;
import cn.jbolt.core.kit.OrgAccessKit;
import cn.jbolt.core.kit.U8DataSourceKit;
import cn.jbolt.core.model.base.JBoltBaseModel;
import cn.jbolt.core.service.base.JBoltBaseService;

public abstract class BaseMesService<M extends JBoltBaseModel<M>> extends JBoltBaseService<M> {
    public BaseMesService() {
    }

    protected Long getOrgId() {
        return OrgAccessKit.getOrgId();
    }

    protected String getOrgCode() {
        return OrgAccessKit.getOrgCode();
    }

    protected String u8SourceConfigName() {
        return U8DataSourceKit.ME.use(this.getOrgCode());
    }

    protected String u8SourceConfigName(long iorgid) {
        return JBoltOrgCache.ME.getU8Alias(iorgid);
    }
}
