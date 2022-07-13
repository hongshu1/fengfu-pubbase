-- ----------------------------
-- Table structure for jb_code_gen
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[jb_code_gen]') AND type IN ('U'))
DROP TABLE [dbo].[jb_code_gen]
    GO

CREATE TABLE [dbo].[jb_code_gen] (
    [id] bigint  NOT NULL,
    [pid] bigint DEFAULT 0 NULL,
    [project_path] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NOT NULL,
    [is_sub_table] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [sort_rank] int DEFAULT 1 NOT NULL,
    [type] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NOT NULL,
    [main_table_name] nvarchar(150) COLLATE Chinese_PRC_CI_AS  NOT NULL,
    [datasource_name] nvarchar(100) COLLATE Chinese_PRC_CI_AS  NOT NULL,
    [datasource_remark] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NOT NULL,
    [database_type] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NOT NULL,
    [is_main_datasource] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [main_table_pkey] nvarchar(64) COLLATE Chinese_PRC_CI_AS DEFAULT 'id' NULL,
    [table_remove_prefix] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NULL,
    [main_table_idgenmode] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NOT NULL,
    [version_sn] int  NOT NULL,
    [main_table_remark] nvarchar(100) COLLATE Chinese_PRC_CI_AS  NULL,
    [author] nvarchar(100) COLLATE Chinese_PRC_CI_AS  NOT NULL,
    [style] int  NOT NULL,
    [is_crud] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [is_editable] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [is_tree_table] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [is_check_can_delete] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [is_check_can_toggle] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [is_check_can_recover] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [editable_submit_type] nvarchar(20) COLLATE Chinese_PRC_CI_AS  NULL,
    [state] int  NOT NULL,
    [sub_table_count] int  NULL,
    [topnav_id] bigint  NULL,
    [permission_id] bigint  NULL,
    [roles] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
    [create_user_id] bigint  NOT NULL,
    [create_time] datetime2(7)  NOT NULL,
    [update_user_id] bigint  NOT NULL,
    [update_time] datetime2(7)  NOT NULL,
    [gen_user_id] bigint  NULL,
    [gen_time] datetime2(7)  NULL,
    [model_name] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
    [base_model_name] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
    [remark] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
    [is_auto_cache] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [is_id_cache] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [is_key_cache] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [key_cache_column] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NULL,
    [key_cache_bind_column] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NULL,
    [controller_name] nvarchar(100) COLLATE Chinese_PRC_CI_AS  NOT NULL,
    [controller_path] nvarchar(100) COLLATE Chinese_PRC_CI_AS  NOT NULL,
    [main_java_package] nvarchar(100) COLLATE Chinese_PRC_CI_AS  NOT NULL,
    [service_name] nvarchar(100) COLLATE Chinese_PRC_CI_AS  NOT NULL,
    [controller_package] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NOT NULL,
    [service_package] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NOT NULL,
    [index_html_page_icon] nvarchar(60) COLLATE Chinese_PRC_CI_AS  NULL,
    [index_html_page_title] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NULL,
    [model_package] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
    [html_view_path] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
    [routes_scan_package] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
    [is_gen_model] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '1' NOT NULL,
    [is_table_use_record] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [is_table_record_camel_case] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [is_import_excel] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [is_export_excel] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [is_export_excel_by_checked_ids] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [is_export_excel_by_form] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '1' NOT NULL,
    [is_export_excel_all] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [is_copy_to_excel] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [is_copy_from_excel] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [is_toolbar] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [is_headbox] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [is_leftbox] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [is_rightbox] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [is_footbox] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [is_paginate] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '1' NOT NULL,
    [is_table_sortable_move] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [leftbox_width] int DEFAULT 220  NULL,
    [rightbox_width] int DEFAULT 220  NULL,
    [headbox_height] int DEFAULT 40  NULL,
    [footbox_height] int DEFAULT 220  NULL,
    [is_leftbox_footer] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '1' NOT NULL,
    [is_rightbox_footer] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '1' NOT NULL,
    [leftbox_footer_button_count] int  NULL,
    [rightbox_footer_button_count] int  NULL,
    [leftbox_title] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NULL,
    [leftbox_icon] nvarchar(60) COLLATE Chinese_PRC_CI_AS  NULL,
    [rightbox_title] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NULL,
    [rightbox_icon] nvarchar(60) COLLATE Chinese_PRC_CI_AS  NULL,
    [is_show_optcol_sort] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [is_show_optcol_edit] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [is_show_optcol_del] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [is_show_optcol] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '1' NOT NULL,
    [is_show_optcol_recover] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [default_sort_column] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NULL,
    [default_sort_type] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NULL,
    [table_optcol_width] int DEFAULT 80 NOT NULL,
    [is_table_column_resize] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [is_table_prepend_column] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [table_prepend_column_type] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NULL,
    [table_prepend_column_index] int DEFAULT 1 NOT NULL,
    [is_table_prepend_column_linkparent] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [is_table_prepend_column_linkson] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [table_prepend_column_rowspan] int DEFAULT 1 NOT NULL,
    [is_table_prepend_column_click_active] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [table_fixed_column_left] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NULL,
    [table_fixed_column_right] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NULL,
    [table_pagesize_options] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
    [table_width_assign] nvarchar(40) COLLATE Chinese_PRC_CI_AS NULL,
    [table_width] nvarchar(40) COLLATE Chinese_PRC_CI_AS  DEFAULT 'fill' NOT NULL,
    [table_height_assign] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NULL,
    [table_height] nvarchar(40) COLLATE Chinese_PRC_CI_AS DEFAULT 'fill' NOT NULL,
    [table_default_sort_column] nvarchar(60) COLLATE Chinese_PRC_CI_AS DEFAULT 'id' NULL,
    [table_default_sort_type] nvarchar(40) COLLATE Chinese_PRC_CI_AS  DEFAULT 'desc' NULL,
    [is_keywords_search] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [keywords_match_columns] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
    [toolbar_extra_button_size] int  NULL,
    [is_deleted] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [form_column_size] int DEFAULT 1 NOT NULL,
    [is_form_group_row] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '1' NOT NULL,
    [form_column_proportion] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NULL,
    [form_column_direction] nvarchar(10) COLLATE Chinese_PRC_CI_AS DEFAULT 'v' NULL,
    [form_group_proportion] nvarchar(40) COLLATE Chinese_PRC_CI_AS DEFAULT '2:10'  NULL,
    [is_view_use_path] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '1' NOT NULL,
    [view_layout] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NOT NULL,
    [is_need_new_route] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [routes_class_name] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
    [is_need_admin_interceptor] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '1' NOT NULL,
    [extra_interceptor_class_name] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
    [is_table_multi_conditions_mode] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [is_toolbar_add_btn] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '1' NOT NULL,
    [is_toolbar_edit_btn] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '1' NOT NULL,
    [is_toolbar_del_btn] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '1' NOT NULL,
    [is_toolbar_recover_btn] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [is_toolbar_refresh_btn] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '1' NOT NULL,
    [is_page_title_add_btn] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '1' NOT NULL,
    [is_page_title_refresh_btn] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '1' NOT NULL,
    [is_page_title_init_rank_btn] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [is_project_system_log] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [project_system_log_target_type_text] nvarchar(20) COLLATE Chinese_PRC_CI_AS  NULL,
    [project_system_log_target_type_value] nvarchar(10) COLLATE Chinese_PRC_CI_AS  NULL,
    [project_system_log_target_type_key_name] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NULL,
    [form_dialog_area] nvarchar(20) COLLATE Chinese_PRC_CI_AS DEFAULT '800,600' NULL
    )
    GO

