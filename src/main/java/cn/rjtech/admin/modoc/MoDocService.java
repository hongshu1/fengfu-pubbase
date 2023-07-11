package cn.rjtech.admin.modoc;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.User;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.inventory.InventoryService;
import cn.rjtech.admin.inventoryrouting.InventoryRoutingService;
import cn.rjtech.admin.inventoryroutingconfig.InventoryRoutingConfigService;
import cn.rjtech.admin.inventoryroutingequipment.InventoryRoutingEquipmentService;
import cn.rjtech.admin.inventoryroutinginvc.InventoryRoutingInvcService;
import cn.rjtech.admin.inventoryroutingsop.InventoryRoutingSopService;
import cn.rjtech.admin.modoc.vo.PushU8ReqVo;
import cn.rjtech.admin.momoroutingsop.MoMoroutingsopService;
import cn.rjtech.admin.morouting.MoMoroutingService;
import cn.rjtech.admin.moroutingcinve.MoMoroutinginvcService;
import cn.rjtech.admin.moroutingconfig.MoMoroutingconfigService;
import cn.rjtech.admin.moroutingconfigequipment.MoMoroutingequipmentService;
import cn.rjtech.admin.moroutingconfigperson.MoMoroutingconfigPersonService;
import cn.rjtech.admin.moworkshiftd.MoWorkShiftDService;
import cn.rjtech.admin.moworkshiftm.MoWorkShiftMService;
import cn.rjtech.admin.person.PersonService;
import cn.rjtech.admin.workregionm.WorkregionmService;
import cn.rjtech.base.exception.ParameterException;
import cn.rjtech.entity.vo.instockqcformm.MoDocFormVo;
import cn.rjtech.enums.MonthOrderStatusEnum;
import cn.rjtech.enums.WeekOrderStatusEnum;
import cn.rjtech.model.momdata.*;
import cn.rjtech.service.approval.IApprovalService;
import cn.rjtech.service.func.mom.MomDataFuncService;
import cn.rjtech.util.DateUtils;
import cn.rjtech.util.ValidationUtils;
import cn.rjtech.wms.utils.HttpApiUtils;
import cn.rjtech.wms.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.javaparser.utils.Log;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static cn.hutool.core.text.StrPool.COMMA;

/**
 * 工单管理 Service
 *
 * @ClassName: MoDocService
 * @author: RJ
 * @date: 2023-04-26 16:15
 */
public class MoDocService extends BaseService<MoDoc> implements IApprovalService {
  @Inject
  private InventoryRoutingService inventoryRoutingService;
  @Inject
  private InventoryRoutingConfigService inventoryRoutingConfigService;
  @Inject
  private InventoryRoutingEquipmentService inventoryRoutingEquipmentService;
  @Inject
  private InventoryRoutingInvcService inventoryRoutingInvcService;

  @Inject
  private MoMoroutingService moMoroutingService;
  @Inject
  private MoMoroutingconfigService moMoroutingconfigService;
  @Inject
  private MoMoroutingconfigPersonService moMoroutingconfigPersonService; //工单人员
  @Inject
  private MoMoroutingequipmentService moroutingequipmentService; //工单设备
  @Inject
  private MoMoroutinginvcService moMoroutinginvcService; //工单物料

  @Inject
  private MomDataFuncService momDataFuncService;
  @Inject
  private PersonService personService;//人员
  @Inject
  private InventoryRoutingSopService inventoryRoutingSopService; //物料-作业指导书

  @Inject
  private MoMoroutingsopService moMoroutingsopService;//工单作业指导书

  @Inject
  private InventoryService inventoryService; //存货档案

  @Inject
  private WorkregionmService workregionmService; //产线

  @Inject
  private MoWorkShiftDService moWorkShiftDService; //指定日期班次人员信息明细

  @Inject
  private MoWorkShiftMService moWorkShiftMService; //指定日期班次人员信息主表


  private final MoDoc dao = new MoDoc().dao();

  @Override
  protected MoDoc dao() {
    return dao;
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
    return dbTemplate("modoc.getPage", keywords).paginate(pageNumber, pageSize);
  }

  /**
   * 查询生产任务
   *
   * @param
   * @return
   */
  public HashMap<String, String> getJob(Long id) {
    List<Record> records = dbTemplate("modoc.getJob", new Kv().set("id", id)).find();
    HashMap<String, String> map = new HashMap<>();
    for (Record record : records) {
      String jobCode = record.getStr("cMoJobSn");
      String planQty = record.getInt("iPlanQty").toString();
      String realQty = record.getInt("iRealQty").toString();
      String status = record.getStr("iStatus").equals("1") ? "未完成" : "已完成";
      String updateTime = record.getTimestamp("dUpdateTime").toString();
      map.put("paln" + jobCode, planQty);
      map.put("actual" + jobCode, realQty);
      map.put("sate" + jobCode, status);
      map.put("time" + jobCode, updateTime);
    }
    return map;
  }

