#sql("findAll")
SELECT
	a.*,
	putype.cPTName,
	ven.cVenName
FROM
	PS_SubcontractOrderM a
	LEFT JOIN Bd_PurchaseType putype ON putype.iAutoId = a.iPurchaseTypeId
	LEFT JOIN Bd_Vendor ven ON ven.iAutoId = a.iVendorId
WHERE
	a.IsDeleted = 0
	#if(cOrderNo)
        AND a.cOrderNo LIKE CONCAT('%',#para(cOrderNo),'%')
	#end
    #if(cVenName)
        AND ven.cVenName LIKE CONCAT('%',#para(cVenName),'%')
	#end
	#if(iAuditStatus)
        AND a.iAuditStatus = #para(iAuditStatus)
	#end
	#if(startDate)
        AND convert(char(10),a.dBeginDate,126) <= #para(startDate)
	#end
	#if(endDate)
        AND convert(char(10),a.dEndDate,126) >= #para(endDate)
	#end

#end
