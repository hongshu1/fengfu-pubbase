package cn.rjtech.api.momoinvbatch;

import cn.jbolt.core.permission.UnCheck;
import cn.rjtech.admin.momaterialscanusedlog.MoMaterialscanusedlogmService;
import cn.rjtech.admin.momoinvbatch.MoMoinvbatchService;
import cn.rjtech.admin.specmaterialsrcvm.SpecMaterialsRcvMService;
import cn.rjtech.api.modoc.ModocApiService;
import cn.rjtech.base.controller.BaseApiController;
import cn.rjtech.entity.vo.modoc.ModocApiPageVo;
import cn.rjtech.entity.vo.modoc.ModocApiResVo;
import cn.rjtech.entity.vo.rcvdocqcformm.RcvDocGetCoperationnameByModocIdVo;
import cn.rjtech.entity.vo.rcvdocqcformm.RcvDocGetMoroutingsopByInventoryroutingconfigIdVo;
import cn.rjtech.util.ValidationUtils;
import com.alibaba.fastjson.JSON;
import com.jfinal.aop.Inject;
import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.Kv;
import io.github.yedaxia.apidocs.ApiDoc;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 现品票
 */
@ApiDoc
public class MoMoinvbatchApiController extends BaseApiController {
    @Inject
    private MoMoinvbatchApiService service;
    @Inject
    MoMoinvbatchService moMoinvbatchService;

    /**
     * 获取工单信息（表头）
     *
     * @param imodocid
     */
    @UnCheck
    public void getModocData(@Para(value = "imodocid") Long imodocid) {
        ValidationUtils.notNull(imodocid, "缺少工单主键");
        renderJBoltApiSuccessWithData(service.getModocData(imodocid));
    }

    /**
     * 获取现成票信息（表体）
     */
    public void getMoMoinvbatchDatas(@Para(value = "imodocid") Long imodocid,
                                     @Para(value = "iprintstatus") Integer iprintstatus) {
        ValidationUtils.notNull(imodocid, "缺少工单主键");
        ValidationUtils.notNull(iprintstatus, "缺少现品票打印状态");
        renderJBoltApiSuccessWithData(moMoinvbatchService.paginateAdminDatas(1, 10000, Kv.by("imodocid", imodocid).set("iprintstatus", iprintstatus)).getList());
    }

    /**
     * 生成现品票
     */
    public void createMomoinvbatch(@Para(value = "imodocid") Long imodocid) {
        ValidationUtils.notNull(imodocid, "缺少工单主键");
        ValidationUtils.isTrue(moMoinvbatchService.createMomoinvbatch(imodocid).isOk(), "生成失败");
        renderJson();
    }

    /**
     * 撤回
     */
    public void withdraw(@Para(value = "iautoid") Long iautoid) {
        ValidationUtils.notNull(iautoid, "缺少现品票主键");
        ValidationUtils.isTrue(moMoinvbatchService.withdraw(iautoid).isOk(), "生成失败");
        renderJson();
    }

    /**
     * 修改数量
     */
    public void updateNumber(@Para(value = "iautoid") Long iautoid,
                             @Para(value = "newqty") Integer newQty) {
        ValidationUtils.notNull(iautoid, "缺少现品票主键");
        ValidationUtils.notNull(newQty, "缺少新数量");
        ValidationUtils.isTrue(moMoinvbatchService.updateNumber(iautoid, new BigDecimal(newQty)).isOk(), "修改数量失败");
        renderJson();
    }

    /**
     * 批量打印
     */
    public void batchPrint(@Para(value = "imodocid") String imodocid, @Para(value = "ids") String ids) {
        ValidationUtils.notNull(imodocid, "缺少工单主键");
        ValidationUtils.notNull(ids, "缺少现品票主键集合");
        ValidationUtils.isTrue(moMoinvbatchService.batchPrint(imodocid, ids).isOk(), "批量打印更新数据失败");
        renderJson();
    }
}
