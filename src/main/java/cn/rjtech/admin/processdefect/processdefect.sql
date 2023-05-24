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
    #if(cdocno)
  AND t2.cDocNo like '%#(cdocno)%'
  #end
    #if(imodocid)
  AND t1.cSpecRcvDocNo like '%#(imodocid)%'
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

    #if(cdocno)
  AND t2.cDocNo like '%#(cdocno)%'
  #end
    #if(imodocid)
  AND t1.cSpecRcvDocNo like '%#(imodocid)%'
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
SELECT t1.* FROM Mo_ProcessDefect t1 WHERE 1=1
    #if(ids)
      AND t1.iAutoId  in (#(ids))
    #end
#end
