package cn.rjtech.admin.dataimport;

import cn.hutool.core.util.StrUtil;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.render.JBoltByteFileType;
import cn.rjtech.admin.cusfieldsmappingd.CusFieldsMappingDService;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.upload.UploadFile;

import java.io.File;
import java.util.List;

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

        List<String> list = StrUtil.split(uploadFile.getOriginalFileName(), StrUtil.DOT);

        // 截取最后一个“.”之前的文件名，作为导入格式名1
        String cformatName = list.get(0);

        String extension = list.get(1);
        
        ValidationUtils.equals(extension, JBoltByteFileType.XLSX.suffix, "系统只支持xlsx格式的Excel文件");

        renderJson(cusFieldsMappingdService.getImportDatas(file, cformatName));
    }

}
