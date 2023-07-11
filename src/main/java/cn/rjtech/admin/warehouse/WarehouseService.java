package cn.rjtech.admin.warehouse;

import cn.hutool.core.util.StrUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.poi.excel.JBoltExcel;
import cn.jbolt.core.poi.excel.JBoltExcelHeader;
import cn.jbolt.core.poi.excel.JBoltExcelSheet;
import cn.jbolt.core.poi.excel.JBoltExcelUtil;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.cusfieldsmappingd.CusFieldsMappingDService;
import cn.rjtech.admin.department.DepartmentService;
import cn.rjtech.admin.person.PersonService;
import cn.rjtech.enums.SourceEnum;
import cn.rjtech.model.momdata.Department;
import cn.rjtech.model.momdata.Person;
import cn.rjtech.model.momdata.VendorAddr;
import cn.rjtech.model.momdata.Warehouse;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.io.File;
import java.math.BigDecimal;
import java.util.*;

/**
 * 仓库建模-仓库档案
 *
 * @ClassName: WarehouseService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-20 16:47
 */
public class WarehouseService extends BaseService<Warehouse> {

  private final Warehouse dao = new Warehouse().dao();

  @Inject
  private CusFieldsMappingDService cusFieldsMappingDService;

  @Inject
  private DepartmentService departmentService;

  @Inject
  private CusFieldsMappingDService cusFieldsMappingdService;

  @Inject
  private PersonService personService;

  @Override
  protected Warehouse dao() {
    return dao;
  }

  /**
   * 设置返回二开业务所属的关键systemLog的targetType
   */
  @Override
  protected int systemLogTargetType() {
    return ProjectSystemLogTargetType.NONE.getValue();
  }


  /**
   * 后台管理数据查询
   *
   * @param pageNumber        第几页
   * @param pageSize          每页几条数据
   * @param keywords          关键词
   * @param bFreeze           是否冻结
   * @param bmrp              是否参与MRP运算
   * @param bShop             是否门店
   * @param bInCost           记入成本
   * @param bInAvailCalcu     纳入可用量计算
   * @param bProxyWh          代管仓
   * @param bBondedWh         是否保税仓
   * @param bWhAsset          资产仓
   * @param bCheckSubitemCost 是否核算分项成本
   * @param bEB               电商仓
   * @param isDeleted         删除状态: 0. 未删除 1. 已删除
   */
  public Page<Warehouse> getAdminDatas(int pageNumber, int pageSize, String keywords, Boolean bFreeze, Boolean bmrp, Boolean bShop, Boolean bInCost, Boolean bInAvailCalcu, Boolean bProxyWh, Boolean bBondedWh, Boolean bWhAsset, Boolean bCheckSubitemCost, Boolean bEB, Boolean isDeleted) {
    //创建sql对象
    Sql sql = selectSql().page(pageNumber, pageSize);
    //sql条件处理
    sql.eqBooleanToChar("bFreeze", bFreeze);
    sql.eqBooleanToChar("bMRP", bmrp);
    sql.eqBooleanToChar("bShop", bShop);
    sql.eqBooleanToChar("bInCost", bInCost);
    sql.eqBooleanToChar("bInAvailCalcu", bInAvailCalcu);
    sql.eqBooleanToChar("bProxyWh", bProxyWh);
    sql.eqBooleanToChar("bBondedWh", bBondedWh);
    sql.eqBooleanToChar("bWhAsset", bWhAsset);
    sql.eqBooleanToChar("bCheckSubitemCost", bCheckSubitemCost);
    sql.eqBooleanToChar("bEB", bEB);
    sql.eqBooleanToChar("isDeleted", isDeleted);
    //关键词模糊查询
    sql.likeMulti(keywords, "cOrgName", "cWhName");
    //排序
    sql.desc("iAutoId");
    return paginate(sql);
  }

  /**
   * 保存
   */
  public Ret save(Warehouse warehouse) {
    if (warehouse == null || isOk(warehouse.getIAutoId())) {
      return fail(JBoltMsg.PARAM_ERROR);
    }

    Integer isRepetition1 = dbTemplate("warehouse.verifyDuplication", Kv.by("cwhcode", warehouse.getCWhCode())).queryInt();
    if (isRepetition1 >= 1) {
      ValidationUtils.error("【仓库编码】" + warehouse.getCWhCode() + "已存在，请修改后保存");
    }

    Integer isRepetition2 = dbTemplate("warehouse.verifyDuplication", Kv.by("cwhname", warehouse.getCWhName())).queryInt();
    if (isRepetition2 >= 1) {
      ValidationUtils.error("【仓库名称】" + warehouse.getCWhName() + "已存在，请修改后保存");
    }

    //创建信息
    warehouse.setIcreateby(JBoltUserKit.getUserId());
    warehouse.setCcreatename(JBoltUserKit.getUserName());
    warehouse.setDCreateTime(new Date());

    //更新信息
    warehouse.setIupdateby(JBoltUserKit.getUserId());
    warehouse.setCupdatename(JBoltUserKit.getUserName());
    warehouse.setDupdatetime(new Date());

    warehouse.setCOrgCode(getOrgCode());
    warehouse.setCOrgName(getOrgName());
    warehouse.setIOrgId(getOrgId());
    warehouse.setISource(SourceEnum.MES.getValue());
    //默认未删除
    warehouse.setIsDeleted(false);
    boolean success = warehouse.save();

    if (success) {
      //添加日志
      //addSaveSystemLog(warehouse.getIAutoId(), JBoltUserKit.getUserId(), warehouse.getName())
    }
    return ret(success);
  }

