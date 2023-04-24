#sql("paginateAdminDatas")
SELECT
    t1.iAutoId AS iRcvDocQcFormMid,
    t1.cRcvDocQcFormNo,
    t1.iInventoryId,
    t1.iVendorId,
    t2.iStatus,
    t2.iAutoId,
    t2.cDocNo,
    t2.iDqQty,
    t2.cApproach,
    t2.cUpdateName,
    t2.dUpdateTime
FROM
    PL_RcvDocQcFormM t1
        LEFT JOIN PL_RcvDocDefect t2 ON t2.iRcvDocQcFormMid = t1.iAutoId
WHERE
        1 = 1
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
order by dUpdateTime desc
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


