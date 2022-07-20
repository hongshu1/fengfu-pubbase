alter table `jb_dept` add column `dept_path` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '部门路径';
alter table `jb_user` add column `dept_path` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '部门路径';
alter table `jb_user` add column `update_time` datetime DEFAULT NULL COMMENT '更新时间';
alter table `jb_user` add column `update_user_id` bigint(20) DEFAULT NULL COMMENT '更新人ID';