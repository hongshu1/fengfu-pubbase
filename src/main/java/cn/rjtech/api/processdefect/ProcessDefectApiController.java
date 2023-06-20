package cn.rjtech.api.processdefect;

import cn.jbolt.core.api.OpenAPI;
import cn.jbolt.core.permission.UnCheck;
import cn.rjtech.base.controller.BaseApiController;
import cn.rjtech.entity.vo.base.NullDataResult;
import cn.rjtech.entity.vo.processdefect.ProcessDefect;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.Kv;
import io.github.yedaxia.apidocs.ApiDoc;

import java.math.BigDecimal;

/**
 * 制造异常品管理
 *
 * @author Kephon
 */
@ApiDoc
public class ProcessDefectApiController extends BaseApiController {

    @Inject
    private ProcessDefectApiService processdefectapiservice;


    /**
     * 查询主表明细
     * @param pageNumber         页码
     * @param pageSize          每页显示条数
     * @param cdocno            制造品单号
     * @param imodocid          工单号
     * @param cinvname          部品名称
     * @param cinvcode          存货编码编码
     * @param cinvcode1         客户部番
     * @param istatus           状态
     * @param startdate         开始时间
     * @param enddate          结束时间
     */
    @ApiDoc(result = ProcessDefect.class)
    @UnCheck
    public void datas(@Para(value = "pageNumber",defaultValue = "1") Integer pageNumber,
                      @Para(value = "pageSize",defaultValue = "15") Integer pageSize,
                      @Para(value = "cdocno") String cdocno,
                      @Para(value = "imodocid") String imodocid,
                      @Para(value = "cinvname") String cinvname,
                      @Para(value = "cinvcode") String cinvcode,
                      @Para(value = "cinvcode1") String cinvcode1,
                      @Para(value = "istatus") String istatus,
                      @Para(value = "startdate") String startdate,
                      @Para(value = "enddate") String enddate) {
        ValidationUtils.validateIdInt(pageNumber,"页码");
        ValidationUtils.validateIdInt(pageSize,"每页显示条数");
        Kv kv = new Kv();
        kv.set("cdocno", cdocno);
        kv.set("imodocid", imodocid);
        kv.set("cinvname", cinvname);
        kv.set("cinvcode", cinvcode);
        kv.set("cinvcode1", cinvcode1);
        kv.set("istatus", istatus);
        kv.set("startdate", startdate);
        kv.set("enddate", enddate);
        renderJBoltApiRet(processdefectapiservice.getAdminDatas(pageNumber,pageSize,kv));
    }


    /**
     * 查询明细表
     */
    @ApiDoc(result = ProcessDefect.class)
    @UnCheck
    public void addlist(@Para(value = "iautoid") Long iautoid,
                        @Para(value = "iissueid") Long iissueid,
                        @Para(value = "type") String type) {
        renderJBoltApiRet(processdefectapiservice.add(iautoid, iissueid, type));
    }


    /**
     * @param iautoid              来料异常品ID
     * @param iissueid             制造表ID
     * @param capproach            处置区分
     * @param processname          工序名称
     * @param idqqty               不良数量
     * @param iresptype            责任区：1. 供应商 2. 其他
     * @param cbadnesssns          不良项目，字典编码，多个“,”分隔
     * @param cdesc                工序名称
     */
    @ApiDoc(result = NullDataResult.class)
    @UnCheck
    public void updateEditTable(@Para(value = "iautoid") Long iautoid,
                                @Para(value = "capproach") String  capproach,
                                @Para(value = "processname") String  processname,
                                @Para(value = "idqqty") BigDecimal idqqty,
                                @Para(value = "iresptype") Integer iresptype,
                                @Para(value = "isfirsttime") Boolean isfirsttime,
                                @Para(value = "cbadnesssns") String  cbadnesssns,
                                @Para(value = "cdesc") String  cdesc,
                                //质量管理-来料检明细
                                @Para(value = "iissueid") Long  iissueid
                                ) {
        Kv kv = new Kv();
        kv.set("iautoid", iautoid);
        kv.set("capproach", capproach);
        kv.set("idqqty", idqqty);
        kv.set("iresptype", iresptype);
        kv.set("isfirsttime", isfirsttime);
        kv.set("cbadnesssns", cbadnesssns);
        kv.set("cdesc", cdesc);
        kv.set("processname", processname);
        kv.set("iissueid", iissueid);
        renderJBoltApiRet(processdefectapiservice.update(kv));
    }

    /**
     * 打印二维码
     * @param ids    异常品id 可多个
     */
    @ApiDoc(result = NullDataResult.class)
    @UnCheck
    public void QRCode(@Para(value = "ids") Long ids) {
        Kv kv = new Kv();
        kv.set("ids", ids);
        renderJBoltApiRet(processdefectapiservice.QRCode(kv));
    }

    /**
     * 工单-异常品记录-保存
     *
     * @param iautoid              来料异常品ID
     * @param imodocid             制造表ID-工单ID
     * @param capproach            处置区分
     * @param processname          工序名称
     * @param idqqty               不良数量
     * @param iresptype            责任区：1. 供应商 2. 其他
     * @param cbadnesssns          不良项目，字典编码，多个“,”分隔
     * @param cdesc
     * @param  ioperationid        工序ID
     */
    @ApiDoc(result = NullDataResult.class)
    @UnCheck

    public void editProcessDefect(@Para(value = "iautoid") Long iautoid,
                                @Para(value = "capproach") String  capproach,
                                @Para(value = "processname") String  processname,
                                @Para(value = "idqqty") BigDecimal idqqty,
                                @Para(value = "iresptype") Integer iresptype,
                                @Para(value = "isfirsttime") Boolean isfirsttime,
                                @Para(value = "cbadnesssns") String  cbadnesssns,
                                @Para(value = "cdesc") String  cdesc,

                                @Para(value = "imodocid") Long  imodocid,
                                 @Para(value = "ioperationid") Long  ioperationid
    ) {
        ValidationUtils.notNull(imodocid,"缺少工单ID");
        Kv kv = new Kv();
        kv.set("iautoid", iautoid);
        kv.set("capproach", capproach);
        kv.set("idqqty", idqqty);
        kv.set("iresptype", iresptype);
        kv.set("isfirsttime", isfirsttime);
        kv.set("cbadnesssns", cbadnesssns);
        kv.set("cdesc", cdesc);
        kv.set("processname", processname);
        kv.set("ioperationid", ioperationid);

        kv.set("imodocid", imodocid);
        renderJBoltApiRet(processdefectapiservice.editProcessDefect(kv));
    }

    /**
     * 删除异常品
     * @param iautoid    异常品id
     */
    @ApiDoc(result = NullDataResult.class)
    @UnCheck
    public void deletes(@Para(value = "iautoid") Long iautoid) {
        renderJBoltApiRet(processdefectapiservice.deletes(iautoid));
    }
}
