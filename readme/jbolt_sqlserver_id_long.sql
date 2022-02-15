/*
 Navicat Premium Data Transfer

 Source Server         : sqlserver
 Source Server Type    : SQL Server
 Source Server Version : 15002000
 Source Host           : localhost:1433
 Source Catalog        : jbolt_platform_mini
 Source Schema         : dbo

 Target Server Type    : SQL Server
 Target Server Version : 15002000
 File Encoding         : 65001

 Date: 23/06/2021 16:19:06
*/


-- ----------------------------
-- Table structure for jb_application
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[jb_application]') AND type IN ('U'))
	DROP TABLE [dbo].[jb_application]
GO

CREATE TABLE [dbo].[jb_application] (
  [id] bigint  NOT NULL,
  [name] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [brief_info] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [app_id] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [app_secret] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [enable] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
  [create_time] datetime  NOT NULL,
  [update_time] datetime  NULL,
  [user_id] bigint  NOT NULL,
  [update_user_id] bigint  NULL,
  [type] int  NOT NULL,
  [need_check_sign] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
  [is_inner] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
  [link_target_id] bigint  NULL
)
GO

ALTER TABLE [dbo].[jb_application] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'主键ID',
'SCHEMA', N'dbo',
'TABLE', N'jb_application',
'COLUMN', N'id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'应用名称',
'SCHEMA', N'dbo',
'TABLE', N'jb_application',
'COLUMN', N'name'
GO

EXEC sp_addextendedproperty
'MS_Description', N'应用简介',
'SCHEMA', N'dbo',
'TABLE', N'jb_application',
'COLUMN', N'brief_info'
GO

EXEC sp_addextendedproperty
'MS_Description', N'应用ID',
'SCHEMA', N'dbo',
'TABLE', N'jb_application',
'COLUMN', N'app_id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'应用密钥',
'SCHEMA', N'dbo',
'TABLE', N'jb_application',
'COLUMN', N'app_secret'
GO

EXEC sp_addextendedproperty
'MS_Description', N'是否启用',
'SCHEMA', N'dbo',
'TABLE', N'jb_application',
'COLUMN', N'enable'
GO

EXEC sp_addextendedproperty
'MS_Description', N'创建时间',
'SCHEMA', N'dbo',
'TABLE', N'jb_application',
'COLUMN', N'create_time'
GO

EXEC sp_addextendedproperty
'MS_Description', N'更新时间',
'SCHEMA', N'dbo',
'TABLE', N'jb_application',
'COLUMN', N'update_time'
GO

EXEC sp_addextendedproperty
'MS_Description', N'创建用户ID',
'SCHEMA', N'dbo',
'TABLE', N'jb_application',
'COLUMN', N'user_id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'更新用户ID',
'SCHEMA', N'dbo',
'TABLE', N'jb_application',
'COLUMN', N'update_user_id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'app类型',
'SCHEMA', N'dbo',
'TABLE', N'jb_application',
'COLUMN', N'type'
GO

EXEC sp_addextendedproperty
'MS_Description', N'是否需要接口校验SIGN',
'SCHEMA', N'dbo',
'TABLE', N'jb_application',
'COLUMN', N'need_check_sign'
GO

EXEC sp_addextendedproperty
'MS_Description', N'是否系统内置',
'SCHEMA', N'dbo',
'TABLE', N'jb_application',
'COLUMN', N'is_inner'
GO

EXEC sp_addextendedproperty
'MS_Description', N'关联目标ID',
'SCHEMA', N'dbo',
'TABLE', N'jb_application',
'COLUMN', N'link_target_id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'API应用中心的应用APP',
'SCHEMA', N'dbo',
'TABLE', N'jb_application'
GO


-- ----------------------------
-- Table structure for jb_dept
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[jb_dept]') AND type IN ('U'))
	DROP TABLE [dbo].[jb_dept]
GO

CREATE TABLE [dbo].[jb_dept] (
  [id] bigint  NOT NULL,
  [name] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [full_name] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [pid] bigint  NOT NULL,
  [type] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [leader] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NULL,
  [phone] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NULL,
  [email] nvarchar(100) COLLATE Chinese_PRC_CI_AS  NULL,
  [address] nvarchar(100) COLLATE Chinese_PRC_CI_AS  NULL,
  [zipcode] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NULL,
  [remark] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [sn] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [sort_rank] int  NOT NULL,
  [enable] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '1' NOT NULL,
  [create_time] datetime  NULL,
  [update_time] datetime  NULL
)
GO

ALTER TABLE [dbo].[jb_dept] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'主键ID',
'SCHEMA', N'dbo',
'TABLE', N'jb_dept',
'COLUMN', N'id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'部门名称',
'SCHEMA', N'dbo',
'TABLE', N'jb_dept',
'COLUMN', N'name'
GO

EXEC sp_addextendedproperty
'MS_Description', N'全称',
'SCHEMA', N'dbo',
'TABLE', N'jb_dept',
'COLUMN', N'full_name'
GO

EXEC sp_addextendedproperty
'MS_Description', N'父级ID',
'SCHEMA', N'dbo',
'TABLE', N'jb_dept',
'COLUMN', N'pid'
GO

EXEC sp_addextendedproperty
'MS_Description', N'类型',
'SCHEMA', N'dbo',
'TABLE', N'jb_dept',
'COLUMN', N'type'
GO

EXEC sp_addextendedproperty
'MS_Description', N'负责人',
'SCHEMA', N'dbo',
'TABLE', N'jb_dept',
'COLUMN', N'leader'
GO

EXEC sp_addextendedproperty
'MS_Description', N'手机号',
'SCHEMA', N'dbo',
'TABLE', N'jb_dept',
'COLUMN', N'phone'
GO

EXEC sp_addextendedproperty
'MS_Description', N'电子邮箱',
'SCHEMA', N'dbo',
'TABLE', N'jb_dept',
'COLUMN', N'email'
GO

EXEC sp_addextendedproperty
'MS_Description', N'地址',
'SCHEMA', N'dbo',
'TABLE', N'jb_dept',
'COLUMN', N'address'
GO

EXEC sp_addextendedproperty
'MS_Description', N'邮政编码',
'SCHEMA', N'dbo',
'TABLE', N'jb_dept',
'COLUMN', N'zipcode'
GO

EXEC sp_addextendedproperty
'MS_Description', N'备注',
'SCHEMA', N'dbo',
'TABLE', N'jb_dept',
'COLUMN', N'remark'
GO

EXEC sp_addextendedproperty
'MS_Description', N'编码',
'SCHEMA', N'dbo',
'TABLE', N'jb_dept',
'COLUMN', N'sn'
GO

EXEC sp_addextendedproperty
'MS_Description', N'排序',
'SCHEMA', N'dbo',
'TABLE', N'jb_dept',
'COLUMN', N'sort_rank'
GO

EXEC sp_addextendedproperty
'MS_Description', N'启用',
'SCHEMA', N'dbo',
'TABLE', N'jb_dept',
'COLUMN', N'enable'
GO

EXEC sp_addextendedproperty
'MS_Description', N'创建时间',
'SCHEMA', N'dbo',
'TABLE', N'jb_dept',
'COLUMN', N'create_time'
GO

EXEC sp_addextendedproperty
'MS_Description', N'更新时间',
'SCHEMA', N'dbo',
'TABLE', N'jb_dept',
'COLUMN', N'update_time'
GO

EXEC sp_addextendedproperty
'MS_Description', N'部门管理',
'SCHEMA', N'dbo',
'TABLE', N'jb_dept'
GO


-- ----------------------------
-- Table structure for jb_dictionary
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[jb_dictionary]') AND type IN ('U'))
	DROP TABLE [dbo].[jb_dictionary]
GO

CREATE TABLE [dbo].[jb_dictionary] (
  [id] bigint  NOT NULL,
  [name] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [type_id] bigint  NOT NULL,
  [pid] bigint  NULL,
  [sort_rank] int  NOT NULL,
  [sn] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [type_key] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NOT NULL
)
GO

ALTER TABLE [dbo].[jb_dictionary] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'字典ID主键',
'SCHEMA', N'dbo',
'TABLE', N'jb_dictionary',
'COLUMN', N'id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'名称',
'SCHEMA', N'dbo',
'TABLE', N'jb_dictionary',
'COLUMN', N'name'
GO

EXEC sp_addextendedproperty
'MS_Description', N'字典类型ID',
'SCHEMA', N'dbo',
'TABLE', N'jb_dictionary',
'COLUMN', N'type_id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'父类ID',
'SCHEMA', N'dbo',
'TABLE', N'jb_dictionary',
'COLUMN', N'pid'
GO

EXEC sp_addextendedproperty
'MS_Description', N'排序',
'SCHEMA', N'dbo',
'TABLE', N'jb_dictionary',
'COLUMN', N'sort_rank'
GO

EXEC sp_addextendedproperty
'MS_Description', N'编号编码',
'SCHEMA', N'dbo',
'TABLE', N'jb_dictionary',
'COLUMN', N'sn'
GO

EXEC sp_addextendedproperty
'MS_Description', N'字典类型KEY',
'SCHEMA', N'dbo',
'TABLE', N'jb_dictionary',
'COLUMN', N'type_key'
GO

EXEC sp_addextendedproperty
'MS_Description', N'字典表',
'SCHEMA', N'dbo',
'TABLE', N'jb_dictionary'
GO


-- ----------------------------
-- Table structure for jb_dictionary_type
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[jb_dictionary_type]') AND type IN ('U'))
	DROP TABLE [dbo].[jb_dictionary_type]
GO

CREATE TABLE [dbo].[jb_dictionary_type] (
  [id] bigint  NOT NULL,
  [name] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [mode_level] int  NOT NULL,
  [type_key] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NOT NULL
)
GO

ALTER TABLE [dbo].[jb_dictionary_type] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'主键ID',
'SCHEMA', N'dbo',
'TABLE', N'jb_dictionary_type',
'COLUMN', N'id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'名称',
'SCHEMA', N'dbo',
'TABLE', N'jb_dictionary_type',
'COLUMN', N'name'
GO

EXEC sp_addextendedproperty
'MS_Description', N'模式层级',
'SCHEMA', N'dbo',
'TABLE', N'jb_dictionary_type',
'COLUMN', N'mode_level'
GO

EXEC sp_addextendedproperty
'MS_Description', N'标识KEY',
'SCHEMA', N'dbo',
'TABLE', N'jb_dictionary_type',
'COLUMN', N'type_key'
GO

EXEC sp_addextendedproperty
'MS_Description', N'字典类型',
'SCHEMA', N'dbo',
'TABLE', N'jb_dictionary_type'
GO

-- ----------------------------
-- Table structure for jb_global_config
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[jb_global_config]') AND type IN ('U'))
	DROP TABLE [dbo].[jb_global_config]
GO

CREATE TABLE [dbo].[jb_global_config] (
  [id] bigint  NOT NULL,
  [config_key] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [config_value] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [create_time] datetime  NOT NULL,
  [user_id] bigint  NOT NULL,
  [update_time] datetime  NULL,
  [update_user_id] bigint  NULL,
  [name] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [value_type] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [type_key] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [type_id] bigint  NULL,
  [built_in] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL
)
GO

ALTER TABLE [dbo].[jb_global_config] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'主键ID',
'SCHEMA', N'dbo',
'TABLE', N'jb_global_config',
'COLUMN', N'id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'参数KEY',
'SCHEMA', N'dbo',
'TABLE', N'jb_global_config',
'COLUMN', N'config_key'
GO

EXEC sp_addextendedproperty
'MS_Description', N'参数值',
'SCHEMA', N'dbo',
'TABLE', N'jb_global_config',
'COLUMN', N'config_value'
GO

EXEC sp_addextendedproperty
'MS_Description', N'创建时间',
'SCHEMA', N'dbo',
'TABLE', N'jb_global_config',
'COLUMN', N'create_time'
GO

EXEC sp_addextendedproperty
'MS_Description', N'创建用户ID',
'SCHEMA', N'dbo',
'TABLE', N'jb_global_config',
'COLUMN', N'user_id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'更新时间',
'SCHEMA', N'dbo',
'TABLE', N'jb_global_config',
'COLUMN', N'update_time'
GO

