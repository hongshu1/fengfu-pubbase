package cn.rjtech.u9;

import com.alibaba.fastjson.annotation.JSONField;

public class U9APIContext {

    @JSONField(name = "CultureName")
    private String CultureName;
    @JSONField(name = "EntCode")
    private String EntCode;
    @JSONField(name = "OrgCode")
    private String OrgCode;
    @JSONField(name = "OrgID")
    private Long OrgID;
    @JSONField(name = "UserCode")
    private String UserCode;

    public String getCultureName() {
        return CultureName;
    }

    public void setCultureName(String cultureName) {
        CultureName = cultureName;
    }

    public String getEntCode() {
        return EntCode;
    }

    public void setEntCode(String entCode) {
        EntCode = entCode;
    }

    public String getOrgCode() {
        return OrgCode;
    }

    public void setOrgCode(String orgCode) {
        OrgCode = orgCode;
    }

    public Long getOrgID() {
        return OrgID;
    }

    public void setOrgID(Long orgID) {
        OrgID = orgID;
    }

    public String getUserCode() {
        return UserCode;
    }

    public void setUserCode(String userCode) {
        UserCode = userCode;
    }
}
