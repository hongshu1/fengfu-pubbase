alter table "jb_dictionary_type" add COLUMN  "is_build_in" char(1) COLLATE "pg_catalog"."default" NOT NULL DEFAULT '0'::bpchar;
COMMENT ON COLUMN "public"."jb_dictionary_type"."is_build_in" IS '是否内置';

alter table "jb_dictionary" add COLUMN  "is_build_in" char(1) COLLATE "pg_catalog"."default" NOT NULL DEFAULT '0'::bpchar;
COMMENT ON COLUMN "public"."jb_dictionary"."is_build_in" IS '是否内置';
