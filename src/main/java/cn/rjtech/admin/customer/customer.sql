#sql("paginateAdminDatas")
SELECT
	a.*,
	b.cCCName AS className,
	b.cCCCode AS classCode,
	dic.name AS dicname,
	p.cPsn_Name as personName,
	jd.name AS DepName
FROM
	Bd_Customer a
	LEFT JOIN Bd_CustomerClass b ON a.iCustomerClassId = b.iAutoId
	LEFT JOIN #(getBaseDbName()).dbo.jb_dictionary dic ON dic.type_key = 'customer_level'
	AND dic.sn = a.cCustomerLevelSn
	LEFT JOIN Bd_Person p ON a.iDutyUserId = p.iAutoId
	LEFT JOIN #(getBaseDbName()).dbo.jb_dept jd ON a.cCusDepart = jd.id
	WHERE 1=1
	#if(orgId)
	    AND a.iOrgId = #para(orgId)
	#end
	#if(icustomerclassid)
        AND a.iCustomerClassId = #para(icustomerclassid)
	#end
	#if(ccustomercode)
	    AND a.cCusCode LIKE CONCAT('%', #para(ccustomercode), '%')
	#end
	#if(ccustomername)
	    AND a.cCusName LIKE CONCAT('%', #para(ccustomername), '%')
	#end
    #if(ccustomerlevelsn)
        AND a.cCustomerLevelSn = #para(ccustomerlevelsn)
    #end
    #if(isenabled)
        AND a.isEnabled = #para(isenabled)
    #end
    #if(isdeleted)
        AND a.isDeleted = #para(isdeleted)
    #end
    #if(ids)
      AND CHARINDEX(','+cast((select a.iAutoId) as nvarchar(20))+',' , ','+#para(ids)+',') > 0
    #end
    ORDER BY dUpdateTime DESC
#end


#sql("findVendor")
SELECT
    A.iAutoId as cvenid,
    A.cVenCode as cvencode,
    A.cVenName as cvenname
FROM
    Bd_Vendor A
WHERE
    A.isEnabled = '1'
    AND A.isDeleted = '0'
    #if(keywords)
        AND (A.cVenCode LIKE CONCAT('%', #para(keywords),'%') or A.cVenName LIKE CONCAT('%', #para(keywords),'%'))
    #end
    #if(cVenItem)
        AND (A.cVenCode = #para(cVenItem) or A.cVenName =  #para(cVenItem))
    #end
    #if(id)
        AND A.iAutoId = #para(id)
    #end
#end

#sql("findVendorByCode")
SELECT
    A.*
FROM
    Bd_Vendor A
WHERE
    A.isEnabled = '1'
    AND A.isDeleted = '0'
    #if(vendorCode)
        AND A.cVenCode = #para(vendorCode)
    #end
#end

#sql("getCustomerByName")
SELECT * from Bd_Customer WHERE isDeleted=0 AND cCusName = #para(name)
#end