package cn.rjtech.wms.print;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.map.CaseInsensitiveMap;
import cn.hutool.core.text.StrSplitter;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.jbolt.core.para.JBoltPara;
import cn.jbolt.extend.enums.PrintExecTypeEnum;
import cn.rjtech.base.controller.BaseRestController;
import cn.rjtech.common.model.Organize;
import cn.rjtech.common.model.Printsetting;
import cn.rjtech.common.organize.OrganizeService;
import cn.rjtech.config.AppConfig;
import cn.rjtech.util.ValidationUtils;
import cn.rjtech.wms.utils.DBUtils;
import cn.rjtech.wms.utils.Md5Utils;
import com.alibaba.fastjson.JSONObject;
import com.beust.jcommander.ParameterException;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static cn.hutool.core.text.CharPool.SPACE;
import static cn.hutool.core.text.StrPool.COMMA;

/**
 * @author dhj
 * @version 2021-9-14
 */
@Path(value = "/web/common/print")
public class PrintController extends BaseRestController {

    @Inject
    private PrintService printService;
    @Inject
    private OrganizeService organizeservice;

    /**
     * 根据组织、tag查询相关打印机每次列表
     */
    private void printNameList(HttpServletRequest request) {
        String organizeCode = request.getParameter("organizeCode");
        String tag = request.getParameter("tag");

        try {
            renderJsonSuccess(printService.getPringCodeNameList(organizeCode, tag));
        } catch (Exception e) {
            renderJsonFail(e.getLocalizedMessage());
        }
    }

    public void print(@Para("") JBoltPara para) throws IOException {
//        HttpServletRequest request = getRequest()
//        request.setCharacterEncoding("UTF-8")
//        HttpServletResponse response = getResponse()
//        response.setContentType("application/json;charset=UTF-8")
//        response.setHeader("Access-Control-Allow-Origin", "*")
//        response.setHeader("Access-Control-Allow-Methods", "GET")

        try {
            // 组织
            String organizeCode = para.get("organizeCode") == null ? "" : para.get("organizeCode").toString();
            String reportName = para.get("reportName") == null ? "" : para.get("reportName").toString();
            String printerVersion = para.get("version") == null ? "" : para.get("version").toString();
            String sqlParm = para.get("sqlParm") == null ? "" : para.get("sqlParm").toString();
            String paperSize = para.get("paperSize").toString() == null ? "" : para.get("paperSize").toString();
            String paperOrientation = para.get("paperOrientation") == null ? "" : para.get("paperOrientation").toString();
            String printNum = para.get("num") == null ? "1" : para.get("num").toString();
            String tag = para.get("tag").toString();
            String printerName = para.get("printerName") == null ? "" : para.get("printerName").toString();
            String dbAlias = para.get("dbAlias") == null ? "" : para.get("dbAlias").toString();
            if (null == reportName || "".equals(reportName) || null == organizeCode || "".equals(organizeCode) || null == paperSize || "".equals(paperSize)) {
                if (null != tag && !"".equals(tag)) {
                    Printsetting printSetting = printService.getPrintSetting(tag);
                    if (printSetting == null) {
                        throw new Exception("获取配置信息失败！");
                    }
                    if (null == organizeCode || "".equals(organizeCode)) {
                        organizeCode = printSetting.getOrganizecode();
                    } else if (null == reportName || "".equals(reportName)) {
                        reportName = printSetting.getReportfilename();
                    }
//                    else {
//                        //paperSize=printSetting.getReportpagesize();
//                    }
                }
            }
            if (null == printerVersion || "".equals(printerVersion)) {
                throw new Exception("请检查传人参数！");
            }

            String params = printService.reportPrint(reportName, printerName, printNum, paperSize, paperOrientation, dbAlias, sqlParm);
            if (params != null) {
                String cmd = "rundll32 url.dll,FileProtocolHandler " + AppConfig.getPrintUrl() + printerVersion + "?" + params;
                Process proc = Runtime.getRuntime().exec(cmd);
                proc.waitFor();
                renderJsonSuccess();            } else {
                renderJsonFail(500, "系统错误！");
            }
        } catch (Exception e) {
            renderJsonFail("打印失败!" + e.getLocalizedMessage());
        }
    }

    public void downloadReportFile(@Para(value = "reportname") String fileName,
                                   @Para(value = "md5") String md5) throws IOException {
        if (StrUtil.isBlank(fileName)) {
            renderJsonFail("请填写报表文件名!");
            return;
        }

        String printFilePath = AppConfig.getPrintFilePath() + "/" + fileName + ".grf";
        File file = new File(printFilePath);

        if (!file.exists()) {
            throw new ParameterException("服务器上报表文件不存在!");
        }

        String localmd5 = Md5Utils.getFileMD5(printFilePath);
        // 20221012 潘家宝增加MD5检验，控制是否传输数据下载
        if (ObjUtil.equal(localmd5, md5)) {
            renderJsonSuccess("报表文件相同，不需下载！");
            return;
        }

        HttpServletResponse response = this.getResponse();
        // 设置响应头，控制浏览器下载该文件
        response.reset();
        response.setHeader("content-disposition", "attachment;filename=" + new String((fileName + ".grf").getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1));
        response.setContentType("multipart/form-data");

        IoUtil.copy(IoUtil.toStream(file), response.getOutputStream());

        LOG.info("成功...");
        renderNull();
    }

