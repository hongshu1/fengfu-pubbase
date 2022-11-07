alter table `jb_user` add column `age` int(11) DEFAULT 0 COMMENT '年龄';
alter table `jb_user` add column `sn` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '工号';
