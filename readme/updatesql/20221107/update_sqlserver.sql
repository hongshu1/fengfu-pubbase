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



CREATE TABLE [dbo].[jb_user_extend] (
    [id] bigint  NOT NULL,
    [english_full_name] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NULL,
    [residential_address] nvarchar(100) COLLATE Chinese_PRC_CI_AS  NULL,
    [company_name] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NULL,
    [company_address] nvarchar(100) COLLATE Chinese_PRC_CI_AS  NULL,
    [recipient_address] nvarchar(100) COLLATE Chinese_PRC_CI_AS  NULL,
    [recipient_phone] nvarchar(20) COLLATE Chinese_PRC_CI_AS  NULL,
    [recipient_name] nvarchar(40) COLLATE Chinese_PRC_CI_AS  NULL,
    [id_number] nvarchar(20) COLLATE Chinese_PRC_CI_AS  NULL,
    [country] nvarchar(20) COLLATE Chinese_PRC_CI_AS  NULL,
    [nation] nvarchar(20) COLLATE Chinese_PRC_CI_AS  NULL,
    [province] nvarchar(20) COLLATE Chinese_PRC_CI_AS  NULL,
    [city] nvarchar(20) COLLATE Chinese_PRC_CI_AS  NULL,
    [city_code] nvarchar(20) COLLATE Chinese_PRC_CI_AS  NULL,
    [county] nvarchar(20) COLLATE Chinese_PRC_CI_AS  NULL,
    [township] nvarchar(20) COLLATE Chinese_PRC_CI_AS  NULL,
    [community] nvarchar(20) COLLATE Chinese_PRC_CI_AS  NULL,
    [marital_status] int  NULL,
    [is_cpc_member] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [is_cyl_member] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL,
    [professional_title] int  NULL,
    [birthday] datetime  NULL,
    CONSTRAINT [PK__jb_user___3213E83F9B5BEAFB] PRIMARY KEY CLUSTERED ([id])
    WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)
    ON [PRIMARY]
    )
    ON [PRIMARY]
    GO

ALTER TABLE [dbo].[jb_user_extend] SET (LOCK_ESCALATION = TABLE)
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'主键ID',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_user_extend',
    'COLUMN', N'id'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'英文全名',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_user_extend',
    'COLUMN', N'english_full_name'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'居住地址',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_user_extend',
    'COLUMN', N'residential_address'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'公司名称',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_user_extend',
    'COLUMN', N'company_name'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'公司地址',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_user_extend',
    'COLUMN', N'company_address'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'快递收件地址',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_user_extend',
    'COLUMN', N'recipient_address'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'收件人电话',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_user_extend',
    'COLUMN', N'recipient_phone'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'收件人姓名',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_user_extend',
    'COLUMN', N'recipient_name'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'身份证号',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_user_extend',
    'COLUMN', N'id_number'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'国家',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_user_extend',
    'COLUMN', N'country'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'民族',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_user_extend',
    'COLUMN', N'nation'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'省',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_user_extend',
    'COLUMN', N'province'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'城市',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_user_extend',
    'COLUMN', N'city'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'城市代码',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_user_extend',
    'COLUMN', N'city_code'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'区县',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_user_extend',
    'COLUMN', N'county'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'乡镇',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_user_extend',
    'COLUMN', N'township'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'行政村 社区',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_user_extend',
    'COLUMN', N'community'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'婚姻状态',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_user_extend',
    'COLUMN', N'marital_status'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否党员',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_user_extend',
    'COLUMN', N'is_cpc_member'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'是否共青团员',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_user_extend',
    'COLUMN', N'is_cyl_member'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'职称',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_user_extend',
    'COLUMN', N'professional_title'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'出生日期',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_user_extend',
    'COLUMN', N'birthday'
    GO

    EXEC sp_addextendedproperty
    'MS_Description', N'用户扩展信息表',
    'SCHEMA', N'dbo',
    'TABLE', N'jb_user_extend'