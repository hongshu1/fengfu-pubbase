package cn.rjtech.admin.moroutingconfigperson;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.modoc.MoDocService;
import cn.rjtech.admin.moroutingconfig.MoMoroutingconfigService;
import cn.rjtech.admin.person.PersonService;
import cn.rjtech.base.exception.ParameterException;
import cn.rjtech.model.momdata.MoDoc;
import cn.rjtech.model.momdata.MoMoroutingconfigPerson;
import cn.rjtech.model.momdata.Person;
import cn.rjtech.util.ValidationUtils;
import cn.rjtech.wms.utils.StringUtils;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;

/**
 * 制造工单-工单工艺人员配置 Service
 *
 * @ClassName: MoMoroutingconfigPersonService
 * @author: RJ
 * @date: 2023-05-09 16:48
 */
public class MoMoroutingconfigPersonService extends BaseService<MoMoroutingconfigPerson> {
  @Inject
  private PersonService personService;//人员
  @Inject
  private MoMoroutingconfigService moMoroutingconfigService; //工单工艺配置
  @Inject
  private MoDocService moDocService;

  private final MoMoroutingconfigPerson dao = new MoMoroutingconfigPerson().dao();

  @Override
  protected MoMoroutingconfigPerson dao() {
    return dao;
  }


  /**
   * 后台管理分页查询
   *
   * @param pageNumber
   * @param pageSize
   * @param kv
   * @return
   */
  public Page<Record> paginateAdminDatas(int pageNumber, int pageSize, Kv kv) {
    Sql sql = selectSql().select("distinct a.iAutoId as iautoid,b.cPsn_Num, b.cPsn_Name ," +
        "b.JobNumber as jobnumber,b.iSex,b.cpsnmobilephone").from(table(), "a").
        leftJoin(personService.table(), "b",
            "b.iAutoId=a.iPersonId").
        innerJoin(moMoroutingconfigService.table(), "c", "c.iAutoId=a.iMoRoutingConfigId").
        like("b." + Person.CPSN_NUM, kv.getStr("cpsnnum"))
        .like("b." + Person.CPSN_NAME, kv.getStr("cpsname"))
        .like("b." + Person.JOBNUMBER, kv.getStr("jobmumber"))
        .eq("c.iAutoId", kv.getStr("imoroutingconfigid")).
            orderBy("a." + MoMoroutingconfigPerson.IAUTOID, true).page(pageNumber, pageSize);


    return paginateRecord(sql);

    //return paginateByKeywords("iAutoId","DESC", pageNumber, pageSize, keywords, "iAutoId");
  }

  /**
   * 获取工单工艺配置相关人员信息
   *
   * @param imoroutingconfigid
   * @return
   */
  public String getMoMoroutingconfigPersonNameData(Long imoroutingconfigid) {
    if (!isOk(imoroutingconfigid)) {
      return "";
    }
    Sql sql = selectSql().select("distinct b.iAutoId as iautoid,b.cPsn_Num, b.cPsn_Name"
    ).from(table(), "a").
        leftJoin(personService.table(), "b",
            "b.iAutoId=a.iPersonId").
        innerJoin(moMoroutingconfigService.table(), "c", "c.iAutoId=a.iMoRoutingConfigId")
        .eq("c.iAutoId", imoroutingconfigid).
            orderBy("b." + Person.IAUTOID, true);
    List<Record> recordList = findRecord(sql);
    StringBuilder s = new StringBuilder();

    if (!recordList.isEmpty()) {
      if (recordList.size() > 1) {
        for (Record record : recordList) {
          if (StringUtils.isNotBlank(record.getStr("cpsn_name"))) {
            s = s.append(record.getStr("cpsn_name")).append(",");
          }
        }
      } else {
        s.append(recordList.get(0).getStr("cpsn_name"));
      }
    }
    return s.toString();

  }

  /**
   * 保存
   *
   * @param moMoroutingconfigPerson
   * @return
   */
  public Ret save(MoMoroutingconfigPerson moMoroutingconfigPerson) {
    if (moMoroutingconfigPerson == null || isOk(moMoroutingconfigPerson.getIAutoId())) {
      return fail(JBoltMsg.PARAM_ERROR);
    }
    boolean success = moMoroutingconfigPerson.save();
    if (success) {
      //添加日志
      //addSaveSystemLog(moMoroutingconfigPerson.getIAutoId(), JBoltUserKit.getUserId(), moMoroutingconfigPerson.getName());
    }
    return ret(success);
  }