  /**
   * 保存
   */
  public Ret save(JBoltTable jBoltTable) {
    MoDoc moDoc = jBoltTable.getFormModel(MoDoc.class, "moDoc");
    Map<String,Object> map=new HashMap<>();
    if (moDoc == null || isOk(moDoc.getIAutoId())) {
      return fail(JBoltMsg.PARAM_ERROR);
    }
    Record formRecord = jBoltTable.getFormRecord();
    ValidationUtils.notNull(moDoc.getIWorkRegionMid(), "未找到产线,请在存货档案维护产线!");
    ValidationUtils.notNull(moDoc.getIDepartmentId(), "未找到部门，请在存货档案维护部门!");

    moDoc.setIType(2);
    //手工新增没有任务,先占着
    moDoc.setIMoTaskId(1001L);
    moDoc.setIWorkRegionMid(moDoc.getIWorkRegionMid());
    moDoc.setIInventoryId(moDoc.getIInventoryId());
    moDoc.setCMoDocNo(generateBarCode());
    Date date = moDoc.getDPlanDate();
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    moDoc.setIYear(calendar.get(Calendar.YEAR));
    moDoc.setIMonth(calendar.get(Calendar.MONTH));
    moDoc.setIDate(calendar.get(Calendar.DAY_OF_MONTH));
    moDoc.setIDepartmentId(moDoc.getIDepartmentId());
    moDoc.setIWorkShiftMid(moDoc.getIWorkShiftMid());
    moDoc.setIQty(moDoc.getIQty());
    moDoc.setICompQty(moDoc.getICompQty());
    moDoc.setIPersonNum(0);
    moDoc.setIStatus(0);
    moDoc.setIInventoryRouting(moDoc.getIInventoryRouting());
    moDoc.setCRoutingName(moDoc.getCRoutingName());
    InventoryRouting byId = inventoryRoutingService.findById(moDoc.getIInventoryRouting());
    moDoc.setCVersion(byId.getCVersion());
    moDoc.setICreateBy(JBoltUserKit.getUserId());
    moDoc.setCCreateName(JBoltUserKit.getUserName());
    moDoc.setDCreateTime(new Date());
    Date dCreateTime = new Date();
    String userName = JBoltUserKit.getUserName();
    Long userId = JBoltUserKit.getUserId();
    moDoc.setICreateBy(userId);
    moDoc.setCCreateName(userName);
    moDoc.setDCreateTime(dCreateTime);
    moDoc.setIAuditWay(1);
    moDoc.setIAuditStatus(0);
    moDoc.setIsDeleted(false);
    tx(() -> {
      moDoc.save();
      // 工艺路线
      map.put("iautoid",moDoc.getIAutoId());
      MoMorouting moMorouting = new MoMorouting();
      moMorouting.setIMoDocId(moDoc.getIAutoId());
      moMorouting.setIInventoryId(moDoc.getIInventoryId());
      moMorouting.setIInventoryRoutingId(moDoc.getIInventoryRouting());
      moMorouting.setCVersion(byId.getCVersion());
      moMorouting.setCRoutingName(byId.getCRoutingName());
      moMorouting.setIFinishedRate(byId.getIFinishedRate());
      moMoroutingService.save(moMorouting);

      //存工艺路线工序,人员,设备,物料集
      List<MoDocFormVo> saveModelList1 = jBoltTable.getSaveModelList(MoDocFormVo.class);
      for (MoDocFormVo record : saveModelList1) {
        String iautoid = record.getIautoid();
        InventoryRoutingConfig inventoryRoutingConfigServiceById = inventoryRoutingConfigService.findById(iautoid);
        //工序
        MoMoroutingconfig moMoroutingconfig = new MoMoroutingconfig();
        moMoroutingconfig.setIMoRoutingId(moMorouting.getIAutoId());
        moMoroutingconfig.setIInventoryRoutingId(inventoryRoutingConfigServiceById.getIInventoryRoutingId());
        moMoroutingconfig.setISeq(record.getIseq());
        moMoroutingconfig.setCMergedSeq(record.getCmergedseq());
        moMoroutingconfig.setCCreateName(record.getCcreatename());
        moMoroutingconfig.setCOperationName(record.getCoperationname());
        moMoroutingconfig.setIMergedNum(record.getImergednum());
        if (null != record.getImergerate()) {
          moMoroutingconfig.setIMergeRate(new BigDecimal(record.getImergerate()));
        }
        moMoroutingconfig.setIMergeSecs(record.getImergesecs());
        //moMoroutingconfig.setIRsInventoryId(Long.parseLong(record.getIrsinventoryid()));
        moMoroutingconfig.setIType(record.getItype());
        moMoroutingconfig.setICreateBy(userId);
          moMoroutingconfig.setDCreateTime(dCreateTime);
        moMoroutingconfigService.save(moMoroutingconfig);
        //人员
        String configperson = record.getConfigpersonids();

        if (configperson != null) {
          String[] arr = configperson.split(",");
          for (int i = 0; i < arr.length; i++) {
            MoMoroutingconfigPerson mp = new MoMoroutingconfigPerson();
            mp.setIMoRoutingConfigId(moMoroutingconfig.getIAutoId());
            mp.setIPersonId(Long.parseLong(arr[i]));
            moMoroutingconfigPersonService.save(mp);
          }
        }
        //设备
        List<InventoryRoutingEquipment> iInventoryEquipments = inventoryRoutingEquipmentService
            .getCommonListByKeywords(inventoryRoutingConfigServiceById.getIAutoId().toString(), "iAutoId", "iInventoryRoutingConfigId");
        for (InventoryRoutingEquipment iInventoryEquipment : iInventoryEquipments) {
          Long iEquipmentId = iInventoryEquipment.getIEquipmentId();
          MoMoroutingequipment moMoroutingequipment = new MoMoroutingequipment();
          moMoroutingequipment.setIEquipmentId(iEquipmentId);
          moMoroutingequipment.setIMoRoutingConfigId(moMoroutingconfig.getIAutoId());
          moroutingequipmentService.save(moMoroutingequipment);
        }
        //物料集
        List<InventoryRoutingInvc> iInventoryRoutingInvcs = inventoryRoutingInvcService
            .getCommonListByKeywords(inventoryRoutingConfigServiceById.getIAutoId().toString(), "iAutoId", "iInventoryRoutingConfigId");
        for (InventoryRoutingInvc iInventoryRoutingInvc : iInventoryRoutingInvcs) {
          MoMoroutinginvc moMoroutinginvc = new MoMoroutinginvc();
          moMoroutinginvc.setIInventoryRoutingConfigId(inventoryRoutingConfigServiceById.getIAutoId());
          moMoroutinginvc.setIMoRoutingConfigId(moMoroutingconfig.getIAutoId());
          moMoroutinginvc.setIInventoryId(iInventoryRoutingInvc.getIInventoryId());
          moMoroutinginvc.setIUsageUOM(iInventoryRoutingInvc.getIUsageUOM());
          moMoroutinginvc.setCMemo(iInventoryRoutingInvc.getCMemo());
          moMoroutinginvc.setICreateBy(userId);
          moMoroutinginvc.setCCreateName(userName);
          moMoroutinginvc.setDCreateTime(dCreateTime);
          moMoroutinginvcService.save(moMoroutinginvc);
        }
      }
      return true;
    });
    return successWithData(map);
  }

