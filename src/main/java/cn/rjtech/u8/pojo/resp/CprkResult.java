package cn.rjtech.u8.pojo.resp;

/**
 * 成品入库通知-请求结果
 *
 * @author Kephon
 */
public class CprkResult extends U8ApiResp {

    private String VouchId;

    public String getVouchId() {
        return VouchId;
    }

    public void setVouchId(String vouchId) {
        this.VouchId = vouchId;
    }

    @Override
    public String toString() {
        return "CprkResult{" +
                "VouchId='" + VouchId + '\'' +
                '}';
    }

}
