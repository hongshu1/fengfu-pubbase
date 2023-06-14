package cn.rjtech.admin.momotask;

import cn.hutool.core.collection.CollUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.model.Dictionary;
import cn.jbolt.core.service.JBoltDictionaryService;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.department.DepartmentService;
import cn.rjtech.admin.modoc.MoDocService;
import cn.rjtech.admin.scheduproductplan.ScheduProductPlanMonthService;
import cn.rjtech.model.momdata.Department;
import cn.rjtech.model.momdata.MoMotask;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 制造工单任务 Service
 *
 * @ClassName: MoMotaskService
 * @author: chentao
 * @date: 2023-04-28 15:19
 */
public class MoMotaskService extends BaseService<MoMotask> {

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

  public Kv getOpenEditPlanViewDatas(Long id) {
    Kv kv = new Kv();
    MoMotask moMotask = findById(id);
    List<Record> records = dbTemplate("modocbatch.editplanviewdatas", Kv.by("id", moMotask.getIAutoId())).find();
    for (Record record : records) {
      String weekDay = DateUtils.formatDate(DateUtils.parseDate(record.getStr("yeartodate")), "E");
      if (weekDay.equals("星期一") || weekDay.equals("Mon")) {
        record.set("weeks", "星期一");
      }
      if (weekDay.equals("星期二") || weekDay.equals("Tue")) {
        record.set("weeks", "星期二");
      }
      if (weekDay.equals("星期三") || weekDay.equals("Wed")) {
        record.set("weeks", "星期三");
      }
      if (weekDay.equals("星期四") || weekDay.equals("Thu")) {
        record.set("weeks", "星期四");
      }
      if (weekDay.equals("星期五") || weekDay.equals("Fri")) {
        record.set("weeks", "星期五");
      }
      if (weekDay.equals("星期六") || weekDay.equals("Sat")) {
        record.set("weeks", "星期六");
      }
      if (weekDay.equals("星期日") || weekDay.equals("Sun")) {
        record.set("weeks", "星期日");
      }
    }
    Department byId = departmentService.findById(moMotask.getIDepartmentId());
    kv.put("depname", byId.getCDepName());
    kv.put("startdate", records.get(0).getStr("yeartodate"));
    kv.put("stopdate", records.get((records.size() - 1)).getStr("yeartodate"));
    kv.put("datas", records);
    return kv;
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
    List<Dictionary> listByTypeKeayName = new JBoltDictionaryService().getListByTypeKey("motask_audit");
    HashMap<String, String> map = new HashMap<>();
    for (Dictionary dictionary : listByTypeKeayName) {
      map.put(dictionary.getSn(), dictionary.getName());
    }
    for (Record record : moMotaskPage.getList()) {
      String istatus = record.getStr("istatus");
      if (istatus != null) {
        String s = map.get(istatus);
        record.set("istatusname", s);
      }
    }
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

    // <editor-fold desc="1. <records>根据制造工单任务id获取同产线同存货的信息     <records1>根据制造工单任务ID获取年月日数据信息">
    List<Record> records = dbTemplate("modocbatch.getModocDatas", kv).find();

    List<Record> records1 = dbTemplate("modocbatch.getModocDateShiftDatas", kv).find();
    // </editor-fold>

    for (Record record : records) {

      // <editor-fold desc="2.根据产线id+存货id获得小行的制造工单(modoc)id">
      String modocid = modocidMap.get(record.getStr("iworkregionmid") + record.getStr("iinventoryid"));
      // </editor-fold>

      Map<String, String> map1 = new HashMap<>();
      List<String> modocidList = new ArrayList<>();

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

        //<editor-fold desc="创建人员信息为空的基础信息">
        List<Record> userDatas = new ArrayList<>();
        Record user = new Record();
        user.set("psnnum", "");
        user.set("psnname", "");
        user.set("dateSplicing", dateSplicing);
        user.set("iworkshiftmid", iWorkShiftMid);
        user.set("iAutoId", modocid1);
        user.set("iyear", iYear);
        user.set("imonth", iMonth);
        user.set("idate", iDate);
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
          List<Record> list2 = dbTemplate("modocbatch.getModocPersonnameByDocid", Kv.by("docid", CollUtil.join(modocidList, ","))).find();
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
            record1.set("cMoDocNo", recorda3.getStr("cMoDocNo"));
            record3.set("dateSplicing", recorda3.getStr("dateSplicing"));
            record2.set("iQty", recorda3.getStr("iQty"));
            record3.set("dateSplicing", recorda3.getStr("dateSplicing"));
            record3.set("iPersonNum", recorda3.getStr("iPersonNum"));
            record3.set("dateSplicing", recorda3.getStr("dateSplicing"));
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

          List<Record> list6 = dbTemplate("modocbatch.getModocDutyPersonnameByDocid", Kv.by("docid", CollUtil.join(modocidList, ","))).find();
          //key:modocid1  value:cMoDocNo
          Map<String, List<Record>> map6 = new HashMap<>();
          for (Record record3 : list6) {
            String iAutoId = record3.getStr("iAutoId");
            Record record1 = new Record();
            record1.set("iDutyPersonId", record3.getBigDecimal("iDutyPersonId"));
            record1.set("cPsn_Name", record3.getBigDecimal("cPsn_Name"));
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
              if (map2.get(matchingid).size() > 0) {
                bol = true;
              }
            }
            Record record2 = new Record();
            if (i < records2.size()) {
              //读取人员数据
              List<Record> records4 = bol ? map2.get(matchingid) : userMapDatas.get(matchingid);
              record2.set("personnel", records4);
            } else if (i == (records2.size())) {
              List<Record> records4 = map3.get(modocid1);
              record2.set("personnel", records4);
            } else if (i == (records2.size() + 1)) {
              List<Record> records4 = map4.get(modocid1);
              record2.set("personnel", records4);
            } else if (i == (records2.size() + 2)) {
              List<Record> records4 = map5.get(modocid1);
              record2.set("personnel", records4);
            } else if (i == (records2.size() + 3)) {
              List<Record> records4 = map6.get(modocid1);
              record2.set("personnel", records4);
            }
            userRecord.add(record2);
          }
          recordDats.put("user", userRecord);
          records3.add(recordDats);
        }
        record.set("rowdatas", records3);
      }
    }
    return records;

  }

}
