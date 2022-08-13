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
  [dept_path] varchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
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
'MS_Description', N'部门路径',
'SCHEMA', N'dbo',
'TABLE', N'jb_dept',
'COLUMN', N'dept_path'
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
  [pid] bigint DEFAULT 0 NOT NULL,
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
  [real_finish_time] datetime  NULL,
  [cancel_time] datetime  NULL,
  [create_user_id] bigint  NOT NULL,
  [update_user_id] bigint  NULL,
  [create_time] datetime  NOT NULL,
  [update_time] datetime  NOT NULL,
  [source_msg_id] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [source_request_id] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [source_url] nvarchar(512) COLLATE Chinese_PRC_CI_AS  NULL,
  [send_user_key] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [receive_user_key] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [source_sys] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [is_readed] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL
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
'MS_Description', N'取消时间',
'SCHEMA', N'dbo',
'TABLE', N'jb_todo',
'COLUMN', N'cancel_time'
GO

EXEC sp_addextendedproperty
'MS_Description', N'第三方系统消息主键',
'SCHEMA', N'dbo',
'TABLE', N'jb_todo',
'COLUMN', N'source_msg_id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'第三方系统流程主键',
'SCHEMA', N'dbo',
'TABLE', N'jb_todo',
'COLUMN', N'source_request_id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'第三方url',
'SCHEMA', N'dbo',
'TABLE', N'jb_todo',
'COLUMN', N'source_url'
GO

EXEC sp_addextendedproperty
'MS_Description', N'发送人第三方系统标识',
'SCHEMA', N'dbo',
'TABLE', N'jb_todo',
'COLUMN', N'send_user_key'
GO

EXEC sp_addextendedproperty
'MS_Description', N'接收人第三方系统标识',
'SCHEMA', N'dbo',
'TABLE', N'jb_todo',
'COLUMN', N'receive_user_key'
GO
EXEC sp_addextendedproperty
'MS_Description', N'来源系统',
'SCHEMA', N'dbo',
'TABLE', N'jb_todo',
'COLUMN', N'source_sys'
GO
EXEC sp_addextendedproperty
'MS_Description', N'是否已读',
'SCHEMA', N'dbo',
'TABLE', N'jb_todo',
'COLUMN', N'is_readed'
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
  [posts] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [dept_path] varchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [update_time] datetime  NOT NULL,
  [update_user_id] bigint  NOT NULL
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
'MS_Description', N'部门路径',
'SCHEMA', N'dbo',
'TABLE', N'jb_user',
'COLUMN', N'dept_path'
GO

EXEC sp_addextendedproperty
'MS_Description', N'更新时间',
'SCHEMA', N'dbo',
'TABLE', N'jb_user',
'COLUMN', N'update_time'
GO

EXEC sp_addextendedproperty
'MS_Description', N'更新人ID',
'SCHEMA', N'dbo',
'TABLE', N'jb_user',
'COLUMN', N'update_user_id'
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
'MS_Description', N'七牛bucket配置',
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
ALTER TABLE [dbo].[jb_qiniu_bucket] ADD CONSTRAINT [PK__jb_qiniu_bucket__3213E83F6EB229B1] PRIMARY KEY CLUSTERED ([id])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Table structure for jb_datasource_filter
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[jb_datasource_filter]') AND type IN ('U'))
	DROP TABLE [dbo].[jb_datasource_filter]
GO

CREATE TABLE [dbo].[jb_datasource_filter] (
  [id] bigint NOT NULL,
  [name] nvarchar(100) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [config_name] nvarchar(100) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [table_name_filter] nvarchar(max) COLLATE Chinese_PRC_CI_AS  NULL,
  [table_name_contains] nvarchar(max) COLLATE Chinese_PRC_CI_AS  NULL,
  [table_name_patterns] nvarchar(max) COLLATE Chinese_PRC_CI_AS  NULL,
  [create_user_id] bigint  NOT NULL,
  [update_user_id] bigint  NOT NULL,
  [create_time] datetime  NOT NULL,
  [update_time] datetime  NULL
)
GO

