package cn.rjtech.admin.fitemss97class;

import java.util.Date;

import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import cn.jbolt._admin.user.UserService;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.cache.JBoltUserCache;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.User;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.enums.SourceEnum;
import cn.rjtech.model.momdata.Fitemss97class;
/**
 * 项目管理项目分类目录 Service
 * @ClassName: Fitemss97classService
 * @author: WYX
 * @date: 2023-03-25 16:44
 */
public class Fitemss97classService extends BaseService<Fitemss97class> {

	private final Fitemss97class dao = new Fitemss97class().dao();

	@Inject
	private UserService userService;
	@Override
	protected Fitemss97class dao() {
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
		Page<Record> pageList = dbTemplate("fitemss97class.paginateAdminDatas",para).paginate(pageNumber, pageSize);
		for (Record row : pageList.getList()) {
			row.set("createusername", JBoltUserCache.me.getUserName(row.getLong("icreateby")));
		}
		return pageList;
	}

	/**
	 * 保存
	 * @param fitemss97class
	 * @return
	 */
	public Ret save(Fitemss97class fitemss97class) {
		if(fitemss97class==null || isOk(fitemss97class.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		fitemss97class.setIOrgId(getOrgId());
		fitemss97class.setCOrgCode(getOrgCode());
		fitemss97class.setCOrgName(getOrgName());
		fitemss97class.setISource(SourceEnum.MES.getValue());
		User loginUser = JBoltUserKit.getUser();
		Date now = new Date();
		fitemss97class.setICreateBy(loginUser.getId());
		fitemss97class.setCCreateName(loginUser.getUsername());
		fitemss97class.setDCreateTime(now);
		fitemss97class.setIUpdateBy(loginUser.getId());
		fitemss97class.setCUpdateName(loginUser.getUsername());
		fitemss97class.setDUpdateTime(now);
		fitemss97class.setIsDeleted(false);
		boolean success=fitemss97class.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(fitemss97class.getIautoid(), JBoltUserKit.getUserId(), fitemss97class.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param fitemss97class
	 * @return
	 */
	public Ret update(Fitemss97class fitemss97class) {
		if(fitemss97class==null || notOk(fitemss97class.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		Fitemss97class dbFitemss97class=findById(fitemss97class.getIAutoId());
		if(dbFitemss97class==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		User loginUser = JBoltUserKit.getUser();
		fitemss97class.setIUpdateBy(loginUser.getId());
		fitemss97class.setCUpdateName(loginUser.getUsername());
		fitemss97class.setDUpdateTime(new Date());
		boolean success=fitemss97class.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(fitemss97class.getIautoid(), JBoltUserKit.getUserId(), fitemss97class.getName());
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
	 * @param fitemss97class 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(Fitemss97class fitemss97class, Kv kv) {
		//addDeleteSystemLog(fitemss97class.getIautoid(), JBoltUserKit.getUserId(),fitemss97class.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param fitemss97class 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(Fitemss97class fitemss97class, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(fitemss97class, kv);
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
	 * 切换isdeleted属性
	 */
	public Ret toggleIsDeleted(Long id) {
		return toggleBoolean(id, "IsDeleted");
	}

	/**
	 * 检测是否可以toggle操作指定列
	 * @param fitemss97class 要toggle的model
	 * @param column 操作的哪一列
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanToggle(Fitemss97class fitemss97class,String column, Kv kv) {
		//没有问题就返回null  有问题就返回提示string 字符串
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(Fitemss97class fitemss97class, String column, Kv kv) {
		//addUpdateSystemLog(fitemss97class.getIautoid(), JBoltUserKit.getUserId(), fitemss97class.getName(),"的字段["+column+"]值:"+fitemss97class.get(column));
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param fitemss97class model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(Fitemss97class fitemss97class, Kv kv) {
		//这里用来覆盖 检测Fitemss97class是否被其它表引用
		return null;
	}

}