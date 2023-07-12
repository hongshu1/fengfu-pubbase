package cn.rjtech.admin.momotask;

import cn.hutool.core.collection.CollUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.department.DepartmentService;
import cn.rjtech.admin.modoc.MoDocService;
import cn.rjtech.admin.scheduproductplan.ScheduProductPlanMonthService;
import cn.rjtech.enums.AuditStatusEnum;
import cn.rjtech.model.momdata.Department;
import cn.rjtech.model.momdata.MoMotask;
import cn.rjtech.service.approval.IApprovalService;
import cn.rjtech.util.DateUtils;
import cn.rjtech.util.Util;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 制造工单任务 Service
 *
 * @ClassName: MoMotaskService
 * @author: chentao
 * @date: 2023-04-28 15:19
 */
public class MoMotaskService extends BaseService<MoMotask> implements IApprovalService {

  private final MoMotask dao = new MoMotask().dao();
  @Inject
  private DepartmentService departmentService;
  @Inject
  private MoDocService moDocService;
  @Inject
  private ScheduProductPlanMonthService scheduProductPlanMonthService;

  @Override
  protected MoMotask dao() {
    return dao;
  }

  public Record personEditHeaderDatas(Kv kv) {
    Record record = new Record();
    ValidationUtils.notBlank(kv.getStr("taskid"), "制造工单任务ID缺失，获取数据异常！！！");
    MoMotask moMotask = findById(kv.getStr("taskid"));
    List<Record> records = dbTemplate("modocbatch.editplanviewdatas", Kv.by("id", moMotask.getIAutoId())).find();
    Map<String, Integer> map = new HashMap<>();
    for (Record record1 : records) {
      String dateKey = record1.getStr("yeartodate");
      if (map.containsKey(dateKey)) {
        map.put(dateKey, map.get(dateKey) + 1);
      } else {
        map.put(dateKey, 1);
      }
    }

    for (Record record1 : records) {
      record1.set("count", map.get(record1.getStr("yeartodate")));
      String weekDay = DateUtils.formatDate(DateUtils.parseDate(record1.getStr("yeartodate")), "E");
      if (weekDay.equals("星期一") || weekDay.equals("Mon")) {
        record1.set("weeks", "星期一");
      }
      if (weekDay.equals("星期二") || weekDay.equals("Tue")) {
        record1.set("weeks", "星期二");
      }
      if (weekDay.equals("星期三") || weekDay.equals("Wed")) {
        record1.set("weeks", "星期三");
      }
      if (weekDay.equals("星期四") || weekDay.equals("Thu")) {
        record1.set("weeks", "星期四");
      }
      if (weekDay.equals("星期五") || weekDay.equals("Fri")) {
        record1.set("weeks", "星期五");
      }
      if (weekDay.equals("星期六") || weekDay.equals("Sat")) {
        record1.set("weeks", "星期六");
      }
      if (weekDay.equals("星期日") || weekDay.equals("Sun")) {
        record1.set("weeks", "星期日");
      }
    }

    Map<String, List<Record>> list = new LinkedHashMap<>();
    for (Record record1 : records) {
      if (list.containsKey(record1.getStr("yeartodate"))) {
        List<Record> obj = list.get(record1.getStr("yeartodate"));
        obj.add(record1);
        list.put(record1.getStr("yeartodate"), obj);
      } else {
        List<Record> obj = new ArrayList<>();
        obj.add(record1);
        list.put(obj.get(0).getStr("yeartodate"), obj);
      }
    }
    List<List<Record>> records1 = new ArrayList<>();
    for (String str : list.keySet()) {
      List<Record> obj = list.get(str);
      Record record1 = new Record();
      record1.put("yeartodate", obj.get(0).getStr("yeartodate"));
      obj.add(record1);
      records1.add(obj);
    }

    Department byId = departmentService.findById(moMotask.getIDepartmentId());
    record.put("iautoid", byId.getIAutoId());
    record.put("depcode", byId.getCDepCode());
    record.put("depname", byId.getCDepName());
    record.put("startdate", records.get(0).getStr("yeartodate"));
    record.put("stopdate", records.get((records.size() - 1)).getStr("yeartodate"));
    record.put("datas", records1);
    return record;
  }

  /**
   * 编辑计划页面列表数据
   */
  public List<Record> getPlanPage(Kv kv) {
    MoMotask motask = findById(kv.getStr("iAutoId"));
    List<Record> moMotaskPage = dbTemplate("modocbatch.getPersonPage", kv).find();
    //拼日期  weeklist  colnamelist colname2list
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    for (Record record : moMotaskPage) {
      //根据存货记录查,按日期排序
      Kv data = new Kv();
      data.set("iInventoryId", record.getStr("iInventoryId"));
      data.set("iMoTaskId", motask.getIAutoId());
      List<Record> records = dbTemplate("modocbatch.getDocList", data).find();
      Map<String, List<Record>> recordMap = new HashMap<>();
      for (Record r : records) {
        String date = r.getStr("dplandate");
        if (!recordMap.containsKey(date)) {
          recordMap.put(date, new ArrayList<>());
        }
        recordMap.get(date).add(r);
      }


      //对齐前端拼的数据
      List<String> betweenDate = Util.getBetweenDate(sdf.format(motask.getDBeginDate()), sdf.format(motask.getDEndDate()));
      List<String> planList = new ArrayList<>();
      List<String> docnoList = new ArrayList<>();
      Map<String, Map> map = new HashMap<>();
      for (String date : betweenDate) {
        //todo 三个班次的区分
        if (recordMap.containsKey(date)) {
          // 当日期对应存在记录时，将记录组装到目标格式中
          for (Record re : recordMap.get(date)) {
            docnoList.add(re.getStr("cmodocno"));
            planList.add(re.getStr("iqty"));
            docnoList.add("");
            planList.add("");
            docnoList.add("");
            planList.add("");
          }
        } else {
          // 当日期对应不存在记录时，将空数据组装到目标格式中
          docnoList.add("");
          planList.add("");
          docnoList.add("");
          planList.add("");
          docnoList.add("");
          planList.add("");
        }
      }
      record.set("planList", planList);
      record.set("docnoList", docnoList);
    }
    return moMotaskPage;
  }

