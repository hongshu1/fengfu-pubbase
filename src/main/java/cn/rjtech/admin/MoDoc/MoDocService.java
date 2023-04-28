package cn.rjtech.admin.MoDoc;

import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Db;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.MoDoc;
import com.jfinal.plugin.activerecord.Record;

import java.time.LocalDate;

/**
 * 在库检 Service
 * @ClassName: MoDocService
 * @author: RJ
 * @date: 2023-04-26 16:15
 */
public class MoDocService extends BaseService<MoDoc> {

	private final MoDoc dao = new MoDoc().dao();

	@Override
	protected MoDoc dao() {
		return dao;
	}

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @return
	 */
	public Page<Record> paginateAdminDatas(int pageNumber, int pageSize, Kv keywords) {
		return dbTemplate("MoDoc.getPage",keywords).paginate(pageNumber,pageSize);
	}

	/**
	 * 保存
	 * @param moDoc
	 * @return
	 */
	public Ret save(MoDoc moDoc) {
		if(moDoc==null || isOk(moDoc.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		moDoc.setIStatus(1);
		moDoc.setIType(2L);
		LocalDate date = LocalDate.parse(moDoc.getDPlanDate().toString());
		moDoc.setIYear(date.getYear());
		moDoc.setIMonth(date.getMonthValue());
		moDoc.setIDate(date.getDayOfMonth());
		boolean success=moDoc.save();

		return ret(success);
	}

	/**
	 * 更新
	 * @param moDoc
	 * @return
	 */
	public Ret update(MoDoc moDoc) {
		if(moDoc==null || notOk(moDoc.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		MoDoc dbMoDoc=findById(moDoc.getIAutoId());
		if(dbMoDoc==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		boolean success=moDoc.update();

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
	 * @param moDoc 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(MoDoc moDoc, Kv kv) {
		//addDeleteSystemLog(moDoc.getIautoid(), JBoltUserKit.getUserId(),moDoc.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param moDoc 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(MoDoc moDoc, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(moDoc, kv);
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
