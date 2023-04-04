#sql("list")
SELECT t1.*, t2.cQcItemName
FROM Bd_QcParam t1
         left join Bd_QcItem t2 on t1.iQcItemId = t2.iAutoId
WHERE t1.isDeleted = '0'
  #if(iautoid)
  AND t1.iautoid =#para(iautoid)
  #end
  #if(cqcparamname)
  AND t1.cqcparamname = #para(cqcparamname)
  #end
  #if(isenabled)
  AND t1.isenabled = #para(isenabled == 'true' ? 1 : 0)
#end
ORDER BY t1.dUpdateTime
    DESC
#end