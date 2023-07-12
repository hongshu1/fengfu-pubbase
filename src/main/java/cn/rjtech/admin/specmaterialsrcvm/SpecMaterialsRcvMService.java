package cn.rjtech.admin.specmaterialsrcvm;

import cn.hutool.core.date.DateTime;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltModelKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.department.DepartmentService;
import cn.rjtech.admin.specmaterialsrcvd.SpecMaterialsRcvDService;
import cn.rjtech.enums.AuditStatusEnum;
import cn.rjtech.model.momdata.Department;
import cn.rjtech.model.momdata.SpecMaterialsRcvD;
import cn.rjtech.model.momdata.SpecMaterialsRcvM;
import cn.rjtech.service.approval.IApprovalService;
import cn.rjtech.util.BillNoUtils;
import cn.rjtech.util.ValidationUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.Date;
import java.util.List;

/**
 * 制造工单-特殊领料单主表 Service
 *
 * @ClassName: SpecMaterialsRcvMService
 * @author: RJ
 * @date: 2023-04-24 10:24
 */
public class SpecMaterialsRcvMService extends BaseService<SpecMaterialsRcvM> implements IApprovalService {

  private final SpecMaterialsRcvM dao = new SpecMaterialsRcvM().dao();

  @Override
  protected SpecMaterialsRcvM dao() {
    return dao;
  }

  @Inject
  private SpecMaterialsRcvDService specMaterialsRcvDService;

  @Inject
  private DepartmentService departmentService;

