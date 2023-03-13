package cn.rjtech.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.jbolt.core.base.JBoltInjector;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Record;

import java.util.ArrayList;
import java.util.List;

/**
 * 提取JBolt内置实现方法
 *
 * @author Kephon
 */
public class JBoltModelKit {

    /**
     * JSON对象转Class对象
     *
     * @param beanClass Class
     * @param json      JSON对象
     * @param <T>       泛型
     * @return Class对象
     */
    public static <T> T getFromBean(Class<T> beanClass, JSONObject json) {
        return json.toJavaObject(beanClass);
    }

    /**
     * JSON对象转Class对象
     *
     * @param beanClass Class
     * @param beanName  实体别名
     * @param json      JSON对象
     * @param <T>       泛型
     * @return Class对象
     */
    public static <T> T getFromBean(Class<T> beanClass, String beanName, JSONObject json) {
        return JBoltInjector.injectModel(beanClass, beanName, json.getInnerMap());
    }

    /**
     * JSON对象转Record
     *
     * @param json JSON对象
     * @return Record对象
     */
    public static Record getFromRecord(JSONObject json) {
        return new Record().setColumns(json.getInnerMap());
    }

    /**
     * JSON对象转Record
     *
     * @param json JSON字符串
     * @return Record对象
     */
    public static Record getFromRecord(String json) {
        return getFromRecord(JSON.parseObject(json));
    }

    /**
     * JSON数组转为Record数组
     *
     * @param jsonArray JSON数组
     * @return Record数组
     */
    public static List<Record> getFromRecords(JSONArray jsonArray) {
        if (CollUtil.isEmpty(jsonArray)) {
            return null;
        }

        List<Record> records = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            records.add(new Record().setColumns(jsonArray.getJSONObject(i).getInnerMap()));
        }
        return records;
    }

    /**
     * JSON数组转为Record数组
     *
     * @param jsonArrayStr JSON数组
     * @return Record数组
     */
    public static List<Record> getFromRecords(String jsonArrayStr) {
        if (StrUtil.isBlank(jsonArrayStr)) {
            return null;
        }
        return getFromRecords(JSON.parseArray(jsonArrayStr));
    }

    /**
     * JSON数组转为Record数组
     *
     * @param jsonArray JSON数组
     * @return Record数组
     */
    public static List<Record> getFromRecords(List<JSONObject> jsonArray) {
        if (CollUtil.isEmpty(jsonArray)) {
            return null;
        }

        List<Record> records = new ArrayList<>();
        for (JSONObject jsonObject : jsonArray) {
            records.add(new Record().setColumns(jsonObject.getInnerMap()));
        }
        return records;
    }

    /**
     * JSON数组转Bean数组
     *
     * @param beanClass 指定类
     * @param jsonArray JSON数组
     * @param <T>       泛型类
     * @return Bean数组
     */
    public static <T> List<T> getBeanList(Class<T> beanClass, JSONArray jsonArray) {
        return jsonArray.toJavaList(beanClass);
    }

}
