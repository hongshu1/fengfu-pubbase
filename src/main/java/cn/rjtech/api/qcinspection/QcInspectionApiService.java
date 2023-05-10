package cn.rjtech.api.qcinspection;

import cn.jbolt.core.api.JBoltApiBaseService;
import cn.jbolt.core.api.JBoltApiRet;
import cn.rjtech.admin.qcinspection.QcInspectionService;
import cn.rjtech.admin.rcvdocdefect.RcvDocDefectService;
import cn.rjtech.admin.rcvdocqcformd.RcvDocQcFormDService;
import cn.rjtech.admin.rcvdocqcformdline.RcvdocqcformdLineService;
import cn.rjtech.admin.rcvdocqcformm.RcvDocQcFormMService;
import cn.rjtech.common.upload.UploadController;
import cn.rjtech.entity.vo.qcinspection.QcInspectionDatas;
import cn.rjtech.entity.vo.qcinspection.QcInspectionVo;
import cn.rjtech.entity.vo.rcvdocqcformm.RcvDocQcFormMApiCheckOut;
import cn.rjtech.entity.vo.rcvdocqcformm.RcvDocQcFormMApiCheckOutVo;
import cn.rjtech.entity.vo.rcvdocqcformm.RcvDocQcFormMOnlyseeApi;
import cn.rjtech.entity.vo.rcvdocqcformm.RcvDocQcFormMOnlyseeApiVo;
import cn.rjtech.model.momdata.QcInspection;
import cn.rjtech.model.momdata.RcvDocQcFormM;
import com.alibaba.fastjson.JSON;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;

import java.util.ArrayList;
import java.util.List;

/**
 * @version 1.0
 * @Author cc
 * @Create 2023/4/27 17:41
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
        return JBoltApiRet.API_SUCCESS_WITH_DATA(qcInspectionService.paginateAdminDatas(pageNumber,pageSize,kv));
    }


    /**
     * 点击查看/修改按钮，跳转到“查看”页面
     */
    public JBoltApiRet edit(Long iautoid) {
        //1、查询跳转到另一页面需要的数据
        QcInspection qcInspection = qcInspectionService.findById(iautoid);
        String supplierInfoId;
        if (qcInspection.getCMeasureAttachments() != null){
            supplierInfoId = qcInspection.getCMeasureAttachments();
        } else {
            supplierInfoId = "0";
        }
        List<Record> files = qcInspectionService.getFilesById(supplierInfoId);

        //2、set到实体类
        QcInspectionDatas qcInspectionDatas = new QcInspectionDatas();
        qcInspectionDatas.setQcInspection(qcInspection);
        qcInspectionDatas.setFiles(files);
        //3、最后返回vo
        QcInspectionVo qcInspectionVo = new QcInspectionVo();
        qcInspectionVo.setCode(0);
        qcInspectionVo.setData(qcInspectionDatas);
        return JBoltApiRet.API_SUCCESS_WITH_DATA(qcInspectionVo);
    }

    /**
     * 点击保存，保存页面明细信息
     */
    public JBoltApiRet update(Kv formRecord) {
        return JBoltApiRet.successWithData(qcInspectionService.updateEditTable(formRecord));
    }

    /**
     * 删除工程内品质巡查表
     * */
    public JBoltApiRet deletes(String ids) {
        return JBoltApiRet.successWithData(qcInspectionService.deleteByBatchIds(ids));
    }

    /**
     * 上传文件
     */
    public JBoltApiRet flies(UploadFile file, String folde, Integer type) {
        UploadController uploadController = new UploadController();
        uploadController.uploadFile(file,folde,type);
        return JBoltApiRet.API_SUCCESS_WITH_DATA(uploadController);
    }

}
