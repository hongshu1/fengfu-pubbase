alter table "jb_user" add COLUMN "sn" varchar(20) COLLATE "pg_catalog"."default";
COMMENT ON COLUMN "public"."jb_user"."age" IS '年龄';

alter table "jb_user" add COLUMN "age" int4 DEFAULT 0 NOT NULL;
COMMENT ON COLUMN "public"."jb_user"."sn" IS '工号';

alter table "jb_user" add COLUMN "email" varchar(60) COLLATE "pg_catalog"."default";
COMMENT ON COLUMN "public"."jb_user"."email" IS '电子邮件地址';

alter table "jb_user" add COLUMN "dept_path" varchar(255) COLLATE "pg_catalog"."default";
COMMENT ON COLUMN "public"."jb_user"."dept_path" IS '部门路径';

alter table "jb_user" add COLUMN "update_user_id" int8 NOT NULL;
COMMENT ON COLUMN "public"."jb_user"."update_user_id" IS '更新用户ID';

alter table "jb_user" add COLUMN "update_time" timestamp(6) COLLATE "pg_catalog"."default";
COMMENT ON COLUMN "public"."jb_user"."update_time" IS '更新时间';

alter table "jb_user" add COLUMN "last_pwd_update_time" timestamp(6) COLLATE "pg_catalog"."default";
COMMENT ON COLUMN "public"."jb_user"."last_pwd_update_time" IS '密码最新更新时间';

alter table "jb_dictionary_type" add COLUMN  "enable" char(1) COLLATE "pg_catalog"."default" NOT NULL DEFAULT '1'::bpchar;
COMMENT ON COLUMN "public"."jb_dictionary_type"."enable" IS '是否启用';

alter table "jb_dictionary" add COLUMN  "enable" char(1) COLLATE "pg_catalog"."default" NOT NULL DEFAULT '1'::bpchar;
COMMENT ON COLUMN "public"."jb_dictionary"."enable" IS '是否启用';

alter table "jb_global_config_type" add COLUMN  "enable" char(1) COLLATE "pg_catalog"."default" NOT NULL DEFAULT '1'::bpchar;
COMMENT ON COLUMN "public"."jb_global_config_type"."enable" IS '是否启用';

alter table "jb_global_config" add COLUMN  "enable" char(1) COLLATE "pg_catalog"."default" NOT NULL DEFAULT '1'::bpchar;
COMMENT ON COLUMN "public"."jb_global_config"."enable" IS '是否启用';

alter table "jb_dept" add COLUMN "dept_path" varchar(255) COLLATE "pg_catalog"."default";
COMMENT ON COLUMN "public"."jb_dept"."dept_path" IS '部门路径';



CREATE TABLE "public"."jb_user_extend" (
"id" int8 NOT NULL,
"english_full_name" varchar(40) COLLATE "pg_catalog"."default",
"birthday" timestamp(6),
"residential_address" varchar(100) COLLATE "pg_catalog"."default",
"company_name" varchar(40) COLLATE "pg_catalog"."default",
"company_address" varchar(100) COLLATE "pg_catalog"."default",
"recipient_address" varchar(100) COLLATE "pg_catalog"."default",
"recipient_phone" varchar(20) COLLATE "pg_catalog"."default",
"recipient_name" varchar(40) COLLATE "pg_catalog"."default",
"id_number" varchar(20) COLLATE "pg_catalog"."default",
"country" varchar(20) COLLATE "pg_catalog"."default",
"nation" varchar(20) COLLATE "pg_catalog"."default",
"province" varchar(20) COLLATE "pg_catalog"."default",
"city" varchar(20) COLLATE "pg_catalog"."default",
"city_code" varchar(20) COLLATE "pg_catalog"."default",
"county" varchar(20) COLLATE "pg_catalog"."default",
"township" varchar(20) COLLATE "pg_catalog"."default",
"community" varchar(20) COLLATE "pg_catalog"."default",
"marital_status" int4,
"is_cpc_member" char(1) COLLATE "pg_catalog"."default" NOT NULL,
"is_cyl_member" char(1) COLLATE "pg_catalog"."default" NOT NULL,
"professional_title" int4,
CONSTRAINT "jb_user_extend_pkey" PRIMARY KEY ("id")
)
;

ALTER TABLE "public"."jb_user_extend"
    OWNER TO "postgres";

COMMENT ON COLUMN "public"."jb_user_extend"."id" IS '主键ID';

COMMENT ON COLUMN "public"."jb_user_extend"."english_full_name" IS '英文全名';

COMMENT ON COLUMN "public"."jb_user_extend"."birthday" IS '出生日期';

COMMENT ON COLUMN "public"."jb_user_extend"."residential_address" IS '居住地址';

COMMENT ON COLUMN "public"."jb_user_extend"."company_name" IS '公司名称';

COMMENT ON COLUMN "public"."jb_user_extend"."company_address" IS '公司地址';

COMMENT ON COLUMN "public"."jb_user_extend"."recipient_address" IS '快递收件地址';

COMMENT ON COLUMN "public"."jb_user_extend"."recipient_phone" IS '收件人电话';

COMMENT ON COLUMN "public"."jb_user_extend"."recipient_name" IS '收件人姓名';

COMMENT ON COLUMN "public"."jb_user_extend"."id_number" IS '身份证号';

COMMENT ON COLUMN "public"."jb_user_extend"."country" IS '国家';

COMMENT ON COLUMN "public"."jb_user_extend"."nation" IS '民族';

COMMENT ON COLUMN "public"."jb_user_extend"."province" IS '省';

COMMENT ON COLUMN "public"."jb_user_extend"."city" IS '城市';

COMMENT ON COLUMN "public"."jb_user_extend"."city_code" IS '城市代码';

COMMENT ON COLUMN "public"."jb_user_extend"."county" IS '区县';

COMMENT ON COLUMN "public"."jb_user_extend"."township" IS '乡镇';

COMMENT ON COLUMN "public"."jb_user_extend"."community" IS '行政村 社区';

COMMENT ON COLUMN "public"."jb_user_extend"."marital_status" IS '婚姻状态';

COMMENT ON COLUMN "public"."jb_user_extend"."is_cpc_member" IS '是否党员';

COMMENT ON COLUMN "public"."jb_user_extend"."is_cyl_member" IS '是否共青团员';

COMMENT ON COLUMN "public"."jb_user_extend"."professional_title" IS '职称';

COMMENT ON TABLE "public"."jb_user_extend" IS '用户扩展信息表';


