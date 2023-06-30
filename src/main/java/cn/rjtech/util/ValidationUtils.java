package cn.rjtech.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.rjtech.base.exception.ParameterException;
import cn.rjtech.enums.ErrorEnums;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.upload.UploadFile;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @Description 参数校验工具类，可以参数格式，正则验证等
 * @Author Create by Alvin
 * @Date 2020-10-27 14:00
 */
public class ValidationUtils {

    private static final Pattern BASE64_PATTERN = Pattern.compile("^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$");
    private static final int PAGE_SIZE_MAX_LENGTH = 90;

    /**
     * 验证码长度
     */
    private static final int VCODE_LENGTH = 6;

    private ValidationUtils() {
        // ignored
    }

    /**
     * 校验字符串是否为base64格式的字符串
     *
     * @param str 待校验字符串
     * @return true/false, true为合法，否则为非法
     */
    public static boolean isBase64Str(String str) {
        return BASE64_PATTERN.matcher(str).matches();
    }

    /**
     * 校验是否为null, 是，则抛出异常，提示错误信息
     *
     * @param object  校验对象
     * @param message 错误信息
     */
    public static void notNull(Object object, String message) {
        isTrue(null != object, message);
    }

    /**
     * 校验是否为null, 是，则抛出异常，提示错误信息
     *
     * @param object     校验对象
     * @param errorEnums 错误枚举
     */
    public static void notNull(Object object, ErrorEnums errorEnums) {
        isTrue(null != object, errorEnums);
    }

    /**
     * 校验是否为空白字符串, 是，则抛出异常，提示错误信息
     *
     * @param string  校验字符串
     * @param message 错误信息
     */
    public static void notBlank(String string, String message) {
        isTrue(StrUtil.isNotBlank(string), message);
    }

    /**
     * 校验是否为空白字符串, 是，则抛出异常，提示错误信息
     *
     * @param string     校验字符串
     * @param errorEnums 错误枚举
     */
    public static void notBlank(String string, ErrorEnums errorEnums) {
        isTrue(StrUtil.isNotBlank(string), errorEnums);
    }

    /**
     * 校验手机号码
     *
     * @param phone 手机号码
     */
    public static void validatePhone(String phone) {
        isTrue(Validator.isMobile(phone), "手机号码不合法");
    }

    /**
     * 校验字符串是否合法，非空且长度符合限定值
     *
     * @param str      字符串
     * @param length   限定值
     * @param paraName 参数名称
     */
    public static void validateStr(String str, int length, String paraName) {
        notBlank(str, paraName + "不能为空");
        isTrue(str.length() <= length, paraName + "长度不能超过" + length + "个字符");
    }

    /**
     * 校验参数是否符合给定条件, 不符合则提示错误信息
     *
     * @param condition 布尔条件
     * @param message   错误提示信息
     */
    public static void isTrue(boolean condition, String message) {
        if (!condition) {
            throw new ParameterException(message);
        }
    }

    /**
     * 响应自定义的业务错误
     *
     * @param condition  条件
     * @param errorEnums 错误枚举
     */
    public static void isTrue(boolean condition, ErrorEnums errorEnums) {
        if (!condition) {
            throw new ParameterException(errorEnums);
        }
    }

    /**
     * 校验参数ID
     *
     * @param id       ID
     * @param paraName 参数名
     */
    public static void validateId(Long id, String paraName) {
        notNull(id, "参数" + paraName + "不能为空");
        isTrue(id > 0, "参数" + paraName + "不合法");
    }

    public static void validateIdInt(Integer id, String paraName) {
        notNull(id, "参数" + paraName + "不能为空");
        isTrue(id > 0, "参数" + paraName + "不合法");
    }

    /**
     * 校验参数ID
     *
     * @param id       ID
     * @param paraName 参数名
     */
    public static void validateIdStr(String id, String paraName) {
        notNull(id, "参数" + paraName + "不能为空");
    }

    /**
     * 校验分页参数：页码，参数必传
     */
    public static void validatePageNumber(Integer pageNumber) {
        notNull(pageNumber, "缺少参数页码");
        isTrue(pageNumber > 0, "页码错误");
    }

    /**
     * 校验分页参数：页大小, 参数必传
     */
    public static void validatePageSize(Integer pageSize) {
        notNull(pageSize, "缺少参数页大小");
        isTrue(pageSize > 0 && pageSize <= PAGE_SIZE_MAX_LENGTH, "页大小参数错误");
    }

