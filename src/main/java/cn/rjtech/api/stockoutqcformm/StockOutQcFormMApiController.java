package cn.rjtech.api.stockoutqcformm;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.crossorigin.CrossOrigin;
import cn.jbolt.core.permission.UnCheck;
import cn.rjtech.base.controller.BaseApiController;
import cn.rjtech.entity.vo.base.NullDataResult;
import cn.rjtech.entity.vo.instockqcformm.GetExportExcelVo;
import cn.rjtech.entity.vo.instockqcformm.InStockAutoGetCheckOutTableDatasVo;
import cn.rjtech.entity.vo.rcvdocqcformm.RcvDocQcFormMApiCheckOutVo;
import cn.rjtech.entity.vo.stockoutqcformm.StockoutQcFormMPageVo;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.Kv;
import io.github.yedaxia.apidocs.ApiDoc;

/**
 * 质量管理-出库检api接口
 */
@CrossOrigin
public class StockOutQcFormMApiController extends BaseApiController {

    @Inject
    private StockOutQcFormMApiService apiService;

    /**
     * 点击左侧导航栏-出库检，显示主页面数据
     *
     * @param cstockoutqcformno 检验单号
     * @param cinvaddcode       存货编码
     * @param cinvcode1         客户部番
     * @param cinvname1         部品名称
     * @param name              检验员
     * @param istatus           检验结果
     * @param iscompleted       状态
     * @param cqcformname       检验表格名称
     * @param starttime         开始时间
     * @param endtime           结束时间
     */
    @ApiDoc(result = StockoutQcFormMPageVo.class)
    @UnCheck
    public void getDatas(@Para(value = "cstockoutqcformno") String cstockoutqcformno,
                         @Para(value = "cinvaddcode") String cinvaddcode,
                         @Para(value = "cinvcode1") String cinvcode1,
                         @Para(value = "cinvname1") String cinvname1,
                         @Para(value = "name") String name,
                         @Para(value = "istatus") String istatus,
                         @Para(value = "iscompleted") String iscompleted,
                         @Para(value = "cqcformname") String cqcformname,
                         @Para(value = "starttime") String starttime,
                         @Para(value = "endtime") String endtime,
                         @Para(value = "page") String page,
                         @Para(value = "pageSize") String pageSize) {
        Kv kv = new Kv();
        kv.set("cstockoutqcformno", cstockoutqcformno);
        kv.set("cinvaddcode", cinvaddcode);
        kv.set("cinvcode1", cinvcode1);
        kv.set("cinvname1", cinvname1);
        kv.set("name", name);
        kv.set("istatus", istatus);
        kv.set("iscompleted", iscompleted);
        kv.set("cqcformname", cqcformname);
        kv.set("starttime", starttime);
        kv.set("endtime", endtime);
        kv.set("page", page);
        kv.set("pageSize", pageSize);
        renderJBoltApiRet(apiService.getDatas(kv));
    }

    /**
     * 点击生成按钮，生成出库成绩表
     *
     * @param iautoid 主表id
     */
    @ApiDoc(result = NullDataResult.class)
    @UnCheck
    public void createTable(@Para(value = "iautoid") Long iautoid) {
        ValidationUtils.notNull(iautoid, JBoltMsg.PARAM_ERROR);

        renderJBoltApiRet(apiService.createTable(iautoid));
    }

    /**
     * 点击检验按钮，跳转到检验页面
     *
     * @param iautoid 主键
     */
    @ApiDoc(result = RcvDocQcFormMApiCheckOutVo.class)
    @UnCheck
    public void jumpCheckout(@Para(value = "iautoid") Long iautoid) {
        ValidationUtils.notNull(iautoid, JBoltMsg.PARAM_ERROR);
        renderJBoltApiRet(apiService.jumpCheckout(iautoid));
    }

    /**
     * 点击查看按钮，跳转到“查看”页面（该页面只能查看，不能编辑）
     *
     * @param iautoid 主键
     */
    @ApiDoc(result = RcvDocQcFormMApiCheckOutVo.class)
    @UnCheck
    public void jumpOnlysee(@Para(value = "iautoid") Long iautoid) {
        ValidationUtils.notNull(iautoid, JBoltMsg.PARAM_ERROR);

        renderJBoltApiRet(apiService.jumpOnlysee(iautoid));
    }

    /**
     * 点击编辑按钮，跳转到编辑页面
     */
    @ApiDoc(result = RcvDocQcFormMApiCheckOutVo.class)
    @UnCheck
    public void jumpEdit(@Para(value = "iautoid") Long iautoid) {
        ValidationUtils.notNull(iautoid, JBoltMsg.PARAM_ERROR);
        renderJBoltApiRet(apiService.jumpEdit(iautoid));
    }

