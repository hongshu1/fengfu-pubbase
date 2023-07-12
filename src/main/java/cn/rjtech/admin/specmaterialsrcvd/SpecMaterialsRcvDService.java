package cn.rjtech.admin.specmaterialsrcvd;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.SpecMaterialsRcvD;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;

/**
 * 制造工单-特殊领料单明细
 *
 * @ClassName: SpecMaterialsRcvDService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-06-30 14:46
 */
public class SpecMaterialsRcvDService extends BaseService<SpecMaterialsRcvD> {
  private final SpecMaterialsRcvD dao = new SpecMaterialsRcvD().dao();

  @Override
  protected SpecMaterialsRcvD dao() {
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
  public Page<SpecMaterialsRcvD> getAdminDatas(int pageNumber, int pageSize) {
    //创建sql对象
    Sql sql = selectSql().page(pageNumber, pageSize);
    //排序
    sql.desc("iAutoId");
    return paginate(sql);
  }

  /**
   * 保存
   *
   * @param specMaterialsRcvD
   * @return
   */
  public Ret save(SpecMaterialsRcvD specMaterialsRcvD) {
    if (specMaterialsRcvD == null || isOk(specMaterialsRcvD.getIAutoId())) {
      return fail(JBoltMsg.PARAM_ERROR);
    }
    boolean success = specMaterialsRcvD.save();
    if (success) {
      //添加日志
      //addSaveSystemLog(specMaterialsRcvD.getIAutoId(), JBoltUserKit.getUserId(), specMaterialsRcvD.getName());
    }
    return ret(success);
  }

  /**
   * 更新
   *
   * @param specMaterialsRcvD
   * @return
   */
  public Ret update(SpecMaterialsRcvD specMaterialsRcvD) {
    if (specMaterialsRcvD == null || notOk(specMaterialsRcvD.getIAutoId())) {
      return fail(JBoltMsg.PARAM_ERROR);
    }
    //更新时需要判断数据存在
    SpecMaterialsRcvD dbSpecMaterialsRcvD = findById(specMaterialsRcvD.getIAutoId());
    if (dbSpecMaterialsRcvD == null) {
      return fail(JBoltMsg.DATA_NOT_EXIST);
    }
    boolean success = specMaterialsRcvD.update();
    if (success) {
      //添加日志
      //addUpdateSystemLog(specMaterialsRcvD.getIAutoId(), JBoltUserKit.getUserId(), specMaterialsRcvD.getName());
    }
    return ret(success);
  }

  /**
   * 删除数据后执行的回调
   *
   * @param specMaterialsRcvD 要删除的model
   * @param kv                携带额外参数一般用不上
   * @return
   */
  @Override
  protected String afterDelete(SpecMaterialsRcvD specMaterialsRcvD, Kv kv) {
    //addDeleteSystemLog(specMaterialsRcvD.getIAutoId(), JBoltUserKit.getUserId(),specMaterialsRcvD.getName());
    return null;
  }

  /**
   * 检测是否可以删除
   *
   * @param specMaterialsRcvD model
   * @param kv                携带额外参数一般用不上
   * @return
   */
  @Override
  public String checkInUse(SpecMaterialsRcvD specMaterialsRcvD, Kv kv) {
    //这里用来覆盖 检测是否被其它表引用
    return null;
  }

  public SpecMaterialsRcvD findById(Long id) {
    return dao.findById(id);
  }
}