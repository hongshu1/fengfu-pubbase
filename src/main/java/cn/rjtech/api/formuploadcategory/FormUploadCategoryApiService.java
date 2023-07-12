package cn.rjtech.api.formuploadcategory;

import cn.jbolt.core.api.JBoltApiBaseService;
import cn.jbolt.core.api.JBoltApiRet;
import cn.rjtech.admin.formuploadcategory.FormUploadCategoryService;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;

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
    public JBoltApiRet options(String q,Long iworkregionmid) {
        return  JBoltApiRet.successWithData(formUploadCategoryService.options(Kv.by("q",q).set("iworkregionmid",iworkregionmid)));
    }
}
