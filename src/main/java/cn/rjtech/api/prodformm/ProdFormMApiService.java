package cn.rjtech.api.prodformm;

import cn.jbolt.core.api.JBoltApiBaseService;
import cn.jbolt.core.api.JBoltApiRet;
import cn.rjtech.admin.prodform.ProdFormService;
import cn.rjtech.admin.prodformm.ProdFormMService;
import cn.rjtech.admin.workregionm.WorkregionmService;
import cn.rjtech.admin.workshiftm.WorkshiftmService;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;

/**
 * 生产表单管理
 * @author yjllzy
 */
public class ProdFormMApiService extends JBoltApiBaseService {

    @Inject
    private ProdFormMService prodFormMService;
    @Inject
    private ProdFormService prodFormService;
    @Inject
    private WorkregionmService workregionmService;

    @Inject
    private WorkshiftmService workshiftmService;

    /**
     * 页面数据源
     * @param pageNumber
     * @param pageSize
     * @param kv
     * @return
     */
    public JBoltApiRet AdminDatas(Integer pageNumber, Integer pageSize, Kv kv) {
        return JBoltApiRet.successWithData(prodFormMService.getAdminDatas(pageNumber, pageSize, kv));

    }

    /**
     * 获取 生产表格数据
     */
    public JBoltApiRet prodFormOptions() {
        return JBoltApiRet.successWithData(prodFormService.options());
    }

    /**
     * 获取产线数据
     * @return
     */
    public JBoltApiRet workregionmOptions() {
        return JBoltApiRet.successWithData(workregionmService.list(Kv.of("isenabled", "true")));
    }

    /**
     * 获取班次数据
     * @return
     */
    public JBoltApiRet workshiftmOptions() {
        return JBoltApiRet.successWithData(workshiftmService.getSelect());
    }

    /**
     *标题数据
     */
    public JBoltApiRet lineRoll(List<Record> formItemLists, String iprodformid) {
        return JBoltApiRet.successWithData(prodFormMService.lineRoll(formItemLists,iprodformid));
    }

    /**
     * 动态表体
     * @param byIdGetDetail
     * @return
     */
    public JBoltApiRet lineRoll2(List<Record> byIdGetDetail) {
        return JBoltApiRet.successWithData(prodFormMService.lineRoll2(byIdGetDetail));
    }

    /**
     * 保存
     */
    public JBoltApiRet submitForm(String formJsonDataStr, String tableJsonDataStr) {
        return JBoltApiRet.successWithData(prodFormMService.submitForm(formJsonDataStr,tableJsonDataStr));
    }


    /**
     * 批量删除
     * @param ids
     * @return
     */
    public JBoltApiRet deleteByIds(String ids) {
        prodFormMService.deleteByIds(ids);
        return JBoltApiRet.API_SUCCESS;
    }

    /**
     * 单删除
     * @param id
     * @return
     */
    public JBoltApiRet delete(Long id) {
        prodFormMService.deleteById(id);
        return JBoltApiRet.API_SUCCESS;
    }

    public JBoltApiRet addDatas(List<Record>  lineRoll,List<Record> lineRoll2) {
        Kv kv = Kv.by("lineRoll", lineRoll).set("lineRoll2", lineRoll2);
        return JBoltApiRet.successWithData(kv);
    }
}