ALTER TABLE [dbo].[jb_code_gen] SET (LOCK_ESCALATION = TABLE)
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'主键ID',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'id'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'父ID',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'pid'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'项目根路径',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'project_path'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否为子表',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_sub_table'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'子表的顺序',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'sort_rank'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'模块类型',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'type'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'主表名',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'main_table_name'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'数据源',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'datasource_name'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'数据源说明',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'datasource_remark'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'数据库类型',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'database_type'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否为主数据源',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_main_datasource'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'主表主键',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'main_table_pkey'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'数据表删除前缀',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'table_remove_prefix'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'主表主键策略',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'main_table_idgenmode'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'版本序号',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'version_sn'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'表备注',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'main_table_remark'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'功能作者',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'author'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'样式类型',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'style'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否CRUD',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_crud'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否可编辑表格',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_editable'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否为树表',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_tree_table'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'检测是否可以刪除数据',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_check_can_delete'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'检测是否可以toggle数据',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_check_can_toggle'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'检测是否可以recover数据',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_check_can_recover'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'可编辑提交方式',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'editable_submit_type'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'生成状态',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'state'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'子表数',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'sub_table_count'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'顶部导航',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'topnav_id'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'关联权限',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'permission_id'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'可访问角色',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'roles'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'创建人ID',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'create_user_id'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'创建时间',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'create_time'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'更新人ID',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'update_user_id'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'更新时间',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'update_time'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'创建后执行生成人ID',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'gen_user_id'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'创建后执行生成时间',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'gen_time'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'modelName',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'model_name'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'baseModelName',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'base_model_name'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'备注',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'remark'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否启用autoCache',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_auto_cache'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否启用idCache',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_id_cache'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否启用keyCache',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_key_cache'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'keyCache指定Column',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'key_cache_column'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'keyCache指定bindColumn',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'key_cache_bind_column'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'Controller Name',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'controller_name'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'controller path',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'controller_path'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'java主包pacakge',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'main_java_package'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'Service Name',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'service_name'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'Controller代码包',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'controller_package'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'Service代码包',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'service_package'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'index.html标题icon',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'index_html_page_icon'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'index.html页面标题',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'index_html_page_title'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'model 所属package',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'model_package'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'html view path',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'html_view_path'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'路由扫描包',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'routes_scan_package'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否需要生成Model',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_gen_model'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'表格是否使用record',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_table_use_record'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'表格列名用驼峰的attrName',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_table_record_camel_case'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否支持Excel导入',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_import_excel'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否支持Excel导出',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_export_excel'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否启用 导出选中行功能',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_export_excel_by_checked_ids'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否启用导出表单查询结果功能',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_export_excel_by_form'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否启用导出所有数据',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_export_excel_all'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否支持表格复制到excel',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_copy_to_excel'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否支持从excel复制到可编辑表格',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_copy_from_excel'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否使用toolbar模式',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_toolbar'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否使用headbox',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_headbox'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否使用leftBox',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_leftbox'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否使用rightBox',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_rightbox'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否使用footbox',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_footbox'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否分页查询',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_paginate'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否开启移动排序功能',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_table_sortable_move'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'leftbox width',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'leftbox_width'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'right width',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'rightbox_width'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'headbox height',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'headbox_height'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'footbox height',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'footbox_height'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否启用leftbox的footer',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_leftbox_footer'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否启用rightbox的footer',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_rightbox_footer'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'leftbox footer button count',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'leftbox_footer_button_count'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'rightbox footer button count',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'rightbox_footer_button_count'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'leftbox title',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'leftbox_title'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'leftbox icon',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'leftbox_icon'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'rightbox title',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'rightbox_title'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'rightbox icon',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'rightbox_icon'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否开启操作列排序功能',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_show_optcol_sort'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否开启操作列显示编辑按钮',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_show_optcol_edit'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否开启操作列显示删除按钮',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_show_optcol_del'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否显示操作列',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_show_optcol'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否显示操作列的恢复按钮',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_show_optcol_recover'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'默认排序字段',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'default_sort_column'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'默认排序方式',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'default_sort_type'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'长度',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'table_optcol_width'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'表格是否开启调整列宽功能',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_table_column_resize'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否增加填充列',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_table_prepend_column'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'表格chechbox radio配置类型',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'table_prepend_column_type'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'填充列到第几列',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'table_prepend_column_index'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'填充列linkparent',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_table_prepend_column_linkparent'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'填充列linkson',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_table_prepend_column_linkson'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'填充列表头是几行rowspan',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'table_prepend_column_rowspan'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否点击行就切换列填充组件选中状态',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_table_prepend_column_click_active'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'左侧固定列',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'table_fixed_column_left'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'右侧固定列',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'table_fixed_column_right'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'分页pagesize自定义设置',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'table_pagesize_options'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'表格宽度自定义值',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'table_width_assign'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'表格宽度',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'table_width'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'表格高度自定义值',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'table_height_assign'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'表格高度',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'table_height'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'表格默认排序字段',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'table_default_sort_column'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'表格默认排序类型',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'table_default_sort_type'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否开启关键词查询',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_keywords_search'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'关键词匹配列',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'keywords_match_columns'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'toolbar 额外预留按钮个数',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'toolbar_extra_button_size'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'删除标识',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_deleted'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'表单分几列',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'form_column_size'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'表单form-group风格 左右还是上下',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_form_group_row'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'表单分多列 比例值',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'form_column_proportion'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'表单列排布方向 横向还是纵向',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'form_column_direction'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'form-group row状态下的比例',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'form_group_proportion'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否启用Path注解路由',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_view_use_path'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'使用布局器',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'view_layout'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否需要创建新的路由配置类',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_need_new_route'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'哪个路由配置类',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'routes_class_name'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否需要后台管理权限拦截器',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_need_admin_interceptor'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'额外配置的拦截器',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'extra_interceptor_class_name'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'表格查询条件是否启用高级多条件模式',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_table_multi_conditions_mode'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'表格toolbar上启用添加按钮',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_toolbar_add_btn'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'表格toolbar上启用编辑按钮',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_toolbar_edit_btn'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'表格toolbar上启用删除按钮',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_toolbar_del_btn'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'表格toolbar上启用恢复按钮 当有is_deleted时',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_toolbar_recover_btn'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'表格tolbar上启用刷新按钮',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_toolbar_refresh_btn'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否启用pageTitle上的添加按钮',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_page_title_add_btn'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否启用pageTitle上的刷新按钮',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_page_title_refresh_btn'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否启用pageTitle上的初始化顺序按钮',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_page_title_init_rank_btn'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否启用systemLog日志',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_project_system_log'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'系统日志text',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'project_system_log_target_type_text'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'系统日志value值',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'project_system_log_target_type_value'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'系统日志KeyName',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'project_system_log_target_type_key_name'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'form表单的dialog的area属性 长宽',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'form_dialog_area'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'代码生成配置',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen'
    GO


    -- ----------------------------
