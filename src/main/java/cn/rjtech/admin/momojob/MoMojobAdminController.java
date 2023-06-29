package cn.rjtech.admin.momojob;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.constants.DataSourceConstants;
import cn.rjtech.model.momdata.MoMojob;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.plugin.activerecord.tx.TxConfig;

/**
 * 制造工单-生产任务
 *
 * @ClassName: MoMojobAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-06-27 15:24
 */
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/moMojob", viewPath = "/_view/admin/momojob")
public class MoMojobAdminController extends BaseAdminController {

    @Inject
    private MoMojobService service;

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
        renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize()));
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
    public void save(@Para("moMojob") MoMojob moMojob) {
        renderJson(service.save(moMojob));
    }

    /**
     * 编辑
     */
    public void edit() {
        MoMojob moMojob = service.findById(getLong(0));
        if (moMojob == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("moMojob", moMojob);
        render("edit.html");
    }

    /**
     * 更新
     */
    @Before(Tx.class)
    @TxConfig(DataSourceConstants.MOMDATA)
    public void update(@Para("moMojob") MoMojob moMojob) {
        renderJson(service.update(moMojob));
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


}
