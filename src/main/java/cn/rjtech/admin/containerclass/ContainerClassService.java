package cn.rjtech.admin.containerclass;

import cn.hutool.core.date.DateUtil;
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
import cn.rjtech.cache.CusFieldsMappingdCache;
import cn.rjtech.model.momdata.ContainerClass;
import cn.rjtech.util.BillNoUtils;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * 分类管理
 *
 * @ClassName: ContainerClassService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-22 16:16
 */
public class ContainerClassService extends BaseService<ContainerClass> {
    
  private final ContainerClass dao = new ContainerClass().dao();

  @Override
  protected ContainerClass dao() {
    return dao;
  }

  @Override
  protected int systemLogTargetType() {
    return ProjectSystemLogTargetType.NONE.getValue();
  }

  /**
   * 后台管理数据查询
   *
   * @param pageNumber 第几页
   * @param pageSize   每页几条数据
   */
  public Page<Record> getAdminDatas(int pageNumber, int pageSize) {
    return dbTemplate("containerclass.paginateAdminDatas").paginate(pageNumber, pageSize);
  }

  /**
   * 保存
   */
  public Ret save(ContainerClass containerClass) {
    if (containerClass == null || isOk(containerClass.getIAutoId())) {
      return fail(JBoltMsg.PARAM_ERROR);
    }

    ValidationUtils.assertNull(findByConClassCode(containerClass.getCContainerClassCode()), "分类编码重复！");
    //if(existsName(containerClass.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
    //创建信息
    containerClass.setICreateBy(JBoltUserKit.getUserId());
    containerClass.setCCreateName(JBoltUserKit.getUserName());
    containerClass.setDCreateTime(new Date());

    //更新信息
    containerClass.setIUpdateBy(JBoltUserKit.getUserId());
    containerClass.setCUpdateName(JBoltUserKit.getUserName());
    containerClass.setDUpdateTime(new Date());

    //组织信息
    containerClass.setCOrgCode(getOrgCode());
    containerClass.setCOrgName(getOrgName());
    containerClass.setIOrgId(getOrgId());
    boolean success = containerClass.save();
    if (success) {
      //添加日志
      //addSaveSystemLog(containerClass.getIAutoId(), JBoltUserKit.getUserId(), containerClass.getName());
    }
    return ret(success);
  }

  /**
   * 查找容器类别编码
   */
  public ContainerClass findByConClassCode(String cContainerClassCode) {
    return findFirst(Okv.by("cContainerClassCode", cContainerClassCode).set("isDeleted", false), "iautoid", "asc");

  }

  /**
   * 更新
   */
  public Ret update(ContainerClass containerClass) {
    if (containerClass == null || notOk(containerClass.getIAutoId())) {
      return fail(JBoltMsg.PARAM_ERROR);
    }
    //更新时需要判断数据存在
    ContainerClass dbContainerClass = findById(containerClass.getIAutoId());
    if (dbContainerClass == null) {
      return fail(JBoltMsg.DATA_NOT_EXIST);
    }


    containerClass.setIUpdateBy(JBoltUserKit.getUserId());
    containerClass.setCUpdateName(JBoltUserKit.getUserName());
    containerClass.setDUpdateTime(new Date());

    //查询
    ContainerClass byConClassCode = findByConClassCode(containerClass.getCContainerClassCode());

    //编码重复判断
    if (byConClassCode != null && !containerClass.getIAutoId().equals(byConClassCode.getIAutoId())) {
      ValidationUtils.assertNull(byConClassCode.getCContainerClassCode(), "分类编码重复！");
    }
    //if(existsName(containerClass.getName(), containerClass.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
    boolean success = containerClass.update();
    if (success) {
      //添加日志
      //addUpdateSystemLog(containerClass.getIAutoId(), JBoltUserKit.getUserId(), containerClass.getName());
    }
    return ret(success);
  }

  /**
   * 删除数据后执行的回调
   *
   * @param containerClass 要删除的model
   * @param kv             携带额外参数一般用不上
   */
  @Override
  protected String afterDelete(ContainerClass containerClass, Kv kv) {
    //addDeleteSystemLog(containerClass.getIAutoId(), JBoltUserKit.getUserId(),containerClass.getName());
    return null;
  }

