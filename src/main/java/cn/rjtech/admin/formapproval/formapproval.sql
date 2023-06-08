#sql("getDeptDataTree")
with T as (
    select iDutyUserId, iPid,cDepCode from Bd_Department where cDepCode = '#(dept)'
    UNION ALL
    SELECT a.iDutyUserId, a.iPid,a.cDepCode FROM Bd_Department a inner join T on T.iPid = a.iAutoId
)
select * from T
#end

#sql("getSuccessUser")
select *
from Bd_FormApprovalFlowD t1
where iFormApprovalFlowMid in (select iAutoId
 from Bd_FormApprovalFlowM where iApprovalId = '#(Mid)') and t1.iAuditStatus = 2 order by iSeq asc
#end


#sql("approvalProcessUsers")
select top #(size) name
from UGCFF_MOM_System.dbo.jb_user where id in
    (select iUserId
     from Bd_FormApprovalFlowD where iFormApprovalFlowMid =
         (select Bd_FormApprovalFlowM.iAutoId
          from Bd_FormApprovalFlowM where iApprovalDid =
              (select top 1 Bd_FormApprovalD.iAutoId
               from Bd_FormApprovalD where iFormApprovalId = (
                   select Bd_FormApproval.iAutoId
                   from Bd_FormApproval where iFormObjectId = '#(formAutoId)' and isDeleted = '0'
               ) and iStatus = 1 order by iSeq desc)))
#end
