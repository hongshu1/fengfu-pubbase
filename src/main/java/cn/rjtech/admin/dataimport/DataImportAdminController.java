package cn.rjtech.admin.dataimport;

import cn.hutool.core.util.StrUtil;
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
        UploadFile uploadFile = getFile("file");
        ValidationUtils.notNull(uploadFile, "上传文件不能为空");

        File file = uploadFile.getFile();

        // 截取最后一个“.”之前的文件名，作为导入格式名
        String cformatName = StrUtil.subBefore(uploadFile.getOriginalFileName(), StrUtil.DOT, true);

        renderJson(cusFieldsMappingdService.getImportDatas(file, cformatName));
    }

}
