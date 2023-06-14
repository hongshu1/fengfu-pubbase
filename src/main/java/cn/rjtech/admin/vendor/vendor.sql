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
#if(corgcode)
  and v.corgcode = #para(corgcode)
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
WHERE 1=1 and isdeleted = 0 and isenabled = 1
    #if(q)
        AND (
            cvencode LIKE CONCAT('%', #para(q), '%') OR
            cvenname LIKE CONCAT('%', #para(q), '%')
        )
    #end
    #if(corgcode)
        and corgcode = #para(corgcode)
    #end
#end

#sql ("getVendorList")
SELECT 
#if(limit)
 top #(limit)
#end
v.*
FROM bd_Vendor v
WHERE  1 = 1
#if(iorgid)
	and v.iorgid = #para(iorgid)
#end
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
    #if(corgcode)
        and v.corgcode = #para(corgcode)
    #end
#end

#sql("getAdminDatas")
select * from bd_Vendor t1
where 1=1 and isdeleted = 0
#if(iventorclassid)
and t1.iVendorClassId = #para(iventorclassid)
#end
#if(cvenname)
    and t1.cvenname = #para(cvenname)
#end
#if(cvencode)
    and t1.cvencode = #para(cvencode)
#end
#if(corgcode)
    and t1.corgcode = #para(corgcode)
#end
order by t1.dUpdateTime desc
#end