  /**
   * 更新
   */
  public Ret update(Warehouse warehouse) {
    if (warehouse == null || notOk(warehouse.getIAutoId())) {
      return fail(JBoltMsg.PARAM_ERROR);
    }
    //更新时需要判断数据存在
    Warehouse dbWarehouse = findById(warehouse.getIAutoId());
    if (dbWarehouse == null) {
      return fail(JBoltMsg.DATA_NOT_EXIST);
    }

    Integer isRepetition1 = dbTemplate("warehouse.verifyDuplication", Kv.by("cwhcode", warehouse.getCWhCode()).
        set("iautoid", warehouse.getIAutoId())).queryInt();
    if (isRepetition1 >= 1) {
      ValidationUtils.error("【仓库编码】" + warehouse.getCWhCode() + "已存在，请修改后保存");
    }

    Integer isRepetition2 = dbTemplate("warehouse.verifyDuplication", Kv.by("cwhname", warehouse.getCWhName()).
        set("iautoid", warehouse.getIAutoId())).queryInt();
    if (isRepetition2 >= 1) {
      ValidationUtils.error("【仓库名称】" + warehouse.getCWhName() + "已存在，请修改后保存");
    }

    //更新信息
    warehouse.setIupdateby(JBoltUserKit.getUserId());
    warehouse.setCupdatename(JBoltUserKit.getUserName());
    warehouse.setDupdatetime(new Date());
    boolean success = warehouse.update();
    if (success) {
      //添加日志
    }
    return ret(success);
  }

  /**
   * 删除数据后执行的回调
   *
   * @param warehouse 要删除的model
   * @param kv        携带额外参数一般用不上
   */
  @Override
  protected String afterDelete(Warehouse warehouse, Kv kv) {
    //addDeleteSystemLog(warehouse.getIAutoId(), JBoltUserKit.getUserId(),warehouse.getName())
    return null;
  }

  /**
   * 检测是否可以删除
   *
   * @param warehouse model
   * @param kv        携带额外参数一般用不上
   */
  @Override
  public String checkInUse(Warehouse warehouse, Kv kv) {
    //这里用来覆盖 检测是否被其它表引用
    return null;
  }

  /**
   * toggle操作执行后的回调处理
   */
  @Override
  protected String afterToggleBoolean(Warehouse warehouse, String column, Kv kv) {
    //addUpdateSystemLog(warehouse.getIAutoId(), JBoltUserKit.getUserId(), warehouse.getName(),"的字段["+column+"]值:"+warehouse.get(column))
    return null;
  }

  //查询有没有这个仓库编码
  public Warehouse findByWhCode(String whcode) {
    return findFirst(Okv.by("cWhCode", whcode).set("isDeleted", false), "iautoid", "asc");
  }

  /**
   * 后台管理分页查询
   */
  public Page<Record> paginateAdminDatas(int pageNumber, int pageSize, Kv kv) {
    kv.set("iorgid", getOrgId());
    Page<Record> paginate = dbTemplate("warehouse.paginateAdminDatas", kv).paginate(pageNumber, pageSize);
    return paginate;
  }

  public Object dataList() {
    Okv kv = Okv.by("isDeleted", ZERO_STR);
    return getCommonList(kv, "iAutoId", "asc");
  }

  public List<Record> list(Kv kv) {
    Long userDeptId = JBoltUserKit.getUserDeptId();
    kv.setIfNotNull("iorgId", userDeptId);
    return dbTemplate("warehouse.paginateAdminDatas", kv).find();
  }

  public List<Record> findByWarehouse() {
    return dbTemplate("warehouse.findByWarehouse", Kv.by("orgId", getOrgId())).find();
  }

  public List<Record> options() {
    return dbTemplate("warehouse.options", Kv.of("isenabled", "true")).find();
  }

  /**
   * 批量删除（逻辑）
   */
  public Ret deleteByBatchIds(String ids) {
    update("UPDATE Bd_Warehouse SET isDeleted = '1' WHERE iAutoId IN (" + ids + ") ");
    return SUCCESS;
  }

  public Warehouse findByWhName(String cwhname) {
    Sql sql = selectSql()
        .eq(Warehouse.CWHNAME, cwhname)
        .eq(Warehouse.IORGID, getOrgId())
        .eq(Warehouse.ISDELETED, ZERO_STR);

    return findFirst(sql);
  }

  public List<Warehouse> findListByWhName(String cwhname) {
    Sql sql = selectSql()
        .eq(Warehouse.CWHNAME, cwhname)
        .eq(Warehouse.IORGID, getOrgId())
        .eq(Warehouse.ISDELETED, ZERO_STR);

    return find(sql);
  }

