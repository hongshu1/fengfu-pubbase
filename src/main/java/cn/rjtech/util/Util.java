package cn.rjtech.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.jfinal.kit.Kv;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.Record;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

/**
 * @Description TODO
 * @Author Create by Alvin
 * @Date 2020-10-29 13:49
 */
public class Util {

    private static final Log LOG = Log.getLog(Util.class);

    private static final String WHERE = "WHERE";
    private static final String AND = "AND";
    public static final int MAXLENGTH = 20;

    private Util() {
        // ignored
    }

    /**
     * HttpServletResponse中输出Json字符串返回
     */
    public static void printJson(HttpServletResponse response, Object o) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter writer = response.getWriter();
        writer.write(JSONUtil.toJsonStr(o));
        writer.flush();
    }

    /**
     * 替换中间4位号码为*
     *
     * @param phone 手机号
     * @return 替换处理后的手机号
     */
    public static String hidePhone(String phone) {
        return StrKit.isBlank(phone) ? null : StrUtil.hide(phone, 3, 7);
    }

    /**
     * 隐藏部分卡号
     *
     * @param account 账号
     * @return 隐藏部分字符后的账号
     */
    public static String hideAccount(String account) {
        return StrKit.isBlank(account) ? null : StrUtil.hide(account, 5, 10);
    }

    /**
     * 隐藏部分身份证号码
     */
    public static String hideIdCard(String idCard) {
        return StrUtil.isBlank(idCard) ? null : StrUtil.hide(idCard, 4, 14);
    }

    /**
     * 截取查询SQL结尾部分, 适用WHERE/AND结尾拼接
     *
     * @param exceptSqlStr 查询条件SQL
     */
    public static String substrSql(String exceptSqlStr) {
        if (exceptSqlStr.endsWith(WHERE)) {
            return exceptSqlStr.substring(0, exceptSqlStr.length() - 5);
        } else if (exceptSqlStr.endsWith(AND)) {
            return exceptSqlStr.substring(0, exceptSqlStr.length() - 3);
        } else {
            return exceptSqlStr;
        }
    }

    /**
     * 将ids字符串主键拼接成sql in查询支持的格式
     * @param ids
     * @return
     */
    public static String getInSqlByIds(String ids){
        String[] split = ids.split(",");
        StringBuilder stringBuilder = new StringBuilder().append("(");
        for (String id : split) {
            stringBuilder.append("'").append(id).append("',");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    public static String concatTreeSorts(BigDecimal treeSort) {
        return "00000000" + treeSort;
    }

    /**
     * utf8转换iso-8859-1
     */
    public static String utfToIso(String fileName) {
        return new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
    }

    public static String arrayToString(String[] strs){
        StringJoiner sj=new StringJoiner(",");
        for(String str:strs){
            sj.add(str);
        }
        return sj.toString();
    }

    public static Map<String,Object> recordsToMap(List<Record> records,String key,String value){
        Map<String,Object> result=new HashMap<>(16);
        for(Record record:records){
            result.put(record.getStr(key),record.get(value));
        }
        return result;
    }

    /**
     * 执行shell命令
     *
     * @param cmd 脚本命令
     */
    public static void exec(String cmd) {
        try {
            // 调用jdk接口执行目标commond;
            Process process = Runtime.getRuntime().exec(cmd);
            // 获得命令执行后的返回值;并存储到StringBuffer容器中;
            LineNumberReader br = new LineNumberReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                LOG.info(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static boolean isEquals(Object o1, Object o2) {
        if (ObjUtil.equals(o1, o2)) {
            return true;
        }
        return String.valueOf(o1).equals(String.valueOf(o2));
    }

    public static List<String> getBetweenDate(String startDateStr, String endDateStr){
        // 声明保存日期集合
        List<String> list = new ArrayList<>();
        
        // 转化成日期类型
        Date startDate = DateUtil.parseDate(startDateStr);
        Date endDate = DateUtil.parseDate(endDateStr);
        
        // 用Calendar 进行日期比较判断
        Calendar calendar = Calendar.getInstance();
        while (startDate.getTime()<=endDate.getTime()){
            // 把日期添加到集合
            list.add(DateUtil.formatDate(startDate));
            // 设置日期
            calendar.setTime(startDate);
            //把日期增加一天
            calendar.add(Calendar.DATE, 1);
            // 获取增加后的日期
            startDate=calendar.getTime();
        }
        return list;
    }

    /**
     *获得当前月份第一天和最后一天
     * @return kv.startdate  kv.startdate
     */
    public static Kv getBeginEndDate() {
        LocalDate now = LocalDate.now();
        LocalDate firstDayOfMonth = now.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate lastDayOfMonth = now.with(TemporalAdjusters.lastDayOfMonth());
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String beginDateStr = firstDayOfMonth.format(formatter);
        String endDateStr = lastDayOfMonth.format(formatter);
        return Kv.by("startdate", beginDateStr).set("enddate", endDateStr);
    }
    
    public static String getTextName(String text){
        if (StrUtil.isBlank(text)){
            return null;
        }
        if (text.length() > MAXLENGTH){
            return text.substring(0, MAXLENGTH) + "...";
        }
        return text;
    }
    /**
     * 获取格式化堆栈异常信息
     */
    public static String getStackMsg(Exception e) {
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            return sw + "\r\n";
        } catch (Exception e2) {
            return "bad getErrorInfoFromException";
        }
    }
}
