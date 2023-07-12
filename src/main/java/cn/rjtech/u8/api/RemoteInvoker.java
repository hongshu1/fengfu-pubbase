package cn.rjtech.u8.api;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import cn.rjtech.u8.constants.U8ApiConstants;
import cn.rjtech.u8.pojo.req.BaseReq;
import cn.rjtech.u8.pojo.req.BaseReqData;
import cn.rjtech.u8.pojo.resp.U8ApiResp;
import cn.rjtech.util.ValidationUtils;
import cn.rjtech.util.xml.XmlUtil;
import com.alibaba.fastjson.JSON;
import com.beust.jcommander.ParameterException;
import com.jfinal.kit.Kv;
import com.jfinal.log.Log;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;

import java.util.ArrayList;
import java.util.List;

import static cn.rjtech.u8.constants.U8ApiConstants.*;

/**
 * 远程调用
 *
 * @author Kephon
 */
public class RemoteInvoker {

    private static final Log LOG = Log.getLog(RemoteInvoker.class);

    private RemoteInvoker() {
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static <T extends U8ApiResp> T invoke(String url, BaseReqData data) {

        String body = "dataJson=" + JSONUtil.parse(Kv.by("data", data));
        LOG.info("URL: {}, body: {}", url, body);

        String result = HttpUtil.post(url, body);
        LOG.info("Result: {}", result);

        String res = XmlUtil.readObjectFromXml(result);

        T o;

        try {
            o = (T) JSON.parseObject(res, data.getResponseClass());
            LOG.info("{}", o);
        } catch (Exception e) {
            throw new ParameterException("解析U8 API响应结果失败");
        }

        ValidationUtils.notNull(o, "请求失败");
        ValidationUtils.isTrue("200".equals(o.getCode()), StrUtil.replaceChars(o.getMessage().trim(), "\n", StrUtil.EMPTY));

        return o;
    }

    /**
     * 获取U8服务统一请求地址
     *
     * @param orgCode 组织编码
     * @return 请求URL
     */
    private static String getUnifiedServiceUrl(String orgCode) {
        return U8ApiConstants.getU8ServiceUrl(orgCode);
    }

    /**
     * 解析U8请求结果
     *
     * @param res 响应xml
     * @return 请求结果
     */
    private static JSONObject parseResp(String res) {
        String xml = res.replaceAll("\r\n", StrUtil.EMPTY).replaceAll("\\\\", StrUtil.EMPTY).replaceAll("\"", "'");
        JSONObject o = XML.toJSONObject(xml);
        LOG.warn("解析结果: {}", o);
        ValidationUtils.notNull(o, "请求失败");
        ValidationUtils.isTrue(o.length() > 0, " 解析U8 API响应结果失败");
        JSONObject ufinterface = o.getJSONObject(UF_INTERFACE);
        Object item = ufinterface.get(ITEM);
        if (item instanceof JSONObject) {
            JSONObject itemObj = (JSONObject) item;
            ValidationUtils.isTrue(CODE_SUCCEED == itemObj.getInt(STATE), itemObj.getString(DSC).trim());
            return o;
        }

        Kv ret = getRet(item);
        LOG.error("ret: {}", ret);
        ValidationUtils.isTrue(CODE_SUCCEED == ret.getInt(STATE), ret.getStr(DSC));
        return o;
    }

    private static Kv getRet(Object item) {
        ValidationUtils.isTrue(item instanceof JSONArray, "参数格式不合法");

        JSONArray jsonArr = (JSONArray) item;

        int succeed = 0;

        List<String> errMsg = new ArrayList<>();

        for (Object value : jsonArr) {
            JSONObject itemObj = (JSONObject) value;
            if (0 != itemObj.getInt(STATE)) {
                // 只要有1条记录失败，则设为失败
                if (succeed == CODE_SUCCEED) {
                    succeed = CODE_FAILED;
                }
                // 追加错误信息
                errMsg.add(itemObj.getString(DSC).trim());
            }
        }

        return Kv.by(STATE, succeed).set(DSC, CollUtil.join(errMsg, StrUtil.COMMA));
    }

    public static JSONObject invoke(String orgCode, String proc, BaseReq req) {
        String url = getUnifiedServiceUrl(orgCode);

        String requestXml = req.getRequestXml(orgCode, proc);
        LOG.info("URL: {} \n{} \n{}", url, requestXml, XML.toJSONObject(requestXml));
        String result = HttpUtil.post(url, requestXml.trim());
        LOG.info("result：{}", result);

        ValidationUtils.notBlank(result, "U8接口无响应");
        return parseResp(result);
    }

}