  /**
   * 保存
   * @param
   * @return
   */
  public Ret addDoc(JBoltTable jBoltTable, String rowid) {
    MoDoc moDoc = jBoltTable.getFormModel(MoDoc.class, "moDoc");
    if (moDoc == null || isOk(moDoc.getIAutoId())) {
      return fail(JBoltMsg.PARAM_ERROR);
    }

    //初始化状态
    moDoc.setIStatus(1);
    moDoc.setIType(2);
    //计划日期
    if (moDoc.getDPlanDate() != null) {
      Date date = moDoc.getDPlanDate();
      java.util.Calendar ca = java.util.Calendar.getInstance();
      ca.setTime(date);

      moDoc.setIYear(ca.get(java.util.Calendar.YEAR));
      moDoc.setIMonth(ca.get(Calendar.MONTH) + 1);
      moDoc.setIDate(ca.get(Calendar.DATE));
    }
    moDoc.setIPersonNum(0);

    moDoc.setIMoTaskId(1001L);//手工新增没有任务,先占着

    Date dCreateTime = new Date();
    String userName = JBoltUserKit.getUserName();
    Long userId = JBoltUserKit.getUserId();
    moDoc.setCMoDocNo(generateBarCode());
    moDoc.setICreateBy(userId);
    moDoc.setCCreateName(userName);
    moDoc.setDCreateTime(dCreateTime);
    // 工艺路线
    MoMorouting moMorouting = new MoMorouting();
    moMorouting.setIMoDocId(moDoc.getIAutoId());
    moMorouting.setIInventoryId(moDoc.getIInventoryId());
    moMorouting.setIInventoryRoutingId(moDoc.getIInventoryRouting());
    if (isOk(moDoc.getIInventoryRouting())) {
      InventoryRouting byId = inventoryRoutingService.findById(moDoc.getIInventoryRouting());
      moDoc.setCVersion(byId.getCVersion());
      moDoc.setCRoutingName(byId.getCRoutingName());
      moMorouting.setCVersion(byId.getCVersion());
      moMorouting.setCRoutingName(byId.getCRoutingName());
      moMorouting.setIFinishedRate(byId.getIFinishedRate());
    }

    tx(() -> {
      moDoc.save();

      moMorouting.setIMoDocId(moDoc.getIAutoId());


      moMoroutingService.save(moMorouting);
      //存工艺路线工序,人员,设备,物料集
      List<MoDocFormVo> saveModelList1 = jBoltTable.getSaveModelList(MoDocFormVo.class);
      Long iMoRoutingConfigId = null;
      MoMoroutingconfig moMoroutingconfig;
      for (MoDocFormVo record : saveModelList1) {
        String iautoid = record.getIautoid();
        System.out.print("iautoid" + iautoid);
        InventoryRoutingConfig inventoryRoutingConfigServiceById = inventoryRoutingConfigService.findById(iautoid);
        if (inventoryRoutingConfigServiceById != null) {

          //工序
          moMoroutingconfig = new MoMoroutingconfig();
          moMoroutingconfig.setIMoRoutingId(moMorouting.getIAutoId());
          moMoroutingconfig.setIInventoryRoutingId(inventoryRoutingConfigServiceById.getIInventoryRoutingId());
          moMoroutingconfig.setISecs(record.getIseq());
          moMoroutingconfig.setCMergedSeq(record.getCmergedseq());
          moMoroutingconfig.setCCreateName(record.getCcreatename());
          moMoroutingconfig.setCOperationName(record.getCoperationname());
          moMoroutingconfig.setIMergedNum(record.getImergednum());
          if (record.getImergerate() != null) {
            moMoroutingconfig.setIMergeRate(new BigDecimal(record.getImergerate()));//要员
          }
          moMoroutingconfig.setIMergeSecs(record.getImergesecs());
          //moMoroutingconfig.setIRsInventoryId(Long.parseLong(record.getIrsinventoryid()));
          moMoroutingconfig.setIType(record.getItype());
          moMoroutingconfig.setICreateBy(userId);
          moMoroutingconfig.setDCreateTime(dCreateTime);
          moMoroutingconfigService.save(moMoroutingconfig);
          if (StrUtil.isNotBlank(rowid) && rowid.equals(iautoid)) {
            iMoRoutingConfigId = moMoroutingconfig.getIAutoId();
          }

          //设备
          List<InventoryRoutingEquipment> iInventoryEquipments = inventoryRoutingEquipmentService
              .getCommonListByKeywords(inventoryRoutingConfigServiceById.getIAutoId().toString(), "iAutoId", "iInventoryRoutingConfigId");
          if (!iInventoryEquipments.isEmpty()) {
            MoMoroutingequipment moMoroutingequipment;
            for (InventoryRoutingEquipment iInventoryEquipment : iInventoryEquipments) {
              if (isOk(iInventoryEquipment.getIEquipmentId())) {
                Long iEquipmentId = iInventoryEquipment.getIEquipmentId();
                moMoroutingequipment = new MoMoroutingequipment();
                moMoroutingequipment.setIEquipmentId(iEquipmentId);
                moMoroutingequipment.setIMoRoutingConfigId(moMoroutingconfig.getIAutoId());
                moroutingequipmentService.save(moMoroutingequipment);
              }
            }
          }
          //物料集
          List<InventoryRoutingInvc> iInventoryRoutingInvcs = inventoryRoutingInvcService
              .getCommonListByKeywords(inventoryRoutingConfigServiceById.getIAutoId().toString(), "iAutoId", "iInventoryRoutingConfigId");
          if (!iInventoryRoutingInvcs.isEmpty()) {
            MoMoroutinginvc moMoroutinginvc;
            for (InventoryRoutingInvc iInventoryRoutingInvc : iInventoryRoutingInvcs) {
              moMoroutinginvc = new MoMoroutinginvc();
              moMoroutinginvc.setIInventoryRoutingConfigId(inventoryRoutingConfigServiceById.getIAutoId());
              moMoroutinginvc.setIMoRoutingConfigId(moMoroutingconfig.getIAutoId());
              moMoroutinginvc.setIInventoryId(iInventoryRoutingInvc.getIInventoryId());
              moMoroutinginvc.setIUsageUOM(iInventoryRoutingInvc.getIUsageUOM());
              moMoroutinginvc.setCMemo(iInventoryRoutingInvc.getCMemo());
              moMoroutinginvc.setICreateBy(userId);
              moMoroutinginvc.setCCreateName(userName);
              moMoroutinginvc.setDCreateTime(dCreateTime);
              moMoroutinginvcService.save(moMoroutinginvc);
            }
          }
          //作业指导书
          List<InventoryRoutingSop> moMoroutinginvcs = inventoryRoutingSopService.getCommonListByKeywords(inventoryRoutingConfigServiceById.getIAutoId().toString(), "iAutoId", "iInventoryRoutingConfigId");
          if (!moMoroutinginvcs.isEmpty()) {
            MoMoroutingsop moMoroutingsop;
            for (InventoryRoutingSop inventoryRoutingSop : moMoroutinginvcs) {
              moMoroutingsop = new MoMoroutingsop();
              moMoroutingsop.setIInventoryRoutingConfigId(inventoryRoutingConfigServiceById.getIAutoId());
              moMoroutingsop.setIMoRoutingConfigId(moMoroutingconfig.getIAutoId());
              moMoroutingsop.setCName(inventoryRoutingSop.getCName());
              moMoroutingsop.setCPath(inventoryRoutingSop.getCPath());
              moMoroutingsop.setCSuffix(inventoryRoutingSop.getCSuffix());
              moMoroutingsop.setISize(inventoryRoutingSop.getISize());
              moMoroutingsop.setIVersion(inventoryRoutingSop.getIVersion());
              moMoroutingsop.setDFromDate(inventoryRoutingSop.getDFromDate());
              moMoroutingsop.setDToDate(inventoryRoutingSop.getDToDate());
              moMoroutingsopService.save(moMoroutingsop);
            }
          }
        }
      }


      if (isOk(iMoRoutingConfigId)) {
        if (jBoltTable.getUpdate() != null) {
          int iPersonNum = 0;
          List<Record> recordList = jBoltTable.getUpdateRecordList();
          if (!recordList.isEmpty()) {
            MoMoroutingconfigPerson moMoroutingconfigPerson;
            for (Record r : recordList) {
              Long iPersonId = null;
              if (isOk(r.getLong("ipersonid"))) {
                Person p = personService.findById(r.getLong("ipersonid"));
                if (p == null) {
                  throw new ParameterException("存在无效人员信息");
                }

                iPersonId = p.getIAutoId();
              } else {
                if (StrUtil.isNotBlank(r.getStr("cpsn_num"))) {
                  Record p = personService.getpersonByCpsnnum(r.getStr("cpsn_num"));
                  if (p == null) {
                    throw new ParameterException("存在无效人员信息");
                  }

                  iPersonId = p.getLong("iautoid");
                }

              }
              moMoroutingconfigPerson = new MoMoroutingconfigPerson();
              moMoroutingconfigPerson.setIPersonId(iPersonId);
              moMoroutingconfigPerson.setIMoRoutingConfigId(iMoRoutingConfigId);
              moMoroutingconfigPerson.save();
              iPersonNum++;
            }
            System.out.print("iPersonNu........................................" + iPersonNum);
            if (iPersonNum > 0) {
              moDoc.setIPersonNum(iPersonNum);
              moDoc.setIStatus(2);
              moDoc.update();
            }
          }
        }
      }
      return true;
    });
    return successWithData(moDoc);
  }

  public Ret saveDoc(JBoltTable jBoltTable, String rowid) {
    MoDoc moDoc = jBoltTable.getFormModel(MoDoc.class, "moDoc");
    if (moDoc == null) {
      System.out.print("dddddddddds");
      return fail(JBoltMsg.PARAM_ERROR);
    }
    /// checkDoc(moDoc);
    if (!isOk(moDoc.getIInventoryId())) {
      return fail("缺少产线,请重新选择人员在提交");
    }
    if (!isOk(moDoc.getIDepartmentId())) {
      return fail("缺少部门,请重新选择人员在提交");
    }
    if (!isOk(moDoc.getIInventoryId())) {
      return fail("缺少存货编号,请重新选择人员在提交");
    }
    if (!isOk(moDoc.getIQty())) {
      return fail("缺少计划数,请重新选择人员在提交");
    }
    if (!isOk(moDoc.getDPlanDate())) {
      return fail("缺少计划日期,请重新选择人员在提交");
    }
    if (!isOk(moDoc.getIAutoId()) && StrUtil.isBlank(moDoc.getCMoDocNo())) {
      return addDoc(jBoltTable, rowid);
    }
    MoDoc oldModoc = findById(moDoc.getIAutoId());
    if (oldModoc == null) {
      oldModoc = findFirst(Okv.create().setIfNotBlank(MoDoc.CMODOCNO, moDoc.getCMoDocNo()));
      if (oldModoc == null) {
        return addDoc(jBoltTable, rowid);
      }


    }
    //初始化状态
    //moDoc.setIStatus(1);
    //moDoc.setIType(2);
    if (moDoc.getDPlanDate() != null) {
      Date date = moDoc.getDPlanDate();
      java.util.Calendar ca = java.util.Calendar.getInstance();
      ca.setTime(date);

      oldModoc.setIYear(ca.get(java.util.Calendar.YEAR));
      oldModoc.setIMonth(ca.get(Calendar.MONTH) + 1);
      oldModoc.setIDate(ca.get(Calendar.DATE));
    }

    Date dCreateTime = new Date();
    String userName = JBoltUserKit.getUserName();
    Long userId = JBoltUserKit.getUserId();
    //moDoc.setCMoDocNo(generateBarCode());
    oldModoc.setIUpdateBy(userId);
    oldModoc.setCUpdateName(userName);
    oldModoc.setDUpdateTime(dCreateTime);
    oldModoc.setIWorkRegionMid(moDoc.getIWorkRegionMid());//产线
    oldModoc.setIDepartmentId(moDoc.getIDepartmentId());
    oldModoc.setIQty(moDoc.getIQty()); //数量
    // oldModoc.setIInventoryRouting(moDoc.getIInventoryRouting());//工艺路线ID

    //存货ID发变化则清除原来数据
    Inventory inventory = inventoryService.findById(moDoc.getIInventoryId());
    if (inventory == null) {
      return fail("无效存货档案信息");
    }
    if (!inventory.getIAutoId().equals(oldModoc.getIInventoryId())) {

      return clearAndSaveLines(oldModoc, jBoltTable, rowid, dCreateTime);
    }
    // 工艺路线
   /* MoMorouting moMorouting = new MoMorouting();
    moMorouting.setIMoDocId(moDoc.getIAutoId());
    moMorouting.setIInventoryId(moDoc.getIInventoryId());//存货ID
    moMorouting.setIInventoryRoutingId(moDoc.getIInventoryRouting());
    if(isOk(moDoc.getIInventoryRouting())){
      InventoryRouting byId = inventoryRoutingService.findById(moDoc.getIInventoryRouting());
      moDoc.setCVersion(byId.getCVersion());
      moDoc.setCRoutingName(byId.getCRoutingName());
      moMorouting.setCVersion(byId.getCVersion());
      moMorouting.setCRoutingName(byId.getCRoutingName());
      moMorouting.setIFinishedRate(byId.getIFinishedRate());
    }*/

    MoDoc finalOldModoc = oldModoc;
    tx(() -> {

      update(finalOldModoc);

      //moMorouting.setIMoDocId(moDoc.getIAutoId());
      //moMoroutingService.save(moMorouting)
      MoMorouting moMorouting = moMoroutingService.findFirst(Okv.create().set(MoMorouting.IMODOCID, finalOldModoc.getIAutoId()),
          MoMorouting.IAUTOID, "desc");
      savelines(jBoltTable, rowid, moMorouting.getIAutoId(), dCreateTime);
      return true;

    });


    return successWithData(finalOldModoc);
  }

