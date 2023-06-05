package cn.rjtech.api.formuploadm;

import cn.jbolt.core.permission.UnCheck;
import cn.rjtech.base.controller.BaseApiController;
import cn.rjtech.entity.vo.base.NullDataResult;
import cn.rjtech.entity.vo.formuploadm.FormUpload;
import cn.rjtech.entity.vo.formuploadm.FormUploadVo;
import com.jfinal.aop.Inject;
import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.Kv;
import io.github.yedaxia.apidocs.ApiDoc;

/**
 * 记录上传api接口
 * @author yjllzy
 */
@ApiDoc
public class FormUploadMApiController  extends BaseApiController {

    @Inject
    private  FormUploadMApiService service;

    /**
     * 页面数据
     */
    @ApiDoc(result = FormUploadVo.class)
    @UnCheck
    public void  datas(@Para(value = "pageNumber",defaultValue = "1") Integer pageNumber,
                       @Para(value = "pageSize",defaultValue = "15") Integer pageSize,
                       @Para(value = "icategoryid") String icategoryid,
                       @Para(value = "iworkregionmid") String iworkregionmid,
                       @Para(value = "ccreatename") String ccreatename,
                       @Para(value = "dcreatetime") String dcreatetime,
                       @Para(value = "ecreatetime") String ecreatetime){
        Kv kv = new Kv();
        kv.set("icategoryid",icategoryid);
        kv.set("iworkregionmid",iworkregionmid);
        kv.set("ccreatename",ccreatename);
        kv.set("dcreatetime",dcreatetime);
        kv.set("ecreatetime",ecreatetime);
        renderJBoltApiRet(service.AdminDatas(pageNumber,pageSize,kv));
    }

    /**
     *批量审核
     */
    @ApiDoc(result = NullDataResult.class)
    @UnCheck
    public void batchAudit() {
        renderJBoltApiRet(service.batchHandle(getKv(), 2));
    }

    /**
     * 批量反审
     */
    @ApiDoc(result = NullDataResult.class)
    @UnCheck
    public void batchAntiAudit() {
        renderJBoltApiRet(service.batchHandle(getKv(), 1));
    }

    /**
     * 批量保存
     */
    @ApiDoc(result = NullDataResult.class)
    @UnCheck
    public void saveTableSubmit(){
        renderJBoltApiRet(service.saveTableSubmit(getKv()));
    }

    /**
     * 删除
     */
    @ApiDoc(result = NullDataResult.class)
    @UnCheck
    public void delete(@Para(value = "iautoid") Long iautoid){
        renderJBoltApiRet(service.delete(iautoid));

    }
    /**
     * 修改状态
     */
    @ApiDoc(result = NullDataResult.class)
    @UnCheck
    public void auditStateUpdate(@Para(value = "iautoid") String iautoid,
                                 @Para(value = "auditstate") Integer auditstate ){
        renderJBoltApiRet(service.auditStateUpdate(iautoid,auditstate));
    }

    /**
     * 详情数据
     */
    @ApiDoc(result = FormUpload.class)
    public void details(@Para(value = "pageNumber",defaultValue = "1") Integer pageNumber,
                        @Para(value = "pageSize",defaultValue = "15") Integer pageSize,
                        @Para(value = "iautoid") String iautoid){
        renderJBoltApiRet(service.details(pageNumber,pageSize,  Kv.by("iautoid",iautoid)));
    }
}