CREATE TABLE "public"."jb_sensitive_word" (
"id" int8 NOT NULL,
"content" varchar(40) COLLATE "pg_catalog"."default" NOT NULL,
"enable" char(1) COLLATE "pg_catalog"."default" NOT NULL,
CONSTRAINT "jb_sensitive_word_pkey" PRIMARY KEY ("id")
)
;

ALTER TABLE "public"."jb_sensitive_word" OWNER TO "postgres";

COMMENT ON COLUMN "public"."jb_sensitive_word"."id" IS '主键ID';

COMMENT ON COLUMN "public"."jb_sensitive_word"."content" IS '内容';

COMMENT ON COLUMN "public"."jb_sensitive_word"."enable" IS '启用状态';

COMMENT ON TABLE "public"."jb_sensitive_word" IS '敏感词词库';




CREATE TABLE "public"."jb_code_gen" (
"id" int8 NOT NULL,
"pid" int8,
"project_path" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
"is_sub_table" char(1) COLLATE "pg_catalog"."default" NOT NULL,
"sort_rank" int4 NOT NULL,
"type" varchar(40) COLLATE "pg_catalog"."default" NOT NULL,
"main_table_name" varchar(150) COLLATE "pg_catalog"."default" NOT NULL,
"datasource_name" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
"datasource_remark" varchar(40) COLLATE "pg_catalog"."default" NOT NULL,
"database_type" varchar(40) COLLATE "pg_catalog"."default" NOT NULL,
"is_main_datasource" char(1) COLLATE "pg_catalog"."default" NOT NULL,
"main_table_pkey" varchar(64) COLLATE "pg_catalog"."default",
"table_remove_prefix" varchar(40) COLLATE "pg_catalog"."default",
"main_table_idgenmode" varchar(40) COLLATE "pg_catalog"."default" NOT NULL,
"version_sn" int4 NOT NULL,
"main_table_remark" varchar(100) COLLATE "pg_catalog"."default",
"author" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
"style" int4 NOT NULL,
"is_crud" char(1) COLLATE "pg_catalog"."default" NOT NULL,
"is_editable" char(1) COLLATE "pg_catalog"."default" NOT NULL,
"is_tree_table" char(1) COLLATE "pg_catalog"."default" NOT NULL,
"is_check_can_delete" char(1) COLLATE "pg_catalog"."default" NOT NULL,
"is_check_can_toggle" char(1) COLLATE "pg_catalog"."default" NOT NULL,
"is_check_can_recover" char(1) COLLATE "pg_catalog"."default" NOT NULL,
"editable_submit_type" varchar(20) COLLATE "pg_catalog"."default",
"state" int4 NOT NULL,
"sub_table_count" int4,
"topnav_id" int8,
"permission_id" int8,
"roles" varchar(255) COLLATE "pg_catalog"."default",
"create_user_id" int8 NOT NULL,
"create_time" timestamp(6) NOT NULL,
"update_user_id" int8 NOT NULL,
"update_time" timestamp(6) NOT NULL,
"gen_user_id" int8,
"gen_time" timestamp(6),
"model_name" varchar(64) COLLATE "pg_catalog"."default",
"base_model_name" varchar(64) COLLATE "pg_catalog"."default",
"remark" varchar(255) COLLATE "pg_catalog"."default",
"is_auto_cache" char(1) COLLATE "pg_catalog"."default" NOT NULL,
"is_id_cache" char(1) COLLATE "pg_catalog"."default" NOT NULL,
"is_key_cache" char(1) COLLATE "pg_catalog"."default" NOT NULL,
"key_cache_column" varchar(40) COLLATE "pg_catalog"."default",
"key_cache_bind_column" varchar(40) COLLATE "pg_catalog"."default",
"controller_name" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
"controller_path" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
"main_java_package" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
"service_name" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
"controller_package" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
"service_package" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
"index_html_page_icon" varchar(60) COLLATE "pg_catalog"."default",
"index_html_page_title" varchar(40) COLLATE "pg_catalog"."default",
"model_package" varchar(255) COLLATE "pg_catalog"."default",
"html_view_path" varchar(255) COLLATE "pg_catalog"."default",
"routes_scan_package" varchar(255) COLLATE "pg_catalog"."default",
"is_gen_model" char(1) COLLATE "pg_catalog"."default" NOT NULL,
"is_table_use_record" char(1) COLLATE "pg_catalog"."default" NOT NULL,
"is_table_record_camel_case" char(1) COLLATE "pg_catalog"."default" NOT NULL,
"is_import_excel" char(1) COLLATE "pg_catalog"."default" NOT NULL,
"is_export_excel" char(1) COLLATE "pg_catalog"."default" NOT NULL,
"is_export_excel_by_checked_ids" char(1) COLLATE "pg_catalog"."default" NOT NULL,
"is_export_excel_by_form" char(1) COLLATE "pg_catalog"."default" NOT NULL,
"is_export_excel_all" char(1) COLLATE "pg_catalog"."default" NOT NULL,
"is_copy_to_excel" char(1) COLLATE "pg_catalog"."default" NOT NULL,
"is_copy_from_excel" char(1) COLLATE "pg_catalog"."default" NOT NULL,
"is_toolbar" char(1) COLLATE "pg_catalog"."default" NOT NULL,
"is_headbox" char(1) COLLATE "pg_catalog"."default" NOT NULL,
"is_leftbox" char(1) COLLATE "pg_catalog"."default" NOT NULL,
"is_rightbox" char(1) COLLATE "pg_catalog"."default" NOT NULL,
"is_footbox" char(1) COLLATE "pg_catalog"."default" NOT NULL,
"is_paginate" char(1) COLLATE "pg_catalog"."default" NOT NULL,
"is_table_sortable_move" char(1) COLLATE "pg_catalog"."default" NOT NULL,
"leftbox_width" int4,
"rightbox_width" int4,
"headbox_height" int4,
"footbox_height" int4,
"is_leftbox_footer" char(1) COLLATE "pg_catalog"."default" NOT NULL,
"is_rightbox_footer" char(1) COLLATE "pg_catalog"."default" NOT NULL,
"leftbox_footer_button_count" int4,
"rightbox_footer_button_count" int4,
"leftbox_title" varchar(40) COLLATE "pg_catalog"."default",
"leftbox_icon" varchar(60) COLLATE "pg_catalog"."default",
"rightbox_title" varchar(40) COLLATE "pg_catalog"."default",
"rightbox_icon" varchar(60) COLLATE "pg_catalog"."default",
"is_show_optcol_sort" char(1) COLLATE "pg_catalog"."default" NOT NULL,
"is_show_optcol_edit" char(1) COLLATE "pg_catalog"."default" NOT NULL,
"is_show_optcol_del" char(1) COLLATE "pg_catalog"."default" NOT NULL,
"is_show_optcol" char(1) COLLATE "pg_catalog"."default" NOT NULL,
"is_show_optcol_recover" char(1) COLLATE "pg_catalog"."default" NOT NULL,
"default_sort_column" varchar(40) COLLATE "pg_catalog"."default",
"default_sort_type" varchar(40) COLLATE "pg_catalog"."default",
"table_optcol_width" int4 NOT NULL,
"is_table_column_resize" char(1) COLLATE "pg_catalog"."default" NOT NULL,
"is_table_prepend_column" char(1) COLLATE "pg_catalog"."default" NOT NULL,
"table_prepend_column_type" varchar(40) COLLATE "pg_catalog"."default",
"table_prepend_column_index" int4 NOT NULL,
"is_table_prepend_column_linkparent" char(1) COLLATE "pg_catalog"."default" NOT NULL,
"is_table_prepend_column_linkson" char(1) COLLATE "pg_catalog"."default" NOT NULL,
"table_prepend_column_rowspan" int4 NOT NULL,
"is_table_prepend_column_click_active" char(1) COLLATE "pg_catalog"."default" NOT NULL,
"table_fixed_column_left" varchar(40) COLLATE "pg_catalog"."default",
"table_fixed_column_right" varchar(40) COLLATE "pg_catalog"."default",
"table_pagesize_options" varchar(255) COLLATE "pg_catalog"."default",
"table_width_assign" varchar(40) COLLATE "pg_catalog"."default",
"table_width" varchar(40) COLLATE "pg_catalog"."default" NOT NULL,
"table_height_assign" varchar(40) COLLATE "pg_catalog"."default",
"table_height" varchar(40) COLLATE "pg_catalog"."default" NOT NULL,
"table_default_sort_column" varchar(60) COLLATE "pg_catalog"."default",
"table_default_sort_type" varchar(40) COLLATE "pg_catalog"."default",
"is_keywords_search" char(1) COLLATE "pg_catalog"."default" NOT NULL,
"keywords_match_columns" varchar(255) COLLATE "pg_catalog"."default",
"toolbar_extra_button_size" int4,
"is_deleted" char(1) COLLATE "pg_catalog"."default" NOT NULL,
"form_column_size" int4 NOT NULL,
"is_form_group_row" char(1) COLLATE "pg_catalog"."default" NOT NULL,
"form_column_proportion" varchar(40) COLLATE "pg_catalog"."default",
"form_column_direction" varchar(10) COLLATE "pg_catalog"."default",
"form_group_proportion" varchar(40) COLLATE "pg_catalog"."default",
"is_view_use_path" char(1) COLLATE "pg_catalog"."default" NOT NULL,
"view_layout" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
"is_need_new_route" char(1) COLLATE "pg_catalog"."default" NOT NULL,
"routes_class_name" varchar(255) COLLATE "pg_catalog"."default",
"is_need_admin_interceptor" char(1) COLLATE "pg_catalog"."default" NOT NULL,
"extra_interceptor_class_name" varchar(255) COLLATE "pg_catalog"."default",
"is_table_multi_conditions_mode" char(1) COLLATE "pg_catalog"."default" NOT NULL,
"is_table_multi_conditions_default_hide" char(1) COLLATE "pg_catalog"."default" NOT NULL,
"is_table_multi_conditions_btn_show_title" char(1) COLLATE "pg_catalog"."default" NOT NULL,
"is_toolbar_add_btn" char(1) COLLATE "pg_catalog"."default" NOT NULL,
"is_toolbar_edit_btn" char(1) COLLATE "pg_catalog"."default" NOT NULL,
"is_toolbar_del_btn" char(1) COLLATE "pg_catalog"."default" NOT NULL,
"is_toolbar_recover_btn" char(1) COLLATE "pg_catalog"."default" NOT NULL,
"is_toolbar_refresh_btn" char(1) COLLATE "pg_catalog"."default" NOT NULL,
"is_page_title_add_btn" char(1) COLLATE "pg_catalog"."default" NOT NULL,
"is_page_title_refresh_btn" char(1) COLLATE "pg_catalog"."default" NOT NULL,
"is_page_title_init_rank_btn" char(1) COLLATE "pg_catalog"."default" NOT NULL,
"is_project_system_log" char(1) COLLATE "pg_catalog"."default" NOT NULL,
"project_system_log_target_type_text" varchar(20) COLLATE "pg_catalog"."default",
"project_system_log_target_type_value" varchar(10) COLLATE "pg_catalog"."default",
"project_system_log_target_type_key_name" varchar(40) COLLATE "pg_catalog"."default",
"form_dialog_area" varchar(20) COLLATE "pg_catalog"."default",
"is_base_model_gen_col_constant" char(1) COLLATE "pg_catalog"."default" NOT NULL,
"is_base_model_gen_col_constant_to_uppercase" char(1) COLLATE "pg_catalog"."default" NOT NULL,
CONSTRAINT "jb_code_gen_pkey" PRIMARY KEY ("id")
)
;

