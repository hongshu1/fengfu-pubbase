package cn.rjtech.admin.proposalcategory;

import cn.hutool.core.text.StrSplitter;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.constants.ErrorMsg;
import cn.rjtech.model.momdata.Proposalcategory;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static cn.hutool.core.text.StrPool.COMMA;

/**
 * 禀议类型区分 Service
 *
 * @ClassName: ProposalcategoryService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-09-14 11:46
 */
public class ProposalcategoryService extends BaseService<Proposalcategory> {

    private final Proposalcategory dao = new Proposalcategory().dao();

    @Override
    protected Proposalcategory dao() {
        return dao;
    }

    /**
     * 后台管理分页查询
     */
    public Page<Record> paginateAdminDatas(int pageNumber, int pageSize, Kv para) {
        return dbTemplate("proposalcategory.paginateAdminDatas", para).paginate(pageNumber, pageSize);
    }

    /**
     * 下级数据源
     */
    public Page<Record> downPaginateAdminDatas(Integer pageNumber, Integer pageSize, Kv para) {
        return dbTemplate("proposalcategory.downPaginateAdminDatas", para).paginate(pageNumber, pageSize);
    }

    /**
     * 保存
     */
    public Ret save(Proposalcategory proposalcategory) {
        if (proposalcategory == null || isOk(proposalcategory.getIautoid())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }

        // 批量新增
        String ccategoryname = proposalcategory.getCcategoryname();
        String[] ccategorynames = ccategoryname.split(" ");
        int size = ccategorynames.length;
        String ccategorycode = proposalcategory.getCcategorycode();
        String[] ccategorycodes = ccategorycode.split(" ");
        if (ccategorynames.length != ccategorycodes.length) {
            return fail("名称与编号数量不一致");
        } else {
            List<Proposalcategory> dics = new ArrayList<>();

            for (int i = 0; i < size; ++i) {
                Proposalcategory proposalcategory1 = new Proposalcategory();
                String name = ccategorynames[i];
                String sn = ccategorycodes[i];
                if (proposalcategory.getIpid() == null) {
                    proposalcategory1.setIlevel(1);
                    proposalcategory1.setIpid(0L);
                } else {
                    proposalcategory1.setIlevel(2);
                    proposalcategory1.setIpid(proposalcategory.getIpid());
                }
                proposalcategory1.setCorgcode(getOrgCode());
                proposalcategory1.setIorgid(getOrgId());
                proposalcategory1.setIcreatebby(JBoltUserKit.getUserId());
                proposalcategory1.setCreatetime(new Date());
                proposalcategory1.setCcategoryname(name);
                proposalcategory1.setCcategorycode(sn);
                dics.add(proposalcategory1);
            }

            tx(() -> {
                batchSave(dics);

                return true;
            });
        }

        // 添加日志
        // addSaveSystemLog(proposalcategory.getIautoid(), JBoltUserKit.getUserId(), proposalcategory.getName());
        return SUCCESS;
    }

