package cn.rjtech.admin.prodformd;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.constants.DataSourceConstants;
import cn.rjtech.model.momdata.ProdFormD;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.plugin.activerecord.tx.TxConfig;

/**
 * 制造管理-生产表单行配置
 *
 * @ClassName: ProdFormDAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-06-28 18:59
 */
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/prodFormD", viewPath = "/_view/admin/prodformd")
public class ProdFormDAdminController extends BaseAdminController {

    @Inject
    private ProdFormDService service;

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
        renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getInt("iType")));
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
    public void save(@Para("prodFormD") ProdFormD prodFormD) {
        renderJson(service.save(prodFormD));
    }

    /**
     * 编辑
     */
    public void edit() {
        ProdFormD prodFormD = service.findById(getLong(0));
        if (prodFormD == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("prodFormD", prodFormD);
        render("edit.html");
    }

    /**
     * 更新
     */
    @Before(Tx.class)
    @TxConfig(DataSourceConstants.MOMDATA)
    public void update(@Para("prodFormD") ProdFormD prodFormD) {
        renderJson(service.update(prodFormD));
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
