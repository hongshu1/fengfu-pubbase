package cn.jbolt.common.model.base;
import cn.jbolt.core.model.base.JBoltBaseModel;
import cn.jbolt.core.gen.JBoltField;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * CodeGen模型详细设计
 * Generated by JBolt, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseCodeGenModelAttr<M extends BaseCodeGenModelAttr<M>> extends JBoltBaseModel<M>{

	/**
	 * 主键ID
	 */
	public M setId(java.lang.Long id) {
		set("id", id);
		return (M)this;
	}
	
	/**
	 * 主键ID
	 */
	@JBoltField(name="id" ,columnName="id",type="Long", remark="主键ID", required=true, maxLength=19, fixed=0, order=1)
	@JSONField(serializeUsing= ToStringSerializer.class)
	public java.lang.Long getId() {
		return getLong("id");
	}

	/**
	 * 所属codeGen
	 */
	public M setCodeGenId(java.lang.Long codeGenId) {
		set("code_gen_id", codeGenId);
		return (M)this;
	}
	
	/**
	 * 所属codeGen
	 */
	@JBoltField(name="codeGenId" ,columnName="code_gen_id",type="Long", remark="所属codeGen", required=true, maxLength=19, fixed=0, order=2)
	@JSONField(serializeUsing= ToStringSerializer.class)
	public java.lang.Long getCodeGenId() {
		return getLong("code_gen_id");
	}

	/**
	 * 列名
	 */
	public M setColName(java.lang.String colName) {
		set("col_name", colName);
		return (M)this;
	}
	
	/**
	 * 列名
	 */
	@JBoltField(name="colName" ,columnName="col_name",type="String", remark="列名", required=true, maxLength=40, fixed=0, order=3)
	public java.lang.String getColName() {
		return getStr("col_name");
	}

	/**
	 * 属性名
	 */
	public M setAttrName(java.lang.String attrName) {
		set("attr_name", attrName);
		return (M)this;
	}
	
	/**
	 * 属性名
	 */
	@JBoltField(name="attrName" ,columnName="attr_name",type="String", remark="属性名", required=true, maxLength=40, fixed=0, order=4)
	public java.lang.String getAttrName() {
		return getStr("attr_name");
	}

	/**
	 * 属性类型
	 */
	public M setJavaType(java.lang.String javaType) {
		set("java_type", javaType);
		return (M)this;
	}
	
	/**
	 * 属性类型
	 */
	@JBoltField(name="javaType" ,columnName="java_type",type="String", remark="属性类型", required=true, maxLength=40, fixed=0, order=5)
	public java.lang.String getJavaType() {
		return getStr("java_type");
	}

	/**
	 * 属性长度
	 */
	public M setAttrLength(java.lang.Integer attrLength) {
		set("attr_length", attrLength);
		return (M)this;
	}
	
	/**
	 * 属性长度
	 */
	@JBoltField(name="attrLength" ,columnName="attr_length",type="Integer", remark="属性长度", required=true, maxLength=10, fixed=0, order=6)
	public java.lang.Integer getAttrLength() {
		return getInt("attr_length");
	}

	/**
	 * 属性小数点
	 */
	public M setAttrFixed(java.lang.Integer attrFixed) {
		set("attr_fixed", attrFixed);
		return (M)this;
	}
	
	/**
	 * 属性小数点
	 */
	@JBoltField(name="attrFixed" ,columnName="attr_fixed",type="Integer", remark="属性小数点", required=false, maxLength=10, fixed=0, order=7)
	public java.lang.Integer getAttrFixed() {
		return getInt("attr_fixed");
	}

	/**
	 * 默认值
	 */
	public M setAttrDefaultValue(java.lang.String attrDefaultValue) {
		set("attr_default_value", attrDefaultValue);
		return (M)this;
	}
	
	/**
	 * 默认值
	 */
	@JBoltField(name="attrDefaultValue" ,columnName="attr_default_value",type="String", remark="默认值", required=false, maxLength=40, fixed=0, order=8)
	public java.lang.String getAttrDefaultValue() {
		return getStr("attr_default_value");
	}

	/**
	 * 数据表内默认顺序
	 */
	public M setSortRank(java.lang.Integer sortRank) {
		set("sort_rank", sortRank);
		return (M)this;
	}
	
	/**
	 * 数据表内默认顺序
	 */
	@JBoltField(name="sortRank" ,columnName="sort_rank",type="Integer", remark="数据表内默认顺序", required=true, maxLength=10, fixed=0, order=9)
	public java.lang.Integer getSortRank() {
		return getInt("sort_rank");
	}

	/**
	 * 表格中的排序
	 */
	public M setSortRankIntable(java.lang.Integer sortRankIntable) {
		set("sort_rank_intable", sortRankIntable);
		return (M)this;
	}
	
	/**
	 * 表格中的排序
	 */
	@JBoltField(name="sortRankIntable" ,columnName="sort_rank_intable",type="Integer", remark="表格中的排序", required=true, maxLength=10, fixed=0, order=10)
	public java.lang.Integer getSortRankIntable() {
		return getInt("sort_rank_intable");
	}

	/**
	 * 表单中的排序
	 */
	public M setSortRankInform(java.lang.Integer sortRankInform) {
		set("sort_rank_inform", sortRankInform);
		return (M)this;
	}
	
	/**
	 * 表单中的排序
	 */
	@JBoltField(name="sortRankInform" ,columnName="sort_rank_inform",type="Integer", remark="表单中的排序", required=true, maxLength=10, fixed=0, order=11)
	public java.lang.Integer getSortRankInform() {
		return getInt("sort_rank_inform");
	}

	/**
	 * 查询条件中的顺序
	 */
	public M setSortRankInsearch(java.lang.Integer sortRankInsearch) {
		set("sort_rank_insearch", sortRankInsearch);
		return (M)this;
	}
	
	/**
	 * 查询条件中的顺序
	 */
	@JBoltField(name="sortRankInsearch" ,columnName="sort_rank_insearch",type="Integer", remark="查询条件中的顺序", required=true, maxLength=10, fixed=0, order=12)
	public java.lang.Integer getSortRankInsearch() {
		return getInt("sort_rank_insearch");
	}

	/**
	 * 是否主键
	 */
	public M setIsPkey(java.lang.Boolean isPkey) {
		set("is_pkey", isPkey);
		return (M)this;
	}
	
	/**
	 * 是否主键
	 */
	@JBoltField(name="isPkey" ,columnName="is_pkey",type="Boolean", remark="是否主键", required=true, maxLength=1, fixed=0, order=13)
	public java.lang.Boolean getIsPkey() {
		return getBoolean("is_pkey");
	}

	/**
	 * 是否必填
	 */
	public M setIsRequired(java.lang.Boolean isRequired) {
		set("is_required", isRequired);
		return (M)this;
	}
	
	/**
	 * 是否必填
	 */
	@JBoltField(name="isRequired" ,columnName="is_required",type="Boolean", remark="是否必填", required=true, maxLength=1, fixed=0, order=14)
	public java.lang.Boolean getIsRequired() {
		return getBoolean("is_required");
	}

	/**
	 * 作为查询条件是否必填
	 */
	public M setIsSearchRequired(java.lang.Boolean isSearchRequired) {
		set("is_search_required", isSearchRequired);
		return (M)this;
	}
	
	/**
	 * 作为查询条件是否必填
	 */
	@JBoltField(name="isSearchRequired" ,columnName="is_search_required",type="Boolean", remark="作为查询条件是否必填", required=true, maxLength=1, fixed=0, order=15)
	public java.lang.Boolean getIsSearchRequired() {
		return getBoolean("is_search_required");
	}

	/**
	 * 查询条件必填校验规则
	 */
	public M setDataRuleForSearch(java.lang.String dataRuleForSearch) {
		set("data_rule_for_search", dataRuleForSearch);
		return (M)this;
	}
	
	/**
	 * 查询条件必填校验规则
	 */
	@JBoltField(name="dataRuleForSearch" ,columnName="data_rule_for_search",type="String", remark="查询条件必填校验规则", required=false, maxLength=255, fixed=0, order=16)
	public java.lang.String getDataRuleForSearch() {
		return getStr("data_rule_for_search");
	}

	/**
	 * 查询条件不符合校验的提示信息
	 */
	public M setDataTipsForSearch(java.lang.String dataTipsForSearch) {
		set("data_tips_for_search", dataTipsForSearch);
		return (M)this;
	}
	
	/**
	 * 查询条件不符合校验的提示信息
	 */
	@JBoltField(name="dataTipsForSearch" ,columnName="data_tips_for_search",type="String", remark="查询条件不符合校验的提示信息", required=false, maxLength=255, fixed=0, order=17)
	public java.lang.String getDataTipsForSearch() {
		return getStr("data_tips_for_search");
	}

	/**
	 * 表单单显示文本
	 */
	public M setFormLabel(java.lang.String formLabel) {
		set("form_label", formLabel);
		return (M)this;
	}
	
	/**
	 * 表单单显示文本
	 */
	@JBoltField(name="formLabel" ,columnName="form_label",type="String", remark="表单单显示文本", required=false, maxLength=255, fixed=0, order=18)
	public java.lang.String getFormLabel() {
		return getStr("form_label");
	}

	/**
	 * 表单placeholder
	 */
	public M setPlaceholder(java.lang.String placeholder) {
		set("placeholder", placeholder);
		return (M)this;
	}
	
	/**
	 * 表单placeholder
	 */
	@JBoltField(name="placeholder" ,columnName="placeholder",type="String", remark="表单placeholder", required=false, maxLength=40, fixed=0, order=19)
	public java.lang.String getPlaceholder() {
		return getStr("placeholder");
	}

	/**
	 * 表格中显示文本
	 */
	public M setTableLabel(java.lang.String tableLabel) {
		set("table_label", tableLabel);
		return (M)this;
	}
	
	/**
	 * 表格中显示文本
	 */
	@JBoltField(name="tableLabel" ,columnName="table_label",type="String", remark="表格中显示文本", required=false, maxLength=40, fixed=0, order=20)
	public java.lang.String getTableLabel() {
		return getStr("table_label");
	}

	/**
	 * 查询表单提示文本
	 */
	public M setSearchFormLabel(java.lang.String searchFormLabel) {
		set("search_form_label", searchFormLabel);
		return (M)this;
	}
	
	/**
	 * 查询表单提示文本
	 */
	@JBoltField(name="searchFormLabel" ,columnName="search_form_label",type="String", remark="查询表单提示文本", required=false, maxLength=40, fixed=0, order=21)
	public java.lang.String getSearchFormLabel() {
		return getStr("search_form_label");
	}

	/**
	 * 备注
	 */
	public M setRemark(java.lang.String remark) {
		set("remark", remark);
		return (M)this;
	}
	
	/**
	 * 备注
	 */
	@JBoltField(name="remark" ,columnName="remark",type="String", remark="备注", required=false, maxLength=255, fixed=0, order=22)
	public java.lang.String getRemark() {
		return getStr("remark");
	}

	/**
	 * 是否为关键词查询列
	 */
	public M setIsKeywordsColumn(java.lang.Boolean isKeywordsColumn) {
		set("is_keywords_column", isKeywordsColumn);
		return (M)this;
	}
	
	/**
	 * 是否为关键词查询列
	 */
	@JBoltField(name="isKeywordsColumn" ,columnName="is_keywords_column",type="Boolean", remark="是否为关键词查询列", required=true, maxLength=1, fixed=0, order=23)
	public java.lang.Boolean getIsKeywordsColumn() {
		return getBoolean("is_keywords_column");
	}

	/**
	 * 是否表单可编辑元素
	 */
	public M setIsFormEle(java.lang.Boolean isFormEle) {
		set("is_form_ele", isFormEle);
		return (M)this;
	}
	
	/**
	 * 是否表单可编辑元素
	 */
	@JBoltField(name="isFormEle" ,columnName="is_form_ele",type="Boolean", remark="是否表单可编辑元素", required=true, maxLength=1, fixed=0, order=24)
	public java.lang.Boolean getIsFormEle() {
		return getBoolean("is_form_ele");
	}

	/**
	 * 是否表格列
	 */
	public M setIsTableCol(java.lang.Boolean isTableCol) {
		set("is_table_col", isTableCol);
		return (M)this;
	}
	
	/**
	 * 是否表格列
	 */
	@JBoltField(name="isTableCol" ,columnName="is_table_col",type="Boolean", remark="是否表格列", required=true, maxLength=1, fixed=0, order=25)
	public java.lang.Boolean getIsTableCol() {
		return getBoolean("is_table_col");
	}

	/**
	 * 是否为表格switchbtn
	 */
	public M setIsTableSwitchbtn(java.lang.Boolean isTableSwitchbtn) {
		set("is_table_switchbtn", isTableSwitchbtn);
		return (M)this;
	}
	
	/**
	 * 是否为表格switchbtn
	 */
	@JBoltField(name="isTableSwitchbtn" ,columnName="is_table_switchbtn",type="Boolean", remark="是否为表格switchbtn", required=true, maxLength=1, fixed=0, order=26)
	public java.lang.Boolean getIsTableSwitchbtn() {
		return getBoolean("is_table_switchbtn");
	}

	/**
	 * 列宽
	 */
	public M setTableColWidth(java.lang.Integer tableColWidth) {
		set("table_col_width", tableColWidth);
		return (M)this;
	}
	
	/**
	 * 列宽
	 */
	@JBoltField(name="tableColWidth" ,columnName="table_col_width",type="Integer", remark="列宽", required=true, maxLength=10, fixed=0, order=27)
	public java.lang.Integer getTableColWidth() {
		return getInt("table_col_width");
	}

	/**
	 * 是否固定宽度
	 */
	public M setIsNeedFixedWidth(java.lang.Boolean isNeedFixedWidth) {
		set("is_need_fixed_width", isNeedFixedWidth);
		return (M)this;
	}
	
	/**
	 * 是否固定宽度
	 */
	@JBoltField(name="isNeedFixedWidth" ,columnName="is_need_fixed_width",type="Boolean", remark="是否固定宽度", required=true, maxLength=1, fixed=0, order=28)
	public java.lang.Boolean getIsNeedFixedWidth() {
		return getBoolean("is_need_fixed_width");
	}

	/**
	 * 是否检索条件
	 */
	public M setIsSearchEle(java.lang.Boolean isSearchEle) {
		set("is_search_ele", isSearchEle);
		return (M)this;
	}
	
	/**
	 * 是否检索条件
	 */
	@JBoltField(name="isSearchEle" ,columnName="is_search_ele",type="Boolean", remark="是否检索条件", required=true, maxLength=1, fixed=0, order=29)
	public java.lang.Boolean getIsSearchEle() {
		return getBoolean("is_search_ele");
	}

	/**
	 * 是否为检索隐藏条件
	 */
	public M setIsSearchHidden(java.lang.Boolean isSearchHidden) {
		set("is_search_hidden", isSearchHidden);
		return (M)this;
	}
	
	/**
	 * 是否为检索隐藏条件
	 */
	@JBoltField(name="isSearchHidden" ,columnName="is_search_hidden",type="Boolean", remark="是否为检索隐藏条件", required=true, maxLength=1, fixed=0, order=30)
	public java.lang.Boolean getIsSearchHidden() {
		return getBoolean("is_search_hidden");
	}

	/**
	 * 格式化操作值
	 */
	public M setColFormat(java.lang.String colFormat) {
		set("col_format", colFormat);
		return (M)this;
	}
	
	/**
	 * 格式化操作值
	 */
	@JBoltField(name="colFormat" ,columnName="col_format",type="String", remark="格式化操作值", required=false, maxLength=255, fixed=0, order=31)
	public java.lang.String getColFormat() {
		return getStr("col_format");
	}

	/**
	 * 查询用ui 组件类型
	 */
	public M setSearchUiType(java.lang.String searchUiType) {
		set("search_ui_type", searchUiType);
		return (M)this;
	}
	
	/**
	 * 查询用ui 组件类型
	 */
	@JBoltField(name="searchUiType" ,columnName="search_ui_type",type="String", remark="查询用ui 组件类型", required=false, maxLength=40, fixed=0, order=32)
	public java.lang.String getSearchUiType() {
		return getStr("search_ui_type");
	}

	/**
	 * 查询用组件数据源类型
	 */
	public M setSearchDataType(java.lang.String searchDataType) {
		set("search_data_type", searchDataType);
		return (M)this;
	}
	
	/**
	 * 查询用组件数据源类型
	 */
	@JBoltField(name="searchDataType" ,columnName="search_data_type",type="String", remark="查询用组件数据源类型", required=false, maxLength=40, fixed=0, order=33)
	public java.lang.String getSearchDataType() {
		return getStr("search_data_type");
	}

	/**
	 * 查询用组件数据值
	 */
	public M setSearchDataValue(java.lang.String searchDataValue) {
		set("search_data_value", searchDataValue);
		return (M)this;
	}
	
	/**
	 * 查询用组件数据值
	 */
	@JBoltField(name="searchDataValue" ,columnName="search_data_value",type="String", remark="查询用组件数据值", required=false, maxLength=255, fixed=0, order=34)
	public java.lang.String getSearchDataValue() {
		return getStr("search_data_value");
	}

	/**
	 * 查询用组件默认值
	 */
	public M setSearchDefaultValue(java.lang.String searchDefaultValue) {
		set("search_default_value", searchDefaultValue);
		return (M)this;
	}
	
	/**
	 * 查询用组件默认值
	 */
	@JBoltField(name="searchDefaultValue" ,columnName="search_default_value",type="String", remark="查询用组件默认值", required=false, maxLength=255, fixed=0, order=35)
	public java.lang.String getSearchDefaultValue() {
		return getStr("search_default_value");
	}

	/**
	 * 独立新行
	 */
	public M setIsSingleLine(java.lang.Boolean isSingleLine) {
		set("is_single_line", isSingleLine);
		return (M)this;
	}
	
	/**
	 * 独立新行
	 */
	@JBoltField(name="isSingleLine" ,columnName="is_single_line",type="Boolean", remark="独立新行", required=true, maxLength=1, fixed=0, order=36)
	public java.lang.Boolean getIsSingleLine() {
		return getBoolean("is_single_line");
	}

	/**
	 * 是否需要data_handler
	 */
	public M setNeedDataHandler(java.lang.Boolean needDataHandler) {
		set("need_data_handler", needDataHandler);
		return (M)this;
	}
	
	/**
	 * 是否需要data_handler
	 */
	@JBoltField(name="needDataHandler" ,columnName="need_data_handler",type="Boolean", remark="是否需要data_handler", required=false, maxLength=1, fixed=0, order=37)
	public java.lang.Boolean getNeedDataHandler() {
		return getBoolean("need_data_handler");
	}

	/**
	 * 表单组件类型
	 */
	public M setFormUiType(java.lang.String formUiType) {
		set("form_ui_type", formUiType);
		return (M)this;
	}
	
	/**
	 * 表单组件类型
	 */
	@JBoltField(name="formUiType" ,columnName="form_ui_type",type="String", remark="表单组件类型", required=false, maxLength=40, fixed=0, order=38)
	public java.lang.String getFormUiType() {
		return getStr("form_ui_type");
	}

	/**
	 * jboltinput filter handler
	 */
	public M setFormJboltinputFilterHandler(java.lang.String formJboltinputFilterHandler) {
		set("form_jboltinput_filter_handler", formJboltinputFilterHandler);
		return (M)this;
	}
	
	/**
	 * jboltinput filter handler
	 */
	@JBoltField(name="formJboltinputFilterHandler" ,columnName="form_jboltinput_filter_handler",type="String", remark="jboltinput filter handler", required=false, maxLength=60, fixed=0, order=39)
	public java.lang.String getFormJboltinputFilterHandler() {
		return getStr("form_jboltinput_filter_handler");
	}

	/**
	 * jboltinput jstree是否有checkbox
	 */
	public M setIsFormJboltinputJstreeCheckbox(java.lang.Boolean isFormJboltinputJstreeCheckbox) {
		set("is_form_jboltinput_jstree_checkbox", isFormJboltinputJstreeCheckbox);
		return (M)this;
	}
	
	/**
	 * jboltinput jstree是否有checkbox
	 */
	@JBoltField(name="isFormJboltinputJstreeCheckbox" ,columnName="is_form_jboltinput_jstree_checkbox",type="Boolean", remark="jboltinput jstree是否有checkbox", required=false, maxLength=1, fixed=0, order=40)
	public java.lang.Boolean getIsFormJboltinputJstreeCheckbox() {
		return getBoolean("is_form_jboltinput_jstree_checkbox");
	}

	/**
	 * jboltinput jstree checkbox只选子节点
	 */
	public M setIsFormJboltinputJstreeOnlyLeaf(java.lang.Boolean isFormJboltinputJstreeOnlyLeaf) {
		set("is_form_jboltinput_jstree_only_leaf", isFormJboltinputJstreeOnlyLeaf);
		return (M)this;
	}
	
	/**
	 * jboltinput jstree checkbox只选子节点
	 */
	@JBoltField(name="isFormJboltinputJstreeOnlyLeaf" ,columnName="is_form_jboltinput_jstree_only_leaf",type="Boolean", remark="jboltinput jstree checkbox只选子节点", required=false, maxLength=1, fixed=0, order=41)
	public java.lang.Boolean getIsFormJboltinputJstreeOnlyLeaf() {
		return getBoolean("is_form_jboltinput_jstree_only_leaf");
	}

	/**
	 * 表单组件数据源类型
	 */
	public M setFormDataType(java.lang.String formDataType) {
		set("form_data_type", formDataType);
		return (M)this;
	}
	
	/**
	 * 表单组件数据源类型
	 */
	@JBoltField(name="formDataType" ,columnName="form_data_type",type="String", remark="表单组件数据源类型", required=false, maxLength=40, fixed=0, order=42)
	public java.lang.String getFormDataType() {
		return getStr("form_data_type");
	}

	/**
	 * 表单组件数据值
	 */
	public M setFormDataValue(java.lang.String formDataValue) {
		set("form_data_value", formDataValue);
		return (M)this;
	}
	
	/**
	 * 表单组件数据值
	 */
	@JBoltField(name="formDataValue" ,columnName="form_data_value",type="String", remark="表单组件数据值", required=false, maxLength=255, fixed=0, order=43)
	public java.lang.String getFormDataValue() {
		return getStr("form_data_value");
	}

	/**
	 * 表单组件默认值
	 */
	public M setFormDefaultValue(java.lang.String formDefaultValue) {
		set("form_default_value", formDefaultValue);
		return (M)this;
	}
	
	/**
	 * 表单组件默认值
	 */
	@JBoltField(name="formDefaultValue" ,columnName="form_default_value",type="String", remark="表单组件默认值", required=false, maxLength=255, fixed=0, order=44)
	public java.lang.String getFormDefaultValue() {
		return getStr("form_default_value");
	}

	/**
	 * 表单校验规则 自定义
	 */
	public M setDataRuleAssign(java.lang.String dataRuleAssign) {
		set("data_rule_assign", dataRuleAssign);
		return (M)this;
	}
	
	/**
	 * 表单校验规则 自定义
	 */
	@JBoltField(name="dataRuleAssign" ,columnName="data_rule_assign",type="String", remark="表单校验规则 自定义", required=false, maxLength=255, fixed=0, order=45)
	public java.lang.String getDataRuleAssign() {
		return getStr("data_rule_assign");
	}

	/**
	 * 校验规则
	 */
	public M setDataRule(java.lang.String dataRule) {
		set("data_rule", dataRule);
		return (M)this;
	}
	
	/**
	 * 校验规则
	 */
	@JBoltField(name="dataRule" ,columnName="data_rule",type="String", remark="校验规则", required=false, maxLength=255, fixed=0, order=46)
	public java.lang.String getDataRule() {
		return getStr("data_rule");
	}

	/**
	 * 校验提示信息
	 */
	public M setDataTips(java.lang.String dataTips) {
		set("data_tips", dataTips);
		return (M)this;
	}
	
	/**
	 * 校验提示信息
	 */
	@JBoltField(name="dataTips" ,columnName="data_tips",type="String", remark="校验提示信息", required=false, maxLength=255, fixed=0, order=47)
	public java.lang.String getDataTips() {
		return getStr("data_tips");
	}

	/**
	 * 是否为导入列
	 */
	public M setIsImportCol(java.lang.Boolean isImportCol) {
		set("is_import_col", isImportCol);
		return (M)this;
	}
	
	/**
	 * 是否为导入列
	 */
	@JBoltField(name="isImportCol" ,columnName="is_import_col",type="Boolean", remark="是否为导入列", required=true, maxLength=1, fixed=0, order=48)
	public java.lang.Boolean getIsImportCol() {
		return getBoolean("is_import_col");
	}

	/**
	 * 导出列
	 */
	public M setIsExportCol(java.lang.Boolean isExportCol) {
		set("is_export_col", isExportCol);
		return (M)this;
	}
	
	/**
	 * 导出列
	 */
	@JBoltField(name="isExportCol" ,columnName="is_export_col",type="Boolean", remark="导出列", required=true, maxLength=1, fixed=0, order=49)
	public java.lang.Boolean getIsExportCol() {
		return getBoolean("is_export_col");
	}

	/**
	 * 是否可排序
	 */
	public M setIsSortable(java.lang.Boolean isSortable) {
		set("is_sortable", isSortable);
		return (M)this;
	}
	
	/**
	 * 是否可排序
	 */
	@JBoltField(name="isSortable" ,columnName="is_sortable",type="Boolean", remark="是否可排序", required=true, maxLength=1, fixed=0, order=50)
	public java.lang.Boolean getIsSortable() {
		return getBoolean("is_sortable");
	}

	/**
	 * 可编辑表格显示组件类型
	 */
	public M setTableUiType(java.lang.String tableUiType) {
		set("table_ui_type", tableUiType);
		return (M)this;
	}
	
	/**
	 * 可编辑表格显示组件类型
	 */
	@JBoltField(name="tableUiType" ,columnName="table_ui_type",type="String", remark="可编辑表格显示组件类型", required=false, maxLength=40, fixed=0, order=51)
	public java.lang.String getTableUiType() {
		return getStr("table_ui_type");
	}

	/**
	 * 表格组件数据库类型
	 */
	public M setTableDataType(java.lang.String tableDataType) {
		set("table_data_type", tableDataType);
		return (M)this;
	}
	
	/**
	 * 表格组件数据库类型
	 */
	@JBoltField(name="tableDataType" ,columnName="table_data_type",type="String", remark="表格组件数据库类型", required=false, maxLength=40, fixed=0, order=52)
	public java.lang.String getTableDataType() {
		return getStr("table_data_type");
	}

	/**
	 * 表格组件数据值
	 */
	public M setTableDataValue(java.lang.String tableDataValue) {
		set("table_data_value", tableDataValue);
		return (M)this;
	}
	
	/**
	 * 表格组件数据值
	 */
	@JBoltField(name="tableDataValue" ,columnName="table_data_value",type="String", remark="表格组件数据值", required=false, maxLength=255, fixed=0, order=53)
	public java.lang.String getTableDataValue() {
		return getStr("table_data_value");
	}

	/**
	 * 组件自定义宽度
	 */
	public M setFormEleWidth(java.lang.Integer formEleWidth) {
		set("form_ele_width", formEleWidth);
		return (M)this;
	}
	
	/**
	 * 组件自定义宽度
	 */
	@JBoltField(name="formEleWidth" ,columnName="form_ele_width",type="Integer", remark="组件自定义宽度", required=true, maxLength=10, fixed=0, order=54)
	public java.lang.Integer getFormEleWidth() {
		return getInt("form_ele_width");
	}

	/**
	 * radio checkbox等是否inline
	 */
	public M setIsItemInline(java.lang.Boolean isItemInline) {
		set("is_item_inline", isItemInline);
		return (M)this;
	}
	
	/**
	 * radio checkbox等是否inline
	 */
	@JBoltField(name="isItemInline" ,columnName="is_item_inline",type="Boolean", remark="radio checkbox等是否inline", required=true, maxLength=1, fixed=0, order=55)
	public java.lang.Boolean getIsItemInline() {
		return getBoolean("is_item_inline");
	}

	/**
	 * data-text-attr
	 */
	public M setFormDataTextAttr(java.lang.String formDataTextAttr) {
		set("form_data_text_attr", formDataTextAttr);
		return (M)this;
	}
	
	/**
	 * data-text-attr
	 */
	@JBoltField(name="formDataTextAttr" ,columnName="form_data_text_attr",type="String", remark="data-text-attr", required=false, maxLength=255, fixed=0, order=56)
	public java.lang.String getFormDataTextAttr() {
		return getStr("form_data_text_attr");
	}

	/**
	 * data-value-attr
	 */
	public M setFormDataValueAttr(java.lang.String formDataValueAttr) {
		set("form_data_value_attr", formDataValueAttr);
		return (M)this;
	}
	
	/**
	 * data-value-attr
	 */
	@JBoltField(name="formDataValueAttr" ,columnName="form_data_value_attr",type="String", remark="data-value-attr", required=false, maxLength=40, fixed=0, order=57)
	public java.lang.String getFormDataValueAttr() {
		return getStr("form_data_value_attr");
	}

	/**
	 * data-column-attr
	 */
	public M setFormDataColumnAttr(java.lang.String formDataColumnAttr) {
		set("form_data_column_attr", formDataColumnAttr);
		return (M)this;
	}
	
	/**
	 * data-column-attr
	 */
	@JBoltField(name="formDataColumnAttr" ,columnName="form_data_column_attr",type="String", remark="data-column-attr", required=false, maxLength=255, fixed=0, order=58)
	public java.lang.String getFormDataColumnAttr() {
		return getStr("form_data_column_attr");
	}

	/**
	 * data-text-attr
	 */
	public M setSearchDataTextAttr(java.lang.String searchDataTextAttr) {
		set("search_data_text_attr", searchDataTextAttr);
		return (M)this;
	}
	
	/**
	 * data-text-attr
	 */
	@JBoltField(name="searchDataTextAttr" ,columnName="search_data_text_attr",type="String", remark="data-text-attr", required=false, maxLength=255, fixed=0, order=59)
	public java.lang.String getSearchDataTextAttr() {
		return getStr("search_data_text_attr");
	}

	/**
	 * data-value-attr
	 */
	public M setSearchDataValueAttr(java.lang.String searchDataValueAttr) {
		set("search_data_value_attr", searchDataValueAttr);
		return (M)this;
	}
	
	/**
	 * data-value-attr
	 */
	@JBoltField(name="searchDataValueAttr" ,columnName="search_data_value_attr",type="String", remark="data-value-attr", required=false, maxLength=40, fixed=0, order=60)
	public java.lang.String getSearchDataValueAttr() {
		return getStr("search_data_value_attr");
	}

	/**
	 * data-column-attr
	 */
	public M setSearchDataColumnAttr(java.lang.String searchDataColumnAttr) {
		set("search_data_column_attr", searchDataColumnAttr);
		return (M)this;
	}
	
	/**
	 * data-column-attr
	 */
	@JBoltField(name="searchDataColumnAttr" ,columnName="search_data_column_attr",type="String", remark="data-column-attr", required=false, maxLength=255, fixed=0, order=61)
	public java.lang.String getSearchDataColumnAttr() {
		return getStr("search_data_column_attr");
	}

	/**
	 * data-text-attr
	 */
	public M setTableDataTextAttr(java.lang.String tableDataTextAttr) {
		set("table_data_text_attr", tableDataTextAttr);
		return (M)this;
	}
	
	/**
	 * data-text-attr
	 */
	@JBoltField(name="tableDataTextAttr" ,columnName="table_data_text_attr",type="String", remark="data-text-attr", required=false, maxLength=255, fixed=0, order=62)
	public java.lang.String getTableDataTextAttr() {
		return getStr("table_data_text_attr");
	}

	/**
	 * data-value-attr
	 */
	public M setTableDataValueAttr(java.lang.String tableDataValueAttr) {
		set("table_data_value_attr", tableDataValueAttr);
		return (M)this;
	}
	
	/**
	 * data-value-attr
	 */
	@JBoltField(name="tableDataValueAttr" ,columnName="table_data_value_attr",type="String", remark="data-value-attr", required=false, maxLength=40, fixed=0, order=63)
	public java.lang.String getTableDataValueAttr() {
		return getStr("table_data_value_attr");
	}

	/**
	 * data-column-attr
	 */
	public M setTableDataColumnAttr(java.lang.String tableDataColumnAttr) {
		set("table_data_column_attr", tableDataColumnAttr);
		return (M)this;
	}
	
	/**
	 * data-column-attr
	 */
	@JBoltField(name="tableDataColumnAttr" ,columnName="table_data_column_attr",type="String", remark="data-column-attr", required=false, maxLength=255, fixed=0, order=64)
	public java.lang.String getTableDataColumnAttr() {
		return getStr("table_data_column_attr");
	}

}
