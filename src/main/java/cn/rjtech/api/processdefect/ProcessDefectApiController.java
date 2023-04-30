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
     * @param pageNumber 页码
     * @param pageSize 每页显示条数
     * @param selectParam 搜索条件
     */
    @ApiDoc(result = RcDocDefectVo.class)
    @UnCheck
    public void datas(@Para(value = "pageNumber") Integer pageNumber,
                         @Para(value = "pageSize") Integer pageSize,
                         @Para(value = "selectParam") String selectParam) {
        ValidationUtils.validateIdInt(pageNumber,"页码");
        ValidationUtils.validateIdInt(pageSize,"每页显示条数");
        renderJBoltApiRet(processdefectapiservice.getAdminDatas(pageNumber,pageSize, Kv.by("selectparam",selectParam)));
    }


    /**
     * 查询明细表
     */
    @ApiDoc(result = RcDocDefectVo.class)
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
        renderJson(processdefectapiservice.update(kv));
    }


}