  /**
   * 检测是否可以删除
   *
   * @param containerClass model
   * @param kv             携带额外参数一般用不上
   */
  @Override
  public String checkInUse(ContainerClass containerClass, Kv kv) {
    //这里用来覆盖 检测是否被其它表引用
    return null;
  }

  public Ret delete(String ids) {
    Integer qty = dbTemplate("containerclass.getContainerByDid", Kv.by("ids", ids)).queryInt();
    if (qty > 0) {
      ValidationUtils.error("数据已被容器档案引用，无法删除！");
    }
    String[] split = ids.split(",");
    boolean bol = true;
    for (String id : split) {
      ContainerClass containerClass = findById(id);
      containerClass.setIsDeleted(true);
      bol = containerClass.update();
    }
    return ret(bol);
  }

  /**
   * 容器分类导入
   */
  public Ret importExcelData(File file) {
    StringBuilder errorMsg = new StringBuilder();
    JBoltExcel jBoltExcel = JBoltExcel
        //从excel文件创建JBoltExcel实例
        .from(file)
        //设置工作表信息
        .setSheets(
            JBoltExcelSheet.create("sheet1")
                //设置列映射 顺序 标题名称
                .setHeaders(
                    JBoltExcelHeader.create("cContainerClassCode", "分类编码"),
                    JBoltExcelHeader.create("cContainerClassName", "分类名称"),
                    JBoltExcelHeader.create("isEnabled", "是否启用")
                )
                //特殊数据转换器
                .setDataChangeHandler((data, index) -> {
                  ValidationUtils.notNull(data.get("cContainerClassCode"), "分类编码为空！");
                  ValidationUtils.notNull(data.get("cContainerClassName"), "分类名称为空！");
                  ValidationUtils.notNull(data.get("isEnabled"), "是否启用为空！");
                  data.changeStrToBoolean("isEnabled", "是");

                  ValidationUtils.isTrue(findByConClassCode(data.getStr("cContainerClassCode")) == null, data.getStr("cContainerClassCode") + "编码重复");

                  //创建信息
                  data.change("iCreateBy", JBoltUserKit.getUserId());
                  data.change("cCreateName", JBoltUserKit.getUserName());
                  data.change("dCreateTime", DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));

                  //更新信息
                  data.change("iUpdateBy", JBoltUserKit.getUserId());
                  data.change("cUpdateName", JBoltUserKit.getUserName());
                  data.change("dUpdateTime", DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));

                  //组织信息
                  data.change("iOrgId", getOrgCode());
                  data.change("cOrgCode", getOrgCode());
                  data.change("cOrgName", getOrgName());
                  //删除默认 0：未删除
                  data.change("IsDeleted", 0);

                })
                //从第三行开始读取
                .setDataStartRow(3)
        );
    //从指定的sheet工作表里读取数据
    List<ContainerClass> models = JBoltExcelUtil.readModels(jBoltExcel, "sheet1", ContainerClass.class, errorMsg);
    if (notOk(models)) {
      if (errorMsg.length() > 0) {
        return fail(errorMsg.toString());
      } else {
        return fail(JBoltMsg.DATA_IMPORT_FAIL_EMPTY);
      }
    }
    //读取数据没有问题后判断必填字段
    if (models.size() > 0) {
      tx(() -> {
        batchSave(models);
        return true;
      });

    }
    return SUCCESS;
  }

  /**
   * 导出查询
   */
  public List<Record> list(Kv kv) {
    Sql sql = selectSql().le("IsDeleted", 0).desc("dCreateTime");

    return findRecord(sql);
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

          if (StrUtil.isBlank(record.getStr("cContainerClassCode"))) {
              return fail("分类编码不能为空");
          }
          if (StrUtil.isBlank(record.getStr("cContainerClassName"))) {
              return fail("分类名称不能为空");
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

    public ContainerClass getContainerClassCode() {
        ContainerClass containerClass = new ContainerClass();
        containerClass.setCContainerClassCode(BillNoUtils.genCode(getOrgCode(), table()));
        return containerClass;
    }
}