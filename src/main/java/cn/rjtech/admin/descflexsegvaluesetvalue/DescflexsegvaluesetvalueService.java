package cn.rjtech.admin.descflexsegvaluesetvalue;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.service.base.JBoltBaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.Descflexsegvaluesetvalue;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;

import java.util.List;

/**
 * 实体扩展字段值集 Service
 * @ClassName: DescflexsegvaluesetvalueService   
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-11-04 11:11  
 */
public class DescflexsegvaluesetvalueService extends JBoltBaseService<Descflexsegvaluesetvalue> {
	private final Descflexsegvaluesetvalue dao=new Descflexsegvaluesetvalue().dao();
	@Override
	protected Descflexsegvaluesetvalue dao() {
		return dao;
	}
		
	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @return
	 */
	public Page<Descflexsegvaluesetvalue> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
		return paginateByKeywords("iAutoId","DESC", pageNumber, pageSize, keywords, "iAutoId");
	}

	public Descflexsegvaluesetvalue findByDescFlexFieldDefIdAndMesDataId(Long iDescFlexFieldDefId, Long iMesDataId){
		return findFirst(Okv.by("iDescFlexFieldDefId", iDescFlexFieldDefId).set("iMesDataId", iMesDataId), "iautoid", "asc");
	}

	public List<Descflexsegvaluesetvalue> findByMesDataIdList(Long imesdataid){
		return find("SELECT * FROM Bd_DescFlexSegValueSetValue WHERE iMesDataId = ?", imesdataid);
	}
	
	/**
	 * 保存
	 * @param descflexsegvaluesetvalue
	 * @return
	 */
	public Ret save(Descflexsegvaluesetvalue descflexsegvaluesetvalue) {
		if(descflexsegvaluesetvalue==null || isOk(descflexsegvaluesetvalue.getIautoid())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(descflexsegvaluesetvalue.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=descflexsegvaluesetvalue.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(descflexsegvaluesetvalue.getIautoid(), JBoltUserKit.getUserId(), descflexsegvaluesetvalue.getName());
		}
		return ret(success);
	}
	
	/**
	 * 更新
	 * @param descflexsegvaluesetvalue
	 * @return
	 */
	public Ret update(Descflexsegvaluesetvalue descflexsegvaluesetvalue) {
		if(descflexsegvaluesetvalue==null || notOk(descflexsegvaluesetvalue.getIautoid())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		Descflexsegvaluesetvalue dbDescflexsegvaluesetvalue=findById(descflexsegvaluesetvalue.getIautoid());
		if(dbDescflexsegvaluesetvalue==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(descflexsegvaluesetvalue.getName(), descflexsegvaluesetvalue.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=descflexsegvaluesetvalue.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(descflexsegvaluesetvalue.getIautoid(), JBoltUserKit.getUserId(), descflexsegvaluesetvalue.getName());
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
	 * 删除数据后执行的回调
	 * @param descflexsegvaluesetvalue 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(Descflexsegvaluesetvalue descflexsegvaluesetvalue, Kv kv) {
		//addDeleteSystemLog(descflexsegvaluesetvalue.getIautoid(), JBoltUserKit.getUserId(),descflexsegvaluesetvalue.getName());
		return null;
	}
	
	/**
	 * 检测是否可以删除
	 * @param descflexsegvaluesetvalue 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(Descflexsegvaluesetvalue descflexsegvaluesetvalue, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(descflexsegvaluesetvalue, kv);
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