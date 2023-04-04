#sql("list")
SELECT *
FROM Bd_QcItem
WHERE isDeleted = '0'
  #if(iautoid)
  AND iautoid =#para(iautoid)
  #end
  #if(cqcitemcode)
  AND cqcitemcode = #para(cqcitemcode)
  #end
  #if(cqcitemname)
  AND cqcitemname = #para(cqcitemname)
  #end
#end