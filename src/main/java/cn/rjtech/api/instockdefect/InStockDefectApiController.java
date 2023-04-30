package cn.rjtech.api.instockdefect;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.UnCheck;
import cn.rjtech.base.controller.BaseApiController;
import cn.rjtech.entity.vo.RcDocDefect.RcDocDefectVo;
import cn.rjtech.entity.vo.instockdefect.InStockDefect;
import cn.rjtech.entity.vo.processdefect.ProcessDefect;
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
    private InStockDefectApiService inStockDefectApiService;

    /**
     * 查询主表明细
     *
     */
    @ApiDoc(result = InStockDefect.class)
    @UnCheck
    public void datas(@Para(value = "pageNumber") Integer pageNumber,
                         @Para(value = "pageSize") Integer pageSize,
                         @Para(value = "selectparam") String selectparam){
        ValidationUtils.validateIdInt(pageNumber,"页码");
        ValidationUtils.validateIdInt(pageSize,"每页显示条数");
        renderJBoltApiRet(inStockDefectApiService.AdminDatas(pageNumber,pageSize, Kv.by("selectparam",selectparam)));
    }

    /**
     * 查询明细表
     */
    @ApiDoc(result = InStockDefect.class)
    @UnCheck
    public void add(@Para(value = "iautoid") Long iautoid,
                    @Para(value = "iinstockqcformmid") Long iinstockqcformmid,
                    @Para(value = "type") String type) {
        ValidationUtils.notNull(iautoid, JBoltMsg.PARAM_ERROR);
        ValidationUtils.notNull(iinstockqcformmid, JBoltMsg.PARAM_ERROR);
        renderJBoltApiRet(inStockDefectApiService.add(iautoid, iinstockqcformmid, type));
    }

    /**
     * @param iautoid              来料异常品ID
     * @param iinstockqcformmid    在库检ID
     * @param capproach            处置区分
     * @param isfirsttime          再首发
     * @param idqqty               不良数量
     * @param iresptype            责任区：1. 供应商 2. 其他
     * @param cbadnesssns          不良项目，字典编码，多个“,”分隔
     * @param cdesc                工序名称
     */
    @ApiDoc(result = InStockDefect.class)
    @UnCheck
    public void updateEditTable(@Para(value = "iautoid") Long iautoid,
                                @Para(value = "capproach") String capproach,
                                @Para(value = "idqqty") BigDecimal idqqty,
                                @Para(value = "iresptype") Integer iresptype,
                                @Para(value = "isfirsttime") Boolean isfirsttime,
                                @Para(value = "cbadnesssns") String cbadnesssns,
                                @Para(value = "cdesc") String cdesc,
                                // 质量管理-来料检明细
                                @Para(value = "iinstockqcformmid") Long iinstockqcformmid
                               ) {
        Kv kv = new Kv();
        kv.set("iautoid", iautoid);
        kv.set("capproach", capproach);
        kv.set("idqqty", idqqty);
        kv.set("iresptype", iresptype);
        kv.set("isfirsttime", isfirsttime);
        kv.set("cbadnesssns", cbadnesssns);
        kv.set("cdesc", cdesc);
        kv.set("iinstockqcformmid", iinstockqcformmid);
        renderJBoltApiRet(inStockDefectApiService.update(kv));
    }


}
