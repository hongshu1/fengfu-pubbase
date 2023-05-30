package cn.rjtech.admin.enumtype;

import cn.jbolt.core.kit.JBoltUserKit;
import cn.rjtech.admin.enumvals.EnumValsService;
import cn.rjtech.model.momdata.EnumVals;
import com.jfinal.aop.Inject;
import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.rjtech.model.momdata.EnumType;
import com.jfinal.plugin.activerecord.Record;

import java.util.Date;
import java.util.List;

import static io.netty.util.internal.PlatformDependent.getLong;

/**
 * 系统管理-枚举类别
 * @ClassName: EnumTypeService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-22 09:08
 */
public class EnumTypeService extends BaseService<EnumType> {
	private final EnumType dao=new EnumType().dao();
	@Inject
	private EnumValsService enumValsService;
	@Inject
	private EnumTypeService service;

	@Override
	protected EnumType dao() {
		return dao;
	}

	@Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

	/**
	 * 后台管理数据查询
	 * @param pageNumber 第几页
	 * @param pageSize   每页几条数据
     * @param cEnumTypeName 枚举类别名称
	 * @return
	 */
	public Page<EnumType> getAdminDatas(int pageNumber, int pageSize, String cEnumTypeName) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
	    //sql条件处理
        sql.eq("cEnumTypeName",cEnumTypeName);
        //排序
        sql.desc("iAutoId");
		return paginate(sql);
	}

	/**
	 * 保存
	 * @param enumType
	 * @return
	 */
	public Ret save(EnumType enumType) {
		if(enumType==null || isOk(enumType.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(enumType.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		String userName = JBoltUserKit.getUserName();
		Long userId = JBoltUserKit.getUserId();
		Date now = new Date();

		enumType.setICreateBy(userId);
		enumType.setDCreateTime(now);
		enumType.setCCreateName(userName);
		enumType.setIUpdateBy(userId);
		enumType.setDUpdateTime(now);
		enumType.setCUpdateName(userName);
		enumType.setIsDeleted(false);
		// 组织信息
		enumType.setCOrgCode(getOrgCode());
		enumType.setCOrgName(getOrgName());
		enumType.setIOrgId(getOrgId());

		//校验编码是否重复
		List<EnumType> ifenumtypeid = service.find("SELECT * FROM Bd_EnumType WHERE cEnumTypeCode = ?",enumType.getCEnumTypeCode());
		if(ifenumtypeid.size()>0){
			return fail(JBoltMsg.DATA_SAME_NAME_EXIST);
		}
		boolean success = enumType.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(enumType.getIAutoId(), JBoltUserKit.getUserId(), enumType.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param enumType
	 * @return
	 */
	public Ret update(EnumType enumType) {
		if(enumType==null || notOk(enumType.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		EnumType dbEnumType=findById(enumType.getIAutoId());
		if(dbEnumType==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(enumType.getName(), enumType.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}


//		List<EnumVals> lenumVals = enumValsService.find("SELECT * FROM Bd_EnumVals WHERE iEnumTypeId = ?",befenumtypecode);
//		for (EnumVals enumVals : lenumVals) {
//			enumVals.setIEnumTypeId(Long.valueOf(enumType.getCEnumTypeCode()));
//		}



		boolean success=enumType.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(enumType.getIAutoId(), JBoltUserKit.getUserId(), enumType.getName());
		}


		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param enumType 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(EnumType enumType, Kv kv) {
		//addDeleteSystemLog(enumType.getIAutoId(), JBoltUserKit.getUserId(),enumType.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param enumType model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(EnumType enumType, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}




}