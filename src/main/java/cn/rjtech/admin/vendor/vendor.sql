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
  AND v.isenabled = #para(isenabled == 'true' ? 1 : 0)
  #end
  #if(ivendorclassid)
  AND v.ivendorclassid = #para(ivendorclassid)
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

#sql("getAutocompleteList")
SELECT TOP #(limit) *
FROM Bd_Vendor
WHERE 1=1 and isdeleted = 0 and isenabled = 0
    #if(q)
        AND (
            cvencode LIKE CONCAT('%', #para(q), '%') OR
            cvenname LIKE CONCAT('%', #para(q), '%')
        )
    #end
#end

#sql ("getVendorList")
SELECT 
#if(limit)
 top #(limit)
#end
v.*
FROM Vendor v
WHERE  1 = 1
    #if(q)
        AND  (
            v.cVenCode like CONCAT ('%', #para(q), '%') OR
            v.cVenName like CONCAT ('%', #para(q), '%')
        )
    #end
    #if(cvencode)
        AND v.cVenCode = #para(cvencode)
    #end
    #if(cvccode)
        AND v.cvccode = #para(cvccode)
    #end
#end