  public Ret subSave(JBoltTable jBoltTable, String rowid) {
    Ret ret = saveDoc(jBoltTable, rowid);
    MoDoc modoc = (MoDoc) ret.get("data");
    if (modoc != null && modoc.getIStatus().equals(7)) {
      return fail("已经是关闭状态");
    }
    // modoc.setIStatus(3);
    modoc.setIUpdateBy(JBoltUserKit.getUserId());
    modoc.setCUpdateName(JBoltUserKit.getUserName());
    modoc.setDUpdateTime(new Date());
    tx(() -> {
      modoc.update();
      return true;
    });
    return successWithData(modoc);
  }


  private Ret clearAndSaveLines(MoDoc oldModoc, JBoltTable jBoltTable, String rowid, Date dCreateTime) {

    MoDoc moDoc = jBoltTable.getFormModel(MoDoc.class, "moDoc");
    // 工艺路线

    MoMorouting oldmoMorouting = moMoroutingService.findFirst(Okv.create().set(MoMorouting.IMODOCID, oldModoc.getIAutoId()), MoMorouting.IAUTOID, "desc");
    /*if(oldmoMorouting!=null&&oldmoMorouting.getIInventoryId().equals(moDoc.getIInventoryId())) {
      return  successWithData(oldModoc);
    }*/

    oldmoMorouting.setIInventoryId(moDoc.getIInventoryId());//存货ID
    Long iInventoryRoutingId = oldmoMorouting.getIInventoryRoutingId();
    if (isOk(moDoc.getIInventoryRouting())) {
      oldmoMorouting.setIInventoryRoutingId(moDoc.getIInventoryRouting());
      InventoryRouting byId = inventoryRoutingService.findById(moDoc.getIInventoryRouting());
      if (byId != null) {
        oldModoc.setIInventoryRouting(byId.getIAutoId());
        oldModoc.setCVersion(byId.getCVersion());
        oldModoc.setCRoutingName(byId.getCRoutingName());
        oldmoMorouting.setCVersion(byId.getCVersion());
        oldmoMorouting.setCRoutingName(byId.getCRoutingName());
        oldmoMorouting.setIFinishedRate(byId.getIFinishedRate());
      }
    }
    tx(() -> {
      oldModoc.update();//更新单据主表

      //工艺路线
      moMoroutingService.updateOneColumn(oldmoMorouting);
      //删除原装载数据
      //工序信息
      //工序
      /* List<MoMoroutingconfig> moMoroutingconfigs =moMoroutingconfigService.getCommonList(Okv.create().
              set(MoMoroutingconfig.IMOROUTINGID,oldmoMorouting.getIAutoId()),MoMoroutingconfig.IAUTOID);
        if(!moMoroutingconfigs.isEmpty()) {
          for(MoMoroutingconfig moMoroutingconfig:moMoroutingconfigs) {*/
      // moMoroutingconfigService.deleteBy(Okv.create().set(MoMoroutingconfig.IINVENTORYROUTINGID, iInventoryRoutingId));
      //物料信息
      //moMoroutinginvcService.deleteBy(Okv.create().set(MoMoroutinginvc.IINVENTORYROUTINGCONFIGID, iInventoryRoutingId));
      //设备信息
      // moroutingequipmentService.deleteBy(Okv.create().set(MoMoroutingequipment.IMOROUTINGCONFIGID, iInventoryRoutingId));
      //作业指导书
      // moMoroutingsopService.deleteBy(Okv.create().set(MoMoroutingsop.IMOROUTINGCONFIGID, iInventoryRoutingId));
      //人员信息
      // moMoroutingconfigPersonService.deleteBy(Okv.create().set(MoMoroutingconfigPerson.IMOROUTINGCONFIGID, moMoroutingconfig.getIAutoId()));
      // }
      // }
      Integer iPersonNum = savelines(jBoltTable, rowid, oldmoMorouting.getIAutoId(), dCreateTime);
      if (iPersonNum > 0) {
        oldModoc.setIPersonNum(iPersonNum);
        oldModoc.setIStatus(2);
        oldModoc.update();
      }


      return true;
    });


    return SUCCESS;
  }

