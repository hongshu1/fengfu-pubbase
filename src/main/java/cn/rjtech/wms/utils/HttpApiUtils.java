package cn.rjtech.wms.utils;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.beust.jcommander.ParameterException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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

    public static String u8Api(String apiUrl,JSONObject jsonData) throws IOException {
        //根据地址获取空间名称
        String soapActionLength=apiUrl.substring(0,apiUrl.indexOf("="));
        String soapAction=apiUrl.substring(soapActionLength.length()+1,apiUrl.length());
        //生单或审核数据
        String dataJson="";
        String result = "";
        //命名空间
        String soapActionString = "http://tempuri.org/"+soapAction;
        URL url = new URL(apiUrl);//地址设置
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        //拼接请求体,此处也可以在外面写xml文件然后读取,但是为了方便一个文件搞定,而且参数也比较好传递直接String拼接
        String soap=null;
        //根据地址判断传参格式
        if("Add".equals(apiUrl.substring(apiUrl.length()-3))||"V1".equals(apiUrl.substring(apiUrl.length()-2))){
            dataJson=jsonData.toString().replaceAll(" ","");
            soap = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                    "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                    "  <soap:Body>\n" +
                    "    <"+soapAction+" xmlns=\"http://tempuri.org/\">\n" +
                    "      <dataJson>"+dataJson+"</dataJson>\n" +
                    "    </"+soapAction+">\n" +
                    "  </soap:Body>\n" +
                    "</soap:Envelope>";
        }else{
            JSONObject jsonObject= (JSONObject) jsonData.get("data");
            soap="<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                    "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                    "  <soap:Body>\n" +
                    "    <"+soapAction+" xmlns=\"http://tempuri.org/\">\n" +
                    "      <VouchId>"+jsonObject.get("VouchId")+"</VouchId>\n" +
                    "      <accid>"+jsonObject.get("accid")+"</accid>\n" +
                    "    </"+soapAction+">\n" +
                    "  </soap:Body>\n" +
                    "</soap:Envelope>";
        }
        byte[] buf = soap.getBytes("UTF-8"); //20220520 潘家宝添加UTF-8 参数解决U8生单乱码问题
        //设置一些头参数
        httpConn.setRequestProperty("Content-Length", String.valueOf(buf.length));
        httpConn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
        httpConn.setRequestProperty("soapActionString", soapActionString);
        httpConn.setRequestMethod("POST");
        //输入参数和输出结果
        httpConn.setDoOutput(true);
        httpConn.setDoInput(true);
        OutputStream out = httpConn.getOutputStream();
        out.write(buf);
        out.close();
        //最后合格解析结果大家就各显神通了,此处打印出解析的过程,最终得到翻译答案
//        byte[]  = readInputStream(httpConn.getInputStream());
        result = readInputStream(httpConn.getInputStream());

//        result = new String(datas);
//        System.out.println(datas);
        System.out.println("result:" + result);
        result = result.substring(result.indexOf("{"), result.lastIndexOf("}") + 1);
//		System.out.println(result.substring(result.indexOf("string") - 1,result.lastIndexOf("string") + 7).replaceAll("</{0,1}(string)?>",""));
//		result = getXmlString(result);
//		System.out.println(result);
        System.out.println(result);
        return result;
    }

    /**
     * 从输入流中读取数据
     *
     * @param inStream
     * @return
     * @throws Exception
     */
    public static String readInputStream(InputStream inStream) throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        String strResult="";
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
            strResult+=new String(buffer,"utf-8");
        }
        byte[] data = outStream.toByteArray();
        outStream.close();
        inStream.close();
//        return data;
        return strResult;
    }
}
