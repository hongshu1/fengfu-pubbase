package cn.rjtech.u9.webservice.rpt;

import cn.rjtech.config.U9ApiConfigKey;
import cn.rjtech.util.ValidationUtils;
import cn.rjtech.u9.RemoteInvoker;
import cn.rjtech.u9.U9APIContext;
import cn.rjtech.u9.U9APIResult;
import cn.rjtech.u9.entity.rpt.RptDTO;
import com.jfinal.kit.JsonKit;
import com.jfinal.kit.Kv;

public class RptInvoke {

    /**
     * 推送U9入库单
     * @param iOrgId    u9组织ID
     * @param cOrgCode  u9组织编码
     * @param rptDTO
     * @return
     */
    public static U9APIResult rptInvoke(Long iOrgId, String cOrgCode, String cPersonCode, RptDTO rptDTO){
        U9APIContext u9APIContext = new U9APIContext();
        u9APIContext.setCultureName("zh-CN");
        u9APIContext.setEntCode(U9ApiConfigKey.U9Entcode());
        u9APIContext.setOrgID(iOrgId);
        u9APIContext.setOrgCode(cOrgCode);
        u9APIContext.setUserCode(cPersonCode);

        String crpkjson = JsonKit.toJson(Kv.by("context", u9APIContext).set("OrgID", u9APIContext.getOrgID()).set("rptDTO", rptDTO));

        U9APIResult u9APIResult = RemoteInvoker.invoke(U9ApiConfigKey.commoncreatervcrpt(), crpkjson);

        ValidationUtils.isTrue(u9APIResult.getIsSuccess(), "创建入库单:"+u9APIResult.getReTag());

        //审核入库单
        String auditjson = JsonKit.toJson(Kv.by("context", u9APIContext).set("OrgID", u9APIContext.getOrgID()).set("docNo", u9APIResult.getDocNo()));

        U9APIResult u9APIAuditResult = RemoteInvoker.invoke(U9ApiConfigKey.commonapprovervcrpt(), auditjson);

        ValidationUtils.notNull(u9APIAuditResult, "审核入库单:" +u9APIResult.getDocNo()+u9APIAuditResult.getReTag());

        return u9APIResult;
    }
}
