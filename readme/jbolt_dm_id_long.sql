CREATE TABLE "JBOLT"."jb_application"
(
    "id" BIGINT NOT NULL,
    "name" VARCHAR(255) NOT NULL,
    "brief_info" VARCHAR(255),
    "app_id" VARCHAR(255),
    "app_secret" VARCHAR(255),
    "enable" CHAR(1) DEFAULT '0' NOT NULL,
    "create_time" TIMESTAMP(0),
    "update_time" TIMESTAMP(0),
    "user_id" BIGINT,
    "update_user_id" BIGINT,
    "type" INT NOT NULL,
    "need_check_sign" CHAR(1) DEFAULT '0',
    "link_target_id" BIGINT,
    "is_inner" CHAR(1) DEFAULT '0' NOT NULL,
    NOT CLUSTER PRIMARY KEY("id")) STORAGE(ON "MAIN", CLUSTERBTR) ;

COMMENT ON TABLE "JBOLT"."jb_application" IS 'API应用中心的应用APP';
COMMENT ON COLUMN "JBOLT"."jb_application"."app_id" IS '应用ID';
COMMENT ON COLUMN "JBOLT"."jb_application"."app_secret" IS '应用密钥';
COMMENT ON COLUMN "JBOLT"."jb_application"."brief_info" IS '应用简介';
COMMENT ON COLUMN "JBOLT"."jb_application"."create_time" IS '创建时间';
COMMENT ON COLUMN "JBOLT"."jb_application"."enable" IS '是否启用';
COMMENT ON COLUMN "JBOLT"."jb_application"."id" IS '主键ID';
COMMENT ON COLUMN "JBOLT"."jb_application"."is_inner" IS '是否内置';
COMMENT ON COLUMN "JBOLT"."jb_application"."link_target_id" IS '关联目标ID';
COMMENT ON COLUMN "JBOLT"."jb_application"."name" IS '应用名称';
COMMENT ON COLUMN "JBOLT"."jb_application"."need_check_sign" IS '是否需要接口校验SIGN';
COMMENT ON COLUMN "JBOLT"."jb_application"."type" IS 'app类型';
COMMENT ON COLUMN "JBOLT"."jb_application"."update_time" IS '更新时间';
COMMENT ON COLUMN "JBOLT"."jb_application"."update_user_id" IS '更新用户ID';
COMMENT ON COLUMN "JBOLT"."jb_application"."user_id" IS '创建用户ID';


CREATE UNIQUE  INDEX "INDEX309699929755000" ON "JBOLT"."jb_application"("id" ASC) STORAGE(ON "MAIN", CLUSTERBTR) ;

CREATE TABLE "JBOLT"."jb_code_gen"
(
    "id" BIGINT NOT NULL,
    "pid" BIGINT DEFAULT 0,
    "project_path" VARCHAR(255) NOT NULL,
    "is_sub_table" CHAR(1) DEFAULT '0' NOT NULL,
    "sort_rank" INT DEFAULT 1 NOT NULL,
    "type" VARCHAR(40) NOT NULL,
    "main_table_name" VARCHAR(150) NOT NULL,
    "datasource_name" VARCHAR(100) NOT NULL,
    "datasource_remark" VARCHAR(40) NOT NULL,
    "database_type" VARCHAR(40) NOT NULL,
    "is_main_datasource" CHAR(1) DEFAULT '0' NOT NULL,
    "main_table_pkey" VARCHAR(64),
    "table_remove_prefix" VARCHAR(40),
    "main_table_idgenmode" VARCHAR(40) NOT NULL,
    "version_sn" INT NOT NULL,
    "main_table_remark" VARCHAR(100),
    "author" VARCHAR(100) NOT NULL,
    "style" INT NOT NULL,
    "is_crud" CHAR(1) DEFAULT '0' NOT NULL,
    "is_editable" CHAR(1) DEFAULT '0' NOT NULL,
    "is_tree_table" CHAR(1) DEFAULT '0' NOT NULL,
    "is_check_can_delete" CHAR(1) DEFAULT '0' NOT NULL,
    "is_check_can_toggle" CHAR(1) DEFAULT '0' NOT NULL,
    "is_check_can_recover" CHAR(1) DEFAULT '0' NOT NULL,
    "editable_submit_type" VARCHAR(20),
    "state" INT NOT NULL,
    "sub_table_count" INT,
    "topnav_id" BIGINT,
    "permission_id" BIGINT,
    "roles" VARCHAR(255),
    "create_user_id" BIGINT NOT NULL,
    "create_time" TIMESTAMP(0) NOT NULL,
    "update_user_id" BIGINT NOT NULL,
    "update_time" TIMESTAMP(0) NOT NULL,
    "gen_user_id" BIGINT,
    "gen_time" TIMESTAMP(0),
    "model_name" VARCHAR(64),
    "base_model_name" VARCHAR(64),
    "remark" VARCHAR(255),
    "is_auto_cache" CHAR(1) DEFAULT '0' NOT NULL,
    "is_id_cache" CHAR(1) DEFAULT '0' NOT NULL,
    "is_key_cache" CHAR(1) DEFAULT '0' NOT NULL,
    "key_cache_column" VARCHAR(40),
    "key_cache_bind_column" VARCHAR(40),
    "controller_name" VARCHAR(100) NOT NULL,
    "controller_path" VARCHAR(100) NOT NULL,
    "main_java_package" VARCHAR(100) NOT NULL,
    "service_name" VARCHAR(100) NOT NULL,
    "controller_package" VARCHAR(255) NOT NULL,
    "service_package" VARCHAR(255) NOT NULL,
    "index_html_page_icon" VARCHAR(60),
    "index_html_page_title" VARCHAR(40),
    "model_package" VARCHAR(255),
    "html_view_path" VARCHAR(255),
    "routes_scan_package" VARCHAR(255),
    "is_gen_model" CHAR(1) DEFAULT '1' NOT NULL,
    "is_table_use_record" CHAR(1) DEFAULT '0' NOT NULL,
    "is_table_record_camel_case" CHAR(1) DEFAULT '0' NOT NULL,
    "is_import_excel" CHAR(1) DEFAULT '0' NOT NULL,
    "is_export_excel" CHAR(1) DEFAULT '0' NOT NULL,
    "is_export_excel_by_checked_ids" CHAR(1) DEFAULT '0' NOT NULL,
    "is_export_excel_by_form" CHAR(1) DEFAULT '1' NOT NULL,
    "is_export_excel_all" CHAR(1) DEFAULT '0' NOT NULL,
    "is_copy_to_excel" CHAR(1) DEFAULT '0' NOT NULL,
    "is_copy_from_excel" CHAR(1) DEFAULT '0' NOT NULL,
    "is_toolbar" CHAR(1) DEFAULT '0' NOT NULL,
    "is_headbox" CHAR(1) DEFAULT '0' NOT NULL,
    "is_leftbox" CHAR(1) DEFAULT '0' NOT NULL,
    "is_rightbox" CHAR(1) DEFAULT '0' NOT NULL,
    "is_footbox" CHAR(1) DEFAULT '0' NOT NULL,
    "is_paginate" CHAR(1) DEFAULT '1' NOT NULL,
    "is_table_sortable_move" CHAR(1) DEFAULT '0' NOT NULL,
    "leftbox_width" INT DEFAULT 220,
    "rightbox_width" INT DEFAULT 220,
    "headbox_height" INT DEFAULT 60,
    "footbox_height" INT DEFAULT 220,
    "is_leftbox_footer" CHAR(1) DEFAULT '1' NOT NULL,
    "is_rightbox_footer" CHAR(1) DEFAULT '1' NOT NULL,
    "leftbox_footer_button_count" INT,
    "rightbox_footer_button_count" INT,
    "leftbox_title" VARCHAR(40) DEFAULT 'leftbox',
    "leftbox_icon" VARCHAR(60) DEFAULT 'fa fa-cog',
    "rightbox_title" VARCHAR(40) DEFAULT 'rightbox',
    "rightbox_icon" VARCHAR(60) DEFAULT 'fa fa-cog',
    "is_show_optcol_sort" CHAR(1) DEFAULT '0' NOT NULL,
    "is_show_optcol_edit" CHAR(1) DEFAULT '0' NOT NULL,
    "is_show_optcol_del" CHAR(1) DEFAULT '0' NOT NULL,
    "is_show_optcol" CHAR(1) DEFAULT '1' NOT NULL,
    "is_show_optcol_recover" CHAR(1) DEFAULT '0' NOT NULL,
    "default_sort_column" VARCHAR(40),
    "default_sort_type" VARCHAR(40),
    "table_optcol_width" INT DEFAULT 80 NOT NULL,
    "is_table_column_resize" CHAR(1) DEFAULT '1' NOT NULL,
    "is_table_prepend_column" CHAR(1) DEFAULT '0' NOT NULL,
    "table_prepend_column_type" VARCHAR(40),
    "table_prepend_column_index" INT DEFAULT 1 NOT NULL,
    "is_table_prepend_column_linkparent" CHAR(1) DEFAULT '0' NOT NULL,
    "is_table_prepend_column_linkson" CHAR(1) DEFAULT '0' NOT NULL,
    "table_prepend_column_rowspan" INT DEFAULT 1 NOT NULL,
    "is_table_prepend_column_click_active" CHAR(1) DEFAULT '0' NOT NULL,
    "table_fixed_column_left" VARCHAR(40),
    "table_fixed_column_right" VARCHAR(40),
    "table_pagesize_options" VARCHAR(255),
    "table_width_assign" VARCHAR(40),
    "table_width" VARCHAR(40) DEFAULT 'fill' NOT NULL,
    "table_height_assign" VARCHAR(40),
    "table_height" VARCHAR(40) DEFAULT 'fill' NOT NULL,
    "table_default_sort_column" VARCHAR(60) DEFAULT 'id',
    "table_default_sort_type" VARCHAR(40) DEFAULT 'desc',
    "is_keywords_search" CHAR(1) DEFAULT '1' NOT NULL,
    "keywords_match_columns" VARCHAR(255),
    "toolbar_extra_button_size" INT,
    "is_deleted" CHAR(1) DEFAULT '0' NOT NULL,
    "form_column_size" INT DEFAULT 1 NOT NULL,
    "is_form_group_row" CHAR(1) DEFAULT '1' NOT NULL,
    "form_column_proportion" VARCHAR(40),
    "form_column_direction" VARCHAR(10) DEFAULT 'v',
    "form_group_proportion" VARCHAR(40) DEFAULT '2:10',
    "is_view_use_path" CHAR(1) DEFAULT '1' NOT NULL,
    "view_layout" VARCHAR(255) NOT NULL,
    "is_need_new_route" CHAR(1) DEFAULT '0' NOT NULL,
    "routes_class_name" VARCHAR(255),
    "is_need_admin_interceptor" CHAR(1) DEFAULT '1' NOT NULL,
    "extra_interceptor_class_name" VARCHAR(255),
    "is_table_multi_conditions_mode" CHAR(1) DEFAULT '0' NOT NULL,
    "is_table_multi_conditions_default_hide" CHAR(1) DEFAULT '1' NOT NULL,
    "is_table_multi_conditions_btn_show_title" CHAR(1) DEFAULT '1' NOT NULL,
    "is_toolbar_add_btn" CHAR(1) DEFAULT '1' NOT NULL,
    "is_toolbar_edit_btn" CHAR(1) DEFAULT '1' NOT NULL,
    "is_toolbar_del_btn" CHAR(1) DEFAULT '1' NOT NULL,
    "is_toolbar_recover_btn" CHAR(1) DEFAULT '0' NOT NULL,
    "is_toolbar_refresh_btn" CHAR(1) DEFAULT '1' NOT NULL,
    "is_page_title_add_btn" CHAR(1) DEFAULT '1' NOT NULL,
    "is_page_title_refresh_btn" CHAR(1) DEFAULT '1' NOT NULL,
    "is_page_title_init_rank_btn" CHAR(1) DEFAULT '0' NOT NULL,
    "is_project_system_log" CHAR(1) DEFAULT '0' NOT NULL,
    "project_system_log_target_type_text" VARCHAR(20),
    "project_system_log_target_type_value" VARCHAR(10),
    "project_system_log_target_type_key_name" VARCHAR(40),
    "form_dialog_area" VARCHAR(20) DEFAULT '800,600',
    "is_base_model_gen_col_constant" CHAR(1) DEFAULT '1' NOT NULL,
    "is_base_model_gen_col_constant_to_uppercase" CHAR(1) DEFAULT '1' NOT NULL,
    NOT CLUSTER PRIMARY KEY("id")) STORAGE(ON "MAIN", CLUSTERBTR) ;

