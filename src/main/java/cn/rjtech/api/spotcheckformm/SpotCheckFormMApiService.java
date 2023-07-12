package cn.rjtech.api.spotcheckformm;

import cn.hutool.core.util.StrUtil;
import cn.jbolt.core.api.JBoltApiBaseService;
import cn.jbolt.core.api.JBoltApiRet;
import cn.rjtech.admin.inventoryspotcheckform.InventorySpotCheckFormService;
import cn.rjtech.admin.spotcheckform.SpotCheckFormService;
import cn.rjtech.admin.spotcheckformitem.SpotCheckFormItemService;
import cn.rjtech.admin.spotcheckformm.SpotCheckFormMService;
import cn.rjtech.model.momdata.SpotCheckForm;
import cn.rjtech.model.momdata.SpotCheckFormM;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;

/**
 * 点检管理
 * @author yjllzy
 */
public class SpotCheckFormMApiService extends JBoltApiBaseService {

    @Inject
    private SpotCheckFormMService service;
    @Inject
    private SpotCheckFormItemService spotCheckFormItemService;
    @Inject
    private SpotCheckFormService spotCheckFormService;
    @Inject
    private InventorySpotCheckFormService inventorySpotCheckFormService;
    /**
     *新增页面数据
     * @return
     */
    public JBoltApiRet AdminDatas(Integer pageNumber, Integer pageSize, Kv kv) {
        return JBoltApiRet.successWithData(service.getAdminDatas2(kv));
    }

    public JBoltApiRet edit(String coperationname, String iinventoryid, String modocid,
                            String routingconfigid, String cequipmentnames, Long spotcheckformmid,
                            Long ispotcheckformid, String cspotcheckformname, int itype) {
        Kv kv = new Kv();
        List<Record> list = inventorySpotCheckFormService.pageList(Kv.create().set("iinventoryid",iinventoryid).set("ispotcheckformid",ispotcheckformid)
                .set("cspotcheckformname",cspotcheckformname)
                .set("page", 1).set("pageSize", 5)).getList();
        if (list.size()>0) {
            Record record = list.get(0);
            SpotCheckForm spotCheckForm = spotCheckFormService.findById(record.getStr("ispotcheckformid"));
            record.set("iautoid",null);
            if (StrUtil.isBlank(cequipmentnames)){
                Record equipment = service.getEquipment(Long.valueOf(routingconfigid));
                record.set("cequipmentnames",equipment.getStr("cequipmentnames"));
            }else {
                record.set("cequipmentnames",cequipmentnames);
            }
            if (spotcheckformmid!=null){
                record.set("iautoid",spotcheckformmid);
                SpotCheckFormM checkFormM = service.findById(spotcheckformmid);
                record.set("iauditstatus",checkFormM.getIAuditStatus());
                record.set("iauditway",checkFormM.getIAuditWay());
                if (StrUtil.isNotBlank(checkFormM.getCDesc())){
                    record.set("cdesc",checkFormM.getCDesc());
                }
                if (StrUtil.isNotBlank(checkFormM.getCMethod())){
                    record.set("cmethod",checkFormM.getCMethod());
                }
            }
            kv.set("spotcheckformm",record);
            kv.set("coperationname",coperationname);
            kv.set("modocid",modocid);
            kv.set("iinventoryid",iinventoryid);
            kv.set("routingconfigid",routingconfigid);
            kv.set("spotcheckformid",spotCheckForm.getIAutoId());
            kv.set("itype",itype);
            //项目标题
            List<Record> formItemLists = spotCheckFormItemService.formItemLists(Kv.by("iqcformid", ispotcheckformid));
            kv.set("lineRoll",formItemLists);
            List<Record> byIdGetDetail = spotCheckFormService.findByIdGetDetail(String.valueOf(ispotcheckformid));
            kv.set("lineRoll2",byIdGetDetail);
        }
        return JBoltApiRet.successWithData(kv);
    }

    public JBoltApiRet submitForm(String formJsonDataStr, String tableJsonDataStr) {
        return JBoltApiRet.successWithData(service.submitForm(formJsonDataStr,tableJsonDataStr));
    }
}
