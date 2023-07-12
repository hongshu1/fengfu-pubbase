package cn.rjtech.admin.subcontractorderdbatch;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
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
import com.google.zxing.BarcodeFormat;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
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
    private SubcontractorderdQtyService subcontractorderdQtyService;
    @Inject
    private SubcontractOrderDBatchVersionService subcontractOrderDBatchVersionService;

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
    /*
     switch(column){
     case "isEffective":
     break;
     }
     */
    return null;
  }

  public SubcontractOrderDBatch createSubcontractOrderDBatch(Long iSubcontractOrderDid, Long iSubcontractOrderdQtyId, Long inventoryId, Date planDate, BigDecimal qty, String barcode) {
    SubcontractOrderDBatch subcontractOrderDBatch = new SubcontractOrderDBatch();
    subcontractOrderDBatch.setISubcontractOrderDid(iSubcontractOrderDid);
    subcontractOrderDBatch.setISubcontractOrderdQtyId(iSubcontractOrderdQtyId);
    subcontractOrderDBatch.setIinventoryId(inventoryId);
    subcontractOrderDBatch.setDPlanDate(planDate);
    subcontractOrderDBatch.setIQty(qty);
    subcontractOrderDBatch.setCVersion("00");
    subcontractOrderDBatch.setCBarcode(barcode);
    subcontractOrderDBatch.setIsEffective(true);
    // 完整条码：现品票_版本号
    subcontractOrderDBatch .setCCompleteBarcode(barcode.concat("-00"));
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
   */
  public Kv orderDBatchExportDatas(Kv kv, String type) throws IOException {
    // 为true 说明是看所有的
    if (Boolean.parseBoolean(kv.getStr("isEffective"))) {
      kv.remove("isEffective");
    } else {
      kv.set("isEffective", "1");
    }
    
    List<Record> rowDatas;
    if ("未更改清单".equals(type)) {
      rowDatas = dbTemplate("subcontractorderdbatch.getPdfBySubcontractOrderMId", kv).find();
    } else {
      rowDatas = dbTemplate("subcontractorderdbatch.getVersionFindBySubcontractOrderMId", kv).find();
    }
    
    Record record = new Record();
    record.set("SequenceNumber", "序号");
    record.set("cBarcode", "现品票");
    record.set("cInvCode1", "客户部番");
    record.set("dPlanDate", "计划到货日期");
    record.set("cOrderNo", "订单编号");
    record.set("iQty", "数量");
    record.set("cVersion", "版本");
    List<String> sheetNames = new ArrayList<>();

    List<Kv> rows = new ArrayList<>();

    List<Record> leftDatas = new ArrayList<>();
    List<Record> rightDatas = new ArrayList<>();
    leftDatas.add(record);
    rightDatas.add(record);
    int counter = 0;
    int i = 1;

    List<String> barCodesheetNames = new ArrayList<>();
    int barcodeqty = 0;
    int barcodecounter = 1;
    int q = 0;

    List<Kv> kvs = new ArrayList<>();

    List<Record> list1 = new ArrayList<>();
    List<Record> list2 = new ArrayList<>();
    List<Record> list3 = new ArrayList<>();
    List<Record> list4 = new ArrayList<>();
    List<Record> list5 = new ArrayList<>();
    List<Record> list6 = new ArrayList<>();
    List<Record> list7 = new ArrayList<>();
    List<Record> list8 = new ArrayList<>();
    int cont = 0;
    int k = 0;
    // 采购现品票明细条码数据sheet分页数组
    List<String> sheetNames2 = new ArrayList<>();

    List<String> sheetNames3 = new ArrayList<>();
    sheetNames3.add("现品票更改清单");

    for (int j = 0; j < rowDatas.size(); j++) {
      rowDatas.get(j).set("dPlanDate", rowDatas.get(j).getStr("dPlanDate"));

      if ("1".equals(kv.getStr("type"))) {

        //<editor-fold desc="生成单个条码数据">
        String sheetName = "订货条码" + (j + 1);
        sheetNames2.add(sheetName);

        BufferedImage bufferedImage = QrCodeUtil.generate(rowDatas.get(j).get("cBarcode"), BarcodeFormat.CODE_39, 1800, 1000);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "jpeg", os);
        rowDatas.get(j).set("img", os.toByteArray());

        BufferedImage bufferedImage2 = QrCodeUtil.generate(rowDatas.get(j).get("cBarcode"), BarcodeFormat.QR_CODE, 250, 250);
        ByteArrayOutputStream os2 = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage2, "jpeg", os2);
        rowDatas.get(j).set("img2", os2.toByteArray());

        kvs.add(Kv.by("sheetName", sheetName).set("list1", Collections.singletonList(rowDatas.get(j))));
        //</editor-fold>

      } else {

        //<editor-fold desc="生成多个条码数据">
        String sheetName2 = "条码" + barcodecounter;

        if (barcodeqty % 8 == 0) {
          barCodesheetNames.add(sheetName2);
          barcodecounter += 1;
        }
        //条形码
        BufferedImage bufferedImage = QrCodeUtil.generate(rowDatas.get(j).get("cBarcode"), BarcodeFormat.CODE_39, 100, 20);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "jpeg", os);
        rowDatas.get(j).set("img", os.toByteArray());
        //二维码
        BufferedImage bufferedImage2 = QrCodeUtil.generate(rowDatas.get(j).get("cBarcode"), BarcodeFormat.QR_CODE, 100, 100);
        ByteArrayOutputStream os2 = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage2, "jpeg", os2);
        rowDatas.get(j).set("img2", os2.toByteArray());
        if (cont < 1) {
          list1.add(rowDatas.get(j));
        } else if (cont < 2) {
          list2.add(rowDatas.get(j));
        } else if (cont < 3) {
          list3.add(rowDatas.get(j));
        } else if (cont < 4) {
          list4.add(rowDatas.get(j));
        } else if (cont < 5) {
          list5.add(rowDatas.get(j));
        } else if (cont < 6) {
          list6.add(rowDatas.get(j));
        } else if (cont < 7) {
          list7.add(rowDatas.get(j));
        } else if (cont < 8) {
          list8.add(rowDatas.get(j));
        }

        cont++;
        if (cont == 8) {
          String sheetName = "订货条码" + (k + 1);
          sheetNames2.add(sheetName);
          kvs.add(Kv.by("sheetName", sheetName)
              .set("list1", list1)
              .set("list2", list2)
              .set("list3", list3)
              .set("list4", list4)
              .set("list5", list5)
              .set("list6", list6)
              .set("list7", list7)
              .set("list8", list8));
          list1 = new ArrayList<>();
          list2 = new ArrayList<>();
          list3 = new ArrayList<>();
          list4 = new ArrayList<>();
          list5 = new ArrayList<>();
          list6 = new ArrayList<>();
          list7 = new ArrayList<>();
          list8 = new ArrayList<>();
          cont = 0;
          k++;
        }
        if ((rowDatas.size() - 1) == j) {
          String sheetName = "订货条码" + (k + 1);
          sheetNames2.add(sheetName);
          kvs.add(Kv.by("sheetName", sheetName)
              .set("list1", list1)
              .set("list2", list2)
              .set("list3", list3)
              .set("list4", list4)
              .set("list5", list5)
              .set("list6", list6)
              .set("list7", list7)
              .set("list8", list8));
        }
        //</editor-fold>

      }


      if (i % 2 != 0) {
        leftDatas.add(rowDatas.get(j));
        if (j == 0) {
          continue;
        }
        int pageCount = i * 14 + (i - 1);
        if (j % pageCount == 0) {
          i += 1;
        }
      } else {
        rightDatas.add(rowDatas.get(j));
        int pageCount = (i / 2) * 29 + ((i / 2) - 1);
        if (j % pageCount == 0) {
          i += 1;
        }
      }
      if (j == 0) {
        continue;
      }
      if (j % 29 == 0) {
        counter += 1;
        String sheetName = "订货清单" + counter;
        sheetNames.add(sheetName);
        rows.add(Kv.by("sheetName", sheetName).set("leftDatas", leftDatas).set("rightDatas", rightDatas));
        leftDatas = new ArrayList<>();
        rightDatas = new ArrayList<>();
        leftDatas.add(record);
        rightDatas.add(record);
      }
      if (j == (rowDatas.size() - 1)) {
        if (leftDatas.size() > 1 || rightDatas.size() > 1) {
          counter += 1;
          String sheetName = "订货清单" + counter;
          sheetNames.add(sheetName);
          Kv kv1 = Kv.by("sheetName", sheetName);
          if (leftDatas.size() > 1) {
            kv1.put("leftDatas", leftDatas);
          }
          if (rightDatas.size() > 1) {
            kv1.put("rightDatas", rightDatas);
          }
          rows.add(kv1);
        }
      }
    }
    //</editor-fold>
    Integer totalQuantity = rowDatas.size() + 1;
    Integer pages = rows.size();
    Date date = new DateTime();

    if ("未更改清单".equals(type)) {
      return Kv.by("rows", rows).set("sheetNames", sheetNames).set("rows2", kvs).set("sheetNames2", sheetNames2);
    } else {
      Kv kv1 = Kv.by("rows", rows).set("sheetNames", sheetNames).set("rows2", kvs).set("sheetNames2", sheetNames2).
          set("sheetNames3", sheetNames3).set("rows3", rowDatas);
      return kv1;
    }
  }

  public Ret updateOrderBatch(Long subcontractOrderMId, Long id, String cVersion, BigDecimal qty) {
    SubcontractOrderDBatch orderDBatch = findById(id);
    ValidationUtils.notNull(orderDBatch, JBoltMsg.DATA_NOT_EXIST);
    ValidationUtils.notBlank(cVersion, "版本号未获取到");
    ValidationUtils.isTrue(qty != null && qty.compareTo(BigDecimal.ZERO) > 0, "版本号未获取到");
    ValidationUtils.isTrue(!cVersion.equals(orderDBatch.getCVersion()), "版本号不能一致");
    ValidationUtils.isTrue(qty.compareTo(orderDBatch.getIQty()) <= 0, "现品票的数量不可大于包装数量，只可小于包装数量");
//    // 新增一个现成票后，再生产一个版本记录表，及修改详情；
//    String barCode = generateBarCode();
    SubcontractOrderDBatch newBatch = createSubcontractOrderDBatch(orderDBatch.getISubcontractOrderDid(), orderDBatch.getISubcontractOrderdQtyId(),
        orderDBatch.getIinventoryId(), orderDBatch.getDPlanDate(), qty, orderDBatch.getCBarcode());
    // 设置新版本号
    newBatch.setCVersion(cVersion);
    newBatch.setCCompleteBarcode(orderDBatch.getCBarcode().concat("-").concat(cVersion));
    // 添加来源id
    newBatch.setCSourceld(String.valueOf(id));
    // 将旧的改为失效
    orderDBatch.setIsEffective(false);
    List<SubcontractorderdQty> subcontractorderdQtyList = subcontractorderdQtyService.findBySubcontractOrderDId(orderDBatch.getISubcontractOrderDid());

    tx(() -> {
      // 新增
      newBatch.save();
      // 保存记录
      SubcontractOrderDBatchVersion batchVersion = subcontractOrderDBatchVersionService.createBatchVersion(subcontractOrderMId, id, orderDBatch.getIinventoryId(),
          orderDBatch.getDPlanDate(), cVersion, orderDBatch.getCVersion(), orderDBatch.getCBarcode(), qty, orderDBatch.getIQty());
      batchVersion.save();
      // 修改
      orderDBatch.update();

      for (SubcontractorderdQty subcontractorderdQty : subcontractorderdQtyList) {
        Long qtyIAutoId = subcontractorderdQty.getIAutoId();
        if (qtyIAutoId.equals(orderDBatch.getISubcontractOrderdQtyId())) {
          // 总数量 - 更改的数量
//          BigDecimal num = subcontractorderdQty.getIQty().subtract(orderDBatch.getIQty().subtract(qty));
          subcontractorderdQty.setIQty(qty);
          subcontractorderdQty.update();
        }
      }
      return true;
    });

    return SUCCESS;
  }

}
