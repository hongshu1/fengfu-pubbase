package cn.rjtech.admin.depref;

import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;

import java.util.List;

import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.DepRef;
/**
 * 部门对照档案 Service
 * @ClassName: DepRefService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-30 20:58
 */
public class DepRefService extends BaseService<DepRef> {

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
		return toggleBoolean(id, "isDefault");
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

}