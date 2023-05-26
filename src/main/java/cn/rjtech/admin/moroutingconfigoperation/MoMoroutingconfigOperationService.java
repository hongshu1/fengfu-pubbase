package cn.rjtech.admin.moroutingconfigoperation;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.MoMoroutingconfigOperation;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;

/**
 * 制造工单-存货工艺配置工序 Service
 * @ClassName: MoMoroutingconfigOperationService
 * @author: RJ
 * @date: 2023-05-09 16:49
 */
public class MoMoroutingconfigOperationService extends BaseService<MoMoroutingconfigOperation> {

	private final MoMoroutingconfigOperation dao = new MoMoroutingconfigOperation().dao();

	@Override
	protected MoMoroutingconfigOperation dao() {
		return dao;
	}

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @return
	 */
	public Page<MoMoroutingconfigOperation> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
		return paginateByKeywords("iAutoId","DESC", pageNumber, pageSize, keywords, "iAutoId");
	}

	/**
	 * 保存
	 * @param moMoroutingconfigOperation
	 * @return
	 */
	public Ret save(MoMoroutingconfigOperation moMoroutingconfigOperation) {
		if(moMoroutingconfigOperation==null || isOk(moMoroutingconfigOperation.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(moMoroutingconfigOperation.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=moMoroutingconfigOperation.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(moMoroutingconfigOperation.getIAutoId(), JBoltUserKit.getUserId(), moMoroutingconfigOperation.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param moMoroutingconfigOperation
	 * @return
	 */
	public Ret update(MoMoroutingconfigOperation moMoroutingconfigOperation) {
		if(moMoroutingconfigOperation==null || notOk(moMoroutingconfigOperation.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		MoMoroutingconfigOperation dbMoMoroutingconfigOperation=findById(moMoroutingconfigOperation.getIAutoId());
		if(dbMoMoroutingconfigOperation==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(moMoroutingconfigOperation.getName(), moMoroutingconfigOperation.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=moMoroutingconfigOperation.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(moMoroutingconfigOperation.getIAutoId(), JBoltUserKit.getUserId(), moMoroutingconfigOperation.getName());
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
	 * @param moMoroutingconfigOperation 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(MoMoroutingconfigOperation moMoroutingconfigOperation, Kv kv) {
		//addDeleteSystemLog(moMoroutingconfigOperation.getIAutoId(), JBoltUserKit.getUserId(),moMoroutingconfigOperation.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param moMoroutingconfigOperation 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(MoMoroutingconfigOperation moMoroutingconfigOperation, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(moMoroutingconfigOperation, kv);
	}

	/**
	 * 设置返回二开业务所属的关键systemLog的targetType
	 * @return
	 */
	@Override
	protected int systemLogTargetType() {
		return ProjectSystemLogTargetType.NONE.getValue();
	}

    public List<Record> dataList(Kv kv){
		return dbTemplate("moroutingconfigoperation.findOperationList",kv).find();
	}

}
