package cn.rjtech.admin.prodparam;

import cn.hutool.core.date.DateUtil;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.common.config.JBoltUploadFolder;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.constants.DataSourceConstants;
import cn.rjtech.model.momdata.ProdParam;
import cn.rjtech.util.Util;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.plugin.activerecord.tx.TxConfig;
import com.jfinal.upload.UploadFile;

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
    public void add() {
        render("add.html");
    }

    /**
     * 保存
     */
    @Before(Tx.class)
    @TxConfig(DataSourceConstants.MOMDATA)
    public void save(@Para("prodParam") ProdParam prodParam) {
        renderJson(service.save(prodParam));
    }

    /**
     * 编辑
     */
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
    public void exportExcelAll() throws Exception {
        List<Record> rows = service.list(getKv());
        if (notOk(rows)) {
            renderJsonFail("无有效数据导出");
            return;
        }
        renderJxls("prodparam.xlsx", Kv.by("rows", rows), "生产表单参数" + DateUtil.today() + ".xlsx");
    }

    public void importExcelClass() {
        String uploadPath = JBoltUploadFolder.todayFolder(JBoltUploadFolder.DEMO_JBOLTTABLE_EXCEL);
        UploadFile file = getFile("file", uploadPath);
        if (notExcel(file)) {
            renderJsonFail("请上传excel文件");
            return;
        }
        renderJson(service.importExcelClass(file.getFile()));
    }
}
