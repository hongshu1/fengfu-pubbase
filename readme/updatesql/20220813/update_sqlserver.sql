alter table [dbo].[jb_code_gen] add [is_table_multi_conditions_default_hide] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '1' NOT NULL;

EXEC sp_addextendedproperty
'MS_Description', N'表格查询高级模式 是否隐藏条件 默认隐藏',
'SCHEMA', N'dbo',
'TABLE', N'jb_code_gen',
'COLUMN', N'is_table_multi_conditions_default_hide'
GO
