package cn.rjtech.u8.pojo.req.purchaseorder;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Kephon
 */
@XmlRootElement(name = "body")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class PurchaseorderBody implements Serializable {

    @XmlElement(name = "entry")
    private List<PurchaseorderEntry> entry = new ArrayList<>();

    public List<PurchaseorderEntry> getEntry() {
        return entry;
    }

    public void setEntry(List<PurchaseorderEntry> entry) {
        this.entry = entry;
    }

    @Override
    public String toString() {
        return "Body{" +
                "entry = '" + entry + '\'' +
                "}";
    }

}
