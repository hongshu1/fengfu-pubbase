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
    t2.dUpdateTime,
    t3.cInvCode,
    t3.cInvName,
    t3.cInvCode1
FROM
    PL_RcvDocQcFormM t1
        LEFT JOIN PL_RcvDocDefect t2 ON t2.iRcvDocQcFormMid = t1.iAutoId
        LEFT JOIN Bd_Inventory t3 ON t3.iAutoId = t1.iInventoryId
WHERE
        1 = 1
    #if(cdocno)
  AND t2.cDocNo like '%#(cdocno)%'
  #end
    #if(imodocid)
  AND t1.cRcvDocQcFormNo like '%#(imodocid)%'
  #end
#if(cinvcode)
  AND t3.cInvCode like '%#(cinvcode)%'
  #end
#if(cinvcode1)
  AND t3.cInvCode1 like '%#(cinvcode1)%'
  #end
#if(cinvname)
  AND t3.cInvName like '%#(cinvname)%'
  #end
  #if(istatus != '0' && istatus)
  AND t2.iStatus = '#(istatus)'
  #end
  #if(istatus == '0' && istatus)
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


