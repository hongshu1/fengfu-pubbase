package cn.rjtech.util;

import cn.hutool.http.HttpUtil;
import cn.rjtech.config.AppConfig;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * @version 1.0
 * @Author cc
 * @Create 2023/5/15 10:08
 * @Description 推送采购入库单到U8系统
 */
public class BaseInU8Util {

    /**
     * 推送采购入库单到U8系统
     */
    public String base_in(String json) {
        String vouchSumbmitUrl = AppConfig.getVouchProcessDynamicSubmitUrl();
        String post = HttpUtil.post(vouchSumbmitUrl, json);
        JSONObject res = JSON.parseObject(post);
        ValidationUtils.notNull(res, "解析JSON为空");

        String code = res.getString("code");
        String message = res.getString("message");
        if (message == null) {
            message = res.getString("msg");
        }
        ValidationUtils.notNull(code, "json:" + json + ";" + message);
        ValidationUtils.equals(code, "200", code + ";" + "json:" + json + ";" + message);
        return post;
    }

    /*
     * 通知U8删除采购入库单
     * */
    public String deleteVouchProcessDynamicSubmitUrl(String json) {
        String vouchSumbmitUrl = AppConfig.deleteVouchProcessDynamicSubmitUrl();
        String post = HttpUtil.post(vouchSumbmitUrl, json);
        JSONObject res = JSON.parseObject(post);

        ValidationUtils.notNull(res, "解析JSON为空");

        String code = res.getString("code");
        String message = res.getString("message");
        if (message == null) {
            message = res.getString("msg");
        }
        ValidationUtils.notNull(code, "json:" + json + ";" + message);
        ValidationUtils.equals(code, "200", code + ";" + "json:" + json + ";" + message);
        return post;
    }

}
