### 分页查询数据
#sql("list")
SELECT
    o.id,
    o.org_Code,
    o.org_Name,
    o.u8_Dbname,
    o.u8_Alias,
    o.enable,
    o.enable_Time,
    o.remark,
    u.name
FROM jb_org o
    LEFT JOIN jb_user u ON u.id = o.user_id
WHERE 1=1
    #if(u8Alias)
        AND o.u8_alias LIKE CONCAT('%', #para(u8Alias), '%')
    #end
    #if(u8Dbname)
        AND o.u8_dbname LIKE CONCAT('%', #para(u8Dbname), '%')
    #end
    #if(orgCode)
        AND o.org_code LIKE CONCAT('%', #para(orgCode), '%')
    #end
    #if(orgName)
        AND o.org_name LIKE CONCAT('%', #para(orgName), '%')
    #end
#end

#sql("getU8DbList")
SELECT name
FROM sys.databases
WHERE name LIKE 'UFDATA_%'
#end