ALTER TABLE [dbo].[jb_datasource_filter] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'主键ID',
'SCHEMA', N'dbo',
'TABLE', N'jb_datasource_filter',
'COLUMN', N'id'
GO
EXEC sp_addextendedproperty
'MS_Description', N'名称',
'SCHEMA', N'dbo',
'TABLE', N'jb_datasource_filter',
'COLUMN', N'name'
GO

EXEC sp_addextendedproperty
'MS_Description', N'配置名称',
'SCHEMA', N'dbo',
'TABLE', N'jb_datasource_filter',
'COLUMN', N'config_name'
GO

EXEC sp_addextendedproperty
'MS_Description', N'不需要生成的表名',
'SCHEMA', N'dbo',
'TABLE', N'jb_datasource_filter',
'COLUMN', N'table_name_filter'
GO

EXEC sp_addextendedproperty
'MS_Description', N'需要排除包含字符',
'SCHEMA', N'dbo',
'TABLE', N'jb_datasource_filter',
'COLUMN', N'table_name_contains'
GO

EXEC sp_addextendedproperty
'MS_Description', N'需要排除符合正则的',
'SCHEMA', N'dbo',
'TABLE', N'jb_datasource_filter',
'COLUMN', N'table_name_patterns'
GO
 
EXEC sp_addextendedproperty
'MS_Description', N'创建人',
'SCHEMA', N'dbo',
'TABLE', N'jb_datasource_filter',
'COLUMN', N'create_user_id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'创建时间',
'SCHEMA', N'dbo',
'TABLE', N'jb_datasource_filter',
'COLUMN', N'create_time'
GO

EXEC sp_addextendedproperty
'MS_Description', N'更新人',
'SCHEMA', N'dbo',
'TABLE', N'jb_datasource_filter',
'COLUMN', N'update_user_id'
GO

EXEC sp_addextendedproperty
'MS_Description', N'更新时间',
'SCHEMA', N'dbo',
'TABLE', N'jb_datasource_filter',
'COLUMN', N'update_time'
GO

EXEC sp_addextendedproperty
'MS_Description', N'数据源过滤规则',
'SCHEMA', N'dbo',
'TABLE', N'jb_datasource_filter'
GO



-- ----------------------------
-- Primary Key structure for table jb_datasource_filter
-- ----------------------------
ALTER TABLE [dbo].[jb_datasource_filter] ADD CONSTRAINT [PK__jb_datasource_filter__3213E83F6EB229B1] PRIMARY KEY CLUSTERED ([id])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO





    -- ----------------------------
-- Table structure for jb_code_gen
-- ----------------------------
    IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[jb_code_gen]') AND type IN ('U'))
DROP TABLE [dbo].[jb_code_gen]
    GO

