package cn.rjtech.admin.prodparam;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.common.config.JBoltUploadFolder;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.render.JBoltByteFileType;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.constants.DataSourceConstants;
import cn.rjtech.model.momdata.ProdParam;
import cn.rjtech.util.Util;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.plugin.activerecord.tx.TxConfig;
import com.jfinal.upload.UploadFile;

import java.io.File;
import java.util.List;

/**
 * 生产表单建模-生产表单参数
 *
 * @ClassName: ProdParamAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-06-26 13:29
 */
@Before(JBoltAdminAuthInterceptor.class)
@CheckPermission(PermissionKey.PRODPARAM)
@Path(value = "/admin/prodParam", viewPath = "/_view/admin/prodparam")
public class ProdParamAdminController extends BaseAdminController {

    @Inject
    private ProdParamService service;

    /**
     * 首页
     */
    public void index() {
        render("index.html");
    }

    /**
     * 数据源
     */
    public void datas() {
        renderJsonData(service.pageList(getKv()));
        //renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getKeywords(), getBoolean("isEnabled"), getBoolean("isDeleted")));
    }

    /**
     * 新增
     */
    @CheckPermission(PermissionKey.PRODPARAM_ADD)
    public void add() {
        render("add.html");
    }

    /**
     * 保存
     */
    @Before(Tx.class)
    @TxConfig(DataSourceConstants.MOMDATA)
    @CheckPermission(PermissionKey.PRODPARAM_ADD)
    public void save(@Para("prodParam") ProdParam prodParam) {
        renderJson(service.save(prodParam));
    }

    /**
     * 编辑
     */
    @CheckPermission(PermissionKey.PRODPARAM_EDIT)
    public void edit() {
        ProdParam prodParam = service.findById(getLong(0));
        if (prodParam == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("prodParam", prodParam);
        render("edit.html");
    }

    /**
     * 更新
     */
    @Before(Tx.class)
    @TxConfig(DataSourceConstants.MOMDATA)
    @CheckPermission(PermissionKey.PRODPARAM_EDIT)
    public void update(@Para("prodParam") ProdParam prodParam) {
        renderJson(service.update(prodParam));
    }

    /**
     * 批量删除
     */
    @Before(Tx.class)
    @TxConfig(DataSourceConstants.MOMDATA)
    public void deleteByIds() {
        renderJson(service.deleteByIds(get("ids")));
    }

    /**
     * 删除
     */
    @Before(Tx.class)
    @TxConfig(DataSourceConstants.MOMDATA)
    @CheckPermission(PermissionKey.PRODPARAM_DELETE)
    public void delete() {
        renderJson(service.deleteById(getLong(0)));
    }

    /**
     * 切换isEnabled
     */
    @Before(Tx.class)
    @TxConfig(DataSourceConstants.MOMDATA)
    public void toggleIsEnabled() {
        renderJson(service.toggleBoolean(getLong(0), "isEnabled"));
    }

    /**
     * 切换isDeleted
     */
    @Before(Tx.class)
    @TxConfig(DataSourceConstants.MOMDATA)
    public void toggleIsDeleted() {
        renderJson(service.toggleBoolean(getLong(0), "isDeleted"));
    }

    @SuppressWarnings("unchecked")
    @CheckPermission(PermissionKey.PRODPARAM_EXPORT)
    public void exportExcelByIds() throws Exception {
        String ids = get("ids");
        if (notOk(ids)) {
            renderJsonFail("未选择有效数据，无法导出");
            return;
        }
        List<Record> data = service.list(Kv.create().setIfNotBlank("ids", Util.getInSqlByIds(ids)));
        if (notOk(data)) {
            renderJsonFail("无有效数据导出");
            return;
        }
        renderJxls("prodparam.xlsx", Kv.by("rows", data), "生产表单参数(选中导出)_" + DateUtil.today() + ".xlsx");
    }

    @SuppressWarnings("unchecked")
    @CheckPermission(PermissionKey.PRODPARAM_EXPORT)
    public void exportExcelAll() throws Exception {
        List<Record> rows = service.list(getKv());
        if (notOk(rows)) {
            renderJsonFail("无有效数据导出");
            return;
        }
        renderJxls("prodparam.xlsx", Kv.by("rows", rows), "生产表单参数" + DateUtil.today() + ".xlsx");
    }


    /**
     * 模板下载
     */
    @SuppressWarnings("unchecked")
    public void downloadTpl() {
        try {
            renderJxls("newprodparam.xlsx", Kv.by("rows", null), "生产表单参数.xlsx");
        } catch (Exception e) {
            ValidationUtils.error("模板下载失败");
        }
    }
    @CheckPermission(PermissionKey.PRODPARAM_IMPORT)
    public void importExcelClass() {
        UploadFile uploadFile = getFile("file");
        ValidationUtils.notNull(uploadFile, "上传文件不能为空");

        File file = uploadFile.getFile();

        List<String> list = StrUtil.split(uploadFile.getOriginalFileName(), StrUtil.DOT);

        // 截取最后一个“.”之前的文件名，作为导入格式名
        String cformatName = list.get(0);

        String extension = list.get(1);

        ValidationUtils.equals(extension, JBoltByteFileType.XLSX.suffix, "系统只支持xlsx格式的Excel文件");
        renderJson(service.importExcelClass(file, cformatName));
    }
}
