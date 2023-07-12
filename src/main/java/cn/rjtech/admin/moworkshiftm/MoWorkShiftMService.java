package cn.rjtech.admin.moworkshiftm;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.MoWorkShiftM;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;

/**
 * 制造工单-指定日期班次人员信息主表
 *
 * @ClassName: MoWorkShiftMService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-06-29 15:37
 */
public class MoWorkShiftMService extends BaseService<MoWorkShiftM> {
  private final MoWorkShiftM dao = new MoWorkShiftM().dao();

  @Override
  protected MoWorkShiftM dao() {
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
  public Page<MoWorkShiftM> getAdminDatas(int pageNumber, int pageSize) {
    //创建sql对象
    Sql sql = selectSql().page(pageNumber, pageSize);
    //排序
    sql.desc("iAutoId");
    return paginate(sql);
  }

  /**
   * 保存
   *
   * @param moWorkShiftM
   * @return
   */
  public Ret save(MoWorkShiftM moWorkShiftM) {
    if (moWorkShiftM == null || isOk(moWorkShiftM.getIAutoId())) {
      return fail(JBoltMsg.PARAM_ERROR);
    }
    boolean success = moWorkShiftM.save();
    if (success) {
      //添加日志
      //addSaveSystemLog(moWorkShiftM.getIAutoId(), JBoltUserKit.getUserId(), moWorkShiftM.getName());
    }
    return ret(success);
  }

  /**
   * 更新
   *
   * @param moWorkShiftM
   * @return
   */
  public Ret update(MoWorkShiftM moWorkShiftM) {
    if (moWorkShiftM == null || notOk(moWorkShiftM.getIAutoId())) {
      return fail(JBoltMsg.PARAM_ERROR);
    }
    //更新时需要判断数据存在
    MoWorkShiftM dbMoWorkShiftM = findById(moWorkShiftM.getIAutoId());
    if (dbMoWorkShiftM == null) {
      return fail(JBoltMsg.DATA_NOT_EXIST);
    }
    boolean success = moWorkShiftM.update();
    if (success) {
      //添加日志
      //addUpdateSystemLog(moWorkShiftM.getIAutoId(), JBoltUserKit.getUserId(), moWorkShiftM.getName());
    }
    return ret(success);
  }

  /**
   * 删除数据后执行的回调
   *
   * @param moWorkShiftM 要删除的model
   * @param kv           携带额外参数一般用不上
   * @return
   */
  @Override
  protected String afterDelete(MoWorkShiftM moWorkShiftM, Kv kv) {
    //addDeleteSystemLog(moWorkShiftM.getIAutoId(), JBoltUserKit.getUserId(),moWorkShiftM.getName());
    return null;
  }

  /**
   * 检测是否可以删除
   *
   * @param moWorkShiftM model
   * @param kv           携带额外参数一般用不上
   * @return
   */
  @Override
  public String checkInUse(MoWorkShiftM moWorkShiftM, Kv kv) {
    //这里用来覆盖 检测是否被其它表引用
    return null;
  }

  public MoWorkShiftM findBtyid(Long id) {
    return findById(id);
  }

}