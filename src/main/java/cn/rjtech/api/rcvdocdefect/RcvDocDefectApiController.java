package cn.rjtech.api.rcvdocdefect;

import cn.jbolt.core.permission.UnCheck;
import cn.rjtech.base.controller.BaseApiController;
import cn.rjtech.entity.vo.RcDocDefect.RcDocDefectVo;
import cn.rjtech.entity.vo.base.NullDataResult;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.Kv;
import io.github.yedaxia.apidocs.ApiDoc;
import java.math.BigDecimal;


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
     * @param selectparam 搜索条件
     */
    @ApiDoc(result = RcDocDefectVo.class)
    @UnCheck
    public void datas(@Para(value = "pageNumber") Integer pageNumber,
                         @Para(value = "pageSize") Integer pageSize,
                         @Para(value = "selectparam") String selectparam) {
        ValidationUtils.validateIdInt(pageNumber,"页码");
        ValidationUtils.validateIdInt(pageSize,"每页显示条数");
        
        renderJBoltApiRet(rcvDocDefectApiService.getAdminDatas(pageNumber,pageSize, Kv.by("selectparam",selectparam)));
    }

    /**
     * 查询明细表
     */
    @ApiDoc(result = RcDocDefectVo.class)
    @UnCheck
    public void addlist(@Para(value = "iautoid") Long iautoid,
                    @Para(value = "ircvdocqcformmid") Long ircvdocqcformmid,
                    @Para(value = "type") String type) {
        renderJBoltApiRet(rcvDocDefectApiService.add(iautoid, ircvdocqcformmid, type));
    }

    /**
     *
     *
     * @param iautoid              来料异常品ID
     * @param ircvdocqcformmid     来料表ID
     * @param capproach            处置区分
     * @param idqqty               不良数量
     * @param iresptype            责任区：1. 供应商 2. 其他
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
        renderJBoltApiRet(rcvDocDefectApiService.update(kv));
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
        String code = rcvDocDefectApiService.rcvDocDefectId(iautoid);
        renderQrCode(code, width, height);
    }


}
