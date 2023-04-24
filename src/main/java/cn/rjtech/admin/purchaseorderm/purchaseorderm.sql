#sql("")

#end

#sql("findByInventoryId")
SELECT
	a.iAutoId,
	a.iInventoryId,
	a.iSeq,
	a.iLevel,
	a.cInvLev,
	a.isOutSourced,
	a.iQty,
	a.iWeight,
	a.cMemo,
	inv.cInvCode,
	inv.cInvName,
	inv.cInvStd,
	inv.cInvCode1,
	inv.cInvName1,
	inv.iPurchaseUomId,
	inv.iPkgQty,
	inv.cInvAddCode,
	inv.cInvAddCode1,
	pur.iYear,
	pur.iMonth,
	pur.iDay
FROM
    PS_PurchaseOrderD a
	INNER JOIN Bd_Inventory inv ON a.iInventoryId = inv.iAutoId AND inv.isDeleted = 0
	LEFT JOIN PS_PurchaseOrderD_Qty pur ON a.iAutoId = pur.iPurchaseOrderDid
WHERE a.isDeleted = '0'
#end

#sql("findAll")
SELECT
	a.*,
	putype.cPTName,
	ven.cVenName
FROM
	PS_PurchaseOrderM a
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
