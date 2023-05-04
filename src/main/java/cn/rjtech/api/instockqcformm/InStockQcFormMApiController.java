package cn.rjtech.api.instockqcformm;

import com.jfinal.aop.Inject;

import cn.rjtech.admin.instockdefect.InStockDefectService;
import cn.rjtech.admin.instockqcformd.InStockQcFormDService;
import cn.rjtech.admin.instockqcformm.InStockQcFormMService;
import cn.rjtech.base.controller.BaseApiController;

/**
 * @version 1.0
 * @Author cc
 * @Create 2023/5/4 14:27
 * @Description 在库检
 */
public class InStockQcFormMApiController extends BaseApiController {

    @Inject
    private InStockQcFormMService service;
    @Inject
    private InStockQcFormMApiService apiService;
    @Inject
    private InStockDefectService defectService;
    @Inject
    private InStockQcFormDService inStockQcFormDService;
}
