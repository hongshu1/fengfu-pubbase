alter table [dbo].[jb_code_gen_model_attr] add [is_need_translate] COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL;
alter table [dbo].[jb_code_gen_model_attr] add [translate_type] nvarchar(20) COLLATE Chinese_PRC_CI_AS  NULL;
alter table [dbo].[jb_code_gen_model_attr] add [translate_use_value] nvarchar(250) COLLATE Chinese_PRC_CI_AS  NULL;
alter table [dbo].[jb_code_gen_model_attr] add [translate_col_name] nvarchar(250) COLLATE Chinese_PRC_CI_AS  NULL;

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