COMMENT ON TABLE "JBOLT"."jb_code_gen" IS '代码生成配置';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."author" IS '功能作者';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."base_model_name" IS 'baseModelName';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."controller_name" IS 'Controller Name';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."controller_package" IS 'Controller代码包';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."controller_path" IS 'controller path';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."create_time" IS '创建时间';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."create_user_id" IS '创建人ID';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."database_type" IS '数据库类型';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."datasource_name" IS '数据源';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."datasource_remark" IS '数据源说明';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."default_sort_column" IS '默认排序字段';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."default_sort_type" IS '默认排序方式';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."editable_submit_type" IS '可编辑提交方式';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."extra_interceptor_class_name" IS '额外配置的拦截器';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."footbox_height" IS 'footbox height';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."form_column_direction" IS '表单列排布方向 横向还是纵向';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."form_column_proportion" IS '表单分多列 比例值';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."form_column_size" IS '表单分几列';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."form_dialog_area" IS 'form表单的dialog的area属性 长宽';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."form_group_proportion" IS 'form-group row状态下的比例';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."gen_time" IS '创建后执行生成时间';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."gen_user_id" IS '创建后执行生成人ID';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."headbox_height" IS 'headbox height';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."html_view_path" IS 'html view path';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."id" IS '主键ID';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."index_html_page_icon" IS 'index.html标题icon';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."index_html_page_title" IS 'index.html页面标题';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."is_auto_cache" IS '是否启用autoCache';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."is_base_model_gen_col_constant" IS '是否在baseModel中生成字段常量';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."is_base_model_gen_col_constant_to_uppercase" IS '是否在baseModel中生成的字段常量 名称转大写';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."is_check_can_delete" IS '检测是否可以刪除数据';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."is_check_can_recover" IS '检测是否可以recover数据';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."is_check_can_toggle" IS '检测是否可以toggle数据';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."is_copy_from_excel" IS '是否支持从excel复制到可编辑表格';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."is_copy_to_excel" IS '是否支持表格复制到excel';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."is_crud" IS '是否CRUD';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."is_deleted" IS '删除标识';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."is_editable" IS '是否可编辑表格';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."is_export_excel" IS '是否支持Excel导出';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."is_export_excel_all" IS '是否启用导出所有数据';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."is_export_excel_by_checked_ids" IS '是否启用 导出选中行功能';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."is_export_excel_by_form" IS '是否启用导出表单查询结果功能';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."is_footbox" IS '是否使用footbox';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."is_form_group_row" IS '表单form-group风格 左右还是上下';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."is_gen_model" IS '是否需要生成Model';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."is_headbox" IS '是否使用headbox';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."is_id_cache" IS '是否启用idCache';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."is_import_excel" IS '是否支持Excel导入';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."is_key_cache" IS '是否启用keyCache';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."is_keywords_search" IS '是否开启关键词查询';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."is_leftbox" IS '是否使用leftBox';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."is_leftbox_footer" IS '是否启用leftbox的footer';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."is_main_datasource" IS '是否为主数据源';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."is_need_admin_interceptor" IS '是否需要后台管理权限拦截器';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."is_need_new_route" IS '是否需要创建新的路由配置类';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."is_page_title_add_btn" IS '是否启用pageTitle上的添加按钮';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."is_page_title_init_rank_btn" IS '是否启用pageTitle上的初始化顺序按钮';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."is_page_title_refresh_btn" IS '是否启用pageTitle上的刷新按钮';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."is_paginate" IS '是否分页查询';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."is_project_system_log" IS '是否启用systemLog日志';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."is_rightbox" IS '是否使用rightBox';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."is_rightbox_footer" IS '是否启用rightbox的footer';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."is_show_optcol" IS '是否显示操作列';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."is_show_optcol_del" IS '是否开启操作列显示删除按钮';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."is_show_optcol_edit" IS '是否开启操作列显示编辑按钮';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."is_show_optcol_recover" IS '是否显示操作列的恢复按钮';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."is_show_optcol_sort" IS '是否开启操作列排序功能';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."is_sub_table" IS '是否为子表';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."is_table_column_resize" IS '表格是否开启调整列宽功能';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."is_table_multi_conditions_btn_show_title" IS '表格高级查询条件切换按钮是否显示标题';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."is_table_multi_conditions_default_hide" IS '表格查询高级模式 是否隐藏条件 默认隐藏';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."is_table_multi_conditions_mode" IS '表格查询条件是否启用高级多条件模式';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."is_table_prepend_column" IS '是否增加填充列';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."is_table_prepend_column_click_active" IS '是否点击行就切换列填充组件选中状态';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."is_table_prepend_column_linkparent" IS '填充列linkparent';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."is_table_prepend_column_linkson" IS '填充列linkson';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."is_table_record_camel_case" IS '表格列名用驼峰的attrName';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."is_table_sortable_move" IS '是否开启移动排序功能';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."is_table_use_record" IS '表格是否使用record';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."is_toolbar" IS '是否使用toolbar模式';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."is_toolbar_add_btn" IS '表格toolbar上启用添加按钮';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."is_toolbar_del_btn" IS '表格toolbar上启用删除按钮';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."is_toolbar_edit_btn" IS '表格toolbar上启用编辑按钮';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."is_toolbar_recover_btn" IS '表格toolbar上启用恢复按钮 当有is_deleted时';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."is_toolbar_refresh_btn" IS '表格tolbar上启用刷新按钮';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."is_tree_table" IS '是否为树表';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."is_view_use_path" IS '是否启用Path注解路由';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."key_cache_bind_column" IS 'keyCache指定bindColumn';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."key_cache_column" IS 'keyCache指定Column';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."keywords_match_columns" IS '关键词匹配列';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."leftbox_footer_button_count" IS 'leftbox footer button count';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."leftbox_icon" IS 'leftbox icon';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."leftbox_title" IS 'leftbox title';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."leftbox_width" IS 'leftbox width';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."main_java_package" IS 'java主包pacakge';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."main_table_idgenmode" IS '主表主键策略';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."main_table_name" IS '主表名';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."main_table_pkey" IS '主表主键';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."main_table_remark" IS '表备注';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."model_name" IS 'modelName';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."model_package" IS 'model 所属package';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."permission_id" IS '关联权限';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."pid" IS '父ID';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."project_path" IS '项目根路径';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."project_system_log_target_type_key_name" IS '系统日志KeyName';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."project_system_log_target_type_text" IS '系统日志text';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."project_system_log_target_type_value" IS '系统日志value值';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."remark" IS '备注';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."rightbox_footer_button_count" IS 'rightbox footer button count';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."rightbox_icon" IS 'rightbox icon';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."rightbox_title" IS 'rightbox title';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."rightbox_width" IS 'right width';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."roles" IS '可访问角色';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."routes_class_name" IS '哪个路由配置类';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."routes_scan_package" IS '路由扫描包';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."service_name" IS 'Service Name';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."service_package" IS 'Service代码包';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."sort_rank" IS '子表的顺序';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."state" IS '生成状态';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."style" IS '样式类型';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."sub_table_count" IS '子表数';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."table_default_sort_column" IS '表格默认排序字段';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."table_default_sort_type" IS '表格默认排序类型';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."table_fixed_column_left" IS '左侧固定列';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."table_fixed_column_right" IS '右侧固定列';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."table_height" IS '表格高度';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."table_height_assign" IS '表格高度自定义值';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."table_optcol_width" IS '长度';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."table_pagesize_options" IS '分页pagesize自定义设置';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."table_prepend_column_index" IS '填充列到第几列';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."table_prepend_column_rowspan" IS '填充列表头是几行rowspan';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."table_prepend_column_type" IS '表格chechbox radio配置类型';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."table_remove_prefix" IS '数据表删除前缀';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."table_width" IS '表格宽度';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."table_width_assign" IS '表格宽度自定义值';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."toolbar_extra_button_size" IS 'toolbar 额外预留按钮个数';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."topnav_id" IS '顶部导航';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."type" IS '模块类型';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."update_time" IS '更新时间';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."update_user_id" IS '更新人ID';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."version_sn" IS '版本序号';
COMMENT ON COLUMN "JBOLT"."jb_code_gen"."view_layout" IS '使用布局器';


CREATE UNIQUE  INDEX "INDEX309699739282000" ON "JBOLT"."jb_code_gen"("id" ASC) STORAGE(ON "MAIN", CLUSTERBTR) ;

CREATE TABLE "JBOLT"."jb_code_gen_model_attr"
(
    "id" BIGINT NOT NULL,
    "code_gen_id" BIGINT NOT NULL,
    "col_name" VARCHAR(40) NOT NULL,
    "attr_name" VARCHAR(40) NOT NULL,
    "java_type" VARCHAR(40) NOT NULL,
    "attr_length" INT DEFAULT 20 NOT NULL,
    "attr_fixed" INT DEFAULT 0,
    "attr_default_value" VARCHAR(40),
    "sort_rank" INT DEFAULT 1 NOT NULL,
    "sort_rank_intable" INT DEFAULT 1 NOT NULL,
    "sort_rank_inform" INT DEFAULT 1 NOT NULL,
    "sort_rank_insearch" INT DEFAULT 1 NOT NULL,
    "is_pkey" CHAR(1) DEFAULT '0' NOT NULL,
    "is_required" CHAR(1) DEFAULT '0' NOT NULL,
    "is_search_required" CHAR(1) DEFAULT '0' NOT NULL,
    "data_rule_for_search" VARCHAR(255),
    "data_tips_for_search" VARCHAR(255),
    "form_label" VARCHAR(255),
    "placeholder" VARCHAR(40),
    "table_label" VARCHAR(40),
    "search_form_label" VARCHAR(40),
    "remark" VARCHAR(255),
    "is_keywords_column" CHAR(1) DEFAULT '0' NOT NULL,
    "is_form_ele" CHAR(1) DEFAULT '1' NOT NULL,
    "is_table_col" CHAR(1) DEFAULT '1' NOT NULL,
    "is_table_switchbtn" CHAR(1) DEFAULT '0' NOT NULL,
    "table_col_width" INT DEFAULT 100 NOT NULL,
    "is_need_fixed_width" CHAR(1) DEFAULT '1' NOT NULL,
    "is_search_ele" CHAR(1) DEFAULT '0' NOT NULL,
    "is_search_hidden" CHAR(1) DEFAULT '0' NOT NULL,
    "col_format" VARCHAR(255),
    "search_ui_type" VARCHAR(40),
    "search_data_type" VARCHAR(40),
    "search_data_value" VARCHAR(255),
    "search_default_value" VARCHAR(255),
    "is_single_line" CHAR(1) DEFAULT '0' NOT NULL,
    "need_data_handler" CHAR(1),
    "form_ui_type" VARCHAR(40),
    "form_jboltinput_filter_handler" VARCHAR(60),
    "is_form_jboltinput_jstree_checkbox" CHAR(1) DEFAULT '0',
    "is_form_jboltinput_jstree_only_leaf" CHAR(1),
    "form_data_type" VARCHAR(40),
    "form_data_value" VARCHAR(255),
    "form_default_value" VARCHAR(255),
    "data_rule_assign" VARCHAR(255),
    "data_rule" VARCHAR(255),
    "data_tips" VARCHAR(255),
    "is_import_col" CHAR(1) DEFAULT '0' NOT NULL,
    "is_export_col" CHAR(1) DEFAULT '0' NOT NULL,
    "is_sortable" CHAR(1) DEFAULT '0' NOT NULL,
    "table_ui_type" VARCHAR(40),
    "table_data_type" VARCHAR(40),
    "table_data_value" VARCHAR(255),
    "form_ele_width" INT DEFAULT 0 NOT NULL,
    "is_item_inline" CHAR(1) DEFAULT '1' NOT NULL,
    "form_data_text_attr" VARCHAR(255),
    "form_data_value_attr" VARCHAR(40),
    "form_data_column_attr" VARCHAR(255),
    "search_data_text_attr" VARCHAR(255),
    "search_data_value_attr" VARCHAR(40),
    "search_data_column_attr" VARCHAR(255),
    "table_data_text_attr" VARCHAR(255),
    "table_data_value_attr" VARCHAR(40),
    "table_data_column_attr" VARCHAR(255),
    "is_need_translate" CHAR(1) DEFAULT '0' NOT NULL,
    "translate_type" VARCHAR(40),
    "translate_use_value" VARCHAR(250),
    "translate_col_name" VARCHAR(250),
    "is_upload_to_qiniu" CHAR(1) DEFAULT '0' NOT NULL,
    "form_upload_url" VARCHAR(255),
    "form_img_uploader_area" VARCHAR(20) DEFAULT '200,200',
    "form_maxsize" INT DEFAULT 200,
    "qiniu_bucket_sn" VARCHAR(60),
    "qiniu_file_key" VARCHAR(100) DEFAULT '[dateTime]/[randomId]/[filename]',
    NOT CLUSTER PRIMARY KEY("id")) STORAGE(ON "MAIN", CLUSTERBTR) ;