CREATE TABLE [dbo].[jb_code_gen] (
    [id] bigint  NOT NULL,
    [pid] bigint DEFAULT 0 NULL,
    [project_path] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NOT NULL,
    [is_sub_table] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [sort_rank] int DEFAULT 1 NOT NULL,
    [type] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NOT NULL,
    [main_table_name] nvarchar(150) COLLATE Chinese_PRC_CI_AS  NOT NULL,
    [datasource_name] nvarchar(100) COLLATE Chinese_PRC_CI_AS  NOT NULL,
    [datasource_remark] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NOT NULL,
    [database_type] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NOT NULL,
    [is_main_datasource] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [main_table_pkey] nvarchar(64) COLLATE Chinese_PRC_CI_AS DEFAULT 'id' NULL,
    [table_remove_prefix] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NULL,
    [main_table_idgenmode] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NOT NULL,
    [version_sn] int  NOT NULL,
    [main_table_remark] nvarchar(100) COLLATE Chinese_PRC_CI_AS  NULL,
    [author] nvarchar(100) COLLATE Chinese_PRC_CI_AS  NOT NULL,
    [style] int  NOT NULL,
    [is_crud] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [is_editable] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [is_tree_table] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [is_check_can_delete] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [is_check_can_toggle] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [is_check_can_recover] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [editable_submit_type] nvarchar(20) COLLATE Chinese_PRC_CI_AS  NULL,
    [state] int  NOT NULL,
    [sub_table_count] int  NULL,
    [topnav_id] bigint  NULL,
    [permission_id] bigint  NULL,
    [roles] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
    [create_user_id] bigint  NOT NULL,
    [create_time] datetime2(7)  NOT NULL,
    [update_user_id] bigint  NOT NULL,
    [update_time] datetime2(7)  NOT NULL,
    [gen_user_id] bigint  NULL,
    [gen_time] datetime2(7)  NULL,
    [model_name] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
    [base_model_name] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
    [remark] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
    [is_auto_cache] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [is_id_cache] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [is_key_cache] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [key_cache_column] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NULL,
    [key_cache_bind_column] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NULL,
    [controller_name] nvarchar(100) COLLATE Chinese_PRC_CI_AS  NOT NULL,
    [controller_path] nvarchar(100) COLLATE Chinese_PRC_CI_AS  NOT NULL,
    [main_java_package] nvarchar(100) COLLATE Chinese_PRC_CI_AS  NOT NULL,
    [service_name] nvarchar(100) COLLATE Chinese_PRC_CI_AS  NOT NULL,
    [controller_package] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NOT NULL,
    [service_package] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NOT NULL,
    [index_html_page_icon] nvarchar(60) COLLATE Chinese_PRC_CI_AS  NULL,
    [index_html_page_title] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NULL,
    [model_package] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
    [html_view_path] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
    [routes_scan_package] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
    [is_gen_model] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '1' NOT NULL,
    [is_table_use_record] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [is_table_record_camel_case] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [is_import_excel] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [is_export_excel] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [is_export_excel_by_checked_ids] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [is_export_excel_by_form] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '1' NOT NULL,
    [is_export_excel_all] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [is_copy_to_excel] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [is_copy_from_excel] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [is_toolbar] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [is_headbox] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [is_leftbox] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [is_rightbox] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [is_footbox] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [is_paginate] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '1' NOT NULL,
    [is_table_sortable_move] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [leftbox_width] int DEFAULT 220  NULL,
    [rightbox_width] int DEFAULT 220  NULL,
    [headbox_height] int DEFAULT 60  NULL,
    [footbox_height] int DEFAULT 220  NULL,
    [is_leftbox_footer] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '1' NOT NULL,
    [is_rightbox_footer] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '1' NOT NULL,
    [leftbox_footer_button_count] int  NULL,
    [rightbox_footer_button_count] int  NULL,
    [leftbox_title] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NULL,
    [leftbox_icon] nvarchar(60) COLLATE Chinese_PRC_CI_AS  NULL,
    [rightbox_title] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NULL,
    [rightbox_icon] nvarchar(60) COLLATE Chinese_PRC_CI_AS  NULL,
    [is_show_optcol_sort] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [is_show_optcol_edit] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [is_show_optcol_del] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [is_show_optcol] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '1' NOT NULL,
    [is_show_optcol_recover] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [default_sort_column] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NULL,
    [default_sort_type] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NULL,
    [table_optcol_width] int DEFAULT 80 NOT NULL,
    [is_table_column_resize] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [is_table_prepend_column] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [table_prepend_column_type] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NULL,
    [table_prepend_column_index] int DEFAULT 1 NOT NULL,
    [is_table_prepend_column_linkparent] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [is_table_prepend_column_linkson] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [table_prepend_column_rowspan] int DEFAULT 1 NOT NULL,
    [is_table_prepend_column_click_active] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [table_fixed_column_left] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NULL,
    [table_fixed_column_right] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NULL,
    [table_pagesize_options] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
    [table_width_assign] nvarchar(40) COLLATE Chinese_PRC_CI_AS NULL,
    [table_width] nvarchar(40) COLLATE Chinese_PRC_CI_AS DEFAULT 'fill' NOT NULL,
    [table_height_assign] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NULL,
    [table_height] nvarchar(40) COLLATE Chinese_PRC_CI_AS DEFAULT 'fill' NOT NULL,
    [table_default_sort_column] nvarchar(60) COLLATE Chinese_PRC_CI_AS DEFAULT 'id' NULL,
    [table_default_sort_type] nvarchar(40) COLLATE Chinese_PRC_CI_AS  DEFAULT 'desc' NULL,
    [is_keywords_search] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [keywords_match_columns] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
    [toolbar_extra_button_size] int  NULL,
    [is_deleted] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [form_column_size] int DEFAULT 1 NOT NULL,
    [is_form_group_row] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '1' NOT NULL,
    [form_column_proportion] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NULL,
    [form_column_direction] nvarchar(10) COLLATE Chinese_PRC_CI_AS DEFAULT 'v' NULL,
    [form_group_proportion] nvarchar(40) COLLATE Chinese_PRC_CI_AS DEFAULT '2:10'  NULL,
    [is_view_use_path] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '1' NOT NULL,
    [view_layout] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NOT NULL,
    [is_need_new_route] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [routes_class_name] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
    [is_need_admin_interceptor] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '1' NOT NULL,
    [extra_interceptor_class_name] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
    [is_table_multi_conditions_mode] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [is_table_multi_conditions_default_hide] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '1' NOT NULL,
    [is_table_multi_conditions_btn_show_title] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '1' NOT NULL,
    [is_toolbar_add_btn] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '1' NOT NULL,
    [is_toolbar_edit_btn] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '1' NOT NULL,
    [is_toolbar_del_btn] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '1' NOT NULL,
    [is_toolbar_recover_btn] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [is_toolbar_refresh_btn] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '1' NOT NULL,
    [is_page_title_add_btn] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '1' NOT NULL,
    [is_page_title_refresh_btn] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '1' NOT NULL,
    [is_page_title_init_rank_btn] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [is_project_system_log] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [project_system_log_target_type_text] nvarchar(20) COLLATE Chinese_PRC_CI_AS  NULL,
    [project_system_log_target_type_value] nvarchar(10) COLLATE Chinese_PRC_CI_AS  NULL,
    [project_system_log_target_type_key_name] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NULL,
    [form_dialog_area] nvarchar(20) COLLATE Chinese_PRC_CI_AS DEFAULT '800,600' NULL,
    [is_base_model_gen_col_constant] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '1' NOT NULL,
    [is_base_model_gen_col_constant_to_uppercase] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '1' NOT NULL
    )
    GO

