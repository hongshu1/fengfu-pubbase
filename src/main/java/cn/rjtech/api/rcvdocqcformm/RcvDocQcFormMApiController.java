package cn.rjtech.api.rcvdocqcformm;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.extend.config.ExtendUploadFolder;
import cn.rjtech.base.controller.BaseApiController;
import cn.rjtech.entity.vo.base.NullDataResult;
import cn.rjtech.entity.vo.instockqcformm.GetExportExcelVo;
import cn.rjtech.entity.vo.rcvdocqcformm.*;
import cn.rjtech.util.ValidationUtils;

import com.jfinal.aop.Inject;
import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.Kv;

import io.github.yedaxia.apidocs.ApiDoc;

/**
 * @version 1.0
 * @Author cc
 * @Create 2023/4/26 18:00
 * @Description 质量管理-来料检api接口
 */
@ApiDoc
public class RcvDocQcFormMApiController extends BaseApiController {

    @Inject
    private RcvDocQcFormMApiService apiService;

    /**
     * 查询参数如下
     *
     * @param crcvdocqcformno：检验单号 cinvaddcode：存货编码
     *                             cinvcode1：客户部番
     *                             cinvname1：部品名称
     *                             cvenname：供应商名称
     *                             crcvdocno：收料单号
     *                             name：检验员
     *                             istatus：检验结果
     *                             iscompleted：状态
     *                             cqcformname：检验表格名称
     *                             starttime：开始时间   endtime：结束时间
     * @desc 点击左侧导航栏-出库检，显示主页面数据
     */
    @ApiDoc(result = RcvDocQcFormMPageVo.class)
    @UnCheck
    public void getDatas(@Para(value = "crcvdocqcformno") String crcvdocqcformno, @Para(value = "cinvaddcode") String cinvaddcode,
                         @Para(value = "cinvcode1") String cinvcode1, @Para(value = "cinvname1") String cinvname1,
                         @Para(value = "name") String name, @Para(value = "cvenname") String cvenname,
                         @Para(value = "crcvdocno") String crcvdocno, @Para(value = "istatus") String istatus,
                         @Para(value = "iscompleted") String iscompleted, @Para(value = "cqcformname") String cqcformname,
                         @Para(value = "starttime") String starttime, @Para(value = "endtime") String endtime) {
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

    /**
     * 创建参数如下
     *
     * @param iautoid：主表id cqcformname：检验表格名称
     * @desc 点击生成按钮，生成来料检成绩表
     */
    @ApiDoc(result = NullDataResult.class)
    @UnCheck
    public void createTable(@Para(value = "iautoid") Long iautoid, @Para(value = "cqcformname") String cqcformname) {
        ValidationUtils.notNull(iautoid, JBoltMsg.PARAM_ERROR);
        ValidationUtils.notNull(cqcformname, JBoltMsg.PARAM_ERROR);

        renderJBoltApiRet(apiService.createTable(iautoid, cqcformname));
    }

    /**
     * 点击检验按钮，跳转到检验页面
     */
    @ApiDoc(result = RcvDocQcFormMApiCheckOutVo.class)
    @UnCheck
    public void jumpCheckout(@Para(value = "iautoid") Long iautoid) {
        ValidationUtils.notNull(iautoid, JBoltMsg.PARAM_ERROR);
        renderJBoltApiRet(apiService.jumpCheckout(iautoid));
    }

    /**
     * 跳转到"检验"页面后，自动加载检验页面table的数据
     */
    @ApiDoc(result = AutoGetCheckOutTableDatasVo.class)
    @UnCheck
    public void autoGetRcvCheckOutTableDatas(@Para(value = "ircvdocqcformmid") Long ircvdocqcformmid) {
        ValidationUtils.notNull(ircvdocqcformmid, JBoltMsg.PARAM_ERROR);
        renderJBoltApiRet(apiService.autoGetRcvCheckOutTableDatas(ircvdocqcformmid));
    }

    /**
     * 点击查看按钮，跳转到“查看”页面（该页面只能查看，不能编辑）
     */
    @ApiDoc(result = RcvDocQcFormMOnlyseeApiVo.class)
    @UnCheck
    public void jumpOnlysee(@Para(value = "iautoid") Long iautoid) {
        ValidationUtils.notNull(iautoid, JBoltMsg.PARAM_ERROR);
        renderJBoltApiRet(apiService.jumpOnlysee(iautoid));
    }

    /**
     * 跳转到"查看"页面或者“编辑”页面后，自动加载查看页面table的数据
     */
    @ApiDoc(result = AutoGetRcvOnlyseeTableDatasVo.class)
    @UnCheck
    public void autoGetRcvOnlyseeOrEditTableDatas(@Para(value = "iautoid") Long iautoid) {
        ValidationUtils.notNull(iautoid, JBoltMsg.PARAM_ERROR);
        renderJBoltApiRet(apiService.autoGetRcvOnlyseeOrEditTableDatas(iautoid));
    }

    /**
     * 在检验页面点击“确定”按钮，将数据带到后台保存
     */
    @ApiDoc(result = NullDataResult.class)
    @UnCheck
    public void saveCheckOut(@Para(value = "cmeasurepurpose") String cmeasurepurpose, @Para(value = "cdcno") String cdcno,
                             @Para(value = "ircvdocqcformmiautoid") Long ircvdocqcformmiautoid,
                             @Para(value = "cmeasureunit") String cmeasureunit, @Para(value = "isok") String isok,
                             @Para(value = "cmeasurereason") String cmeasurereason,
                             @Para(value = "serializeSubmitList") String serializeSubmitList,
                             @Para(value = "cmemo") String cmemo) {
        ValidationUtils.notNull(cmeasurepurpose, JBoltMsg.PARAM_ERROR);
        ValidationUtils.notNull(ircvdocqcformmiautoid, JBoltMsg.PARAM_ERROR);
        ValidationUtils.notNull(serializeSubmitList, JBoltMsg.PARAM_ERROR);
        ValidationUtils.notNull(isok, JBoltMsg.PARAM_ERROR);
        ValidationUtils.notNull(cmeasureunit, JBoltMsg.PARAM_ERROR);

        renderJBoltApiRet(apiService.saveCheckOut(cmeasurepurpose, cdcno, ircvdocqcformmiautoid, cmeasureunit,
            isok, cmeasurereason, serializeSubmitList, cmemo));
    }

    /*
     * 点击编辑按钮，跳转到编辑页面
     * */
    @ApiDoc(result = RcvDocQcFormMOnlyseeApiVo.class)
    @UnCheck
    public void jumpEdit(@Para(value = "iautoid") Long iautoid) {
        ValidationUtils.notNull(iautoid, JBoltMsg.PARAM_ERROR);
        renderJBoltApiRet(apiService.jumpEdit(iautoid));
    }

    /**
     * 在编辑页面点击“确定”按钮，将数据带到后台保存
     *
     * @param cmeasurepurpose：测试目的 0：定期检查，1：初物检查，2：委托测定，3：特别检查
     *                             cdcno：设变号
     *                             ircvdocqcformmiautoid：主表id（PL_RcvDocQcFormM）
     *                             cmeasureunit：测定单位 0：μm，1：㎜
     *                             isok：是否合格 0：合格，1：不合格
     *                             cmemo：备注
     *                             cmeasurereason：测定理由
     *                             serializeSubmitList：table里的数据(传json)
     * @desc：点击“编辑”按钮，在编辑页面点击“确定”按钮，将数据带到后台保存
     */
    @ApiDoc(result = NullDataResult.class)
    @UnCheck
    public void saveEdit(@Para(value = "cmeasurepurpose") String cmeasurepurpose, @Para(value = "cdcno") String cdcno,
                         @Para(value = "ircvdocqcformmiautoid") Long ircvdocqcformmiautoid,
                         @Para(value = "cmeasureunit") String cmeasureunit, @Para(value = "isok") String isok,
                         @Para(value = "cmeasurereason") String cmeasurereason,
                         @Para(value = "serializeSubmitList") String serializeSubmitList, @Para(value = "cmemo") String cmemo) {
        ValidationUtils.notNull(cmeasurepurpose, JBoltMsg.PARAM_ERROR);
        ValidationUtils.notNull(ircvdocqcformmiautoid, JBoltMsg.PARAM_ERROR);
        ValidationUtils.notNull(serializeSubmitList, JBoltMsg.PARAM_ERROR);
        ValidationUtils.notNull(isok, JBoltMsg.PARAM_ERROR);
        ValidationUtils.notNull(cmeasureunit, JBoltMsg.PARAM_ERROR);

        renderJBoltApiRet(apiService.saveEdit(cmeasurepurpose, cdcno, ircvdocqcformmiautoid, cmeasureunit,
            isok, cmeasurereason, serializeSubmitList, cmemo));
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
     * 导出详情页
     * */
    @ApiDoc(GetExportExcelVo.class)
    @UnCheck
    public void exportExcel(@Para(value = "iauoid") Long iauoid){
        ValidationUtils.notNull(iauoid, JBoltMsg.PARAM_ERROR);
        renderJBoltApiRet(apiService.getExportData(iauoid));
    }
}
