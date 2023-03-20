package cn.rjtech.admin.qcitem;

import java.util.List;

import com.jfinal.aop.Inject;

import cn.hutool.core.date.DateUtil;
import cn.jbolt.common.config.JBoltUploadFolder;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.jbolt.core.poi.excel.JBoltExcel;
import cn.jbolt.core.poi.excel.JBoltExcelHeader;
import cn.jbolt.core.poi.excel.JBoltExcelMerge;
import cn.jbolt.core.poi.excel.JBoltExcelSheet;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;

import com.jfinal.core.Path;
import com.jfinal.aop.Before;

import cn.jbolt._admin.interceptor.JBoltAdminAuthInterceptor;

import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.upload.UploadFile;

import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.QcItem;
import cn.rjtech.util.Util;

/**
 * 质量建模-检验项目（分类）
 *
 * @ClassName: QcItemAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-17 15:31
 */
@CheckPermission(PermissionKey.QCITEM)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/qcitem", viewPath = "/_view/admin/qcitem")
public class QcItemAdminController extends BaseAdminController {

    @Inject
    private QcItemService service;

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
        renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getKeywords(), getBoolean("isDeleted")));
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
    public void save() {
        renderJson(service.save(getModel(QcItem.class, "qcItem")));
    }

    /**
     * 编辑
     */
    public void edit() {
        QcItem qcItem = service.findById(getLong(0));
        if (qcItem == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("qcItem", qcItem);
        render("edit.html");
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(getModel(QcItem.class, "qcItem")));
    }

    /**
     * 删除
     */
    public void delete() {
        renderJson(service.deleteById(getLong(0)));
    }

    /**
     * 切换isDeleted
     */
    public void toggleIsDeleted() {
        renderJson(service.toggleBoolean(getLong(0), "isDeleted"));
    }

    /*
     * 导出选中
     * */
    public void exportExcelByIds() {
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
        for (Record r : data) {
//			r.put("ilevel",personService.formatPattenSn(r.getStr("ilevel"),"work_level"));
        }
        try {
            renderJxls("检验项目（分类）.xlsx", Kv.by("rows", data), "检验项目（分类）(选中导出)_" + DateUtil.today() + ".xlsx");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * 导出全部
     * */
    public void exportExcelAll() {
        List<Record> rows = service.list(getKv());
        if (notOk(rows)) {
            renderJsonFail("无有效数据导出");
            return;
        }
        for (Record r : rows) {
//			r.put("ilevel",personService.formatPattenSn(r.getStr("ilevel"),"work_level"));
        }
        try {
            renderJxls("检验项目（分类）.xlsx", Kv.by("rows", rows), "检验项目（分类）" + DateUtil.today() + ".xlsx");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
