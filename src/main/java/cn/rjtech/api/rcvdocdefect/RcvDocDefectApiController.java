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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 来料异常品管理api接口
 */
@ApiDoc
public class RcvDocDefectApiController extends BaseApiController {

    @Inject
    private RcvDocDefectApiService rcvDocDefectApiService;

    /**
     * 查询主表明细
     * @param pageNumber 页码
     * @param pageSize 每页显示条数
     * @param selectParam 搜索条件
     */
    @ApiDoc(result = RcDocDefectVo.class)
    @UnCheck
    public void optionss(@Para(value = "pageNumber") Integer pageNumber,
                         @Para(value = "pageSize") Integer pageSize,
                         @Para(value = "selectParam") String selectParam) {
        ValidationUtils.validateIdInt(pageNumber,"页码");
        ValidationUtils.validateIdInt(pageSize,"每页显示条数");
        
        renderJBoltApiRet(rcvDocDefectApiService.getAdminDatas(pageNumber,pageSize, Kv.by("selectparam",selectParam)));
    }

    /**
     * 查询明细表
     */
    @ApiDoc(result = RcDocDefectVo.class)
    @UnCheck
    public void add(@Para(value = "iautoid") Long iautoid,
                    @Para(value = "istockoutqcformmid") Long istockoutqcformmid,
                    @Para(value = "type") String type) {
        ValidationUtils.notNull(iautoid, JBoltMsg.PARAM_ERROR);
        ValidationUtils.notNull(istockoutqcformmid, JBoltMsg.PARAM_ERROR);
        
        renderJBoltApiRet(rcvDocDefectApiService.add(iautoid, istockoutqcformmid, type));
    }

    /**
     *
     *
     * @param iautoid              来料异常品ID
     * @param ircvdocqcformmid     来料表ID
     * @param capproach            处置区分
     * @param idqqty               不良数量
     * @param iresptype            责任区：1. 本工序 2. 其他
     * @param cbadnesssns          不良项目，字典编码，多个“,”分隔
     * @param cdesc                工序名称
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
                                //质量管理-来料检明细
                                @Para(value = "ircvdocqcformmid") Long ircvdocqcformmid
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
        ValidationUtils.notNull(iautoid, "缺少来料异常品ID");
        ValidationUtils.notNull(ircvdocqcformmid, "缺少来料检ID");
        renderJBoltApiRet(rcvDocDefectApiService.update(kv));
    }

}
