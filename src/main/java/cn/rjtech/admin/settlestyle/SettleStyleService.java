package cn.rjtech.admin.settlestyle;

import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;

import java.util.Date;

import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.cache.JBoltDictionaryCache;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.User;
import cn.rjtech.config.DictionaryTypeKey;
import cn.rjtech.model.momdata.SettleStyle;
/**
 * 结算方式 Service
 * @ClassName: SettleStyleService
 * @author: WYX
 * @date: 2023-03-24 09:08
 */
public class SettleStyleService extends BaseService<SettleStyle> {

	private final SettleStyle dao = new SettleStyle().dao();

	@Override
	protected SettleStyle dao() {
		return dao;
	}

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @return
	 */
	public Page<Record> paginateAdminDatas(int pageNumber, int pageSize, Kv para) {
		Page<Record> pageList = dbTemplate("settlestyle.paginateAdminDatas",para).paginate(pageNumber, pageSize);
		for (Record row : pageList.getList()) {
			row.set("issbilltypedesc", JBoltDictionaryCache.me.getNameBySn(DictionaryTypeKey.ISSBILLTYPE, row.getStr("issbilltype")));
		}
		return pageList;
	}

	/**
	 * 保存
	 * @param settleStyle
	 * @return
	 */
	public Ret save(SettleStyle settleStyle) {
		if(settleStyle==null || isOk(settleStyle.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		User loginUser = JBoltUserKit.getUser();
		Date now = new Date();
		settleStyle.setICreateBy(loginUser.getId())
			.setCCreateName(loginUser.getName())
			.setDCreateTime(now)
			.setIUpdateBy(loginUser.getId())
			.setCUpdateName(loginUser.getName())
			.setDUpdateTime(now)
			.setIsDeleted(false);
		boolean success=settleStyle.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(settleStyle.getIautoid(), JBoltUserKit.getUserId(), settleStyle.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param settleStyle
	 * @return
	 */
	public Ret update(SettleStyle settleStyle) {
		if(settleStyle==null || notOk(settleStyle.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		SettleStyle dbSettleStyle=findById(settleStyle.getIAutoId());
		if(dbSettleStyle==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		User loginUser = JBoltUserKit.getUser();
		Date now = new Date();
		settleStyle.setIUpdateBy(loginUser.getId())
			.setCUpdateName(loginUser.getName())
			.setDUpdateTime(now);
		boolean success=settleStyle.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(settleStyle.getIautoid(), JBoltUserKit.getUserId(), settleStyle.getName());
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
		return updateColumn(id, "isdeleted", true);
	}

	/**
	 * 删除数据后执行的回调
	 * @param settleStyle 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(SettleStyle settleStyle, Kv kv) {
		//addDeleteSystemLog(settleStyle.getIautoid(), JBoltUserKit.getUserId(),settleStyle.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param settleStyle 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(SettleStyle settleStyle, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(settleStyle, kv);
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
	 * 切换bssflag属性
	 */
	public Ret toggleBSSFlag(Long id) {
		return toggleBoolean(id, "bSSFlag");
	}

	/**
	 * 切换isdeleted属性
	 */
	public Ret toggleIsDeleted(Long id) {
		return toggleBoolean(id, "IsDeleted");
	}

	/**
	 * 检测是否可以toggle操作指定列
	 * @param settleStyle 要toggle的model
	 * @param column 操作的哪一列
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanToggle(SettleStyle settleStyle,String column, Kv kv) {
		//没有问题就返回null  有问题就返回提示string 字符串
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(SettleStyle settleStyle, String column, Kv kv) {
		//addUpdateSystemLog(settleStyle.getIautoid(), JBoltUserKit.getUserId(), settleStyle.getName(),"的字段["+column+"]值:"+settleStyle.get(column));
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param settleStyle model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(SettleStyle settleStyle, Kv kv) {
		//这里用来覆盖 检测SettleStyle是否被其它表引用
		return null;
	}

}