  /**
   * 更新
   *
   * @param moMoroutingconfigPerson
   * @return
   */
  public Ret update(MoMoroutingconfigPerson moMoroutingconfigPerson) {
    if (moMoroutingconfigPerson == null || notOk(moMoroutingconfigPerson.getIAutoId())) {
      return fail(JBoltMsg.PARAM_ERROR);
    }
    //更新时需要判断数据存在
    MoMoroutingconfigPerson dbMoMoroutingconfigPerson = findById(moMoroutingconfigPerson.getIAutoId());
    if (dbMoMoroutingconfigPerson == null) {
      return fail(JBoltMsg.DATA_NOT_EXIST);
    }
    //if(existsName(moMoroutingconfigPerson.getName(), moMoroutingconfigPerson.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
    boolean success = moMoroutingconfigPerson.update();
    if (success) {
      //添加日志
      //addUpdateSystemLog(moMoroutingconfigPerson.getIAutoId(), JBoltUserKit.getUserId(), moMoroutingconfigPerson.getName());
    }
    return ret(success);
  }

  /**
   * 删除 指定多个ID
   *
   * @param ids
   * @return
   */
  public Ret deleteByBatchIds(String ids, Long imdocid) {
    if (StringUtils.isBlank(ids)) {
      return fail("未选中数据，不允许操作");
    }
    String[] idsStrs = ids.split(",");
    MoDoc moDoc = null;
    if (isOk(imdocid)) {
      moDoc = moDocService.findById(imdocid);
      if (moDoc != null) {
        if (moDoc.getIStatus().equals(7)) {
          return fail("工单已经关闭，不允许操作");
        }
      }
    }
    MoDoc finalMoDoc = moDoc;
    tx(() -> {

      Ret ret = deleteByIds(ids, true);
      if (ret.isOk() == true) {
        if (finalMoDoc != null) {
          finalMoDoc.setIPersonNum(finalMoDoc.getIPersonNum() - idsStrs.length);
          if (finalMoDoc.getIPersonNum() == 0) {
            finalMoDoc.setIStatus(1);
          }
          finalMoDoc.update();
        }
      }

      return true;
    });

    return SUCCESS;
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
   * @param moMoroutingconfigPerson 要删除的model
   * @param kv                      携带额外参数一般用不上
   * @return
   */
  @Override
  protected String afterDelete(MoMoroutingconfigPerson moMoroutingconfigPerson, Kv kv) {
    //addDeleteSystemLog(moMoroutingconfigPerson.getIAutoId(), JBoltUserKit.getUserId(),moMoroutingconfigPerson.getName());
    return null;
  }

  /**
   * 检测是否可以删除
   *
   * @param moMoroutingconfigPerson 要删除的model
   * @param kv                      携带额外参数一般用不上
   * @return
   */
  @Override
  public String checkCanDelete(MoMoroutingconfigPerson moMoroutingconfigPerson, Kv kv) {
    //如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
    return checkInUse(moMoroutingconfigPerson, kv);
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

  public Ret saveEquipmentPerson(JBoltTable jBoltTable, Long configid) {
    if (isOk(jBoltTable.getSave())) {
      ValidationUtils.error("缺少人员信息");
    }
    List<Record> saveRecordList = jBoltTable.getSaveRecordList();

    tx(() -> {

      MoMoroutingconfigPerson moMoroutingconfigPerson;
      for (Record record : saveRecordList) {
        Long iPersonId = null;
        if (isOk(record.getLong("ipersonid"))) {
          Person p = personService.findById(record.getLong("ipersonid"));
          if (p == null) {
            throw new ParameterException("存在无效人员信息");
          }
          iPersonId = p.getIAutoId();
        } else {
          if (StringUtils.isNotBlank(record.getStr("cpsn_num"))) {
            Record p = personService.getpersonByCpsnnum(record.getStr("cpsn_num"));
            if (p == null) {
              throw new ParameterException("存在无效人员信息");
            }
            iPersonId = p.getLong("iautoid");
          }

        }
        moMoroutingconfigPerson = new MoMoroutingconfigPerson();
        moMoroutingconfigPerson.setIPersonId(iPersonId);
        moMoroutingconfigPerson.setIMoRoutingConfigId(configid);
        moMoroutingconfigPerson.save();
      }

      return true;

    });
    return SUCCESS;

  }

  public MoMoroutingconfigPerson findByid(Long id) {
    return findById(id);
  }
}
