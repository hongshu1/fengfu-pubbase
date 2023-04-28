package cn.rjtech.api.instockdefect;

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
 * 工段（产线） API
 *
 * @author Kephon
 */
@ApiDoc
public class InStockDefectApiController extends BaseApiController {

    @Inject
    private InStockDefectApiService service;

    /**
     * 查询主表明细
     *
     * @param cdocno
     * @param imodocid
     * @param cinvcode
     * @param cinvcode1
     * @param cinvname
     * @param istatus
     * @param startdate
     * @param enddate
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
                         @Para(value = "enddate") String enddate) {
        Okv kv = Okv.by("cdocno", cdocno)
                .set("imodocid", imodocid)
                .set("cinvcode", cinvcode)
                .set("cinvcode1", cinvcode1)
                .set("cinvname", cinvname)
                .set("istatus", istatus)
                .set("startdate", startdate)
                .set("enddate", enddate);

        renderJBoltApiRet(service.AdminDatas(getPageSize(), getPageNumber(), kv));
    }

    /**
     * 查询明细表
     */
    @ApiDoc(result = RcDocDefectVo.class)
    @UnCheck
    public void add(@Para(value = "iautoid") Long iautoid,
                    @Para(value = "iinstockqcformmid") Long iinstockqcformmid,
                    @Para(value = "type") String type) {
        ValidationUtils.notNull(iautoid, JBoltMsg.PARAM_ERROR);
        ValidationUtils.notNull(iinstockqcformmid, JBoltMsg.PARAM_ERROR);
        renderJBoltApiRet(service.add(iautoid, iinstockqcformmid, type));
    }

    /**
     * 更新保存
     */
    @ApiDoc(result = RcDocDefectVo.class)
    @UnCheck
    public void updateEditTable(@Para(value = "iautoid") Long iautoid,
                                @Para(value = "capproach") String capproach,
                                @Para(value = "idqqty") BigDecimal idqqty,
                                @Para(value = "iresptype") Integer iresptype,
                                @Para(value = "isfirsttime") Boolean isfirsttime,
                                @Para(value = "cbadnesssns") String cbadnesssns,
                                @Para(value = "cdesc") String cdesc,
                                // 质量管理-来料检明细
                                @Para(value = "iinstockqcformmid") Long iinstockqcformmid,
                                @Para(value = "iCustomerId") Long iCustomerId,
                                @Para(value = "iInventoryId") Long iInventoryId,
                                @Para(value = "iupdateby") Long iupdateby,
                                @Para(value = "dupdatetime") Date dupdatetime) {
        ValidationUtils.notNull(iautoid, "缺少在库检ID");
        ValidationUtils.notNull(iCustomerId, "缺少供应商ID");
        ValidationUtils.notNull(iCustomerId, "生产存货ID");

        Kv kv = new Kv();
        kv.set("iautoid", iautoid);
        kv.set("capproach", capproach);
        kv.set("idqqty", idqqty);
        kv.set("iresptype", iresptype);
        kv.set("isfirsttime", isfirsttime);
        kv.set("cbadnesssns", cbadnesssns);
        kv.set("cdesc", cdesc);
        kv.set("iinstockqcformmid", iinstockqcformmid);
        kv.set("iCustomerId", iCustomerId);
        kv.set("iInventoryId", iInventoryId);
        kv.set("iupdateby", iupdateby);
        kv.set("dupdatetime", dupdatetime);
        renderJson(service.update(kv));
    }

}
