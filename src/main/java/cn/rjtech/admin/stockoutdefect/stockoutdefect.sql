#sql("paginateAdminDatas")
SELECT
    t1.iAutoId AS stockoutQcFormMid,
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
    #if(cdocno)
  AND t2.cDocNo like '%#(cdocno)%'
  #end
    #if(imodocid)
  AND t1.cStockoutQcFormNo like '%#(imodocid)%'
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

    #sql("paginateAdminDatasapi")
SELECT
        AuditState =
        CASE WHEN t2.iStatus=1 THEN '待判断'
             WHEN t2.iStatus=2 THEN '已完成'
             ELSE '待记录' END,
        t1.iAutoId AS stockoutqcformmid,
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
WHERE 1 = 1

    #if(cdocno)
  AND t2.cDocNo like '%#(cdocno)%'
  #end
    #if(imodocid)
  AND t1.cStockoutQcFormNo like '%#(imodocid)%'
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

#sql("getstockoutQcFormMList")
select t1.*, t2.cInvCode, t2.cInvName, t2.cInvCode1, t3.cCusName
from PL_StockoutQcFormM t1
         LEFT JOIN Bd_Inventory t2 ON t2.iAutoId = t1.iInventoryId
         LEFT JOIN Bd_Customer t3 ON t3.iAutoId = t1.iCustomerId
where t1.iAutoId = '#(iautoid)'
    #end


#sql("containerPrintData")
SELECT t1.* FROM PL_StockoutDefect t1 WHERE 1=1
    #if(ids)
      AND t1.iAutoId  in (#(ids))
    #end
#end