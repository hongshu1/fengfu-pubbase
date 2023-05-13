package cn.rjtech.admin.modoc;

import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.rjtech.admin.inventoryrouting.InventoryRoutingService;
import cn.rjtech.admin.inventoryroutingconfig.InventoryRoutingConfigService;
import cn.rjtech.admin.inventoryroutingequipment.InventoryRoutingEquipmentService;
import cn.rjtech.admin.inventoryroutinginvc.InventoryRoutingInvcService;
import cn.rjtech.admin.morouting.MoMoroutingService;
import cn.rjtech.admin.moroutingcinve.MoMoroutinginvcService;
import cn.rjtech.admin.moroutingconfig.MoMoroutingconfigService;
import cn.rjtech.admin.moroutingconfigequipment.MoMoroutingequipmentService;
import cn.rjtech.admin.moroutingconfigperson.MoMoroutingconfigPersonService;
import cn.rjtech.entity.vo.instockqcformm.MoDocFormVo;
import cn.rjtech.model.momdata.*;
import cn.rjtech.model.momdata.MoMorouting;
import cn.rjtech.model.momdata.MoMoroutinginvc;
import cn.rjtech.model.momdata.MoMoroutingconfig;
import cn.rjtech.model.momdata.MoMoroutingequipment;
import cn.rjtech.model.momdata.MoMoroutingconfigPerson;
import com.jfinal.aop.Inject;
import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import cn.jbolt.core.base.JBoltMsg;
import com.jfinal.plugin.activerecord.Record;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 工单管理 Service
 *
 * @ClassName: MoDocService
 * @author: RJ
 * @date: 2023-04-26 16:15
 */
public class MoDocService extends BaseService<MoDoc> {
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
  private MoMoroutingconfigPersonService moMoroutingconfigPersonService;
  @Inject
  private MoMoroutingequipmentService moroutingequipmentService;
  @Inject
  private MoMoroutinginvcService moMoroutinginvcService;


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
   * @param id
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
   *
   * @param
   * @return
   */
  public Ret save(JBoltTable jBoltTable) {
    MoDoc moDoc = jBoltTable.getFormModel(MoDoc.class, "moDoc");
    if (moDoc == null || isOk(moDoc.getIAutoId())) {
      return fail(JBoltMsg.PARAM_ERROR);
    }
    //初始化状态
    moDoc.setIStatus(1);
    moDoc.setIType(2);
    Date date = moDoc.getDPlanDate();
    moDoc.setIYear(date.getYear());
    moDoc.setIMonth(date.getMonth());
    moDoc.setIDate(date.getDay());
    moDoc.setIPersonNum(0);
    InventoryRouting byId = inventoryRoutingService.findById(moDoc.getIInventoryRouting());
    moDoc.setCVersion(byId.getCVersion());
    moDoc.setIMoTaskId(1001L);//手工新增没有任务,先占着
    moDoc.setCRoutingName(byId.getCRoutingName());
    Date dCreateTime = new Date();
    String userName = JBoltUserKit.getUserName();
    Long userId = JBoltUserKit.getUserId();
    tx(() -> {
      moDoc.save();
      // 工艺路线
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
        moMoroutingconfig.setISecs(record.getIseq());
        moMoroutingconfig.setCMergedSeq(record.getCmergedseq());
        moMoroutingconfig.setCCreateName(record.getCcreatename());
        moMoroutingconfig.setCOperationName(record.getCoperationname());
        moMoroutingconfig.setIMergedNum(record.getImergednum());
        moMoroutingconfig.setIMergeRate(new BigDecimal(record.getImergerate()));
        moMoroutingconfig.setIMergeSecs(record.getImergesecs());
        moMoroutingconfig.setIRsInventoryId(Long.parseLong(record.getIrsinventoryid()));
        moMoroutingconfig.setIType(record.getItype());
        moMoroutingconfig.setICreateBy(userId);
        moMoroutingconfig.setDCreateTime(dCreateTime);
        moMoroutingconfigService.save(moMoroutingconfig);
        //人员
        String configperson = record.getConfigperson();
        if (configperson != null) {
          String[] arr = configperson.split(",");
          for (int i = 0; i < arr.length; i++) {
            System.out.println(arr[i]);
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
    return ret(true);
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
}
