package cn.rjtech.api.formuploadcategory;

import cn.jbolt.core.permission.UnCheck;
import cn.rjtech.base.controller.BaseApiController;
import cn.rjtech.entity.vo.formuploadcategory.FormUploadCategoryVo;
import com.jfinal.aop.Inject;
import com.jfinal.core.paragetter.Para;
import io.github.yedaxia.apidocs.ApiDoc;

/**
 * 记录上传-分类管理
 * @author yjllzy
 */
@ApiDoc
@UnCheck
public class FormUploadCategoryApiController extends BaseApiController {

    @Inject
    private FormUploadCategoryApiService service;


    /**
     * 数据获取
     */
    @ApiDoc(result = FormUploadCategoryVo.class)
    public void  options(@Para(value = "q") String q){
        renderJBoltApiRet(service.options(q));
    }
}
