alter table [dbo].[jb_code_gen_model_attr] add [is_upload_to_qiniu] char(1) COLLATE Chinese_PRC_CI_AS DEFAULT '0' NOT NULL;

EXEC sp_addextendedproperty
'MS_Description', N'是否上传到七牛',
'SCHEMA', N'dbo',
'TABLE', N'jb_code_gen_model_attr',
'COLUMN', N'is_upload_to_qiniu'
GO

alter table [dbo].[jb_code_gen_model_attr] add [form_upload_url] nvarchar(255) COLLATE Chinese_PRC_CI_AS NULL;

EXEC sp_addextendedproperty
'MS_Description', N'上传地址',
'SCHEMA', N'dbo',
'TABLE', N'jb_code_gen_model_attr',
'COLUMN', N'form_upload_url'
GO

alter table [dbo].[jb_code_gen_model_attr] add [form_img_uploader_area] nvarchar(20) COLLATE Chinese_PRC_CI_AS DEFAULT '200,200' NOT NULL;

EXEC sp_addextendedproperty
'MS_Description', N'上传组件area',
'SCHEMA', N'dbo',
'TABLE', N'jb_code_gen_model_attr',
'COLUMN', N'form_img_uploader_area'
GO

alter table [dbo].[jb_code_gen_model_attr] add [form_maxsize] int DEFAULT 200 NOT NULL;

EXEC sp_addextendedproperty
'MS_Description', N'上传尺寸限制',
'SCHEMA', N'dbo',
'TABLE', N'jb_code_gen_model_attr',
'COLUMN', N'form_maxsize'
GO

alter table [dbo].[jb_code_gen_model_attr] add [qiniu_bucket_sn] nvarchar(60) COLLATE Chinese_PRC_CI_AS NULL;

EXEC sp_addextendedproperty
'MS_Description', N'七牛bucket sn',
'SCHEMA', N'dbo',
'TABLE', N'jb_code_gen_model_attr',
'COLUMN', N'qiniu_bucket_sn'
GO


alter table [dbo].[jb_code_gen_model_attr] add [qiniu_file_key] nvarchar(100) COLLATE Chinese_PRC_CI_AS DEFAULT '[dateTime]/[randomId]/[filename]' NOT NULL;

EXEC sp_addextendedproperty
'MS_Description', N'七牛file key',
'SCHEMA', N'dbo',
'TABLE', N'jb_code_gen_model_attr',
'COLUMN', N'qiniu_file_key'
GO