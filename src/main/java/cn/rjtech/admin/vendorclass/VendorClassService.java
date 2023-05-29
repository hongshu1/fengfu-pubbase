package cn.rjtech.admin.vendorclass;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.jbolt.common.util.CACHE;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.bean.JsTreeBean;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.poi.excel.*;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.enums.SourceEnum;
import cn.rjtech.model.momdata.VendorClass;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 往来单位-供应商分类
 *
 * @ClassName: VendorClassService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-22 10:04
 */
public class VendorClassService extends BaseService<VendorClass> {

    private final VendorClass dao = new VendorClass().dao();

    @Override
    protected VendorClass dao() {
        return dao;
    }

    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

    /**
     * 后台管理数据查询
     *
     * @param pageNumber 第几页
     * @param pageSize   每页几条数据
     * @param keywords   关键词
     * @param isDeleted  删除状态：0. 未删除 1. 已删除
     */
    public Page<VendorClass> getAdminDatas(int pageNumber, int pageSize, String keywords, Boolean isDeleted) {
        //创建sql对象
        Sql sql = selectSql().page(pageNumber, pageSize);
        //sql条件处理
        sql.eqBooleanToChar("isDeleted", isDeleted);
        //关键词模糊查询
        sql.likeMulti(keywords, "cOrgName", "cVCName", "cCreateName", "cUpdateName");
        //排序
        sql.desc("iAutoId");
        return paginate(sql);
    }

    public Page<Record> pageList(Kv kv) {
        Page<Record> recordPage = dbTemplate("vendorclass.list", kv).paginate(kv.getInt("page"), kv.getInt("pageSize"));
        return recordPage;
    }

