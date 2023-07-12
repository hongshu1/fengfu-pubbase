package cn.rjtech.admin.moworkshiftd;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.MoWorkShiftD;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;

/**
 * 制造工单-指定日期班次人员信息明细
 *
 * @ClassName: MoWorkShiftDService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-06-29 15:37
 */
public class MoWorkShiftDService extends BaseService<MoWorkShiftD> {
  private final MoWorkShiftD dao = new MoWorkShiftD().dao();

  @Override
  protected MoWorkShiftD dao() {
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
   * @param iType      人员类型：1. 班长 2. 其余人员1 3. 其余人员2
   * @return
   */
  public Page<MoWorkShiftD> getAdminDatas(int pageNumber, int pageSize, Integer iType) {
    //创建sql对象
    Sql sql = selectSql().page(pageNumber, pageSize);
    //sql条件处理
    sql.eq("iType", iType);
    //排序
    sql.desc("iAutoId");
    return paginate(sql);
  }

  /**
   * 保存
   *
   * @param moWorkShiftD
   * @return
   */
  public Ret save(MoWorkShiftD moWorkShiftD) {
    if (moWorkShiftD == null || isOk(moWorkShiftD.getIAutoId())) {
      return fail(JBoltMsg.PARAM_ERROR);
    }
    boolean success = moWorkShiftD.save();
    if (success) {
      //添加日志
      //addSaveSystemLog(moWorkShiftD.getIAutoId(), JBoltUserKit.getUserId(), moWorkShiftD.getName());
    }
    return ret(success);
  }

  /**
   * 更新
   *
   * @param moWorkShiftD
   * @return
   */
  public Ret update(MoWorkShiftD moWorkShiftD) {
    if (moWorkShiftD == null || notOk(moWorkShiftD.getIAutoId())) {
      return fail(JBoltMsg.PARAM_ERROR);
    }
    //更新时需要判断数据存在
    MoWorkShiftD dbMoWorkShiftD = findById(moWorkShiftD.getIAutoId());
    if (dbMoWorkShiftD == null) {
      return fail(JBoltMsg.DATA_NOT_EXIST);
    }
    boolean success = moWorkShiftD.update();
    if (success) {
      //添加日志
      //addUpdateSystemLog(moWorkShiftD.getIAutoId(), JBoltUserKit.getUserId(), moWorkShiftD.getName());
    }
    return ret(success);
  }

  /**
   * 删除数据后执行的回调
   *
   * @param moWorkShiftD 要删除的model
   * @param kv           携带额外参数一般用不上
   * @return
   */
  @Override
  protected String afterDelete(MoWorkShiftD moWorkShiftD, Kv kv) {
    //addDeleteSystemLog(moWorkShiftD.getIAutoId(), JBoltUserKit.getUserId(),moWorkShiftD.getName());
    return null;
  }

  /**
   * 检测是否可以删除
   *
   * @param moWorkShiftD model
   * @param kv           携带额外参数一般用不上
   * @return
   */
  @Override
  public String checkInUse(MoWorkShiftD moWorkShiftD, Kv kv) {
    //这里用来覆盖 检测是否被其它表引用
    return null;
  }

  /**
   * 外部调用获当前对象的数据
   *
   * @param id
   * @return
   */
  public MoWorkShiftD finByid(Long id) {
    return findById(id);
  }

}