  public Integer savelines(JBoltTable jBoltTable, String rowid, Long imoroutingid, Date dCreateTime) {


    //存工艺路线工序,人员,设备,物料集
    List<MoDocFormVo> saveModelList1 = jBoltTable.getSaveModelList(MoDocFormVo.class);
    Long iMoRoutingConfigId = null;
    MoMoroutingconfig moMoroutingconfig;
    for (MoDocFormVo record : saveModelList1) {
      String iautoid = record.getIautoid();
      System.out.print("iautoid" + iautoid);
      InventoryRoutingConfig inventoryRoutingConfigServiceById = inventoryRoutingConfigService.findById(iautoid);
      if (inventoryRoutingConfigServiceById != null) {

        //工序
        moMoroutingconfig = new MoMoroutingconfig();
        moMoroutingconfig.setIMoRoutingId(imoroutingid);
        moMoroutingconfig.setIInventoryRoutingId(inventoryRoutingConfigServiceById.getIInventoryRoutingId());
        moMoroutingconfig.setISecs(record.getIseq());
        moMoroutingconfig.setCMergedSeq(record.getCmergedseq());
        moMoroutingconfig.setCCreateName(record.getCcreatename());
        moMoroutingconfig.setCOperationName(record.getCoperationname());
        moMoroutingconfig.setIMergedNum(record.getImergednum());
        if (record.getImergerate() != null) {
          moMoroutingconfig.setIMergeRate(new BigDecimal(record.getImergerate()));//要员
        }
        moMoroutingconfig.setIMergeSecs(record.getImergesecs());
        //moMoroutingconfig.setIRsInventoryId(Long.parseLong(record.getIrsinventoryid()));
        moMoroutingconfig.setIType(record.getItype());
        moMoroutingconfig.setICreateBy(JBoltUserKit.getUserId());
        moMoroutingconfig.setCCreateName(JBoltUserKit.getUserName());
        moMoroutingconfig.setDCreateTime(dCreateTime);
        moMoroutingconfigService.save(moMoroutingconfig);
        if (StrUtil.isNotBlank(rowid) && rowid.equals(iautoid)) {
          iMoRoutingConfigId = moMoroutingconfig.getIAutoId();
        }

        //设备
        List<InventoryRoutingEquipment> iInventoryEquipments = inventoryRoutingEquipmentService
            .getCommonListByKeywords(inventoryRoutingConfigServiceById.getIAutoId().toString(), "iAutoId", "iInventoryRoutingConfigId");
        if (!iInventoryEquipments.isEmpty()) {
          MoMoroutingequipment moMoroutingequipment;
          for (InventoryRoutingEquipment iInventoryEquipment : iInventoryEquipments) {
            Long iEquipmentId = iInventoryEquipment.getIEquipmentId();
            moMoroutingequipment = new MoMoroutingequipment();
            moMoroutingequipment.setIEquipmentId(iEquipmentId);
            moMoroutingequipment.setIMoRoutingConfigId(moMoroutingconfig.getIAutoId());
            moroutingequipmentService.save(moMoroutingequipment);
          }
        }
        //物料集
        List<InventoryRoutingInvc> iInventoryRoutingInvcs = inventoryRoutingInvcService
            .getCommonListByKeywords(inventoryRoutingConfigServiceById.getIAutoId().toString(), "iAutoId", "iInventoryRoutingConfigId");
        if (!iInventoryRoutingInvcs.isEmpty()) {
          MoMoroutinginvc moMoroutinginvc;
          for (InventoryRoutingInvc iInventoryRoutingInvc : iInventoryRoutingInvcs) {
            moMoroutinginvc = new MoMoroutinginvc();
            moMoroutinginvc.setIInventoryRoutingConfigId(inventoryRoutingConfigServiceById.getIAutoId());
            moMoroutinginvc.setIMoRoutingConfigId(moMoroutingconfig.getIAutoId());
            moMoroutinginvc.setIInventoryId(iInventoryRoutingInvc.getIInventoryId());
            moMoroutinginvc.setIUsageUOM(iInventoryRoutingInvc.getIUsageUOM());
            moMoroutinginvc.setCMemo(iInventoryRoutingInvc.getCMemo());
            moMoroutinginvc.setICreateBy(JBoltUserKit.getUserId());
            moMoroutinginvc.setCCreateName(JBoltUserKit.getUserName());
            moMoroutinginvc.setDCreateTime(dCreateTime);
            moMoroutinginvcService.save(moMoroutinginvc);
          }
        }
        //作业指导书
        List<InventoryRoutingSop> moMoroutinginvcs = inventoryRoutingSopService.getCommonListByKeywords(inventoryRoutingConfigServiceById.getIAutoId().toString(), "iAutoId", "iInventoryRoutingConfigId");
        if (!moMoroutinginvcs.isEmpty()) {
          MoMoroutingsop moMoroutingsop;
          for (InventoryRoutingSop inventoryRoutingSop : moMoroutinginvcs) {
            moMoroutingsop = new MoMoroutingsop();
            moMoroutingsop.setIInventoryRoutingConfigId(inventoryRoutingConfigServiceById.getIAutoId());
            moMoroutingsop.setIMoRoutingConfigId(moMoroutingconfig.getIAutoId());
            moMoroutingsop.setCName(inventoryRoutingSop.getCName());
            moMoroutingsop.setCPath(inventoryRoutingSop.getCPath());
            moMoroutingsop.setCSuffix(inventoryRoutingSop.getCSuffix());
            moMoroutingsop.setISize(inventoryRoutingSop.getISize());
            moMoroutingsop.setIVersion(inventoryRoutingSop.getIVersion());
            moMoroutingsop.setDFromDate(inventoryRoutingSop.getDFromDate());
            moMoroutingsop.setDToDate(inventoryRoutingSop.getDToDate());
            moMoroutingsopService.save(moMoroutingsop);
          }
        }
      } else {
        moMoroutingconfig = moMoroutingconfigService.findById(iautoid);
        if (moMoroutingconfig != null) {
          if (StrUtil.isNotBlank(rowid) && rowid.equals(iautoid)) {
            iMoRoutingConfigId = moMoroutingconfig.getIAutoId();
            //删除原有人员信息
            moMoroutingconfigPersonService.deleteBy(Okv.create().set(MoMoroutingconfigPerson.IMOROUTINGCONFIGID, moMoroutingconfig.getIAutoId()));

          }
        }
      }
    }
    int iPersonNum = 0;
    if (isOk(iMoRoutingConfigId)) {
      //人员处理
      if (jBoltTable.getUpdate() != null) {
        List<Record> recordList = jBoltTable.getUpdateRecordList();
        if (!recordList.isEmpty()) {

          MoMoroutingconfigPerson moMoroutingconfigPerson;
          for (Record r : recordList) {
            Long iPersonId = null;
            if (isOk(r.getLong("ipersonid"))) {
              Person p = personService.findById(r.getLong("ipersonid"));
              if (p == null) {
                throw new ParameterException("存在无效人员信息");
              }

              iPersonId = p.getIAutoId();
            } else {
              if (StrUtil.isNotBlank(r.getStr("cpsn_num"))) {
                Record p = personService.getpersonByCpsnnum(r.getStr("cpsn_num"));
                if (p == null) {
                  throw new ParameterException("存在无效人员信息");
                }

                iPersonId = p.getLong("iautoid");
              }

            }
            moMoroutingconfigPerson = new MoMoroutingconfigPerson();
            moMoroutingconfigPerson.setIPersonId(iPersonId);
            moMoroutingconfigPerson.setIMoRoutingConfigId(iMoRoutingConfigId);
            moMoroutingconfigPerson.save();
            iPersonNum++;
          }
        }
      }
    }
    System.out.print("iPersonNu........................................" + iPersonNum);
    return iPersonNum;
  }

