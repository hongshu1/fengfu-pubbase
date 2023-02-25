alter table `jb_user` add column `of_module` int(11) NOT NULL DEFAULT '1' COMMENT '哪个模块';
alter table `jb_user` add column `of_module_link` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '具体指向关联';
alter table `jb_permission` add column `of_module` int(11) NOT NULL DEFAULT '1' COMMENT '哪个模块';
alter table `jb_permission` add column `of_module_link` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '具体指向关联';