COMMENT ON TABLE "JBOLT"."jb_code_gen_model_attr" IS 'CodeGen模型详细设计';
COMMENT ON COLUMN "JBOLT"."jb_code_gen_model_attr"."attr_default_value" IS '默认值';
COMMENT ON COLUMN "JBOLT"."jb_code_gen_model_attr"."attr_fixed" IS '属性小数点';
COMMENT ON COLUMN "JBOLT"."jb_code_gen_model_attr"."attr_length" IS '属性长度';
COMMENT ON COLUMN "JBOLT"."jb_code_gen_model_attr"."attr_name" IS '属性名';
COMMENT ON COLUMN "JBOLT"."jb_code_gen_model_attr"."code_gen_id" IS '所属codeGen';
COMMENT ON COLUMN "JBOLT"."jb_code_gen_model_attr"."col_format" IS '格式化操作值';
COMMENT ON COLUMN "JBOLT"."jb_code_gen_model_attr"."col_name" IS '列名';
COMMENT ON COLUMN "JBOLT"."jb_code_gen_model_attr"."data_rule" IS '校验规则';
COMMENT ON COLUMN "JBOLT"."jb_code_gen_model_attr"."data_rule_assign" IS '表单校验规则 自定义';
COMMENT ON COLUMN "JBOLT"."jb_code_gen_model_attr"."data_rule_for_search" IS '查询条件必填校验规则';
COMMENT ON COLUMN "JBOLT"."jb_code_gen_model_attr"."data_tips" IS '校验提示信息';
COMMENT ON COLUMN "JBOLT"."jb_code_gen_model_attr"."data_tips_for_search" IS '查询条件不符合校验的提示信息';
COMMENT ON COLUMN "JBOLT"."jb_code_gen_model_attr"."form_data_column_attr" IS 'data-column-attr';
COMMENT ON COLUMN "JBOLT"."jb_code_gen_model_attr"."form_data_text_attr" IS 'data-text-attr';
COMMENT ON COLUMN "JBOLT"."jb_code_gen_model_attr"."form_data_type" IS '表单组件数据源类型';
COMMENT ON COLUMN "JBOLT"."jb_code_gen_model_attr"."form_data_value" IS '表单组件数据值';
COMMENT ON COLUMN "JBOLT"."jb_code_gen_model_attr"."form_data_value_attr" IS 'data-value-attr';
COMMENT ON COLUMN "JBOLT"."jb_code_gen_model_attr"."form_default_value" IS '表单组件默认值';
COMMENT ON COLUMN "JBOLT"."jb_code_gen_model_attr"."form_ele_width" IS '组件自定义宽度';
COMMENT ON COLUMN "JBOLT"."jb_code_gen_model_attr"."form_img_uploader_area" IS '上传组件area';
COMMENT ON COLUMN "JBOLT"."jb_code_gen_model_attr"."form_jboltinput_filter_handler" IS 'jboltinput filter handler';
COMMENT ON COLUMN "JBOLT"."jb_code_gen_model_attr"."form_label" IS '表单单显示文本';
COMMENT ON COLUMN "JBOLT"."jb_code_gen_model_attr"."form_maxsize" IS '上传尺寸限制';
COMMENT ON COLUMN "JBOLT"."jb_code_gen_model_attr"."form_ui_type" IS '表单组件类型';
COMMENT ON COLUMN "JBOLT"."jb_code_gen_model_attr"."form_upload_url" IS '上传地址';
COMMENT ON COLUMN "JBOLT"."jb_code_gen_model_attr"."id" IS '主键ID';
COMMENT ON COLUMN "JBOLT"."jb_code_gen_model_attr"."is_export_col" IS '导出列';
COMMENT ON COLUMN "JBOLT"."jb_code_gen_model_attr"."is_form_ele" IS '是否表单可编辑元素';
COMMENT ON COLUMN "JBOLT"."jb_code_gen_model_attr"."is_form_jboltinput_jstree_checkbox" IS 'jboltinput jstree是否有checkbox';
COMMENT ON COLUMN "JBOLT"."jb_code_gen_model_attr"."is_form_jboltinput_jstree_only_leaf" IS 'jboltinput jstree checkbox只选子节点';
COMMENT ON COLUMN "JBOLT"."jb_code_gen_model_attr"."is_import_col" IS '是否为导入列';
COMMENT ON COLUMN "JBOLT"."jb_code_gen_model_attr"."is_item_inline" IS 'radio checkbox等是否inline';
COMMENT ON COLUMN "JBOLT"."jb_code_gen_model_attr"."is_keywords_column" IS '是否为关键词查询列';
COMMENT ON COLUMN "JBOLT"."jb_code_gen_model_attr"."is_need_fixed_width" IS '是否固定宽度';
COMMENT ON COLUMN "JBOLT"."jb_code_gen_model_attr"."is_need_translate" IS '是否需要翻译';
COMMENT ON COLUMN "JBOLT"."jb_code_gen_model_attr"."is_pkey" IS '是否主键';
COMMENT ON COLUMN "JBOLT"."jb_code_gen_model_attr"."is_required" IS '是否必填';
COMMENT ON COLUMN "JBOLT"."jb_code_gen_model_attr"."is_search_ele" IS '是否检索条件';
COMMENT ON COLUMN "JBOLT"."jb_code_gen_model_attr"."is_search_hidden" IS '是否为检索隐藏条件';
COMMENT ON COLUMN "JBOLT"."jb_code_gen_model_attr"."is_search_required" IS '作为查询条件是否必填';
COMMENT ON COLUMN "JBOLT"."jb_code_gen_model_attr"."is_single_line" IS '独立新行';
COMMENT ON COLUMN "JBOLT"."jb_code_gen_model_attr"."is_sortable" IS '是否可排序';
COMMENT ON COLUMN "JBOLT"."jb_code_gen_model_attr"."is_table_col" IS '是否表格列';
COMMENT ON COLUMN "JBOLT"."jb_code_gen_model_attr"."is_table_switchbtn" IS '是否为表格switchbtn';
COMMENT ON COLUMN "JBOLT"."jb_code_gen_model_attr"."is_upload_to_qiniu" IS '是否上传到七牛';
COMMENT ON COLUMN "JBOLT"."jb_code_gen_model_attr"."java_type" IS '属性类型';
COMMENT ON COLUMN "JBOLT"."jb_code_gen_model_attr"."need_data_handler" IS '是否需要data_handler';
COMMENT ON COLUMN "JBOLT"."jb_code_gen_model_attr"."placeholder" IS '表单placeholder';
COMMENT ON COLUMN "JBOLT"."jb_code_gen_model_attr"."qiniu_bucket_sn" IS '七牛bucket sn';
COMMENT ON COLUMN "JBOLT"."jb_code_gen_model_attr"."qiniu_file_key" IS '七牛file key';
COMMENT ON COLUMN "JBOLT"."jb_code_gen_model_attr"."remark" IS '备注';
COMMENT ON COLUMN "JBOLT"."jb_code_gen_model_attr"."search_data_column_attr" IS 'data-column-attr';
COMMENT ON COLUMN "JBOLT"."jb_code_gen_model_attr"."search_data_text_attr" IS 'data-text-attr';
COMMENT ON COLUMN "JBOLT"."jb_code_gen_model_attr"."search_data_type" IS '查询用组件数据源类型';
COMMENT ON COLUMN "JBOLT"."jb_code_gen_model_attr"."search_data_value" IS '查询用组件数据值';
COMMENT ON COLUMN "JBOLT"."jb_code_gen_model_attr"."search_data_value_attr" IS 'data-value-attr';
COMMENT ON COLUMN "JBOLT"."jb_code_gen_model_attr"."search_default_value" IS '查询用组件默认值';
COMMENT ON COLUMN "JBOLT"."jb_code_gen_model_attr"."search_form_label" IS '查询表单提示文本';
COMMENT ON COLUMN "JBOLT"."jb_code_gen_model_attr"."search_ui_type" IS '查询用ui 组件类型';
COMMENT ON COLUMN "JBOLT"."jb_code_gen_model_attr"."sort_rank" IS '数据表内默认顺序';
COMMENT ON COLUMN "JBOLT"."jb_code_gen_model_attr"."sort_rank_inform" IS '表单中的排序';
COMMENT ON COLUMN "JBOLT"."jb_code_gen_model_attr"."sort_rank_insearch" IS '查询条件中的顺序';
COMMENT ON COLUMN "JBOLT"."jb_code_gen_model_attr"."sort_rank_intable" IS '表格中的排序';
COMMENT ON COLUMN "JBOLT"."jb_code_gen_model_attr"."table_col_width" IS '列宽';
COMMENT ON COLUMN "JBOLT"."jb_code_gen_model_attr"."table_data_column_attr" IS 'data-column-attr';
COMMENT ON COLUMN "JBOLT"."jb_code_gen_model_attr"."table_data_text_attr" IS 'data-text-attr';
COMMENT ON COLUMN "JBOLT"."jb_code_gen_model_attr"."table_data_type" IS '表格组件数据库类型';
COMMENT ON COLUMN "JBOLT"."jb_code_gen_model_attr"."table_data_value" IS '表格组件数据值';
COMMENT ON COLUMN "JBOLT"."jb_code_gen_model_attr"."table_data_value_attr" IS 'data-value-attr';
COMMENT ON COLUMN "JBOLT"."jb_code_gen_model_attr"."table_label" IS '表格中显示文本';
COMMENT ON COLUMN "JBOLT"."jb_code_gen_model_attr"."table_ui_type" IS '可编辑表格显示组件类型';
COMMENT ON COLUMN "JBOLT"."jb_code_gen_model_attr"."translate_col_name" IS '翻译后的列名';
COMMENT ON COLUMN "JBOLT"."jb_code_gen_model_attr"."translate_type" IS '翻译类型';
COMMENT ON COLUMN "JBOLT"."jb_code_gen_model_attr"."translate_use_value" IS '翻译用值';


CREATE UNIQUE  INDEX "INDEX309699627530600" ON "JBOLT"."jb_code_gen_model_attr"("id" ASC) STORAGE(ON "MAIN", CLUSTERBTR) ;

CREATE TABLE "JBOLT"."jb_datasource_filter"
(
    "id" BIGINT NOT NULL,
    "name" VARCHAR(100) NOT NULL,
    "config_name" VARCHAR(100) NOT NULL,
    "table_name_filter" TEXT,
    "table_name_contains" TEXT,
    "table_name_patterns" TEXT,
    "create_user_id" BIGINT NOT NULL,
    "update_user_id" BIGINT NOT NULL,
    "create_time" TIMESTAMP(0) NOT NULL,
    "update_time" TIMESTAMP(0) NOT NULL,
    NOT CLUSTER PRIMARY KEY("id")) STORAGE(ON "MAIN", CLUSTERBTR) ;

COMMENT ON TABLE "JBOLT"."jb_datasource_filter" IS '数据源过滤配置';
COMMENT ON COLUMN "JBOLT"."jb_datasource_filter"."config_name" IS '配置名称';
COMMENT ON COLUMN "JBOLT"."jb_datasource_filter"."create_time" IS '创建时间';
COMMENT ON COLUMN "JBOLT"."jb_datasource_filter"."create_user_id" IS '创建人';
COMMENT ON COLUMN "JBOLT"."jb_datasource_filter"."id" IS '主键ID';
COMMENT ON COLUMN "JBOLT"."jb_datasource_filter"."name" IS '名称';
COMMENT ON COLUMN "JBOLT"."jb_datasource_filter"."table_name_contains" IS '需要排除包含字符';
COMMENT ON COLUMN "JBOLT"."jb_datasource_filter"."table_name_filter" IS '不需要生成的表名';
COMMENT ON COLUMN "JBOLT"."jb_datasource_filter"."table_name_patterns" IS '需要排除符合正则的';
COMMENT ON COLUMN "JBOLT"."jb_datasource_filter"."update_time" IS '更新时间';
COMMENT ON COLUMN "JBOLT"."jb_datasource_filter"."update_user_id" IS '更新人';


CREATE UNIQUE  INDEX "INDEX309699609962900" ON "JBOLT"."jb_datasource_filter"("id" ASC) STORAGE(ON "MAIN", CLUSTERBTR) ;

CREATE TABLE "JBOLT"."jb_dept"
(
    "id" BIGINT NOT NULL,
    "name" VARCHAR(64) NOT NULL,
    "full_name" VARCHAR(40) NOT NULL,
    "pid" BIGINT,
    "type" VARCHAR(40) NOT NULL,
    "leader" VARCHAR(40),
    "phone" VARCHAR(40),
    "email" VARCHAR(100),
    "address" VARCHAR(100),
    "zipcode" VARCHAR(40),
    "remark" VARCHAR(255),
    "sort_rank" INT,
    "sn" VARCHAR(40) NOT NULL,
    "enable" CHAR(1) DEFAULT '1' NOT NULL,
    "update_time" TIMESTAMP(0),
    "create_time" TIMESTAMP(0),
    "dept_path" VARCHAR(255),
    NOT CLUSTER PRIMARY KEY("id")) STORAGE(ON "MAIN", CLUSTERBTR) ;

COMMENT ON TABLE "JBOLT"."jb_dept" IS '组织机构';
COMMENT ON COLUMN "JBOLT"."jb_dept"."address" IS '联系地址';
COMMENT ON COLUMN "JBOLT"."jb_dept"."create_time" IS '创建时间';
COMMENT ON COLUMN "JBOLT"."jb_dept"."dept_path" IS '部门路径';
COMMENT ON COLUMN "JBOLT"."jb_dept"."email" IS '电子邮箱';
COMMENT ON COLUMN "JBOLT"."jb_dept"."enable" IS '启用/禁用';
COMMENT ON COLUMN "JBOLT"."jb_dept"."full_name" IS '全称';
COMMENT ON COLUMN "JBOLT"."jb_dept"."id" IS '主键ID';
COMMENT ON COLUMN "JBOLT"."jb_dept"."leader" IS '负责人';
COMMENT ON COLUMN "JBOLT"."jb_dept"."name" IS '名称';
COMMENT ON COLUMN "JBOLT"."jb_dept"."phone" IS '联系电话';
COMMENT ON COLUMN "JBOLT"."jb_dept"."pid" IS '父级ID';
COMMENT ON COLUMN "JBOLT"."jb_dept"."remark" IS '备注信息';
COMMENT ON COLUMN "JBOLT"."jb_dept"."sn" IS '机构代码';
COMMENT ON COLUMN "JBOLT"."jb_dept"."sort_rank" IS '顺序';
COMMENT ON COLUMN "JBOLT"."jb_dept"."type" IS '类型';
COMMENT ON COLUMN "JBOLT"."jb_dept"."update_time" IS '最后更新时间';
COMMENT ON COLUMN "JBOLT"."jb_dept"."zipcode" IS '邮政编码';


CREATE UNIQUE  INDEX "INDEX309699583629400" ON "JBOLT"."jb_dept"("id" ASC) STORAGE(ON "MAIN", CLUSTERBTR) ;

CREATE TABLE "JBOLT"."jb_dictionary"
(
    "id" BIGINT NOT NULL,
    "name" VARCHAR(255),
    "type_id" BIGINT,
    "pid" BIGINT,
    "sort_rank" INT,
    "sn" VARCHAR(255),
    "type_key" VARCHAR(40),
    "enable" CHAR(1) DEFAULT '1' NOT NULL,
    "is_build_in" CHAR(1) DEFAULT '0' NOT NULL,
    NOT CLUSTER PRIMARY KEY("id")) STORAGE(ON "MAIN", CLUSTERBTR) ;

COMMENT ON TABLE "JBOLT"."jb_dictionary" IS '字典表';
COMMENT ON COLUMN "JBOLT"."jb_dictionary"."enable" IS '启用状态';
COMMENT ON COLUMN "JBOLT"."jb_dictionary"."id" IS '字典ID主键';
COMMENT ON COLUMN "JBOLT"."jb_dictionary"."is_build_in" IS '是否内置 不允许变更删除';
COMMENT ON COLUMN "JBOLT"."jb_dictionary"."name" IS '名称';
COMMENT ON COLUMN "JBOLT"."jb_dictionary"."pid" IS '父类ID';
COMMENT ON COLUMN "JBOLT"."jb_dictionary"."sn" IS '编号编码';
COMMENT ON COLUMN "JBOLT"."jb_dictionary"."sort_rank" IS '排序';
COMMENT ON COLUMN "JBOLT"."jb_dictionary"."type_id" IS '字典类型ID';
COMMENT ON COLUMN "JBOLT"."jb_dictionary"."type_key" IS '字典类型KEY';


CREATE UNIQUE  INDEX "INDEX309699562451700" ON "JBOLT"."jb_dictionary"("id" ASC) STORAGE(ON "MAIN", CLUSTERBTR) ;

CREATE TABLE "JBOLT"."jb_dictionary_type"
(
    "id" BIGINT NOT NULL,
    "name" VARCHAR(255),
    "mode_level" INT,
    "type_key" VARCHAR(255),
    "enable" CHAR(1) DEFAULT '1' NOT NULL,
    "is_build_in" CHAR(1) DEFAULT '0' NOT NULL,
    NOT CLUSTER PRIMARY KEY("id")) STORAGE(ON "MAIN", CLUSTERBTR) ;

COMMENT ON TABLE "JBOLT"."jb_dictionary_type" IS '字典类型';
COMMENT ON COLUMN "JBOLT"."jb_dictionary_type"."enable" IS '启用状态';
COMMENT ON COLUMN "JBOLT"."jb_dictionary_type"."id" IS '主键ID';
COMMENT ON COLUMN "JBOLT"."jb_dictionary_type"."is_build_in" IS '系统内置 不允许人为变更删除';
COMMENT ON COLUMN "JBOLT"."jb_dictionary_type"."mode_level" IS '模式层级';
COMMENT ON COLUMN "JBOLT"."jb_dictionary_type"."name" IS '名称';
COMMENT ON COLUMN "JBOLT"."jb_dictionary_type"."type_key" IS '标识KEY';


CREATE UNIQUE  INDEX "INDEX309699546095500" ON "JBOLT"."jb_dictionary_type"("id" ASC) STORAGE(ON "MAIN", CLUSTERBTR) ;

