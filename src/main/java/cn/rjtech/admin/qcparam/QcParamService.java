package cn.rjtech.admin.qcparam;

import java.io.File;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.poi.excel.JBoltExcel;
import cn.jbolt.core.poi.excel.JBoltExcelHeader;
import cn.jbolt.core.poi.excel.JBoltExcelSheet;
import cn.jbolt.core.poi.excel.JBoltExcelUtil;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.rjtech.model.momdata.QcParam;

/**
 * 质量建模-检验参数
 *
 * @ClassName: QcParamService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-17 15:15
 */
public class QcParamService extends BaseService<QcParam> {

    private final QcParam dao = new QcParam().dao();

    @Override
    protected QcParam dao() {
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
     * @param isEnabled  是否启用：0. 否 1. 是
     * @param isDeleted  删除状态：0. 未删除 1. 已删除
     */
    public Page<QcParam> getAdminDatas(int pageNumber, int pageSize, String keywords, Boolean isEnabled, Boolean isDeleted) {
        //创建sql对象
        Sql sql = selectSql().page(pageNumber, pageSize);
        //sql条件处理
        sql.eqBooleanToChar("isEnabled", isEnabled);
        sql.eqBooleanToChar("isDeleted", isDeleted);
        //关键词模糊查询
        sql.likeMulti(keywords, "cOrgName", "cQcParamName", "cCreateName", "cUpdateName");
        //排序
        sql.desc("iAutoId");
        return paginate(sql);
    }

    /**
     * 保存
     */
    public Ret save(QcParam qcParam) {
        if (qcParam == null || isOk(qcParam.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //if(existsName(qcParam.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = qcParam.save();
        if (success) {
            //添加日志
            //addSaveSystemLog(qcParam.getIAutoId(), JBoltUserKit.getUserId(), qcParam.getName());
        }
        return ret(success);
    }

    /**
     * 更新
     */
    public Ret update(QcParam qcParam) {
        if (qcParam == null || notOk(qcParam.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //更新时需要判断数据存在
        QcParam dbQcParam = findById(qcParam.getIAutoId());
        if (dbQcParam == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        //if(existsName(qcParam.getName(), qcParam.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = qcParam.update();
        if (success) {
            //添加日志
            //addUpdateSystemLog(qcParam.getIAutoId(), JBoltUserKit.getUserId(), qcParam.getName());
        }
        return ret(success);
    }

    /**
     * 删除数据后执行的回调
     *
     * @param qcParam 要删除的model
     * @param kv      携带额外参数一般用不上
     */
    @Override
    protected String afterDelete(QcParam qcParam, Kv kv) {
        //addDeleteSystemLog(qcParam.getIAutoId(), JBoltUserKit.getUserId(),qcParam.getName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param qcParam model
     * @param kv      携带额外参数一般用不上
     */
    @Override
    public String checkInUse(QcParam qcParam, Kv kv) {
        //这里用来覆盖 检测是否被其它表引用
        return null;
    }

    /**
     * toggle操作执行后的回调处理
     */
    @Override
    protected String afterToggleBoolean(QcParam qcParam, String column, Kv kv) {
        //addUpdateSystemLog(qcParam.getIAutoId(), JBoltUserKit.getUserId(), qcParam.getName(),"的字段["+column+"]值:"+qcParam.get(column));
        /**
         switch(column){
         case "isEnabled":
         break;
         case "isDeleted":
         break;
         }
         */
        return null;
    }

    /*
     * 数据导入
     * */
    public Ret importExcelData(File file) {
        StringBuilder errorMsg = new StringBuilder();
        JBoltExcel jBoltExcel = JBoltExcel
            //从excel文件创建JBoltExcel实例
            .from(file)
            //设置工作表信息
            .setSheets(
                JBoltExcelSheet.create("检验参数")
                    //设置列映射 顺序 标题名称
                    .setHeaders(
                        JBoltExcelHeader.create("cworkclasscode", "工种编码"),
                        JBoltExcelHeader.create("cworkclassname", "工种名称"),
                        JBoltExcelHeader.create("ilevel", "工种等级"),
                        JBoltExcelHeader.create("isalary", "工种薪酬"),
                        JBoltExcelHeader.create("cmemo", "备注")
                    )
                    //特殊数据转换器
                    /*.setDataChangeHandler((data, index) -> {
                        try {
                            String isalary = data.getStr("isalary");
                            data.change("isalary", new BigDecimal(isalary));
                        } catch (Exception e) {
                            errorMsg.append(data.getStr("isalary") + "薪酬填写有误");
                        }
                    })*/
                    //从第三行开始读取
                    .setDataStartRow(2)
            );
        //从指定的sheet工作表里读取数据
        List<QcParam> models = JBoltExcelUtil.readModels(jBoltExcel, "检验参数", QcParam.class, errorMsg);
        if (notOk(models)) {
            if (errorMsg.length() > 0) {
                return fail(errorMsg.toString());
            } else {
                return fail(JBoltMsg.DATA_IMPORT_FAIL_EMPTY);
            }
        }
        if (errorMsg.length() > 0) {
            return fail(errorMsg.toString());
        }
        //读取数据没有问题后判断必填字段
        if (models.size() > 0) {
            for (QcParam param : models) {
                /*if (notOk(param.getCworkclasscode())) {
                    return fail("工种编码不能为空");
                }
                if (notOk(param.getCworkclassname())) {
                    return fail("工种名称不能为空");
                }
                if (notOk(param.getIlevel())) {
                    return fail("工种等级不能为空");
                }*/
            }
        }
        savaModelHandle(models);

        //执行批量操作
        boolean result = tx(new IAtom() {
            @Override
            public boolean run() throws SQLException {
                batchSave(models);
                return true;
            }
        });
        if (!result) {
            return fail(JBoltMsg.DATA_IMPORT_FAIL);
        }
        return SUCCESS;
    }

    private void savaModelHandle(List<QcParam> models) {
        Long userId = JBoltUserKit.getUserId();//用户id
        String userName = JBoltUserKit.getUserName();//用户名
        Date date = new Date();
        getOrgId();//组织id
        getOrgCode();//组织编码
        getOrgName();//组织名称
        for (QcParam qcParam : models) {
        }
    }

}