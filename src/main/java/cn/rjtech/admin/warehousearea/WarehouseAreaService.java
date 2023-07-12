package cn.rjtech.admin.warehousearea;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.warehouse.WarehouseService;
import cn.rjtech.cache.CusFieldsMappingdCache;
import cn.rjtech.enums.SourceEnum;
import cn.rjtech.model.momdata.Warehouse;
import cn.rjtech.model.momdata.WarehouseArea;
import cn.rjtech.util.BillNoUtils;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * 库区档案 Service
 *
 * @ClassName: WarehouseAreaService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-11-02 20:43
 */
public class WarehouseAreaService extends BaseService<WarehouseArea> {

  private final WarehouseArea dao = new WarehouseArea().dao();

  @Override
  protected WarehouseArea dao() {
    return dao;
  }

  @Inject
  private WarehouseService warehouseService;

  /**
   * 后台管理分页查询
   */
  public Page<Record> paginateAdminDatas(int pageNumber, int pageSize, Kv kv) {
    return dbTemplate("warehousearea.paginateAdminDatas", kv).paginate(pageNumber, pageSize);
  }

  public List<Record> list(Kv kv) {
    if (kv.getStr("select") != null) {
      kv.set("iwarehouseid", kv.getStr("iwarehouseid") == null ? "0" : kv.getStr("iwarehouseid"));
    }
    return dbTemplate("warehousearea.paginateAdminDatas", kv).find();
  }

  public WarehouseArea findByWhAreaCode(String careacode) {
    return findFirst(Okv.by("cAreaCode", careacode).set("isDeleted", false), "iautoid", "asc");
  }

  public WarehouseArea findByWhAreaName(String careaname) {
    return findFirst(Okv.by("cAreaName", careaname).set("isDeleted", false), "iautoid", "asc");
  }

  public List<WarehouseArea> findAreaListByWhName(String carename) {
    Sql sql = selectSql()
        .eq(WarehouseArea.CAREANAME, carename)
        .eq(WarehouseArea.IORGID, getOrgId())
        .eq(WarehouseArea.ISDELETED, ZERO_STR);

    return find(sql);
  }

  /**
   * 保存
   */
  public Ret save(WarehouseArea warehouseArea) {
    if (warehouseArea == null || isOk(warehouseArea.getIautoid())) {
      return fail(JBoltMsg.PARAM_ERROR);
    }
    Warehouse warehouse = warehouseService.findById(warehouseArea.getIwarehouseid());
    if (dbTemplate("warehousearea.verifyDuplication", Kv.by("careacode", warehouseArea.getCareacode()).
        set("iwarehouseid", warehouseArea.getIwarehouseid())).queryInt() > 0) {
      ValidationUtils.error("【" + warehouse.getCWhName() + "】下【库区编码】" + warehouseArea.getCareacode() + "已存在，请修改后保存");
    }
    if (dbTemplate("warehousearea.verifyDuplication", Kv.by("careaname", warehouseArea.getCareaname()).
        set("iwarehouseid", warehouseArea.getIwarehouseid())).queryInt() > 0) {
      ValidationUtils.error("【" + warehouse.getCWhName() + "】下【库区名称】" + warehouseArea.getCareaname() + "已存在，请修改后保存");
    }

    //if(existsName(warehouseArea.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
    warehouseArea.setIcreateby(JBoltUserKit.getUserId());
    warehouseArea.setCcreatename(JBoltUserKit.getUserName());
    warehouseArea.setDcreatetime(new Date());

    warehouseArea.setCorgcode(getOrgCode());
    warehouseArea.setCorgname(getOrgName());
    warehouseArea.setIorgid(getOrgId());

    boolean success = warehouseArea.save();
    if (success) {
      //添加日志
      //addSaveSystemLog(warehouseArea.getIautoid(), JBoltUserKit.getUserId(), warehouseArea.getName());
    }
    return ret(success);
  }

