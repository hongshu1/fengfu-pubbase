package cn.rjtech.admin.investmentplanitem;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.StrSplitter;
import cn.hutool.core.util.StrUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.User;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.department.DepartmentService;
import cn.rjtech.constants.Constants;
import cn.rjtech.constants.ErrorMsg;
import cn.rjtech.enums.FinishStatusEnum;
import cn.rjtech.enums.IsEnableEnum;
import cn.rjtech.enums.ProposalmSourceTypeEnum;
import cn.rjtech.enums.ServiceTypeEnum;
import cn.rjtech.model.momdata.InvestmentPlanItem;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;

import static cn.hutool.core.text.StrPool.COMMA;

/**
 * 投资计划项目 Service
 *
 * @ClassName: InvestmentPlanItemService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-10-18 09:37
 */
public class InvestmentPlanItemService extends BaseService<InvestmentPlanItem> {

    private final InvestmentPlanItem dao = new InvestmentPlanItem().dao();
    @Inject
    private DepartmentService departmentService;
    @Override
    protected InvestmentPlanItem dao() {
        return dao;
    }

    /**
     * 后台管理分页查询
     */
    public Page<InvestmentPlanItem> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
        return paginateByKeywords("iAutoId", "desc", pageNumber, pageSize, keywords, "name");
    }

    /**
     * 保存
     */
    public Ret save(InvestmentPlanItem investmentPlanItem) {
        if (investmentPlanItem == null || isOk(investmentPlanItem.getIautoid())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }

        tx(() -> {
            // ValidationUtils.isTrue(notExists(columnName, value), JBoltMsg.DATA_SAME_NAME_EXIST);
            ValidationUtils.isTrue(investmentPlanItem.save(), ErrorMsg.SAVE_FAILED);


            // TODO 其他业务代码实现

            return true;
        });

        // 添加日志
        // addSaveSystemLog(investmentPlanItem.getIautoid(), JBoltUserKit.getUserId(), investmentPlanItem.getName());
        return SUCCESS;
    }

    /**
     * 更新
     */
    public Ret update(InvestmentPlanItem investmentPlanItem) {
        if (investmentPlanItem == null || notOk(investmentPlanItem.getIautoid())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }

        tx(() -> {
            // 更新时需要判断数据存在
            InvestmentPlanItem dbInvestmentPlanItem = findById(investmentPlanItem.getIautoid());
            ValidationUtils.notNull(dbInvestmentPlanItem, JBoltMsg.DATA_NOT_EXIST);

            // TODO 其他业务代码实现
            // ValidationUtils.isTrue(notExists(columnName, value), JBoltMsg.DATA_SAME_NAME_EXIST);

            ValidationUtils.isTrue(investmentPlanItem.update(), ErrorMsg.UPDATE_FAILED);

            return true;
        });

        //添加日志
        //addUpdateSystemLog(investmentPlanItem.getIautoid(), JBoltUserKit.getUserId(), investmentPlanItem.getName());
        return SUCCESS;
    }

    /**
     * 删除 指定多个ID
     */
    public Ret deleteByBatchIds(String ids) {
        tx(() -> {
            for (String idStr : StrSplitter.split(ids, COMMA, true, true)) {
                long iAutoId = Long.parseLong(idStr);
                InvestmentPlanItem dbInvestmentPlanItem = findById(iAutoId);
                ValidationUtils.notNull(dbInvestmentPlanItem, JBoltMsg.DATA_NOT_EXIST);

                // TODO 可能需要补充校验组织账套权限
                // TODO 存在关联使用时，校验是否仍在使用

                ValidationUtils.isTrue(dbInvestmentPlanItem.delete(), JBoltMsg.FAIL);
            }

            return true;
        });

        // 添加日志
        // InvestmentPlanItem investmentPlanItem = ret.getAs("data");
        // addDeleteSystemLog(iAutoId, JBoltUserKit.getUserId(), SystemLog.TARGETTYPE_xxx, investmentPlanItem.getName());
        return SUCCESS;
    }

    /**
     * 删除数据后执行的回调
     *
     * @param investmentPlanItem 要删除的model
     * @param kv                 携带额外参数一般用不上
     */
    @Override
    protected String afterDelete(InvestmentPlanItem investmentPlanItem, Kv kv) {
        //addDeleteSystemLog(investmentPlanItem.getIautoid(), JBoltUserKit.getUserId(),investmentPlanItem.getName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param investmentPlanItem 要删除的model
     * @param kv                 携带额外参数一般用不上
     */
    @Override
    public String checkCanDelete(InvestmentPlanItem investmentPlanItem, Kv kv) {
        //如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
        return checkInUse(investmentPlanItem, kv);
    }

    /**
     * 设置返回二开业务所属的关键systemLog的targetType
     */
    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

    /**
     * 切换isimport属性
     */
    public Ret toggleIsimport(Long id) {
        return toggleBoolean(id, "isImport");
    }

    /**
     * 切换ispriorreport属性
     */
    public Ret toggleIspriorreport(Long id) {
        return toggleBoolean(id, "isPriorReport");
    }

    /**
     * 检测是否可以toggle操作指定列
     *
     * @param investmentPlanItem 要toggle的model
     * @param column             操作的哪一列
     * @param kv                 携带额外参数一般用不上
     */
    @Override
    public String checkCanToggle(InvestmentPlanItem investmentPlanItem, String column, Kv kv) {
        //没有问题就返回null  有问题就返回提示string 字符串
        return null;
    }

    /**
     * toggle操作执行后的回调处理
     */
    @Override
    protected String afterToggleBoolean(InvestmentPlanItem investmentPlanItem, String column, Kv kv) {
        //addUpdateSystemLog(investmentPlanItem.getIautoid(), JBoltUserKit.getUserId(), investmentPlanItem.getName(),"的字段["+column+"]值:"+investmentPlanItem.get(column));
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param investmentPlanItem model
     * @param kv                 携带额外参数一般用不上
     */
    @Override
    public String checkInUse(InvestmentPlanItem investmentPlanItem, Kv kv) {
        //这里用来覆盖 检测InvestmentPlanItem是否被其它表引用
        return null;
    }

    public List<InvestmentPlanItem> findModelByPlanId(Long iplanid) {
        return find(selectSql().eq("iplanid", iplanid));
    }

    public Page<Record> paginateMdatas(Integer pageNumber, Integer pageSize, Kv para) {
        User user = JBoltUserKit.getUser();
        // 系统管理员 || 存在权限部门
	    para.set("iorgid", getOrgId())
	            .set("isSystemAdmin", user.getIsSystemAdmin())
	            .set("iservicetype",ServiceTypeEnum.INVESTMENT_PLAN.getValue())
				.set("istatus",FinishStatusEnum.UNFINISHED.getValue())
	    		.set("isfreeze",IsEnableEnum.NO.getValue());
	    Page<Record> page = dbTemplate("investmentplanitem.paginateMdatas", para).paginate(pageNumber, pageSize);
	    if (CollUtil.isNotEmpty(page.getList())) {
	        for (Record row : page.getList()) {
	        	row.set("cdepname", departmentService.getCdepName(row.getStr("cdepcode")));
	            Constants.fillPlanItem(row);
	        }
	    }
	    return page;
    }

    public List<Record> findByIautoids(String iinvestmentplanitemids) {
        return findRecords("SELECT * FROM PL_Investment_Plan_Item WHERE iautoid IN (" + iinvestmentplanitemids + ")");
    }

    public Ret checkPlanItem(String iinvestmentplanitemids) {
        List<InvestmentPlanItem> edittingItems = getEdittingPlanItems(iinvestmentplanitemids);
        if (CollUtil.isNotEmpty(edittingItems)) {
            StringBuilder errMsg = new StringBuilder();
            for (InvestmentPlanItem item : edittingItems) {
                errMsg.append(item.getCitemname()).append(StrUtil.DASHED).append(item.getCplanno()).append("<br/>");
            }
            return fail(errMsg.append("存在于编辑状态的禀议书中，请排除以上选项").toString());
        }
        return SUCCESS;
    }

    public List<InvestmentPlanItem> getEdittingPlanItems(String iinvestmentplanitemids) {
        Okv para = Okv.by("isourcetype", ProposalmSourceTypeEnum.INVESTMENT_PLAN.getValue())
                .set("iinvestmentplanitemids", iinvestmentplanitemids);

        return daoTemplate("investmentplanitem.getEdittingPlanItems", para).find();
    }
    /**
     * 查询不存在于项目卡片的投资计划项目
     * */
	public List<InvestmentPlanItem> findNotExistsProjectCardInvestmentItem(Long iplanid) {
		Kv para = Kv.by("iservicetype", ServiceTypeEnum.INVESTMENT_PLAN.getValue()).set("iorgid",getOrgId()).set("iplanid",iplanid);
		return daoTemplate("investmentplanitem.findNotExistsProjectCardInvestmentItem",para).find();
	}

	public void deleteByCplanNo(String ccode) {
		delete(deleteSql().eq("cplanno", ccode));
	}

}