ALTER TABLE "public"."jb_code_gen"
    OWNER TO "postgres";

COMMENT ON COLUMN "public"."jb_code_gen"."id" IS '主键ID';

COMMENT ON COLUMN "public"."jb_code_gen"."pid" IS '父ID';

COMMENT ON COLUMN "public"."jb_code_gen"."project_path" IS '项目根路径';

COMMENT ON COLUMN "public"."jb_code_gen"."is_sub_table" IS '是否为子表';

COMMENT ON COLUMN "public"."jb_code_gen"."sort_rank" IS '子表的顺序';

COMMENT ON COLUMN "public"."jb_code_gen"."type" IS '模块类型';

COMMENT ON COLUMN "public"."jb_code_gen"."main_table_name" IS '主表名';

COMMENT ON COLUMN "public"."jb_code_gen"."datasource_name" IS '数据源';

COMMENT ON COLUMN "public"."jb_code_gen"."datasource_remark" IS '数据源说明';

COMMENT ON COLUMN "public"."jb_code_gen"."database_type" IS '数据库类型';

COMMENT ON COLUMN "public"."jb_code_gen"."is_main_datasource" IS '是否为主数据源';

COMMENT ON COLUMN "public"."jb_code_gen"."main_table_pkey" IS '主表主键';

COMMENT ON COLUMN "public"."jb_code_gen"."table_remove_prefix" IS '数据表删除前缀';