CREATE TABLE "JBOLT"."jb_global_config"
(
    "id" BIGINT NOT NULL,
    "config_key" VARCHAR(255) NOT NULL,
    "config_value" VARCHAR(255),
    "create_time" TIMESTAMP(0) NOT NULL,
    "user_id" BIGINT,
    "update_time" TIMESTAMP(0),
    "update_user_id" BIGINT,
    "name" VARCHAR(255),
    "value_type" VARCHAR(40),
    "type_key" VARCHAR(40),
    "type_id" BIGINT,
    "built_in" CHAR(1) DEFAULT '0',
    NOT CLUSTER PRIMARY KEY("id")) STORAGE(ON "MAIN", CLUSTERBTR) ;

COMMENT ON TABLE "JBOLT"."jb_global_config" IS '全局配置';
COMMENT ON COLUMN "JBOLT"."jb_global_config"."built_in" IS '内置参数';
COMMENT ON COLUMN "JBOLT"."jb_global_config"."config_key" IS '配置KEY';
COMMENT ON COLUMN "JBOLT"."jb_global_config"."config_value" IS '配置项值';
COMMENT ON COLUMN "JBOLT"."jb_global_config"."create_time" IS '创建时间';
COMMENT ON COLUMN "JBOLT"."jb_global_config"."id" IS '主键ID';
COMMENT ON COLUMN "JBOLT"."jb_global_config"."type_id" IS '类型ID';
COMMENT ON COLUMN "JBOLT"."jb_global_config"."type_key" IS '类型key';
COMMENT ON COLUMN "JBOLT"."jb_global_config"."update_time" IS '更新时间';
COMMENT ON COLUMN "JBOLT"."jb_global_config"."update_user_id" IS '更新用户ID';
COMMENT ON COLUMN "JBOLT"."jb_global_config"."user_id" IS '创建用户ID';
COMMENT ON COLUMN "JBOLT"."jb_global_config"."value_type" IS '值类型';


CREATE UNIQUE  INDEX "INDEX309699523537500" ON "JBOLT"."jb_global_config"("id" ASC) STORAGE(ON "MAIN", CLUSTERBTR) ;

CREATE TABLE "JBOLT"."jb_global_config_type"
(
    "id" BIGINT NOT NULL,
    "name" VARCHAR(40) NOT NULL,
    "sort_rank" INT,
    "type_key" VARCHAR(40) NOT NULL,
    "built_in" CHAR(1) DEFAULT '0',
    NOT CLUSTER PRIMARY KEY("id")) STORAGE(ON "MAIN", CLUSTERBTR) ;

COMMENT ON TABLE "JBOLT"."jb_global_config_type" IS '全局参数类型分组';
COMMENT ON COLUMN "JBOLT"."jb_global_config_type"."built_in" IS '内置';
COMMENT ON COLUMN "JBOLT"."jb_global_config_type"."id" IS '主键ID';
COMMENT ON COLUMN "JBOLT"."jb_global_config_type"."name" IS '类型名称';
COMMENT ON COLUMN "JBOLT"."jb_global_config_type"."sort_rank" IS '序号';
COMMENT ON COLUMN "JBOLT"."jb_global_config_type"."type_key" IS '类型KEY';


CREATE UNIQUE  INDEX "INDEX309699509621600" ON "JBOLT"."jb_global_config_type"("id" ASC) STORAGE(ON "MAIN", CLUSTERBTR) ;

CREATE TABLE "JBOLT"."jb_hiprint_tpl"
(
    "id" BIGINT NOT NULL,
    "name" VARCHAR(40) NOT NULL,
    "sn" VARCHAR(40) NOT NULL,
    "content" TEXT NOT NULL,
    "remark" VARCHAR(255),
    "create_time" TIMESTAMP(0) NOT NULL,
    "update_time" TIMESTAMP(0) NOT NULL,
    "create_user_id" BIGINT NOT NULL,
    "update_user_id" BIGINT NOT NULL,
    "test_api_url" VARCHAR(255),
    "test_json_data" TEXT,
    NOT CLUSTER PRIMARY KEY("id")) STORAGE(ON "MAIN", CLUSTERBTR) ;

COMMENT ON COLUMN "JBOLT"."jb_hiprint_tpl"."content" IS '模板内容';
COMMENT ON COLUMN "JBOLT"."jb_hiprint_tpl"."create_time" IS '创建时间';
COMMENT ON COLUMN "JBOLT"."jb_hiprint_tpl"."create_user_id" IS '创建人';
COMMENT ON COLUMN "JBOLT"."jb_hiprint_tpl"."id" IS '主键ID';
COMMENT ON COLUMN "JBOLT"."jb_hiprint_tpl"."name" IS '模板名称';
COMMENT ON COLUMN "JBOLT"."jb_hiprint_tpl"."remark" IS '备注';
COMMENT ON COLUMN "JBOLT"."jb_hiprint_tpl"."sn" IS '模板编码';
COMMENT ON COLUMN "JBOLT"."jb_hiprint_tpl"."test_api_url" IS '测试API地址';
COMMENT ON COLUMN "JBOLT"."jb_hiprint_tpl"."test_json_data" IS '测试JSON数据';
COMMENT ON COLUMN "JBOLT"."jb_hiprint_tpl"."update_time" IS '更新时间';
COMMENT ON COLUMN "JBOLT"."jb_hiprint_tpl"."update_user_id" IS '更新人';


CREATE UNIQUE  INDEX "INDEX309699490029200" ON "JBOLT"."jb_hiprint_tpl"("id" ASC) STORAGE(ON "MAIN", CLUSTERBTR) ;

CREATE TABLE "JBOLT"."jb_jbolt_file"
(
    "id" BIGINT NOT NULL,
    "local_path" VARCHAR(255),
    "local_url" VARCHAR(255),
    "cdn_url" VARCHAR(255),
    "create_time" TIMESTAMP(0),
    "user_id" BIGINT,
    "file_name" VARCHAR(255),
    "file_type" INT,
    "file_size" INT DEFAULT 0,
    "file_suffix" VARCHAR(255),
    NOT CLUSTER PRIMARY KEY("id")) STORAGE(ON "MAIN", CLUSTERBTR) ;

COMMENT ON TABLE "JBOLT"."jb_jbolt_file" IS '文件库';
COMMENT ON COLUMN "JBOLT"."jb_jbolt_file"."cdn_url" IS '外部CDN地址';
COMMENT ON COLUMN "JBOLT"."jb_jbolt_file"."create_time" IS '创建时间';
COMMENT ON COLUMN "JBOLT"."jb_jbolt_file"."file_name" IS '文件名';
COMMENT ON COLUMN "JBOLT"."jb_jbolt_file"."file_size" IS '文件大小';
COMMENT ON COLUMN "JBOLT"."jb_jbolt_file"."file_suffix" IS '后缀名';
COMMENT ON COLUMN "JBOLT"."jb_jbolt_file"."file_type" IS '文件类型 图片 附件 视频 音频';
COMMENT ON COLUMN "JBOLT"."jb_jbolt_file"."id" IS '主键ID';
COMMENT ON COLUMN "JBOLT"."jb_jbolt_file"."local_path" IS '保存物理地址';
COMMENT ON COLUMN "JBOLT"."jb_jbolt_file"."local_url" IS '本地可访问URL地址';
COMMENT ON COLUMN "JBOLT"."jb_jbolt_file"."user_id" IS '用户ID';


CREATE UNIQUE  INDEX "INDEX309699462987100" ON "JBOLT"."jb_jbolt_file"("id" ASC) STORAGE(ON "MAIN", CLUSTERBTR) ;

CREATE TABLE "JBOLT"."jb_login_log"
(
    "id" BIGINT NOT NULL,
    "user_id" BIGINT,
    "username" VARCHAR(40) NOT NULL,
    "login_ip" VARCHAR(40),
    "login_time" TIMESTAMP(0) NOT NULL,
    "login_state" INT NOT NULL,
    "is_browser" CHAR(1) DEFAULT '0',
    "browser_version" VARCHAR(40),
    "browser_name" VARCHAR(100),
    "os_name" VARCHAR(100),
    "login_city" VARCHAR(40),
    "login_address" VARCHAR(255),
    "login_city_code" VARCHAR(40),
    "login_province" VARCHAR(40),
    "login_country" VARCHAR(40),
    "is_mobile" CHAR(1) DEFAULT '0',
    "os_platform_name" VARCHAR(40),
    "browser_engine_name" VARCHAR(40),
    "browser_engine_version" VARCHAR(40),
    "login_address_latitude" VARCHAR(40),
    "login_address_longitude" VARCHAR(40),
    "is_remote_login" CHAR(1) DEFAULT '0',
    "create_time" TIMESTAMP(0),
    NOT CLUSTER PRIMARY KEY("id")) STORAGE(ON "MAIN", CLUSTERBTR) ;

COMMENT ON TABLE "JBOLT"."jb_login_log" IS '登录日志';
COMMENT ON COLUMN "JBOLT"."jb_login_log"."browser_engine_name" IS '浏览器引擎名';
COMMENT ON COLUMN "JBOLT"."jb_login_log"."browser_engine_version" IS '浏览器引擎版本';
COMMENT ON COLUMN "JBOLT"."jb_login_log"."browser_name" IS '浏览器';
COMMENT ON COLUMN "JBOLT"."jb_login_log"."browser_version" IS '浏览器版本号';
COMMENT ON COLUMN "JBOLT"."jb_login_log"."create_time" IS '记录创建时间';
COMMENT ON COLUMN "JBOLT"."jb_login_log"."id" IS '主键ID';
COMMENT ON COLUMN "JBOLT"."jb_login_log"."is_browser" IS '是否是浏览器访问';
COMMENT ON COLUMN "JBOLT"."jb_login_log"."is_mobile" IS '是否移动端';
COMMENT ON COLUMN "JBOLT"."jb_login_log"."is_remote_login" IS '是否为异地异常登录';
COMMENT ON COLUMN "JBOLT"."jb_login_log"."login_address" IS '登录位置详情';
COMMENT ON COLUMN "JBOLT"."jb_login_log"."login_address_latitude" IS '登录地横坐标';
COMMENT ON COLUMN "JBOLT"."jb_login_log"."login_address_longitude" IS '登录地纵坐标';
COMMENT ON COLUMN "JBOLT"."jb_login_log"."login_city" IS '登录城市';
COMMENT ON COLUMN "JBOLT"."jb_login_log"."login_city_code" IS '登录城市代码';
COMMENT ON COLUMN "JBOLT"."jb_login_log"."login_country" IS '登录国家';
COMMENT ON COLUMN "JBOLT"."jb_login_log"."login_ip" IS 'IP地址';
COMMENT ON COLUMN "JBOLT"."jb_login_log"."login_province" IS '登录省份';
COMMENT ON COLUMN "JBOLT"."jb_login_log"."login_state" IS '登录状态';
COMMENT ON COLUMN "JBOLT"."jb_login_log"."login_time" IS '登录时间';
COMMENT ON COLUMN "JBOLT"."jb_login_log"."os_name" IS '操作系统';
COMMENT ON COLUMN "JBOLT"."jb_login_log"."os_platform_name" IS '平台名称';
COMMENT ON COLUMN "JBOLT"."jb_login_log"."user_id" IS '用户ID';
COMMENT ON COLUMN "JBOLT"."jb_login_log"."username" IS '用户名';


CREATE UNIQUE  INDEX "INDEX309699428142500" ON "JBOLT"."jb_login_log"("id" ASC) STORAGE(ON "MAIN", CLUSTERBTR) ;

CREATE TABLE "JBOLT"."jb_online_user"
(
    "id" BIGINT NOT NULL,
    "session_id" VARCHAR(255) NOT NULL,
    "user_id" BIGINT NOT NULL,
    "login_log_id" BIGINT NOT NULL,
    "login_time" TIMESTAMP(0),
    "update_time" TIMESTAMP(0),
    "expiration_time" TIMESTAMP(0) NOT NULL,
    "screen_locked" CHAR(1) DEFAULT '0' NOT NULL,
    "online_state" INT NOT NULL,
    "offline_time" TIMESTAMP(0),
    NOT CLUSTER PRIMARY KEY("id")) STORAGE(ON "MAIN", CLUSTERBTR) ;

COMMENT ON TABLE "JBOLT"."jb_online_user" IS '在线用户';
COMMENT ON COLUMN "JBOLT"."jb_online_user"."expiration_time" IS '过期时间';
COMMENT ON COLUMN "JBOLT"."jb_online_user"."id" IS '主键ID';
COMMENT ON COLUMN "JBOLT"."jb_online_user"."login_log_id" IS '登录日志ID';
COMMENT ON COLUMN "JBOLT"."jb_online_user"."login_time" IS '登录时间';
COMMENT ON COLUMN "JBOLT"."jb_online_user"."offline_time" IS '离线时间';
COMMENT ON COLUMN "JBOLT"."jb_online_user"."online_state" IS '在线状态';
COMMENT ON COLUMN "JBOLT"."jb_online_user"."screen_locked" IS '是否锁屏';
COMMENT ON COLUMN "JBOLT"."jb_online_user"."session_id" IS '会话ID';
COMMENT ON COLUMN "JBOLT"."jb_online_user"."update_time" IS '更新时间';
COMMENT ON COLUMN "JBOLT"."jb_online_user"."user_id" IS '用户ID';


CREATE UNIQUE  INDEX "INDEX309699408015500" ON "JBOLT"."jb_online_user"("id" ASC) STORAGE(ON "MAIN", CLUSTERBTR) ;

CREATE TABLE "JBOLT"."jb_permission"
(
    "id" BIGINT NOT NULL,
    "title" VARCHAR(40) NOT NULL,
    "pid" BIGINT DEFAULT 0,
    "url" VARCHAR(255),
    "icons" VARCHAR(40),
    "sort_rank" INT,
    "permission_level" INT,
    "permission_key" VARCHAR(255),
    "is_menu" CHAR(1) DEFAULT '0',
    "is_target_blank" CHAR(1) DEFAULT '0',
    "is_system_admin_default" CHAR(1) DEFAULT '0',
    "open_type" INT DEFAULT 1,
    "open_option" VARCHAR(255),
    NOT CLUSTER PRIMARY KEY("id")) STORAGE(ON "MAIN", CLUSTERBTR) ;

