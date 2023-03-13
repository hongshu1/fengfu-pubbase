### 后台管理分页查询
#sql("paginateAdminList")
select #(columns?? "*") from #(table) where 1=1
#if(keywords??)
#setLocal(kw=SqlUtil.likeValue(keywords))
and ((name like '%#(kw)%') or (username like '%#(kw)%') or (phone like '%#(kw)%') or (pinyin like '%#(kw)%'))
#end
order by id desc
#end


#sql("hasUserId")
SELECT TOP 1 1
FROM jb_user
WHERE cUser_Id = #para(cuserid)
#end

#sql("findByCuserId")
SELECT *
FROM jb_user
WHERE cuser_id = #para(cuserId)
#end

#sql("getListByRoleId")
SELECT *
FROM jb_user
WHERE roles LIKE CONCAT('%', #para(roleid), '%')
#end

#sql("hasRecord")
SELECT TOP 1 1
FROM #(getBaseDbName()).dbo.jb_user u
INNER JOIN Ua_User uu ON u.cUser_Id = uu.cUser_Id
WHERE uu.cDept = #para(cdepname)
AND u.roles LIKE CONCAT('%', #para(iroleid), '%')
#end

#sql("findPurchaseUsers")
select * from (
select *, (select top 1 id from jb_role where name = '采购') roleid
from #(getBaseDbName()).dbo.jb_user ju
) T where CHARINDEX(Convert(nvarchar,T.roleid),roles) > 0
#end

#sql("getRecords")
SELECT u.*
FROM #(getBaseDbName()).dbo.jb_user u
INNER JOIN Ua_User uu ON u.cUser_Id = uu.cUser_Id
WHERE uu.cDept = #para(cdepname)
AND u.roles LIKE CONCAT('%', #para(iroleid), '%')
#end

#sql("queryEmail")
SELECT email
FROM jb_user
WHERE id = #para(id)
#end

#sql("datas")
SELECT
avatar,
cpersoncode,
create_time,
create_user_id,
u.cuser_id,
dept_id,
email,
enable,
id,
is_remote_login,
is_system_admin,
login_city,
login_country,
login_ip,
login_province,
login_time,
name,
phone,
pinyin,
posts,
roles,
sex,
username
FROM #(getBaseDbName()).dbo.jb_user u
WHERE 1 = 1
#if(keywords)
AND  ( u.name LIKE CONCAT('%', #para(keywords), '%') OR
u.username LIKE CONCAT('%', #para(keywords), '%') OR
u.phone LIKE CONCAT('%', #para(keywords), '%')
OR u.pinyin LIKE CONCAT('%', #para(keywords), '%')
)
#end
#if(is_system_admin)
AND u.is_system_admin = #para(is_system_admin)
#end
#if(sex)
AND u.sex = #para(sex)
#end
#if(enable)
AND u.enable = #para(enable)
#end
#if(roleId)
AND u.roles LIKE CONCAT('%', #para(roleId), '%')
#end
ORDER BY u.id DESC
#end

#sql("findFirstByDepCodeAndRole")
SELECT u.*
FROM #(getBaseDbName()).dbo.jb_user u
INNER JOIN Ua_User uu ON u.cuser_id = uu.cuser_id
INNER JOIN Department d ON uu.cdept = d.cdepname
WHERE u.roles LIKE CONCAT('%', #para(iroleid), '%')
AND d.cdepcode = #para(cdepcode)
#end

#sql("findUserByVerifyProgressNode")
SELECT ju.*
FROM #(getBaseDbName()).dbo.jb_user ju
LEFT JOIN UA_User uau ON ju.cuser_id = uau.cuser_id
LEFT JOIN department d ON d.cdepname = uau.cdept
WHERE d.cdepcode = #para(cdepcode)
AND ju.roles LIKE CONCAT('%', #para(roleid),'%')
#end