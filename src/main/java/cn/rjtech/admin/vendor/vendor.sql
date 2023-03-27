#sql("list")
SELECT *
FROM Bd_Vendor v
WHERE v.isDeleted = '0'
  #if(iautoid)
  AND v.iautoid =#para(iautoid)
  #end
  #if(cvencode)
  AND v.cvencode = #para(cvencode)
  #end
  #if(cvenname)
  AND v.cvenname = #para(cvenname)
  #end
  #if(isenabled)
  AND v.isenabled = #para(isenabled)
    #end
ORDER BY v.dUpdateTime
    DESC
#end

#sql("findColumns")
SELECT *
FROM Bd_Vendor v
WHERE v.isDeleted = '0'
  #if(isenabled)
  AND v.isenabled = #para(isenabled)
  #end
#end