COMMENT ON TABLE "JBOLT"."jb_permission" IS 'function定义';
COMMENT ON COLUMN "JBOLT"."jb_permission"."icons" IS '图标';
COMMENT ON COLUMN "JBOLT"."jb_permission"."id" IS '主键ID';
COMMENT ON COLUMN "JBOLT"."jb_permission"."is_menu" IS '是否是菜单';
COMMENT ON COLUMN "JBOLT"."jb_permission"."is_system_admin_default" IS '是否系统超级管理员默认拥有的权限';
COMMENT ON COLUMN "JBOLT"."jb_permission"."is_target_blank" IS '是否新窗口打开';
COMMENT ON COLUMN "JBOLT"."jb_permission"."open_option" IS '组件属性json';
COMMENT ON COLUMN "JBOLT"."jb_permission"."open_type" IS '打开类型 1 默认 2 iframe 3 dialog';
COMMENT ON COLUMN "JBOLT"."jb_permission"."permission_key" IS '权限资源KEY';
COMMENT ON COLUMN "JBOLT"."jb_permission"."permission_level" IS '层级';
COMMENT ON COLUMN "JBOLT"."jb_permission"."sort_rank" IS '排序';
COMMENT ON COLUMN "JBOLT"."jb_permission"."url" IS '地址';


CREATE UNIQUE  INDEX "INDEX309699386485900" ON "JBOLT"."jb_permission"("id" ASC) STORAGE(ON "MAIN", CLUSTERBTR) ;

CREATE TABLE "JBOLT"."jb_post"
(
    "id" BIGINT NOT NULL,
    "name" VARCHAR(40) NOT NULL,
    "type" VARCHAR(40) NOT NULL,
    "sort_rank" INT,
    "remark" VARCHAR(255),
    "sn" VARCHAR(40) NOT NULL,
    "enable" CHAR(1) DEFAULT '1',
    NOT CLUSTER PRIMARY KEY("id")) STORAGE(ON "MAIN", CLUSTERBTR) ;

COMMENT ON TABLE "JBOLT"."jb_post" IS '岗位';
COMMENT ON COLUMN "JBOLT"."jb_post"."enable" IS '启用/禁用';
COMMENT ON COLUMN "JBOLT"."jb_post"."id" IS '主键ID';
COMMENT ON COLUMN "JBOLT"."jb_post"."name" IS '岗位名称';
COMMENT ON COLUMN "JBOLT"."jb_post"."remark" IS '备注信息';
COMMENT ON COLUMN "JBOLT"."jb_post"."sn" IS '编码';
COMMENT ON COLUMN "JBOLT"."jb_post"."sort_rank" IS '顺序';
COMMENT ON COLUMN "JBOLT"."jb_post"."type" IS '岗位类型';


CREATE UNIQUE  INDEX "INDEX309699371255100" ON "JBOLT"."jb_post"("id" ASC) STORAGE(ON "MAIN", CLUSTERBTR) ;

CREATE TABLE "JBOLT"."jb_private_message"
(
    "id" BIGINT NOT NULL,
    "content" TEXT NOT NULL,
    "create_time" TIMESTAMP(0) NOT NULL,
    "from_user_id" BIGINT NOT NULL,
    "to_user_id" BIGINT NOT NULL,
    NOT CLUSTER PRIMARY KEY("id")) STORAGE(ON "MAIN", CLUSTERBTR) ;

COMMENT ON TABLE "JBOLT"."jb_private_message" IS '内部私信';
COMMENT ON COLUMN "JBOLT"."jb_private_message"."content" IS '私信内容';
COMMENT ON COLUMN "JBOLT"."jb_private_message"."create_time" IS '发送时间';
COMMENT ON COLUMN "JBOLT"."jb_private_message"."from_user_id" IS '发信人';
COMMENT ON COLUMN "JBOLT"."jb_private_message"."id" IS '主键ID';
COMMENT ON COLUMN "JBOLT"."jb_private_message"."to_user_id" IS '收信人';


CREATE UNIQUE  INDEX "INDEX309699358469600" ON "JBOLT"."jb_private_message"("id" ASC) STORAGE(ON "MAIN", CLUSTERBTR) ;

CREATE TABLE "JBOLT"."jb_qiniu"
(
    "id" BIGINT NOT NULL,
    "name" VARCHAR(255) NOT NULL,
    "sn" VARCHAR(40) NOT NULL,
    "ak" VARCHAR(64) NOT NULL,
    "sk" VARCHAR(64) NOT NULL,
    "type" INT NOT NULL,
    "bucket_count" INT DEFAULT 0 NOT NULL,
    "is_default" CHAR(1) DEFAULT '0' NOT NULL,
    "enable" CHAR(1) DEFAULT '0' NOT NULL,
    "remark" VARCHAR(255),
    "create_time" TIMESTAMP(0) NOT NULL,
    "update_time" TIMESTAMP(0) NOT NULL,
    "create_user_id" BIGINT NOT NULL,
    "update_user_id" BIGINT NOT NULL,
    NOT CLUSTER PRIMARY KEY("id")) STORAGE(ON "MAIN", CLUSTERBTR) ;

COMMENT ON TABLE "JBOLT"."jb_qiniu" IS '七牛账号表';
COMMENT ON COLUMN "JBOLT"."jb_qiniu"."ak" IS 'AK';
COMMENT ON COLUMN "JBOLT"."jb_qiniu"."bucket_count" IS '空间个数';
COMMENT ON COLUMN "JBOLT"."jb_qiniu"."create_time" IS '创建时间';
COMMENT ON COLUMN "JBOLT"."jb_qiniu"."create_user_id" IS '创建人';
COMMENT ON COLUMN "JBOLT"."jb_qiniu"."enable" IS '是否启用';
COMMENT ON COLUMN "JBOLT"."jb_qiniu"."id" IS '主键ID';
COMMENT ON COLUMN "JBOLT"."jb_qiniu"."is_default" IS '是否默认';
COMMENT ON COLUMN "JBOLT"."jb_qiniu"."name" IS '账号';
COMMENT ON COLUMN "JBOLT"."jb_qiniu"."remark" IS '备注';
COMMENT ON COLUMN "JBOLT"."jb_qiniu"."sk" IS 'SK';
COMMENT ON COLUMN "JBOLT"."jb_qiniu"."sn" IS '编号SN';
COMMENT ON COLUMN "JBOLT"."jb_qiniu"."type" IS '账号类型';
COMMENT ON COLUMN "JBOLT"."jb_qiniu"."update_time" IS '更新时间';
COMMENT ON COLUMN "JBOLT"."jb_qiniu"."update_user_id" IS '更新人';


CREATE UNIQUE  INDEX "INDEX309699330215100" ON "JBOLT"."jb_qiniu"("id" ASC) STORAGE(ON "MAIN", CLUSTERBTR) ;

CREATE TABLE "JBOLT"."jb_qiniu_bucket"
(
    "id" BIGINT NOT NULL,
    "name" VARCHAR(64) NOT NULL,
    "sn" VARCHAR(40) NOT NULL,
    "qiniu_id" BIGINT NOT NULL,
    "domain_url" VARCHAR(255),
    "callback_url" VARCHAR(255),
    "callback_body" VARCHAR(255),
    "callback_body_type" VARCHAR(64),
    "expires" INT,
    "is_default" CHAR(1) NOT NULL,
    "enable" CHAR(1) NOT NULL,
    "remark" VARCHAR(255),
    "create_user_id" BIGINT NOT NULL,
    "create_time" TIMESTAMP(0) NOT NULL,
    "update_user_id" BIGINT NOT NULL,
    "update_time" TIMESTAMP(0),
    "region" VARCHAR(20) NOT NULL,
    NOT CLUSTER PRIMARY KEY("id")) STORAGE(ON "MAIN", CLUSTERBTR) ;

COMMENT ON TABLE "JBOLT"."jb_qiniu_bucket" IS '七牛bucket配置';
COMMENT ON COLUMN "JBOLT"."jb_qiniu_bucket"."callback_body" IS '回调body定义';
COMMENT ON COLUMN "JBOLT"."jb_qiniu_bucket"."callback_body_type" IS '回调Body类型';
COMMENT ON COLUMN "JBOLT"."jb_qiniu_bucket"."callback_url" IS '回调地址';
COMMENT ON COLUMN "JBOLT"."jb_qiniu_bucket"."create_time" IS '创建时间';
COMMENT ON COLUMN "JBOLT"."jb_qiniu_bucket"."create_user_id" IS '创建人';
COMMENT ON COLUMN "JBOLT"."jb_qiniu_bucket"."domain_url" IS '绑定域名';
COMMENT ON COLUMN "JBOLT"."jb_qiniu_bucket"."enable" IS '是否启用';
COMMENT ON COLUMN "JBOLT"."jb_qiniu_bucket"."expires" IS '有效期(秒)';
COMMENT ON COLUMN "JBOLT"."jb_qiniu_bucket"."id" IS '主键ID';
COMMENT ON COLUMN "JBOLT"."jb_qiniu_bucket"."is_default" IS '是否默认';
COMMENT ON COLUMN "JBOLT"."jb_qiniu_bucket"."name" IS 'bucket名称';
COMMENT ON COLUMN "JBOLT"."jb_qiniu_bucket"."qiniu_id" IS '所属七牛账号';
COMMENT ON COLUMN "JBOLT"."jb_qiniu_bucket"."region" IS '地区';
COMMENT ON COLUMN "JBOLT"."jb_qiniu_bucket"."remark" IS '备注';
COMMENT ON COLUMN "JBOLT"."jb_qiniu_bucket"."sn" IS '编码';
COMMENT ON COLUMN "JBOLT"."jb_qiniu_bucket"."update_time" IS '更新时间';
COMMENT ON COLUMN "JBOLT"."jb_qiniu_bucket"."update_user_id" IS '更新人';


CREATE UNIQUE  INDEX "INDEX309699302024900" ON "JBOLT"."jb_qiniu_bucket"("id" ASC) STORAGE(ON "MAIN", CLUSTERBTR) ;

CREATE TABLE "JBOLT"."jb_remote_login_log"
(
    "id" BIGINT NOT NULL,
    "last_login_country" VARCHAR(40),
    "last_login_province" VARCHAR(40),
    "last_login_city" VARCHAR(40),
    "last_login_time" TIMESTAMP(0),
    "login_country" VARCHAR(40),
    "login_province" VARCHAR(40),
    "login_city" VARCHAR(40),
    "login_time" TIMESTAMP(0),
    "user_id" BIGINT,
    "username" VARCHAR(40),
    "is_new" CHAR(1) DEFAULT '0',
    "last_login_ip" VARCHAR(40),
    "login_ip" VARCHAR(40),
    "create_time" TIMESTAMP(0),
    NOT CLUSTER PRIMARY KEY("id")) STORAGE(ON "MAIN", CLUSTERBTR) ;

COMMENT ON TABLE "JBOLT"."jb_remote_login_log" IS '异地登录日志记录';
COMMENT ON COLUMN "JBOLT"."jb_remote_login_log"."create_time" IS '记录创建时间';
COMMENT ON COLUMN "JBOLT"."jb_remote_login_log"."id" IS '主键ID';
COMMENT ON COLUMN "JBOLT"."jb_remote_login_log"."is_new" IS '是否为最新一次';
COMMENT ON COLUMN "JBOLT"."jb_remote_login_log"."last_login_city" IS '最近一次登录城市';
COMMENT ON COLUMN "JBOLT"."jb_remote_login_log"."last_login_country" IS '最近一次登录国家';
COMMENT ON COLUMN "JBOLT"."jb_remote_login_log"."last_login_ip" IS '最近一次登录IP';
COMMENT ON COLUMN "JBOLT"."jb_remote_login_log"."last_login_province" IS '最近一次登录省份';
COMMENT ON COLUMN "JBOLT"."jb_remote_login_log"."last_login_time" IS '最近一次登录时间';
COMMENT ON COLUMN "JBOLT"."jb_remote_login_log"."login_city" IS '新登录城市';
COMMENT ON COLUMN "JBOLT"."jb_remote_login_log"."login_country" IS '新登录国家';
COMMENT ON COLUMN "JBOLT"."jb_remote_login_log"."login_ip" IS '新登录IP';
COMMENT ON COLUMN "JBOLT"."jb_remote_login_log"."login_province" IS '新登录省份';
COMMENT ON COLUMN "JBOLT"."jb_remote_login_log"."login_time" IS '新登录时间';
COMMENT ON COLUMN "JBOLT"."jb_remote_login_log"."user_id" IS '登录用户ID';
COMMENT ON COLUMN "JBOLT"."jb_remote_login_log"."username" IS '登录用户名';


CREATE UNIQUE  INDEX "INDEX309699272982300" ON "JBOLT"."jb_remote_login_log"("id" ASC) STORAGE(ON "MAIN", CLUSTERBTR) ;

CREATE TABLE "JBOLT"."jb_role"
(
    "id" BIGINT NOT NULL,
    "name" VARCHAR(20) NOT NULL,
    "sn" VARCHAR(40),
    "pid" BIGINT DEFAULT 0,
    NOT CLUSTER PRIMARY KEY("id")) STORAGE(ON "MAIN", CLUSTERBTR) ;

COMMENT ON TABLE "JBOLT"."jb_role" IS '角色表';
COMMENT ON COLUMN "JBOLT"."jb_role"."id" IS '主键ID';
COMMENT ON COLUMN "JBOLT"."jb_role"."name" IS '名称';
COMMENT ON COLUMN "JBOLT"."jb_role"."pid" IS '父级角色ID';
COMMENT ON COLUMN "JBOLT"."jb_role"."sn" IS '编码';


CREATE UNIQUE  INDEX "INDEX309699263090800" ON "JBOLT"."jb_role"("id" ASC) STORAGE(ON "MAIN", CLUSTERBTR) ;

CREATE TABLE "JBOLT"."jb_role_permission"
(
    "id" BIGINT NOT NULL,
    "role_id" BIGINT NOT NULL,
    "permission_id" BIGINT NOT NULL,
    NOT CLUSTER PRIMARY KEY("id")) STORAGE(ON "MAIN", CLUSTERBTR) ;

COMMENT ON TABLE "JBOLT"."jb_role_permission" IS '角色功能列表';
COMMENT ON COLUMN "JBOLT"."jb_role_permission"."id" IS '主键ID';
COMMENT ON COLUMN "JBOLT"."jb_role_permission"."permission_id" IS '权限ID';
COMMENT ON COLUMN "JBOLT"."jb_role_permission"."role_id" IS '角色ID';


CREATE UNIQUE  INDEX "INDEX309699252270000" ON "JBOLT"."jb_role_permission"("id" ASC) STORAGE(ON "MAIN", CLUSTERBTR) ;