    @SuppressWarnings("unchecked")
    public void reportData() {
        CaseInsensitiveMap<String, String> params = new CaseInsensitiveMap<String, String>(getKv());
        if (params.get("organizecode") == null || params.get("reportname") == null) {
            renderJsonFail("缺失组织参数organizecode或者模板文件名reportname，请检查！");
            return;
        }
        // 通过报表名获取存储过程名称
        // 文件名
        String fileName = params.get("reportname");

        //参数去重
        printService.dedup(params);

        Printsetting printsetting = printService.getPrintSettingByReportfilename(fileName);

        String sql = printsetting.getExecmethod();
        // 数据库别名key
        String dbalias = printsetting.getDbalias().toLowerCase();

        if (StrUtil.isBlank(sql) || StrUtil.isBlank(dbalias)) {
            renderJsonFail("打印配置表配置有误，请检查！");
            return;
        }

        // 获取组织
        Organize organize = organizeservice.getOrgByCode(params.get("organizecode"));
        if (null == organize) {
            renderJsonFail("组织不存在");
            return;
        }
        if (organize.get(dbalias) == null) {
            renderJsonFail("组织对象上的别名有误，请检查！");
            return;
        }

        String dbaliasVal = organize.get(dbalias);
        // 框架名称
        String erpdbschemas = organize.getErpdbschemas();

        // 打印参数，根据指定的获取方式处理
        switch (PrintExecTypeEnum.toEnum(printsetting.getExectype())) {
            // sql指令（存储过程）
            case SQL:
                JSONObject json = new JSONObject();

                // 判断当前sql是否是存储过程
                if (sql.toLowerCase().startsWith("exec ")) {
                    // exec P_sys_sdf billno,moid,orgcode
                    StringJoiner sj = new StringJoiner("','", "'", "'");
                    // 参数列表
                    Map<String, Object> sqlParamsMap = new HashMap<>();
                    String paramStr = sql.split("\\s+")[2];
                    String[] sqlParams = paramStr.split(",");
                    for (String sqlParam : sqlParams) {
                        if (!params.containsKey(sqlParam)) {
                            ValidationUtils.error("缺失存储过程参数【" + sqlParam + "】，请检查！");
                            return;
                        }
                        String p = params.get(sqlParam) == null ? "" : params.get(sqlParam);
                        sqlParamsMap.put(sqlParam.toLowerCase(), p.replaceAll("＞", ">").replaceAll("＜", "<").replaceAll("＇", "'"));
                        sj.add(p);
                    }

                    LOG.info(sql);

                    String url = params.get("url");

                    // 通过组织获取数据库别名
                    // 执行存储过程
                    try {
                        Map<String, Object> map;
//                        Map<String, Object> map = DBUtils.ExecSql(dbaliasVal, erpdbschemas, sql.split("\\s+")[1].trim(), sqlParamsMap, params.getStr("url"));
                        if (StrUtil.isBlank(url)) {
                            map = DBUtils.ExecSql1(dbaliasVal, erpdbschemas, sql.split("\\s+")[1].trim(), sqlParamsMap);
                        } else {
                            map = DBUtils.ExecSql(dbaliasVal, erpdbschemas, sql.split("\\s+")[1].trim(), sqlParamsMap, params.get("url"));
                        }
                        // Map<String, Object> map = DBUtils.ExecSql(dbaliasVal,erpdbschemas+"."+sql.split("\\s+")[1].trim() + sj.toString(),null,request);
                        Set<String> keySet = map.keySet();
                        Iterator<String> iterator = keySet.iterator();
                        int index = 0;
                        while (iterator.hasNext()) {
                            String key = iterator.next();
                            if (index == 0) {
                                json.put("Master", map.get(key));
                            } else {
                                json.put("Detail" + index, map.get(key));
                            }
                            index += 1;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        renderJsonFail(e.getLocalizedMessage());
                        return;
                    }
                } else {
                    // 普通的sql语句
                    List<Map<String, Object>> list = DBUtils.ExecSql(dbaliasVal, sql, get("url"));
                    json.put("Master", list);
                }
                // 打印数据返回为非标准JSON格式数据
                renderJson(json);
                break;
            case JAVA:
                List<String> classMethod = StrSplitter.split(sql, SPACE, true, true);
                ValidationUtils.isTrue(classMethod.size() >= 3, "参数格式不合法");

                // 示例 Call cn.jbolt._admin.user.UserService findById id

                // 使用enjoy指令进行解析处理
                Kv para = Kv.by("className", classMethod.get(1))
                        .set("methodName", classMethod.get(2))
                        .set("para", getMethodParaList(classMethod,  params));

                String strTemplate = "#set(obj = aop(className))#json(eval(obj, methodName, para))";

                renderText(renderStringTemplateToString(strTemplate, para));
                break;
            default:
                renderJsonFail("未知参数类型");
                break;
        }
    }

    private Okv getMethodParaList(List<String> classMethod, CaseInsensitiveMap<String, String> params) {
        Okv methodPara = Okv.create();

        // 遍历设置参数
        List<String> parameterNames = StrSplitter.split(classMethod.get(classMethod.size() - 1), COMMA, true, true);
        for (String paraName : parameterNames) {
            methodPara.set(paraName, params.get(paraName));
        }
        return methodPara;
    }

}
