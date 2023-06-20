#sql("getDeptDataTree")
with T as (
    select cDepPerson, iPid,cDepCode from Bd_Department where cDepCode = '#(dept)'
    UNION ALL
    SELECT a.cDepPerson, a.iPid,a.cDepCode FROM Bd_Department a inner join T on T.iPid = a.iAutoId
)
select * from T
#end

#sql("getDeptDataTree_bak_2023_06_16")
with T as (
    select iDutyUserId, iPid,cDepCode from Bd_Department where cDepCode = '#(dept)'
    UNION ALL
    SELECT a.iDutyUserId, a.iPid,a.cDepCode FROM Bd_Department a inner join T on T.iPid = a.iAutoId
)
select * from T
#end

#sql("getSuccessUser")
SELECT *
FROM Bd_FormApprovalFlowD t1
WHERE iFormApprovalFlowMid IN (
        SELECT iAutoId
        FROM Bd_FormApprovalFlowM
        WHERE iApprovalId = '#(Mid)'
    ) AND t1.iAuditStatus = 2
ORDER BY iSeq ASC
#end

#sql("approvalProcessUsers")
select top #(size) name
from #(getBaseDbName()).dbo.jb_user
where id in
    (select iUserId
     from Bd_FormApprovalFlowD where iFormApprovalFlowMid =
         (select Bd_FormApprovalFlowM.iAutoId
          from Bd_FormApprovalFlowM where iApprovalDid =
              (select top 1 Bd_FormApprovalD.iAutoId
               from Bd_FormApprovalD where iFormApprovalId = (
                   select Bd_FormApproval.iAutoId
                   from Bd_FormApproval where iFormObjectId = '#(formAutoId)' and isDeleted = '0'
               ) and iStatus = 1 order by iSeq asc )) and iAuditStatus = 1)
#end


#sql("revocationApprove")
select * from Bd_FormApprovalFlowD t1 where iFormApprovalFlowMid in (
    select t2.iAutoId from Bd_FormApprovalFlowM t2 where t2.iApprovalDid in (
        select t3.iAutoId from Bd_FormApprovalD t3 where t3.iFormApprovalId = (
            select t4.iAutoId from Bd_FormApproval t4 where iFormObjectId = '#(formAutoId)' and t4.isDeleted = '0'
            )
        )
    ) and iAuditStatus > 1
#end

#sql("isFirstReverse")
select *
from Bd_FormApprovalFlowD t1
where iFormApprovalFlowMid in (
    select t2.iAutoId
    from Bd_FormApprovalFlowM t2
    where t2.iApprovalDid in (
        select t3.iAutoId
        from Bd_FormApprovalD t3
        where t3.iFormApprovalId = (
            select t4.iAutoId from Bd_FormApproval t4 where iFormObjectId = '#(formId)' and t4.isDeleted = '0'
        )
    )
) and t1.iAuditStatus > 1
#end


#sql("findPersonByUserId")
select t1.*
from Bd_Person t1
left join (
    select g.iPersonId, u.id
    from #(getBaseDbName()).dbo.jb_user u
    left join #(getBaseDbName()).dbo.base_user_org g on u.id = g.user_id and g.org_id = #(orgId)
) t3 on t3.iPersonId = t1.iAutoId
where t3.id = #para(userId)
#end
