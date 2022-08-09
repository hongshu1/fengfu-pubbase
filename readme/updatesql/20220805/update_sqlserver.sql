alter table [dbo].[jb_code_gen] add [is_table_multi_conditions_btn_show_title] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '1' NOT NULL;
alter table [dbo].[jb_code_gen] add [is_base_model_gen_col_constant] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '1' NOT NULL;
alter table [dbo].[jb_code_gen] add [is_base_model_gen_col_constant_to_uppercase] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '1' NOT NULL;

EXEC sp_addextendedproperty
'MS_Description', N'表格高级查询条件切换按钮是否显示标题',
'SCHEMA', N'dbo',
'TABLE', N'jb_code_gen',
'COLUMN', N'is_table_multi_conditions_btn_show_title'
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