    /**
     * 校验base64格式编码字符串
     *
     * @param str     字符串
     * @param message 错误信息
     */
    public static void validateBase64Str(String str, String message) {
        isTrue(isBase64Str(str), message);
    }

    /**
     * 校验排序值
     *
     * @param seq 排序值
     */
    public static void validateSeq(Integer seq) {
        notNull(seq, "缺少排序值");
        isTrue(seq > 0, "排序值不合法");
    }

    /**
     * 校验集合是否为非空
     *
     * @param collection 集合对象
     * @param message    错误信息
     */
    public static void notEmpty(Collection<?> collection, String message) {
        isTrue(CollUtil.isNotEmpty(collection), message);
    }

    /**
     * 校验Map是否非空
     *
     * @param map     Map
     * @param message 错误信息
     */
    public static void notEmpty(Map<?, ?> map, String message) {
        isTrue(MapUtil.isNotEmpty(map), message);
    }

    /**
     * 校验集合是否为非空
     *
     * @param arr     数组对象
     * @param message 错误信息
     */
    public static void notEmpty(Object[] arr, String message) {
        isTrue(ArrayUtil.isNotEmpty(arr), message);
    }

    /**
     * 校验Model对象是否为空
     *
     * @param model    数组对象
     * @param paraName 参数名
     */
    public static void validateModel(Model<?> model, String paraName) {
        notEmpty(model._getAttrValues(), (StrUtil.isBlank(paraName) ? "参数" : paraName) + "不能为空");
    }

    /**
     * 校验集合是否为空
     *
     * @param collection 集合对象
     * @param message    错误信息
     */
    public static void assertEmpty(Collection<?> collection, String message) {
        isTrue(CollUtil.isEmpty(collection), message);
    }

    /**
     * 检查验证码
     *
     * @param vcode 验证码
     */
    public static void validateVcode(String vcode) {
        notBlank(vcode, "验证码不能为空");
        validateStr(vcode, VCODE_LENGTH, "验证码");
    }

    /**
     * 排序方式
     *
     * @param order 排序方式
     */
    public static void validateOrder(String order) {
        notBlank(order, "排序方式不能为空");
        isTrue("asc".equalsIgnoreCase(order) || "desc".equalsIgnoreCase(order), "排序方式参数不合法");
    }

    /**
     * 校验是否为数字
     *
     * @param value    数字字符串
     * @param paraName 参数名
     */
    public static void isNumber(String value, String paraName) {
        isTrue(Validator.isNumber(value), paraName + "参数非数字格式");
    }

    /**
     * 校验省份
     *
     * @param province 省份
     */
    public static void validateProvince(String province) {
        validateStr(province, 25, "省份");
    }

    /**
     * 校验城市
     *
     * @param city 城市
     */
    public static void validateCity(String city) {
        validateStr(city, 25, "城市");
    }

    /**
     * 校验区县
     *
     * @param district 区县
     */
    public static void validateDistrict(String district) {
        validateStr(district, 25, "区县");
    }

    /**
     * 校验乡镇
     *
     * @param town 乡镇
     */
    public static void validateTown(String town) {
        validateStr(town, 25, "乡镇");
    }

    /**
     * 校验可空的字符串
     */
    public static void validateNullableStr(String str, int maxLength, String paraName) {
        if (StrUtil.isNotBlank(str)) {
            isTrue(str.length() <= maxLength, paraName + "过长，请缩短，不多于" + maxLength + "个字符");
        }
    }

    /**
     * 校验为null，NOT NULL则抛出异常，提示错误信息
     *
     * @param object  校验对象
     * @param message 错误信息
     */
    public static void assertNull(Object object, String message) {
        isTrue(null == object, message);
    }

    /**
     * 校验为空串
     *
     * @param str     字符串
     * @param message 提示信息
     */
    public static void assertBlank(String str, String message) {
        isTrue(StrUtil.isBlank(str), message);
    }

    /**
     * 校验身份证
     *
     * @param idCard 身份证号码
     */
    public static void validateIdCard(String idCard) {
        isTrue(Validator.isCitizenId(idCard), "身份证号码不合法");
    }

    /**
     * 校验数量
     *
     * @param quantity 数量
     */
    public static void validateQuantity(Integer quantity) {
        notNull(quantity, "缺少数量");
        isTrue(quantity > 0, "数量不合法");
    }

