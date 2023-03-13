package cn.rjtech.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Author: ZHu
 * Description: 集合工具类
 * Create Time: 2022/11/14 14:38
 */
public class CollectionUtils {
    /**
     * 集合按长度分组
     *
     * @param list 集合
     * @param size 分割大小
     * @param <T>
     * @return
     */
    public static <T> List<List<T>> partition(final List<T> list, final int size) {
        List<List<T>> result = new ArrayList<>();
        Iterator<T> it = list.iterator();
        List<T> subList = null;
        while (it.hasNext()) {
            if (subList == null) {
                subList = new ArrayList<>();
            }
            T t = it.next();
            subList.add(t);
            if (subList.size() == size) {
                result.add(subList);
                subList = null;
            }
        }
        //补充最后一页
        if (subList != null) {
            result.add(subList);
        }
        return result;

    }
}