CREATE TABLE "JBOLT"."jb_sensitive_word"
(
    "id" BIGINT NOT NULL,
    "content" VARCHAR(40) NOT NULL,
    "enable" CHAR(1) DEFAULT '1' NOT NULL,
    NOT CLUSTER PRIMARY KEY("id")) STORAGE(ON "MAIN", CLUSTERBTR) ;

COMMENT ON TABLE "JBOLT"."jb_sensitive_word" IS '敏感词词库';
COMMENT ON COLUMN "JBOLT"."jb_sensitive_word"."content" IS '内容';
COMMENT ON COLUMN "JBOLT"."jb_sensitive_word"."enable" IS '启用状态';
COMMENT ON COLUMN "JBOLT"."jb_sensitive_word"."id" IS '主键ID';


CREATE UNIQUE  INDEX "INDEX309699240438800" ON "JBOLT"."jb_sensitive_word"("id" ASC) STORAGE(ON "MAIN", CLUSTERBTR) ;

CREATE TABLE "JBOLT"."jb_sys_notice"
(
    "id" BIGINT NOT NULL,
    "title" VARCHAR(255) NOT NULL,
    "content" TEXT NOT NULL,
    "type" INT NOT NULL,
    "priority_level" INT NOT NULL,
    "read_count" INT DEFAULT 0,
    "create_time" TIMESTAMP(0) NOT NULL,
    "update_time" TIMESTAMP(0),
    "create_user_id" BIGINT,
    "update_user_id" BIGINT,
    "receiver_type" INT,
    "receiver_value" VARCHAR(1000),
    "files" VARCHAR(255),
    "del_flag" CHAR(1) DEFAULT '0',
    NOT CLUSTER PRIMARY KEY("id")) STORAGE(ON "MAIN", CLUSTERBTR) ;

COMMENT ON TABLE "JBOLT"."jb_sys_notice" IS '系统通知';
COMMENT ON COLUMN "JBOLT"."jb_sys_notice"."content" IS '消息内容';
COMMENT ON COLUMN "JBOLT"."jb_sys_notice"."create_time" IS '创建时间';
COMMENT ON COLUMN "JBOLT"."jb_sys_notice"."create_user_id" IS '创建人';
COMMENT ON COLUMN "JBOLT"."jb_sys_notice"."del_flag" IS '删除标志';
COMMENT ON COLUMN "JBOLT"."jb_sys_notice"."files" IS '附件';
COMMENT ON COLUMN "JBOLT"."jb_sys_notice"."id" IS '主键ID';
COMMENT ON COLUMN "JBOLT"."jb_sys_notice"."priority_level" IS '优先级';
COMMENT ON COLUMN "JBOLT"."jb_sys_notice"."read_count" IS '已读人数';
COMMENT ON COLUMN "JBOLT"."jb_sys_notice"."receiver_type" IS '接收人类型';
COMMENT ON COLUMN "JBOLT"."jb_sys_notice"."receiver_value" IS '接收人值';
COMMENT ON COLUMN "JBOLT"."jb_sys_notice"."title" IS '标题';
COMMENT ON COLUMN "JBOLT"."jb_sys_notice"."type" IS '通知类型';
COMMENT ON COLUMN "JBOLT"."jb_sys_notice"."update_time" IS '更新时间';
COMMENT ON COLUMN "JBOLT"."jb_sys_notice"."update_user_id" IS '更新人';


CREATE UNIQUE  INDEX "INDEX309699217234800" ON "JBOLT"."jb_sys_notice"("id" ASC) STORAGE(ON "MAIN", CLUSTERBTR) ;

CREATE TABLE "JBOLT"."jb_sys_notice_reader"
(
    "id" BIGINT NOT NULL,
    "sys_notice_id" BIGINT NOT NULL,
    "user_id" BIGINT NOT NULL,
    "create_time" TIMESTAMP(0) NOT NULL,
    NOT CLUSTER PRIMARY KEY("id")) STORAGE(ON "MAIN", CLUSTERBTR) ;

COMMENT ON TABLE "JBOLT"."jb_sys_notice_reader" IS '通知阅读用户关系表';
COMMENT ON COLUMN "JBOLT"."jb_sys_notice_reader"."create_time" IS '创建时间';
COMMENT ON COLUMN "JBOLT"."jb_sys_notice_reader"."id" IS '主键ID';
COMMENT ON COLUMN "JBOLT"."jb_sys_notice_reader"."sys_notice_id" IS '通知ID';
COMMENT ON COLUMN "JBOLT"."jb_sys_notice_reader"."user_id" IS '用户ID';


CREATE UNIQUE  INDEX "INDEX309699205732900" ON "JBOLT"."jb_sys_notice_reader"("id" ASC) STORAGE(ON "MAIN", CLUSTERBTR) ;

CREATE TABLE "JBOLT"."jb_system_log"
(
    "id" BIGINT NOT NULL,
    "title" VARCHAR(255),
    "type" INT,
    "url" VARCHAR(255),
    "user_id" BIGINT,
    "user_name" VARCHAR(255),
    "target_type" INT,
    "create_time" TIMESTAMP(0),
    "target_id" BIGINT,
    "open_type" INT,
    NOT CLUSTER PRIMARY KEY("id")) STORAGE(ON "MAIN", CLUSTERBTR) ;

COMMENT ON TABLE "JBOLT"."jb_system_log" IS '系统操作日志表';
COMMENT ON COLUMN "JBOLT"."jb_system_log"."create_time" IS '记录创建时间';
COMMENT ON COLUMN "JBOLT"."jb_system_log"."id" IS '主键ID';
COMMENT ON COLUMN "JBOLT"."jb_system_log"."open_type" IS '打开类型URL还是Dialog';
COMMENT ON COLUMN "JBOLT"."jb_system_log"."target_id" IS '操作对象ID';
COMMENT ON COLUMN "JBOLT"."jb_system_log"."target_type" IS '操作对象类型';
COMMENT ON COLUMN "JBOLT"."jb_system_log"."title" IS '标题';
COMMENT ON COLUMN "JBOLT"."jb_system_log"."type" IS '操作类型 删除 更新 新增';
COMMENT ON COLUMN "JBOLT"."jb_system_log"."url" IS '连接对象详情地址';
COMMENT ON COLUMN "JBOLT"."jb_system_log"."user_id" IS '操作人ID';
COMMENT ON COLUMN "JBOLT"."jb_system_log"."user_name" IS '操作人姓名';


CREATE UNIQUE  INDEX "INDEX309699186786800" ON "JBOLT"."jb_system_log"("id" ASC) STORAGE(ON "MAIN", CLUSTERBTR) ;

CREATE TABLE "JBOLT"."jb_todo"
(
    "id" BIGINT NOT NULL,
    "title" VARCHAR(255) NOT NULL,
    "content" TEXT,
    "user_id" BIGINT NOT NULL,
    "state" INT NOT NULL,
    "specified_finish_time" TIMESTAMP(0) NOT NULL,
    "type" INT NOT NULL,
    "url" VARCHAR(512),
    "priority_level" INT NOT NULL,
    "real_finish_time" TIMESTAMP(0),
    "cancel_time" TIMESTAMP(0),
    "source_msg_id" VARCHAR(64),
    "source_request_id" VARCHAR(64),
    "source_url" VARCHAR(512),
    "send_user_key" VARCHAR(64),
    "receive_user_key" VARCHAR(64),
    "source_sys" VARCHAR(64),
    "create_user_id" BIGINT NOT NULL,
    "update_user_id" BIGINT,
    "create_time" TIMESTAMP(0) NOT NULL,
    "update_time" TIMESTAMP(0) NOT NULL,
    "is_readed" CHAR(1) NOT NULL,
    NOT CLUSTER PRIMARY KEY("id")) STORAGE(ON "MAIN", CLUSTERBTR) ;

COMMENT ON TABLE "JBOLT"."jb_todo" IS '代办事项';
COMMENT ON COLUMN "JBOLT"."jb_todo"."cancel_time" IS '取消时间';
COMMENT ON COLUMN "JBOLT"."jb_todo"."content" IS '详情内容';
COMMENT ON COLUMN "JBOLT"."jb_todo"."create_time" IS '创建时间';
COMMENT ON COLUMN "JBOLT"."jb_todo"."create_user_id" IS '创建人ID';
COMMENT ON COLUMN "JBOLT"."jb_todo"."id" IS '主键ID';
COMMENT ON COLUMN "JBOLT"."jb_todo"."is_readed" IS '是否已读';
COMMENT ON COLUMN "JBOLT"."jb_todo"."priority_level" IS '优先级';
COMMENT ON COLUMN "JBOLT"."jb_todo"."real_finish_time" IS '完成时间';
COMMENT ON COLUMN "JBOLT"."jb_todo"."receive_user_key" IS '接收人第三方系统标识';
COMMENT ON COLUMN "JBOLT"."jb_todo"."send_user_key" IS '发送人第三方系统标识';
COMMENT ON COLUMN "JBOLT"."jb_todo"."source_msg_id" IS '第三方系统消息主键';
COMMENT ON COLUMN "JBOLT"."jb_todo"."source_request_id" IS '第三方系统流程主键';
COMMENT ON COLUMN "JBOLT"."jb_todo"."source_sys" IS '来源系统';
COMMENT ON COLUMN "JBOLT"."jb_todo"."source_url" IS '第三方url';
COMMENT ON COLUMN "JBOLT"."jb_todo"."specified_finish_time" IS '规定完成时间';
COMMENT ON COLUMN "JBOLT"."jb_todo"."state" IS '状态';
COMMENT ON COLUMN "JBOLT"."jb_todo"."title" IS '标题';
COMMENT ON COLUMN "JBOLT"."jb_todo"."type" IS '类型 链接还是内容 还是都有';
COMMENT ON COLUMN "JBOLT"."jb_todo"."update_time" IS '更新时间';
COMMENT ON COLUMN "JBOLT"."jb_todo"."update_user_id" IS '更新人ID';
COMMENT ON COLUMN "JBOLT"."jb_todo"."url" IS '链接';
COMMENT ON COLUMN "JBOLT"."jb_todo"."user_id" IS '所属用户';


CREATE UNIQUE  INDEX "INDEX309699149980700" ON "JBOLT"."jb_todo"("id" ASC) STORAGE(ON "MAIN", CLUSTERBTR) ;

CREATE TABLE "JBOLT"."jb_topnav"
(
    "id" BIGINT NOT NULL,
    "name" VARCHAR(40),
    "icon" VARCHAR(40),
    "enable" CHAR(1) DEFAULT '0',
    "sort_rank" INT,
    NOT CLUSTER PRIMARY KEY("id")) STORAGE(ON "MAIN", CLUSTERBTR) ;

COMMENT ON TABLE "JBOLT"."jb_topnav" IS '顶部导航';
COMMENT ON COLUMN "JBOLT"."jb_topnav"."enable" IS '是否启用';
COMMENT ON COLUMN "JBOLT"."jb_topnav"."icon" IS '图标';
COMMENT ON COLUMN "JBOLT"."jb_topnav"."id" IS '主键ID';
COMMENT ON COLUMN "JBOLT"."jb_topnav"."name" IS '名称';
COMMENT ON COLUMN "JBOLT"."jb_topnav"."sort_rank" IS '排序';


CREATE UNIQUE  INDEX "INDEX309699135458600" ON "JBOLT"."jb_topnav"("id" ASC) STORAGE(ON "MAIN", CLUSTERBTR) ;

CREATE TABLE "JBOLT"."jb_topnav_menu"
(
    "id" BIGINT NOT NULL,
    "topnav_id" BIGINT,
    "permission_id" BIGINT,
    NOT CLUSTER PRIMARY KEY("id")) STORAGE(ON "MAIN", CLUSTERBTR) ;

COMMENT ON TABLE "JBOLT"."jb_topnav_menu" IS '顶部菜单对应左侧一级导航中间表';
COMMENT ON COLUMN "JBOLT"."jb_topnav_menu"."id" IS '主键ID';
COMMENT ON COLUMN "JBOLT"."jb_topnav_menu"."permission_id" IS '菜单资源ID';
COMMENT ON COLUMN "JBOLT"."jb_topnav_menu"."topnav_id" IS '顶部导航ID';


CREATE UNIQUE  INDEX "INDEX309699124216500" ON "JBOLT"."jb_topnav_menu"("id" ASC) STORAGE(ON "MAIN", CLUSTERBTR) ;

CREATE TABLE "JBOLT"."jb_user"
(
    "id" BIGINT NOT NULL,
    "sn" VARCHAR(20),
    "username" VARCHAR(40) NOT NULL,
    "password" VARCHAR(128) NOT NULL,
    "name" VARCHAR(20) NOT NULL,
    "avatar" VARCHAR(255),
    "email" VARCHAR(60),
    "create_time" TIMESTAMP(0),
    "phone" VARCHAR(40),
    "enable" CHAR(1) DEFAULT '0',
    "sex" INT DEFAULT 0,
    "age" INT,
    "pinyin" VARCHAR(255),
    "roles" VARCHAR(255),
    "is_system_admin" CHAR(1) DEFAULT '0',
    "create_user_id" BIGINT,
    "pwd_salt" VARCHAR(128),
    "login_ip" VARCHAR(255),
    "login_time" TIMESTAMP(0),
    "login_city" VARCHAR(40),
    "login_province" VARCHAR(40),
    "login_country" VARCHAR(40),
    "is_remote_login" CHAR(1) DEFAULT '0',
    "dept_id" BIGINT,
    "dept_path" VARCHAR(255),
    "posts" VARCHAR(255),
    "update_time" TIMESTAMP(0),
    "update_user_id" BIGINT,
    "last_pwd_update_time" TIMESTAMP(0),
    NOT CLUSTER PRIMARY KEY("id")) STORAGE(ON "MAIN", CLUSTERBTR) ;

