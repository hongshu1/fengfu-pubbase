package cn.rjtech.admin.subjectd;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.Subjectd;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;

/**
 * 科目对照细表 Controller
 *
 * @ClassName: SubjectdAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-11-01 13:52
 */
@UnCheck
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/subjectd", viewPath = "/_view/admin/subjectd")
public class SubjectdAdminController extends BaseAdminController {

    @Inject
    private SubjectdService service;

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
        renderJsonData(service.paginateAdminDatas(getPageNumber(), getPageSize(), getKeywords()));
    }

    /**
     * 新增
     */
    public void add() {
        render("add.html");
    }

    /**
     * 编辑
     */
    public void edit() {
        Subjectd subjectd = service.findById(useIfPresent(getLong(0)));
        ValidationUtils.notNull(subjectd, JBoltMsg.DATA_NOT_EXIST);

        set("subjectd", subjectd);
        render("edit.html");
    }

    /**
     * 保存
     */
    public void save() {
        renderJson(service.save(useIfValid(Subjectd.class, "subjectd")));
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(useIfValid(Subjectd.class, "subjectd")));
    }

    /**
     * 批量删除
     */
    public void deleteByIds() {
        renderJson(service.deleteByBatchIds(get("ids")));
    }


}
