package cn.rjtech.u9.webservice.mo;

import cn.rjtech.config.U9ApiConfigKey;
import cn.rjtech.u9.RemoteInvoker;
import cn.rjtech.u9.U9APIContext;
import cn.rjtech.u9.U9APIResult;
import cn.rjtech.u9.entity.mo.MODTO;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.kit.JsonKit;
import com.jfinal.kit.Kv;

public class MoInvoke {

    /**
     * 推送U9生产订单
     * @param iOrgId    u9组织ID
     * @param cOrgCode  u9组织编码
     * @param modto
     * @return
     */
    public static U9APIResult moInvoke(Long iOrgId, String cOrgCode, String cPersonCode,MODTO modto){
        U9APIContext u9APIContext = new U9APIContext();
        u9APIContext.setCultureName("zh-CN");
        u9APIContext.setEntCode(U9ApiConfigKey.U9Entcode());
        u9APIContext.setOrgCode(cOrgCode);
        u9APIContext.setOrgID(iOrgId);
        u9APIContext.setUserCode(cPersonCode);

        String json = JsonKit.toJson(Kv.by("context", u9APIContext).set("mODTO", modto));

        U9APIResult u9APIResult = RemoteInvoker.invoke(U9ApiConfigKey.commoncreatemo(), json);

        ValidationUtils.isTrue(u9APIResult.getIsSuccess(), "创建生产订单:"+u9APIResult.getReTag());

        //审核订单
        String auditjson = JsonKit.toJson(Kv.by("context", u9APIContext).set("OrgID", u9APIContext.getOrgID()).set("docNo", u9APIResult.getDocNo()));

        U9APIResult u9APIAuditResult = RemoteInvoker.invoke(U9ApiConfigKey.commonapprovemo(), auditjson);

        ValidationUtils.isTrue(u9APIAuditResult.getIsSuccess(), "审核生产订单:" + u9APIResult.getDocNo()+u9APIResult.getReTag());

        return u9APIResult;
    }
}
