package cn.rjtech.api.nfcswipecard;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.jbolt.core.api.JBoltApiBaseService;
import cn.rjtech.admin.pad.PadService;
import cn.rjtech.admin.person.PersonService;
import cn.rjtech.admin.personequipment.PersonEquipmentService;
import cn.rjtech.model.momdata.Personswipelog;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;

public class NfcSwipeCardApiService extends JBoltApiBaseService {

  @Inject
  private PadService padService;//平板

  @Inject
  private PersonService personService;//平板

  @Inject
  private PersonEquipmentService personEquipmentService;//


  /**
   * 产线刷卡
   *
   * @param kv
   * @return
   */
  public Record nfcswipecard(Kv kv) {
    Record record = new Record();
    Personswipelog personswipelog = new Personswipelog();
    //根据mac地址获取当前平板对应的产线
    List<Record> deviceaddress = padService.getPadWorkRegionByCmac(kv.getStr("deviceaddress"));
    boolean bol = true;
    boolean bol1 = true;
    Record records = new Record();
    for (Record record1 : deviceaddress) {
      records = personEquipmentService.getPersonequipmentByCpsnnumId(Kv.by("iautoid", record1.getLong("iworkregionmid")).set("cpsnnum", kv.getStr("usercode")));
      if (records != null) {
        bol = false;
        record.put("cworkcode", records.getStr("cworkcode"));//产线编码
        record.put("cworkname", records.getStr("cworkname"));//产线名称
        personswipelog.setCPadCode("cpadcode");
        personswipelog.setCPadName("cpadname");
        personswipelog.setCWorkName("cworkname");
        break;
      }
    }
    //1.根据产线id找到对应的工单 用人员编码去工单里面查找


    Record record1 = personService.getpersonByCpsnnum("cardusercode");//获取刷卡用户
    personswipelog.setIOrgId(getOrgId());
    personswipelog.setCOrgCode(getOrgCode());
    personswipelog.setCOrgName(getOrgName());
    personswipelog.setIPersonId(record1.getLong("iautoid"));
    personswipelog.setIUserId(record1.getLong("iuserid"));
    personswipelog.setCEcardNo(kv.getStr("cardcode"));
    personswipelog.setUsername(record1.getStr("cpsn_name"));
    personswipelog.setCpsnName(record1.getStr("username"));
    personswipelog.setCIp("测试IP");
    personswipelog.setICreateBy(record1.getLong("iautoid"));
    personswipelog.setCCreateName(record1.getStr("cpsn_name"));
    personswipelog.setDCreateTime(new DateTime());
    personswipelog.setIsDeleted(false);
    personswipelog.setCResult(bol || bol1 ? "刷卡成功" : "刷卡失败");
    if (!bol) {
      personswipelog.setCCause("人员操作资质不足");
    }
    if (!bol1) {
      personswipelog.setCCause("不是当班生产线人员");
    }
    personswipelog.setCDepName(record1.getStr("cdepname"));
    personswipelog.save();
    record.put("cardcode", kv.getStr("cardcode"));
    record.put("cpsnname", record1.getStr("cpsn_name"));
    record.put("datetoday", DateUtil.today());
    return record;
  }
}
