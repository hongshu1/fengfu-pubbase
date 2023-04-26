package cn.rjtech.admin.QcInspection;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.QcInspection;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
/**
 * 工程内品质巡查 Service
 * @ClassName: QcInspectionService
 * @author: RJ
 * @date: 2023-04-26 13:49
 */
public class QcInspectionService extends BaseService<QcInspection> {

	private final QcInspection dao = new QcInspection().dao();

	@Override
	protected QcInspection dao() {
		return dao;
	}

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @return
	 */
	public Page<QcInspection> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
		return paginateByKeywords("iAutoId","DESC", pageNumber, pageSize, keywords, "iAutoId");
	}

	/**
	 * 保存
	 * @param qcInspection
	 * @return
	 */
	public Ret save(QcInspection qcInspection) {
		if(qcInspection==null || isOk(qcInspection.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(qcInspection.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=qcInspection.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(qcInspection.getIautoid(), JBoltUserKit.getUserId(), qcInspection.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param qcInspection
	 * @return
	 */
	public Ret update(QcInspection qcInspection) {
		if(qcInspection==null || notOk(qcInspection.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		QcInspection dbQcInspection=findById(qcInspection.getIAutoId());
		if(dbQcInspection==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(qcInspection.getName(), qcInspection.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=qcInspection.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(qcInspection.getIautoid(), JBoltUserKit.getUserId(), qcInspection.getName());
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
	 * @param qcInspection 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(QcInspection qcInspection, Kv kv) {
		//addDeleteSystemLog(qcInspection.getIautoid(), JBoltUserKit.getUserId(),qcInspection.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param qcInspection 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(QcInspection qcInspection, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(qcInspection, kv);
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
	 * 切换isfirstcase属性
	 */
	public Ret toggleIsFirstCase(Long id) {
		return toggleBoolean(id, "isFirstCase");
	}

	/**
	 * 检测是否可以toggle操作指定列
	 * @param qcInspection 要toggle的model
	 * @param column 操作的哪一列
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanToggle(QcInspection qcInspection,String column, Kv kv) {
		//没有问题就返回null  有问题就返回提示string 字符串
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(QcInspection qcInspection, String column, Kv kv) {
		//addUpdateSystemLog(qcInspection.getIautoid(), JBoltUserKit.getUserId(), qcInspection.getName(),"的字段["+column+"]值:"+qcInspection.get(column));
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param qcInspection model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(QcInspection qcInspection, Kv kv) {
		//这里用来覆盖 检测QcInspection是否被其它表引用
		return null;
	}

}