    /**
     * 校验地址
     *
     * @param address 详细地址
     */
    public static void validateAddress(String address) {
        validateStr(address, 50, "地址");
    }

    /**
     * 校验BigDecimal类型参数 （条件 >= 0）
     *
     * @param decimal  数值
     * @param paraName 参数名
     */
    public static void validateDecimalGe0(BigDecimal decimal, String paraName) {
        notNull(decimal, "缺少" + paraName + "参数");
        isTrue(decimal.compareTo(BigDecimal.ZERO) >= 0, paraName + "参数不合法");
    }

    /**
     * 校验BigDecimal类型参数 （条件 > 0）
     *
     * @param decimal  数值
     * @param paraName 参数名
     */
    public static void validateDecimalGt0(BigDecimal decimal, String paraName) {
        notNull(decimal, "缺少" + paraName + "参数");
        isTrue(decimal.compareTo(BigDecimal.ZERO) > 0, paraName + "参数不合法");
    }

    /**
     * 校验BigDecimal类型参数 （条件 >= 0）
     *
     * @param decimal  数值
     * @param paraName 参数名
     */
    public static void validateNullableDecimalGe0(BigDecimal decimal, String paraName) {
        if (null != decimal) {
            isTrue(decimal.compareTo(BigDecimal.ZERO) >= 0, paraName + "参数不合法");
        }
    }

    /**
     * 校验BigDecimal类型参数 （条件 > 0）
     *
     * @param decimal  数值
     * @param paraName 参数名
     */
    public static void validateNullableDecimalGt0(BigDecimal decimal, String paraName) {
        if (null != decimal) {
            isTrue(decimal.compareTo(BigDecimal.ZERO) > 0, paraName + "参数不合法");
        }
    }

    /**
     * 校验Integer类型参数 （条件 >= 0）
     *
     * @param i        整型数
     * @param paraName 参数名
     */
    public static void validateIntGe0(Integer i, String paraName) {
        notNull(i, "缺少" + paraName + "参数");
        isTrue(i >= 0, paraName + "参数不合法");
    }

    /**
     * 校验Integer类型参数 （条件 > 0）
     *
     * @param i        整型数
     * @param paraName 参数名
     */
    public static void validateIntGt0(Integer i, String paraName) {
        notNull(i, "缺少" + paraName + "参数");
        isTrue(i > 0, paraName + "参数不合法");
    }

    /**
     * 校验年月格式的日期字符串
     *
     * @param monthDate 年月日期，格式: yyyy-MM
     * @param msg       错误信息
     */
    public static void validateNullableMonthDate(String monthDate, String msg) {
        if (StrUtil.isNotBlank(monthDate)) {
            notNull(DateUtil.parse(monthDate, "yyyy-MM"), msg);
        }
    }

    /**
     * 校验年月格式的日期字符串
     *
     * @param date 年月日期，格式: yyyy-MM-dd
     * @param msg  错误信息
     */
    public static void validateNullableDate(String date, String msg) {
        if (StrUtil.isNotBlank(date)) {
            notNull(DateUtil.parseDate(date), msg);
        }
    }

    public static void isExcel(UploadFile uploadFile) {
        notNull(uploadFile, "文件不存在");
        isTrue(FileUtil.pathEndsWith(uploadFile.getFile(), "xls") || FileUtil.pathEndsWith(uploadFile.getFile(), "xlsx"), "请上传excel文件");
        isTrue("xls".equalsIgnoreCase(FileTypeUtil.getType(uploadFile.getFile())), "请上传excel文件");
    }

    /**
     * 校验比率是否合法
     *
     * @param rate     比率
     * @param paraName 参数名
     */
    public static void validateRate(BigDecimal rate, String paraName) {
        notNull(rate, "缺少参数" + paraName);
        isTrue(rate.compareTo(BigDecimal.ZERO) > 0 && rate.compareTo(BigDecimal.ONE) <= 0, paraName + "参数不合法");
    }

    /**
     * 校验对象是否相等
     *
     * @param o1  对象1
     * @param o2  对象2
     * @param msg 错误信息
     */
    public static void equals(Object o1, Object o2, String msg) {
        isTrue(ObjUtil.equal(o1, o2), msg);
    }

    /**
     * 抛出错误信息
     *
     * @param msg 错误信息
     */
    public static void error(String msg) {
        throw new ParameterException(msg);
    }

}
