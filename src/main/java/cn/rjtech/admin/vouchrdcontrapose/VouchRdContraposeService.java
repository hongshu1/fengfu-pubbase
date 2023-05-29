package cn.rjtech.admin.vouchrdcontrapose;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.User;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.enums.SourceEnum;
import cn.rjtech.model.momdata.VouchRdContrapose;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.Date;
import java.util.List;

/**
 * 单据类型与收发类别对照表 Service
 * @ClassName: VouchRdContraposeService
 * @author: heming
 * @date: 2023-03-27 14:10
 */
public class VouchRdContraposeService extends BaseService<VouchRdContrapose> {

	private final VouchRdContrapose dao = new VouchRdContrapose().dao();

	@Override
	protected VouchRdContrapose dao() {
		return dao;
	}

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param
	 * @return
	 */
	public Page<Record> paginateAdminDatas(int pageNumber, int pageSize, Kv para) {
		para.set("iorgid",getOrgId());
		return dbTemplate("vouchrdcontrapose.paginateAdminDatas",para).paginate(pageNumber, pageSize);

	}

	/**
	 * 保存
	 * @param vouchRdContrapose
	 * @return
	 */
	public Ret save(VouchRdContrapose vouchRdContrapose) {
		if(vouchRdContrapose==null || isOk(vouchRdContrapose.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
	   VouchRdContrapose flag = selectCvrrcodeAndCvrsCode(vouchRdContrapose.getCVRRCode(), vouchRdContrapose.getCVRSCode());

		if (isOk(flag)){
			ValidationUtils.error("添加失败，类别已存在");
		}

		User user = JBoltUserKit.getUser();
		Date now = new Date();
		vouchRdContrapose.setIOrgId(getOrgId());
		vouchRdContrapose.setCOrgCode(getOrgCode());
		vouchRdContrapose.setCOrgName(getOrgName());
		vouchRdContrapose.setCVRRCode(vouchRdContrapose.getCVRRCode());
		vouchRdContrapose.setCVBTID(vouchRdContrapose.getCVBTID());
		vouchRdContrapose.setCVRSCode(vouchRdContrapose.getCVRSCode());

		vouchRdContrapose.setISource(SourceEnum.MES.getValue());
		vouchRdContrapose.setIsDeleted(false);
		vouchRdContrapose.setICreateBy(user.getId());
		vouchRdContrapose.setCCreateName(user.getName());
		vouchRdContrapose.setDCreateTime(now);
		vouchRdContrapose.setIUpdateBy(user.getId());
		vouchRdContrapose.setCUpdateName(user.getName());
		vouchRdContrapose.setDUpdateTime(now);
		boolean success=vouchRdContrapose.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(vouchRdContrapose.getIautoid(), JBoltUserKit.getUserId(), vouchRdContrapose.getName());
		}
		return ret(success);
	}

	private VouchRdContrapose selectCvrrcodeAndCvrsCode(String cvrrCode, String cvrsCode) {
		Okv data=Okv.by("cvrrCode",cvrrCode)
				.set("cvrsCode",cvrsCode);
		return daoTemplate("vouchrdcontrapose.selectCvrrcodeAndCvrsCode",data).findFirst();
	}

	private String getCvrrName(String cvrrCode) {
		Kv para=Kv.by("cvrrcode",cvrrCode);
		return dbTemplate("vouchrdcontrapose.getCvrrName",para).queryStr();

	}

	/**
	 * 更新
	 * @param vouchRdContrapose
	 * @return
	 */
	public Ret update(VouchRdContrapose vouchRdContrapose) {
		if(vouchRdContrapose==null || notOk(vouchRdContrapose.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		String cvrrCode = vouchRdContrapose.getCVRRCode();
		String cvrsCode = vouchRdContrapose.getCVRSCode();
		List<Record> list = selectCvrrcodeOnCvrsCodeList();
		for (Record row : list) {
			if (row.get("cvrrCode").equals(cvrrCode)&&row.get("cvrsCode").equals(cvrsCode)){
				ValidationUtils.error("修改失败，类别已存在");
			}
		}

		//更新时需要判断数据存在
		VouchRdContrapose dbVouchRdContrapose=findById(vouchRdContrapose.getIAutoId());
		if(dbVouchRdContrapose==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		User user = JBoltUserKit.getUser();
		Date now = new Date();
		vouchRdContrapose.setIUpdateBy(user.getId());
		vouchRdContrapose.setCUpdateName(user.getName());
		vouchRdContrapose.setDUpdateTime(now);
		boolean success=vouchRdContrapose.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(vouchRdContrapose.getIautoid(), JBoltUserKit.getUserId(), vouchRdContrapose.getName());
		}
		return ret(success);
	}

	private List<Record> selectCvrrcodeOnCvrsCodeList() {
		return dbTemplate("vouchrdcontrapose.selectCvrrcodeOnCvrsCodeList").find();
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
		return updateColumn(id, "isdeleted", true);
	}

	/**
	 * 删除数据后执行的回调
	 * @param vouchRdContrapose 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(VouchRdContrapose vouchRdContrapose, Kv kv) {
		//addDeleteSystemLog(vouchRdContrapose.getIautoid(), JBoltUserKit.getUserId(),vouchRdContrapose.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param vouchRdContrapose 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(VouchRdContrapose vouchRdContrapose, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(vouchRdContrapose, kv);
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
	 * 切换isdeleted属性
	 */
	public Ret toggleIsDeleted(Long id) {
		return toggleBoolean(id, "IsDeleted");
	}

	/**
	 * 检测是否可以toggle操作指定列
	 * @param vouchRdContrapose 要toggle的model
	 * @param column 操作的哪一列
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanToggle(VouchRdContrapose vouchRdContrapose,String column, Kv kv) {
		//没有问题就返回null  有问题就返回提示string 字符串
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(VouchRdContrapose vouchRdContrapose, String column, Kv kv) {
		//addUpdateSystemLog(vouchRdContrapose.getIautoid(), JBoltUserKit.getUserId(), vouchRdContrapose.getName(),"的字段["+column+"]值:"+vouchRdContrapose.get(column));
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param vouchRdContrapose model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(VouchRdContrapose vouchRdContrapose, Kv kv) {
		//这里用来覆盖 检测VouchRdContrapose是否被其它表引用
		return null;
	}

}