package cn.rjtech.api.instockqcformm;

import com.jfinal.aop.Inject;
import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.Kv;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.crossorigin.CrossOrigin;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.extend.config.ExtendUploadFolder;
import cn.rjtech.admin.instockdefect.InStockDefectService;
import cn.rjtech.admin.instockqcformd.InStockQcFormDService;
import cn.rjtech.admin.instockqcformm.InStockQcFormMService;
import cn.rjtech.base.controller.BaseApiController;
import cn.rjtech.entity.vo.base.NullDataResult;
import cn.rjtech.entity.vo.instockqcformm.GetExportExcelVo;
import cn.rjtech.entity.vo.instockqcformm.InStockAutoGetCheckOutTableDatasVo;
import cn.rjtech.entity.vo.instockqcformm.InStockAutoGetOnlyseeTableDatasVo;
import cn.rjtech.entity.vo.instockqcformm.InStockQcFormMApiJumpCheckOutVo;
import cn.rjtech.entity.vo.instockqcformm.InStockQcFormMApiJumpOnlySeeVo;
import cn.rjtech.entity.vo.instockqcformm.InStockQcFormMApiPageVo;
import cn.rjtech.util.ValidationUtils;
import io.github.yedaxia.apidocs.ApiDoc;

/**
 * @version 1.0
 * @Author cc
 * @Create 2023/5/4 14:27
 * @Description 在库检
 */
@CrossOrigin
public class InStockQcFormMApiController extends BaseApiController {

    @Inject
    private InStockQcFormMService    service;
    @Inject
    private InStockQcFormMApiService apiService;
    @Inject
    private InStockDefectService     defectService;
    @Inject
    private InStockQcFormDService    inStockQcFormDService;

