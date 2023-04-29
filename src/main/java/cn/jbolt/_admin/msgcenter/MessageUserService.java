package cn.jbolt._admin.msgcenter;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Db;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.main.MessageUser;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;

/**
 * 消息模板 Service
 * @ClassName: MessageUserService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-03 14:52
 */
public class MessageUserService extends BaseService<MessageUser> {

	private final MessageUser dao = new MessageUser().dao();

	@Override
	protected MessageUser dao() {
		return dao;
	}

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @return
	 */
	public Page<MessageUser> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
		return paginateByKeywords("id","DESC", pageNumber, pageSize, keywords, "del_flag");
	}
	/**
	 *	保存可编辑表格的新增行
	 * @param jBoltTable
	 * @param iPersonId
	 */
	public void addSubmitTableDatas(JBoltTable jBoltTable, Long iPersonId) {
		List<Record> list = jBoltTable.getSaveRecordList();
		if(CollUtil.isEmpty(list)) return;
		for (Record row : list) {
			MessageUser messageUser = new MessageUser();
			Long userId = row.getLong("userid");
			messageUser.setMessageId(iPersonId);
			messageUser.setUserId(userId);
			messageUser.setDelFag(false);
			ValidationUtils.isTrue(messageUser.save(), JBoltMsg.FAIL);
		}
	}
	/**
	 *	保存可编辑表格的修改行
	 * @param jBoltTable
	 */
	public void updateSubmitTableDatas(JBoltTable jBoltTable) {
		List<Record> list = jBoltTable.getUpdateRecordList();
		if(CollUtil.isEmpty(list)) return;
		for (Record row : list) {
			MessageUser messageUser = findById(row.getLong("id"));
			Long userId = row.getLong("userId");
			messageUser.setUserId(userId);
			ValidationUtils.isTrue(messageUser.update(), JBoltMsg.FAIL);
		}
	}
	/**
	 *	保存可编辑表格的删除行
	 * @param jBoltTable
	 */
	public void deleteSubmitTableDatas(JBoltTable jBoltTable) {
		Object[] ids = jBoltTable.getDelete();
		if(ArrayUtil.isEmpty(ids)) return;
		for (Object id : ids) {
			MessageUser messageUser = findById(id);
			messageUser.setDelFag(true);
			ValidationUtils.isTrue(messageUser.update(), JBoltMsg.FAIL);
		}
	}
	/**
	 * 保存
	 * @param messageUser
	 * @return
	 */
	public Ret save(MessageUser messageUser) {
		if(messageUser==null || isOk(messageUser.getId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(messageUser.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=messageUser.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(messageUser.getId(), JBoltUserKit.getUserId(), messageUser.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param messageUser
	 * @return
	 */
	public Ret update(MessageUser messageUser) {
		if(messageUser==null || notOk(messageUser.getId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		MessageUser dbMessageUser=findById(messageUser.getId());
		if(dbMessageUser==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(messageUser.getName(), messageUser.getId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=messageUser.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(messageUser.getId(), JBoltUserKit.getUserId(), messageUser.getName());
		}
		return ret(success);
	}

	/**
	 * 检测是否可以删除
	 * @param messageUser 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(MessageUser messageUser, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(messageUser, kv);
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
	 * 切换delfag属性
	 */
	public Ret toggleDelFag(Long id) {
		return toggleBoolean(id, "del_fag");
	}

	/**
	 * 检测是否可以toggle操作指定列
	 * @param messageUser 要toggle的model
	 * @param column 操作的哪一列
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanToggle(MessageUser messageUser,String column, Kv kv) {
		//没有问题就返回null  有问题就返回提示string 字符串
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(MessageUser messageUser, String column, Kv kv) {
		//addUpdateSystemLog(messageUser.getId(), JBoltUserKit.getUserId(), messageUser.getName(),"的字段["+column+"]值:"+messageUser.get(column));
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param messageUser model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(MessageUser messageUser, Kv kv) {
		//这里用来覆盖 检测MessageUser是否被其它表引用
		return null;
	}

}
