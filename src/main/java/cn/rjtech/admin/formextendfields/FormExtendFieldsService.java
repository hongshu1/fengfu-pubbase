package cn.rjtech.admin.formextendfields;

import cn.hutool.core.collection.CollUtil;
import cn.jbolt._admin.dictionary.DictionaryTypeKey;
import cn.jbolt._admin.permission.PermissionService;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.cache.JBoltDictionaryCache;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.FormExtendFields;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;


/**
 * 系统管理-拓展字段配置表
 * @ClassName: FormExtendFieldsService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-06-01 11:37
 */
public class FormExtendFieldsService extends BaseService<FormExtendFields> {
	private final FormExtendFields dao=new FormExtendFields().dao();

	@Inject
	private PermissionService permissionService;

	@Override
	protected FormExtendFields dao() {
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
	 * @param keywords   关键词
     * @param iFormId 系统表单ID
     * @param iFormFieldId 系统字段ID
     * @param cFieldCode 系统字段编码
     * @param cFieldName 系统字段名称
	 * @return
	 */
	public Page<Record> getAdminDatas(int pageNumber, int pageSize, String keywords, Long iFormId, Long iFormFieldId, String cFieldCode, String cFieldName,String ifform) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
	    //筛选
		if(ifform.equals("1")){
			sql.isNotNull("iFormId");
		}else {
			sql.isNotNull("iFormFieldId");
		}
	    //sql条件处理
        sql.eq("iFormId",iFormId);
        sql.eq("iFormFieldId",iFormFieldId);
        sql.eq("cFieldCode",cFieldCode);
        sql.eq("cFieldName",cFieldName);
        //关键词模糊查询
        sql.like("cFieldName",keywords);
        //排序
        sql.asc("iAutoId");
		Page<Record> page = paginateRecord(sql);
		if (CollUtil.isNotEmpty(page.getList())) {
			page.getList().forEach(row -> {
				row.set("iformidname", permissionService.getOneColumnValueById(row.getStr("iFormId"),"title"));
				row.set("iformfieldidname", permissionService.getOneColumnValueById(row.getStr("iFormFieldId"),"title"));
				row.set("ifieldtypename", JBoltDictionaryCache.me.getNameBySn(DictionaryTypeKey.extend_field_type.name(), row.getStr("iFieldType")));
				row.set("isenabledname", JBoltDictionaryCache.me.getNameBySn(DictionaryTypeKey.whether_enable.name(), row.getStr("isEnabled")));
				row.set("isrequiredname", JBoltDictionaryCache.me.getNameBySn(DictionaryTypeKey.is_required.name(), row.getStr("isRequired")));
				row.set("isourcename", JBoltDictionaryCache.me.getNameBySn(DictionaryTypeKey.value_source.name(), row.getStr("iSource")));
				row.set("iseditablename", JBoltDictionaryCache.me.getNameBySn(DictionaryTypeKey.is_edit.name(), row.getStr("isEditable")));
				row.set("isdisplayedname", JBoltDictionaryCache.me.getNameBySn(DictionaryTypeKey.is_visible.name(), row.getStr("isDisplayed")));
				row.set("ipositionname", JBoltDictionaryCache.me.getNameBySn(DictionaryTypeKey.visible_position.name(), row.getStr("iPosition")));
			});
		}
		return page;
//		return paginate(sql);
	}

	/**
	 * 保存
	 * @param formExtendFields
	 * @return
	 */
	public Ret save(FormExtendFields formExtendFields) {
		if(formExtendFields==null || isOk(formExtendFields.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		formExtendFields.setIsDeleted(false);
		//未选择小数、枚举时清空小数位、枚举值
		if (formExtendFields.getIFieldType() != 3){
			formExtendFields.setIDigit(null);
		}
		if (formExtendFields.getISource() != 2){
			formExtendFields.setISourceVal(null);
		}
		boolean success=formExtendFields.save();

		if(success) {
			//添加日志
			//addSaveSystemLog(formExtendFields.getIAutoId(), JBoltUserKit.getUserId(), formExtendFields.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param formExtendFields
	 * @return
	 */
	public Ret update(FormExtendFields formExtendFields) {
		if(formExtendFields==null || notOk(formExtendFields.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		FormExtendFields dbFormExtendFields=findById(formExtendFields.getIAutoId());
		if(dbFormExtendFields==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//未选择小数、枚举时清空小数位、枚举值
		if (formExtendFields.getIFieldType() != 3){
			formExtendFields.setIDigit(null);
		}
		if (formExtendFields.getISource() != 2){
			formExtendFields.setISourceVal(null);
		}

		boolean success=formExtendFields.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(formExtendFields.getIAutoId(), JBoltUserKit.getUserId(), formExtendFields.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param formExtendFields 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(FormExtendFields formExtendFields, Kv kv) {
		//addDeleteSystemLog(formExtendFields.getIAutoId(), JBoltUserKit.getUserId(),formExtendFields.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param formExtendFields model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(FormExtendFields formExtendFields, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}
	}