    /*
     * @desc: 点击左侧导航栏-在库检，显示主页面数据
     *
     * @param cInvQcFormNo 检验单号
     * @param cinvaddcode       存货编码
     * @param cinvcode1         客户部番
     * @param cinvname1         部品名称
     * @param name              检验员
     * @param istatus           检验结果
     * @param iscompleted       状态
     * @param cqcformname       检验表格名称
     * @param starttime         开始时间
     * @param endtime           结束时间
     * */
    @ApiDoc(result = InStockQcFormMApiPageVo.class)
    @UnCheck
    public void getDatas(@Para(value = "cinvqcformno") String cinvqcformno,
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
        kv.set("cinvqcformno", cinvqcformno);
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

    /*
     * @desc: 生成
     * @param iautoid: 主键
     *        cqcformname: 表格名称
     * */
    @ApiDoc(result = NullDataResult.class)
    @UnCheck
    public void createTable(@Para(value = "iautoid") Long iautoid, @Para(value = "cqcformname") String cqcformname) {
        ValidationUtils.notNull(iautoid, JBoltMsg.PARAM_ERROR);
        ValidationUtils.notNull(cqcformname, JBoltMsg.PARAM_ERROR);
        renderJBoltApiRet(apiService.createTable(iautoid, cqcformname));
    }

    /*
     * 点击检验按钮，跳转到checkout页面
     *
     * @param iautoid 主键
     * */
    @ApiDoc(result = InStockQcFormMApiJumpCheckOutVo.class)
    @UnCheck
    public void jumpCheckOut(@Para(value = "iautoid") Long iautoid) {
        ValidationUtils.notNull(iautoid, JBoltMsg.PARAM_ERROR);
        renderJBoltApiRet(apiService.jumpCheckOut(iautoid));
    }

    /*
     * 跳转到checkout页面后，自动加载table数据
     * @param iinstockqcformmid：主表主键
     * */
    @ApiDoc(result = InStockAutoGetCheckOutTableDatasVo.class)
    @UnCheck
    public void autoGetCheckOutTableDatas(@Para(value = "iinstockqcformmid") Long iinstockqcformmid) {
        ValidationUtils.notNull(iinstockqcformmid, JBoltMsg.PARAM_ERROR);
        renderJBoltApiRet(apiService.autoGetCheckOutTableDatas(iinstockqcformmid));
    }

    /*
     * 在检验页面点击确定,保存数据
     */
    @ApiDoc(result = NullDataResult.class)
    @UnCheck
    public void saveCheckOut(@Para(value = "cmeasurepurpose") String cmeasurepurpose,
                             @Para(value = "cdcno") String cdcno,
                             @Para(value = "iinstockqcformmid") Long iinstockqcformmid,
                             @Para(value = "cmeasureunit") String cmeasureunit,
                             @Para(value = "isok") String isok,
                             @Para(value = "cmeasurereason") String cmeasurereason,
                             @Para(value = "serializeSubmitList") String serializeSubmitList,
                             @Para(value = "cmemo") String cmemo) {
        ValidationUtils.notNull(iinstockqcformmid, JBoltMsg.PARAM_ERROR);
        ValidationUtils.notNull(isok, JBoltMsg.PARAM_ERROR);
        renderJBoltApiRet(apiService.saveCheckOut(cmeasurepurpose, cdcno, iinstockqcformmid, cmeasureunit, isok, cmeasurereason,
            serializeSubmitList, cmemo));
    }

    /*
     * 删除在库检查表
     * @param iautoid: PL_InStockQcFormM主表id
     * */
    public void deleteCheckoutByIautoid(@Para(value = "iautoid") Long iautoid) {
        ValidationUtils.notNull(iautoid, JBoltMsg.PARAM_ERROR);
        renderJBoltApiRet(apiService.deleteCheckoutByIautoid(iautoid));
    }

    /**
     * 点击查看按钮，跳转到onlysee页面
     */
    @ApiDoc(result = InStockQcFormMApiJumpOnlySeeVo.class)
    @UnCheck
    public void jumpOnlySee(@Para(value = "iautoid") Long iautoid) {
        ValidationUtils.notNull(iautoid, JBoltMsg.PARAM_ERROR);
        renderJBoltApiRet(apiService.jumpOnlySee(iautoid));
    }

    /**
     * 跳转到onlysee或者edit页面，自动加载table数据
     */
    @ApiDoc(result = InStockAutoGetOnlyseeTableDatasVo.class)
    @UnCheck
    public void autoGetOnlySeeOrEditTableDatas(@Para(value = "iautoid") Long iautoid) {
        ValidationUtils.notNull(iautoid, JBoltMsg.PARAM_ERROR);

        renderJBoltApiRet(apiService.autoGetOnlySeeOrEditTableDatas(iautoid));
    }

    /*
     * 点击编辑按钮，跳转到编辑页面
     * */
    @ApiDoc(result = InStockQcFormMApiJumpOnlySeeVo.class)
    @UnCheck
    public void jumpEdit(@Para(value = "iautoid") Long iautoid) {
        ValidationUtils.notNull(iautoid, JBoltMsg.PARAM_ERROR);
        renderJBoltApiRet(apiService.jumpEdit(iautoid));
    }

    /*
     * 在编辑页面点击确定，保存编辑页面的数据
     *
     * @param cmeasurepurpose     测试目的 0 定期检查，1 初物检查，2 委托测定，3 特别检查
     * @param cdcno               设变号
     * @param iinstockqcformmid 主表id
     * @param cmeasureunit        测定单位 0 μm，1 ㎜
     * @param isok                是否合格 0 合格，1 不合格
     * @param cmemo               备注
     * @param cmeasurereason      测定理由
     * @param serializeSubmitList table里的数据(传json)
     */
    @ApiDoc(result =NullDataResult.class)
    @UnCheck
    public void saveEdit(@Para(value = "cmeasurepurpose") String cmeasurepurpose,
                         @Para(value = "cdcno") String cdcno,
                         @Para(value = "iinstockqcformmid") Long iinstockqcformmid,
                         @Para(value = "cmeasureunit") String cmeasureunit,
                         @Para(value = "isok") String isok,
                         @Para(value = "cmeasurereason") String cmeasurereason,
                         @Para(value = "serializeSubmitList") String serializeSubmitList,
                         @Para(value = "cmemo") String cmemo) {
        ValidationUtils.notNull(cmeasurepurpose, JBoltMsg.PARAM_ERROR);
        ValidationUtils.notNull(iinstockqcformmid, JBoltMsg.PARAM_ERROR);
        ValidationUtils.notNull(isok, JBoltMsg.PARAM_ERROR);
        ValidationUtils.notNull(serializeSubmitList, JBoltMsg.PARAM_ERROR);
        ValidationUtils.notNull(cmeasureunit, JBoltMsg.PARAM_ERROR);

        renderJBoltApiRet(apiService.saveEdit(cmeasurepurpose, cdcno, iinstockqcformmid,
            cmeasureunit, isok, cmeasurereason, serializeSubmitList, cmemo));
    }

    /*
     * 自动加载图片
     * */
    @ApiDoc
    @UnCheck
    public void uploadImage() {
        renderJBoltApiRet(apiService.uploadImage(getFiles(
            ExtendUploadFolder.EXTEND_ITEMMASTER_EDITOR_IMAGE + "/inventory" + "/")));
    }

    /*
     * 导出在库检详情页数据
     * */
    @ApiDoc(GetExportExcelVo.class)
    @UnCheck
    public void exportExcel(@Para(value = "iautoid") Long iautoid){
        ValidationUtils.notNull(iautoid, JBoltMsg.PARAM_ERROR);
        renderJBoltApiRet(apiService.getExportData(iautoid));
    }

}
