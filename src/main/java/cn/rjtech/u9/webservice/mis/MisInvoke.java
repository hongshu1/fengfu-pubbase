package cn.rjtech.u9.webservice.mis;


import cn.rjtech.config.U9ApiConfigKey;
import cn.rjtech.u9.RemoteInvoker;
import cn.rjtech.u9.U9APIContext;
import cn.rjtech.u9.U9APIResult;
import cn.rjtech.u9.entity.mis.MisDTO;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.kit.JsonKit;
import com.jfinal.kit.Kv;

public class MisInvoke {

    /**
     * 推送U9杂发单
     * @param iOrgId u9组织ID
     * @param cOrgCode u9组织编码
     * @param misDTO
     * @return
     */
    public static U9APIResult misInvoke(Long iOrgId, String cOrgCode, String cPersonCode, MisDTO misDTO){
        U9APIContext u9APIContext = new U9APIContext();
        u9APIContext.setCultureName("zh-CN");
        u9APIContext.setEntCode(U9ApiConfigKey.U9Entcode());
        u9APIContext.setOrgCode(cOrgCode);
        u9APIContext.setOrgID(iOrgId);
        u9APIContext.setUserCode(cPersonCode);

        String json = JsonKit.toJson(Kv.by("context", u9APIContext).set("OrgID", u9APIContext.getOrgID()).set("misDTO", misDTO));

        U9APIResult u9APIResult = RemoteInvoker.invoke(U9ApiConfigKey.commoncreatemis(), json);

        ValidationUtils.isTrue(u9APIResult.getIsSuccess(), "创建杂发单:"+u9APIResult.getReTag());

        //审核订单
        String auditjson = JsonKit.toJson(Kv.by("context", u9APIContext).set("OrgID", u9APIContext.getOrgID()).set("docNo", u9APIResult.getDocNo()));

        U9APIResult u9APIAuditResult = RemoteInvoker.invoke(U9ApiConfigKey.commonapprovemis(), auditjson);

        ValidationUtils.isTrue(u9APIAuditResult.getIsSuccess(), "审核杂发单:" + u9APIResult.getDocNo()+u9APIAuditResult.getReTag());

        return u9APIResult;
    }
}
