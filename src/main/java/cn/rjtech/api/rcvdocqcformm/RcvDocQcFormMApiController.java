package cn.rjtech.api.rcvdocqcformm;

import com.jfinal.aop.Inject;
import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.Kv;

import cn.jbolt.core.crossorigin.CrossOrigin;
import cn.jbolt.core.permission.UnCheck;
import cn.rjtech.admin.rcvdocqcformd.RcvDocQcFormDService;
import cn.rjtech.admin.rcvdocqcformdline.RcvdocqcformdLineService;
import cn.rjtech.admin.rcvdocqcformm.RcvDocQcFormMService;
import cn.rjtech.base.controller.BaseApiController;
import cn.rjtech.entity.vo.rcvdocqcformm.RcvDocQcFormMPageVo;
import cn.rjtech.entity.vo.stockoutqcformm.StockoutQcFormMPageVo;
import io.github.yedaxia.apidocs.ApiDoc;

/**
 * @version 1.0
 * @Author cc
 * @Create 2023/4/26 18:00
 * @Description 质量管理-来料检api接口
 */
@ApiDoc
public class RcvDocQcFormMApiController  extends BaseApiController {

    @Inject
    private RcvDocQcFormMApiService apiService;
    @Inject
    private RcvDocQcFormMService     service;              //质量管理-来料检验表
    @Inject
    private RcvDocQcFormDService     rcvDocQcFormDService; //质量管理-来料检单行配置表
    @Inject
    private RcvdocqcformdLineService rcvdocqcformdLineService; //质量管理-来料检明细列值表

    /*
     * @desc 点击左侧导航栏-出库检，显示主页面数据
     * @param 查询参数如下
     * crcvdocqcformno：检验单号
     * cinvaddcode：存货编码
     * cinvcode1：客户部番
     * cinvname1：部品名称
     * cvenname：供应商名称
     * crcvdocno：收料单号
     * name：检验员
     * istatus：检验结果
     * iscompleted：状态
     * cqcformname：检验表格名称
     * starttime：开始时间   endtime：结束时间
     * */
    @ApiDoc(RcvDocQcFormMPageVo.class)
    @UnCheck
    public void getDatas(@Para(value = "crcvdocqcformno") String crcvdocqcformno,
                         @Para(value = "cinvaddcode") String cinvaddcode,
                         @Para(value = "cinvcode1") String cinvcode1,
                         @Para(value = "cinvname1") String cinvname1,
                         @Para(value = "name") String name,
                         @Para(value = "cvenname") String cvenname,
                         @Para(value = "crcvdocno") String crcvdocno,
                         @Para(value = "istatus") String istatus,
                         @Para(value = "iscompleted") String iscompleted,
                         @Para(value = "cqcformname") String cqcformname,
                         @Para(value = "starttime") String starttime,
                         @Para(value = "endtime") String endtime) {
        Kv kv = new Kv();
        kv.set("crcvdocqcformno", crcvdocqcformno);
        kv.set("cinvaddcode", cinvaddcode);
        kv.set("cinvcode1", cinvcode1);
        kv.set("cinvname1", cinvname1);
        kv.set("name", name);
        kv.set("istatus", istatus);
        kv.set("iscompleted", iscompleted);
        kv.set("cqcformname", cqcformname);
        kv.set("starttime", starttime);
        kv.set("endtime", endtime);
        kv.set("cvenname", cvenname);
        kv.set("crcvdocno", crcvdocno);
        kv.set("page", 1);
        kv.set("pageSize", 15);
        renderJBoltApiRet(apiService.getDatas(kv));
    }
}