-- Table structure for jb_code_gen_model_attr
-- ----------------------------
    IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[jb_code_gen_model_attr]') AND type IN ('U'))
DROP TABLE [dbo].[jb_code_gen_model_attr]
    GO

CREATE TABLE [dbo].[jb_code_gen_model_attr] (
    [id] bigint  NOT NULL,
    [code_gen_id] bigint  NOT NULL,
    [col_name] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NOT NULL,
    [attr_name] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NOT NULL,
    [java_type] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NOT NULL,
    [attr_length] int DEFAULT 20 NOT NULL,
    [attr_fixed] int DEFAULT 0 NULL,
    [attr_default_value] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NULL,
    [sort_rank] int DEFAULT 1 NOT NULL,
    [sort_rank_intable] int DEFAULT 1 NOT NULL,
    [sort_rank_inform] int DEFAULT 1 NOT NULL,
    [sort_rank_insearch] int DEFAULT 1 NOT NULL,
    [is_pkey] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [is_required] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [is_search_required] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [data_rule_for_search] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
    [data_tips_for_search] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
    [form_label] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
    [placeholder] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NULL,
    [table_label] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NULL,
    [search_form_label] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NULL,
    [remark] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
    [is_keywords_column] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [is_form_ele] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '1' NOT NULL,
    [is_table_col] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '1' NOT NULL,
    [is_table_switchbtn] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [table_col_width] int DEFAULT 100 NOT NULL,
    [is_need_fixed_width] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '1' NOT NULL,
    [is_search_ele] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [is_search_hidden] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [col_format] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
    [search_ui_type] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NULL,
    [search_data_type] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NULL,
    [search_data_value] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
    [search_default_value] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
    [is_single_line] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [need_data_handler] nchar(1) COLLATE Chinese_PRC_CI_AS  NULL,
    [form_ui_type] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NULL,
    [form_jboltinput_filter_handler] nvarchar(60) COLLATE Chinese_PRC_CI_AS  NULL,
    [is_form_jboltinput_jstree_checkbox] nchar(1) COLLATE Chinese_PRC_CI_AS  NULL,
    [is_form_jboltinput_jstree_only_leaf] nchar(1) COLLATE Chinese_PRC_CI_AS  NULL,
    [form_data_type] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NULL,
    [form_data_value] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
    [form_default_value] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
    [data_rule_assign] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
    [data_rule] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
    [data_tips] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
    [is_import_col] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [is_export_col] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [is_sortable] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [table_ui_type] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NULL,
    [table_data_type] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NULL,
    [table_data_value] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
    [form_ele_width] int DEFAULT 0 NOT NULL,
    [is_item_inline] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '1' NOT NULL,
    [form_data_text_attr] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
    [form_data_value_attr] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NULL,
    [form_data_column_attr] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
    [search_data_text_attr] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
    [search_data_value_attr] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NULL,
    [search_data_column_attr] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
    [table_data_text_attr] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
    [table_data_value_attr] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NULL,
    [table_data_column_attr] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL
    )
    GO

