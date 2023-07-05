package cn.rjtech.admin.warehouseshelves;

import cn.hutool.core.util.ArrayUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.poi.excel.JBoltExcel;
import cn.jbolt.core.poi.excel.JBoltExcelHeader;
import cn.jbolt.core.poi.excel.JBoltExcelSheet;
import cn.jbolt.core.poi.excel.JBoltExcelUtil;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.cusfieldsmappingd.CusFieldsMappingDService;
import cn.rjtech.admin.warehouse.WarehouseService;
import cn.rjtech.admin.warehousearea.WarehouseAreaService;
import cn.rjtech.model.momdata.Warehouse;
import cn.rjtech.model.momdata.WarehouseArea;
import cn.rjtech.model.momdata.WarehouseShelves;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static cn.hutool.core.text.StrPool.COMMA;

/**
 * 货架档案 Service
 *
 * @ClassName: WarehouseShelvesService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-11-02 20:44
 */
public class WarehouseShelvesService extends BaseService<WarehouseShelves> {
  private final WarehouseShelves dao = new WarehouseShelves().dao();

  @Override
  protected WarehouseShelves dao() {
    return dao;
  }

  @Inject
  private WarehouseService warehouseService;
  @Inject
  private WarehouseAreaService warehouseAreaService;

  @Inject
  private CusFieldsMappingDService cusFieldsMappingdService;

  /**
   * 后台管理分页查询
   */
  public Page<Record> paginateAdminDatas(int pageNumber, int pageSize, Kv kv) {
    return dbTemplate("warehouseshelves.paginateAdminDatas", kv).paginate(pageNumber, pageSize);
  }

  public List<Record> list(Kv kv) {
    kv.set("iwarehouseareaid", kv.getStr("iwarehouseareaid") == null ? 0 : kv.getStr("iwarehouseareaid"));
    return dbTemplate("warehouseshelves.paginateAdminDatas", kv).find();
  }


  /**
   * 查找此货架编码是否存在
   *
   * @param cshelvescode
   * @return
   */
  public WarehouseShelves findByCshelvesCode(String cshelvescode) {
    return findFirst(Okv.by("cShelvesCode", cshelvescode).set("isDeleted", false), "iautoid", "asc");
  }

  /**
   * 保存
   */
  public Ret save(WarehouseShelves warehouseShelves) {
    if (warehouseShelves == null || isOk(warehouseShelves.getIautoid())) {
      return fail(JBoltMsg.PARAM_ERROR);
    }

    Integer cshelvescode = dbTemplate("warehouseshelves.verifyDuplication", Kv.by("cshelvescode", warehouseShelves.getCshelvescode())
        .set("iwarehouseid", warehouseShelves.getIwarehouseid()).set("iwarehouseareaid", warehouseShelves.getIwarehouseareaid())).queryInt();
    if (cshelvescode >= 1) {
      ValidationUtils.error("【货架编码】" + warehouseShelves.getCshelvescode() + "已存在，请修改后保存");
    }

    Integer cshelvesname = dbTemplate("warehouseshelves.verifyDuplication", Kv.by("cshelvesname", warehouseShelves.getCshelvesname())
        .set("iwarehouseid", warehouseShelves.getIwarehouseid()).set("iwarehouseareaid", warehouseShelves.getIwarehouseareaid())).queryInt();
    if (cshelvesname >= 1) {
      ValidationUtils.error("【货架名称】" + warehouseShelves.getCshelvesname() + "已存在，请修改后保存");
    }

    warehouseShelves.setIcreateby(JBoltUserKit.getUserId());
    warehouseShelves.setCcreatename(JBoltUserKit.getUserName());
    warehouseShelves.setDcreatetime(new Date());

    warehouseShelves.setCorgcode(getOrgCode());
    warehouseShelves.setCorgname(getOrgName());
    warehouseShelves.setIorgid(getOrgId());

    //更新
    warehouseShelves.setIupdateby(JBoltUserKit.getUserId());
    warehouseShelves.setCupdatename(JBoltUserKit.getUserName());
    warehouseShelves.setDupdatetime(new Date());


    boolean success = warehouseShelves.save();
    if (success) {
      //添加日志
      //addSaveSystemLog(warehouseShelves.getIautoid(), JBoltUserKit.getUserId(), warehouseShelves.getName());
    }
    return ret(success);
  }

