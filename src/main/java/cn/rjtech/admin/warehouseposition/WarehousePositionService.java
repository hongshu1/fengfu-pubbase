package cn.rjtech.admin.warehouseposition;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.cusfieldsmappingd.CusFieldsMappingDService;
import cn.rjtech.admin.warehouse.WarehouseService;
import cn.rjtech.admin.warehousearea.WarehouseAreaService;
import cn.rjtech.admin.warehouseshelves.WarehouseShelvesService;
import cn.rjtech.enums.SourceEnum;
import cn.rjtech.model.momdata.WarehousePosition;
import cn.rjtech.util.BillNoUtils;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 库位档案 Service
 *
 * @ClassName: WarehousePositionService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-11-02 20:45
 */
public class WarehousePositionService extends BaseService<WarehousePosition> {
  private final WarehousePosition dao = new WarehousePosition().dao();


  @Override
  protected WarehousePosition dao() {
    return dao;
  }

  @Inject
  private WarehouseService warehouseService;
  @Inject
  private WarehouseAreaService warehouseAreaService;
  @Inject
  private WarehouseShelvesService warehouseShelvesService;

  @Inject
  private CusFieldsMappingDService cusFieldsMappingdService;

  /**
   * 后台管理分页查询
   */
  public Page<Record> paginateAdminDatas(int pageNumber, int pageSize, Kv kv) {
    return dbTemplate("warehouseposition.paginateAdminDatas", kv).paginate(pageNumber, pageSize);
    //return paginateByKeywords("iAutoId","DESC", pageNumber, pageSize, keywords, "iAutoId");
  }

  public List<Record> list(Kv kv) {
    return dbTemplate("warehouseposition.paginateAdminDatas", kv).find();
  }

  public Record findByIautoId(Kv kv) {
    return dbTemplate("warehouseposition.paginateAdminDatas", kv).findFirst();
  }

  public WarehousePosition findByCpositionCode(String cshelvescode) {
    return findFirst(Okv.by("cPositionCode", cshelvescode).set("isDeleted", false), "iautoid", "asc");
  }

  /**
   * 保存
   */
  public Ret save(WarehousePosition warehousePosition) {
    if (warehousePosition == null || isOk(warehousePosition.getIautoid())) {
      return fail(JBoltMsg.PARAM_ERROR);
    }

    Integer cshelvescode = dbTemplate("warehouseposition.verifyDuplication", Kv.by("cpositioncode", warehousePosition.getCpositioncode())
        .set("iwarehouseid", warehousePosition.getIwarehouseid()).set("iwarehouseareaid", warehousePosition.getIwarehouseareaid()).
            set("iwarehouseshelvesid", warehousePosition.getIwarehouseshelvesid())).queryInt();
    if (cshelvescode >= 1) {
      ValidationUtils.error("【库位编码】" + warehousePosition.getCpositioncode() + "已存在，请修改后保存");
    }

    Integer cshelvesname = dbTemplate("warehouseposition.verifyDuplication", Kv.by("cpositionname", warehousePosition.getCpositionname())
        .set("iwarehouseid", warehousePosition.getIwarehouseid()).set("iwarehouseareaid", warehousePosition.getIwarehouseareaid()).
            set("iwarehouseshelvesid", warehousePosition.getIwarehouseshelvesid())).queryInt();
    if (cshelvesname >= 1) {
      ValidationUtils.error("【库位名称】" + warehousePosition.getCpositionname() + "已存在，请修改后保存");
    }

    warehousePosition.setIcreateby(JBoltUserKit.getUserId());
    warehousePosition.setCcreatename(JBoltUserKit.getUserName());
    warehousePosition.setDcreatetime(new Date());

    warehousePosition.setCorgcode(getOrgCode());
    warehousePosition.setCorgname(getOrgName());
    warehousePosition.setIorgid(getOrgId());

    //更新信息
    warehousePosition.setIupdateby(JBoltUserKit.getUserId());
    warehousePosition.setCupdatename(JBoltUserKit.getUserName());
    warehousePosition.setDupdatetime(new Date());


    boolean success = warehousePosition.save();
    if (success) {
      //添加日志
      //addSaveSystemLog(warehousePosition.getIautoid(), JBoltUserKit.getUserId(), warehousePosition.getName());
    }
    return ret(success);
  }

