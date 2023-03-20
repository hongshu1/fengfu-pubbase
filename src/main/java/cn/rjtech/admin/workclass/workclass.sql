#sql("list")
SELECT *
FROM Bd_WorkClass
WHERE isDeleted = '0'
  #if(iautoid)
  AND iautoid =#para(iautoid)
  #end
  #if(cworkclasscode)
  AND cworkclasscode = #para(cworkclasscode)
  #end
  #if(cworkclassname)
  AND cworkclassname = #para(cworkclassname)
  #end
  #if(ilevel)
  AND ilevel = #para(ilevel)
  #end
  #if(isenabled)
  AND isenabled = #para(isenabled == 'true' ? 1 : 0)
  #end
ORDER BY dUpdateTime
DESC
#end