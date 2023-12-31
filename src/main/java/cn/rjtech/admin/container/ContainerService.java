package cn.rjtech.admin.container;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.containerstockind.ContainerStockInDService;
import cn.rjtech.admin.containerstockind.ContainerStockInMService;
import cn.rjtech.admin.containerstockind.ContainerStockOutDService;
import cn.rjtech.admin.containerstockind.ContainerStockOutMService;
import cn.rjtech.admin.warehouse.WarehouseService;
import cn.rjtech.cache.CusFieldsMappingdCache;
import cn.rjtech.model.momdata.*;
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

import static cn.hutool.core.text.StrPool.COMMA;

/**
 * 仓库建模-容器档案
 *
 * @ClassName: ContainerService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-22 14:48
 */
public class ContainerService extends BaseService<Container> {
    
  private final Container dao = new Container().dao();

  @Override
  protected Container dao() {
    return dao;
  }

  @Override
  protected int systemLogTargetType() {
    return ProjectSystemLogTargetType.NONE.getValue();
  }

    @Inject
    private WarehouseService warehouseService;
    @Inject
    private ContainerStockOutDService stockOutDService;
    @Inject
    private ContainerStockOutMService stockOutMService;
    @Inject
    private ContainerStockInDService containerStockInDService;
    @Inject
    private ContainerStockInMService containerStockInMService;

  /**
   * 后台管理数据查询
   *
   * @param pageNumber 第几页
   * @param pageSize   每页几条数据
   * @param keywords   关键词
   * @param isInner    社内/社外：0. 社内 1. 社外
   */
  public Page<Container> getAdminDatas(int pageNumber, int pageSize, String keywords, Boolean isInner) {
    //创建sql对象
    Sql sql = selectSql().page(pageNumber, pageSize);
    //sql条件处理
    sql.eqBooleanToChar("isInner", isInner);
    //关键词模糊查询
    sql.likeMulti(keywords, "cContainerCode", "cContainerName", "iContainerClassId");
    //排序
    sql.desc("iAutoId");
    return paginate(sql);
  }

  /**
   * 保存
   */
  public Ret save(Container container) {
    if (container == null || isOk(container.getIAutoId())) {
      return fail(JBoltMsg.PARAM_ERROR);
    }
    ValidationUtils.assertNull(findByContainerCode(container.getCContainerCode()), "容器编码重复");

    //创建信息
    container.setICreateBy(JBoltUserKit.getUserId());
    container.setCCreateName(JBoltUserKit.getUserName());
    container.setDCreateTime(new Date());

    //更新信息
    container.setIUpdateBy(JBoltUserKit.getUserId());
    container.setCUpdateName(JBoltUserKit.getUserName());
    container.setDUpdateTime(new Date());

    //组织信息
    container.setCOrgCode(getOrgCode());
    container.setCOrgName(getOrgName());
    container.setIOrgId(getOrgId());
    //if(existsName(container.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
    boolean success = container.save();
    if (success) {
      //添加日志
      //addSaveSystemLog(container.getIAutoId(), JBoltUserKit.getUserId(), container.getName());
    }
    return ret(success);
  }

  /**
   * 查找容器编码
   */
  public Container findByContainerCode(String cContainerCode) {
    return findFirst(Okv.by("cContainerCode", cContainerCode).set("isDeleted", false), "iautoid", "asc");
  }

  /**
   * 更新
   */
  public Ret update(Container container) {
    if (container == null || notOk(container.getIAutoId())) {
      return fail(JBoltMsg.PARAM_ERROR);
    }
    //更新时需要判断数据存在
    Container dbContainer = findById(container.getIAutoId());
    if (dbContainer == null) {
      return fail(JBoltMsg.DATA_NOT_EXIST);
    }

    //查询编码是否存在
    Container con = findByContainerCode(container.getCContainerCode());

    //编码重复判断
    if (con != null && !container.getIAutoId().equals(con.getIAutoId())) {
      ValidationUtils.assertNull(con.getCContainerCode(), "容器编码重复！");
    }

    container.setIUpdateBy(JBoltUserKit.getUserId());
    container.setCUpdateName(JBoltUserKit.getUserName());
    container.setDUpdateTime(new Date());

    //if(existsName(container.getName(), container.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
    boolean success = container.update();
    if (success) {
      //添加日志
      //addUpdateSystemLog(container.getIAutoId(), JBoltUserKit.getUserId(), container.getName());
    }
    return ret(success);
  }

