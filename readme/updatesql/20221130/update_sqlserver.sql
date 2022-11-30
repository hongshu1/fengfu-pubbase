alter table [dbo].[jb_dictionary_type] add [is_build_in] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL;

EXEC sp_addextendedproperty
'MS_Description', N'是否内置',
'SCHEMA', N'dbo',
'TABLE', N'jb_dictionary_type',
'COLUMN', N'is_build_in'
GO

alter table [dbo].[jb_dictionary] add [is_build_in] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL;

EXEC sp_addextendedproperty
'MS_Description', N'是否内置',
'SCHEMA', N'dbo',
'TABLE', N'jb_dictionary',
'COLUMN', N'is_build_in'
GO