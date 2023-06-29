package cn.rjtech.wms.print;

import cn.hutool.core.map.CaseInsensitiveMap;
import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.Setting;
import cn.jbolt.core.base.config.JBoltConfig;
import cn.jbolt.core.base.config.JBoltExtendDatabaseConfig;
import cn.rjtech.common.model.Printsetting;
import cn.rjtech.util.ValidationUtils;
import cn.rjtech.wms.print.printmachine.PrintmachineService;
import cn.rjtech.wms.print.printsetting.PrintsettingService;
import com.jfinal.aop.Inject;
import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.Record;

import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author Kephon
 */
public class PrintService {

    private static final Log LOG = Log.getLog(PrintService.class);

    @Inject
    private PrintsettingService printsettingService;
    @Inject
    private PrintmachineService printmachineService;

    /**
     * 获取打印机列表
     *
     * @param organizeCode 组织
     */
    public List<Record> getPringCodeNameList(String organizeCode, String tag) throws Exception {
        List<Record> pringCodeOnNameList = new ArrayList<>();
        if (null == organizeCode || "".equals(organizeCode)) {
            throw new Exception("请检查传入的参数！");
        }
        String printMachineCode = printsettingService.getPrintMachineCode(organizeCode, tag);
        List<Record> list = printsettingService.valueandsymbolSplit(printMachineCode, ",");
        for (Record res : list) {
            Record pringCodeOnName = printmachineService.getPringCodeNameList(organizeCode, res.get("name").toString());
            pringCodeOnNameList.add(pringCodeOnName);
        }
        return pringCodeOnNameList;
    }

    /**
     * 通过组织和tag获取配置信息
     */
    public Printsetting getPrintSetting(String tag) {
        return printsettingService.getPrintSetting(tag);
    }

    public Printsetting getPrintSettingByReportfilename(String reportfilename) {
        return printsettingService.getPrintSettingByReportfilename(reportfilename);
    }

    /**
     * web打印报表
     *
     * @param reportName       报表模板名
     * @param printerName      打印机名称
     * @param printNum         打印文档份数
     * @param paperSize        打印纸张大小
     * @param paperOrientation 打印纸张方向
     * @param dbAlias          数据库别名
     * @return String
     */
    public String reportPrint(String reportName, String printerName, String printNum, String paperSize, String paperOrientation, String dbAlias, String sqlParams) {
        StringBuilder sb = new StringBuilder();

        if (StrUtil.isNotBlank(reportName)) {
            sb.append("reportName=").append(reportName);
        }
        if (StrUtil.isNotBlank(printerName)) {
            sb.append("&printerName=").append(printerName);
        }
        if (StrUtil.isNotBlank(printNum)) {
            sb.append("&printNum=").append(printNum);
        }
        if (StrUtil.isNotBlank(paperSize)) {
            sb.append("&paperSize=").append(paperSize);
        }
        if (StrUtil.isNotBlank(paperOrientation)) {
            sb.append("&paperOrientation=").append(paperOrientation);
        }
        if (StrUtil.isNotBlank(dbAlias)) {
            Setting setting = JBoltExtendDatabaseConfig.me().getSetting().getSetting(dbAlias);
            ValidationUtils.notNull(setting, String.format("指定的数据源‘%s’不存在", dbAlias));

            sb.append("&driver=").append(JBoltConfig.getDriverClass(setting.getStr("db_type")));
            sb.append("&url=").append(setting.get("jdbc_url"));
            sb.append("&username=").append(setting.get("user"));
            sb.append("&password=").append(setting.get("password"));
        }
        if (StrUtil.isNotBlank(sqlParams)) {
            sb.append("&").append(sqlParams);
        }

        String stringParam = sb.toString();

        LOG.info("Param: {}", stringParam);

        if (stringParam.getBytes(StandardCharsets.UTF_8).length < 1) {
            return "";
        }

        // 加密
        StringBuilder sb2 = new StringBuilder();
        sb2.append("uri=");

        for (byte t : stringParam.getBytes(StandardCharsets.UTF_8)) {
            if ((t & 0xF0) == 0) {
                sb2.append("0");
            }
            //t & 0xFF 操作是为去除Integer高位多余的符号位（java数据是用补码表示）
            sb2.append(Integer.toHexString(t & 0xFF));
        }
        return sb2.toString();
    }


    /**参数去重*/
    public CaseInsensitiveMap<String, String> dedup(CaseInsensitiveMap<String, String> map){
        Set<String> strings = map.keySet();
        for (String str : strings) {
            map = dedup2(map, str);
        }
        return map;
    }


    /**参数去重
     * @param str 要去重的key
     * */
    public CaseInsensitiveMap<String, String> dedup2(CaseInsensitiveMap<String, String> map,String str){
        String list = map.get(str);
        if(list!=null){
            String[] listSplit = list.split(",");
            HashSet<String> set=new HashSet<>(Arrays.asList(listSplit));
            String targetList = String.join(",", set);
            map.put(str,targetList);
        }else{
            return new CaseInsensitiveMap<String, String>();
        }

        return map;
    }

}
