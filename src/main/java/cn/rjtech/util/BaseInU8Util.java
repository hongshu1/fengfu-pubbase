package cn.rjtech.util;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.rjtech.config.AppConfig;
import cn.rjtech.util.xml.XmlUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.log.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @version 1.0
 * @Author cc
 * @Create 2023/5/15 10:08
 * @Description 推送采购入库单到U8系统
 */
public class BaseInU8Util {

    private static final Log LOG = Log.getLog(BaseInU8Util.class);

    /**
     * 推送采购入库单到U8系统
     */
    public String base_in(String json) {
        String vouchSumbmitUrl = AppConfig.getVouchSubmitUrl();
        String post = HttpUtil.post(vouchSumbmitUrl, json);
        JSONObject res = JSON.parseObject(post);

        ValidationUtils.notNull(res, "解析JSON为空");
        LOG.info("res: {}", res);

        String code = res.getString("code");
        String message = StrUtil.nullToDefault(res.getString("message"), res.getString("msg"));

        if (ObjUtil.equal(res.getString("state"), "fail")) {
            ValidationUtils.error(message);
        }
        ValidationUtils.notNull(code, "json:" + json + ";" + message);
        ValidationUtils.equals(code, "200", code + ";" + "json:" + json + ";" + message);

        JSONObject dataJson = JSON.parseObject(res.getString("data"));
        String u8Billno = "";
        if ("200".equals(dataJson.getString("code"))) {
            u8Billno = extractU8Billno(dataJson.getString("message").trim());
        }
        return u8Billno;
    }

    /*
     * 通知U8删除采购入库单
     * */
    public String deleteVouchProcessDynamicSubmitUrl(String json) {
        //1、先调用采购入库弃审接口
        String vouchSumbmitUrl = AppConfig.inUnVouchProcessDynamicSubmitUrl();
        String inUnVouchGet = HttpUtil.get(vouchSumbmitUrl + "?dataJson=" + json);
//        String post = HttpUtil.post(vouchSumbmitUrl, "dataJson=" + json);

        String res = XmlUtil.readObjectFromXml(inUnVouchGet);
        JSONObject jsonObject = JSON.parseObject(res);

        ValidationUtils.notNull(jsonObject, "解析JSON为空");
        String code = jsonObject.getString("code");
        String message = StrUtil.nullToDefault(jsonObject.getString("message"), jsonObject.getString("msg"));
        LOG.info("res: {}", res);

        if (ObjUtil.equal(jsonObject.getString("state"), "fail")) {
            ValidationUtils.error(message);
        }
        ValidationUtils.notNull(code, "json:" + json + ";" + message);
        ValidationUtils.equals(code, "200", message);

        //2、再调用采购入库u8删除接口，删除u8数据
        String deleteUrl = AppConfig.deleteVouchProcessDynamicSubmitUrl();
        String deleteGet = HttpUtil.get(deleteUrl + "?dataJson=" + json);
        String deleteRes = XmlUtil.readObjectFromXml(deleteGet);
        JSONObject deleteJsonObject = JSON.parseObject(deleteRes);

        ValidationUtils.notNull(deleteJsonObject, "解析JSON为空");
        String deleteCode = deleteJsonObject.getString("code");
        String deleteMessage = StrUtil.nullToDefault(deleteJsonObject.getString("message"), deleteJsonObject.getString("msg"));
        LOG.info("deleteRes: {}", deleteRes);

        if (ObjUtil.equal(deleteJsonObject.getString("state"), "fail")) {
            ValidationUtils.error(deleteMessage);
        }
        ValidationUtils.notNull(deleteCode, "json:" + json + ";" + deleteMessage);
        ValidationUtils.equals(deleteCode, "200", deleteMessage);

        return code;
    }

    /**
     * 提取字符串里面的数字
     */
    public String extractU8Billno(String message) {
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(message);
        return m.replaceAll("").trim();
    }

}
