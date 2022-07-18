alter table [dbo].[jb_dept] add [dept_path] varchar(255) COLLATE Chinese_PRC_CI_AS  NULL;
EXEC sp_addextendedproperty
'MS_Description', N'部门路径',
'SCHEMA', N'dbo',
'TABLE', N'jb_dept',
'COLUMN', N'dept_path'
GO

alter table [dbo].[jb_user] add [dept_path] varchar(255) COLLATE Chinese_PRC_CI_AS  NULL;
alter table [dbo].[jb_user] add [update_time] datetime  NOT NULL;
alter table [dbo].[jb_user] add [update_user_id] datetime  NOT NULL;

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
