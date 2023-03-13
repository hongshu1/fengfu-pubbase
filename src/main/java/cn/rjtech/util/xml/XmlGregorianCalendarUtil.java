package cn.rjtech.util.xml;

import cn.rjtech.base.exception.CommonException;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author Kephon
 */
public class XmlGregorianCalendarUtil {

    private XmlGregorianCalendarUtil() {
    }

    /**
     * data转换为XMLGregorianCalendar
     */
    public static XMLGregorianCalendar dateToXmlGregorianCalendar(Date date) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        try {
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CommonException(e.getLocalizedMessage());
        }
    }

}
