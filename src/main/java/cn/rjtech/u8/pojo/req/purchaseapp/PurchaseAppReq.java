package cn.rjtech.u8.pojo.req.purchaseapp;

import cn.rjtech.u8.constants.U8ApiConstants;
import cn.rjtech.u8.pojo.req.BaseReq;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 采购申请单
 *
 * @author Kephon
 */
@XmlRootElement(name = "purchaseapp")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class PurchaseAppReq extends BaseReq {

    @XmlElement(name = "header")
    private PurchaseAppHeader header = new PurchaseAppHeader();

    @XmlElement(name = "body")
    private PurchaseAppBody body = new PurchaseAppBody();

    public PurchaseAppReq() {
    }

    public PurchaseAppReq(String orgCode) {
        super(orgCode);
    }

    public void setHeader(PurchaseAppHeader header) {
        this.header = header;
    }

    public PurchaseAppHeader getHeader() {
        return header;
    }

    public PurchaseAppBody getBody() {
        return body;
    }

    public void setBody(PurchaseAppBody body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "PurchaseAppReq{" +
                "header=" + header +
                ", body=" + body +
                '}';
    }

    @Override
    public String getRequestXml(String orgCode, String proc) {
        return U8ApiConstants.getPurchaseAppRequestXml(orgCode, proc, this);
    }

}
