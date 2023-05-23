package cn.rjtech.util.xml;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import org.xml.sax.InputSource;

import java.beans.XMLDecoder;
import java.io.File;

/**
 * @author Kephon
 */
public class XmlUtil extends cn.hutool.core.util.XmlUtil {

    /**
     * 从XML中读取对象 Reads serialized object from the XML file.<br>
     * 注意，使用此方法解析不受信任 XML 字符串时，可能容易受到远程代码执行攻击！<br>
     * 见：https://gitee.com/dromara/hutool/issues/I6AEX2
     *
     * @param <T>    对象类型
     * @param source XML文件
     * @return 对象
     */
    public static <T> T readObjectFromXml(File source) {
        return readObjectFromXml(new InputSource(FileUtil.getInputStream(source)));
    }

    /**
     * 从XML中读取对象 Reads serialized object from the XML file.<br>
     * 注意，使用此方法解析不受信任 XML 字符串时，可能容易受到远程代码执行攻击！<br>
     * 见：https://gitee.com/dromara/hutool/issues/I6AEX2
     *
     * @param <T>    对象类型
     * @param xmlStr XML内容
     * @return 对象
     * @since 3.2.0
     */
    public static <T> T readObjectFromXml(String xmlStr) {
        return readObjectFromXml(new InputSource(StrUtil.getReader(xmlStr)));
    }

    /**
     * 从XML中读取对象 Reads serialized object from the XML file.<br>
     * 注意，使用此方法解析不受信任 XML 字符串时，可能容易受到远程代码执行攻击！<br>
     * 见：https://gitee.com/dromara/hutool/issues/I6AEX2
     *
     * @param <T>    对象类型
     * @param source {@link InputSource}
     * @return 对象
     * @since 3.2.0
     */
    @SuppressWarnings("unchecked")
    public static <T> T readObjectFromXml(InputSource source) {
        Object result;
        XMLDecoder xmldec = null;
        try {
            xmldec = new XMLDecoder(source);
            result = xmldec.readObject();
        } finally {
            IoUtil.close(xmldec);
        }
        return (T) result;
    }
    
}