  /**
   * 更新
   */
  public Ret update(WarehousePosition warehousePosition) {
    if (warehousePosition == null || notOk(warehousePosition.getIautoid())) {
      return fail(JBoltMsg.PARAM_ERROR);
    }
    //更新时需要判断数据存在
    WarehousePosition dbWarehousePosition = findById(warehousePosition.getIautoid());
    if (dbWarehousePosition == null) {
      return fail(JBoltMsg.DATA_NOT_EXIST);
    }

    Integer cshelvescode = dbTemplate("warehouseposition.verifyDuplication", Kv.by("cpositioncode", warehousePosition.getCpositioncode())
        .set("iwarehouseid", warehousePosition.getIwarehouseid()).set("iwarehouseareaid", warehousePosition.getIwarehouseareaid()).
            set("iwarehouseshelvesid", warehousePosition.getIwarehouseshelvesid()).set("iautoid", warehousePosition.getIautoid())).queryInt();
    if (cshelvescode >= 1) {
      ValidationUtils.error("【库位编码】" + warehousePosition.getCpositioncode() + "已存在，请修改后保存");
    }

    Integer cshelvesname = dbTemplate("warehouseposition.verifyDuplication", Kv.by("cpositionname", warehousePosition.getCpositionname())
        .set("iwarehouseid", warehousePosition.getIwarehouseid()).set("iwarehouseareaid", warehousePosition.getIwarehouseareaid()).
            set("iwarehouseshelvesid", warehousePosition.getIwarehouseshelvesid()).set("iautoid", warehousePosition.getIautoid())).queryInt();
    if (cshelvesname >= 1) {
      ValidationUtils.error("【库位名称】" + warehousePosition.getCpositionname() + "已存在，请修改后保存");
    }

    dbWarehousePosition.setIupdateby(JBoltUserKit.getUserId());
    dbWarehousePosition.setCupdatename(JBoltUserKit.getUserName());
    dbWarehousePosition.setDupdatetime(new Date());
    //if(existsName(warehousePosition.getName(), warehousePosition.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
    boolean success = warehousePosition.update();
    if (success) {
      //添加日志
      //addUpdateSystemLog(warehousePosition.getIautoid(), JBoltUserKit.getUserId(), warehousePosition.getName());
    }
    return ret(success);
  }

  /**
   * 删除 指定多个ID
   */
  public Ret deleteByBatchIds(String ids) {
    tx(() -> {
      String[] split = ids.split(",");
      for (String id : split) {
        WarehousePosition warehousePosition = findById(id);
        if (warehousePosition.getIsource() != null) {
          if (warehousePosition.getIsource() == 2) {
            ValidationUtils.error("【" + warehousePosition.getCpositionname() + "】来源U8，无法删除");
          }
        }
        warehousePosition.setIsdeleted(true);
        warehousePosition.update();
      }
      //料品档案主表
      //deleteByIds(ids,true);
//      update("UPDATE Bd_Warehouse_Position SET isDeleted = 1 WHERE iAutoId IN (" + ArrayUtil.join(idarry, COMMA) + ") ");
      return true;
    });
    return SUCCESS;
//    return deleteByIds(ids,true);
  }

  /**
   * 删除数据后执行的回调
   *
   * @param warehousePosition 要删除的model
   * @param kv                携带额外参数一般用不上
   */
  @Override
  protected String afterDelete(WarehousePosition warehousePosition, Kv kv) {
    //addDeleteSystemLog(warehousePosition.getIautoid(), JBoltUserKit.getUserId(),warehousePosition.getName());
    return null;
  }

  /**
   * 检测是否可以删除
   *
   * @param warehousePosition 要删除的model
   * @param kv                携带额外参数一般用不上
   */
  @Override
  public String checkCanDelete(WarehousePosition warehousePosition, Kv kv) {
    //如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
    return checkInUse(warehousePosition, kv);
  }

  /**
   * 设置返回二开业务所属的关键systemLog的targetType
   */
  @Override
  protected int systemLogTargetType() {
    return ProjectSystemLogTargetType.NONE.getValue();
  }

  /**
   * 切换isdeleted属性
   */
  public Ret toggleIsdeleted(Long id) {
    return toggleBoolean(id, "isDeleted");
  }

