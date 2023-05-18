package cn.rjtech.api.modoc;

import cn.jbolt.core.api.JBoltApiBaseService;
import cn.jbolt.core.api.JBoltApiRet;
import cn.rjtech.admin.modoc.MoDocService;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Record;

import java.util.Date;
import java.util.List;

public class ModocApiService extends JBoltApiBaseService {

  @Inject
  private MoDocService moDocService;


  /**
   * 根据制造工单id查询工序名称信息
   *
   * @param modocid 制造工单id
   */
  public List<Record> getCoperationnameByModocId(String modocid){
   return moDocService.getApiCoperationnameByModocId(modocid);
  }

  /**
   * 根据料品工艺档案配置ID查询工单工序作业指导书信息
   *
   * @param inventoryroutingconfigid 料品工艺档案配置ID
   */
  public List<Record> getMoroutingsopByInventoryroutingconfigId(String inventoryroutingconfigid){
    return moDocService.getMoroutingsopByInventoryroutingconfigId(inventoryroutingconfigid);
  }
  public JBoltApiRet page(Integer page, Integer pageSize, String cmodocno, String cinvaddcode, String cinvcode1,
                          String cinvname1,
                          String cdepname, Long iworkregionmid, Integer status, Date starttime, Date endtime) {
    return JBoltApiRet.API_SUCCESS_WITH_DATA(moDocService.page(page, pageSize, cmodocno, cinvaddcode,
            cinvcode1, cinvname1, cdepname, iworkregionmid, status, starttime, endtime));
  }
}
