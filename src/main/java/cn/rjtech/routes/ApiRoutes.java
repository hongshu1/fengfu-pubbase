package cn.rjtech.routes;

import cn.rjtech.api.general.GeneralApiController;
import cn.rjtech.api.modoc.ModocApiController;
import cn.rjtech.api.momaterialsscansum.MoMaterialsscansumApiController;
import cn.rjtech.api.nfcswipecard.NfcSwipeCardApiController;
import cn.rjtech.api.instockdefect.InStockDefectApiController;
import cn.rjtech.api.instockqcformm.InStockQcFormMApiController;
import cn.rjtech.api.modoc.ModocApiController;
import cn.rjtech.api.momaterialsscansum.MoMaterialsscansumApiController;
import cn.rjtech.api.nfcswipecard.NfcSwipeCardApiController;
import cn.rjtech.api.org.OrgApiController;
import cn.rjtech.api.processdefect.ProcessDefectApiController;
import cn.rjtech.api.qcinspection.QcInspectionApiController;
import cn.rjtech.api.rcvdocdefect.RcvDocDefectApiController;
import cn.rjtech.api.rcvdocqcformm.RcvDocQcFormMApiController;
import cn.rjtech.api.stockoutdefect.StockoutDefectApiController;
import cn.rjtech.api.stockoutqcformm.StockOutQcFormMApiController;
import cn.rjtech.api.user.UserApiController;
import cn.rjtech.api.workregion.WorkRegionmApiController;
import cn.rjtech.common.CommonApiController;
import com.jfinal.config.Routes;

/**
 * API路由
 *
 * @author Kephon
 */
public class ApiRoutes extends Routes {

  @Override
  public void config() {
    this.add("/api/org", OrgApiController.class);
    this.add("/api/user", UserApiController.class);
    this.add("/api/erp/common", CommonApiController.class);

    //通用接口
    this.add("/api/general", GeneralApiController.class);

    //NFC刷卡
    this.add("/api/nfcswipecard", NfcSwipeCardApiController.class);
    //制造工单
    this.add("/api/modoc", ModocApiController.class);

    this.add("/api/rcvdocqcformm", RcvDocQcFormMApiController.class);
    this.add("/api/rcvdocdefect", RcvDocDefectApiController.class);
    this.add("/api/processdefect", ProcessDefectApiController.class);
    this.add("/api/instockdefect", InStockDefectApiController.class);
    this.add("/api/stockoutqcformm", StockOutQcFormMApiController.class);
    this.add("/api/instockqcformm", InStockQcFormMApiController.class);
    this.add("/api/stockoutdefect", StockoutDefectApiController.class);
    this.add("/api/qcinspection", QcInspectionApiController.class);
    this.add("/api/workregionm", WorkRegionmApiController.class);//产线
    this.add("/api/momaterialsscansum", MoMaterialsscansumApiController.class);//齐料检查


  }

}