  /**
   * 更新
   */
  public Ret update(WarehouseShelves warehouseShelves) {
    if (warehouseShelves == null || notOk(warehouseShelves.getIautoid())) {
      return fail(JBoltMsg.PARAM_ERROR);
    }
    //更新时需要判断数据存在
    WarehouseShelves dbWarehouseShelves = findById(warehouseShelves.getIautoid());
    if (dbWarehouseShelves == null) {
      return fail(JBoltMsg.DATA_NOT_EXIST);
    }

    Integer cshelvescode = dbTemplate("warehouseshelves.verifyDuplication", Kv.by("cshelvescode", warehouseShelves.getCshelvescode())
        .set("iwarehouseid", warehouseShelves.getIwarehouseid()).set("iwarehouseareaid", warehouseShelves.getIwarehouseareaid()).
            set("iautoid", warehouseShelves.getIautoid())).queryInt();
    if (cshelvescode >= 1) {
      ValidationUtils.error("【货架编码】" + warehouseShelves.getCshelvescode() + "已存在，请修改后保存");
    }
    Integer cshelvesname = dbTemplate("warehouseshelves.verifyDuplication", Kv.by("cshelvesname", warehouseShelves.getCshelvesname())
        .set("iwarehouseid", warehouseShelves.getIwarehouseid()).set("iwarehouseareaid", warehouseShelves.getIwarehouseareaid()).
            set("iautoid", warehouseShelves.getIautoid())).queryInt();
    if (cshelvesname >= 1) {
      ValidationUtils.error("【货架名称】" + warehouseShelves.getCshelvescode() + "已存在，请修改后保存");
    }

    dbWarehouseShelves.setIupdateby(JBoltUserKit.getUserId());
    dbWarehouseShelves.setCupdatename(JBoltUserKit.getUserName());
    dbWarehouseShelves.setDupdatetime(new Date());
    //if(existsName(warehouseShelves.getName(), warehouseShelves.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
    boolean success = warehouseShelves.update();
    if (success) {
      //添加日志
      //addUpdateSystemLog(warehouseShelves.getIautoid(), JBoltUserKit.getUserId(), warehouseShelves.getName());
    }
    return ret(success);
  }

  /**
   * 删除 指定多个ID
   */
  public Ret deleteByBatchIds(String ids) {
    tx(() -> {
      String[] idarry = ids.split(",");


      //料品档案主表
      //deleteByIds(ids,true);
      update("UPDATE Bd_Warehouse_Shelves SET isDeleted = 1 WHERE iAutoId IN (" + ArrayUtil.join(idarry, COMMA) + ") ");
      return true;
    });
    return SUCCESS;
//    return deleteByIds(ids,true);
  }

  /**
   * 删除数据后执行的回调
   *
   * @param warehouseShelves 要删除的model
   * @param kv               携带额外参数一般用不上
   */
  @Override
  protected String afterDelete(WarehouseShelves warehouseShelves, Kv kv) {
    //addDeleteSystemLog(warehouseShelves.getIautoid(), JBoltUserKit.getUserId(),warehouseShelves.getName());
    return null;
  }