COMMENT ON COLUMN "public"."jb_code_gen"."main_table_idgenmode" IS '主表主键策略';

COMMENT ON COLUMN "public"."jb_code_gen"."version_sn" IS '版本序号';

COMMENT ON COLUMN "public"."jb_code_gen"."main_table_remark" IS '表备注';

COMMENT ON COLUMN "public"."jb_code_gen"."author" IS '功能作者';

COMMENT ON COLUMN "public"."jb_code_gen"."style" IS '样式类型';

COMMENT ON COLUMN "public"."jb_code_gen"."is_crud" IS '是否CRUD';

COMMENT ON COLUMN "public"."jb_code_gen"."is_editable" IS '是否可编辑表格';

COMMENT ON COLUMN "public"."jb_code_gen"."is_tree_table" IS '是否为树表';

COMMENT ON COLUMN "public"."jb_code_gen"."is_check_can_delete" IS '检测是否可以刪除数据';

COMMENT ON COLUMN "public"."jb_code_gen"."is_check_can_toggle" IS '检测是否可以toggle数据';

COMMENT ON COLUMN "public"."jb_code_gen"."is_check_can_recover" IS '检测是否可以recover数据';

COMMENT ON COLUMN "public"."jb_code_gen"."editable_submit_type" IS '可编辑提交方式';

COMMENT ON COLUMN "public"."jb_code_gen"."state" IS '生成状态';

COMMENT ON COLUMN "public"."jb_code_gen"."sub_table_count" IS '子表数';

COMMENT ON COLUMN "public"."jb_code_gen"."topnav_id" IS '顶部导航';

COMMENT ON COLUMN "public"."jb_code_gen"."permission_id" IS '关联权限';

COMMENT ON COLUMN "public"."jb_code_gen"."roles" IS '可访问角色';

COMMENT ON COLUMN "public"."jb_code_gen"."create_user_id" IS '创建人ID';

COMMENT ON COLUMN "public"."jb_code_gen"."create_time" IS '创建时间';

COMMENT ON COLUMN "public"."jb_code_gen"."update_user_id" IS '更新人ID';

COMMENT ON COLUMN "public"."jb_code_gen"."update_time" IS '更新时间';

COMMENT ON COLUMN "public"."jb_code_gen"."gen_user_id" IS '创建后执行生成人ID';

COMMENT ON COLUMN "public"."jb_code_gen"."gen_time" IS '创建后执行生成时间';

COMMENT ON COLUMN "public"."jb_code_gen"."model_name" IS 'modelName';

COMMENT ON COLUMN "public"."jb_code_gen"."base_model_name" IS 'baseModelName';

COMMENT ON COLUMN "public"."jb_code_gen"."remark" IS '备注';

COMMENT ON COLUMN "public"."jb_code_gen"."is_auto_cache" IS '是否启用autoCache';

COMMENT ON COLUMN "public"."jb_code_gen"."is_id_cache" IS '是否启用idCache';

COMMENT ON COLUMN "public"."jb_code_gen"."is_key_cache" IS '是否启用keyCache';

COMMENT ON COLUMN "public"."jb_code_gen"."key_cache_column" IS 'keyCache指定Column';

COMMENT ON COLUMN "public"."jb_code_gen"."key_cache_bind_column" IS 'keyCache指定bindColumn';

COMMENT ON COLUMN "public"."jb_code_gen"."controller_name" IS 'Controller Name';

COMMENT ON COLUMN "public"."jb_code_gen"."controller_path" IS 'controller path';

COMMENT ON COLUMN "public"."jb_code_gen"."main_java_package" IS 'java主包pacakge';

COMMENT ON COLUMN "public"."jb_code_gen"."service_name" IS 'Service Name';

COMMENT ON COLUMN "public"."jb_code_gen"."controller_package" IS 'Controller代码包';

COMMENT ON COLUMN "public"."jb_code_gen"."service_package" IS 'Service代码包';

COMMENT ON COLUMN "public"."jb_code_gen"."index_html_page_icon" IS 'index.html标题icon';

COMMENT ON COLUMN "public"."jb_code_gen"."index_html_page_title" IS 'index.html页面标题';

COMMENT ON COLUMN "public"."jb_code_gen"."model_package" IS 'model 所属package';

COMMENT ON COLUMN "public"."jb_code_gen"."html_view_path" IS 'html view path';

COMMENT ON COLUMN "public"."jb_code_gen"."routes_scan_package" IS '路由扫描包';

COMMENT ON COLUMN "public"."jb_code_gen"."is_gen_model" IS '是否需要生成Model';

COMMENT ON COLUMN "public"."jb_code_gen"."is_table_use_record" IS '表格是否使用record';

COMMENT ON COLUMN "public"."jb_code_gen"."is_table_record_camel_case" IS '表格列名用驼峰的attrName';

COMMENT ON COLUMN "public"."jb_code_gen"."is_import_excel" IS '是否支持Excel导入';

COMMENT ON COLUMN "public"."jb_code_gen"."is_export_excel" IS '是否支持Excel导出';

COMMENT ON COLUMN "public"."jb_code_gen"."is_export_excel_by_checked_ids" IS '是否启用 导出选中行功能';

COMMENT ON COLUMN "public"."jb_code_gen"."is_export_excel_by_form" IS '是否启用导出表单查询结果功能';

COMMENT ON COLUMN "public"."jb_code_gen"."is_export_excel_all" IS '是否启用导出所有数据';

COMMENT ON COLUMN "public"."jb_code_gen"."is_copy_to_excel" IS '是否支持表格复制到excel';

COMMENT ON COLUMN "public"."jb_code_gen"."is_copy_from_excel" IS '是否支持从excel复制到可编辑表格';

COMMENT ON COLUMN "public"."jb_code_gen"."is_toolbar" IS '是否使用toolbar模式';

COMMENT ON COLUMN "public"."jb_code_gen"."is_headbox" IS '是否使用headbox';

COMMENT ON COLUMN "public"."jb_code_gen"."is_leftbox" IS '是否使用leftBox';

COMMENT ON COLUMN "public"."jb_code_gen"."is_rightbox" IS '是否使用rightBox';

COMMENT ON COLUMN "public"."jb_code_gen"."is_footbox" IS '是否使用footbox';

COMMENT ON COLUMN "public"."jb_code_gen"."is_paginate" IS '是否分页查询';

