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