  /**
   * 编辑人员页面列表
   */
  public Page<Record> findDoc(int pageNumber, int pageSize, Long iAutoId) {
    return dbTemplate("modocbatch.getPersonPage", Okv.by("iAutoId", iAutoId)).paginate(pageNumber, pageSize);
  }

  public List<Record> findRoutingConfig(String docId) {
    return dbTemplate("modocbatch.findRoutingConfig", Okv.by("docId", docId)).find();
  }

  /**
   * 后台管理分页查询
   *
   * @param pageNumber
   * @param pageSize
   * @param keywords
   * @return
   */
  public Page<Record> paginateAdminDatas(int pageNumber, int pageSize, Kv keywords) {
    Page<Record> moMotaskPage = dbTemplate("modocbatch.getPage", keywords).paginate(pageNumber, pageSize);
//    List<Dictionary> listByTypeKeayName = new JBoltDictionaryService().getListByTypeKey("motask_audit");
//    HashMap<String, String> map = new HashMap<>();
//    for (Dictionary dictionary : listByTypeKeayName) {
//      map.put(dictionary.getSn(), dictionary.getName());
//    }
//    for (Record record : moMotaskPage.getList()) {
//      String istatus = record.getStr("istatus");
//      if (istatus != null) {
//        String s = map.get(istatus);
//        record.set("istatusname", s);
//      }
//    }
    return moMotaskPage;
  }

  /**
   * 保存
   *
   * @param moMotask
   * @return
   */
  public Ret save(MoMotask moMotask) {
    if (moMotask == null || isOk(moMotask.getIAutoId())) {
      return fail(JBoltMsg.PARAM_ERROR);
    }
    //if(existsName(moMotask.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
    boolean success = moMotask.save();
    if (success) {
      //添加日志
      //addSaveSystemLog(moMotask.getIautoid(), JBoltUserKit.getUserId(), moMotask.getName());
    }
    return ret(success);
  }


  /**
   * 工单计划数据
   */
  public List<Record> getApsMonthPlanSumPage(int pageNumber, int pageSize, Kv kv) {
    List<Record> scheduProductPlanMonthList = new ArrayList<>();

    String startDate = kv.getStr("startdate");
    String endDate = kv.getStr("enddate");
    if (notOk(startDate) || notOk(endDate)) {
      ValidationUtils.error("开始日期-结束日期不能为空！");
    }
    //排产开始日期到截止日期之间的日期集 包含开始到结束那天 有序
    List<String> scheduDateList = Util.getBetweenDate(startDate, endDate);

    pageSize = pageSize * 15;

    //TODO:根据日期及条件获取月周生产计划表数据三班汇总
    Page<Record> recordPage = dbTemplate("scheduproductplan.getApsMonthPlanSumList", kv).paginate(pageNumber, pageSize);
    List<Record> apsPlanQtyList = recordPage.getList();

    //key:产线id   value:List物料集
    Map<Long, List<String>> workInvListMap = new HashMap<>();
    //key:inv，   value:<yyyy-MM-dd，qty>
    Map<String, Map<String, BigDecimal>> invPlanDateMap = new HashMap<>();
    //key:inv   value:info
    Map<String, Record> invInfoMap = new HashMap<>();
    for (Record record : apsPlanQtyList) {
      Long iWorkRegionMid = record.getLong("iWorkRegionMid");
      String cInvCode = record.getStr("cInvCode");
      String iYear = record.getStr("iYear");
      int iMonth = record.getInt("iMonth");
      int iDate = record.getInt("iDate");
      BigDecimal planQty = record.getBigDecimal("iQty3");
      //yyyy-MM-dd
      String dateKey = iYear;
      dateKey = iMonth < 10 ? dateKey + "-0" + iMonth : dateKey + "-" + iMonth;
      dateKey = iDate < 10 ? dateKey + "-0" + iDate : dateKey + "-" + iDate;

      if (workInvListMap.containsKey(iWorkRegionMid)) {
        List<String> list = workInvListMap.get(iWorkRegionMid);
        list.add(cInvCode);
      } else {
        List<String> list = new ArrayList<>();
        list.add(cInvCode);
        workInvListMap.put(iWorkRegionMid, list);
      }

      if (invPlanDateMap.containsKey(cInvCode)) {
        //key:yyyy-MM-dd   value:qty
        Map<String, BigDecimal> dateQtyMap = invPlanDateMap.get(cInvCode);
        dateQtyMap.put(dateKey, planQty);
      } else {
        Map<String, BigDecimal> dateQtyMap = new HashMap<>();
        dateQtyMap.put(dateKey, planQty);
        invPlanDateMap.put(cInvCode, dateQtyMap);
      }
      invInfoMap.put(cInvCode, record);
    }


    //对产线逐个处理
    for (Long key : workInvListMap.keySet()) {
      List<String> recordList = workInvListMap.get(key);
      for (String inv : recordList) {
        //inv信息
        Record invInfo = invInfoMap.get(inv);
        //key:yyyy-MM-dd   value:qty
        Map<String, BigDecimal> dateQtyMap = invPlanDateMap.get(inv);

        //数据处理 行转列并赋值
        //scheduRowToColumn(scheduProductPlanMonthList,scheduDateList,invInfo,dateQtyMap,null);
      }
    }

    Record planRecord = new Record();
    planRecord.set("cInvCode", "物料编码");
    planRecord.set("cInvCode1", "存货");
    planRecord.set("cInvName1", "部番");
    planRecord.set("cWorkName", "产线");
    planRecord.set("qty1", 1);
    planRecord.set("qty2", 2);
    planRecord.set("qty3", 3);
    planRecord.set("qty4", 4);
    planRecord.set("qty5", 5);
    planRecord.set("qty6", 6);
    planRecord.set("qty7", 7);
    planRecord.set("qty8", 8);
    planRecord.set("qty9", 9);
    planRecord.set("qty10", 10);


    scheduProductPlanMonthList.add(planRecord);

    Page<Record> page = new Page<>();
    page.setPageNumber(recordPage.getPageNumber());
    page.setPageSize(recordPage.getPageSize() / 15);
    page.setTotalPage(recordPage.getTotalPage());
    page.setTotalRow(recordPage.getTotalRow() / 15);
    page.setList(scheduProductPlanMonthList);

    return scheduProductPlanMonthList;
  }


