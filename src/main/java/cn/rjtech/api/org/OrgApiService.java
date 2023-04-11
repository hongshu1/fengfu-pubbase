package cn.rjtech.api.org;

import cn.jbolt.core.api.JBoltApiBaseService;
import cn.jbolt.core.api.JBoltApiRet;
import cn.jbolt.core.service.OrgService;
import com.jfinal.aop.Inject;

/**
 * 组织API Service
 *
 * @author Kephon
 */
public class OrgApiService extends JBoltApiBaseService {

    @Inject
    private OrgService orgService;

    public JBoltApiRet getList() {
        return JBoltApiRet.API_SUCCESS_WITH_DATA(orgService.getList());
    }

}
