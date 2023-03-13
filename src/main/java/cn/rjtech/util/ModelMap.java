package cn.rjtech.util;

import cn.hutool.core.collection.CollUtil;
import com.google.gson.Gson;
import com.jfinal.plugin.activerecord.Model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ModelMap处理对象
 *
 * @param <K> key泛型
 * @param <T> value泛型
 * @author Kephon
 */
public class ModelMap<K, T> {

    private final Map<K, T> attrs = new HashMap<>();

    @SuppressWarnings({"rawtypes", "unchecked"})
    public ModelMap(List<? extends Model> records, String keyName, String valueName) {
        if (CollUtil.isNotEmpty(records)) {
            for (Model row : records) {
                attrs.put((K) row.get(keyName), (T) row.get(valueName));
            }
        }
    }

    public T get(K key) {
        return attrs.get(key);
    }

    public T set(K k, T t) {
        return attrs.put(k, t);
    }

    public T remove(K key) {
        return attrs.remove(key);
    }

    public int size() {
        return attrs.size();
    }

    @Override
    public String toString() {
        return new Gson().toJson(attrs);
    }

}
