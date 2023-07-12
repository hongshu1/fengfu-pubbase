package cn.rjtech.u8.pojo.req.purchaseapp;

import cn.rjtech.u8.pojo.req.BaseBody;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Kephon
 */
@XmlRootElement(name = "body")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class PurchaseAppBody extends BaseBody {

    @XmlElement(name = "entry")
    private List<PurchaseAppEntry> entry = new ArrayList<>();

    public void setEntry(List<PurchaseAppEntry> entry) {
        this.entry = entry;
    }

    public List<PurchaseAppEntry> getEntry() {
        return entry;
    }

}