COMMENT ON TABLE "JBOLT"."jb_user" IS '用户登录账户表';
COMMENT ON COLUMN "JBOLT"."jb_user"."age" IS '年龄';
COMMENT ON COLUMN "JBOLT"."jb_user"."avatar" IS '头像';
COMMENT ON COLUMN "JBOLT"."jb_user"."create_time" IS '记录创建时间';
COMMENT ON COLUMN "JBOLT"."jb_user"."create_user_id" IS '创建人';
COMMENT ON COLUMN "JBOLT"."jb_user"."dept_id" IS '部门ID';
COMMENT ON COLUMN "JBOLT"."jb_user"."dept_path" IS '部门路径';
COMMENT ON COLUMN "JBOLT"."jb_user"."email" IS '电子邮箱地址';
COMMENT ON COLUMN "JBOLT"."jb_user"."enable" IS '启用';
COMMENT ON COLUMN "JBOLT"."jb_user"."id" IS '主键ID';
COMMENT ON COLUMN "JBOLT"."jb_user"."is_remote_login" IS '是否异地登录';
COMMENT ON COLUMN "JBOLT"."jb_user"."is_system_admin" IS '是否系统超级管理员';
COMMENT ON COLUMN "JBOLT"."jb_user"."last_pwd_update_time" IS '最近一次密码修改时间';
COMMENT ON COLUMN "JBOLT"."jb_user"."login_city" IS '最后登录城市';
COMMENT ON COLUMN "JBOLT"."jb_user"."login_country" IS '最后登录国家';
COMMENT ON COLUMN "JBOLT"."jb_user"."login_ip" IS '最后登录IP';
COMMENT ON COLUMN "JBOLT"."jb_user"."login_province" IS '最后登录省份';
COMMENT ON COLUMN "JBOLT"."jb_user"."login_time" IS '最后登录时间';
COMMENT ON COLUMN "JBOLT"."jb_user"."name" IS '姓名';
COMMENT ON COLUMN "JBOLT"."jb_user"."password" IS '密码';
COMMENT ON COLUMN "JBOLT"."jb_user"."phone" IS '手机号';
COMMENT ON COLUMN "JBOLT"."jb_user"."pinyin" IS '拼音码';
COMMENT ON COLUMN "JBOLT"."jb_user"."posts" IS '岗位IDS';
COMMENT ON COLUMN "JBOLT"."jb_user"."pwd_salt" IS '密码盐值';
COMMENT ON COLUMN "JBOLT"."jb_user"."roles" IS '角色 一对多';
COMMENT ON COLUMN "JBOLT"."jb_user"."sex" IS '性别';
COMMENT ON COLUMN "JBOLT"."jb_user"."sn" IS '工号';
COMMENT ON COLUMN "JBOLT"."jb_user"."update_time" IS '更新时间';
COMMENT ON COLUMN "JBOLT"."jb_user"."update_user_id" IS '更新人ID';
COMMENT ON COLUMN "JBOLT"."jb_user"."username" IS '用户名';


CREATE UNIQUE  INDEX "INDEX309699082297600" ON "JBOLT"."jb_user"("id" ASC) STORAGE(ON "MAIN", CLUSTERBTR) ;

CREATE TABLE "JBOLT"."jb_user_config"
(
    "id" BIGINT NOT NULL,
    "name" VARCHAR(255) NOT NULL,
    "config_key" VARCHAR(255) NOT NULL,
    "config_value" VARCHAR(255) NOT NULL,
    "user_id" BIGINT NOT NULL,
    "create_time" TIMESTAMP(0),
    "update_time" TIMESTAMP(0),
    "value_type" VARCHAR(40),
    NOT CLUSTER PRIMARY KEY("id")) STORAGE(ON "MAIN", CLUSTERBTR) ;

COMMENT ON TABLE "JBOLT"."jb_user_config" IS '用户系统样式自定义设置表';
COMMENT ON COLUMN "JBOLT"."jb_user_config"."config_key" IS '配置KEY';
COMMENT ON COLUMN "JBOLT"."jb_user_config"."config_value" IS '配置值';
COMMENT ON COLUMN "JBOLT"."jb_user_config"."create_time" IS '创建时间';
COMMENT ON COLUMN "JBOLT"."jb_user_config"."id" IS '主键ID';
COMMENT ON COLUMN "JBOLT"."jb_user_config"."name" IS '配置名';
COMMENT ON COLUMN "JBOLT"."jb_user_config"."update_time" IS '更新时间';
COMMENT ON COLUMN "JBOLT"."jb_user_config"."user_id" IS '用户ID';
COMMENT ON COLUMN "JBOLT"."jb_user_config"."value_type" IS '取值类型';


CREATE UNIQUE  INDEX "INDEX309699065967700" ON "JBOLT"."jb_user_config"("id" ASC) STORAGE(ON "MAIN", CLUSTERBTR) ;

CREATE TABLE "JBOLT"."jb_user_extend"
(
    "id" BIGINT NOT NULL,
    "english_full_name" VARCHAR(40),
    "birthday" TIMESTAMP(0),
    "residential_address" VARCHAR(100),
    "company_name" VARCHAR(40),
    "company_address" VARCHAR(100),
    "recipient_address" VARCHAR(100),
    "recipient_phone" VARCHAR(20),
    "recipient_name" VARCHAR(40),
    "id_number" VARCHAR(20),
    "country" VARCHAR(20),
    "nation" VARCHAR(20),
    "province" VARCHAR(20),
    "city" VARCHAR(20),
    "city_code" VARCHAR(20),
    "county" VARCHAR(20),
    "township" VARCHAR(20),
    "community" VARCHAR(20),
    "marital_status" INT,
    "is_cpc_member" CHAR(1) DEFAULT '0' NOT NULL,
    "is_cyl_member" CHAR(1) DEFAULT '0' NOT NULL,
    "professional_title" INT,
    NOT CLUSTER PRIMARY KEY("id")) STORAGE(ON "MAIN", CLUSTERBTR) ;

COMMENT ON TABLE "JBOLT"."jb_user_extend" IS '用户扩展信息表';
COMMENT ON COLUMN "JBOLT"."jb_user_extend"."birthday" IS '出生日期';
COMMENT ON COLUMN "JBOLT"."jb_user_extend"."city" IS '城市';
COMMENT ON COLUMN "JBOLT"."jb_user_extend"."city_code" IS '城市代码';
COMMENT ON COLUMN "JBOLT"."jb_user_extend"."community" IS '行政村 社区';
COMMENT ON COLUMN "JBOLT"."jb_user_extend"."company_address" IS '公司地址';
COMMENT ON COLUMN "JBOLT"."jb_user_extend"."company_name" IS '公司名称';
COMMENT ON COLUMN "JBOLT"."jb_user_extend"."country" IS '国家';
COMMENT ON COLUMN "JBOLT"."jb_user_extend"."county" IS '区县';
COMMENT ON COLUMN "JBOLT"."jb_user_extend"."english_full_name" IS '英文全名';
COMMENT ON COLUMN "JBOLT"."jb_user_extend"."id" IS '主键ID';
COMMENT ON COLUMN "JBOLT"."jb_user_extend"."id_number" IS '身份证号';
COMMENT ON COLUMN "JBOLT"."jb_user_extend"."is_cpc_member" IS '是否党员';
COMMENT ON COLUMN "JBOLT"."jb_user_extend"."is_cyl_member" IS '是否共青团员';
COMMENT ON COLUMN "JBOLT"."jb_user_extend"."marital_status" IS '婚姻状态';
COMMENT ON COLUMN "JBOLT"."jb_user_extend"."nation" IS '民族';
COMMENT ON COLUMN "JBOLT"."jb_user_extend"."professional_title" IS '职称';
COMMENT ON COLUMN "JBOLT"."jb_user_extend"."province" IS '省';
COMMENT ON COLUMN "JBOLT"."jb_user_extend"."recipient_address" IS '快递收件地址';
COMMENT ON COLUMN "JBOLT"."jb_user_extend"."recipient_name" IS '收件人姓名';
COMMENT ON COLUMN "JBOLT"."jb_user_extend"."recipient_phone" IS '收件人电话';
COMMENT ON COLUMN "JBOLT"."jb_user_extend"."residential_address" IS '居住地址';
COMMENT ON COLUMN "JBOLT"."jb_user_extend"."township" IS '乡镇';


CREATE UNIQUE  INDEX "INDEX309699029996200" ON "JBOLT"."jb_user_extend"("id" ASC) STORAGE(ON "MAIN", CLUSTERBTR) ;

CREATE TABLE "JBOLT"."jb_wechat_article"
(
    "id" BIGINT NOT NULL,
    "title" VARCHAR(255),
    "content" VARCHAR(255),
    "create_time" TIMESTAMP(0),
    "user_id" BIGINT,
    "update_time" TIMESTAMP(0),
    "update_user_id" BIGINT,
    "brief_info" VARCHAR(120),
    "poster" VARCHAR(255),
    "view_count" INT,
    "media_id" VARCHAR(255),
    "mp_id" BIGINT,
    "url" VARCHAR(255),
    "type" INT,
    NOT CLUSTER PRIMARY KEY("id")) STORAGE(ON "MAIN", CLUSTERBTR) ;

COMMENT ON TABLE "JBOLT"."jb_wechat_article" IS '微信图文信息';
COMMENT ON COLUMN "JBOLT"."jb_wechat_article"."brief_info" IS '文章摘要';
COMMENT ON COLUMN "JBOLT"."jb_wechat_article"."create_time" IS '创建时间';
COMMENT ON COLUMN "JBOLT"."jb_wechat_article"."id" IS '主键ID';
COMMENT ON COLUMN "JBOLT"."jb_wechat_article"."media_id" IS '微信素材 ID';
COMMENT ON COLUMN "JBOLT"."jb_wechat_article"."mp_id" IS '微信 ID';
COMMENT ON COLUMN "JBOLT"."jb_wechat_article"."type" IS '本地图文 公众号图文素材 外部图文';
COMMENT ON COLUMN "JBOLT"."jb_wechat_article"."update_time" IS '更新时间';
COMMENT ON COLUMN "JBOLT"."jb_wechat_article"."update_user_id" IS '更新用户 ID';
COMMENT ON COLUMN "JBOLT"."jb_wechat_article"."url" IS '图文链接地址';
COMMENT ON COLUMN "JBOLT"."jb_wechat_article"."user_id" IS '用户 ID';
COMMENT ON COLUMN "JBOLT"."jb_wechat_article"."view_count" IS '阅读数';


CREATE UNIQUE  INDEX "INDEX309699006898600" ON "JBOLT"."jb_wechat_article"("id" ASC) STORAGE(ON "MAIN", CLUSTERBTR) ;

CREATE TABLE "JBOLT"."jb_wechat_autoreply"
(
    "id" BIGINT NOT NULL,
    "mp_id" BIGINT,
    "type" INT DEFAULT 0,
    "name" VARCHAR(40),
    "reply_type" INT,
    "create_time" TIMESTAMP(0),
    "user_id" BIGINT,
    "enable" CHAR(1) DEFAULT '0',
    NOT CLUSTER PRIMARY KEY("id")) STORAGE(ON "MAIN", CLUSTERBTR) ;

COMMENT ON TABLE "JBOLT"."jb_wechat_autoreply" IS '微信公众号自动回复规则';
COMMENT ON COLUMN "JBOLT"."jb_wechat_autoreply"."create_time" IS '记录创建时间';
COMMENT ON COLUMN "JBOLT"."jb_wechat_autoreply"."enable" IS '是否启用';
COMMENT ON COLUMN "JBOLT"."jb_wechat_autoreply"."id" IS '主键ID';
COMMENT ON COLUMN "JBOLT"."jb_wechat_autoreply"."mp_id" IS '微信 ID';
COMMENT ON COLUMN "JBOLT"."jb_wechat_autoreply"."name" IS '规则名称';
COMMENT ON COLUMN "JBOLT"."jb_wechat_autoreply"."reply_type" IS '回复类型 全部还是随机一条';
COMMENT ON COLUMN "JBOLT"."jb_wechat_autoreply"."type" IS '类型 关注回复 无内容时回复 关键词回复';
COMMENT ON COLUMN "JBOLT"."jb_wechat_autoreply"."user_id" IS '用户 ID';


CREATE UNIQUE  INDEX "INDEX309698986870900" ON "JBOLT"."jb_wechat_autoreply"("id" ASC) STORAGE(ON "MAIN", CLUSTERBTR) ;

CREATE TABLE "JBOLT"."jb_wechat_config"
(
    "id" BIGINT NOT NULL,
    "mp_id" BIGINT,
    "config_key" VARCHAR(255),
    "config_value" VARCHAR(255),
    "name" VARCHAR(255),
    "type" INT,
    NOT CLUSTER PRIMARY KEY("id")) STORAGE(ON "MAIN", CLUSTERBTR) ;

COMMENT ON TABLE "JBOLT"."jb_wechat_config" IS '微信公众号配置表';
COMMENT ON COLUMN "JBOLT"."jb_wechat_config"."config_key" IS '配置key';
COMMENT ON COLUMN "JBOLT"."jb_wechat_config"."config_value" IS '配置值';
COMMENT ON COLUMN "JBOLT"."jb_wechat_config"."id" IS '主键ID';
COMMENT ON COLUMN "JBOLT"."jb_wechat_config"."mp_id" IS '微信 ID';
COMMENT ON COLUMN "JBOLT"."jb_wechat_config"."name" IS '配置项名称';
COMMENT ON COLUMN "JBOLT"."jb_wechat_config"."type" IS '配置类型';


CREATE UNIQUE  INDEX "INDEX309698969920200" ON "JBOLT"."jb_wechat_config"("id" ASC) STORAGE(ON "MAIN", CLUSTERBTR) ;

CREATE TABLE "JBOLT"."jb_wechat_keywords"
(
    "id" BIGINT NOT NULL,
    "mp_id" BIGINT,
    "name" VARCHAR(40),
    "type" INT DEFAULT 0,
    "auto_reply_id" BIGINT,
    NOT CLUSTER PRIMARY KEY("id")) STORAGE(ON "MAIN", CLUSTERBTR) ;

COMMENT ON TABLE "JBOLT"."jb_wechat_keywords" IS '微信关键词回复中的关键词定义';
COMMENT ON COLUMN "JBOLT"."jb_wechat_keywords"."auto_reply_id" IS '回复规则ID';
COMMENT ON COLUMN "JBOLT"."jb_wechat_keywords"."id" IS '主键ID';
COMMENT ON COLUMN "JBOLT"."jb_wechat_keywords"."mp_id" IS '微信 ID';
COMMENT ON COLUMN "JBOLT"."jb_wechat_keywords"."type" IS '模糊 全匹配';


CREATE UNIQUE  INDEX "INDEX309698958447200" ON "JBOLT"."jb_wechat_keywords"("id" ASC) STORAGE(ON "MAIN", CLUSTERBTR) ;

CREATE TABLE "JBOLT"."jb_wechat_media"
(
    "id" BIGINT NOT NULL,
    "title" VARCHAR(500),
    "digest" TEXT,
    "type" VARCHAR(255),
    "mp_id" BIGINT,
    "media_id" VARCHAR(255),
    "thumb_media_id" VARCHAR(255),
    "content_source_url" VARCHAR(255),
    "url" VARCHAR(255),
    "author" VARCHAR(255),
    "update_time" TIMESTAMP(0),
    "server_url" VARCHAR(255),
    NOT CLUSTER PRIMARY KEY("id")) STORAGE(ON "MAIN", CLUSTERBTR) ;