ALTER TABLE [dbo].[jb_code_gen] SET (LOCK_ESCALATION = TABLE)
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'主键ID',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'id'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'父ID',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'pid'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'项目根路径',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'project_path'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否为子表',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_sub_table'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'子表的顺序',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'sort_rank'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'模块类型',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'type'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'主表名',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'main_table_name'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'数据源',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'datasource_name'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'数据源说明',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'datasource_remark'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'数据库类型',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'database_type'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否为主数据源',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_main_datasource'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'主表主键',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'main_table_pkey'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'数据表删除前缀',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'table_remove_prefix'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'主表主键策略',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'main_table_idgenmode'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'版本序号',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'version_sn'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'表备注',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'main_table_remark'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'功能作者',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'author'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'样式类型',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'style'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否CRUD',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_crud'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否可编辑表格',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_editable'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否为树表',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_tree_table'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'检测是否可以刪除数据',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_check_can_delete'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'检测是否可以toggle数据',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_check_can_toggle'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'检测是否可以recover数据',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_check_can_recover'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'可编辑提交方式',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'editable_submit_type'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'生成状态',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'state'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'子表数',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'sub_table_count'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'顶部导航',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'topnav_id'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'关联权限',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'permission_id'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'可访问角色',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'roles'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'创建人ID',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'create_user_id'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'创建时间',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'create_time'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'更新人ID',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'update_user_id'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'更新时间',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'update_time'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'创建后执行生成人ID',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'gen_user_id'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'创建后执行生成时间',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'gen_time'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'modelName',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'model_name'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'baseModelName',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'base_model_name'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'备注',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'remark'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否启用autoCache',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_auto_cache'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否启用idCache',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_id_cache'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否启用keyCache',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_key_cache'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'keyCache指定Column',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'key_cache_column'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'keyCache指定bindColumn',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'key_cache_bind_column'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'Controller Name',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'controller_name'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'controller path',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'controller_path'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'java主包pacakge',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'main_java_package'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'Service Name',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'service_name'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'Controller代码包',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'controller_package'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'Service代码包',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'service_package'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'index.html标题icon',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'index_html_page_icon'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'index.html页面标题',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'index_html_page_title'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'model 所属package',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'model_package'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'html view path',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'html_view_path'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'路由扫描包',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'routes_scan_package'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否需要生成Model',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_gen_model'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'表格是否使用record',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_table_use_record'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'表格列名用驼峰的attrName',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_table_record_camel_case'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否支持Excel导入',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_import_excel'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否支持Excel导出',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_export_excel'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否启用 导出选中行功能',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_export_excel_by_checked_ids'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否启用导出表单查询结果功能',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_export_excel_by_form'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否启用导出所有数据',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_export_excel_all'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否支持表格复制到excel',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_copy_to_excel'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否支持从excel复制到可编辑表格',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_copy_from_excel'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否使用toolbar模式',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_toolbar'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否使用headbox',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_headbox'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否使用leftBox',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_leftbox'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否使用rightBox',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_rightbox'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否使用footbox',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_footbox'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否分页查询',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_paginate'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否开启移动排序功能',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_table_sortable_move'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'leftbox width',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'leftbox_width'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'right width',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'rightbox_width'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'headbox height',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'headbox_height'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'footbox height',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'footbox_height'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否启用leftbox的footer',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_leftbox_footer'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否启用rightbox的footer',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_rightbox_footer'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'leftbox footer button count',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'leftbox_footer_button_count'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'rightbox footer button count',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'rightbox_footer_button_count'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'leftbox title',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'leftbox_title'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'leftbox icon',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'leftbox_icon'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'rightbox title',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'rightbox_title'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'rightbox icon',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'rightbox_icon'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否开启操作列排序功能',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_show_optcol_sort'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否开启操作列显示编辑按钮',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_show_optcol_edit'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否开启操作列显示删除按钮',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_show_optcol_del'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否显示操作列',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_show_optcol'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否显示操作列的恢复按钮',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_show_optcol_recover'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'默认排序字段',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'default_sort_column'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'默认排序方式',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'default_sort_type'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'长度',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'table_optcol_width'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'表格是否开启调整列宽功能',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_table_column_resize'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否增加填充列',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_table_prepend_column'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'表格chechbox radio配置类型',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'table_prepend_column_type'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'填充列到第几列',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'table_prepend_column_index'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'填充列linkparent',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_table_prepend_column_linkparent'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'填充列linkson',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_table_prepend_column_linkson'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'填充列表头是几行rowspan',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'table_prepend_column_rowspan'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否点击行就切换列填充组件选中状态',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_table_prepend_column_click_active'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'左侧固定列',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'table_fixed_column_left'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'右侧固定列',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'table_fixed_column_right'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'分页pagesize自定义设置',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'table_pagesize_options'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'表格宽度自定义值',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'table_width_assign'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'表格宽度',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'table_width'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'表格高度自定义值',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'table_height_assign'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'表格高度',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'table_height'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'表格默认排序字段',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'table_default_sort_column'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'表格默认排序类型',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'table_default_sort_type'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否开启关键词查询',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_keywords_search'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'关键词匹配列',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'keywords_match_columns'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'toolbar 额外预留按钮个数',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'toolbar_extra_button_size'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'删除标识',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_deleted'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'表单分几列',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'form_column_size'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'表单form-group风格 左右还是上下',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_form_group_row'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'表单分多列 比例值',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'form_column_proportion'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'表单列排布方向 横向还是纵向',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'form_column_direction'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'form-group row状态下的比例',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'form_group_proportion'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否启用Path注解路由',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_view_use_path'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'使用布局器',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'view_layout'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否需要创建新的路由配置类',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_need_new_route'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'哪个路由配置类',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'routes_class_name'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否需要后台管理权限拦截器',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_need_admin_interceptor'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'额外配置的拦截器',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'extra_interceptor_class_name'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'表格查询条件是否启用高级多条件模式',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_table_multi_conditions_mode'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'表格查询高级模式 是否隐藏条件 默认隐藏',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_table_multi_conditions_default_hide'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'表格高级查询条件切换按钮是否显示标题',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_table_multi_conditions_btn_show_title'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'表格toolbar上启用添加按钮',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_toolbar_add_btn'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'表格toolbar上启用编辑按钮',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_toolbar_edit_btn'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'表格toolbar上启用删除按钮',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_toolbar_del_btn'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'表格toolbar上启用恢复按钮 当有is_deleted时',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_toolbar_recover_btn'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'表格tolbar上启用刷新按钮',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_toolbar_refresh_btn'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否启用pageTitle上的添加按钮',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_page_title_add_btn'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否启用pageTitle上的刷新按钮',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_page_title_refresh_btn'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否启用pageTitle上的初始化顺序按钮',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_page_title_init_rank_btn'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否启用systemLog日志',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_project_system_log'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'系统日志text',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'project_system_log_target_type_text'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'系统日志value值',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'project_system_log_target_type_value'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'系统日志KeyName',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'project_system_log_target_type_key_name'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'form表单的dialog的area属性 长宽',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'form_dialog_area'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否在baseModel中生成字段常量',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_base_model_gen_col_constant'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否在baseModel中生成的字段常量 名称转大写',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen',
    'COLUMN', N'is_base_model_gen_col_constant_to_uppercase'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'代码生成配置',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen'
    GO


    -- ----------------------------
