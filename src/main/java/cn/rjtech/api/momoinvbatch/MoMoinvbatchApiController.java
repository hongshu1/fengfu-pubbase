package cn.rjtech.api.momoinvbatch;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.admin.momoinvbatch.MoMoinvbatchService;
import cn.rjtech.base.controller.BaseApiController;
import cn.rjtech.entity.vo.base.NullDataResult;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.core.paragetter.Para;
import io.github.yedaxia.apidocs.ApiDoc;

import java.math.BigDecimal;

/**
 * 现品票
 */
@CheckPermission(PermissionKey.API_MOMOINVBATCH)
@UnCheckIfSystemAdmin
@ApiDoc
public class MoMoinvbatchApiController extends BaseApiController {
    @Inject
    private MoMoinvbatchApiService service;
    @Inject
    private MoMoinvbatchService moMoinvbatchService;

    /**
     * 获取工单信息（表头）
     *
     * @param imodocid
     */
    @ApiDoc(result = NullDataResult.class)
    @UnCheck
    public void getModocData(@Para(value = "imodocid") Long imodocid) {
        ValidationUtils.notNull(imodocid, "缺少工单主键");
        renderJBoltApiRet(service.getModocData(imodocid));
    }

    /**
     * 获取现成票信息（表体）
     */
    @ApiDoc(result = NullDataResult.class)
    @UnCheck
    public void getMoMoinvbatchDatas(@Para(value = "imodocid") Long imodocid,
                                     @Para(value = "iprintstatus") Integer iprintstatus) {
        ValidationUtils.notNull(imodocid, "缺少工单主键");
        ValidationUtils.notNull(iprintstatus, "缺少现品票打印状态");
        renderJBoltApiRet(service.getMoMoinvbatchDatas(imodocid, iprintstatus));
    }

    /**
     * 生成现品票
     */
    @ApiDoc(result = NullDataResult.class)
    @CheckPermission(PermissionKey.API_CREATEMOMOINVBATCH)
    public void createMomoinvbatch(@Para(value = "imodocid") Long imodocid) {
        ValidationUtils.notNull(imodocid, "缺少工单主键");
        ValidationUtils.isTrue(moMoinvbatchService.createMomoinvbatch(imodocid).isOk(), "生成失败");
        renderJBoltApiSuccess();
    }

    /**
     * 撤回
     */
    @ApiDoc(result = NullDataResult.class)
    @CheckPermission(PermissionKey.API_WITHDRAW)
    public void withdraw(@Para(value = "iautoid") Long iautoid) {
        ValidationUtils.notNull(iautoid, "缺少现品票主键");
        ValidationUtils.isTrue(moMoinvbatchService.withdraw(iautoid).isOk(), "生成失败");
        renderJBoltApiSuccess();
    }

    /**
     * 修改数量
     */
    @ApiDoc(result = NullDataResult.class)
    @CheckPermission(PermissionKey.API_UPDATENUMBER)
    public void updateNumber(@Para(value = "iautoid") Long iautoid,
                             @Para(value = "newqty") Integer newQty) {
        ValidationUtils.notNull(iautoid, "缺少现品票主键");
        ValidationUtils.notNull(newQty, "缺少新数量");
        ValidationUtils.isTrue(moMoinvbatchService.updateNumber(iautoid, new BigDecimal(newQty)).isOk(), "修改数量失败");
        renderJBoltApiSuccess();
    }

    /**
     * 批量打印
     */
    @ApiDoc(result = NullDataResult.class)
    @CheckPermission(PermissionKey.API_BATCHPRINT)
    public void batchPrint(@Para(value = "imodocid") String imodocid, 
                           @Para(value = "ids") String ids) {
        ValidationUtils.notNull(imodocid, "缺少工单主键");
        ValidationUtils.notNull(ids, "缺少现品票主键集合");
        ValidationUtils.isTrue(moMoinvbatchService.batchPrint(imodocid, ids).isOk(), "批量打印更新数据失败");
        
        renderJBoltApiSuccess();
    }

    /**
     * 批量报工
     */
    @ApiDoc(result = NullDataResult.class)
    @CheckPermission(PermissionKey.API_WORKBYIDS)
    public void workByIds(@Para(value = "imodocid") String imodocid, @Para(value = "ids") String ids) {
        ValidationUtils.notNull(imodocid, "缺少工单主键");
        ValidationUtils.notNull(ids, "缺少现品票主键集合");
        
        ValidationUtils.isTrue(moMoinvbatchService.workByIds(imodocid, ids).isOk(), "批量报工更新数据失败");
        renderJBoltApiSuccess();
    }
    
}
