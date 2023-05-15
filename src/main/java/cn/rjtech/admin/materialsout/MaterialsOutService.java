package cn.rjtech.admin.materialsout;

import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Db;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.MaterialsOut;
import com.jfinal.plugin.activerecord.Record;

/**
 * 出库管理-材料出库单列表 Service
 * @ClassName: MaterialsOutService
 * @author: RJ
 * @date: 2023-05-13 16:16
 */
public class MaterialsOutService extends BaseService<MaterialsOut> {

	private final MaterialsOut dao = new MaterialsOut().dao();

	@Override
	protected MaterialsOut dao() {
		return dao;
	}

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param kv
	 * @return
	 */
	public Page<Record> paginateAdminDatas(int pageNumber, int pageSize, Kv kv) {
		return dbTemplate("materialsout.paginateAdminDatas",kv).paginate(pageNumber, pageSize);
	}

	/**
	 * 保存
	 * @param materialsOut
	 * @return
	 */
	public Ret save(MaterialsOut materialsOut) {
		if(materialsOut==null || isOk(materialsOut.getAutoID())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(materialsOut.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=materialsOut.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(materialsOut.getAutoid(), JBoltUserKit.getUserId(), materialsOut.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param materialsOut
	 * @return
	 */
	public Ret update(MaterialsOut materialsOut) {
		if(materialsOut==null || notOk(materialsOut.getAutoID())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		MaterialsOut dbMaterialsOut=findById(materialsOut.getAutoID());
		if(dbMaterialsOut==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(materialsOut.getName(), materialsOut.getAutoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=materialsOut.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(materialsOut.getAutoid(), JBoltUserKit.getUserId(), materialsOut.getName());
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
	 * @param materialsOut 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(MaterialsOut materialsOut, Kv kv) {
		//addDeleteSystemLog(materialsOut.getAutoid(), JBoltUserKit.getUserId(),materialsOut.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param materialsOut 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(MaterialsOut materialsOut, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(materialsOut, kv);
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