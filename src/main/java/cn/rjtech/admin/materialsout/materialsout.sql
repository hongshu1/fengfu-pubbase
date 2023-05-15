#sql("paginateAdminDatas")
SELECT
    t1.*
FROM
    T_Sys_MaterialsOut t1
WHERE 1 = 1

    #if(selectparam)
    AND (t1.BillNo LIKE CONCAT('%',#para(selectparam), '%')
    OR t1.Whcode LIKE CONCAT('%', #para(selectparam), '%')
    OR t1.SourceBillDid LIKE CONCAT('%', #para(selectparam), '%')
    OR (SELECT cDepName FROM Bd_Department WHERE cDepCode = #para(selectparam)) LIKE CONCAT('%', #para(selectparam), '%')
    )
    #end
   #if(iorderstatus)
        AND t1.State = #para(iorderstatus)
    #end
#if(startdate)
    and CONVERT(VARCHAR(10),t1.ModifyDate,23) >='#(startdate)'
#end
#if(enddate)
    and CONVERT(VARCHAR(10),t1.ModifyDate,23) <='#(enddate)'
#end
order by t1.ModifyDate desc
    #end

