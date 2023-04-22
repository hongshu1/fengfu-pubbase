#sql("paginateAdminDatas")
SELECT
    t1.iAutoId AS iIssueId,
    t1.iMoDocId ,
    t1.iDepartmentId,
    t1.cSpecRcvDocNo,
    t1.dDemandDate,
    t2.iStatus
FROM
    Mo_SpecMaterialsRcvM t1
    LEFT JOIN Mo_ProcessDefect t2 ON t2.iIssueId = t1.iAutoId
WHERE 1 = 1
  #if(cDocNo)
  AND cDocNo like '%#(cDocNo)%'
  #end
#if(iRcvDocQcFormMid)
  AND iRcvDocQcFormMid like '%#(iRcvDocQcFormMid)%'
  #end
#if(cInvCode)
  AND cInvCode like '%#(cInvCode)%'
  #end
#if(iInventoryId)
  AND iInventoryId like '%#(iInventoryId)%'
  #end
#if(cInvName)
  AND cInvName like '%#(cInvName)%'
  #end
  #if(iStatus)
  AND iStatus like '%#(iStatus)%'
  #end

#if(startdate)
    and CONVERT(VARCHAR(10),dQcTime,23) >='#(startdate)'
#end
#if(enddate)
    and CONVERT(VARCHAR(10),dQcTime,23) <='#(enddate)'
#end
order by t2.dUpdateTime desc
#end



    #sql("AdminDatas")
SELECT *
FROM Bd_QcForm
WHERE 1 = 1
    #if(cQcFormName)
  AND cQcFormName like '%#(cQcFormName)%'
  #end
order by dUpdateTime desc
    #end


