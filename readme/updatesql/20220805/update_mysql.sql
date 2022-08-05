alter table `jb_code_gen` add column `is_base_model_gen_col_constant` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '是否在baseModel中生成字段常量';
alter table `jb_code_gen` add column `is_base_model_gen_col_constant_to_uppercase` char(1) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '是否在baseModel中生成的字段常量 名称转大写';
alter table `jb_code_gen` add column `is_table_multi_conditions_btn_show_title` char(1) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '表格高级查询条件切换按钮是否显示标题';