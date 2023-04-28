package cn.rjtech.api.rcvdocqcformm;

import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;

import cn.jbolt.core.api.JBoltApiRet;
import cn.rjtech.admin.rcvdocqcformd.RcvDocQcFormDService;
import cn.rjtech.admin.rcvdocqcformdline.RcvdocqcformdLineService;
import cn.rjtech.admin.rcvdocqcformm.RcvDocQcFormMService;

/**
 * @version 1.0
 * @Author cc
 * @Create 2023/4/27 17:41
 * @Description 质量管理-来料检api接口
 */
public class RcvDocQcFormMApiService {

    @Inject
    private RcvDocQcFormMApiService  apiService;
    @Inject
    private RcvDocQcFormMService     service;              //质量管理-来料检验表
    @Inject
    private RcvDocQcFormDService     rcvDocQcFormDService; //质量管理-来料检单行配置表
    @Inject
    private RcvdocqcformdLineService rcvdocqcformdLineService; //质量管理-来料检明细列值表

    /*
     * 点击左侧导航栏-出库检，显示主页面数据
     * */
    public JBoltApiRet getDatas(Kv kv) {
        return JBoltApiRet.successWithData(service.pageList(kv));
    }


}