ALTER TABLE [dbo].[jb_code_gen_model_attr] SET (LOCK_ESCALATION = TABLE)
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'主键ID',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'id'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'所属codeGen',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'code_gen_id'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'列名',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'col_name'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'属性名',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'attr_name'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'属性类型',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'java_type'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'属性长度',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'attr_length'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'属性小数点',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'attr_fixed'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'默认值',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'attr_default_value'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'数据表内默认顺序',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'sort_rank'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'表格中的排序',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'sort_rank_intable'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'表单中的排序',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'sort_rank_inform'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'查询条件中的顺序',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'sort_rank_insearch'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否主键',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'is_pkey'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否必填',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'is_required'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'作为查询条件是否必填',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'is_search_required'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'查询条件必填校验规则',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'data_rule_for_search'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'查询条件不符合校验的提示信息',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'data_tips_for_search'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'表单单显示文本',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'form_label'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'表单placeholder',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'placeholder'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'表格中显示文本',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'table_label'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'查询表单提示文本',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'search_form_label'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'备注',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'remark'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否为关键词查询列',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'is_keywords_column'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否表单可编辑元素',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'is_form_ele'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否表格列',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'is_table_col'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否为表格switchbtn',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'is_table_switchbtn'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'列宽',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'table_col_width'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否固定宽度',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'is_need_fixed_width'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否检索条件',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'is_search_ele'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否为检索隐藏条件',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'is_search_hidden'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'格式化操作值',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'col_format'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'查询用ui 组件类型',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'search_ui_type'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'查询用组件数据源类型',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'search_data_type'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'查询用组件数据值',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'search_data_value'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'查询用组件默认值',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'search_default_value'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'独立新行',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'is_single_line'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否需要data_handler',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'need_data_handler'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'表单组件类型',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'form_ui_type'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'jboltinput filter handler',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'form_jboltinput_filter_handler'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'jboltinput jstree是否有checkbox',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'is_form_jboltinput_jstree_checkbox'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'jboltinput jstree checkbox只选子节点',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'is_form_jboltinput_jstree_only_leaf'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'表单组件数据源类型',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'form_data_type'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'表单组件数据值',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'form_data_value'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'表单组件默认值',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'form_default_value'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'表单校验规则 自定义',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'data_rule_assign'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'校验规则',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'data_rule'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'校验提示信息',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'data_tips'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否为导入列',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'is_import_col'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'导出列',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'is_export_col'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否可排序',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'is_sortable'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'可编辑表格显示组件类型',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'table_ui_type'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'表格组件数据库类型',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'table_data_type'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'表格组件数据值',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'table_data_value'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'组件自定义宽度',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'form_ele_width'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'radio checkbox等是否inline',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'is_item_inline'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'data-text-attr',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'form_data_text_attr'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'data-value-attr',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'form_data_value_attr'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'data-column-attr',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'form_data_column_attr'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'data-text-attr',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'search_data_text_attr'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'data-value-attr',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'search_data_value_attr'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'data-column-attr',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'search_data_column_attr'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'data-text-attr',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'table_data_text_attr'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'data-value-attr',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'table_data_value_attr'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'data-column-attr',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'table_data_column_attr'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'CodeGen模型详细设计',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr'
    GO


-- ----------------------------
-- Primary Key structure for table jb_code_gen
-- ----------------------------
ALTER TABLE [dbo].[jb_code_gen] ADD CONSTRAINT [PK__jb_code___3213E83F4A6C6EF3] PRIMARY KEY CLUSTERED ([id])
    WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)
    ON [PRIMARY]
    GO


-- ----------------------------
-- Primary Key structure for table jb_code_gen_model_attr
-- ----------------------------
ALTER TABLE [dbo].[jb_code_gen_model_attr] ADD CONSTRAINT [PK__jb_code___3213E83F5BC641F6] PRIMARY KEY CLUSTERED ([id])
    WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)
    ON [PRIMARY]
    GO