  /**
   * 更新
   *
   * @param moMotask
   * @return
   */
  public Ret update(MoMotask moMotask) {
    if (moMotask == null || notOk(moMotask.getIAutoId())) {
      return fail(JBoltMsg.PARAM_ERROR);
    }
    //更新时需要判断数据存在
    MoMotask dbMoMotask = findById(moMotask.getIAutoId());
    if (dbMoMotask == null) {
      return fail(JBoltMsg.DATA_NOT_EXIST);
    }
    //if(existsName(moMotask.getName(), moMotask.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
    boolean success = moMotask.update();
    if (success) {
      //添加日志
      //addUpdateSystemLog(moMotask.getIautoid(), JBoltUserKit.getUserId(), moMotask.getName());
    }
    return ret(success);
  }

  /**
   * 删除 指定多个ID
   *
   * @param ids
   * @return
   */
  public Ret deleteByBatchIds(String ids) {
    return deleteByIds(ids, true);
  }

  /**
   * 删除
   *
   * @param id
   * @return
   */
  public Ret delete(Long id) {
    return deleteById(id, true);
  }

  /**
   * 删除数据后执行的回调
   *
   * @param moMotask 要删除的model
   * @param kv       携带额外参数一般用不上
   * @return
   */
  @Override
  protected String afterDelete(MoMotask moMotask, Kv kv) {
    //addDeleteSystemLog(moMotask.getIautoid(), JBoltUserKit.getUserId(),moMotask.getName());
    return null;
  }

  /**
   * 检测是否可以删除
   *
   * @param moMotask 要删除的model
   * @param kv       携带额外参数一般用不上
   * @return
   */
  @Override
  public String checkCanDelete(MoMotask moMotask, Kv kv) {
    //如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
    return checkInUse(moMotask, kv);
  }

  /**
   * 设置返回二开业务所属的关键systemLog的targetType
   *
   * @return
   */
  @Override
  protected int systemLogTargetType() {
    return ProjectSystemLogTargetType.NONE.getValue();
  }

  /**
   * 切换isdeleted属性
   */
  public Ret toggleIsDeleted(Long id) {
    return toggleBoolean(id, "IsDeleted");
  }

  /**
   * 检测是否可以toggle操作指定列
   *
   * @param moMotask 要toggle的model
   * @param column   操作的哪一列
   * @param kv       携带额外参数一般用不上
   * @return
   */
  @Override
  public String checkCanToggle(MoMotask moMotask, String column, Kv kv) {
    //没有问题就返回null  有问题就返回提示string 字符串
    return null;
  }

  /**
   * toggle操作执行后的回调处理
   */
  @Override
  protected String afterToggleBoolean(MoMotask moMotask, String column, Kv kv) {
    //addUpdateSystemLog(moMotask.getIautoid(), JBoltUserKit.getUserId(), moMotask.getName(),"的字段["+column+"]值:"+moMotask.get(column));
    return null;
  }

  /**
   * 检测是否可以删除
   *
   * @param moMotask model
   * @param kv       携带额外参数一般用不上
   * @return
   */
  @Override
  public String checkInUse(MoMotask moMotask, Kv kv) {
    //这里用来覆盖 检测MoMotask是否被其它表引用
    return null;
  }

