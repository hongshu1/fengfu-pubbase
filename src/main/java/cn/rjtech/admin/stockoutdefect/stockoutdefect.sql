#sql("paginateAdminDatas")
SELECT
    t1.iAutoId AS iStockoutQcFormMid,
    t1.cStockoutQcFormNo,
    t1.iInventoryId,
    t1.iCustomerId,
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
    PL_StockoutQcFormM t1
        LEFT JOIN PL_StockoutDefect t2 ON t2.iStockoutQcFormMid = t1.iAutoId
        LEFT JOIN Bd_Inventory t3 ON t3.iAutoId = t1.iInventoryId
WHERE
        1 = 1
    #if(cDocNo)
  AND t2.cDocNo like '%#(cDocNo)%'
  #end
    #if(iMoDocId)
  AND t1.cStockoutQcFormNo like '%#(iMoDocId)%'
  #end
#if(iStockoutQcFormMid)
  AND t2.iStockoutQcFormMid like '%#(iStockoutQcFormMid)%'
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


