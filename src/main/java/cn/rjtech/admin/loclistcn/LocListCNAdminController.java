package cn.rjtech.admin.loclistcn;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.LocListCN;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;

import java.util.List;


/**
 * 地区 Controller
 *
 * @ClassName: LocListCNAdminController
 * @author: RJ
 * @date: 2023-03-27 11:03
 */
@UnCheck
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/loclistcn", viewPath = "/_view/admin/loclistcn")
public class LocListCNAdminController extends BaseAdminController {

    @Inject
    private LocListCNService service;

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
        LocListCN locListCN = service.findById(getLong(0));
        if (locListCN == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("locListCN", locListCN);
        render("edit.html");
    }

    /**
     * 保存
     */
    public void save() {
        renderJson(service.save(getModel(LocListCN.class, "locListCN")));
    }

    /**
     * 更新
     */
    public void update() {
        renderJson(service.update(getModel(LocListCN.class, "locListCN")));
    }

    /**
     * 批量删除
     */
    public void deleteByIds() {
        renderJson(service.deleteByBatchIds(get("ids")));
    }

    public void QQI18NLocListSave() {
        renderJson(service.QQI18NLocListSave());
    }

    /**
     * 删除
     */
    public void delete() {
        renderJson(service.delete(getLong(0)));
    }

    public void findByIPid() {
        Long ipid = 0L;
        String ipid1 = get("ipid");
        if (isOk(get("ccountry"))) {
            ipid = getLong("ccountry");
        } else if (isOk(get("cprovince"))) {
            ipid = getLong("cprovince");
        } else if (isOk(get("ccity"))) {
            ipid = getLong("ccity");
        }
        List<LocListCN> list = service.findByIPid(ipid);
        renderJsonData(list);
    }

    public void findByNameChild() {
        String name = null;
        if (isOk(get("ccountry"))) { //
            name = get("ccountry");
        } else if (isOk(get("cprovince"))) {
            name = get("cprovince");
        } else if (isOk(get("ccity"))) {
            name = get("ccity");
        }
        List<LocListCN> byNameChild = service.findByNameChild(name);
        renderJsonData(byNameChild);
    }
    
}