  /**
   * 制造工单人员批量编辑获取数据源
   *
   * @param kv
   * @return
   */
  public List<Record> getModocStaffEditorDatas(Kv kv) {
    ValidationUtils.notBlank(kv.getStr("taskid"), "制造工单任务ID缺失，获取数据异常！！！");
    // <editor-fold desc="A前置数据源--(iworkregionmid)产线id+(iinventoryid)存货id   根据taskid获取制造工单的产线id和存货id   key:(iworkregionmid)产线id+(iinventoryid)存货id  value:modocid">
    List<Record> docIdList = dbTemplate("modocbatch.getModocAllDatasByTaskid", kv).find();
    Map<String, String> modocidMap = new HashMap<>();
    List<String> modocids = new ArrayList<>();
    for (Record record : docIdList) {
      modocidMap.put(record.getStr("iWorkRegionMid") + record.getStr("iInventoryId"), record.getStr("iAutoId"));
      modocids.add(record.getStr("iAutoId"));
    }
    // </editor-fold>

    //<editor-fold desc="B前置数据源--工单工艺配置     modocids集查询  获取工单工艺配置    key:modocid  value:List<String>">
    List<Record> getRoutingConfigList = dbTemplate("modocbatch.getModocConfigtypeByModocid", Kv.by("imodocid", CollUtil.join(modocids, ","))).find();
    Map<String, List<Record>> getMoroutingconfigDatasMap = new HashMap<>();
    List<String> configIds = new ArrayList<>();
    for (Record record : getRoutingConfigList) {
      String modocid = record.getStr("iMoDocId");
      if (getMoroutingconfigDatasMap.containsKey(modocid)) {
        List<Record> list = getMoroutingconfigDatasMap.get(modocid);
        list.add(record);
      } else {
        List<Record> list = new ArrayList<>();
        list.add(record);
        getMoroutingconfigDatasMap.put(modocid, list);
      }
      String iAutoId = record.getStr("iAutoId");
      configIds.add(iAutoId);
    }
    //</editor-fold>

    //<editor-fold desc="C-1前置数据源--设备id  设备名称 key:moconfigid  value:List<String>">
    List<Record> recordsa1List = dbTemplate("modocbatch.getEquipmentnameByConfigid", Kv.by("configid", CollUtil.join(configIds, ","))).find();
    Map<String, List<Record>> recordsa1ListMap = new HashMap<>();
    for (Record record : recordsa1List) {
      String moconfigid = record.getStr("iMoRoutingConfigId");
      if (recordsa1ListMap.containsKey(moconfigid)) {
        List<Record> list = recordsa1ListMap.get(moconfigid);
        list.add(record);
      } else {
        List<Record> list = new ArrayList<>();
        list.add(record);
        recordsa1ListMap.put(moconfigid, list);
      }
    }
    //</editor-fold>

    //<editor-fold desc="C-1前置数据源--工序id 工序名称 key:moconfigid  value:List<String>">
    List<Record> recordsa2List = dbTemplate("modocbatch.getOperationnameByConfigid", Kv.by("configid", CollUtil.join(configIds, ","))).find();
    Map<String, List<Record>> recordsa2ListMap = new HashMap<>();
    for (Record record : recordsa2List) {
      String moconfigid = record.getStr("iMoRoutingConfigId");
      if (recordsa2ListMap.containsKey(moconfigid)) {
        List<Record> list = recordsa2ListMap.get(moconfigid);
        list.add(record);
      } else {
        List<Record> list = new ArrayList<>();
        list.add(record);
        recordsa2ListMap.put(moconfigid, list);
      }
    }
    //</editor-fold>

    // <editor-fold desc="1. <records>根据制造工单任务id获取同产线同存货的信息     根据制造工单任务ID获取年+月+日+班次数据信息 workShifts">
    List<Record> records = dbTemplate("modocbatch.getModocDatas", kv).find();
    List<Record> workShifts = dbTemplate("modocbatch.getModocWorkShifts", kv).find();
    // </editor-fold>

    //<editor-fold desc="D根据任务单id获取班长maps，其他人员1maps1，其他人员2maps2">
    List<Record> LeaderRecs = dbTemplate("modocbatch.getModocLeaderByTaskid", kv).find();
    Map<String, Record> maps = new HashMap<>();
    Map<String, Record> maps1 = new HashMap<>();
    Map<String, Record> maps2 = new HashMap<>();

    for (Record record1 : LeaderRecs) {
      record1.put("cmodocno", "");
      record1.put("datesplicing", "");
      record1.put("iautoid", "");
      record1.put("idate", "");
      record1.put("idutypersonid", "");
      record1.put("imonth", "");
      record1.put("ipersonnum", "");
      record1.put("iqty", "");
      record1.put("iworkshiftmid", "");
      record1.put("iyear", "");
      record1.put("psnname", "");
      record1.put("psnnum", "");
      record1.put("lock", false);
      if (record1.getStr("itype").equals("1")) {
        maps.put(record1.getStr("dataid"), record1);
      } else if (record1.getStr("itype").equals("2")) {
        maps1.put(record1.getStr("dataid"), record1);
      } else if (record1.getStr("itype").equals("3")) {
        maps2.put(record1.getStr("dataid"), record1);
      }
    }
    //</editor-fold>

    Record record4 = new Record();
    record4.set("idutypersonid", " ");
    record4.set("cpsn_name", " ");
    int recordssize = records.size() + 3;
    for (int qty = 0; qty < recordssize; qty++) {
      if (qty < records.size()) {
        List<Record> records1 = dbTemplate("modocbatch.getModocDateShiftDatas", kv.set("iinventoryid",
            records.get(qty).getStr("iinventoryid")).set("iworkregionmid", records.get(qty).getStr("iworkregionmid"))).find();

        // <editor-fold desc="2.根据产线id+存货id获得小行的制造工单(modoc)id">
        String modocid = modocidMap.get(records.get(qty).getStr("iworkregionmid") + records.get(qty).getStr("iinventoryid"));
        // </editor-fold>

        Map<String, String> map1 = new HashMap<>();
        List<String> modocidList = new ArrayList<>();

        List<String> modocidList2 = new ArrayList<>();

        Map<String, List<Record>> userMapDatas = new HashMap<>();

        for (Record record1 : records1) {
          String iYear = record1.getStr("iYear");
          String iMonth = record1.getStr("iMonth");
          String iDate = record1.getStr("iDate");
          String iWorkShiftMid = record1.getStr("iWorkShiftMid");
          String modocid1 = record1.getStr("iAutoId");
          String dateSplicing = iYear + iMonth + iDate + iWorkShiftMid;
          map1.put(dateSplicing, modocid1);
          modocidList.add(modocid);
          modocidList2.add(modocid1);
          //<editor-fold desc="创建人员信息为空的基础信息">
          List<Record> userDatas = new ArrayList<>();
          Record user = new Record();
          user.put("psnnum", "");
          user.put("psnname", "");
          user.put("dateSplicing", dateSplicing);
          user.put("iworkshiftmid", iWorkShiftMid);
          user.put("iAutoId", modocid1);
          user.put("iyear", iYear);
          user.put("imonth", iMonth);
          user.put("idate", iDate);
          user.put("cmodocno", "");
          user.put("iqty", "");
          user.put("datesplicing", "");
          user.put("ipersonnum", "");
          user.put("cpsn_name", "");
          user.put("idutypersonid", "");
          user.put("cpsn_num", "");
          user.put("dataid", "");
          user.put("ipersonid", "");
          user.put("itype", "");
          user.put("sdate", "");
          user.put("lock", false);
          userDatas.add(user);
          userMapDatas.put(dateSplicing, userDatas);
          //</editor-fold>
        }

        //<editor-fold desc="3.根据小行的制造工单(modoc)id获取">
        List<Record> records2 = getMoroutingconfigDatasMap.get(modocid) != null ? getMoroutingconfigDatasMap.get(modocid) : new ArrayList<>();
        //</editor-fold>
        if (records2.size() > 0) {
          List<Record> records3 = new ArrayList<>();
          for (int i = 0; i < (records2.size() + 4); i++) {
            Record recordDats = new Record();
            String iequipmentid = "";
            String ioperationid = "";

            //<editor-fold desc="4. 设备名称  工序名称处理">
            Record cequipmentnameRecord = new Record();
            Record equipmentnameRecord = new Record();
            equipmentnameRecord.set("cequipmentname", "");
            equipmentnameRecord.set("iequipmentid", "");
            cequipmentnameRecord.set("coperationname", "");
            cequipmentnameRecord.set("ioperationid", "");
            if (i < records2.size()) {
              if (records2.get(i).getStr("itype") != null) {
                List<Record> recordsa1 = recordsa1ListMap.get(records2.get(i).getStr("iautoid")) != null ?
                    recordsa1ListMap.get(records2.get(i).getStr("iautoid")) : new ArrayList<>();
                StringBuilder sa1 = new StringBuilder();
                StringBuilder sa2 = new StringBuilder();
                for (Record recorda1 : recordsa1) {
                  sa1.append(recorda1.getStr("cequipmentname") != null ? recorda1.getStr("cequipmentname") : "").append("/");
                  sa2.append(recorda1.getStr("iequipmentid") != null ? recorda1.getStr("iequipmentid") : "").append(",");
                }
                String cequipmentname = sa1.toString().endsWith("/") ? sa1.toString().substring(0, sa1.toString().length() - 1) : sa1.toString();//设备名称
                iequipmentid = sa2.toString().endsWith(",") ? sa2.toString().substring(0, sa2.toString().length() - 1) : sa2.toString();//设备id
                StringBuilder sb1 = new StringBuilder();
                StringBuilder sb2 = new StringBuilder();

                List<Record> recordsb1 = recordsa2ListMap.get(records2.get(i).getStr("iautoid")) != null ?
                    recordsa2ListMap.get(records2.get(i).getStr("iautoid")) : new ArrayList<>();
                for (Record recordb1 : recordsb1) {
                  sb1.append(recordb1.getStr("coperationname") != null ? recordb1.getStr("coperationname") : "").append("/");
                  sb2.append(recordb1.getStr("ioperationid") != null ? recordb1.getStr("ioperationid") : "").append(",");
                }
                String coperationname = sb1.toString().endsWith("/") ? sb1.toString().substring(0, sb1.toString().length() - 1) : sb1.toString();
                ioperationid = sb2.toString().endsWith(",") ? sb2.toString().substring(0, sb2.toString().length() - 1) : sb2.toString();

                equipmentnameRecord.set("cequipmentname", cequipmentname);
                equipmentnameRecord.set("iequipmentid", iequipmentid);


                cequipmentnameRecord.set("coperationname", coperationname);
                cequipmentnameRecord.set("ioperationid", ioperationid);

                recordDats.set("cequipment", equipmentnameRecord);
                recordDats.set("coperation", cequipmentnameRecord);
              } else {
                recordDats.set("cequipment", equipmentnameRecord);
                recordDats.set("coperation", cequipmentnameRecord);
              }
            } else {
              recordDats.set("cequipment", equipmentnameRecord);
              recordDats.set("coperation", cequipmentnameRecord);
            }
            //</editor-fold>


            //<editor-fold desc="5.获取人员信息 modocid1  value:List<String>">
            List<Record> list2 = dbTemplate("modocbatch.getModocPersonnameByDocid", Kv.by("docid", CollUtil.join(modocidList2, ","))).find();
            Map<String, List<Record>> map2 = new HashMap<>();
            for (Record record2 : list2) {
              String dateSplicing = record2.getStr("dateSplicing");
              if (map2.containsKey(dateSplicing)) {
                List<Record> list = map2.get(dateSplicing);
                list.add(record2);
                map2.put(dateSplicing, list);
              } else {
                List<Record> list = new ArrayList<>();
                list.add(record2);
                map2.put(dateSplicing, list);
              }
            }
            //</editor-fold>


            //<editor-fold desc="6 获取工单号，生产计划量，小计人数">
            List<Record> list3 = dbTemplate("modocbatch.getModocNoQtyNumByDocid", kv).find();
            //key:modocid1  value:cMoDocNo
            Map<String, List<Record>> map3 = new HashMap<>();
            Map<String, List<Record>> map4 = new HashMap<>();
            Map<String, List<Record>> map5 = new HashMap<>();
            for (Record recorda3 : list3) {
              String iAutoId = recorda3.getStr("iAutoId");
              Record record1 = new Record();
              Record record2 = new Record();
              Record record3 = new Record();

              record1.put("iqty", "");
              record1.put("datesplicing", "");
              record1.put("ipersonnum", "");
              record1.put("cpsn_name", "");
              record1.put("idutypersonid", "");
              record1.put("datesplicing", "");
              record1.put("iautoid", "");
              record1.put("idate", "");
              record1.put("imonth", "");
              record1.put("iworkshiftmid", "");
              record1.put("iyear", "");
              record1.put("psnname", "");
              record1.put("psnnum", "");
              record1.put("cpsn_num", "");
              record1.put("dataid", "");
              record1.put("ipersonid", "");
              record1.put("itype", "");
              record1.put("sdate", "");
              record1.put("lock", true);

              record2.put("cMoDocNo", "");
              record2.put("datesplicing", "");
              record2.put("ipersonnum", "");
              record2.put("cpsn_name", "");
              record2.put("idutypersonid", "");
              record2.put("datesplicing", "");
              record2.put("iautoid", "");
              record2.put("idate", "");
              record2.put("imonth", "");
              record2.put("iworkshiftmid", "");
              record2.put("iyear", "");
              record2.put("psnname", "");
              record2.put("psnnum", "");
              record2.put("lock", true);

              record2.put("cpsn_num", "");
              record2.put("dataid", "");
              record2.put("ipersonid", "");
              record2.put("itype", "");
              record2.put("sdate", "");

              record3.put("cMoDocNo", "");
              record3.put("iqty", "");
              record3.put("datesplicing", "");
              record3.put("ipersonnum", "");
              record3.put("cpsn_name", "");
              record3.put("idutypersonid", "");
              record3.put("datesplicing", "");
              record3.put("iautoid", "");
              record3.put("idate", "");
              record3.put("imonth", "");
              record3.put("iworkshiftmid", "");
              record3.put("iyear", "");
              record3.put("psnname", "");
              record3.put("psnnum", "");

              record3.put("cpsn_num", "");
              record3.put("dataid", "");
              record3.put("ipersonid", "");
              record3.put("itype", "");
              record3.put("sdate", "");
              record3.put("lock", true);

              record1.put("cMoDocNo", recorda3.getStr("cMoDocNo"));

              record2.put("iQty", recorda3.getStr("iQty"));

              record3.put("dateSplicing", recorda3.getStr("dateSplicing"));
              record3.put("dateSplicing", recorda3.getStr("dateSplicing"));
              record3.put("iPersonNum", recorda3.getStr("iPersonNum"));
              record3.put("dateSplicing", recorda3.getStr("dateSplicing"));

              List<Record> lista1 = new ArrayList<>();
              List<Record> lista2 = new ArrayList<>();
              List<Record> lista3 = new ArrayList<>();
              if (map3.containsKey(iAutoId)) {
                lista1 = map3.get(iAutoId);
                lista2 = map3.get(iAutoId);
                lista3 = map3.get(iAutoId);
                lista1.add(record1);
                lista2.add(record2);
                lista3.add(record3);
              } else {
                lista1.add(record1);
                lista2.add(record2);
                lista3.add(record3);
              }
              map3.put(iAutoId, lista1);
              map4.put(iAutoId, lista2);
              map5.put(iAutoId, lista3);
            }
            //</editor-fold>

            List<Record> list6 = dbTemplate("modocbatch.getModocDutyPersonnameByDocid", kv.set("iinventoryid",
                records.get(qty).getStr("iinventoryid")).set("iworkregionmid", records.get(qty).getStr("iworkregionmid"))).find();
            //key:modocid1  value:cMoDocNo
            Map<String, List<Record>> map6 = new HashMap<>();
            for (Record record3 : list6) {
              String iAutoId = record3.getStr("iAutoId");
              Record record1 = new Record();
              record3.put("lock", false);

              record1.put("datesplicing", "");
              record1.put("iautoid", "");
              record1.put("idate", "");
              record1.put("imonth", "");
              record1.put("iworkshiftmid", "");
              record1.put("iyear", "");
              record1.put("psnname", "");
              record1.put("psnnum", "");
              record1.put("cmodocno", "");
              record1.put("iqty", "");
              record1.put("ipersonnum", "");
              record1.put("cpsn_num", "");
              record1.put("dataid", "");
              record1.put("ipersonid", "");
              record1.put("itype", "");
              record1.put("sdate", "");
              record1.put("lock", false);
              record1.set("iDutyPersonId", record3.getStr("iDutyPersonId") == null ? "" : record3.getStr("iDutyPersonId"));
              record1.set("cPsn_Name", record3.getStr("cPsn_Name") == null ? "" : record3.getStr("cPsn_Name"));
              if (map6.containsKey(iAutoId)) {
                List<Record> list = map6.get(iAutoId);
                list.add(record1);
              } else {
                List<Record> list = new ArrayList<>();
                list.add(record1);
                map6.put(iAutoId, list);
              }
            }

            List<Record> userRecord = new ArrayList<>();
            for (Record record1 : records1) {
              String year = record1.getStr("iyear");
              String imonth = record1.getStr("imonth");
              String idate = record1.getStr("idate");
              String iworkshiftmid = record1.getStr("iworkshiftmid");
              Kv kv1 = Kv.by("iyear", record1.getStr("iyear")).
                  set("imonth", record1.getStr("imonth")).set("idate", record1.getStr("idate")).
                  set("iworkshiftmid", record1.getStr("iworkshiftmid")).set("taskid", kv.getStr("taskid"));
              String modocid1 = record1.getStr("iAutoId");
              String matchingid = year + imonth + idate + iworkshiftmid;
              boolean bol = false;
              if (map2.size() > 0) {
                List<Record> records11 = map2.get(matchingid) == null ? new ArrayList<>() : map2.get(matchingid);
                if (records11.size() > 0) {
                  bol = true;
                }
              }
              Record record2 = new Record();
              if (i < records2.size()) {
                //读取人员数据
                List<Record> records4 = bol ? map2.get(matchingid) : userMapDatas.get(matchingid);
                Record record = new Record();
                String psnname = "";
                String psnnum = "";
                for (int k = 0; k < records4.size(); k++) {
                  if (k == 0) {
                    psnname = records4.get(k).getStr("psnname");
                    psnnum = records4.get(k).getStr("psnnum");
                  } else {
                    psnname = psnname + "/" + records4.get(k).getStr("psnname");
                    psnnum = psnnum + "," + records4.get(k).getStr("psnnum");
                  }
                }
                record.put("psnnum", psnnum);
                record.put("psnname", psnname);
                record.put("lock", false);
                record2.set("personne", record);
              } else if (i == (records2.size())) {
                List<Record> records4 = map3.get(modocid1);
                record2.set("personne", records4.get(0));
              } else if (i == (records2.size() + 1)) {
                List<Record> records4 = map4.get(modocid1);
                record2.set("personne", records4.get(0));
              } else if (i == (records2.size() + 2)) {
                List<Record> records4 = map5.get(modocid1);
                record2.set("personne", records4.get(0));
              } else if (i == (records2.size() + 3)) {
                List<Record> records4 = map6.get(modocid1);
                if (records4 == null || records4.size() <= 0) {
                  records4.add(record4);
                }
                record2.set("personne", records4.get(0));
              }
              userRecord.add(record2);
            }
            recordDats.put("user", userRecord);
            records3.add(recordDats);
          }
          records.get(qty).set("rowdatas", records3);
        }
      } else {
        Record datas = new Record();
        List<Record> records1 = new ArrayList<>();
        //<editor-fold desc="模拟基础数据">
        Record cequipment = new Record();
        cequipment.put("cequipmentname", "");
        cequipment.put("iequipmentid", "");


        Record coperation = new Record();
        coperation.put("coperationname", "");
        coperation.put("ioperationid", "");

        datas.put("cequipment", cequipment);
        datas.put("coperation", coperation);
        //</editor-fold>

        List<Record> recordLisc = new ArrayList<>();
        Record record = new Record();
        record.put("cinvcode", "");
        record.put("cinvcode1", "");
        record.put("cinvname1", "");
        record.put("cworkname", "");
        record.put("iinventoryid", "");
        record.put("iworkregionmid", "");

        for (Record workShift : workShifts) {
          Record leaderRec = new Record();
          leaderRec.put("itype", "");
          leaderRec.put("ipersonid", "");
          leaderRec.put("cpsn_num", "");
          leaderRec.put("cpsn_name", "");

          leaderRec.put("cmodocno", "");
          leaderRec.put("datesplicing", "");
          leaderRec.put("iautoid", "");
          leaderRec.put("idate", "");
          leaderRec.put("idutypersonid", "");
          leaderRec.put("imonth", "");
          leaderRec.put("ipersonnum", "");
          leaderRec.put("iqty", "");
          leaderRec.put("iworkshiftmid", "");
          leaderRec.put("iyear", "");
          leaderRec.put("psnname", "");
          leaderRec.put("psnnum", "");

          leaderRec.put("lock", false);
          leaderRec.put("dataid", workShift.getStr("dataid"));
          leaderRec.put("sdate", workShift.getStr("sdate"));
          Record record2 = new Record();
          if (qty == (recordssize - 3)) {
            record2.put("personne", maps.get(workShift.getStr("dataid")) == null ? leaderRec : maps.get(workShift.getStr("dataid")));
            recordLisc.add(record2);
          } else if (qty == (recordssize - 2)) {
            record2.put("personne", maps1.get(workShift.getStr("dataid")) == null ? leaderRec : maps1.get(workShift.getStr("dataid")));
            recordLisc.add(record2);
          } else if (qty == (recordssize - 1)) {
            record2.put("personne", maps2.get(workShift.getStr("dataid")) == null ? leaderRec : maps2.get(workShift.getStr("dataid")));
            recordLisc.add(record2);
          }
        }

        datas.put("user", recordLisc);
        records1.add(datas);
        record.put("rowdatas", records1);
        records.add(record);
      }
    }
    return records;
  }

