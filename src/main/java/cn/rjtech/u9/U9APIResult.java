package cn.rjtech.u9;

public class U9APIResult {
    private Long ID;
    private String DocNo;
    private String ReTag;
    private Boolean IsSuccess;

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getDocNo() {
        return DocNo;
    }

    public void setDocNo(String docNo) {
        DocNo = docNo;
    }

    public String getReTag() {
        return ReTag;
    }

    public void setReTag(String reTag) {
        ReTag = reTag;
    }

    public Boolean getIsSuccess() {
        return IsSuccess;
    }

    public void setIsSuccess(Boolean issuccess) {
        IsSuccess = issuccess;
    }
}