-- Table structure for jb_code_gen_model_attr
-- ----------------------------
    IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[jb_code_gen_model_attr]') AND type IN ('U'))
DROP TABLE [dbo].[jb_code_gen_model_attr]
    GO

CREATE TABLE [dbo].[jb_code_gen_model_attr] (
    [id] bigint  NOT NULL,
    [code_gen_id] bigint  NOT NULL,
    [col_name] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NOT NULL,
    [attr_name] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NOT NULL,
    [java_type] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NOT NULL,
    [attr_length] int DEFAULT 20 NOT NULL,
    [attr_fixed] int DEFAULT 0 NULL,
    [attr_default_value] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NULL,
    [sort_rank] int DEFAULT 1 NOT NULL,
    [sort_rank_intable] int DEFAULT 1 NOT NULL,
    [sort_rank_inform] int DEFAULT 1 NOT NULL,
    [sort_rank_insearch] int DEFAULT 1 NOT NULL,
    [is_pkey] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [is_required] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [is_search_required] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [data_rule_for_search] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
    [data_tips_for_search] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
    [form_label] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
    [placeholder] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NULL,
    [table_label] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NULL,
    [search_form_label] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NULL,
    [remark] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
    [is_keywords_column] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [is_form_ele] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '1' NOT NULL,
    [is_table_col] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '1' NOT NULL,
    [is_table_switchbtn] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [table_col_width] int DEFAULT 100 NOT NULL,
    [is_need_fixed_width] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '1' NOT NULL,
    [is_search_ele] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [is_search_hidden] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [col_format] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
    [search_ui_type] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NULL,
    [search_data_type] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NULL,
    [search_data_value] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
    [search_default_value] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
    [is_single_line] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [need_data_handler] nchar(1) COLLATE Chinese_PRC_CI_AS  NULL,
    [form_ui_type] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NULL,
    [form_jboltinput_filter_handler] nvarchar(60) COLLATE Chinese_PRC_CI_AS  NULL,
    [is_form_jboltinput_jstree_checkbox] nchar(1) COLLATE Chinese_PRC_CI_AS  NULL,
    [is_form_jboltinput_jstree_only_leaf] nchar(1) COLLATE Chinese_PRC_CI_AS  NULL,
    [form_data_type] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NULL,
    [form_data_value] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
    [form_default_value] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
    [data_rule_assign] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
    [data_rule] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
    [data_tips] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
    [is_import_col] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [is_export_col] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [is_sortable] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [table_ui_type] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NULL,
    [table_data_type] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NULL,
    [table_data_value] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
    [form_ele_width] int DEFAULT 0 NOT NULL,
    [is_item_inline] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '1' NOT NULL,
    [form_data_text_attr] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
    [form_data_value_attr] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NULL,
    [form_data_column_attr] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
    [search_data_text_attr] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
    [search_data_value_attr] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NULL,
    [search_data_column_attr] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
    [table_data_text_attr] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
    [table_data_value_attr] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NULL,
    [table_data_column_attr] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
    [is_need_translate] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [translate_type] nvarchar(20) COLLATE Chinese_PRC_CI_AS  NULL,
    [translate_use_value] nvarchar(250) COLLATE Chinese_PRC_CI_AS  NULL,
    [translate_col_name] nvarchar(250) COLLATE Chinese_PRC_CI_AS  NULL
    )
    GO

