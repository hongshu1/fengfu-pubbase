package cn.rjtech.admin.sysenumeration;

import cn.jbolt.core.controller.base.JBoltBaseController;
import cn.jbolt.core.permission.UnCheck;
import cn.rjtech.admin.syspureceive.SysPureceiveService;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;

@UnCheck
@Path(value = "/admin/SysEnumeration")
public class SysEnumerationController extends JBoltBaseController {

    @Inject
    private SysEnumerationService service;

    /**
     *  仓库试图数据源
     */
    public void wareHouse() {
        renderJsonData(service.getwareHouseDatas(getKv()));
    }

    /**
     *  供应商视图数据源
     */
    public void vendor() {
        renderJsonData(service.getvendorDatas(getKv()));
    }

    /**
     *  条码视图数据源
     */
    public void barcodedetail() {
        renderJsonData(service.getbarcodedetailDatas(getKv()));
    }
    /**
     *  存货档案视图数据源
     */
    public void inventory() {
        renderJsonData(service.getinventoryDatas(getKv()));
    }
    /**
     *  生产工单视图数据源
     */
    public void modetail() {
        renderJsonData(service.getmodetailDatas(getKv()));
    }
    /**
     *  采购订单视图数据源
     */
    public void podetail() {
        renderJsonData(service.getpodetailDatas(getKv()));
    }
    /**
     *  货位视图数据源
     */
    public void position() {
        renderJsonData(service.getpositionDatas(getKv()));
    }
    /**
     *  客户信息视图数据源
     */
    public void customer() {
        renderJsonData(service.getcustomerDatas(getKv()));
    }


}
