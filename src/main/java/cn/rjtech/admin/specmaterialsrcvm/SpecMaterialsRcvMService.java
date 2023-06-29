package cn.rjtech.admin.specmaterialsrcvm;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.SpecMaterialsRcvM;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;

/**
 * 制造工单-特殊领料单主表 Service
 *
 * @ClassName: SpecMaterialsRcvMService
 * @author: RJ
 * @date: 2023-04-24 10:24
 */
public class SpecMaterialsRcvMService extends BaseService<SpecMaterialsRcvM> {

  private final SpecMaterialsRcvM dao = new SpecMaterialsRcvM().dao();

  @Override
  protected SpecMaterialsRcvM dao() {
    return dao;
  }

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
}