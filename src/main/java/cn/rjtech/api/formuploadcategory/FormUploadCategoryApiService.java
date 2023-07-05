package cn.rjtech.api.formuploadcategory;

import cn.jbolt.core.api.JBoltApiBaseService;
import cn.jbolt.core.api.JBoltApiRet;
import cn.rjtech.admin.formuploadcategory.FormUploadCategoryService;
import com.jfinal.aop.Inject;

/**
 * @author yjllzy
 * 记录上传-分类管理
 */
public class FormUploadCategoryApiService extends JBoltApiBaseService {

    @Inject
    private FormUploadCategoryService formUploadCategoryService;

    /**
     * 数据获取
     * @param q
     * @return
     */
    public JBoltApiRet options(String q) {
        return  JBoltApiRet.successWithData(formUploadCategoryService.options(q));
    }
}
