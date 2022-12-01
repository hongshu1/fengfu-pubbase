alter table jb_dictionary_type add column is_build_in char(1) DEFAULT '0' COMMENT '是否内置';
alter table jb_dictionary add column is_build_in char(1) DEFAULT '0' COMMENT '是否内置';