    /**
     * 保存
     */
    public Ret save(VendorClass vendorClass) {
        if (vendorClass == null || isOk(vendorClass.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //1、判断上级分类id是否存在
        final Ret ret = checkIpidIsEmpty(vendorClass.getIPid());
        Object msg = ret.get("msg");
        if (ret.isFail()){
            return fail((String) msg);
        }
        //2、分类编码不能重复
        String cvcCode = vendorClass.getCVCCode();//分类编码
        ValidationUtils.isTrue(StringUtils.isBlank(findCVCCodeInfo(cvcCode)), cvcCode+" 分类编码不能重复！");
        //3、给参数赋值
        saveVendorClassModel(vendorClass, new Date());
        boolean success = vendorClass.save();
        return ret(success);
    }

    /**
     * 给参数赋值
     */
    public void saveVendorClassModel(VendorClass vendorClass, Date date) {
        Long userId = JBoltUserKit.getUserId();
        String userName = JBoltUserKit.getUserName();
        vendorClass.setIAutoId(JBoltSnowflakeKit.me.nextId());
        vendorClass.setIPid(vendorClass.getIPid());
        vendorClass.setICreateBy(userId);
        vendorClass.setCCreateName(userName);
        vendorClass.setCUpdateName(userName);
        vendorClass.setIUpdateBy(userId);
        vendorClass.setDCreateTime(date);
        vendorClass.setDUpdateTime(date);
        vendorClass.setCOrgCode(getOrgCode());
        vendorClass.setCOrgName(getOrgName());
        vendorClass.setIsDeleted(false);
        vendorClass.setISource(SourceEnum.MES.getValue());
    }

    /**
     * 判断上级分类是否存在
     */
    public Ret checkIpidIsEmpty(Long ipid) {
        if (notNull(ipid)) {
            VendorClass isEmptyVendorClass = findById(ipid);
            if (null == isEmptyVendorClass) {
                return fail("此上级分类不存在");
            }
        }
        return SUCCESS;
    }

    public String findCVCCodeInfo(String cvccode) {
        Sql sql = selectSql().select(VendorClass.CVCCODE).eq(VendorClass.CVCCODE, cvccode);
        return queryColumn(sql);
    }

    public Record findRecordByCVCCode(String cvccode) {
        Kv kv = new Kv();
        kv.set("cvccode",cvccode);
        return dbTemplate("vendorclass.findRecordByCVCCode", kv).findFirst();
    }

    /**
     * 更新
     */
    public Ret update(VendorClass vendorClass) {
        if (vendorClass == null || notOk(vendorClass.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //更新时需要判断数据存在
        VendorClass dbVendorClass = findById(vendorClass.getIAutoId());
        if (dbVendorClass == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        boolean success = vendorClass.update();
        return ret(success);
    }

    /**
     * 删除数据后执行的回调
     *
     * @param vendorClass 要删除的model
     * @param kv          携带额外参数一般用不上
     */
    @Override
    protected String afterDelete(VendorClass vendorClass, Kv kv) {
        //addDeleteSystemLog(vendorClass.getIAutoId(), JBoltUserKit.getUserId(),vendorClass.getName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param vendorClass model
     * @param kv          携带额外参数一般用不上
     */
    @Override
    public String checkInUse(VendorClass vendorClass, Kv kv) {
        //这里用来覆盖 检测是否被其它表引用
        return null;
    }

    /**
     * 供应商分类excel导入数据库
     */
    public Ret importExcelData(File file) {
        StringBuilder errorMsg = new StringBuilder();
        Date date = new Date();
        JBoltExcel jBoltExcel = JBoltExcel
            //从excel文件创建JBoltExcel实例
            .from(file)
            //设置工作表信息
            .setSheets(
                JBoltExcelSheet.create("供应商分类")
                    //设置列映射 顺序 标题名称
                    .setHeaders(
                        JBoltExcelHeader.create("ipid", "父级供应商分类编码"),
                        JBoltExcelHeader.create("cvccode", "供应商分类编码"),
                        JBoltExcelHeader.create("cvcname", "供应商分类名称")
                    )
                    //特殊数据转换器
                    .setDataChangeHandler((data, index) -> {
                        //非空判断
                        ValidationUtils.notNull(data.get("ipid"), "父级供应商分类编码为空！");
                        ValidationUtils.notNull(data.get("cvccode"), "供应商分类编码为空！");
                        ValidationUtils.notNull(data.get("cvcname"), "供应商分类名称为空！");
                        //子级供应商编码不能重复
                        ValidationUtils.isTrue(StringUtils.isBlank(findCVCCodeInfo(data.getStr("cvccode"))),
                            data.getStr("cvccode")+" 供应商分类编码重复");
                        if (!"父级供应商分类编码".equals(data.getStr("ipid"))){
                            VendorClass vendorClass = findFirst(Okv.by("cVCCode", data.getStr("ipid")), "iautoid", "desc");
                            ValidationUtils.isTrue(null != vendorClass, data.getStr("ipid")+" 父级供应商分类不存在，请手动添加！");
                        }
                        VendorClass vendorClass = findFirst(Okv.by("cVCCode", data.getStr("ipid")), "iautoid", "desc");
                        if (null != vendorClass){
                            data.change("ipid",vendorClass.getIAutoId());
                        }
                        data.change("icreateby", JBoltUserKit.getUserId());
                        data.change("ccreatename", JBoltUserKit.getUserName());
                        data.change("cupdatename",JBoltUserKit.getUserName());
                        data.change("iupdateby",JBoltUserKit.getUserId());
                    })
                    //从第2行开始读取
                    .setDataStartRow(2)
            );
        // 从指定的sheet工作表里读取数据
        List<VendorClass> models = JBoltExcelUtil.readModels(jBoltExcel, "供应商分类", VendorClass.class, errorMsg);
        if (notOk(models)) {
            if (errorMsg.length() > 0) {
                return fail(errorMsg.toString());
            } else {
                return fail(JBoltMsg.DATA_IMPORT_FAIL_EMPTY);
            }
        }
        // 读取数据没有问题后判断必填字段
        if (models.size() > 0) {
            Date now = new Date();
            for (VendorClass vendorClass : models) {
                vendorClass.setIAutoId(JBoltSnowflakeKit.me.nextId());
                vendorClass.setDCreateTime(now);
                vendorClass.setDUpdateTime(date);
                vendorClass.setIOrgId(getOrgId());
                vendorClass.setCOrgCode(getOrgCode());
                vendorClass.setCOrgName(getOrgName());
                vendorClass.setIsDeleted(false);
                vendorClass.setISource(SourceEnum.MES.getValue());
            }
        }
        
        //执行批量操作
        boolean success = tx(() -> {
            batchSave(models);
            return true;
        });
        
        if (!success) {
            return fail(JBoltMsg.DATA_IMPORT_FAIL);
        }
        
        return SUCCESS;
    }

    /**
     * 树结构数据源
     */
    public List<JsTreeBean> getMgrTree(Long checkedId, int openLevel) {
        List<VendorClass> supplierclasses = getMgrList();
        List<JsTreeBean> jsTreeBeanList = new ArrayList<>();
        for (VendorClass vendorClass : supplierclasses) {
            Long id = vendorClass.getIAutoId();
            Object pid = vendorClass.getIPid();
            if (pid == null) {
                pid = "#";
            }
            String text = "[" + vendorClass.getCVCCode() + "]" + vendorClass.getCVCName();
            String type = vendorClass.getCVCCode();
            JsTreeBean jsTreeBean = new JsTreeBean(id, pid, text, type, "", false);
            jsTreeBeanList.add(jsTreeBean);
        }
        return jsTreeBeanList;
    }

    /**
     * 获取分类数据中的所有后端分类数据
     */
    public List<VendorClass> getMgrList() {
        return getVendorclass(false);
    }

    /**
     * 获取分类数据
     */
    public List<VendorClass> getVendorclass(Boolean isDeleteD) {
        Okv kv = Okv.create();
        if (isDeleteD != null) {
            kv.set("isDeleted", isDeleteD);
        }
        return getCommonList(kv, "dUpdateTime", "asc");
    }

    /**
     * 导出数据
     */
    public List<Record> getDataExport(Kv kv) {
        return dbTemplate("vendorclass.getDataExport", kv).find();
    }

    /**
     * 删除
     */
    public Ret delete(Long id) {
        return deleteById(id, true);
    }

    public List<VendorClass> getTreeDatas() {
        List<VendorClass> vendorClassList = getMgrList();
        return this.convertToModelTree(vendorClassList, "iAutoId", "iPid", (p) -> this.notOk(p.getIPid()));
    }

    public List<VendorClass> list(Kv kv){
        return find("SELECT t1.cVCCode as fathercvccode,t1.cVCName as fathercvcname,t2.cVCCode as childcvccoded,t2.cVCName as childcvcnamed FROM "
            + "Bd_VendorClass t1 LEFT "
            + "JOIN Bd_VendorClass t2 ON t1.iAutoId = t2.iPid\n"
            + "WHERE 1=1");
    }

    /**
     * 导出excel文件
     */
    public JBoltExcel exportExcelTpl(List<VendorClass> datas) {
        //2、创建JBoltExcel
        JBoltExcel jBoltExcel = JBoltExcel
            .create()
            .addSheet(//设置sheet
                JBoltExcelSheet.create("供应商分类")//创建sheet name保持与模板中的sheet一致
                    .setHeaders(//sheet里添加表头
                        JBoltExcelHeader.create("fathercvccode", "父级供应商分类编码",25),
                        JBoltExcelHeader.create("fathercvcname", "父级供应商分类名称",25),
                        JBoltExcelHeader.create("childcvccoded", "供应商分类编码",25),
                        JBoltExcelHeader.create("childcvcnamed", "供应商分类名称",25)
                    )
                    .setDataChangeHandler((data, index) -> {//设置数据转换处理器
                        //将user_id转为user_name
                        data.changeWithKey("user_id", "user_username", CACHE.me.getUserUsername(data.get("user_id")));
                        data.changeBooleanToStr("is_enabled", "是", "否");
                    })
                    .setModelDatas(2, datas)//设置数据
            )
            .setFileName("供应商分类" + "_" + DateUtil.today());
        //3、返回生成的excel文件
        return jBoltExcel;
    }

    /**
     * 生成Excel 导入模板的数据 byte[]
     */
    public JBoltExcel getExcelImportTpl() {
        return JBoltExcel
            //创建
            .create()
            .setSheets(
                JBoltExcelSheet.create("供应商分类")
                    //设置列映射 顺序 标题名称 不处理别名
                    .setHeaders(2, false,
                        JBoltExcelHeader.create("父级供应商分类编码", 25),
//                        JBoltExcelHeader.create("父级供应商分类名称", 25),
                        JBoltExcelHeader.create("供应商分类编码", 25),
                        JBoltExcelHeader.create("供应商分类名称", 25)
                    )
                    .setMerges(JBoltExcelMerge.create("A", "C", 1, 1, "供应商分类"))
            );
    }

    public List<JsTreeBean> getTreeList() {
        List<JsTreeBean> treeBeans = new ArrayList<>();
        // 根节点
        treeBeans.add(new JsTreeBean(0, "#", "供应商分类档案", true, "root_opened", true));
        appendSubTree(treeBeans,0L);
        return treeBeans;
    }
    private void appendSubTree(List<JsTreeBean> treeBeans, Long pid) {
    	List<Record> subList = getSubList(pid);
    	if(CollUtil.isEmpty(subList)) return;
        for (Record row : subList) {
            // 当前节点
            treeBeans.add(new JsTreeBean(row.getStr("cvccode"), pid, row.getStr("cvcname") + "(" + row.getStr("cvccode") + ")","default", null, true));
            // 追加子节点
            appendSubTree(treeBeans,row.getLong("iautoid"));
        }
    }
    private List<Record> getSubList(Long pid) {
        Okv para = Okv.by("pid", pid);
        return dbTemplate("vendorclass.getSubList", para).find();
    }     
}