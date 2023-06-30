package cn.rjtech.api.spotcheckformm;

import cn.hutool.core.util.StrUtil;
import cn.jbolt.core.api.JBoltApiRet;
import cn.jbolt.core.permission.UnCheck;
import cn.rjtech.admin.inventoryspotcheckform.InventorySpotCheckFormService;
import cn.rjtech.admin.spotcheckform.SpotCheckFormService;
import cn.rjtech.admin.spotcheckformitem.SpotCheckFormItemService;
import cn.rjtech.admin.spotcheckformm.SpotCheckFormMService;
import cn.rjtech.base.controller.BaseApiController;
import cn.rjtech.model.momdata.SpotCheckForm;
import cn.rjtech.model.momdata.SpotCheckFormM;
import com.jfinal.aop.Inject;
import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;
import java.util.Map;

/**
 * 点检管理
 * @author yjllzy
 */
public class SpotCheckFormMApiController extends BaseApiController {

    @Inject
    private SpotCheckFormMApiService service;
    @Inject
    private SpotCheckFormItemService spotCheckFormItemService;
    @Inject
    private SpotCheckFormService spotCheckFormService;
    @Inject
    private InventorySpotCheckFormService inventorySpotCheckFormService;
    @Inject
    private SpotCheckFormMService spotCheckFormMService;
    /**
     * 页面数据
     */
    @UnCheck
    public void datas(@Para(value = "pageNumber", defaultValue = "1") Integer pageNumber,
                      @Para(value = "pageSize", defaultValue = "15") Integer pageSize,
                      @Para(value = "itype") String itype,
                      @Para(value = "modocid") String modocid) {
        Kv kv = Kv.by("itype", itype)
                .set("modocid", modocid);

        renderJBoltApiRet(service.AdminDatas(pageNumber, pageSize, kv));
    }

    /**
     * 生成页面数据
     */
    public void edit(@Para(value = "coperationname") String coperationname,
                     @Para(value = "iinventoryid") String iinventoryid,
                     @Para(value = "modocid") String modocid,
                     @Para(value = "routingconfigid") String routingconfigid,
                     @Para(value = "cequipmentnames") String cequipmentnames,
                     @Para(value = "spotcheckformmid") Long spotcheckformmid,
                     @Para(value = "ispotcheckformid") Long ispotcheckformid,
                     @Para(value = "cspotcheckformname") String cspotcheckformname,
                     @Para(value = "controls") int controls,
                     @Para(value = "itype") int itype,
                     @Para(value = "croutingname") String croutingname){

        renderJBoltApiRet(service.edit( coperationname,iinventoryid,modocid,routingconfigid,cequipmentnames,spotcheckformmid,ispotcheckformid,cspotcheckformname
                ,controls,itype,croutingname));
    }

    /**
     * 保存
     * @param formJsonDataStr
     * @param tableJsonDataStr
     */
    public void submitForm(@Para(value = "formJsonData") String formJsonDataStr,
                           @Para(value = "tableJsonData") String tableJsonDataStr){
        renderJsonData(service.submitForm(formJsonDataStr, tableJsonDataStr));
    }

}
