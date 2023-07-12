package cn.rjtech.admin.userthirdparty;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.poi.excel.JBoltExcel;
import cn.jbolt.core.poi.excel.JBoltExcelHeader;
import cn.jbolt.core.poi.excel.JBoltExcelSheet;
import cn.jbolt.core.poi.excel.JBoltExcelUtil;
import cn.jbolt.core.service.base.JBoltBaseService;
import cn.jbolt.core.util.JBoltCamelCaseUtil;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.enums.ThirdpartySystemEnum;
import cn.rjtech.model.main.UserThirdparty;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Record;

import java.io.File;
import java.util.List;

import static cn.hutool.core.text.StrPool.COMMA;

/**
 * 第三方系统账号信息表
 *
 * @ClassName: UserThirdpartyService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-11-14 21:06
 */
public class UserThirdpartyService extends JBoltBaseService<UserThirdparty> {

    private final UserThirdparty dao = new UserThirdparty().dao();

    @Override
    protected UserThirdparty dao() {
        return dao;
    }

    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

    /**
     * 后台管理数据查询
     */
    public List<Record> getAdminDatas(Long userId) {
        // 创建sql对象
        Sql sql = selectSql();
        // sql条件处理
        sql.eq("user_id", userId);
        // 排序
        sql.asc("id");
        List<Record> list = findRecord(sql);
        if (CollUtil.isNotEmpty(list)) {
            for (Record row : list) {
                row.set("thirdpartySystemName", ThirdpartySystemEnum.toEnum(row.getStr("thirdparty_system")).getText());
            }
        }
        JBoltCamelCaseUtil.keyToCamelCase(list);
        return list;
    }