  /**
   * 更新
   */
  public Ret update(WarehouseArea warehouseArea) {
    if (warehouseArea == null || notOk(warehouseArea.getIautoid())) {
      return fail(JBoltMsg.PARAM_ERROR);
    }
    //更新时需要判断数据存在
    WarehouseArea dbWarehouseArea = findById(warehouseArea.getIautoid());
    if (dbWarehouseArea == null) {
      return fail(JBoltMsg.DATA_NOT_EXIST);
    }

    Warehouse warehouse = warehouseService.findById(warehouseArea.getIwarehouseid());
    if (dbTemplate("warehousearea.verifyDuplication", Kv.by("careacode", warehouseArea.getCareacode()).
        set("iwarehouseid", warehouseArea.getIwarehouseid()).set("iautoid", warehouseArea.getIautoid())).queryInt() > 0) {
      ValidationUtils.error("【" + warehouse.getCWhName() + "】下【库区编码】" + warehouseArea.getCareacode() + "已存在，请修改后保存");
    }
    if (dbTemplate("warehousearea.verifyDuplication", Kv.by("careaname", warehouseArea.getCareaname()).
        set("iwarehouseid", warehouseArea.getIwarehouseid()).set("iautoid", warehouseArea.getIautoid())).queryInt() > 0) {
      ValidationUtils.error("【" + warehouse.getCWhName() + "】下【库区名称】" + warehouseArea.getCareaname() + "已存在，请修改后保存");
    }

    warehouseArea.setIupdateby(JBoltUserKit.getUserId());
    warehouseArea.setCupdatename(JBoltUserKit.getUserName());
    warehouseArea.setDupdatetime(new Date());
    //if(existsName(warehouseArea.getName(), warehouseArea.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
    boolean success = warehouseArea.update();
    if (success) {
      //添加日志
      //addUpdateSystemLog(warehouseArea.getIautoid(), JBoltUserKit.getUserId(), warehouseArea.getName());
    }
    return ret(success);
  }

  /**
   * 删除 指定多个ID
   */
  public Ret deleteByBatchIds(String ids) {
    tx(() -> {
      String[] idarry = ids.split(",");
      Integer qty = dbTemplate("warehousearea.getShelvesById", Kv.by("id", ids)).queryInt();
      if (qty > 0) {
        ValidationUtils.error("数据已被货架档案引用，无法删除！");
      }

      String[] split = ids.split(",");
      for (String id : split) {
        WarehouseArea warehouseArea = findById(id);
        if (warehouseArea.getIsource() != null) {
          if (warehouseArea.getIsource() == 2) {
            ValidationUtils.error("【"+warehouseArea.getCareaname() + "】来源U8，无法删除");
          }
        }
        warehouseArea.setIsdeleted(true);
        warehouseArea.update();
      }

      //料品档案主表
      //deleteByIds(ids,true);
//      update("UPDATE Bd_Warehouse_Area SET isDeleted = 1 WHERE iAutoId IN (" + ArrayUtil.join(idarry, COMMA) + ") ");
      return true;
    });
    return SUCCESS;
//    return deleteByIds(ids,true);
  }

  /**
   * 删除数据后执行的回调
   *
   * @param warehouseArea 要删除的model
   * @param kv            携带额外参数一般用不上
   */
  @Override
  protected String afterDelete(WarehouseArea warehouseArea, Kv kv) {
    //addDeleteSystemLog(warehouseArea.getIautoid(), JBoltUserKit.getUserId(),warehouseArea.getName());
    return null;
  }

