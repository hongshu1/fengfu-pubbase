package cn.rjtech.admin.momotask;

import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Db;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.MoMotask;
/**
 * 制造工单任务 Service
 * @ClassName: MoMotaskService
 * @author: chentao
 * @date: 2023-04-28 15:19
 */
public class MoMotaskService extends BaseService<MoMotask> {

	private final MoMotask dao = new MoMotask().dao();

	@Override
	protected MoMotask dao() {
		return dao;
	}

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @return
	 */
	public Page<MoMotask> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
		return paginateByKeywords("iAutoId","DESC", pageNumber, pageSize, keywords, "iAutoId");
	}

	/**
	 * 保存
	 * @param moMotask
	 * @return
	 */
	public Ret save(MoMotask moMotask) {
		if(moMotask==null || isOk(moMotask.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(moMotask.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=moMotask.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(moMotask.getIautoid(), JBoltUserKit.getUserId(), moMotask.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param moMotask
	 * @return
	 */
	public Ret update(MoMotask moMotask) {
		if(moMotask==null || notOk(moMotask.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		MoMotask dbMoMotask=findById(moMotask.getIAutoId());
		if(dbMoMotask==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(moMotask.getName(), moMotask.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=moMotask.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(moMotask.getIautoid(), JBoltUserKit.getUserId(), moMotask.getName());
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
	 * @param moMotask 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(MoMotask moMotask, Kv kv) {
		//addDeleteSystemLog(moMotask.getIautoid(), JBoltUserKit.getUserId(),moMotask.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param moMotask 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(MoMotask moMotask, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(moMotask, kv);
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
	 * @param moMotask 要toggle的model
	 * @param column 操作的哪一列
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanToggle(MoMotask moMotask,String column, Kv kv) {
		//没有问题就返回null  有问题就返回提示string 字符串
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(MoMotask moMotask, String column, Kv kv) {
		//addUpdateSystemLog(moMotask.getIautoid(), JBoltUserKit.getUserId(), moMotask.getName(),"的字段["+column+"]值:"+moMotask.get(column));
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param moMotask model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(MoMotask moMotask, Kv kv) {
		//这里用来覆盖 检测MoMotask是否被其它表引用
		return null;
	}

}