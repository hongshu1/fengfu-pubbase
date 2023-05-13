package cn.rjtech.api.modoc;

import cn.jbolt.core.api.JBoltApiBaseService;
import cn.rjtech.admin.modoc.MoDocService;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Record;

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

}
