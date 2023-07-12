package cn.rjtech.api.modoc;

import cn.hutool.core.util.StrUtil;
import cn.jbolt.core.api.JBoltApiBaseService;
import cn.jbolt.core.api.JBoltApiRet;
import cn.rjtech.admin.department.DepartmentService;
import cn.rjtech.admin.inventory.InventoryService;
import cn.rjtech.admin.modoc.MoDocService;
import cn.rjtech.admin.morouting.MoMoroutingService;
import cn.rjtech.admin.specmaterialsrcvm.SpecMaterialsRcvMService;
import cn.rjtech.admin.uom.UomService;
import cn.rjtech.admin.workregionm.WorkregionmService;
import cn.rjtech.admin.workshiftm.WorkshiftmService;
import cn.rjtech.entity.vo.modoc.ModocApiPage;
import cn.rjtech.model.momdata.*;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Okv;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class ModocApiService extends JBoltApiBaseService {

  @Inject
  private MoDocService moDocService;
  @Inject
  private InventoryService inventoryService; //存货档案
  @Inject
  private UomService uomService; //单位

  @Inject
  private MoMoroutingService moMoroutingService; //工艺路线

  @Inject
  private WorkshiftmService workshiftmService; //班次

  @Inject
  private WorkregionmService workregionmService; //产线
  @Inject
  private DepartmentService departmentService; //部门

  //特殊领料
  @Inject
  private SpecMaterialsRcvMService specMaterialsRcvMService;


  /**
   * 根据制造工单id查询工序名称信息
   *
   * @param modocid 制造工单id
   */
  public List<Record> getCoperationnameByModocId(String modocid) {
    return moDocService.getApiCoperationnameByModocId(modocid);
  }

  /**
   * 根据料品工艺档案配置ID查询工单工序作业指导书信息
   *
   * @param inventoryroutingconfigid 料品工艺档案配置ID
   */
  public List<Record> getMoroutingsopByInventoryroutingconfigId(String inventoryroutingconfigid) {
    return moDocService.getMoroutingsopByInventoryroutingconfigId(inventoryroutingconfigid);
  }

  public JBoltApiRet page(Integer page, Integer pageSize, String cmodocno, String cinvaddcode, String cinvcode1, String cinvname1, String cdepname, Long iworkregionmid, Integer status, Date starttime, Date endtime) {
    return JBoltApiRet.API_SUCCESS_WITH_DATA(moDocService.page(page, pageSize, cmodocno, cinvaddcode, cinvcode1, cinvname1, cdepname, iworkregionmid, status, starttime, endtime));
  }

    public JBoltApiRet getModocdetails(Long imodocid) {
        MoDoc moDoc = moDocService.findById(imodocid);
        if (moDoc == null) {
            return JBoltApiRet.fail(4000, "工单信息不存在");
        }
        
        Record record = getModoc(moDoc);

        Okv ret = Okv.by("doc", record)
                .set("job", moDocService.getMoJobData(imodocid));
        return JBoltApiRet.API_SUCCESS_WITH_DATA(ret);
    }


  public Record getModoc(MoDoc moDoc) {
    Record moRecod = moDoc.toRecord();
    moRecod.keep("cmodocno", "dplandate", "iqty", "icompqty");
    if (notOk(moRecod.getBigDecimal("icompqty"))) {
      moRecod.set("icompqty", BigDecimal.ZERO);
    }
    if (isOk(moDoc.getIInventoryId())) {
      //存货
      Inventory inventory = inventoryService.findById(moDoc.getIInventoryId());
      if (inventory != null) {
        //料品编码
        if (StrUtil.isNotBlank(inventory.getCInvCode())) {
          moRecod.set("cinvcode", inventory.getCInvCode());
        } else {
          moRecod.set("cinvcode", "");
        }
        //客户部番
        if (StrUtil.isNotBlank(inventory.getCInvCode1())) {
          moRecod.set("cinvcode1", inventory.getCInvCode1());
        } else {
          moRecod.set("cinvcode1", "");
        }
        //部品名称
        if (StrUtil.isNotBlank(inventory.getCInvName1())) {
          moRecod.set("cinvname1", inventory.getCInvName1());
        } else {
          moRecod.set("cinvname1", "");
        }
      } else {
        moRecod.set("cinvcode", "");
        moRecod.set("cinvcode1", "");
        moRecod.set("cinvname1", "");
      }
    } else {
      moRecod.set("cinvcode", "");
      moRecod.set("cinvcode1", "");
      moRecod.set("cinvname1", "");
    }


    //差异数量
    if (moDoc.getIQty() != null && moDoc.getICompQty() != null) {
      BigDecimal cyqty = moDoc.getIQty().subtract(moDoc.getICompQty());
      moRecod.set("cyqty", cyqty);
    } else {
      moRecod.set("cyqty", BigDecimal.ZERO);
    }
    //产线
    if (isOk(moDoc.getIWorkRegionMid())) {
      Workregionm workregionm = workregionmService.findById(moDoc.getIWorkRegionMid());
      if (workregionm != null) {
        if (StrUtil.isNotBlank(workregionm.getCWorkName())) {
          moRecod.set("cworkname", workregionm.getCWorkName());
        } else {

          moRecod.set("cworkname", "");
        }
      } else {
        moRecod.set("cworkname", "");
      }
    } else {

      moRecod.set("cworkname", "");
    }
    //班次
    if (isOk(moDoc.getIWorkShiftMid())) {
      Workshiftm workshiftm = workshiftmService.findById(moDoc.getIWorkShiftMid());
      if (workshiftm != null) {
        if (StrUtil.isNotBlank(workshiftm.getCworkshiftname())) {
          moRecod.set("cworkshiftname", workshiftm.getCworkshiftname());
        } else {
          moRecod.set("cworkshiftname", "");
        }
      } else {
        moRecod.set("cworkshiftname", "");
      }
    } else {
      moRecod.set("cworkshiftname", "");
    }
    //部门
    if (isOk(moDoc.getIDepartmentId())) {
      Department department = departmentService.findById(moDoc.getIDepartmentId());
      if (department != null) {
        if (StrUtil.isNotBlank(department.getCDepName())) {
          moRecod.set("cdepname", department.getCDepName());
        } else {
          moRecod.set("cdepname", "");
        }
      } else {
        moRecod.set("cdepname", "");
      }
    } else {
      moRecod.set("cdepname", "");
    }
    return moRecod;
  }

  /**
   * 获取工单信息
   *
   * @param moDoc
   */
  public ModocApiPage getModoc1(MoDoc moDoc) {
    ModocApiPage modocApiPag = new ModocApiPage();

    modocApiPag.setiAutoId(moDoc.getIAutoId());
    modocApiPag.setcMoDocNo(moDoc.getCMoDocNo());
    modocApiPag.setdPlanDate(moDoc.getDPlanDate());


    if (isOk(moDoc.getIInventoryId())) {
      //存货
      Inventory inventory = inventoryService.findById(moDoc.getIInventoryId());
      if (inventory != null) {
        //料品编码
        modocApiPag.setcInvCode(inventory.getCInvCode());

        //客户部番
        modocApiPag.setcInvCode1(inventory.getCInvCode1());

        //部品名称
        if (StrUtil.isNotBlank(inventory.getCInvName1())) {
          modocApiPag.setcInvName1(inventory.getCInvName1());
        } else {
          modocApiPag.setcInvName1("");
        }


      }
      //工艺路线


    }
    //差异数量
    if (moDoc.getIQty() != null && moDoc.getICompQty() != null) {
      BigDecimal cyqty = moDoc.getIQty().subtract(moDoc.getICompQty());
      modocApiPag.setCyqty(cyqty);
    } else {
      modocApiPag.setCyqty(BigDecimal.ZERO);
    }

    //产线
    if (isOk(moDoc.getIWorkRegionMid())) {
      Workregionm workregionm = workregionmService.findById(moDoc.getIWorkRegionMid());
      if (workregionm != null) {
        modocApiPag.setcWorkName(workregionm.getCWorkName());

      }
    } else {
      modocApiPag.setcWorkName("");
    }
    //班次
    if (isOk(moDoc.getIWorkShiftMid())) {
      Workshiftm workshiftm = workshiftmService.findById(moDoc.getIWorkShiftMid());
      if (workshiftm != null) {
        modocApiPag.setcWorkShiftName(workshiftm.getCworkshiftname());
      }
    } else {
      modocApiPag.setcWorkShiftName("");
    }
    //部门
    if (isOk(moDoc.getIDepartmentId())) {
      Department department = departmentService.findById(moDoc.getIDepartmentId());
      if (department != null) {
        modocApiPag.setcDepName(department.getCDepName());

      }
    } else {
      modocApiPag.setcDepName("");
    }
    return modocApiPag;
  }

  /**
   * 根据制造工单id查询特殊领料数据源Api
   *
   * @param imodocid   制造工单id
   * @param pageNumber 页数
   * @param pageSize   页面数量
   * @return
   */
  public Page<Record> getSpecmaterialsrcvmDatas(Long imodocid, Integer pageNumber, Integer pageSize) {
    return specMaterialsRcvMService.getApiSpecmaterialsrcvmDatas(imodocid, pageNumber, pageSize);
  }

  /**
   * 根据制造工单id查询当前工单的存货档案数据
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
    return specMaterialsRcvMService.getInventoryDatasByDocid(imodocid, pageNumber, pageSize, cinvcode, cinvcode1, cinvname1);
  }

  public Page<Record> getBarcodeAllBycBarcodeApi(Long imodocid, Integer pageNumber, Integer pageSize, String cinvcode, String cinvcode1, String cinvname1) {
    return specMaterialsRcvMService.getInventoryDatasByDocid(imodocid, pageNumber, pageSize, cinvcode, cinvcode1, cinvname1);
  }
}
