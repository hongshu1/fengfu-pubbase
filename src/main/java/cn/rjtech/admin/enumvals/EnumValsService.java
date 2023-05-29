package cn.rjtech.admin.enumvals;

import cn.hutool.core.collection.CollUtil;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.rjtech.constants.ErrorMsg;
import cn.rjtech.model.momdata.EnumType;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.rjtech.model.momdata.EnumVals;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;

/**
 * 系统管理-枚举值
 * @ClassName: EnumValsService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-22 09:35
 */
public class EnumValsService extends BaseService<EnumVals> {
	private final EnumVals dao=new EnumVals().dao();
	@Inject
	private EnumValsService service;

	@Override
	protected EnumVals dao() {
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
     * @param iEnumTypeId 枚举类型ID
	 * @return
	 */
	public Page<Record> getAdminDatas(int pageNumber, int pageSize, Long iEnumTypeId) {

		//创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
	    //sql条件处理
        sql.eq("iEnumTypeId",iEnumTypeId);
        //排序
        sql.desc("iAutoId");
		Page<Record> page = paginateRecord(sql);
		if (CollUtil.isNotEmpty(page.getList())) {
			page.getList().forEach(row -> {
				//查询关联字段
				String strEnumName = service.find("SELECT cEnumTypeName FROM Bd_EnumType WHERE cEnumTypeCode = ?",row.getStr("iEnumTypeId")).get(0).toString();
				//剔除多余字符
				strEnumName=strEnumName.replace("{cenumtypename:","").replace("}","");
				row.set("cenumtypename",strEnumName);
			});
		}
		return page;


//		return paginate(sql);
	}

	/**
	 * 保存
	 * @param enumVals
	 * @return
	 */
	public Ret save(EnumVals enumVals) {
		if(enumVals==null || isOk(enumVals.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(enumVals.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=enumVals.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(enumVals.getIAutoId(), JBoltUserKit.getUserId(), enumVals.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param enumVals
	 * @return
	 */
	public Ret update(EnumVals enumVals) {
		if(enumVals==null || notOk(enumVals.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		EnumVals dbEnumVals=findById(enumVals.getIAutoId());
		if(dbEnumVals==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(enumVals.getName(), enumVals.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}

		//校验编码是否存在
		List<EnumVals> cenumtyoeid = service.find("SELECT * FROM Bd_EnumType WHERE cEnumTypeCode = ?",enumVals.getIEnumTypeId());
		if(cenumtyoeid.size()==0){
			return fail(JBoltMsg.PARAM_ERROR);
		}




		boolean success=enumVals.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(enumVals.getIAutoId(), JBoltUserKit.getUserId(), enumVals.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param enumVals 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(EnumVals enumVals, Kv kv) {
		//addDeleteSystemLog(enumVals.getIAutoId(), JBoltUserKit.getUserId(),enumVals.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param enumVals model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(EnumVals enumVals, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

	/**
	 * 执行JBoltTable表格整体提交
	 *
	 * @param jBoltTable
	 * @return
	 */
	public Ret submitByJBoltTable(JBoltTable jBoltTable) {
		if (jBoltTable.getSaveRecordList() == null && jBoltTable.getDelete() == null && jBoltTable.getUpdateRecordList() == null) {
			return Ret.msg("行数据不能为空");
		}
		tx(() -> {
			//新增
			List<EnumVals> saveRecords = jBoltTable.getSaveModelList(EnumVals.class);
			if (CollUtil.isNotEmpty(saveRecords)) {
				for (int i = 0; i < saveRecords.size(); i++) {
					Long autoid = JBoltSnowflakeKit.me.nextId();
					saveRecords.get(i).setIAutoId(autoid);
				}
				batchSave(saveRecords, 500);
			}

			//修改
			List<EnumVals> updateRecords = jBoltTable.getUpdateModelList(EnumVals.class);
			if (CollUtil.isNotEmpty(updateRecords)) {
				batchUpdate(updateRecords, 500);
			}

			// 删除
//			Object[] deletes = jBoltTable.getDelete();
//			if (ArrayUtil.isNotEmpty(deletes)) {
//				deleteMultiByIds(deletes);
//			}
			return true;
		});
		return SUCCESS;
    }
}