    /**
     * 更新
     */
    public Ret update(Proposalcategory proposalcategory) {
        if (proposalcategory == null || notOk(proposalcategory.getIautoid())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        int size1 = find("select * from Bas_ProposalCategory where cCategoryCode = ? and iautoid != ?  ", proposalcategory.getCcategorycode(), proposalcategory.getIautoid()).size();
        int size = find("select * from Bas_ProposalCategory where cCategoryName = ? and iautoid != ? ", proposalcategory.getCcategoryname(), proposalcategory.getIautoid()).size();
        ValidationUtils.isTrue(size == 0, "存在重复的名称");
        ValidationUtils.isTrue(size1 == 0, "存在重复的编号");
        proposalcategory.setCorgcode(getOrgCode());
        proposalcategory.setIorgid(getOrgId());
        proposalcategory.setIupdateby(JBoltUserKit.getUserId());
        proposalcategory.setUpdatetime(new Date());

        tx(() -> {
            // 更新时需要判断数据存在
            Proposalcategory dbProposalcategory = findById(proposalcategory.getIautoid());
            ValidationUtils.notNull(dbProposalcategory, JBoltMsg.DATA_NOT_EXIST);

            // TODO 其他业务代码实现
            // ValidationUtils.isTrue(notExists(columnName, value), JBoltMsg.DATA_SAME_NAME_EXIST);

            ValidationUtils.isTrue(proposalcategory.update(), ErrorMsg.UPDATE_FAILED);

            return true;
        });

        //添加日志
        //addUpdateSystemLog(proposalcategory.getIautoid(), JBoltUserKit.getUserId(), proposalcategory.getName());
        return SUCCESS;
    }

    /**
     * 删除 指定多个ID
     */
    public Ret deleteByBatchIds(String ids) {
        tx(() -> {
            for (String idStr : StrSplitter.split(ids, COMMA, true, true)) {
                long iAutoId = Long.parseLong(idStr);
                Proposalcategory dbProposalcategory = findById(iAutoId);
                //查询是否有使用的类别区分
                //int size= proposalmServce.find("select * from PL_ProposalM where categoryCode =",dbProposalcategory.getCcategorycode()).size();
                //ValidationUtils.isTrue(size!=0,"在禀议书中已使用该禀议类别区分不可删除");
                ValidationUtils.notNull(dbProposalcategory, JBoltMsg.DATA_NOT_EXIST);
                if (dbProposalcategory.getIpid() == 0) {
                    List<Proposalcategory> proposalcategories = find("select * from Bas_ProposalCategory where ipid=?", iAutoId);
                    for (Proposalcategory proposalcategory : proposalcategories) {
                        ValidationUtils.isTrue(proposalcategory.delete(), JBoltMsg.FAIL);
                    }
                }
                // TODO 可能需要补充校验组织账套权限
                // TODO 存在关联使用时，校验是否仍在使用

                ValidationUtils.isTrue(dbProposalcategory.delete(), JBoltMsg.FAIL);
            }

            return true;
        });

        // 添加日志
        // Proposalcategory proposalcategory = ret.getAs("data");
        // addDeleteSystemLog(iAutoId, JBoltUserKit.getUserId(), SystemLog.TARGETTYPE_xxx, proposalcategory.getName());
        return SUCCESS;
    }

    /**
     * 删除数据后执行的回调
     *
     * @param proposalcategory 要删除的model
     * @param kv               携带额外参数一般用不上
     */
    @Override
    protected String afterDelete(Proposalcategory proposalcategory, Kv kv) {
        //addDeleteSystemLog(proposalcategory.getIautoid(), JBoltUserKit.getUserId(),proposalcategory.getName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param proposalcategory 要删除的model
     * @param kv               携带额外参数一般用不上
     */
    @Override
    public String checkCanDelete(Proposalcategory proposalcategory, Kv kv) {
        //如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
        return checkInUse(proposalcategory, kv);
    }

    /**
     * 设置返回二开业务所属的关键systemLog的targetType
     */
    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

    public Ret clearByTypeId(Long typeId) {
        if (this.notOk(typeId)) {
            return this.fail("参数异常");
        }

        List<Proposalcategory> proposalcategories = find("select * from Bas_ProposalCategory where ipid=?", typeId);
        if (proposalcategories != null) {
            for (Proposalcategory proposalcategory : proposalcategories) {
                ValidationUtils.notNull(proposalcategory, JBoltMsg.DATA_NOT_EXIST);
                ValidationUtils.isTrue(proposalcategory.delete(), JBoltMsg.FAIL);
            }
        }
        return SUCCESS;
    }

    /**
     * 根据主编号查询
     */
    public List<Record> numberSelect(String cCategoryCode) {
        //查询是否存在父节点
        Proposalcategory first = findFirst("select * from Bas_ProposalCategory where cCategoryCode =? and iPid =0 ", cCategoryCode);
        ValidationUtils.notNull(first, "没有该编号为:" + cCategoryCode + ",一级禀议区分类型");
        return dbTemplate("proposalcategory.numberSelect", Kv.by("cCategoryCode", cCategoryCode)).find();
    }

    public List<Proposalcategory> getAllRoleTreeDatas() {
        return this.convertToModelTree(this.findAll(), "iautoid", "ipid", (m) -> this.notOk(m.getIpid()));
    }

    public List<Record> findAllRecords() {
        return findRecords("SELECT * FROM Bas_ProposalCategory WHERE iorgid = ? ", getOrgId());
    }

}