COMMENT ON TABLE "JBOLT"."jb_wechat_media" IS '微信公众平台素材库同步管理';
COMMENT ON COLUMN "JBOLT"."jb_wechat_media"."author" IS '图文作者';
COMMENT ON COLUMN "JBOLT"."jb_wechat_media"."content_source_url" IS '原文地址';
COMMENT ON COLUMN "JBOLT"."jb_wechat_media"."digest" IS '描述';
COMMENT ON COLUMN "JBOLT"."jb_wechat_media"."id" IS '主键ID';
COMMENT ON COLUMN "JBOLT"."jb_wechat_media"."media_id" IS '微信素材ID';
COMMENT ON COLUMN "JBOLT"."jb_wechat_media"."mp_id" IS '微信 ID';
COMMENT ON COLUMN "JBOLT"."jb_wechat_media"."server_url" IS '存服务器URL';
COMMENT ON COLUMN "JBOLT"."jb_wechat_media"."thumb_media_id" IS '封面图ID';
COMMENT ON COLUMN "JBOLT"."jb_wechat_media"."title" IS '标题';
COMMENT ON COLUMN "JBOLT"."jb_wechat_media"."type" IS '类型 image video voice news';
COMMENT ON COLUMN "JBOLT"."jb_wechat_media"."update_time" IS '更新时间';
COMMENT ON COLUMN "JBOLT"."jb_wechat_media"."url" IS '访问地址';


CREATE UNIQUE  INDEX "INDEX309698926454100" ON "JBOLT"."jb_wechat_media"("id" ASC) STORAGE(ON "MAIN", CLUSTERBTR) ;

CREATE TABLE "JBOLT"."jb_wechat_menu"
(
    "id" BIGINT NOT NULL,
    "mp_id" BIGINT,
    "type" VARCHAR(40),
    "name" VARCHAR(40),
    "pid" BIGINT DEFAULT 0,
    "sort_rank" INT,
    "value" VARCHAR(255),
    "app_id" VARCHAR(255),
    "page_path" VARCHAR(255),
    NOT CLUSTER PRIMARY KEY("id")) STORAGE(ON "MAIN", CLUSTERBTR) ;

COMMENT ON TABLE "JBOLT"."jb_wechat_menu" IS '微信菜单';
COMMENT ON COLUMN "JBOLT"."jb_wechat_menu"."app_id" IS '微信小程序APPID';
COMMENT ON COLUMN "JBOLT"."jb_wechat_menu"."id" IS '主键ID';
COMMENT ON COLUMN "JBOLT"."jb_wechat_menu"."mp_id" IS '微信 ID';
COMMENT ON COLUMN "JBOLT"."jb_wechat_menu"."page_path" IS '微信小程序页面地址';
COMMENT ON COLUMN "JBOLT"."jb_wechat_menu"."sort_rank" IS '排序';


CREATE UNIQUE  INDEX "INDEX309698910466000" ON "JBOLT"."jb_wechat_menu"("id" ASC) STORAGE(ON "MAIN", CLUSTERBTR) ;

CREATE TABLE "JBOLT"."jb_wechat_mpinfo"
(
    "id" BIGINT NOT NULL,
    "name" VARCHAR(255),
    "logo" VARCHAR(255),
    "create_time" TIMESTAMP(0),
    "user_id" BIGINT,
    "enable" CHAR(1) DEFAULT '0',
    "update_time" TIMESTAMP(0),
    "update_user_id" BIGINT,
    "brief_info" VARCHAR(255),
    "remark" VARCHAR(255),
    "type" INT,
    "is_authenticated" CHAR(1) DEFAULT '0',
    "subject_type" INT,
    "wechat_id" VARCHAR(255),
    "qrcode" VARCHAR(255),
    "link_app_id" BIGINT,
    NOT CLUSTER PRIMARY KEY("id")) STORAGE(ON "MAIN", CLUSTERBTR) ;

COMMENT ON TABLE "JBOLT"."jb_wechat_mpinfo" IS '微信公众号与小程序';
COMMENT ON COLUMN "JBOLT"."jb_wechat_mpinfo"."brief_info" IS '简介';
COMMENT ON COLUMN "JBOLT"."jb_wechat_mpinfo"."create_time" IS '创建时间';
COMMENT ON COLUMN "JBOLT"."jb_wechat_mpinfo"."enable" IS '是否启用';
COMMENT ON COLUMN "JBOLT"."jb_wechat_mpinfo"."id" IS '主键ID';
COMMENT ON COLUMN "JBOLT"."jb_wechat_mpinfo"."is_authenticated" IS '是否已认证';
COMMENT ON COLUMN "JBOLT"."jb_wechat_mpinfo"."link_app_id" IS '关联应用ID';
COMMENT ON COLUMN "JBOLT"."jb_wechat_mpinfo"."logo" IS '头像LOGO';
COMMENT ON COLUMN "JBOLT"."jb_wechat_mpinfo"."name" IS '平台名称';
COMMENT ON COLUMN "JBOLT"."jb_wechat_mpinfo"."qrcode" IS '二维码';
COMMENT ON COLUMN "JBOLT"."jb_wechat_mpinfo"."remark" IS '备注';
COMMENT ON COLUMN "JBOLT"."jb_wechat_mpinfo"."subject_type" IS '申请认证主体的类型 个人还是企业';
COMMENT ON COLUMN "JBOLT"."jb_wechat_mpinfo"."type" IS '类型 订阅号、服务号、小程序、企业号';
COMMENT ON COLUMN "JBOLT"."jb_wechat_mpinfo"."update_time" IS '创建时间';
COMMENT ON COLUMN "JBOLT"."jb_wechat_mpinfo"."update_user_id" IS '更新人ID';
COMMENT ON COLUMN "JBOLT"."jb_wechat_mpinfo"."user_id" IS '用户ID';
COMMENT ON COLUMN "JBOLT"."jb_wechat_mpinfo"."wechat_id" IS '微信号';


CREATE UNIQUE  INDEX "INDEX309698860272900" ON "JBOLT"."jb_wechat_mpinfo"("id" ASC) STORAGE(ON "MAIN", CLUSTERBTR) ;

CREATE TABLE "JBOLT"."jb_wechat_reply_content"
(
    "id" BIGINT NOT NULL,
    "type" VARCHAR(40) DEFAULT '0',
    "title" VARCHAR(64),
    "content" CLOB,
    "poster" VARCHAR(255),
    "url" VARCHAR(255),
    "auto_reply_id" BIGINT,
    "create_time" TIMESTAMP(0),
    "user_id" BIGINT,
    "media_id" VARCHAR(255),
    "mp_id" BIGINT,
    "sort_rank" INT,
    "enable" CHAR(1) DEFAULT '0',
    NOT CLUSTER PRIMARY KEY("id")) STORAGE(ON "MAIN", CLUSTERBTR) ;

COMMENT ON TABLE "JBOLT"."jb_wechat_reply_content" IS '自动回复内容';
COMMENT ON COLUMN "JBOLT"."jb_wechat_reply_content"."auto_reply_id" IS '回复规则ID';
COMMENT ON COLUMN "JBOLT"."jb_wechat_reply_content"."content" IS '内容';
COMMENT ON COLUMN "JBOLT"."jb_wechat_reply_content"."create_time" IS '创建时间';
COMMENT ON COLUMN "JBOLT"."jb_wechat_reply_content"."enable" IS '是否启用';
COMMENT ON COLUMN "JBOLT"."jb_wechat_reply_content"."id" IS '主键ID';
COMMENT ON COLUMN "JBOLT"."jb_wechat_reply_content"."media_id" IS '关联数据';
COMMENT ON COLUMN "JBOLT"."jb_wechat_reply_content"."mp_id" IS '微信 ID';
COMMENT ON COLUMN "JBOLT"."jb_wechat_reply_content"."poster" IS '封面';
COMMENT ON COLUMN "JBOLT"."jb_wechat_reply_content"."sort_rank" IS '排序';
COMMENT ON COLUMN "JBOLT"."jb_wechat_reply_content"."title" IS '标题';
COMMENT ON COLUMN "JBOLT"."jb_wechat_reply_content"."type" IS '类型 图文 文字 图片 音频 视频';
COMMENT ON COLUMN "JBOLT"."jb_wechat_reply_content"."url" IS '地址';
COMMENT ON COLUMN "JBOLT"."jb_wechat_reply_content"."user_id" IS '用户 ID';


CREATE UNIQUE  INDEX "INDEX309698834758500" ON "JBOLT"."jb_wechat_reply_content"("id" ASC) STORAGE(ON "MAIN", CLUSTERBTR) ;

CREATE TABLE "JBOLT"."jb_wechat_user"
(
    "id" BIGINT NOT NULL,
    "nickname" VARCHAR(255),
    "open_id" VARCHAR(255),
    "union_id" VARCHAR(255),
    "sex" INT DEFAULT 0,
    "language" VARCHAR(255),
    "subscibe" CHAR(1) DEFAULT '0',
    "country" VARCHAR(255),
    "province" VARCHAR(255),
    "city" VARCHAR(255),
    "head_img_url" VARCHAR(255),
    "subscribe_time" TIMESTAMP(0),
    "remark" VARCHAR(255),
    "group_id" INT,
    "tag_ids" VARCHAR(255),
    "subscribe_scene" VARCHAR(255),
    "qr_scene" INT,
    "qr_scene_str" VARCHAR(255),
    "realname" VARCHAR(255),
    "phone" VARCHAR(255),
    "phone_country_code" VARCHAR(40),
    "check_code" VARCHAR(255),
    "is_checked" CHAR(1) DEFAULT '0',
    "source" INT,
    "session_key" VARCHAR(255),
    "enable" CHAR(1) DEFAULT '0',
    "create_time" TIMESTAMP(0),
    "checked_time" TIMESTAMP(0),
    "mp_id" BIGINT,
    "weixin" VARCHAR(255),
    "update_time" TIMESTAMP(0),
    "last_login_time" TIMESTAMP(0),
    "first_auth_time" TIMESTAMP(0),
    "last_auth_time" TIMESTAMP(0),
    "first_login_time" TIMESTAMP(0),
    "bind_user" VARCHAR(255),
    NOT CLUSTER PRIMARY KEY("id")) STORAGE(ON "MAIN", CLUSTERBTR) ;

COMMENT ON TABLE "JBOLT"."jb_wechat_user" IS '微信用户信息_模板表';
COMMENT ON COLUMN "JBOLT"."jb_wechat_user"."bind_user" IS '绑定其他用户信息';
COMMENT ON COLUMN "JBOLT"."jb_wechat_user"."check_code" IS '手机验证码';
COMMENT ON COLUMN "JBOLT"."jb_wechat_user"."checked_time" IS '验证绑定手机号时间';
COMMENT ON COLUMN "JBOLT"."jb_wechat_user"."city" IS '城市';
COMMENT ON COLUMN "JBOLT"."jb_wechat_user"."country" IS '国家';
COMMENT ON COLUMN "JBOLT"."jb_wechat_user"."create_time" IS '创建时间';
COMMENT ON COLUMN "JBOLT"."jb_wechat_user"."enable" IS '禁用访问';
COMMENT ON COLUMN "JBOLT"."jb_wechat_user"."first_auth_time" IS '首次授权时间';
COMMENT ON COLUMN "JBOLT"."jb_wechat_user"."first_login_time" IS '首次登录时间';
COMMENT ON COLUMN "JBOLT"."jb_wechat_user"."group_id" IS '分组';
COMMENT ON COLUMN "JBOLT"."jb_wechat_user"."head_img_url" IS '头像';
COMMENT ON COLUMN "JBOLT"."jb_wechat_user"."id" IS '主键ID';
COMMENT ON COLUMN "JBOLT"."jb_wechat_user"."is_checked" IS '是否已验证';
COMMENT ON COLUMN "JBOLT"."jb_wechat_user"."language" IS '语言';
COMMENT ON COLUMN "JBOLT"."jb_wechat_user"."last_auth_time" IS '最后一次更新授权时间';
COMMENT ON COLUMN "JBOLT"."jb_wechat_user"."last_login_time" IS '最后登录时间';
COMMENT ON COLUMN "JBOLT"."jb_wechat_user"."mp_id" IS '所属公众平台ID';
COMMENT ON COLUMN "JBOLT"."jb_wechat_user"."nickname" IS '用户昵称';
COMMENT ON COLUMN "JBOLT"."jb_wechat_user"."open_id" IS 'openId';
COMMENT ON COLUMN "JBOLT"."jb_wechat_user"."phone" IS '手机号';
COMMENT ON COLUMN "JBOLT"."jb_wechat_user"."phone_country_code" IS '手机号国家代码';
COMMENT ON COLUMN "JBOLT"."jb_wechat_user"."province" IS '省';
COMMENT ON COLUMN "JBOLT"."jb_wechat_user"."qr_scene" IS '二维码场景-开发者自定义';
COMMENT ON COLUMN "JBOLT"."jb_wechat_user"."qr_scene_str" IS '二维码扫码场景描述-开发者自定义';
COMMENT ON COLUMN "JBOLT"."jb_wechat_user"."realname" IS '真实姓名';
COMMENT ON COLUMN "JBOLT"."jb_wechat_user"."remark" IS '备注';
COMMENT ON COLUMN "JBOLT"."jb_wechat_user"."session_key" IS '小程序登录SessionKey';
COMMENT ON COLUMN "JBOLT"."jb_wechat_user"."sex" IS '性别 1男 2女 0 未知';
COMMENT ON COLUMN "JBOLT"."jb_wechat_user"."source" IS '来源 小程序还是公众平台';
COMMENT ON COLUMN "JBOLT"."jb_wechat_user"."subscibe" IS '是否已关注';
COMMENT ON COLUMN "JBOLT"."jb_wechat_user"."subscribe_scene" IS '关注渠道';
COMMENT ON COLUMN "JBOLT"."jb_wechat_user"."subscribe_time" IS '关注时间';
COMMENT ON COLUMN "JBOLT"."jb_wechat_user"."tag_ids" IS '标签';
COMMENT ON COLUMN "JBOLT"."jb_wechat_user"."union_id" IS 'unionID';
COMMENT ON COLUMN "JBOLT"."jb_wechat_user"."update_time" IS '更新时间';
COMMENT ON COLUMN "JBOLT"."jb_wechat_user"."weixin" IS '微信号';


CREATE UNIQUE  INDEX "PRIMARY" ON "JBOLT"."jb_wechat_user"("id" ASC) STORAGE(ON "MAIN", CLUSTERBTR) ;

