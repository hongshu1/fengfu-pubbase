package cn.rjtech.admin.momaterialsreturnd;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.MoMaterialsreturnd;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;

/**
 * 制造工单-生产退料明细 Service
 * @ClassName: MoMaterialsreturndService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-25 16:34
 */
public class MoMaterialsreturndService extends BaseService<MoMaterialsreturnd> {

	private final MoMaterialsreturnd dao = new MoMaterialsreturnd().dao();

	@Override
	protected MoMaterialsreturnd dao() {
		return dao;
	}

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @return
	 */
	public Page<MoMaterialsreturnd> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
		return paginateByKeywords("iAutoId","DESC", pageNumber, pageSize, keywords, "iAutoId");
	}

	/**
	 * 保存
	 * @param moMaterialsreturnd
	 * @return
	 */
	public Ret save(MoMaterialsreturnd moMaterialsreturnd) {
		if(moMaterialsreturnd==null || isOk(moMaterialsreturnd.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(moMaterialsreturnd.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=moMaterialsreturnd.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(moMaterialsreturnd.getIautoid(), JBoltUserKit.getUserId(), moMaterialsreturnd.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param moMaterialsreturnd
	 * @return
	 */
	public Ret update(MoMaterialsreturnd moMaterialsreturnd) {
		if(moMaterialsreturnd==null || notOk(moMaterialsreturnd.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		MoMaterialsreturnd dbMoMaterialsreturnd=findById(moMaterialsreturnd.getIAutoId());
		if(dbMoMaterialsreturnd==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(moMaterialsreturnd.getName(), moMaterialsreturnd.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=moMaterialsreturnd.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(moMaterialsreturnd.getIautoid(), JBoltUserKit.getUserId(), moMaterialsreturnd.getName());
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
	 * @param moMaterialsreturnd 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(MoMaterialsreturnd moMaterialsreturnd, Kv kv) {
		//addDeleteSystemLog(moMaterialsreturnd.getIautoid(), JBoltUserKit.getUserId(),moMaterialsreturnd.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param moMaterialsreturnd 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(MoMaterialsreturnd moMaterialsreturnd, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(moMaterialsreturnd, kv);
	}

	/**
	 * 设置返回二开业务所属的关键systemLog的targetType 
	 * @return
	 */
	@Override
	protected int systemLogTargetType() {
		return ProjectSystemLogTargetType.NONE.getValue();
	}

}