  /**
   * 检测是否可以删除
   *
   * @param warehouseShelves 要删除的model
   * @param kv               携带额外参数一般用不上
   */
  @Override
  public String checkCanDelete(WarehouseShelves warehouseShelves, Kv kv) {
    //如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
    return checkInUse(warehouseShelves, kv);
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
   * @param warehouseShelves 要toggle的model
   * @param column           操作的哪一列
   * @param kv               携带额外参数一般用不上
   */
  @Override
  public String checkCanToggle(WarehouseShelves warehouseShelves, String column, Kv kv) {
    //没有问题就返回null  有问题就返回提示string 字符串
    return null;
  }

  /**
   * toggle操作执行后的回调处理
   */
  @Override
  protected String afterToggleBoolean(WarehouseShelves warehouseShelves, String column, Kv kv) {
    //addUpdateSystemLog(warehouseShelves.getIautoid(), JBoltUserKit.getUserId(), warehouseShelves.getName(),"的字段["+column+"]值:"+warehouseShelves.get(column));
    return null;
  }

  /**
   * 检测是否可以删除
   *
   * @param warehouseShelves model
   * @param kv               携带额外参数一般用不上
   */
  @Override
  public String checkInUse(WarehouseShelves warehouseShelves, Kv kv) {
    //这里用来覆盖 检测WarehouseShelves是否被其它表引用
    return null;
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
        ValidationUtils.notNull(data.get("cshelvescode"), "第【" + iseq + "】行的货架编码不能为空！");
        ValidationUtils.notNull(data.get("cshelvesname"), "第【" + iseq + "】行的货架名称不能为空！");
        ValidationUtils.notNull(data.get("cwhname"), "第【" + iseq + "】行的所属仓库名称不能为空！");

        Warehouse warehouse = warehouseService.findByWhName(data.get("cwhname") + "");
        ValidationUtils.notNull(warehouse, "第【" + iseq + "】行【仓库名称-" + data.get("cwhname") + "】"
            + JBoltMsg.DATA_NOT_EXIST);

        WarehouseArea warehouseArea = warehouseAreaService.findByWhAreaName(data.get("careaname") + "");
        if (isOk(data.get("careaname"))) {
          ValidationUtils.notNull(warehouseArea, "第【" + iseq + "】行【所属库区名称-" + data.get("careaname") + "】"
              + JBoltMsg.DATA_NOT_EXIST);
        }


        Integer cshelvescode = dbTemplate("warehouseshelves.verifyDuplication", Kv.by("cshelvescode", data.get("cshelvescode"))
            .set("iwarehouseid", warehouse.getIAutoId()).set("iwarehouseareaid", warehouseArea.getIautoid())).queryInt();
        if (cshelvescode >= 1) {
          ValidationUtils.error("第【" + iseq + "】行【货架编码】" + data.get("cshelvescode") + "已存在，请修改后保存");
        }

        Integer cshelvesname = dbTemplate("warehouseshelves.verifyDuplication", Kv.by("cshelvesname", data.get("cshelvesname"))
            .set("iwarehouseid", warehouse.getIAutoId()).set("iwarehouseareaid", warehouseArea.getIautoid())).queryInt();
        if (cshelvesname >= 1) {
          ValidationUtils.error("第【" + iseq + "】行【货架名称】" + data.get("cshelvesname") + "已存在，请修改后保存");
        }

        WarehouseShelves warehouseShelves = new WarehouseShelves();

        //组织数据
        warehouseShelves.setIorgid(getOrgId());
        warehouseShelves.setCorgcode(getOrgCode());
        warehouseShelves.setCorgname(getOrgName());

        //创建人
        warehouseShelves.setIcreateby(JBoltUserKit.getUserId());
        warehouseShelves.setCcreatename(JBoltUserKit.getUserName());
        warehouseShelves.setDcreatetime(new Date());

        //更新人
        warehouseShelves.setIupdateby(JBoltUserKit.getUserId());
        warehouseShelves.setCupdatename(JBoltUserKit.getUserName());
        warehouseShelves.setDupdatetime(new Date());

        //是否删除，是否启用,数据来源
        warehouseShelves.setIsdeleted(false);
        warehouseShelves.setIsenabled(true);
        warehouseShelves.setIsource(1);

        warehouseShelves.setCshelvescode(data.get("cshelvescode") + "");
        warehouseShelves.setCshelvesname(data.get("cshelvesname") + "");
        warehouseShelves.setIwarehouseid(warehouse.getIAutoId());
        warehouseShelves.setIwarehouseareaid(warehouseArea.getIautoid());
        warehouseShelves.setCmemo(data.get("cmemo") + "");

        ValidationUtils.isTrue(warehouseShelves.save(), "第" + iseq + "行保存数据失败");

        iseq++;
      }

      return true;
    });

    ValidationUtils.assertBlank(msg.toString(), msg + ",其他数据已处理");
    return SUCCESS;
  }

  /**
   * 打印
   */
  public Kv selectPrint(String code) {
    Kv kv = Kv.by("ids", code);

    StringBuilder str = new StringBuilder("(");
    String[] split = code.split(",");
    for (String id : split) {
      str.append("'").append(id).append("',");
    }
    str = new StringBuilder(str.substring(0, str.length() - 1));
    str.append(")");
    kv.set("str", str.toString());
    try {
      List<Record> list = dbTemplate("warehouseshelves.selectPrint", kv).find();
      Kv target = new Kv();
      target.set("table", list);
      return target;
    } finally {

    }

  }

}