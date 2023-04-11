package cn.rjtech.wms.utils;

import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * @Author: lidehui
 * @Date: 2023/1/31 15:53
 * @Version: 1.0
 * @Desc:
 */
public class WebService {
    /**
     * U9APl Web服务
     * @param tag 类型
     * @param openAPIs openAPI表的数据
     * @param params json数据
     * @return
     */
    public static String ApiWebService(String tag, List<Map<String,String>> openAPIs, JSONObject params) throws RuntimeException {

        //遍历openAPI表的数据
        for(Map record : openAPIs){
            Map data= MapToLowerCase.mapToLowerCaseToMap(record);
            //判断类型是否一样
            if(tag.equals(data.get("tag"))){
                //获取api的url
                String url = String.valueOf(data.get("url"));
                //判断url是否为空
                if(url == null || "".equals(url)){
                    throw new RuntimeException(tag+":没有配置URL");
                }
                System.out.println(params);
                //执行接口
                return  HttpApiUtils.invokeApi(url, params);
            }
        }
        throw new RuntimeException("没有找到'"+tag+"'的URL");
    }



    public static String ApiWebService(String URl, JSONObject params) throws RuntimeException {
        return  HttpApiUtils.invokeApi(URl, params);
    }

    public static String ApiWebService(String url, Map params, Map<String, String> head) throws RuntimeException {
        return HttpApiUtils.httpHutoolPost(url, params, head);
    }
}
