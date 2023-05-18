package cn.rjtech.admin.cusfieldsmappingdcodingrule;

import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.UnCheck;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.CusfieldsmappingdCodingrule;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;

/**
 * 系统设置-导入字段编码规则
 *
 * @ClassName: CusfieldsmappingdCodingruleAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-02 13:47
 */
@UnCheck
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/cusfieldsmappingdcodingrule", viewPath = "/_view/admin/cusfieldsmappingdcodingrule")
public class CusfieldsmappingdCodingruleAdminController extends BaseAdminController {

    @Inject
    private CusfieldsmappingdCodingruleService service;

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
        renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getLong("icusfieldsmappingdid"), getInt("iType")));
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
        renderJson(service.save(getModel(CusfieldsmappingdCodingrule.class, "cusfieldsmappingdCodingrule")));
    }

    /**
     * 编辑
     */
    public void edit() {
        CusfieldsmappingdCodingrule cusfieldsmappingdCodingrule = service.findById(getLong(0));
        if (cusfieldsmappingdCodingrule == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("cusfieldsmappingdCodingrule", cusfieldsmappingdCodingrule);
        render("edit.html");
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(getModel(CusfieldsmappingdCodingrule.class, "cusfieldsmappingdCodingrule")));
    }

    /**
     * 批量删除
     */
    public void deleteByIds() {
        renderJson(service.deleteByIds(get("ids")));
    }


}
