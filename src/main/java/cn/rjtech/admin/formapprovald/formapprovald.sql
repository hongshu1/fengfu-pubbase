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

#sql("findRecordsByFormIdHistory")
select t1.*,
 case when iStatus=2 then '已通过' when iStatus=3 then '不通过' else '待审批' end as status,
 t2.name as stepname,t3.name as way from Bd_FormApprovalD t1
 left join #(getBaseDbName()).dbo.jb_dictionary t2 on t2.type_key = 'approval_d_name' and t2.sn = t1.iStep
 left join #(getBaseDbName()).dbo.jb_dictionary t3 on t3.type_key = 'approval_person_config' and t3.sn = t1.iType
 where iFormApprovalId = '#(approvalId)'
  order by
 iSeq asc
#end

#sql("historyDatas")
select t.*,
       case
           when t.t_iStatus = 2 then '审批通过'
           when t.t_iStatus = 3 then '审批不通过'
           else '待审批' end         as iStatus,
       t2.name                    as iStepName,
       (select top 1 u.name
        from Bd_FormApprovalFlowD d
                 left join Bd_FormApprovalFlowM m on d.iFormApprovalFlowMid = m.iAutoId
                 left join UGCFF_MOM_System.dbo.jb_user u on d.iUserId = u.id
        where m.iApprovalDid = t.did
          and d.iAuditStatus > 1) as username
from (select t1.iAutoId,
             (select top 1 t2.iStep
              from Bd_FormApprovalD t2
              where t1.iAutoId = t2.iFormApprovalId
              order by dAuditTime
                  desc)                 as t_iStep,
             (select top 1 t2.iAutoId
              from Bd_FormApprovalD t2
              where t1.iAutoId = t2.iFormApprovalId
              order by dAuditTime
                  desc)                 as did,
             (select top 1 t2.iStatus
              from Bd_FormApprovalD t2
              where t1.iAutoId = t2.iFormApprovalId
              order by dAuditTime
                  desc)                 as t_iStatus,
             (select top 1 t2.dAuditTime
              from Bd_FormApprovalD t2
              where t1.iAutoId = t2.iFormApprovalId
              order by dAuditTime desc) as dAuditTime,
             (select top 1 t2.iStep
              from Bd_FormApprovalD t2
              where t1.iAutoId = t2.iFormApprovalId
              order by dAuditTime desc) as iStep
      from Bd_FormApproval t1
      where t1.iFormObjectId = '#(formId)'
        and t1.isDeleted = '1') t
         left join UGCFF_MOM_System.dbo.jb_dictionary t2
                   on t2.type_key = 'approval_d_name' and t2.sn = t.t_iStep
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
select t1.iAutoId as ipersonid, t1.cPsn_Num as cpsncode, t1.cPsn_Name as cpsnname, t2.cDepName as cdeptname, t1.iUserId as
iuserid, t3.username
from Bd_Person t1
###left join #(getBaseDbName()).dbo.jb_dept t2 on t2.sn = t1.cDept_num
left join Bd_Department t2 on t2.cDepCode = t1.cDept_num
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

#sql("chooseUsers")
select t1.id as iuserid, t1.sex, t1.name, t1.username, t1.enable from #(getBaseDbName()).dbo.jb_user t1 WHERE roles LIKE
CONCAT('%', #para(roleId), '%')
and not exists(select 1 from Bd_FormApprovalD_RoleUsers t2 where t2.iFormApprovaldRoleId = #(autoId) and t1.id = t2.iUserId)
#if(itemHidden)
and t1.id not in (#(itemHidden))
#end
#end

#sql("roleUsers")
select t1.iAutoId, t1.iUserId, t3.name,t3.sex, t3.username, t3.enable from Bd_FormApprovalD_RoleUsers t1
left join #(getBaseDbName()).dbo.jb_user t3 on t3.id = t1.iUserId
where t1.iFormApprovaldRoleId = '#(id)'
#end
