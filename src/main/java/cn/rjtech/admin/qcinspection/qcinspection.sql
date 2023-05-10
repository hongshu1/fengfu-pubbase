#sql("DepartmentList")
SELECT
    t1.iAutoId as iQcDutyPersonId,
    t1.cPsn_Name as PsnName,
    t2.iAutoId as iQcDutyDepartmentId,
    t2.cDepName as DepName
FROM Bd_Person t1
    LEFT JOIN  Bd_Department t2 ON t2.cDepCode = t1.cDept_num
WHERE t1.cOrgCode = #para(orgCode)
    #if(cus)
  and ( t1.cPsn_Name like CONCAT('%', #para(cus), '%') or t2.cDepName like CONCAT('%', #para(cus), '%'))
    #end

    #if(null !=orderByColumn)
order by #(orderByColumn) #(orderByType)
    #end
    #end


#sql("getQcInspectionList")
SELECT
    t1.*,
    (SELECT t2.cPsn_Name FROM Bd_Person t2 WHERE t2.iAutoId = t1.iQcDutyPersonId ) AS PsnName,
    (SELECT t3.cDepName FROM Bd_Department t3 WHERE t3.iAutoId = t1.iQcDutyDepartmentId ) AS DepName
FROM
    PL_QcInspection t1
    WHERE
    t1.iAutoId = #para(iautoid)
#end


    #sql("paginateAdminDatas")
SELECT
    t1.InspectionNo,
    t1.cChainNo,
    t1.cChainName,
    t1.iAutoId,
    t1.dRecordDate,
    isFirstCase =
    CASE WHEN t1.isFirstCase=0 THEN '再发'
         ELSE '首发' END,
    iEstimate =
    CASE WHEN t1.iEstimate=1 THEN 'OK'
         ELSE 'NG' END,
    t1.cUpdateName,
    t1.dUpdateTime
FROM
    PL_QcInspection t1
WHERE 1 = 1

    #if(selectparam)
    AND (t1.cChainNo LIKE CONCAT('%',#para(selectparam), '%')
    OR t1.cChainName LIKE CONCAT('%', #para(selectparam), '%')
    OR t1.InspectionNo LIKE CONCAT('%', #para(selectparam), '%'))
    #end
#if(starttime)
    and CONVERT(VARCHAR(10),t1.dUpdateTime,23) >='#(starttime)'
#end
#if(endtime)
    and CONVERT(VARCHAR(10),t1.dUpdateTime,23) <='#(endtime)'
#end
order by t1.dUpdateTime desc
    #end


#sql("getFilesById")
SELECT file_name as fileName,
       id,
       local_url as localNrl,
    local_path as localPath
FROM UGCFF_MOM_System.dbo.jb_jbolt_file
WHERE id IN (#(supplierInfoId))
#end