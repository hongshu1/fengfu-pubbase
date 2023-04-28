package cn.rjtech.api.rcvdocdefect;

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
 *来料异常品管理
 *
 */
@ApiDoc
public class RcvDocDefectapicontroller extends BaseApiController {

    @Inject
    private RcvDocDefectapiservice rcvDocDefectApiService;
    /**
     * 查询主表明细
     **/
    @ApiDoc(RcDocDefectVo.class)
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
        renderJBoltApiRet(rcvDocDefectApiService.AdminDatas(getPageSize(), getPageNumber() ,kv));
    }


    /**
     * 查询明细表
     **/
    @ApiDoc(RcDocDefectVo.class)
    @UnCheck
    public void add(@Para(value = "iautoid") Long iautoid,
                            @Para(value = "istockoutqcformmid") Long istockoutqcformmid,
                            @Para(value = "type") String type) {
        ValidationUtils.notNull(iautoid, JBoltMsg.PARAM_ERROR);
        ValidationUtils.notNull(istockoutqcformmid, JBoltMsg.PARAM_ERROR);
        renderJBoltApiRet(rcvDocDefectApiService.add(iautoid,istockoutqcformmid,type));
    }

    /**
     * 更新保存
     **/
    @ApiDoc(RcDocDefectVo.class)
    @UnCheck
    public void updateEditTable(@Para(value = "iautoid") Long iautoid,
                                @Para(value = "capproach") String  capproach,
                                @Para(value = "idqqty") BigDecimal idqqty,
                                @Para(value = "iresptype") Integer iresptype,
                                @Para(value = "isfirsttime") Boolean isfirsttime,
                                @Para(value = "cbadnesssns") String  cbadnesssns,
                                @Para(value = "cdesc") String  cdesc,
                                //质量管理-来料检明细
                                @Para(value = "ircvdocqcformmid") Long  ircvdocqcformmid,
                                @Para(value = "iinventoryid") Long  iinventoryid,
                                @Para(value = "ivendorid") Long  ivendorid,
                                @Para(value = "iupdateby") Long  iupdateby,
                                @Para(value = "dUpdateTime") Date dUpdateTime
                                ) {
        Kv kv = new Kv();
        kv.set("iautoid", iautoid);
        kv.set("capproach", capproach);
        kv.set("idqqty", idqqty);
        kv.set("iresptype", iresptype);
        kv.set("isfirsttime", isfirsttime);
        kv.set("cbadnesssns", cbadnesssns);
        kv.set("cdesc", cdesc);
        kv.set("ircvdocqcformmid", ircvdocqcformmid);
        kv.set("iinventoryid", iinventoryid);
        kv.set("ivendorid", ivendorid);
        kv.set("iupdateby", iupdateby);
        kv.set("ivendorid", dUpdateTime);
        ValidationUtils.notNull(ircvdocqcformmid, "缺少来料检ID");
        ValidationUtils.notNull(iinventoryid, "缺少供应商ID");
        ValidationUtils.notNull(ivendorid, "缺少存货ID");
        renderJson(rcvDocDefectApiService.update(kv));
    }


}
