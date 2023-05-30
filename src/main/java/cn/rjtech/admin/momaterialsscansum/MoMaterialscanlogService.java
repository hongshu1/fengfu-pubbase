package cn.rjtech.admin.momaterialsscansum;

import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Db;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.MoMaterialscanlog;
/**
 * 制造工单-齐料明细 Service
 * @ClassName: MoMaterialscanlogService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-26 09:50
 */
public class MoMaterialscanlogService extends BaseService<MoMaterialscanlog> {

	private final MoMaterialscanlog dao = new MoMaterialscanlog().dao();

	@Override
	protected MoMaterialscanlog dao() {
		return dao;
	}

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @return
	 */
	public Page<MoMaterialscanlog> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
		return paginateByKeywords("iAutoId","DESC", pageNumber, pageSize, keywords, "iAutoId");
	}

	/**
	 * 保存
	 * @param moMaterialscanlog
	 * @return
	 */
	public Ret save(MoMaterialscanlog moMaterialscanlog) {
		if(moMaterialscanlog==null || isOk(moMaterialscanlog.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(moMaterialscanlog.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=moMaterialscanlog.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(moMaterialscanlog.getIautoid(), JBoltUserKit.getUserId(), moMaterialscanlog.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param moMaterialscanlog
	 * @return
	 */
	public Ret update(MoMaterialscanlog moMaterialscanlog) {
		if(moMaterialscanlog==null || notOk(moMaterialscanlog.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		MoMaterialscanlog dbMoMaterialscanlog=findById(moMaterialscanlog.getIAutoId());
		if(dbMoMaterialscanlog==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(moMaterialscanlog.getName(), moMaterialscanlog.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=moMaterialscanlog.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(moMaterialscanlog.getIautoid(), JBoltUserKit.getUserId(), moMaterialscanlog.getName());
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
	 * @param moMaterialscanlog 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(MoMaterialscanlog moMaterialscanlog, Kv kv) {
		//addDeleteSystemLog(moMaterialscanlog.getIautoid(), JBoltUserKit.getUserId(),moMaterialscanlog.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param moMaterialscanlog 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(MoMaterialscanlog moMaterialscanlog, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(moMaterialscanlog, kv);
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