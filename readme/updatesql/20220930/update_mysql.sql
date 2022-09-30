CREATE TABLE `jb_sensitive_word` (
     `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
     `content` varchar(40) COLLATE utf8mb4_general_ci NOT NULL COMMENT '内容',
     `enable` char(1) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '启用状态',
     PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='敏感词词库';
