package cn.rjtech.admin.approvald;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.core.ui.jbolttable.JBoltTableMulti;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.approvaldrole.ApprovaldRoleService;
import cn.rjtech.admin.approvalduser.ApprovaldUserService;
import cn.rjtech.model.momdata.ApprovalD;
import cn.rjtech.model.momdata.ApprovaldRole;
import cn.rjtech.model.momdata.ApprovaldUser;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.Date;
import java.util.List;

/**
 * 审批流节点 Service
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
	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @return
	 */
	public Page<ApprovalD> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
		return paginateByKeywords("iAutoId","DESC", pageNumber, pageSize, keywords, "iAutoId");
	}

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param kv
	 * @return
	 */
	public Page<Record> paginateAdminDatas(int pageNumber, int pageSize, Kv kv) {

		return dbTemplate("approvald.findRecordsByMid", kv).paginate(pageNumber, pageSize);

	}

	/**
	 * 保存
	 * @param approvalD
	 * @return
	 */
	public Ret save(ApprovalD approvalD) {
		if(approvalD==null || isOk(approvalD.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(approvalD.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=approvalD.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(approvalD.getIautoid(), JBoltUserKit.getUserId(), approvalD.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param approvalD
	 * @return
	 */
	public Ret update(ApprovalD approvalD) {
		if(approvalD==null || notOk(approvalD.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		ApprovalD dbApprovalD=findById(approvalD.getIAutoId());
		if(dbApprovalD==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(approvalD.getName(), approvalD.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=approvalD.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(approvalD.getIautoid(), JBoltUserKit.getUserId(), approvalD.getName());
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

		boolean success = tx(()->{

			delete("delete from Bd_ApprovalD_Role where iApprovalDid = "+id);
			delete("delete from Bd_ApprovalD_User where iApprovalDid = "+id);
			deleteById(id,true);
			return true;
		});
		if(!success) {
			return fail("操作失败！");
		}
		return SUCCESS;
	}

	/**
	 * 删除数据后执行的回调
	 * @param approvalD 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(ApprovalD approvalD, Kv kv) {
		//addDeleteSystemLog(approvalD.getIautoid(), JBoltUserKit.getUserId(),approvalD.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param approvalD 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(ApprovalD approvalD, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(approvalD, kv);
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
	 * @param approvalD 要toggle的model
	 * @param column 操作的哪一列
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanToggle(ApprovalD approvalD,String column, Kv kv) {
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
	 * @param approvalD model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(ApprovalD approvalD, Kv kv) {
		//这里用来覆盖 检测ApprovalD是否被其它表引用
		return null;
	}

	/**
	 * 根据外键获取数据
	 * @param id
	 * @return
	 */
    public List<ApprovalD> findListByMid(String id) {

    	Kv kv = new Kv();
    	kv.set("id",id);
		return daoTemplate("approvald.findListByMid", kv).find();
    }

	/**
	 * 人员数据源
	 * @param pageNum
	 * @param pageSize
	 * @param kv
	 * @return
	 */
    public Page<Record> getPerson(int pageNum, int pageSize, Kv kv){
		String orgCode = getOrgCode();
		kv.setIfNotNull("orgCode", orgCode);
		return dbTemplate("approvald.getPerson", kv).paginate(pageNum, pageSize);
	}

	/**
	 * 角色数据源
	 * @param pageNum
	 * @param pageSize
	 * @param kv
	 * @return
	 */
	public Page<Record> getRole(int pageNum, int pageSize, Kv kv){
		String orgCode = getOrgCode();
		kv.setIfNotNull("orgCode", orgCode);
		return dbTemplate("approvald.getRole", kv).paginate(pageNum, pageSize);
	}

	/**
	 * 下拉框数据源
	 * @param kv
	 * @return
	 */
	public List<Record> selectPerson(Kv kv){
		String orgCode = getOrgCode();
		kv.setIfNotNull("orgCode", orgCode);
		return dbTemplate("approvald.selectPerson", kv).find();
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

		ApprovalD approvalD = jBoltTable.getFormModel(ApprovalD.class, "approvalD");
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
						List<ApprovaldUser> saveModelList = jBoltTable.getSaveModelList(ApprovaldUser.class);
						saveModelList.forEach(approvaldUser -> {
							approvaldUser.setIApprovalDid(iApprovalDid);
						});
						approvaldUserService.batchSave(saveModelList,saveModelList.size());
					}
					if (jBoltTable.updateIsNotBlank()){
						List<ApprovaldUser> updateModelList = jBoltTable.getUpdateModelList(ApprovaldUser.class);
						approvaldUserService.batchUpdate(updateModelList,updateModelList.size());
					}

					if (jBoltTable.deleteIsNotBlank()){
						approvaldUserService.deleteByIds(jBoltTable.getDelete());
					}
					break;
				case 5:
					if (jBoltTable2.saveIsNotBlank()){
						List<ApprovaldRole> saveModelList = jBoltTable2.getSaveModelList(ApprovaldRole.class);
						saveModelList.forEach(approvaldRole -> {
							approvaldRole.setIApprovalDid(iApprovalDid);
						});
						approvaldRoleService.batchSave(saveModelList,saveModelList.size());
					}

                    if (jBoltTable.updateIsNotBlank()){
                        List<ApprovaldRole> updateModelList = jBoltTable.getUpdateModelList(ApprovaldRole.class);
                        approvaldRoleService.batchUpdate(updateModelList,updateModelList.size());
                    }

					if (jBoltTable2.deleteIsNotBlank()){
						approvaldRoleService.deleteByIds(jBoltTable2.getDelete());
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
		return dbTemplate("approvald.lineDatas",kv).paginate(pageNum, pageSize);
	}


	/**
	 * 角色行数据
	 * @param pageNum
	 * @param pageSize
	 * @param kv
	 * @return
	 */
	public Page<Record> roleDatas(int pageNum, int pageSize, Kv kv){
		return dbTemplate("approvald.roleDatas",kv).paginate(pageNum, pageSize);
	}

    /**
     * 排序 上移
     * @param id
     * @return
     */
    public Ret up(Long id) {
        ApprovalD approvalD = findById(id);
        if(approvalD==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
        else {
            Integer iSeq = approvalD.getISeq();
            if (iSeq != null && iSeq > 0) {
                if (iSeq == 1) {
                    return fail("已经是第一个");
                } else {
                    Long iApprovalMid = approvalD.getIApprovalMid();
                    Integer wSeq = iSeq-1;
                    ApprovalD upApprovalD =
                            findFirst("select * from Bd_ApprovalD where iApprovalMid = '" + iApprovalMid +
                            "' and iSeq = " + wSeq);
                    if (upApprovalD == null) {
                        return fail("操作失败，请点击刷新按钮");
                    } else {
                        upApprovalD.setISeq(iSeq);
                        approvalD.setISeq(wSeq);
                        upApprovalD.update();
                        approvalD.update();
                        return SUCCESS;
                    }
                }
            } else {
                return fail("操作失败，请点击刷新按钮");
            }
        }
    }

    /**
     * 排序 下移
     * @param id
     * @return
     */
    public Ret down(Long id) {
		ApprovalD approvalD = findById(id);
		if(approvalD==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		else {
			Integer iSeq = approvalD.getISeq();
			Long iApprovalMid = approvalD.getIApprovalMid();
			List<ApprovalD> listByMid = findListByMid(iApprovalMid.toString());
			int max = listByMid.size();
			if (iSeq != null && iSeq > 0) {
				if (iSeq == max){
					return fail("已经是最后一个");
				} else {
					Integer wSeq = iSeq+1;
					ApprovalD upApprovalD =
							findFirst("select * from Bd_ApprovalD where iApprovalMid = '" + iApprovalMid +
									"' and iSeq = " + wSeq);
					if (upApprovalD == null) {
						return fail("操作失败，请点击刷新按钮");
					} else {
						upApprovalD.setISeq(iSeq);
						approvalD.setISeq(wSeq);
						upApprovalD.update();
						approvalD.update();
						return SUCCESS;
					}
				}
			} else {
				return fail("操作失败，请点击刷新按钮");
			}
		}
    }
}
