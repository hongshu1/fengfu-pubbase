#sql("workRegionMList")
SELECT
    t1.cWorkCode,
    t1.cWorkName
FROM  Bd_WorkRegionM t1
WHERE t1.cOrgCode = #para(orgCode)
    #if(cus)
  and ( t1.cWorkCode like CONCAT('%', #para(cus), '%') or t1.cWorkName like CONCAT('%', #para(cus), '%'))
    #end

    #if(null !=orderByColumn)
order by #(orderByColumn) #(orderByType)
    #end
    #end