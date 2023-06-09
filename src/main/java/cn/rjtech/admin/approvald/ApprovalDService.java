package cn.rjtech.admin.approvald;

import cn.jbolt._admin.user.UserService;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.model.User;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.core.ui.jbolttable.JBoltTableMulti;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.approvaldrole.ApprovaldRoleService;
import cn.rjtech.admin.approvaldroleusers.ApprovaldRoleusersService;
import cn.rjtech.admin.approvalduser.ApprovaldUserService;
import cn.rjtech.model.momdata.ApprovalD;
import cn.rjtech.model.momdata.ApprovaldRole;
import cn.rjtech.model.momdata.ApprovaldRoleusers;
import cn.rjtech.model.momdata.ApprovaldUser;
import cn.rjtech.util.ValidationUtils;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 审批流节点 Service
 *
 * @ClassName: ApprovalDService
 * @author: RJ
 * @date: 2023-04-18 17:04
 */
public class ApprovalDService extends BaseService<ApprovalD> {

    private final ApprovalD dao = new ApprovalD().dao();

    @Override
    protected ApprovalD dao() {
        return dao;
    }

    @Inject
    private ApprovaldUserService approvaldUserService;
    @Inject
    private ApprovaldRoleService approvaldRoleService;
    @Inject
    private UserService userService;
    @Inject
    private ApprovaldRoleusersService roleusersService;

