package cn.rjtech.common;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.jbolt.core.api.JBoltApplyJWT;
import cn.jbolt.core.util.JBoltStringUtil;
import cn.rjtech.app.user.service.UserAppService;
import cn.rjtech.base.controller.BaseRestController;
import cn.rjtech.common.columsmap.ColumsmapService;
import cn.rjtech.common.model.Organize;
import cn.rjtech.common.organize.OrganizeService;
import cn.rjtech.erp.mood.DataConversion;
import cn.rjtech.util.ValidationUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.weixin.sdk.jfinal.ApiInterceptor;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

@Path(value = "/web/erp/common")
public class CommonController extends BaseRestController {

    @Inject
    private OrganizeService organizeService;
    @Inject
    private ColumsmapService commonService;
    @Inject
    private UserAppService userAppService;

    /**
     * 通过存储过程的方式查询信息 免登陆
     * erp/common/findComponentList
     *
     * @param type      类型
     * @param queryCode 编码
     */
    @JBoltApplyJWT
    public void findComponentListNoLogin(@Para(value = "type") String type,
                                         @Para(value = "queryCode", defaultValue = "") String queryCode,
                                         @Para(value = "queryID", defaultValue = "") String queryId,
                                         @Para(value = "queryName", defaultValue = "") String queryName,
                                         @Para(value = "extData", defaultValue = "") String extData,
                                         @Para(value = "flag", defaultValue = "") String flag,
                                         @Para(value = "organizeCode") String organizeCode,
                                         @Para(value = "userCode") String userCode) {
        if (StrUtil.isBlank(type)) {
            renderJsonFail("请检查传入的类型！");
            return;
        }
        if (StrUtil.isBlank(organizeCode)) {
            renderJsonFail("组织编码不能为空");
            return;
        }
        if (StrUtil.isNotBlank(queryName)) {
            queryId = queryName;
        }

        Organize orgApp = organizeService.getOrgByCode(organizeCode);
        Record userApp = userAppService.getUserByUserCode(orgApp.getErpdbname(), orgApp.getErpdbschemas(), userCode);
        if (null == userApp) {
            renderJsonFail("请重新登录！");
            return;
        }
        Map<String, Object> generalQuerySeting = commonService.generalQuerySeting(type);
        if (null == generalQuerySeting) {
            renderJsonFail("当前类型未配置！");
            return;
        }

        Map<String, Object> paramMap = new HashMap<>(10);
        paramMap.put("erpDBName", orgApp.getErpdbname());
        paramMap.put("erpDBAlias", orgApp.getErpdbalias());//erp数据库别名
        paramMap.put("erpDBSchemas", orgApp.getErpdbschemas());
        paramMap.put("processName", generalQuerySeting.get("process").toString());
        paramMap.put("OrganizeCode", orgApp.getOrganizecode());
        paramMap.put("QueryCode", queryCode);
        paramMap.put("QueryID", queryId);
        paramMap.put("ExtData", extData);
        paramMap.put("Flag", flag);
        paramMap.put("ResultCode", "200");
        paramMap.put("ResultInfo", "resultInfo");

        Map<String, Object> map = commonService.findComponentList(paramMap);
        // Map<String, Object> map = DBUtils.ExecSql(orgApp.getErpDBAlias(), generalQuerySeting.get("process").toString(), paramMap)

        String dataStr = JSON.toJSONString(map.get("list"));
        LOG.info(dataStr);
        JsonElement jsonElement = JsonParser.parseString(dataStr);

        List<Kv> maps = JSONUtil.toList(jsonElement.toString(), Kv.class);
        LOG.info("maps: {}", maps);

        Map<String, Object> result = new HashMap<>();
        result.put("detail", maps);
        result.put("ColumnList", paramMap.get("ResultInfo").toString());
        renderJson(result);
    }

    /**
     * 单据提交
     */
    @Before(ApiInterceptor.class)
    public void vouchProcessSubmit(){
        Map map = commonService.vouchProcessSubmit(getKv());
        if ("200".equals(map.get("code").toString())) {
            renderJsonSuccess(map);
        } else {
            renderJson(Kv.by("code", map.get("code").toString()).set("data", map));
        }
    }

    /**
     * 单据提交
     */
    @JBoltApplyJWT
    public void test(){
        Map map = commonService.tmp(getKv());
    }

