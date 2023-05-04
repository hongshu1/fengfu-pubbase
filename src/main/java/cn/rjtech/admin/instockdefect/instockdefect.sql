#sql("paginateAdminDatas")
SELECT
    t1.iAutoId AS iInStockQcFormMid,
    t1.cInvQcFormNo,
    t1.iInventoryId,
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
    PL_InStockQcFormM t1
        LEFT JOIN PL_InStockDefect t2 ON t2.iInStockQcFormMid = t1.iAutoId
        LEFT JOIN Bd_Inventory t3 ON t3.iAutoId = t1.iInventoryId
WHERE
        1 = 1
    #if(cDocNo)
  AND t2.cDocNo like '%#(cDocNo)%'
  #end
#if(iMoDocId)
  AND  t1.cInvQcFormNo like '%#(iMoDocId)%'
  #end
#if(cInvCode1)
  AND t3.cInvCode1 like '%#(cInvCode1)%'
  #end
#if(iInventoryId)
  AND t3.cInvCode like '%#(cInvCode)%'
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

#sql("getinStockQcFormMList")
select t1.*, t2.cInvCode, t2.cInvName, t2.cInvCode1
  from PL_InStockQcFormM t1
           LEFT JOIN Bd_Inventory t2 ON t2.iAutoId = t1.iInventoryId
  where t1.iAutoId = '#(iautoid)'
#end

#sql("paginateAdminDatasapi")
SELECT
        AuditState =
        CASE WHEN t2.iStatus=1 THEN '待判断'
             WHEN t2.iStatus=2 THEN '已完成'
             ELSE '待记录' END,
        t1.iAutoId AS iInStockQcFormMid,
        t1.cInvQcFormNo,
        t1.iInventoryId,
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
    PL_InStockQcFormM t1
        LEFT JOIN PL_InStockDefect t2 ON t2.iInStockQcFormMid = t1.iAutoId
        LEFT JOIN Bd_Inventory t3 ON t3.iAutoId = t1.iInventoryId
WHERE
        1 = 1
    #if(selectparam)
    AND (t2.cDocNo LIKE CONCAT('%',#para(selectparam), '%')
    OR t1.cInvQcFormNo LIKE CONCAT('%', #para(selectparam), '%')
    OR t3.cInvCode1 LIKE CONCAT('%', #para(selectparam), '%')
    OR t3.cInvCode LIKE CONCAT('%',#para(selectparam), '%')
    OR t3.cInvName LIKE CONCAT('%', #para(selectparam), '%'))
    #end
#if(startdate)
    and CONVERT(VARCHAR(10),t2.dUpdateTime,23) >='#(startdate)'
#end
#if(enddate)
    and CONVERT(VARCHAR(10),t2.dUpdateTime,23) <='#(enddate)'
#end
ORDER BY
    t2.dUpdateTime DESC
#end