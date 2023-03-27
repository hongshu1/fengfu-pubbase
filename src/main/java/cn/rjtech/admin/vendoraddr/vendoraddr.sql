#sql("list")
SELECT t1.*,t2.*
FROM Bd_VendorAddr t1
LEFT JOIN Bd_Vendor t2 ON t1.iCustomerId = t2.iAutoId
WHERE t1.isDeleted = '0'
  #if(iautoid)
  AND t1.iautoid =#para(iautoid)
  #end
  #if(cdistrictcode)
  AND t1.cdistrictcode = #para(cdistrictcode)
  #end
  #if(cdistrictname)
  AND t1.cdistrictname = #para(cdistrictname)
  #end
  #if(icustomerid)
  AND t1.icustomerid = #para(icustomerid)
  #end
  #if(isenabled)
  AND t1.isenabled = #para(isenabled)
  #end
ORDER BY t1.iautoid
    DESC
#end