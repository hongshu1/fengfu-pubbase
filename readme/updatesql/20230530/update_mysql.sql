alter table `jb_code_gen` add column `is_gen_options_action` char(1) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '是否生成options接口';
alter table `jb_code_gen` add column `is_return_option_type` char(1) COLLATE utf8mb4_general_ci DEFAULT '1' COMMENT '是否返回Option类型';
alter table `jb_code_gen` add column `options_text_column` varchar(40) COLLATE utf8mb4_general_ci DEFAULT 'name' COMMENT 'options接口text用哪一列';
alter table `jb_code_gen` add column `options_value_column` varchar(40) COLLATE utf8mb4_general_ci DEFAULT 'id' COMMENT 'options接口value用哪一列';
alter table `jb_code_gen` add column `is_gen_autocomplete_action` char(1) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '是否生成Autocomplete接口';
alter table `jb_code_gen` add column `autocomplete_limit` int(10) NOT NULL DEFAULT '20' COMMENT 'autocomplete接口limit';
alter table `jb_code_gen` add column `autocomplete_match_columns` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'autocomplete接口匹配字段';
alter table `jb_code_gen_model_attr` add column `is_need_check_exists` char(1) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '是否检测数据重复字段';
