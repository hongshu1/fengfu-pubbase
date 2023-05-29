package cn.rjtech.u8.pojo.req.purchaseorder;

import cn.rjtech.u8.constants.U8ApiConstants;
import cn.rjtech.u8.pojo.req.BaseReq;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "purchaseorder")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class PurchaseorderReq extends BaseReq {

    @XmlElement(name = "header")
    private PurchaseorderHeader header = new PurchaseorderHeader();

    @XmlElement(name = "body")
    private PurchaseorderBody body = new PurchaseorderBody();

    public PurchaseorderHeader getHeader() {
        return header;
    }

    public void setHeader(PurchaseorderHeader header) {
        this.header = header;
    }

    public PurchaseorderBody getBody() {
        return body;
    }

    public void setBody(PurchaseorderBody body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "Purchaseorder{" +
                "header = '" + header + '\'' +
                ",body = '" + body + '\'' +
                "}";
    }

    @Override
    public String getRequestXml(String orgCode, String proc) {
        return U8ApiConstants.getPurchaseOrderRequestXml(orgCode, proc, this);
    }

}