    /**
     * 保存
     */
    public Ret save(UserThirdparty userThirdparty) {
        if (userThirdparty == null || isOk(userThirdparty.getId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //if(existsName(userThirdparty.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = userThirdparty.save();
        if (success) {
            // 添加日志
            // addSaveSystemLog(userThirdparty.getId(), JBoltUserKit.getUserId(), userThirdparty.getName())
        }
        return ret(success);
    }

    /**
     * 更新
     */
    public Ret update(UserThirdparty userThirdparty) {
        if (userThirdparty == null || notOk(userThirdparty.getId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //更新时需要判断数据存在
        UserThirdparty dbUserThirdparty = findById(userThirdparty.getId());
        if (dbUserThirdparty == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        //if(existsName(userThirdparty.getName(), userThirdparty.getId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = userThirdparty.update();
        if (success) {
            //添加日志
            //addUpdateSystemLog(userThirdparty.getId(), JBoltUserKit.getUserId(), userThirdparty.getName())
        }
        return ret(success);
    }

    /**
     * 删除数据后执行的回调
     *
     * @param userThirdparty 要删除的model
     * @param kv             携带额外参数一般用不上
     */
    @Override
    protected String afterDelete(UserThirdparty userThirdparty, Kv kv) {
        //addDeleteSystemLog(userThirdparty.getId(), JBoltUserKit.getUserId(),userThirdparty.getName())
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param userThirdparty model
     * @param kv             携带额外参数一般用不上
     */
    @Override
    public String checkInUse(UserThirdparty userThirdparty, Kv kv) {
        //这里用来覆盖 检测是否被其它表引用
        return null;
    }

    /**
     * 生成excel导入使用的模板
     */
    public JBoltExcel getImportExcelTpl() {
        return JBoltExcel
            //创建
            .create()
            .setSheets(
                JBoltExcelSheet.create()
                    //设置列映射 顺序 标题名称 不处理别名
                    .setHeaders(1, false,
                        JBoltExcelHeader.create("第三方系统(钉钉、微信企业和ERP)", 15),
                        JBoltExcelHeader.create("第三方用户编码", 15),
                        JBoltExcelHeader.create("第三方用户名称", 15),
                        JBoltExcelHeader.create("用户ID", 15),
                        JBoltExcelHeader.create("第三方用户密码", 15),
                        JBoltExcelHeader.create("备注", 15)
                    )
            );
    }

    /**
     * 读取excel文件
     */
    public Ret importExcel(File file) {
        StringBuilder errorMsg = new StringBuilder();
        JBoltExcel jBoltExcel = JBoltExcel
            //从excel文件创建JBoltExcel实例
            .from(file)
            //设置工作表信息
            .setSheets(
                JBoltExcelSheet.create()
                    //设置列映射 顺序 标题名称
                    .setHeaders(1,
                        JBoltExcelHeader.create("thirdparty_system", "第三方系统(钉钉、微信企业和ERP)"),
                        JBoltExcelHeader.create("thirdparty_code", "第三方用户编码"),
                        JBoltExcelHeader.create("thirdparty_name", "第三方用户名称"),
                        JBoltExcelHeader.create("user_id", "用户ID"),
                        JBoltExcelHeader.create("thirdparty_pwd", "第三方用户密码"),
                        JBoltExcelHeader.create("remark", "备注")
                    )
                    //从第三行开始读取
                    .setDataStartRow(2)
            );
        //从指定的sheet工作表里读取数据
        List<UserThirdparty> userThirdpartys = JBoltExcelUtil.readModels(jBoltExcel, 1, UserThirdparty.class, errorMsg);
        if (notOk(userThirdpartys)) {
            if (errorMsg.length() > 0) {
                return fail(errorMsg.toString());
            } else {
                return fail(JBoltMsg.DATA_IMPORT_FAIL_EMPTY);
            }
        }
        // 执行批量操作
        boolean success = tx(() -> {
            batchSave(userThirdpartys);
            return true;
        });

        if (!success) {
            return fail(JBoltMsg.DATA_IMPORT_FAIL);
        }
        return SUCCESS;
    }

    /**
     * 生成要导出的Excel
     */
    public JBoltExcel exportExcel(List<UserThirdparty> datas) {
        return JBoltExcel
            //创建
            .create()
            //设置工作表
            .setSheets(
                //设置工作表 列映射 顺序 标题名称
                JBoltExcelSheet
                    .create()
                    //表头映射关系
                    .setHeaders(1,
                        JBoltExcelHeader.create("thirdparty_system", "第三方系统(钉钉、微信企业和ERP)", 15),
                        JBoltExcelHeader.create("thirdparty_code", "第三方用户编码", 15),
                        JBoltExcelHeader.create("thirdparty_name", "第三方用户名称", 15),
                        JBoltExcelHeader.create("user_id", "用户ID", 15),
                        JBoltExcelHeader.create("thirdparty_pwd", "第三方用户密码", 15),
                        JBoltExcelHeader.create("remark", "备注", 15)
                    )
                    //设置导出的数据源 来自于数据库查询出来的Model List
                    .setModelDatas(2, datas)
            );
    }

    public void deleteByMultiIds(Object[] delete) {
        delete("DELETE FROM base_user_thirdparty WHERE id IN (" + ArrayUtil.join(delete, COMMA) + ")");
    }

    public boolean notExitsDuplicate(long userId) {
        return CollUtil.isEmpty(find(selectSql()
            .select("thirdparty_system", "thirdparty_code")
            .eq("user_id", userId)
            .groupBy("thirdparty_system,thirdparty_code")
            .having("count(*)>1")
        ));
    }

    /**
     * 获取该用户的u9用户名
     */
    public String getU9Name(long userId) {
        String value = ThirdpartySystemEnum.U9.getValue();
        Record record = getAdminDatas(userId)
            .stream().filter(e -> e.getStr("thirdpartySystem").equals(value))
            .findFirst()
            .orElse(new Record());
        return record.getStr("thirdpartyCode");
    }

    /*
     * 获取u8用户名
     * */
    public String getU8Name(long userId) {
        String value = ThirdpartySystemEnum.U8.getValue();
        Record record = getAdminDatas(userId)
            .stream().filter(e -> e.getStr("thirdpartySystem").equals(value))
            .findFirst()
            .orElse(new Record());
        return record.getStr("thirdpartyCode");
    }
}