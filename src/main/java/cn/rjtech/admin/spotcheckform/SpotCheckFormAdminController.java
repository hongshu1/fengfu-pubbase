package cn.rjtech.admin.spotcheckform;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.rjtech.admin.spotcheckformtableparam.SpotCheckFormTableParamService;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.SpotCheckForm;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.Kv;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 点检表格 Controller
 *
 * @ClassName: SpotCheckFormAdminController
 * @author: RJ
 * @date: 2023-04-03 10:42
 */
@CheckPermission(PermissionKey.SPOTCHECK_FORM)
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/spotcheckform", viewPath = "/_view/admin/spotcheckform")
public class SpotCheckFormAdminController extends BaseAdminController {

    @Inject
    private SpotCheckFormService service;
    @Inject
    private SpotCheckFormTableParamService SpotCheckFormTableParamService;

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
    @CheckPermission(PermissionKey.SPOTCHECKFORM_ADD)
    public void add() {
        render("add.html");
    }

    /**
     * 编辑
     */
    @CheckPermission(PermissionKey.SPOTCHECKFORM_EDIT)
    public void edit() {
        SpotCheckForm spotCheckForm = service.findById(getLong(0));
        if (spotCheckForm == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }
        set("spotCheckForm", spotCheckForm);
        //set("spotCheckForm", spotCheckForm);
        render("edit.html");
    }

    /**
     * 保存
     */
    public void save() {
        renderJson(service.save(getModel(SpotCheckForm.class, "spotCheckForm")));
    }

    /**
     * 更新
     */
/*    public void update() {
        renderJson(service.update(getModel(SpotCheckForm.class, "spotCheckForm")));
    }*/

    /**
     * 批量删除
     */
    @CheckPermission(PermissionKey.SPOTCHECKFORM_DELETE)
    public void deleteByIds() {
        renderJson(service.deleteByBatchIds(get("ids")));
    }

    /**
     * 删除
     */
    @CheckPermission(PermissionKey.SPOTCHECKFORM_DELETE)
    public void delete() {
        renderJson(service.delete(getLong(0)));
    }

    /**
     * 切换toggleIsDeleted
     */
    public void toggleIsDeleted() {
        renderJson(service.toggleIsDeleted(getLong(0)));
    }

    /**
     * 切换toggleIsEnabled
     */
    public void toggleIsEnabled() {
        renderJson(service.toggleIsEnabled(getLong(0)));
    }

    @UnCheck
    public void options() {
        renderJsonData(service.options());
    }
    
    /**
     * 按主表qcformparam查询列表
     */
    @UnCheck
    public void getQcFormParamListByPId() {
        renderJsonData(service.getQcFormParamListByPId(getPageNumber(), getPageSize(), getKv()));
    }

    /**
     * 按主表qcformtableparam查询列表
     */
    @UnCheck
    public void getQcFormTableParamListByPId() {
        /**
         * 三种情况
         *  1.新增进来，没有formId也没有新增数据
         *  2.新增进来的，没有formId 但是有新增数据，将新增数据返回 或 修改进来的，有formId，不管是否有新增还是删除直接将页面的数据传入过来
         *  3.默认加载时，是没有数据操作的，直接读取数据
         */
        // 判断是否有新增的值
        if (StrUtil.isBlank(get("qcTableParamJsonStr")) && StrUtil.isBlank(get("iqcformid"))){
            renderJsonData(null);
            return;
        }else if (StrUtil.isNotBlank(get("iqcformid")) && StrUtil.isBlank(get("qcTableParamJsonStr")) ){
            // 查询
        }
        renderJsonData(JSONObject.parseArray(get("qcTableParamJsonStr")));
    }

    /**
     * 按主表qcformitem查询列表qcform
     */
    @UnCheck
    public void getItemCombinedListByPId() {
        renderJsonData(service.getItemCombinedListByPId(getKv()));
    }

    /**
     * qcformitem可编辑表格提交
     */
    public void submitByJBoltTable() {
        renderJson(service.submitByJBoltTable(getJBoltTable()));
    }

    /**
     * qcformparam可编辑表格提交
     */
    public void QcFormParamJBoltTable() {
        renderJson(service.QcFormParamJBoltTable(getJBoltTable()));
    }


    /**
     * qcformtableparam可编辑表格提交
     */
//	public void QcFormTableParamJBoltTable() {
//		renderJson(service.QcFormTableParamJBoltTable(getJBoltTable()));
//	}
    public void customerList() {
        //列表排序
        String cus = get("q");
        Kv kv = new Kv();
        kv.set("cus", StrUtil.trim(cus));
        kv.setIfNotNull("iQcFormId", get("iQcFormId"));
        // 调用采购订单的列表数据源查询
        renderJsonData(service.customerList(kv));

    }

    public void table3(@Para(value = "qcItemJsonStr") String itemJsonStr,
                       @Para(value = "qcParamJsonStr") String itemParamJsonStr,
                       @Para(value = "qcTableParamJsonStr") String tableParamJsonStr,
                       @Para(value = "iqcformid") Long formId){
        // 表头项目
        List tableHeadData = service.getTableHeadData(formId, itemJsonStr, itemParamJsonStr);
        set("columns", tableHeadData);

        /**
         * 三种情况
         *  1.新增进来，没有formId也没有新增数据
         *  2.新增进来的，没有formId 但是有新增数据，将新增数据返回 或 修改进来的，有formId，不管是否有新增还是删除直接将页面的数据传入过来
         *  3.默认加载时，是没有数据操作的，直接读取数据
         */
        // 判断是否有新增的值
        if (ObjUtil.isNotNull(formId) && (StrUtil.isBlank(tableParamJsonStr) || StrUtil.isNotBlank(tableParamJsonStr) && CollUtil.isEmpty(JSONObject.parseArray(tableParamJsonStr))) ){
            // 查询表格行记录
            List<Map<String, Object>> recordList = SpotCheckFormTableParamService.findByFormId(formId);
            // 查询表头数据及参数数据
            set("dataList", recordList);
        }else if(StrUtil.isNotBlank(tableParamJsonStr) && CollUtil.isNotEmpty(JSONObject.parseArray(tableParamJsonStr))){
            JSONArray jsonArray = JSONObject.parseArray(tableParamJsonStr);
            JSONArray itemJson = JSONObject.parseArray(itemJsonStr);
            Map<String, JSONObject> map = itemJson.stream().collect(Collectors.toMap(r -> ((JSONObject) r).getString("iqcitemid"), r -> (JSONObject) r, (key1, key2) -> key2));

            for (String key : map.keySet()){
                for (Object object : jsonArray){
                    JSONObject jsonObject = (JSONObject)object;
                    if (!jsonObject.containsKey(key)){
                        jsonObject.put(key, null);
                    }
                }
            }
            set("dataList", jsonArray);
        }
        render("_table3.html");
    }
    @CheckPermission(PermissionKey.SPOTCHECKFORM_SUBMIT)
    public void submitForm(@Para(value = "formJsonData") String formJsonDataStr,
                           @Para(value = "qcItemTableJsonData") String qcItemTableJsonDataStr,
                           @Para(value = "qcParamTableJsonData") String qcParamTableJsonDataStr,
                           @Para(value = "tableJsonData") String tableJsonDataStr){
        renderJsonData(service.submitForm(formJsonDataStr, qcItemTableJsonDataStr, qcParamTableJsonDataStr, tableJsonDataStr));
    }

}