  /**
   * 制造工单计划批量编辑数据源
   */
  public List<Record> getEditorialPlanDatas(Kv kv) {
    ValidationUtils.notBlank(kv.getStr("taskid"), "制造工单任务ID缺失，获取数据异常！！！");

    //<editor-fold desc="A获取产线物料信息">
    List<Record> productionLineMaterials = dbTemplate("modocbatch.getModocDatas", kv).find();
    //</editor-fold>

    //<editor-fold desc="B获取日期班次信息 dateShifts ">
    List<Record> dateShifts = dbTemplate("modocbatch.getModocDateShiftDatas", kv).find();
    //</editor-fold>

    //<editor-fold desc="C获取单号数量信息">
    List<Record> planDatas = dbTemplate("modocbatch.getPlanDatasBytaskId", kv).find();
    //</editor-fold>

    for (Record productionLineMaterial : productionLineMaterials) {
      List<Record> datas = new ArrayList<>();
      for (Record dateShift : dateShifts) {
        for (Record plandata : planDatas) {
          if (plandata.getStr("mergeid").equals(productionLineMaterial.getStr("mergeid")) && plandata.getStr("dates").equals(dateShift.getStr("dates1"))) {
            Record data = new Record();
            data.put("cmodocno", plandata.getStr("cmodocno"));
            data.put("iqty", plandata.getStr("iqty"));
            data.put("iautoid", plandata.getStr("iAutoId"));
            data.put("ismodified", plandata.getStr("isModified"));
            datas.add(data);
            break;
          }
        }
      }
      productionLineMaterial.put("rowDatas", datas);
    }
    return productionLineMaterials;
  }

