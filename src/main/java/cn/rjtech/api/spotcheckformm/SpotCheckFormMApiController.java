package cn.rjtech.api.spotcheckformm;

import cn.jbolt.core.permission.UnCheck;
import cn.rjtech.admin.inventoryspotcheckform.InventorySpotCheckFormService;
import cn.rjtech.admin.spotcheckform.SpotCheckFormService;
import cn.rjtech.admin.spotcheckformitem.SpotCheckFormItemService;
import cn.rjtech.admin.spotcheckformm.SpotCheckFormMService;
import cn.rjtech.base.controller.BaseApiController;
import com.jfinal.aop.Inject;
import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.Kv;


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
     * 传入数据：制作工单 的工单id,点检类型 itype：1.首末点检表 2.首中末点检表
     * 输出：iauditstatus 状态 ，cspotcheckformname 表格名称 可为空 ，coperationname ：工序名称
     * cequipmentnames 设备名称 ccreatename 生产人员    dcreatetime2 提交时间
     * iinventoryid 存货id modocid 工单id routingconfigid 存货工艺id  cequipmentnames 设备名称
     *  ispotcheckformid 表格id
     *
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
     *
     * @param coperationname 工序名称
     * @param iinventoryid 存货id
     * @param modocid 工单id
     * @param routingconfigid   存货工艺id
     * @param cequipmentnames 设备名称
     * @param spotcheckformmid 主表id
     * @param ispotcheckformid 表格id
     * @param cspotcheckformname 表格名称
     * @param itype 类型 类型1.首末点检表 2.首中末点检表
     *
     *
     */
    public void edit(@Para(value = "coperationname") String coperationname,
                     @Para(value = "iinventoryid") String iinventoryid,
                     @Para(value = "modocid") String modocid,
                     @Para(value = "routingconfigid") String routingconfigid,
                     @Para(value = "cequipmentnames") String cequipmentnames,
                     @Para(value = "spotcheckformmid") Long spotcheckformmid,
                     @Para(value = "ispotcheckformid") Long ispotcheckformid,
                     @Para(value = "cspotcheckformname") String cspotcheckformname,
                     @Para(value = "itype") int itype){

        renderJBoltApiRet(service.edit( coperationname,iinventoryid,modocid,routingconfigid,cequipmentnames,spotcheckformmid,ispotcheckformid,cspotcheckformname
                ,itype));
    }

    /**
     * 保存
     * @param formJsonDataStr {
     * spotCheckFormM.iautoid：主表id
     * routingconfigid:工序工艺ID
     * modocid:生产订单ID
     * coperationname：工序名称
     * iprodformid：点检表格ID
     * itype：类型1.首末点检表 2.首中末点检表
     * cdesc：点检记录
     * cmethod：处理方式
     * }
     * @param tableJsonDataStr{
     *  spotcheckformtableparamid：检验项目ID
     * iseq：项目次序
     * spotcheckparamid：检验参数值ID
     * itype：参数录入方式：1. CPK数值 2. 文本框 3. 选择（√，/，×，△，◎） 4. 单选 5. 复选 6. 下拉列表 7. 日期 8. 时间
     * istdval：标准值
     * imaxval：最大设定值
     * iminval：最小设定值
     * coptions：列表可选值;多个“,”分隔
     * cvalue：cvalue：填写值
     * }
     */
    public void submitForm(@Para(value = "formJsonData") String formJsonDataStr,
                           @Para(value = "tableJsonData") String tableJsonDataStr){
        renderJsonData(service.submitForm(formJsonDataStr, tableJsonDataStr));
    }

}
