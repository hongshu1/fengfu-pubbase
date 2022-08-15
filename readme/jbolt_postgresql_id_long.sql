/*
 Navicat Premium Data Transfer

 Source Server         : pgsql
 Source Server Type    : PostgreSQL
 Source Server Version : 130003
 Source Host           : localhost:5432
 Source Catalog        : postgres
 Source Schema         : public

 Target Server Type    : PostgreSQL
 Target Server Version : 130003
 File Encoding         : 65001

 Date: 21/06/2021 21:34:03
*/


-- ----------------------------
-- Table structure for jb_application
-- ----------------------------
DROP TABLE IF EXISTS "public"."jb_application";
CREATE TABLE "public"."jb_application" (
  "id" int8 NOT NULL,
  "name" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "brief_info" varchar(255) COLLATE "pg_catalog"."default",
  "app_id" varchar(255) COLLATE "pg_catalog"."default",
  "app_secret" varchar(255) COLLATE "pg_catalog"."default",
  "enable" char(1) COLLATE "pg_catalog"."default" DEFAULT '0'::bpchar NOT NULL,
  "create_time" timestamp(6),
  "update_time" timestamp(6),
  "user_id" int8,
  "update_user_id" int8,
  "type" int4 NOT NULL,
  "need_check_sign" char(1) COLLATE "pg_catalog"."default" DEFAULT '0'::bpchar NOT NULL,
  "is_inner" char(1) COLLATE "pg_catalog"."default" DEFAULT '0'::bpchar NOT NULL,
  "link_target_id" int8
)
;
COMMENT ON COLUMN "public"."jb_application"."id" IS '主键ID';
COMMENT ON COLUMN "public"."jb_application"."name" IS '应用名称';
COMMENT ON COLUMN "public"."jb_application"."brief_info" IS '应用简介';
COMMENT ON COLUMN "public"."jb_application"."app_id" IS '应用ID';
COMMENT ON COLUMN "public"."jb_application"."app_secret" IS '应用密钥';
COMMENT ON COLUMN "public"."jb_application"."enable" IS '是否启用';
COMMENT ON COLUMN "public"."jb_application"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."jb_application"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."jb_application"."user_id" IS '创建用户ID';
COMMENT ON COLUMN "public"."jb_application"."update_user_id" IS '更新用户ID';
COMMENT ON COLUMN "public"."jb_application"."type" IS 'app类型';
COMMENT ON COLUMN "public"."jb_application"."need_check_sign" IS '是否需要接口校验SIGN';
COMMENT ON COLUMN "public"."jb_application"."is_inner" IS '是否系统内置';
COMMENT ON COLUMN "public"."jb_application"."link_target_id" IS '关联目标ID';
COMMENT ON TABLE "public"."jb_application" IS 'API应用中心的应用APP';

-- ----------------------------
-- Table structure for jb_hiprint_tpl
-- ----------------------------
DROP TABLE IF EXISTS "public"."jb_hiprint_tpl";
CREATE TABLE "public"."jb_hiprint_tpl" (
  "id" int8 NOT NULL,
  "name" varchar(40) COLLATE "pg_catalog"."default" NOT NULL,
  "sn" varchar(40) COLLATE "pg_catalog"."default" NOT NULL,
  "content" text COLLATE "pg_catalog"."default" NOT NULL,
  "remark" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6) NOT NULL,
  "update_time" timestamp(6) NOT NULL,
  "create_user_id" int8 NOT NULL,
  "update_user_id" int8 NOT NULL,
  "test_api_url" varchar(255) COLLATE "pg_catalog"."default",
  "test_json_data" text COLLATE "pg_catalog"."default"
)
;

COMMENT ON COLUMN "public"."jb_hiprint_tpl"."id" IS '主键ID';
COMMENT ON COLUMN "public"."jb_hiprint_tpl"."name" IS '模板名称';
COMMENT ON COLUMN "public"."jb_hiprint_tpl"."sn" IS '模板编码';
COMMENT ON COLUMN "public"."jb_hiprint_tpl"."content" IS '模板内容';
COMMENT ON COLUMN "public"."jb_hiprint_tpl"."remark" IS '备注';
COMMENT ON COLUMN "public"."jb_hiprint_tpl"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."jb_hiprint_tpl"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."jb_hiprint_tpl"."create_user_id" IS '创建人';
COMMENT ON COLUMN "public"."jb_hiprint_tpl"."update_user_id" IS '更新人';
COMMENT ON COLUMN "public"."jb_hiprint_tpl"."test_api_url" IS '测试API地址';
COMMENT ON COLUMN "public"."jb_hiprint_tpl"."test_json_data" IS '测试JSON数据';
COMMENT ON TABLE "public"."jb_hiprint_tpl" IS 'hiprint模板';

-- ----------------------------
-- Table structure for jb_dept
-- ----------------------------
DROP TABLE IF EXISTS "public"."jb_dept";
CREATE TABLE "public"."jb_dept" (
  "id" int8 NOT NULL,
  "name" varchar(40) COLLATE "pg_catalog"."default" NOT NULL,
  "full_name" varchar(40) COLLATE "pg_catalog"."default" NOT NULL,
  "pid" int8 NOT NULL,
  "type" varchar(40) COLLATE "pg_catalog"."default" NOT NULL,
  "leader" varchar(40) COLLATE "pg_catalog"."default",
  "phone" varchar(40) COLLATE "pg_catalog"."default",
  "email" varchar(100) COLLATE "pg_catalog"."default",
  "address" varchar(100) COLLATE "pg_catalog"."default",
  "zipcode" varchar(40) COLLATE "pg_catalog"."default",
  "sn" varchar(40) COLLATE "pg_catalog"."default",
  "sort_rank" int4,
  "remark" varchar(255) COLLATE "pg_catalog"."default",
  "enable" char(1) COLLATE "pg_catalog"."default" DEFAULT '1'::bpchar NOT NULL,
  "create_time" timestamp(6),
  "update_time" timestamp(6)
)
;
COMMENT ON COLUMN "public"."jb_dept"."id" IS '主键ID';
COMMENT ON COLUMN "public"."jb_dept"."name" IS '部门名称';
COMMENT ON COLUMN "public"."jb_dept"."full_name" IS '全称';
COMMENT ON COLUMN "public"."jb_dept"."type" IS '部门类型';
COMMENT ON COLUMN "public"."jb_dept"."leader" IS '负责人';
COMMENT ON COLUMN "public"."jb_dept"."phone" IS '手机号';
COMMENT ON COLUMN "public"."jb_dept"."email" IS '电子邮件';
COMMENT ON COLUMN "public"."jb_dept"."address" IS '地址';
COMMENT ON COLUMN "public"."jb_dept"."zipcode" IS '邮政编码';
COMMENT ON COLUMN "public"."jb_dept"."sn" IS '编码';
COMMENT ON COLUMN "public"."jb_dept"."sort_rank" IS '排序';
COMMENT ON COLUMN "public"."jb_dept"."remark" IS '备注';
COMMENT ON COLUMN "public"."jb_dept"."enable" IS '启用';
COMMENT ON COLUMN "public"."jb_dept"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."jb_dept"."update_time" IS '更新时间';
COMMENT ON TABLE "public"."jb_dept" IS '部门管理';

-- ----------------------------
-- Table structure for jb_dictionary
-- ----------------------------
DROP TABLE IF EXISTS "public"."jb_dictionary";
CREATE TABLE "public"."jb_dictionary" (
  "id" int8 NOT NULL,
  "name" varchar(255) COLLATE "pg_catalog"."default",
  "type_id" int8,
  "pid" int8,
  "sort_rank" int4,
  "sn" varchar(255) COLLATE "pg_catalog"."default",
  "type_key" varchar(40) COLLATE "pg_catalog"."default",
  "enable" char(1) COLLATE "pg_catalog"."default" DEFAULT '1'::bpchar NOT NULL
)
;
COMMENT ON COLUMN "public"."jb_dictionary"."id" IS '字典ID主键';
COMMENT ON COLUMN "public"."jb_dictionary"."name" IS '名称';
COMMENT ON COLUMN "public"."jb_dictionary"."type_id" IS '字典类型ID';
COMMENT ON COLUMN "public"."jb_dictionary"."pid" IS '父类ID';
COMMENT ON COLUMN "public"."jb_dictionary"."sort_rank" IS '排序';
COMMENT ON COLUMN "public"."jb_dictionary"."sn" IS '编号编码';
COMMENT ON COLUMN "public"."jb_dictionary"."type_key" IS '字典类型KEY';
COMMENT ON COLUMN "public"."jb_dictionary"."enable" IS '是否启用';
COMMENT ON TABLE "public"."jb_dictionary" IS '字典表';

-- ----------------------------
-- Table structure for jb_dictionary_type
-- ----------------------------
DROP TABLE IF EXISTS "public"."jb_dictionary_type";
CREATE TABLE "public"."jb_dictionary_type" (
  "id" int8 NOT NULL,
  "name" varchar(255) COLLATE "pg_catalog"."default",
  "mode_level" int4,
  "type_key" varchar(255) COLLATE "pg_catalog"."default",
  "enable" char(1) COLLATE "pg_catalog"."default" DEFAULT '1'::bpchar NOT NULL
)
;
COMMENT ON COLUMN "public"."jb_dictionary_type"."id" IS '主键ID';
COMMENT ON COLUMN "public"."jb_dictionary_type"."name" IS '类型名称';
COMMENT ON COLUMN "public"."jb_dictionary_type"."mode_level" IS '模式层级';
COMMENT ON COLUMN "public"."jb_dictionary_type"."type_key" IS '标识KEY';
COMMENT ON COLUMN "public"."jb_dictionary_type"."enable" IS '是否启用';
COMMENT ON TABLE "public"."jb_dictionary_type" IS '字典类型';

