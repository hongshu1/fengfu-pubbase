package cn.rjtech.common.columsmap;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.CaseInsensitiveMap;
import cn.hutool.core.text.StrSplitter;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.util.JBoltStringUtil;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.app.user.service.UserAppService;
import cn.rjtech.base.service.BaseService;
import cn.rjtech.common.columsmapdetail.ColumsmapdetailService;
import cn.rjtech.common.exchangetable.ExchangeTableService;
import cn.rjtech.common.model.*;
import cn.rjtech.common.organize.OrganizeService;
import cn.rjtech.common.tsyslog.TsysLogService;
import cn.rjtech.common.vouchprocessnote.VouchProcessNoteService;
import cn.rjtech.common.vouchrollbackref.VouchRollBackRefService;
import cn.rjtech.constants.ErrorMsg;
import cn.rjtech.erp.mood.DataConversion;
import cn.rjtech.util.ValidationUtils;
import cn.rjtech.wms.utils.ListUtils;
import cn.rjtech.wms.utils.StringUtils;
import cn.rjtech.wms.utils.WebService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.ICallback;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.sql.*;
import java.util.Date;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static cn.hutool.core.util.StrUtil.COMMA;

/**
 * 字段映射 Service
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

    //日志对象
    private Log tsysLog = new Log();

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
        for (int i=0; i<maps.size(); i++){
            String[] types = StringUtils.split(maps.get(i).get("type").toString(),',') ;
            for (int j=0; j<types.length; j++){
                if(types[j].equals(type)){
                    map = maps.get(i);
                    return map.toMap();
                }
            }
        }
        return map.toMap();
    }

    public List<Record> vouchType(String vouchCode)  {
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
        List<Map<String, Object>> mapList = (List<Map<String, Object>>) execute(erpdbalias, (conn) -> {
            List<Map<String, Object>> list = new ArrayList<>();
            String sql = "{ call " + erpDBName + "." + erpDBSchemas + "." + processName + "(?, ?, ?, ?, ?, ?, ?) }";
            System.err.println(sql);
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
            try{
                rs = proc.executeQuery();
            }catch (SQLException e){
                isSuccess = false;
            }
            //ResultSet rs = proc.getResultSet();

            // 获取结果集列名
            if(isSuccess){
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

        return mapList;
    }

    /**
     * 获取单据信息
     * @param erpDBName	数据库
     * @param organizeCode	组织代码
     * @param billNo		条码
     * @param flag			查看标志
     * @return
     * @throws Exception
     */
    public List<Map<String,Object>> getBillInfo(Organize orgApp, String barCode, String flag){
        HashMap<String, Object> paraMap = new HashMap<>();
        List<Map<String, Object>> mapList = (List<Map<String, Object>>) execute(orgApp.getErpdbalias(), (conn) -> {
            List<Map<String, Object>> list = new ArrayList<>();
            String sql = "{ call " + orgApp.getErpdbname() + "." + orgApp.getErpdbschemas() + ".P_Sys_GetBillInfo" + "(?, ?, ?, ?, ?) }";
            System.err.println(sql);
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
            try{
                rs = proc.executeQuery();
            }catch (SQLException e){
                isSuccess = false;
            }
            if(isSuccess){
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

        return mapList;
    }

    /**
     * 获取条码信息
     *
     * @param posCode
     * @param flag         查询标志
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getBarcodeInfo(Organize orgApp, String barCode, String posCode, String flag) {
        HashMap<String, Object> paraMap = new HashMap<>();

        return (List<Map<String, Object>>) execute(orgApp.getErpdbalias(), (conn) -> {
            List<Map<String, Object>> list = new ArrayList<>();
            String sql = "{ call " + orgApp.getErpdbname() + "." + orgApp.getErpdbschemas() + ".P_Sys_GetBarcodeInfo" + "(?, ?, ?, ?, ?, ?, ?) }";
            System.err.println(sql);
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
            try{
                rs = proc.executeQuery();
            }catch (SQLException e){
                isSuccess = false;
            }
            if(isSuccess){
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

    public Map vouchProcessSubmit(Kv k){
        CaseInsensitiveMap<String, Object> kv = new CaseInsensitiveMap<String, Object>(k);
        String sourceJson = k.toJson();

        Kv result = Kv.create();
        tx(Connection.TRANSACTION_READ_UNCOMMITTED, () -> {

            Organize orgApp = organizeService.getOrgByCode((String) kv.get("organizecode"));

            Record userApp = userAppService.getUserByUserCode(orgApp.getErpdbname(), orgApp.getErpdbschemas(), (String) kv.get("usercode"));
            ValidationUtils.notNull(userApp, "当前组织下没有该用户！");

            String erpDBName = orgApp.getErpdbname();
            String erpDbAlias = orgApp.getErpdbalias();

            // 业务id
            String vouchBusinessID = String.valueOf(JBoltSnowflakeKit.me.nextId());
            String seqBusinessID = vouchBusinessID;

            // 获取所有钟工组件返回的u8单据类型值
            JSONObject preAllocate = (JSONObject) kv.get("preallocate");

            //通过数据的分组id，将数据分成多组，分别提交
            Map<Object, List<Object>> dataGroup = ((JSONArray) kv.get("maindata")).stream().collect(Collectors.groupingBy(p -> {
                return ((Map<?, ?>) p).get("GroupFlag") == null ? "10" : ((Map<?, ?>) p).get("GroupFlag");
            }, Collectors.toList()));
            
            Set<Object> groupFlags = new TreeSet<>(dataGroup.keySet());
            for (Object flag : groupFlags) {
                // 获取主数据
                JSONArray mainData = JSONArray.parseArray(JSON.toJSONString(dataGroup.get(flag)));//(JSONArray) kv.get("maindata");
                // 获取详细数据
                JSONArray detailData = (JSONArray) kv.get("DetailData");
                // 获取扩展数据
                JSONArray extData = (JSONArray) kv.get("ExtData");

                ValidationUtils.notEmpty(mainData, "detail没有数据");

                Map plugeReturnMap = ListUtils.dealWithReturnData(preAllocate);

                String type = plugeReturnMap.get("type").toString();
                List<Record> list = findInfoFlag(type);
                String synergyTag = list.get(0).getStr("synergytag");

                // 获取对应
                Record vouchType = findInfoFlag(type).get(0);
                Integer id1 = vouchType.getInt("autoid");
                // 获取所有流程
                List<Record> processBusMaps = processBus(id1);
                // 流程执行
                plugeReturnMap.put("ProcessType", vouchType.get("processtype"));
                plugeReturnMap.put("CreatePersonName", userApp.getStr("user_name"));
                plugeReturnMap.put("CreatePerson", userApp.getStr("user_code"));
                plugeReturnMap.put("organizeCode", kv.get("organizecode"));
                
                AtomicInteger currentSeq = new AtomicInteger();
                
                try {

                    // 内层事务,当异常条件下，对已执行的子事务提交，并返回错误信息
                    tx(Connection.TRANSACTION_READ_UNCOMMITTED, () -> {

                        // 默认代码为200
                        int code = 200;
                        // 返回的数据
                        String message = "成功";
                        // 日志状态
                        int state = 0;

                        Map<String, List<Record>> recordMap;

                        Map<Object, List<Record>> groupProcessDatas = processBusMaps.stream().collect(Collectors.groupingBy(p -> p.get("groupseq") == null ? "10" : p.get("groupseq"), Collectors.toList()));//通过配置表的groupseq分组
                        Set<Object> groupSeqs = new TreeSet<>(groupProcessDatas.keySet());
                        Iterator<Object> groupId = groupSeqs.iterator();
                        DataConversion dataConversion = new DataConversion(this, columsmapdetailService);

                        while (groupId.hasNext()) {
                            // 取出分组中的流程
                            List<Record> processBusList = groupProcessDatas.get(groupId.next());

                            for (int i = 0; i < processBusList.size(); i++) {
                                Record processBus = processBusList.get(i);
                                currentSeq.set(processBus.getInt("seq"));
                                // 预制项配置
                                plugeReturnMap.put("InitializeMapID", processBus.get("initializemapid"));
                                // 判断流程是否可用
                                if (null == processBus.get("pcloseperson") || "".equals(processBus.get("pcloseperson")) && null == processBus.get("bcloseperson") || "".equals(processBus.get("bcloseperson"))) {
                                    // 保存交换表步骤
                                    if (null == processBus.get("processname") && null == processBus.get("url") || "".equals(processBus.get("processname")) && null == processBus.get("url")) {
                                        // 创建数据动态交换表list
                                        List<ExchangeTable> dt = dataConversion.getTSysExchangetable(type, plugeReturnMap, mainData, detailData, extData);
                                        exchangeTableService.saveInTx(dt);

                                        VouchProcessNote note = new VouchProcessNote();
                                        note.setVouchbusinessid(vouchBusinessID);
                                        note.setSeq(processBus.getInt("seq"));
                                        note.setExchangeid(dt.get(0).getExchangeID());
                                        note.setCreatedate(new Date());
                                        vouchProcessNoteService.saveVouchprocessnoteInTx(note);

                                        String vouchtypeId = vouchType.getStr("vouchcode");
                                        String source = sourceJson;//"PreAllocate:" + preAllocate;
                                        String memo = "Exchangeid:" + dt.get(0).getExchangeID();

                                        Log tsysLog = new Log();
                                        dataConversion.recordLog(orgApp.getOrganizecode(), vouchBusinessID, vouchtypeId, processBus.get("seq").toString(), "", dt.get(0).getExchangeID(), state, source, "", "", memo, userApp.getStr("user_code"), tsysLog);
                                        tsysLogService.saveLogInTx(tsysLog);
                                    }

                                    // flag级别使用
                                    String processFlag = processFlag(processBus);

                                    if (null != processBus.get("processname") && !"".equals(processBus.get("processname"))) {
                                        // 获取节点信息
                                        List<Record> datamap = vouchProcessNoteService.processNote(processBus.getStr("exechgeseq"), seqBusinessID);
                                        String ExchangeId = datamap.get(datamap.size() - 1).getStr("exchangeid");

                                        Log tsysLog = new Log();
                                        tsysLog.setVouchtype(vouchType.getStr("vouchcode"));
                                        tsysLog.setVouchstep(processBus.getStr("seq"));
                                        // 执行U9数据源事务
                                        txInU9(erpDbAlias, erpDBName, vouchBusinessID, orgApp, processBus, processFlag, ExchangeId, dataConversion, userApp, tsysLog, plugeReturnMap, result);
                                        message = result.getStr("message");
                                        // 失败,
                                        if (result.getInt("state") == 1) {
                                            // 返回错误信息
                                            //result.set("message", result.getStr("message"));
                                            // 提交事务
                                            return true;
                                        }
                                    }

                                    if (null != processBus.get("url") && "Audit".equals(processBus.getStr("tag"))) {
                                        //日志状态初始
                                        state = 0;
                                        Map<String, String> apimap = new HashMap<>();

                                        try {
                                            recordMap = queryExchange(erpDBName, orgApp.getErpdbschemas(), result.getStr("resultExchangeID"));
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            // 返回错误信息
                                            result.set("code", "201").set("message", e.getLocalizedMessage());
                                            // 提交事务
                                            return true;
                                        }

                                        System.out.println(recordMap);
                                        plugeReturnMap.put("ProcessMapID", processBus.get("initializemapid"));

                                        JSONObject jsonData = dataConversion.InterfaceTransform(processBus.get("resulttype").toString(), plugeReturnMap, recordMap);
                                        System.out.println(jsonData);

                                        VouchRollBackRef vouchrollbackref = new VouchRollBackRef();
                                        vouchrollbackref.setVouchbusinessid(seqBusinessID);
                                        vouchrollbackref.setProcessvalue(jsonData.toString());
                                        vouchrollbackref.setProcessid(processBusList.get(i - 1).getStr("autoid"));
                                        vouchRollBackRefService.updateRefInTx(vouchrollbackref);

                                        apimap.put("tag", processBus.get("tag"));
                                        apimap.put("url", processBus.get("url"));

                                        List<Map<String, String>> apimaps = new ArrayList<>();
                                        apimaps.add(apimap);

                                        // u9调用
                                        message = WebService.ApiWebService(processBus.getStr("tag"), apimaps, jsonData);
                                        System.out.println(message);

                                        // 返回解析
                                        Map processResult = dataConversion.processResult(processBus.getStr("resulttype"), message, 1);
                                        //System.out.println(processResult.get("resultCode").toString() + processResult.get("resultInfo").toString());
                                        String resultCode = processResult.get("resultCode").toString();
                                        message = processResult.get("resultInfo").toString();

                                        // 单据处理节点信息
                                        VouchProcessNote vouchprocessnote = new VouchProcessNote();
                                        vouchprocessnote.setVouchbusinessid(seqBusinessID);
                                        vouchprocessnote.setSeq(processBus.getInt("seq"));
                                        vouchprocessnote.setPreallocate(plugeReturnMap.toString());
                                        vouchprocessnote.setExchangeid(result.getStr("resultExchangeID"));
                                        vouchprocessnote.setReturnvalue(jsonData.toString());
                                        vouchprocessnote.setCreatedate(new Date());
                                        vouchProcessNoteService.saveVouchprocessnoteInTx(vouchprocessnote);

                                        //审核是否失败
                                        if (!"200".equals(resultCode)) {
                                            result.set("code", "201").set("message", message);
                                            throw new RuntimeException(message);
                                            // 日志记录
                                            /*state = 1;
                                            Record rollback = vouchRollBackRefService.rollback(processBusList.get(i - 1).getStr("autoid"));
                                            if (rollback == null) throw new RuntimeException(message);
                                            List<Record> rollbackref = vouchRollBackRefService.rollbackref(seqBusinessID);
                                            String processvalue = rollbackref.get(0).getStr("processvalue");
                                            // map2=JSON.parseObject(processvalue,HashMap.class);
                                            JSONObject rojsonObject = JSONObject.parseObject(processvalue);
                                            System.out.println(rojsonObject);
                                            //回滚api
                                            Map roapimap = new HashMap();
                                            roapimap.put("tag",rollback.get("tag"));
                                            roapimap.put("url",rollback.get("url"));
                                            List<Map<String,String>> roapimaps = new ArrayList<>();
                                            roapimaps.add(roapimap);
                                            //u9调用
                                            message = WebService.ApiWebService(rollback.get("tag","").toString(),roapimaps,rojsonObject);
                                            //返回解析
                                            Map roprocessResult = dataConversion.processResult(rollback.get("resulttype","").toString().toLowerCase(), message,1);
                                            String roresultCode = roprocessResult.get("resultCode").toString();
                                            String roresultInfo = roprocessResult.get("resultInfo").toString();
                                            message = roresultInfo;
                                            if (!"200".equals(roresultCode)) {
                                                String source = rojsonObject.toString();
                                                String memo = "系统回滚";
                                                String logResult = "审核信息:" + resultInfo + ";" + message;
                                                String logUrl = roapimap.get("url").toString();
                                                Log tsysLog = new Log();
                                                dataConversion.recordLog(orgApp.getOrganizecode(),vouchBusinessID,vouchType.getStr("vouchcode"),processBusList.get(i-1).getStr("seq"),result.getStr("resultExchangeID"),"",state,source,logResult,logUrl,memo,userApp.getStr("user_code"),tsysLog);
                                                tsysLogService.saveLogInTx(tsysLog);
                                            }else{
                                                state=0;
                                                // 日志记录
                                                String rsource = jsonData.toString();
                                                String rmemo = "系统回滚";
                                                String rlogResult = "审核信息:" + resultInfo + ";" + message;
                                                String rlogUrl = roapimap.get("url").toString();
                                                Log tsysLog = new Log();
                                                dataConversion.recordLog(orgApp.getOrganizecode(),vouchBusinessID,vouchType.getStr("vouchcode"),processBus.getStr("seq"),result.getStr("resultExchangeID"),"",state,rsource,rlogResult,rlogUrl,rmemo,userApp.getStr("user_code"),tsysLog);
                                                tsysLogService.saveLogInTx(tsysLog);

                                                // 返回错误信息
                                                result.set("code", resultCode).set("message", rlogResult);
                                                // 提交事务
                                                return true;
                                            }

                                            System.out.println(rojsonObject);
                                            System.out.println(message);
                                            // 日志记录
                                            String source = jsonData.toString();
                                            String memo = "单据审核";
                                            String logResult = message;
                                            String logUrl = apimap.get("url").toString();
                                            Log tsysLog = new Log();
                                            dataConversion.recordLog(orgApp.getOrganizecode(),vouchBusinessID,vouchType.getStr("vouchcode"),processBus.getStr("seq"),result.getStr("resultExchangeID"),"",state,source,logResult,logUrl,memo,userApp.getStr("user_code"),tsysLog);
                                            tsysLogService.saveLogInTx(tsysLog);

                                            // 返回错误信息
                                            result.set("code", resultCode).set("message", message);
                                            // 提交事务
                                            return true;*/
                                        } else {
                                            // 日志记录
                                            String source = jsonData.toString();
                                            String memo = "单据审核";
                                            String logUrl = apimap.get("url");
                                            Log tsysLog = new Log();
                                            dataConversion.recordLog(orgApp.getOrganizecode(), vouchBusinessID, vouchType.getStr("vouchcode"), processBus.getStr("seq"), result.getStr("resultExchangeID"), "", state, source, message, logUrl, memo, userApp.getStr("user_name"), tsysLog);
                                            tsysLogService.saveLogInTx(tsysLog);
                                        }
                                    }
                                }
                            }

                        }

                        result.set("code", code).set("message", message);
                        // 内层事务提交
                        return true;
                    });

                } catch (Exception e) {
                    // 内层循环出异常，则外层事务不提交
                    e.printStackTrace();
                    //rollbackProcess(currentSeq.get(), seqBusinessID, processBusMaps);
                    // 回滚外层事务
                    return false;
                } finally {
                    if (!"200".equals(result.getStr("code"))) {
                        rollbackProcess(currentSeq.get(), seqBusinessID, processBusMaps);
                    }
                }
            }

            // 外层事务提交
            return true;
        });

        // 外层事务
        return result;
    }

    /**
     *
     * @param currentSeq  当前执行流程的序号
     * @param currentSeq  流程id
     * @param processBusMaps 流程集合
     */
    private void rollbackProcess(Integer currentSeq, String seqBusinessID, List<Record> processBusMaps) {
        while (currentSeq > 10){
            Integer finalCurrentSeq = currentSeq;
            List<Record> list = processBusMaps.stream().filter(p -> Objects.equals(p.getInt("seq"), finalCurrentSeq)).collect(Collectors.toList());
            if (!list.isEmpty()) {
                String processID = list.get(0).getStr("autoid");
                Record rollback = vouchRollBackRefService.rollback(processID);
                if (rollback != null) {
                    if (list.get(0).getStr("memo").contains("审核")){
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
                    System.out.println(rojsonObject);
                    //回滚api
                    Map roapimap = new HashMap();
                    roapimap.put("tag",rollback.get("tag"));
                    roapimap.put("url",rollback.get("url"));
                    List<Map<String,String>> roapimaps = new ArrayList<>();
                    roapimaps.add(roapimap);
                    //u9调用
                    String message = WebService.ApiWebService(rollback.get("tag","").toString(),roapimaps,rojsonObject);
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

                System.err.println(recordMap);
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
                    String message = WebService.ApiWebService(processBusMap.getStr("tag"), apimaps, jsonData);
                    // 返回解析
                    Map processResult = dataConversion.processResult(processBusMap.getStr("resulttype"), message, 1);
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
                        String docId = processResult.get("docID") == null ? null : processResult.get("docID").toString();
                        if (null != docId) {
                            plugeReturnMap.put("ResultDocID", docId);
                        }
                        plugeReturnMap.put("ResultDocNo", processResult.get("docNo"));
                        result.set("code", resultCode).set("message", message);
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

    public Map tmp(Kv kv){
        //默认代码为200
        int code = 200;
        //返回的数据
        String message = "成功";
        Map result = new HashMap();
        Organize orgApp = organizeService.getOrgByCode(kv.getStr("organizecode"));
        Record userApp = userAppService.getUserByUserCode(orgApp.getErpdbname(), orgApp.getErpdbschemas(), kv.getStr("usercode"));
        ValidationUtils.notNull(userApp, "当前组织下没有该用户！");

        String erpDBName = orgApp.getErpdbname();

        //获取所有钟工组件返回的u8单据类型值
        JSONObject preAllocate = JSONObject.parseObject(kv.getStr("PreAllocate"));
        //通过数据的分组id，将数据分成多组，分别提交
        Map<Object, List<Object>> dataGroup = ((JSONArray) kv.get("MainData")).stream().collect(Collectors.groupingBy(p -> {
            Map<String, Object> a = (Map) p;
            return ((Map<?, ?>) p).get("GroupFlag") == null ? "10" : ((Map<?, ?>) p).get("GroupFlag");
        }, Collectors.toList()));
        Set<Object> groupFlags = new TreeSet<>(dataGroup.keySet());
        Iterator groupFlag = groupFlags.iterator();
        while (groupFlag.hasNext()){
            //业务id
            String vouchBusinessID = String.valueOf(JBoltSnowflakeKit.me.nextId());
            String seqBusinessID = vouchBusinessID;
            //获取主数据
            JSONArray mainData = JSONArray.parseArray(JSON.toJSONString(dataGroup.get(groupFlag.next())));//JSONArray.parseArray(kv.getStr("maindata"));
            // 获取详细数据
            JSONArray detailData = JSONArray.parseArray(kv.getStr("DetailData"));
            // 获取扩展数据
            JSONArray extData = JSONArray.parseArray(kv.getStr("ExtData"));
            //判断主数据是否有数据
            ValidationUtils.isTrue(mainData.size() > 0, "detail没有数据");
            Map plugeReturnMap = ListUtils.dealWithReturnData(preAllocate);
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

            List<ExchangeTable> dt = null;
            //获取所有流程
            List<Record> processBusMaps = processBusDynamic(id1, seqBusinessID);
            Map<String, List<Record>> recordMap = null;
            Record map = null;
            //日志状态
            int state = 0;
            //流程执行
            plugeReturnMap.put("ProcessType", vouchType.get("processtype"));
            plugeReturnMap.put("CreatePersonName", userApp.getStr("user_name"));
            plugeReturnMap.put("CreatePerson", userApp.getStr("user_code"));
            plugeReturnMap.put("organizeCode", kv.getStr("organizeCode"));
            List<Record> processBusMap = new ArrayList<>();
            while (true){
                List<Record> dynamic = processBusDynamic(id1, seqBusinessID);
                if (dynamic.isEmpty()){
                    break;
                }
                String autoid = dynamic.get(0).getStr("autoid");
                update("UPDATE dbo.T_Sys_VouchProcessDynamic SET stat=1 WHERE MasID=? AND VersionID=? AND AutoID=?", id1, seqBusinessID, autoid);

            }
            /*Map<Object, List<Record>> groupProcessDatas = processBusMaps.stream().collect(Collectors.groupingBy(p -> p.get("groupseq") == null ? "10" : p.get("groupseq"), Collectors.toList()));//通过配置表的groupseq分组
            Set<Object> groupSeqs = new TreeSet<>(groupProcessDatas.keySet());
            Iterator groupId = groupSeqs.iterator();
            DataConversion dataConversion = new DataConversion(this, columsmapdetailService);
            while (groupId.hasNext()){
                processBusMap = groupProcessDatas.get(groupId.next());//取出分组中的流程
                tsysLog = new Log();
            }*/
            result.put("code",code);
            result.put("message",message);
        }

        return result;
    }

    /**
     * 查询数据动态交换数据
     * @param erpDBName	数据库类型
     * @param exchangeID	交换表exchangeID
     * @return
     */
    public Map<String,List<Record>> queryExchange(String erpDBName,String erpDBSchemas, String exchangeID)throws RuntimeException{
        Map<String,List<Record>> map = new HashMap<>();
        String[] exchangeIDs = exchangeID.split(",");
        /*Kv kv = Kv.by("erpDBName", erpDBName);
        kv.set("erpDBSchemas", erpDBSchemas);*/
        for (int i=0; i<exchangeIDs.length; i++){
            //kv.set("ExchangeID", exchangeIDs[i]);
            List<Record> list = exchangeTableService.findRecord("SELECT * FROM T_Sys_ExchangeTable WHERE ExchangeID in(" + exchangeIDs[i] + ")");
            switch (i){
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

    //执行存储过程
    //执行存储过程
    private Record StoredProcedure(String dataSourceConfigName, String erpDBName, String erpdbschemas, String processname, String processFlag, String exchangeId) {
        AtomicReference<Record> atomicReference = new AtomicReference();

        Record rc = (Record) execute(dataSourceConfigName, (conn) -> {
            String sql = "{ call " + erpDBName + "." + erpdbschemas + "." + processname + "(?, ?, ?, ?, ?) }";
            System.err.println(sql);
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
        atomicReference.set(rc);
        //ValidationUtils.isTrue("200".equals(rc.getStr("resultCode")), rc.getStr("resultInfo"));
        /*if (!"200".equals(rc.getStr("resultcode"))) {
            return null;
        }*/
        return atomicReference.get();
    }
    public Object execute(ICallback callback) {
        return Db.use(dataSourceConfigName()).execute(callback);
    }
    protected Object execute(String dataSourceConfigName, ICallback callback) {
        return Db.use(dataSourceConfigName).execute(callback);
    }

    /**
     * 读取流程表
     * @param masID
     * @return
     */
    public List<Record> processBus(Integer masID){
        Kv kv = Kv.by("masID", masID);
        return dbTemplate("openapi.processBus", kv).find();
    }

    /**
     * 读取动态流程表
     * @param masID
     * @return
     */
    public List<Record> processBusDynamic(Integer masID, String seqBusinessID){
        Kv kv = Kv.by("masID", masID).set("seqBusinessID", seqBusinessID);
        return dbTemplate("openapi.processBusDynamic", kv).find();
    }

    public List<Record> getOpenAPI(String code)throws RuntimeException{
        Kv kv = Kv.by("code", code);
        return dbTemplate("openapi.api", kv).find();
    }

}