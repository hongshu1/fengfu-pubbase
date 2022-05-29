/*
 Navicat Premium Data Transfer

 Source Server         : mysql8
 Source Server Type    : MySQL
 Source Server Version : 80016
 Source Host           : localhost:3306
 Source Schema         : jbolt_platform_mini

 Target Server Type    : MySQL
 Target Server Version : 80016
 File Encoding         : 65001

 Date: 17/08/2020 05:41:59
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for jb_application
-- ----------------------------
DROP TABLE IF EXISTS `jb_application`;
CREATE TABLE `jb_application`  (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '应用名称',
  `brief_info` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '应用简介',
  `app_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '应用ID',
  `app_secret` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '应用密钥',
  `enable` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '是否启用',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '创建用户ID',
  `update_user_id` bigint(20) NULL DEFAULT NULL COMMENT '更新用户ID',
  `type` int(11) NOT NULL COMMENT 'app类型',
  `need_check_sign` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '是否需要接口校验SIGN',
  `is_inner` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '是否需要接口校验SIGN',
  `link_target_id` bigint(20) NULL DEFAULT NULL COMMENT '关联目标ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'API应用中心的应用APP' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for jb_dictionary
-- ----------------------------
DROP TABLE IF EXISTS `jb_dictionary`;
CREATE TABLE `jb_dictionary`  (
  `id` bigint(20) NOT NULL COMMENT '字典ID主键',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
  `type_id` bigint(20) NULL DEFAULT NULL COMMENT '字典类型ID',
  `pid` bigint(20) NULL DEFAULT NULL COMMENT '父类ID',
  `sort_rank` int(11) NULL DEFAULT NULL COMMENT '排序',
  `sn` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '编号编码',
  `type_key` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字典类型KEY',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字典表' ROW_FORMAT = Dynamic;


-- ----------------------------
-- Table structure for jb_dictionary_type
-- ----------------------------
DROP TABLE IF EXISTS `jb_dictionary_type`;
CREATE TABLE `jb_dictionary_type`  (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `mode_level` int(11) NULL DEFAULT NULL COMMENT '模式层级',
  `type_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标识KEY',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字典类型' ROW_FORMAT = Dynamic;

 
-- ----------------------------
-- Table structure for jb_global_config
-- ----------------------------
DROP TABLE IF EXISTS `jb_global_config`;
CREATE TABLE `jb_global_config`  (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `config_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '配置KEY',
  `config_value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '配置项值',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '创建用户ID',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `update_user_id` bigint(20) NULL DEFAULT NULL COMMENT '更新用户ID',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `value_type` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '值类型',
  `type_key` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '类型key',
  `type_id` bigint(20) DEFAULT NULL COMMENT '类型ID',
  `built_in` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '内置参数',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '全局配置' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for jb_global_config_type
-- ----------------------------
DROP TABLE IF EXISTS `jb_global_config_type`;
CREATE TABLE `jb_global_config_type` (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `name` varchar(40) COLLATE utf8mb4_general_ci NOT NULL COMMENT '类型名称',
  `sort_rank` int(11) DEFAULT NULL COMMENT '序号',
  `type_key` varchar(40) COLLATE utf8mb4_general_ci NOT NULL COMMENT '类型KEY',
  `built_in` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '内置',
  PRIMARY KEY (`id`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT='全局参数类型分组';


-- ----------------------------
-- Table structure for jb_jbolt_file
-- ----------------------------
DROP TABLE IF EXISTS `jb_jbolt_file`;
CREATE TABLE `jb_jbolt_file`  (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `local_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '保存物理地址',
  `local_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '本地可访问URL地址',
  `cdn_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '外部CDN地址',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户ID',
  `file_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件名',
  `file_type` int(11) NULL DEFAULT NULL COMMENT '文件类型 图片 附件 视频 音频',
  `file_size` int(11) NULL DEFAULT 0 COMMENT '文件大小',
  `file_suffix` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '后缀名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '文件库' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for jb_login_log
-- ----------------------------
DROP TABLE IF EXISTS `jb_login_log`;
CREATE TABLE `jb_login_log`  (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户ID',
  `username` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
  `login_ip` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'IP地址',
  `login_time` datetime(0) NOT NULL COMMENT '登录时间',
  `login_state` int(11) NOT NULL COMMENT '登录状态',
  `is_browser` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '是否是浏览器访问',
  `browser_version` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '浏览器版本号',
  `browser_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '浏览器',
  `os_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作系统',
  `login_city` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '登录城市',
  `login_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '登录位置详情',
  `login_city_code` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '登录城市代码',
  `login_province` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '登录省份',
  `login_country` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '登录国家',
  `is_mobile` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '是否移动端',
  `os_platform_name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '平台名称',
  `browser_engine_name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '浏览器引擎名',
  `browser_engine_version` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '浏览器引擎版本',
  `login_address_latitude` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '登录地横坐标',
  `login_address_longitude` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '登录地纵坐标',
  `is_remote_login` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '是否为异地异常登录',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '记录创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '登录日志' ROW_FORMAT = Dynamic;

 
-- ----------------------------
-- Table structure for jb_online_user
-- ----------------------------
DROP TABLE IF EXISTS `jb_online_user`;
CREATE TABLE `jb_online_user`  (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `session_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '会话ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `login_log_id` bigint(20) NOT NULL COMMENT '登录日志ID',
  `login_time` datetime(0) NULL DEFAULT NULL COMMENT '登录时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `expiration_time` datetime NOT NULL COMMENT '过期时间',
  `screen_locked` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '是否锁屏',
  `online_state` int(11) NOT NULL COMMENT '在线状态',
  `offline_time` datetime DEFAULT NULL COMMENT '离线时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '在线用户' ROW_FORMAT = Dynamic;


-- ----------------------------
-- Table structure for jb_permission
-- ----------------------------
DROP TABLE IF EXISTS `jb_permission`;
CREATE TABLE `jb_permission`  (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `title` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `pid` bigint(20) NULL DEFAULT 0 NOT NULL,
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '地址',
  `icons` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图标',
  `sort_rank` int(11) NULL DEFAULT NULL COMMENT '排序',
  `permission_level` int(11) NULL DEFAULT NULL COMMENT '层级',
  `permission_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '权限资源KEY',
  `is_menu` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '是否是菜单',
  `is_target_blank` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '是否新窗口打开',
  `is_system_admin_default` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '是否系统超级管理员默认拥有的权限',
  `open_type` int(11) NULL DEFAULT 1 COMMENT '打开类型 1 默认 2 iframe 3 dialog',
  `open_option` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组件属性json',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'function定义' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for jb_remote_login_log
-- ----------------------------
DROP TABLE IF EXISTS `jb_remote_login_log`;
CREATE TABLE `jb_remote_login_log`  (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `last_login_country` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '最近一次登录国家',
  `last_login_province` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '最近一次登录省份',
  `last_login_city` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '最近一次登录城市',
  `last_login_time` datetime(0) NULL DEFAULT NULL COMMENT '最近一次登录时间',
  `login_country` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '新登录国家',
  `login_province` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '新登录省份',
  `login_city` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '新登录城市',
  `login_time` datetime(0) NULL DEFAULT NULL COMMENT '新登录时间',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '登录用户ID',
  `username` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '登录用户名',
  `is_new` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '是否为最新一次',
  `last_login_ip` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '最近一次登录IP',
  `login_ip` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '新登录IP',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '记录创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '异地登录日志记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for jb_role
-- ----------------------------
DROP TABLE IF EXISTS `jb_role`;
CREATE TABLE `jb_role`  (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
  `sn` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '编码',
  `pid` bigint(20) NULL DEFAULT 0 COMMENT '父级角色ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色表' ROW_FORMAT = Dynamic;



-- ----------------------------
-- Table structure for jb_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `jb_role_permission`;
CREATE TABLE `jb_role_permission`  (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `permission_id` bigint(20) NOT NULL COMMENT '权限ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色功能列表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for jb_system_log
-- ----------------------------
DROP TABLE IF EXISTS `jb_system_log`;
CREATE TABLE `jb_system_log`  (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标题',
  `type` int(11) NULL DEFAULT NULL COMMENT '操作类型 删除 更新 新增',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '连接对象详情地址',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '操作人ID',
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作人姓名',
  `target_type` int(11) NULL DEFAULT NULL COMMENT '操作对象类型',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '记录创建时间',
  `target_id` bigint(20) NULL DEFAULT NULL COMMENT '操作对象ID',
  `open_type` int(11) NULL DEFAULT NULL COMMENT '打开类型URL还是Dialog',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统操作日志表' ROW_FORMAT = Dynamic;


-- ----------------------------
-- Table structure for jb_dept
-- ----------------------------
DROP TABLE IF EXISTS `jb_dept`;
CREATE TABLE `jb_dept` (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
  `full_name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '全称',
  `pid` bigint(20) DEFAULT NULL COMMENT '父级ID',
  `type` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '类型',
  `leader` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '负责人',
  `phone` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '联系电话',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '电子邮箱',
  `address` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '联系地址',
  `zipcode` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '邮政编码',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注信息',
  `sort_rank` int(11) DEFAULT NULL COMMENT '顺序',
  `sn` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '机构代码',
  `enable` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '启用/禁用',
  `update_time` datetime DEFAULT NULL COMMENT '最后更新时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT='组织机构';
-- ----------------------------
-- Table structure for jb_post
-- ----------------------------
DROP TABLE IF EXISTS `jb_post`;
CREATE TABLE `jb_post` (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '岗位名称',
  `type` varchar(40) COLLATE utf8mb4_general_ci NOT NULL COMMENT '岗位类型',
  `sort_rank` int(11) DEFAULT NULL COMMENT '顺序',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注信息',
  `sn` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '编码',
  `enable` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '1' COMMENT '启用/禁用',
  PRIMARY KEY (`id`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT='岗位';

-- ----------------------------
-- Table structure for jb_user
-- ----------------------------
DROP TABLE IF EXISTS `jb_user`;
CREATE TABLE `jb_user`  (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `username` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '姓名',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '记录创建时间',
  `phone` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `enable` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '启用',
  `sex` int(11) NULL DEFAULT 0 COMMENT '性别',
  `pinyin` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '拼音码',
  `roles` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色 一对多',
  `is_system_admin` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '是否系统超级管理员',
  `create_user_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `pwd_salt` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码盐值',
  `login_ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '最后登录IP',
  `login_time` datetime(0) NULL DEFAULT NULL COMMENT '最后登录时间',
  `login_city` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '最后登录城市',
  `login_province` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '最后登录省份',
  `login_country` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '最后登录国家',
  `is_remote_login` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '是否异地登录',
  `dept_id` bigint(20) DEFAULT NULL COMMENT '部门ID',
  `posts` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '岗位IDS',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户登录账户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for jb_user_config
-- ----------------------------
DROP TABLE IF EXISTS `jb_user_config`;
CREATE TABLE `jb_user_config`  (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '配置名',
  `config_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '配置KEY',
  `config_value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '配置值',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `value_type` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '取值类型',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户系统样式自定义设置表' ROW_FORMAT = Dynamic;


-- ----------------------------
-- Table structure for jb_wechat_article
-- ----------------------------
DROP TABLE IF EXISTS `jb_wechat_article`;
CREATE TABLE `jb_wechat_article`  (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户 ID',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `update_user_id` bigint(20) NULL DEFAULT NULL COMMENT '更新用户 ID',
  `brief_info` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文章摘要',
  `poster` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `view_count` int(11) NULL DEFAULT NULL COMMENT '阅读数',
  `media_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信素材 ID',
  `mp_id` bigint(20) NULL DEFAULT NULL COMMENT '微信 ID',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图文链接地址',
  `type` int(11) NULL DEFAULT NULL COMMENT '本地图文 公众号图文素材 外部图文',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '微信图文信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for jb_wechat_autoreply
-- ----------------------------
DROP TABLE IF EXISTS `jb_wechat_autoreply`;
CREATE TABLE `jb_wechat_autoreply`  (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `mp_id` bigint(20) NULL DEFAULT NULL COMMENT '微信 ID',
  `type` int(11) NULL DEFAULT 0 COMMENT '类型 关注回复 无内容时回复 关键词回复',
  `name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '规则名称',
  `reply_type` int(11) NULL DEFAULT NULL COMMENT '回复类型 全部还是随机一条',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '记录创建时间',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户 ID',
  `enable` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '是否启用',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '微信公众号自动回复规则' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for jb_wechat_config
-- ----------------------------
DROP TABLE IF EXISTS `jb_wechat_config`;
CREATE TABLE `jb_wechat_config`  (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `mp_id` bigint(20) NULL DEFAULT NULL COMMENT '微信 ID',
  `config_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '配置key',
  `config_value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '配置值',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '配置项名称',
  `type` int(11) NULL DEFAULT NULL COMMENT '配置类型',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '微信公众号配置表' ROW_FORMAT = Dynamic;
 

-- ----------------------------
-- Table structure for jb_wechat_keywords
-- ----------------------------
DROP TABLE IF EXISTS `jb_wechat_keywords`;
CREATE TABLE `jb_wechat_keywords`  (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `mp_id` bigint(20) NULL DEFAULT NULL COMMENT '微信 ID',
  `name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `type` int(11) NULL DEFAULT 0 COMMENT '模糊 全匹配',
  `auto_reply_id` bigint(20) NULL DEFAULT NULL COMMENT '回复规则ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '微信关键词回复中的关键词定义' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for jb_wechat_media
-- ----------------------------
DROP TABLE IF EXISTS `jb_wechat_media`;
CREATE TABLE `jb_wechat_media`  (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标题',
  `digest` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类型 image video voice news',
  `mp_id` bigint(20) NULL DEFAULT NULL COMMENT '微信 ID',
  `media_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信素材ID',
  `thumb_media_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '封面图ID',
  `content_source_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '原文地址',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '访问地址',
  `author` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图文作者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `server_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '存服务器URL',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '微信公众平台素材库同步管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for jb_wechat_menu
-- ----------------------------
DROP TABLE IF EXISTS `jb_wechat_menu`;
CREATE TABLE `jb_wechat_menu`  (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `mp_id` bigint(20) NULL DEFAULT NULL COMMENT '微信 ID',
  `type` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `pid` bigint(20) NULL DEFAULT 0,
  `sort_rank` int(11) NULL DEFAULT NULL COMMENT '排序',
  `value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `app_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信小程序APPID',
  `page_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信小程序页面地址',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '微信菜单' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for jb_wechat_mpinfo
-- ----------------------------
DROP TABLE IF EXISTS `jb_wechat_mpinfo`;
CREATE TABLE `jb_wechat_mpinfo`  (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '平台名称',
  `logo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像LOGO',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户ID',
  `enable` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '是否启用',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_user_id` bigint(20) NULL DEFAULT NULL COMMENT '更新人ID',
  `brief_info` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '简介',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `type` int(11) NULL DEFAULT NULL COMMENT '类型 订阅号、服务号、小程序、企业号',
  `is_authenticated` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '是否已认证',
  `subject_type` int(11) NULL DEFAULT NULL COMMENT '申请认证主体的类型 个人还是企业',
  `wechat_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信号',
  `qrcode` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '二维码',
  `link_app_id` bigint(20) NULL DEFAULT NULL COMMENT '关联应用ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '微信公众号与小程序' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for jb_wechat_reply_content
-- ----------------------------
DROP TABLE IF EXISTS `jb_wechat_reply_content`;
CREATE TABLE `jb_wechat_reply_content`  (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `type` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '类型 图文 文字 图片 音频 视频',
  `title` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标题',
  `content` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '内容',
  `poster` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '封面',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '地址',
  `auto_reply_id` bigint(20) NULL DEFAULT NULL COMMENT '回复规则ID',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户 ID',
  `media_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '关联数据',
  `mp_id` bigint(20) NULL DEFAULT NULL COMMENT '微信 ID',
  `sort_rank` int(11) NULL DEFAULT NULL COMMENT '排序',
  `enable` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '是否启用',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '自动回复内容' ROW_FORMAT = Dynamic;


-- ----------------------------
-- Table structure for jb_wechat_user
-- ----------------------------
DROP TABLE IF EXISTS `jb_wechat_user`;
CREATE TABLE `jb_wechat_user`  (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `nickname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户昵称',
  `open_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'openId',
  `union_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'unionID',
  `sex` int(11) NULL DEFAULT 0 COMMENT '性别 1男 2女 0 未知',
  `language` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '语言',
  `subscibe` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '是否已关注',
  `country` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '国家',
  `province` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '省',
  `city` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '城市',
  `head_img_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像',
  `subscribe_time` datetime(0) NULL DEFAULT NULL COMMENT '关注时间',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `group_id` int(11) NULL DEFAULT NULL COMMENT '分组',
  `tag_ids` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标签',
  `subscribe_scene` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '关注渠道',
  `qr_scene` int(11) NULL DEFAULT NULL COMMENT '二维码场景-开发者自定义',
  `qr_scene_str` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '二维码扫码场景描述-开发者自定义',
  `realname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '真实姓名',
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `phone_country_code` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号国家代码',
  `check_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机验证码',
  `is_checked` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '是否已验证',
  `source` int(11) NULL DEFAULT NULL COMMENT '来源 小程序还是公众平台',
  `session_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '小程序登录SessionKey',
  `enable` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '禁用访问',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `checked_time` datetime(0) NULL DEFAULT NULL COMMENT '验证绑定手机号时间',
  `mp_id` bigint(20) NULL DEFAULT NULL COMMENT '所属公众平台ID',
  `weixin` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信号',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `last_login_time` datetime(0) NULL DEFAULT NULL COMMENT '最后登录时间',
  `first_auth_time` datetime(0) NULL DEFAULT NULL COMMENT '首次授权时间',
  `last_auth_time` datetime(0) NULL DEFAULT NULL COMMENT '最后一次更新授权时间',
  `first_login_time` datetime(0) NULL DEFAULT NULL COMMENT '首次登录时间',
  `bind_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '绑定其他用户信息',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '微信用户信息_模板表' ROW_FORMAT = Dynamic;


CREATE TABLE `jb_topnav` (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `name` varchar(40) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '名称',
  `icon` varchar(40) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '图标',
  `enable` char(1) COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '是否启用',
  `sort_rank` int(11) DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='顶部导航';

CREATE TABLE `jb_topnav_menu` (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `topnav_id` bigint(20) DEFAULT NULL COMMENT '顶部导航ID',
  `permission_id` bigint(20) DEFAULT NULL COMMENT '菜单资源ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='顶部菜单对应左侧一级导航中间表';

CREATE TABLE `jb_todo` (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '详情内容',
  `user_id` bigint(20) NOT NULL COMMENT '所属用户',
  `state` int(11) NOT NULL COMMENT '状态',
  `specified_finish_time` datetime NOT NULL COMMENT '规定完成时间',
  `type` int(11) NOT NULL COMMENT '类型 链接还是内容 还是都有',
  `url` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '链接',
  `priority_level` int(11) NOT NULL COMMENT '优先级',
  `real_finish_time` datetime DEFAULT NULL COMMENT '完成时间',
  `cancel_time` datetime DEFAULT NULL COMMENT '取消时间',
  `create_user_id` bigint(20) NOT NULL COMMENT '创建人ID',
  `update_user_id` bigint(20) DEFAULT NULL COMMENT '更新人ID',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `source_msg_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '第三方系统消息主键',
  `source_request_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '第三方系统流程主键',
  `source_url` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '第三方url',
  `send_user_key` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '发送人第三方系统标识',
  `receive_user_key` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '接收人第三方系统标识',
  `source_sys` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '来源系统',
  `is_readed` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '是否已读',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='待办事项';



CREATE TABLE `jb_sys_notice` (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '消息内容',
  `type` int(11) NOT NULL COMMENT '通知类型',
  `priority_level` int(11) NOT NULL COMMENT '优先级',
  `read_count` int(11) DEFAULT '0' COMMENT '已读人数',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_user_id` bigint(20) DEFAULT NULL COMMENT '更新人',
  `receiver_type` int(11) DEFAULT NULL COMMENT '接收人类型',
  `receiver_value` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '接收人值',
  `files` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '附件',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '删除标志',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='系统通知';

CREATE TABLE `jb_sys_notice_reader` (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `sys_notice_id` bigint(20) NOT NULL COMMENT '通知ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='通知阅读用户关系表';

CREATE TABLE `jb_private_message` (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '私信内容',
  `create_time` datetime NOT NULL COMMENT '发送时间',
  `from_user_id` bigint(20) NOT NULL COMMENT '发信人',
  `to_user_id` bigint(20) NOT NULL COMMENT '收信人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='内部私信';

-- ----------------------------
-- Table structure for jb_hiprint_tpl
-- ----------------------------
DROP TABLE IF EXISTS `jb_hiprint_tpl`;
CREATE TABLE `jb_hiprint_tpl`  (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '模板名称',
  `sn` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '模板编码',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '模板内容',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `create_user_id` bigint(20) NOT NULL COMMENT '创建人',
  `update_user_id` bigint(20) NOT NULL COMMENT '更新人',
  `test_api_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '测试API地址',
  `test_json_data` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '测试JSON数据',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for jb_qiniu
-- ----------------------------
DROP TABLE IF EXISTS `jb_qiniu`;
CREATE TABLE `jb_qiniu`  (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '账号',
  `sn` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '编号SN',
  `ak` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'AK',
  `sk` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'SK',
  `type` int(11) NOT NULL COMMENT '账号类型',
  `bucket_count` int(11) NOT NULL DEFAULT 0 COMMENT '空间个数',
  `is_default` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '是否默认',
  `enable` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '是否启用',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `create_user_id` bigint(20) NOT NULL COMMENT '创建人',
  `update_user_id` bigint(20) NOT NULL COMMENT '更新人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '七牛账号表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for jb_qiniu_bucket
-- ----------------------------
DROP TABLE IF EXISTS `jb_qiniu_bucket`;
CREATE TABLE `jb_qiniu_bucket`  (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'bucket名称',
  `sn` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '编码',
  `qiniu_id` bigint(20) NOT NULL COMMENT '所属七牛账号',
  `domain_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '绑定域名',
  `callback_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '回调地址',
  `callback_body` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '回调body定义',
  `callback_body_type` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '回调Body类型',
  `expires` int(11) NULL DEFAULT NULL COMMENT '有效期(秒)',
  `is_default` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '是否默认',
  `enable` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '是否启用',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_user_id` bigint(20) NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_user_id` bigint(20) NOT NULL COMMENT '更新人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `region` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '地区',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '七牛bucket配置' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for jb_datasource_filter
-- ----------------------------
DROP TABLE IF EXISTS `jb_datasource_filter`;
CREATE TABLE `jb_datasource_filter` (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
  `config_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '配置名称',
  `table_name_filter` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '不需要生成的表名',
  `table_name_contains` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '需要排除包含字符',
  `table_name_patterns` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '需要排除符合正则的',
  `create_user_id` bigint NOT NULL COMMENT '创建人',
  `update_user_id` bigint NOT NULL COMMENT '更新人',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据源过滤配置' ROW_FORMAT = Dynamic;
SET FOREIGN_KEY_CHECKS = 1;