-- ----------------------------
-- Table structure for jb_global_config
-- ----------------------------
DROP TABLE IF EXISTS "public"."jb_global_config";
CREATE TABLE "public"."jb_global_config" (
  "id" int8 NOT NULL,
  "config_key" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "config_value" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6) NOT NULL,
  "user_id" int8,
  "update_time" timestamp(6),
  "update_user_id" int8,
  "name" varchar(255) COLLATE "pg_catalog"."default",
  "value_type" varchar(40) COLLATE "pg_catalog"."default",
  "type_key" varchar(255) COLLATE "pg_catalog"."default",
  "type_id" int8,
  "built_in" char(1) COLLATE "pg_catalog"."default" DEFAULT '0'::bpchar NOT NULL
)
;
COMMENT ON COLUMN "public"."jb_global_config"."id" IS '主键ID';
COMMENT ON COLUMN "public"."jb_global_config"."config_key" IS '参数KEY';
COMMENT ON COLUMN "public"."jb_global_config"."config_value" IS '参数值';
COMMENT ON COLUMN "public"."jb_global_config"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."jb_global_config"."user_id" IS '创建用户ID';
COMMENT ON COLUMN "public"."jb_global_config"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."jb_global_config"."update_user_id" IS '更新用户ID';
COMMENT ON COLUMN "public"."jb_global_config"."name" IS '参名称';
COMMENT ON COLUMN "public"."jb_global_config"."value_type" IS '值类型';
COMMENT ON COLUMN "public"."jb_global_config"."type_key" IS '参数类型';
COMMENT ON COLUMN "public"."jb_global_config"."type_id" IS '参数类型ID';
COMMENT ON COLUMN "public"."jb_global_config"."built_in" IS '系统内置';
COMMENT ON TABLE "public"."jb_global_config" IS '全局参数配置';

-- ----------------------------
-- Table structure for jb_global_config_type
-- ----------------------------
DROP TABLE IF EXISTS "public"."jb_global_config_type";
CREATE TABLE "public"."jb_global_config_type" (
  "id" int8 NOT NULL,
  "name" varchar(255) COLLATE "pg_catalog"."default",
  "sort_rank" int4,
  "type_key" varchar(255) COLLATE "pg_catalog"."default",
  "built_in" char(1) COLLATE "pg_catalog"."default" DEFAULT '0'::bpchar NOT NULL
)
;
COMMENT ON COLUMN "public"."jb_global_config_type"."id" IS '主键ID';
COMMENT ON COLUMN "public"."jb_global_config_type"."name" IS '类型名称';
COMMENT ON COLUMN "public"."jb_global_config_type"."sort_rank" IS '排序';
COMMENT ON COLUMN "public"."jb_global_config_type"."type_key" IS '类型KEY';
COMMENT ON COLUMN "public"."jb_global_config_type"."built_in" IS '系统内置';
COMMENT ON TABLE "public"."jb_global_config_type" IS '全局参数类型';