COMMENT ON COLUMN "public"."jb_code_gen"."is_table_sortable_move" IS '是否开启移动排序功能';

COMMENT ON COLUMN "public"."jb_code_gen"."leftbox_width" IS 'leftbox width';

COMMENT ON COLUMN "public"."jb_code_gen"."rightbox_width" IS 'right width';

COMMENT ON COLUMN "public"."jb_code_gen"."headbox_height" IS 'headbox height';

COMMENT ON COLUMN "public"."jb_code_gen"."footbox_height" IS 'footbox height';

COMMENT ON COLUMN "public"."jb_code_gen"."is_leftbox_footer" IS '是否启用leftbox的footer';

COMMENT ON COLUMN "public"."jb_code_gen"."is_rightbox_footer" IS '是否启用rightbox的footer';

COMMENT ON COLUMN "public"."jb_code_gen"."leftbox_footer_button_count" IS 'leftbox footer button count';

COMMENT ON COLUMN "public"."jb_code_gen"."rightbox_footer_button_count" IS 'rightbox footer button count';

COMMENT ON COLUMN "public"."jb_code_gen"."leftbox_title" IS 'leftbox title';

COMMENT ON COLUMN "public"."jb_code_gen"."leftbox_icon" IS 'leftbox icon';

COMMENT ON COLUMN "public"."jb_code_gen"."rightbox_title" IS 'rightbox title';

COMMENT ON COLUMN "public"."jb_code_gen"."rightbox_icon" IS 'rightbox icon';

COMMENT ON COLUMN "public"."jb_code_gen"."is_show_optcol_sort" IS '是否开启操作列排序功能';

COMMENT ON COLUMN "public"."jb_code_gen"."is_show_optcol_edit" IS '是否开启操作列显示编辑按钮';

COMMENT ON COLUMN "public"."jb_code_gen"."is_show_optcol_del" IS '是否开启操作列显示删除按钮';

COMMENT ON COLUMN "public"."jb_code_gen"."is_show_optcol" IS '是否显示操作列';

COMMENT ON COLUMN "public"."jb_code_gen"."is_show_optcol_recover" IS '是否显示操作列的恢复按钮';

COMMENT ON COLUMN "public"."jb_code_gen"."default_sort_column" IS '默认排序字段';

COMMENT ON COLUMN "public"."jb_code_gen"."default_sort_type" IS '默认排序方式';

COMMENT ON COLUMN "public"."jb_code_gen"."table_optcol_width" IS '长度';

COMMENT ON COLUMN "public"."jb_code_gen"."is_table_column_resize" IS '表格是否开启调整列宽功能';

COMMENT ON COLUMN "public"."jb_code_gen"."is_table_prepend_column" IS '是否增加填充列';

COMMENT ON COLUMN "public"."jb_code_gen"."table_prepend_column_type" IS '表格chechbox radio配置类型';

COMMENT ON COLUMN "public"."jb_code_gen"."table_prepend_column_index" IS '填充列到第几列';

COMMENT ON COLUMN "public"."jb_code_gen"."is_table_prepend_column_linkparent" IS '填充列linkparent';

COMMENT ON COLUMN "public"."jb_code_gen"."is_table_prepend_column_linkson" IS '填充列linkson';

COMMENT ON COLUMN "public"."jb_code_gen"."table_prepend_column_rowspan" IS '填充列表头是几行rowspan';

COMMENT ON COLUMN "public"."jb_code_gen"."is_table_prepend_column_click_active" IS '是否点击行就切换列填充组件选中状态';

COMMENT ON COLUMN "public"."jb_code_gen"."table_fixed_column_left" IS '左侧固定列';

COMMENT ON COLUMN "public"."jb_code_gen"."table_fixed_column_right" IS '右侧固定列';

COMMENT ON COLUMN "public"."jb_code_gen"."table_pagesize_options" IS '分页pagesize自定义设置';

COMMENT ON COLUMN "public"."jb_code_gen"."table_width_assign" IS '表格宽度自定义值';

COMMENT ON COLUMN "public"."jb_code_gen"."table_width" IS '表格宽度';

COMMENT ON COLUMN "public"."jb_code_gen"."table_height_assign" IS '表格高度自定义值';

COMMENT ON COLUMN "public"."jb_code_gen"."table_height" IS '表格高度';

COMMENT ON COLUMN "public"."jb_code_gen"."table_default_sort_column" IS '表格默认排序字段';

COMMENT ON COLUMN "public"."jb_code_gen"."table_default_sort_type" IS '表格默认排序类型';

COMMENT ON COLUMN "public"."jb_code_gen"."is_keywords_search" IS '是否开启关键词查询';

COMMENT ON COLUMN "public"."jb_code_gen"."keywords_match_columns" IS '关键词匹配列';

COMMENT ON COLUMN "public"."jb_code_gen"."toolbar_extra_button_size" IS 'toolbar 额外预留按钮个数';

COMMENT ON COLUMN "public"."jb_code_gen"."is_deleted" IS '删除标识';

COMMENT ON COLUMN "public"."jb_code_gen"."form_column_size" IS '表单分几列';

COMMENT ON COLUMN "public"."jb_code_gen"."is_form_group_row" IS '表单form-group风格 左右还是上下';

COMMENT ON COLUMN "public"."jb_code_gen"."form_column_proportion" IS '表单分多列 比例值';

COMMENT ON COLUMN "public"."jb_code_gen"."form_column_direction" IS '表单列排布方向 横向还是纵向';

COMMENT ON COLUMN "public"."jb_code_gen"."form_group_proportion" IS 'form-group row状态下的比例';

COMMENT ON COLUMN "public"."jb_code_gen"."is_view_use_path" IS '是否启用Path注解路由';

COMMENT ON COLUMN "public"."jb_code_gen"."view_layout" IS '使用布局器';

COMMENT ON COLUMN "public"."jb_code_gen"."is_need_new_route" IS '是否需要创建新的路由配置类';

COMMENT ON COLUMN "public"."jb_code_gen"."routes_class_name" IS '哪个路由配置类';

COMMENT ON COLUMN "public"."jb_code_gen"."is_need_admin_interceptor" IS '是否需要后台管理权限拦截器';

COMMENT ON COLUMN "public"."jb_code_gen"."extra_interceptor_class_name" IS '额外配置的拦截器';

COMMENT ON COLUMN "public"."jb_code_gen"."is_table_multi_conditions_mode" IS '表格查询条件是否启用高级多条件模式';

COMMENT ON COLUMN "public"."jb_code_gen"."is_table_multi_conditions_default_hide" IS '表格查询高级模式 是否隐藏条件 默认隐藏';

