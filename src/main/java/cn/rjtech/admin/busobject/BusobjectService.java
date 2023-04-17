package cn.rjtech.admin.busobject;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.User;
import cn.jbolt.core.service.base.JBoltBaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.application.RjApplicationService;
import cn.rjtech.enums.DataSourceEnum;
import cn.rjtech.model.main.Application;
import cn.rjtech.model.main.Busobject;
import cn.rjtech.util.ModelMap;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.Date;
import java.util.List;

/**
 * 业务对象
 *
 * @ClassName: BusobjectService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-11-23 13:41
 */
public class BusobjectService extends JBoltBaseService<Busobject> {

    private final Busobject dao = new Busobject().dao();

    @Inject
    private RjApplicationService applicationService;

    @Override
    protected Busobject dao() {
        return dao;
    }

    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.BUS_OBJECT.getValue();
    }

    /**
     * 后台管理数据查询
     *
     * @param pageNumber 第几页
     * @param pageSize   每页几条数据
     * @param keywords   关键词
     * @param sortColumn 排序列名
     * @param sortType   排序方式 asc desc
     * @param appId      应用系统ID
     * @param dataSource 数据来源
     * @param isEnabled  启用状态：0. 禁用 1. 启用
     * @param isDeleted  是否删除
     */
    public Page<Record> getAdminDatas(int pageNumber, int pageSize, String keywords, String sortColumn, String sortType, Long appId, String dataSource, Boolean isEnabled, Boolean isDeleted) {
        //创建sql对象
        Sql sql = selectSql().page(pageNumber, pageSize);
        //sql条件处理
        sql.eq("app_id", appId);
        sql.eq("data_source", dataSource);
        sql.eqBooleanToChar("is_enabled", isEnabled);
        sql.eqBooleanToChar("is_deleted", isDeleted);
        //关键词模糊查询
        sql.likeMulti(keywords, "busobject_code", "display_name", "busobject_name");
        //排序
        sql.orderBy(sortColumn, sortType);

        Page<Record> page =  paginateRecord(sql, true);

        if (CollUtil.isNotEmpty(page.getList())) {

            ModelMap<Long, String> modelMap = new ModelMap<>(applicationService.getAllApplications(), Application.ID, Application.APP_NAME);

            for (Record row : page.getList()) {
                row.set("appName", modelMap.get(row.getLong("appId")));
                row.set("datasourceName", DataSourceEnum.toEnum(row.getStr("dataSource")).getText());
            }

        }
        return page;
    }

    private boolean notExists(String columnName, Object value) {
        return null == queryInt(selectSql().select("1").eq(columnName, value).eq(Busobject.IS_DELETED, "0"));
    }

    private boolean notExistsExcludeId(String columnName, Object value, long excludeId) {
        return null == queryInt(selectSql().select("1").eq(columnName, value).eq(Busobject.IS_DELETED, "0").notEq("id", excludeId));
    }

    /**
     * 保存
     */
    public Ret save(Busobject busobject, User loginUser, Date now) {
        if (busobject == null || isOk(busobject.getId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }

        ValidationUtils.isTrue(notExists(Busobject.BUSOBJECT_CODE, busobject.getBusobjectCode()), "业务对象编码已存在");
        ValidationUtils.isTrue(notExists(Busobject.BUSOBJECT_NAME, busobject.getBusobjectName()), "业务对象名称已存在");
        ValidationUtils.isTrue(notExists(Busobject.DATA_SOURCE, busobject.getDataSource()), "数据源已存在");

        busobject.setCreateUserId(loginUser.getId());
        busobject.setCreateUserName(loginUser.getName());
        busobject.setCreateTime(now);
        busobject.setLastUserId(loginUser.getId());
        busobject.setLastUserName(loginUser.getName());
        busobject.setLastUpdateTime(now);

        boolean success = busobject.save();
        if (success) {
            //添加日志
            addSaveSystemLog(busobject.getId(), JBoltUserKit.getUserId(), busobject.getBusobjectName());
        }
        return ret(success);
    }

    /**
     * 更新
     */
    public Ret update(Busobject busobject, User loginUser, Date now) {
        if (busobject == null || notOk(busobject.getId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }

        // 更新时需要判断数据存在
        Busobject dbBusobject = findById(busobject.getId());
        if (dbBusobject == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        ValidationUtils.isTrue(!dbBusobject.getIsDeleted(), "业务对象已被删除");

        if (ObjUtil.notEqual(busobject.getBusobjectCode(), dbBusobject.getBusobjectCode())) {
            ValidationUtils.isTrue(notExistsExcludeId(Busobject.BUSOBJECT_CODE, busobject.getBusobjectCode(), busobject.getId()), "业务对象编码已存在");
        }
        if (ObjUtil.notEqual(busobject.getBusobjectName(), dbBusobject.getBusobjectName())) {
            ValidationUtils.isTrue(notExistsExcludeId(Busobject.BUSOBJECT_NAME, busobject.getBusobjectName(), busobject.getId()), "业务对象名称已存在");
        }

        busobject.setLastUserId(loginUser.getId());
        busobject.setLastUserName(loginUser.getName());
        busobject.setLastUpdateTime(now);

        boolean success = busobject.update();

        if (success) {
            //添加日志
            addUpdateSystemLog(busobject.getId(), JBoltUserKit.getUserId(), busobject.getBusobjectName());
        }
        return ret(success);
    }

    /**
     * 删除数据后执行的回调
     *
     * @param busobject 要删除的model
     * @param kv        携带额外参数一般用不上
     */
    @Override
    protected String afterDelete(Busobject busobject, Kv kv) {
        addDeleteSystemLog(busobject.getId(), JBoltUserKit.getUserId(), busobject.getBusobjectName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param busobject model
     * @param kv        携带额外参数一般用不上
     */
    @Override
    public String checkInUse(Busobject busobject, Kv kv) {
        //这里用来覆盖 检测是否被其它表引用
        return null;
    }

    /**
     * toggle操作执行后的回调处理
     */
    @Override
    protected String afterToggleBoolean(Busobject busobject, String column, Kv kv) {
        addUpdateSystemLog(busobject.getId(), JBoltUserKit.getUserId(), busobject.getBusobjectName(), "的字段[" + column + "]值:" + busobject.get(column));
        /*
         switch(column){
         case "is_enabled":
         break;
         }
         */
        return null;
    }

    /**
     * 恢复假删数据后执行的回调
     *
     * @param busobject 要恢复的model
     */
    @Override
    protected String afterRecover(Busobject busobject) {
        addRecoverSystemLog(busobject.getId(), JBoltUserKit.getUserId(), busobject.getBusobjectName());
        return null;
    }

    public List<Busobject> getBusobjectList() {
        return find(selectSql().eq(Busobject.IS_DELETED, ZERO_STR));
    }

}