  /**
   * 编辑人员页面获取用户信息
   *
   * @param kv
   * @return
   */
  public Page<Record> getUserDatas(Kv kv) {
    ValidationUtils.notBlank(kv.getStr("pageNumber"), "每页条数数据未传入！！！");
    ValidationUtils.notBlank(kv.getStr("pageSize"), "分页数据未传入！！！");
//    ValidationUtils.notBlank(kv.getStr("depcode"), "部门编码未传入！！！");AND per.cdept_num=#para(depcode)
    return dbTemplate("modocbatch.getUserDatas", kv).paginate(kv.getInt("pageNumber"), kv.getInt("pageSize"));
  }

  /**
   * 重写制造工单人员编辑数据源
   *
   * @param kv
   * @return
   */
  public List<Record> getModocStaffEditorRewriteDatas(Kv kv) {
    //<editor-fold desc="1.查询产线名称，存货信息  records2">
    List<Record> records2 = dbTemplate("modocbatch.getModocDatas", kv).find();
    List<String> strings = new ArrayList<>();
    records2.forEach(record -> {
      strings.add(record.getStr("iinventoryid"));
    });
    String inventoryids = CollUtil.join(strings, ",");
    //</editor-fold>

    //<editor-fold desc="2.获取设备，产线信息">
    Map<String, Record> equipmentMap = new HashMap<>();
    Map<String, Record> productionlineMap = new HashMap<>();


    List<Record> equipments = dbTemplate("modocbatch.getEquipmentsByInventoryId", Kv.by("inventoryids", inventoryids)).find();
    equipments.forEach(record -> {
      if (equipmentMap.containsKey(record.getStr("iinventoryid"))) {
        Record records11 = equipmentMap.get(record.getStr("iinventoryid"));
        records11.set("iequipmentid", records11.getStr("iequipmentid") + "," + record.getStr("iequipmentid"));
        records11.set("cequipmentname", records11.getStr("cequipmentname") + "/" + record.getStr("cequipmentname"));
        equipmentMap.put(record.getStr("iinventoryid"), records11);
      } else {
        Record records11 = new Record();
        records11.set("iequipmentid", records11.getStr("iequipmentid"));
        records11.set("cequipmentname", records11.getStr("cequipmentname"));
        equipmentMap.put(record.getStr("iinventoryid"), records11);
      }
    });

    List<Record> productionlines = dbTemplate("modocbatch.getProductionLinesByInventoryId", Kv.by("inventoryids", inventoryids)).find();
    productionlines.forEach(record -> {
      if (productionlineMap.containsKey(record.getStr("iinventoryid"))) {
        Record records11 = productionlineMap.get(record.getStr("iinventoryid"));
        records11.set("ioperationid", records11.getStr("ioperationid") + "," + record.getStr("ioperationid"));
        records11.set("coperationname", records11.getStr("coperationname") + "/" + record.getStr("coperationname"));
        productionlineMap.put(record.getStr("iinventoryid"), records11);
      } else {
        Record records11 = new Record();
        records11.set("ioperationid", records11.getStr("ioperationid"));
        records11.set("coperationname", records11.getStr("coperationname"));
        productionlineMap.put(record.getStr("iinventoryid"), records11);
      }
    });
    //</editor-fold>


    int circulation = records2.size() + 3;
    for (int i = 0; i < circulation; i++) {
      Record record = new Record();
      if (i < (circulation - 3)) {
        record.put("cequipment", equipmentMap.get(records2.get(i).getStr("iinventoryid")));
        record.put("coperation", productionlineMap.get(records2.get(i).getStr("iinventoryid")));

      } else {

      }
      records2.add(record);
    }

    return records2;
  }


//  ------------------------------------------------------------------------审批流---------------------------------------------------------------------