COMMENT ON COLUMN "public"."jb_code_gen"."is_table_multi_conditions_btn_show_title" IS '表格高级查询条件切换按钮是否显示标题';

COMMENT ON COLUMN "public"."jb_code_gen"."is_toolbar_add_btn" IS '表格toolbar上启用添加按钮';

COMMENT ON COLUMN "public"."jb_code_gen"."is_toolbar_edit_btn" IS '表格toolbar上启用编辑按钮';

COMMENT ON COLUMN "public"."jb_code_gen"."is_toolbar_del_btn" IS '表格toolbar上启用删除按钮';

COMMENT ON COLUMN "public"."jb_code_gen"."is_toolbar_recover_btn" IS '表格toolbar上启用恢复按钮 当有is_deleted时';

COMMENT ON COLUMN "public"."jb_code_gen"."is_toolbar_refresh_btn" IS '表格tolbar上启用刷新按钮';

COMMENT ON COLUMN "public"."jb_code_gen"."is_page_title_add_btn" IS '是否启用pageTitle上的添加按钮';

COMMENT ON COLUMN "public"."jb_code_gen"."is_page_title_refresh_btn" IS '是否启用pageTitle上的刷新按钮';

COMMENT ON COLUMN "public"."jb_code_gen"."is_page_title_init_rank_btn" IS '是否启用pageTitle上的初始化顺序按钮';

COMMENT ON COLUMN "public"."jb_code_gen"."is_project_system_log" IS '是否启用systemLog日志';

COMMENT ON COLUMN "public"."jb_code_gen"."project_system_log_target_type_text" IS '系统日志text';

COMMENT ON COLUMN "public"."jb_code_gen"."project_system_log_target_type_value" IS '系统日志value值';

COMMENT ON COLUMN "public"."jb_code_gen"."project_system_log_target_type_key_name" IS '系统日志KeyName';

COMMENT ON COLUMN "public"."jb_code_gen"."form_dialog_area" IS 'form表单的dialog的area属性 长宽';

COMMENT ON COLUMN "public"."jb_code_gen"."is_base_model_gen_col_constant" IS '是否在baseModel中生成字段常量';

COMMENT ON COLUMN "public"."jb_code_gen"."is_base_model_gen_col_constant_to_uppercase" IS '是否在baseModel中生成的字段常量 名称转大写';

COMMENT ON TABLE "public"."jb_code_gen" IS '代码生成配置';


CREATE TABLE "public"."jb_code_gen_model_attr" (
"id" int8 NOT NULL,
"code_gen_id" int8 NOT NULL,
"col_name" varchar(40) COLLATE "pg_catalog"."default" NOT NULL,
"attr_name" varchar(40) COLLATE "pg_catalog"."default" NOT NULL,
"java_type" varchar(40) COLLATE "pg_catalog"."default" NOT NULL,
"attr_length" int4 NOT NULL,
"attr_fixed" int4,
"attr_default_value" varchar(40) COLLATE "pg_catalog"."default",
"sort_rank" int4 NOT NULL,
"sort_rank_intable" int4 NOT NULL,
"sort_rank_inform" int4 NOT NULL,
"sort_rank_insearch" int4 NOT NULL,
"is_pkey" char(1) COLLATE "pg_catalog"."default" NOT NULL,
"is_required" char(1) COLLATE "pg_catalog"."default" NOT NULL,
"is_search_required" char(1) COLLATE "pg_catalog"."default" NOT NULL,
"data_rule_for_search" varchar(255) COLLATE "pg_catalog"."default",
"data_tips_for_search" varchar(255) COLLATE "pg_catalog"."default",
"form_label" varchar(255) COLLATE "pg_catalog"."default",
"placeholder" varchar(40) COLLATE "pg_catalog"."default",
"table_label" varchar(40) COLLATE "pg_catalog"."default",
"search_form_label" varchar(40) COLLATE "pg_catalog"."default",
"remark" varchar(255) COLLATE "pg_catalog"."default",
"is_keywords_column" char(1) COLLATE "pg_catalog"."default" NOT NULL,
"is_form_ele" char(1) COLLATE "pg_catalog"."default" NOT NULL,
"is_table_col" char(1) COLLATE "pg_catalog"."default" NOT NULL,
"is_table_switchbtn" char(1) COLLATE "pg_catalog"."default" NOT NULL,
"table_col_width" int4 NOT NULL,
"is_need_fixed_width" char(1) COLLATE "pg_catalog"."default" NOT NULL,
"is_search_ele" char(1) COLLATE "pg_catalog"."default" NOT NULL,
"is_search_hidden" char(1) COLLATE "pg_catalog"."default" NOT NULL,
"col_format" varchar(255) COLLATE "pg_catalog"."default",
"search_ui_type" varchar(40) COLLATE "pg_catalog"."default",
"search_data_type" varchar(40) COLLATE "pg_catalog"."default",
"search_data_value" varchar(255) COLLATE "pg_catalog"."default",
"search_default_value" varchar(255) COLLATE "pg_catalog"."default",
"is_single_line" char(1) COLLATE "pg_catalog"."default" NOT NULL,
"need_data_handler" char(1) COLLATE "pg_catalog"."default",
"form_ui_type" varchar(40) COLLATE "pg_catalog"."default",
"form_jboltinput_filter_handler" varchar(60) COLLATE "pg_catalog"."default",
"is_form_jboltinput_jstree_checkbox" char(1) COLLATE "pg_catalog"."default",
"is_form_jboltinput_jstree_only_leaf" char(1) COLLATE "pg_catalog"."default",
"form_data_type" varchar(40) COLLATE "pg_catalog"."default",
"form_data_value" varchar(255) COLLATE "pg_catalog"."default",
"form_default_value" varchar(255) COLLATE "pg_catalog"."default",
"data_rule_assign" varchar(255) COLLATE "pg_catalog"."default",
"data_rule" varchar(255) COLLATE "pg_catalog"."default",
"data_tips" varchar(255) COLLATE "pg_catalog"."default",
"is_import_col" char(1) COLLATE "pg_catalog"."default" NOT NULL,
"is_export_col" char(1) COLLATE "pg_catalog"."default" NOT NULL,
"is_sortable" char(1) COLLATE "pg_catalog"."default" NOT NULL,
"table_ui_type" varchar(40) COLLATE "pg_catalog"."default",
"table_data_type" varchar(40) COLLATE "pg_catalog"."default",
"table_data_value" varchar(255) COLLATE "pg_catalog"."default",
"form_ele_width" int4 NOT NULL,
"is_item_inline" char(1) COLLATE "pg_catalog"."default" NOT NULL,
"form_data_text_attr" varchar(255) COLLATE "pg_catalog"."default",
"form_data_value_attr" varchar(40) COLLATE "pg_catalog"."default",
"form_data_column_attr" varchar(255) COLLATE "pg_catalog"."default",
"search_data_text_attr" varchar(255) COLLATE "pg_catalog"."default",
"search_data_value_attr" varchar(40) COLLATE "pg_catalog"."default",
"search_data_column_attr" varchar(255) COLLATE "pg_catalog"."default",
"table_data_text_attr" varchar(255) COLLATE "pg_catalog"."default",
"table_data_value_attr" varchar(40) COLLATE "pg_catalog"."default",
"table_data_column_attr" varchar(255) COLLATE "pg_catalog"."default",
"is_need_translate" char(1) COLLATE "pg_catalog"."default" NOT NULL,
"translate_type" varchar(40) COLLATE "pg_catalog"."default",
"translate_use_value" varchar(250) COLLATE "pg_catalog"."default",
"translate_col_name" varchar(250) COLLATE "pg_catalog"."default",
"is_upload_to_qiniu" char(1) COLLATE "pg_catalog"."default" NOT NULL,
"form_upload_url" varchar(255) COLLATE "pg_catalog"."default",
"form_img_uploader_area" varchar(20) COLLATE "pg_catalog"."default",
"form_maxsize" int4,
"qiniu_bucket_sn" varchar(60) COLLATE "pg_catalog"."default",
"qiniu_file_key" varchar(100) COLLATE "pg_catalog"."default",
CONSTRAINT "jb_code_gen_model_attr_pkey" PRIMARY KEY ("id")
)
;

