package cn.rjtech.api.processdefect;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.UnCheck;
import cn.rjtech.base.controller.BaseApiController;
import cn.rjtech.entity.vo.RcDocDefect.RcDocDefectVo;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import io.github.yedaxia.apidocs.ApiDoc;

import java.math.BigDecimal;
import java.util.Date;

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
     */
    @ApiDoc(result = RcDocDefectVo.class)
    @UnCheck
    public void optionss(@Para(value = "cdocno") String cdocno,
                         @Para(value = "imodocid") String imodocid,
                         @Para(value = "cinvcode") String cinvcode,
                         @Para(value = "cinvcode1") String cinvcode1,
                         @Para(value = "cinvname") String cinvname,
                         @Para(value = "istatus") String istatus,
                         @Para(value = "startdate") String startdate,
                         @Para(value = "enddate") String enddate
                         ) {
        Okv kv = Okv.by("cdocno", cdocno).set("imodocid", imodocid).set("cinvcode", cinvcode).set("cinvcode1", cinvcode1)
                .set("cinvname", cinvname).set("istatus", istatus).set("startdate", startdate).set("enddate", enddate);
        renderJBoltApiRet(processdefectapiservice.getAdminDatas(getPageSize(), getPageNumber() ,kv));
    }


    /**
     * 查询明细表
     */
    @ApiDoc(result = RcDocDefectVo.class)
    @UnCheck
    public void add(@Para(value = "iautoid") Long iautoid,
                            @Para(value = "iissueid") Long iissueid,
                            @Para(value = "type") String type) {
        ValidationUtils.notNull(iautoid, JBoltMsg.PARAM_ERROR);
        ValidationUtils.notNull(iissueid, JBoltMsg.PARAM_ERROR);
        renderJBoltApiRet(processdefectapiservice.add(iautoid,iissueid,type));
    }

    /**
     * 更新保存
     */
    @ApiDoc(result = RcDocDefectVo.class)
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
                                @Para(value = "iissueid") Long  iissueid,
                                @Para(value = "imodocid") Long  imodocid,
                                @Para(value = "idepartmentid") Long  idepartmentid,
                                @Para(value = "ddemanddate") Date  ddemanddate
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
        kv.set("imodocid", imodocid);
        kv.set("idepartmentid", idepartmentid);
        kv.set("ddemanddate", ddemanddate);
        ValidationUtils.notNull(iautoid, "缺少领料单ID");
        ValidationUtils.notNull(imodocid, "缺少生产工单ID");
        ValidationUtils.notNull(idepartmentid, "生产部门ID");
        renderJson(processdefectapiservice.update(kv));
    }


}
