package cn.rjtech.api.prodformm;

import cn.jbolt.core.api.JBoltApiRet;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.UnCheck;
import cn.rjtech.admin.prodform.ProdFormService;
import cn.rjtech.admin.prodformitem.ProdFormItemService;
import cn.rjtech.admin.prodformm.ProdFormMService;
import cn.rjtech.base.controller.BaseApiController;
import cn.rjtech.entity.vo.base.NullDataResult;
import cn.rjtech.entity.vo.prodformm.ProdFormMResVo;
import cn.rjtech.entity.vo.prodformm.ProdFormMVo;
import cn.rjtech.entity.vo.workregionm.WorkRegionmVo;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Record;
import io.github.yedaxia.apidocs.ApiDoc;

import java.util.List;

/**
 * 生产表单管理
 * @author yjllzy
 */
@ApiDoc
public class ProdFormMApiController extends BaseApiController {
    @Inject
    private ProdFormMApiService service;
    @Inject
    private ProdFormItemService prodFormItemService;
    @Inject
    private ProdFormService prodFormService;
    @Inject
    private ProdFormMService prodFormMService;
    /**
     * 页面数据
     */
    @ApiDoc(result = ProdFormMVo.class)
    public void datas(@Para(value = "pageNumber", defaultValue = "1") Integer pageNumber,
                      @Para(value = "pageSize", defaultValue = "15") Integer pageSize,
                      @Para(value = "cworkid") String cworkid,
                      @Para(value = "cworkshiftid") String cworkshiftid,
                      @Para(value = "iprodformid") String iprodformid,
                      @Para(value = "iauditstatus") String iauditstatus,
                      @Para(value = "startdate") String startdate,
                      @Para(value = "enddate") String enddate) {
        Kv kv = Kv.by("cworkid", cworkid)
                .set("cworkshiftid", cworkshiftid)
                .set("iprodformid", iprodformid)
                .set("iauditstatus", iauditstatus)
                .set("startdate", startdate)
                .set("enddate", enddate);

        renderJBoltApiRet(service.AdminDatas(pageNumber, pageSize, kv));
    }

    /**
     * 获取 生产表格数据
     */
    @ApiDoc(result = ProdFormMVo.class)
    public void   prodFormOptions(){
        renderJBoltApiRet(service.prodFormOptions());
    }

    /**
     * 获取产线数据
     */
    @ApiDoc(result = WorkRegionmVo.class)
    public void   workregionmOptions(){
        renderJBoltApiRet(service.workregionmOptions());
    }

    /**
     * 获取班次数据
     */
    @UnCheck
    public void  workshiftmOptions(){
        renderJBoltApiRet(service.workshiftmOptions());
    }

    /**
     * 新增页面数据
     * byIdGetDetail 中的iseq 相同的要合成一条数据并排序，proditemiseq 为页面数据排序
     */
    @ApiDoc(result = ProdFormMResVo.class)
    public void addDatas(@Para( value = "iprodformid") String iprodformid){
        //生产表单项目标题
        List<Record> formItemLists = prodFormItemService.formItemLists(Kv.by("iqcformid", iprodformid));
        List<Record> byIdGetDetail = prodFormService.findByIdGetDetail(iprodformid);
        renderJBoltApiRet( service.addDatas( formItemLists,byIdGetDetail));
    }
    /**
     * 编辑页面
     */
    @ApiDoc(result = ProdFormMResVo.class)
    public void redact(@Para( value = "iautoid") String iautoid){
        //生产表单项目标题
        renderJBoltApiRet(JBoltApiRet.successWithData(prodFormMService.findById(iautoid)));
    }



    /**
     * 保存修改
     */
    @UnCheck
    public void submitForm(@Para(value = "formJsonData") String formJsonDataStr,
                           @Para(value = "tableJsonData") String tableJsonDataStr){
        ValidationUtils.notNull(formJsonDataStr, JBoltMsg.PARAM_ERROR);
        ValidationUtils.notNull(tableJsonDataStr, JBoltMsg.PARAM_ERROR);
        renderJBoltApiRet(service.submitForm(formJsonDataStr, tableJsonDataStr));

    }

    /**
     * 批量删除
     */
    @ApiDoc(result = NullDataResult.class)
    @UnCheck
    public void deleteByIds(@Para( value = "ids") String ids){
        renderJBoltApiRet(service.deleteByIds(ids));

    }
    /**
     * 删除
     */
    @ApiDoc(result = NullDataResult.class)
    @UnCheck
    public  void delete(@Para( value = "id") Long id){
        renderJBoltApiRet(service.delete(id));
    }

}