ALTER TABLE "public"."jb_code_gen_model_attr"
    OWNER TO "postgres";

COMMENT ON COLUMN "public"."jb_code_gen_model_attr"."id" IS '主键ID';

COMMENT ON COLUMN "public"."jb_code_gen_model_attr"."code_gen_id" IS '所属codeGen';

COMMENT ON COLUMN "public"."jb_code_gen_model_attr"."col_name" IS '列名';

COMMENT ON COLUMN "public"."jb_code_gen_model_attr"."attr_name" IS '属性名';

COMMENT ON COLUMN "public"."jb_code_gen_model_attr"."java_type" IS '属性类型';

COMMENT ON COLUMN "public"."jb_code_gen_model_attr"."attr_length" IS '属性长度';

COMMENT ON COLUMN "public"."jb_code_gen_model_attr"."attr_fixed" IS '属性小数点';

COMMENT ON COLUMN "public"."jb_code_gen_model_attr"."attr_default_value" IS '默认值';

COMMENT ON COLUMN "public"."jb_code_gen_model_attr"."sort_rank" IS '数据表内默认顺序';

COMMENT ON COLUMN "public"."jb_code_gen_model_attr"."sort_rank_intable" IS '表格中的排序';

COMMENT ON COLUMN "public"."jb_code_gen_model_attr"."sort_rank_inform" IS '表单中的排序';

COMMENT ON COLUMN "public"."jb_code_gen_model_attr"."sort_rank_insearch" IS '查询条件中的顺序';

COMMENT ON COLUMN "public"."jb_code_gen_model_attr"."is_pkey" IS '是否主键';

COMMENT ON COLUMN "public"."jb_code_gen_model_attr"."is_required" IS '是否必填';

COMMENT ON COLUMN "public"."jb_code_gen_model_attr"."is_search_required" IS '作为查询条件是否必填';

COMMENT ON COLUMN "public"."jb_code_gen_model_attr"."data_rule_for_search" IS '查询条件必填校验规则';

COMMENT ON COLUMN "public"."jb_code_gen_model_attr"."data_tips_for_search" IS '查询条件不符合校验的提示信息';

COMMENT ON COLUMN "public"."jb_code_gen_model_attr"."form_label" IS '表单单显示文本';

COMMENT ON COLUMN "public"."jb_code_gen_model_attr"."placeholder" IS '表单placeholder';

COMMENT ON COLUMN "public"."jb_code_gen_model_attr"."table_label" IS '表格中显示文本';

COMMENT ON COLUMN "public"."jb_code_gen_model_attr"."search_form_label" IS '查询表单提示文本';

COMMENT ON COLUMN "public"."jb_code_gen_model_attr"."remark" IS '备注';

COMMENT ON COLUMN "public"."jb_code_gen_model_attr"."is_keywords_column" IS '是否为关键词查询列';

COMMENT ON COLUMN "public"."jb_code_gen_model_attr"."is_form_ele" IS '是否表单可编辑元素';

COMMENT ON COLUMN "public"."jb_code_gen_model_attr"."is_table_col" IS '是否表格列';

COMMENT ON COLUMN "public"."jb_code_gen_model_attr"."is_table_switchbtn" IS '是否为表格switchbtn';

COMMENT ON COLUMN "public"."jb_code_gen_model_attr"."table_col_width" IS '列宽';

COMMENT ON COLUMN "public"."jb_code_gen_model_attr"."is_need_fixed_width" IS '是否固定宽度';

COMMENT ON COLUMN "public"."jb_code_gen_model_attr"."is_search_ele" IS '是否检索条件';

COMMENT ON COLUMN "public"."jb_code_gen_model_attr"."is_search_hidden" IS '是否为检索隐藏条件';

COMMENT ON COLUMN "public"."jb_code_gen_model_attr"."col_format" IS '格式化操作值';

COMMENT ON COLUMN "public"."jb_code_gen_model_attr"."search_ui_type" IS '查询用ui 组件类型';

COMMENT ON COLUMN "public"."jb_code_gen_model_attr"."search_data_type" IS '查询用组件数据源类型';

COMMENT ON COLUMN "public"."jb_code_gen_model_attr"."search_data_value" IS '查询用组件数据值';

COMMENT ON COLUMN "public"."jb_code_gen_model_attr"."search_default_value" IS '查询用组件默认值';

COMMENT ON COLUMN "public"."jb_code_gen_model_attr"."is_single_line" IS '独立新行';

COMMENT ON COLUMN "public"."jb_code_gen_model_attr"."need_data_handler" IS '是否需要data_handler';

COMMENT ON COLUMN "public"."jb_code_gen_model_attr"."form_ui_type" IS '表单组件类型';