  /**
   * 更新
   *
   * @param moDoc
   * @return
   */
  public Ret update(MoDoc moDoc) {
    if (moDoc == null || notOk(moDoc.getIAutoId())) {
      return fail(JBoltMsg.PARAM_ERROR);
    }
    //更新时需要判断数据存在
    MoDoc dbMoDoc = findById(moDoc.getIAutoId());
    if (dbMoDoc == null) {
      return fail(JBoltMsg.DATA_NOT_EXIST);
    }
    boolean success = moDoc.update();

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
   * @param moDoc 要删除的model
   * @param kv    携带额外参数一般用不上
   * @return
   */
  @Override
  protected String afterDelete(MoDoc moDoc, Kv kv) {
    //addDeleteSystemLog(moDoc.getIautoid(), JBoltUserKit.getUserId(),moDoc.getName());
    return null;
  }

  /**
   * 检测是否可以删除
   *
   * @param moDoc 要删除的model
   * @param kv    携带额外参数一般用不上
   * @return
   */
  @Override
  public String checkCanDelete(MoDoc moDoc, Kv kv) {
    //如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
    return checkInUse(moDoc, kv);
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
   * 根据产线id和人员编码查找指定日期班次人员信息明细表
   *
   * @param iworkregionmid 产线id
   * @param cpsnnum        人员编码
   * @return
   */
  public List<Record> getMoworkshiftdByUserAnRegionid(Long iworkregionmid, String cpsnnum) {
    return dbTemplate("modoc.getMoworkshiftdByUserAnRegionid", Kv.by("iworkregionmid", iworkregionmid).set("cpsnnum", cpsnnum)).find();
  }

  /**
   * 根据产线id和人员编码查找工单工艺人员配置
   *
   * @param iworkregionmid 产线id
   * @param cpsnnum        人员编码
   * @return
   */
  public List<Record> getMoroutingconfigpersonByUserAnRegionid(Long iworkregionmid, String cpsnnum) {
    return dbTemplate("modoc.getMoroutingconfigpersonByUserAnRegionid", Kv.by("iworkregionmid", iworkregionmid).set("cpsnnum", cpsnnum)).find();
  }

  public Ret savePerson(JBoltTable jBoltTable, Long configid) {
    return SUCCESS;
  }

  /**
   * 根据制造工单id查询工序名称信息
   *
   * @param modocid 制造工单id
   */
  public List<Record> getApiCoperationnameByModocId(String modocid) {
    return dbTemplate("modoc.getapicoperationnamebymodocid", Kv.by("modocid", modocid)).find();
  }

  /**
   * 根据料品工艺档案配置ID查询工单工序作业指导书信息
   *
   * @param inventoryroutingconfigid 料品工艺档案配置ID
   */
  public List<Record> getMoroutingsopByInventoryroutingconfigId(String inventoryroutingconfigid) {
    return dbTemplate("modoc.getmoroutingsopbyinventoryroutingconfigid", Kv.by("inventoryroutingconfigid", inventoryroutingconfigid)).find();
  }

  public Page<Record> page(Integer page, Integer pageSize, String cmodocno,
                           String cinvcode, String cinvcode1, String cinvname1,
                           String cdepname, Long iworkregionmid, Integer istatus,
                           Date starttime, Date endtime) {
    return dbTemplate("modoc.getPage", Kv.by("cMoDocNo", cmodocno).set("cInvCode", cinvcode).
        set("cInvCode1", cinvcode1).
        set("cinvname1", cinvname1).
        set("cDepName", cdepname).
        set("iWorkRegionMid", iworkregionmid).
        set("istatus", istatus).
        set("startdate", starttime).
        set("enddate", endtime)).paginate(page, pageSize);

  }

  public String generateBarCode() {
    String prefix = "SCGD";
    String format = DateUtil.format(DateUtil.date(), DatePattern.PURE_DATE_FORMAT);
    String s = momDataFuncService.getNextNo(prefix.concat(format), 4);

    return momDataFuncService.getNextRouteNo(1L, "SCGD"+s, 6);
  }

  public List<Record> getDocdetail(Long imodocid, Long iinventoryroutingid) {
    List<Record> records = dbTemplate("modoc.findDocdetail", Okv.by("iMoDocId", imodocid).set("iInventoryRoutingId", iinventoryroutingid)).find();
    if (!records.isEmpty()) {
      for (Record r : records) {
        if (isOk(r.getLong("iautoid"))) {
          r.set("personnames", moMoroutingconfigPersonService.getMoMoroutingconfigPersonNameData(r.getLong("iautoid")));
        }
      }
    }
    return records;

  }

  public Ret checkDoc(MoDoc moDoc) {
    if (!isOk(moDoc.getIInventoryId())) {
      return fail("缺少产线");
    }
    if (!isOk(moDoc.getIDepartmentId())) {
      return fail("缺少部门");
    }
    if (!isOk(moDoc.getIInventoryId())) {
      return fail("缺少存货编号");
    }
    if (!isOk(moDoc.getIQty())) {
      return fail("缺少计划数");
    }
    if (!isOk(moDoc.getDPlanDate())) {
      return fail("缺少计划日期");
    }
    return SUCCESS;
  }

  public Ret stopSave(JBoltTable jBoltTable, String rowid) {

    Ret ret = saveDoc(jBoltTable, rowid);
    MoDoc modoc = (MoDoc) ret.get("data");
    if (modoc != null && modoc.getIStatus().equals(7)) {
      return fail("已经是关闭状态");
    }
    modoc.setIStatus(7);
    modoc.setIUpdateBy(JBoltUserKit.getUserId());
    modoc.setCUpdateName(JBoltUserKit.getUserName());
    modoc.setDUpdateTime(new Date());
    tx(() -> {
      modoc.update();
      return true;
    });

    return SUCCESS;
  }

  public Ret deletemodoc(MoDoc modoc) {
    modoc.setIsDeleted(true);
    tx(() -> {
      modoc.update();
      return true;
    });
    return SUCCESS;
  }

  public Ret updateStatus(MoDoc modoc) {
    if (modoc != null && modoc.getIStatus().equals(7)) {
      return fail("已经是关闭状态");
    }
    modoc.setIStatus(7);
    modoc.setIUpdateBy(JBoltUserKit.getUserId());
    modoc.setCUpdateName(JBoltUserKit.getUserName());
    modoc.setDUpdateTime(new Date());
    tx(() -> {
      modoc.update();
      return true;
    });
    return SUCCESS;
  }
    /**
     * 获取工单对应产线关联所属仓库信息
     *
     * @param imdcocid
     */
    @UnCheck
    public void getModocWarehouse(Long imdcocid) {
        MoDoc moDoc = findById(imdcocid);
        //获取产线
        if (moDoc != null) {

        }
    }

  //推送u8数据接口
  public Ret pushU8(MoDoc moDoc) {

    List<PushU8ReqVo> rows = new ArrayList();
    PushU8ReqVo pushU8ReqVo = new PushU8ReqVo();
    pushU8ReqVo.setDocno(moDoc.getCMoDocNo());
    pushU8ReqVo.setDdate(DateUtils.formatDate(moDoc.getDCreateTime(), "yyyy-MM-dd"));
    pushU8ReqVo.setcWhCode("");
    Inventory inventory = inventoryService.findById(moDoc.getIInventoryId());
    if (inventory != null) {
      pushU8ReqVo.setCinvcode(inventory.getCInvCode());
      pushU8ReqVo.setcInvName(inventory.getCInvName());
      pushU8ReqVo.setcInvStd(inventory.getCInvStd());
    }
    if (moDoc.getICompQty() != null) {
      pushU8ReqVo.setIquantity(Integer.valueOf(moDoc.getICompQty().intValue()));
    }
    if (isOk(moDoc.getDPlanDate())) {
      pushU8ReqVo.setDStartDate(DateUtils.formatDate(moDoc.getDCreateTime(), "yyyy-MM-dd"));
      pushU8ReqVo.setDdate(DateUtils.formatDate(moDoc.getDCreateTime(), "yyyy-MM-dd"));
    }
    pushU8ReqVo.setIrowno("1");
    pushU8ReqVo.setcBatch("");
    rows.add(pushU8ReqVo);
    Map map = new HashMap();
    map.put("data", rows);


    //            请求头
    Map<String, String> header = new HashMap<>(5);
    header.put("Content-Type", "application/json");
    String url = "http://192.168.1.19:8090/api/cwapi/AddMoToERP?dbname=U8Context";

    try {
      String post = HttpApiUtils.httpHutoolPost(url, map, header);
      com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(post);
      if (isOk(post)) {
        if ("201".equals(jsonObject.getString("code"))) {
          System.out.println(jsonObject);
          return Ret.ok("提交成功");
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return fail("上传u8失败");
  }

  //通过当前登录人名称获取部门id
  public String getdeptid() {
    String dept = "001";
    User user = JBoltUserKit.getUser();
    Person person = personService.findFirstByUserId(user.getId());
    if (null != person && "".equals(person)) {
      dept = person.getCOrgCode();
    }
    return dept;
  }

  public List<Record> getMoJobData(Long imdcocid) {
    List<Record> records = dbTemplate("modoc.findMoJobByImodocId", new Kv().set("imdcocid", imdcocid)).find();
    return records;
  }

  public Page<Record> getInventoryList(int pageNumber, int pageSize, Kv keywords) {
    return dbTemplate("modoc.getInventoryList", keywords).paginate(pageNumber, pageSize);
  }

  public List<Record> inventoryAutocompleteDatas(String q, Integer limit) {
    return dbTemplate("modoc.inventoryAutocompleteDatas", Okv.by("q", q).set("limit", limit)).find();
  }

  public Page<Record> getPersonByEquipment(int pageNumber, int pageSize, Kv keywords) {
    if (keywords.get("configpersonids") != null) {
      String configpersonids = keywords.getStr("configpersonids");
      Page<Record> persons = dbTemplate("modoc.getPersonsByIds", Kv.by("configpersonids", configpersonids)).paginate(pageNumber, pageSize);
      return persons;
    }

    List<Record> records = dbTemplate("modoc.getEquipments", Kv.by("iEquipmentIds", keywords.getLong("configid"))).find();
    if (records.size() == 0) {
      records = dbTemplate("modoc.getMoDocEquipments", Kv.by("iEquipmentIds", keywords.getLong("configid"))).find();
    }

    StringBuilder sb = new StringBuilder();
    for (Record record : records) {
      long iEquipmentId = record.getLong("iEquipmentId");
      sb.append(iEquipmentId).append(",");
    }

    String equipmentIds = sb.toString();
    if (equipmentIds.endsWith(",")) {
      equipmentIds = equipmentIds.substring(0, equipmentIds.length() - 1);
    }
    keywords.set("iEquipmentIds", equipmentIds);
    if (equipmentIds.equals("")) {
      return null;
    }
    Page<Record> iEquipmentIds = dbTemplate("modoc.getPersonByEquipment", keywords).paginate(pageNumber, pageSize);
    return iEquipmentIds;
  }

  public Record getmoDocupdata(Long iautoid) {
    return dbTemplate("modoc.getmoDocupdata", Kv.by("iautoid", iautoid)).findFirst();
  }

  public List<Record> getMoDocbyIinventoryRoutingId(Long iMoDocId) {
    List<Record> datalists = dbTemplate("modoc.getMoDocbyIinventoryRoutingId", Kv.by("iMoDocId", iMoDocId)).find();
    return datalists;
  }

  public List<Record> getMoDocinv(Kv kv) {
    List<Record> datalists = dbTemplate("modoc.getMoDocinv", Kv.by("iautoid", kv.getLong("configid"))).find();
    return datalists;
  }

  public List<Record> getMoDocEquipment(Kv kv) {
    List<Record> datalists = dbTemplate("modoc.getMoDocEquipment", Kv.by("iautoid", kv.getLong("configid"))).find();
    return datalists;
  }

  public List<Record> moDocInventoryRouting(Kv kv) {
    List<Record> datalists = dbTemplate("modoc.moDocInventoryRouting", Kv.by("iautoid", kv.getLong("configid"))).find();
    return datalists;
  }

  public String findByisScanned(Long imodocid) {
    Record jobN = dbTemplate("momaterialsscansum.getByBarcodeF", Kv.by("imodocid", imodocid)).findFirst();
    Record jobY = dbTemplate("momaterialsscansum.getByBarcodeN", Kv.by("imodocid", imodocid)).findFirst();
    String isScanned = "";
    if (jobN.getInt("iplanqty") == jobY.getInt("irealqty") && jobN.getInt("iplanqty") != 0 &&  jobY.getInt("irealqty")!=0) {
      isScanned = "已齐料";
    } else {
      isScanned = "未齐料";
    }
    return isScanned;
  }


  /**
   * 编辑计划保存
   *
   * @param records
   * @return
   */
  public Ret savePlan(List<Record> records) {
    tx(() -> {
      records.forEach(record -> {
        MoDoc moDoc = findById(record.getStr("iautoid"));
        if (record.getStr("qty").matches("\\d+")) { // 使用正则表达式判断字符串是否由数字组成
          int num = Integer.parseInt(record.getStr("qty")); // 将字符串转换为整数
          if (num <= 0) {
            ValidationUtils.error("工单号【" + moDoc.getCMoDocNo() + "】数量必须大于0");
          }
        } else {
          ValidationUtils.error("工单号【" + moDoc.getCMoDocNo() + "】生产计划量数据错误，请输入正确的数据");
        }
        moDoc.setIQty(record.getBigDecimal("qty"));
        moDoc.setIsModified(true);
        moDoc.update();
      });
      return true;
    });
    return SUCCESS;
  }

  /**
   * 编辑人员保存
   *
   * @param records
   * @return
   */
  public Ret savePersonnel(List<Record> records) {
    tx(() -> {
      records.forEach(record -> {
        if (record.getStr("type").equals("0")) {
          Date date = DateUtil.parse(record.getStr("data"));
          //获取工单工艺配置ID
          String routingconfigid = dbTemplate("modocbatch.getMoroutingconfigId", Kv.by("imotaskid", record.getStr("taskid")).set("iyear", DateUtil.year(date)).
              set("imonth", DateUtil.month(date) + 1).set("idate", DateUtil.dayOfMonth(date)).set("iworkshiftmid", record.getStr("iworkshiftmid")).
              set("iinventoryid", record.getStr("codekehu")).set("ioperationid", record.getStr("gonxuid"))).queryStr();
          ValidationUtils.notBlank(routingconfigid, "工单号【" + record.getStr("cmodocno") + "】对应列未找到工单工艺配置数据！！！");
          //删除之前跟工单工艺配置ID相关联的工单工艺人员配置信息
          List<Record> records11 = dbTemplate("modocbatch.deleteModocPersonByConfigId", Kv.by("routingconfigid", Long.parseLong(routingconfigid))).find();
          records11.forEach(record1 -> {
            MoMoroutingconfigPerson person = moMoroutingconfigPersonService.findByid(record1.getLong("iautoid"));
            person.delete();
          });
          JSONArray records1 = JSON.parseArray(record.getStr("renyuanid"));
          for (Object o : records1) {
            MoMoroutingconfigPerson person = new MoMoroutingconfigPerson();
            person.setIMoRoutingConfigId(Long.parseLong(routingconfigid));
            person.setIPersonId(Long.parseLong(String.valueOf(o)));
            person.save();
          }
        } else if (record.getStr("type").equals("2")) {
          JSONArray records1 = JSON.parseArray(record.getStr("renyuanid"));
          ValidationUtils.isTrue(records1.size() == 1, "工单号【" + record.getStr("cmodocno") + "】的组长只能选择一个人员");
          Long iautoid = dbTemplate("modocbatch.getModocId", Kv.by("cmodocno", record.getStr("cmodocno")).
              set("taskid", record.getStr("taskid"))).queryLong();
          MoDoc moDoc = findById(iautoid);
          moDoc.setIDutyPersonId(Long.parseLong(String.valueOf(records1.get(0))));
          moDoc.update();
        } else if (record.getStr("type").equals("3") || record.getStr("type").equals("4") || record.getStr("type").equals("5")) {
          Integer itype = 0;
          String type = record.getStr("type");
          switch (type) {
            case "3":
              itype = 1;
              break;
            case "4":
              itype = 2;
              break;
            case "5":
              itype = 3;
              break;
            default:
              itype = 0;
              break;
          }
          JSONArray records1 = JSON.parseArray(record.getStr("renyuanid"));
          Date date = DateUtil.parse(record.getStr("data"));
          List<Record> records2 = dbTemplate("modocbatch.getMoworkshiftMDId", Kv.by("imotaskid", record.getStr("taskid")).
              set("iyear", DateUtil.year(date)).set("imonth", DateUtil.month(date) + 1).set("idate", DateUtil.dayOfMonth(date)).
              set("iworkshiftmid", record.getStr("iworkshiftmid"))).find();
          if (records2.size() > 0) {
            AtomicReference<String> diautoid = new AtomicReference<>("");
            Integer finalItype = itype;
            records2.forEach(record2 -> {
              if (record2.getInt("itype").equals(finalItype)) {
                diautoid.set(record2.getStr("diautoid"));
              }
            });
            MoWorkShiftD shiftD = new MoWorkShiftD();
            if (diautoid.get() != null && !diautoid.get().equals("")) {
              moWorkShiftDService.finByid(Long.parseLong(diautoid.get()));
            }
            shiftD.setIType(itype);
            shiftD.setIMoWorkShiftMid(Long.parseLong(records2.get(0).getStr("miautoid")));
            shiftD.setIPersonId(Long.parseLong(String.valueOf(records1.get(0))));
            if (diautoid.get() != null && !diautoid.get().equals("")) {
              shiftD.update();
            } else {
              shiftD.save();
            }
          } else {
            MoWorkShiftM shiftM = new MoWorkShiftM();
            shiftM.setIAutoId(JBoltSnowflakeKit.me.nextId());
            shiftM.setIMoTaskId(record.getLong("taskid"));
            shiftM.setIYear(DateUtil.year(date));
            shiftM.setIMonth(DateUtil.month(date) + 1);
            shiftM.setIDate(DateUtil.dayOfMonth(date));
            shiftM.setIWorkShiftMid(record.getLong("iworkshiftmid"));
            shiftM.save();

            MoWorkShiftD shiftD = new MoWorkShiftD();
            shiftD.setIMoWorkShiftMid(shiftM.getIAutoId());
            shiftD.setIType(itype);
            shiftD.setIPersonId(Long.parseLong(String.valueOf(records1.get(0))));
            shiftD.save();
          }
        }
      });
      return true;
    });
    return SUCCESS;
  }

  /**
   * 根据ID获取作业人员信息
   * @param iAutoId
   * @return
   */
  public String getPsnNameById(Long iAutoId) {
    ValidationUtils.notNull(iAutoId, "制造工单主键不允许为空");
    Record record = dbTemplate("modoc.getPsnNameById", Kv.by("iautoid", iAutoId)).findFirst();
    if (isOk(record)) {
      return record.getStr("cpsn_name");
    }
    return null;
  }

  /**
   *获取对应的工艺数据
   */
    public List<Record> findByModecIdProcessDatas(Long modocid) {
      List<Record> records = dbTemplate("modoc.findByModecIdProcessDatas", Kv.by("imodocid", modocid)).find();
      for (Record record : records) {
        List<Record> moDocEquipment = getMoDocEquipment(Kv.by("configid", record.getStr("routingconfigid")));
        StringBuilder cequipmentnames =new StringBuilder();
        StringBuilder cequipmentids =new StringBuilder();
        for (Record record1 : moDocEquipment) {
          cequipmentnames.append(	record1.getStr("cequipmentname")).append(",");
          cequipmentids.append(	record1.getStr("iequipmentid")).append(",");
        }
        String keywordStr="";
        String keywordStr2="";
        if (moDocEquipment.size()>0) {
          keywordStr = cequipmentnames.deleteCharAt(cequipmentnames.length() - 1).toString();
          keywordStr2 = cequipmentids.deleteCharAt(cequipmentids.length() - 1).toString();
        }
        record.set("cequipmentnames",keywordStr);
        record.set("cequipmentids",keywordStr2);
      }
      return records;
    }


  /**
   * 处理审批通过的其他业务操作，如有异常返回错误信息
   * @param formAutoId 单据ID
   * @return 错误信息
   */
  @Override
  public String postApproveFunc(long formAutoId, boolean isWithinBatch) {

    Record record=dbTemplate("modoc.getModocByid",Kv.by("iautoid",formAutoId)).findFirst();
    MoDoc moDoc=findById(formAutoId);
    moDoc.setIStatus(2);
    if(record.getInt("iauditway")==2 && record.getInt("iauditstatus")==2){
        JSONArray jsonArray=new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("docno", record.getStr("cmodocno"));
        jsonObject.put("ddate", DateUtil.format(record.getDate("dcreatetime"), "yyyy-MM-dd"));
        jsonObject.put("cwhcode", record.getStr("cwhcode"));
        jsonObject.put("cdepcode", record.get("cdepcode"));
        jsonObject.put("cinvcode", record.get("cinvcode"));
        jsonObject.put("cinvname", record.get("cinvname"));
        jsonObject.put("cinvstd", record.get("cInvStd"));
        jsonObject.put("iquantity", record.get("iquantity"));
        jsonObject.put("DStartDate",record.getStr("DStartDate"));
        jsonObject.put("DDueDate",record.getStr("DDueDate"));
        jsonObject.put("irowno",record.getStr("irowno"));
        jsonObject.put("cBatch",record.getStr("cbatch"));
        jsonArray.add(jsonObject);
        JSONObject params = new JSONObject();
        params.put("data",jsonArray);
        LOG.info(params.toJSONString());
      tx(() -> {
        moDoc.update();
        String result = HttpUtil.post("http://120.24.44.82:8099/api/cwapi/AddMoToERP?dbname=U8Context", params.toString());
        JSONObject jsonObject2 = JSONObject.parseObject(result);
        String remark=jsonObject2.getString("remark");
        if(!jsonObject2.getString("status").equals("S")){
          ValidationUtils.error(remark);
        }
        return true;
      });
    }
    return null;
  }

  /**
   * 处理审批不通过的其他业务操作，如有异常处理返回错误信息
   */
  @Override
  public String postRejectFunc(long formAutoId, boolean isWithinBatch) {
    return null;
  }

  /**
   * 实现反审之前的其他业务操作，如有异常返回错误信息
   * @param formAutoId 单据ID
   * @param isFirst    是否为审批的第一个节点
   * @param isLast     是否为审批的最后一个节点
   */
  @Override
  public String preReverseApproveFunc(long formAutoId, boolean isFirst, boolean isLast) {
    return null;
  }

  /**
   * 实现反审之后的其他业务操作, 如有异常返回错误信息
   *
   * @param formAutoId 单据ID
   * @param isFirst    是否为审批的第一个节点
   * @param isLast     是否为审批的最后一个节点
   */
  @Override
  public String postReverseApproveFunc(long formAutoId, boolean isFirst, boolean isLast) {
    return null;
  }

  /**
   * 提审前业务，如有异常返回错误信息
   */
  @Override
  public String preSubmitFunc(long formAutoId) {

    return null;
  }

  /**
   * 提审后业务处理，如有异常返回错误信息
   */
  @Override
  public String postSubmitFunc(long formAutoId) {

    return null;
  }

  /**
   * 撤回审核业务处理，如有异常返回错误信息
   */
  @Override
  public String postWithdrawFunc(long formAutoId) {
    return null;
  }

  /**
   * 从审批中，撤回到已保存，业务实现，如有异常返回错误信息
   */
  @Override
  public String withdrawFromAuditting(long formAutoId) {
   return null;
  }

  /**
   * 从已审核，撤回到已保存，前置业务实现，如有异常返回错误信息
   */
  @Override
  public String preWithdrawFromAuditted(long formAutoId) {
    return null;
  }

  /**
   * 从已审核，撤回到已保存，业务实现，如有异常返回错误信息
   */
  @Override
  public String postWithdrawFromAuditted(long formAutoId) {
   return null;
  }

  /**
   * 批量审批（审核）通过
   * @param formAutoIds 单据IDs
   * @return  错误信息
   */
  @Override
  public String postBatchApprove(List<Long> formAutoIds) {

    return null;
  }

  /**
   * 批量审批（审核）不通过
   * @param formAutoIds 单据IDs
   * @return  错误信息
   */
  @Override
  public String postBatchReject(List<Long> formAutoIds) {
    return null;
  }

  /**
   * 批量撤销审批
   * @param formAutoIds 单据IDs
   * @return  错误信息
   */
  @Override
  public String postBatchBackout(List<Long> formAutoIds) {

    return null;
  }

  public Ret subUpdata(JBoltTable jBoltTable){
    MoDoc moDoc = jBoltTable.getFormModel(MoDoc.class, "moDoc");
    Map<String,Object> map=new HashMap<>();
    if (moDoc == null ) {
      return fail(JBoltMsg.PARAM_ERROR);
    }
    //存工艺路线工序,人员,设备,物料集
    List<MoDocFormVo> saveModelList1 = jBoltTable.getSaveModelList(MoDocFormVo.class);
    //人员
    for (MoDocFormVo record : saveModelList1) {
      String configperson = record.getConfigpersonids();
      List<Record> records = dbTemplate("modoc.getConfigPersonById",Kv.by("id",record.getIautoid())).find();
      if(records.size()==0 && configperson != null){
          String[] arr = configperson.split(",");
          for (int i = 0; i < arr.length; i++) {
            MoMoroutingconfigPerson mp =new MoMoroutingconfigPerson();
            mp.setIMoRoutingConfigId(Long.parseLong(record.getIautoid()));
            mp.setIPersonId(Long.parseLong(arr[i]));
            moMoroutingconfigPersonService.save(mp);
          }
      }
      if(records.size()!=0 && configperson != null){
         String[] arr2 = configperson.split(",");
          for (Record record1 : records) {
            moMoroutingconfigPersonService.deleteById(record1.getLong("iautoid"));
          }
          for (int i = 0; i < arr2.length; i++) {
            MoMoroutingconfigPerson mp =new MoMoroutingconfigPerson();
            mp.setIMoRoutingConfigId(Long.parseLong(record.getIautoid()));
            mp.setIPersonId(Long.parseLong(arr2[i]));
            moMoroutingconfigPersonService.save(mp);
          }
      }
    }
    return successWithData(moDoc.getIAutoId());
  }
}
