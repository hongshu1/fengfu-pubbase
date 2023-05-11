#sql("findRecordsByFormId")
select t1.*,
 case when iStatus=2 then '已通过' when iStatus=3 then '不通过' else '待审批' end as status,
 t2.name as stepname,t3.name as way from Bd_FormApprovalD t1
 left join #(getBaseDbName()).dbo.jb_dictionary t2 on t2.type_key = 'approval_d_name' and t2.sn = t1.iStep
 left join #(getBaseDbName()).dbo.jb_dictionary t3 on t3.type_key = 'approval_person_config' and t3.sn = t1.iType
 where iFormApprovalId = (select top 1 iAutoId from Bd_FormApproval where iFormObjectId = '#(formId)' and isDeleted =
  '0')
  order by
 iSeq asc
#end

#sql("userDatas")
select t1.*,t2.username,t2.name,
  case when t1.iAuditStatus=2 then '已通过' when t1.iAuditStatus=3 then '不通过' else '待审批' end as status from
  Bd_FormApprovalFlowD t1
  left join #(getBaseDbName()).dbo.jb_user t2 on t2.id = t1.iUserId
  where iFormApprovalFlowMid = (
select iAutoId from Bd_FormApprovalFlowM where iApprovalDid = '#(approvalDId)'
)
#end

#sql("lineDatas")
select t1.iAutoId, t1.iSeq, t.* from Bd_FormApprovalD_User t1
join (
select t1.iAutoId as ipersonid, t1.cPsn_Num as cpsncode, t1.cPsn_Name as cpsnname, t2.name as cdeptname, t1.iUserId as
iuserid, t3.username
from Bd_Person t1
left join #(getBaseDbName()).dbo.jb_dept t2 on t2.sn = t1.cDept_num
left join #(getBaseDbName()).dbo.jb_user t3 on t3.id = t1.iUserId
where t1.isDeleted = '0' and t1.iUserId is not null
) t on t1.iPersonId = t.ipersonid
where t1.iFormApprovalDid = '#(mid)'
#end

#sql("roleDatas")
select t1.iAutoId, t1.iSeq, t.* from Bd_FormApprovalD_Role t1
left join (
select id as iroleid,
name as rolename,
sn as rolesn
from #(getBaseDbName()).dbo.jb_role
) t on t1.iRoleId = t.iroleid
where t1.iFormApprovalDid = '#(mid)'
#end
