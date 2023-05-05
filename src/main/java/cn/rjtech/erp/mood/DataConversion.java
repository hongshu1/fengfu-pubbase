package cn.rjtech.erp.mood;

import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.util.JBoltStringUtil;
import cn.rjtech.common.columsmap.ColumsmapService;
import cn.rjtech.common.columsmapdetail.ColumsmapdetailService;
import cn.rjtech.common.model.ExchangeTable;
import cn.rjtech.common.model.Log;
import cn.rjtech.util.DateUtils;
import cn.rjtech.wms.utils.MapToLowerCase;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jfinal.plugin.activerecord.Record;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class DataConversion {

    ColumsmapdetailService columsmapdetailService;
    ColumsmapService columsmapService;

    private Map<String, Object> PreAllocate;//预分数据
    private Map<String, List<Record>> map;//数据集Map

    public DataConversion (ColumsmapService columsmapService, ColumsmapdetailService columsmapdetailService){
        this.columsmapService = columsmapService;
        this.columsmapdetailService = columsmapdetailService;
    }

    /**
     * String转List
     * @param json
     * @return
     */
    public static List<Map<String, Object>> toListMap(String json){
        List<Object> list = JSON.parseArray(json);

        List< Map<String,Object>> listw = new ArrayList<Map<String,Object>>();
        for (Object object : list){
            Map<String,Object> ageMap = new HashMap<String,Object>();
            Map <String,Object> ret = (Map<String, Object>) object;//取出list里面的值转为map
        /*for (Entry<String, Object> entry : ret.entrySet()) {
            ageMap.put(entry.getKey());
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
            listw.add(ageMap);  //添加到list集合  成为 list<map<String,Object>> 集合
        }  */
            listw.add(ret);
        }
        return listw;

    }

    /**
     * 前端转换要生成的数据
     *
     * @param itemCode     项目编码
     * @param preAllocate  预置项
     * @param JSONMainData 主数据
     * @param detailData   详细数据
     * @param extData      扩展数据
     * @return
     */
    public List<ExchangeTable> getTSysExchangetable(String itemCode, Map preAllocate, JSONArray JSONMainData, JSONArray detailData, JSONArray extData) throws RuntimeException {
        String exchangeID = String.valueOf(JBoltSnowflakeKit.me.nextId());
        List<Record> list = columsmapdetailService.getColumsmapdetailList(preAllocate.get("InitializeMapID").toString(), "");
        //创建返回的list
        List<ExchangeTable> result = new ArrayList<>();
        for (int i = 0; i < JSONMainData.size(); i++) {
            ExchangeTable exchangetable = new ExchangeTable();
            JSONObject curItem = JSONMainData.getJSONObject(i);
            exchangetable.setExchangeID(exchangeID);//数据交换ID
            exchangetable.setProcessType(preAllocate.get("ProcessType")==null?"":preAllocate.get("ProcessType").toString());
            Field[] fields = ExchangeTable.class.getFields();
            for (Record columsmapdetail : list) {
                String value = "";
                String sourcecolumn = columsmapdetail.getStr("Sourcecolumn");
                String defaultvalue = columsmapdetail.getStr("Defaultvalue");
                String sourceflag = columsmapdetail.getStr("Sourceflag");
                String tagcolumn = columsmapdetail.getStr("Tagcolumn");
                if (sourcecolumn == null) {
                    value = defaultvalue;
                } else {
                    if ("PreAllocate".equals(sourceflag)) {
                        value = getCorrespondingValue(preAllocate, sourcecolumn, sourceflag, defaultvalue, null);
                    } else {
                        value = getCorrespondingValue(curItem, sourcecolumn, sourceflag, defaultvalue, null);
                    }
                }
                System.out.println(tagcolumn);
                if(null != value && !"".equals(value)){
                    for(Field field : fields){
                        if (field.getName().toLowerCase().equals(tagcolumn.toLowerCase())){
                            //String type = field.getGenericType().toString();
                            setFieldValueByFieldName(field.getName(), exchangetable, value);
                            break;
                        }
                    }
                }

            }
            result.add(exchangetable);
        }
        return result;
    }

    /**
     * @param map         数据
     * @param key         数据key
     * @param dataSetName 数据集名称
     * @return
     */
    private String getCorrespondingValue(Map map, String key, String dataSetName, String Defaultvalue, JsonArray bodys) throws RuntimeException {
        String s = "";
        Map map1 = MapToLowerCase.mapToLowerCaseToMap(map);
        String key1 = key.toLowerCase();
        if (map1 != null) {
            //判断数据集是否没有对应的值
            if (map1.containsKey(key1) && map1.get(key1) != null) {
                if(map1.get(key1).toString().contains(",") && (bodys == null || bodys.size() == 0) && "PreAllocate".equals(dataSetName)){
                    s = map1.get(key1).toString().split(",")[0];
                }else{
                    if(bodys != null && bodys.size() > 0){
                        String[] valArr = map1.get(key1).toString().split(",");
                        for (int j = 0; j < valArr.length; j++) {
                            if (!bodys.toString().contains(valArr[j])){
                                s = valArr[j];
                            }
                        }

                    }else{
                        s = map1.get(key1).toString();
                    }
                }
            } else {
                //判断有没有缺省值
                if (Defaultvalue != null) {
                    s = Defaultvalue;
                }
//                else {
//                    throw new RuntimeException(dataSetName + "数据集没有" + key);
//                }
            }
        }
        return s;
    }

    /**
     * 根据属性名设置属性值
     *
     * @param fieldName
     * @param object
     * @return
     */
    public boolean setFieldValueByFieldName(String fieldName, Object object,String value) {
        try {
            // 获取obj类的字节文件对象
            Class c = object.getClass();
            // 获取该类的成员变量
            Field f = c.getField(fieldName);
            // 取消语言访问检查
            f.setAccessible(true);

            char[] chars = f.get(null).toString().toCharArray();
            if (Character.isLowerCase(chars[0])){
                chars[0] -= 32;
            }
            Type returnType = c.getMethod("get" + String.valueOf(chars)).getGenericReturnType(); //get方法的返回值类型
            String type = returnType.getTypeName();

            if(type.endsWith("Double")){
                // 给变量赋值
                Method method = c.getMethod("set" + String.valueOf(chars), Double.class);
                method.invoke(object, Double.valueOf(value));
            }else if(type.endsWith("Date")){
                // 给变量赋值
                Method method = c.getMethod("set" + String.valueOf(chars), Date.class);
                method.invoke(object, DateUtils.parseDate(value));
            }else if (type.endsWith("Integer")){
                // 给变量赋值
                Method method = c.getMethod("set" + String.valueOf(chars), Integer.class);
                method.invoke(object, Integer.valueOf(value));
            }else if (type.endsWith("Long")){
                // 给变量赋值
                Method method = c.getMethod("set" + String.valueOf(chars), Long.class);
                method.invoke(object, Long.valueOf(value));
            }else if (type.endsWith("BigDecimal")){
                // 给变量赋值
                Method method = c.getMethod("set" + String.valueOf(chars), BigDecimal.class);
                method.invoke(object, new BigDecimal(value));
            }else {
                // 给变量赋值
                Method method = c.getMethod("set" + String.valueOf(chars), String.class);
                method.invoke(object, value);
                //f.set(object, value);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 日志纪录
     * @param organizecode
     * @param vouchbusinessid
     * @param vouchtype
     * @param vouchstep
     * @param source
     * @param action
     * @param memo
     * @param tsysLog
     * @return
     */
    public Log recordLog(String organizecode, String vouchbusinessid, String vouchtype, String vouchstep, String sourceexchangeid, String resultexchangeid, int state, String source, String result, String action, String memo, String createPersom, Log tsysLog){
        tsysLog.setState(state);
        if(null!=organizecode&&!"".equals(organizecode)){
            tsysLog.setOrganizecode(organizecode);
        }
        if(null!=vouchbusinessid&&!"".equals(vouchbusinessid)){
            tsysLog.setVouchbusinessid(vouchbusinessid);
        }
        if(null!=vouchtype&&!"".equals(vouchtype)){
            tsysLog.setVouchtype(vouchtype);
        }
        if(null!=result&&!"".equals(result)){
            tsysLog.setResult(result);
        }
        if(null!=vouchstep&&!"".equals(vouchstep)){
            tsysLog.setVouchstep(vouchstep);
        }
        tsysLog.setSourceexchangeid(sourceexchangeid);
        tsysLog.setResultexchangeid(resultexchangeid);
        tsysLog.setSource(source);
        tsysLog.setAction(action);
        tsysLog.setAutoid(null);
        if(null!=memo&&!"".equals(memo)){
            tsysLog.setMemo(memo);
        }
        if(null!=createPersom&&!"".equals(createPersom)){
            tsysLog.setCreateperson(createPersom);
        }

        return tsysLog;
    }

    /**
     * 将数据转换成json
     *
     * @param resultType    result类型
     * @param PreAllocate 预分数据
     * @param map         数据集Map
     * @return json数据
     */
    public JSONObject InterfaceTransform(String resultType, Map<String, Object> PreAllocate, Map<String, List<Record>> map) {
        this.PreAllocate = PreAllocate;
        this.map = map;
        //josn数据
        JsonObject dataJson = new JsonObject();
        //拼接Json数据
        dataJson = SplicingJsonObject(resultType,PreAllocate.get("ProcessMapID").toString());
        Map params = new HashMap();
        return JSONObject.parseObject(dataJson.toString());
    }

    /**
     * 拼接Json数据
     *
     * @param ProcessMapID 映射ID
     * @return Json数据
     * @throws Exception
     */
    private JsonObject SplicingJsonObject(String resultType, String ProcessMapID) {
        //返回的json数据
        JsonObject dataJson = new JsonObject();
        //查询MapID对应的第一层数据
        List<Record> list = columsmapdetailService.theFirstLayerOfData(ProcessMapID);
        //遍历第一层数据
        for (Record ts : list) {
            //返回拼接好的数据
            String json = getJsonObject(ts.getStr("masid"), ts.getStr("flag"), ts.getStr("flagtype"), ts.getStr("sourceflag"), null);
            // 将json字符串转换成JsonElement
            JsonElement jsonElement = JsonParser.parseString(json);
            //按照标识加入到json离
            dataJson.add(ts.getStr("flag"), jsonElement);
        }
        if("u9erp".equals(resultType.toLowerCase())){
            List<Record> extend = columsmapdetailService.theFirstLayerOfDataExtend(ProcessMapID);
            //遍历第一层数据
            for (Record ts : extend) {
                //返回拼接好的数据
                String json = getJsonObject(ts.getStr("Masid"), ts.getStr("Flag"), ts.getStr("Flagtype"), ts.getStr("Sourceflag"), null);
                Map jsonMap= JSON.parseObject(json, HashMap.class);
                // 将json字符串转换成JsonElement
                for(Object key : jsonMap.keySet()){
                    String value = jsonMap.get(key).toString();
                    System.out.println(key+"  "+value);
                    JsonElement jsonElement = JsonParser.parseString(value);
                    dataJson.add(key.toString(),jsonElement);
                }
            }
        }else {
            List<Record> extend = columsmapdetailService.theFirstLayerOfDataExtend(ProcessMapID);
            //遍历第一层数据
            for (Record ts : extend) {
                //返回拼接好的数据
                String json = getJsonObjectFirst(ts.getStr("Masid"), ts.getStr("Flag"), ts.getStr("Flagtype"), ts.getStr("Sourceflag"), null);
                Map jsonMap= JSON.parseObject(json, HashMap.class);
                // 将json字符串转换成JsonElement
                for(Object key : jsonMap.keySet()){
                    String value = jsonMap.get(key).toString();
                    System.out.println(key+"  "+value);
                    JsonElement jsonElement = JsonParser.parseString(value);
                    dataJson.add(key.toString(),jsonElement);
                }
            }
        }
        return dataJson;
    }

    private String getJsonObjectFirst(String masID, String flag, String flagType, String sourceflag, Map<String,Object> superior) throws RuntimeException {
        //根据父标识和主项目ID查询数据
        List<Record> lsit = columsmapdetailService.theFirstLayerOfDataExtend(masID);
        //要返回的json数据
        JSONObject jsonObject = new JSONObject();
        //转换成String的JSon
        String json = "";

        //判断标识类型
        if ("0".equals(flagType)) {
            //遍历查询出来的数据
            for (Record st : lsit) {
                String tagcolumn = st.getStr("tagcolumn");
                String masid = st.getStr("masid");
                String s1 = st.getStr("flag");
                String flagtype = st.getStr("flagtype");
                String sf = st.getStr("sourceflag");
                String sourcecolumn = st.getStr("sourcecolumn");
                String defaultvalue = st.getStr("defaultvalue");
                //判断目标字段是否为空
                if (JBoltStringUtil.isBlank(tagcolumn)) {
                    //为空用就递归
                    String a = getJsonObject(masid, s1, flagtype, sf, null);
                    //赋值到json
                    jsonObject.put(s1, JSON.parse(a));
                } else {
                    String s = "";
                    //判断来源字段是否为空
                    if (JBoltStringUtil.isBlank(sourcecolumn)) {
                        //为空直接用缺省值
                        s = defaultvalue;
                    } else {
                        //判断用那个数据集
                        if ("PreAllocate".equals(sf)) {
                            s = getCorrespondingValue(PreAllocate, sourcecolumn, sf, defaultvalue, null);
                        } else {
                            //判断数据集是否存在map里
                            if (map.containsKey(sf)) {
                                Map MainData = map.get(sf).get(0).toMap();
                                s = getCorrespondingValue(MainData, sourcecolumn, sf, defaultvalue, null);
                            } else {
                                throw new RuntimeException(sf + "数据集为空");
                            }
                        }
                    }
                    //赋值到json
                    jsonObject.put(tagcolumn, s);
                }
            }
            //把jsonObject转换成String
            json = jsonObject.toString();
        } else {
            //创建Jsonlist
            JsonArray jsonArray = new JsonArray();
            List<Record> lists = map.get(sourceflag);
            Map<String, List<Record>> collect = lists.stream().collect(Collectors.groupingBy(p -> p.getStr("groupflag") == null ? "10" : p.getStr("groupflag"), Collectors.toList()));
            Set<String> groupSeqs = new TreeSet<>(collect.keySet());
            Iterator groupId = groupSeqs.iterator();
            while (groupId.hasNext()){
                List<Record> list = collect.get(groupId.next());
                for (Record record : list) {
                    if (superior != null) {
                        String sbillrowno = record.get("pid").toString().toLowerCase();
                        String rbillrowno = record.get("id").toString().toLowerCase();
                        if (!superior.get(sbillrowno).equals(record.get(rbillrowno))) {
                            continue;
                        }
                    }
                    //list里面的json
                    JsonObject bodyObject = new JsonObject();
                    //遍历查询出来的数据
                    for (Record columsmapdetail : lsit) {
                        String tagcolumn = columsmapdetail.getStr("tagcolumn");
                        String masid = columsmapdetail.getStr("masid");
                        String flag1 = columsmapdetail.getStr("flag");
                        String sf = columsmapdetail.getStr("sourceflag");
                        String sourcecolumn = columsmapdetail.getStr("sourcecolumn");
                        columsmapdetail.getStr("defaultvalue");
                        //判断目标字段是否为空
                        if (JBoltStringUtil.isBlank(tagcolumn)) {
                            //为空用就递归System.out.println(columsmapdetail.getMasid()+"--"+ columsmapdetail.getFlag()+"--"+columsmapdetail.getFlagtype()+"-"+columsmapdetail.getSourceflag()+"-"+record);
                            String obj = getJsonObject(masid, flag1, columsmapdetail.getStr("flagtype"), sf, record.toMap());

                            JsonElement jsonElement = JsonParser.parseString(obj);
                            //赋值到json
                            bodyObject.add(flag1, jsonElement);
                        } else {
                            String s = "";
                            //判断是否直接用缺省值
                            if (JBoltStringUtil.isBlank(sourcecolumn)) {
                                s = columsmapdetail.getStr("defaultvalue");
                            } else {
                                //
                                if ("PreAllocate".equals(sf)) {
                                    s = getCorrespondingValue(PreAllocate, sourcecolumn, sf, columsmapdetail.getStr("defaultvalue"), jsonArray);
                                } else {
                                    s = getCorrespondingValue(record.toMap(), sourcecolumn, sf, columsmapdetail.getStr("defaultvalue"), null);
                                }
                            }
                            //赋值到json
                            bodyObject.addProperty(tagcolumn, s);
                        }
                    }
                    //加入到jsonlist里
                    jsonArray.add(bodyObject);
                }
            }
            json = jsonArray.toString();
        }
        //返回json
        return json;
    }


    private String getJsonObject(String masID, String flag, String flagType, String sourceflag, Map<String,Object> superior) throws RuntimeException {
        //根据父标识和主项目ID查询数据
        List<Record> lsit = null;
        if(JBoltStringUtil.isBlank(flag)){
            lsit = columsmapdetailService.theFirstLayerOfDataExtend(masID);
        }else {
            lsit = columsmapdetailService.queryLowerLevelData(masID, flag);
        }
        //要返回的json数据
        JSONObject jsonObject = new JSONObject();
        //转换成String的JSon
        String json = "";

        //判断标识类型
        if ("0".equals(flagType)) {
            //遍历查询出来的数据
            for (Record st : lsit) {
                String tagcolumn = st.getStr("tagcolumn");
                String masid = st.getStr("masid");
                String s1 = st.getStr("flag");
                String flagtype = st.getStr("flagtype");
                String sf = st.getStr("sourceflag");
                String sourcecolumn = st.getStr("sourcecolumn");
                String defaultvalue = st.getStr("defaultvalue");
                //判断目标字段是否为空
                if (JBoltStringUtil.isBlank(tagcolumn)) {
                    //为空用就递归
                    String a = getJsonObject(masid, s1, flagtype, sf, null);
                    //赋值到json
                    jsonObject.put(s1, JSON.parse(a));
                } else {
                    String s = "";
                    //判断来源字段是否为空
                    if (JBoltStringUtil.isBlank(sourcecolumn)) {
                        //为空直接用缺省值
                        s = defaultvalue;
                    } else {
                        //判断用那个数据集
                        if ("PreAllocate".equals(sf)) {
                            s = getCorrespondingValue(PreAllocate, sourcecolumn, sf, defaultvalue, null);
                        } else {
                            //判断数据集是否存在map里
                            if (map.containsKey(sf)) {
                                Map MainData = map.get(sf).get(0).toMap();
                                s = getCorrespondingValue(MainData, sourcecolumn, sf, defaultvalue, null);
                            } else {
                                throw new RuntimeException(sf + "数据集为空");
                            }
                        }
                    }
                    //赋值到json
                    jsonObject.put(tagcolumn, s);
                }
            }
            //把jsonObject转换成String
            json = jsonObject.toString();
        } else {
            //创建Jsonlist
            JsonArray jsonArray = new JsonArray();
            List<Record> lists = map.get(sourceflag);
            Map<String, List<Record>> collect = lists.stream().collect(Collectors.groupingBy(p -> p.getStr("groupflag") == null ? "10" : p.getStr("groupflag"), Collectors.toList()));
            Set<String> groupSeqs = new TreeSet<>(collect.keySet());
            Iterator groupId = groupSeqs.iterator();
            while (groupId.hasNext()){
                List<Record> list = collect.get(groupId.next());
                for (Record record : list) {
                    if (superior != null) {
                        String sbillrowno = record.get("pid").toString().toLowerCase();
                        String rbillrowno = record.get("id").toString().toLowerCase();
                        if (!superior.get(sbillrowno).equals(record.get(rbillrowno))) {
                            continue;
                        }
                    }
                    //list里面的json
                    JsonObject bodyObject = new JsonObject();
                    //遍历查询出来的数据
                    for (Record columsmapdetail : lsit) {
                        String tagcolumn = columsmapdetail.getStr("tagcolumn");
                        String masid = columsmapdetail.getStr("masid");
                        String flag1 = columsmapdetail.getStr("flag");
                        String sf = columsmapdetail.getStr("sourceflag");
                        String sourcecolumn = columsmapdetail.getStr("sourcecolumn");
                        columsmapdetail.getStr("defaultvalue");
                        //判断目标字段是否为空
                        if (JBoltStringUtil.isBlank(tagcolumn)) {
                            //为空用就递归System.out.println(columsmapdetail.getMasid()+"--"+ columsmapdetail.getFlag()+"--"+columsmapdetail.getFlagtype()+"-"+columsmapdetail.getSourceflag()+"-"+record);
                            String obj = getJsonObject(masid, flag1, columsmapdetail.getStr("flagtype"), sf, record.toMap());

                            JsonElement jsonElement = JsonParser.parseString(obj);
                            //赋值到json
                            bodyObject.add(flag1, jsonElement);
                        } else {
                            String s = "";
                            //判断是否直接用缺省值
                            if (JBoltStringUtil.isBlank(sourcecolumn)) {
                                s = columsmapdetail.getStr("defaultvalue");
                            } else {
                                //
                                if ("PreAllocate".equals(sf)) {
                                    s = getCorrespondingValue(PreAllocate, sourcecolumn, sf, columsmapdetail.getStr("defaultvalue"), jsonArray);
                                } else {
                                    s = getCorrespondingValue(record.toMap(), sourcecolumn, sf, columsmapdetail.getStr("defaultvalue"), null);
                                }
                            }
                            //赋值到json
                            bodyObject.addProperty(tagcolumn, s);
                        }
                    }
                    //加入到jsonlist里
                    jsonArray.add(bodyObject);
                }
            }
            json = jsonArray.toString();
        }
        //返回json
        return json;
    }

    /**
     * 通过result标识解析返回值
     * @return
     */
    public Map processResult(String resultType,String msg, Integer flag) {
        Map resultMap = new HashMap();
        String ruslte = columsmapdetailService.getVouchProcessRuslte(resultType.toLowerCase());//对应关系json字符串
        if (ruslte != null && ruslte.length() > 0){
            Map ruslteMap = JSON.parseObject(ruslte, HashMap.class);//
            JSONArray datas = (JSONArray) ruslteMap.get("data");
            Map jsonMap = analysisJSON(JSON.parseObject(msg), new JSONObject());
            for (int i = 0; i < datas.size(); i++) {
                Map keyMap = JSON.parseObject(datas.get(i).toString(),HashMap.class);
                if (keyMap.get("key") == null || keyMap.get("key").toString().length() == 0) {
                    continue;//如果key对应没有值，说明没有配关键字段，进去下一次循环
                }
                String key = keyMap.get("key").toString();
                String matchkey = keyMap.get("matchkey").toString();
                String matchvalue = keyMap.get("matchvalue").toString();
                String keyvalue = keyMap.get("keyvalue").toString();
                //通过规则的matchkey去匹配返回的结果
                //com.alibaba.fastjson.JSONArray ds = (com.alibaba.fastjson.JSONArray) jsonMap.get("d");
                String matchVal = resultMap.get(key) == null ? "" : resultMap.get(key).toString();
                Map dsObject = jsonMap;//ds.getJSONObject(j);
                if (dsObject.get(matchkey) != null && JBoltStringUtil.isNotBlank(matchvalue) && JBoltStringUtil.isNotBlank(keyvalue) && matchvalue.equals(dsObject.get(matchkey).toString())){
                    if(JBoltStringUtil.isNotBlank(matchVal) && dsObject.get(matchkey) != null && !matchVal.equals(dsObject.get(matchkey).toString())){
                        matchVal = matchVal + "," + keyvalue;
                    }else {
                        matchVal = keyvalue;
                    }
                } else {
                    if (JBoltStringUtil.isNotBlank(matchVal) && dsObject.get(matchkey) != null && !matchVal.equals(dsObject.get(matchkey).toString())){
                        matchVal = matchVal + "," + dsObject.get(matchkey).toString();
                    } else if (dsObject.get(matchkey) != null && JBoltStringUtil.isNotBlank(dsObject.get(matchkey).toString())){
                        matchVal = dsObject.get(matchkey).toString();//对应匹配的值
                    }
                }
                resultMap.put(key, matchVal);
            }
        }
        return resultMap;
    }

    public JSONObject analysisJSON(JSONObject data, JSONObject result){
        Set<String> parentKey = data.keySet();//获取所有的key
        for(String key : parentKey){
            Object sunData = data.get(key);//通过key获取value
            //System.out.println(sunData.toString());
            if(sunData instanceof JSONObject){
                analysisJSON((JSONObject)sunData,result);//递归解析子层JSON
            }else if (sunData instanceof JSONArray){
                for(Object json : (JSONArray)sunData){
                    analysisJSON((JSONObject)json,result);//递归解析子层JSON
                }
            }else if ((sunData == null ? "" : sunData).toString().contains("{")){
                HashMap map = JSON.parseObject(sunData.toString(), HashMap.class);
                analysisJSON(new JSONObject(map),result);//递归解析子层JSON
            }else{
                if (result.size() > 0 && result.get(key) != null && !sunData.toString().equals(result.get(key).toString())){
                    result.put(key,result.get(key).toString() + "," + sunData);
                }else{
                    result.put(key,sunData);//如果value不是json类型那么直接输出
                }
            }
        }
        return result;//输出解析后的数据
    }
}
