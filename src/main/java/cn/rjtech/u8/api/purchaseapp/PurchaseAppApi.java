package cn.rjtech.u8.api.purchaseapp;

import cn.jbolt.core.util.JBoltDateUtil;
import cn.rjtech.model.momdata.Purchased;
import cn.rjtech.model.momdata.Purchasem;
import cn.rjtech.u8.api.RemoteInvoker;
import cn.rjtech.u8.enums.ProcTypeEnum;
import cn.rjtech.u8.pojo.req.purchaseapp.PurchaseAppBody;
import cn.rjtech.u8.pojo.req.purchaseapp.PurchaseAppEntry;
import cn.rjtech.u8.pojo.req.purchaseapp.PurchaseAppHeader;
import cn.rjtech.u8.pojo.req.purchaseapp.PurchaseAppReq;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * 采购申请单API
 *
 * @author Kephon
 */
public class PurchaseAppApi {

    private PurchaseAppApi() {
    }

    public static JSONObject add(String orgCode, PurchaseAppReq req) {
        return RemoteInvoker.invoke(orgCode, ProcTypeEnum.ADD.getText(), req);
    }

    /**
     * 创建成品申购单
     */
    public static String add(String orgCode, Purchasem purchasem, List<Purchased> purchaseds) {
        PurchaseAppReq purchaseAppReq = new PurchaseAppReq(orgCode);
        // 构建主表信息
        PurchaseAppHeader handle = new PurchaseAppHeader();
        // 单据号 非空、唯一
        handle.setCode(purchasem.getCPurchaseNo());
        // 单据日期 非空
        handle.setDate(JBoltDateUtil.format(purchasem.getCPurchaseDate(), JBoltDateUtil.YMD));
        // 部门编号 可空
        handle.setDepartmentcode(purchasem.get("cenddepcode"));
        // 职员编号 可空
        handle.setPersoncode(purchasem.getCPersonCode());
        // 采购类型 可空
        handle.setPurchasetypecode(purchasem.getIPurchaseType());
        // 业务类型 非空
        handle.setBusinesstype(purchasem.getCServiceTypeName());
        // 	制单人 非空
        handle.setMaker(purchasem.get("createname"));
        // 自定义字段 可空
        handle.setDefine1(null);
        handle.setDefine2(null);
        handle.setDefine3(null);
        handle.setDefine4(null);
        handle.setDefine5(null);
        handle.setDefine6(null);
        handle.setDefine7(null);
        handle.setDefine8(purchasem.getIsPurchased() ? "是" : "否");
        handle.setDefine9(null);
        handle.setDefine10(null);
        handle.setDefine11(null);
        handle.setDefine12(null);
        handle.setDefine13(null);
        handle.setDefine14(null);
        handle.setDefine15(null);
        handle.setDefine16(null);
        // 备注 可空
        handle.setMemory(null);

        // 构建细表信息
        PurchaseAppBody body = new PurchaseAppBody();
        List<PurchaseAppEntry> entryList = new ArrayList<>();
        // 原币合计含税总金额
        String totalMoney = purchaseds.stream().map(Purchased::getItotalamount).reduce(BigDecimal.ZERO, BigDecimal::add).toString();
        for (Purchased purchased : purchaseds) {
            PurchaseAppEntry entry = new PurchaseAppEntry();
            // 供应商编号 非空
            entry.setVendorcode(purchased.getCvencode());
            // 存货编码 非空
            entry.setInventorycode(purchased.getCinvcode());
            // 主计量数量 非空
            entry.setQuantity(BigDecimal.valueOf(purchased.getIquantity()));
            // 无税单价（本币） 可空
            entry.setPrice(purchased.getInatunitprice());
            // 含税单价（本币） 可空; 含税单价 = 无税单价 * (1 + 税率)
            BigDecimal taxprice = purchased.getInatunitprice().multiply(BigDecimal.ONE.add(purchased.getItaxrate().divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP)));
            entry.setTaxprice(taxprice.toString());
            // 金额（本币） 可空        取不含税总金额
            entry.setMoney(purchased.getInatmoney());
            // 价税合计（本币） 可空    取含税总金额
            entry.setSum(purchased.getInatsum().toString());
            // 税率 可空              U8税率 = 税率 * 100
            entry.setTaxrate(purchased.getItaxrate().toString());
            // 税额（本币） 可空; 税额 = 金额 * 税率
            entry.setTax(purchased.getInattax().toString());
            // 需求日期 非空
            entry.setRequiredate(JBoltDateUtil.format(purchased.getDdemandate(), JBoltDateUtil.YMD));
            // 建议订货日期 可空
            entry.setArrivedate(null);
            // 项目大类 可空
            entry.setItemClass(null);
            // 项目 可空
            entry.setItemCode(null);
            // 项目名称 可空
            entry.setItemName(null);
            // 单价标准 可空
            entry.setBtaxcost(null);
            // 件数 可空
            entry.setNum(null);
            // 单位编码 可空
            entry.setUnitid(null);
            // 执行部门编号 可空
            entry.setDeptcodeexec(null);
            // 执行业务员	可空
            entry.setPersoncodeexec(null);
            // 外币名称 非空
            entry.setCurrencyName(purchased.getCcurrency());
            // 汇率 非空
            BigDecimal nflat = purchased.getNflat();
            entry.setCurrencyRate(nflat);
            // 无税单价（原币） 可空
            entry.setOriginalprice(purchased.getIprice().toString());
            //含税单价（原币） 可空 : 无税单价 * (1+税率)
            BigDecimal originaltaxedprice = purchased.getIprice().multiply(BigDecimal.ONE.add(purchased.getItaxrate().divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP)));
            entry.setOriginaltaxedprice(originaltaxedprice.toString());
            // 无税金额（原币） 可空
            entry.setOriginalmoney(purchased.getItaxexclusivetotalamount().toString());
            // 税额（原币） 可空
            entry.setOriginaltax(purchased.getItax().toString());
            // 价税合计（原币） 可空
            entry.setOriginalsum(nflat.multiply(purchased.getItotalamount()).toString());
            // 行号 可空
            //entry.setIvouchrowno(0);
            // 自定义项 可空
            entry.setDefine22(null);
            entry.setDefine23(purchasem.getCDepCode());
            entry.setDefine24(purchasem.getCPurchaseNo());
            entry.setDefine25(purchased.getCreferencepurpose());
            entry.setDefine26(null);
            entry.setDefine27(null);
            entry.setDefine28(null);
            entry.setDefine29(null);
            entry.setDefine30(purchased.getIautoid().toString());
            entry.setDefine31(purchased.getCmemo());
            entry.setDefine32(purchased.get("cbudgetno"));
            entry.setDefine33(purchased.get("cdepname"));
            entry.setDefine34(totalMoney);
            entry.setDefine35(null);
            entry.setDefine36(null);
            entry.setDefine37(null);
            // 自由项 可空
            entry.setFree1(null);
            entry.setFree2(null);
            entry.setFree3(null);
            entry.setFree4(null);
            entry.setFree5(null);
            entry.setFree6(null);
            entry.setFree7(null);
            entry.setFree8(null);
            entry.setFree9(null);
            entry.setFree10(null);
            entryList.add(entry);
        }

        // 封装数据
        body.setEntry(entryList);
        purchaseAppReq.setHeader(handle);
        purchaseAppReq.setBody(body);

        // 获取响应数据
        JSONObject invoke = RemoteInvoker.invoke(purchasem.getCOrgCode(), ProcTypeEnum.ADD.getText(), purchaseAppReq);
        JSONObject ufinterface = invoke.getJSONObject("ufinterface");
        JSONObject item = ufinterface.getJSONObject("item");
        return item.getString("dsc");
    }

}
