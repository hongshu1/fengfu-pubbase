package cn.rjtech.api.uptimem;

import cn.jbolt.core.permission.UnCheck;
import cn.rjtech.admin.uptimem.UptimeMService;
import cn.rjtech.base.controller.BaseApiController;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.Kv;
import io.github.yedaxia.apidocs.ApiDoc;

/**
 * 稼动时间管理
 *
 * @author chentao
 */
@ApiDoc
public class UptimeMApiController extends BaseApiController {

    @Inject
    private UptimeMApiService service;

    /**
     * 获取稼动时间管理主界面分页数据
     * @param pageNumber 页码
     * @param pageSize 页数
     * @param cworkname 产线名称
     * @param cworkshiftname 班次名称
     * @param iauditstatus 状态
     * @param startdate 开始日期
     * @param enddate 结束日期
     */
    @UnCheck
    public void getPageList(@Para(value = "pageNumber") int pageNumber,
                            @Para(value = "pageSize") int pageSize,
                            @Para(value = "cworkname") String cworkname,
                            @Para(value = "cworkshiftname") String cworkshiftname,
                            @Para(value = "iauditstatus") int iauditstatus,
                            @Para(value = "startdate") String startdate,
                            @Para(value = "enddate") String enddate) {
        //ValidationUtils.validateId(iMoDocId,"工单主键id");
        Kv kv = Kv.by("cworkname",cworkname).set("cworkshiftname",cworkshiftname).set("iauditstatus",iauditstatus).set("startdate",startdate).set("enddate",enddate);
        renderJBoltApiRet(service.getPageList(pageNumber,pageSize,kv));
    }

    /**
     * 新增页面选择产线班次后调用接口
     * @param iworkregionmid 产线id
     * @param iworkshiftmid 班次id
     */
    @UnCheck
    public void getUptimeTplInfoList(@Para(value = "iworkregionmid") Long iworkregionmid,
                            @Para(value = "iworkshiftmid") Long iworkshiftmid) {
        //ValidationUtils.validateId(iMoDocId,"工单主键id");
        Kv kv = Kv.by("iworkregionmid",iworkregionmid).set("iworkshiftmid",iworkshiftmid);
        renderJBoltApiRet(service.getUptimeTplInfoList(kv));
    }

    /**
     * 查看及编辑页面详情接口
     * @param iautoid 稼动时间管理主界面主键id
     */
    @UnCheck
    public void getUptimeMInfoList(@Para(value = "iautoid") Long iautoid) {
        ValidationUtils.validateId(iautoid,"主键id");
        Kv kv = Kv.by("iautoid",iautoid);
        renderJBoltApiRet(service.getUptimeMInfoList(kv));
    }


    /**
     * 补焊纪录保存接口
     * @param updateOrSaveType 新增或者编辑，新增传1，编辑传0
     * @param data {
     *                 "iautoid":"1674253970028417024",
     *                 "ibasemins":55,
     *                 "iotmins":10,
     *                 "irate1":"54.545455",
     *                 "irate2":"18.181818",
     *                 "istopmins":25,
     *                 "iswitchmins":20,
     *                 "iworkmins":20,
     *                 "iworkregionmid":"1663734219389444096",
     *                 "iworkshiftmid":"1646683347910529024"
     *             }  //表头数据json串，新增时iautoid为空
     *
     * @param dataList [
     *                     {
     *                         "iautoid":"1674253970569482240",
     *                         "imins":5,
     *                         "istdmins":8,
     *                         "iuptimecategoryid":"1673585765055537152",
     *                         "iuptimeparamid":"1673585877248974848",
     *                         "iseq":1
     *                     }
     *                 ]  //表格数据json串，新增时iautoid为空
     */
    @UnCheck
    public void updateAndSaveApi(@Para(value = "updateOrSaveType") int updateOrSaveType,
                                 @Para(value = "data") String data,
                                 @Para(value = "dataList") String dataList) {
        ValidationUtils.notNull(updateOrSaveType,"保存类型不能为空！");
        renderJBoltApiRet(service.updateAndSaveApi(updateOrSaveType,data,dataList));
    }

    /***
     * 获取班次
     */
    @UnCheck
    public void getWorkshiftmSelect(){
        renderJBoltApiRet(service.getWorkshiftmSelect());
    }
    /***
     * 获取产线
     */
    @UnCheck
    public void getWorkregionmSelect(){
        renderJBoltApiRet(service.getWorkregionmSelect());
    }

}
