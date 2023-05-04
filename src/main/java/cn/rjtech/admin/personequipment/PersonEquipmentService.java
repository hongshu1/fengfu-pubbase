package cn.rjtech.admin.personequipment;

import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;

import java.util.List;

import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.PersonEquipment;
import cn.rjtech.util.ValidationUtils;
/**
 * 人员设备档案 Service
 * @ClassName: PersonEquipmentService
 * @author: WYX
 * @date: 2023-03-22 15:36
 */
public class PersonEquipmentService extends BaseService<PersonEquipment> {

	private final PersonEquipment dao = new PersonEquipment().dao();

	@Override
	protected PersonEquipment dao() {
		return dao;
	}

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @return
	 */
	public Page<PersonEquipment> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
		return paginateByKeywords("iAutoId","DESC", pageNumber, pageSize, keywords, "iAutoId");
	}

	/**
	 * 保存
	 * @param personEquipment
	 * @return
	 */
	public Ret save(PersonEquipment personEquipment) {
		if(personEquipment==null || isOk(personEquipment.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(personEquipment.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=personEquipment.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(personEquipment.getIautoid(), JBoltUserKit.getUserId(), personEquipment.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param personEquipment
	 * @return
	 */
	public Ret update(PersonEquipment personEquipment) {
		if(personEquipment==null || notOk(personEquipment.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		PersonEquipment dbPersonEquipment=findById(personEquipment.getIAutoId());
		if(dbPersonEquipment==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(personEquipment.getName(), personEquipment.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=personEquipment.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(personEquipment.getIautoid(), JBoltUserKit.getUserId(), personEquipment.getName());
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
	 * @param personEquipment 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(PersonEquipment personEquipment, Kv kv) {
		//addDeleteSystemLog(personEquipment.getIautoid(), JBoltUserKit.getUserId(),personEquipment.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param personEquipment 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(PersonEquipment personEquipment, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(personEquipment, kv);
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
	 * 检测是否可以toggle操作指定列
	 * @param personEquipment 要toggle的model
	 * @param column 操作的哪一列
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanToggle(PersonEquipment personEquipment,String column, Kv kv) {
		//没有问题就返回null  有问题就返回提示string 字符串
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(PersonEquipment personEquipment, String column, Kv kv) {
		//addUpdateSystemLog(personEquipment.getIautoid(), JBoltUserKit.getUserId(), personEquipment.getName(),"的字段["+column+"]值:"+personEquipment.get(column));
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param personEquipment model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(PersonEquipment personEquipment, Kv kv) {
		//这里用来覆盖 检测PersonEquipment是否被其它表引用
		return null;
	}
	/**
	 *	保存可编辑表格的新增行
	 * @param jBoltTable 
	 * @param iPersonId
	 */
	public void addSubmitTableDatas(JBoltTable jBoltTable, Long iPersonId) {
		List<Record> list = jBoltTable.getSaveRecordList();
		if(CollUtil.isEmpty(list)) return;
		for (Record row : list) {
			PersonEquipment personEquipment = new PersonEquipment();
			Long iEquipmentId = row.getLong("iequipmentid");
			personEquipment.setIPersonId(iPersonId);
			personEquipment.setIEquipmentId(iEquipmentId);
			personEquipment.setIsDeleted(false);
			ValidationUtils.isTrue(personEquipment.save(), JBoltMsg.FAIL);
		}
	}
	/**
	 *	保存可编辑表格的修改行
	 * @param jBoltTable 
	 */
	public void updateSubmitTableDatas(JBoltTable jBoltTable) {
		List<Record> list = jBoltTable.getUpdateRecordList();
		if(CollUtil.isEmpty(list)) return;
		for (Record row : list) {
			PersonEquipment personEquipment = findById(row.getLong("iautoid"));
			Long iEquipmentId = row.getLong("iequipmentid");
			personEquipment.setIEquipmentId(iEquipmentId);
			ValidationUtils.isTrue(personEquipment.update(), JBoltMsg.FAIL);
		}
	}
	/**
	 *	保存可编辑表格的删除行
	 * @param jBoltTable 
	 */
	public void deleteSubmitTableDatas(JBoltTable jBoltTable) {
		Object[] ids = jBoltTable.getDelete();
		if(ArrayUtil.isEmpty(ids)) return;
		for (Object id : ids) {
			PersonEquipment personEquipment = findById(id);
			personEquipment.setIsDeleted(true);
			ValidationUtils.isTrue(personEquipment.update(), JBoltMsg.FAIL);
		}
	}
	/**
	 * 根据人员档案ID查询人员设备数据
	 */
	public List<Record> findEditableDatas(Long iPersonId) {
		return dbTemplate("personequipment.findEditableDatas",Kv.by("iPersonId", iPersonId)).find();
	}
}