  /**
   * 检测是否可以删除
   *
   * @param warehouseArea 要删除的model
   * @param kv            携带额外参数一般用不上
   */
  @Override
  public String checkCanDelete(WarehouseArea warehouseArea, Kv kv) {
    //如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
    return checkInUse(warehouseArea, kv);
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
   * @param warehouseArea 要toggle的model
   * @param column        操作的哪一列
   * @param kv            携带额外参数一般用不上
   */
  @Override
  public String checkCanToggle(WarehouseArea warehouseArea, String column, Kv kv) {
    //没有问题就返回null  有问题就返回提示string 字符串
    return null;
  }

  /**
   * toggle操作执行后的回调处理
   */
  @Override
  protected String afterToggleBoolean(WarehouseArea warehouseArea, String column, Kv kv) {
    //addUpdateSystemLog(warehouseArea.getIautoid(), JBoltUserKit.getUserId(), warehouseArea.getName(),"的字段["+column+"]值:"+warehouseArea.get(column));
    return null;
  }

  /**
   * 检测是否可以删除
   *
   * @param warehouseArea model
   * @param kv            携带额外参数一般用不上
   */
  @Override
  public String checkInUse(WarehouseArea warehouseArea, Kv kv) {
    //这里用来覆盖 检测WarehouseArea是否被其它表引用
    return null;
  }

  public List<Record> findByWareHouseId(long wareHouseId) {
    return dbTemplate("warehousearea.findByWareHouseId", Okv.by("wareHouseId", wareHouseId)).find();
  }

  /**
   * 打印数据
   *
   * @param kv 查询参数
   * @return
   */
  public Object getPrintDataCheck(Kv kv) {
    return dbTemplate("warehousearea.containerPrintData", kv).find();
  }

  public List<Record> options(Kv kv) {
    Object whcode = kv.get("whcode");
    if (whcode != null) {
      Warehouse warehouse = warehouseService.findByCwhcode(whcode.toString());
      if (warehouse != null) {
        kv.set("iwarehouseid", warehouse.getIAutoId());
      }
    }
    return dbTemplate("warehousearea.options", kv).find();
  }

    /**
     * 数据导入
     */
    public Ret importExcelData(File file) {
        List<Record> datas = CusFieldsMappingdCache.ME.getImportRecordsByTableName(file, table());
        ValidationUtils.notEmpty(datas, "导入数据不能为空");

        Date now = new Date();

        tx(() -> {
            int iseq = 1;

            // 封装数据
            for (Record data : datas) {
                // 基本信息校验
                ValidationUtils.notNull(data.get("careacode"), "第" + iseq + "行的【库区编码】不能为空！");
                ValidationUtils.notNull(data.get("careaname"), "第" + iseq + "行的【库区名称】不能为空！");
                ValidationUtils.notNull(data.get("cwhname"), "第" + iseq + "行的【所属仓库名称】不能为空！");

                //判断是否存在这个仓库名称
                Warehouse warehouse = warehouseService.findByWhName(data.get("cwhname") + "");
                ValidationUtils.notNull(warehouse, "第" + iseq + "行【" + data.get("cwhname") + "】" + JBoltMsg.DATA_NOT_EXIST);
                ValidationUtils.isTrue(warehouse.getIsEnabled(), "第" + iseq + "行【" + data.get("cwhname") + "】未启用库区选项");

                if (dbTemplate("warehousearea.verifyDuplication", Kv.by("careacode", data.get("careacode")).set("iwarehouseid", warehouse.getIAutoId())).queryInt() > 0) {
                    ValidationUtils.error("第" + iseq + "行的【" + warehouse.getCWhName() + "】下【库区编码】" + data.get("careacode") + "已存在，请修改后保存");
                }
                if (dbTemplate("warehousearea.verifyDuplication", Kv.by("careaname", data.get("careaname")).set("iwarehouseid", warehouse.getIAutoId())).queryInt() > 0) {
                    ValidationUtils.error("第" + iseq + "行的【" + warehouse.getCWhName() + "】下【库区名称】" + data.get("careaname") + "已存在，请修改后保存");
                }

                WarehouseArea warehouseArea = new WarehouseArea();
                
                // 组织数据
                warehouseArea.setIorgid(getOrgId());
                warehouseArea.setCorgcode(getOrgCode());
                warehouseArea.setCorgname(getOrgName());

                // 创建人
                warehouseArea.setIcreateby(JBoltUserKit.getUserId());
                warehouseArea.setCcreatename(JBoltUserKit.getUserName());
                warehouseArea.setDcreatetime(now);

                // 更新人
                warehouseArea.setIupdateby(JBoltUserKit.getUserId());
                warehouseArea.setCupdatename(JBoltUserKit.getUserName());
                warehouseArea.setDupdatetime(now);

                // 是否删除，是否启用,数据来源
                warehouseArea.setIsdeleted(false);
                warehouseArea.setIsenabled(true);
                warehouseArea.setIsource(SourceEnum.MES.getValue());

                warehouseArea.setCareacode(data.getStr("careacode"));
                warehouseArea.setCareaname(data.getStr("careaname"));

                warehouseArea.setIMaxCapacity(Long.parseLong(data.getStr("imaxcapacity")));
                warehouseArea.setIwarehouseid(warehouse.getIAutoId());
                warehouseArea.setCmemo(data.getStr("cmemo"));
                ValidationUtils.isTrue(warehouseArea.save(), "第" + iseq + "行保存数据失败");
                iseq++;
            }
            return true;
        });

        return SUCCESS;
    }

    public WarehouseArea getWarehouseAreaCode() {
        WarehouseArea warehouseArea = new WarehouseArea();
        warehouseArea.setCareacode(BillNoUtils.genCode(getOrgCode(), table()));
        return warehouseArea;
    }
    
}
