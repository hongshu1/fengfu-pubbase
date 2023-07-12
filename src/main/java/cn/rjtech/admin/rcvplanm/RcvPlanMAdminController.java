package cn.rjtech.admin.rcvplanm;

import cn.hutool.core.util.StrUtil;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.common.config.JBoltUploadFolder;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.jbolt.core.render.JBoltByteFileType;
import cn.rjtech.admin.customer.CustomerService;
import cn.rjtech.admin.rcvpland.RcvPlanDService;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.Customer;
import cn.rjtech.model.momdata.RcvPlanM;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;

import java.io.File;
import java.util.List;

/**
 * 发货管理-取货计划主表
 *
 * @ClassName: RcvPlanMAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-27 14:09
 */
@UnCheckIfSystemAdmin
@CheckPermission(PermissionKey.RCVPLANM)
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/pickupPlanManage", viewPath = "/_view/admin/rcvplanm")
public class RcvPlanMAdminController extends BaseAdminController {

    @Inject
    private RcvPlanMService service;
    @Inject
    private RcvPlanDService planDService;
    @Inject
    private CustomerService customerService;

    /**
     * 首页
     */
    public void index() {
        render("index.html");
    }

    /**
     * 数据源
     */
    @UnCheck
    public void datas() {
        renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getKv()));
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
//		renderJson(service.save(getModel(RcvPlanM.class, "rcvPlanM"),
//				getModel(RcvPlanD.class, "rcvPlanD") ));
    }

    /**
     * 编辑
     */
    public void edit() {
        RcvPlanM rcvPlanM = service.findById(getLong("iautoid"));
        if (rcvPlanM == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        Record rcvplanm = rcvPlanM.toRecord();
        Customer customer = customerService.findById(rcvPlanM.getICustomerId());
        rcvplanm.set("ccusname", customer == null ? null : customer.getCCusName());
        set("rcvplanm", rcvplanm);
        keepPara();
        render("edit.html");
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(getModel(RcvPlanM.class, "rcvPlanM")));
    }

    /**
     * 批量删除主从表
     */
    public void deleteByIds() {
        renderJson(service.deleteBatchByIds(get("ids")));
    }

    /**
     * 删除主从表
     */
    public void delete() {
        renderJson(service.delete(getLong(0)));
    }

    /**
     * 切换IsDeleted
     */
    public void toggleIsDeleted() {
        renderJson(service.toggleBoolean(getLong(0), "IsDeleted"));
    }


    /**
     * 新增-可编辑表格-批量提交
     */
    public void submitAll() {
        renderJson(service.submitByJBoltTable(getJBoltTable()));
    }


    /**
     * 下载模板
     */
    @UnCheck
    @SuppressWarnings("unchecked")
    public void downloadTpl() throws Exception {
        renderJxls("rcvplanm.xlsx", Kv.create(), "取货计划导入.xlsx");
    }

    /**
     * 导入数据
     */
    public void importExcelData(){
        String uploadPath = JBoltUploadFolder.todayFolder(JBoltUploadFolder.DEMO_JBOLTTABLE_EXCEL);
        List<UploadFile> files = getFiles(uploadPath);
        if (!isOk(files)) {
            renderBootFileUploadFail("文件上传失败!");
            return;
        }
        for (UploadFile file : files) {
            if (notExcel(file)) {
                renderJsonFail("请上传excel文件");
                return;
            }
        }
        renderJson(service.importExcelDatas(files));
    }

    /**
     * 执行导入excel
     */
    public void importExcel() {
        UploadFile uploadFile = getFile("file");
        ValidationUtils.notNull(uploadFile, "上传文件不能为空");
        File file = uploadFile.getFile();
        
        List<String> list = StrUtil.split(uploadFile.getOriginalFileName(), StrUtil.DOT);
        ValidationUtils.equals( list.get(1), JBoltByteFileType.XLSX.suffix, "系统只支持xlsx格式的Excel文件");
        
        renderJsonData(service.importExcel(file));
    }

}
