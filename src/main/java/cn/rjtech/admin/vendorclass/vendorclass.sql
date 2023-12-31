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
FROM Bd_VendorClass
WHERE 1=1
#if(iorgid)
	and iorgid = #para(iorgid)
#end
#if(pid > 0)
	and ipid = #para(pid)
#else
	and (ipid is null or ipid =0)
#end
ORDER BY
    cvccode
#end