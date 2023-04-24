package cn.rjtech.wms.utils;

import com.jfinal.kit.Kv;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Author: lidehui
 * @Date: 2023/1/14 9:59
 * @Version: 1.0
 * @Desc:
 */
public class MapToLowerCase {
    /**
     * 转小写
     * @param
     * @return
     */
    public static Map<String, Object> mapToLowerCaseToMap(Map<String, Object> orgMap){
        Map<String, Object> resultMap = new HashMap<>();
        if (orgMap == null || orgMap.isEmpty()){
            return resultMap;
        }

        Set<String> keySet = orgMap.keySet();
        for (String key : keySet){
            String newKey = key.toLowerCase();
            newKey = newKey.replace("_", "");

            resultMap.put(newKey, orgMap.get(key));
        }

        return resultMap;
    }

    public static Kv mapToLowerCaseToKv(Map<String, Object> orgMap){
        return Kv.create().set(mapToLowerCaseToMap(orgMap));
    }
}
