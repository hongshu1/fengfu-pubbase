package cn.rjtech.api.formuploadm;

import cn.jbolt.core.api.JBoltApiBaseService;
import cn.jbolt.core.api.JBoltApiRet;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.rjtech.admin.formuploadd.FormUploadDService;
import cn.rjtech.admin.formuploadm.FormUploadMService;
import cn.rjtech.model.momdata.FormUploadM;
import com.alibaba.fastjson.JSON;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;

import java.util.Date;

/**
 * 记录上传 API Service
 *
 * @author yjllzy
 */
public class FormUploadMApiService extends JBoltApiBaseService {
    
    @Inject
    private FormUploadMService formUploadMService;
    @Inject
    private FormUploadDService formUploadDService;

    /**
     * 首页数据
     */
    public JBoltApiRet AdminDatas(Integer pageNumber, Integer pageSize, Kv kv) {
        return JBoltApiRet.successWithData(formUploadMService.getApiAdminDatas(pageNumber, pageSize, kv));
    }

    /**
     * 状态修改
     */
    public JBoltApiRet batchHandle(Kv kv, int i) {
        formUploadMService.batchHandle(kv, i);
        return JBoltApiRet.API_SUCCESS;
    }

    /**
     * 批量保存
     */
    public JBoltApiRet saveTableSubmit(Long iautoid, String iworkregionmid, String icategoryid, Date ddate, String formuploaddsv) {
        return JBoltApiRet.successWithData(formUploadMService.saveTableSubmitApi(iautoid, iworkregionmid, icategoryid, ddate, JSON.parseArray(formuploaddsv)));

    }

    /**
     * 删除
     */
    public JBoltApiRet delete(Long iautoid) {
        formUploadMService.delete(iautoid);
        return JBoltApiRet.API_SUCCESS;
    }

    /**
     * 列表行审核
     */
    public JBoltApiRet auditStateUpdate(String iautoid, Integer auditstate) {
        FormUploadM formUploadM = formUploadMService.findById(iautoid);
        formUploadM.setIAuditStatus(auditstate);
        formUploadM.setIAuditBy(JBoltUserKit.getUserId());
        formUploadM.setDAuditTime(new Date());
        formUploadM.setCAuditName(JBoltUserKit.getUserName());
        formUploadM.update();
        return JBoltApiRet.API_SUCCESS;
    }

    /**
     * 详情
     */
    public JBoltApiRet details(Integer pageNumber, Integer pageSize, Kv iautoid) {
        return JBoltApiRet.successWithData(formUploadMService.detailsApi(pageNumber, pageSize, iautoid));
    }

    /**
     * 行删除
     */
    public JBoltApiRet delete2(Long iautoid) {
        formUploadDService.delete2(iautoid);
        return JBoltApiRet.API_SUCCESS;
    }

}
