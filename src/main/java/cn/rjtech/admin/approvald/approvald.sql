#sql("findListByMid")
select * from Bd_ApprovalD where iApprovalMid = '#(id)' order by iSeq asc
#end

#sql("findRecordsByMid")
select t1.*, t2.name as stepname,t3.name as way from Bd_ApprovalD t1
 left join #(getBaseDbName()).dbo.jb_dictionary t2 on t2.type_key = 'approval_d_name' and t2.sn = t1.iStep
 left join #(getBaseDbName()).dbo.jb_dictionary t3 on t3.type_key = 'approval_person_config' and t3.sn = t1.iType
 where iApprovalMid = '#(id)' order by iSeq asc
#end

#sql("getPerson")
select t1.iAutoId as ipersonid, t1.cPsn_Num as cpsncode, t1.cPsn_Name as cpsnname, t2.name as cdeptname, t1.iUserId as
iuserid, t3.username
from Bd_Person t1
left join #(getBaseDbName()).dbo.jb_dept t2 on t2.sn = t1.cDept_num
left join #(getBaseDbName()).dbo.jb_user t3 on t3.id = t1.iUserId
where t1.isDeleted = '0' and t1.iUserId is not null
#if(itemHidden)
    and t1.iAutoId not in (#(itemHidden))
#end
#if(orgCode)
and t1.cOrgCode = '#(orgCode)'
#end
#if(dept)
and t1.cDept_num = '#(dept)'
#end
#if(keys)
and (t1.cPsn_Num like '#(keys)%' or t1.cPsn_Name like '%#(keys)%')
#end
#if(keywords)
and (t1.cPsn_Num like '#(keywords)%' or t1.cPsn_Name like '%#(keywords)%')
#end
order by t1.dUpdateTime
#end

#sql("selectPerson")
select t1.iAutoId as ipersonid, t1.cPsn_Name as cpsnname,t1.cPsn_Num as cpsnnum
from Bd_Person t1
where t1.isDeleted = '0' and t1.iUserId is not null
#if(orgCode)
and t1.cOrgCode = '#(orgCode)'
#end
#if(keys)
and (t1.cPsn_Num like '#(keys)%' or t1.cPsn_Name like '%#(keys)%')
#end
order by t1.dUpdateTime
#end

#sql("getRole")
select id as iroleid,
name as rolename,
sn as rolesn
from #(getBaseDbName()).dbo.jb_role
where 1=1
#if(roleHidden)
    and id not in (#(roleHidden))
#end
#if(keywords)
and (sn like '#(keywords)%' or name like '%#(keywords)%')
#end
#end

#sql("lineDatas")
select t1.iAutoId, t1.iSeq, t.* from Bd_ApprovalD_User t1
join (
select t1.iAutoId as ipersonid, t1.cPsn_Num as cpsncode, t1.cPsn_Name as cpsnname, t2.cDepName as cdeptname, t1.iUserId as
iuserid, t3.username
from Bd_Person t1
###left join #(getBaseDbName()).dbo.jb_dept t2 on t2.sn = t1.cDept_num
left join Bd_Department t2 on t2.cDepCode = t1.cDept_num
left join #(getBaseDbName()).dbo.jb_user t3 on t3.id = t1.iUserId
where t1.isDeleted = '0' and t1.iUserId is not null
) t on t1.iPersonId = t.ipersonid
where t1.iApprovalDid = '#(mid)' order by t1.iSeq asc
#end

#sql("roleDatas")
select t1.iAutoId, t1.iSeq, t.* from Bd_ApprovalD_Role t1
left join (
select id as iroleid,
name as rolename,
sn as rolesn
from #(getBaseDbName()).dbo.jb_role
) t on t1.iRoleId = t.iroleid
where t1.iApprovalDid = '#(mid)'
#end

#sql("findUsersByDid")
select * from Bd_ApprovalD_User where iApprovalDid = #(did) order by iSeq asc
#end

#sql("findRolesByDid")
select * from Bd_ApprovalD_Role where iApprovalDid = #(did)
#end

#sql("roleUsers")
select t1.iAutoId, t1.iUserId, t3.name,t3.sex, t3.username, t3.enable from Bd_ApprovalD_RoleUsers t1
left join #(getBaseDbName()).dbo.jb_user t3 on t3.id = t1.iUserId
where t1.iApprovaldRoleId = '#(id)'
#end

#sql("chooseUsers")
select t1.id as iuserid, t1.sex, t1.name, t1.username, t1.enable from #(getBaseDbName()).dbo.jb_user t1 WHERE roles LIKE
CONCAT('%', #para(roleId), '%')
and not exists(select 1 from Bd_ApprovalD_RoleUsers t2 where t2.iApprovaldRoleId = #(autoId) and t1.id = t2.iUserId)
#if(itemHidden)
and t1.id not in (#(itemHidden))
#end
#end
