alter table `jb_user` add column `age` int(11) DEFAULT 0 COMMENT '年龄';
alter table `jb_user` add column `sn` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '工号';

CREATE TABLE `jb_user_extend` (
`id` bigint(20) NOT NULL COMMENT '主键ID',
`english_full_name` varchar(40) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '英文全名',
`birthday` datetime DEFAULT NULL COMMENT '出生日期',
`residential_address` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '居住地址',
`company_name` varchar(40) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '公司名称',
`company_address` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '公司地址',
`recipient_address` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '快递收件地址',
`recipient_phone` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '收件人电话',
`recipient_name` varchar(40) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '收件人姓名',
`id_number` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '身份证号',
`country` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '国家',
`nation` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '民族',
`province` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '省',
`city` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '城市',
`city_code` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '城市代码',
`county` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '区县',
`township` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '乡镇',
`community` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '行政村 社区',
`marital_status` int(11) DEFAULT NULL COMMENT '婚姻状态',
`is_cpc_member` char(1) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '是否党员',
`is_cyl_member` char(1) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '是否共青团员',
`professional_title` int(11) DEFAULT NULL COMMENT '职称',
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户扩展信息表';

