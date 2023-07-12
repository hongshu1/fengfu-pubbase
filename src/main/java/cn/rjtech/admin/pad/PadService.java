package cn.rjtech.admin.pad;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ArrayUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.padworkregion.PadWorkRegionService;
import cn.rjtech.model.momdata.Pad;
import cn.rjtech.model.momdata.PadWorkRegion;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 系统配置-平板端设置
 *
 * @ClassName: PadService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-03 17:17
 */
public class PadService extends BaseService<Pad> {
  private final Pad dao = new Pad().dao();
  @Inject
  private PadWorkRegionService padWorkRegionService;

  @Override
  protected Pad dao() {
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
   * @return
   */
  public Page<Record> getAdminDatas(int pageNumber, int pageSize, Kv kv) {
    Page<Record> page = dbTemplate("pad.list", kv).paginate(pageNumber, pageSize);
//    List<String> strings=new ArrayList<>();
//    page.getList().forEach(record -> {
//      strings.add(record.getStr("iautoid"));
//    });
    return page;
  }

  /**
   * 保存
   *
   * @param pad
   * @return
   */
  public Ret save(Pad pad) {
    if (pad == null || isOk(pad.getIAutoId())) {
      return fail(JBoltMsg.PARAM_ERROR);
    }
    Integer code = dbTemplate("pad.uniqueCheck", Kv.by("code", pad.getCPadCode())).queryInt();
    if (code > 0) {
      ValidationUtils.error("【平板编码】已存在，请修改后保存");
    }
    Integer mac = dbTemplate("pad.uniqueCheck", Kv.by("mac", pad.getCMac())).queryInt();
    if (mac > 0) {
      ValidationUtils.error("【MAC地址】已存在，请修改后保存");
    }
    boolean success = pad.save();
    if (success) {
      //添加日志
      //addSaveSystemLog(pad.getIAutoId(), JBoltUserKit.getUserId(), pad.getName());
    }
    return ret(success);
  }

  /**
   * 更新
   *
   * @param pad
   * @return
   */
  public Ret update(Pad pad) {
    if (pad == null || notOk(pad.getIAutoId())) {
      return fail(JBoltMsg.PARAM_ERROR);
    }
    //更新时需要判断数据存在
    Integer code = dbTemplate("pad.uniqueCheck", Kv.by("code", pad.getCPadCode()).set("iautoid", pad.getIAutoId())).queryInt();
    if (code > 0) {
      ValidationUtils.error("【平板编码】已存在，请修改后保存");
    }
    Integer mac = dbTemplate("pad.uniqueCheck", Kv.by("mac", pad.getCMac()).set("iautoid", pad.getIAutoId())).queryInt();
    if (mac > 0) {
      ValidationUtils.error("【MAC地址】已存在，请修改后保存");
    }
    //if(existsName(pad.getName(), pad.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
    boolean success = pad.update();
    if (success) {
      //添加日志
      //addUpdateSystemLog(pad.getIAutoId(), JBoltUserKit.getUserId(), pad.getName());
    }
    return ret(success);
  }

  /**
   * 删除数据后执行的回调
   *
   * @param pad 要删除的model
   * @param kv  携带额外参数一般用不上
   * @return
   */
  @Override
  protected String afterDelete(Pad pad, Kv kv) {
    //addDeleteSystemLog(pad.getIAutoId(), JBoltUserKit.getUserId(),pad.getName());
    return null;
  }

  /**
   * 检测是否可以删除
   *
   * @param pad model
   * @param kv  携带额外参数一般用不上
   * @return
   */
  @Override
  public String checkInUse(Pad pad, Kv kv) {
    //这里用来覆盖 检测是否被其它表引用
    return null;
  }

  /**
   * 设置参数
   *
   * @param pad
   * @return
   */
  private Pad setPad(Pad pad) {
    pad.setCOrgCode(getOrgCode());
    pad.setCOrgName(getOrgName());
    pad.setIOrgId(getOrgId());
    pad.setIsDeleted(false);
    Long userId = JBoltUserKit.getUserId();
    pad.setICreateBy(userId);
    pad.setIUpdateBy(userId);
    String userName = JBoltUserKit.getUserName();
    pad.setCCreateName(userName);
    pad.setCUpdateName(userName);
    Date date = new Date();
    pad.setDCreateTime(date);
    pad.setDUpdateTime(date);
    return pad;
  }

  public Ret saveForm(Pad pad, JBoltTable jBoltTable) {
    AtomicReference<Ret> res = new AtomicReference<>();
    res.set(SUCCESS);
    tx(() -> {
      int qty = 0;
      setPad(pad);
      pad.setIsEnabled(true);
      pad.setIsDeleted(false);
      Ret inventoryRet = save(pad);
      if (inventoryRet.isFail()) {
        res.set(inventoryRet);
        return false;
      }
      List<PadWorkRegion> saveBeanList = jBoltTable.getSaveBeanList(PadWorkRegion.class);
      if (saveBeanList != null && saveBeanList.size() > 0) {
        for (PadWorkRegion region : saveBeanList) {
          region.setIPadId(pad.getIAutoId());
          region.setIsDeleted(false);
          if (region.getIsDefault()) {
            qty++;
          }
          region.save();
        }
      }
      if (qty > 1) {
        ValidationUtils.error("所属生产线列表的【是否默认】只能存在一个是");
      }
      return true;
    });
    return res.get();
  }

  public Ret updateForm(Pad pad, JBoltTable jBoltTable) {
    AtomicReference<Ret> res = new AtomicReference<>();
    res.set(SUCCESS);
    tx(() -> {
      update(pad);
      int qty = 0;
      List<PadWorkRegion> saveBeanList = jBoltTable.getSaveBeanList(PadWorkRegion.class);
      if (saveBeanList != null) {
        for (PadWorkRegion padWorkRegion : saveBeanList) {
          padWorkRegion.setIPadId(pad.getIAutoId());
          padWorkRegion.setIsDeleted(false);
          if (padWorkRegion.getIsDefault()) {
            qty++;
          }
          padWorkRegion.save();
        }
      }

      List<PadWorkRegion> updateBeanList = jBoltTable.getUpdateBeanList(PadWorkRegion.class);
      if (updateBeanList != null) {
        for (PadWorkRegion padWorkRegion : updateBeanList) {
          if (padWorkRegion.getIsDefault()) {
            qty++;
          }
          padWorkRegion.update();
        }
      }
      //      if (saveBeanList != null && saveBeanList.size() > 0) {
//        for (int i = 0; i < saveBeanList.size(); i++) {
//          saveBeanList.get(i).setIPadId(pad.getIAutoId());
//          saveBeanList.get(i).setIsDeleted(false);
//          if (saveBeanList.get(i).getIsDefault()) {
//            qty++;
//          }
//        }
//        padWorkRegionService.batchSave(saveBeanList);
//      }

//      if (updateBeanList != null && updateBeanList.size() > 0) {
//        for (int i = 0; i < updateBeanList.size(); i++) {
//          updateBeanList.get(i).setIPadId(pad.getIAutoId());
//          if (updateBeanList.get(i).getIsDefault()) {
//            qty++;
//          }
//        }
//        padWorkRegionService.batchUpdate(updateBeanList);
//      }
      if (qty > 1) {
        ValidationUtils.error("所属生产线列表的【是否默认】只能存在一个是");
      }
      Object[] delete = jBoltTable.getDelete();
      // 删除
      if (ArrayUtil.isNotEmpty(delete)) {
        deleteMultiByIds(delete);
      }
      return true;
    });

    return res.get();
  }

  public void deleteMultiByIds(Object[] deletes) {
    delete("DELETE FROM Bd_PadWorkRegion WHERE iAutoId IN (" + ArrayUtil.join(deletes, ",") + ") ");
  }

  /**
   * 根据mac地址获取对应的产线
   *
   * @param cmac 平板mac地址
   * @return
   */
  public Record getPadWorkRegionByCmac(String cmac) {
    return dbTemplate("pad.getPadWorkRegionByCmac", Kv.by("cmac", cmac)).findFirst();
  }

  public Ret deleteByid(Long id) {
    Pad pad = findById(id);
    pad.setIsDeleted(true);
    pad.setIUpdateBy(JBoltUserKit.getUserId());
    pad.setCUpdateName(JBoltUserKit.getUserName());
    pad.setDUpdateTime(new DateTime());
    boolean success = pad.update();
    return ret(success);
  }

  /**
   * 根据mac地址获取平板配置
   *
   * @param mac 平板mac地址
   * @return
   */
  public Record getPadByMac(String mac) {
    return dbTemplate("pad.list", Kv.by("mac", mac)).findFirst();
  }
}