  /**
   * 切换isenabled属性
   */
  public Ret toggleIsenabled(Long id) {
    return toggleBoolean(id, "isEnabled");
  }

  /**
   * 检测是否可以toggle操作指定列
   *
   * @param warehousePosition 要toggle的model
   * @param column            操作的哪一列
   * @param kv                携带额外参数一般用不上
   */
  @Override
  public String checkCanToggle(WarehousePosition warehousePosition, String column, Kv kv) {
    //没有问题就返回null  有问题就返回提示string 字符串
    return null;
  }

  /**
   * toggle操作执行后的回调处理
   */
  @Override
  protected String afterToggleBoolean(WarehousePosition warehousePosition, String column, Kv kv) {
    //addUpdateSystemLog(warehousePosition.getIautoid(), JBoltUserKit.getUserId(), warehousePosition.getName(),"的字段["+column+"]值:"+warehousePosition.get(column));
    return null;
  }

  /**
   * 检测是否可以删除
   *
   * @param warehousePosition model
   * @param kv                携带额外参数一般用不上
   */
  @Override
  public String checkInUse(WarehousePosition warehousePosition, Kv kv) {
    //这里用来覆盖 检测WarehousePosition是否被其它表引用
    return null;
  }

  public List<WarehousePosition> dataList(Boolean isEnabled, Long iwarehouseid) {
    Okv kv = Okv.create();
    kv.set("isDeleted", false);
    if (isEnabled != null) {
      kv.set("isEnabled", isEnabled);
    }
    if (iwarehouseid == null) {
      return null;
    }
    kv.set("iWarehouseId", iwarehouseid);
    return getCommonList(kv, "iAutoId", "asc");
  }

  /**
   * 数据导入
   *
   * @param file
   * @param cformatName
   * @return
   */
  public Ret importExcelData(File file, String cformatName) {
    Ret ret = cusFieldsMappingdService.getImportDatas(file, cformatName);
    ValidationUtils.isTrue(ret.isOk(), "导入失败");
    ArrayList<Map> datas = (ArrayList<Map>) ret.get("data");
    StringBuilder msg = new StringBuilder();

    tx(() -> {
      Integer iseq = 1;
      // 封装数据
      for (Map<String, Object> data : datas) {
        // 基本信息校验
        ValidationUtils.notNull(data.get("cpositioncode"), "第【" + iseq + "】行的【库位编码】不能为空！");
        ValidationUtils.notNull(data.get("cpositionname"), "第【" + iseq + "】行的【库位名称】不能为空！");
        ValidationUtils.notNull(data.get("iwarehousename"), "第【" + iseq + "】行的【所属仓库名称】不能为空！");
        ValidationUtils.notNull(data.get("iwarehouseareaname"), "第【" + iseq + "】行的【所属库区名称】不能为空！");
        ValidationUtils.notNull(data.get("iwarehouseshelvesname"), "第【" + iseq + "】行的【所属货架名称】不能为空！");

        //数据完整性校验
        Record record = dbTemplate("warehouseposition.integrityCheck", Kv.by("iwarehousename", data.get("iwarehousename")).set("iwarehouseareaname", data.get("iwarehouseareaname")).
            set("iwarehouseshelvesname", data.get("iwarehouseshelvesname"))).findFirst();
        ValidationUtils.notNull(record, "第【" + iseq + "】行的【所属仓库名称】【所属库区名称】【所属货架名称】未找到对应的数据关联关系！");

        Integer cpositioncode = dbTemplate("warehouseposition.verifyDuplication", Kv.by("cpositioncode", data.get("cpositioncode")).
            set("iwarehouseid", record.getLong("iwarehouseid")).set("iwarehouseareaid", record.getLong("iwarehouseareaid")).
            set("iwarehouseshelvesid", record.getLong("iautoid"))).queryInt();
        if (cpositioncode > 0) {
          ValidationUtils.error("第【" + iseq + "】行的【库位编码】" + data.get("cpositioncode") + "已存在，请修改后保存");
        }

        Integer cpositionname = dbTemplate("warehouseposition.verifyDuplication", Kv.by("cpositionname", data.get("cpositionname")).
            set("iwarehouseid", record.getLong("iwarehouseid")).set("iwarehouseareaid", record.getLong("iwarehouseareaid")).
            set("iwarehouseshelvesid", record.getLong("iautoid"))).queryInt();
        if (cpositionname > 0) {
          ValidationUtils.error("第【" + iseq + "】行的【库位名称】" + data.get("cpositionname") + "已存在，请修改后保存");
        }

        WarehousePosition warehousePosition = new WarehousePosition();

        //组织数据
        warehousePosition.setIorgid(getOrgId());
        warehousePosition.setCorgcode(getOrgCode());
        warehousePosition.setCorgname(getOrgName());

        //创建人
        warehousePosition.setIcreateby(JBoltUserKit.getUserId());
        warehousePosition.setCcreatename(JBoltUserKit.getUserName());
        warehousePosition.setDcreatetime(new Date());

        //更新人
        warehousePosition.setIupdateby(JBoltUserKit.getUserId());
        warehousePosition.setCupdatename(JBoltUserKit.getUserName());
        warehousePosition.setDupdatetime(new Date());

        //是否删除，是否启用,数据来源
        warehousePosition.setIsdeleted(false);
        warehousePosition.setIsenabled(true);
        warehousePosition.setIsource(SourceEnum.MES.getValue());

        warehousePosition.setCpositioncode(data.get("cpositioncode") + "");
        warehousePosition.setCpositionname(data.get("cpositionname") + "");

        warehousePosition.setIwarehouseid(record.getLong("iwarehouseid"));
        warehousePosition.setIwarehouseareaid(record.getLong("iwarehouseareaid"));
        warehousePosition.setIwarehouseshelvesid(record.getLong("iautoid"));

        warehousePosition.setILength(BigDecimal.valueOf(Integer.parseInt(data.get("ilength") + "")));
        warehousePosition.setIwidth(BigDecimal.valueOf(Integer.parseInt(data.get("iwidth") + "")));
        warehousePosition.setIheight(BigDecimal.valueOf(Integer.parseInt(data.get("iheight") + "")));
        warehousePosition.setImaxweight(BigDecimal.valueOf(Integer.parseInt(data.get("imaxweight") + "")));
        warehousePosition.setImaxbulk(BigDecimal.valueOf(Integer.parseInt(data.get("imaxbulk") + "")));

        ValidationUtils.isTrue(warehousePosition.save(), "第" + iseq + "行保存数据失败");

        iseq++;
      }

      return true;
    });

    ValidationUtils.assertBlank(msg.toString(), msg + ",其他数据已处理");
    return SUCCESS;
  }