    /**
     * 后台管理分页查询
     */
    public Page<ApprovalD> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
        return paginateByKeywords("iAutoId", "DESC", pageNumber, pageSize, keywords, "iAutoId");
    }

    /**
     * 后台管理分页查询
     */
    public Page<Record> paginateAdminDatas(int pageNumber, int pageSize, Kv kv) {

        return dbTemplate("approvald.findRecordsByMid", kv).paginate(pageNumber, pageSize);

    }

    /**
     * 保存
     */
    public Ret save(ApprovalD approvalD) {
        if (approvalD == null || isOk(approvalD.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //if(existsName(approvalD.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = approvalD.save();
        if (success) {
            //添加日志
            //addSaveSystemLog(approvalD.getIautoid(), JBoltUserKit.getUserId(), approvalD.getName());
        }
        return ret(success);
    }

    /**
     * 更新
     */
    public Ret update(ApprovalD approvalD) {
        if (approvalD == null || notOk(approvalD.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //更新时需要判断数据存在
        ApprovalD dbApprovalD = findById(approvalD.getIAutoId());
        if (dbApprovalD == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        //if(existsName(approvalD.getName(), approvalD.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = approvalD.update();
        if (success) {
            //添加日志
            //addUpdateSystemLog(approvalD.getIautoid(), JBoltUserKit.getUserId(), approvalD.getName());
        }
        return ret(success);
    }

    /**
     * 删除 指定多个ID
     */
    public Ret deleteByBatchIds(String ids) {
        return deleteByIds(ids, true);
    }

    /**
     * 删除
     */
    public Ret delete(Long id) {

        boolean success = tx(() -> {

            delete("delete from Bd_ApprovalD_Role where iApprovalDid = " + id);
            delete("delete from Bd_ApprovalD_User where iApprovalDid = " + id);
            deleteById(id, true);
            return true;
        });
        if (!success) {
            return fail("操作失败！");
        }
        return SUCCESS;
    }

    /**
     * 删除数据后执行的回调
     *
     * @param approvalD 要删除的model
     * @param kv        携带额外参数一般用不上
     */
    @Override
    protected String afterDelete(ApprovalD approvalD, Kv kv) {
        //addDeleteSystemLog(approvalD.getIautoid(), JBoltUserKit.getUserId(),approvalD.getName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param approvalD 要删除的model
     * @param kv        携带额外参数一般用不上
     */
    @Override
    public String checkCanDelete(ApprovalD approvalD, Kv kv) {
        //如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
        return checkInUse(approvalD, kv);
    }

    /**
     * 设置返回二开业务所属的关键systemLog的targetType
     */
    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

    /**
     * 切换isdirectonmissing属性
     */
    public Ret toggleIsDirectOnMissing(Long id) {
        return toggleBoolean(id, "isDirectOnMissing");
    }

    /**
     * 检测是否可以toggle操作指定列
     *
     * @param approvalD 要toggle的model
     * @param column    操作的哪一列
     * @param kv        携带额外参数一般用不上
     */
    @Override
    public String checkCanToggle(ApprovalD approvalD, String column, Kv kv) {
        //没有问题就返回null  有问题就返回提示string 字符串
        return null;
    }

    /**
     * toggle操作执行后的回调处理
     */
    @Override
    protected String afterToggleBoolean(ApprovalD approvalD, String column, Kv kv) {
        //addUpdateSystemLog(approvalD.getIautoid(), JBoltUserKit.getUserId(), approvalD.getName(),"的字段["+column+"]值:"+approvalD.get(column));
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param approvalD model
     * @param kv        携带额外参数一般用不上
     */
    @Override
    public String checkInUse(ApprovalD approvalD, Kv kv) {
        //这里用来覆盖 检测ApprovalD是否被其它表引用
        return null;
    }

    /**
     * 根据外键获取数据
     */
    public List<ApprovalD> findListByMid(String id) {
        Kv kv = new Kv();
        kv.set("id", id);
        return daoTemplate("approvald.findListByMid", kv).find();
    }

    /**
     * 人员数据源
     */
    public Page<Record> getPerson(int pageNum, int pageSize, Kv kv) {
        String orgCode = getOrgCode();
        kv.setIfNotNull("orgCode", orgCode);
        return dbTemplate("approvald.getPerson", kv).paginate(pageNum, pageSize);
    }

    /**
     * 角色数据源
     */
    public Page<Record> getRole(int pageNum, int pageSize, Kv kv) {
        String orgCode = getOrgCode();
        kv.setIfNotNull("orgCode", orgCode);
        return dbTemplate("approvald.getRole", kv).paginate(pageNum, pageSize);
    }

    /**
     * 下拉框数据源
     */
    public List<Record> selectPerson(Kv kv) {
        String orgCode = getOrgCode();
        kv.setIfNotNull("orgCode", orgCode);
        return dbTemplate("approvald.selectPerson", kv).find();
    }

    /**
     * 多个可编辑表格同时提交
     */
    public Ret submitByJBoltTables(JBoltTableMulti jboltTableMulti) {
        if (jboltTableMulti == null || jboltTableMulti.isEmpty()) {
            return fail(JBoltMsg.JBOLTTABLE_IS_BLANK);
        }

        // 这里可以循环遍历 保存处理每个表格 也可以 按照name自己get出来单独处理
        // jboltTableMulti.entrySet().forEach(en->{ JBoltTable jBoltTable = en.getValue()
        JBoltTable jBoltTable = jboltTableMulti.getJBoltTable("table1");
        JBoltTable jBoltTable2 = jboltTableMulti.getJBoltTable("table2");

        ApprovalD approvalD = jBoltTable.getFormModel(ApprovalD.class, "approvalD");
        ValidationUtils.notNull(approvalD, JBoltMsg.PARAM_ERROR);
        AtomicReference<Long> iApprovalDid = new AtomicReference<>();
        tx(() -> {
            //修改
            if (isOk(approvalD.getIAutoId())) {
                ValidationUtils.isTrue(approvalD.update(), JBoltMsg.FAIL);
            } else {
                //新增
                ValidationUtils.isTrue(approvalD.save(), JBoltMsg.FAIL);
            }
            iApprovalDid.set(approvalD.getIAutoId());

            Integer iType = approvalD.getIType();

            switch (iType) {
                case 1:  //指定人员
                    if (jBoltTable.saveIsNotBlank()) {
                        List<ApprovaldUser> saveModelList = jBoltTable.getSaveModelList(ApprovaldUser.class);
                        saveModelList.forEach(approvaldUser -> {
                            approvaldUser.setIApprovalDid(iApprovalDid.get());
                        });
                        approvaldUserService.batchSave(saveModelList, saveModelList.size());
                    }
                    if (jBoltTable.updateIsNotBlank()) {
                        List<ApprovaldUser> updateModelList = jBoltTable.getUpdateModelList(ApprovaldUser.class);
                        approvaldUserService.batchUpdate(updateModelList, updateModelList.size());
                    }

                    if (jBoltTable.deleteIsNotBlank()) {
                        approvaldUserService.deleteByIds(jBoltTable.getDelete());
                    }
                    break;
                case 5:
                    if (jBoltTable2.saveIsNotBlank()) {
                        List<ApprovaldRole> saveModelList = jBoltTable2.getSaveModelList(ApprovaldRole.class);
                        saveModelList.forEach(approvaldRole -> {
                            approvaldRole.setIApprovalDid(iApprovalDid.get());
                            approvaldRole.save();
                            Page<User> userPage = userService.paginateUsersByRoleId(1, 20, approvaldRole.getIRoleId());
                            List<User> userList = userPage.getList();
                            if (userList.size() > 0){
                                List<ApprovaldRoleusers> list = new ArrayList<>();
                                for (int i = 0; i < userList.size(); i++) {
                                    User user = userList.get(i);
                                    int j = i;
                                    ApprovaldRoleusers roleusers = new ApprovaldRoleusers();
                                    roleusers.setIApprovaldRoleId(approvaldRole.getIAutoId());
                                    roleusers.setISeq(++j);
                                    roleusers.setIUserId(user.getId());
                                    list.add(roleusers);
                                }
                                roleusersService.batchSave(list,list.size());
                            }
                        });
//                        approvaldRoleService.batchSave(saveModelList, saveModelList.size());
                    }

                    if (jBoltTable.updateIsNotBlank()) {
                        List<ApprovaldRole> updateModelList = jBoltTable.getUpdateModelList(ApprovaldRole.class);
                        approvaldRoleService.batchUpdate(updateModelList, updateModelList.size());
                    }

                    if (jBoltTable2.deleteIsNotBlank()) {
                        approvaldRoleService.deleteByIds(jBoltTable2.getDelete());
                        roleusersService.deleteByApprovalId(jBoltTable2.getDelete());
                    }
                    break;
                default:
                    break;
            }

//            序列化Iseq
            List<ApprovaldUser> approvaldUsers = approvaldUserService.find("select * from Bd_ApprovalD_User where iApprovalDid = " + iApprovalDid.get() + " " +
                    "order by iSeq asc");
            if (approvaldUsers.size() > 0){
                for (int i = 0, iSeq = 0; i < approvaldUsers.size(); i++) {
                    ApprovaldUser approvaldUser = approvaldUsers.get(i);
                    approvaldUser.setISeq(++iSeq);
                }
                approvaldUserService.batchUpdate(approvaldUsers,approvaldUsers.size());
            }

            List<ApprovaldRole> approvaldRoles =
                    approvaldRoleService.find("select * from Bd_ApprovalD_Role where iApprovalDid = " + iApprovalDid.get() + " " +
                    "order by iSeq asc");
            if (approvaldRoles.size() > 0){

                for (int i = 0, iSeq = 0; i < approvaldRoles.size(); i++) {
                    ApprovaldRole approvaldRole = approvaldRoles.get(i);
                    approvaldRole.setISeq(++iSeq);
                }

                approvaldRoleService.batchUpdate(approvaldRoles,approvaldRoles.size());
            }

            return true;
        });

        return SUCCESS.set("iautoid",iApprovalDid.get());
    }


    /**
     * 人员行数据
     */
    public Page<Record> lineDatas(int pageNum, int pageSize, Kv kv) {
        return dbTemplate("approvald.lineDatas", kv).paginate(pageNum, pageSize);
    }

    /**
     * 角色行数据
     */
    public Page<Record> roleDatas(int pageNum, int pageSize, Kv kv) {
        return dbTemplate("approvald.roleDatas", kv).paginate(pageNum, pageSize);
    }

    /**
     * 排序 上移
     */
    public Ret up(Long id) {
        ApprovalD approvalD = findById(id);
        if (approvalD == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }

        Integer iSeq = approvalD.getISeq();
        if (iSeq != null && iSeq > 0) {
            if (iSeq == 1) {
                return fail("已经是第一个");
            }

            Long iApprovalMid = approvalD.getIApprovalMid();
            Integer wSeq = iSeq - 1;
            ApprovalD upApprovalD = findFirst("select * from Bd_ApprovalD where iApprovalMid = '" + iApprovalMid + "' and iSeq = " + wSeq);
            if (upApprovalD == null) {
                return fail("操作失败，请点击刷新按钮");
            }

            upApprovalD.setISeq(iSeq);
            approvalD.setISeq(wSeq);
            upApprovalD.update();
            approvalD.update();
            return SUCCESS;
        }
        return fail("操作失败，请点击刷新按钮");
    }

    /**
     * 排序 下移
     */
    public Ret down(Long id) {
        ApprovalD approvalD = findById(id);
        if (approvalD == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }

        Integer iSeq = approvalD.getISeq();
        Long iApprovalMid = approvalD.getIApprovalMid();
        List<ApprovalD> listByMid = findListByMid(iApprovalMid.toString());

        int max = listByMid.size();

        if (iSeq != null && iSeq > 0) {
            if (iSeq == max) {
                return fail("已经是最后一个");
            }

            Integer wSeq = iSeq + 1;
            ApprovalD upApprovalD = findFirst("select * from Bd_ApprovalD where iApprovalMid = '" + iApprovalMid + "' and iSeq = " + wSeq);
            if (upApprovalD == null) {
                return fail("操作失败，请点击刷新按钮");
            }

            upApprovalD.setISeq(iSeq);
            approvalD.setISeq(wSeq);
            upApprovalD.update();
            approvalD.update();
            return SUCCESS;
        }
        return fail("操作失败，请点击刷新按钮");
    }

    /**
     * 获取角色人员信息
     * @return
     */
    public Page<Record> roleUsers(int pageNumber, int pageSize, Kv kv){
        return roleusersService.dbTemplate("approvald.roleUsers",kv).paginate(pageNumber, pageSize);
    }

    /**
     *
     * @param pageNumber
     * @param pageSize
     * @param kv
     */
    public Page<Record> chooseUsers(int pageNumber, int pageSize, Kv kv){

        return roleusersService.dbTemplate("approvald.chooseUsers",kv).paginate(pageNumber, pageSize);
    }

    /**
     * 保存角色人员方法
     * @param jBoltTable
     * @return
     */
    public Ret saveRoleUser(JBoltTable jBoltTable) {
        ValidationUtils.notNull(jBoltTable, JBoltMsg.PARAM_ERROR);

        JSONObject form = jBoltTable.getForm();
        System.out.println("form===="+form);
        Long autoId = form.getLong("autoId");

        tx(() -> {
            if (jBoltTable.saveIsNotBlank()){
            List<ApprovaldRoleusers> saveModelList = jBoltTable.getSaveModelList(ApprovaldRoleusers.class);
                System.out.println("saveModelList===>"+saveModelList);
            saveModelList.forEach(approvaldRoleusers -> {
                approvaldRoleusers.setIApprovaldRoleId(autoId);
            });
            roleusersService.batchSave(saveModelList,saveModelList.size());
        }
        if (jBoltTable.deleteIsNotBlank()){
            roleusersService.realDeleteByIds(jBoltTable.getDelete());
        }

            List<ApprovaldRoleusers> roleusers = roleusersService.find("select * from Bd_ApprovalD_RoleUsers where iApprovaldRoleId = " + autoId + " " +
                    "order by iSeq asc");

            if (roleusers.size() > 0) {
                List<ApprovaldRoleusers> roleusersList = new ArrayList<>();
                for (int j = 0, rank = 0; j < roleusers.size(); j++) {
                    ApprovaldRoleusers approvaldRoleusers = roleusers.get(j);
                    approvaldRoleusers.setISeq(++rank);
                    roleusersList.add(approvaldRoleusers);
                }
                roleusersService.batchUpdate(roleusersList,roleusersList.size());
            }

            return true;
        });

        return SUCCESS;
    }
}