COMMENT ON COLUMN "public"."jb_code_gen_model_attr"."form_jboltinput_filter_handler" IS 'jboltinput filter handler';

COMMENT ON COLUMN "public"."jb_code_gen_model_attr"."is_form_jboltinput_jstree_checkbox" IS 'jboltinput jstree是否有checkbox';

COMMENT ON COLUMN "public"."jb_code_gen_model_attr"."is_form_jboltinput_jstree_only_leaf" IS 'jboltinput jstree checkbox只选子节点';

COMMENT ON COLUMN "public"."jb_code_gen_model_attr"."form_data_type" IS '表单组件数据源类型';

COMMENT ON COLUMN "public"."jb_code_gen_model_attr"."form_data_value" IS '表单组件数据值';

COMMENT ON COLUMN "public"."jb_code_gen_model_attr"."form_default_value" IS '表单组件默认值';

COMMENT ON COLUMN "public"."jb_code_gen_model_attr"."data_rule_assign" IS '表单校验规则 自定义';

COMMENT ON COLUMN "public"."jb_code_gen_model_attr"."data_rule" IS '校验规则';

COMMENT ON COLUMN "public"."jb_code_gen_model_attr"."data_tips" IS '校验提示信息';

COMMENT ON COLUMN "public"."jb_code_gen_model_attr"."is_import_col" IS '是否为导入列';

COMMENT ON COLUMN "public"."jb_code_gen_model_attr"."is_export_col" IS '导出列';

COMMENT ON COLUMN "public"."jb_code_gen_model_attr"."is_sortable" IS '是否可排序';

COMMENT ON COLUMN "public"."jb_code_gen_model_attr"."table_ui_type" IS '可编辑表格显示组件类型';

COMMENT ON COLUMN "public"."jb_code_gen_model_attr"."table_data_type" IS '表格组件数据库类型';

COMMENT ON COLUMN "public"."jb_code_gen_model_attr"."table_data_value" IS '表格组件数据值';

COMMENT ON COLUMN "public"."jb_code_gen_model_attr"."form_ele_width" IS '组件自定义宽度';

COMMENT ON COLUMN "public"."jb_code_gen_model_attr"."is_item_inline" IS 'radio checkbox等是否inline';

COMMENT ON COLUMN "public"."jb_code_gen_model_attr"."form_data_text_attr" IS 'data-text-attr';

COMMENT ON COLUMN "public"."jb_code_gen_model_attr"."form_data_value_attr" IS 'data-value-attr';

COMMENT ON COLUMN "public"."jb_code_gen_model_attr"."form_data_column_attr" IS 'data-column-attr';

COMMENT ON COLUMN "public"."jb_code_gen_model_attr"."search_data_text_attr" IS 'data-text-attr';

COMMENT ON COLUMN "public"."jb_code_gen_model_attr"."search_data_value_attr" IS 'data-value-attr';

COMMENT ON COLUMN "public"."jb_code_gen_model_attr"."search_data_column_attr" IS 'data-column-attr';

COMMENT ON COLUMN "public"."jb_code_gen_model_attr"."table_data_text_attr" IS 'data-text-attr';

COMMENT ON COLUMN "public"."jb_code_gen_model_attr"."table_data_value_attr" IS 'data-value-attr';

COMMENT ON COLUMN "public"."jb_code_gen_model_attr"."table_data_column_attr" IS 'data-column-attr';

COMMENT ON COLUMN "public"."jb_code_gen_model_attr"."is_need_translate" IS '是否需要翻译';

COMMENT ON COLUMN "public"."jb_code_gen_model_attr"."translate_type" IS '翻译类型';

COMMENT ON COLUMN "public"."jb_code_gen_model_attr"."translate_use_value" IS '翻译用值';

COMMENT ON COLUMN "public"."jb_code_gen_model_attr"."translate_col_name" IS '翻译后的列名';

COMMENT ON COLUMN "public"."jb_code_gen_model_attr"."is_upload_to_qiniu" IS '是否上传到七牛';

COMMENT ON COLUMN "public"."jb_code_gen_model_attr"."form_upload_url" IS '上传地址';

COMMENT ON COLUMN "public"."jb_code_gen_model_attr"."form_img_uploader_area" IS '上传组件area';

COMMENT ON COLUMN "public"."jb_code_gen_model_attr"."form_maxsize" IS '上传尺寸限制';

COMMENT ON COLUMN "public"."jb_code_gen_model_attr"."qiniu_bucket_sn" IS '七牛bucket sn';

COMMENT ON COLUMN "public"."jb_code_gen_model_attr"."qiniu_file_key" IS '七牛file key';

COMMENT ON TABLE "public"."jb_code_gen_model_attr" IS 'CodeGen模型详细设计';

CREATE TABLE "public"."jb_datasource_filter" (
"id" int8 NOT NULL,
"name" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
"config_name" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
"table_name_filter" text COLLATE "pg_catalog"."default",
"table_name_contains" text COLLATE "pg_catalog"."default",
"table_name_patterns" text COLLATE "pg_catalog"."default",
"create_user_id" int8 NOT NULL,
"update_user_id" int8 NOT NULL,
"create_time" timestamp(6) NOT NULL,
"update_time" timestamp(6) NOT NULL,
CONSTRAINT "jb_datasource_filter_pkey" PRIMARY KEY ("id")
)
;

ALTER TABLE "public"."jb_datasource_filter"
    OWNER TO "postgres";

COMMENT ON COLUMN "public"."jb_datasource_filter"."id" IS '主键ID';

COMMENT ON COLUMN "public"."jb_datasource_filter"."name" IS '名称';

COMMENT ON COLUMN "public"."jb_datasource_filter"."config_name" IS '配置名称';

COMMENT ON COLUMN "public"."jb_datasource_filter"."table_name_filter" IS '不需要生成的表名';

COMMENT ON COLUMN "public"."jb_datasource_filter"."table_name_contains" IS '需要排除包含字符';

COMMENT ON COLUMN "public"."jb_datasource_filter"."table_name_patterns" IS '需要排除符合正则的';

COMMENT ON COLUMN "public"."jb_datasource_filter"."create_user_id" IS '创建人';

COMMENT ON COLUMN "public"."jb_datasource_filter"."update_user_id" IS '更新人';

COMMENT ON COLUMN "public"."jb_datasource_filter"."create_time" IS '创建时间';

COMMENT ON COLUMN "public"."jb_datasource_filter"."update_time" IS '更新时间';

COMMENT ON TABLE "public"."jb_datasource_filter" IS '数据源过滤配置';