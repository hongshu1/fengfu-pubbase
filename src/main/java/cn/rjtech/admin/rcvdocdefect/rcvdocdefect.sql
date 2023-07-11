#sql("paginateAdminDatas")
SELECT
        AuditState =
        CASE WHEN t2.iStatus=1 THEN '待记录'
             WHEN t2.iStatus=2 THEN '待判断'
             WHEN t2.iStatus=3 THEN '已完成' END,
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
    ( SELECT usr.name FROM UGCFF_MOM_System.dbo.jb_user usr WHERE usr.id = t2.iQcUserId ) AS dQcName,
    t2.dQcTime,
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
    and CONVERT(VARCHAR(10),t2.dQcTime,23) >='#(startdate)'
#end
#if(enddate)
    and CONVERT(VARCHAR(10),t2.dQcTime,23) <='#(enddate)'
#end
order by t2.dQcTime desc
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

#sql("getrcvDocQcFormList")
select t1.*, t2.cInvCode, t2.cInvName, t2.cInvCode1, t3.cVenName
from PL_RcvDocQcFormM t1
         LEFT JOIN Bd_Inventory t2 ON t2.iAutoId = t1.iInventoryId
         LEFT JOIN Bd_Vendor t3 ON t3.iAutoId = t1.iVendorId
where t1.iAutoId = '#(iautoid)'
#end


#sql("paginateAdminDatasapi")
SELECT
        AuditState =
        CASE WHEN t2.iStatus=1 THEN '待判断'
             WHEN t2.iStatus=2 THEN '已完成'
             ELSE '待记录' END,
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


#sql("containerPrintData")
SELECT t1.* FROM PL_RcvDocDefect t1 WHERE 1=1
    #if(ids)
      AND t1.iAutoId  in (#(ids))
    #end
#end