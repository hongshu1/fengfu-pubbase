#sql("paginateAdminDatas")
SELECT
    t1.iAutoId AS iIssueId,
    t1.iMoDocId ,
    t1.iDepartmentId,
    t1.cSpecRcvDocNo,
    t2.dDemandDate, ### 需求日期
    t2.iStatus,
    t2.iAutoId,
    t2.cDocNo,
    o.cOperationName as ProcessName, ###工序名称
    t2.iDqQty,
    t2.cApproach,
    t2.cCreateName,
    t2.dCreateTime,
    m.cMoDocNo, ### 工单号
    bi.cInvCode, ### 存货编码
    bi.cInvCode1, ### 客户部番
    bi.cInvName1, ### 部品名称
    bd.cDepName  ### 生产部门
FROM
    Mo_ProcessDefect t2
    LEFT  JOIN Mo_MoDoc m on t2.imodocid=m.iAutoId
    LEFT JOIN Mo_SpecMaterialsRcvM t1 ON t2.iIssueId = t1.iAutoId
    LEFT  JOIN  Bd_Operation  o on t2.iOperationId=o.iAutoId
    LEFT  JOIN  Bd_Inventory bi on  t2.iInventoryId = bi.iAutoId
    LEFT JOIN  Bd_Department bd ON t2.iDepartmentId = bd.iAutoId
WHERE 1 = 1 and t2.IsDeleted=0
    #if(cdocno)
  AND t2.cDocNo like '%#(cdocno)%'
  #end
    #if(imodocid)
  AND t2.iMoDocId=#para(imodocid) ###工单ID
  #end
    #if(cmodocno)
  AND m.cMoDocNo LIKE CONCAT ( '%', #para(cmodocno), '%' )  ###工单号
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
    and CONVERT(VARCHAR(10),t2.dCreateTime,23) >='#(startdate)'
#end
#if(enddate)
    and CONVERT(VARCHAR(10),t2.dCreateTime,23) <='#(enddate)'
#end
#if(idepartmentid)
  AND  m.iDepartmentId =#para(idepartmentid)
    #end
order by t2.dCreateTime desc,t2.iAutoId desc
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
    t2.iDqQty,
    t2.cApproach,
    t2.cCreateName,
    t2.dCreateTime,
    o.cOperationName AS ProcessName,###工序名称
    m.cMoDocNo,
    bi.cInvCode,
    bi.cInvCode1,
    bi.cInvName1,
    bd.cDepName
FROM
    Mo_ProcessDefect t2
        LEFT JOIN Mo_MoDoc m ON t2.imodocid= m.iAutoId
        LEFT JOIN Mo_SpecMaterialsRcvM t1 ON t2.iIssueId = t1.iAutoId
        LEFT JOIN Bd_Operation o ON t2.iOperationId= o.iAutoId
        LEFT JOIN Bd_Inventory bi ON t2.iInventoryId = bi.iAutoId
        LEFT JOIN Bd_Department bd ON t2.iDepartmentId = bd.iAutoId
WHERE 1 = 1
  AND t2.IsDeleted= 0

    #if(cdocno)
  AND t2.cDocNo like '%#(cdocno)%'
  #end
    #if(imodocid)
  AND t2.iModocId like '%#(imodocid)%'
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
#sql("OneMaterialTitle")
#end

#sql("OperationDatas")
SELECT iAutoId, cOperationName
FROM Bd_Operation
WHERE 1 = 1
and isDeleted = 0
and cOrgCode = #(OrgCode)
    #end