  /**
   * 后台管理分页查询
   *
   * @param pageNumber
   * @param pageSize
   * @param keywords
   * @return
   */
  public Page<SpecMaterialsRcvM> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
    return paginateByKeywords("iAutoId", "DESC", pageNumber, pageSize, keywords, "iAutoId");
  }

  /**
   * 保存
   *
   * @param specMaterialsRcvM
   * @return
   */
  public Ret save(SpecMaterialsRcvM specMaterialsRcvM) {
    if (specMaterialsRcvM == null || isOk(specMaterialsRcvM.getIAutoId())) {
      return fail(JBoltMsg.PARAM_ERROR);
    }
    //if(existsName(specMaterialsRcvM.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
    boolean success = specMaterialsRcvM.save();
    if (success) {
      //添加日志
      //addSaveSystemLog(specMaterialsRcvM.getIautoid(), JBoltUserKit.getUserId(), specMaterialsRcvM.getName());
    }
    return ret(success);
  }

  /**
   * 更新
   *
   * @param specMaterialsRcvM
   * @return
   */
  public Ret update(SpecMaterialsRcvM specMaterialsRcvM) {
    if (specMaterialsRcvM == null || notOk(specMaterialsRcvM.getIAutoId())) {
      return fail(JBoltMsg.PARAM_ERROR);
    }
    //更新时需要判断数据存在
    SpecMaterialsRcvM dbSpecMaterialsRcvM = findById(specMaterialsRcvM.getIAutoId());
    if (dbSpecMaterialsRcvM == null) {
      return fail(JBoltMsg.DATA_NOT_EXIST);
    }
    //if(existsName(specMaterialsRcvM.getName(), specMaterialsRcvM.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
    boolean success = specMaterialsRcvM.update();
    if (success) {
      //添加日志
      //addUpdateSystemLog(specMaterialsRcvM.getIautoid(), JBoltUserKit.getUserId(), specMaterialsRcvM.getName());
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
   * @param specMaterialsRcvM 要删除的model
   * @param kv                携带额外参数一般用不上
   * @return
   */
  @Override
  protected String afterDelete(SpecMaterialsRcvM specMaterialsRcvM, Kv kv) {
    //addDeleteSystemLog(specMaterialsRcvM.getIautoid(), JBoltUserKit.getUserId(),specMaterialsRcvM.getName());
    return null;
  }

  /**
   * 检测是否可以删除
   *
   * @param specMaterialsRcvM 要删除的model
   * @param kv                携带额外参数一般用不上
   * @return
   */
  @Override
  public String checkCanDelete(SpecMaterialsRcvM specMaterialsRcvM, Kv kv) {
    //如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
    return checkInUse(specMaterialsRcvM, kv);
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
   * @param specMaterialsRcvM 要toggle的model
   * @param column            操作的哪一列
   * @param kv                携带额外参数一般用不上
   * @return
   */
  @Override
  public String checkCanToggle(SpecMaterialsRcvM specMaterialsRcvM, String column, Kv kv) {
    //没有问题就返回null  有问题就返回提示string 字符串
    return null;
  }

  /**
   * toggle操作执行后的回调处理
   */
  @Override
  protected String afterToggleBoolean(SpecMaterialsRcvM specMaterialsRcvM, String column, Kv kv) {
    //addUpdateSystemLog(specMaterialsRcvM.getIautoid(), JBoltUserKit.getUserId(), specMaterialsRcvM.getName(),"的字段["+column+"]值:"+specMaterialsRcvM.get(column));
    return null;
  }

  /**
   * 检测是否可以删除
   *
   * @param specMaterialsRcvM model
   * @param kv                携带额外参数一般用不上
   * @return
   */
  @Override
  public String checkInUse(SpecMaterialsRcvM specMaterialsRcvM, Kv kv) {
    //这里用来覆盖 检测SpecMaterialsRcvM是否被其它表引用
    return null;
  }

  /**
   * 根据制造工单id查询特殊领料数据源Api
   *
   * @param imodocid   制造工单id
   * @param pageNumber 页数
   * @param pageSize   页面数量
   * @return
   */
  public Page<Record> getApiSpecmaterialsrcvmDatas(Long imodocid, Integer pageNumber, Integer pageSize) {
    return dbTemplate("specmaterialsrcvm.getApiSpecmaterialsrcvmDatas", Kv.by("imodocid", imodocid)).paginate(pageNumber, pageSize);
  }


  /**
   * 根据制造工单id查询特殊领料数据源Api
   *
   * @param imodocid   制造工单id
   * @param pageNumber 页数
   * @param pageSize   页面数量
   * @param cinvcode   存货编码
   * @param cinvcode1  客户部番
   * @param cinvname1  部品名称
   * @return
   */
  public Page<Record> getInventoryDatasByDocid(Long imodocid, Integer pageNumber, Integer pageSize, String cinvcode, String cinvcode1, String cinvname1) {
    return dbTemplate("specmaterialsrcvm.getInventoryDatasByDocid", Kv.by("imodocid", imodocid).set("cinvcode", cinvcode).
        set("cinvcode1", cinvcode1).set("cinvname1", cinvname1)).paginate(pageNumber, pageSize);
  }

  public Ret saveSpecMaterialsRcv(String rcvm, String rcvd, String type, Long id) {
    JSONObject jsonObject1 = JSONObject.parseObject(rcvm);
    List<Record> records2 = JBoltModelKit.getFromRecords(JSONArray.parseArray(rcvd));
    tx(() -> {
      SpecMaterialsRcvM specMaterialsRcvM;
      if (type.equals("2")) {
        specMaterialsRcvM = findById(id);
        List<Record> records1 = dbTemplate("specmaterialsrcvm.getRcvdIdByRevmId", Kv.by("id", id)).find();
        records1.forEach(record -> {
          SpecMaterialsRcvD specMaterialsRcvD = specMaterialsRcvDService.findById(record.getLong("iautoid"));
          specMaterialsRcvD.delete();
        });
      } else {
        specMaterialsRcvM = new SpecMaterialsRcvM();
        specMaterialsRcvM.setIOrgId(jsonObject1.getLong("orgid"));
        specMaterialsRcvM.setCOrgCode(jsonObject1.getString("orgcode"));
        specMaterialsRcvM.setCOrgName(jsonObject1.getString("orgname"));

        specMaterialsRcvM.setIMoDocId(jsonObject1.getLong("imodocid"));
//      Record department = dbTemplate("specmaterialsrcvm.getDepartmentIdByPersonId", Kv.by("id", jsonObject1.getLong("icreateby"))).findFirst();
//      ValidationUtils.notNull(department, "未找到当前登录人员【" + jsonObject1.getString("ccreatename") + "】的部门信息");
        specMaterialsRcvM.setIDepartmentId(jsonObject1.getLong("imodocid"));
        specMaterialsRcvM.setCSpecRcvDocNo(BillNoUtils.getcDocNo(jsonObject1.getLong("orgid"), "TSLL", 6));
        specMaterialsRcvM.setDDemandDate(new DateTime());
        specMaterialsRcvM.setCReason(jsonObject1.getString("creason"));
        specMaterialsRcvM.setCSpecAdviceSns(jsonObject1.getString("cspecadvicesns"));
        specMaterialsRcvM.setIStatus(jsonObject1.getInteger("istatus"));
        specMaterialsRcvM.setIAuditWay(2);
        specMaterialsRcvM.setDSubmitTime(new DateTime());
        specMaterialsRcvM.setIAuditStatus(jsonObject1.getInteger("istatus") == 1 ? 0 : 1);
        specMaterialsRcvM.setDAuditTime(new DateTime());
        specMaterialsRcvM.setICreateBy(jsonObject1.getLong("icreateby"));
        specMaterialsRcvM.setCCreateName(jsonObject1.getString("ccreatename"));
        specMaterialsRcvM.setDCreateTime(new DateTime());
        specMaterialsRcvM.setIUpdateBy(jsonObject1.getLong("icreateby"));
        specMaterialsRcvM.setCUpdateName(jsonObject1.getString("ccreatename"));
        specMaterialsRcvM.setDUpdateTime(new DateTime());
        specMaterialsRcvM.setIsDeleted(false);
        specMaterialsRcvM.save();
      }

      records2.forEach(record -> {
        SpecMaterialsRcvD specMaterialsRcvD = new SpecMaterialsRcvD();
        specMaterialsRcvD.setISpecRcvMid(specMaterialsRcvM.getIAutoId());
        specMaterialsRcvD.setIInventoryId(record.getLong("iautoid"));
        specMaterialsRcvD.setIQty(record.getBigDecimal("iqty"));
        specMaterialsRcvD.save();
      });
      return true;
    });
    return SUCCESS;
  }

  public Ret deleteSpecMaterialsRcv(Long id) {
    tx(() -> {
      SpecMaterialsRcvM specMaterialsRcvm = findById(id);
      specMaterialsRcvm.setIsDeleted(true);
      specMaterialsRcvm.update();
      return true;
    });
    return SUCCESS;
  }

  /**
   * 根据特殊领料ID查询主细表数据
   *
   * @param id 主表ID
   * @return
   */
  public Record getSpecMaterialsRcvDatas(Long id) {
    SpecMaterialsRcvM specMaterialsRcvm = findById(id);
    Department department = departmentService.findByid(specMaterialsRcvm.getIDepartmentId());
    Record record = new Record();
    record.put("cspecrcvdocno", specMaterialsRcvm.getCSpecRcvDocNo());
    record.put("ddemanddate", specMaterialsRcvm.getDDemandDate());
    if (department != null) {
      record.put("cdepname", department.getCDepName());
    } else {
      record.put("cdepname", " ");
    }
    record.put("creason", specMaterialsRcvm.getCReason());
    record.put("cspecadvicesns", specMaterialsRcvm.getCSpecAdviceSns());

    record.put("ccreatename", specMaterialsRcvm.getCCreateName());
    record.put("dcreatetime", specMaterialsRcvm.getDCreateTime());
    record.put("rcvd", dbTemplate("specmaterialsrcvm.getRcvdDatasByRevmId", Kv.by("id", id)).find());
    return record;
  }

  public Ret revocationSpecMaterialsRcvMById(Long id) {
    update("UPDATE Mo_SpecMaterialsRcvM SET iAuditStatus = 0 WHERE iAutoId = ?", id);
    return SUCCESS;
  }


  @Override
  public String postApproveFunc(long formAutoId, boolean isWithinBatch) {
    SpecMaterialsRcvM specMaterialsRcvm = findById(formAutoId);
    // 审核状态修改
    specMaterialsRcvm.setIAuditStatus(AuditStatusEnum.APPROVED.getValue());
    specMaterialsRcvm.setIUpdateBy(JBoltUserKit.getUserId());
    specMaterialsRcvm.setCUpdateName(JBoltUserKit.getUserName());
    specMaterialsRcvm.setDUpdateTime(new Date());
//    specMaterialsRcvm.setIAuditBy(JBoltUserKit.getUserId());
//    specMaterialsRcvm.setCAuditName(JBoltUserKit.getUserName());
    specMaterialsRcvm.setDSubmitTime(new Date());
    specMaterialsRcvm.update();
    return null;
  }

  @Override
  public String postRejectFunc(long formAutoId, boolean isWithinBatch) {
    SpecMaterialsRcvM specMaterialsRcvm = findById(formAutoId);
    // 审核状态修改
    specMaterialsRcvm.setIAuditStatus(AuditStatusEnum.REJECTED.getValue());
    specMaterialsRcvm.setIUpdateBy(JBoltUserKit.getUserId());
    specMaterialsRcvm.setCUpdateName(JBoltUserKit.getUserName());
    specMaterialsRcvm.setDUpdateTime(new Date());
//    specMaterialsRcvm.setIAuditBy(JBoltUserKit.getUserId());
//    specMaterialsRcvm.setCAuditName(JBoltUserKit.getUserName());
    specMaterialsRcvm.setDSubmitTime(new Date());
    specMaterialsRcvm.update();
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
    SpecMaterialsRcvM specMaterialsRcvm = findById(formAutoId);
    switch (AuditStatusEnum.toEnum(specMaterialsRcvm.getIAuditStatus())) {
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
      SpecMaterialsRcvM specMaterialsRcvM = findById(formAutoId);
      // 审核状态修改
      specMaterialsRcvM.setIAuditStatus(AuditStatusEnum.APPROVED.getValue());
      specMaterialsRcvM.setIUpdateBy(JBoltUserKit.getUserId());
      specMaterialsRcvM.setCUpdateName(JBoltUserKit.getUserName());
      specMaterialsRcvM.setDUpdateTime(new Date());
//      formUploadM.setIAuditBy(JBoltUserKit.getUserId());
//      formUploadM.setCAuditName(JBoltUserKit.getUserName());
      specMaterialsRcvM.setDSubmitTime(new Date());
      specMaterialsRcvM.update();
    }
    return null;
  }

  @Override
  public String postBatchReject(List<Long> formAutoIds) {
    for (Long formAutoId : formAutoIds) {
      SpecMaterialsRcvM specMaterialsRcvM = findById(formAutoId);
      // 审核状态修改
      specMaterialsRcvM.setIAuditStatus(AuditStatusEnum.REJECTED.getValue());
      specMaterialsRcvM.setIUpdateBy(JBoltUserKit.getUserId());
      specMaterialsRcvM.setCUpdateName(JBoltUserKit.getUserName());
      specMaterialsRcvM.setDUpdateTime(new Date());
//      specMaterialsRcvM.setIAuditBy(JBoltUserKit.getUserId());
//      specMaterialsRcvM.setCAuditName(JBoltUserKit.getUserName());
      specMaterialsRcvM.setDSubmitTime(new Date());
      specMaterialsRcvM.update();
    }
    return null;
  }

  @Override
  public String postBatchBackout(List<Long> formAutoIds) {
    return null;
  }
}