ALTER TABLE [dbo].[jb_code_gen_model_attr] SET (LOCK_ESCALATION = TABLE)
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'主键ID',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'id'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'所属codeGen',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'code_gen_id'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'列名',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'col_name'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'属性名',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'attr_name'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'属性类型',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'java_type'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'属性长度',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'attr_length'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'属性小数点',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'attr_fixed'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'默认值',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'attr_default_value'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'数据表内默认顺序',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'sort_rank'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'表格中的排序',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'sort_rank_intable'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'表单中的排序',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'sort_rank_inform'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'查询条件中的顺序',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'sort_rank_insearch'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否主键',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'is_pkey'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否必填',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'is_required'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'作为查询条件是否必填',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'is_search_required'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'查询条件必填校验规则',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'data_rule_for_search'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'查询条件不符合校验的提示信息',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'data_tips_for_search'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'表单单显示文本',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'form_label'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'表单placeholder',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'placeholder'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'表格中显示文本',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'table_label'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'查询表单提示文本',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'search_form_label'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'备注',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'remark'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否为关键词查询列',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'is_keywords_column'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否表单可编辑元素',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'is_form_ele'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否表格列',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'is_table_col'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否为表格switchbtn',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'is_table_switchbtn'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'列宽',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'table_col_width'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否固定宽度',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'is_need_fixed_width'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否检索条件',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'is_search_ele'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否为检索隐藏条件',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'is_search_hidden'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'格式化操作值',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'col_format'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'查询用ui 组件类型',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'search_ui_type'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'查询用组件数据源类型',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'search_data_type'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'查询用组件数据值',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'search_data_value'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'查询用组件默认值',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'search_default_value'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'独立新行',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'is_single_line'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否需要data_handler',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'need_data_handler'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'表单组件类型',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'form_ui_type'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'jboltinput filter handler',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'form_jboltinput_filter_handler'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'jboltinput jstree是否有checkbox',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'is_form_jboltinput_jstree_checkbox'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'jboltinput jstree checkbox只选子节点',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'is_form_jboltinput_jstree_only_leaf'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'表单组件数据源类型',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'form_data_type'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'表单组件数据值',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'form_data_value'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'表单组件默认值',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'form_default_value'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'表单校验规则 自定义',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'data_rule_assign'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'校验规则',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'data_rule'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'校验提示信息',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'data_tips'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否为导入列',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'is_import_col'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'导出列',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'is_export_col'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否可排序',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'is_sortable'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'可编辑表格显示组件类型',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'table_ui_type'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'表格组件数据库类型',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'table_data_type'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'表格组件数据值',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'table_data_value'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'组件自定义宽度',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'form_ele_width'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'radio checkbox等是否inline',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'is_item_inline'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'data-text-attr',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'form_data_text_attr'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'data-value-attr',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'form_data_value_attr'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'data-column-attr',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'form_data_column_attr'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'data-text-attr',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'search_data_text_attr'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'data-value-attr',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'search_data_value_attr'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'data-column-attr',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'search_data_column_attr'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'data-text-attr',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'table_data_text_attr'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'data-value-attr',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'table_data_value_attr'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'data-column-attr',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'table_data_column_attr'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否需要翻译',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'is_need_translate'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'翻译类型',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'translate_type'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'翻译用值',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'translate_use_value'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'翻译后的列名',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr',
    'COLUMN', N'translate_col_name'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'CodeGen模型详细设计',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_code_gen_model_attr'
    GO




-- ----------------------------
-- Primary Key structure for table jb_code_gen
-- ----------------------------
ALTER TABLE [dbo].[jb_code_gen] ADD CONSTRAINT [PK__jb_code___3213E83F4A6C6EF3] PRIMARY KEY CLUSTERED ([id])
    WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)
    ON [PRIMARY]
    GO


-- ----------------------------
-- Primary Key structure for table jb_code_gen_model_attr
-- ----------------------------
ALTER TABLE [dbo].[jb_code_gen_model_attr] ADD CONSTRAINT [PK__jb_code___3213E83F5BC641F6] PRIMARY KEY CLUSTERED ([id])
    WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)
    ON [PRIMARY]
    GO

