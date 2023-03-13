package cn.rjtech.base.controller;

import cn.hutool.core.lang.Assert;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.controller.base.JBoltBaseController;
import cn.jbolt.core.kit.OrgAccessKit;
import cn.jbolt.core.para.jbolttablemenufilter.JBoltTableMenuFilter;
import cn.rjtech.kit.LayuiRet;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import org.apache.commons.lang3.ArrayUtils;

import java.util.List;

/**
 * @author Kephon
 */
public class BaseAdminController extends JBoltBaseController {

    protected void ok() {
        renderJsonSuccess();
    }

    protected void fail(String msg) {
        renderJsonFail(msg);
    }

    protected void ok(Object o) {
        renderJsonData(o);
    }

    /**
     * 仅支持 layui 分页，其他场景不支持
     *
     * @param page 分页对象
     */
    protected void renderLayPage(Page<?> page) {
        renderJson(LayuiRet.paginate(page));
    }

    /**
     * 仅支持layui 数据表格，其他场景不支持
     */
    protected void renderLaylist(List<?> list) {
        renderJson(LayuiRet.list(list));
    }

    /**
     * 先校验再返回model类, 校验出错则提示参数错误
     *
     * @param modelClass model类Class
     * @param modelName  model别名
     * @param <T>        Model类
     * @return T Model实例
     */
    @SuppressWarnings("rawtypes")
    protected <T extends Model> T useIfValid(Class<T> modelClass, String modelName) {
        T t = getModel(modelClass, modelName);
        ValidationUtils.validateModel(t, null);
        return t;
    }

    /**
     * 先校验再返回model类, 校验出错则提示参数错误
     *
     * @param modelClass model类Class
     * @param modelName  model别名
     * @param paraName   参数名
     * @param <T>        Model类
     * @return T Model实例
     */
    @SuppressWarnings("rawtypes")
    protected <T extends Model> T useIfValid(Class<T> modelClass, String modelName, String paraName) {
        T t = getModel(modelClass, modelName);
        ValidationUtils.validateModel(t, paraName);
        return t;
    }

    protected Long useIdIfValid(Long id, String paraName) {
        ValidationUtils.validateId(id, paraName);
        return id;
    }

    protected Long useIfPresent(Long id) {
        ValidationUtils.notNull(id, JBoltMsg.PARAM_ERROR);
        return id;
    }

    protected Integer useIfPresent(Integer id) {
        ValidationUtils.notNull(id, JBoltMsg.PARAM_ERROR);
        return id;
    }

    protected Integer useIfPresent(Integer id, String paraName) {
        ValidationUtils.notNull(id, paraName + "不能为空");
        return id;
    }

    protected String useIfNotBlank(String paraValue) {
        ValidationUtils.notBlank(paraValue, JBoltMsg.PARAM_ERROR);
        return paraValue;
    }

    /**
     * 复制Model对象的字段到其他Model对象, 指定字段列表
     */
    protected void copyPropertiesIncludes(Model<?> source, Model<?> target, String... includesArr) {
        if (ArrayUtils.isEmpty(includesArr)) {
            return;
        }
        Assert.notNull(source, "源对象不能为空");
        Assert.notNull(target, "目标对象不能为空");

        // 设置保留的字段
        source.keep(includesArr);
        // 覆盖字段
        target.put(source);
    }

    /**
     * 复制Model对象的字段到其他Model对象
     */
    protected void copyProperties(Model<?> source, Model<?> target) {
        Assert.notNull(source, "源对象不能为空");
        Assert.notNull(target, "目标对象不能为空");

        // 覆盖字段
        target.put(source);
    }

    /**
     * 优先从右键菜单对象中获取页码
     */
    protected int getPageNumberWithMenuFilter(JBoltTableMenuFilter menuFilter) {
        return null == menuFilter ? getPageNumber() : menuFilter.getPageNumber();
    }

    /**
     * 优先从右键菜单对象中获取页大小
     */
    protected int getPageSizeWithMenuFilter(JBoltTableMenuFilter menuFilter) {
        return null == menuFilter ? getPageSize() : menuFilter.getPageSize();
    }

    /**
     * 获取登录的组织ID
     *
     * @return 可能为NULL，即非组织方式登录
     */
    protected Long getOrgId() {
        return OrgAccessKit.getOrgId();
    }

    /**
     * 获取登录的组织编码
     *
     * @return 可能为NULL，即非组织方式登录
     */
    protected String getOrgCode() {
        return OrgAccessKit.getOrgCode();
    }

    /**
     * 获取登录的组织编码
     *
     * @return 可能为NULL，即非组织方式登录
     */
    protected String getOrgName() {
        return OrgAccessKit.getOrgName();
    }

}
