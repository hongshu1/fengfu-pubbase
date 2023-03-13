package cn.rjtech.util.xml;

import cn.hutool.core.util.StrUtil;

import javax.xml.bind.Marshaller;
import java.lang.reflect.Field;

/**
 * 编组器监听实现
 *
 * @author Kephon
 */
public class MarshallerListener extends Marshaller.Listener {

    @Override
    public void beforeMarshal(Object source) {
        super.beforeMarshal(source);

        Field[] fields = source.getClass().getDeclaredFields();

        try {
            for (Field f : fields) {
                f.setAccessible(true);

                if (f.getType() == String.class && f.get(source) == null) {
                    f.set(source, StrUtil.EMPTY);
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
