package cn.rjtech.admin.spotcheckform;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.SpotCheckForm;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;
/**
 * 点检表格 Service
 * @ClassName: SpotCheckFormService
 * @author: RJ
 * @date: 2023-04-03 10:42
 */
public class SpotCheckFormService extends BaseService<SpotCheckForm> {

	private final SpotCheckForm dao = new SpotCheckForm().dao();

	@Override
	protected SpotCheckForm dao() {
		return dao;
	}

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @return
	 */
	public Page<SpotCheckForm> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
		return paginateByKeywords("iAutoId","DESC", pageNumber, pageSize, keywords, "iAutoId");
	}

	/**
	 * 保存
	 * @param spotCheckForm
	 * @return
	 */
	public Ret save(SpotCheckForm spotCheckForm) {
		if(spotCheckForm==null || isOk(spotCheckForm.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(spotCheckForm.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=spotCheckForm.save();
		if(!success) {
			return fail(JBoltMsg.FAIL);
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param spotCheckForm
	 * @return
	 */
	public Ret update(SpotCheckForm spotCheckForm) {
		if(spotCheckForm==null || notOk(spotCheckForm.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		SpotCheckForm dbSpotCheckForm=findById(spotCheckForm.getIAutoId());
		if(dbSpotCheckForm==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		boolean success=spotCheckForm.update();
		if(!success) {
			return fail(JBoltMsg.FAIL);
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
	 * @param spotCheckForm 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(SpotCheckForm spotCheckForm, Kv kv) {
		//addDeleteSystemLog(spotCheckForm.getIautoid(), JBoltUserKit.getUserId(),spotCheckForm.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param spotCheckForm 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(SpotCheckForm spotCheckForm, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(spotCheckForm, kv);
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
	 * 切换isenabled属性
	 */
	public Ret toggleIsEnabled(Long id) {
		return toggleBoolean(id, "isEnabled");
	}

	/**
	 * 检测是否可以toggle操作指定列
	 * @param spotCheckForm 要toggle的model
	 * @param column 操作的哪一列
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanToggle(SpotCheckForm spotCheckForm,String column, Kv kv) {
		//没有问题就返回null  有问题就返回提示string 字符串
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(SpotCheckForm spotCheckForm, String column, Kv kv) {
		//addUpdateSystemLog(spotCheckForm.getIautoid(), JBoltUserKit.getUserId(), spotCheckForm.getName(),"的字段["+column+"]值:"+spotCheckForm.get(column));
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param spotCheckForm model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(SpotCheckForm spotCheckForm, Kv kv) {
		//这里用来覆盖 检测SpotCheckForm是否被其它表引用
		return null;
	}

	public List<Record> options() {
		return dbTemplate("spotcheckform.list", Kv.of("isenabled", "true")).find();
	}
}