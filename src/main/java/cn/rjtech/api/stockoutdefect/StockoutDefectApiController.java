package cn.rjtech.api.stockoutdefect;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.UnCheck;
import cn.rjtech.base.controller.BaseApiController;
import cn.rjtech.entity.vo.base.NullDataResult;
import cn.rjtech.entity.vo.stockoutdefect.StockoutDefectVo;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.Kv;
import io.github.yedaxia.apidocs.ApiDoc;

import java.math.BigDecimal;

/**
 * 出库异常品管理api接口
 */
@ApiDoc
public class StockoutDefectApiController extends BaseApiController {

    @Inject
    private StockoutDefectApiService stockoutDefectApiService;

    /**
     * 查询主表明细
     * @param pageNumber 页码
     * @param pageSize 每页显示条数
     * @param selectparam 搜索条件
     */
    @ApiDoc(result = StockoutDefectVo.class)
    @UnCheck
    public void datas(@Para(value = "pageNumber") Integer pageNumber,
                      @Para(value = "pageSize") Integer pageSize,
                      @Para(value = "selectparam") String selectparam,
                      @Para(value = "starttime") String starttime, @Para(value = "endtime") String endtime) {
        ValidationUtils.validateIdInt(pageNumber,"页码");
        ValidationUtils.validateIdInt(pageSize,"每页显示条数");
        Kv kv = new Kv();
        kv.set("selectparam", selectparam);
        kv.set("starttime", starttime);
        kv.set("endtime", endtime);
        renderJBoltApiRet(stockoutDefectApiService.getAdminDatas(pageNumber,pageSize,kv));
    }

    /**
     * 查询明细表
     */
    @ApiDoc(result = StockoutDefectVo.class)
    @UnCheck
    public void addlist(@Para(value = "iautoid") Long iautoid,
                    @Para(value = "stockoutqcformmid") Long stockoutqcformmid,
                    @Para(value = "type") String type) {
        ValidationUtils.notNull(iautoid, JBoltMsg.PARAM_ERROR);
        ValidationUtils.notNull(stockoutqcformmid, JBoltMsg.PARAM_ERROR);
        renderJBoltApiRet(stockoutDefectApiService.add(iautoid, stockoutqcformmid, type));
    }

    /**
     *
     *
     * @param iautoid              来料异常品ID
     * @param stockoutqcformmid     在库表ID
     * @param capproach            处置区分
     * @param idqqty               不良数量
     * @param iresptype            责任区：1. 工程内 2. 其他
     * @param cbadnesssns          不良项目，字典编码，多个“,”分隔
     * @param isfirsttime          首发再发
     */
    @ApiDoc(result = NullDataResult.class)
    @UnCheck
    public void updateEditTable(@Para(value = "iautoid") Long iautoid,
                                @Para(value = "capproach") String capproach,
                                @Para(value = "idqqty") BigDecimal idqqty,
                                @Para(value = "iresptype") Integer iresptype,
                                @Para(value = "isfirsttime") Boolean isfirsttime,
                                @Para(value = "cbadnesssns") String cbadnesssns,
                                @Para(value = "cdesc") String cdesc,
                                //质量管理-来料检明细
                                @Para(value = "stockoutqcformmid") Long stockoutqcformmid
                               ) {
        Kv kv = new Kv();
        kv.set("iautoid", iautoid);
        kv.set("capproach", capproach);
        kv.set("idqqty", idqqty);
        kv.set("iresptype", iresptype);
        kv.set("isfirsttime", isfirsttime);
        kv.set("cbadnesssns", cbadnesssns);
        kv.set("cdesc", cdesc);
        kv.set("ircvdocqcformmid", stockoutqcformmid);
        renderJBoltApiRet(stockoutDefectApiService.update(kv));
    }

    /**
     * 二维码
     * @param iautoid     来料异常品ID
     * @param width       宽
     * @param height      高
     */
    @ApiDoc(result = NullDataResult.class)
    @UnCheck
    public void qrcode(
            @Para(value = "width", defaultValue = "200") Integer width,
            @Para(value = "height", defaultValue = "200") Integer height,
            @Para(value = "iautoid") Long iautoid){
        String code = stockoutDefectApiService.stockoutDefectId(iautoid);
        renderQrCode(code, width, height);
    }

}
