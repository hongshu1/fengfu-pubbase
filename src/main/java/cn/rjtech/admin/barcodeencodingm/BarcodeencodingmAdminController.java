package cn.rjtech.admin.barcodeencodingm;

import java.util.Date;
import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.para.jbolttablemenufilter.JBoltTableMenuFilter;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.admin.barcodeencodingd.BarcodeencodingdService;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.enums.ItemEnum;
import cn.rjtech.model.momdata.Barcodeencodingm;
import cn.rjtech.util.ValidationUtils;

/**
 * 销售报价单明细扩展自定义项管理 Controller
 *
 * @ClassName: BarcodeencodingmAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-04-15 15:46
 */
@UnCheckIfSystemAdmin
@CheckPermission(PermissionKey.BARCODEENCODINGM)
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/barcodeencodingm", viewPath = "/_view/admin/barcodeencodingm")
public class BarcodeencodingmAdminController extends BaseAdminController {

    @Inject
    private BarcodeencodingmService service;
    @Inject
    private BarcodeencodingdService barcodeencodingdService;

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
    public void datas(JBoltTableMenuFilter menuFilter) {
        Page<Record> recordPage = service.paginateAdminList(getPageNumberWithMenuFilter(menuFilter), getPageSizeWithMenuFilter(menuFilter), getKv());
        List<Record> list = recordPage.getList();
        for (Record record : list) {
            Long iautoid = record.getLong("iautoid");
            String tag = barcodeencodingdService.getTag(iautoid);
            record.set("tag", tag);
        }
        renderJsonData(recordPage);
    }

    /**
     * 新增
     */
    public void add() {
        Barcodeencodingm barcodeencodingm = new Barcodeencodingm();
        barcodeencodingm.setCorgcode(getOrgCode());

        set("barcodeencodingm", barcodeencodingm);
        set("isupdate", false);
        set("isadd", true);
        render("add.html");
    }

    /**
     * 编辑
     */
    public void edit() {
        Barcodeencodingm barcodeencodingm = service.findById(useIfPresent(getLong(0)));
        ValidationUtils.notNull(barcodeencodingm, JBoltMsg.DATA_NOT_EXIST);
        set("barcodeencodingm", barcodeencodingm);
        set("isupdate", true);
        render("edit.html");
    }

    /**
     * 保存
     */
    public void save() {
        Barcodeencodingm barcodeencodingm = getModel(Barcodeencodingm.class, "barcodeencodingm");
        barcodeencodingm.setDcreatetime(new Date());
        barcodeencodingm.setDupdatetime(new Date());
        barcodeencodingm.setIcreateby(JBoltUserKit.getUserId());
        barcodeencodingm.setIupdateby(JBoltUserKit.getUserId());
        barcodeencodingm.setCorgcode(getOrgCode());

        Ret save = service.save(barcodeencodingm);

        renderJson(save);
    }

    /**
     * 更新
     */
    public void update() {
        Barcodeencodingm barcodeencodingm = useIfValid(Barcodeencodingm.class, "barcodeencodingm");
        barcodeencodingm.setCorgcode(getOrgCode());
        barcodeencodingm.setDupdatetime(new Date());
        barcodeencodingm.setIupdateby(JBoltUserKit.getUserId());

        renderJson(service.update(barcodeencodingm));
    }

    /**
     * 批量删除
     */
    public void deleteByIds() {
        renderJson(service.deleteByBatchIds(get("ids")));
    }

    /**
     * 新增编辑表格提交
     */
    public void saveTableSubmit() {
        renderJson(service.saveTableSumit(getJBoltTable()));
    }

    @UnCheck
    public void autocomplete() {
        renderJsonData(service.getAutocompleteList(get("q"), getInt("limit", 10), true, "iautoid,crulename,crulecode"));
    }

    /**
     * 切换启用状态
     */
    public void istate(@Para(value = "iautoid") Long iautoid) {
        ValidationUtils.validateId(iautoid, JBoltMsg.PARAM_ERROR);

        Barcodeencodingm barcodeencodingm = service.findById(iautoid);
        ValidationUtils.notNull(barcodeencodingm, JBoltMsg.DATA_NOT_EXIST);

        barcodeencodingm.setIstate((short) (barcodeencodingm.getIstate() == 1 ?  0 : 1));
        renderJson(service.update(barcodeencodingm));
    }

    public void test() {
        String barCode = service.genCode(Kv.by("cdepcode", "cdepcode"), ItemEnum.PURCHASE.getValue());
        System.out.println(barCode);
        ok();
    }

}
