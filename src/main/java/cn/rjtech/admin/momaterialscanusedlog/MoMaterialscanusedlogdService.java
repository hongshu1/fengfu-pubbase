package cn.rjtech.admin.momaterialscanusedlog;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.MoMaterialscanusedlogd;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
/**
 * 制造工单-材料耗用明细 Service
 * @ClassName: MoMaterialscanusedlogdService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-26 09:36
 */
public class MoMaterialscanusedlogdService extends BaseService<MoMaterialscanusedlogd> {

	private final MoMaterialscanusedlogd dao = new MoMaterialscanusedlogd().dao();

	@Override
	protected MoMaterialscanusedlogd dao() {
		return dao;
	}

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @return
	 */
	public Page<MoMaterialscanusedlogd> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
		return paginateByKeywords("iAutoId","DESC", pageNumber, pageSize, keywords, "iAutoId");
	}

	/**
	 * 保存
	 * @param moMaterialscanusedlogd
	 * @return
	 */
	public Ret save(MoMaterialscanusedlogd moMaterialscanusedlogd) {
		if(moMaterialscanusedlogd==null || isOk(moMaterialscanusedlogd.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(moMaterialscanusedlogd.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=moMaterialscanusedlogd.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(moMaterialscanusedlogd.getIautoid(), JBoltUserKit.getUserId(), moMaterialscanusedlogd.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param moMaterialscanusedlogd
	 * @return
	 */
	public Ret update(MoMaterialscanusedlogd moMaterialscanusedlogd) {
		if(moMaterialscanusedlogd==null || notOk(moMaterialscanusedlogd.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		MoMaterialscanusedlogd dbMoMaterialscanusedlogd=findById(moMaterialscanusedlogd.getIAutoId());
		if(dbMoMaterialscanusedlogd==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(moMaterialscanusedlogd.getName(), moMaterialscanusedlogd.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=moMaterialscanusedlogd.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(moMaterialscanusedlogd.getIautoid(), JBoltUserKit.getUserId(), moMaterialscanusedlogd.getName());
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
	 * @param moMaterialscanusedlogd 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(MoMaterialscanusedlogd moMaterialscanusedlogd, Kv kv) {
		//addDeleteSystemLog(moMaterialscanusedlogd.getIautoid(), JBoltUserKit.getUserId(),moMaterialscanusedlogd.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param moMaterialscanusedlogd 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(MoMaterialscanusedlogd moMaterialscanusedlogd, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(moMaterialscanusedlogd, kv);
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