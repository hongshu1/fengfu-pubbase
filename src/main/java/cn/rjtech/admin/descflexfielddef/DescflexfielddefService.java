package cn.rjtech.admin.descflexfielddef;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.descflexsegvaluesetvalue.DescflexsegvaluesetvalueService;
import cn.rjtech.base.service.BaseService;
import cn.rjtech.model.momdata.Descflexfielddef;
import cn.rjtech.model.momdata.Descflexsegvaluesetvalue;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.Date;
import java.util.List;

/**
 * 实体扩展字段 Service
 * @ClassName: DescflexfielddefService   
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-11-04 11:10  
 */
public class DescflexfielddefService extends BaseService<Descflexfielddef> {
	private final Descflexfielddef dao=new Descflexfielddef().dao();
	@Override
	protected Descflexfielddef dao() {
		return dao;
	}
	@Inject
	private DescflexsegvaluesetvalueService descflexsegvaluesetvalueService;
		
	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param kv
	 * @return
	 */
	public Page<Record> paginateAdminDatas(int pageNumber, int pageSize, Kv kv) {
		return dbTemplate("descflexfielddef.getDescflexfielddefList", kv).paginate(pageNumber, pageSize);
		//return paginateByKeywords("iAutoId","DESC", pageNumber, pageSize, keywords, "iAutoId");
	}

	public List<Record> getDescflexfielddefList(Kv kv){
		return dbTemplate("descflexfielddef.getDescflexfielddefList", kv.set("isenabled", 1)).find();
	}

	public List<Record> getTableList(Kv kv){
		return dbTemplate("descflexfielddef.getTableList", kv).find();
	}
	/**
	 * 保存
	 * @param descflexfielddef
	 * @return
	 */
	public Ret save(Descflexfielddef descflexfielddef) {
		if(descflexfielddef==null || isOk(descflexfielddef.getIautoid())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		descflexfielddef.setIcreateby(JBoltUserKit.getUserId());
		descflexfielddef.setCcreatename(JBoltUserKit.getUserName());
		descflexfielddef.setDcreatetime(new Date());
		descflexfielddef.setCorgcode(getOrgCode());
		descflexfielddef.setCorgname(getOrgName());
		descflexfielddef.setIorgid(getOrgId());
		//if(existsName(descflexfielddef.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=descflexfielddef.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(descflexfielddef.getIautoid(), JBoltUserKit.getUserId(), descflexfielddef.getName());
		}
		return ret(success);
	}
	
	/**
	 * 更新
	 * @param descflexfielddef
	 * @return
	 */
	public Ret update(Descflexfielddef descflexfielddef) {
		if(descflexfielddef==null || notOk(descflexfielddef.getIautoid())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		Descflexfielddef dbDescflexfielddef=findById(descflexfielddef.getIautoid());
		if(dbDescflexfielddef==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(descflexfielddef.getName(), descflexfielddef.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=descflexfielddef.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(descflexfielddef.getIautoid(), JBoltUserKit.getUserId(), descflexfielddef.getName());
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
	 * @param descflexfielddef 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(Descflexfielddef descflexfielddef, Kv kv) {
		//addDeleteSystemLog(descflexfielddef.getIautoid(), JBoltUserKit.getUserId(),descflexfielddef.getName());
		return null;
	}
	
	/**
	 * 检测是否可以删除
	 * @param descflexfielddef 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(Descflexfielddef descflexfielddef, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(descflexfielddef, kv);
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
	 * 切换isenabled属性
	 */
	public Ret toggleIsenabled(Long id) {
		return toggleBoolean(id, "isEnabled");
	}
	
	/**
	 * 检测是否可以toggle操作指定列
	 * @param descflexfielddef 要toggle的model
	 * @param column 操作的哪一列
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanToggle(Descflexfielddef descflexfielddef, String column, Kv kv) {
		//没有问题就返回null  有问题就返回提示string 字符串
		return null;
	}
	
	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(Descflexfielddef descflexfielddef, String column, Kv kv) {
		//addUpdateSystemLog(descflexfielddef.getIautoid(), JBoltUserKit.getUserId(), descflexfielddef.getName(),"的字段["+column+"]值:"+descflexfielddef.get(column));
		return null;
	}
	
	/**
	 * 检测是否可以删除
	 * @param descflexfielddef model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(Descflexfielddef descflexfielddef, Kv kv) {
		//这里用来覆盖 检测Descflexfielddef是否被其它表引用
		return null;
	}

	/**
	 * 处理添加数据
	 */
	public void tableDataSave(Long imesdataid, Record record){
		List<Record> list = getDescflexfielddefList(Kv.by("leftcentityname", record.getStr("centityname")));
		for(Record descRecord: list){
			if(isOk(record.getStr("descflexsegvaluesetvalue"+descRecord.getStr("iautoid")))){
				//根据数据ID字段ID查询是否存在值
				Descflexsegvaluesetvalue descflexsegvaluesetvalue = descflexsegvaluesetvalueService.findByDescFlexFieldDefIdAndMesDataId(descRecord.getLong("iautoid"), imesdataid);
				if(descflexsegvaluesetvalue!=null){
					descflexsegvaluesetvalue.setCvalue(record.getStr("descflexsegvaluesetvalue"+descRecord.getStr("iautoid")));
					descflexsegvaluesetvalue.update();
				}else{
					descflexsegvaluesetvalue = new Descflexsegvaluesetvalue();
					descflexsegvaluesetvalue.setIdescflexfielddefid(descRecord.getLong("iautoid"));
					descflexsegvaluesetvalue.setImesdataid(imesdataid);
					descflexsegvaluesetvalue.setCvalue(record.getStr("descflexsegvaluesetvalue"+descRecord.getStr("iautoid")));
					descflexsegvaluesetvalue.save();
				}
			}
		}
	}

	public void tableDataSave(Long imesdataid, Kv kv){
		List<Record> list = getDescflexfielddefList(Kv.by("leftcentityname", kv.getStr("centityname")));
		for(Record descRecord: list){
			if(isOk(kv.getStr("descflexsegvaluesetvalue"+descRecord.getStr("iautoid")))){
				//根据数据ID字段ID查询是否存在值
				Descflexsegvaluesetvalue descflexsegvaluesetvalue = descflexsegvaluesetvalueService.findByDescFlexFieldDefIdAndMesDataId(descRecord.getLong("iautoid"), imesdataid);
				if(descflexsegvaluesetvalue!=null){
					descflexsegvaluesetvalue.setCvalue(kv.getStr("descflexsegvaluesetvalue"+descRecord.getStr("iautoid")));
					descflexsegvaluesetvalue.update();
				}else{
					descflexsegvaluesetvalue = new Descflexsegvaluesetvalue();
					descflexsegvaluesetvalue.setIdescflexfielddefid(descRecord.getLong("iautoid"));
					descflexsegvaluesetvalue.setImesdataid(imesdataid);
					descflexsegvaluesetvalue.setCvalue(kv.getStr("descflexsegvaluesetvalue"+descRecord.getStr("iautoid")));
					descflexsegvaluesetvalue.save();
				}
			}
		}
	}
	
}