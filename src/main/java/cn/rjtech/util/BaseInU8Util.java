package cn.rjtech.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.hutool.http.HttpUtil;
import cn.rjtech.config.AppConfig;

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
    public String base_in(String json){
        String vouchSumbmitUrl = AppConfig.getVouchProcessDynamicSubmitUrl();
        System.out.println("----------推单前---------------");
        System.out.println(json);
        System.out.println("------------------------------");
        System.out.println(vouchSumbmitUrl);
        System.out.println("----------推单前---------------");

        String post = HttpUtil.post(vouchSumbmitUrl, json);
        String post2 = post;

        System.out.println("----------------post返回信息-------------------");
        System.out.println(post2);
        System.out.println("----------------post返回信息-------------------");

        JSONObject res = JSON.parseObject(post);
        ValidationUtils.notNull(res, "解析JSON为空");

        String code = res.getString("code");
        ValidationUtils.notNull(code, json + ";" + post);
        JSONObject data = (JSONObject)res.get("data");
        String message = data.getString("message");
        if (message == null) {
            message = res.getString("msg");
            if(message == null){
                message=post;
            }
        }

        ValidationUtils.notNull(code, json + ";" + message);
        ValidationUtils.equals(code, "200",  json + ";" + message);
        if(code.equals("200")){
            System.out.println("----------推单成功-------------");
            System.out.println(json);
            System.out.println("------------------------------");
            System.out.println(message);
            System.out.println("----------推单成功-------------");
        }

        System.out.println("----------inBase:return-------------");
        System.out.println(post);
        System.out.println("----------inBase:return-------------");
        return post;
    }

}
