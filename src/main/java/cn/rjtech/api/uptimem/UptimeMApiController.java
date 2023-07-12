package cn.rjtech.api.uptimem;

import cn.jbolt.core.permission.UnCheck;
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
                            @Para(value = "iauditstatus") String iauditstatus,
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
        Kv kv = Kv.by("mid",iautoid).set("iautoid",iautoid);
        renderJBoltApiRet(service.getUptimeMInfoList(kv));
    }


    /**
     * 补焊纪录保存及审核接口
     * @param updateOrSaveType 新增或者编辑，新增传1，编辑传0
     * @param data {  //表头数据json串，新增时iautoid为空
     *                 "iautoid":"1674253970028417024",  //主键id
     *                 "ibasemins":55,                   //基本稼动时间（分钟）
     *                 "iotmins":10,                     //加班时间（分钟）
     *                 "irate1":"54.545455",             //无机种切换稼动率
     *                 "irate2":"18.181818",             //有机种切换稼动率
     *                 "istopmins":25,                   //不稼动时间（分钟）
     *                 "iswitchmins":20,                 //机种切换时间（分钟）
     *                 "iworkmins":20,                   //机种切换时间（分钟）
     *                 "iworkregionmid":"1663734219389444096",  //产线id
     *                 "iworkshiftmid":"1646683347910529024",   //班次id
     *                 "iauditstatus":0                         //保存或审核，保存传0，审核传1
     *             }
     *
     * @param dataList [  //表格数据json串，新增时iautoid为空
     *                     {
     *                         "iautoid":"1674253970569482240",    //表格主键id
     *                         "imins":5,                          //时间（分钟）
     *                         "istdmins":8,                       //设定值（分钟）
     *                         "iuptimecategoryid":"1673585765055537152",  //分类名称id
     *                         "iuptimeparamid":"1673585877248974848",     //项目名称id
     *                         "iseq":1                                    //顺序值
     *                     }
     *                 ]
     */
    @UnCheck
    public void updateAndSaveApi(@Para(value = "updateOrSaveType") int updateOrSaveType,
                                 @Para(value = "data") String data,
                                 @Para(value = "dataList") String dataList) {
        ValidationUtils.notNull(updateOrSaveType,"保存类型不能为空！");
        renderJBoltApiRet(service.updateAndSaveApi(updateOrSaveType,data,dataList));
    }

    /***
     * 撤回主纪录
     */
    @UnCheck
    public void revocationUptimeMById(@Para(value = "iautoid") Long iautoid){
        ValidationUtils.validateId(iautoid,"主键id");
        renderJBoltApiRet(service.revocationUptimeMById(iautoid));
    }

    /***
     * 删除主纪录
     */
    @UnCheck
    public void deleteUptimeMById(@Para(value = "iautoid") Long iautoid){
        ValidationUtils.validateId(iautoid,"主键id");
        renderJBoltApiRet(service.deleteUptimeMById(iautoid));
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