    /**
     * 跳转到详情页面，获取table数据
     *
     * @param istockoutqcformmid 主表主键
     */
    @ApiDoc(result = InStockAutoGetCheckOutTableDatasVo.class)
    @UnCheck
    public void getTableDatas(@Para(value = "istockoutqcformmid") Long istockoutqcformmid) {
        ValidationUtils.notNull(istockoutqcformmid, JBoltMsg.PARAM_ERROR);
        renderJBoltApiRet(apiService.getTableDatas(istockoutqcformmid));
    }

    /**
     * 点击“检验”按钮，在检验页面点击“确定”按钮，将数据带到后台保存
     */
    @ApiDoc(result = NullDataResult.class)
    @UnCheck
    public void saveCheckOut(@Para(value = "cmeasurepurpose") String cmeasurepurpose,
                             @Para(value = "cdcno") String cdcno,
                             @Para(value = "stockqcformmiautoid") Long stockqcformmiautoid,
                             @Para(value = "cmeasureunit") String cmeasureunit,
                             @Para(value = "isok") String isok,
                             @Para(value = "cmeasurereason") String cmeasurereason,
                             @Para(value = "serializeSubmitList") String serializeSubmitList,
                             @Para(value = "cmemo") String cmemo,
                             @Para(value = "cbatchno") String cbatchno) {
        ValidationUtils.notNull(cmeasurepurpose, JBoltMsg.PARAM_ERROR);
        ValidationUtils.notNull(stockqcformmiautoid, JBoltMsg.PARAM_ERROR);
        ValidationUtils.notNull(serializeSubmitList, JBoltMsg.PARAM_ERROR);
        ValidationUtils.notNull(isok, JBoltMsg.PARAM_ERROR);
        ValidationUtils.notNull(cmeasureunit, JBoltMsg.PARAM_ERROR);

        renderJBoltApiRet(apiService.saveCheckOut(cmeasurepurpose, cdcno, stockqcformmiautoid, cmeasureunit,
                isok, cmeasurereason, serializeSubmitList, cmemo, cbatchno));
    }

    /**
     * 点击“编辑”按钮，在编辑页面点击“确定”按钮，将数据带到后台保存
     *
     * @param cmeasurepurpose     测试目的 0 定期检查，1 初物检查，2 委托测定，3 特别检查
     * @param cdcno               设变号
     * @param stockqcformmiautoid 主表id（PL_StockoutQcFormM）
     * @param cmeasureunit        测定单位 0 μm，1 ㎜
     * @param isok                是否合格 0 合格，1 不合格
     * @param cmemo               备注
     * @param cmeasurereason      测定理由
     * @param serializeSubmitList table里的数据(传json)
     */
    @ApiDoc(NullDataResult.class)
    @UnCheck
    public void saveEdit(@Para(value = "cmeasurepurpose") String cmeasurepurpose,
                         @Para(value = "cdcno") String cdcno,
                         @Para(value = "stockqcformmiautoid") Long stockqcformmiautoid,
                         @Para(value = "cmeasureunit") String cmeasureunit,
                         @Para(value = "isok") String isok,
                         @Para(value = "cmeasurereason") String cmeasurereason,
                         @Para(value = "serializeSubmitList") String serializeSubmitList,
                         @Para(value = "cmemo") String cmemo,
                         @Para(value = "cbatchno") String cbatchno) {
        ValidationUtils.notNull(cmeasurepurpose, JBoltMsg.PARAM_ERROR);
        ValidationUtils.notNull(stockqcformmiautoid, JBoltMsg.PARAM_ERROR);
        ValidationUtils.notNull(serializeSubmitList, JBoltMsg.PARAM_ERROR);
        ValidationUtils.notNull(isok, JBoltMsg.PARAM_ERROR);
        ValidationUtils.notNull(cmeasureunit, JBoltMsg.PARAM_ERROR);

        renderJBoltApiRet(apiService.saveEdit(cmeasurepurpose, cdcno, stockqcformmiautoid, cmeasureunit, isok,
                cmeasurereason, serializeSubmitList, cmemo, cbatchno));
    }

    /**
     * 导出详情页
     */
    @ApiDoc(GetExportExcelVo.class)
    @UnCheck
    public void exportExcel(@Para(value = "iauoid") Long iauoid) throws Exception {
        ValidationUtils.notNull(iauoid, JBoltMsg.PARAM_ERROR);
        renderJBoltApiRet(apiService.getExportData(iauoid));
    }

}
