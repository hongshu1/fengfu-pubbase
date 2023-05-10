package cn.rjtech.admin.dataimport;

import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.rjtech.admin.cusfieldsmappingd.CusFieldsMappingDService;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.upload.UploadFile;

import java.io.File;

/**
 * 数据导入
 *
 * @author Kephon
 */
@Path(value = "/admin/dataimport")
@UnCheck
@Before(JBoltAdminAuthInterceptor.class)
public class DataImportAdminController extends BaseAdminController {

    @Inject
    private CusFieldsMappingDService cusFieldsMappingdService;

    public void importExcelData() {
        String cformatname = get("cformatname");
        ValidationUtils.notBlank(cformatname, "导入格式不能为空");
        
        UploadFile uploadFile = getFile("file");
        ValidationUtils.notNull(uploadFile, "上传文件不能为空");

        File file = uploadFile.getFile();

        renderJson(cusFieldsMappingdService.getImportDatas(file, cformatname));
    }

}
