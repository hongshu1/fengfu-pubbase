package cn.rjtech.admin.subcontractorderdbatch;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.subcontractorderdbatchversion.SubcontractOrderDBatchVersionService;
import cn.rjtech.admin.subcontractorderdqty.SubcontractorderdQtyService;
import cn.rjtech.model.momdata.SubcontractOrderDBatch;
import cn.rjtech.model.momdata.SubcontractOrderDBatchVersion;
import cn.rjtech.model.momdata.SubcontractorderdQty;
import cn.rjtech.service.func.mom.MomDataFuncService;
import cn.rjtech.util.ValidationUtils;
import com.alibaba.fastjson.JSON;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 采购/委外管理-采购现品票
 *
 * @ClassName: SubcontractOrderDBatchService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-25 21:32
 */
public class SubcontractOrderDBatchService extends BaseService<SubcontractOrderDBatch> {
  private final SubcontractOrderDBatch dao = new SubcontractOrderDBatch().dao();

  @Inject
  private MomDataFuncService momDataFuncService;
  @Inject
  private SubcontractOrderDBatchVersionService subcontractOrderDBatchVersionService;
  @Inject
  private SubcontractorderdQtyService subcontractorderdQtyService;

  @Override
  protected SubcontractOrderDBatch dao() {
    return dao;
  }

  @Override
  protected int systemLogTargetType() {
    return ProjectSystemLogTargetType.NONE.getValue();
  }

  /**
   * 后台管理数据查询
   *
   * @param pageNumber  第几页
   * @param pageSize    每页几条数据
   * @param isEffective 是否生效：0. 否  1. 是
   * @return
   */
  public Page<SubcontractOrderDBatch> getAdminDatas(int pageNumber, int pageSize, Boolean isEffective) {
    //创建sql对象
    Sql sql = selectSql().page(pageNumber, pageSize);
    //sql条件处理
    sql.eqBooleanToChar("isEffective", isEffective);
    //排序
    sql.desc("iAutoId");
    return paginate(sql);
  }

  /**
   * 保存
   *
   * @param subcontractOrderDBatch
   * @return
   */
  public Ret save(SubcontractOrderDBatch subcontractOrderDBatch) {
    if (subcontractOrderDBatch == null || isOk(subcontractOrderDBatch.getIAutoId())) {
      return fail(JBoltMsg.PARAM_ERROR);
    }
    //if(existsName(subcontractOrderDBatch.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
    boolean success = subcontractOrderDBatch.save();
    if (success) {
      //添加日志
      //addSaveSystemLog(subcontractOrderDBatch.getIAutoId(), JBoltUserKit.getUserId(), subcontractOrderDBatch.getName());
    }
    return ret(success);
  }

  /**
   * 更新
   *
   * @param subcontractOrderDBatch
   * @return
   */
  public Ret update(SubcontractOrderDBatch subcontractOrderDBatch) {
    if (subcontractOrderDBatch == null || notOk(subcontractOrderDBatch.getIAutoId())) {
      return fail(JBoltMsg.PARAM_ERROR);
    }
    //更新时需要判断数据存在
    SubcontractOrderDBatch dbSubcontractOrderDBatch = findById(subcontractOrderDBatch.getIAutoId());
    if (dbSubcontractOrderDBatch == null) {
      return fail(JBoltMsg.DATA_NOT_EXIST);
    }
    //if(existsName(subcontractOrderDBatch.getName(), subcontractOrderDBatch.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
    boolean success = subcontractOrderDBatch.update();
    if (success) {
      //添加日志
      //addUpdateSystemLog(subcontractOrderDBatch.getIAutoId(), JBoltUserKit.getUserId(), subcontractOrderDBatch.getName());
    }
    return ret(success);
  }

  /**
   * 删除数据后执行的回调
   *
   * @param subcontractOrderDBatch 要删除的model
   * @param kv                     携带额外参数一般用不上
   * @return
   */
  @Override
  protected String afterDelete(SubcontractOrderDBatch subcontractOrderDBatch, Kv kv) {
    //addDeleteSystemLog(subcontractOrderDBatch.getIAutoId(), JBoltUserKit.getUserId(),subcontractOrderDBatch.getName());
    return null;
  }

