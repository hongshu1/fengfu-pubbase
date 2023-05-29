package cn.rjtech.u8.pojo.req.purchaseorder;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Kephon
 */
@XmlRootElement(name = "entry")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class PurchaseorderEntry implements Serializable {

    @XmlElement(name = "code")
    private String code;

    @XmlElement(name = "natdiscount")
    private String natdiscount;

    @XmlElement(name = "num")
    private String num;

    @XmlElement(name = "natprice")
    private BigDecimal natprice;

    @XmlElement(name = "define34")
    private String define34;

    @XmlElement(name = "discount")
    private String discount;

    @XmlElement(name = "define35")
    private String define35;

    @XmlElement(name = "define36")
    private String define36;

    @XmlElement(name = "define37")
    private String define37;

    @XmlElement(name = "define30")
    private String define30;

    @XmlElement(name = "item_class")
    private String itemClass;

    @XmlElement(name = "define31")
    private String define31;

    @XmlElement(name = "define32")
    private String define32;

    @XmlElement(name = "define33")
    private String define33;

    @XmlElement(name = "free10")
    private String free10;

    @XmlElement(name = "taxprice")
    private BigDecimal taxprice;

    @XmlElement(name = "define27")
    private String define27;

    @XmlElement(name = "free2")
    private String free2;

    @XmlElement(name = "define28")
    private String define28;

    @XmlElement(name = "free3")
    private String free3;

    @XmlElement(name = "price")
    private BigDecimal price;

    @XmlElement(name = "define29")
    private String define29;

    @XmlElement(name = "free1")
    private String free1;

    @XmlElement(name = "free6")
    private String free6;

    @XmlElement(name = "free7")
    private String free7;

    @XmlElement(name = "free4")
    private String free4;

    @XmlElement(name = "natmoney")
    private BigDecimal natmoney;

    @XmlElement(name = "free5")
    private String free5;

    @XmlElement(name = "arrivedate")
    private String arrivedate;

    @XmlElement(name = "free8")
    private String free8;

    @XmlElement(name = "free9")
    private String free9;

    @XmlElement(name = "taxrate")
    private BigDecimal taxrate;

    @XmlElement(name = "nattax")
    private BigDecimal nattax;

    @XmlElement(name = "tax")
    private BigDecimal tax;

    @XmlElement(name = "checkflag")
    private String checkflag;

    @XmlElement(name = "item_code")
    private String itemCode;

    @XmlElement(name = "natsum")
    private BigDecimal natsum;

    @XmlElement(name = "sum")
    private BigDecimal sum;

    @XmlElement(name = "inventorycode")
    private String inventorycode;

    @XmlElement(name = "quantity")
    private BigDecimal quantity;

    @XmlElement(name = "unitquantity")
    private BigDecimal unitquantity;

    @XmlElement(name = "unitcode")
    private String unitcode;

    @XmlElement(name = "define23")
    private String define23;

    @XmlElement(name = "define24")
    private String define24;

    @XmlElement(name = "item_name")
    private String itemName;

    @XmlElement(name = "define25")
    private String define25;

    @XmlElement(name = "define26")
    private String define26;

    @XmlElement(name = "define22")
    private String define22;

    @XmlElement(name = "quotedprice")
    private String quotedprice;

    @XmlElement(name = "money")
    private BigDecimal money;

    @XmlElement(name = "assistantunit")
    private String assistantunit;

    @XmlElement(name = "natunitprice")
    private BigDecimal natunitprice;

    @XmlElement(name = "unitprice")
    private BigDecimal unitprice;

    @XmlElement(name = "bmemo")
    private String bmemo;

    @XmlElement(name = "taxcost")
    private Integer taxcost;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNatdiscount() {
        return natdiscount;
    }

    public void setNatdiscount(String natdiscount) {
        this.natdiscount = natdiscount;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public BigDecimal getNatprice() {
        return natprice;
    }

    public void setNatprice(BigDecimal natprice) {
        this.natprice = natprice;
    }

    public String getDefine34() {
        return define34;
    }

    public void setDefine34(String define34) {
        this.define34 = define34;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getDefine35() {
        return define35;
    }

    public void setDefine35(String define35) {
        this.define35 = define35;
    }

    public String getDefine36() {
        return define36;
    }

    public void setDefine36(String define36) {
        this.define36 = define36;
    }

    public String getDefine37() {
        return define37;
    }

    public void setDefine37(String define37) {
        this.define37 = define37;
    }

    public String getDefine30() {
        return define30;
    }

    public void setDefine30(String define30) {
        this.define30 = define30;
    }

    public String getItemClass() {
        return itemClass;
    }

    public void setItemClass(String itemClass) {
        this.itemClass = itemClass;
    }

    public String getDefine31() {
        return define31;
    }

    public void setDefine31(String define31) {
        this.define31 = define31;
    }

    public String getDefine32() {
        return define32;
    }

    public void setDefine32(String define32) {
        this.define32 = define32;
    }

    public String getDefine33() {
        return define33;
    }

    public void setDefine33(String define33) {
        this.define33 = define33;
    }

    public String getFree10() {
        return free10;
    }

    public void setFree10(String free10) {
        this.free10 = free10;
    }

    public BigDecimal getTaxprice() {
        return taxprice;
    }

    public void setTaxprice(BigDecimal taxprice) {
        this.taxprice = taxprice;
    }

    public String getDefine27() {
        return define27;
    }

    public void setDefine27(String define27) {
        this.define27 = define27;
    }

    public String getFree2() {
        return free2;
    }

    public void setFree2(String free2) {
        this.free2 = free2;
    }

    public String getDefine28() {
        return define28;
    }

    public void setDefine28(String define28) {
        this.define28 = define28;
    }

    public String getFree3() {
        return free3;
    }

    public void setFree3(String free3) {
        this.free3 = free3;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDefine29() {
        return define29;
    }

    public void setDefine29(String define29) {
        this.define29 = define29;
    }

    public String getFree1() {
        return free1;
    }

    public void setFree1(String free1) {
        this.free1 = free1;
    }

    public String getFree6() {
        return free6;
    }

    public void setFree6(String free6) {
        this.free6 = free6;
    }

    public String getFree7() {
        return free7;
    }

    public void setFree7(String free7) {
        this.free7 = free7;
    }

    public String getFree4() {
        return free4;
    }

    public void setFree4(String free4) {
        this.free4 = free4;
    }

    public BigDecimal getNatmoney() {
        return natmoney;
    }

    public void setNatmoney(BigDecimal natmoney) {
        this.natmoney = natmoney;
    }

    public String getFree5() {
        return free5;
    }

    public void setFree5(String free5) {
        this.free5 = free5;
    }

    public String getArrivedate() {
        return arrivedate;
    }

    public void setArrivedate(String arrivedate) {
        this.arrivedate = arrivedate;
    }

    public String getFree8() {
        return free8;
    }

    public void setFree8(String free8) {
        this.free8 = free8;
    }

    public String getFree9() {
        return free9;
    }

    public void setFree9(String free9) {
        this.free9 = free9;
    }

    public BigDecimal getTaxrate() {
        return taxrate;
    }

    public void setTaxrate(BigDecimal taxrate) {
        this.taxrate = taxrate;
    }

    public BigDecimal getNattax() {
        return nattax;
    }

    public void setNattax(BigDecimal nattax) {
        this.nattax = nattax;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public String getCheckflag() {
        return checkflag;
    }

    public void setCheckflag(String checkflag) {
        this.checkflag = checkflag;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public BigDecimal getNatsum() {
        return natsum;
    }

    public void setNatsum(BigDecimal natsum) {
        this.natsum = natsum;
    }

    public BigDecimal getSum() {
        return sum;
    }

    public void setSum(BigDecimal sum) {
        this.sum = sum;
    }

    public String getInventorycode() {
        return inventorycode;
    }

    public void setInventorycode(String inventorycode) {
        this.inventorycode = inventorycode;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public String getUnitcode() {
        return unitcode;
    }

    public void setUnitcode(String unitcode) {
        this.unitcode = unitcode;
    }

    public String getDefine23() {
        return define23;
    }

    public void setDefine23(String define23) {
        this.define23 = define23;
    }

    public String getDefine24() {
        return define24;
    }

    public void setDefine24(String define24) {
        this.define24 = define24;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getDefine25() {
        return define25;
    }

    public void setDefine25(String define25) {
        this.define25 = define25;
    }

    public String getDefine26() {
        return define26;
    }

    public void setDefine26(String define26) {
        this.define26 = define26;
    }

    public String getDefine22() {
        return define22;
    }

    public void setDefine22(String define22) {
        this.define22 = define22;
    }

    public String getQuotedprice() {
        return quotedprice;
    }

    public void setQuotedprice(String quotedprice) {
        this.quotedprice = quotedprice;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public String getAssistantunit() {
        return assistantunit;
    }

    public void setAssistantunit(String assistantunit) {
        this.assistantunit = assistantunit;
    }

    public BigDecimal getUnitquantity() {
        return unitquantity;
    }

    public void setUnitquantity(BigDecimal unitquantity) {
        this.unitquantity = unitquantity;
    }

    public void setNatunitprice(BigDecimal natunitprice) {
        this.natunitprice = natunitprice;
    }

    public BigDecimal getNatunitprice() {
        return natunitprice;
    }

    public String getBmemo() {
        return bmemo;
    }

    public void setBmemo(String bmemo) {
        this.bmemo = bmemo;
    }

    public BigDecimal getUnitprice() {
        return unitprice;
    }

    public void setUnitprice(BigDecimal unitprice) {
        this.unitprice = unitprice;
    }

    public Integer getTaxcost() {
        return taxcost;
    }

    public void setTaxcost(Integer taxcost) {
        this.taxcost = taxcost;
    }

    @Override
    public String toString() {
        return "PurchaseorderEntry{" +
                "code='" + code + '\'' +
                ", natdiscount='" + natdiscount + '\'' +
                ", num='" + num + '\'' +
                ", natprice=" + natprice +
                ", define34='" + define34 + '\'' +
                ", discount='" + discount + '\'' +
                ", define35='" + define35 + '\'' +
                ", define36='" + define36 + '\'' +
                ", define37='" + define37 + '\'' +
                ", define30='" + define30 + '\'' +
                ", itemClass='" + itemClass + '\'' +
                ", define31='" + define31 + '\'' +
                ", define32='" + define32 + '\'' +
                ", define33='" + define33 + '\'' +
                ", free10='" + free10 + '\'' +
                ", taxprice=" + taxprice +
                ", define27='" + define27 + '\'' +
                ", free2='" + free2 + '\'' +
                ", define28='" + define28 + '\'' +
                ", free3='" + free3 + '\'' +
                ", price=" + price +
                ", define29='" + define29 + '\'' +
                ", free1='" + free1 + '\'' +
                ", free6='" + free6 + '\'' +
                ", free7='" + free7 + '\'' +
                ", free4='" + free4 + '\'' +
                ", natmoney=" + natmoney +
                ", free5='" + free5 + '\'' +
                ", arrivedate='" + arrivedate + '\'' +
                ", free8='" + free8 + '\'' +
                ", free9='" + free9 + '\'' +
                ", taxrate=" + taxrate +
                ", nattax=" + nattax +
                ", tax=" + tax +
                ", checkflag='" + checkflag + '\'' +
                ", itemCode='" + itemCode + '\'' +
                ", natsum=" + natsum +
                ", sum=" + sum +
                ", inventorycode='" + inventorycode + '\'' +
                ", quantity=" + quantity +
                ", unitquantity=" + unitquantity +
                ", unitcode='" + unitcode + '\'' +
                ", define23='" + define23 + '\'' +
                ", define24='" + define24 + '\'' +
                ", itemName='" + itemName + '\'' +
                ", define25='" + define25 + '\'' +
                ", define26='" + define26 + '\'' +
                ", define22='" + define22 + '\'' +
                ", quotedprice='" + quotedprice + '\'' +
                ", money=" + money +
                ", assistantunit='" + assistantunit + '\'' +
                ", natunitprice=" + natunitprice +
                ", unitprice=" + unitprice +
                ", bmemo='" + bmemo + '\'' +
                ", taxcost=" + taxcost +
                '}';
    }

}
