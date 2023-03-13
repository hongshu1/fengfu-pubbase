package cn.rjtech.kit;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ReflectUtil;
import com.jfinal.aop.Aop;
import com.jfinal.kit.Okv;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

/**
 * 模板引擎反射调用模板实现
 *
 * @author Kephon
 */
public class ReflectionTemplateFn {

    /**
     * 转调Aop.get加载字符串名称的类或接口关联的Class对象
     *
     * @param name 指定字符串名称的类或接口关联的Class对象
     * @return Aop.get返回的对象
     */
    public Object aop(String name) {
        try {
            return Aop.get(Class.forName(name));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("找不到这个类:" + name);
        }
    }

    /**
     * 调动指定对象的方法
     *
     * @param obj        对象
     * @param methodName 方法
     * @return 方法返回值
     */
    public Object eval(Object obj, String methodName, Okv para) {
        try {
            Method method = ReflectUtil.getMethodByName(obj.getClass(), methodName);

            // 参数列表
            Parameter[] parameters = method.getParameters();
            if (ArrayUtil.isEmpty(parameters)) {
                // 无参数列表
                return method.invoke(obj);
            }

            List<Object> params = new ArrayList<>();

            int i = 0;

            for (Object key : para.keySet()) {
                params.add(Convert.convert(parameters[i++].getType(), para.get(key)));
            }

            // 以下方式依赖代理返回真实的参数名，采用cglib时获取不到，所以废弃使用
            // for (Parameter parameter : parameters) {
            //      Object p = para.get(parameter.getName())
            //      ValidationUtils.notNull(p, String.format("参数%s不能为空", parameter.getName()))
            //      params.add(Convert.convert(parameter.getType(), p))
            // }

            return method.invoke(obj, params.toArray());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
