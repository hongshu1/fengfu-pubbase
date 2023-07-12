package cn.rjtech.common.columsmap;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.CaseInsensitiveMap;
import cn.hutool.core.text.StrSplitter;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.OrgAccessKit;
import cn.jbolt.core.kit.U8DataSourceKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.util.JBoltStringUtil;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.app.user.service.UserAppService;
import cn.rjtech.common.columsmapdetail.ColumsmapdetailService;
import cn.rjtech.common.exchangetable.ExchangeTableService;
import cn.rjtech.common.model.*;
import cn.rjtech.common.organize.OrganizeService;
import cn.rjtech.common.tsyslog.TsysLogService;
import cn.rjtech.common.vouchprocessnote.VouchProcessNoteService;
import cn.rjtech.common.vouchrollbackref.VouchRollBackRefService;
import cn.rjtech.constants.DataSourceConstants;
import cn.rjtech.constants.ErrorMsg;
import cn.rjtech.erp.mood.DataConversion;
import cn.rjtech.util.ValidationUtils;
import cn.rjtech.wms.utils.HttpApiUtils;
import cn.rjtech.wms.utils.ListUtils;
import cn.rjtech.wms.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.io.IOException;
import java.sql.*;
import java.util.Date;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static cn.hutool.core.util.StrUtil.COMMA;

/**
 * 字段映射 Service
 *
 * @ClassName: ColumsmapService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-12-07 15:15
 */
public class ColumsmapService extends BaseService<Columsmap> {

    private final Columsmap dao = new Columsmap().dao();

    @Inject
    private OrganizeService organizeService;
    @Inject
    private UserAppService userAppService;
    @Inject
    private ColumsmapdetailService columsmapdetailService;
    @Inject
    private ExchangeTableService exchangeTableService;
    @Inject
    private VouchProcessNoteService vouchProcessNoteService;
    @Inject
    private TsysLogService tsysLogService;
    @Inject
    private VouchRollBackRefService vouchRollBackRefService;

    @Override
    protected Columsmap dao() {
        return dao;
    }