  /**
   * 删除数据后执行的回调
   *
   * @param container 要删除的model
   * @param kv        携带额外参数一般用不上
   */
  @Override
  protected String afterDelete(Container container, Kv kv) {
    //addDeleteSystemLog(container.getIAutoId(), JBoltUserKit.getUserId(),container.getName());
    return null;
  }

  /**
   * 检测是否可以删除
   *
   * @param container model
   * @param kv        携带额外参数一般用不上
   */
  @Override
  public String checkInUse(Container container, Kv kv) {
    //这里用来覆盖 检测是否被其它表引用
    return null;
  }

  /**
   * toggle操作执行后的回调处理
   */
  @Override
  protected String afterToggleBoolean(Container container, String column, Kv kv) {
    //addUpdateSystemLog(container.getIAutoId(), JBoltUserKit.getUserId(), container.getName(),"的字段["+column+"]值:"+container.get(column));
    /*
     switch(column){
     case "isInner":
     break;
     case "isEnabled":
     break;
     }
     */
    return null;
  }

  /**
   * 分页查询
   */
  public Object paginateAdminDatas(Integer pageNumber, Integer pageSize, Kv kv) {
    return dbTemplate("container.paginateAdminDatas", kv).paginate(pageNumber, pageSize);
  }

  public Ret deleted(Long id) {
    return toggleBoolean(id, "isDeleted");
  }

  //删除（逻辑）
  public Ret deleteByBatchIds(String ids) {
    update("UPDATE Bd_Container SET isDeleted = 1 WHERE iAutoId IN (" + ids + ") ");
    return SUCCESS;
  }

