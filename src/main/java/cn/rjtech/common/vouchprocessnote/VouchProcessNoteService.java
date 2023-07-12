package cn.rjtech.common.vouchprocessnote;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.common.model.VouchProcessNote;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.sql.Connection;
import java.util.List;

/**
 *  Service
 * @ClassName: VouchProcessNoteService
 * @author: lidehui
 * @date: 2023-01-30 17:15
 */
public class VouchProcessNoteService extends BaseService<VouchProcessNote> {

	private final VouchProcessNote dao = new VouchProcessNote().dao();

	@Override
	protected VouchProcessNote dao() {
		return dao;
	}

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @return
	 */
	public Page<VouchProcessNote> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
		return paginateByKeywords("AutoId","DESC", pageNumber, pageSize, keywords, "AutoId");
	}

	/**
	 * 保存
	 * @param vouchProcessNote
	 * @return
	 */
	public Ret save(VouchProcessNote vouchProcessNote) {
		if(vouchProcessNote==null || isOk(vouchProcessNote.getAutoid())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(vouchProcessNote.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=vouchProcessNote.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(vouchProcessNote.getAutoid(), JBoltUserKit.getUserId(), vouchProcessNote.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param vouchProcessNote
	 * @return
	 */
	public Ret update(VouchProcessNote vouchProcessNote) {
		if(vouchProcessNote==null || notOk(vouchProcessNote.getAutoid())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		VouchProcessNote dbVouchProcessNote=findById(vouchProcessNote.getAutoid());
		if(dbVouchProcessNote==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(vouchProcessNote.getName(), vouchProcessNote.getAutoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=vouchProcessNote.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(vouchProcessNote.getAutoid(), JBoltUserKit.getUserId(), vouchProcessNote.getName());
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
	 * @param vouchProcessNote 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(VouchProcessNote vouchProcessNote, Kv kv) {
		//addDeleteSystemLog(vouchProcessNote.getAutoid(), JBoltUserKit.getUserId(),vouchProcessNote.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param vouchProcessNote 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(VouchProcessNote vouchProcessNote, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(vouchProcessNote, kv);
	}

	/**
	 * 设置返回二开业务所属的关键systemLog的targetType 
	 * @return
	 */
	@Override
	protected int systemLogTargetType() {
		return ProjectSystemLogTargetType.NONE.getValue();
	}

	public List<Record> processNote(String seq,String vouchBusinessID){
		Kv kv = Kv.by("seq", seq);
		kv.set("vouchBusinessID", vouchBusinessID);
		List<Record> records = dbTemplate("vouchprocessnote.processNote", kv).find();
		return records;
	}

	public void saveVouchprocessnoteInTx(VouchProcessNote vouchprocessnote) {
		tx(Connection.TRANSACTION_READ_UNCOMMITTED, () -> {
			vouchprocessnote.save();

			return true;
		});
	}
}