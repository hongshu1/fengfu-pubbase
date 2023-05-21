package cn.rjtech.u9.webservice.issue;

import cn.rjtech.config.U9ApiConfigKey;
import cn.rjtech.u9.RemoteInvoker;
import cn.rjtech.u9.U9APIContext;
import cn.rjtech.u9.U9APIResult;
import cn.rjtech.u9.entity.issue.IssueDTO;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.kit.JsonKit;
import com.jfinal.kit.Kv;

public class IssueInvoke {

    /**
     * 推送U9领料单
     * @param iOrgId U9组织ID
     * @param cOrgCode u9组织编码
     * @param issueDTO
     * @return
     */
    public static U9APIResult issInvoke(Long iOrgId, String cOrgCode, String cPersonCode, IssueDTO issueDTO){
        U9APIContext u9APIContext = new U9APIContext();
        u9APIContext.setCultureName("zh-CN");
        u9APIContext.setEntCode(U9ApiConfigKey.U9Entcode());
        u9APIContext.setOrgCode(cOrgCode);
        u9APIContext.setOrgID(iOrgId);
        u9APIContext.setUserCode(cPersonCode);

        String json = JsonKit.toJson(Kv.by("context", u9APIContext).set("OrgID", u9APIContext.getOrgID()).set("issueDTO", issueDTO));

        U9APIResult u9APIResult = RemoteInvoker.invoke(U9ApiConfigKey.commoncreateissue(), json);

        ValidationUtils.isTrue(u9APIResult.getIsSuccess(), "创建领料单:"+u9APIResult.getReTag());

        //审核订单
        String auditjson = JsonKit.toJson(Kv.by("context", u9APIContext).set("OrgID", u9APIContext.getOrgID()).set("docNo", u9APIResult.getDocNo()));

        U9APIResult u9APIAuditResult = RemoteInvoker.invoke(U9ApiConfigKey.commonapproveissue(), auditjson);

        ValidationUtils.isTrue(u9APIAuditResult.getIsSuccess(), "审核领料单:" + u9APIResult.getDocNo()+u9APIResult.getReTag());

        return u9APIResult;
    }
}
