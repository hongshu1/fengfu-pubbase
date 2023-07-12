package cn.rjtech.admin.qcform;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheck;
import cn.rjtech.admin.qcformtableparam.QcFormTableParamService;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.model.momdata.QcForm;
import cn.rjtech.model.momdata.QcParam;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 质量建模-检验表格
 *
 * @ClassName: QcFormAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-27 17:53
 */
@CheckPermission(PermissionKey.QCFORM)
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/qcform", viewPath = "/_view/admin/qcform")
public class QcFormAdminController extends BaseAdminController {

    @Inject
    private QcFormService service;
    @Inject
    private QcFormTableParamService qcFormTableParamService;

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
        Okv kv = new Okv();
        kv.setIfNotNull("cQcFormName", get("cQcFormName"));
        renderJsonData(service.getAdminDatas(getPageSize(), getPageNumber(), kv));
    }

    /**
     * 新增
     */
    public void add() {
        QcParam qcParam = new QcParam();
        Long OrgId = getOrgId();
        String orgCode = getOrgCode();
        String orgName = getOrgName();
        qcParam.setIOrgId(OrgId);
        qcParam.setCOrgCode(orgCode);
        qcParam.setCOrgName(orgName);
        set("qcparam", qcParam);
        render("add.html");
    }

//    /**
//     * 保存
//     */
//    public void save() {
//        renderJson(service.save(getModel(QcForm.class, "qcForm")));
//    }

    /**
     * 编辑
     */
    @CheckPermission(PermissionKey.QCPARAM_EDIT)
    public void edit() {
        QcForm qcForm = service.findById(getLong(0));
        if (qcForm == null) {
            renderFail(JBoltMsg.DATA_NOT_EXIST);
            return;
        }

        //获取已保存的表头
        List<Record> rowOneList = service.qcformtableparamOneTitle(qcForm.getIAutoId());
        List<Record> rowTwoList = service.qcformtableparamTwoTitle(qcForm.getIAutoId());

        set("rowOneList", rowOneList);
        set("rowTwoList", rowTwoList);

        set("qcForm", qcForm);
        render("edit.html");
    }

//    /**
//     * 更新
//     */
//    public void update() {
//        renderJson(service.update(getModel(QcForm.class, "qcForm")));
//    }

    /**
     * 批量删除
     */
    @CheckPermission(PermissionKey.QCFORM_DELETE)
    public void deleteByIds() {
        renderJson(service.deleteByIds(get("ids")));
    }

    /**
     * 删除
     */
    @CheckPermission(PermissionKey.QCFORM_DELETE)
    public void delete() {
        renderJson(service.deleteById(getLong(0)));
    }

    /**
     * 切换isDeleted
     */
    public void toggleIsDeleted() {
        renderJson(service.toggleBoolean(getLong(0), "isDeleted"));
    }

    /**
     * 切换isEnabled
     */
    public void toggleIsEnabled() {
        renderJson(service.toggleBoolean(getLong(0), "isEnabled"));
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
        /*
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

    @UnCheck
	public void options(){
		renderJsonData(service.options());
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
           List<Map<String, Object>> recordList = qcFormTableParamService.findByFormId(formId);
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
    @CheckPermission(PermissionKey.QCFORM_SUBMIT)
    public void submitForm(@Para(value = "formJsonData") String formJsonDataStr,
                           @Para(value = "qcItemTableJsonData") String qcItemTableJsonDataStr,
                           @Para(value = "qcParamTableJsonData") String qcParamTableJsonDataStr,
                           @Para(value = "tableJsonData") String tableJsonDataStr){
        renderJsonData(service.submitForm(formJsonDataStr, qcItemTableJsonDataStr, qcParamTableJsonDataStr, tableJsonDataStr));
    }

    /**
     * 下载导入模板
     */
    public void downloadTpl() throws Exception {
        renderJxls("qcform.xlsx", getKv(), "检验表格_导入模板_" + DateUtil.today() + ".xlsx");
    }

//    /**
//     * 检验表格Excel导入数据库
//     */
//    public void importExcelFile(){
//        String uploadPath= JBoltUploadFolder.todayFolder(JBoltUploadFolder.DEMO_JBOLTTABLE_EXCEL);
//        UploadFile file=getFile("file",uploadPath);
//        if(notExcel(file)){
//            renderJsonFail("请上传excel文件");
//            return;
//        }
//        renderJson(service.importExcelData(file.getFile()));
//    }
  
}