  @Override
  public String postApproveFunc(long formAutoId, boolean isWithinBatch) {
    MoMotask moMotask = findById(formAutoId);
    // 审核状态修改
    moMotask.setIAuditStatus(AuditStatusEnum.APPROVED.getValue());
    moMotask.setIUpdateBy(JBoltUserKit.getUserId());
    moMotask.setCUpdateName(JBoltUserKit.getUserName());
    moMotask.setDUpdateTime(new Date());
//    moMotask.setIAuditBy(JBoltUserKit.getUserId());
//    moMotask.setCAuditName(JBoltUserKit.getUserName());
    moMotask.setDSubmitTime(new Date());
    moMotask.update();
    return null;
  }

  @Override
  public String postRejectFunc(long formAutoId, boolean isWithinBatch) {
    MoMotask moMotask = findById(formAutoId);
    // 审核状态修改
    moMotask.setIAuditStatus(AuditStatusEnum.REJECTED.getValue());
    moMotask.setIUpdateBy(JBoltUserKit.getUserId());
    moMotask.setCUpdateName(JBoltUserKit.getUserName());
    moMotask.setDUpdateTime(new Date());
//    moMotask.setIAuditBy(JBoltUserKit.getUserId());
//    moMotask.setCAuditName(JBoltUserKit.getUserName());
    moMotask.setDSubmitTime(new Date());
    moMotask.update();
    return null;
  }

