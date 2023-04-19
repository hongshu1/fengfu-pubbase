package cn.rjtech.admin.rcvdocqcformm;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.aop.Inject;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.jbolt.common.config.JBoltUploadFolder;
import cn.jbolt.core.para.JBoltPara;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.jbolt.core.util.JBoltRealUrlUtil;
import cn.jbolt.extend.config.ExtendUploadFolder;
import cn.rjtech.base.controller.BaseAdminController;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt._admin.permission.PermissionKey;

import com.jfinal.core.JFinal;
import com.jfinal.core.Path;
import com.jfinal.aop.Before;

import cn.jbolt._admin.interceptor.JBoltAdminAuthInterceptor;

import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.upload.UploadFile;

import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.RcvDocQcFormM;

/**
 * 质量管理-来料检
 *
 * @ClassName: RcvDocQcFormMAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-11 11:44
 */
@CheckPermission(PermissionKey.RCVDOCQCFORMM)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/rcvdocqcformm", viewPath = "/_view/admin/rcvdocqcformm")
public class RcvDocQcFormMAdminController extends BaseAdminController {

    @Inject
    private RcvDocQcFormMService service;

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
//		renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getKeywords(), getBoolean("isCompleted"), getBoolean("isCpkSigned"), getBoolean("isOk"), getBoolean("IsDeleted")));
    }

    /**
     * 检验
     */
    public void checkout() {
        RcvDocQcFormM rcvDocQcFormM = service.findById(getLong(0));
        set("rcvDocQcFormM", rcvDocQcFormM);
        render("checkout.html");
    }

    /**
     * 查看
     */
    public void onlysee() {
        RcvDocQcFormM rcvDocQcFormM = service.findById(getLong(0));
        set("rcvDocQcFormM", rcvDocQcFormM);
        render("onlysee.html");
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
        renderJson(service.save(getModel(RcvDocQcFormM.class, "rcvDocQcFormM")));
    }

    /**
     * 编辑
     */
    public void edit() {
        RcvDocQcFormM rcvDocQcFormM = service.findById(getLong(0));
        if (rcvDocQcFormM == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("rcvDocQcFormM", rcvDocQcFormM);
        render("edit.html");
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(getModel(RcvDocQcFormM.class, "rcvDocQcFormM")));
    }

    /**
     * 批量删除
     */
    public void deleteByIds() {
        renderJson(service.deleteByIds(get("ids")));
    }

    /**
     * 删除
     */
    public void delete() {
        renderJson(service.deleteById(getLong(0)));
    }

    /**
     * 切换isCompleted
     */
    public void toggleIsCompleted() {
        renderJson(service.toggleBoolean(getLong(0), "isCompleted"));
    }

    /**
     * 切换isCpkSigned
     */
    public void toggleIsCpkSigned() {
        renderJson(service.toggleBoolean(getLong(0), "isCpkSigned"));
    }

    /**
     * 切换isOk
     */
    public void toggleIsOk() {
        renderJson(service.toggleBoolean(getLong(0), "isOk"));
    }

    /**
     * 导入图片
     */
    public void uploadImage() {
        String uploadPath = JBoltUploadFolder.todayFolder(ExtendUploadFolder.EXTEND_ITEMMASTER_EDITOR_IMAGE + "/inventory" + "/");
        renderJsonData(service.uploadImage(getFiles(ExtendUploadFolder.EXTEND_ITEMMASTER_EDITOR_IMAGE + "/inventory" + "/")));
    }

    /*
     * 保存或编辑检验表
     * */
    public void updateTable(JBoltPara JboltPara) {
        renderJson(service.updateEditTable(JboltPara));
    }


    /*
     * 生成
     * */
    public void createchecklist() {
        //todo 生成检查表
    }
}
