package cn.rjtech.admin.depref;

import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import static cn.hutool.core.text.StrPool.COMMA;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import cn.hutool.core.collection.CollUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.User;
import cn.rjtech.admin.department.DepartmentService;
import cn.rjtech.constants.ErrorMsg;
import cn.rjtech.enums.IsOkEnum;
import cn.rjtech.model.momdata.DepRef;
import cn.rjtech.model.momdata.Department;
import cn.rjtech.util.ValidationUtils;
/**
 * 部门对照档案 Service
 * @ClassName: DepRefService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-30 20:58
 */
public class DepRefService extends BaseService<DepRef> {

	@Inject
	private DepartmentService departmentService;
	
	private final DepRef dao = new DepRef().dao();

	@Override
	protected DepRef dao() {
		return dao;
	}

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @return
	 */
	public Page<DepRef> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
		return paginateByKeywords("iAutoId","DESC", pageNumber, pageSize, keywords, "iAutoId");
	}

	/**
	 * 保存
	 * @param depRef
	 * @return
	 */
	public Ret save(DepRef depRef) {
		if(depRef==null || isOk(depRef.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(depRef.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=depRef.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(depRef.getIautoid(), JBoltUserKit.getUserId(), depRef.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param depRef
	 * @return
	 */
	public Ret update(DepRef depRef) {
		if(depRef==null || notOk(depRef.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		DepRef dbDepRef=findById(depRef.getIAutoId());
		if(dbDepRef==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(depRef.getName(), depRef.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=depRef.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(depRef.getIautoid(), JBoltUserKit.getUserId(), depRef.getName());
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
	 * @param depRef 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(DepRef depRef, Kv kv) {
		//addDeleteSystemLog(depRef.getIautoid(), JBoltUserKit.getUserId(),depRef.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param depRef 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(DepRef depRef, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(depRef, kv);
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
		return toggleBoolean(id, "isDeleted");
	}

	/**
	 * 切换isdefault属性
	 */
	public Ret toggleIsDefault(Long id) {
		//校验是否勾选保存到了部门对照表中
		ValidationUtils.isTrue(id != 0, "请勾选保存后设置默认值!");
		tx(()->{
			DepRef depRef = findById(id);
			ValidationUtils.notNull(depRef, "部门对照表中不存在数据，设置默认值失败!");
			//每个部门只能设置一个默认的对照部门，当设置默认值为是的时候，其它的默认值要设置为否
			if(!depRef.getIsDefault()){
				DepRef depRefElse = findFirst(selectSql().eq("idepid", depRef.getIDepId()).eq("isdefault", IsOkEnum.YES.getValue()));
				if(depRefElse != null) {
					depRefElse.setIsDefault(IsOkEnum.NO.getText());
					ValidationUtils.isTrue(depRefElse.update(), ErrorMsg.UPDATE_FAILED);
				}
			}
			toggleBoolean(id, "isDefault");
			return true;
		});
		return SUCCESS;
	}

	/**
	 * 检测是否可以toggle操作指定列
	 * @param depRef 要toggle的model
	 * @param column 操作的哪一列
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanToggle(DepRef depRef,String column, Kv kv) {
		//没有问题就返回null  有问题就返回提示string 字符串
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(DepRef depRef, String column, Kv kv) {
		//addUpdateSystemLog(depRef.getIautoid(), JBoltUserKit.getUserId(), depRef.getName(),"的字段["+column+"]值:"+depRef.get(column));
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param depRef model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(DepRef depRef, Kv kv) {
		//这里用来覆盖 检测DepRef是否被其它表引用
		return null;
	}
	/**
	 * 部门对照表查询部门档案中应用于禀议系统的部门（带层级关系）
	 * */
	public List<Record> findAllProposalData(Kv para) {
		para.set("iorgid",getOrgId());
		List<Record> records = dbTemplate("depref.findAllProposalData", para).find();
		return this.convertToRecordTree(records, "iautoid", "ipid", (px) -> {
			return this.notOk(px.getStr("ipid"));
		});		
	}
	/**
	 * 部门对照表查询部门档案的末级部门（带层级关系）
	 * */
	public List<Record> findEndDepData(Kv para) {
		Long sourceidepid = para.getLong("sourceidepid");
		Department sourceDepartment = departmentService.findById(sourceidepid);
		if(sourceDepartment.getBDepEnd()) return null;
		List<Record> rsList = new ArrayList<Record>();
		appendEndDepartmentDatas(rsList,sourceidepid,para);
		return rsList;
	}
	
	private void appendEndDepartmentDatas(List<Record> list,Long ipid,Kv para){
		List<Record> records = dbTemplate("depref.findDepDataByPId", Kv.by("iorgid", getOrgId()).set("ipid",ipid).set(para)).find();
		for (Record record : records) {
			if(record.getBoolean("bdepend")) {
				Long sourceidepid = para.getLong("sourceidepid");
				Long ireldepid = record.getLong("iautoid");
				Record depRefDataForEndDepRc = dbTemplate("depref.findDepRefDataForEndDep",Kv.by("iorgid", getOrgId()).set("idepid", sourceidepid).set("ireldepid",ireldepid)).findFirst();
				Long ideprefid = 0L;
				String isDefault = "0";
				if(depRefDataForEndDepRc!=null){
					ideprefid = depRefDataForEndDepRc.getLong("iautoid");
					isDefault = depRefDataForEndDepRc.getStr("isdefault");
				}
				record.set("ideprefid",ideprefid);
				record.set("isdefault", isDefault);
				list.add(record);
			}else {
				appendEndDepartmentDatas(list,record.getLong("iautoid"),para);
			}
		}
	}

	public Ret saveTableDatas(Kv para) {
		String ireldepids = para.getStr("ireldepids");
		Long idepid = para.getLong("idepid");
		ValidationUtils.notBlank(ireldepids, "末级部门未勾选!");
		ValidationUtils.notNull(idepid, "请加载末级部门数据!");
		User loginUser = JBoltUserKit.getUser();
		Date now = new Date();
		tx(()->{
			//删除对照部门所有的对照关系数据
			delete(deleteSql().eq("idepid", idepid));
			//重新生成部门对照关系数据
			String[] ireldepidArr = ireldepids.split(",");
			for (String ireldepid : ireldepidArr) {
				DepRef depRef = new DepRef(); 
				depRef.setIOrgId(getOrgId())
					.setCOrgCode(getOrgCode())
					.setCOrgName(getOrgName())
					.setIDepId(idepid)
					.setIRelDepId(Long.parseLong(ireldepid))
					.setIsDefault(IsOkEnum.NO.getText())
					.setICreateBy(loginUser.getId())
					.setCCreateName(loginUser.getUsername())
					.setDCreateTime(now)
					.setIUpdateBy(loginUser.getId())
					.setCUpdateName(loginUser.getUsername())
					.setDUpdateTime(now)
					.setIsDeleted(IsOkEnum.NO.getText());
				ValidationUtils.isTrue(depRef.save(), ErrorMsg.SAVE_FAILED);
			}
			return true;
		});
		return SUCCESS;
	}
	/**
	 * 获取指定部门id的对照部门id集合
	 * */
	public Ret findCheckedIds(Kv para) {
		para.set("iorgid",getOrgId());
        List<Long> list = dbTemplate("depref.findCheckedIds",para).query();
        return successWithData(Okv.by("ids", CollUtil.join(list, COMMA)));
    }
	/**
	 * 查询部门默认的末级对照部门record
	 * */
	public Record findIsDefaultEndDepRecord(String cdepcode){
		ValidationUtils.notBlank(cdepcode, "查询部门默认的末级对照部门时，部门编码参数为空!");
		Record rc = dbTemplate("depref.findIsDefaultEndDepRecord",Kv.by("cdepcode", cdepcode)).findFirst();
		ValidationUtils.notNull(rc, "[" + cdepcode + "]未设置默认的末级对照部门!");
		return rc;
	}

}