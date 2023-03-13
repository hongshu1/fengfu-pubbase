package cn.rjtech.u9;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.kit.JsonKit;
import com.jfinal.log.Log;

public class RemoteInvoker {

    private static final Log LOG = Log.getLog(RemoteInvoker.class);

    public static U9APIResult invoke(String url, String data) {
        LOG.info("URL: {}, body: {}", url, data);

        String result = HttpUtil.post(url, data);
        LOG.info("Result: {}", result);

        ValidationUtils.notBlank(result, "U9接口无响应");

        ValidationUtils.isTrue(!result.contains("Error"), result);

        JSONObject json = (JSONObject) JSONUtil.parse(result);

        return JsonKit.parse(json.getStr("d"), U9APIResult.class);
    }

    public static JSONObject invokeGet(String url) {
        LOG.info("URL: {}", url);

        String result = HttpUtil.get(url, 8000);
        LOG.info("Result: {}", result);

        return (JSONObject) JSONUtil.parse(result);
    }

}