  public List<Record> options(Kv kv) {
    String iMouldsId = kv.getStr("iMouldsId");
    if (isOk(iMouldsId)) {
      return dbTemplate("warehouseposition.findByMouldsId", kv).find();
    } else {
      Sql sql = selectSql().eq("isDeleted", false).eq("isEnabled", true).orderBy("dCreateTime", false);
      Long iAutoid = kv.getLong("iWarehouseId");
      if (isOk(iAutoid)) {
        sql.eq("iWarehouseId", iAutoid);
      }
      return findRecord(sql);
    }
  }

  public List<WarehousePosition> getIdAndNameList() {
    return find("SELECT iAutoId,cPositionName FROM Bd_Warehouse_Position WHERE isDeleted = '0' ");
  }

  /**
   * 打印
   *
   * @param code
   * @return
   */
  public Kv selectPrint(String code) {
    Kv kv = Kv.by("ids", code);

    String str = "(";
    String[] split = code.split(",");
    for (String id : split) {
      str += "'" + id + "',";
    }
    str = str.substring(0, str.length() - 1);
    str += ")";
    kv.set("str", str);
    try {
      List<Record> list = dbTemplate("warehouseposition.printwarehouseposition", kv).find();
      Kv target = new Kv();
      target.set("table", list);
      return target;
    } finally {

    }
  }

  /**
   * 打印数据
   *
   * @param kv 查询参数
   * @return
   */
  public Object getPrintDataCheck(Kv kv) {
    return dbTemplate("warehouseposition.printwarehouseposition", kv).find();
  }

  public WarehousePosition getWarehousePositionCode() {
    WarehousePosition warehousePosition = new WarehousePosition();
    warehousePosition.setCpositioncode(BillNoUtils.genCode(getOrgCode(), table()));
    return warehousePosition;
  }
}