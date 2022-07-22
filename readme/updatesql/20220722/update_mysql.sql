alter table `jb_code_gen_model_attr` add column `is_need_translate` char(1) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '是否需要翻译';
alter table `jb_code_gen_model_attr` add column `translate_type` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '翻译类型';
alter table `jb_code_gen_model_attr` add column `translate_use_value` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '翻译用值';
alter table `jb_code_gen_model_attr` add column `translate_col_name` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '翻译后的列名';