  public Warehouse findByCwhcode(String cwhcode) {
    return findFirst("SELECT * FROM Bd_Warehouse WHERE cwhcode = ? AND corgcode = ? AND isDeleted = ? ", cwhcode, getOrgCode(), ZERO_STR);
  }

  /**
   * 根据仓库id获得对应的数据
   *
   * @param id
   * @return
   */
  public Warehouse findById(Long id) {
    return dao.findById(id);
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
        ValidationUtils.notNull(data.get("cwhcode"), "第" + iseq + "行的【仓库编码】不能为空！");
        ValidationUtils.notNull(data.get("cwhname"), "第" + iseq + "行的【仓库名称】不能为空！");
        ValidationUtils.notNull(data.get("cdepcode"), "第" + iseq + "行的【所属部门名称】不能为空");
        ValidationUtils.notNull(data.get("isspacecontrolenabled"), "第" + iseq + "行的【是否启用空间掌控】不能为空！");
        ValidationUtils.notNull(data.get("isstockwarnenabled"), "第" + iseq + "行的【启用库存预警】不能为空！");
        ValidationUtils.notNull(data.get("isreservoirarea"), "第" + iseq + "行的【启用库区】不能为空！");

        // 查重
        ValidationUtils.assertNull(findByWhCode(data.get("cwhcode") + ""), "第" + iseq + "行的【仓库编码】已存在！");
        ValidationUtils.assertNull(findByWhName(data.get("cwhname") + ""), "第" + iseq + "行的【仓库名称】已存在！");
        Department dept = departmentService.findByCdepName(data.get("cdepcode") + "");
        ValidationUtils.notNull(dept, "第" + iseq + "行的【所属部门名称】未找到对应的部门档案数据！");
        if (data.get("imaxstock") != null || data.get("imaxspace") != null) {
          if ((data.get("isspacecontrolenabled") + "").equals("否")) {
            ValidationUtils.error("第" + iseq + "行的【是否启用空间掌控】为否时，【最大存储数量】【最大存储空间】必须为空！");
          }
        } else {
          if ((data.get("isspacecontrolenabled") + "").equals("是")) {
            ValidationUtils.error("第" + iseq + "行的【是否启用空间掌控】为是时，【最大存储数量】【最大存储空间】不能为空！");
          }
        }

        //仓库数据保存
        Warehouse warehouse = new Warehouse();
        //组织数据
        warehouse.setIOrgId(getOrgId());
        warehouse.setCOrgCode(getOrgCode());
        warehouse.setCOrgName(getOrgName());

        //创建人
        warehouse.setIcreateby(JBoltUserKit.getUserId());
        warehouse.setCcreatename(JBoltUserKit.getUserName());
        warehouse.setDCreateTime(new Date());

        //更新人
        warehouse.setIupdateby(JBoltUserKit.getUserId());
        warehouse.setCupdatename(JBoltUserKit.getUserName());
        warehouse.setDupdatetime(new Date());

        //是否删除，是否启用,数据来源
        warehouse.setIsDeleted(false);
        warehouse.setIsEnabled(true);
        warehouse.setISource(1);

        //数据添加
        warehouse.setCWhCode(data.get("cwhcode") + "");
        warehouse.setCWhName(data.get("cwhname") + "");
        warehouse.setCDepCode(dept.getCDepCode());
        if (data.get("cwhperson") != null) {
          Person person = personService.findFirstByCpersonName(data.get("cwhperson") + "");
          if (person == null) {
            ValidationUtils.error("第" + iseq + "行的【负责人】未找到对应的人员档案数据！");
          } else {
            warehouse.setCWhPerson(person.getCpsnNum());
          }
        }
        if (data.get("imaxstock") != null) {
          warehouse.setIMaxStock(BigDecimal.valueOf(Integer.parseInt(data.get("imaxstock") + "")));
        }
        if (data.get("imaxspace") != null) {
          warehouse.setImaxspace(BigDecimal.valueOf(Integer.parseInt(data.get("imaxspace") + "")));
        }
        //空间掌控
        boolean isspacecontrolenabled = (data.get("isspacecontrolenabled") + "").equals("是") ? true : false;
        //库存预警
        boolean isstockwarnenabled = (data.get("isstockwarnenabled") + "").equals("是") ? true : false;
        warehouse.setIsSpaceControlEnabled(isspacecontrolenabled);
        warehouse.setIsStockWarnEnabled(isstockwarnenabled);
        warehouse.setCWhAddress(data.get("cwhaddress") + "");
        warehouse.setIsReservoirAread(true);
        warehouse.setCWhMemo(data.get("cwhmemo") + "");

        ValidationUtils.isTrue(warehouse.save(), "第" + iseq + "行保存数据失败");
        iseq++;
      }

      return true;
    });

    ValidationUtils.assertBlank(msg.toString(), msg + ",其他数据已处理");
    return SUCCESS;
  }

  public List<Warehouse> findByIds(List<Long> ids){
    Sql sql = selectSql().in(Warehouse.IAUTOID, ids);
    return find(sql);
  }
}
