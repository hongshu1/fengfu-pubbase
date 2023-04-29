#sql("paginateAdminDatas")
SELECT
    t1.iAutoId AS iIssueId,
    t1.iMoDocId ,
    t1.iDepartmentId,
    t1.cSpecRcvDocNo,
    t1.dDemandDate,
    t2.iStatus,
    t2.iAutoId,
    t2.cDocNo,
    t2.ProcessName,
    t2.iDqQty,
    t2.cApproach,
    t2.cCreateName,
    t2.dCreateTime
FROM
    Mo_SpecMaterialsRcvM t1
    LEFT JOIN Mo_ProcessDefect t2 ON t2.iIssueId = t1.iAutoId
WHERE 1 = 1
    #if(cDocNo)
  AND t2.cDocNo like '%#(cDocNo)%'
  #end
    #if(iMoDocId)
  AND t1.cSpecRcvDocNo like '%#(iMoDocId)%'
  #end
#if(iRcvDocQcFormMid)
  AND t2.iRcvDocQcFormMid like '%#(iRcvDocQcFormMid)%'
  #end
#if(cInvCode)
  AND t3.cInvCode like '%#(cInvCode)%'
  #end
#if(cInvCode1)
  AND t3.cInvCode1 like '%#(cInvCode1)%'
  #end
#if(cInvName)
  AND t3.cInvName like '%#(cInvName)%'
  #end
  #if(iStatus != '0' && iStatus)
  AND t2.iStatus = '#(iStatus)'
  #end
  #if(iStatus == '0' && iStatus)
  AND t2.iStatus  is null
  #end

#if(startdate)
    and CONVERT(VARCHAR(10),t2.dUpdateTime,23) >='#(startdate)'
#end
#if(enddate)
    and CONVERT(VARCHAR(10),t2.dUpdateTime,23) <='#(enddate)'
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


