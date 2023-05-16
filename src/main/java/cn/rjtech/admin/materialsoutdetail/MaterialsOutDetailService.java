package cn.rjtech.admin.materialsoutdetail;

import cn.rjtech.util.ValidationUtils;
import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Db;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.MaterialsOutDetail;
/**
 * 出库管理-材料出库单列表 Service
 * @ClassName: MaterialsOutDetailService
 * @author: RJ
 * @date: 2023-05-13 16:09
 */
public class MaterialsOutDetailService extends BaseService<MaterialsOutDetail> {

	private final MaterialsOutDetail dao = new MaterialsOutDetail().dao();

	@Override
	protected MaterialsOutDetail dao() {
		return dao;
	}

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @return
	 */
	public Page<MaterialsOutDetail> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
		return paginateByKeywords("iAutoId","DESC", pageNumber, pageSize, keywords, "iAutoId");
	}

	/**
	 * 保存
	 * @param materialsOutDetail
	 * @return
	 */
	public Ret save(MaterialsOutDetail materialsOutDetail) {
		if(materialsOutDetail==null || isOk(materialsOutDetail.getAutoID())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(materialsOutDetail.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=materialsOutDetail.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(materialsOutDetail.getAutoid(), JBoltUserKit.getUserId(), materialsOutDetail.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param materialsOutDetail
	 * @return
	 */
	public Ret update(MaterialsOutDetail materialsOutDetail) {
		if(materialsOutDetail==null || notOk(materialsOutDetail.getAutoID())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		MaterialsOutDetail dbMaterialsOutDetail=findById(materialsOutDetail.getAutoID());
		if(dbMaterialsOutDetail==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(materialsOutDetail.getName(), materialsOutDetail.getAutoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=materialsOutDetail.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(materialsOutDetail.getAutoid(), JBoltUserKit.getUserId(), materialsOutDetail.getName());
		}
		return ret(success);
	}

	/**
	 * 删除 指定多个ID
	 * @param ids
	 * @return
	 */
	public Ret deleteByBatchIds(String ids) {
		tx(() -> {
			ValidationUtils.notNull(ids, JBoltMsg.DATA_NOT_EXIST);
			delete("delete from T_Sys_MaterialsOutDetail where MasID = '"+ids+"'");
			return true;
		});
		return SUCCESS;
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
	 * @param materialsOutDetail 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(MaterialsOutDetail materialsOutDetail, Kv kv) {
		//addDeleteSystemLog(materialsOutDetail.getAutoid(), JBoltUserKit.getUserId(),materialsOutDetail.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param materialsOutDetail 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(MaterialsOutDetail materialsOutDetail, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(materialsOutDetail, kv);
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