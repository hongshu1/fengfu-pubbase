package cn.rjtech.admin.userorg;

import cn.hutool.core.collection.CollUtil;
import cn.jbolt._admin.dept.DeptService;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.poi.excel.JBoltExcel;
import cn.jbolt.core.poi.excel.JBoltExcelHeader;
import cn.jbolt.core.poi.excel.JBoltExcelSheet;
import cn.jbolt.core.poi.excel.JBoltExcelUtil;
import cn.jbolt.core.service.base.JBoltBaseService;
import cn.jbolt.core.util.JBoltCamelCaseUtil;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.main.UserOrg;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Record;

import java.io.File;
import java.util.List;

/**
 * 用户组织关系
 *
 * @ClassName: UserOrgService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-11-14 16:02
 */
public class UserOrgService extends JBoltBaseService<UserOrg> {

    private final UserOrg dao = new UserOrg().dao();

    @Inject
    private DeptService deptService;

    @Override
    protected UserOrg dao() {
        return dao;
    }

    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

    /**
     * 后台管理数据查询
     *
     * @param position    职位
     * @param isPrincipal 负责人标识：0. 否 1. 是
     * @param isDeleted   是否删除
     */
    public List<UserOrg> getAdminDatas(String position, Boolean isPrincipal, Boolean isDeleted) {
        //创建sql对象
        Sql sql = selectSql();
        //sql条件处理
        sql.eq("position", position);
        sql.eqBooleanToChar("is_principal", isPrincipal);
        sql.eqBooleanToChar("is_deleted", isDeleted);
        //排序
        sql.asc("id");
        return find(sql);
    }

    /**
     * 删除数据后执行的回调
     *
     * @param userOrg 要删除的model
     * @param kv      携带额外参数一般用不上
     */
    @Override
    protected String afterDelete(UserOrg userOrg, Kv kv) {
        //addDeleteSystemLog(userOrg.getId(), JBoltUserKit.getUserId(),userOrg.getName())
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param userOrg model
     * @param kv      携带额外参数一般用不上
     */
    @Override
    public String checkInUse(UserOrg userOrg, Kv kv) {
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
                                        JBoltExcelHeader.create("组织ID", 15),
                                        JBoltExcelHeader.create("用户ID", 15),
                                        JBoltExcelHeader.create("职位", 15),
                                        JBoltExcelHeader.create("负责人标识：0. 否 1. 是", 15),
                                        JBoltExcelHeader.create("直接上级", 15),
                                        JBoltExcelHeader.create("版本号", 15)
                                )
                );
    }

    /**
     * 读取excel文件
     */
    public Ret importExcel(File file) {
        StringBuilder errorMsg = new StringBuilder();
        JBoltExcel jBoltExcel = JBoltExcel
                // 从excel文件创建JBoltExcel实例
                .from(file)
                // 设置工作表信息
                .setSheets(
                        JBoltExcelSheet.create()
                                //设置列映射 顺序 标题名称
                                .setHeaders(1,
                                        JBoltExcelHeader.create("org_id", "组织ID"),
                                        JBoltExcelHeader.create("user_id", "用户ID"),
                                        JBoltExcelHeader.create("position", "职位"),
                                        JBoltExcelHeader.create("is_principal", "负责人标识：0. 否 1. 是"),
                                        JBoltExcelHeader.create("parent_psn_id", "直接上级"),
                                        JBoltExcelHeader.create("version_num", "版本号")
                                )
                                //从第三行开始读取
                                .setDataStartRow(2)
                );
        // 从指定的sheet工作表里读取数据
        List<UserOrg> userOrgs = JBoltExcelUtil.readModels(jBoltExcel, 1, UserOrg.class, errorMsg);
        if (notOk(userOrgs)) {
            if (errorMsg.length() > 0) {
                return fail(errorMsg.toString());
            } else {
                return fail(JBoltMsg.DATA_IMPORT_FAIL_EMPTY);
            }
        }
        // 执行批量操作
        boolean success = tx(() -> {
            batchSave(userOrgs);
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
    public JBoltExcel exportExcel(List<UserOrg> datas) {
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
                                        JBoltExcelHeader.create("org_id", "组织ID", 15),
                                        JBoltExcelHeader.create("user_id", "用户ID", 15),
                                        JBoltExcelHeader.create("position", "职位", 15),
                                        JBoltExcelHeader.create("is_principal", "负责人标识：0. 否 1. 是", 15),
                                        JBoltExcelHeader.create("parent_psn_id", "直接上级", 15),
                                        JBoltExcelHeader.create("create_time", "创建时间", 15),
                                        JBoltExcelHeader.create("create_user_name", "创建人", 15),
                                        JBoltExcelHeader.create("last_update_time", "最后更新时间", 15),
                                        JBoltExcelHeader.create("last_update_name", "最后更新人", 15),
                                        JBoltExcelHeader.create("version_num", "版本号", 15)
                                )
                                //设置导出的数据源 来自于数据库查询出来的Model List
                                .setModelDatas(2, datas)
                );
    }

    /**
     * toggle操作执行后的回调处理
     */
    @Override
    protected String afterToggleBoolean(UserOrg userOrg, String column, Kv kv) {
        // addUpdateSystemLog(userOrg.getId(), JBoltUserKit.getUserId(), userOrg.getName(),"的字段["+column+"]值:"+userOrg.get(column))
        /*
         switch(column){
         case "is_principal":
         break;
         }
         */
        return null;
    }

    /**
     * 恢复假删数据后执行的回调
     *
     * @param userOrg 要恢复的model
     */
    @Override
    protected String afterRecover(UserOrg userOrg) {
        // addRecoverSystemLog(userOrg.getId(), JBoltUserKit.getUserId(),userOrg.getName())
        return null;
    }

    public boolean notExistsDuplicate(long userId) {
        return CollUtil.isEmpty(find(selectSql()
                .select("org_id")
                .eq("user_id", userId)
                .eq("is_deleted", "0")
                .groupBy("org_id")
                .having("count(*)>1")));
    }

    public List<Record> getList(Long userId) {
        List<Record> list = dbTemplate("userorg.getList", Okv.by("userId", userId)).find();

        JBoltCamelCaseUtil.keyToCamelCase(list);

        return list;
    }

    public List<Record> getAccessList(Long userId) {
        Sql sql = selectSql().select("dept.id, dept.name")
                .from(table(), "uo")
                .innerJoin(deptService.table(), "dept", "uo.org_id = dept.id")
                .eq("uo.is_deleted", ZERO_STR)
                .eq("uo.user_id", userId);

        return findRecord(sql, false);
    }

    public boolean checkUserHasOrg(long userId, Long orgId) {
        return null != queryInt(selectSql().select("1").eq(UserOrg.USER_ID, userId).eq(UserOrg.ORG_ID, orgId).eq(UserOrg.IS_DELETED, ZERO_STR).first()) ;
    }

    public UserOrg getUserOrg(long userId, long orgId) {
        Sql sql = selectSql()
                .eq(UserOrg.USER_ID, userId)
                .eq(UserOrg.ORG_ID, orgId)
                .eq(UserOrg.IS_DELETED, ZERO_STR);
        
        return findFirst(sql);
    }

    public boolean notExistsDuplicatePerson(Long orgId, Long iPersonId) {
        return null == queryInt("SELECT TOP 1 1 FROM base_user_org WHERE org_id = ? AND ipersonid = ? AND is_deleted = ? ", orgId, iPersonId, ZERO_STR);
    }
    
}