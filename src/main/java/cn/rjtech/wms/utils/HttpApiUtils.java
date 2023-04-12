package cn.rjtech.wms.utils;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.beust.jcommander.ParameterException;

import java.util.Map;

/**
 * @Author: lidehui
 * @Date: 2023/1/31 15:54
 * @Version: 1.0
 * @Desc:
 */
public class HttpApiUtils {
    /**
     * u8单据Api
     *
     * @param url    执行的url加方法名称
     * @param params 接口数据
     * @return String 返回数据
     */
    public static JSONObject invoke(String url, JSONObject params) {
        String result = HttpUtil.post(url, params.toString());
        JSONObject jsonObject = JSONObject.parseObject(result);
        try {
            JSONArray is = JSONObject.parseObject(result).getJSONArray("d");
            for (int i = 0; i < is.size(); i++) {
                JSONObject a = is.getJSONObject(i);
                if (a.getBoolean("IsSuccess")) {
                    throw new ParameterException(a.getString("Msg"));
                }
            }
        } catch (Exception e) {
            throw new ParameterException("API响应结果失败");
        }
        return jsonObject;
    }

    public static String invokeApi(String url, JSONObject params){
        return HttpUtil.post(url, params.toString());
    }

    public static String httpHutoolPost(String url, Map param, Map<String, String> map){
        String body = JSONObject.toJSONString(param);
        //System.out.println(body);
        //Restful请求
        String object = HttpRequest.post(url).body(body).addHeaders(map).execute().body();
        return object;
    }
}
