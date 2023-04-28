package cn.rjtech.api.RcvDocDefect;

import cn.jbolt.core.permission.UnCheck;
import cn.rjtech.base.controller.BaseApiController;
import cn.rjtech.entity.vo.base.NullDataResult;
import com.jfinal.aop.Inject;
import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.Okv;
import io.github.yedaxia.apidocs.ApiDoc;

/**
 * 工段（产线） API
 *
 * @author Kephon
 */
@ApiDoc
public class RcvDocDefectApiController extends BaseApiController {

    @Inject
    private RcvDocDefectApiService rcvDocDefectApiService;
    /**
     * 查询主表明细
     **/
    @ApiDoc(result = NullDataResult.class)
    @UnCheck
    public void optionss(@Para(value = "cDocNo") String cDocNo,
                         @Para(value = "iMoDocId") String iMoDocId,
                         @Para(value = "cInvCode") String cInvCode,
                         @Para(value = "iInventoryId") String iInventoryId,
                         @Para(value = "cInvName") String cInvName,
                         @Para(value = "iStatus") String iStatus,
                         @Para(value = "startdate") String startdate,
                         @Para(value = "enddate") String enddate
                         ) {
        Okv kv = Okv.by("cDocNo", cDocNo).set("iMoDocId", iMoDocId).set("cInvCode", cInvCode).set("iInventoryId", iInventoryId)
                .set("cInvName", cInvName).set("iStatus", iStatus).set("startdate", startdate).set("enddate", enddate);
        renderJBoltApiRet(rcvDocDefectApiService.AdminDatas(getPageSize(), getPageNumber() ,kv));
    }


    public void updateEditTable() {
        renderJson(rcvDocDefectApiService.update(getJBoltTable(), getKv()));
    }




}
