package cn.rjtech.admin.formapprovald;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.core.ui.jbolttable.JBoltTableMulti;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.formapprovaldrole.FormapprovaldRoleService;
import cn.rjtech.admin.formapprovalduser.FormapprovaldUserService;
import cn.rjtech.admin.formapprovalflowd.FormApprovalFlowDService;
import cn.rjtech.model.momdata.*;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 表单审批流 Service
 * @ClassName: FormApprovalDService
 * @author: RJ
 * @date: 2023-04-18 17:29
 */
public class FormApprovalDService extends BaseService<FormApprovalD> {

	private final FormApprovalD dao = new FormApprovalD().dao();

	@Override
	protected FormApprovalD dao() {
		return dao;
	}

	@Inject
    private FormapprovaldUserService formapprovaldUserService;
	@Inject
    private FormapprovaldRoleService formapprovaldRoleService;
	@Inject
    private FormApprovalFlowDService flowDService;


	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @return
	 */
	public Page<FormApprovalD> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
		return paginateByKeywords("iAutoId","DESC", pageNumber, pageSize, keywords, "iAutoId");
	}

    /**
     * 后台管理分页查询
     * @param pageNumber
     * @param pageSize
     * @param kv
     * @return
     */
    public Page<Record> datas(int pageNumber, int pageSize, Kv kv) {
        Page<Record> paginate = dbTemplate("formapprovald.findRecordsByFormId", kv).paginate(pageNumber, pageSize);
        return paginate;

    }

	/**
	 * 历史数据源
	 * @param pageNumber
	 * @param pageSize
	 * @param kv
	 * @return
	 */
	public Page<Record> historyDatas(int pageNumber, int pageSize, Kv kv) {
		Page<Record> paginate = dbTemplate("formapprovald.findRecordsByFormIdHistory", kv).paginate(pageNumber,
				pageSize);
		return paginate;

	}

	/**
	 * 保存
	 * @param formApprovalD
	 * @return
	 */
	public Ret save(FormApprovalD formApprovalD) {
		if(formApprovalD==null || isOk(formApprovalD.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(formApprovalD.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=formApprovalD.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(formApprovalD.getIautoid(), JBoltUserKit.getUserId(), formApprovalD.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param formApprovalD
	 * @return
	 */
	public Ret update(FormApprovalD formApprovalD) {
		if(formApprovalD==null || notOk(formApprovalD.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		FormApprovalD dbFormApprovalD=findById(formApprovalD.getIAutoId());
		if(dbFormApprovalD==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(formApprovalD.getName(), formApprovalD.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=formApprovalD.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(formApprovalD.getIautoid(), JBoltUserKit.getUserId(), formApprovalD.getName());
		}
		return ret(success);
	}

	/**
	 * 删除 指定多个ID
	 * @param ids
	 * @return
	 */
	public Ret deleteByBatchIds(String ids) {
		return deleteByIds(ids,true);
	}

	/**
	 * 删除
	 * @param id
	 * @return
	 */
	public Ret delete(Long id) {
		return deleteById(id,true);
	}

	/**
	 * 删除数据后执行的回调
	 * @param formApprovalD 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(FormApprovalD formApprovalD, Kv kv) {
		//addDeleteSystemLog(formApprovalD.getIautoid(), JBoltUserKit.getUserId(),formApprovalD.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param formApprovalD 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(FormApprovalD formApprovalD, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(formApprovalD, kv);
	}

	/**
	 * 设置返回二开业务所属的关键systemLog的targetType
	 * @return
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
	 * @param formApprovalD 要toggle的model
	 * @param column 操作的哪一列
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanToggle(FormApprovalD formApprovalD,String column, Kv kv) {
		//没有问题就返回null  有问题就返回提示string 字符串
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(FormApprovalD formApprovalD, String column, Kv kv) {
		//addUpdateSystemLog(formApprovalD.getIautoid(), JBoltUserKit.getUserId(), formApprovalD.getName(),"的字段["+column+"]值:"+formApprovalD.get(column));
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param formApprovalD model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(FormApprovalD formApprovalD, Kv kv) {
		//这里用来覆盖 检测FormApprovalD是否被其它表引用
		return null;
	}

    /**
     * 复制相同数据
     * @param Mid
     * @param approvalD
     * @return
     */
	public FormApprovalD copySetObj(Long Mid, ApprovalD approvalD){
		FormApprovalD formApprovalD = new FormApprovalD();
		formApprovalD.setIFormApprovalId(Mid);
		formApprovalD.setISeq(approvalD.getISeq());
		formApprovalD.setIStep(approvalD.getIStep());
		formApprovalD.setCName(approvalD.getCName());
		formApprovalD.setIType(approvalD.getIType());
		formApprovalD.setIApprovalWay(approvalD.getIApprovalWay());
		formApprovalD.setISupervisorType(approvalD.getISupervisorType());
		formApprovalD.setIsDirectOnMissing(approvalD.getIsDirectOnMissing());
		formApprovalD.setISkipOn(approvalD.getISkipOn());
		formApprovalD.setISpecUserId(approvalD.getISpecUserId());
		formApprovalD.setISelfSelectWay(approvalD.getISelfSelectWay());
		formApprovalD.setISingleType(approvalD.getISingleType());
		formApprovalD.setISingleId(approvalD.getISingleId());
		formApprovalD.setIStatus(1);
		return formApprovalD;
	}

    /**
     * 根据外键获取数据
     * @param id
     * @return
     */
    public List<FormApprovalD> findListByMid(Long id) {

        return find("select * from Bd_FormApprovalD where iFormApprovalId = '"+id+"' order by iSeq asc ");
    }

	/**
	 * 根据外键获取未审批的节点数据
	 * @param id
	 * @return
	 */
	public List<FormApprovalD> findListByAid(Long id) {

		return find("select * from Bd_FormApprovalD where iFormApprovalId = '"+id+"' and iStatus = 1 order by iSeq " +
				"asc ");
	}

	/**
	 * 审批人列表数据源
	 * @param pageNum
	 * @param pageSize
	 * @param kv
	 * @return
	 */
	public Page<Record> userDatas(int pageNum, int pageSize, Kv kv){
		return dbTemplate("formapprovald.userDatas", kv).paginate(pageNum,pageSize);
	}


    /**
     * 多个可编辑表格同时提交
     * @param jboltTableMulti
     * @return
     */
    public Ret submitByJBoltTables(JBoltTableMulti jboltTableMulti) {
        if(jboltTableMulti==null||jboltTableMulti.isEmpty()) {
            return fail(JBoltMsg.JBOLTTABLE_IS_BLANK);
        }
        //这里可以循环遍历 保存处理每个表格 也可以 按照name自己get出来单独处理
//		jboltTableMulti.entrySet().forEach(en->{
//			JBoltTable jBoltTable = en.getValue();
        JBoltTable jBoltTable = jboltTableMulti.getJBoltTable("table1");
        JBoltTable jBoltTable2 = jboltTableMulti.getJBoltTable("table2");
        Date now = new Date();

        FormApprovalD approvalD = jBoltTable.getFormModel(FormApprovalD.class, "formApprovalD");
        ValidationUtils.notNull(approvalD, JBoltMsg.PARAM_ERROR);

        tx(() -> {
            //修改
            if(isOk(approvalD.getIAutoId())){
                ValidationUtils.isTrue(approvalD.update(), JBoltMsg.FAIL);
            }else{
                //新增
                ValidationUtils.isTrue(approvalD.save(), JBoltMsg.FAIL);
            }

            Long iApprovalDid = approvalD.getIAutoId();
            Integer iType = approvalD.getIType();

            switch (iType){
                case 1:  //指定人员
                    if (jBoltTable.saveIsNotBlank()){
                        List<FormapprovaldUser> saveModelList = jBoltTable.getSaveModelList(FormapprovaldUser.class);

                        Record flowM = findFirstRecord("select * from Bd_FormApprovalFlowM where iApprovalDid = " + iApprovalDid);
                        Long flowMid = flowM.getLong("iautoid");

                        List<Record> flowDList = findRecord("select * from Bd_FormApprovalFlowD where " +
                                "iFormApprovalFlowMid = " + flowMid);

                        int size = flowDList.size();
                        List<FormApprovalFlowD> flowDS = new ArrayList<>();

                        for (int i = 0; i < saveModelList.size(); i++) {
                            FormapprovaldUser approvaldUser = saveModelList.get(i);
                            approvaldUser.setIAuditStatus(1);
                            approvaldUser.setIFormApprovalId(iApprovalDid);
                            FormApprovalFlowD flowD = new FormApprovalFlowD();
                            flowD.setIFormApprovalFlowMid(flowMid);
                            flowD.setISeq(++size);
                            flowD.setIUserId(approvaldUser.getIUserId());
                            flowD.setIAuditStatus(1);
                            flowDS.add(flowD);
                        }
                        formapprovaldUserService.batchSave(saveModelList,saveModelList.size());
                        flowDService.batchSave(flowDS,flowDS.size());

                    }
                    if (jBoltTable.updateIsNotBlank()){
                        List<FormapprovaldUser> updateModelList = jBoltTable.getUpdateModelList(FormapprovaldUser.class);
                        formapprovaldUserService.batchUpdate(updateModelList,updateModelList.size());
                    }

                    if (jBoltTable.deleteIsNotBlank()){
                        formapprovaldUserService.deleteByIds(jBoltTable.getDelete());
//                        flowDService.deleteByMidAndUserId(iApprovalDid,jBoltTable.getDelete());
                    }
                    break;
                case 5:
                    if (jBoltTable2.saveIsNotBlank()){
                        List<FormapprovaldRole> saveModelList = jBoltTable2.getSaveModelList(FormapprovaldRole.class);
                        saveModelList.forEach(approvaldRole -> {
                            approvaldRole.setIFormApprovalId(iApprovalDid);
                        });
                        formapprovaldRoleService.batchSave(saveModelList,saveModelList.size());
                    }
                    if (jBoltTable.updateIsNotBlank()){
                        List<FormapprovaldRole> updateModelList = jBoltTable.getUpdateModelList(FormapprovaldRole.class);
                        formapprovaldRoleService.batchUpdate(updateModelList,updateModelList.size());
                    }

                    if (jBoltTable2.deleteIsNotBlank()){
                        formapprovaldRoleService.deleteByIds(jBoltTable2.getDelete());
                    }
                    break;
                default:
                    break;
            }
            return true;
        });

        return SUCCESS;
    }

    /**
     * 人员行数据
     * @param pageNum
     * @param pageSize
     * @param kv
     * @return
     */
    public Page<Record> lineDatas(int pageNum, int pageSize, Kv kv){
        return dbTemplate("formapprovald.lineDatas",kv).paginate(pageNum, pageSize);
    }


    /**
     * 角色行数据
     * @param pageNum
     * @param pageSize
     * @param kv
     * @return
     */
    public Page<Record> roleDatas(int pageNum, int pageSize, Kv kv){
        return dbTemplate("formapprovald.roleDatas",kv).paginate(pageNum, pageSize);
    }

}
