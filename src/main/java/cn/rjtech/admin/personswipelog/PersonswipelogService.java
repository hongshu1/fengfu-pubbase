package cn.rjtech.admin.personswipelog;

import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Db;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.Personswipelog;
/**
 * 质量管理-在库检单行配置 Service
 * @ClassName: PersonswipelogService
 * @author: RJ
 * @date: 2023-05-05 20:31
 */
public class PersonswipelogService extends BaseService<Personswipelog> {

	private final Personswipelog dao = new Personswipelog().dao();

	@Override
	protected Personswipelog dao() {
		return dao;
	}

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @return
	 */
	public Page<Personswipelog> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
		return paginateByKeywords("iAutoId","DESC", pageNumber, pageSize, keywords, "iAutoId");
	}

	/**
	 * 保存
	 * @param personswipelog
	 * @return
	 */
	public Ret save(Personswipelog personswipelog) {
		if(personswipelog==null || isOk(personswipelog.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(personswipelog.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=personswipelog.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(personswipelog.getIautoid(), JBoltUserKit.getUserId(), personswipelog.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param personswipelog
	 * @return
	 */
	public Ret update(Personswipelog personswipelog) {
		if(personswipelog==null || notOk(personswipelog.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		Personswipelog dbPersonswipelog=findById(personswipelog.getIAutoId());
		if(dbPersonswipelog==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(personswipelog.getName(), personswipelog.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=personswipelog.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(personswipelog.getIautoid(), JBoltUserKit.getUserId(), personswipelog.getName());
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
	 * @param personswipelog 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(Personswipelog personswipelog, Kv kv) {
		//addDeleteSystemLog(personswipelog.getIautoid(), JBoltUserKit.getUserId(),personswipelog.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param personswipelog 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(Personswipelog personswipelog, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(personswipelog, kv);
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
		return toggleBoolean(id, "isDeleted");
	}

	/**
	 * 检测是否可以toggle操作指定列
	 * @param personswipelog 要toggle的model
	 * @param column 操作的哪一列
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanToggle(Personswipelog personswipelog,String column, Kv kv) {
		//没有问题就返回null  有问题就返回提示string 字符串
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(Personswipelog personswipelog, String column, Kv kv) {
		//addUpdateSystemLog(personswipelog.getIautoid(), JBoltUserKit.getUserId(), personswipelog.getName(),"的字段["+column+"]值:"+personswipelog.get(column));
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param personswipelog model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(Personswipelog personswipelog, Kv kv) {
		//这里用来覆盖 检测Personswipelog是否被其它表引用
		return null;
	}

}