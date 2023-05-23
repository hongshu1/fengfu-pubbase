#sql("list")
SELECT *
FROM Bd_VendorClass t1
WHERE t1.isDeleted = '0'
  #if(iautoid)
  AND t1.iautoid =#para(iautoid)
  #end
  #if(cvccode)
  AND t1.cvccode = #para(cvccode)
  #end
  #if(cvcname)
  AND t1.cvcname = #para(cvcname)
  #end
ORDER BY t1.dUpdateTime
    desc
#end

#sql("getDataExport")
SELECT
    t1.cVCCode,
    t1.cVCName,
    t2.cVCCode as cVCCoded,
    t2.cVCName as cVCNamed
FROM
    Bd_VendorClass t1
        LEFT JOIN Bd_VendorClass t2 ON t1.iAutoId = t2.iPid
WHERE 1=1
#end

#sql("findRecordByCVCCode")
SELECT *
FROM Bd_VendorClass t1
WHERE t1.isDeleted = '0'
  #if(cvccode)
  AND t1.cvccode = #para(cvccode)
  #end
#end

#sql("getSubList")
SELECT *
FROM VendorClass
WHERE iVCGrade = #para(ivcgrade)
#if(ivcgrade > 1)
    AND cvccode LIKE concat(#para(cvccode),'%')
#end
ORDER BY
    cvccode
#end