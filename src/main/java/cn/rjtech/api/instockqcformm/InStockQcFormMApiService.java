package cn.rjtech.api.instockqcformm;

import com.jfinal.aop.Inject;

import cn.jbolt.core.api.JBoltApiBaseService;
import cn.rjtech.admin.instockdefect.InStockDefectService;
import cn.rjtech.admin.instockqcformd.InStockQcFormDService;
import cn.rjtech.admin.instockqcformm.InStockQcFormMService;

/**
 * @version 1.0
 * @Author cc
 * @Create 2023/5/4 14:27
 * @Description 在库检
 */
public class InStockQcFormMApiService extends JBoltApiBaseService {

    @Inject
    private InStockQcFormMService    service;
    @Inject
    private InStockDefectService     defectService;
    @Inject
    private InStockQcFormDService    inStockQcFormDService;
}