EXEC sp_addextendedproperty
'MS_Description', N'更新用户ID',
'SCHEMA', N'dbo',
'TABLE', N'jb_global_config',
'COLUMN', N'update_user_id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'参数名',
'SCHEMA', N'dbo',
'TABLE', N'jb_global_config',
'COLUMN', N'name'
GO

EXEC sp_addextendedproperty
'MS_Description', N'值类型',
'SCHEMA', N'dbo',
'TABLE', N'jb_global_config',
'COLUMN', N'value_type'
GO

EXEC sp_addextendedproperty
'MS_Description', N'参数类型KEY',
'SCHEMA', N'dbo',
'TABLE', N'jb_global_config',
'COLUMN', N'type_key'
GO

EXEC sp_addextendedproperty
'MS_Description', N'参数类型ID',
'SCHEMA', N'dbo',
'TABLE', N'jb_global_config',
'COLUMN', N'type_id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'系统内置',
'SCHEMA', N'dbo',
'TABLE', N'jb_global_config',
'COLUMN', N'built_in'
GO

EXEC sp_addextendedproperty
'MS_Description', N'全局参数配置',
'SCHEMA', N'dbo',
'TABLE', N'jb_global_config'
GO


-- ----------------------------
-- Table structure for jb_global_config_type
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[jb_global_config_type]') AND type IN ('U'))
	DROP TABLE [dbo].[jb_global_config_type]
GO

CREATE TABLE [dbo].[jb_global_config_type] (
  [id] bigint  NOT NULL,
  [name] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [sort_rank] int  NOT NULL,
  [type_key] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [built_in] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL
)
GO

ALTER TABLE [dbo].[jb_global_config_type] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'主键ID',
'SCHEMA', N'dbo',
'TABLE', N'jb_global_config_type',
'COLUMN', N'id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'类型名称',
'SCHEMA', N'dbo',
'TABLE', N'jb_global_config_type',
'COLUMN', N'name'
GO

EXEC sp_addextendedproperty
'MS_Description', N'排序',
'SCHEMA', N'dbo',
'TABLE', N'jb_global_config_type',
'COLUMN', N'sort_rank'
GO

EXEC sp_addextendedproperty
'MS_Description', N'参数类型KEY',
'SCHEMA', N'dbo',
'TABLE', N'jb_global_config_type',
'COLUMN', N'type_key'
GO

EXEC sp_addextendedproperty
'MS_Description', N'系统内置',
'SCHEMA', N'dbo',
'TABLE', N'jb_global_config_type',
'COLUMN', N'built_in'
GO

EXEC sp_addextendedproperty
'MS_Description', N'全局参数类型',
'SCHEMA', N'dbo',
'TABLE', N'jb_global_config_type'
GO

-- ----------------------------
-- Table structure for jb_jbolt_file
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[jb_jbolt_file]') AND type IN ('U'))
	DROP TABLE [dbo].[jb_jbolt_file]
GO

