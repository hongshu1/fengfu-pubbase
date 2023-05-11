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