-- ----------------------------
-- Table structure for jb_jbolt_file
-- ----------------------------
DROP TABLE IF EXISTS "public"."jb_jbolt_file";
CREATE TABLE "public"."jb_jbolt_file" (
  "id" int8 NOT NULL,
  "local_path" varchar(255) COLLATE "pg_catalog"."default",
  "local_url" varchar(255) COLLATE "pg_catalog"."default",
  "cdn_url" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "user_id" int8,
  "file_name" varchar(255) COLLATE "pg_catalog"."default",
  "file_type" int4,
  "file_size" int4 DEFAULT 0,
  "file_suffix" varchar(255) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."jb_jbolt_file"."local_path" IS '保存物理地址';
COMMENT ON COLUMN "public"."jb_jbolt_file"."local_url" IS '本地可访问URL地址';
COMMENT ON COLUMN "public"."jb_jbolt_file"."cdn_url" IS '外部CDN地址';
COMMENT ON COLUMN "public"."jb_jbolt_file"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."jb_jbolt_file"."user_id" IS '用户ID';
COMMENT ON COLUMN "public"."jb_jbolt_file"."file_name" IS '文件名';
COMMENT ON COLUMN "public"."jb_jbolt_file"."file_type" IS '文件类型 图片 附件 视频 音频';
COMMENT ON COLUMN "public"."jb_jbolt_file"."file_size" IS '文件大小';
COMMENT ON COLUMN "public"."jb_jbolt_file"."file_suffix" IS '后缀名';
COMMENT ON TABLE "public"."jb_jbolt_file" IS '文件库';


-- ----------------------------
-- Table structure for jb_login_log
-- ----------------------------
DROP TABLE IF EXISTS "public"."jb_login_log";
CREATE TABLE "public"."jb_login_log" (
  "id" int8 NOT NULL,
  "user_id" int8,
  "username" varchar(40) COLLATE "pg_catalog"."default" NOT NULL,
  "login_ip" varchar(40) COLLATE "pg_catalog"."default",
  "login_time" timestamp(6) NOT NULL,
  "login_state" int4 NOT NULL,
  "is_browser" char(1) COLLATE "pg_catalog"."default" DEFAULT '0'::bpchar NOT NULL,
  "browser_version" varchar(40) COLLATE "pg_catalog"."default",
  "browser_name" varchar(100) COLLATE "pg_catalog"."default",
  "os_name" varchar(100) COLLATE "pg_catalog"."default",
  "login_city" varchar(40) COLLATE "pg_catalog"."default",
  "login_address" varchar(255) COLLATE "pg_catalog"."default",
  "login_city_code" varchar(40) COLLATE "pg_catalog"."default",
  "login_province" varchar(40) COLLATE "pg_catalog"."default",
  "login_country" varchar(40) COLLATE "pg_catalog"."default",
  "is_mobile" char(1) COLLATE "pg_catalog"."default" DEFAULT '0'::bpchar NOT NULL,
  "os_platform_name" varchar(40) COLLATE "pg_catalog"."default",
  "browser_engine_name" varchar(40) COLLATE "pg_catalog"."default",
  "browser_engine_version" varchar(40) COLLATE "pg_catalog"."default",
  "login_address_latitude" varchar(40) COLLATE "pg_catalog"."default",
  "login_address_longitude" varchar(40) COLLATE "pg_catalog"."default",
  "is_remote_login" char(1) COLLATE "pg_catalog"."default" DEFAULT '0'::bpchar NOT NULL,
  "create_time" timestamp(6)
)
;
COMMENT ON COLUMN "public"."jb_login_log"."id" IS 'ID';
COMMENT ON COLUMN "public"."jb_login_log"."user_id" IS '用户ID';
COMMENT ON COLUMN "public"."jb_login_log"."username" IS '用户名';
COMMENT ON COLUMN "public"."jb_login_log"."login_ip" IS 'IP地址';
COMMENT ON COLUMN "public"."jb_login_log"."login_time" IS '登录时间';
COMMENT ON COLUMN "public"."jb_login_log"."login_state" IS '登录状态';
COMMENT ON COLUMN "public"."jb_login_log"."is_browser" IS '是否是浏览器访问';
COMMENT ON COLUMN "public"."jb_login_log"."browser_version" IS '浏览器版本号';
COMMENT ON COLUMN "public"."jb_login_log"."browser_name" IS '浏览器';
COMMENT ON COLUMN "public"."jb_login_log"."os_name" IS '操作系统';
COMMENT ON COLUMN "public"."jb_login_log"."login_city" IS '登录城市';
COMMENT ON COLUMN "public"."jb_login_log"."login_address" IS '登录位置详情';
COMMENT ON COLUMN "public"."jb_login_log"."login_city_code" IS '登录城市代码';
COMMENT ON COLUMN "public"."jb_login_log"."login_province" IS '登录省份';
COMMENT ON COLUMN "public"."jb_login_log"."login_country" IS '登录国家';
COMMENT ON COLUMN "public"."jb_login_log"."is_mobile" IS '是否移动端';
COMMENT ON COLUMN "public"."jb_login_log"."os_platform_name" IS '平台名称';
COMMENT ON COLUMN "public"."jb_login_log"."browser_engine_name" IS '浏览器引擎名';
COMMENT ON COLUMN "public"."jb_login_log"."browser_engine_version" IS '浏览器引擎版本';
COMMENT ON COLUMN "public"."jb_login_log"."login_address_latitude" IS '登录地横坐标';
COMMENT ON COLUMN "public"."jb_login_log"."login_address_longitude" IS '登录地纵坐标';
COMMENT ON COLUMN "public"."jb_login_log"."is_remote_login" IS '是否为异地异常登录';
COMMENT ON COLUMN "public"."jb_login_log"."create_time" IS '记录创建时间';
COMMENT ON TABLE "public"."jb_login_log" IS '登录日志';


-- ----------------------------
-- Table structure for jb_online_user
-- ----------------------------
DROP TABLE IF EXISTS "public"."jb_online_user";
CREATE TABLE "public"."jb_online_user" (
  "id" int8 NOT NULL,
  "session_id" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "user_id" int8 NOT NULL,
  "login_log_id" int8 NOT NULL,
  "login_time" timestamp(6) NOT NULL,
  "update_time" timestamp(6) NOT NULL,
  "expiration_time" timestamp(6) NOT NULL,
  "screen_locked" char(1) COLLATE "pg_catalog"."default" DEFAULT '0'::bpchar NOT NULL,
  "online_state" int4 NOT NULL,
  "offline_time" timestamp(6)
)
;
COMMENT ON COLUMN "public"."jb_online_user"."id" IS '主键ID';
COMMENT ON COLUMN "public"."jb_online_user"."session_id" IS '会话ID';
COMMENT ON COLUMN "public"."jb_online_user"."user_id" IS '用户ID';
COMMENT ON COLUMN "public"."jb_online_user"."login_log_id" IS '登录日志ID';
COMMENT ON COLUMN "public"."jb_online_user"."login_time" IS '登录时间';
COMMENT ON COLUMN "public"."jb_online_user"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."jb_online_user"."expiration_time" IS '过期时间';
COMMENT ON COLUMN "public"."jb_online_user"."screen_locked" IS '是否锁屏';
COMMENT ON COLUMN "public"."jb_online_user"."online_state" IS '在线状态';
COMMENT ON COLUMN "public"."jb_online_user"."offline_time" IS '离线时间';


-- ----------------------------
-- Table structure for jb_permission
-- ----------------------------
DROP TABLE IF EXISTS "public"."jb_permission";
CREATE TABLE "public"."jb_permission" (
  "id" int8 NOT NULL,
  "title" varchar(40) COLLATE "pg_catalog"."default" NOT NULL,
  "pid" int8 DEFAULT 0 NOT NULL,
  "url" varchar(255) COLLATE "pg_catalog"."default",
  "icons" varchar(40) COLLATE "pg_catalog"."default",
  "sort_rank" int4,
  "permission_level" int4,
  "permission_key" varchar(255) COLLATE "pg_catalog"."default",
  "is_menu" char(1) COLLATE "pg_catalog"."default" DEFAULT '0'::bpchar NOT NULL,
  "is_target_blank" char(1) COLLATE "pg_catalog"."default" DEFAULT '0'::bpchar NOT NULL,
  "is_system_admin_default" char(1) COLLATE "pg_catalog"."default" DEFAULT '0'::bpchar NOT NULL,
  "open_type" int4,
  "open_option" varchar(255) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."jb_permission"."id" IS '主键';
COMMENT ON COLUMN "public"."jb_permission"."url" IS '地址';
COMMENT ON COLUMN "public"."jb_permission"."icons" IS '图标';
COMMENT ON COLUMN "public"."jb_permission"."sort_rank" IS '排序';
COMMENT ON COLUMN "public"."jb_permission"."permission_level" IS '层级';
COMMENT ON COLUMN "public"."jb_permission"."permission_key" IS '权限资源KEY';
COMMENT ON COLUMN "public"."jb_permission"."is_menu" IS '是否是菜单';
COMMENT ON COLUMN "public"."jb_permission"."is_target_blank" IS '是否新窗口打开';
COMMENT ON COLUMN "public"."jb_permission"."is_system_admin_default" IS '是否系统超级管理员默认拥有的权限';
COMMENT ON COLUMN "public"."jb_permission"."open_type" IS '打开类型 1 默认 2 iframe 3 dialog';
COMMENT ON COLUMN "public"."jb_permission"."open_option" IS '组件属性json';
COMMENT ON TABLE "public"."jb_permission" IS 'function定义';

-- ----------------------------
-- Table structure for jb_post
-- ----------------------------
DROP TABLE IF EXISTS "public"."jb_post";
CREATE TABLE "public"."jb_post" (
  "id" int8 NOT NULL,
  "name" varchar(40) COLLATE "pg_catalog"."default" NOT NULL,
  "type" varchar(40) COLLATE "pg_catalog"."default" NOT NULL,
  "sn" varchar(40) COLLATE "pg_catalog"."default",
  "sort_rank" int4,
  "remark" varchar(255) COLLATE "pg_catalog"."default",
  "enable" char(1) COLLATE "pg_catalog"."default" DEFAULT '1'::bpchar NOT NULL
)
;
COMMENT ON COLUMN "public"."jb_post"."id" IS '主键ID';
COMMENT ON COLUMN "public"."jb_post"."name" IS '部门名称';
COMMENT ON COLUMN "public"."jb_post"."type" IS '部门类型';
COMMENT ON COLUMN "public"."jb_post"."sn" IS '编码';
COMMENT ON COLUMN "public"."jb_post"."sort_rank" IS '排序';
COMMENT ON COLUMN "public"."jb_post"."remark" IS '备注';
COMMENT ON COLUMN "public"."jb_post"."enable" IS '启用';
COMMENT ON TABLE "public"."jb_post" IS '岗位管理';

-- ----------------------------
-- Table structure for jb_private_message
-- ----------------------------
DROP TABLE IF EXISTS "public"."jb_private_message";
CREATE TABLE "public"."jb_private_message" (
  "id" int8 NOT NULL,
  "content" text COLLATE "pg_catalog"."default" NOT NULL,
  "create_time" timestamp(6) NOT NULL,
  "from_user_id" int8 NOT NULL,
  "to_user_id" int8 NOT NULL
)
;
COMMENT ON COLUMN "public"."jb_private_message"."id" IS '主键ID';
COMMENT ON COLUMN "public"."jb_private_message"."content" IS '私信内容';
COMMENT ON COLUMN "public"."jb_private_message"."create_time" IS '发送时间';
COMMENT ON COLUMN "public"."jb_private_message"."from_user_id" IS '发信人';
COMMENT ON COLUMN "public"."jb_private_message"."to_user_id" IS '收信人';
COMMENT ON TABLE "public"."jb_private_message" IS '内部私信';

-- ----------------------------
-- Table structure for jb_remote_login_log
-- ----------------------------
DROP TABLE IF EXISTS "public"."jb_remote_login_log";
CREATE TABLE "public"."jb_remote_login_log" (
  "id" int8 NOT NULL,
  "last_login_country" varchar(40) COLLATE "pg_catalog"."default",
  "last_login_province" varchar(40) COLLATE "pg_catalog"."default",
  "last_login_city" varchar(40) COLLATE "pg_catalog"."default",
  "last_login_time" timestamp(6),
  "login_country" varchar(40) COLLATE "pg_catalog"."default",
  "login_province" varchar(40) COLLATE "pg_catalog"."default",
  "login_city" varchar(40) COLLATE "pg_catalog"."default",
  "login_time" timestamp(6),
  "user_id" int8,
  "username" varchar(40) COLLATE "pg_catalog"."default",
  "is_new" char(1) COLLATE "pg_catalog"."default" DEFAULT '0'::bpchar NOT NULL,
  "last_login_ip" varchar(40) COLLATE "pg_catalog"."default",
  "login_ip" varchar(40) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6)
)
;
COMMENT ON COLUMN "public"."jb_remote_login_log"."id" IS '主键ID';
COMMENT ON COLUMN "public"."jb_remote_login_log"."last_login_country" IS '最近一次登录国家';
COMMENT ON COLUMN "public"."jb_remote_login_log"."last_login_province" IS '最近一次登录省份';
COMMENT ON COLUMN "public"."jb_remote_login_log"."last_login_city" IS '最近一次登录城市';
COMMENT ON COLUMN "public"."jb_remote_login_log"."last_login_time" IS '最近一次登录时间';
COMMENT ON COLUMN "public"."jb_remote_login_log"."login_country" IS '新登录国家';
COMMENT ON COLUMN "public"."jb_remote_login_log"."login_province" IS '新登录省份';
COMMENT ON COLUMN "public"."jb_remote_login_log"."login_city" IS '新登录城市';
COMMENT ON COLUMN "public"."jb_remote_login_log"."login_time" IS '新登录时间';
COMMENT ON COLUMN "public"."jb_remote_login_log"."user_id" IS '登录用户ID';
COMMENT ON COLUMN "public"."jb_remote_login_log"."username" IS '登录用户名';
COMMENT ON COLUMN "public"."jb_remote_login_log"."is_new" IS '是否为最新一次';
COMMENT ON COLUMN "public"."jb_remote_login_log"."last_login_ip" IS '最近一次登录IP';
COMMENT ON COLUMN "public"."jb_remote_login_log"."login_ip" IS '新登录IP';
COMMENT ON COLUMN "public"."jb_remote_login_log"."create_time" IS '记录创建时间';
COMMENT ON TABLE "public"."jb_remote_login_log" IS '异地登录日志记录';

-- ----------------------------
-- Table structure for jb_role
-- ----------------------------
DROP TABLE IF EXISTS "public"."jb_role";
CREATE TABLE "public"."jb_role" (
  "id" int8 NOT NULL,
  "name" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
  "sn" varchar(40) COLLATE "pg_catalog"."default",
  "pid" int8
)
;
COMMENT ON COLUMN "public"."jb_role"."id" IS '主键';
COMMENT ON COLUMN "public"."jb_role"."name" IS '名称';
COMMENT ON COLUMN "public"."jb_role"."sn" IS '编码';
COMMENT ON COLUMN "public"."jb_role"."pid" IS '父级角色ID';
COMMENT ON TABLE "public"."jb_role" IS '角色表';

-- ----------------------------
-- Table structure for jb_role_permission
-- ----------------------------
DROP TABLE IF EXISTS "public"."jb_role_permission";
CREATE TABLE "public"."jb_role_permission" (
  "id" int8 NOT NULL,
  "role_id" int8 NOT NULL,
  "permission_id" int8 NOT NULL
)
;
COMMENT ON COLUMN "public"."jb_role_permission"."id" IS '主键';
COMMENT ON COLUMN "public"."jb_role_permission"."role_id" IS '角色ID';
COMMENT ON COLUMN "public"."jb_role_permission"."permission_id" IS '权限ID';
COMMENT ON TABLE "public"."jb_role_permission" IS '角色功能列表';


-- ----------------------------
-- Table structure for jb_sys_notice
-- ----------------------------
DROP TABLE IF EXISTS "public"."jb_sys_notice";
CREATE TABLE "public"."jb_sys_notice" (
  "id" int8 NOT NULL,
  "title" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "content" text COLLATE "pg_catalog"."default" NOT NULL,
  "type" int4 NOT NULL,
  "priority_level" int4 NOT NULL,
  "read_count" int4,
  "create_time" timestamp(6) NOT NULL,
  "update_time" timestamp(6),
  "create_user_id" int8,
  "update_user_id" int8,
  "receiver_type" int4,
  "receiver_value" text COLLATE "pg_catalog"."default",
  "files" varchar(255) COLLATE "pg_catalog"."default",
  "del_flag" char(1) COLLATE "pg_catalog"."default" DEFAULT '0'::bpchar NOT NULL
)
;
COMMENT ON COLUMN "public"."jb_sys_notice"."id" IS '主键ID';
COMMENT ON COLUMN "public"."jb_sys_notice"."title" IS '标题';
COMMENT ON COLUMN "public"."jb_sys_notice"."content" IS '消息内容';
COMMENT ON COLUMN "public"."jb_sys_notice"."type" IS '通知类型';
COMMENT ON COLUMN "public"."jb_sys_notice"."priority_level" IS '优先级';
COMMENT ON COLUMN "public"."jb_sys_notice"."read_count" IS '已读人数';
COMMENT ON COLUMN "public"."jb_sys_notice"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."jb_sys_notice"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."jb_sys_notice"."create_user_id" IS '创建人';
COMMENT ON COLUMN "public"."jb_sys_notice"."update_user_id" IS '更新人';
COMMENT ON COLUMN "public"."jb_sys_notice"."receiver_type" IS '接收人类型';
COMMENT ON COLUMN "public"."jb_sys_notice"."receiver_value" IS '接收人值';
COMMENT ON COLUMN "public"."jb_sys_notice"."files" IS '附件';
COMMENT ON COLUMN "public"."jb_sys_notice"."del_flag" IS '删除标志';
COMMENT ON TABLE "public"."jb_sys_notice" IS '系统通知';

-- ----------------------------
-- Table structure for jb_sys_notice_reader
-- ----------------------------
DROP TABLE IF EXISTS "public"."jb_sys_notice_reader";
CREATE TABLE "public"."jb_sys_notice_reader" (
  "id" int8 NOT NULL,
  "sys_notice_id" int8 NOT NULL,
  "user_id" int8 NOT NULL,
  "create_time" timestamp(6) NOT NULL
)
;
COMMENT ON COLUMN "public"."jb_sys_notice_reader"."id" IS '主键ID';
COMMENT ON COLUMN "public"."jb_sys_notice_reader"."sys_notice_id" IS '通知ID';
COMMENT ON COLUMN "public"."jb_sys_notice_reader"."user_id" IS '用户ID';
COMMENT ON COLUMN "public"."jb_sys_notice_reader"."create_time" IS '创建时间';
COMMENT ON TABLE "public"."jb_sys_notice_reader" IS '通知阅读用户关系表';

-- ----------------------------
-- Table structure for jb_system_log
-- ----------------------------
DROP TABLE IF EXISTS "public"."jb_system_log";
CREATE TABLE "public"."jb_system_log" (
  "id" int8 NOT NULL,
  "title" varchar(255) COLLATE "pg_catalog"."default",
  "type" int4,
  "url" varchar(255) COLLATE "pg_catalog"."default",
  "user_id" int8,
  "user_name" varchar(255) COLLATE "pg_catalog"."default",
  "target_type" int4,
  "create_time" timestamp(6),
  "target_id" int8,
  "open_type" int4
)
;
COMMENT ON COLUMN "public"."jb_system_log"."id" IS '主键';
COMMENT ON COLUMN "public"."jb_system_log"."title" IS '标题';
COMMENT ON COLUMN "public"."jb_system_log"."type" IS '操作类型 删除 更新 新增';
COMMENT ON COLUMN "public"."jb_system_log"."url" IS '连接对象详情地址';
COMMENT ON COLUMN "public"."jb_system_log"."user_id" IS '操作人ID';
COMMENT ON COLUMN "public"."jb_system_log"."user_name" IS '操作人姓名';
COMMENT ON COLUMN "public"."jb_system_log"."target_type" IS '操作对象类型';
COMMENT ON COLUMN "public"."jb_system_log"."create_time" IS '记录创建时间';
COMMENT ON COLUMN "public"."jb_system_log"."target_id" IS '操作对象ID';
COMMENT ON COLUMN "public"."jb_system_log"."open_type" IS '打开类型URL还是Dialog';
COMMENT ON TABLE "public"."jb_system_log" IS '系统操作日志表';

-- ----------------------------
-- Table structure for jb_todo
-- ----------------------------
DROP TABLE IF EXISTS "public"."jb_todo";
CREATE TABLE "public"."jb_todo" (
  "id" int8 NOT NULL,
  "title" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "content" text COLLATE "pg_catalog"."default",
  "user_id" int8 NOT NULL,
  "state" int4 NOT NULL,
  "specified_finish_time" timestamp(6) NOT NULL,
  "type" int4 NOT NULL,
  "url" varchar(512) COLLATE "pg_catalog"."default",
  "priority_level" int4 NOT NULL,
  "real_finish_time" timestamp(6),
  "cancel_time" timestamp(6),
  "create_user_id" int8 NOT NULL,
  "update_user_id" int8,
  "create_time" timestamp(6) NOT NULL,
  "update_time" timestamp(6) NOT NULL,
  "source_msg_id" varchar(64) COLLATE "pg_catalog"."default",
  "source_request_id" varchar(64) COLLATE "pg_catalog"."default",
  "source_url" varchar(512) COLLATE "pg_catalog"."default",
  "send_user_key" varchar(64) COLLATE "pg_catalog"."default",
  "receive_user_key" varchar(64) COLLATE "pg_catalog"."default",
  "source_sys" varchar(64) COLLATE "pg_catalog"."default",
  "is_readed" char(1) COLLATE "pg_catalog"."default" NOT NULL DEFAULT '0'::bpchar
)
;
COMMENT ON COLUMN "public"."jb_todo"."id" IS '主键ID';
COMMENT ON COLUMN "public"."jb_todo"."title" IS '标题';
COMMENT ON COLUMN "public"."jb_todo"."content" IS '详情内容';
COMMENT ON COLUMN "public"."jb_todo"."user_id" IS '所属用户';
COMMENT ON COLUMN "public"."jb_todo"."state" IS '状态';
COMMENT ON COLUMN "public"."jb_todo"."specified_finish_time" IS '规定完成时间';
COMMENT ON COLUMN "public"."jb_todo"."type" IS '类型 链接还是内容 还是都有';
COMMENT ON COLUMN "public"."jb_todo"."url" IS '链接';
COMMENT ON COLUMN "public"."jb_todo"."priority_level" IS '优先级';
COMMENT ON COLUMN "public"."jb_todo"."real_finish_time" IS '完成时间';
COMMENT ON COLUMN "public"."jb_todo"."cancel_time" IS '取消时间';
COMMENT ON COLUMN "public"."jb_todo"."create_user_id" IS '创建人ID';
COMMENT ON COLUMN "public"."jb_todo"."update_user_id" IS '更新人ID';
COMMENT ON COLUMN "public"."jb_todo"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."jb_todo"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."jb_todo"."source_msg_id" IS '第三方系统消息主键';
COMMENT ON COLUMN "public"."jb_todo"."source_request_id" IS '第三方系统流程主键';
COMMENT ON COLUMN "public"."jb_todo"."source_url" IS '第三方url';
COMMENT ON COLUMN "public"."jb_todo"."send_user_key" IS '发送人第三方系统标识';
COMMENT ON COLUMN "public"."jb_todo"."receive_user_key" IS '接收人第三方系统标识';
COMMENT ON COLUMN "public"."jb_todo"."source_sys" IS '来源系统';
COMMENT ON COLUMN "public"."jb_todo"."is_readed" IS '是否已读';
COMMENT ON TABLE "public"."jb_todo" IS '代办事项';

-- ----------------------------
-- Table structure for jb_topnav
-- ----------------------------
DROP TABLE IF EXISTS "public"."jb_topnav";
CREATE TABLE "public"."jb_topnav" (
  "id" int8 NOT NULL,
  "name" varchar(40) COLLATE "pg_catalog"."default",
  "icon" varchar(40) COLLATE "pg_catalog"."default",
  "enable" char(1) COLLATE "pg_catalog"."default" DEFAULT '0'::bpchar NOT NULL,
  "sort_rank" int4
)
;
COMMENT ON COLUMN "public"."jb_topnav"."id" IS '主键ID';
COMMENT ON COLUMN "public"."jb_topnav"."name" IS '名称';
COMMENT ON COLUMN "public"."jb_topnav"."icon" IS '图标';
COMMENT ON COLUMN "public"."jb_topnav"."enable" IS '是否启用';
COMMENT ON COLUMN "public"."jb_topnav"."sort_rank" IS '排序';
COMMENT ON TABLE "public"."jb_topnav" IS '顶部导航';

-- ----------------------------
-- Table structure for jb_topnav_menu
-- ----------------------------
DROP TABLE IF EXISTS "public"."jb_topnav_menu";
CREATE TABLE "public"."jb_topnav_menu" (
  "id" int8 NOT NULL,
  "topnav_id" int8,
  "permission_id" int8
)
;
COMMENT ON COLUMN "public"."jb_topnav_menu"."id" IS '主键ID';
COMMENT ON COLUMN "public"."jb_topnav_menu"."topnav_id" IS '顶部导航ID';
COMMENT ON COLUMN "public"."jb_topnav_menu"."permission_id" IS '菜单资源ID';
COMMENT ON TABLE "public"."jb_topnav_menu" IS '顶部菜单对应左侧一级导航中间表';


-- ----------------------------
-- Table structure for jb_user
-- ----------------------------
DROP TABLE IF EXISTS "public"."jb_user";
CREATE TABLE "public"."jb_user" (
  "id" int8 NOT NULL,
  "username" varchar(40) COLLATE "pg_catalog"."default" NOT NULL,
  "password" varchar(128) COLLATE "pg_catalog"."default" NOT NULL,
  "name" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
  "avatar" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "phone" varchar(40) COLLATE "pg_catalog"."default",
  "email" varchar(100) COLLATE "pg_catalog"."default",
  "enable" char(1) COLLATE "pg_catalog"."default" DEFAULT '0'::bpchar NOT NULL,
  "sex" int4,
  "pinyin" varchar(255) COLLATE "pg_catalog"."default",
  "roles" varchar(255) COLLATE "pg_catalog"."default",
  "is_system_admin" char(1) COLLATE "pg_catalog"."default" DEFAULT '0'::bpchar NOT NULL,
  "create_user_id" int8,
  "pwd_salt" varchar(128) COLLATE "pg_catalog"."default",
  "login_ip" varchar(255) COLLATE "pg_catalog"."default",
  "login_time" timestamp(6),
  "login_city" varchar(40) COLLATE "pg_catalog"."default",
  "login_province" varchar(40) COLLATE "pg_catalog"."default",
  "login_country" varchar(40) COLLATE "pg_catalog"."default",
  "is_remote_login" char(1) COLLATE "pg_catalog"."default" DEFAULT '0'::bpchar NOT NULL,
  "dept_id" int8,
  "posts" varchar(255) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."jb_user"."id" IS '主键ID';
COMMENT ON COLUMN "public"."jb_user"."username" IS '用户名';
COMMENT ON COLUMN "public"."jb_user"."password" IS '密码';
COMMENT ON COLUMN "public"."jb_user"."name" IS '姓名';
COMMENT ON COLUMN "public"."jb_user"."avatar" IS '头像';
COMMENT ON COLUMN "public"."jb_user"."create_time" IS '记录创建时间';
COMMENT ON COLUMN "public"."jb_user"."phone" IS '手机号';
COMMENT ON COLUMN "public"."jb_user"."email" IS '电子邮箱';
COMMENT ON COLUMN "public"."jb_user"."enable" IS '启用';
COMMENT ON COLUMN "public"."jb_user"."sex" IS '性别';
COMMENT ON COLUMN "public"."jb_user"."pinyin" IS '拼音码';
COMMENT ON COLUMN "public"."jb_user"."roles" IS '角色 一对多';
COMMENT ON COLUMN "public"."jb_user"."is_system_admin" IS '是否系统超级管理员';
COMMENT ON COLUMN "public"."jb_user"."create_user_id" IS '创建人';
COMMENT ON COLUMN "public"."jb_user"."pwd_salt" IS '密码盐值';
COMMENT ON COLUMN "public"."jb_user"."login_ip" IS '最后登录IP';
COMMENT ON COLUMN "public"."jb_user"."login_time" IS '最后登录时间';
COMMENT ON COLUMN "public"."jb_user"."login_city" IS '最后登录城市';
COMMENT ON COLUMN "public"."jb_user"."login_province" IS '最后登录省份';
COMMENT ON COLUMN "public"."jb_user"."login_country" IS '最后登录国家';
COMMENT ON COLUMN "public"."jb_user"."is_remote_login" IS '是否异地登录';
COMMENT ON COLUMN "public"."jb_user"."dept_id" IS '部门ID';
COMMENT ON COLUMN "public"."jb_user"."posts" IS '岗位IDS';
COMMENT ON TABLE "public"."jb_user" IS '用户登录账户表';

-- ----------------------------
-- Table structure for jb_user_config
-- ----------------------------
DROP TABLE IF EXISTS "public"."jb_user_config";
CREATE TABLE "public"."jb_user_config" (
  "id" int8 NOT NULL,
  "name" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "config_key" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "config_value" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "user_id" int8 NOT NULL,
  "create_time" timestamp(6),
  "update_time" timestamp(6),
  "value_type" varchar(40) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."jb_user_config"."id" IS '主键ID';
COMMENT ON COLUMN "public"."jb_user_config"."name" IS '配置名';
COMMENT ON COLUMN "public"."jb_user_config"."config_key" IS '配置KEY';
COMMENT ON COLUMN "public"."jb_user_config"."config_value" IS '配置值';
COMMENT ON COLUMN "public"."jb_user_config"."user_id" IS '用户ID';
COMMENT ON COLUMN "public"."jb_user_config"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."jb_user_config"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."jb_user_config"."value_type" IS '取值类型';
COMMENT ON TABLE "public"."jb_user_config" IS '用户系统样式自定义设置表';

-- ----------------------------
-- Table structure for jb_wechat_article
-- ----------------------------
DROP TABLE IF EXISTS "public"."jb_wechat_article";
CREATE TABLE "public"."jb_wechat_article" (
  "id" int8 NOT NULL,
  "title" varchar(255) COLLATE "pg_catalog"."default",
  "content" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "user_id" int8,
  "update_time" timestamp(6),
  "update_user_id" int8,
  "brief_info" varchar(120) COLLATE "pg_catalog"."default",
  "poster" varchar(255) COLLATE "pg_catalog"."default",
  "view_count" int4,
  "media_id" varchar(255) COLLATE "pg_catalog"."default",
  "mp_id" int8,
  "url" varchar(255) COLLATE "pg_catalog"."default",
  "type" int4
)
;
COMMENT ON COLUMN "public"."jb_wechat_article"."id" IS '主键ID';
COMMENT ON COLUMN "public"."jb_wechat_article"."title" IS '标题';
COMMENT ON COLUMN "public"."jb_wechat_article"."content" IS '内容';
COMMENT ON COLUMN "public"."jb_wechat_article"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."jb_wechat_article"."user_id" IS '用户 ID';
COMMENT ON COLUMN "public"."jb_wechat_article"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."jb_wechat_article"."update_user_id" IS '更新用户 ID';
COMMENT ON COLUMN "public"."jb_wechat_article"."brief_info" IS '文章摘要';
COMMENT ON COLUMN "public"."jb_wechat_article"."poster" IS '海报';
COMMENT ON COLUMN "public"."jb_wechat_article"."view_count" IS '阅读数';
COMMENT ON COLUMN "public"."jb_wechat_article"."media_id" IS '微信素材 ID';
COMMENT ON COLUMN "public"."jb_wechat_article"."mp_id" IS '微信 ID';
COMMENT ON COLUMN "public"."jb_wechat_article"."url" IS '图文链接地址';
COMMENT ON COLUMN "public"."jb_wechat_article"."type" IS '本地图文 公众号图文素材 外部图文';
COMMENT ON TABLE "public"."jb_wechat_article" IS '微信图文信息';

-- ----------------------------
-- Table structure for jb_wechat_autoreply
-- ----------------------------
DROP TABLE IF EXISTS "public"."jb_wechat_autoreply";
CREATE TABLE "public"."jb_wechat_autoreply" (
  "id" int8 NOT NULL,
  "mp_id" int8,
  "type" int4,
  "name" varchar(40) COLLATE "pg_catalog"."default",
  "reply_type" int4,
  "create_time" timestamp(6),
  "user_id" int8,
  "enable" char(1) COLLATE "pg_catalog"."default" DEFAULT '0'::bpchar NOT NULL
)
;
COMMENT ON COLUMN "public"."jb_wechat_autoreply"."mp_id" IS '微信 ID';
COMMENT ON COLUMN "public"."jb_wechat_autoreply"."type" IS '类型 关注回复 无内容时回复 关键词回复';
COMMENT ON COLUMN "public"."jb_wechat_autoreply"."name" IS '规则名称';
COMMENT ON COLUMN "public"."jb_wechat_autoreply"."reply_type" IS '回复类型 全部还是随机一条';
COMMENT ON COLUMN "public"."jb_wechat_autoreply"."create_time" IS '记录创建时间';
COMMENT ON COLUMN "public"."jb_wechat_autoreply"."user_id" IS '用户 ID';
COMMENT ON TABLE "public"."jb_wechat_autoreply" IS '微信公众号自动回复规则';

-- ----------------------------
-- Table structure for jb_wechat_config
-- ----------------------------
DROP TABLE IF EXISTS "public"."jb_wechat_config";
CREATE TABLE "public"."jb_wechat_config" (
  "id" int8 NOT NULL,
  "mp_id" int8,
  "config_key" varchar(255) COLLATE "pg_catalog"."default",
  "config_value" varchar(255) COLLATE "pg_catalog"."default",
  "name" varchar(255) COLLATE "pg_catalog"."default",
  "type" int4
)
;
COMMENT ON COLUMN "public"."jb_wechat_config"."mp_id" IS '微信 ID';
COMMENT ON COLUMN "public"."jb_wechat_config"."config_key" IS '配置key';
COMMENT ON COLUMN "public"."jb_wechat_config"."config_value" IS '配置值';
COMMENT ON COLUMN "public"."jb_wechat_config"."name" IS '配置项名称';
COMMENT ON COLUMN "public"."jb_wechat_config"."type" IS '配置类型';
COMMENT ON TABLE "public"."jb_wechat_config" IS '微信公众号配置表';

-- ----------------------------
-- Table structure for jb_wechat_keywords
-- ----------------------------
DROP TABLE IF EXISTS "public"."jb_wechat_keywords";
CREATE TABLE "public"."jb_wechat_keywords" (
  "id" int8 NOT NULL,
  "mp_id" int8,
  "name" varchar(40) COLLATE "pg_catalog"."default",
  "type" int4,
  "auto_reply_id" int8
)
;
COMMENT ON COLUMN "public"."jb_wechat_keywords"."mp_id" IS '微信 ID';
COMMENT ON COLUMN "public"."jb_wechat_keywords"."type" IS '模糊 全匹配';
COMMENT ON COLUMN "public"."jb_wechat_keywords"."auto_reply_id" IS '回复规则ID';
COMMENT ON TABLE "public"."jb_wechat_keywords" IS '微信关键词回复中的关键词定义';

-- ----------------------------
-- Table structure for jb_wechat_media
-- ----------------------------
DROP TABLE IF EXISTS "public"."jb_wechat_media";
CREATE TABLE "public"."jb_wechat_media" (
  "id" int8 NOT NULL,
  "title" varchar(255) COLLATE "pg_catalog"."default",
  "digest" varchar(255) COLLATE "pg_catalog"."default",
  "type" varchar(255) COLLATE "pg_catalog"."default",
  "mp_id" int8,
  "media_id" varchar(255) COLLATE "pg_catalog"."default",
  "thumb_media_id" varchar(255) COLLATE "pg_catalog"."default",
  "content_source_url" varchar(255) COLLATE "pg_catalog"."default",
  "url" varchar(255) COLLATE "pg_catalog"."default",
  "author" varchar(255) COLLATE "pg_catalog"."default",
  "update_time" timestamp(6),
  "server_url" varchar(255) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."jb_wechat_media"."id" IS '主键ID';
COMMENT ON COLUMN "public"."jb_wechat_media"."title" IS '标题';
COMMENT ON COLUMN "public"."jb_wechat_media"."digest" IS '描述';
COMMENT ON COLUMN "public"."jb_wechat_media"."type" IS '类型 image video voice news';
COMMENT ON COLUMN "public"."jb_wechat_media"."mp_id" IS '微信 ID';
COMMENT ON COLUMN "public"."jb_wechat_media"."media_id" IS '微信素材ID';
COMMENT ON COLUMN "public"."jb_wechat_media"."thumb_media_id" IS '封面图ID';
COMMENT ON COLUMN "public"."jb_wechat_media"."content_source_url" IS '原文地址';
COMMENT ON COLUMN "public"."jb_wechat_media"."url" IS '访问地址';
COMMENT ON COLUMN "public"."jb_wechat_media"."author" IS '图文作者';
COMMENT ON COLUMN "public"."jb_wechat_media"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."jb_wechat_media"."server_url" IS '存服务器URL';
COMMENT ON TABLE "public"."jb_wechat_media" IS '微信公众平台素材库同步管理';

-- ----------------------------
-- Table structure for jb_wechat_menu
-- ----------------------------
DROP TABLE IF EXISTS "public"."jb_wechat_menu";
CREATE TABLE "public"."jb_wechat_menu" (
  "id" int8 NOT NULL,
  "mp_id" int8,
  "type" varchar(40) COLLATE "pg_catalog"."default",
  "name" varchar(40) COLLATE "pg_catalog"."default",
  "pid" int8,
  "sort_rank" int4,
  "value" varchar(255) COLLATE "pg_catalog"."default",
  "app_id" varchar(255) COLLATE "pg_catalog"."default",
  "page_path" varchar(255) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."jb_wechat_menu"."mp_id" IS '微信 ID';
COMMENT ON COLUMN "public"."jb_wechat_menu"."sort_rank" IS '排序';
COMMENT ON COLUMN "public"."jb_wechat_menu"."app_id" IS '微信小程序APPID';
COMMENT ON COLUMN "public"."jb_wechat_menu"."page_path" IS '微信小程序页面地址';
COMMENT ON TABLE "public"."jb_wechat_menu" IS '微信菜单';

-- ----------------------------
-- Table structure for jb_wechat_mpinfo
-- ----------------------------
DROP TABLE IF EXISTS "public"."jb_wechat_mpinfo";
CREATE TABLE "public"."jb_wechat_mpinfo" (
  "id" int8 NOT NULL,
  "name" varchar(255) COLLATE "pg_catalog"."default",
  "logo" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "user_id" int8,
  "enable" char(1) COLLATE "pg_catalog"."default" DEFAULT '0'::bpchar NOT NULL,
  "update_time" timestamp(6),
  "update_user_id" int8,
  "brief_info" varchar(255) COLLATE "pg_catalog"."default",
  "remark" varchar(255) COLLATE "pg_catalog"."default",
  "type" int4,
  "is_authenticated" char(1) COLLATE "pg_catalog"."default" DEFAULT '0'::bpchar NOT NULL,
  "subject_type" int4,
  "wechat_id" varchar(255) COLLATE "pg_catalog"."default",
  "qrcode" varchar(255) COLLATE "pg_catalog"."default",
  "link_app_id" int8
)
;
COMMENT ON COLUMN "public"."jb_wechat_mpinfo"."id" IS '主键';
COMMENT ON COLUMN "public"."jb_wechat_mpinfo"."name" IS '平台名称';
COMMENT ON COLUMN "public"."jb_wechat_mpinfo"."logo" IS '头像LOGO';
COMMENT ON COLUMN "public"."jb_wechat_mpinfo"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."jb_wechat_mpinfo"."user_id" IS '用户ID';
COMMENT ON COLUMN "public"."jb_wechat_mpinfo"."enable" IS '是否启用';
COMMENT ON COLUMN "public"."jb_wechat_mpinfo"."update_time" IS '创建时间';
COMMENT ON COLUMN "public"."jb_wechat_mpinfo"."update_user_id" IS '更新人ID';
COMMENT ON COLUMN "public"."jb_wechat_mpinfo"."brief_info" IS '简介';
COMMENT ON COLUMN "public"."jb_wechat_mpinfo"."remark" IS '备注';
COMMENT ON COLUMN "public"."jb_wechat_mpinfo"."type" IS '类型 订阅号、服务号、小程序、企业号';
COMMENT ON COLUMN "public"."jb_wechat_mpinfo"."is_authenticated" IS '是否已认证';
COMMENT ON COLUMN "public"."jb_wechat_mpinfo"."subject_type" IS '申请认证主体的类型 个人还是企业';
COMMENT ON COLUMN "public"."jb_wechat_mpinfo"."wechat_id" IS '微信号';
COMMENT ON COLUMN "public"."jb_wechat_mpinfo"."qrcode" IS '二维码';
COMMENT ON COLUMN "public"."jb_wechat_mpinfo"."link_app_id" IS '关联应用ID';
COMMENT ON TABLE "public"."jb_wechat_mpinfo" IS '微信公众号与小程序';

-- ----------------------------
-- Table structure for jb_wechat_reply_content
-- ----------------------------
DROP TABLE IF EXISTS "public"."jb_wechat_reply_content";
CREATE TABLE "public"."jb_wechat_reply_content" (
  "id" int8 NOT NULL,
  "type" varchar(40) COLLATE "pg_catalog"."default",
  "title" varchar(64) COLLATE "pg_catalog"."default",
  "content" text COLLATE "pg_catalog"."default",
  "poster" varchar(255) COLLATE "pg_catalog"."default",
  "url" varchar(255) COLLATE "pg_catalog"."default",
  "auto_reply_id" int8,
  "create_time" timestamp(6),
  "user_id" int8,
  "media_id" varchar(255) COLLATE "pg_catalog"."default",
  "mp_id" int8,
  "sort_rank" int4,
  "enable" char(1) COLLATE "pg_catalog"."default" DEFAULT '0'::bpchar NOT NULL
)
;
COMMENT ON COLUMN "public"."jb_wechat_reply_content"."type" IS '类型 图文 文字 图片 音频 视频';
COMMENT ON COLUMN "public"."jb_wechat_reply_content"."title" IS '标题';
COMMENT ON COLUMN "public"."jb_wechat_reply_content"."content" IS '内容';
COMMENT ON COLUMN "public"."jb_wechat_reply_content"."poster" IS '海报';
COMMENT ON COLUMN "public"."jb_wechat_reply_content"."url" IS '地址';
COMMENT ON COLUMN "public"."jb_wechat_reply_content"."auto_reply_id" IS '回复规则ID';
COMMENT ON COLUMN "public"."jb_wechat_reply_content"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."jb_wechat_reply_content"."user_id" IS '用户 ID';
COMMENT ON COLUMN "public"."jb_wechat_reply_content"."media_id" IS '关联数据';
COMMENT ON COLUMN "public"."jb_wechat_reply_content"."mp_id" IS '微信 ID';
COMMENT ON COLUMN "public"."jb_wechat_reply_content"."sort_rank" IS '排序';
COMMENT ON COLUMN "public"."jb_wechat_reply_content"."enable" IS '是否启用';
COMMENT ON TABLE "public"."jb_wechat_reply_content" IS '自动回复内容';

-- ----------------------------
-- Table structure for jb_wechat_user
-- ----------------------------
DROP TABLE IF EXISTS "public"."jb_wechat_user";
CREATE TABLE "public"."jb_wechat_user" (
  "id" int8 NOT NULL,
  "nickname" varchar(255) COLLATE "pg_catalog"."default",
  "open_id" varchar(255) COLLATE "pg_catalog"."default",
  "union_id" varchar(255) COLLATE "pg_catalog"."default",
  "sex" int4,
  "language" varchar(255) COLLATE "pg_catalog"."default",
  "subscibe" char(1) COLLATE "pg_catalog"."default" DEFAULT '0'::bpchar NOT NULL,
  "country" varchar(255) COLLATE "pg_catalog"."default",
  "province" varchar(255) COLLATE "pg_catalog"."default",
  "city" varchar(255) COLLATE "pg_catalog"."default",
  "head_img_url" varchar(255) COLLATE "pg_catalog"."default",
  "subscribe_time" timestamp(6),
  "remark" varchar(255) COLLATE "pg_catalog"."default",
  "group_id" int4,
  "tag_ids" varchar(255) COLLATE "pg_catalog"."default",
  "subscribe_scene" varchar(255) COLLATE "pg_catalog"."default",
  "qr_scene" int4,
  "qr_scene_str" varchar(255) COLLATE "pg_catalog"."default",
  "realname" varchar(255) COLLATE "pg_catalog"."default",
  "phone" varchar(255) COLLATE "pg_catalog"."default",
  "phone_country_code" varchar(40) COLLATE "pg_catalog"."default",
  "check_code" varchar(255) COLLATE "pg_catalog"."default",
  "is_checked" char(1) COLLATE "pg_catalog"."default" DEFAULT '0'::bpchar NOT NULL,
  "source" int4,
  "session_key" varchar(255) COLLATE "pg_catalog"."default",
  "enable" char(1) COLLATE "pg_catalog"."default" DEFAULT '0'::bpchar NOT NULL,
  "create_time" timestamp(6),
  "checked_time" timestamp(6),
  "mp_id" int8,
  "weixin" varchar(255) COLLATE "pg_catalog"."default",
  "update_time" timestamp(6),
  "last_login_time" timestamp(6),
  "first_auth_time" timestamp(6),
  "last_auth_time" timestamp(6),
  "first_login_time" timestamp(6),
  "bind_user" varchar(255) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."jb_wechat_user"."id" IS '用户ID';
COMMENT ON COLUMN "public"."jb_wechat_user"."nickname" IS '用户昵称';
COMMENT ON COLUMN "public"."jb_wechat_user"."open_id" IS 'openId';
COMMENT ON COLUMN "public"."jb_wechat_user"."union_id" IS 'unionID';
COMMENT ON COLUMN "public"."jb_wechat_user"."sex" IS '性别 1男 2女 0 未知';
COMMENT ON COLUMN "public"."jb_wechat_user"."language" IS '语言';
COMMENT ON COLUMN "public"."jb_wechat_user"."subscibe" IS '是否已关注';
COMMENT ON COLUMN "public"."jb_wechat_user"."country" IS '国家';
COMMENT ON COLUMN "public"."jb_wechat_user"."province" IS '省';
COMMENT ON COLUMN "public"."jb_wechat_user"."city" IS '城市';
COMMENT ON COLUMN "public"."jb_wechat_user"."head_img_url" IS '头像';
COMMENT ON COLUMN "public"."jb_wechat_user"."subscribe_time" IS '关注时间';
COMMENT ON COLUMN "public"."jb_wechat_user"."remark" IS '备注';
COMMENT ON COLUMN "public"."jb_wechat_user"."group_id" IS '分组';
COMMENT ON COLUMN "public"."jb_wechat_user"."tag_ids" IS '标签';
COMMENT ON COLUMN "public"."jb_wechat_user"."subscribe_scene" IS '关注渠道';
COMMENT ON COLUMN "public"."jb_wechat_user"."qr_scene" IS '二维码场景-开发者自定义';
COMMENT ON COLUMN "public"."jb_wechat_user"."qr_scene_str" IS '二维码扫码场景描述-开发者自定义';
COMMENT ON COLUMN "public"."jb_wechat_user"."realname" IS '真实姓名';
COMMENT ON COLUMN "public"."jb_wechat_user"."phone" IS '手机号';
COMMENT ON COLUMN "public"."jb_wechat_user"."phone_country_code" IS '手机号国家代码';
COMMENT ON COLUMN "public"."jb_wechat_user"."check_code" IS '手机验证码';
COMMENT ON COLUMN "public"."jb_wechat_user"."is_checked" IS '是否已验证';
COMMENT ON COLUMN "public"."jb_wechat_user"."source" IS '来源 小程序还是公众平台';
COMMENT ON COLUMN "public"."jb_wechat_user"."session_key" IS '小程序登录SessionKey';
COMMENT ON COLUMN "public"."jb_wechat_user"."enable" IS '禁用访问';
COMMENT ON COLUMN "public"."jb_wechat_user"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."jb_wechat_user"."checked_time" IS '验证绑定手机号时间';
COMMENT ON COLUMN "public"."jb_wechat_user"."mp_id" IS '所属公众平台ID';
COMMENT ON COLUMN "public"."jb_wechat_user"."weixin" IS '微信号';
COMMENT ON COLUMN "public"."jb_wechat_user"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."jb_wechat_user"."last_login_time" IS '最后登录时间';
COMMENT ON COLUMN "public"."jb_wechat_user"."first_auth_time" IS '首次授权时间';
COMMENT ON COLUMN "public"."jb_wechat_user"."last_auth_time" IS '最后一次更新授权时间';
COMMENT ON COLUMN "public"."jb_wechat_user"."first_login_time" IS '首次登录时间';
COMMENT ON COLUMN "public"."jb_wechat_user"."bind_user" IS '绑定其他用户信息';
COMMENT ON TABLE "public"."jb_wechat_user" IS '微信用户信息_模板表';

-- ----------------------------
-- Primary Key structure for table jb_application
-- ----------------------------
ALTER TABLE "public"."jb_application" ADD CONSTRAINT "jb_application_pkey" PRIMARY KEY ("id");
-- ----------------------------
-- Primary Key structure for table jb_hiprint_tpl
-- ----------------------------
ALTER TABLE "public"."jb_hiprint_tpl" ADD CONSTRAINT "jb_hiprint_tpl_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table jb_dept
-- ----------------------------
ALTER TABLE "public"."jb_dept" ADD CONSTRAINT "jb_dept_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table jb_dictionary
-- ----------------------------
ALTER TABLE "public"."jb_dictionary" ADD CONSTRAINT "jb_dictionary_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table jb_dictionary_type
-- ----------------------------
ALTER TABLE "public"."jb_dictionary_type" ADD CONSTRAINT "jb_dictionary_type_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table jb_global_config
-- ----------------------------
ALTER TABLE "public"."jb_global_config" ADD CONSTRAINT "jb_global_config_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table jb_global_config_type
-- ----------------------------
ALTER TABLE "public"."jb_global_config_type" ADD CONSTRAINT "jb_global_config_type_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table jb_jbolt_file
-- ----------------------------
ALTER TABLE "public"."jb_jbolt_file" ADD CONSTRAINT "jb_jbolt_file_pkey" PRIMARY KEY ("id");
-- ----------------------------
-- Primary Key structure for table jb_login_log
-- ----------------------------
ALTER TABLE "public"."jb_login_log" ADD CONSTRAINT "jb_login_log_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table jb_online_user
-- ----------------------------
ALTER TABLE "public"."jb_online_user" ADD CONSTRAINT "jb_online_user_pkey" PRIMARY KEY ("id");


-- ----------------------------
-- Primary Key structure for table jb_permission
-- ----------------------------
ALTER TABLE "public"."jb_permission" ADD CONSTRAINT "jb_permission_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table jb_post
-- ----------------------------
ALTER TABLE "public"."jb_post" ADD CONSTRAINT "jb_post_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table jb_private_message
-- ----------------------------
ALTER TABLE "public"."jb_private_message" ADD CONSTRAINT "jb_private_message_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table jb_remote_login_log
-- ----------------------------
ALTER TABLE "public"."jb_remote_login_log" ADD CONSTRAINT "jb_remote_login_log_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table jb_role
-- ----------------------------
ALTER TABLE "public"."jb_role" ADD CONSTRAINT "jb_role_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table jb_role_permission
-- ----------------------------
ALTER TABLE "public"."jb_role_permission" ADD CONSTRAINT "jb_role_permission_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table jb_sys_notice
-- ----------------------------
ALTER TABLE "public"."jb_sys_notice" ADD CONSTRAINT "jb_sys_notice_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table jb_sys_notice_reader
-- ----------------------------
ALTER TABLE "public"."jb_sys_notice_reader" ADD CONSTRAINT "jb_sys_notice_reader_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table jb_system_log
-- ----------------------------
ALTER TABLE "public"."jb_system_log" ADD CONSTRAINT "jb_system_log_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table jb_todo
-- ----------------------------
ALTER TABLE "public"."jb_todo" ADD CONSTRAINT "jb_todo_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table jb_topnav
-- ----------------------------
ALTER TABLE "public"."jb_topnav" ADD CONSTRAINT "jb_topnav_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table jb_topnav_menu
-- ----------------------------
ALTER TABLE "public"."jb_topnav_menu" ADD CONSTRAINT "jb_topnav_menu_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table jb_user
-- ----------------------------
ALTER TABLE "public"."jb_user" ADD CONSTRAINT "jb_user_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table jb_user_config
-- ----------------------------
ALTER TABLE "public"."jb_user_config" ADD CONSTRAINT "jb_user_config_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table jb_wechat_article
-- ----------------------------
ALTER TABLE "public"."jb_wechat_article" ADD CONSTRAINT "jb_wechat_article_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table jb_wechat_autoreply
-- ----------------------------
ALTER TABLE "public"."jb_wechat_autoreply" ADD CONSTRAINT "jb_wechat_autoreply_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table jb_wechat_config
-- ----------------------------
ALTER TABLE "public"."jb_wechat_config" ADD CONSTRAINT "jb_wechat_config_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table jb_wechat_keywords
-- ----------------------------
ALTER TABLE "public"."jb_wechat_keywords" ADD CONSTRAINT "jb_wechat_keywords_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table jb_wechat_media
-- ----------------------------
ALTER TABLE "public"."jb_wechat_media" ADD CONSTRAINT "jb_wechat_media_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table jb_wechat_menu
-- ----------------------------
ALTER TABLE "public"."jb_wechat_menu" ADD CONSTRAINT "jb_wechat_menu_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table jb_wechat_mpinfo
-- ----------------------------
ALTER TABLE "public"."jb_wechat_mpinfo" ADD CONSTRAINT "jb_wechat_mpinfo_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table jb_wechat_reply_content
-- ----------------------------
ALTER TABLE "public"."jb_wechat_reply_content" ADD CONSTRAINT "jb_wechat_reply_content_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table jb_wechat_user
-- ----------------------------
ALTER TABLE "public"."jb_wechat_user" ADD CONSTRAINT "jb_wechat_user_pkey" PRIMARY KEY ("id");



-- ----------------------------
-- Table structure for jb_qiniu
-- ----------------------------
DROP TABLE IF EXISTS "public"."jb_qiniu";
CREATE TABLE "public"."jb_qiniu" (
  "id" int8 NOT NULL,
  "name" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "sn" varchar(40) COLLATE "pg_catalog"."default" NOT NULL,
  "ak" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "sk" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "type" int4 NOT NULL,
  "bucket_count" int4 NOT NULL,
  "is_default" char(1) COLLATE "pg_catalog"."default" NOT NULL DEFAULT '0'::bpchar,
  "enable" char(1) COLLATE "pg_catalog"."default" NOT NULL DEFAULT '0'::bpchar,
  "remark" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6) NOT NULL,
  "update_time" timestamp(6) NOT NULL,
  "create_user_id" int8 NOT NULL,
  "update_user_id" int8 NOT NULL
)
;
COMMENT ON COLUMN "public"."jb_qiniu"."id" IS '主键ID';
COMMENT ON COLUMN "public"."jb_qiniu"."name" IS '账号';
COMMENT ON COLUMN "public"."jb_qiniu"."sn" IS '编号SN';
COMMENT ON COLUMN "public"."jb_qiniu"."ak" IS 'AK';
COMMENT ON COLUMN "public"."jb_qiniu"."sk" IS 'SK';
COMMENT ON COLUMN "public"."jb_qiniu"."type" IS '账号类型';
COMMENT ON COLUMN "public"."jb_qiniu"."bucket_count" IS '空间个数';
COMMENT ON COLUMN "public"."jb_qiniu"."is_default" IS '是否默认';
COMMENT ON COLUMN "public"."jb_qiniu"."enable" IS '是否启用';
COMMENT ON COLUMN "public"."jb_qiniu"."remark" IS '备注';
COMMENT ON COLUMN "public"."jb_qiniu"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."jb_qiniu"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."jb_qiniu"."create_user_id" IS '创建人';
COMMENT ON COLUMN "public"."jb_qiniu"."update_user_id" IS '更新人';
COMMENT ON TABLE "public"."jb_qiniu" IS '七牛账号表';

-- ----------------------------
-- Table structure for jb_qiniu_bucket
-- ----------------------------
DROP TABLE IF EXISTS "public"."jb_qiniu_bucket";
CREATE TABLE "public"."jb_qiniu_bucket" (
  "id" int8 NOT NULL,
  "name" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "sn" varchar(40) COLLATE "pg_catalog"."default" NOT NULL,
  "qiniu_id" int8 NOT NULL,
  "domain_url" varchar(255) COLLATE "pg_catalog"."default",
  "callback_url" varchar(255) COLLATE "pg_catalog"."default",
  "callback_body" varchar(255) COLLATE "pg_catalog"."default",
  "callback_body_type" varchar(64) COLLATE "pg_catalog"."default",
  "expires" int4,
  "is_default" char(1) COLLATE "pg_catalog"."default" NOT NULL DEFAULT '0'::bpchar,
  "enable" char(1) COLLATE "pg_catalog"."default" NOT NULL DEFAULT '0'::bpchar,
  "remark" varchar(255) COLLATE "pg_catalog"."default",
  "create_user_id" int8 NOT NULL,
  "create_time" timestamp(6) NOT NULL,
  "update_user_id" int8 NOT NULL,
  "update_time" timestamp(6),
  "region" varchar(20) COLLATE "pg_catalog"."default" NOT NULL
)
;
COMMENT ON COLUMN "public"."jb_qiniu_bucket"."id" IS '主键ID';
COMMENT ON COLUMN "public"."jb_qiniu_bucket"."name" IS 'bucket名称';
COMMENT ON COLUMN "public"."jb_qiniu_bucket"."sn" IS '编码';
COMMENT ON COLUMN "public"."jb_qiniu_bucket"."qiniu_id" IS '所属七牛账号';
COMMENT ON COLUMN "public"."jb_qiniu_bucket"."domain_url" IS '绑定域名';
COMMENT ON COLUMN "public"."jb_qiniu_bucket"."callback_url" IS '回调地址';
COMMENT ON COLUMN "public"."jb_qiniu_bucket"."callback_body" IS '回调body定义';
COMMENT ON COLUMN "public"."jb_qiniu_bucket"."callback_body_type" IS '回调Body类型';
COMMENT ON COLUMN "public"."jb_qiniu_bucket"."expires" IS '有效期(秒)';
COMMENT ON COLUMN "public"."jb_qiniu_bucket"."is_default" IS '是否默认';
COMMENT ON COLUMN "public"."jb_qiniu_bucket"."enable" IS '是否启用';
COMMENT ON COLUMN "public"."jb_qiniu_bucket"."remark" IS '备注';
COMMENT ON COLUMN "public"."jb_qiniu_bucket"."create_user_id" IS '创建人';
COMMENT ON COLUMN "public"."jb_qiniu_bucket"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."jb_qiniu_bucket"."update_user_id" IS '更新人';
COMMENT ON COLUMN "public"."jb_qiniu_bucket"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."jb_qiniu_bucket"."region" IS '地区';
COMMENT ON TABLE "public"."jb_qiniu_bucket" IS '七牛bucket配置';


-- ----------------------------
-- Primary Key structure for table jb_qiniu
-- ----------------------------
ALTER TABLE "public"."jb_qiniu" ADD CONSTRAINT "jb_qiniu_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table jb_qiniu_bucket
-- ----------------------------
ALTER TABLE "public"."jb_qiniu_bucket" ADD CONSTRAINT "jb_qiniu_bucket_pkey" PRIMARY KEY ("id");