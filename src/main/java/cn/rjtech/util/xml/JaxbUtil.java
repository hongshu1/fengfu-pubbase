package cn.rjtech.util.xml;

import cn.hutool.core.util.StrUtil;
import cn.jbolt.core.exception.CommonException;

import javax.xml.bind.*;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.namespace.QName;
import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Collection;

/**
 * Jaxb Xml与对象的转换工具类
 *
 * @author Kephon
 */
public class JaxbUtil<T> {

    /**
     * 多线程安全的Context
     */
    private final JAXBContext jaxbContext;

    public JaxbUtil(T t) {
        try {
            jaxbContext = JAXBContext.newInstance(t.getClass());
        } catch (JAXBException e) {
            throw new CommonException(e.getLocalizedMessage());
        }
    }

    /**
     * xml文件配置转换为对象
     *
     * @param xmlFilePath xml文件路径
     * @return java对象
     */
    @SuppressWarnings("unchecked")
    public T xmlFile2Bean(String xmlFilePath) throws JAXBException {
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        return (T) unmarshaller.unmarshal(new File(xmlFilePath));
    }

    /**
     * 转换xml字符串为对象
     *
     * @param xml xml字符串
     */
    @SuppressWarnings("unchecked")
    public T xml2Bean(String xml) throws JAXBException {
        Unmarshaller um = jaxbContext.createUnmarshaller();
        StringReader sr = new StringReader(xml);
        return (T) um.unmarshal(sr);
    }

    /**
     * Java Object->Xml.
     */
    public String toXml(Object root, String encoding) {
        try {
            StringWriter writer = new StringWriter();
            // 自定义xml头部声明
            writer.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
            createMarshaller(encoding).marshal(root, writer);
            return writer.toString();
        } catch (JAXBException e) {
            e.printStackTrace();
            throw new CommonException(e.getLocalizedMessage());
        }
    }

    /**
     * Java Object->Xml.
     */
    public String toXml(Object root, String encoding, String head, String end) {
        try {
            StringWriter writer = new StringWriter();
            // 自定义xml头部声明
            writer.write(head);
            createMarshaller(encoding).marshal(root, writer);
            writer.write(end);
            return writer.toString();
        } catch (JAXBException e) {
            e.printStackTrace();
            throw new CommonException(e.getLocalizedMessage());
        }
    }

    /**
     * Java Object->Xml, 特别支持对Root Element是Collection的情形.
     */
    public String toXml(Collection<?> root, String rootName, String encoding) {
        try {
            CollectionWrapper wrapper = new CollectionWrapper();
            wrapper.collection = root;

            JAXBElement<CollectionWrapper> wrapperElement = new JAXBElement<>(new QName(rootName), CollectionWrapper.class, wrapper);

            StringWriter writer = new StringWriter();
            createMarshaller(encoding).marshal(wrapperElement, writer);
            return writer.toString();
        } catch (JAXBException e) {
            throw new CommonException(e.getLocalizedMessage());
        }
    }

    /**
     * Xml->Java Object.
     */
    @SuppressWarnings("unchecked")
    public T fromXml(String xml) {
        try {
            return (T) createUnmarshaller().unmarshal(new StringReader(xml));
        } catch (JAXBException e) {
            e.printStackTrace();
            throw new CommonException(e.getLocalizedMessage());
        }
    }

    /**
     * Xml->Java Object, 支持大小写敏感或不敏感.
     */
    @SuppressWarnings("unchecked")
    public T fromXml(String xml, boolean caseSensitive) {
        try {
            StringReader reader = new StringReader(caseSensitive ? xml.toLowerCase() : xml);
            return (T) createUnmarshaller().unmarshal(reader);
        } catch (JAXBException e) {
            e.printStackTrace();
            throw new CommonException(e.getLocalizedMessage());
        }
    }

    /**
     * 创建UnMarshaller.
     */
    public Unmarshaller createUnmarshaller() {
        try {
            return jaxbContext.createUnmarshaller();
        } catch (JAXBException e) {
            throw new CommonException(e.getLocalizedMessage());
        }
    }

    /**
     * 封装Root Element 是 Collection的情况.
     */
    public static class CollectionWrapper {
        @XmlAnyElement
        Collection<?> collection;
    }

    /**
     * 创建Marshaller, 设定encoding(可为Null).
     */
    public Marshaller createMarshaller(String encoding) {
        try {
            Marshaller marshaller = jaxbContext.createMarshaller();
            // 格式化配置
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);
            // 去除xml文件头部的standalone="yes"声明
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
            // 字符串类型为NULL设置为空串
            marshaller.setListener(new MarshallerListener());

            if (StrUtil.isBlank(encoding)) {
                return marshaller;
            }
            marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);
            return marshaller;
        } catch (JAXBException e) {
            e.printStackTrace();
            throw new CommonException(e.getLocalizedMessage());
        }
    }

}