CREATE TABLE [dbo].[jb_jbolt_file] (
  [id] bigint  NOT NULL,
  [local_path] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [local_url] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [cdn_url] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [create_time] datetime  NOT NULL,
  [user_id] bigint  NOT NULL,
  [file_name] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [file_type] int  NOT NULL,
  [file_size] int  NOT NULL,
  [file_suffix] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[jb_jbolt_file] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'主键ID',
'SCHEMA', N'dbo',
'TABLE', N'jb_jbolt_file',
'COLUMN', N'id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'保存物理地址',
'SCHEMA', N'dbo',
'TABLE', N'jb_jbolt_file',
'COLUMN', N'local_path'
GO

EXEC sp_addextendedproperty
'MS_Description', N'本地可访问URL地址',
'SCHEMA', N'dbo',
'TABLE', N'jb_jbolt_file',
'COLUMN', N'local_url'
GO

EXEC sp_addextendedproperty
'MS_Description', N'外部CDN地址',
'SCHEMA', N'dbo',
'TABLE', N'jb_jbolt_file',
'COLUMN', N'cdn_url'
GO

EXEC sp_addextendedproperty
'MS_Description', N'创建时间',
'SCHEMA', N'dbo',
'TABLE', N'jb_jbolt_file',
'COLUMN', N'create_time'
GO

EXEC sp_addextendedproperty
'MS_Description', N'用户ID',
'SCHEMA', N'dbo',
'TABLE', N'jb_jbolt_file',
'COLUMN', N'user_id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'文件名',
'SCHEMA', N'dbo',
'TABLE', N'jb_jbolt_file',
'COLUMN', N'file_name'
GO

EXEC sp_addextendedproperty
'MS_Description', N'文件类型 图片 附件 视频 音频',
'SCHEMA', N'dbo',
'TABLE', N'jb_jbolt_file',
'COLUMN', N'file_type'
GO

EXEC sp_addextendedproperty
'MS_Description', N'文件大小',
'SCHEMA', N'dbo',
'TABLE', N'jb_jbolt_file',
'COLUMN', N'file_size'
GO

EXEC sp_addextendedproperty
'MS_Description', N'后缀名',
'SCHEMA', N'dbo',
'TABLE', N'jb_jbolt_file',
'COLUMN', N'file_suffix'
GO

EXEC sp_addextendedproperty
'MS_Description', N'文件库',
'SCHEMA', N'dbo',
'TABLE', N'jb_jbolt_file'
GO

-- ----------------------------
-- Table structure for jb_login_log
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[jb_login_log]') AND type IN ('U'))
	DROP TABLE [dbo].[jb_login_log]
GO

CREATE TABLE [dbo].[jb_login_log] (
  [id] bigint  NOT NULL,
  [user_id] bigint,
  [username] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [login_ip] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NULL,
  [login_time] datetime  NOT NULL,
  [login_state] int  NOT NULL,
  [is_browser] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
  [browser_version] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NULL,
  [browser_name] nvarchar(100) COLLATE Chinese_PRC_CI_AS  NULL,
  [os_name] nvarchar(100) COLLATE Chinese_PRC_CI_AS  NULL,
  [login_city] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NULL,
  [login_address] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [login_city_code] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NULL,
  [login_province] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NULL,
  [login_country] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NULL,
  [is_mobile] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
  [os_platform_name] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NULL,
  [browser_engine_name] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NULL,
  [browser_engine_version] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NULL,
  [login_address_latitude] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NULL,
  [login_address_longitude] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NULL,
  [is_remote_login] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
  [create_time] datetime  NOT NULL
)
GO

ALTER TABLE [dbo].[jb_login_log] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'主键ID',
'SCHEMA', N'dbo',
'TABLE', N'jb_login_log',
'COLUMN', N'id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'用户ID',
'SCHEMA', N'dbo',
'TABLE', N'jb_login_log',
'COLUMN', N'user_id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'用户名',
'SCHEMA', N'dbo',
'TABLE', N'jb_login_log',
'COLUMN', N'username'
GO

EXEC sp_addextendedproperty
'MS_Description', N'IP地址',
'SCHEMA', N'dbo',
'TABLE', N'jb_login_log',
'COLUMN', N'login_ip'
GO

EXEC sp_addextendedproperty
'MS_Description', N'登录时间',
'SCHEMA', N'dbo',
'TABLE', N'jb_login_log',
'COLUMN', N'login_time'
GO

EXEC sp_addextendedproperty
'MS_Description', N'登录状态',
'SCHEMA', N'dbo',
'TABLE', N'jb_login_log',
'COLUMN', N'login_state'
GO

EXEC sp_addextendedproperty
'MS_Description', N'是否是浏览器访问',
'SCHEMA', N'dbo',
'TABLE', N'jb_login_log',
'COLUMN', N'is_browser'
GO

EXEC sp_addextendedproperty
'MS_Description', N'浏览器版本号',
'SCHEMA', N'dbo',
'TABLE', N'jb_login_log',
'COLUMN', N'browser_version'
GO

EXEC sp_addextendedproperty
'MS_Description', N'浏览器',
'SCHEMA', N'dbo',
'TABLE', N'jb_login_log',
'COLUMN', N'browser_name'
GO

EXEC sp_addextendedproperty
'MS_Description', N'操作系统',
'SCHEMA', N'dbo',
'TABLE', N'jb_login_log',
'COLUMN', N'os_name'
GO

EXEC sp_addextendedproperty
'MS_Description', N'登录城市',
'SCHEMA', N'dbo',
'TABLE', N'jb_login_log',
'COLUMN', N'login_city'
GO

EXEC sp_addextendedproperty
'MS_Description', N'登录位置详情',
'SCHEMA', N'dbo',
'TABLE', N'jb_login_log',
'COLUMN', N'login_address'
GO

EXEC sp_addextendedproperty
'MS_Description', N'登录城市代码',
'SCHEMA', N'dbo',
'TABLE', N'jb_login_log',
'COLUMN', N'login_city_code'
GO

EXEC sp_addextendedproperty
'MS_Description', N'登录省份',
'SCHEMA', N'dbo',
'TABLE', N'jb_login_log',
'COLUMN', N'login_province'
GO

EXEC sp_addextendedproperty
'MS_Description', N'登录国家',
'SCHEMA', N'dbo',
'TABLE', N'jb_login_log',
'COLUMN', N'login_country'
GO

EXEC sp_addextendedproperty
'MS_Description', N'是否移动端',
'SCHEMA', N'dbo',
'TABLE', N'jb_login_log',
'COLUMN', N'is_mobile'
GO

EXEC sp_addextendedproperty
'MS_Description', N'平台名称',
'SCHEMA', N'dbo',
'TABLE', N'jb_login_log',
'COLUMN', N'os_platform_name'
GO

EXEC sp_addextendedproperty
'MS_Description', N'浏览器引擎名',
'SCHEMA', N'dbo',
'TABLE', N'jb_login_log',
'COLUMN', N'browser_engine_name'
GO

EXEC sp_addextendedproperty
'MS_Description', N'浏览器引擎版本',
'SCHEMA', N'dbo',
'TABLE', N'jb_login_log',
'COLUMN', N'browser_engine_version'
GO

EXEC sp_addextendedproperty
'MS_Description', N'登录地横坐标',
'SCHEMA', N'dbo',
'TABLE', N'jb_login_log',
'COLUMN', N'login_address_latitude'
GO

EXEC sp_addextendedproperty
'MS_Description', N'登录地纵坐标',
'SCHEMA', N'dbo',
'TABLE', N'jb_login_log',
'COLUMN', N'login_address_longitude'
GO

EXEC sp_addextendedproperty
'MS_Description', N'是否为异地异常登录',
'SCHEMA', N'dbo',
'TABLE', N'jb_login_log',
'COLUMN', N'is_remote_login'
GO

EXEC sp_addextendedproperty
'MS_Description', N'记录创建时间',
'SCHEMA', N'dbo',
'TABLE', N'jb_login_log',
'COLUMN', N'create_time'
GO

EXEC sp_addextendedproperty
'MS_Description', N'登录日志',
'SCHEMA', N'dbo',
'TABLE', N'jb_login_log'
GO


-- ----------------------------
-- Table structure for jb_online_user
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[jb_online_user]') AND type IN ('U'))
	DROP TABLE [dbo].[jb_online_user]
GO

CREATE TABLE [dbo].[jb_online_user] (
  [id] bigint  NOT NULL,
  [session_id] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [user_id] bigint  NOT NULL,
  [login_log_id] bigint  NOT NULL,
  [login_time] datetime  NOT NULL,
  [update_time] datetime  NOT NULL,
  [expiration_time] datetime  NOT NULL,
  [screen_locked] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
  [online_state] int  NOT NULL,
  [offline_time] datetime  NULL
)
GO

ALTER TABLE [dbo].[jb_online_user] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'主键ID',
'SCHEMA', N'dbo',
'TABLE', N'jb_online_user',
'COLUMN', N'id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'会话ID',
'SCHEMA', N'dbo',
'TABLE', N'jb_online_user',
'COLUMN', N'session_id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'用户ID',
'SCHEMA', N'dbo',
'TABLE', N'jb_online_user',
'COLUMN', N'user_id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'登录日志ID',
'SCHEMA', N'dbo',
'TABLE', N'jb_online_user',
'COLUMN', N'login_log_id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'登录时间',
'SCHEMA', N'dbo',
'TABLE', N'jb_online_user',
'COLUMN', N'login_time'
GO

EXEC sp_addextendedproperty
'MS_Description', N'更新时间',
'SCHEMA', N'dbo',
'TABLE', N'jb_online_user',
'COLUMN', N'update_time'
GO

EXEC sp_addextendedproperty
'MS_Description', N'过期时间',
'SCHEMA', N'dbo',
'TABLE', N'jb_online_user',
'COLUMN', N'expiration_time'
GO

EXEC sp_addextendedproperty
'MS_Description', N'是否锁屏',
'SCHEMA', N'dbo',
'TABLE', N'jb_online_user',
'COLUMN', N'screen_locked'
GO

EXEC sp_addextendedproperty
'MS_Description', N'在线状态',
'SCHEMA', N'dbo',
'TABLE', N'jb_online_user',
'COLUMN', N'online_state'
GO

EXEC sp_addextendedproperty
'MS_Description', N'离线时间',
'SCHEMA', N'dbo',
'TABLE', N'jb_online_user',
'COLUMN', N'offline_time'
GO

EXEC sp_addextendedproperty
'MS_Description', N'在线用户',
'SCHEMA', N'dbo',
'TABLE', N'jb_online_user'
GO

-- ----------------------------
-- Table structure for jb_permission
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[jb_permission]') AND type IN ('U'))
	DROP TABLE [dbo].[jb_permission]
GO

CREATE TABLE [dbo].[jb_permission] (
  [id] bigint  NOT NULL,
  [title] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [pid] bigint  NULL,
  [url] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [icons] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NULL,
  [sort_rank] int  NOT NULL,
  [permission_level] int  NOT NULL,
  [permission_key] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [is_menu] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
  [is_target_blank] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
  [is_system_admin_default] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
  [open_type] int  NOT NULL,
  [open_option] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[jb_permission] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'主键ID',
'SCHEMA', N'dbo',
'TABLE', N'jb_permission',
'COLUMN', N'id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'名称',
'SCHEMA', N'dbo',
'TABLE', N'jb_permission',
'COLUMN', N'title'
GO

EXEC sp_addextendedproperty
'MS_Description', N'上级ID',
'SCHEMA', N'dbo',
'TABLE', N'jb_permission',
'COLUMN', N'pid'
GO

EXEC sp_addextendedproperty
'MS_Description', N'地址',
'SCHEMA', N'dbo',
'TABLE', N'jb_permission',
'COLUMN', N'url'
GO

EXEC sp_addextendedproperty
'MS_Description', N'图标',
'SCHEMA', N'dbo',
'TABLE', N'jb_permission',
'COLUMN', N'icons'
GO

EXEC sp_addextendedproperty
'MS_Description', N'排序',
'SCHEMA', N'dbo',
'TABLE', N'jb_permission',
'COLUMN', N'sort_rank'
GO

EXEC sp_addextendedproperty
'MS_Description', N'层级',
'SCHEMA', N'dbo',
'TABLE', N'jb_permission',
'COLUMN', N'permission_level'
GO

EXEC sp_addextendedproperty
'MS_Description', N'权限资源KEY',
'SCHEMA', N'dbo',
'TABLE', N'jb_permission',
'COLUMN', N'permission_key'
GO

EXEC sp_addextendedproperty
'MS_Description', N'是否是菜单',
'SCHEMA', N'dbo',
'TABLE', N'jb_permission',
'COLUMN', N'is_menu'
GO

EXEC sp_addextendedproperty
'MS_Description', N'是否新窗口打开',
'SCHEMA', N'dbo',
'TABLE', N'jb_permission',
'COLUMN', N'is_target_blank'
GO

EXEC sp_addextendedproperty
'MS_Description', N'是否系统超级管理员默认拥有的权限',
'SCHEMA', N'dbo',
'TABLE', N'jb_permission',
'COLUMN', N'is_system_admin_default'
GO

EXEC sp_addextendedproperty
'MS_Description', N'打开类型 1 默认 2 iframe 3 dialog',
'SCHEMA', N'dbo',
'TABLE', N'jb_permission',
'COLUMN', N'open_type'
GO

EXEC sp_addextendedproperty
'MS_Description', N'组件属性json',
'SCHEMA', N'dbo',
'TABLE', N'jb_permission',
'COLUMN', N'open_option'
GO

EXEC sp_addextendedproperty
'MS_Description', N'权限资源定义',
'SCHEMA', N'dbo',
'TABLE', N'jb_permission'
GO


-- ----------------------------
-- Table structure for jb_post
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[jb_post]') AND type IN ('U'))
	DROP TABLE [dbo].[jb_post]
GO

CREATE TABLE [dbo].[jb_post] (
  [id] bigint  NOT NULL,
  [name] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [type] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [remark] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [sn] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [sort_rank] int  NOT NULL,
  [enable] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '1' NOT NULL
)
GO

ALTER TABLE [dbo].[jb_post] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'主键ID',
'SCHEMA', N'dbo',
'TABLE', N'jb_post',
'COLUMN', N'id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'名称',
'SCHEMA', N'dbo',
'TABLE', N'jb_post',
'COLUMN', N'name'
GO

EXEC sp_addextendedproperty
'MS_Description', N'类型',
'SCHEMA', N'dbo',
'TABLE', N'jb_post',
'COLUMN', N'type'
GO

EXEC sp_addextendedproperty
'MS_Description', N'备注',
'SCHEMA', N'dbo',
'TABLE', N'jb_post',
'COLUMN', N'remark'
GO

EXEC sp_addextendedproperty
'MS_Description', N'编码',
'SCHEMA', N'dbo',
'TABLE', N'jb_post',
'COLUMN', N'sn'
GO

EXEC sp_addextendedproperty
'MS_Description', N'排序',
'SCHEMA', N'dbo',
'TABLE', N'jb_post',
'COLUMN', N'sort_rank'
GO

EXEC sp_addextendedproperty
'MS_Description', N'启用',
'SCHEMA', N'dbo',
'TABLE', N'jb_post',
'COLUMN', N'enable'
GO

EXEC sp_addextendedproperty
'MS_Description', N'岗位管理',
'SCHEMA', N'dbo',
'TABLE', N'jb_post'
GO


-- ----------------------------
-- Table structure for jb_private_message
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[jb_private_message]') AND type IN ('U'))
	DROP TABLE [dbo].[jb_private_message]
GO

CREATE TABLE [dbo].[jb_private_message] (
  [id] bigint  NOT NULL,
  [content] nvarchar(max) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [create_time] datetime  NOT NULL,
  [from_user_id] bigint  NOT NULL,
  [to_user_id] bigint  NOT NULL
)
GO

ALTER TABLE [dbo].[jb_private_message] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'主键ID',
'SCHEMA', N'dbo',
'TABLE', N'jb_private_message',
'COLUMN', N'id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'私信内容',
'SCHEMA', N'dbo',
'TABLE', N'jb_private_message',
'COLUMN', N'content'
GO

EXEC sp_addextendedproperty
'MS_Description', N'发送时间',
'SCHEMA', N'dbo',
'TABLE', N'jb_private_message',
'COLUMN', N'create_time'
GO

EXEC sp_addextendedproperty
'MS_Description', N'发信人',
'SCHEMA', N'dbo',
'TABLE', N'jb_private_message',
'COLUMN', N'from_user_id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'收信人',
'SCHEMA', N'dbo',
'TABLE', N'jb_private_message',
'COLUMN', N'to_user_id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'内部私信',
'SCHEMA', N'dbo',
'TABLE', N'jb_private_message'
GO


-- ----------------------------
-- Table structure for jb_remote_login_log
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[jb_remote_login_log]') AND type IN ('U'))
	DROP TABLE [dbo].[jb_remote_login_log]
GO

CREATE TABLE [dbo].[jb_remote_login_log] (
  [id] bigint  NOT NULL,
  [last_login_country] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [last_login_province] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [last_login_city] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [last_login_time] datetime  NOT NULL,
  [login_country] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NULL,
  [login_province] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NULL,
  [login_city] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NULL,
  [login_time] datetime  NULL,
  [user_id] bigint  NOT NULL,
  [username] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [is_new] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
  [last_login_ip] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [login_ip] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [create_time] datetime  NOT NULL
)
GO

ALTER TABLE [dbo].[jb_remote_login_log] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'主键ID',
'SCHEMA', N'dbo',
'TABLE', N'jb_remote_login_log',
'COLUMN', N'id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'最近一次登录国家',
'SCHEMA', N'dbo',
'TABLE', N'jb_remote_login_log',
'COLUMN', N'last_login_country'
GO

EXEC sp_addextendedproperty
'MS_Description', N'最近一次登录省份',
'SCHEMA', N'dbo',
'TABLE', N'jb_remote_login_log',
'COLUMN', N'last_login_province'
GO

EXEC sp_addextendedproperty
'MS_Description', N'最近一次登录城市',
'SCHEMA', N'dbo',
'TABLE', N'jb_remote_login_log',
'COLUMN', N'last_login_city'
GO

EXEC sp_addextendedproperty
'MS_Description', N'最近一次登录时间',
'SCHEMA', N'dbo',
'TABLE', N'jb_remote_login_log',
'COLUMN', N'last_login_time'
GO

EXEC sp_addextendedproperty
'MS_Description', N'新登录国家',
'SCHEMA', N'dbo',
'TABLE', N'jb_remote_login_log',
'COLUMN', N'login_country'
GO

EXEC sp_addextendedproperty
'MS_Description', N'新登录省份',
'SCHEMA', N'dbo',
'TABLE', N'jb_remote_login_log',
'COLUMN', N'login_province'
GO

EXEC sp_addextendedproperty
'MS_Description', N'新登录城市',
'SCHEMA', N'dbo',
'TABLE', N'jb_remote_login_log',
'COLUMN', N'login_city'
GO

EXEC sp_addextendedproperty
'MS_Description', N'新登录时间',
'SCHEMA', N'dbo',
'TABLE', N'jb_remote_login_log',
'COLUMN', N'login_time'
GO

EXEC sp_addextendedproperty
'MS_Description', N'登录用户ID',
'SCHEMA', N'dbo',
'TABLE', N'jb_remote_login_log',
'COLUMN', N'user_id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'登录用户名',
'SCHEMA', N'dbo',
'TABLE', N'jb_remote_login_log',
'COLUMN', N'username'
GO

EXEC sp_addextendedproperty
'MS_Description', N'是否为最新一次',
'SCHEMA', N'dbo',
'TABLE', N'jb_remote_login_log',
'COLUMN', N'is_new'
GO

EXEC sp_addextendedproperty
'MS_Description', N'最近一次登录IP',
'SCHEMA', N'dbo',
'TABLE', N'jb_remote_login_log',
'COLUMN', N'last_login_ip'
GO

EXEC sp_addextendedproperty
'MS_Description', N'新登录IP',
'SCHEMA', N'dbo',
'TABLE', N'jb_remote_login_log',
'COLUMN', N'login_ip'
GO

EXEC sp_addextendedproperty
'MS_Description', N'记录创建时间',
'SCHEMA', N'dbo',
'TABLE', N'jb_remote_login_log',
'COLUMN', N'create_time'
GO

EXEC sp_addextendedproperty
'MS_Description', N'异地登录日志记录',
'SCHEMA', N'dbo',
'TABLE', N'jb_remote_login_log'
GO


-- ----------------------------
-- Table structure for jb_role
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[jb_role]') AND type IN ('U'))
	DROP TABLE [dbo].[jb_role]
GO

CREATE TABLE [dbo].[jb_role] (
  [id] bigint  NOT NULL,
  [name] nvarchar(20) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [sn] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NULL,
  [pid] bigint  NULL
)
GO

ALTER TABLE [dbo].[jb_role] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'主键ID',
'SCHEMA', N'dbo',
'TABLE', N'jb_role',
'COLUMN', N'id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'名称',
'SCHEMA', N'dbo',
'TABLE', N'jb_role',
'COLUMN', N'name'
GO

EXEC sp_addextendedproperty
'MS_Description', N'编码',
'SCHEMA', N'dbo',
'TABLE', N'jb_role',
'COLUMN', N'sn'
GO

EXEC sp_addextendedproperty
'MS_Description', N'父级角色ID',
'SCHEMA', N'dbo',
'TABLE', N'jb_role',
'COLUMN', N'pid'
GO

EXEC sp_addextendedproperty
'MS_Description', N'角色表',
'SCHEMA', N'dbo',
'TABLE', N'jb_role'
GO


-- ----------------------------
-- Table structure for jb_role_permission
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[jb_role_permission]') AND type IN ('U'))
	DROP TABLE [dbo].[jb_role_permission]
GO

CREATE TABLE [dbo].[jb_role_permission] (
  [id] bigint  NOT NULL,
  [role_id] bigint  NOT NULL,
  [permission_id] bigint  NOT NULL
)
GO

ALTER TABLE [dbo].[jb_role_permission] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'主键ID',
'SCHEMA', N'dbo',
'TABLE', N'jb_role_permission',
'COLUMN', N'id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'角色ID',
'SCHEMA', N'dbo',
'TABLE', N'jb_role_permission',
'COLUMN', N'role_id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'权限ID',
'SCHEMA', N'dbo',
'TABLE', N'jb_role_permission',
'COLUMN', N'permission_id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'角色功能列表',
'SCHEMA', N'dbo',
'TABLE', N'jb_role_permission'
GO


-- ----------------------------
-- Table structure for jb_sys_notice
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[jb_sys_notice]') AND type IN ('U'))
	DROP TABLE [dbo].[jb_sys_notice]
GO

CREATE TABLE [dbo].[jb_sys_notice] (
  [id] bigint  NOT NULL,
  [title] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [content] nvarchar(max) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [type] int  NOT NULL,
  [priority_level] int  NOT NULL,
  [read_count] int  NULL,
  [create_time] datetime  NOT NULL,
  [update_time] datetime  NULL,
  [create_user_id] bigint  NULL,
  [update_user_id] bigint  NULL,
  [receiver_type] int  NULL,
  [receiver_value] nvarchar(max) COLLATE Chinese_PRC_CI_AS  NULL,
  [files] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [del_flag] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL
)
GO

ALTER TABLE [dbo].[jb_sys_notice] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'主键ID',
'SCHEMA', N'dbo',
'TABLE', N'jb_sys_notice',
'COLUMN', N'id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'标题',
'SCHEMA', N'dbo',
'TABLE', N'jb_sys_notice',
'COLUMN', N'title'
GO

EXEC sp_addextendedproperty
'MS_Description', N'消息内容',
'SCHEMA', N'dbo',
'TABLE', N'jb_sys_notice',
'COLUMN', N'content'
GO

EXEC sp_addextendedproperty
'MS_Description', N'通知类型',
'SCHEMA', N'dbo',
'TABLE', N'jb_sys_notice',
'COLUMN', N'type'
GO

EXEC sp_addextendedproperty
'MS_Description', N'优先级',
'SCHEMA', N'dbo',
'TABLE', N'jb_sys_notice',
'COLUMN', N'priority_level'
GO

EXEC sp_addextendedproperty
'MS_Description', N'已读人数',
'SCHEMA', N'dbo',
'TABLE', N'jb_sys_notice',
'COLUMN', N'read_count'
GO

EXEC sp_addextendedproperty
'MS_Description', N'创建时间',
'SCHEMA', N'dbo',
'TABLE', N'jb_sys_notice',
'COLUMN', N'create_time'
GO

EXEC sp_addextendedproperty
'MS_Description', N'更新时间',
'SCHEMA', N'dbo',
'TABLE', N'jb_sys_notice',
'COLUMN', N'update_time'
GO

EXEC sp_addextendedproperty
'MS_Description', N'创建人',
'SCHEMA', N'dbo',
'TABLE', N'jb_sys_notice',
'COLUMN', N'create_user_id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'更新人',
'SCHEMA', N'dbo',
'TABLE', N'jb_sys_notice',
'COLUMN', N'update_user_id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'接收人类型',
'SCHEMA', N'dbo',
'TABLE', N'jb_sys_notice',
'COLUMN', N'receiver_type'
GO

EXEC sp_addextendedproperty
'MS_Description', N'接收人值',
'SCHEMA', N'dbo',
'TABLE', N'jb_sys_notice',
'COLUMN', N'receiver_value'
GO

EXEC sp_addextendedproperty
'MS_Description', N'附件',
'SCHEMA', N'dbo',
'TABLE', N'jb_sys_notice',
'COLUMN', N'files'
GO

EXEC sp_addextendedproperty
'MS_Description', N'删除标志',
'SCHEMA', N'dbo',
'TABLE', N'jb_sys_notice',
'COLUMN', N'del_flag'
GO

EXEC sp_addextendedproperty
'MS_Description', N'系统通知',
'SCHEMA', N'dbo',
'TABLE', N'jb_sys_notice'
GO


-- ----------------------------
-- Table structure for jb_sys_notice_reader
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[jb_sys_notice_reader]') AND type IN ('U'))
	DROP TABLE [dbo].[jb_sys_notice_reader]
GO

CREATE TABLE [dbo].[jb_sys_notice_reader] (
  [id] bigint  NOT NULL,
  [sys_notice_id] bigint  NOT NULL,
  [user_id] bigint  NOT NULL,
  [create_time] datetime  NOT NULL
)
GO

ALTER TABLE [dbo].[jb_sys_notice_reader] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'主键ID',
'SCHEMA', N'dbo',
'TABLE', N'jb_sys_notice_reader',
'COLUMN', N'id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'通知ID',
'SCHEMA', N'dbo',
'TABLE', N'jb_sys_notice_reader',
'COLUMN', N'sys_notice_id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'用户ID',
'SCHEMA', N'dbo',
'TABLE', N'jb_sys_notice_reader',
'COLUMN', N'user_id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'创建时间',
'SCHEMA', N'dbo',
'TABLE', N'jb_sys_notice_reader',
'COLUMN', N'create_time'
GO

EXEC sp_addextendedproperty
'MS_Description', N'通知阅读用户关系表',
'SCHEMA', N'dbo',
'TABLE', N'jb_sys_notice_reader'
GO


-- ----------------------------
-- Table structure for jb_system_log
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[jb_system_log]') AND type IN ('U'))
	DROP TABLE [dbo].[jb_system_log]
GO

CREATE TABLE [dbo].[jb_system_log] (
  [id] bigint  NOT NULL,
  [title] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [type] int  NOT NULL,
  [url] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [user_id] bigint  NOT NULL,
  [user_name] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [target_type] int  NOT NULL,
  [create_time] datetime  NOT NULL,
  [target_id] bigint  NULL,
  [open_type] int  NOT NULL
)
GO

ALTER TABLE [dbo].[jb_system_log] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'主键ID',
'SCHEMA', N'dbo',
'TABLE', N'jb_system_log',
'COLUMN', N'id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'标题',
'SCHEMA', N'dbo',
'TABLE', N'jb_system_log',
'COLUMN', N'title'
GO

EXEC sp_addextendedproperty
'MS_Description', N'操作类型 删除 更新 新增',
'SCHEMA', N'dbo',
'TABLE', N'jb_system_log',
'COLUMN', N'type'
GO

EXEC sp_addextendedproperty
'MS_Description', N'连接对象详情地址',
'SCHEMA', N'dbo',
'TABLE', N'jb_system_log',
'COLUMN', N'url'
GO

EXEC sp_addextendedproperty
'MS_Description', N'操作人ID',
'SCHEMA', N'dbo',
'TABLE', N'jb_system_log',
'COLUMN', N'user_id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'操作人姓名',
'SCHEMA', N'dbo',
'TABLE', N'jb_system_log',
'COLUMN', N'user_name'
GO

EXEC sp_addextendedproperty
'MS_Description', N'操作对象类型',
'SCHEMA', N'dbo',
'TABLE', N'jb_system_log',
'COLUMN', N'target_type'
GO

EXEC sp_addextendedproperty
'MS_Description', N'记录创建时间',
'SCHEMA', N'dbo',
'TABLE', N'jb_system_log',
'COLUMN', N'create_time'
GO

EXEC sp_addextendedproperty
'MS_Description', N'操作对象ID',
'SCHEMA', N'dbo',
'TABLE', N'jb_system_log',
'COLUMN', N'target_id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'打开类型URL还是Dialog',
'SCHEMA', N'dbo',
'TABLE', N'jb_system_log',
'COLUMN', N'open_type'
GO

EXEC sp_addextendedproperty
'MS_Description', N'系统操作日志表',
'SCHEMA', N'dbo',
'TABLE', N'jb_system_log'
GO


-- ----------------------------
-- Table structure for jb_todo
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[jb_todo]') AND type IN ('U'))
	DROP TABLE [dbo].[jb_todo]
GO

CREATE TABLE [dbo].[jb_todo] (
  [id] bigint  NOT NULL,
  [title] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [content] nvarchar(max) COLLATE Chinese_PRC_CI_AS  NULL,
  [user_id] bigint  NOT NULL,
  [state] int  NOT NULL,
  [specified_finish_time] datetime  NOT NULL,
  [type] int  NOT NULL,
  [url] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [priority_level] int  NOT NULL,
  [create_time] datetime  NOT NULL,
  [update_time] datetime  NOT NULL,
  [create_user_id] bigint  NOT NULL,
  [update_user_id] bigint  NULL,
  [real_finish_time] datetime  NULL
)
GO

ALTER TABLE [dbo].[jb_todo] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'主键ID',
'SCHEMA', N'dbo',
'TABLE', N'jb_todo',
'COLUMN', N'id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'标题',
'SCHEMA', N'dbo',
'TABLE', N'jb_todo',
'COLUMN', N'title'
GO

EXEC sp_addextendedproperty
'MS_Description', N'详情内容',
'SCHEMA', N'dbo',
'TABLE', N'jb_todo',
'COLUMN', N'content'
GO

EXEC sp_addextendedproperty
'MS_Description', N'所属用户',
'SCHEMA', N'dbo',
'TABLE', N'jb_todo',
'COLUMN', N'user_id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'状态',
'SCHEMA', N'dbo',
'TABLE', N'jb_todo',
'COLUMN', N'state'
GO

EXEC sp_addextendedproperty
'MS_Description', N'规定完成时间',
'SCHEMA', N'dbo',
'TABLE', N'jb_todo',
'COLUMN', N'specified_finish_time'
GO

EXEC sp_addextendedproperty
'MS_Description', N'类型 链接还是内容 还是都有',
'SCHEMA', N'dbo',
'TABLE', N'jb_todo',
'COLUMN', N'type'
GO

EXEC sp_addextendedproperty
'MS_Description', N'链接',
'SCHEMA', N'dbo',
'TABLE', N'jb_todo',
'COLUMN', N'url'
GO

EXEC sp_addextendedproperty
'MS_Description', N'优先级',
'SCHEMA', N'dbo',
'TABLE', N'jb_todo',
'COLUMN', N'priority_level'
GO

EXEC sp_addextendedproperty
'MS_Description', N'创建时间',
'SCHEMA', N'dbo',
'TABLE', N'jb_todo',
'COLUMN', N'create_time'
GO

EXEC sp_addextendedproperty
'MS_Description', N'更新时间',
'SCHEMA', N'dbo',
'TABLE', N'jb_todo',
'COLUMN', N'update_time'
GO

EXEC sp_addextendedproperty
'MS_Description', N'创建人ID',
'SCHEMA', N'dbo',
'TABLE', N'jb_todo',
'COLUMN', N'create_user_id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'更新人ID',
'SCHEMA', N'dbo',
'TABLE', N'jb_todo',
'COLUMN', N'update_user_id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'完成时间',
'SCHEMA', N'dbo',
'TABLE', N'jb_todo',
'COLUMN', N'real_finish_time'
GO

EXEC sp_addextendedproperty
'MS_Description', N'待办事项',
'SCHEMA', N'dbo',
'TABLE', N'jb_todo'
GO


-- ----------------------------
-- Table structure for jb_topnav
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[jb_topnav]') AND type IN ('U'))
	DROP TABLE [dbo].[jb_topnav]
GO

CREATE TABLE [dbo].[jb_topnav] (
  [id] bigint  NOT NULL,
  [name] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [icon] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NULL,
  [enable] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
  [sort_rank] int  NOT NULL
)
GO

ALTER TABLE [dbo].[jb_topnav] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'主键ID',
'SCHEMA', N'dbo',
'TABLE', N'jb_topnav',
'COLUMN', N'id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'名称',
'SCHEMA', N'dbo',
'TABLE', N'jb_topnav',
'COLUMN', N'name'
GO

EXEC sp_addextendedproperty
'MS_Description', N'图标',
'SCHEMA', N'dbo',
'TABLE', N'jb_topnav',
'COLUMN', N'icon'
GO

EXEC sp_addextendedproperty
'MS_Description', N'是否启用',
'SCHEMA', N'dbo',
'TABLE', N'jb_topnav',
'COLUMN', N'enable'
GO

EXEC sp_addextendedproperty
'MS_Description', N'排序',
'SCHEMA', N'dbo',
'TABLE', N'jb_topnav',
'COLUMN', N'sort_rank'
GO

EXEC sp_addextendedproperty
'MS_Description', N'顶部导航',
'SCHEMA', N'dbo',
'TABLE', N'jb_topnav'
GO


-- ----------------------------
-- Table structure for jb_topnav_menu
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[jb_topnav_menu]') AND type IN ('U'))
	DROP TABLE [dbo].[jb_topnav_menu]
GO

CREATE TABLE [dbo].[jb_topnav_menu] (
  [id] bigint  NOT NULL,
  [topnav_id] bigint  NOT NULL,
  [permission_id] bigint  NOT NULL
)
GO

ALTER TABLE [dbo].[jb_topnav_menu] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'主键ID',
'SCHEMA', N'dbo',
'TABLE', N'jb_topnav_menu',
'COLUMN', N'id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'顶部导航ID',
'SCHEMA', N'dbo',
'TABLE', N'jb_topnav_menu',
'COLUMN', N'topnav_id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'菜单资源ID',
'SCHEMA', N'dbo',
'TABLE', N'jb_topnav_menu',
'COLUMN', N'permission_id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'顶部菜单对应左侧一级导航中间表',
'SCHEMA', N'dbo',
'TABLE', N'jb_topnav_menu'
GO

-- ----------------------------
-- Table structure for jb_user
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[jb_user]') AND type IN ('U'))
	DROP TABLE [dbo].[jb_user]
GO

CREATE TABLE [dbo].[jb_user] (
  [id] bigint  NOT NULL,
  [username] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [password] nvarchar(128) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [name] nvarchar(20) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [avatar] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [create_time] datetime  NOT NULL,
  [phone] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NULL,
  [enable] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
  [sex] int  NULL,
  [pinyin] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [roles] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [is_system_admin] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
  [create_user_id] bigint  NOT NULL,
  [pwd_salt] nvarchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [login_ip] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [login_time] datetime  NULL,
  [login_city] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NULL,
  [login_province] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NULL,
  [login_country] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NULL,
  [is_remote_login] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NULL,
  [dept_id] bigint  NULL,
  [posts] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[jb_user] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'主键ID',
'SCHEMA', N'dbo',
'TABLE', N'jb_user',
'COLUMN', N'id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'用户名',
'SCHEMA', N'dbo',
'TABLE', N'jb_user',
'COLUMN', N'username'
GO

EXEC sp_addextendedproperty
'MS_Description', N'密码',
'SCHEMA', N'dbo',
'TABLE', N'jb_user',
'COLUMN', N'password'
GO

EXEC sp_addextendedproperty
'MS_Description', N'姓名',
'SCHEMA', N'dbo',
'TABLE', N'jb_user',
'COLUMN', N'name'
GO

EXEC sp_addextendedproperty
'MS_Description', N'头像',
'SCHEMA', N'dbo',
'TABLE', N'jb_user',
'COLUMN', N'avatar'
GO

EXEC sp_addextendedproperty
'MS_Description', N'记录创建时间',
'SCHEMA', N'dbo',
'TABLE', N'jb_user',
'COLUMN', N'create_time'
GO

EXEC sp_addextendedproperty
'MS_Description', N'手机号',
'SCHEMA', N'dbo',
'TABLE', N'jb_user',
'COLUMN', N'phone'
GO

EXEC sp_addextendedproperty
'MS_Description', N'启用',
'SCHEMA', N'dbo',
'TABLE', N'jb_user',
'COLUMN', N'enable'
GO

EXEC sp_addextendedproperty
'MS_Description', N'性别',
'SCHEMA', N'dbo',
'TABLE', N'jb_user',
'COLUMN', N'sex'
GO

EXEC sp_addextendedproperty
'MS_Description', N'拼音码',
'SCHEMA', N'dbo',
'TABLE', N'jb_user',
'COLUMN', N'pinyin'
GO

EXEC sp_addextendedproperty
'MS_Description', N'角色 一对多',
'SCHEMA', N'dbo',
'TABLE', N'jb_user',
'COLUMN', N'roles'
GO

EXEC sp_addextendedproperty
'MS_Description', N'是否系统超级管理员',
'SCHEMA', N'dbo',
'TABLE', N'jb_user',
'COLUMN', N'is_system_admin'
GO

EXEC sp_addextendedproperty
'MS_Description', N'创建人',
'SCHEMA', N'dbo',
'TABLE', N'jb_user',
'COLUMN', N'create_user_id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'密码盐值',
'SCHEMA', N'dbo',
'TABLE', N'jb_user',
'COLUMN', N'pwd_salt'
GO

EXEC sp_addextendedproperty
'MS_Description', N'最后登录IP',
'SCHEMA', N'dbo',
'TABLE', N'jb_user',
'COLUMN', N'login_ip'
GO

EXEC sp_addextendedproperty
'MS_Description', N'最后登录时间',
'SCHEMA', N'dbo',
'TABLE', N'jb_user',
'COLUMN', N'login_time'
GO

EXEC sp_addextendedproperty
'MS_Description', N'最后登录城市',
'SCHEMA', N'dbo',
'TABLE', N'jb_user',
'COLUMN', N'login_city'
GO

EXEC sp_addextendedproperty
'MS_Description', N'最后登录省份',
'SCHEMA', N'dbo',
'TABLE', N'jb_user',
'COLUMN', N'login_province'
GO

EXEC sp_addextendedproperty
'MS_Description', N'最后登录国家',
'SCHEMA', N'dbo',
'TABLE', N'jb_user',
'COLUMN', N'login_country'
GO

EXEC sp_addextendedproperty
'MS_Description', N'是否异地登录',
'SCHEMA', N'dbo',
'TABLE', N'jb_user',
'COLUMN', N'is_remote_login'
GO

EXEC sp_addextendedproperty
'MS_Description', N'部门ID',
'SCHEMA', N'dbo',
'TABLE', N'jb_user',
'COLUMN', N'dept_id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'岗位IDS',
'SCHEMA', N'dbo',
'TABLE', N'jb_user',
'COLUMN', N'posts'
GO

EXEC sp_addextendedproperty
'MS_Description', N'用户登录账户表',
'SCHEMA', N'dbo',
'TABLE', N'jb_user'
GO


-- ----------------------------
-- Table structure for jb_user_config
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[jb_user_config]') AND type IN ('U'))
	DROP TABLE [dbo].[jb_user_config]
GO

CREATE TABLE [dbo].[jb_user_config] (
  [id] bigint  NOT NULL,
  [name] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [config_key] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [config_value] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [user_id] bigint  NOT NULL,
  [create_time] datetime  NOT NULL,
  [update_time] datetime  NULL,
  [value_type] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NOT NULL
)
GO

ALTER TABLE [dbo].[jb_user_config] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'主键ID',
'SCHEMA', N'dbo',
'TABLE', N'jb_user_config',
'COLUMN', N'id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'配置名',
'SCHEMA', N'dbo',
'TABLE', N'jb_user_config',
'COLUMN', N'name'
GO

EXEC sp_addextendedproperty
'MS_Description', N'配置KEY',
'SCHEMA', N'dbo',
'TABLE', N'jb_user_config',
'COLUMN', N'config_key'
GO

EXEC sp_addextendedproperty
'MS_Description', N'配置值',
'SCHEMA', N'dbo',
'TABLE', N'jb_user_config',
'COLUMN', N'config_value'
GO

EXEC sp_addextendedproperty
'MS_Description', N'用户ID',
'SCHEMA', N'dbo',
'TABLE', N'jb_user_config',
'COLUMN', N'user_id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'创建时间',
'SCHEMA', N'dbo',
'TABLE', N'jb_user_config',
'COLUMN', N'create_time'
GO

EXEC sp_addextendedproperty
'MS_Description', N'更新时间',
'SCHEMA', N'dbo',
'TABLE', N'jb_user_config',
'COLUMN', N'update_time'
GO

EXEC sp_addextendedproperty
'MS_Description', N'取值类型',
'SCHEMA', N'dbo',
'TABLE', N'jb_user_config',
'COLUMN', N'value_type'
GO

EXEC sp_addextendedproperty
'MS_Description', N'用户系统样式自定义设置表',
'SCHEMA', N'dbo',
'TABLE', N'jb_user_config'
GO


-- ----------------------------
-- Table structure for jb_wechat_article
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[jb_wechat_article]') AND type IN ('U'))
	DROP TABLE [dbo].[jb_wechat_article]
GO

CREATE TABLE [dbo].[jb_wechat_article] (
  [id] bigint  NOT NULL,
  [title] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [content] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [create_time] datetime  NOT NULL,
  [user_id] bigint  NOT NULL,
  [update_time] datetime  NULL,
  [update_user_id] bigint  NULL,
  [brief_info] nvarchar(120) COLLATE Chinese_PRC_CI_AS  NULL,
  [poster] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [view_count] int  NULL,
  [media_id] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [mp_id] bigint  NOT NULL,
  [url] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [type] int  NOT NULL
)
GO

ALTER TABLE [dbo].[jb_wechat_article] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'主键ID',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_article',
'COLUMN', N'id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'标题',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_article',
'COLUMN', N'title'
GO

EXEC sp_addextendedproperty
'MS_Description', N'内容',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_article',
'COLUMN', N'content'
GO

EXEC sp_addextendedproperty
'MS_Description', N'创建时间',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_article',
'COLUMN', N'create_time'
GO

EXEC sp_addextendedproperty
'MS_Description', N'用户 ID',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_article',
'COLUMN', N'user_id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'更新时间',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_article',
'COLUMN', N'update_time'
GO

EXEC sp_addextendedproperty
'MS_Description', N'更新用户 ID',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_article',
'COLUMN', N'update_user_id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'文章摘要',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_article',
'COLUMN', N'brief_info'
GO

EXEC sp_addextendedproperty
'MS_Description', N'海报',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_article',
'COLUMN', N'poster'
GO

EXEC sp_addextendedproperty
'MS_Description', N'阅读数',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_article',
'COLUMN', N'view_count'
GO

EXEC sp_addextendedproperty
'MS_Description', N'微信素材 ID',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_article',
'COLUMN', N'media_id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'微信 ID',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_article',
'COLUMN', N'mp_id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'图文链接地址',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_article',
'COLUMN', N'url'
GO

EXEC sp_addextendedproperty
'MS_Description', N'本地图文 公众号图文素材 外部图文',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_article',
'COLUMN', N'type'
GO

EXEC sp_addextendedproperty
'MS_Description', N'微信图文信息',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_article'
GO


-- ----------------------------
-- Table structure for jb_wechat_autoreply
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[jb_wechat_autoreply]') AND type IN ('U'))
	DROP TABLE [dbo].[jb_wechat_autoreply]
GO

CREATE TABLE [dbo].[jb_wechat_autoreply] (
  [id] bigint  NOT NULL,
  [mp_id] bigint  NOT NULL,
  [type] int  NOT NULL,
  [name] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [reply_type] int  NOT NULL,
  [create_time] datetime  NOT NULL,
  [user_id] bigint  NOT NULL,
  [enable] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL
)
GO

ALTER TABLE [dbo].[jb_wechat_autoreply] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'主键ID',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_autoreply',
'COLUMN', N'id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'微信 ID',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_autoreply',
'COLUMN', N'mp_id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'类型 关注回复 无内容时回复 关键词回复',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_autoreply',
'COLUMN', N'type'
GO

EXEC sp_addextendedproperty
'MS_Description', N'规则名称',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_autoreply',
'COLUMN', N'name'
GO

EXEC sp_addextendedproperty
'MS_Description', N'回复类型 全部还是随机一条',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_autoreply',
'COLUMN', N'reply_type'
GO

EXEC sp_addextendedproperty
'MS_Description', N'记录创建时间',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_autoreply',
'COLUMN', N'create_time'
GO

EXEC sp_addextendedproperty
'MS_Description', N'用户 ID',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_autoreply',
'COLUMN', N'user_id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'启用',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_autoreply',
'COLUMN', N'enable'
GO

EXEC sp_addextendedproperty
'MS_Description', N'微信公众号自动回复规则',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_autoreply'
GO


-- ----------------------------
-- Table structure for jb_wechat_config
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[jb_wechat_config]') AND type IN ('U'))
	DROP TABLE [dbo].[jb_wechat_config]
GO

CREATE TABLE [dbo].[jb_wechat_config] (
  [id] bigint  NOT NULL,
  [mp_id] bigint  NOT NULL,
  [config_key] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [config_value] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [name] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [type] int  NOT NULL
)
GO

ALTER TABLE [dbo].[jb_wechat_config] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'主键ID',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_config',
'COLUMN', N'id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'微信 ID',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_config',
'COLUMN', N'mp_id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'配置key',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_config',
'COLUMN', N'config_key'
GO

EXEC sp_addextendedproperty
'MS_Description', N'配置值',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_config',
'COLUMN', N'config_value'
GO

EXEC sp_addextendedproperty
'MS_Description', N'配置项名称',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_config',
'COLUMN', N'name'
GO

EXEC sp_addextendedproperty
'MS_Description', N'配置类型',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_config',
'COLUMN', N'type'
GO

EXEC sp_addextendedproperty
'MS_Description', N'微信公众号配置表',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_config'
GO


-- ----------------------------
-- Table structure for jb_wechat_keywords
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[jb_wechat_keywords]') AND type IN ('U'))
	DROP TABLE [dbo].[jb_wechat_keywords]
GO

CREATE TABLE [dbo].[jb_wechat_keywords] (
  [id] bigint  NOT NULL,
  [mp_id] bigint  NOT NULL,
  [name] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [type] int  NOT NULL,
  [auto_reply_id] bigint  NOT NULL
)
GO

ALTER TABLE [dbo].[jb_wechat_keywords] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'主键ID',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_keywords',
'COLUMN', N'id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'微信 ID',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_keywords',
'COLUMN', N'mp_id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'名称',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_keywords',
'COLUMN', N'name'
GO

EXEC sp_addextendedproperty
'MS_Description', N'模糊 全匹配',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_keywords',
'COLUMN', N'type'
GO

EXEC sp_addextendedproperty
'MS_Description', N'回复规则ID',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_keywords',
'COLUMN', N'auto_reply_id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'微信关键词回复中的关键词定义',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_keywords'
GO


-- ----------------------------
-- Table structure for jb_wechat_media
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[jb_wechat_media]') AND type IN ('U'))
	DROP TABLE [dbo].[jb_wechat_media]
GO

CREATE TABLE [dbo].[jb_wechat_media] (
  [id] bigint  NOT NULL,
  [title] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [digest] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [type] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [mp_id] bigint  NOT NULL,
  [media_id] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [thumb_media_id] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [content_source_url] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [url] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [author] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [update_time] datetime  NULL,
  [server_url] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[jb_wechat_media] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'主键ID',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_media',
'COLUMN', N'id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'标题',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_media',
'COLUMN', N'title'
GO

EXEC sp_addextendedproperty
'MS_Description', N'描述',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_media',
'COLUMN', N'digest'
GO

EXEC sp_addextendedproperty
'MS_Description', N'类型 image video voice news',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_media',
'COLUMN', N'type'
GO

EXEC sp_addextendedproperty
'MS_Description', N'微信 ID',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_media',
'COLUMN', N'mp_id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'微信素材ID',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_media',
'COLUMN', N'media_id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'封面图ID',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_media',
'COLUMN', N'thumb_media_id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'原文地址',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_media',
'COLUMN', N'content_source_url'
GO

EXEC sp_addextendedproperty
'MS_Description', N'访问地址',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_media',
'COLUMN', N'url'
GO

EXEC sp_addextendedproperty
'MS_Description', N'图文作者',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_media',
'COLUMN', N'author'
GO

EXEC sp_addextendedproperty
'MS_Description', N'更新时间',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_media',
'COLUMN', N'update_time'
GO

EXEC sp_addextendedproperty
'MS_Description', N'存服务器URL',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_media',
'COLUMN', N'server_url'
GO

EXEC sp_addextendedproperty
'MS_Description', N'微信公众平台素材库同步管理',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_media'
GO


-- ----------------------------
-- Table structure for jb_wechat_menu
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[jb_wechat_menu]') AND type IN ('U'))
	DROP TABLE [dbo].[jb_wechat_menu]
GO

CREATE TABLE [dbo].[jb_wechat_menu] (
  [id] bigint  NOT NULL,
  [mp_id] bigint  NOT NULL,
  [type] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [name] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [pid] bigint  NULL,
  [sort_rank] int  NOT NULL,
  [value] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [app_id] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [page_path] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[jb_wechat_menu] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'主键ID',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_menu',
'COLUMN', N'id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'微信 ID',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_menu',
'COLUMN', N'mp_id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'类型',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_menu',
'COLUMN', N'type'
GO

EXEC sp_addextendedproperty
'MS_Description', N'名称',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_menu',
'COLUMN', N'name'
GO

EXEC sp_addextendedproperty
'MS_Description', N'上级ID',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_menu',
'COLUMN', N'pid'
GO

EXEC sp_addextendedproperty
'MS_Description', N'排序',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_menu',
'COLUMN', N'sort_rank'
GO

EXEC sp_addextendedproperty
'MS_Description', N'值',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_menu',
'COLUMN', N'value'
GO

EXEC sp_addextendedproperty
'MS_Description', N'微信小程序APPID',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_menu',
'COLUMN', N'app_id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'微信小程序页面地址',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_menu',
'COLUMN', N'page_path'
GO

EXEC sp_addextendedproperty
'MS_Description', N'微信菜单',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_menu'
GO


-- ----------------------------
-- Table structure for jb_wechat_mpinfo
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[jb_wechat_mpinfo]') AND type IN ('U'))
	DROP TABLE [dbo].[jb_wechat_mpinfo]
GO

CREATE TABLE [dbo].[jb_wechat_mpinfo] (
  [id] bigint  NOT NULL,
  [name] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [logo] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [create_time] datetime  NOT NULL,
  [user_id] bigint  NOT NULL,
  [enable] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
  [update_time] datetime  NOT NULL,
  [update_user_id] bigint  NULL,
  [brief_info] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [remark] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [type] int  NOT NULL,
  [is_authenticated] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
  [subject_type] int  NOT NULL,
  [wechat_id] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [qrcode] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [link_app_id] bigint  NULL
)
GO

ALTER TABLE [dbo].[jb_wechat_mpinfo] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'主键ID',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_mpinfo',
'COLUMN', N'id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'平台名称',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_mpinfo',
'COLUMN', N'name'
GO

EXEC sp_addextendedproperty
'MS_Description', N'头像LOGO',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_mpinfo',
'COLUMN', N'logo'
GO

EXEC sp_addextendedproperty
'MS_Description', N'创建时间',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_mpinfo',
'COLUMN', N'create_time'
GO

EXEC sp_addextendedproperty
'MS_Description', N'用户ID',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_mpinfo',
'COLUMN', N'user_id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'是否启用',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_mpinfo',
'COLUMN', N'enable'
GO

EXEC sp_addextendedproperty
'MS_Description', N'创建时间',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_mpinfo',
'COLUMN', N'update_time'
GO

EXEC sp_addextendedproperty
'MS_Description', N'更新人ID',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_mpinfo',
'COLUMN', N'update_user_id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'简介',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_mpinfo',
'COLUMN', N'brief_info'
GO

EXEC sp_addextendedproperty
'MS_Description', N'备注',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_mpinfo',
'COLUMN', N'remark'
GO

EXEC sp_addextendedproperty
'MS_Description', N'类型 订阅号、服务号、小程序、企业号',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_mpinfo',
'COLUMN', N'type'
GO

EXEC sp_addextendedproperty
'MS_Description', N'是否已认证',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_mpinfo',
'COLUMN', N'is_authenticated'
GO

EXEC sp_addextendedproperty
'MS_Description', N'申请认证主体的类型 个人还是企业',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_mpinfo',
'COLUMN', N'subject_type'
GO

EXEC sp_addextendedproperty
'MS_Description', N'微信号',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_mpinfo',
'COLUMN', N'wechat_id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'二维码',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_mpinfo',
'COLUMN', N'qrcode'
GO

EXEC sp_addextendedproperty
'MS_Description', N'关联应用ID',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_mpinfo',
'COLUMN', N'link_app_id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'微信公众号与小程序',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_mpinfo'
GO


-- ----------------------------
-- Table structure for jb_wechat_reply_content
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[jb_wechat_reply_content]') AND type IN ('U'))
	DROP TABLE [dbo].[jb_wechat_reply_content]
GO

CREATE TABLE [dbo].[jb_wechat_reply_content] (
  [id] bigint  NOT NULL,
  [type] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [title] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [content] nvarchar(max) COLLATE Chinese_PRC_CI_AS  NULL,
  [poster] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [url] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [auto_reply_id] bigint  NOT NULL,
  [create_time] datetime  NOT NULL,
  [user_id] bigint  NOT NULL,
  [media_id] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [mp_id] bigint  NOT NULL,
  [sort_rank] int  NOT NULL,
  [enable] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL
)
GO

ALTER TABLE [dbo].[jb_wechat_reply_content] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'主键ID',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_reply_content',
'COLUMN', N'id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'类型 图文 文字 图片 音频 视频',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_reply_content',
'COLUMN', N'type'
GO

EXEC sp_addextendedproperty
'MS_Description', N'标题',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_reply_content',
'COLUMN', N'title'
GO

EXEC sp_addextendedproperty
'MS_Description', N'内容',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_reply_content',
'COLUMN', N'content'
GO

EXEC sp_addextendedproperty
'MS_Description', N'封面',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_reply_content',
'COLUMN', N'poster'
GO

EXEC sp_addextendedproperty
'MS_Description', N'地址',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_reply_content',
'COLUMN', N'url'
GO

EXEC sp_addextendedproperty
'MS_Description', N'回复规则ID',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_reply_content',
'COLUMN', N'auto_reply_id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'创建时间',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_reply_content',
'COLUMN', N'create_time'
GO

EXEC sp_addextendedproperty
'MS_Description', N'用户 ID',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_reply_content',
'COLUMN', N'user_id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'关联数据',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_reply_content',
'COLUMN', N'media_id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'微信 ID',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_reply_content',
'COLUMN', N'mp_id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'排序',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_reply_content',
'COLUMN', N'sort_rank'
GO

EXEC sp_addextendedproperty
'MS_Description', N'是否启用',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_reply_content',
'COLUMN', N'enable'
GO

EXEC sp_addextendedproperty
'MS_Description', N'自动回复内容',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_reply_content'
GO


-- ----------------------------
-- Table structure for jb_wechat_user
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[jb_wechat_user]') AND type IN ('U'))
	DROP TABLE [dbo].[jb_wechat_user]
GO

CREATE TABLE [dbo].[jb_wechat_user] (
  [id] bigint  NOT NULL,
  [nickname] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [open_id] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [union_id] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [sex] int  NULL,
  [language] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [subscibe] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
  [country] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [province] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [city] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [head_img_url] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [subscribe_time] datetime  NULL,
  [remark] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [group_id] int  NULL,
  [tag_ids] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [subscribe_scene] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [qr_scene] int  NULL,
  [qr_scene_str] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [realname] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [phone] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [phone_country_code] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NULL,
  [check_code] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [is_checked] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
  [source] int  NULL,
  [session_key] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [enable] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
  [create_time] datetime  NOT NULL,
  [checked_time] datetime  NULL,
  [mp_id] bigint  NOT NULL,
  [weixin] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [update_time] datetime  NULL,
  [last_login_time] datetime  NULL,
  [first_auth_time] datetime  NULL,
  [last_auth_time] datetime  NULL,
  [first_login_time] datetime  NULL,
  [bind_user] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[jb_wechat_user] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'主键ID',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_user',
'COLUMN', N'id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'用户昵称',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_user',
'COLUMN', N'nickname'
GO

EXEC sp_addextendedproperty
'MS_Description', N'openId',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_user',
'COLUMN', N'open_id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'unionID',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_user',
'COLUMN', N'union_id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'性别 1男 2女 0 未知',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_user',
'COLUMN', N'sex'
GO

EXEC sp_addextendedproperty
'MS_Description', N'语言',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_user',
'COLUMN', N'language'
GO

EXEC sp_addextendedproperty
'MS_Description', N'是否已关注',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_user',
'COLUMN', N'subscibe'
GO

EXEC sp_addextendedproperty
'MS_Description', N'国家',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_user',
'COLUMN', N'country'
GO

EXEC sp_addextendedproperty
'MS_Description', N'省',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_user',
'COLUMN', N'province'
GO

EXEC sp_addextendedproperty
'MS_Description', N'城市',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_user',
'COLUMN', N'city'
GO

EXEC sp_addextendedproperty
'MS_Description', N'头像',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_user',
'COLUMN', N'head_img_url'
GO

EXEC sp_addextendedproperty
'MS_Description', N'关注时间',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_user',
'COLUMN', N'subscribe_time'
GO

EXEC sp_addextendedproperty
'MS_Description', N'备注',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_user',
'COLUMN', N'remark'
GO

EXEC sp_addextendedproperty
'MS_Description', N'分组',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_user',
'COLUMN', N'group_id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'标签',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_user',
'COLUMN', N'tag_ids'
GO

EXEC sp_addextendedproperty
'MS_Description', N'关注渠道',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_user',
'COLUMN', N'subscribe_scene'
GO

EXEC sp_addextendedproperty
'MS_Description', N'二维码场景-开发者自定义',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_user',
'COLUMN', N'qr_scene'
GO

EXEC sp_addextendedproperty
'MS_Description', N'二维码扫码场景描述-开发者自定义',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_user',
'COLUMN', N'qr_scene_str'
GO

EXEC sp_addextendedproperty
'MS_Description', N'真实姓名',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_user',
'COLUMN', N'realname'
GO

EXEC sp_addextendedproperty
'MS_Description', N'手机号',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_user',
'COLUMN', N'phone'
GO

EXEC sp_addextendedproperty
'MS_Description', N'手机号国家代码',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_user',
'COLUMN', N'phone_country_code'
GO

EXEC sp_addextendedproperty
'MS_Description', N'手机验证码',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_user',
'COLUMN', N'check_code'
GO

EXEC sp_addextendedproperty
'MS_Description', N'是否已验证',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_user',
'COLUMN', N'is_checked'
GO

EXEC sp_addextendedproperty
'MS_Description', N'来源 小程序还是公众平台',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_user',
'COLUMN', N'source'
GO

EXEC sp_addextendedproperty
'MS_Description', N'小程序登录SessionKey',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_user',
'COLUMN', N'session_key'
GO

EXEC sp_addextendedproperty
'MS_Description', N'禁用访问',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_user',
'COLUMN', N'enable'
GO

EXEC sp_addextendedproperty
'MS_Description', N'创建时间',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_user',
'COLUMN', N'create_time'
GO

EXEC sp_addextendedproperty
'MS_Description', N'验证绑定手机号时间',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_user',
'COLUMN', N'checked_time'
GO

EXEC sp_addextendedproperty
'MS_Description', N'所属公众平台ID',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_user',
'COLUMN', N'mp_id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'微信号',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_user',
'COLUMN', N'weixin'
GO

EXEC sp_addextendedproperty
'MS_Description', N'更新时间',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_user',
'COLUMN', N'update_time'
GO

EXEC sp_addextendedproperty
'MS_Description', N'最后登录时间',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_user',
'COLUMN', N'last_login_time'
GO

EXEC sp_addextendedproperty
'MS_Description', N'首次授权时间',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_user',
'COLUMN', N'first_auth_time'
GO

EXEC sp_addextendedproperty
'MS_Description', N'最后一次更新授权时间',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_user',
'COLUMN', N'last_auth_time'
GO

EXEC sp_addextendedproperty
'MS_Description', N'首次登录时间',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_user',
'COLUMN', N'first_login_time'
GO

EXEC sp_addextendedproperty
'MS_Description', N'绑定其他用户信息',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_user',
'COLUMN', N'bind_user'
GO

EXEC sp_addextendedproperty
'MS_Description', N'微信用户信息_模板表',
'SCHEMA', N'dbo',
'TABLE', N'jb_wechat_user'
GO


-- ----------------------------
-- Primary Key structure for table jb_application
-- ----------------------------
ALTER TABLE [dbo].[jb_application] ADD CONSTRAINT [PK__jb_appli__3213E83F14881682] PRIMARY KEY CLUSTERED ([id])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table jb_dept
-- ----------------------------
ALTER TABLE [dbo].[jb_dept] ADD CONSTRAINT [PK__jb_dept__3213E83FA3FAA3D6] PRIMARY KEY CLUSTERED ([id])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table jb_dictionary
-- ----------------------------
ALTER TABLE [dbo].[jb_dictionary] ADD CONSTRAINT [PK__jb_dicti__3213E83FDE11399E] PRIMARY KEY CLUSTERED ([id])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table jb_dictionary_type
-- ----------------------------
ALTER TABLE [dbo].[jb_dictionary_type] ADD CONSTRAINT [PK__jb_dicti__3213E83FA5BE844D] PRIMARY KEY CLUSTERED ([id])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


 

-- ----------------------------
-- Primary Key structure for table jb_global_config
-- ----------------------------
ALTER TABLE [dbo].[jb_global_config] ADD CONSTRAINT [PK__jb_globa__3213E83F48E1D29F] PRIMARY KEY CLUSTERED ([id])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table jb_global_config_type
-- ----------------------------
ALTER TABLE [dbo].[jb_global_config_type] ADD CONSTRAINT [PK__jb_globa__3213E83F000BC670] PRIMARY KEY CLUSTERED ([id])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table jb_jbolt_file
-- ----------------------------
ALTER TABLE [dbo].[jb_jbolt_file] ADD CONSTRAINT [PK__jb_jbolt__3213E83FF2332518] PRIMARY KEY CLUSTERED ([id])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO

-- ----------------------------
-- Primary Key structure for table jb_login_log
-- ----------------------------
ALTER TABLE [dbo].[jb_login_log] ADD CONSTRAINT [PK__jb_login__3213E83F5E2B4C75] PRIMARY KEY CLUSTERED ([id])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table jb_online_user
-- ----------------------------
ALTER TABLE [dbo].[jb_online_user] ADD CONSTRAINT [PK__jb_onlin__3213E83FDCD5AADB] PRIMARY KEY CLUSTERED ([id])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table jb_permission
-- ----------------------------
ALTER TABLE [dbo].[jb_permission] ADD CONSTRAINT [PK__jb_permi__3213E83F8908B18B] PRIMARY KEY CLUSTERED ([id])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table jb_post
-- ----------------------------
ALTER TABLE [dbo].[jb_post] ADD CONSTRAINT [PK__jb_post__3213E83FA2906C1E] PRIMARY KEY CLUSTERED ([id])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table jb_private_message
-- ----------------------------
ALTER TABLE [dbo].[jb_private_message] ADD CONSTRAINT [PK__jb_priva__3213E83F289819A2] PRIMARY KEY CLUSTERED ([id])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table jb_remote_login_log
-- ----------------------------
ALTER TABLE [dbo].[jb_remote_login_log] ADD CONSTRAINT [PK__jb_remot__3213E83F9F373FBD] PRIMARY KEY CLUSTERED ([id])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table jb_role
-- ----------------------------
ALTER TABLE [dbo].[jb_role] ADD CONSTRAINT [PK__jb_role__3213E83FC1D5064C] PRIMARY KEY CLUSTERED ([id])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table jb_role_permission
-- ----------------------------
ALTER TABLE [dbo].[jb_role_permission] ADD CONSTRAINT [PK__jb_role___3213E83F141ABCD5] PRIMARY KEY CLUSTERED ([id])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO

-- ----------------------------
-- Primary Key structure for table jb_sys_notice
-- ----------------------------
ALTER TABLE [dbo].[jb_sys_notice] ADD CONSTRAINT [PK__jb_sys_n__3213E83F9F57293A] PRIMARY KEY CLUSTERED ([id])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table jb_sys_notice_reader
-- ----------------------------
ALTER TABLE [dbo].[jb_sys_notice_reader] ADD CONSTRAINT [PK__jb_sys_n__3213E83F4585F950] PRIMARY KEY CLUSTERED ([id])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table jb_system_log
-- ----------------------------
ALTER TABLE [dbo].[jb_system_log] ADD CONSTRAINT [PK__jb_syste__3213E83F60A38B0F] PRIMARY KEY CLUSTERED ([id])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table jb_todo
-- ----------------------------
ALTER TABLE [dbo].[jb_todo] ADD CONSTRAINT [PK__jb_todo__3213E83F84E87232] PRIMARY KEY CLUSTERED ([id])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table jb_topnav
-- ----------------------------
ALTER TABLE [dbo].[jb_topnav] ADD CONSTRAINT [PK__jb_topna__3213E83FBF49EB09] PRIMARY KEY CLUSTERED ([id])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table jb_topnav_menu
-- ----------------------------
ALTER TABLE [dbo].[jb_topnav_menu] ADD CONSTRAINT [PK__jb_topna__3213E83F33ACE6D5] PRIMARY KEY CLUSTERED ([id])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO

-- ----------------------------
-- Primary Key structure for table jb_user
-- ----------------------------
ALTER TABLE [dbo].[jb_user] ADD CONSTRAINT [PK__jb_user__3213E83F6B6735D6] PRIMARY KEY CLUSTERED ([id])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table jb_user_config
-- ----------------------------
ALTER TABLE [dbo].[jb_user_config] ADD CONSTRAINT [PK__jb_user___3213E83F64EFD751] PRIMARY KEY CLUSTERED ([id])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table jb_wechat_article
-- ----------------------------
ALTER TABLE [dbo].[jb_wechat_article] ADD CONSTRAINT [PK__jb_wecha__3213E83F5FE58132] PRIMARY KEY CLUSTERED ([id])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table jb_wechat_autoreply
-- ----------------------------
ALTER TABLE [dbo].[jb_wechat_autoreply] ADD CONSTRAINT [PK__jb_wecha__3213E83F4579BBF5] PRIMARY KEY CLUSTERED ([id])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table jb_wechat_config
-- ----------------------------
ALTER TABLE [dbo].[jb_wechat_config] ADD CONSTRAINT [PK__jb_wecha__3213E83F5D6C0A79] PRIMARY KEY CLUSTERED ([id])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table jb_wechat_keywords
-- ----------------------------
ALTER TABLE [dbo].[jb_wechat_keywords] ADD CONSTRAINT [PK__jb_wecha__3213E83F4D075F8B] PRIMARY KEY CLUSTERED ([id])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table jb_wechat_media
-- ----------------------------
ALTER TABLE [dbo].[jb_wechat_media] ADD CONSTRAINT [PK__jb_wecha__3213E83FFB4A2393] PRIMARY KEY CLUSTERED ([id])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table jb_wechat_menu
-- ----------------------------
ALTER TABLE [dbo].[jb_wechat_menu] ADD CONSTRAINT [PK__jb_wecha__3213E83F7BC14F1A] PRIMARY KEY CLUSTERED ([id])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table jb_wechat_mpinfo
-- ----------------------------
ALTER TABLE [dbo].[jb_wechat_mpinfo] ADD CONSTRAINT [PK__jb_wecha__3213E83F23ED57D2] PRIMARY KEY CLUSTERED ([id])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table jb_wechat_reply_content
-- ----------------------------
ALTER TABLE [dbo].[jb_wechat_reply_content] ADD CONSTRAINT [PK__jb_wecha__3213E83F2A52CE76] PRIMARY KEY CLUSTERED ([id])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table jb_wechat_user
-- ----------------------------
ALTER TABLE [dbo].[jb_wechat_user] ADD CONSTRAINT [PK__jb_wecha__3213E83F77595977] PRIMARY KEY CLUSTERED ([id])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO



-- ----------------------------
-- Table structure for jb_hiprint_tpl
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[jb_hiprint_tpl]') AND type IN ('U'))
	DROP TABLE [dbo].[jb_hiprint_tpl]
GO

CREATE TABLE [dbo].[jb_hiprint_tpl] (
  [id] bigint  NOT NULL,
  [name] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [sn] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [content] nvarchar(max) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [remark] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [create_time] datetime  NOT NULL,
  [update_time] datetime  NOT NULL,
  [create_user_id] bigint  NOT NULL,
  [update_user_id] bigint  NOT NULL,
  [test_api_url] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [test_json_data] nvarchar(max) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[jb_hiprint_tpl] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'主键ID',
'SCHEMA', N'dbo',
'TABLE', N'jb_hiprint_tpl',
'COLUMN', N'id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'模板名称',
'SCHEMA', N'dbo',
'TABLE', N'jb_hiprint_tpl',
'COLUMN', N'name'
GO

EXEC sp_addextendedproperty
'MS_Description', N'模板编码',
'SCHEMA', N'dbo',
'TABLE', N'jb_hiprint_tpl',
'COLUMN', N'sn'
GO

EXEC sp_addextendedproperty
'MS_Description', N'模板内容',
'SCHEMA', N'dbo',
'TABLE', N'jb_hiprint_tpl',
'COLUMN', N'content'
GO

EXEC sp_addextendedproperty
'MS_Description', N'备注',
'SCHEMA', N'dbo',
'TABLE', N'jb_hiprint_tpl',
'COLUMN', N'remark'
GO

EXEC sp_addextendedproperty
'MS_Description', N'创建时间',
'SCHEMA', N'dbo',
'TABLE', N'jb_hiprint_tpl',
'COLUMN', N'create_time'
GO

EXEC sp_addextendedproperty
'MS_Description', N'更新时间',
'SCHEMA', N'dbo',
'TABLE', N'jb_hiprint_tpl',
'COLUMN', N'update_time'
GO

EXEC sp_addextendedproperty
'MS_Description', N'创建人',
'SCHEMA', N'dbo',
'TABLE', N'jb_hiprint_tpl',
'COLUMN', N'create_user_id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'更新人',
'SCHEMA', N'dbo',
'TABLE', N'jb_hiprint_tpl',
'COLUMN', N'update_user_id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'测试API地址',
'SCHEMA', N'dbo',
'TABLE', N'jb_hiprint_tpl',
'COLUMN', N'test_api_url'
GO

EXEC sp_addextendedproperty
'MS_Description', N'测试JSON数据',
'SCHEMA', N'dbo',
'TABLE', N'jb_hiprint_tpl',
'COLUMN', N'test_json_data'
GO


-- ----------------------------
-- Primary Key structure for table jb_hiprint_tpl
-- ----------------------------
ALTER TABLE [dbo].[jb_hiprint_tpl] ADD CONSTRAINT [PK__jb_hipri__3213E83F291300F3] PRIMARY KEY CLUSTERED ([id])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO

-- ----------------------------
-- Table structure for jb_qiniu
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[jb_qiniu]') AND type IN ('U'))
	DROP TABLE [dbo].[jb_qiniu]
GO

CREATE TABLE [dbo].[jb_qiniu] (
  [id] bigint NOT NULL,
  [name] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [sn] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [ak] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [sk] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [type] int  NOT NULL,
  [bucket_count] int  NOT NULL,
  [is_default] char(1) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [enable] char(1) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [remark] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [create_time] datetime  NOT NULL,
  [update_time] datetime  NOT NULL,
  [create_user_id] bigint  NOT NULL,
  [update_user_id] bigint  NOT NULL
)
GO

ALTER TABLE [dbo].[jb_qiniu] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'主键ID',
'SCHEMA', N'dbo',
'TABLE', N'jb_qiniu',
'COLUMN', N'id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'账号',
'SCHEMA', N'dbo',
'TABLE', N'jb_qiniu',
'COLUMN', N'name'
GO

EXEC sp_addextendedproperty
'MS_Description', N'编号SN',
'SCHEMA', N'dbo',
'TABLE', N'jb_qiniu',
'COLUMN', N'sn'
GO

EXEC sp_addextendedproperty
'MS_Description', N'AK',
'SCHEMA', N'dbo',
'TABLE', N'jb_qiniu',
'COLUMN', N'ak'
GO

EXEC sp_addextendedproperty
'MS_Description', N'SK',
'SCHEMA', N'dbo',
'TABLE', N'jb_qiniu',
'COLUMN', N'sk'
GO

EXEC sp_addextendedproperty
'MS_Description', N'账号类型',
'SCHEMA', N'dbo',
'TABLE', N'jb_qiniu',
'COLUMN', N'type'
GO

EXEC sp_addextendedproperty
'MS_Description', N'空间个数',
'SCHEMA', N'dbo',
'TABLE', N'jb_qiniu',
'COLUMN', N'bucket_count'
GO

EXEC sp_addextendedproperty
'MS_Description', N'是否默认',
'SCHEMA', N'dbo',
'TABLE', N'jb_qiniu',
'COLUMN', N'is_default'
GO

EXEC sp_addextendedproperty
'MS_Description', N'是否启用',
'SCHEMA', N'dbo',
'TABLE', N'jb_qiniu',
'COLUMN', N'enable'
GO

EXEC sp_addextendedproperty
'MS_Description', N'备注',
'SCHEMA', N'dbo',
'TABLE', N'jb_qiniu',
'COLUMN', N'remark'
GO

EXEC sp_addextendedproperty
'MS_Description', N'创建时间',
'SCHEMA', N'dbo',
'TABLE', N'jb_qiniu',
'COLUMN', N'create_time'
GO

EXEC sp_addextendedproperty
'MS_Description', N'更新时间',
'SCHEMA', N'dbo',
'TABLE', N'jb_qiniu',
'COLUMN', N'update_time'
GO

EXEC sp_addextendedproperty
'MS_Description', N'创建人',
'SCHEMA', N'dbo',
'TABLE', N'jb_qiniu',
'COLUMN', N'create_user_id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'更新人',
'SCHEMA', N'dbo',
'TABLE', N'jb_qiniu',
'COLUMN', N'update_user_id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'七牛账号表',
'SCHEMA', N'dbo',
'TABLE', N'jb_qiniu'
GO


-- ----------------------------
-- Table structure for jb_qiniu_bucket
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[jb_qiniu_bucket]') AND type IN ('U'))
	DROP TABLE [dbo].[jb_qiniu_bucket]
GO

CREATE TABLE [dbo].[jb_qiniu_bucket] (
  [id] bigint NOT NULL,
  [name] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [sn] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [qiniu_id] bigint  NOT NULL,
  [domain_url] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [callback_url] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [callback_body] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [callback_body_type] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [expires] int  NULL,
  [is_default] char(1) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [enable] char(1) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [remark] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [create_user_id] bigint  NOT NULL,
  [create_time] datetime  NOT NULL,
  [update_user_id] bigint  NOT NULL,
  [update_time] datetime  NULL,
  [region] nvarchar(20) COLLATE Chinese_PRC_CI_AS  NOT NULL
)
GO

ALTER TABLE [dbo].[jb_qiniu_bucket] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'主键ID',
'SCHEMA', N'dbo',
'TABLE', N'jb_qiniu_bucket',
'COLUMN', N'id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'bucket名称',
'SCHEMA', N'dbo',
'TABLE', N'jb_qiniu_bucket',
'COLUMN', N'name'
GO

EXEC sp_addextendedproperty
'MS_Description', N'编码',
'SCHEMA', N'dbo',
'TABLE', N'jb_qiniu_bucket',
'COLUMN', N'sn'
GO

EXEC sp_addextendedproperty
'MS_Description', N'所属七牛账号',
'SCHEMA', N'dbo',
'TABLE', N'jb_qiniu_bucket',
'COLUMN', N'qiniu_id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'绑定域名',
'SCHEMA', N'dbo',
'TABLE', N'jb_qiniu_bucket',
'COLUMN', N'domain_url'
GO

EXEC sp_addextendedproperty
'MS_Description', N'回调地址',
'SCHEMA', N'dbo',
'TABLE', N'jb_qiniu_bucket',
'COLUMN', N'callback_url'
GO

EXEC sp_addextendedproperty
'MS_Description', N'回调body定义',
'SCHEMA', N'dbo',
'TABLE', N'jb_qiniu_bucket',
'COLUMN', N'callback_body'
GO

EXEC sp_addextendedproperty
'MS_Description', N'回调Body类型',
'SCHEMA', N'dbo',
'TABLE', N'jb_qiniu_bucket',
'COLUMN', N'callback_body_type'
GO

EXEC sp_addextendedproperty
'MS_Description', N'有效期(秒)',
'SCHEMA', N'dbo',
'TABLE', N'jb_qiniu_bucket',
'COLUMN', N'expires'
GO

EXEC sp_addextendedproperty
'MS_Description', N'是否默认',
'SCHEMA', N'dbo',
'TABLE', N'jb_qiniu_bucket',
'COLUMN', N'is_default'
GO

EXEC sp_addextendedproperty
'MS_Description', N'是否启用',
'SCHEMA', N'dbo',
'TABLE', N'jb_qiniu_bucket',
'COLUMN', N'enable'
GO

EXEC sp_addextendedproperty
'MS_Description', N'备注',
'SCHEMA', N'dbo',
'TABLE', N'jb_qiniu_bucket',
'COLUMN', N'remark'
GO

EXEC sp_addextendedproperty
'MS_Description', N'创建人',
'SCHEMA', N'dbo',
'TABLE', N'jb_qiniu_bucket',
'COLUMN', N'create_user_id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'创建时间',
'SCHEMA', N'dbo',
'TABLE', N'jb_qiniu_bucket',
'COLUMN', N'create_time'
GO

EXEC sp_addextendedproperty
'MS_Description', N'更新人',
'SCHEMA', N'dbo',
'TABLE', N'jb_qiniu_bucket',
'COLUMN', N'update_user_id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'更新时间',
'SCHEMA', N'dbo',
'TABLE', N'jb_qiniu_bucket',
'COLUMN', N'update_time'
GO

EXEC sp_addextendedproperty
'MS_Description', N'地区',
'SCHEMA', N'dbo',
'TABLE', N'jb_qiniu_bucket',
'COLUMN', N'region'
GO

EXEC sp_addextendedproperty
'MS_Description', N'七牛bucket配置',
'SCHEMA', N'dbo',
'TABLE', N'jb_qiniu_bucket'
GO

-- ----------------------------
-- Primary Key structure for table jb_qiniu
-- ----------------------------
ALTER TABLE [dbo].[jb_qiniu] ADD CONSTRAINT [PK__jb_qiniu__3213E83F77D3576B] PRIMARY KEY CLUSTERED ([id])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table jb_qiniu_bucket
-- ----------------------------
ALTER TABLE [dbo].[jb_qiniu_bucket] ADD CONSTRAINT [PK__jb_qiniu__3213E83F6EB229B1] PRIMARY KEY CLUSTERED ([id])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO
