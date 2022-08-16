alter table `jb_code_gen_model_attr` add column `is_upload_to_qiniu` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '是否上传到七牛';
alter table `jb_code_gen_model_attr` add column `form_upload_url` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '上传地址';
alter table `jb_code_gen_model_attr` add column `form_img_uploader_area` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '200,200' COMMENT '上传组件area';
alter table `jb_code_gen_model_attr` add column `form_maxsize` int(11) DEFAULT '200' COMMENT '上传尺寸限制';
alter table `jb_code_gen_model_attr` add column `qiniu_bucket_sn` varchar(60) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '七牛bucket sn';
alter table `jb_code_gen_model_attr` add column `qiniu_file_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '[dateTime]/[randomId]/[filename]' COMMENT '七牛file key';