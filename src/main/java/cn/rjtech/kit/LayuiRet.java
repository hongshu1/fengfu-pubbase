package cn.rjtech.kit;

import cn.hutool.core.collection.CollUtil;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;

import java.util.List;

/**
 * Layui分页结果集
 *
 * @author Kephon
 */
public class LayuiRet extends Ret {

    /**
     * 分页结果
     *
     * @param page 分页对象
     * @return 分页结果
     */
    public static Ret paginate(Page<?> page) {
        return ok().set("count", page.getTotalRow()).set("data", page.getList());
    }

    /**
     * 返回列表数据
     *
     * @param list 列表
     * @return 列表结果
     */
    public static Ret list(List<?> list) {
        return ok().set("count", CollUtil.isEmpty(list) ? 0 : list.size()).set("data", list);
    }

}