    /**
     * 数据导入
     */
    public Ret importExcelData(File file) {
        List<Record> datas = CusFieldsMappingdCache.ME.getImportRecordsByTableName(file, table());
        ValidationUtils.notEmpty(datas, "导入记录不能为空");

        tx(() -> {
            int iseq = 1;

            // 封装数据
            for (Record data : datas) {
                // 基本信息校验
                ValidationUtils.notNull(data.get("ccontainercode"), "第" + iseq + "行【容器编码】不能为空！");
                ValidationUtils.notNull(data.get("ccontainername"), "第" + iseq + "行【容器名称】不能为空！");
                ValidationUtils.notNull(data.get("icontainerclassid"), "第" + iseq + "行【容器类型】不能为空！");
                ValidationUtils.notNull(data.get("idepid"), "第" + iseq + "行【部门】不能为空！");
                ValidationUtils.notNull(data.get("iwarehouseid"), "第" + iseq + "行【所属仓库】不能为空！");
                ValidationUtils.notNull(data.get("isinner"), "第" + iseq + "行【存放地点】不能为空！");
                ValidationUtils.notNull(data.get("ilength"), "第" + iseq + "行【长(mm)】不能为空！");
                ValidationUtils.notNull(data.get("iwidth"), "第" + iseq + "行【宽(mm)】不能为空！");
                ValidationUtils.notNull(data.get("iheight"), "第" + iseq + "行【高(mm)】不能为空！");
                ValidationUtils.notNull(data.get("isenabled"), "第" + iseq + "行【是否启用】不能为空！");

                // 唯一校验
                Integer recordCodes = dbTemplate("container.uniqueCheck", Kv.by("ccontainercode", data.get("ccontainercode"))).queryInt();
                if (recordCodes > 0) {
                    ValidationUtils.error("第" + iseq + "行【容器编码】已存在，请修改后保存！");
                }

                Integer recordNames = dbTemplate("container.uniqueCheck", Kv.by("ccontainercode", data.get("ccontainercode"))).queryInt();
                if (recordNames > 0) {
                    ValidationUtils.error("第" + iseq + "行【容器名称】已存在，请修改后保存！");
                }

                //数据检验是否正确
                Record containerClass = dbTemplate("container.getContainerClassByName", Kv.by("name", data.get("icontainerclassid"))).findFirst();
                ValidationUtils.notNull(containerClass, "第" + iseq + "行【容器类型】未找到对应的容器类型数据！");

                Record department = dbTemplate("department.getDepartmentByName", Kv.by("name", data.get("idepid"))).findFirst();
                ValidationUtils.notNull(department, "第" + iseq + "行【部门】未找到对应的部门档案数据！");

                Warehouse warehouse = warehouseService.findByWhName(data.getStr("iwarehouseid"));
                ValidationUtils.notNull(warehouse, "第" + iseq + "行【所属仓库】未找到对应的仓库档案数据！");

                if (!"社内".equals(data.getStr("isinner")) && !"社外".equals(data.getStr("isinner"))) {
                    ValidationUtils.error("第" + iseq + "行【存放地点】数据不正确，只能填写【社内】或【社外】");
                }
                boolean isinner = "社内".equals(data.getStr("isinner"));

                if (!"是".equals(data.getStr("isenabled")) && !"否".equals(data.getStr("isenabled"))) {
                    ValidationUtils.error("第" + iseq + "行【是否启用】数据不正确，只能填写【是】或【否】");
                }
                boolean isenabled = "是".equals(data.getStr("isenabled"));

                ValidationUtils.isNumber(data.getStr("ilength"), "第" + iseq + "行【长(mm)】不是数字类型！");
                ValidationUtils.isNumber(data.getStr("iwidth"), "第" + iseq + "行【宽(mm)】不是数字类型！");
                ValidationUtils.isNumber(data.getStr("iheight"), "第" + iseq + "行【高(mm)】不是数字类型！");

                Container container = new Container();
                //组织数据
                container.setIOrgId(getOrgId());
                container.setCOrgCode(getOrgCode());
                container.setCOrgName(getOrgName());

                //创建人
                container.setICreateBy(JBoltUserKit.getUserId());
                container.setCCreateName(JBoltUserKit.getUserName());
                container.setDCreateTime(new Date());

                //更新人
                container.setIUpdateBy(JBoltUserKit.getUserId());
                container.setCUpdateName(JBoltUserKit.getUserName());
                container.setDUpdateTime(new Date());

                //是否删除，是否启用,数据来源
                container.setIsDeleted(false);
                container.setIsEnabled(true);

                if (data.get("icustomerid") != null) {
                    Record record = dbTemplate("customer.getCustomerByName", Kv.by("icustomername", data.getStr("icustomerid"))).findFirst();
                    ValidationUtils.notNull(record, "第" + iseq + "行【客户名称】未找到对应的客户档案数据！");
                    container.setICustomerId(record.getLong("iautoid"));
                }

                container.setCContainerCode(data.getStr("ccontainercode"));
                container.setCContainerName(data.getStr("ccontainername"));
                container.setIContainerClassId(containerClass.getLong("iautoid"));
                container.setIDepId(department.getLong("iautoid"));
                container.setIWarehouseId(warehouse.getIAutoId());
                container.setIsInner(isinner);
                container.setILength(data.getBigDecimal("ilength"));
                container.setIWidth(data.getBigDecimal("iwidth"));
                container.setIHeight(data.getBigDecimal("iheight"));
                container.setCVolume(data.getStr("cvolume"));
                container.setCCommonModel(data.getStr("ccommonmodel"));
                container.setIsEnabled(isenabled);

                ValidationUtils.isTrue(container.save(), "第" + iseq + "行保存数据失败");

                iseq++;
            }

            return true;
        });

        return SUCCESS;
    }

  public List<Record> list(Kv kv) {
    return dbTemplate("container.paginateAdminDatas", kv).find();
  }

