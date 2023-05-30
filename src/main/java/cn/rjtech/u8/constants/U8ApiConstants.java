package cn.rjtech.u8.constants;

import cn.jbolt.core.base.config.JBoltConfig;
import cn.rjtech.u8.pojo.req.purchaseapp.PurchaseAppReq;
import cn.rjtech.u8.pojo.req.purchaseorder.PurchaseorderReq;
import cn.rjtech.util.xml.JaxbUtil;

import java.nio.charset.StandardCharsets;

/**
 * U8接口常量
 *
 * @author Kephon
 */
public class U8ApiConstants {

    public static final int CODE_SUCCEED = 0;
    public static final int CODE_FAILED = 1;

    public static final String STATE = "succeed";
    public static final String DSC = "dsc";

    public static final String UF_INTERFACE = "ufinterface";
    public static final String ITEM = "item";
    public static final String U8_KEY = "u8key";

    public static final String USERNAME = "demo";

    /**
     * U8 API 访问地址
     */
    static String BASE = JBoltConfig.prop.get("u8.api.url");
    /**
     * 成品入库
     */
    public static String PRODUCT_IN_ADD_URL = BASE + "/StockAPI.asmx/ProductInAdd";
    /**
     * 材料出库
     */
    public static String MATERIAL_OUT_ADD_URL = BASE + "/StockAPI.asmx/MaterialOutAdd";

    private U8ApiConstants() {
    }

    public static final String XML_END = "</ufinterface>";

    /**
     * U8接口请求地址参数
     */
    public static final String U8_URL = JBoltConfig.prop.get("u8.service.url") + "/api/moorder/PostMOData";

    public static String getU8ServiceUrl(String orgCode) {
        return String.format("%s?accId=%s", U8_URL, orgCode);
    }

    /**
     * 获取xml头部信息
     *
     * @param orgCode 组织编码
     * @param proc    操作类型，参考ProcTypeEnum
     * @return xml头部信息
     */
    private static String getXmlHead(String orgCode, String roottag, String proc, String display, String family) {
        return String.format("<?xml version=\"1.0\" encoding=\"utf-8\"?><ufinterface sender=\"%s\" receiver=\"u8\" roottag=\"%s\" docid=\"\" proc=\"%s\" codeexchanged=\"N\" exportneedexch=\"N\" paginate=\"0\" \n" +
                "display=\"%s\" family=\"%s\" timestamp=\"0x000000000027B6EB\">", orgCode, roottag, proc, display, family);
    }

    // -----------------------------------------------------
    // 根据业务场景获取xml头部信息
    // -----------------------------------------------------

    /**
     * 采购请购单xml头部信息
     *
     * @param orgCode 组织编码
     * @param proc    操作类型，参考ProcTypeEnum
     * @return 销售订单xml头部信息
     */
    private static String getPurchaseAppXmlHead(String orgCode, String proc) {
        return getXmlHead(orgCode, "purchaseapp", proc, "采购请购单", "采购请购单");
    }

    /**
     * 采购订单xml头部信息
     *
     * @param orgCode 组织编码
     * @param proc    操作类型，参考ProcTypeEnum
     * @return 销售订单xml头部信息
     */
    private static String getPurchaseOrderXmlHead(String orgCode, String proc) {
        return getXmlHead(orgCode, "purchaseorder", proc, "采购订单", "采购订单");
    }

    /**
     * 采购请购单xml请求报文
     *
     * @param orgCode 组织编码
     * @param proc    操作类型
     * @param req     采购申请单报文对象
     * @return 采购申请单xml请求报文
     */
    public static String getPurchaseAppRequestXml(String orgCode, String proc, PurchaseAppReq req) {
        return new JaxbUtil<>(req).toXml(req, StandardCharsets.UTF_8.name(), getPurchaseAppXmlHead(orgCode, proc), XML_END);
    }

    /**
     * 采购订单xml请求报文
     *
     * @param orgCode 组织编码
     * @param proc    操作类型
     * @param req     采购申请单报文对象
     * @return 采购申请单xml请求报文
     */
    public static String getPurchaseOrderRequestXml(String orgCode, String proc, PurchaseorderReq req) {
        return new JaxbUtil<>(req).toXml(req, StandardCharsets.UTF_8.name(), getPurchaseOrderXmlHead(orgCode, proc), XML_END);
    }

}