  @Override
  public String preReverseApproveFunc(long formAutoId, boolean isFirst, boolean isLast) {
    return null;
  }

  @Override
  public String postReverseApproveFunc(long formAutoId, boolean isFirst, boolean isLast) {
    return null;
  }

  @Override
  public String preSubmitFunc(long formAutoId) {
    MoMotask moMotask = findById(formAutoId);
    switch (AuditStatusEnum.toEnum(moMotask.getIAuditStatus())) {
      // 已保存
      case NOT_AUDIT:
        // 不通过
      case REJECTED:

        break;
      default:
        return "订单非已保存状态";
    }
    return null;
  }

  @Override
  public String postSubmitFunc(long formAutoId) {
    return null;
  }

  @Override
  public String postWithdrawFunc(long formAutoId) {
    return null;
  }

  @Override
  public String withdrawFromAuditting(long formAutoId) {
    ValidationUtils.isTrue(updateColumn(formAutoId, "iAuditStatus", AuditStatusEnum.NOT_AUDIT.getValue()).isOk(), "撤回失败");
    return null;
  }

  @Override
  public String preWithdrawFromAuditted(long formAutoId) {
    return null;
  }

  @Override
  public String postWithdrawFromAuditted(long formAutoId) {
    return null;
  }

  @Override
  public String postBatchApprove(List<Long> formAutoIds) {
    for (Long formAutoId : formAutoIds) {
      MoMotask moMotask = findById(formAutoId);
      // 审核状态修改
      moMotask.setIAuditStatus(AuditStatusEnum.APPROVED.getValue());
      moMotask.setIUpdateBy(JBoltUserKit.getUserId());
      moMotask.setCUpdateName(JBoltUserKit.getUserName());
      moMotask.setDUpdateTime(new Date());
//      moMotask.setIAuditBy(JBoltUserKit.getUserId());
//      moMotask.setCAuditName(JBoltUserKit.getUserName());
      moMotask.setDSubmitTime(new Date());
      moMotask.update();
    }
    return null;
  }

  @Override
  public String postBatchReject(List<Long> formAutoIds) {
    for (Long formAutoId : formAutoIds) {
      MoMotask moMotask = findById(formAutoId);
      // 审核状态修改
      moMotask.setIAuditStatus(AuditStatusEnum.REJECTED.getValue());
      moMotask.setIUpdateBy(JBoltUserKit.getUserId());
      moMotask.setCUpdateName(JBoltUserKit.getUserName());
      moMotask.setDUpdateTime(new Date());
//      moMotask.setIAuditBy(JBoltUserKit.getUserId());
//      moMotask.setCAuditName(JBoltUserKit.getUserName());
      moMotask.setDSubmitTime(new Date());
      moMotask.update();
    }
    return null;
  }

  @Override
  public String postBatchBackout(List<Long> formAutoIds) {
    return null;
  }
}