  /**
   * 出入库处理
   *
   * @param jBoltTable 表格数据
   * @param mark       记号
   * @return
   */
  public Ret handleData(JBoltTable jBoltTable, String mark) {
    //参数判空
    if (jBoltTable == null || jBoltTable.isBlank()) {
      return fail(JBoltMsg.JBOLTTABLE_IS_BLANK);
    }
    //记号判空
    if (mark == null || StrUtil.isBlank(mark)) {
      return fail(JBoltMsg.PARAM_ERROR);
    }

    //转换javabean
    List<Container> containers = jBoltTable.getSaveModelList(Container.class);
    //将集合中的id抽取成id集合
    Long[] ids = containers.stream().map(Container::getIAutoId).toArray(Long[]::new);

    //记号Mark ——入库：1 出库：0
    if ("1".equals(mark)) {
      //入库
      //修改容器存放地点
      updateInnerByCRk(ids, "0");
      for (Container container : containers) {
        //入库主表处理
        ContainerStockInM stockInM = new ContainerStockInM();
        stockInM.setCMemo(container.getCMemo());
        containerStockInMService.save(stockInM);
        //入库明细表处理
        ContainerStockInD stockInD = new ContainerStockInD();
        stockInD.setCMemo(container.getCMemo());
        stockInD.setIContainerStockInMid(stockInM.getIAutoId());
        stockInD.setIContainerId(container.getIAutoId());
        containerStockInDService.save(stockInD);
      }
    } else {
      //出库
      //修改容器存放地点
      updateInnerByCRk(ids, "1");
      for (Container container : containers) {
        //出库主表处理
        ContainerStockOutM stockOutM = new ContainerStockOutM();
        stockOutM.setCMemo(container.getCMemo());
        stockOutMService.save(stockOutM);
        //出库明细表处理
        ContainerStockOutD stockOutD = new ContainerStockOutD();
        stockOutD.setCMemo(container.getCMemo());
        stockOutD.setIContainerId(container.getIAutoId());
        stockOutD.setIContainerStockOutMid(stockOutM.getIAutoId());
        stockOutDService.save(stockOutD);
      }
    }
    return SUCCESS;
  }

  /**
   * @param ids     容器id
   * @param isInner 1：出库，0：入库
   */
  private void updateInnerByCRk(Long[] ids, String isInner) {
    update("UPDATE Bd_Container SET isInner =" + isInner + " WHERE iAutoId IN (" + ArrayUtil.join(ids, COMMA) + ")");
  }


  public List<Container> options() {
    return find(selectSql().eq("IsDeleted", "0").eq("isEnabled", "1"));
  }

  /**
   * 打印数据
   *
   * @param kv 参数
   */
  public Object getPrintDataCheck(Kv kv) {
    return dbTemplate("container.containerPrintData", kv).find();
  }

  /**
   * 从系统导入字段配置，获得导入的数据
   */
  public Ret importExcelClass(File file) {
      List<Record> records = CusFieldsMappingdCache.ME.getImportRecordsByTableName(file, table());
      if (notOk(records)) {
          return fail(JBoltMsg.DATA_IMPORT_FAIL_EMPTY);
      }

      Date now = new Date();
      
    for (Record record : records) {

      if (StrUtil.isBlank(record.getStr("iContainerClassId"))) {
        return fail("容器类型不能为空");
      }
      if (StrUtil.isBlank(record.getStr("cContainerCode"))) {
        return fail("容器编码不能为空");
      }
      if (StrUtil.isBlank(record.getStr("cContainerName"))) {
        return fail("容器名称不能为空");
      }
      if (StrUtil.isBlank(record.getStr("iDepId"))) {
        return fail("管理部门不能为空");
      }
      if (StrUtil.isBlank(record.getStr("iWarehouseId"))) {
        return fail("管理仓库不能为空");
      }
      if (StrUtil.isBlank(record.getStr("isInner"))) {
        return fail("存放地点不能为空");
      }
      if (StrUtil.isBlank(record.getStr("iLength"))) {
        return fail("长不能为空");
      }
      if (StrUtil.isBlank(record.getStr("iWidth"))) {
        return fail("宽不能为空");
      }
      if (StrUtil.isBlank(record.getStr("iHeight"))) {
        return fail("高不能为空");
      }

      record.set("iAutoId", JBoltSnowflakeKit.me.nextId());
      record.set("iOrgId", getOrgId());
      record.set("cOrgCode", getOrgCode());
      record.set("cOrgName", getOrgName());
      record.set("iCreateBy", JBoltUserKit.getUserId());
      record.set("dCreateTime", now);
      record.set("cCreateName", JBoltUserKit.getUserName());
      record.set("isEnabled", 1);
      record.set("isDeleted", 0);
      record.set("iUpdateBy", JBoltUserKit.getUserId());
      record.set("dUpdateTime", now);
      record.set("cUpdateName", JBoltUserKit.getUserName());

    }

    // 执行批量操作
    tx(() -> {
      batchSaveRecords(records);
      return true;
    });
    return SUCCESS;
  }

  public Container getContainerCode() {
    Container container = new Container();
    container.setCContainerCode(BillNoUtils.genCode(getOrgCode(), table()));
    return container;
  }
}