    /**
     * 获取工单信息
     * erp/common/getBillInfo
     */
    @JBoltApplyJWT
    public void getBillInfo(){
        Kv kv = getKv();
        String barCode = kv.getStr("barCode");
        String vouchCode = kv.getStr("vouchCode");
        String organizeCode = kv.getStr("organizeCode");
        Organize orgApp = organizeService.getOrgByCode(kv.getStr("organizeCode"));
        List<Record> list = commonService.findInfoFlag(vouchCode);
        Map<String, String> map = new Hashtable<>();
        List<Map<String,Object>> record = commonService.getBillInfo(orgApp, barCode, list.get(0).getStr("billinfoflag"));
        Map data = new HashMap();
        data.put("detail", record);
        renderJsonSuccess(data);
    }

    /**
     * 获取条码信息
     * erp/common/getBarcodeInfo
     */
    @JBoltApplyJWT
    public void getBarcodeInfo(){
        Kv kv = getKv();
        String barCode = kv.getStr("barCode");
        String vouchCode = kv.getStr("vouchCode");
        String posCode = kv.getStr("PosCode");
        String infoFlag = kv.getStr("infoFlag");
        String billno = kv.getStr("billno");
        String organizeCode = kv.getStr("organizeCode");
        Organize orgApp = organizeService.getOrgByCode(organizeCode);
        String barcodeInfoFlag = infoFlag;
        Map data = new HashMap();
        if(vouchCode.length() != 0 ) {
            List<Record> list = commonService.findInfoFlag(vouchCode);
            barcodeInfoFlag = list.get(0).getStr("barcodeinfoflag");
            if (JBoltStringUtil.isNotBlank(barcodeInfoFlag) && barcodeInfoFlag.indexOf("@") >= 0) {
                //替换送货单条码
                if (barcodeInfoFlag.indexOf("@billNo") >= 0) {
                    barcodeInfoFlag = barcodeInfoFlag.replace("@billNo", billno);
                }
            }
            List<Map<String, Object>> barcodeInfo = commonService.getBarcodeInfo(orgApp, barCode, posCode, barcodeInfoFlag);
            data.put("detail",barcodeInfo);
        }
        //commonService.getBarcodeInfo();
        renderJsonSuccess(data);
    }

    @JBoltApplyJWT
    public void getVouchTypeList(){
        Kv kv = getKv();
        String vouchCode = kv.getStr("vouchCode");
        Record record = commonService.findInfoFlag(vouchCode).get(0);
        Map data = new HashMap();
        data.put("detail", record);
        renderJsonSuccess(data);
    }

    /**
     * 通过存储过程的方式查询信息
     * erp/common/findComponentList
     */
    @JBoltApplyJWT
    public void findComponentList(){
        Map result =new HashMap();
        Kv kv = getKv();
        String type = kv.getStr("type");//类型
        String queryCode = kv.getStr("queryCode");//编码
        String queryID = kv.getStr("queryID");//名称
        String queryName = kv.getStr("queryName");
        String extData = kv.getStr("extData");
        String flag = kv.getStr("flag");
        if (JBoltStringUtil.isNotBlank(queryName)){
            queryID=queryName;
        }
        ValidationUtils.notBlank(type, "请检查传入的类型！");
        Map<String, Object> generalQuerySeting = commonService.generalQuerySeting(type);
        ValidationUtils.notNull(generalQuerySeting, "当前类型未配置！");

        Organize orgApp = organizeService.getOrgByCode(kv.getStr("organizeCode"));
        Map paramMap=new HashMap();
        paramMap.put("erpDBName",orgApp.getErpdbname());
        paramMap.put("erpDBSchemas",orgApp.getErpdbschemas());
        paramMap.put("processName",generalQuerySeting.get("process").toString());
        paramMap.put("OrganizeCode",orgApp.getOrganizecode());
        paramMap.put("QueryCode",queryCode);
        paramMap.put("QueryID",queryID);
        paramMap.put("ExtData",extData);
        paramMap.put("Flag",flag);
        paramMap.put("erpdbalias",orgApp.getErpdbalias());
        paramMap.put("ResultCode","resultCode");
        paramMap.put("ResultInfo","resultInfo");

        Map map = commonService.findComponentList(paramMap);
        String dataStr = JSONObject.toJSONString(map.get("list"));
        System.out.println(dataStr);
        JsonElement jsonElement = new JsonParser().parse(dataStr);
        List<Map<String, Object>> maps = DataConversion.toListMap(jsonElement.toString());
        result.put("detail",maps);
        result.put("ColumnList",paramMap.get("ResultInfo").toString());
        renderJsonSuccess(result);
    }

}
