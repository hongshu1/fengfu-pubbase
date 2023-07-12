package cn.rjtech.api.qcinspection;

import cn.jbolt.core.api.JBoltApiBaseService;
import cn.jbolt.core.api.JBoltApiRet;
import cn.rjtech.admin.qcinspection.QcInspectionService;
import cn.rjtech.common.upload.UploadController;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;

import java.util.List;

/**
 * @version 1.0
 * @Description 工程内品质巡查api接口
 */
public class QcInspectionApiService extends JBoltApiBaseService {

    @Inject
    private QcInspectionApiService apiService;
    @Inject
    private QcInspectionService qcInspectionService;


    /**
     * 显示主页面数据
     */
    public JBoltApiRet getAdminDatas(int pageSize, int pageNumber, Kv kv) {
        return JBoltApiRet.successWithData(qcInspectionService.paginateAdminDatas(pageNumber,pageSize,kv));
    }


    /**
     * 点击查看/修改按钮，跳转到“查看”页面
     */
    public JBoltApiRet edit(Long iautoid) {
        return JBoltApiRet.successWithData(qcInspectionService.getqcinspectionApi(iautoid));
    }

    /**
     * 点击保存，保存页面明细信息
     */
    public JBoltApiRet update(Kv formRecord) {
        qcInspectionService.updateEditTable(formRecord);
        return JBoltApiRet.API_SUCCESS;
    }

    /**
     * 删除工程内品质巡查表
     * */
    public JBoltApiRet deletes(String ids) {
        qcInspectionService.deleteByBatchIds(ids);
        return JBoltApiRet.API_SUCCESS;
    }

    /**
     * 查询人员和部门
     * */
    public JBoltApiRet DepartmentList(Kv kv) {
        List<Record> list = qcInspectionService.DepartmentList(kv);
        return JBoltApiRet.API_SUCCESS_WITH_DATA(list);
    }



    /**
     * 上传文件
     */
    public JBoltApiRet flies(UploadFile file, String folder, Integer type) {
        UploadController uploadController = new UploadController();
        uploadController.uploadFile(file,folder,type);
        return JBoltApiRet.API_SUCCESS;
    }

}
