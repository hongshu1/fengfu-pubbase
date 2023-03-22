package cn.rjtech.admin.person;

import cn.jbolt.core.cache.JBoltDictionaryCache;
import cn.jbolt.core.model.Dictionary;
import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.Person;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * 人员档案 Service
 * @ClassName: PersonService
 * @author: WYX
 * @date: 2023-03-21 15:11
 */
public class PersonService extends BaseService<Person> {

	private final Person dao = new Person().dao();

	@Override
	protected Person dao() {
		return dao;
	}

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @return
	 */
	public Page<Person> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
		return paginateByKeywords("iAutoId","DESC", pageNumber, pageSize, keywords, "iAutoId");
	}

	/**
	 * 保存
	 * @param person
	 * @return
	 */
	public Ret save(Person person) {
		if(person==null || isOk(person.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(person.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=person.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(person.getIautoid(), JBoltUserKit.getUserId(), person.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param person
	 * @return
	 */
	public Ret update(Person person) {
		if(person==null || notOk(person.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		Person dbPerson=findById(person.getIAutoId());
		if(dbPerson==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(person.getName(), person.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=person.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(person.getIautoid(), JBoltUserKit.getUserId(), person.getName());
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
	 * @param person 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(Person person, Kv kv) {
		//addDeleteSystemLog(person.getIautoid(), JBoltUserKit.getUserId(),person.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param person 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(Person person, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(person, kv);
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
	 * 切换bpsnperson属性
	 */
	public Ret toggleBPsnPerson(Long id) {
		return toggleBoolean(id, "bPsnPerson");
	}

	/**
	 * 切换bprobation属性
	 */
	public Ret toggleBProbation(Long id) {
		return toggleBoolean(id, "bProbation");
	}

	/**
	 * 切换bdutylock属性
	 */
	public Ret toggleBDutyLock(Long id) {
		return toggleBoolean(id, "bDutyLock");
	}

	/**
	 * 切换bpsnshop属性
	 */
	public Ret toggleBpsnshop(Long id) {
		return toggleBoolean(id, "bpsnshop");
	}

	/**
	 * 切换isenabled属性
	 */
	public Ret toggleIsEnabled(Long id) {
		return toggleBoolean(id, "isEnabled");
	}

	/**
	 * 切换isdeleted属性
	 */
	public Ret toggleIsDeleted(Long id) {
		return toggleBoolean(id, "isDeleted");
	}

	/**
	 * 检测是否可以toggle操作指定列
	 * @param person 要toggle的model
	 * @param column 操作的哪一列
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanToggle(Person person,String column, Kv kv) {
		//没有问题就返回null  有问题就返回提示string 字符串
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(Person person, String column, Kv kv) {
		//addUpdateSystemLog(person.getIautoid(), JBoltUserKit.getUserId(), person.getName(),"的字段["+column+"]值:"+person.get(column));
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param person model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(Person person, Kv kv) {
		//这里用来覆盖 检测Person是否被其它表引用
		return null;
	}

    public Object formatPattenSn(String value, String key) {
		if (notNull(value)) {
			if (notNull(key)) {
				List<Dictionary> list = JBoltDictionaryCache.me.getListByTypeKey(key, true);
				Dictionary find = list.stream().filter(dictionary -> StringUtils.equalsIgnoreCase(dictionary.getSn(), String.valueOf(value))).findFirst().orElse(null);
				return find == null ? null : find.getName();
			}
		}
		return null;
    }
}