  /**
   * 检测是否可以删除
   *
   * @param subcontractOrderDBatch model
   * @param kv                     携带额外参数一般用不上
   * @return
   */
  @Override
  public String checkInUse(SubcontractOrderDBatch subcontractOrderDBatch, Kv kv) {
    //这里用来覆盖 检测是否被其它表引用
    return null;
  }

  /**
   * toggle操作执行后的回调处理
   */
  @Override
  protected String afterToggleBoolean(SubcontractOrderDBatch subcontractOrderDBatch, String column, Kv kv) {
    //addUpdateSystemLog(subcontractOrderDBatch.getIAutoId(), JBoltUserKit.getUserId(), subcontractOrderDBatch.getName(),"的字段["+column+"]值:"+subcontractOrderDBatch.get(column));
    /**
     switch(column){
     case "isEffective":
     break;
     }
     */
    return null;
  }

  public SubcontractOrderDBatch createSubcontractOrderDBatch(Long iSubcontractOrderDid, Long inventoryId, Date planDate, BigDecimal qty, String barcode) {
    SubcontractOrderDBatch subcontractOrderDBatch = new SubcontractOrderDBatch();
    subcontractOrderDBatch.setISubcontractOrderDid(iSubcontractOrderDid);
    subcontractOrderDBatch.setIinventoryId(inventoryId);
    subcontractOrderDBatch.setDPlanDate(planDate);
    subcontractOrderDBatch.setIQty(qty);
    subcontractOrderDBatch.setCVersion("00");
    subcontractOrderDBatch.setCBarcode(barcode);
    subcontractOrderDBatch.setIsEffective(true);
    return subcontractOrderDBatch;
  }

  public String generateBarCode() {
    String prefix = "XCP";
    String format = DateUtil.format(DateUtil.date(), DatePattern.PURE_DATE_FORMAT);
    return momDataFuncService.getNextNo(prefix.concat(format), 4);
  }

  public Page<Record> findBySubcontractOrderMId(int pageNumber, int pageSize, Kv kv) {
    // 为true 说明是看所有的
    if (Boolean.parseBoolean(kv.getStr("isEffective"))) {
      kv.remove("isEffective");
    } else {
      kv.set("isEffective", "1");
    }
    return dbTemplate("subcontractorderdbatch.findBySubcontractOrderMId", kv).paginate(pageNumber, pageSize);
  }

  /**
   * 导出PDF数据源
   *
   * @param kv
   * @return
   */
  public Kv orderDBatchExportDatas(Kv kv) {
    // 为true 说明是看所有的
    if (Boolean.parseBoolean(kv.getStr("isEffective"))) {
      kv.remove("isEffective");
    } else {
      kv.set("isEffective", "1");
    }
    List<Record> rowDatas = dbTemplate("subcontractorderdbatch.findBySubcontractOrderMId", kv).find();
    Record record = new Record();
    record.set("SequenceNumber", "序号");
    record.set("cBarcode", "现品票");
    record.set("cInvCode1", "客户部番");
    record.set("dPlanDate", "计划到货日期");
    record.set("iQty", "数量");
    record.set("cVersion", "版本");
    List<String> sheetNames = new ArrayList<>();
    // 改用List<List<Record>>来表示多个数据集合
    List<Kv> rows = new ArrayList<>();

    List<Record> leftDatas = new ArrayList<>();
    List<Record> rightDatas = new ArrayList<>();
    leftDatas.add(record);
    rightDatas.add(record);
    int counter = 0;
    int i = 0;

    for (Record row : rowDatas) {
      row.set("dPlanDate", row.getStr("dPlanDate") + "");
      if (counter < 15) {
        leftDatas.add(row);
      } else {
        rightDatas.add(row);
      }

      counter++;

      if (counter == 30) {
        String sheetName = "订货清单" + (i + 1);
        sheetNames.add(sheetName);
        rows.add(Kv.by("sheetName", sheetName).set("leftDatas", leftDatas).set("rightDatas", rightDatas));

        leftDatas = new ArrayList<>();
        rightDatas = new ArrayList<>();
        leftDatas.add(record);
        rightDatas.add(record);
        counter = 0;
        i++;
      }

    }

    // 如果 rows 的数量不是 30 的整数倍，将剩余的数据添加到 datas 中
    Kv remainData = Kv.create();

    if (CollUtil.isNotEmpty(leftDatas)) {
      remainData.set("leftDatas", leftDatas);
    }
    if (CollUtil.isNotEmpty(rightDatas)) {
      remainData.set("rightDatas", rightDatas);
    }

    if (MapUtil.isNotEmpty(remainData)) {
      rows.add(remainData);
    }
    return Kv.by("rows", rows).set("sheetNames", sheetNames);
  }

