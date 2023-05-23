
#sql("paginateAdminDatas")
SELECT
      t1.*,
      t4.cDepName
FROM
    T_Sys_OtherOut t1
    LEFT JOIN Bd_Department t4 ON t4.iAutoId = t1.DeptCode
WHERE 1 = 1
    AND t1.Type = 'OtherOutMES'
    AND t1.Status
    #if(selectparam)
    AND (t1.BillNo LIKE CONCAT('%',#para(selectparam), '%')
    OR t1.SourceBillType LIKE CONCAT('%', #para(selectparam), '%')
    OR  t4.cDepName LIKE CONCAT('%', #para(selectparam), '%')
    )
    #end
   #if(iorderstatus)
        AND t1.Status = #para(iorderstatus)
    #end
#if(startdate)
    and CONVERT(VARCHAR(10),t1.ModifyDate,23) >='#(startdate)'
#end
#if(enddate)
    and CONVERT(VARCHAR(10),t1.ModifyDate,23) <='#(enddate)'
#end
order by t1.ModifyDate desc
    #end

