alter table [dbo].[jb_user] add [sn] nvarchar(20) COLLATE Chinese_PRC_CI_AS NULL;

EXEC sp_addextendedproperty
'MS_Description', N'工号',
'SCHEMA', N'dbo',
'TABLE', N'jb_user',
'COLUMN', N'sn'
GO

alter table [dbo].[jb_user] add [age] int DEFAULT 0 NOT NULL;

EXEC sp_addextendedproperty
'MS_Description', N'年龄',
'SCHEMA', N'dbo',
'TABLE', N'jb_user',
'COLUMN', N'age'
GO




CREATE TABLE [dbo].[jb_sensitive_word] (
    [id] bigint  NOT NULL,
    [content] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NOT NULL,
    [enable] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '1' NOT NULL,
    CONSTRAINT [PK__jb_sensi__3213E83FAACE9BB0] PRIMARY KEY CLUSTERED ([id])
    WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)
    ON [PRIMARY]
    )
    ON [PRIMARY]
    GO

ALTER TABLE [dbo].[jb_sensitive_word] SET (LOCK_ESCALATION = TABLE)
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'主键ID',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_sensitive_word',
    'COLUMN', N'id'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'内容',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_sensitive_word',
    'COLUMN', N'content'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'启用状态',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_sensitive_word',
    'COLUMN', N'enable'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'敏感词词库',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_sensitive_word'