package cn.rjtech.routes;

import cn.rjtech.api.appversion.AppversionApiController;
import cn.rjtech.api.formapproval.FormApprovalApiController;
import cn.rjtech.api.formuploadcategory.FormUploadCategoryApiController;
import cn.rjtech.api.formuploadm.FormUploadMApiController;
import cn.rjtech.api.general.GeneralApiController;
import cn.rjtech.api.instockdefect.InStockDefectApiController;
import cn.rjtech.api.instockqcformm.InStockQcFormMApiController;
import cn.rjtech.api.modoc.ModocApiController;
import cn.rjtech.api.momaterialsreturnm.MoMaterialsreturnmApiController;
import cn.rjtech.api.momaterialsscansum.MoMaterialsscansumApiController;
import cn.rjtech.api.momoinvbatch.MoMoinvbatchApiController;
import cn.rjtech.api.momopatchweldm.MoMopatchweldmApiController;
import cn.rjtech.api.moroutingconfigoperation.MoMoroutingconfigOperationApiController;
import cn.rjtech.api.nfcswipecard.NfcSwipeCardApiController;
import cn.rjtech.api.org.OrgApiController;
import cn.rjtech.api.processdefect.ProcessDefectApiController;
import cn.rjtech.api.prodformm.ProdFormMApiController;
import cn.rjtech.api.qcinspection.QcInspectionApiController;
import cn.rjtech.api.rcvdocdefect.RcvDocDefectApiController;
import cn.rjtech.api.rcvdocqcformm.RcvDocQcFormMApiController;
import cn.rjtech.api.spotcheckformm.SpotCheckFormMApiController;
import cn.rjtech.api.stockoutdefect.StockoutDefectApiController;
import cn.rjtech.api.stockoutqcformm.StockOutQcFormMApiController;
import cn.rjtech.api.upload.UploadApiController;
import cn.rjtech.api.uptimem.UptimeMApiController;
import cn.rjtech.api.user.UserApiController;
import cn.rjtech.api.workregion.WorkRegionmApiController;
import cn.rjtech.common.CommonApiController;
import cn.rjtech.wms.print.PrintController;
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
        this.add("/api/upload", UploadApiController.class);
        this.add("/api/erp/common", CommonApiController.class);
        this.add("/api/appversion", AppversionApiController.class);

        // 通用接口
        this.add("/api/general", GeneralApiController.class);
        // 制造工单
        this.add("/api/modoc", ModocApiController.class);
        //生产退料
        this.add("/api/momaterialsreturnm",MoMaterialsreturnmApiController.class);
		 // 现品票
        this.add("/api/momoinvbatch", MoMoinvbatchApiController.class);

        // NFC刷卡
        this.add("/api/nfcswipecard", NfcSwipeCardApiController.class);
        this.add("/api/rcvdocdefect", RcvDocDefectApiController.class);
        this.add("/api/qcinspection", QcInspectionApiController.class);
        this.add("/api/processdefect", ProcessDefectApiController.class);
        this.add("/api/instockdefect", InStockDefectApiController.class);
        this.add("/api/rcvdocqcformm", RcvDocQcFormMApiController.class);
        this.add("/api/instockqcformm", InStockQcFormMApiController.class);
        this.add("/api/stockoutdefect", StockoutDefectApiController.class);
        this.add("/api/stockoutqcformm", StockOutQcFormMApiController.class);

        // 产线
        this.add("/api/workregionm", WorkRegionmApiController.class);
        // 齐料检查
        this.add("/api/momaterialsscansum", MoMaterialsscansumApiController.class);
        //记录上传
        this.add("/api/formuploadm", FormUploadMApiController.class);
        //记录上传-分类管理
        this.add("/api/formuploadcategory", FormUploadCategoryApiController.class);
        //工单工序
        this.add("/api/momoroutingconfigoperation", MoMoroutingconfigOperationApiController.class);
        //补焊记录
        this.add("/api/momopatchweldm", MoMopatchweldmApiController.class);
        //稼动时间管理
        this.add("/api/uptimem", UptimeMApiController.class);
        //审批流/审核
        this.add("/api/formapproval", FormApprovalApiController.class);

        this.add("/api/prodformm", ProdFormMApiController.class);

        this.add("/api/spotcheckformm", SpotCheckFormMApiController.class);


        this.add("/web/common/print", PrintController.class);
    }

}
