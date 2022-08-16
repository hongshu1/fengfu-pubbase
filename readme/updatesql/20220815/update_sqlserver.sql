alter table [dbo].[jb_dictionary_type] add [enable] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '1' NOT NULL;

EXEC sp_addextendedproperty
'MS_Description', N'是否启用',
'SCHEMA', N'dbo',
'TABLE', N'jb_dictionary_type',
'COLUMN', N'enable'
GO

alter table [dbo].[jb_dictionary] add [enable] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '1' NOT NULL;

EXEC sp_addextendedproperty
'MS_Description', N'是否启用',
'SCHEMA', N'dbo',
'TABLE', N'jb_dictionary',
'COLUMN', N'enable'
GO