    /**
     * 后台管理分页查询
     */
    public Page<Columsmap> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
        return paginateByKeywords("id", "desc", pageNumber, pageSize, keywords, "name");
    }

    /**
     * 保存
     */
    public Ret save(Columsmap columsmap) {
        if (columsmap == null || isOk(columsmap.getAutoid())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }

        tx(() -> {
            // ValidationUtils.isTrue(notExists(columnName, value), JBoltMsg.DATA_SAME_NAME_EXIST);
            ValidationUtils.isTrue(columsmap.save(), ErrorMsg.SAVE_FAILED);


            // TODO 其他业务代码实现

            return true;
        });

        // 添加日志
        // addSaveSystemLog(columsmap.getAutoid(), JBoltUserKit.getUserId(), columsmap.getName());
        return SUCCESS;
    }

    /**
     * 更新
     */
    public Ret update(Columsmap columsmap) {
        if (columsmap == null || notOk(columsmap.getAutoid())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }

        tx(() -> {
            // 更新时需要判断数据存在
            Columsmap dbColumsmap = findById(columsmap.getAutoid());
            ValidationUtils.notNull(dbColumsmap, JBoltMsg.DATA_NOT_EXIST);

            // TODO 其他业务代码实现
            // ValidationUtils.isTrue(notExists(columnName, value), JBoltMsg.DATA_SAME_NAME_EXIST);

            ValidationUtils.isTrue(columsmap.update(), ErrorMsg.UPDATE_FAILED);

            return true;
        });

        //添加日志
        //addUpdateSystemLog(columsmap.getAutoid(), JBoltUserKit.getUserId(), columsmap.getName());
        return SUCCESS;
    }

    /**
     * 删除 指定多个ID
     */
    public Ret deleteByBatchIds(String ids) {
        tx(() -> {
            for (String idStr : StrSplitter.split(ids, COMMA, true, true)) {
                long AutoID = Long.parseLong(idStr);
                Columsmap dbColumsmap = findById(AutoID);
                ValidationUtils.notNull(dbColumsmap, JBoltMsg.DATA_NOT_EXIST);

                // TODO 可能需要补充校验组织账套权限
                // TODO 存在关联使用时，校验是否仍在使用

                ValidationUtils.isTrue(dbColumsmap.delete(), JBoltMsg.FAIL);
            }

            return true;
        });

        // 添加日志
        // Columsmap columsmap = ret.getAs("data");
        // addDeleteSystemLog(AutoID, JBoltUserKit.getUserId(), SystemLog.TARGETTYPE_xxx, columsmap.getName());
        return SUCCESS;
    }

    /**
     * 删除数据后执行的回调
     *
     * @param columsmap 要删除的model
     * @param kv        携带额外参数一般用不上
     */
    @Override
    protected String afterDelete(Columsmap columsmap, Kv kv) {
        //addDeleteSystemLog(columsmap.getAutoid(), JBoltUserKit.getUserId(),columsmap.getName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param columsmap 要删除的model
     * @param kv        携带额外参数一般用不上
     */
    @Override
    public String checkCanDelete(Columsmap columsmap, Kv kv) {
        //如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
        return checkInUse(columsmap, kv);
    }

    /**
     * 设置返回二开业务所属的关键systemLog的targetType
     */
    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

    /**
     * 根据组织代码,来源类型,项目编码筛选数据
     *
     * @param organizeCode 组织代码
     * @param sourceTupe   来源类型
     * @param itemCode     项目编码
     */
    public List<Columsmap> getColumsmapList(String organizeCode, String sourceTupe, String itemCode) {
        Columsmap columsmap1 = new Columsmap();
        columsmap1.setOrganizecode(organizeCode);
        columsmap1.setSourcetype(sourceTupe);

        List<Columsmap> list = findList(columsmap1);
        if (CollUtil.isEmpty(list)) {
            Columsmap columsmap2 = new Columsmap();
            columsmap2.setItemcode(itemCode);
            columsmap2.setSourcetype(sourceTupe);
            list = findList(columsmap2);
        }
        return list;
    }

    /**
     * 通过实体的参数自动匹配List
     */
    public List<Columsmap> findList(Columsmap columsmap) {
        Kv para = Kv.create().set(columsmap.toMap());
        return daoTemplate("columsmap.findList", para).find();
    }

    /**
     * 通过单据代码查询单据类型信息
     *
     * @param vouchCode 单据代码
     * @return 单据类型信息
     */
    public List<Record> findInfoFlag(String vouchCode) throws RuntimeException {
        ValidationUtils.notBlank(vouchCode, "任务不存在,请检查传入的参数!");
        List<Record> list = vouchType(vouchCode);
        ValidationUtils.isTrue(!CollUtil.isEmpty(list), vouchCode + ":没有东西的信息!");
        return list;
    }

    /**
     * 根据类型查询存储过程
     */
    public Map<String, Object> generalQuerySeting(String type) throws RuntimeException {
        List<Record> maps = generalQuerySeting();
        Record map = new Record();
        for (int i = 0; i < maps.size(); i++) {
            String[] types = StringUtils.split(maps.get(i).get("type").toString(), ',');
            for (int j = 0; j < types.length; j++) {
                if (types[j].equals(type)) {
                    map = maps.get(i);
                    return map.toMap();
                }
            }
        }
        return map.toMap();
    }

    public List<Record> vouchType(String vouchCode) {
        Kv kv = Kv.by("vouchCode", vouchCode);
        return dbTemplate("columsmap.vouchType", kv).find();
    }

    public List<Record> vouchTypeF(String vouchCode) {
        Kv kv = Kv.by("vouchCode", vouchCode);
        return dbTemplate("columsmap.vouchTypeF", kv).find();
    }

    public List<Record> generalQuerySeting() {
        return dbTemplate("columsmap.generalQuerySeting").find();
    }

    public Map<String, Object> findComponentList(Map<String, Object> map) throws RuntimeException {
        Map<String, Object> result = new HashMap<>(1);
        List<Map<String, Object>> componentList = daofindComponentList(map);
        ValidationUtils.isTrue("200".equals(map.get("ResultCode").toString()), map.get("ResultInfo").toString());
        result.put("list", componentList);
        return result;
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> daofindComponentList(Map<String, Object> paraMap) {
        String erpDBName = (String) paraMap.get("erpDBName");
        String erpDBSchemas = (String) paraMap.get("erpDBSchemas");
        String processName = (String) paraMap.get("processName");
        String erpdbalias = paraMap.get("erpdbalias").toString();

        Kv kv = Kv.create();
        kv.set(paraMap);
        kv.remove("erpDBName");
        kv.remove("erpDBSchemas");
        kv.remove("processName");

        return (List<Map<String, Object>>) executeFunc(erpdbalias, (conn) -> {
            List<Map<String, Object>> list = new ArrayList<>();
            String sql = "{ call " + erpDBName + "." + erpDBSchemas + "." + processName + "(?, ?, ?, ?, ?, ?, ?) }";
            LOG.info(sql);
            CallableStatement proc = conn.prepareCall(sql);
            proc.setString(1, (String) kv.getOrDefault("OrganizeCode", ""));
            proc.setString(2, (String) kv.getOrDefault("QueryCode", ""));
            proc.setString(3, (String) kv.getOrDefault("QueryID", ""));
            proc.setString(4, (String) kv.getOrDefault("ExtData", ""));
            proc.setString(5, (String) kv.getOrDefault("Flag", ""));
            // 注册输出参数
            proc.registerOutParameter(6, Types.VARCHAR);
            proc.registerOutParameter(7, Types.VARCHAR);
            //执行
            boolean isSuccess = true;
            ResultSet rs = null;
            try {
                rs = proc.executeQuery();
            } catch (SQLException e) {
                isSuccess = false;
            }
            //ResultSet rs = proc.getResultSet();

            // 获取结果集列名
            if (isSuccess) {
                ResultSetMetaData rsm = rs.getMetaData();
                List<String> allColumn = new ArrayList<>();
                // 获取结果集列名集合
                int cH = 1;
                while (cH <= rsm.getColumnCount()) {
                    String columnName = rsm.getColumnName(cH).toLowerCase();
                    allColumn.add(columnName);
                    cH++;
                }
                while (rs.next()) {
                    Map<String, Object> map = new HashMap<>(10);
                    int i = 1;
                    while (i <= allColumn.size()) {
                        String string = rs.getString(i);
                        map.put(allColumn.get(i - 1), string);
                        i++;
                    }
                    list.add(map);
                }
            }
            paraMap.put("ResultCode", proc.getString(6));
            paraMap.put("ResultInfo", proc.getString(7));
            ValidationUtils.isTrue("200".equals(paraMap.get("ResultCode")), paraMap.get("ResultInfo").toString());

            return list;
        });
    }

    /**
     * 获取单据信息
     *
     * @param flag         查看标志
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getBillInfo(Organize orgApp, String barCode, String flag) {
        HashMap<String, Object> paraMap = new HashMap<>();

        return (List<Map<String, Object>>) executeFunc(orgApp.getErpdbalias(), (conn) -> {
            List<Map<String, Object>> list = new ArrayList<>();
            String sql = "{ call " + orgApp.getErpdbname() + "." + orgApp.getErpdbschemas() + ".P_Sys_GetBillInfo" + "(?, ?, ?, ?, ?) }";
            LOG.info(sql);
            CallableStatement proc = conn.prepareCall(sql);
            proc.setString(1, orgApp.getOrganizecode());
            proc.setString(2, barCode);
            proc.setString(3, flag);
            // 注册输出参数
            proc.registerOutParameter(4, Types.VARCHAR);
            proc.registerOutParameter(5, Types.VARCHAR);
            //执行
            boolean isSuccess = true;
            ResultSet rs = null;
            try {
                rs = proc.executeQuery();
            } catch (SQLException e) {
                isSuccess = false;
            }
            if (isSuccess) {
                // 获取结果集列名
                ResultSetMetaData rsm = rs.getMetaData();
                List<String> allColumn = new ArrayList<>();
                // 获取结果集列名集合
                int cH = 1;
                while (cH <= rsm.getColumnCount()) {
                    String columnName = rsm.getColumnName(cH).toLowerCase();
                    allColumn.add(columnName);
                    cH++;
                }
                while (rs.next()) {
                    Map<String, Object> map = new HashMap<>(10);
                    int i = 1;
                    while (i <= allColumn.size()) {
                        String string = rs.getString(i);
                        map.put(allColumn.get(i - 1), string);
                        i++;
                    }
                    list.add(map);
                }
            }

            paraMap.put("resultCode", proc.getString(4));
            paraMap.put("resultInfo", proc.getString(5));
            ValidationUtils.isTrue("200".equals(paraMap.get("resultCode")), paraMap.get("resultInfo").toString());
            return list;
        });
    }

    /**
     * 获取条码信息
     *
     * @param posCode
     * @param flag    查询标志
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getBarcodeInfo(Organize orgApp, String barCode, String posCode, String flag) {
        HashMap<String, Object> paraMap = new HashMap<>();

        return (List<Map<String, Object>>) executeFunc(orgApp.getErpdbalias(), (conn) -> {
            List<Map<String, Object>> list = new ArrayList<>();
            String sql = "{ call " + orgApp.getErpdbname() + "." + orgApp.getErpdbschemas() + ".P_Sys_GetBarcodeInfo" + "(?, ?, ?, ?, ?, ?, ?) }";
            LOG.info(sql);
            CallableStatement proc = conn.prepareCall(sql);
            proc.setString(1, orgApp.getOrganizecode());
            proc.setString(2, barCode);
            proc.setString(3, "");
            proc.setString(4, JBoltStringUtil.isBlank(posCode) ? "" : posCode);
            proc.setString(5, JBoltStringUtil.isBlank(flag) ? "" : flag);
            // 注册输出参数
            proc.registerOutParameter(6, Types.VARCHAR);
            proc.registerOutParameter(7, Types.VARCHAR);
            ResultSet rs = null;
            boolean isSuccess = true;
            //执行
            try {
                rs = proc.executeQuery();
            } catch (SQLException e) {
                isSuccess = false;
            }
            if (isSuccess) {
                // 获取结果集列名
                ResultSetMetaData rsm = rs.getMetaData();
                List<String> allColumn = new ArrayList<>();
                // 获取结果集列名集合
                int cH = 1;
                while (cH <= rsm.getColumnCount()) {
                    String columnName = rsm.getColumnName(cH).toLowerCase();
                    allColumn.add(columnName);
                    cH++;
                }
                while (rs.next()) {
                    Map<String, Object> map = new HashMap<>(10);
                    int i = 1;
                    while (i <= allColumn.size()) {
                        String string = rs.getString(i);
                        map.put(allColumn.get(i - 1), string);
                        i++;
                    }
                    list.add(map);
                }
            }

            paraMap.put("resultCode", proc.getString(6));
            paraMap.put("resultInfo", proc.getString(7));
            ValidationUtils.isTrue("200".equals(paraMap.get("resultCode")), paraMap.get("resultInfo").toString());
            return list;
        });
    }

    @SuppressWarnings("unchecked")
    public Kv vouchProcessSubmit(Kv k){
        CaseInsensitiveMap<String, Object> kv = new CaseInsensitiveMap<String, Object>(k);
        String sourceJson = k.toJson();

        Kv result = Kv.create();

        Date now = new Date();

        String orgCode = (String) kv.get("organizecode");

        Organize orgApp = organizeService.getOrgByCode(orgCode);

        // 对U8数据源别名做特殊处理，如果是u8开头的数据源别名，结合组织编码定义数据源别名，如u8001
        String erpDbAlias = StrUtil.startWith(orgApp.getErpdbalias(), "u8") ? U8DataSourceKit.ME.use(orgApp.getOrganizecode()) : orgApp.getErpdbalias();

        OrgAccessKit.setOrgCode(orgCode);
        OrgAccessKit.setOrgId(orgApp.getId());
        OrgAccessKit.setU8DbAlias(erpDbAlias);

        Record userApp = userAppService.getUserByUserCode(erpDbAlias, orgApp.getErpdbname(), orgApp.getErpdbschemas(), (String) kv.get("usercode"));
        ValidationUtils.notNull(userApp, "当前组织下没有该用户！");

        String erpDBName = orgApp.getErpdbname();
        
        // 业务id
        String vouchBusinessID = JBoltSnowflakeKit.me.nextIdStr();

        // 获取所有钟工组件返回的u8单据类型值
        JSONObject preAllocate = (JSONObject) kv.get("preallocate");

        //通过数据的分组id，将数据分成多组，分别提交
        Map<Object, List<Object>> dataGroup = ((JSONArray) kv.get("maindata")).stream().collect(Collectors.groupingBy(p -> ((Map<?, ?>) p).get("GroupFlag") == null ? "10" : ((Map<?, ?>) p).get("GroupFlag"), Collectors.toList()));

        Set<Object> groupFlags = new TreeSet<>(dataGroup.keySet());

        for (Object flag : groupFlags) {
            // 获取主数据
            JSONArray mainData = JSONArray.parseArray(JSON.toJSONString(dataGroup.get(flag)));//(JSONArray) kv.get("maindata");
            // 获取详细数据
            JSONArray detailData = (JSONArray) kv.get("DetailData");
            // 获取扩展数据
            JSONArray extData = (JSONArray) kv.get("ExtData");

            ValidationUtils.notEmpty(mainData, "detail没有数据");

            Map<String, Object> plugeReturnMap = ListUtils.dealWithReturnData(preAllocate);

            String type = plugeReturnMap.get("type").toString();
            // List<Record> list = findInfoFlag(type)
            // String synergyTag = list.get(0).getStr("synergytag")

            // 获取对应
            Record vouchType = findInfoFlag(type).get(0);
            Integer id1 = vouchType.getInt("autoid");
            // 获取所有流程
            List<Record> processBusMaps = processBus(id1);
            // 流程执行
            plugeReturnMap.put("ProcessType", vouchType.get("processtype"));
            plugeReturnMap.put("CreatePersonName", userApp.getStr("u8_name"));
            plugeReturnMap.put("CreatePerson", userApp.getStr("u8_code"));
            plugeReturnMap.put("organizeCode", kv.get("organizecode"));
            plugeReturnMap.put("password", userApp.getStr("u8_pwd"));

            AtomicInteger currentSeq = new AtomicInteger();

            DataConversion dataConversion = new DataConversion(this, columsmapdetailService);
            try {
                Map<Object, List<Record>> groupProcessDatas = processBusMaps.stream().collect(
                        Collectors.groupingBy(p -> p.get("groupseq") == null ? "10" : p.get("groupseq"), Collectors.toList())
                );//通过配置表的groupseq分组

                Set<Object> groupSeqs = new TreeSet<>(groupProcessDatas.keySet());

                for (Object groupSeq : groupSeqs) {
                    // 取出分组中的流程
                    List<Record> processBusList = groupProcessDatas.get(groupSeq);

                    for (int i = 0; i < processBusList.size(); i++) {
                        Record processBusMap = processBusList.get(i);

                        currentSeq.set(processBusMap.getInt("seq"));

                        // 预制项配置
                        plugeReturnMap.put("InitializeMapID", processBusMap.get("initializemapid"));

                        // 判断流程是否可用
                        if (StrUtil.isBlank(processBusMap.get("pcloseperson")) && StrUtil.isBlank(processBusMap.get("bcloseperson"))) {

                            Record processBusPre = i > 0 ? processBusList.get(i - 1) : null;

                            // ---------------------------------------------------------
                            // ERP 生单、审单事务上下文开始
                            // ---------------------------------------------------------
                            try {
                                txInErp(erpDbAlias, erpDBName, vouchType, vouchBusinessID, orgApp, processBusMap, dataConversion, userApp, processBusPre, type, plugeReturnMap, result, mainData, detailData, extData, sourceJson, now);
                                if (result.containsKey("isBreak") && result.getInt("isBreak") == 1) {
                                    break;
                                }
                            } catch (Exception e) {
                                LOG.error(e.getLocalizedMessage());
                                e.printStackTrace();
                                // 这里不打印异常信息，处理回滚外层事务
                                return result;
                            }
                        }
                    }
                }
            } finally {
                if (!"200".equals(result.getStr("code"))) {
                    rollbackProcess(currentSeq.get(), vouchBusinessID, processBusMaps, dataConversion, userApp, type);
                }
            }
        }

        return result;
    }

    /**
     * @param currentSeq     当前执行流程的序号
     * @param currentSeq     流程id
     * @param processBusMaps 流程集合
     */
    private void rollbackProcess(Integer currentSeq, String seqBusinessID, List<Record> processBusMaps, DataConversion dataConversion, Record userApp, String type) {
        while (currentSeq > 10) {
            Integer finalCurrentSeq = currentSeq;
            List<Record> list = processBusMaps.stream().filter(p -> Objects.equals(p.getInt("seq"), finalCurrentSeq)).collect(Collectors.toList());
            if (!list.isEmpty()) {
                String processID = list.get(0).getStr("autoid");
                Record rollback = vouchRollBackRefService.rollback(processID);
                if (rollback != null) {
                    if (list.get(0).getStr("memo").contains("审核")) {
                        processID = processBusMaps.stream().filter(p -> p.getInt("seq") == (finalCurrentSeq - 10)).collect(Collectors.toList()).get(0).getStr("autoid");
                    }
                    List<Record> rollbackref = vouchRollBackRefService.rollbackref(seqBusinessID, processID);
                    if (rollbackref.isEmpty()) {
                        currentSeq -= 10;
                        continue;
                    }
                    String processvalue = rollbackref.get(0).getStr("processvalue");
                    // map2=JSON.parseObject(processvalue,HashMap.class);
                    JSONObject rojsonObject = JSONObject.parseObject(processvalue);
                    LOG.info("{}", rojsonObject);
                    //回滚api
                    Map roapimap = new HashMap();
                    roapimap.put("tag", rollback.get("tag"));
                    roapimap.put("url", rollback.get("url"));
                    List<Map<String, String>> roapimaps = new ArrayList<>();
                    roapimaps.add(roapimap);
                    //u9调用
                    if (rojsonObject != null){
                        String message;
                        try {
                            message = HttpApiUtils.u8Api(roapimap.get("url").toString(), rojsonObject);
                            Map processResult = dataConversion.processResult("u9erprecdelete", message);
                            LOG.info("回滚：" + message);
                            String resultCode = processResult.get("resultCode").toString();
                            message = processResult.get("resultInfo").toString();
                            //日志保存
                            Log log = new Log();
                            String source = rojsonObject.toString();
                            String memo = "系统回滚";
                            String logUrl = roapimap.get("url").toString();
                            String logResult = message;
                            dataConversion.recordLog("", seqBusinessID, type, String.valueOf(currentSeq), "", "", 1, source, logResult, logUrl, memo, userApp.getStr("user_code"), log);
                            tsysLogService.save(log);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            currentSeq -= 10;
        }
    }

    /**
     *
     */
    private void txInU9(String erpDbAlias, String erpDBName, String vouchBusinessID, Organize orgApp, Record processBusMap, String processFlag, String ExchangeId, DataConversion dataConversion, Record userApp, Log tsysLog, Map plugeReturnMap, Kv result) {
        tx(erpDbAlias, Connection.TRANSACTION_READ_UNCOMMITTED, () -> {

            // 调用相关存储过程
            Record map = StoredProcedure(erpDbAlias, erpDBName, orgApp.getErpdbschemas(), processBusMap.getStr("processname"), processFlag, ExchangeId);
            result.set("resultExchangeID", map.get("resultexchangeid"));
            result.set("message", map.get("resultinfo"));
            if (map.get("resultExchangeID") == null) {
                int state = 1;
                result.set("state", state).set("code", map.getStr("resultcode"));

                // 日志记录
                String source = "ProcessName:" + processBusMap.getStr("processname") + "ProcessFlag:" + processFlag + "";
                String memo = "存储过程调用";
                // String logResult="resultCode:"+map.get("resultCode")+"resultInfo:"+map.get("resultInfo");
                String logResult = map.toString();
                String logUrl = "";
                dataConversion.recordLog(orgApp.getOrganizecode(), vouchBusinessID, tsysLog.getVouchtype(), processBusMap.getStr("seq"), ExchangeId, "", state, source, logResult, logUrl, memo, userApp.getStr("user_code"), tsysLog);
                tsysLogService.saveLogInTx(tsysLog);
                // message = map.getStr("resultInfo")
                result.set("message", map.getStr("resultInfo"));

            } else {
                Map<String, List<Record>> recordMap;

                try {
                    recordMap = queryExchange(erpDBName, orgApp.getErpdbschemas(), result.getStr("resultExchangeID"));
                } catch (Exception e) {
                    e.printStackTrace();
                    // 返回错误信息
                    result.set("code", "201").set("message", e.getLocalizedMessage()).set("state", 1);
                    // 事务回滚
                    return false;
                }

                LOG.info("{}", recordMap);
                plugeReturnMap.put("ProcessMapID", processBusMap.getStr("initializemapid"));
                if (null != processBusMap.getStr("url") && "Create".equals(processBusMap.getStr("tag"))) {
                    JSONObject jsonData = dataConversion.InterfaceTransform("", plugeReturnMap, recordMap);

                    // 创建api信息
                    Map apimap = new HashMap();
                    apimap.put("tag", processBusMap.get("tag"));
                    apimap.put("url", processBusMap.get("url"));

                    List<Map<String, String>> apimaps = new ArrayList<>();
                    apimaps.add(apimap);

                    // u9调用
                    String message = null;
                    try {
                        message = HttpApiUtils.u8Api(processBusMap.get("url"), jsonData);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // 返回解析
                    Map processResult = dataConversion.processResult(processBusMap.getStr("resulttype"), message);
                    String resultCode = JBoltStringUtil.isBlank(processResult.get("resultCode").toString()) ? "201" : processResult.get("resultCode").toString();
                    String resultInfo = JBoltStringUtil.isBlank(processResult.get("resultInfo").toString()) ? "未知原因" : processResult.get("resultInfo").toString();
                    message = resultInfo;

                    //单据处理节点信息
                    VouchProcessNote vouchprocessnote = new VouchProcessNote();
                    vouchprocessnote.setVouchbusinessid(vouchBusinessID);
                    vouchprocessnote.setSeq(processBusMap.getInt("seq"));
                    vouchprocessnote.setPreallocate(plugeReturnMap.toString());
                    vouchprocessnote.setExchangeid(map.get("resultExchangeID"));
                    vouchprocessnote.setReturnvalue(jsonData.toString());
                    vouchprocessnote.setCreatedate(new Date());
                    vouchProcessNoteService.saveVouchprocessnoteInTx(vouchprocessnote);

                    //回滚参照信息
                    VouchRollBackRef vouchrollbackref = new VouchRollBackRef();
                    vouchrollbackref.setVouchbusinessid(vouchBusinessID);
                    vouchrollbackref.setState("0");
                    vouchrollbackref.setProcessid(processBusMap.get("autoid").toString());
                    vouchrollbackref.setCreateperson(userApp.getStr("usercode"));
                    vouchrollbackref.setCreatedate(new Date());
                    vouchRollBackRefService.saveVouchrollbackref(vouchrollbackref);

                    //创建是否成功
                    if ("200".equals(resultCode)) {
                        result.set("state", 0);
                        plugeReturnMap.put("VouchId", processResult.get("docID"));
                        result.put("message", resultInfo);
                        result.put("dommessage", processResult.get("domMsg"));
                        /*String docId = processResult.get("docID") == null ? null : processResult.get("docID").toString();
                        if (null != docId) {
                            plugeReturnMap.put("ResultDocID", docId);
                        }
                        plugeReturnMap.put("ResultDocNo", processResult.get("docNo"));
                        result.set("code", resultCode).set("message", message);*/
                    } else {
                        result.set("state", 1);
                        // 日志记录
                        String source = jsonData.toString();
                        String memo = "系统生单";
                        String logResult = message;
                        String logUrl = apimap.get("url").toString();
                        dataConversion.recordLog(orgApp.getOrganizecode(), vouchBusinessID, tsysLog.getVouchtype(), processBusMap.get("seq").toString(), ExchangeId, map.get("resultExchangeID"), result.getInt("state"), source, logResult, logUrl, memo, userApp.getStr("user_code"), tsysLog);
                        tsysLogService.saveLogInTx(tsysLog);

                        // 返回错误信息
                        result.set("code", resultCode).set("message", logResult);
                        // 提交事务
                        return false;
                    }

                    // 日志记录
                    String source = jsonData.toString();
                    String memo = "系统生单";
                    String logResult = message;
                    String logUrl = apimap.get("url").toString();
                    dataConversion.recordLog(orgApp.getOrganizecode(), vouchBusinessID, tsysLog.getVouchtype(), processBusMap.get("seq").toString(), ExchangeId, map.get("resultExchangeID"), result.getInt("state"), source, logResult, logUrl, memo, userApp.getStr("user_code"), tsysLog);
                    tsysLogService.saveLogInTx(tsysLog);
                }
            }
            /*if (!"200".equals(result.getStr("code"))){
                throw new RuntimeException(result.getStr("message"));
            }*/
            return true;
        });
    }

    private void txInErp(String erpDbAlias, String erpDBName, Record vouchType, String vouchBusinessID, Organize orgApp, Record processBusMap, DataConversion dataConversion, Record userApp, Record processBusPre, String type, Map plugeReturnMap, Kv result, JSONArray mainData, JSONArray detailData, JSONArray extData, String sourceJson, Date now) {
        // 日志操作记录
        List<Log> saveLogs = new ArrayList<>();
        // 交换表数据保存
        List<ExchangeTable> saveExchangeTables = new ArrayList<>();
        // 单据处理节点信息
        List<VouchProcessNote> saveVouchProcessNotes = new ArrayList<>();
        // 单据回滚日志参照表
        List<VouchRollBackRef> saveVouchRollBackRefs = new ArrayList<>();

        List<VouchRollBackRef> updateVouchRollBackRefs = new ArrayList<>();
        
        try {
            // 保存交换表步骤
            if (StrUtil.isBlank(processBusMap.get("processname")) && ObjUtil.isNull(processBusMap.get("url"))) {
                // 创建数据动态交换表list
                List<ExchangeTable> dt = dataConversion.getTSysExchangetable(type, plugeReturnMap, mainData, detailData, extData);
                saveExchangeTables.addAll(dt);
                
                VouchProcessNote note = new VouchProcessNote();
                note.setVouchbusinessid(vouchBusinessID);
                note.setSeq(processBusMap.getInt("seq"));
                note.setExchangeid(dt.get(0).getExchangeID());
                note.setCreatedate(now);
                saveVouchProcessNotes.add(note);

                String vouchtypeId = vouchType.getStr("vouchcode");
                String memo = "Exchangeid:" + dt.get(0).getExchangeID();

                Log tsysLog = new Log();

                dataConversion.recordLog(orgApp.getOrganizecode(), vouchBusinessID, vouchtypeId, processBusMap.get("seq").toString(), "", dt.get(0).getExchangeID(), 0, sourceJson, "", "", memo, userApp.getStr("user_code"), tsysLog);
                saveLogs.add(tsysLog);
            }

            // flag级别使用
            String processFlag = processFlag(processBusMap);

            // ------------------------------
            // ERP 生单开始
            // ------------------------------

            if (StrUtil.isNotBlank(processBusMap.get("processname"))) {
                // 获取节点信息
                List<Record> datamap = vouchProcessNoteService.processNote(processBusMap.getStr("exechgeseq"), vouchBusinessID);
                String ExchangeId = datamap.get(datamap.size() - 1).getStr("exchangeid");

                Log log = new Log().setVouchtype(vouchType.getStr("vouchcode")).setVouchstep(processBusMap.getStr("seq"));

                // 调用相关存储过程
                Record map = StoredProcedure(erpDbAlias, erpDBName, orgApp.getErpdbschemas(), processBusMap.getStr("processname"), processFlag, ExchangeId);
                result.set("resultExchangeID", map.get("resultexchangeid"));
                result.set("message", map.get("resultinfo"));

                if (map.get("resultExchangeID") == null) {
                    result.set("state", 1)
                            .set("code", map.getStr("resultcode"))
                            .set("message", map.getStr("resultInfo"));

                    // 日志记录
                    String source = "ProcessName:" + processBusMap.getStr("processname") + "ProcessFlag:" + processFlag + "";
                    String memo = "存储过程调用";
                    String logResult = map.toString();
                    String logUrl = "";
                    dataConversion.recordLog(orgApp.getOrganizecode(), vouchBusinessID, log.getVouchtype(), processBusMap.getStr("seq"), ExchangeId, "", result.getInt("state"), source, logResult, logUrl, memo, userApp.getStr("user_code"), log);
                    saveLogs.add(log);

                    // 失败，则提回滚 返回错误信息
                    if (result.getInt("state") == 1 && "200".equals(map.getStr("resultcode"))) {
                        //成功，但是可以跳出当前步骤或者跳出循环
                        if (map.toMap().containsKey("isBreak") && map.getInt("isBreak") == 1){
                            result.set("isBreak", 1);
                        } else if (map.toMap().containsKey("isContinue") && map.getInt("isContinue") == 1){
                            result.set("isContinue", 1);
                        }else {
                            result.set("isBreak", 1);
                        }
                    }else {
                        // 失败，则提回滚 返回错误信息
                        throw new RuntimeException(result.getStr("message"));
                    }
                } else {
                    Map<String, List<Record>> recordMap = queryExchange(erpDBName, orgApp.getErpdbschemas(), result.getStr("resultExchangeID"));
                    LOG.info("{}", recordMap);

                    plugeReturnMap.put("ProcessMapID", processBusMap.getStr("initializemapid"));
                    if (null != processBusMap.getStr("url") && "Create".equals(processBusMap.getStr("tag"))) {
                        JSONObject jsonData = dataConversion.InterfaceTransform("", plugeReturnMap, recordMap);

                        // 创建api信息
                        Map<String, String> apimap = new HashMap<>();
                        apimap.put("tag", processBusMap.get("tag"));
                        apimap.put("url", processBusMap.get("url"));

                        List<Map<String, String>> apimaps = new ArrayList<>();
                        apimaps.add(apimap);

                        String message = null;
                        try {
                            LOG.info("{}", jsonData);
                            message = HttpApiUtils.u8Api(processBusMap.get("url"), jsonData);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        // 返回解析
                        Map processResult = dataConversion.processResult(processBusMap.getStr("resulttype"), message);
                        String resultCode = JBoltStringUtil.isBlank(processResult.get("resultCode").toString()) ? "201" : processResult.get("resultCode").toString();
                        if (!JBoltStringUtil.isBlank(processResult.get("resultInfo").toString())){
                            message = processResult.get("resultInfo").toString();
                        }

                        //单据处理节点信息
                        VouchProcessNote vouchprocessnote = new VouchProcessNote();
                        vouchprocessnote.setVouchbusinessid(vouchBusinessID);
                        vouchprocessnote.setSeq(processBusMap.getInt("seq"));
                        vouchprocessnote.setPreallocate(plugeReturnMap.toString());
                        vouchprocessnote.setExchangeid(map.get("resultExchangeID"));
                        vouchprocessnote.setReturnvalue(jsonData.toString());
                        vouchprocessnote.setCreatedate(now);
                        saveVouchProcessNotes.add(vouchprocessnote);

                        //回滚参照信息
                        VouchRollBackRef vouchrollbackref = new VouchRollBackRef();
                        vouchrollbackref.setVouchbusinessid(vouchBusinessID);
                        vouchrollbackref.setState("0");
                        vouchrollbackref.setProcessid(processBusMap.get("autoid").toString());
                        vouchrollbackref.setCreateperson(userApp.getStr("usercode"));
                        vouchrollbackref.setCreatedate(now);
                        saveVouchRollBackRefs.add(vouchrollbackref);

                        // 创建成功
                        if ("200".equals(resultCode)) {
                            result.set("state", 0);
                            plugeReturnMap.put("VouchId", processResult.get("docID"));
                            /*String docId = processResult.get("docID") == null ? null : processResult.get("docID").toString();
                            if (null != docId) {
                                plugeReturnMap.put("ResultDocID", docId);
                            }
                            plugeReturnMap.put("ResultDocNo", processResult.get("docNo"));
                            result.set("code", resultCode).set("message", message);*/
                            result.set("code", resultCode).set("message", message).set("dommessage", processResult.get("domMsg"));

                            // 日志记录
                            String source = jsonData.toString();
                            String memo = "系统生单";
                            String logResult = message;
                            String logUrl = apimap.get("url");
                            dataConversion.recordLog(orgApp.getOrganizecode(), vouchBusinessID, log.getVouchtype(), processBusMap.get("seq").toString(), ExchangeId, map.get("resultExchangeID"), result.getInt("state"), source, logResult, logUrl, memo, userApp.getStr("user_code"), log);
                            saveLogs.add(log);
                        } else {
                            result.set("state", 1);
                            // 日志记录
                            String source = jsonData.toString();
                            String memo = "系统生单";
                            String logUrl = apimap.get("url");
                            dataConversion.recordLog(orgApp.getOrganizecode(), vouchBusinessID, log.getVouchtype(), processBusMap.get("seq").toString(), ExchangeId, map.get("resultExchangeID"), result.getInt("state"), source, message, logUrl, memo, userApp.getStr("user_code"), log);
                            saveLogs.add(log);

                            // 返回错误信息
                            result.set("code", resultCode).set("message", message);
                            throw new RuntimeException(message);
                        }
                    }
                }
            }
            // ---------------------------------------------------------
            // ERP 生单结束
            // ---------------------------------------------------------

            // ---------------------------------------------------------
            // ERP 审单开始
            // ---------------------------------------------------------
            if (null != processBusMap.get("url") && "Audit".equals(processBusMap.getStr("tag"))) {
                // 日志状态初始
                int state = 0;

                Map<String, List<Record>> recordMap = queryExchange(erpDBName, orgApp.getErpdbschemas(), result.getStr("resultExchangeID"));
                LOG.info("{}", recordMap);
                plugeReturnMap.put("ProcessMapID", processBusMap.get("initializemapid"));

                JSONObject jsonData = dataConversion.InterfaceTransform(processBusMap.get("resulttype").toString(), plugeReturnMap, recordMap);
                LOG.info("{}", jsonData);

                VouchRollBackRef vouchrollbackref = new VouchRollBackRef();
                vouchrollbackref.setVouchbusinessid(vouchBusinessID);
                vouchrollbackref.setProcessvalue(jsonData.toString());
                vouchrollbackref.setProcessid(processBusPre.getStr("autoid"));
                updateVouchRollBackRefs.add(vouchrollbackref);

                Map<String, String> apimap = new HashMap<>();
                apimap.put("tag", processBusMap.get("tag"));
                apimap.put("url", processBusMap.get("url"));

                List<Map<String, String>> apimaps = new ArrayList<>();
                apimaps.add(apimap);

                String message = null;
                try {
                    message = HttpApiUtils.u8Api(apimap.get("url").toString(), jsonData);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                LOG.info(message);

                // 返回解析
                Map processResult = dataConversion.processResult(processBusMap.getStr("resulttype"), message);
                //LOG.info(processResult.get("resultCode").toString() + processResult.get("resultInfo").toString());
                String resultCode = processResult.get("resultCode").toString();
                message = processResult.get("resultInfo").toString();

                // 单据处理节点信息
                VouchProcessNote vouchprocessnote = new VouchProcessNote();
                vouchprocessnote.setVouchbusinessid(vouchBusinessID);
                vouchprocessnote.setSeq(processBusMap.getInt("seq"));
                vouchprocessnote.setPreallocate(plugeReturnMap.toString());
                vouchprocessnote.setExchangeid(result.getStr("resultExchangeID"));
                vouchprocessnote.setReturnvalue(jsonData.toString());
                vouchprocessnote.setCreatedate(now);
                saveVouchProcessNotes.add(vouchprocessnote);

                // 审核是否成功
                if ("200".equals(resultCode)) {
                    // 日志记录
                    String source = jsonData.toString();
                    String memo = "单据审核";
                    String logUrl = apimap.get("url");

                    Log log = new Log();

                    dataConversion.recordLog(orgApp.getOrganizecode(), vouchBusinessID, vouchType.getStr("vouchcode"), processBusMap.getStr("seq"), result.getStr("resultExchangeID"), "", state, source, message, logUrl, memo, userApp.getStr("user_name"), log);
                    result.set("code", "200").set("message", message);
                    saveLogs.add(log);
                } else {
                    result.set("code", "201").set("message", message);
                    String source = jsonData.toString();
                    String memo = "审核错误";
                    String logUrl = apimap.get("url");

                    Log log = new Log();

                    dataConversion.recordLog(orgApp.getOrganizecode(), vouchBusinessID, vouchType.getStr("vouchcode"), processBusMap.getStr("seq"), result.getStr("resultExchangeID"), "", state, source, message, logUrl, memo, userApp.getStr("user_name"), log);
                    saveLogs.add(log);
                    throw new RuntimeException(message);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 返回错误信息
            result.set("code", "201").set("message", e.getLocalizedMessage());

            throw new RuntimeException(e.getLocalizedMessage());
        } finally {

            if (CollUtil.isNotEmpty(saveExchangeTables)){
                tx(exchangeTableService.dataSourceConfigName(), Connection.TRANSACTION_READ_UNCOMMITTED, () -> {
                    exchangeTableService.batchSave(saveExchangeTables);
                    return true;
                });
            }

            // 保存所有日志操作及调用参数记录
            tx(DataSourceConstants.MOMDATA, Connection.TRANSACTION_READ_UNCOMMITTED, () -> {

                // 保存日志
                if (CollUtil.isNotEmpty(saveLogs)) {
                    tsysLogService.save(saveLogs.get(0));
                }
                // 保存单据操作记录
                if (CollUtil.isNotEmpty(saveVouchProcessNotes)) {
                    vouchProcessNoteService.batchSave(saveVouchProcessNotes);
                }
                // 保存回滚
                if (CollUtil.isNotEmpty(saveVouchRollBackRefs)) {
                    vouchRollBackRefService.batchSave(saveVouchRollBackRefs);
                }
                // 更新
                if (CollUtil.isNotEmpty(updateVouchRollBackRefs)) {
                    vouchRollBackRefService.batchUpdate(updateVouchRollBackRefs);
                }

                return true;
            });
        }
    }

    private String processFlag(Record processBus) {
        String processFlag = "";
        if (null == processBus.get("bprocessflag")) {
            if (null != processBus.get("processflag")) {
                processFlag = processBus.getStr("processflag");
            }
        } else {
            processFlag = processBus.getStr("bprocessflag");
        }
        return processFlag;
    }

    public Kv vouchProcessDynamicSubmit(Kv k) {
        CaseInsensitiveMap<String, Object> kv = new CaseInsensitiveMap<String, Object>(k);
        
        String SourceJson = k.toJson();

        Kv result = Kv.create();
        
        Date now = new Date();

        String organizecode = (String) kv.get("organizecode");
        
        Organize orgApp = organizeService.getOrgByCode(organizecode);
        ValidationUtils.notNull(orgApp, "组织不存在");
        
        // 对U8数据源别名做特殊处理，如果是u8开头的数据源别名，结合组织编码定义数据源别名，如u8001
        String erpDbAlias = StrUtil.startWith(orgApp.getErpdbalias(), "u8") ? U8DataSourceKit.ME.use(orgApp.getOrganizecode()) : orgApp.getErpdbalias();

        OrgAccessKit.setOrgCode(organizecode);
        OrgAccessKit.setOrgId(orgApp.getId());
        OrgAccessKit.setU8DbAlias(erpDbAlias);
        
        Record userApp = userAppService.getUserByUserCode(erpDbAlias, orgApp.getErpdbname(), orgApp.getErpdbschemas(), (String) kv.get("usercode"));
        ValidationUtils.notNull(userApp, "当前组织下没有该用户！");

        String erpDBName = orgApp.getErpdbname();
        
        // 获取所有钟工组件返回的u8单据类型值
        JSONObject preAllocate = (JSONObject) kv.get("preallocate");
        
        //通过数据的分组id，将数据分成多组，分别提交
        Map<Object, List<Object>> dataGroup = ((JSONArray) kv.get("maindata")).stream().collect(
                Collectors.groupingBy(p -> ((Map<?, ?>) p).get("GroupFlag") == null ? "10" : ((Map<?, ?>) p).get("GroupFlag"), Collectors.toList())
        );
        Set<Object> groupFlags = new TreeSet<>(dataGroup.keySet());
        
        for (Object flag : groupFlags) {
            //业务id
            String vouchBusinessID = JBoltSnowflakeKit.me.nextIdStr();
            String seqBusinessID = vouchBusinessID;
            //获取主数据
            JSONArray mainData = JSONArray.parseArray(JSON.toJSONString(dataGroup.get(flag)));//JSONArray.parseArray(kv.getStr("maindata"));
            // 获取详细数据
            JSONArray detailData = (JSONArray) kv.get("detaildata");
            // 获取扩展数据
            JSONArray extData = (JSONArray) kv.get("extdata");
            //判断主数据是否有数据
            ValidationUtils.isTrue(mainData.size() > 0, "detail没有数据");
            Map<String, Object> plugeReturnMap = ListUtils.dealWithReturnData(preAllocate);
            String type = plugeReturnMap.get("type").toString();
            List<Record> list = findInfoFlag(type);
            String synergyTag = list.get(0).getStr("synergytag");
            //u8或u9的接口配置表
            //List<Record> openAPIs = getOpenAPI(synergyTag);
            //获取对应
            Record vouchType = findInfoFlag(type).get(0);
            Integer id1 = vouchType.getInt("autoid");
            //先清空当前masid下的数据，再重新从流程表载入
            //创建临时表,并初始化当前流程的数据
            //int isDeleteSuccess = update("SELECT *,0 stat INTO "+ templateName +" FROM dbo.T_Sys_VouchProcess WHERE masid=" + id1);
            update("INSERT INTO dbo.T_Sys_VouchProcessDynamic(AutoID, MasID, Seq, ExechgeSeq, ReturnSeq, GroupSeq, AutoRollback, IgnoreFailure, ProcessID, ProcessFlag, Memo, ClosePerson, CreatePerson, CreateDate, ModifyPerson, ModifyDate,VersionID)\n" +
                    "SELECT *," + seqBusinessID + " FROM dbo.T_Sys_VouchProcess WHERE MasID=" + id1);

            //获取所有流程
            //List<Record> processBusMaps = processBusDynamic(id1, seqBusinessID);
            //流程执行
            plugeReturnMap.put("ProcessType", vouchType.get("processtype"));
            plugeReturnMap.put("CreatePersonName", userApp.getStr("u8_name"));
            plugeReturnMap.put("CreatePerson", userApp.getStr("u8_code"));
            plugeReturnMap.put("organizeCode", kv.get("organizecode"));
            plugeReturnMap.put("password", userApp.getStr("u8_pwd"));
            AtomicInteger currentSeq = new AtomicInteger();//用于回滚
            
            DataConversion dataConversion = new DataConversion(this, columsmapdetailService);
            
            // 内层事务,当异常条件下，对已执行的子事务提交，并返回错误信息
            tx(erpDbAlias, Connection.TRANSACTION_READ_UNCOMMITTED, () -> {

                List<Record> processBusMap;

                Record prevProcessBus = null;

                try {
                    while (true) {
                        processBusMap = processBusDynamic(id1, seqBusinessID);
                        if (processBusMap.isEmpty()) {
                            break;
                        }
                        
                        Record processBusMapData = processBusMap.get(0);
                        currentSeq.set(processBusMapData.getInt("seq"));
                        //预制项配置
                        plugeReturnMap.put("InitializeMapID", processBusMapData.get("initializemapid"));
                        
                        //判断流程是否可用
                        if (StrUtil.isBlank(processBusMapData.get("pcloseperson")) && StrUtil.isBlank(processBusMapData.get("bcloseperson"))) {
                            // ---------------------------------------------------------
                            // ERP 生单、审单事务上下文开始
                            // ---------------------------------------------------------
                            try {
                                txInErp(erpDbAlias, erpDBName, vouchType, vouchBusinessID, orgApp, processBusMapData, dataConversion, userApp, prevProcessBus, type, plugeReturnMap, result, mainData, detailData, extData, SourceJson, now);
                                if (result.containsKey("isBreak") && result.getInt("isBreak") == 1) {
                                    break;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                LOG.error(e.getLocalizedMessage());
                                result.set("message", e.getLocalizedMessage());
                                // 这里不打印异常信息，处理回滚外层事务
                                return false;
                            }
                        }

                        String autoid = processBusMapData.getStr("autoid");
                        update("UPDATE dbo.T_Sys_VouchProcessDynamic SET stat=1 WHERE MasID=? AND VersionID=? AND AutoID=?", id1, seqBusinessID, autoid);

                        prevProcessBus = processBusMap.get(0);//上一次的步骤信息，留下次步骤使用
                    }
                    // result.set("code", code).set("message", message);
                } catch (Exception e) {
                    // 内层循环出异常，则外层事务不提交
                    e.printStackTrace();
                    // 回滚外层事务
                    return false;
                } finally {
                    if (!"200".equals(result.getStr("code"))) {
                        rollbackProcess(currentSeq.get(), seqBusinessID, getAllProcessBusDynamic(id1, seqBusinessID), dataConversion, userApp, type);
                    }
                }
                return true;
            });
        }

        return result;
    }

    /**
     * 读取动态流程表当前需要执行的步骤
     * @param masID
     * @return
     */
    public List<Record> getAllProcessBusDynamic(Integer masID, String seqBusinessID){
        Kv kv = Kv.by("masID", masID).set("seqBusinessID", seqBusinessID);
        return dbTemplate("openapi.allProcessBusDynamic", kv).find();
    }

    /**
     * 查询数据动态交换数据
     *
     * @param erpDBName  数据库类型
     * @param exchangeID 交换表exchangeID
     */
    public Map<String, List<Record>> queryExchange(String erpDBName, String erpDBSchemas, String exchangeID) throws RuntimeException {
        Map<String, List<Record>> map = new HashMap<>();
        
        String[] exchangeIDs = exchangeID.split(",");
        
        /*Kv kv = Kv.by("erpDBName", erpDBName);
        kv.set("erpDBSchemas", erpDBSchemas);*/
        
        for (int i = 0; i < exchangeIDs.length; i++) {
            //kv.set("ExchangeID", exchangeIDs[i]);
            List<Record> list = exchangeTableService.findRecord("SELECT * FROM T_Sys_ExchangeTable WHERE ExchangeID = ? ", false, exchangeIDs[i]);
            switch (i) {
                case 0:
                    map.put("MainData", list);
                    break;
                case 1:
                    map.put("DetailData", list);
                    break;
                case 2:
                    map.put("ExtData", list);
                    break;
            }
        }
        return map;
    }

    /**
     * 执行存储过程
     */
    private Record StoredProcedure(String dataSourceConfigName, String erpDBName, String erpdbschemas, String processname, String processFlag, String exchangeId) {
        return (Record) executeFunc(dataSourceConfigName, (conn) -> {
            String sql = "{ call " + erpDBName + "." + erpdbschemas + "." + processname + "(?, ?, ?, ?, ?) }";
            LOG.info(sql);
            CallableStatement proc = conn.prepareCall(sql);
            proc.setString(1, exchangeId);
            proc.setString(2, processFlag);
            // 注册输出参数
            proc.registerOutParameter(3, Types.VARCHAR);
            proc.registerOutParameter(4, Types.VARCHAR);
            proc.registerOutParameter(5, Types.VARCHAR);
            //执行
            proc.execute();

            Record record = new Record();
            record.set("resultCode", proc.getString(3));
            record.set("resultInfo", proc.getString(4));
            record.set("resultExchangeID", proc.getString(5));
            return record;
        });
        //ValidationUtils.isTrue("200".equals(rc.getStr("resultCode")), rc.getStr("resultInfo"));
        /*if (!"200".equals(rc.getStr("resultcode"))) {
            return null;
        }*/
    }

    /**
     * 读取流程表
     */
    public List<Record> processBus(Integer masID) {
        Kv kv = Kv.by("masID", masID);
        return dbTemplate("openapi.processBus", kv).find();
    }

    /**
     * 读取动态流程表
     */
    public List<Record> processBusDynamic(Integer masID, String seqBusinessID) {
        Kv kv = Kv.by("masID", masID).set("seqBusinessID", seqBusinessID);
        return dbTemplate("openapi.processBusDynamic", kv).find();
    }

    public List<Record> getOpenAPI(String code) throws RuntimeException {
        Kv kv = Kv.by("code", code);
        return dbTemplate("openapi.api", kv).find();
    }

}