  public Ret updateOrderBatch(Long subcontractOrderMId, Long id, String cVersion, BigDecimal qty) {
    SubcontractOrderDBatch orderDBatch = findById(id);
    ValidationUtils.notNull(orderDBatch, JBoltMsg.DATA_NOT_EXIST);
    ValidationUtils.notBlank(cVersion, "版本号未获取到");
    ValidationUtils.isTrue(qty != null && qty.compareTo(BigDecimal.ZERO) > 0, "版本号未获取到");
    ValidationUtils.isTrue(!cVersion.equals(orderDBatch.getCVersion()), "版本号不能一致");
    ValidationUtils.isTrue(qty.compareTo(orderDBatch.getIQty()) <= 0, "现品票的数量不可大于包装数量，只可小于包装数量");
    // 新增一个现成票后，再生产一个版本记录表，及修改详情；
    String barCode = generateBarCode();
    SubcontractOrderDBatch newBatch = createSubcontractOrderDBatch(orderDBatch.getISubcontractOrderDid(), orderDBatch.getIinventoryId(), orderDBatch.getDPlanDate(), qty, barCode);
    // 设置新版本号
    newBatch.setCVersion(cVersion);
    // 添加来源id
    newBatch.setCSourceld(String.valueOf(id));
    // 将旧的改为失效
    orderDBatch.setIsEffective(false);


    // 将计划类型拆分成 年月日
    String yearStr = DateUtil.format(orderDBatch.getDPlanDate(), "yyyy");
    String monthStr = DateUtil.format(orderDBatch.getDPlanDate(), "MM");
    String dateStr = DateUtil.format(orderDBatch.getDPlanDate(), "dd");
    List<SubcontractorderdQty> subcontractorderdQtyList = subcontractorderdQtyService.findBySubcontractOrderDId(orderDBatch.getISubcontractOrderDid());

    tx(() -> {
      // 新增
      newBatch.save();
      // 保存记录
      SubcontractOrderDBatchVersion batchVersion = subcontractOrderDBatchVersionService.createBatchVersion(subcontractOrderMId, id, orderDBatch.getIinventoryId(),
          orderDBatch.getDPlanDate(), cVersion, orderDBatch.getCVersion(), barCode, orderDBatch.getCBarcode(), qty, orderDBatch.getIQty());
      batchVersion.save();
      // 修改
      orderDBatch.update();

      for (SubcontractorderdQty subcontractorderdQty : subcontractorderdQtyList) {
        Integer year = subcontractorderdQty.getIYear();
        Integer month = subcontractorderdQty.getIMonth();
        Integer date = subcontractorderdQty.getIDate();
        if (Integer.valueOf(yearStr).equals(year) && Integer.valueOf(monthStr).equals(month) && Integer.valueOf(dateStr).equals(date)) {
          // 总数量 - 更改的数量
//					BigDecimal num = subcontractorderdQty.getIQty().subtract(orderDBatch.getIQty().subtract(qty));
          subcontractorderdQty.setIQty(qty);
          subcontractorderdQty.update();
        }
      }
      return true;
    });

    return SUCCESS;
  }

}
